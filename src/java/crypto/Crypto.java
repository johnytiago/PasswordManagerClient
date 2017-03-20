package crypto;

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

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Crypto {

	private static String _username;
	private static String _password;
	private static KeyStore _clientKeyStore;

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

	public byte[] getPubKey(){
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

	public byte[] encrypt(byte[] cleartext) throws Exception{
		byte[] encrypted = null;

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
			encrypted = aesCipher.doFinal(cleartext);

		}
		return encrypted;
	}

	public String decrypt(byte[] encryptedMessage){
		String decryptedMessage = "";
		Key privateKey = null;

		try {
			privateKey = _clientKeyStore.getKey(getUsername(), getPassword().toCharArray());
		} catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException e2) {
			System.out.println(e2.getMessage());
			System.out.println("Error Fetching PrivKey");
		}

		Cipher aesCipher = null;
		try {
			aesCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			aesCipher.init(Cipher.DECRYPT_MODE, privateKey);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e1) {
			System.out.println(e1.getMessage());
			System.out.println("Error Iniciating Cipher");
		}

		try {
			decryptedMessage = new String(aesCipher.doFinal(encryptedMessage));
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			System.out.println(e.getMessage());
			System.out.println("Error decrypting message");
		}
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
		}catch(Exception e){
			System.err.println("Failed to save keypair");
		}
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
		}catch(Exception e){
			System.err.println("Failed to initialize KeyStore");
		}

		return _clientKeyStore;

	}

	private void openKeystore(){
		try{
			FileInputStream fis = new FileInputStream(getKeyStoreDirectory()); 
			KeyStore ks = KeyStore.getInstance("JCEKS");

			ks.load(fis, getPassword().toCharArray());
			fis.close();

			_clientKeyStore = ks;

		}catch(Exception e){
			System.err.println("Failed to open Keystore");
		}	
	}

	private String getKeyStoreDirectory(){
		return getUsername() + ".jce";
	}
}
