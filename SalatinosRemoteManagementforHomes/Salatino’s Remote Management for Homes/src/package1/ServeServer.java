package package1;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class ServeServer implements Runnable {
	
	private int port;							
	private Socket link;				// socket del server
	private boolean login;
	private boolean closesrv;
	private String nameThread;
	private BufferedReader in;
	private PrintWriter out;
	/*Instanziamento di oggetti vari*/
	private Alarm alarm;
	private Clime clime;
	private Irrigate irrigate;
	private Lamp lamp;
	private Oven oven;
	private Scene scene;
	private Blind blind;
	private Help help;
	private StateServer stateserver;
	/*-------------------------------*/
	
	/*
	 * Dichiarazione e definizione dei metodi
	 */
	/**
	 * Costruttore che inizializza tutti gli attributi della classe, utile ad eliminare le ricorsività
	 * 
	 */
	public ServeServer(){
		alarm=new Alarm();
		clime=new Clime();
		irrigate=new Irrigate();
		blind=new Blind();
		lamp=new Lamp();
		oven=new Oven();
		
		
		
	}
	/**
	 * Costruttore primario chiamato per la maggior parte dei casi
	 * @param port
	 * @param link
	 * @param myThread
	 */
	public ServeServer(int port, Socket link, String myThread){
		
		this.port=port;
		login=false;
		closesrv=false;
		this.link=link;
		alarm=new Alarm();
		clime=new Clime();
		irrigate=new Irrigate();
		blind=new Blind();
		scene=new Scene();
		lamp=new Lamp();
		oven=new Oven();
		help= new Help();
		setName(myThread);//imposto il nome del thread;
		stateserver=new StateServer();
		}
	
/**
 * Metodo che mette in attività il thread
 */
	public void run(){
		try{
			System.out.println("Attivo il thread che si occuppa della connessione stabilita tra: "+link.getInetAddress()+":"+link.getPort());
			System.out.println("e "+link.getLocalAddress()+":"+link.getLocalPort());
			System.out.println("/*******************************************************************/");
			in= new BufferedReader(new InputStreamReader(link.getInputStream()));
			out= new PrintWriter(link.getOutputStream(),true);
			
			//gestione login
			login(in,out);
			//gestione comandi
			serverun();
			
			try {
				System.out.println("Connessione in chiusura con "+link.getLocalAddress()+":"+link.getLocalPort());
				link.close();							
				
			} catch(IOException ioe) {
				System.out.println("Impossibile rilasciare la connessione");
				System.exit(1);
			
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Questo metodo svolge la funzione di interpretare in parte il comando ricevuto e di delegare altri metodi per espletare le richieste
	 * @param messaggio
	 * @return
	 */
	public void serverun(){
	try{
		String message, answer;
		
		while(login){
			
			//codice per leggere lo stream
			StringBuffer sb = new StringBuffer(); 
			int ch = 0; 

			while ((ch = in.read()) != -1 & ch != 35 & ch!=42){ //35=#, 42=*
			sb.append((char)ch);
			//System.out.print((char) ch);
			}
			if(sb.charAt(0)==13 && sb.charAt(1)==10){
				message=sb.substring(2);
			}else{
				message=sb.toString();	
			}
			
	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
			System.out.println("CLIENT "+getName()+">: Comando ricevuto: " + message);
			
							answer=commands(message);
							
			//invio sul stream d'uscita uno delle possibili risposte ottenute dal server.				
							String temp;
							int i,a;
							i=0;
							try{
									PrintWriter fout=new PrintWriter(new FileWriter("logfile.txt",true));
									do{							
											a=answer.indexOf("\n",i);
											if(a>i){
												temp=answer.substring(i,a);
											}else{
												temp=answer.substring(i);
												a=answer.length()-1;
											}
											out.println(temp);
											i=a+1;
											System.out.println("\tCLIENT "+getName()+">: Risposta inviata: " + temp);
											fout.println("\tCLIENT "+getName()+">: Risposta inviata: " + temp);
											fout.flush();
										}while(i<answer.length());
										String answer1="#";
									out.println(answer1);
								fout.close();
								}catch(IOException e){
									System.out.println("Impossibile usare il logfile");
								}
					
					if(closesrv)System.exit(1);//NEL CASO IN CUI VIENE INVOCATO IL COMANDO: CLOSESRV

		
			
			
		}
		
	}catch(IOException e){
		e.printStackTrace();
	}catch(NullPointerException npe){
		npe.printStackTrace();
	}finally{
		
	}
	}
	
	public String commands(String messaggio){
		//metodo che consente di gestire i comandi in entrata.
		int k=-1;
		String answer = null;
		
		try{
			PrintWriter fout=new PrintWriter(new FileWriter("logfile.txt",true));
			fout.println("\tCLIENT "+getName()+">: Comando ricevuto: " + messaggio);
			fout.flush();
			fout.close();
		}catch(IOException e){
			System.out.println("Impossibile usare il logfile");
		}
		try{
			
				if(messaggio.startsWith("HELP"))k=0;
					if(messaggio.startsWith("ALARM"))k=1;
						if(messaggio.startsWith("CLIME"))k=2;
							if(messaggio.startsWith("IRR"))k=3;
								if(messaggio.startsWith("LAMP"))k=4;
									if(messaggio.startsWith("OVEN"))k=5;
										if(messaggio.startsWith("SCENE"))k=6;
											if(messaggio.startsWith("BLIND"))k=7;
												if(messaggio.startsWith("STATE"))k=8;
													if(messaggio.startsWith("QUIT"))k=9;
														if(messaggio.startsWith("CLOSESRV"))k=10;
		//switch-case
				if(k>=0){										
					switch(k){
							case 0:
								answer=help.serve();
								break;
								
							case 1:
								answer=alarm.serve(messaggio);
								break;
								
							case 2:
								answer=clime.serve(messaggio);
								break;
								
							case 3:
								answer=irrigate.serve(messaggio);
								break;
								
							case 4:
								answer=lamp.serve(messaggio);
								break;
								
							case 5:
								answer=oven.serve(messaggio);
								break;
								
							case 6:
								answer=scene.serve(messaggio);
								break;
								
							case 7:
								answer=blind.serve(messaggio);
								break;
								
							case 8:
								answer=stateserver.serve();
								break;
								
							case 9:
								logout();
								answer="Logout effettauto.";
								break;
								
							case 10:
								shutdown();
								answer="Server chiuso. Riattivarlo in loco.";
								break;
					}		
				}else{
					answer="400 Comando sconosciuto";
					}
		}catch(NullPointerException e){
			System.out.println("Catturato un null pointer");
		}
	
		
		return answer;
		
	}
	/**
	 * Questo metodo si preoccupa di loggare il client appena connesso, deve controllare che la password sia corretta in modo tale da consentirne la completa accessibilità al sistema
	 * @param in
	 * @param out
	 */
	public void login(BufferedReader in, PrintWriter out){
		//Apre il file legge la stringa 
		//la passa al membro code, che esegue la codifica in md5
		//nel while la confronta se è esatta!
		String pass, answer;
		int i=0;
		try{
			BufferedReader f=new BufferedReader(new FileReader("pwd.txt"));
			pass=f.readLine();
			f.close();
			
			pass=code(pass);
			
				while(!login&&i<3){
					StringBuffer sb = new StringBuffer(); 
					int ch = 0; 

					while ((ch = in.read()) != -1 & ch != 35 & ch!=42){ //35=#, 42=*
					sb.append((char)ch);
					//System.out.print((char) ch);
					}
					String pwd;
					if(sb.charAt(0)==13 && sb.charAt(1)==10){
						pwd=sb.substring(2);
					}else{
						pwd=sb.toString();	
					}
					
					
					if(pwd.equals(pass)){
						login=true;
						answer="230 Password accettata#";
						System.out.println("CLIENT "+getName()+">: PWD accettata, quindi è attivo il dialogo.");
						
					}else {
						i++;
						answer="430 Password non accettata, reinserire. Tentativi rimasti "+(3-i)+"#";
						if(sb.charAt(sb.length()-1)==35){//indica le condizioni di una connessione j2me
							link.close();
						}
						System.out.println("CLIENT "+getName()+">: PWD non accettata");
					}
					if(i>=3)answer="La password è stata inserita incoretta per tre volta. Connessione in chiusura#";
					out.println(answer);
				}
				
				
		}catch(IOException e){
			
		}catch(StringIndexOutOfBoundsException ex){
			System.out.println("Close");
		}

		
	}
	/**
	 * Quando un client decide di effettuare il logout questo metodo espleta tale funzione.
	 */
	public void logout(){
		login=false;
		try{
			PrintWriter fout=new PrintWriter(new FileWriter("logfile.txt",true));
			Date d= new Date();
			fout.println("Disconnesso da: "+link.getInetAddress()+":"+link.getPort()+" -> "+d);
			fout.flush();
			fout.close();
		}catch(IOException e){
			
		}
	}
	/**
	 * Qualora per questioni di sicurezza, si decide di spegnere il server, attraverso il comando CLOSESRV, verra chiamato questo metodo
	 */
	public void shutdown(){
		logout();
		RemoteManagementforHomes rmft = new RemoteManagementforHomes();
		rmft.stop();
		Date d= new Date();
		try{
			PrintWriter fout=new PrintWriter(new FileWriter("logfile.txt",true));
			fout.println("Server non in ascolto ->"+d);
			fout.flush();
			fout.close();
		}catch(IOException e){
			
		}
		closesrv=true;
		System.out.println("Server in chiusura");
	}
	/**
	 * Metodo che provvede alla codifica in MD5 della password conservata in un file sul server.
	 */
	public String code(String pass){
		
		//metodo per la codifica della password secondo lo standard predente nel file word.
		try{
			MessageDigest md= java.security.MessageDigest.getInstance("MD5");
			md.reset();
			md.update(pass.getBytes());
			byte[] md5=md.digest();
			
			StringBuffer hexs=new StringBuffer();
			for(int i=0;i<md5.length;i++){
				hexs.append(Integer.toHexString(0xFF & md5[i]));
			}
			
			pass=hexs.toString();
			}catch(NoSuchAlgorithmException nsae){
			
			}
	
		
		return pass;
	}
	/**
	 * Metodo che setta il nome al thread attraverso la stringa passata al metodo
	 * @param name
	 */
	public void setName(String name){
		this.nameThread=name;
	}
	/**
	 * Restituisce il nome del Thread
	 * @return
	 */
	public String getName(){
		return this.nameThread;
	}
	
}

