package package2;

import java.io.*;
import java.util.*;

import package1.genericMethod;

public class setup {
	/**
	 * Attraveso uno switch-case gestisco tutto il setup
	 * 1->Genera password sistema
	 * 2->Setup
	 * 3->Crea Scenari
	 * 4->Distruggi scenari
	 * 5->Distruggi Precedente setup
	 * @param args
	 */

	public static void main(String[] args){
		
		String stringa=null;
		do{
			System.out.println("Setup di sistema:");
		
		
		
		ObjectOutputStream out=null;
		ObjectInputStream in;
		BufferedReader tast=new BufferedReader(new InputStreamReader(System.in));
		try{
		System.out.println("1->Genera password sistema\n2->Setup\n3->Crea Scenari\n4->Distruggi scenari\n5->Distruggi Precedente setup");
		int a=Integer.parseInt(tast.readLine());
		ArrayList<contentFile> arrlst=new ArrayList<contentFile>();
		contentFile[] cf2=null;
		int i=0;
		int flag=0;
		switch(a){
		case 1:
			System.out.println("Inserisci la password del sistema.");
			String pwd=tast.readLine();
			PrintWriter fout=new PrintWriter(new FileWriter("pwd.txt"));
			fout.println(pwd);
			fout.flush();
			fout.close();
			System.out.println("File scritto");
			break;
		case 2:
			ArrayList<contentFile> al=new ArrayList<contentFile>();
			int b=0;
			String tast_in;
			System.out.println("Procedura guidata per il setup.\nNota bene: se vi è stata una precedente configurazione è possibile modificarla\nattraverso la seguente procedura guidata "); 
			if(new File("setup\\stanze.dat").exists()){
				System.out.println("File di configurazione stanze gia presente. Riutilizzarlo? y/n");
				tast_in=tast.readLine();
				if(tast_in.equalsIgnoreCase("y")){
					try{
						in=new ObjectInputStream(new FileInputStream("setup\\stanze.dat"));
						al=(ArrayList<contentFile>)in.readObject();
						in.close();
						b=al.size();
						//il file esiste già, perche gia configurato in precedenza.
						//non è necessario riconfigurare il tutto
						setupCondizionale(al,out,b);
					}catch(ClassNotFoundException e){}
				}else{
					b=scritturaStanze(al,out);
					setupObbligato(al,out,b);
				}
			}else{
				b=scritturaStanze(al,out);
				setupObbligato(al,out,b);
			}
			/*
			 * Setup completato, avviso l'utente.
			 * 
			 */
			System.out.println("****************************\nSetup completato.\n Avviare il server per gestire l'abitazione.");
			
			break;
		
		case 3:
			System.out.println("Modulo di creazione di scenari.");
			String stringa1;
			try{
				    if(!new File("setup\\scene\\list.dat").exists()){
				    	//se il file non esiste quanto meno lo creo
				    	out=new ObjectOutputStream(new FileOutputStream("setup\\scene\\list.dat"));
						out.flush();
						out.close();
				    }
				    
					do{					
						in=new ObjectInputStream(new FileInputStream("setup\\scene\\list.dat"));
					
					
							try{
								arrlst=(ArrayList<contentFile>)in.readObject();	
								
							}catch(IOException e){
								System.out.println("Scenari non presenti");
							}catch(ClassCastException e){
								System.out.println("Scenari non presenti");
							}finally{
								//in.close();
							}
							
							cf2=new contentFile[arrlst.size()+1];//ipotizzo di aggiungere un novo scenario
							//inizializzo
							for(i=0;i<arrlst.size()+1;i++){
								cf2[i]=new contentFile();
								
							}
							
							for(i=0;i<arrlst.size()&& flag==0;i++){
								cf2[i]=arrlst.get(i);
								if(cf2[i].getNum()==-1)flag=1;
							}
							if(flag==1)i--;
					
							System.out.println("Si sta creando lo scenario n°: "+(i+1));
							System.out.println("Inserire il nome da attribuire allo scenario");
							String name=tast.readLine();
							cf2[i].setRoom(name); //scrivo il primo campo della contentFile
							cf2[i].setNum(i);//identificativo dello scenario if(-1) scenario creato in precedenza ma poi cancellato
							cf2[i].setValue(0);//ridondante ma utile per uso futuro
							//la scrittura di questo dato nell'array list lo eseguo dopo.
							
							ArrayList<String> contenuto=new ArrayList<String>();//conterra i comandi dello scenario
							//ArrayList<String> dcontenuto=new ArrayList<String>();//conterra i comandi del controscenario
							System.out.println("Inserisci comando:");
							String command;
							boolean retr=false;//variabile di controllo
							int j=0;
							do{
								command=tast.readLine();
								command=command.toUpperCase();
								//qui si deve kiamare la funzione che controlla il comando
								retr=existCommand(command);
								if(retr){
									//vorra dire ke il comando probabilmente è valido, quindi lo inserisco nell'arraylist di stringhe
									contenuto.add(j, command);
									
									j++;
								}else{
									System.out.println("Comando non valido");
								}
								System.out.println("Inserire un altro comando? y/n");
								stringa1=tast.readLine();
							}while(stringa1.equalsIgnoreCase("y"));
							
							
							// inserire il blocco per la creazione del controscenario
							
							//scrittura nell'arraylist
							if(flag==0)
								arrlst.add(i, cf2[i]); //NEL CASO IN CUI NON ESISTE LO SCENARIO
							else
								arrlst.set(i, cf2[i]); // NEL CASO IN CUI LO SCENARIO ESISTEVA, MA DOVRA SOVRASCRIVERE UNA CELLA IN DISUSO
							
							//scritture varie su file
							out=new ObjectOutputStream(new FileOutputStream("setup\\scene\\scene"+(i+1)));
							out.writeObject(contenuto);
							out.flush();
							out.close();
							
							out=new ObjectOutputStream(new FileOutputStream("setup\\scene\\list.dat"));
							out.writeObject(arrlst);
							out.flush();
							out.close();
					
							flag=0;
							
					System.out.println("Inserire un altro scenario? y/n");
					stringa1=tast.readLine();
				}while(stringa1.equalsIgnoreCase("y"));
			}catch(IOException e){
				System.out.println("File non trovato. Rieseguire il setup.");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			break;
			
		case 4:
			System.out.println("Modulo per l'eliminazione degli scenari.");
			i=0;
			flag=0;
			
			try{
					do{
						System.out.println("Inserisci il numero dello scenario da eliminare.");
							int del = Integer.parseInt(tast.readLine());
						in=new ObjectInputStream(new FileInputStream("setup\\scene\\list.dat"));
					
					try{
						arrlst=(ArrayList<contentFile>)in.readObject();
						
						cf2=new contentFile[arrlst.size()];
						for(i=0;i<arrlst.size()&& flag==0;i++){
							cf2[i]=new contentFile();
							
						}
						for(i=0;i<arrlst.size()&& flag==0;i++){
							cf2[i]=arrlst.get(i);
						}
						
					}catch(IOException e){
						System.out.println("Scenari non presenti");
					}catch(ClassCastException e){
						System.out.println("Scenari non presenti");
					}finally{
						in.close();
					}
					if(del<=arrlst.size()&&del>0){//gli scenari iniziano da 1->infinito
						if(cf2[del-1].getNum()==-1){
							System.out.println("Scenario già eliminato in precedenza");
						}else{
							System.out.println("Si sta eliminando lo scenario n°: "+del);
							
							cf2[del-1].setNum(-1);//impostandolo a -1, implico l'inesistenza dello scenario
							
							//la scrittura di questo dato nell'array list lo eseguo dopo.
							
								arrlst.set(del-1, cf2[del-1]); 
							
							
							
							out=new ObjectOutputStream(new FileOutputStream("setup\\scene\\list.dat"));
							out.writeObject(arrlst);
							out.flush();
							out.close();
							File f=new File("setup\\scene\\scene"+del);
							f.delete();
						}
					}else{
						System.out.println("Scenario non presente.");
					}
					
					
					System.out.println("Eliminare un altro scenario? y/n");
					stringa1=tast.readLine();
				}while(stringa1.equalsIgnoreCase("y"));
			}catch(IOException e){
				System.out.println("File non trovato. Rieseguire il setup.");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			break;
			
		case 5:
			System.out.println("Si sta procedendo con l'eliminazione del precedente setup. Continuare? y/n");
			stringa1=tast.readLine();
			if(stringa1.equalsIgnoreCase("y")){
				File output=new File("setup\\stanze.dat");
				output.delete();
			}
			break;
		}
		System.out.println("Riciclare? Inserici \"quit\" per uscire");
		stringa=tast.readLine();
		}catch(IOException e){
			
		}
		
		}while(!stringa.equalsIgnoreCase("quit"));
	}//close main
	
	/**
	 * Esegue la scrittura del file Stanze
	 *  
	 */
	public static int scritturaStanze(ArrayList<contentFile> al,ObjectOutputStream out ){
						BufferedReader tast=new BufferedReader(new InputStreamReader(System.in));
						System.out.println(" \n Inserisci il numero delle stanze.");
						int stanze = 0;
						try{
							int b=Integer.parseInt(tast.readLine());
							stanze=b;
							contentFile[] cf = new contentFile[b];
							
							contentFile cntfl=new contentFile();		 
							 
							 System.out.println("****************************\nSetup stanze e luoghi.");
							
							
							for(int i=0;i<b;i++){
								cf[i]=new contentFile();
								System.out.println("Inserisci il nome della stanza n° "+(i+1));
								cf[i].setRoom(tast.readLine());
								cf[i].setNum(i+1);
								cf[i].setValue(0);//impongo zero come valore di default
								al.add(i,cf[i]);//carico man mano il mio array list
								
							}
							/*
							 * Stampo quello che ho creato.
							 */
										 
							for(int j=0;j<b;j++){
								cntfl=al.get(j);
								String a1=cntfl.getRoom();
								int a2=cntfl.getNum();
								int a3=cntfl.getValue();
								System.out.println("Stampo: "+a1+a2+a3);
							}
							//scrittura su file
							out=new ObjectOutputStream(new FileOutputStream("setup\\stanze.dat"));
							out.writeObject(al);
							out.flush();
							out.close();
							
						}catch(IOException e){
							
						}
						
						return stanze;
	}
	public static void setupLuci(ArrayList<contentFile> al,ObjectOutputStream out){
		/*
		 * gestisco il file delle luci, assumo che:tutte le stanze abbiano una luce pilotabile e siano inizialmente tutte spente.
		 * Quindi riuso lo stesso array list visto che rispetta le assunzioni fatte
		 * per lo scrivo su un altro file
		 */
		try{
			System.out.println("****************************\nSetup Impianto illuminazione completato.");
			 
			out=new ObjectOutputStream(new FileOutputStream("setup\\luci.dat"));
			out.writeObject(al);
			out.flush();
			out.close();
		}catch(IOException e){}
	}
	
	public static void setupTapparelle(ArrayList<contentFile> al,ObjectOutputStream out, int b ){
		/*
		 * Gestione delle tapparelle.
		 * devo usare necessariamente il vettore di contentFile cosi lo modifico , lo inserisco nello stesso
		 * array list di prima e lo salvo su di un file
		 */
		contentFile[] cf=new contentFile[b];
		for(int i=0;i<b;i++){
			cf[i]=al.get(i);
		}
		System.out.println("****************************\nSetup tapparelle.");
		BufferedReader tast=new BufferedReader(new InputStreamReader(System.in));
		contentFile cntfl=new contentFile();
		try{
				for(int j=0;j<b;j++){
					String a1=cf[j].getRoom();
					System.out.println("Per la stanza: "+a1+" esistono tapparelle da gestire? (y/n)");
					if("n".equalsIgnoreCase(tast.readLine())){
						cf[j].setValue(-1);//se nn esistono tapparelle da gestire imposto a -1(valore immutabile fino ad un prossimo setup.
					}
					al.set(j,cf[j]);
				}
				for(int j=0;j<b;j++){
					cntfl=al.get(j);
					String a1=cntfl.getRoom();
					int a2=cntfl.getNum();
					int a3=cntfl.getValue();
					System.out.println("Stampo: "+a1+a2+a3);
				}
				
				out=new ObjectOutputStream(new FileOutputStream("setup\\tapparelle.dat"));
				out.writeObject(al);
				out.flush();
				out.close();
		}catch(IOException e){
			
		}
		
	}
	
	public static void setupClimatizzatori(ArrayList<contentFile> al,ObjectOutputStream out, int b){
		/*
		 * Gestione climatizzatori.
		 * Qui devo reinizializzare il vettore cf[], in modo tale da assumere che siano tutti spenti,poi di conseguenza 
		 * devo specificarne l'esistenza come è stato fatto per le tapparelle
		 */
		contentFile[] cf=new contentFile[b];
		for(int i=0;i<b;i++){
			cf[i]=al.get(i);
		}
		System.out.println("****************************\nSetup Impianto di condizionamento.");
		BufferedReader tast=new BufferedReader(new InputStreamReader(System.in));
		contentFile cntfl=new contentFile();
		try{
				for(int i=0;i<b;i++){
					cf[i].setValue(0);//reset value, modificato precedentemente
				}
				for(int j=0;j<b;j++){
					String a1=cf[j].getRoom();
					System.out.println("Per la stanza: "+a1+" esistono condizionatori da gestire? (y/n)");
					if("n".equalsIgnoreCase(tast.readLine())){
						cf[j].setValue(-1);//se nn esistono climatizzatori da gestire imposto a -1(valore immutabile fino ad un prossimo setup.
					}
					al.set(j,cf[j]);
				}
				for(int j=0;j<b;j++){
					cntfl=al.get(j);
					String a1=cntfl.getRoom();
					int a2=cntfl.getNum();
					int a3=cntfl.getValue();
					System.out.println("Stampo: "+a1+a2+a3);
				}
				
				out=new ObjectOutputStream(new FileOutputStream("setup\\condizionatori.dat"));
				out.writeObject(al);
				out.flush();
				out.close();
		}catch(IOException e){}
	}
	
	public static int setupIrrigazione(ArrayList<contentFile> al,ObjectOutputStream out){
		/*
		 * Gestione irrigazione.
		 * Inizialmente devo caricare le zone da irrigare con una nuova classe, 
		 * assumo allo stesso tempo che all'inserimento della zona esista "un impianto" inizialmente spento
		 * Questi mi permette in un unico ciclo for di identificare la zona e l'impianto.
		 */
		BufferedReader tast=new BufferedReader(new InputStreamReader(System.in));
		contentFile cntfl=new contentFile();
		int c=0;
		try{
				al.clear();//svuoto l'array list
				
				System.out.println("****************************\nSetup impianto irrigazione.");
				System.out.println("Inserisci il numero delle zone.");
				c=Integer.parseInt(tast.readLine());
				
				contentFile[] cf1=new contentFile[c];
				
				for(int i=0;i<c;i++){
					cf1[i]=new contentFile();
					System.out.println("Inserisci il nome della zona n° "+(i+1));
					cf1[i].setRoom(tast.readLine());
					cf1[i].setNum(i+1);
					cf1[i].setValue(0);//impongo zero come valore di default
					al.add(i,cf1[i]);//carico man mano il mio array list
					
				}
							 
				for(int j=0;j<c;j++){
					cntfl=al.get(j);
					String a1=cntfl.getRoom();
					int a2=cntfl.getNum();
					int a3=cntfl.getValue();
					System.out.println("Stampo: "+a1+a2+a3);
				}
				
				out=new ObjectOutputStream(new FileOutputStream("setup\\irrigazione.dat"));
				out.writeObject(al);
				out.flush();
				out.close();
		}catch(IOException e){}
		return c;
	}
	public static void setupAllarme(ObjectOutputStream out){
		/*
		 * Gestione allarme, devo chiedere all'utente se esiste un impianto di allarme.
		 */
		System.out.println("****************************\nSetup impianto di allarme.");
		System.out.println("Inserisci y/n se vi è un impianto di allarme nell'abitazione.");
		BufferedReader tast=new BufferedReader(new InputStreamReader(System.in));
		int d;
		try{
			if("y".equalsIgnoreCase(tast.readLine())){
				 d=0; //impongo che esiste e si attualmente spento
			}else{
				 d=-1;//impongo che non esiste.
			}
			out=new ObjectOutputStream(new FileOutputStream("setup\\alarm.dat"));
			out.writeInt(d);
			out.flush();
			out.close();
		}catch(IOException e ){}
	}
	
	public static void setupForno(ObjectOutputStream out){
		
		/*
		 * Gestione forno, devo chiedere all'utente se esiste un impianto di allarme.
		 */
		System.out.println("****************************\nSetup della cucina.");
		System.out.println("Inserisci y/n se vi è possibile comandare un forno da esterno.");
		BufferedReader tast=new BufferedReader(new InputStreamReader(System.in));
		int d;
		try{
				if("y".equalsIgnoreCase(tast.readLine())){
					 d=0; //impongo che esiste e si attualmente spento
				}else{
					 d=-1;//impongo che non esiste.
				}
				out=new ObjectOutputStream(new FileOutputStream("setup\\oven.dat"));
				out.writeInt(d);
				out.flush();
				out.close();
		}catch(IOException e){}
		
	}
	/**
	 * Effettuo il setup obbligato qualora non ci fosse gia un'impostazione precedente
	 * @param al
	 * @param out
	 * @param b
	 */
	public static void setupObbligato(ArrayList<contentFile> al,ObjectOutputStream out, int b){
		setupLuci(al,out);
		setupTapparelle(al,out,b);
		setupClimatizzatori(al,out,b);
		setupIrrigazione(al,out);
		setupAllarme(out);
		setupForno(out);
	}
	/**
	 * Setup condizionale qualora si vuole modificare solo una singola parte del sistema
	 * @param al
	 * @param out
	 * @param b
	 */
	public static void setupCondizionale(ArrayList<contentFile> al,ObjectOutputStream out, int b){
		/*
		 * In questa parte del menu avviene tutto il setup dell'abitazione, insirimento di stanze, inserimento dei vari condizionatori, 
		 * zone per l'irrigazione etc. 
		 * 
		 * Instazio un array list in modo tale da semplificarmi la gestione sia per l'uso dei file, sia quando il client invierà i comandi.
		 * 
		 * */
		BufferedReader tast=new BufferedReader(new InputStreamReader(System.in));
		String tast_in;
		try{
			System.out.println("Configurare luci? y/n");
			tast_in=tast.readLine();
			if(tast_in.equalsIgnoreCase("y")){
				setupLuci(al,out);
			}
			System.out.println("Configurare tapperelle? y/n");
			tast_in=tast.readLine();
			if(tast_in.equalsIgnoreCase("y")){
				setupTapparelle(al,out,b);
			}
			System.out.println("Configurare climatizzatori? y/n");
			tast_in=tast.readLine();
			if(tast_in.equalsIgnoreCase("y")){
				setupClimatizzatori(al,out,b);
			}
			System.out.println("Configurare irrigazione? y/n");
			tast_in=tast.readLine();
			if(tast_in.equalsIgnoreCase("y")){
				setupIrrigazione(al,out);
			}
			System.out.println("Configurare allarme? y/n");
			tast_in=tast.readLine();
			if(tast_in.equalsIgnoreCase("y")){
				setupAllarme(out);
			}
			System.out.println("Configurare forno? y/n");
			tast_in=tast.readLine();
			if(tast_in.equalsIgnoreCase("y")){
				setupForno(out);
			}
		}catch(IOException e){}
		
	}
	
	/**
	 * Riduce la probabilita che l'utente inserisca un comando errato
	 * @param mess
	 * @return
	 */
	
	public static boolean existCommand(String mess){
		boolean flag=false,flag2;
		//"DEACT".contains("ACT");perche deact contiene act
		if(mess.contains("CLIME")&&mess.contains("ACT"))flag=true;
		if(mess.contains("LAMP")&&mess.contains("ON"))flag=true;
		if(mess.contains("LAMP")&&mess.contains("OFF"))flag=true;
		if(mess.contains("ALARM")&&mess.contains("ON"))flag=true;
		if(mess.contains("ALARM")&&mess.contains("OFF"))flag=true;
		if(mess.contains("IRR")&&mess.contains("ON"))flag=true;
		if(mess.contains("IRR")&&mess.contains("OFF"))flag=true;
		if(mess.contains("SCENE")&&mess.contains("ACT"))flag=true;
		if(mess.contains("BLIND")&&mess.contains("LOWER"))flag=true;
		if(mess.contains("BLIND")&&mess.contains("RAISE"))flag=true;
		if(mess.contains("OVEN")&&mess.contains("ON"))flag=true;
		if(mess.contains("OVEN")&&mess.contains("OFF"))flag=true;
		/*
		 * NON PERMETTO L'INSERIMENTO DI COMANDI STATE, E DI QUIT OPPURE DI HELP..
		 */
		flag2=containsInt(mess)||mess.contains("ALARM")||mess.contains("OVEN");
		//la precedente istruzione perche alarm e oven non contengono interi nel comando
		
		return flag&&flag2;
	}
	/**
	 * Effettua una scansione del comando ricevuto e controlla se all'interno di esso vi è un intero
	 * @param value
	 * @param file
	 */
	public static boolean containsInt(String mess){
		boolean flag=false;
		for(int i=0;i<mess.length();i++){
			if(mess.charAt(i)>=48 && mess.charAt(i)<=57)flag=true;
		}
		return flag;
	}

}

