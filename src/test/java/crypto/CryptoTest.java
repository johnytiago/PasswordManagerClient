package crypto;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CryptoTest {

	@Test
	public void encryptionTest() {
    Crypto crypto = new Crypto();
    crypto.init("username", "password");
    String clearText = "This is cleartext";

    byte [] encryptedText = null;
    try{
    	encryptedText = crypto.encrypt(clearText.getBytes());
    } catch (Exception e){
    	System.out.println(e.getMessage());
    }
    String result =  crypto.decrypt(encryptedText);
    assertEquals(clearText, result);
	}
}
