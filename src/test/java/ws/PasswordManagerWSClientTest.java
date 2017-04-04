package ws;

import message.PasswordManagerMessages;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PasswordManagerWSClientTest {

	private static PasswordManagerWSClient _clientAPI;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		_clientAPI = new PasswordManagerWSClient();
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
		byte[] res = _clientAPI.get("pubKey".getBytes(), "domain".getBytes(), "username".getBytes());
		assertEquals(message.PasswordManagerMessages.PASSWORD_NOT_FOUND.getBytes(), res);
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
		byte[] res = _clientAPI.get("NotRegisterUser".getBytes(), "trash".getBytes(), "trash".getBytes());
		assertEquals(message.PasswordManagerMessages.PUBKEY_NOT_FOUND.getBytes(), res);
	}

	@Test
	public void test_7_getWrongDomain() {
		byte[] res = _clientAPI.get("pubKey".getBytes(), "wrongDomain".getBytes(), "username".getBytes());
		assertEquals(message.PasswordManagerMessages.PASSWORD_NOT_FOUND.getBytes(), res);
	}
	@Test
	public void test_7_getWrongUsername() {
		byte[] res = _clientAPI.get("pubKey".getBytes(), "domain".getBytes(), "wrongUsername".getBytes());
		assertEquals(message.PasswordManagerMessages.PASSWORD_NOT_FOUND.getBytes(), res);
	}

	@Test
	public void test_8_getPasswordSuccess() {
		byte[] res = _clientAPI.get("pubKey".getBytes(), "domain".getBytes(), "username".getBytes());
		assertEquals("password".getBytes(), res);
	}
}
