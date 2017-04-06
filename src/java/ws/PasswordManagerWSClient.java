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

  public Envelope register(byte[] pubKey) throws PasswordManagerExceptionHandler {

    Message msg = new Message();
    msg.publicKey = pubKey;
    Envelope envelope = new Envelope();
    envelope.message = msg;

    return _passwordmanagerWS.register(envelope);
  }

  public Envelope get(byte[] pubKey, byte[] domainHash, byte[] usernameHash) throws PasswordManagerExceptionHandler{

    Message msg = new Message();
    msg.setPublicKey(pubKey);
    msg.setDomainHash(domainHash);
    msg.setUsernameHash(usernameHash);

    Envelope envelope = new Envelope();
    envelope.setMessage(msg);

    return _passwordmanagerWS.get(envelope);
  }

  public Envelope put(byte[] pubKey, byte[] domainHash, byte[] usernameHash, byte[] password, byte[] tripletHash ) 
      throws PasswordManagerExceptionHandler {

    Message msg = new Message();
    msg.setPublicKey( pubKey);
    msg.setDomainHash(domainHash);
    msg.setUsernameHash(usernameHash);
    msg.setTripletHash(tripletHash);
    msg.setPassword(password);

    Envelope envelope = new Envelope();
    envelope.setMessage(msg);

    return _passwordmanagerWS.put(envelope);
  }

}
