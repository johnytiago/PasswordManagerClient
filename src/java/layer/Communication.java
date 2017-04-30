package layer;

import java.io.File;
import java.nio.file.Files;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import crypto.Crypto;
import ws.*;
import util.PublicKeyStore;

public class Communication {

	private static final String PATH_TO_SERVERS_DHPUBKEYS = "../PasswordManager/keys/";
	private static ws.PasswordManagerWS _passwordmanagerWS;
	private Security securityLayer;
	private PublicKeyStore dhPubKeyStore = new PublicKeyStore();
	private Class<?>[] envelopeClass = new Class[]{Envelope.class};


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
		PasswordManagerWSImplService pmWSImplService = new PasswordManagerWSImplService();
		_passwordmanagerWS = pmWSImplService.getPasswordManagerWSImplPort();
		
		//System.out.println("DEBUGG" + pmWSImplService.getWSDLDocumentLocation().toString());
		
		//for( Iterator<QName> qn = pmWSImplService.getPorts(); qn.hasNext(); ){
		//	QName q = qn.next();
		//	System.out.println(q.getNamespaceURI());
		//}
		
		System.out.println("Connected to PasswordManagerServer.");
		System.out.println("Found service running at: " + pmWSImplService.getWSDLDocumentLocation().toString());
	}
	
	// ## REGISTER ##
	public Boolean register(Envelope envelope) throws PasswordManagerException_Exception, PubKeyAlreadyExistsException_Exception {
	    try {
			Method register = _passwordmanagerWS.getClass().getMethod("register", envelopeClass);
			send(register, envelope);
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
			return send(get, envelope);
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
			send(put, envelope);
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

	public Envelope send( Method method, Envelope envelope ) {
	  
		// Foreach _passwordmanagerWS.
	    // TODO: get name of server to talk to
		// TODO: round robin servers and talk to each on of them.
	    // TODO: use pubKey structure to fetch pubkey and pass to prepare
		
		// Server is dummy
		byte[] pubkey = dhPubKeyStore.get("server");
	    securityLayer.prepareEnvelope( envelope, pubkey);
	    
	    try {
	    	Envelope rEnvelope = (Envelope) method.invoke(_passwordmanagerWS, envelope);

	    	// Do crypto evaluations
	    	if( !securityLayer.verifyEnvelope( rEnvelope )) {
	    		System.out.println("Security verifications failed... Aborting");
	    		// TODO: let calling method know if it was a security fail (Exception)
	    		return null;
	    	}

	    	System.out.println("Security verifications passed.");
	    	return rEnvelope;

	    } catch( IllegalAccessException e){
	    	e.printStackTrace();
	    	return null;
	    } catch (IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			return null;
		} 
	}
}
