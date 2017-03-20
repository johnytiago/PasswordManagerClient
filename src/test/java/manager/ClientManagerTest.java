package manager;

import static org.junit.Assert.*;
import org.junit.runners.MethodSorters;
import org.junit.FixMethodOrder;

import org.junit.BeforeClass;
import org.junit.Test;

//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ClientManagerTest {
	//@BeforeClass
	//public static void setUpBeforeClass() throws Exception {
		//_clientAPI = new PasswordManagerWSClient();
	//}

	@Test
	public void encryptionTest() {
    ClientManager manager = new ClientManager();
    manager.init("username", "password");
    String clearText = "This is cleartext";

    byte [] encryptedText = null;
    try{
    	encryptedText = manager.encript(clearText.getBytes());
    } catch (Exception e){

    }
    String result =  manager.decrypt(encryptedText);
    assertEquals(clearText, result);
	}
}
