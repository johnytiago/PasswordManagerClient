package manager;

import java.io.File;
import java.io.FileInputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import ws.PasswordManagerWSClient;
					
public class ClientManager{

	private static PasswordManagerWSClient _clientAPI;
	private static String _username;
	private static String _password;
	private static KeyStore _clientKeyStore;
	
	  
  public static void main(String[] args) {
	  _clientAPI = new PasswordManagerWSClient();
  }
	
	private String getUsername() {
		return _username;
	}
	
	private String getPassword() {
		return _password;
	}

	public void init(String username, String password){
		_username = username;
		_password = password;
		doInitLoad();
	}
	
	/*DEFINED ON API*/
    public void register(){    
	    System.out.println(_clientAPI.register(getPubKey()));           
    }
    
    /*DEFINED ON API*/
	public void savePassword(String domain, String username, String password){
		_password = password;
		try{		
			System.out.println(_clientAPI.put(getPubKey(), encript(domain.getBytes()), encript(username.getBytes()), encript(password.getBytes())));
		}catch(Exception e){}
	}
	
    /*DEFINED ON API*/
	public byte[] getPassword(String domain, String username){
		try{
			//String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
			//byte[] stampCif = encript(timeStamp.getBytes());
			return _clientAPI.get(getPubKey(), encript(domain.getBytes()), encript(username.getBytes()));
			
		}catch(Exception e){
			return null;
		}
	}
	
	private byte[] getPubKey(){
		Key key = null;
		PublicKey publicKey = null;
		
		try {
			key = _clientKeyStore.getKey(getUsername(), getPassword().toCharArray());
		} catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException e) {}
		
		if (key instanceof PrivateKey) {
		      // Get certificate of public key
				Certificate cert = null;
				try {
					cert = _clientKeyStore.getCertificate(getUsername());
				} catch (KeyStoreException e) {}

		      // Get public key
		      publicKey = cert.getPublicKey();
			}
		return publicKey.getEncoded();
	}
    
    public byte[] encript(byte[] cleartext) throws Exception{
		byte[] encripted = null;
		
		Key key = _clientKeyStore.getKey(getUsername(), getPassword().toCharArray());
		
		if (key instanceof PrivateKey) {
		      // Get certificate of public key
				Certificate cert = _clientKeyStore.getCertificate(getUsername());

		      // Get public key
		      PublicKey publicKey = cert.getPublicKey();

		      // Return a key pair
		      new KeyPair(publicKey, (PrivateKey) key);
		
			// Initialize cipher object
			Cipher aesCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			aesCipher.init(Cipher.ENCRYPT_MODE, publicKey);

			// Encrypt the cleartext
			encripted = aesCipher.doFinal(cleartext);
			
			}
		return encripted;
    }
    
    public String decrypt(byte[] encryptedMessage){
		String decryptedMessage = "";
		Key privateKey = null;
		
		try {
			privateKey = _clientKeyStore.getKey(getUsername(), getPassword().toCharArray());
		} catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException e2) {}
		
		Cipher aesCipher = null;
		try {
			aesCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			aesCipher.init(Cipher.DECRYPT_MODE, privateKey);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e1) {}
		
		try {
			decryptedMessage = new String(aesCipher.doFinal(encryptedMessage));
		} catch (IllegalBlockSizeException | BadPaddingException e) {}
		return decryptedMessage;
    }
    
	private void doInitLoad(){/*TODO - Check if KGEN already exists.*/
	    KeyPairGenerator kgen = null;
		try {
			kgen = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {}
		
	    kgen.initialize(1024);
	    KeyPair pair = kgen.generateKeyPair();
	    
	    saveKeypair(pair);
	}

    private void saveKeypair(KeyPair pair){
    	try{
    		createKeystore(getUsername(), getPassword().toCharArray());
    		KeyStore keystore = _clientKeyStore; 
    		//###############################################################
			X509Certificate [] cert = GenCert.generateCertificate(pair);
	
    		//###################################################
    		
    		
		    //String ali = "mykeypair";

		    Key key = keystore.getKey(getUsername(), getPassword().toCharArray());
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
			File f = new File("./"+getUsername()+".jce");
			if(!f.exists()){
		   
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
			_clientKeyStore=ks;
			System.out.println("success");
			}
			else{
				openKeystore(getUsername(),keystorePassword);
		    System.out.println("fail");
			}
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
			_clientKeyStore=ks;
			return ks;
		
		}catch(Exception e){
			
		}	
	return null;
	}
}
