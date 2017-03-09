
package ws;


import java.security.Key;
import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.security.PrivateKey;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.io.*;
import java.util.*;
import java.security.cert.X509Certificate;

import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

public class ClientManager{

	//
	

	public void init(KeyStore ks){}


    public void register_user(){
    	String password = "password";
    	char[] pass = password.toCharArray();
    	
        try{
        KeyPairGenerator kgen = KeyPairGenerator.getInstance("RSA");
        kgen.initialize(1024);
        KeyPair pair = kgen.generateKeyPair();
        
        saveKeypair(pair,pass);


        }
        catch(Exception e){}  
          
    }


    public void saveKeypair(KeyPair pair, char [] password){
    	
    	try{
    		KeyStore keystore = openKeystore(password);
    		//###############################################################
			X509Certificate [] cert = GenCert.generateCertificate(pair);
	

    		//###################################################
    		//PrivateKey priv = pair.getPrivate();
    		


		    String ali = "mykeypair";

		    Key key = keystore.getKey(ali, password);
			keystore.setCertificateEntry(ali, cert[0]);

    	 if ( key instanceof PrivateKey) {

		      // Get certificate of public key
		      Certificate certificate = keystore.getCertificate(ali);

		      // Get public key
		      PublicKey publicKey = cert[0].getPublicKey();

		      // Return a key pair
		      new KeyPair(publicKey, (PrivateKey) key);
    	}
    	}catch(Exception e){}

    }

 	

	public void createKeystore(char [] keystorePassword){
		try{
		// Create an instance of KeyStore of type “JCEKS”.
		 // JCEKS refers the KeyStore implementation from SunJCE provider
		KeyStore ks = KeyStore.getInstance("JCEKS");
		 // Load the null Keystore and set the password to keyStorepassword
		 ks.load(null, keystorePassword); 
		 /*
		 //add password protectio to keystore
		KeyStore.PasswordProtection password;
		password = new KeyStore.PasswordProtection(keystorePassword); 
		*/
		//Create a new file to store the KeyStore object
		java.io.FileOutputStream fos = new java.io.FileOutputStream("keystorefile.jce");

		//Write the KeyStore into the file
		ks.store(fos, keystorePassword);
		//Close the file stream
		fos.close(); 

	}catch(Exception e){}
	}

	public KeyStore openKeystore(char [] keystorePassword){
		try{
		//Open the KeyStore file
		FileInputStream fis = new FileInputStream("keystorefile.jce"); 
		//Create an instance of KeyStore of type “JCEKS”
		KeyStore ks = KeyStore.getInstance("JCEKS");
		//Load the key entries from the file into the KeyStore object.
		ks.load(fis, keystorePassword);
		fis.close();
		//Get the key with the given alias.
		
		//String alias=args[0];
		//Key k = ks.getKey(alias, "changeme".toCharArray()); 
		return ks;
		
		}catch(Exception e){}	
		return null;
		}




	public void save_password(byte[] domain, byte[] username, byte[] password){}
	public byte[] retrieve_password(byte[] domain, byte[] username) { 
		byte [] password=null;
		return password;
	}


}


