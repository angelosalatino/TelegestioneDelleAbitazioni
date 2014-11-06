package package1;	

import java.util.*;
import java.io.*;

import package2.contentFile;


	public class Blind {
		ArrayList<contentFile> al;
		contentFile cf;
		
		public Blind(){
			cf=new contentFile();
		}
		
		public String serve(String mess){
			String answer=null;
			if(new File("setup\\tapparelle.dat").exists()){
				if(mess.contains("STATE")){
					answer=blindState();
				}else{
					if(mess.contains("LOWER")){
						//abbassa le tapparelle
						answer=blindL(mess);
					}else{
						if(mess.contains("RAISE")){
							//alza le tapparelle
							answer=blindR(mess); 
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
		
		public String blindR(String mess){
			String answer=null;
			al=genericMethod.readOnFile("tapparelle.dat");
			if(genericMethod.containsInt(mess)){
						int b=genericMethod.returnInt(mess)-1;//-1 perche nell'inserimento delle stanze non è stato considerato lo Zero
						if(b>=al.size()){
							answer="410 Comando non accettato: Stanza non inserita.";
						}else{
								cf=al.get(b);
								
								if(cf.getValue()==0){
									cf.setValue(1);
									al.set(b, cf);
									genericMethod.writeOnFile(al, "tapparelle.dat");
									answer="210 Comando accettato.";
								}else{
									if(cf.getValue()==1){
										answer="310 Comando Ridondante: Tapparella già alzata.";
										
									}else{
										if(cf.getValue()==-1){
											answer="410 Comando non accettato: Tapparella non presente.";
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
		
		public String blindL(String mess){
			String answer=null;
			al=genericMethod.readOnFile("tapparelle.dat");
			if(genericMethod.containsInt(mess)){
				int b=genericMethod.returnInt(mess)-1;
					if(b>=al.size()){
						answer="410 Comando non accettato: Stanza non inserita.";
					}else{
							cf=al.get(b);
							
							if(cf.getValue()==1){
								cf.setValue(0);
								al.set(b, cf);
								genericMethod.writeOnFile(al, "tapparelle.dat");
								answer="210 Comando accettato.";
							}else{
								if(cf.getValue()==0){
									answer="310 Comando Ridondante: Tapparella già abbassata.";
									
								}else{
									if(cf.getValue()==-1){
										answer="410 Comando non accettato: Tapparella non presente.";
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
		
		public String blindState(){
			String answer;
			String temp;
			al=genericMethod.readOnFile("tapparelle.dat");
			StringBuffer sb=new StringBuffer();
			for(int i=0;i<al.size();i++){
				cf=al.get(i);
				String a1=cf.getRoom();
				int a2=cf.getNum();
				int a3=cf.getValue();
			//Nel messaggio di ritorno includo solo le stanze che hanno le tapparelle	
			if(a3==0 || a3==1){
				if(a3==0){
						temp="Abbassata";
					}else{
						temp="Alzata";
						}
				sb.append("--"+a2+" "+a1+": "+temp+"\n");
			}	
			}
			
			answer=sb.toString();
			
			return answer;
		}
		
		
	}
