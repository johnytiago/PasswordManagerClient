package ws;

//import message.*;
import ws.PasswordManagerWSClient;

import static org.junit.Assert.*;
import org.junit.runners.MethodSorters;
import org.junit.FixMethodOrder;

import org.junit.BeforeClass;
import org.junit.Test;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PasswordManagerWSClientTest {
	
	private static PasswordManagerWSClient _clientAPI;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		_clientAPI = new PasswordManagerWSClient();
		PasswordManagerWSClient.connect();
	}

	@Test
	public void test_1_registerNewUser() {
		String res = _clientAPI.register("pubKey".getBytes());
		assertEquals("", res);
	}

	@Test
	public void test_2_registerUserAgain() {
		String res = _clientAPI.register("pubKey".getBytes());
		assertEquals(message.PasswordManagerMessages.PUBKEY_ALREADY_EXISTS, res);
	}

	@Test
	public void test_3_getWhileEmpty() {
		String res = _clientAPI.get("pubKey".getBytes(), "domain".getBytes(), "username".getBytes());
		assertEquals(message.PasswordManagerMessages.PASSWORD_NOT_FOUND, res);
	}

	@Test
	public void test_4_putNotRegistered() {
		String res = _clientAPI.put("NotRegisterUser".getBytes(), "trash".getBytes(), "trash".getBytes(), "trash".getBytes());
		assertEquals(message.PasswordManagerMessages.PUBKEY_NOT_FOUND, res);
  }

	@Test
	public void test_5_putSuccess() {
		String res = _clientAPI.put("pubKey".getBytes(), "domain".getBytes(), "username".getBytes(), "password".getBytes());
		assertEquals("", res);
  }

	@Test
	public void test_6_putTwice() {
		String res = _clientAPI.put("pubKey".getBytes(), "domain".getBytes(), "username".getBytes(), "password".getBytes());
		assertEquals(message.PasswordManagerMessages.USER_ALREADY_EXISTS_DOMAIN, res);
  }
	
	@Test
	public void test_7_getNotRegistered() {
		String res = _clientAPI.get("NotRegisterUser".getBytes(), "trash".getBytes(), "trash".getBytes());
		assertEquals(message.PasswordManagerMessages.PUBKEY_NOT_FOUND, res);
	}

	@Test
	public void test_8_getPasswordSuccess() {
		String res = _clientAPI.get("pubKey".getBytes(), "domain".getBytes(), "username".getBytes());
		assertEquals("password", res);
	}
}
