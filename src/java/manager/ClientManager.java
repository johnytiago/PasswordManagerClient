package manager;

import java.security.PrivateKey;
import java.util.Arrays;
import crypto.Crypto;
import ws.PasswordManagerWSClient;
import ws.Envelope;
import ws.Message;
import util.Util;


public class ClientManager{

  private static PasswordManagerWSClient _clientAPI;
  private Crypto _crypto;
  private Util _util = new Util();

  public static void main(String[] args) {
    _clientAPI = new PasswordManagerWSClient();
  }

  public void init(String username, String password){
    _crypto = new Crypto();
    _crypto.init(username, password);
  }

  public void register(){    
    try {
      Envelope envelope = _clientAPI.register(_crypto.getPublicKey().getEncoded());
      Message msg = envelope.getMessage();
      
      // 1st Verify HMAC
      byte[] HMAC = _crypto.genMac(
          _util.msgToByteArray( msg ), 
          _crypto.getSecretKey());
      if (!Arrays.equals(HMAC, envelope.getHmac())){
        System.out.println("Integrity of the message not verified");
      }
      // 2nd Verify Counter
      // TODO: this
      
      System.out.println("Success! Registered");

    } catch(Exception e){
      System.out.println("Error registring");
      // TODO: Handle exceptions from server
    }
  }

  public void savePassword(String domain, String username, String password){
    try{
      byte[] pubKey  = _crypto.getPublicKey().getEncoded();
      byte[] domainHash = _crypto.genSign(domain.getBytes(), (PrivateKey)_crypto.getPrivateKey());
      byte[] usernameHash = _crypto.genSign(username.getBytes(), (PrivateKey)_crypto.getPrivateKey());
      byte[] encryptedPassword = _crypto.encrypt(password.getBytes(), _crypto.getPublicKey());
      byte[] tripletHash = _crypto.signTriplet(domainHash, usernameHash, encryptedPassword, (PrivateKey)_crypto.getPrivateKey());

      Envelope envelope = _clientAPI.put(pubKey, domainHash, usernameHash, encryptedPassword, tripletHash);
      Message msg = envelope.getMessage();

      // 1st Verify HMAC
      byte[] HMAC = _crypto.genMac(
          _util.msgToByteArray( msg ), 
          _crypto.getSecretKey());
      if (!Arrays.equals(HMAC, envelope.getHmac())){
        System.out.println("Integrity of the message not verified");
      }
      // 2nd Verify Counter
      // TODO: this
      
      System.out.println("Success! Password saved");

    } catch(Exception e){
      System.out.println("Error saving password");
      // TODO: Handle exceptions from server
    }
  }

  public byte[] getPassword(String domain, String username){
    try{
      byte[] pubKey  = _crypto.getPublicKey().getEncoded();
      byte[] domainHash = _crypto.genSign(domain.getBytes(), (PrivateKey)_crypto.getPrivateKey());
      byte[] usernameHash = _crypto.genSign(username.getBytes(), (PrivateKey)_crypto.getPrivateKey());

      Envelope envelope = _clientAPI.get(pubKey, domainHash, usernameHash);
      Message msg = envelope.getMessage();

      // 1st Verify HMAC
      byte[] HMAC = _crypto.genMac(
          _util.msgToByteArray( msg ), 
          _crypto.getSecretKey());
      if (!Arrays.equals(HMAC, envelope.getHmac())){
        System.out.println("Integrity of the message not verified");
      }
      // 2nd Verify Counter
      // TODO: this
      // 3rd Verify tripletHash
      byte[] tripletHash = _crypto.signTriplet(domainHash, usernameHash, msg.getPassword(), (PrivateKey)_crypto.getPrivateKey());
      if (!Arrays.equals(tripletHash, msg.getTripletHash())){
        System.out.println("Integrity of the triplet not verified");
      }

      byte[] pw = _crypto.decrypt(msg.getPassword(), _crypto.getPrivateKey());
      System.out.println("Success!" + new String(pw));
      return pw;

    }catch(Exception e){
      System.out.println("Error getting password");
      // TODO: Handle exceptions from server
      return null;
    }
  }
}
