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
    byte [] encryptedText = manager.encrypt(clearText.getBytes());
    String result =  manager.decrypt(encryptedText);
    AssertEquals(clearText, result);
	}
}
