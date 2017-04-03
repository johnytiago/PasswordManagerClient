package ws;

import exception.PasswordManagerExceptionHandler;

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
  
  public byte[] get(byte[] pubKey, byte[] domain, byte[] username) {
	  Envelope envelope = new Envelope();
		
	  Message msg = new Message();
	  msg.setPublicKey(pubKey);
	  msg.setDomain(domain);
	  msg.setUsername(username);
	  
	  envelope.setMessage(msg);
	  try{
		  return _passwordmanagerWS.get(envelope);
	  }
	  catch(PasswordManagerExceptionHandler pme){
		  return null;
	  }
  }
  
  public String put(byte[] pubKey, byte[] domain, byte[] username, byte[] password) {
	  
	  // check if res equals error ?
	  
	  Envelope envelope = new Envelope();
		
	  Message msg = new Message();
	  msg.setPublicKey(pubKey);
	  msg.setDomain(domain);
	  msg.setUsername(username);
	  msg.setPassword(password);
	  
	  envelope.setMessage(msg);
	  try{
		  _passwordmanagerWS.put(envelope);
	  }
	  catch(PasswordManagerExceptionHandler pme){
		  return pme.getMessage();
	  }
	  return "";
  }
  
  public String register(byte[] pubKey) {
	  Envelope envelope = new Envelope();
	
	  Message msg = new Message();
	  msg.setPublicKey(pubKey);
	  
	  envelope.setMessage(msg);
	  try{
		  _passwordmanagerWS.register(envelope);
	  }
	  catch(PasswordManagerExceptionHandler pme){
		  return pme.getMessage();
	  }
	  return "";
	  // check if res equals error ?
  }
}
