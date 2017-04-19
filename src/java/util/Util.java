package util;

import java.io.ByteArrayOutputStream;

public class Util {
  public byte[] msgToByteArray(ws.Message msg) {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
    try {
      outputStream.write( msg.getPublicKey() );
      outputStream.write( msg.getUsernameHash() );
      outputStream.write( msg.getPassword() );
      outputStream.write( msg.getTripletHash() );
      outputStream.write( msg.getCounter() );
      return outputStream.toByteArray();
    } catch( Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
