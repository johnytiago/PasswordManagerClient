package ws;

import ws.PasswordManagerWS;
import ws.PasswordManagerWSImplService;

public class PasswordManagerWSClient {

  private static PasswordManagerWS _passwordmanagerWS;
  
  public static void main(String[] args) {
	  connect();
  }

  public PasswordManagerWSClient() {
    connect();
  }
  
  public static void connect() {
	  PasswordManagerWSImplService pmWSImplService = new PasswordManagerWSImplService();
	  _passwordmanagerWS = pmWSImplService.getPasswordManagerWSImplPort();
	  
	  System.out.println("Connected to PasswordManagerServer.");
	  System.out.println("Found service running at: " + pmWSImplService.getWSDLDocumentLocation().toString());
  }
  
  public String get(byte[] pubKey, byte[] domain, byte[] username) {
	  return _passwordmanagerWS.get(pubKey, domain, username);
	  // check if res equals error ?
  }
  
  public String put(byte[] pubKey, byte[] domain, byte[] username, byte[] password) {
	 return _passwordmanagerWS.put(pubKey, domain, username, password);
	  // check if res equals error ?
  }
  
  public String register(byte[] pubKey) {
	  return _passwordmanagerWS.register(pubKey);
	  // check if res equals error ?
  }
}
