package layer;

import java.io.ByteArrayOutputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

import javax.crypto.SecretKey;

import crypto.Crypto;
import util.SecretKeyStore;
import util.Util;
import ws.Envelope;

public class Security {

  private Crypto _crypto;
  private Util _util = new Util();
  private SecretKeyStore dhKeyStore = new SecretKeyStore();
  private boolean DEBUG = Boolean.valueOf((System.getenv("DEBUG")));

  public Security (Crypto crypto){
    this._crypto = crypto;
  }

  public byte[] addSalt(byte[] data, byte[] salt){
    try{
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
      outputStream.write( data );
      outputStream.write( salt );
      return outputStream.toByteArray();
    } catch (Exception e){
      e.printStackTrace();
      return null;
    }
  }

  private boolean verifyHMAC( Envelope envelope ) {
    SecretKey DHSecretKey = dhKeyStore.get( envelope.getDHPublicKey() );
    byte[] HMAC = _crypto.genMac( _util.msgToByteArray( envelope.getMessage() ), DHSecretKey );
    return Arrays.equals(HMAC, envelope.getHMAC());
  }

  private void addHMAC( Envelope envelope, byte[] dhPubKeySrv ) {
    SecretKey DHSecretKey = dhKeyStore.get( dhPubKeySrv );
    byte[] HMAC = _crypto.genMac( _util.msgToByteArray( envelope.getMessage() ), DHSecretKey );
    envelope.setHMAC( HMAC );
    return;
  }

  public void prepareEnvelope( Envelope envelope, byte[] pubKeySrv ) {
    generateDH( envelope, pubKeySrv );
    envelope.getMessage().setPublicKey( _crypto.getPublicKey().getEncoded() ); 
    envelope.setDHPublicKey( _crypto.getDHPublicKey().getEncoded() );
    envelope.getMessage().setCounter(_crypto.addCounter(pubKeySrv));
    byte[] msg = _util.singnableByteArray(envelope.getMessage());
    envelope.setSignature(_crypto.genSign( msg , (PrivateKey)_crypto.getPrivateKey() ));
    // Must be the last to add to envelope
    addHMAC( envelope, pubKeySrv );
    return;
  }

  public boolean verifyEnvelope( Envelope envelope ) {
    byte[] msg = _util.singnableByteArray( envelope.getMessage() );
    Boolean sign = _crypto.verSign(msg, _crypto.retrievePubKey(envelope.getMessage().getPublicKey()), envelope.getSignature());

    if( DEBUG ) {
      System.out.println("[DEBUG] MAC verification passed? " + verifyHMAC( envelope ));
      System.out.println("[DEBUG] Counter verification passed? " + _crypto.verifyCounter( envelope.getDHPublicKey(), envelope.getMessage().getCounter() ));
      System.out.println("[DEBUG] Sign verification passed? " + sign);
    }

    return verifyHMAC( envelope ) && sign && _crypto.verifyCounter( envelope.getDHPublicKey(), envelope.getMessage().getCounter());
  }

  private void generateDH( Envelope envelope, byte[] pubKeySrv ){
    PublicKey DHPubKeySrv = _crypto.retrieveDHPubKey( pubKeySrv );
    SecretKey DHSecretKey = _crypto.generateDH( _crypto.getDHPrivateKey(), DHPubKeySrv );
    // Store key to save CPU
    dhKeyStore.put( pubKeySrv , DHSecretKey);
  }
}
