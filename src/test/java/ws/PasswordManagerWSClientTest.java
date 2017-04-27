package ws;

import ws.Envelope;
import exception.PubKeyAlreadyExistsException;
import crypto.*;
import util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PasswordManagerWSClientTest {

  private static PasswordManagerWSClient _clientAPI;
  private static Crypto _crypto;
  private Util _util = new Util();

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    _clientAPI = new PasswordManagerWSClient();
    _clientAPI.init("client", "client");
    _crypto = new Crypto();
    _crypto.init("client", "client"); // IF it is the same it gets the client's ks
  }

  @Test
  public void test_1_registerNewUser() {
    try {
      assertTrue(_clientAPI.register());
    } catch (PasswordManagerException_Exception | PubKeyAlreadyExistsException_Exception e) {
      System.out.println("Failed 1");
      e.printStackTrace();
    }
  }

  @Test(expected=PubKeyAlreadyExistsException_Exception.class)
  public void test_2_registerUserAgain() throws PasswordManagerException_Exception, PubKeyAlreadyExistsException_Exception {
    _clientAPI.register();
  }
  @Test
  public void test_saltGet(){
	  try{
	  String obj = _clientAPI.get("domain", "admin");
	  assertNotNull(obj);
	  }catch(Exception e){
		  
	  }
  }
  //###########################
  @Test(expected=IndexOutOfBoundsException.class)
  public void testIndexOutOfBoundsException() {
	  
  }
  //##########################
  @Test
  public void test_saltPut(){
	  try{
		  boolean test =_clientAPI.put("domain", "admin","password");
		  assertTrue(test);
	  }catch(Exception e){
		  
	  }
	  
  }
  // TODO: Update tests
  //@Test
  //public void test_3_getWhileEmpty() {
  //Envelope res = _clientAPI.get("pubKey".getBytes(), "domain".getBytes(), "username".getBytes());
  ////assertEquals(message.PasswordManagerMessages.PASSWORD_NOT_FOUND.getBytes(), res);
  //}

  //@Test
  //public void test_4_putNotRegistered() {
  //Envelope res = _clientAPI.put("NotRegisterUser".getBytes(), "trash".getBytes(), "trash".getBytes(), "trash".getBytes(), "trash".getBytes());
  ////assertEquals(message.PasswordManagerMessages.PUBKEY_NOT_FOUND, res);
  //}

  //@Test
  //public void test_5_putSuccess() {
  ////Envelope res = _clientAPI.put("pubKey".getBytes(), "domain".getBytes(), "username".getBytes(), "password".getBytes());
  ////assertEquals("", res);
  //}

  //@Test
  //public void test_6_putTwice() {
  ////Envelope res = _clientAPI.put("pubKey".getBytes(), "domain".getBytes(), "username".getBytes(), "password".getBytes());
  ////assertEquals(message.PasswordManagerMessages.USER_ALREADY_EXISTS_DOMAIN, res);
  //}

  //@Test
  //public void test_7_getNotRegistered() {
  //Envelope res = _clientAPI.get("NotRegisterUser".getBytes(), "trash".getBytes(), "trash".getBytes());
  ////assertEquals(message.PasswordManagerMessages.PUBKEY_NOT_FOUND.getBytes(), res);
  //}

  //@Test
  //public void test_7_getWrongDomain() {
  //Envelope res = _clientAPI.get("pubKey".getBytes(), "wrongDomain".getBytes(), "username".getBytes());
  ////assertEquals(message.PasswordManagerMessages.PASSWORD_NOT_FOUND.getBytes(), res);
  //}
  //@Test
  //public void test_7_getWrongUsername() {
  //Envelope res = _clientAPI.get("pubKey".getBytes(), "domain".getBytes(), "wrongUsername".getBytes());
  ////assertEquals(message.PasswordManagerMessages.PASSWORD_NOT_FOUND.getBytes(), res);
  //}

  //@Test
  //public void test_8_getPasswordSuccess() {
  //Envelope res = _clientAPI.get("pubKey".getBytes(), "domain".getBytes(), "username".getBytes());
  ////assertEquals("password".getBytes(), res);
  //}
}
