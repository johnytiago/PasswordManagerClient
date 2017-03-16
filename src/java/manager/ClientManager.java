package manager;

import java.io.FileInputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import ws.PasswordManagerWS;
					
public class ClientManager{

	private PasswordManagerWS _passwordManagerWS;
	private String _username;
	
	public ClientManager(PasswordManagerWS WS){
		_passwordManagerWS = WS;
	}
	
	public String getUsername() {
		return _username;
	}
	
	
	public void init(String username, String password){
		_username = username;
		createKeystore(getUsername(), password.toCharArray());
	}

    public void register_user(String password){
    	
        try{
	        KeyPairGenerator kgen = KeyPairGenerator.getInstance("RSA");
	        kgen.initialize(1024);
	        KeyPair pair = kgen.generateKeyPair();
	        
	        saveKeypair(pair, password.toCharArray());
	        PublicKey pub = pair.getPublic();
	        sendPubKey(pub);

        }
        catch(Exception e){
        	
        }    
    }
    //This method will be used to send the Server the client PubKey
    public void sendPubKey(PublicKey pub){
    	
    	
    	
    }
	public void save_password(byte[] domain, byte[] username, byte[] password){
		
	}
	
	public String retrieve_password(byte[] domain, byte[] username) { 
		return "";
	}


    private void saveKeypair(KeyPair pair, char[] password){
    	
    	try{
    		KeyStore keystore = openKeystore(getUsername(), password);
    		//###############################################################
			X509Certificate [] cert = GenCert.generateCertificate(pair);
	
    		//###################################################
    		
    		
		    //String ali = "mykeypair";

		    Key key = keystore.getKey(getUsername(), password);
			keystore.setCertificateEntry(getUsername(), cert[0]);

    	 if ( key instanceof PrivateKey) {

		      // Get certificate of public key
		      Certificate certificate = keystore.getCertificate(getUsername());

		      // Get public key
		      PublicKey publicKey = cert[0].getPublicKey();

		      // Return a key pair
		      new KeyPair(publicKey, (PrivateKey) key);
    	}
    	}catch(Exception e){
    		
    	}
    }	

	private void createKeystore(String keystoreName, char[] keystorePassword){
		try{
			// Create an instance of KeyStore of type “JCEKS”.
			 // JCEKS refers the KeyStore implementation from SunJCE provider
			KeyStore ks = KeyStore.getInstance("JCEKS");
			 // Load the null Keystore and set the password to keyStorepassword
			 ks.load(null, keystorePassword); 
			 /*
			password = new KeyStore.PasswordProtection(keystorePassword); 
			*/
			//Create a new file to store the KeyStore object
			java.io.FileOutputStream fos = new java.io.FileOutputStream(keystoreName + ".jce");
	
			//Write the KeyStore into the file
			ks.store(fos, keystorePassword);
			//Close the file stream
			fos.close(); 

		}catch(Exception e){
			
		}
	}

	private KeyStore openKeystore(String keystoreName, char [] keystorePassword){
		try{
			//Open the KeyStore file
			FileInputStream fis = new FileInputStream(keystoreName + ".jce"); 
			//Create an instance of KeyStore of type “JCEKS”
			KeyStore ks = KeyStore.getInstance("JCEKS");
			//Load the key entries from the file into the KeyStore object.
			ks.load(fis, keystorePassword);
			fis.close();
			//Get the key with the given alias.
			
			//String alias=args[0];
			//Key k = ks.getKey(alias, "changeme".toCharArray()); 
			return ks;
		
		}catch(Exception e){
			
		}	
	return null;
	}
}
