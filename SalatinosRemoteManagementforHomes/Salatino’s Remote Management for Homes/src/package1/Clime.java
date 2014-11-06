package package1;


import java.util.*;
import java.io.*;

import package2.*;


public class Clime {
	ArrayList<contentFile> al;
	contentFile cf;
	
	public Clime(){
		cf=new contentFile();
	}
	
	public String serve(String mess){
		String answer=null;
		if(new File("setup\\condizionatori.dat").exists()){
			if(mess.contains("DEACT")){
				answer=climeDeact(mess); 
			}else{
				if(mess.contains("ACT")){
					answer=climeAct(mess);
				}else{
					if(mess.contains("STATE")){
						answer=climeState();
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
	
	public String climeAct(String mess){
		String answer=null;
		al=genericMethod.readOnFile("condizionatori.dat");
		if(genericMethod.containsInt(mess)){
					int b=genericMethod.returnInt(mess)-1;//-1 perche nell'inserimento delle stanze non è stato considerato lo Zero
					if(b>=al.size()){
						answer="410 Comando non accettato: Stanza non inserita.";
					}else{
							cf=al.get(b);
							
							if(cf.getValue()==0){
								cf.setValue(1);
								al.set(b, cf);
								genericMethod.writeOnFile(al, "condizionatori.dat");
								answer="210 Comando accettato.";
							}else{
								if(cf.getValue()==1){
									answer="310 Comando Ridondante: Climatizzatore già acceso.";
									
								}else{
									if(cf.getValue()==-1){
										answer="410 Comando non accettato: Climatizzatore non presente.";
									}else{
										mess="410 Comando non accettato :Errore di sistema, viene consigliato l'uso del setup.";
									}
								}
							}
							
						}
				}else{
					answer="401 Sintassi del comando errata.";
				}
		return answer;
	}
	
	public String climeDeact(String mess){
		String answer=null;
		al=genericMethod.readOnFile("condizionatori.dat");
		if(genericMethod.containsInt(mess)){
			int b=genericMethod.returnInt(mess)-1;
				if(b>=al.size()){
					answer="410 Comando non accettato: Stanza non inserita.";
				}else{
						cf=al.get(b);
						
						if(cf.getValue()==1){
							cf.setValue(0);
							al.set(b, cf);
							genericMethod.writeOnFile(al, "condizionatori.dat");
							answer="210 Comando accettato.";
						}else{
							if(cf.getValue()==0){
								answer="310 Comando Ridondante: Climatizzatore già spento.";
								
							}else{
								if(cf.getValue()==-1){
									answer="410 Comando non accettato: Climatizzatore non presente.";
								}else{
									mess="410 Comando non accettato :Errore di sistema, viene consigliato l'uso del setup.";
								}
							}
						}
						
				}
		}else{
			answer="410 Sintassi del comando errata.";
		}
		return answer;
	}
	
	public String climeState(){
		String answer="Tutte le stanze sono sprovviste di condizionatore";
		String temp;
		al=genericMethod.readOnFile("condizionatori.dat");
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
