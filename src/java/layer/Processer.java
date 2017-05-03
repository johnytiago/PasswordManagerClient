package layer;

import java.util.Collections;
import java.util.HashMap;
import ws.Envelope;

public class Processer{
	private HashMap<Integer, byte[]> readlist= new HashMap<Integer, byte[]>();
	private int N = 10;//numero de replicas
	
	//################### Checks read values################
	public byte[] Checker(){
		
			int maxKey = Collections.max(readlist.keySet());//get highest value from the readlist
			byte[] value = readlist.get(maxKey); 
			readlist.clear();//clear to reuse it not sure if really needed garbage collector probably will take care of it
			return value;//the way we are suposed to read from the server sending the infos
		}
	
		public byte[] NumberMatch(int ts, byte[]password){
			readlist.put(ts, password);
			if(readlist.size()>(N+2)/2){ //falta f nesta conta
				Checker();
			}
			return null;
			
		}
	//#######################################################
		
	
}