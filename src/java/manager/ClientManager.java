package manager;

import crypto.Crypto;
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
      byte[] domainHash = _crypto.genSign(domain.getBytes(), _crypto.getPrivateKey());
      byte[] usernameHash = _crypto.genSign(username.getBytes(), _crypto.getPrivateKey());
      byte[] encryptedPassword = _crypto.encrypt(password.getBytes(), _crypto.getPublicKey());
      byte[] tripletHash = _crypto.signTriplet( domainHash, usernameHash, encryptedPassword );

			System.out.println(
        _clientAPI.put( pubKey, domainHash, usernameHash, encryptedPassword, tripletHash)
      );

		} catch(Exception e){
			System.out.println("Error saving password");
		}
	}

	public byte[] getPassword(String domain, String username){
		try{
			//String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
			//byte[] stampCif = encrypt(timeStamp.getBytes());
			return _clientAPI.get(_crypto.getPublicKey().getEncoded(),
					_crypto.encrypt(domain.getBytes(), _crypto.getPublicKey()),
					_crypto.encrypt(username.getBytes(), _crypto.getPublicKey()));

		}catch(Exception e){
			System.out.println("Error getting password");
			return null;
		}
	}
}
