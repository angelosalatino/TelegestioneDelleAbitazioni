package package1;
import java.io.*;
import java.util.*;

import package2.contentFile;



public class Lamp {
	ArrayList<contentFile> al;
	contentFile cf;
	
	public String serve(String mess){
		String answer=null;
		if(new File("setup\\luci.dat").exists()){
			if(mess.contains("ON")){
				answer=lampOn(mess); 
			}else{
				if(mess.contains("OFF")){
					answer=lampOff(mess);
				}else{
					if(mess.contains("STATE")){
						answer=lampState();
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
	 * NB. il valore -1 non esiste perche si assume che tutte le stanza abbiano la luce
	 */
	
	public String lampOn(String mess){
		String answer=null;
		al=genericMethod.readOnFile("luci.dat");
		if(genericMethod.containsInt(mess)){
					int b=genericMethod.returnInt(mess)-1;//-1 perche nell'inserimento delle stanze non è stato considerato lo Zero
					if(b>=al.size()){
						answer="410 Comando non accettato: Stanza non inserita.";
					}else{
							cf=al.get(b);
							
							if(cf.getValue()==0){
								cf.setValue(1);
								al.set(b, cf);
								genericMethod.writeOnFile(al, "luci.dat");
								answer="210 Comando accettato.";
							}else{
								if(cf.getValue()==1){
									answer="310 Comando Ridondante: Luce già accesa.";
									
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
	
	public String lampOff(String mess){
		String answer=null;
		al=genericMethod.readOnFile("luci.dat");
		if(genericMethod.containsInt(mess)){
			int b=genericMethod.returnInt(mess)-1;
				if(b>=al.size()){
					answer="410 Comando non accettato: Stanza non inserita.";
				}else{
						cf=al.get(b);
						
						if(cf.getValue()==1){
							cf.setValue(0);
							al.set(b, cf);
							genericMethod.writeOnFile(al, "luci.dat");
							answer="210 Comando accettato.";
						}else{
							if(cf.getValue()==0){
								answer="310 Comando Ridondante: Luce già spenta.";
								
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
	
	public String lampState(){
		String answer="Tutte le stanze sono sprovviste di condizionatore";
		String temp;
		al=genericMethod.readOnFile("luci.dat");
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<al.size();i++){
			cf=al.get(i);
			String a1=cf.getRoom();
			int a2=cf.getNum();
			int a3=cf.getValue();
		//Nel messaggio di ritorno includo solo le stanze che hannoil condizionatore	
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
