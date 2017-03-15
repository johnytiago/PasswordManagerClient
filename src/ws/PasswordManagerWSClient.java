package ws;

import ws.PasswordManagerWS;
import ws.PasswordManagerWSImplService;

public class PasswordManagerWSClient {

  private static PasswordManagerWS _passwordmanagerWS;
  
  public static void main(String[] args) {

	PasswordManagerWSImplService pmWSImplService = new PasswordManagerWSImplService();
	_passwordmanagerWS = pmWSImplService.getPasswordManagerWSImplPort();
	System.out.println("Connected to PasswordManagerServer.");
	System.out.println("Found service running at: " + pmWSImplService.getWSDLDocumentLocation().toString());
  }

  public void get(byte[] pubKey, byte[] domain, byte[] username) {
	  String res = _passwordmanagerWS.get(pubKey, domain, username);
	  // check if res equals error ?
  }
  
  public void put(byte[] pubKey, byte[] domain, byte[] username, byte[] password) {
	  String res = _passwordmanagerWS.put(pubKey, domain, username, password);
	  // check if res equals error ?
  }
  
  public void register(byte[] pubKey) {
	  String res = _passwordmanagerWS.register(pubKey);
	  // check if res equals error ?
  }
}
