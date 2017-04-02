package ws;

public class PasswordManagerWSClient {

  private static PasswordManagerWS _passwordmanagerWS;


  public PasswordManagerWSClient() {
	  connect();
  }
  
  private static void connect() {
	  PasswordManagerWSImplService pmWSImplService = new PasswordManagerWSImplService();
	  _passwordmanagerWS = pmWSImplService.getPasswordManagerWSImplPort();
	  
	  System.out.println("Connected to PasswordManagerServer.");
	  System.out.println("Found service running at: " + pmWSImplService.getWSDLDocumentLocation().toString());
  }
  
  public byte[] get(Envelope envelope) {
	  return _passwordmanagerWS.get(envelope);
	  // check if res equals error ?
  }
  
  public void put(Envelope envelope) {
	  _passwordmanagerWS.put(envelope);
	  // check if res equals error ?
  }
  
  public void register(Envelope envelope) {
	  _passwordmanagerWS.register(envelope);
	  // check if res equals error ?
  }
}
