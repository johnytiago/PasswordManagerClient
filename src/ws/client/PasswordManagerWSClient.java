package ws.client;

import ws.PasswordManagerWS;  
import ws.PasswordManagerWSImplService;  
  	

public class PasswordManagerWSClient {
	public static void main(String[] args) {  

		PasswordManagerWSImplService passwordManagerWSImplService = new PasswordManagerWSImplService();  
		PasswordManagerWS passwordManagerWS = passwordManagerWSImplService.getPasswordManagerWSImplPort();  
		
		System.out.println(passwordManagerWS.helloWorld("Joao"));  
	}  
} 