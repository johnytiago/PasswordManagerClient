package ws.client;

import java.util.Scanner;

import ws.PasswordManagerWS;
import ws.PasswordManagerWSImplService;  
  	

public class PasswordManagerWSClient {
	public static void main(String[] args) {  
		final String INIT_COMMAND = "init";
		final String REG_COMMAND = "register";
		final String PASS_COMMAND = "passwd";
		final String CLOSE_COMMAND = "close";
		
		PasswordManagerWSImplService passwordManagerWSImplService = new PasswordManagerWSImplService();  
		PasswordManagerWS passwordManagerWS = passwordManagerWSImplService.getPasswordManagerWSImplPort();  
		
		System.out.println("Available commands are:");
		System.out.println("	" + INIT_COMMAND);
		System.out.println("	" + REG_COMMAND);
		System.out.println("	" + PASS_COMMAND);
		System.out.println("	" + CLOSE_COMMAND);
		System.out.println("");
		System.out.print(">");
		
		Scanner reader = new Scanner(System.in);
		while(!reader.nextLine().equals(CLOSE_COMMAND)){
			switch(reader.nextLine()){
				case INIT_COMMAND:
					passwordManagerWS.register(null);
					break;
				case REG_COMMAND:
					break;
				case PASS_COMMAND:
					break;				
			}
		}
		
		passwordManagerWS = null;
		reader.close();
		System.out.println("goodbye!");
		return;
	}  
} 