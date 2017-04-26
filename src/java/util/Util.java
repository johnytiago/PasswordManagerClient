package util;

import java.io.ByteArrayOutputStream;

public class Util {
  public byte[] msgToByteArray(ws.Message msg) {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
    try {
      if ( msg.getPublicKey() != null)
        outputStream.write( msg.getPublicKey() );
      if ( msg.getUsernameHash() != null)
        outputStream.write( msg.getUsernameHash() );
      if ( msg.getPassword() != null)
        outputStream.write( msg.getPassword() );
      if ( msg.getTripletHash() != null)
        outputStream.write( msg.getTripletHash() );
      outputStream.write( msg.getCounter() );
      return outputStream.toByteArray();
    } catch( Exception e) {
      e.printStackTrace();
      return null;
    }
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
}
