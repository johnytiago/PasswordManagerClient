package lib;

import crypto.Crypto;
import ws.*;
import layer.Security;
import layer.Communication;

import java.security.PrivateKey;
import java.util.Arrays;

public class PasswordManagerWSClient {

  private Crypto _crypto;
  private Security securityLayer;
  private Communication communicationLayer;

  public PasswordManagerWSClient() {
    _crypto = new Crypto();
	communicationLayer = new Communication(_crypto);
    securityLayer = new Security(_crypto);
  }

  public void init(String username, String password){
    _crypto.init(username, password);
  }

  // ##################
  // #### REGISTER ####
  // ##################
  public Boolean register() throws PasswordManagerException_Exception, PubKeyAlreadyExistsException_Exception {
	  
	  Envelope envelope = new Envelope();
	  Message msg = new Message();
	  envelope.setMessage(msg);

	  return communicationLayer.register(envelope);
  }

  // ##################
  // ###### GET #######
  // ##################
  public String get(String domain, String username) throws PasswordManagerException_Exception {

	// Domain specific security
    byte[] salt = _crypto.getSalt();
    byte[] domainHash = _crypto.genSign(securityLayer.addSalt(domain.getBytes(),salt), (PrivateKey)_crypto.getPrivateKey());
    byte[] usernameHash = _crypto.genSign(securityLayer.addSalt(username.getBytes(),salt), (PrivateKey)_crypto.getPrivateKey());

    Envelope envelope = new Envelope();
    Message msg = new Message();
    msg.setDomainHash(domainHash);
    msg.setUsernameHash(usernameHash);
    envelope.setMessage(msg);

	Envelope rEnvelope = communicationLayer.get(envelope);
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

	// Domain specific security
    byte[] salt = _crypto.getSalt();
    byte[] domainHash = _crypto.genSign(securityLayer.addSalt(domain.getBytes(),salt), (PrivateKey)_crypto.getPrivateKey());
    byte[] usernameHash = _crypto.genSign(securityLayer.addSalt(username.getBytes(),salt), (PrivateKey)_crypto.getPrivateKey());
    byte[] encryptedPassword = _crypto.encrypt(password.getBytes(), _crypto.getPublicKey());
    byte[] tripletHash = _crypto.signTriplet(domainHash, usernameHash, encryptedPassword, (PrivateKey)_crypto.getPrivateKey());

    Envelope envelope = new Envelope();
    Message msg = new Message();
    msg.setDomainHash(domainHash);
    msg.setUsernameHash(usernameHash);
    msg.setTripletHash(tripletHash);
    msg.setPassword(encryptedPassword);
    envelope.setMessage(msg);

    return communicationLayer.put(envelope);
  }
}
