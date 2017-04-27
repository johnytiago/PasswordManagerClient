package ws;

import java.io.File;
import java.nio.file.Files;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

import javax.crypto.SecretKey;

import crypto.Crypto;
import util.Util;

public class PasswordManagerWSClient {

  private static final String PATH_TO_SERVER_DHPUBKEY = "../PasswordManager/keys/server.pubKey";

  private static PasswordManagerWS _passwordmanagerWS;
  private Crypto _crypto;
  private Util _util = new Util();
  private PublicKey DHPubKeySrv;
  private SecretKey DHSecretKey;

  public PasswordManagerWSClient() {
    connect();
    _crypto = new Crypto();
  }

  public void init(String username, String password){
    _crypto.init(username, password);
    getDHPubKeySrv();
  }

  private void getDHPubKeySrv(){
    // First time connecting to the server
    // Go read DH public key from the server directory
    try {
      byte[] pubKeyBytes = Files.readAllBytes(new File( PATH_TO_SERVER_DHPUBKEY ).toPath());
      DHPubKeySrv = _crypto.retrieveDHPubKey( pubKeyBytes );
    } catch (Exception e){
      e.printStackTrace();
      System.out.println("Error reading server DH pubkey file");
    }
  }

  private void connect() {
    PasswordManagerWSImplService pmWSImplService = new PasswordManagerWSImplService();
    _passwordmanagerWS = pmWSImplService.getPasswordManagerWSImplPort();

    System.out.println("Connected to PasswordManagerServer.");
    System.out.println("Found service running at: " + pmWSImplService.getWSDLDocumentLocation().toString());
  }

  // ##################
  // #### REGISTER ####
  // ##################
  public Boolean register() throws PasswordManagerException_Exception {

    Envelope envelope = new Envelope();
    Message msg = new Message();

    msg.setPublicKey( _crypto.getPublicKey().getEncoded() ); 
    envelope.setMessage(msg);

    // Add crypto primitives
    prepareEnvelope( envelope );
    
    try{
    	_passwordmanagerWS.register(envelope);
    }catch(PasswordManagerException_Exception pme){
    	return false;
    }
    return true;
  }

  // ##################
  // ###### GET #######
  // ##################
  public String get(String domain, String username) throws PasswordManagerException_Exception {

    byte[] pubKey  = _crypto.getPublicKey().getEncoded();
    byte[] salt = _crypto.getSalt();
    byte[] domainHash = _crypto.genSign(_util.addSalt(domain.getBytes(),salt), (PrivateKey)_crypto.getPrivateKey());
    byte[] usernameHash = _crypto.genSign(_util.addSalt(username.getBytes(),salt), (PrivateKey)_crypto.getPrivateKey());

    Message msg = new Message();
    msg.setPublicKey(pubKey);
    msg.setDomainHash(domainHash);
    msg.setUsernameHash(usernameHash);

    Envelope envelope = new Envelope();
    envelope.setMessage(msg);

    // Add crypto primitives
    prepareEnvelope( envelope );

    Envelope rEnvelope = null;
    try{
    	rEnvelope = _passwordmanagerWS.get(envelope);
    }catch(PasswordManagerException_Exception pme){
    	return null;
    }
    Message rMsg = rEnvelope.getMessage();

    // Verify TripletHash matches
    byte[] tripletHash = _crypto.signTriplet(domainHash, usernameHash, rMsg.getPassword(), (PrivateKey)_crypto.getPrivateKey());
    if (!Arrays.equals(tripletHash, rMsg.getTripletHash())){
      System.out.println("Integrity of the triplet not verified");
      // TODO: let client know something bad happend
      return null;
    }
    System.out.println("Security verifications passed.");

    byte[] pw = _crypto.decrypt(msg.getPassword(), _crypto.getPrivateKey());
    return new String(pw);
  }

  // ##################
  // ###### PUT #######
  // ##################
  public Boolean put(String domain, String username, String password) throws PasswordManagerException_Exception {

    byte[] pubKey  = _crypto.getPublicKey().getEncoded();
    byte[] salt = _crypto.getSalt();
    byte[] domainHash = _crypto.genSign(_util.addSalt(domain.getBytes(),salt), (PrivateKey)_crypto.getPrivateKey());
    byte[] usernameHash = _crypto.genSign(_util.addSalt(username.getBytes(),salt), (PrivateKey)_crypto.getPrivateKey());
    byte[] encryptedPassword = _crypto.encrypt(password.getBytes(), _crypto.getPublicKey());
    byte[] tripletHash = _crypto.signTriplet(domainHash, usernameHash, encryptedPassword, (PrivateKey)_crypto.getPrivateKey());

    Message msg = new Message();
    msg.setPublicKey( pubKey);
    msg.setDomainHash(domainHash);
    msg.setUsernameHash(usernameHash);
    msg.setTripletHash(tripletHash);
    msg.setPassword(encryptedPassword);

    Envelope envelope = new Envelope();
    envelope.setMessage(msg);

    // Add Crypto primitives
    prepareEnvelope( envelope );

    Envelope rEnvelope = null;
    try{
    	_passwordmanagerWS.put(envelope);
    }catch(PasswordManagerException_Exception pme){
    	return false;
    }

    return true;
  }

  private boolean verifyHMAC( Envelope envelope ) {
    //PublicKey DHPubKeySrv = _crypto.retrieveDHPubKey(envelope.getDHPublicKey());
    //SecretKey DHSecretKey = _crypto.generateDH( _crypto.getDHPrivateKey(), DHPubKeySrv );
    byte[] HMAC = _crypto.genMac( _util.msgToByteArray( envelope.getMessage() ), DHSecretKey );
    return Arrays.equals(HMAC, envelope.getHMAC());
  }

  private void addHMAC( Envelope envelope ) {
    DHSecretKey = _crypto.generateDH( _crypto.getDHPrivateKey(), DHPubKeySrv);
    byte[] HMAC = _crypto.genMac( _util.msgToByteArray( envelope.getMessage() ), DHSecretKey );
    envelope.setHMAC( HMAC );
    return;
  }

  private void prepareEnvelope( Envelope envelope ) {
    envelope.setDHPublicKey( _crypto.getDHPublicKey().getEncoded() );
    addHMAC( envelope );
    // TODO: counter
    return;
  }

  private boolean verifyEnvelope( Envelope envelope ) {
    // TODO: Counter
    return verifyHMAC( envelope ); // && verifyCounter( envelope );
  }
}
