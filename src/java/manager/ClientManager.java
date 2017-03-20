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
		System.out.println(_clientAPI.register(_crypto.getPubKey()));           
	}

	public void savePassword(String domain, String username, String password){
    try{
			System.out.println(_clientAPI.put(_crypto.getPubKey(), _crypto.encrypt(domain.getBytes()), _crypto.encrypt(username.getBytes()), _crypto.encrypt(password.getBytes())));
		}catch(Exception e){
      System.out.println("Error saving password");
    }
	}

	public byte[] getPassword(String domain, String username){
		try{
			//String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
			//byte[] stampCif = encrypt(timeStamp.getBytes());
			return _clientAPI.get(_crypto.getPubKey(), _crypto.encrypt(domain.getBytes()), _crypto.encrypt(username.getBytes()));

		}catch(Exception e){
      System.out.println("Error getting password");
			return null;
		}
	}
}
