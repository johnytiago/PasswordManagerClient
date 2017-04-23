package manager;

import ws.PasswordManagerWSClient;

public class ClientManager{

  private static PasswordManagerWSClient _clientAPI;

  public static void main(String[] args) {
    _clientAPI = new PasswordManagerWSClient();
  }

  public void init(String username, String password){
    _clientAPI.init(username, password);
  }

  public void register(){    
    try {
      if(!_clientAPI.register())
        System.out.println("Error Not Registered");
      System.out.println("Success! Registered");
    } catch(Exception e){
      System.out.println("Error registring");
      // TODO: Handle exceptions from server
    }
  }

  public String getPassword(String domain, String username){
    try{
      String pw = _clientAPI.get(domain, username);

      System.out.println("Success! Password retrieved: " + pw );
      return pw;
    }catch(Exception e){
      System.out.println("Error getting password");
      // TODO: Handle exceptions from server
      return null;
    }
  }

  public void savePassword(String domain, String username, String password){
    try{
      if (!_clientAPI.put(domain, username, password))
        System.out.println("Error Saving Password");
      System.out.println("Success! Password saved");
    } catch(Exception e){
      System.out.println("Error saving password");
      // TODO: Handle exceptions from server
    }
  }
}
