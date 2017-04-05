package manager;

import crypto.Crypto;
import java.security.PrivateKey;
import ws.PasswordManagerWSClient;

public class ClientManager{

  private static PasswordManagerWSClient _clientAPI;
  private Crypto _crypto;

  public static void main(String[] args) {
    _clientAPI = new PasswordManagerWSClient();
  }

  public void init(String username, String password){
    _crypto = new Crypto();
    _crypto.init(username, password);
  }

  public void register(){    
    System.out.println(_clientAPI.register(_crypto.getPublicKey().getEncoded()));           
  }

  public void savePassword(String domain, String username, String password){
    try{

      byte[] pubKey  = _crypto.getPublicKey().getEncoded();
      byte[] domainHash = _crypto.genSign(domain.getBytes(), (PrivateKey)_crypto.getPrivateKey());
      byte[] usernameHash = _crypto.genSign(username.getBytes(), (PrivateKey)_crypto.getPrivateKey());
      byte[] encryptedPassword = _crypto.encrypt(password.getBytes(), _crypto.getPublicKey());
      byte[] tripletHash = _crypto.signTriplet( domainHash, usernameHash, encryptedPassword, (PrivateKey)_crypto.getPrivateKey());

      String res = _clientAPI.put( pubKey, domainHash, usernameHash, encryptedPassword, tripletHash);
      System.out.println( res );

    } catch(Exception e){
      System.out.println("Error saving password");
    }
  }

  public byte[] getPassword(String domain, String username){
    try{
      byte[] pubKey  = _crypto.getPublicKey().getEncoded();
      byte[] domainHash = _crypto.genSign(domain.getBytes(), (PrivateKey)_crypto.getPrivateKey());
      byte[] usernameHash = _crypto.genSign(username.getBytes(), (PrivateKey)_crypto.getPrivateKey());

      // TODO : Expect the password and triplet and confirm is the same hash
      return _clientAPI.get( pubKey, domainHash, usernameHash );

    }catch(Exception e){
      System.out.println("Error getting password");
      return null;
    }
  }
}
