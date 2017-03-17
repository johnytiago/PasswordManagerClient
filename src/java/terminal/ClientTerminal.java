package terminal;

import java.util.Scanner;

import manager.ClientManager;
  	

public class ClientTerminal {
	public static void main(String[] args) {  
		
		final String INIT_COMMAND = "init";
		final String REG_COMMAND = "register";
		final String PASS_COMMAND = "passwd";
		final String CLOSE_COMMAND = "close";
		
		
		ClientManager _clientManager = new ClientManager();
		
		System.out.println("Available commands are:");
		System.out.println("	" + INIT_COMMAND);
		System.out.println("	" + REG_COMMAND);
		System.out.println("	" + PASS_COMMAND);
		System.out.println("	" + CLOSE_COMMAND);
		System.out.println("");
		System.out.print(">");
		
		Scanner reader = new Scanner(System.in);
		//while(!reader.nextLine().equals(CLOSE_COMMAND)){
			//String username,password;
			//switch(reader.nextLine()){
				//case INIT_COMMAND:
					//System.out.println("username: ");
					//username = reader.nextLine();
					//System.out.println("password: ");
					//password = reader.nextLine();
					
					//_clientManager.init(username, password);
					//break;
				//case REG_COMMAND:
					//System.out.println("password: ");
					//password = reader.nextLine();
					
					//_clientManager.register_user();					
					//break;
				//case PASS_COMMAND:
					//break;		
				//default:
					//username = "";
					//password = "";
					//break;
			//}
		//}
		
		//passwordManagerWS = null;
		reader.close();
		System.out.println("goodbye!");
		return;
	}  
} 
