package package1;

import java.io.*;
import java.util.*;

import package2.contentFile;



public class Irrigate {
	ArrayList<contentFile> al;
	contentFile cf;
	
	public String serve(String mess){
		String answer=null;
		if(new File("setup\\irrigazione.dat").exists()){
			if(mess.contains("ON")){
				answer=irrOn(mess); 
			}else{
				if(mess.contains("OFF")){
					answer=irrOff(mess);
				}else{
					if(mess.contains("STATE")){
						answer=irrState();
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
	
	/*
	 * NB. In questa classe non potremo mai tr0vare valori -1 significativi, perche alla dichiarazione
	 * dell'array list, abbiamo assunto l'esistenza dell'impianto per zona.
	 * Non ha senso una zona senza impianto.
	 * Quindi il valore -1 verra trattato come valore senza senso
	 */
	
	public String irrOn(String mess){
		String answer=null;
		al=genericMethod.readOnFile("irrigazione.dat");
		if(genericMethod.containsInt(mess)){
					int b=genericMethod.returnInt(mess)-1;//-1 perche nell'inserimento delle stanze non è stato considerato lo Zero
					if(b>=al.size()){
						answer="410 Comando non accettato: Zona non inserita.";
					}else{
							cf=al.get(b);
							
							if(cf.getValue()==0){
								cf.setValue(1);
								al.set(b, cf);
								genericMethod.writeOnFile(al, "irrigazione.dat");
								answer="210 Comando accettato.";
							}else{
								if(cf.getValue()==1){
									answer="310 Comando Ridondante: Impianto di zona già acceso.";
									
								}else{
										mess="410 Comando non accettato :Errore di sistema, viene consigliato l'uso del setup.";
									
								}
							}
							
						}
					}else{
						answer="401 Sintassi del comando errata.";
					}
		return answer;
	}
	
	public String irrOff(String mess){
		String answer=null;
		al=genericMethod.readOnFile("irrigazione.dat");
		if(genericMethod.containsInt(mess)){
			int b=genericMethod.returnInt(mess)-1;
				if(b>=al.size()){
					answer="410 Comando non accettato: Zona non inserita.";
				}else{
						cf=al.get(b);
						
						if(cf.getValue()==1){
							cf.setValue(0);
							al.set(b, cf);
							genericMethod.writeOnFile(al, "irrigazione.dat");
							answer="210 Comando accettato.";
						}else{
							if(cf.getValue()==0){
								answer="310 Comando Ridondante: Impianto di zona già spento.";
								
							}else{
								
									mess="410 Comando non accettato :Errore di sistema, viene consigliato l'uso del setup.";
								
							}
						}
						
				}
		}else{
			answer="410 Sintassi del comando errata.";
		}
		return answer;
	}
	
	
	public String irrState(){
		String answer;
		String temp;
		al=genericMethod.readOnFile("irrigazione.dat");
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<al.size();i++){
			cf=al.get(i);
			String a1=cf.getRoom();
			int a2=cf.getNum();
			int a3=cf.getValue();
		//Includo nella stringa di ritorno solo i valori che hanno senso
		if(a3==0 || a3==1){
			if(a3==0){
					temp="Spento";
				}else{
					temp="Acceso";
					}
			sb.append("--"+a2+" "+a1+": "+temp+"\n");
		}	
		}
		
		answer=sb.toString();
		return answer;
	}
	
	

}
