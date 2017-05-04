package lib;

import java.util.HashMap;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class Bizantine{ 
	
	//private int NUM_REPLICAS ;
	
	//private HashMap<Integer, String> readList;
	
	//private HashMap<String, Boolean> ackList;	
	//private int wts;
	//private int rid;
	//private int tsFinal;
	//private String value;

	//public void Init(int N){
		//wts = 0;
		//ackList = new HashMap<String, Boolean>();
		//readList = new HashMap<Integer, String>();
		//rid = 0;
	//}

	//public void Write(String v){
		//wts = wts + 1;
		//for(int i ; i<ackList.size() ; i++){// suposed to write to all on the acklist
			//writeAux(wts,v);
		//}
	//}
	//public void writeAux(int ts ,String v){
		//if(ts>tsFinal){
			//tsFinal = ts;
			//value = v;
		//}
		//ACKs(ts);
		////return v; // should only return if passed on ack testing
	//}

	//public void ACKs(int ts){
		////send a message to all servers with the timestamp and wait for their ack
		////stays in a lock probably waiting ffor this next if while the acks are coming in
		//ackList.add(server); //put ack on the ones that acked
		//if(ackList.size()>(N+2)/2){ //check if the number of acks is higher than formula
			//ackList.clear(); //mb reset the acklist?
			//return;//missing this fuction probably will compare the output
		//}
	//}

	//public void Read(){
		//rid=rid+1;

		//for(int i;i<readList.size();i++){//suposed to write to all in the readlist
			//SendREAD(rid);
		//}
	//}
	//public void SendREAD(int rid){
		////missing the send thing  and whar we need to send
		////here it will pass the values to the checker
		//Checker(r,ts,val);//r=rid, ts=timestamp of the read, val = value returned of the read
	//}

	//public String Checker(String v,int ts,int rid){
		//readList.put(ts,v);
		//if(readList.size()>(N+2)/2){ //o que é este f? seria multiplicado pelo 2
			//int maxKey = Collections.max(readList.keySet());//get highest value from the readlist
			//String value = readList.get(maxKey); 
			//readList.clear();//clear to reuse it not sure if really needed garbage collector probably will take care of it
			//return value;//the way we are suposed to read from the server sending the infos
		//}
	//}
}
