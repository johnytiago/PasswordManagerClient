package layer;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

import crypto.Crypto;
import exception.FailedToGetQuorumException;
import exception.SecurityVerificationException;
import ws.*;
import util.PublicKeyStore;

public class Communication {

	private static final String PATH_TO_SERVERS_DHPUBKEYS = "../PasswordManager/keys/";
	private static ws.PasswordManagerWS _passwordmanagerWS;
	private Security securityLayer;
	private PublicKeyStore dhPubKeyStore = new PublicKeyStore();
	private Class<?>[] envelopeClass = new Class[]{Envelope.class};
	private HashMap<String, PasswordManagerWS> replicas= new HashMap<String, PasswordManagerWS>();
	private String PORT = System.getenv("PORT");
	private int NUM_REPLICAS = Integer.valueOf(System.getenv("NUM_REPLICAS"));
	private int NUM_FAULTS = Integer.valueOf(System.getenv("NUM_FAULTS"));
	private int wts = 0;

	public Communication (Crypto crypto){
	    securityLayer = new Security(crypto);
	    getDHPubKeysSrvs();
		connect();
	}
	
	private void getDHPubKeysSrvs(){
		// Go read DH public keys from the server directory
		try {
			  File dir = new File(PATH_TO_SERVERS_DHPUBKEYS);
			  File[] directoryListing = dir.listFiles();
			  
			  for (File file : directoryListing) {
				  if (file.toString().endsWith(".pubKey")){
					  try {
						  byte[] pubKeyBytes = Files.readAllBytes(file.toPath());

						  // get only the filename in a path e.g( file in /this/is/a/path/to/file.pubKey)
						  String fileName = file.getName();
						  fileName = fileName.substring(0, fileName.lastIndexOf("."));
						  
						  // Store for future uses in the pubkey store
						  dhPubKeyStore.put( fileName, pubKeyBytes);
						   
					  } catch (Exception e) {
						  e.printStackTrace();
						  System.out.println("Error reading server DH pubkey file");
					  }			    
				  }
			  }
		} catch (Exception e){
			e.printStackTrace();
			System.out.println("Error reading server DH pubkey file");
		}
	}
	  
	public void connect() {
		int port = Integer.valueOf(PORT);
		int lastPort = port + NUM_REPLICAS - 1;
		
		for( ; port <= lastPort ; port++ ) {
			PasswordManagerWSImplService pmWSImplService = null;
			try {
				URL url = new URL("http://localhost:"+port+"/WS/PasswordManager?wsdl");
				pmWSImplService = new PasswordManagerWSImplService(url);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			replicas.put("server"+port, pmWSImplService.getPasswordManagerWSImplPort());
			// Store just to getClass later on. No real use;
			_passwordmanagerWS = pmWSImplService.getPasswordManagerWSImplPort();
			System.out.println("Connected to replica: " + pmWSImplService.getWSDLDocumentLocation().toString());
		}
	}
	
	// ## REGISTER ##
	public Boolean register(Envelope envelope) throws PasswordManagerException_Exception, PubKeyAlreadyExistsException_Exception {
	    try {
			Method register = _passwordmanagerWS.getClass().getMethod("register", envelopeClass);
			broadcast(register, envelope);
			return true;
			//TODO handle security exception
		} catch( NoSuchMethodException e){
			System.out.println("[Communication] Error calling put method");
			return false;
		} catch( Exception e ){
			e.printStackTrace();
			System.out.println("[Communication] Register method failed");
			return false;
		}
	}
	
	// ## GET ##
	public Envelope get(Envelope envelope) throws PasswordManagerException_Exception {
		try {
			Method get = _passwordmanagerWS.getClass().getMethod("get", envelopeClass);
			return broadcast(get, envelope);
		} catch( NoSuchMethodException e){
			System.out.println("[Communication] Error calling put method");
			return null;
		} catch( Exception e ){
			System.out.println(e.getMessage());
			System.out.println("[Communication] Get method failed");
			return null;
		}
	}
	
	// ## PUT ##
	public Boolean put(Envelope envelope) throws PasswordManagerException_Exception {
		try {
			Method put = _passwordmanagerWS.getClass().getMethod("put", envelopeClass);
			wts = wts + 1;
			securityLayer.signMessage(envelope);
			broadcast(put, envelope);
			return true;
		} catch( NoSuchMethodException e){
			System.out.println("[Communication] Error calling put method");
			return false;
		} catch( Exception e ){
			System.out.println(e.getMessage());
			System.out.println("[Communication] Put method failed");
			return false;
		}
	}

	public Envelope send( Method method, Envelope envelope, PasswordManagerWS server, String serverName ) 
			throws SecurityVerificationException {		
		try {

			byte[] pubkey = dhPubKeyStore.get(serverName);
			securityLayer.prepareEnvelope( envelope, pubkey);
			
			Envelope rEnvelope = (Envelope) method.invoke(server, envelope);

			if( !securityLayer.verifyEnvelope( rEnvelope )) {
				System.out.println("Security verifications failed for " + serverName);
				throw new SecurityVerificationException();
			}

			System.out.println("Security verifications passed.");
			return rEnvelope;

		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Envelope broadcast(Method method, Envelope envelope) throws FailedToGetQuorumException {
		ArrayList<Envelope> readList = new ArrayList<Envelope>();
		int quorum = (NUM_REPLICAS+NUM_FAULTS)/2;
		Envelope rEnvelope = null;
		
		for (Entry<String, PasswordManagerWS> pmWS : replicas.entrySet()) {
			String serverName = pmWS.getKey();
			PasswordManagerWS server = pmWS.getValue();
			try {
				rEnvelope = send(method, envelope, server, serverName);
				
				// verify integrity if only it's a read
				if (method.getName().equals("get"))
					if( !securityLayer.verifySignature(rEnvelope) )
						continue;
				
				readList.add(rEnvelope);
				if( readList.size() > quorum)
					break;
			} catch(Exception e){
				// verifications failed. not valid
				continue;
			}
		}
		
		// broadcast ended without quorum
		if( !(readList.size() > quorum) )
			throw new FailedToGetQuorumException();

		// only sort for read method
		if (method.getName().equals("get")){
			Collections.sort(readList, new Comparator<Envelope>() {
				public int compare(Envelope e1, Envelope e2) {
					return e1.getMessage().getWts() - e2.getMessage().getWts();
				}
			});
			
			// pick the most recent one
			rEnvelope = readList.get(0);
		}
		
		return rEnvelope;
	}
}
