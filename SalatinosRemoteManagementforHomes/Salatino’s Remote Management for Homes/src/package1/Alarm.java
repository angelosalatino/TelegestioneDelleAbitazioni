package package1;

import java.io.*;


public class Alarm {
	
	private int value;
	
	public Alarm(){
		//costruttore
	value=0;
	}
	
	public String serve(String mess){
		String answer=null;
		if(new File("setup\\alarm.dat").exists()){
			
			if(mess.contains("ON")){
				answer=alarmOn(); 
			}else{
				if(mess.contains("OFF")){
					answer=alarmOff();
				}else{
					if(mess.contains("STATE")){
						answer=alarmState();
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
	
	public String alarmOn(){
		String mess;
		value=genericMethod.simpleReadOnFile("alarm.dat");
		
		if(value==-1){
			mess="410 Comando non accettato :Sistema di allarme non inserito nel sistema.";
		}else{
			if(value==0){
				value=1;
				genericMethod.simpleWriteOnFile(value, "alarm.dat");
				mess="210 Comando accettato.";
			}else{
				if(value==1){
					mess="410 Comando non accettato :Sistema di allarme già attivo.";
				}else{
					mess="410 Comando non accettato :Errore di sistema, viene consigliato l'uso del setup.";
				}
			}
		}
		
		return mess;
	}
	
	public String alarmOff(){
		String mess;
		value=genericMethod.simpleReadOnFile("alarm.dat");
		
		if(value==-1){
			mess="410 Comando non accettato :Sistema di allarme non inserito nel sistema.";
		}else{
			if(value==1){
				value=0;
				genericMethod.simpleWriteOnFile(value, "alarm.dat");
				mess="210 Comando accettato.";
			}else{
				if(value==0){
					mess="410 Comando non accettato :Sistema di allarme già disattivato.";
				}else{
					mess="410 Comando non accettato :Errore di sistema, viene consigliato l'uso del setup.";
				}
			}
		}
		
		return mess;
	}
	
	public String alarmState(){
		String mess,temp;
		value=genericMethod.simpleReadOnFile("alarm.dat");

		if(value==-1){
			temp="Non implementato";
		}else{
			if(value==1){
				temp="Attivato";
			}else{
				if(value==0){
					temp="Disattivato";
				}else{
					temp="Crash";
				}
			}
		}
		
		mess="--"+"Stato allarme: "+temp;
		
		return mess;
	}
	
	

}
