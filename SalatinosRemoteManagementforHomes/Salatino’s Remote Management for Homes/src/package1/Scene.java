package package1;

import java.io.*;
import java.util.*;

import package2.contentFile;



public class Scene {
	ArrayList<String> content;
	ArrayList<contentFile> al;
	contentFile cf;
	ServeServer rmfh;
	
	public Scene(){
		content=new ArrayList<String>();
		cf=new contentFile();
		rmfh=new ServeServer();
	}
	
	public String serve(String mess){
		String answer=null;
		if(new File("setup\\scene\\list.dat").exists()){
			if(mess.contains("STATE")){
				answer=sceneState();
			}else{
				if(genericMethod.containsInt(mess)){
					if(mess.contains("SEE")){
						answer=sceneSee(mess);
					}else{
						answer=sceneAct(mess);
					}
				}else{
						answer="401 Sintassi del comando errata";
					}
				}
		}else{
			answer="510 Errore del server, non è possibile gestire il comando";
		}
		return answer;
	}
	
	public String sceneAct(String mess){
		String answer=null;
		al=genericMethod.readOnFile("scene\\list.dat");
		if(genericMethod.containsInt(mess)){
					int b=genericMethod.returnInt(mess)-1;//-1 perche nell'inserimento delle stanze non è stato considerato lo Zero
					if(b<0||b>al.size()||al.get(b).getNum()<0){
						answer="410 Comando non accettato: Scenario non presente.";
					}else{
							cf=al.get(b);
							
						if(cf.getNum()!=-1){//CONDIZIONE DI ESISTENZA DELLO SCENARIO
							
											answer=serveScene("scene\\scene"+(b+1));
											answer=answer.concat("Scenario attivato.");
							
						}else{
							answer="410 Comando non accettato: Scenario non presente.";
						}
					}
				}else{
					answer="401 Sintassi del comando errata.";
				}
		return answer;
		
	}
	
	public String sceneSee(String mess){
		String answer=null;
		al=genericMethod.readOnFile("scene\\list.dat");
		if(genericMethod.containsInt(mess)){
			int b=genericMethod.returnInt(mess)-1;
				if(b<0||b>al.size()||al.get(b).getNum()<0){
					answer="410 Comando non accettato: Scenario non presente.";
				}else{
						cf=al.get(b);
						
						if(cf.getNum()!=-1){//CONDIZIONE DI ESISTENZA DELLO SCENARIO
						
							try{
								ObjectInputStream in=new ObjectInputStream(new FileInputStream("setup\\scene\\scene"+(b+1)));
								content=(ArrayList<String>)in.readObject();
								in.close();
								StringBuffer sb=new StringBuffer();
								for(int i=0;i<content.size();i++){
									sb.append(content.get(i)+"\n");
								}
								answer=sb.toString();
								
							}catch(IOException e){
								
							}catch(ClassNotFoundException e){
								e.printStackTrace();
							}
								
							answer=answer.concat("Termine lista.");
								
							
						}else{
							answer="410 Comando non accettato: Scenario non presente.";
						}
				}
		}else{
			answer="410 Sintassi del comando errata.";
		}
		return answer;
		
	}

	public String sceneState(){
		
		String answer=null;
		String temp=null;
		al=genericMethod.readOnFile("scene\\list.dat");
		StringBuffer sb=new StringBuffer();
		int j=0;//mi serve a contare quanti scenari ci sono
		for(int i=0;i<al.size();i++){
			cf=al.get(i);
			String a1=cf.getRoom();//contiene il nome
			int a2=cf.getNum();//contiene -1 se non esiste e valori >0 come id
			
				if(a2!=-1)
					j++;
					sb.append("--"+(a2+1)+" "+a1+"\n");			
				}
		if(j==0)sb.append("\tScenari non presenti\n");
		answer=sb.toString();
		
		
		return answer;
	
	}
	
	public String serveScene(String mess){
		String answer=null;
		try{
			ObjectInputStream in=new ObjectInputStream(new FileInputStream("setup\\"+mess));
			content=(ArrayList<String>)in.readObject();
			in.close();
			StringBuffer sb=new StringBuffer();
			for(int i=0;i<content.size();i++){
				sb.append(rmfh.commands(content.get(i))+"\n");
			}
			answer=sb.toString();
			
		}catch(IOException e){
			
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		
		return answer;
	}

}
