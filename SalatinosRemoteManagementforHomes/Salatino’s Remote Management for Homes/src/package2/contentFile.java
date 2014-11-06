package package2;

import java.io.*;

public class contentFile implements Serializable {
	private String room;//nome della stanza
	private int num;//id univoco della stanza in modo da poter essere tracciato con il comando
	private int value;// value specifica se una determinata risorsa è accesa(1), spenta(0) oppure indisponibile(-1)
	
	public contentFile(){
		//costruttore
		
		room=null;
		num=0;
		value=0;
	}
	
	public void setRoom(String room){
		this.room=room;
	}
	
	public void setNum(int num){
		this.num=num;
	}
	
	public void setValue(int value){
		this.value=value;
	}
	
	public String getRoom(){
		return room;
	}
	
	public int getNum(){
		return num;
	}
	
	public int getValue(){
		return value;
	}
}
