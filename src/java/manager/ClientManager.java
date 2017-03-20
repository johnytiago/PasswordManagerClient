package manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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

	private void doInitLoad(){
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
			KeyStore keystore = initializeKeyStore();

			X509Certificate [] cert = GenCert.generateCertificate(pair);

			Key key = keystore.getKey(getUsername(), getPassword().toCharArray());
			keystore.setCertificateEntry(getUsername(), cert[0]);

			if (key instanceof PrivateKey) {

				Certificate certificate = keystore.getCertificate(getUsername());

				// Get public key
				PublicKey publicKey = cert[0].getPublicKey();

				// Return a key pair
				new KeyPair(publicKey, (PrivateKey) key);
			}
		}catch(Exception e){}
	}	

	private KeyStore initializeKeyStore(){
		try{
			File f = new File(getKeyStoreDirectory());
			if(!f.exists()){
				KeyStore ks = KeyStore.getInstance("JCEKS");
				ks.load(null, getPassword().toCharArray()); 
				/*
				password = new KeyStore.PasswordProtection(keystorePassword); 
				 */
				FileOutputStream fos = new FileOutputStream(getKeyStoreDirectory());

				ks.store(fos, getPassword().toCharArray());

				fos.close(); 
				_clientKeyStore = ks;
			}
			else{
				openKeystore();
			}
		}catch(Exception e){}

		return _clientKeyStore;

	}

	private void openKeystore(){
		try{
			FileInputStream fis = new FileInputStream(getKeyStoreDirectory()); 
			KeyStore ks = KeyStore.getInstance("JCEKS");

			ks.load(fis, getPassword().toCharArray());
			fis.close();

			_clientKeyStore = ks;

		}catch(Exception e){}	
	}

	private String getKeyStoreDirectory(){
		return getUsername() + ".jce";
	}
}
