package ws;

import java.util.Arrays;
import java.util.HashMap;
import java.security.PrivateKey;
import javax.crypto.SecretKey;
import java.security.PublicKey;
import java.io.File;
import java.nio.file.Files;

import crypto.Crypto;
import util.Util;
import ws.Envelope;
import ws.Message;

public class PasswordManagerWSClient {

  private static final String PATH_TO_SERVER_DHPUBKEY = "../PasswordManager/keys/server.pubKey";

  private static PasswordManagerWS _passwordmanagerWS;
  private Crypto _crypto;
  private Util _util = new Util();
  private PublicKey DHPubKeySrv;
  private SecretKey DHSecretKey;
  private HashMap<String,Integer > counters = new HashMap<String, Integer>();

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
  public Boolean register() throws PasswordManagerException_Exception, PubKeyAlreadyExistsException_Exception {

    Envelope envelope = new Envelope();
    Message msg = new Message();

    msg.setPublicKey( _crypto.getPublicKey().getEncoded() ); 
    envelope.setMessage(msg);

    // Add crypto primitives
    prepareEnvelope( envelope );

    Envelope rEnvelope = _passwordmanagerWS.register(envelope);

    // Do crypto evaluations
    if( !verifyEnvelope( rEnvelope )) {
      System.out.println("Security verifications failed... Aborting");
      // TODO: let client know something bad happend
      return false;
    }
    System.out.println("Security verifications passed.");
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
    int counter;
    if(checkCounter(_util.publickeyToString(pubKey),counter)){
    	counter = getCounter(_util.publickeyToString(pubKey));
    }
    else{
    	//TODO: send some kind of exception
    }
    Message msg = new Message();
    msg.setPublicKey(pubKey);
    msg.setDomainHash(domainHash);
    msg.setUsernameHash(usernameHash);
    msg.setCounter(counter); //why do the prepare envelope and also set the counter here=? weird

    Envelope envelope = new Envelope();
    envelope.setMessage(msg);

    // Add crypto primitives
    prepareEnvelope( envelope );

    Envelope rEnvelope = _passwordmanagerWS.get(envelope);
    Message rMsg = rEnvelope.getMessage();

    // Do crypto evaluations
    if( !verifyEnvelope( rEnvelope )) {
      System.out.println("Security verifications failed... Aborting");
      // TODO: let client know something bad happend
      return null;
    }

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

    Envelope rEnvelope = _passwordmanagerWS.put(envelope);

    // Do crypto evaluations
    if( !verifyEnvelope( rEnvelope )) {
      System.out.println("Security verifications failed... Aborting");
      // TODO: let client know something bad happend
      return false;
    }
    System.out.println("Security verifications passed.");
    return true;
  }

  private boolean verifyHMAC( Envelope envelope ) {
    //PublicKey DHPubKeySrv = _crypto.retrieveDHPubKey(envelope.getDHPublicKey());
    //SecretKey DHSecretKey = _crypto.generateDH( _crypto.getDHPrivateKey(), DHPubKeySrv );
    byte[] HMAC = _crypto.genMac( _util.msgToByteArray( envelope.getMessage() ), DHSecretKey );
    return Arrays.equals(HMAC, envelope.getHMAC());
  }
  private boolean verifyCounter(Envelope envelope){
	  int counter = envelope.getMessage().getCounter();
	  byte [] pub = envelope.getMessage().getPublicKey(); //Not sure if it is DHkey that we want to check here
	  return checkCounter(_util.publickeyToString(pub),counter);
  }

  private void addHMAC( Envelope envelope ) {
    DHSecretKey = _crypto.generateDH( _crypto.getDHPrivateKey(), DHPubKeySrv);
    byte[] HMAC = _crypto.genMac( _util.msgToByteArray( envelope.getMessage() ), DHSecretKey );
    envelope.setHMAC( HMAC );
    return;
  }
  private void addCounter(String pub, Envelope envelope){
	  envelope.getMessage().setCounter(getCounter(pub));
  }

  private void prepareEnvelope( Envelope envelope ) {
    envelope.setDHPublicKey( _crypto.getDHPublicKey().getEncoded() );
    addHMAC( envelope );
    addCounter(_util.publickeyToString(_crypto.getPublicKey().getEncoded()),envelope);//not sure if this is the pubkey to use
    return;
  }

  private boolean verifyEnvelope( Envelope envelope ) {
    
    return verifyHMAC( envelope )&&verifyCounter(envelope); // && verifyCounter( envelope );
  }
  
  
	public boolean checkCounter(String pub,int clientCounter){
		if(counters.containsKey(pub)){
			if(counters.get(pub)<clientCounter){
			counters.put(pub,clientCounter+1);// update counter value after checking it
			return true;
			}else{
			//throw new CounterIncorrectException();
				return false;
			}
		}

			initCounter(pub);
			return true;
		
	}
	
	public int incrementCounter(int counter){
		return counter+1;
	}
	
	public int initCounter(String pub){
		if(!counters.containsKey(pub)){
			int  value = (int )(Math.random() * 10000);
			counters.put(pub, value);
			return value;
		}
		else{
			return counters.get(pub);
			//throw exception?
			
		}
		}
	public int getCounter(String pub){
		return counters.get(pub);
	}
	
}
