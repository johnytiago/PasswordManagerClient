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

  public byte[] get(byte[] pubKey, byte[] domainHash, byte[] usernameHash) {
    Envelope envelope = new Envelope();

    Message msg = new Message();
    msg.setPublicKey(pubKey);
    msg.setDomainHash(domainHash);
    msg.setUsernameHash(usernameHash);

    envelope.setMessage(msg);
    try{
      return _passwordmanagerWS.get(envelope);
    }
    catch(PasswordManagerExceptionHandler pme){
      return null;
    }
  }

  public String put(byte[] pubKey, byte[] domainHash, byte[] usernameHash, byte[] password, byte[] tripletHash, ) {

    Message msg = new Message();
    msg.setPublicKey( pubKey);
    msg.setDomainHash(domainHash);
    msg.setUsernameHash(usernameHash);
    msg.setTripletHash(tripletHash);
    msg.setPassword(password);

    Envelope envelope = new Envelope();
    envelope.setMessage(msg);

    try{
      _passwordmanagerWS.put(envelope);
      return "";
    }
    catch(PasswordManagerExceptionHandler pme){
      return pme.getMessage();
    }
  }

  public String register(byte[] pubKey) {

    Message msg = new Message();
    msg.publicKey = pubKey;
    Envelope envelope = new Envelope();
    envelope.message = msg;

    try{
      _passwordmanagerWS.register(envelope);
      return "";
    }
    catch(PasswordManagerExceptionHandler pme){
      return pme.getMessage();
    }
    // check if res equals error ?
  }
}
