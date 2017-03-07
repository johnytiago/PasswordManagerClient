
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

public class ClientManager{
    public void register_user(){
        try{
        KeyPairGenerator kgen = KeyPairGenerator.getInstance("RSA");
        kgen.initialize(1024);
        KeyPair pair = kgen.generateKeyPair();
        PrivateKey priv = pair.getPrivate();
        savePrivateKey(priv);
        PublicKey pub = pair.getPublic();
        savePublicKey(pub);
        }
        catch(Exception e){}  

       
               
    }
    public void savePublicKey(PublicKey pub){
		try{
		byte[] pubEnc = pub.getEncoded();
		FileOutputStream fos = new FileOutputStream("." + "/public.key");
		fos.write(pubEnc);
		fos.close();
		}
		catch(Exception e){}
    }
    public void savePrivateKey(PrivateKey priv){
    	try{
		byte[] privEnc = priv.getEncoded();
		FileOutputStream fos = new FileOutputStream("." + "/private.key");
		fos.write(privEnc);
		fos.close();
		}
		catch(Exception e){}
    }

    public Key getPublic(){
    	Key pubKey = null;
    	try{
        
    	File f = new File("public.key");
	    FileInputStream fis = new FileInputStream(f);
	    DataInputStream dis = new DataInputStream(fis);
	    byte[] keyBytes = new byte[(int)f.length()];
	    dis.readFully(keyBytes);
	    dis.close();

	    X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
	    //there can be a bug because When I'm saving the key I don't put it in this mode

	    KeyFactory kf = KeyFactory.getInstance("RSA");
	    pubKey = kf.generatePublic(spec);
	    return pubKey;
		}
		catch(Exception e){

		}
		return pubKey;
    
	}


}