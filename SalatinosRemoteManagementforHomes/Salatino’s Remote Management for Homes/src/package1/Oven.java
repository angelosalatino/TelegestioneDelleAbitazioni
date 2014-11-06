package package1;

import java.io.*;


public class Oven {
	
	private int value;
	
	public Oven(){
		//costruttore
	value=0;
	}
	
	public String serve(String mess){
		String answer=null;
		if(new File("setup\\oven.dat").exists()){
			if(mess.contains("ON")){
				answer=ovenOn(); 
			}else{
				if(mess.contains("OFF")){
					answer=ovenOff();
				}else{
					if(mess.contains("STATE")){
						answer=ovenState();
					}else{
						answer="401 Sintassi del comando errata";
					}
				}
				
			}
		}else{
			answer="510 Errore del server, non è possibile gestire il comando";
		}
		
		return answer;
	}
	
	public String ovenOn(){String mess;
	value=genericMethod.simpleReadOnFile("oven.dat");
	
	if(value==-1){
		mess="410 Comando non accettato :Sistema di allarme non inserito nel sistema.";
	}else{
		if(value==0){
			value=1;
			genericMethod.simpleWriteOnFile(value, "oven.dat");
			mess="210 Comando accettato.";
		}else{
			if(value==1){
				mess="410 Comando non accettato :Forno già acceso.";
			}else{
				mess="410 Comando non accettato :Errore di sistema, viene consigliato l'uso del setup.";
			}
		}
	}
	
	return mess;
	}
	
	public String ovenOff(){
		String mess;
		value=genericMethod.simpleReadOnFile("oven.dat");
		
		if(value==-1){
			mess="410 Comando non accettato :Sistema di allarme non inserito nel sistema.";
		}else{
			if(value==1){
				value=0;
				genericMethod.simpleWriteOnFile(value, "oven.dat");
				mess="210 Comando accettato.";
			}else{
				if(value==0){
					mess="410 Comando non accettato :Forno già spento.";
				}else{
					mess="410 Comando non accettato :Errore di sistema, viene consigliato l'uso del setup.";
				}
			}
		}
		
		return mess;
	}

	public String ovenState(){
		String mess,temp;
		value=genericMethod.simpleReadOnFile("oven.dat");

		if(value==-1){
			temp="Non implementato";
		}else{
			if(value==1){
				temp="Acceso";
			}else{
				if(value==0){
					temp="Spento";
				}else{
					temp="Crash";
				}
			}
		}
		
		mess="--Stato del forno: "+temp;
		
		return mess;
	}
	
}

