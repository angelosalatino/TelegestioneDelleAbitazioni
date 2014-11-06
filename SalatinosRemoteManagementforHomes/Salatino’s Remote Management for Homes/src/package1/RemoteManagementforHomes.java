package package1;
/**
 * @author Angelo Antonio Salatino n° matr 541532
 * Cdl di Ingegneria Informatica L3
 * Telematica 2
 * 
 * Progetto di fine anno. 
 * Telegestione delle abitazioni.
 * 
 */
import java.net.*;
import java.util.Date;
import java.io.*;

public class RemoteManagementforHomes{
	
	
	private int port;							// semi-associazione lato server port+IP
	private static final int PORT = 2718;
	private ServerSocket servSocket;				// socket del server
	private static boolean running;
	private int numThread;

	

	
	/*
	 * Dichiarazione e definizione dei metodi
	 */
	public RemoteManagementforHomes(){
		
		}
	/**
	 * Costruttore che inizializza tutti i modi della classe
	 * @param port
	 */
	public RemoteManagementforHomes(int port){
		this.port=port;
		running=false;
		servSocket=null;
		numThread=0;
		
		
		}
	/**
	 * Metodo chiamato dal main subito dopo aver istanziato un oggetto della medesima classe
	 * esso copre tutta l'azione del server, lo attiva e lo mette in ascolto sulla porta 2718.
	 * Finche il server resta in ascolto esso chiamerà il metodo serve()
	 */
	public void start(){
		try{
		servSocket=new ServerSocket(port);
		System.out.println("Server in ascolto sulla porta: "+port);
		
		Date d= new Date();
		try{
			PrintWriter fout=new PrintWriter(new FileWriter("logfile.txt",true));
			fout.println("Server in ascolto sulla porta: "+port+" -> "+d);
			fout.flush();
			fout.close();
		}catch(IOException e){
			
		}
		/* *********************
		 * Chiamo il secondo server
		 */
		MD5 md5=new MD5();
		md5.start();
		
		running=true;
		}catch(IOException e){
			System.out.println("Impossibile accedere alla porta "+port);
		}
		
		while(running)serve();
		
		try {
            servSocket.close();       
            System.out.println("Server non in ascolto");
            Date d= new Date();
    		
    			PrintWriter fout=new PrintWriter(new FileWriter("logfile.txt",true));
    			fout.println("Server non in ascolto -> "+d);
    			fout.flush();
    			fout.close();
    		
        } catch(IOException ioe) {
            System.out.println("Impossibile rilasciare la porta");
            System.exit(1);
        }
	}
	
	/**
	 * Questo metodo ha come scopo quello di accettare le nuove connessioni in ingresso e avviare nuovi thread
	 */
	public void serve(){
		Socket link=null;
		
		
		
		if(running){
			try{
				
					link=servSocket.accept();
				
					System.out.println("/*******************************************************************/");
					System.out.println("Connessione stabilita con: "+link.getInetAddress()+":"+link.getPort());
						//scrittura sul log file
					PrintWriter fout=new PrintWriter(new FileWriter("logfile.txt",true));
					Date d= new Date();
					fout.println("Connessione stabilita con: "+link.getInetAddress()+":"+link.getPort()+" -> "+d);
					fout.flush();
					fout.close();
				
							
				}catch(IOException e){
					System.out.println("Impossibile accettare la connessione in ingresso");
		            stop();	
				}finally{
				
					System.out.println("Nuovo client accettato.");
					String nameThread=link.getInetAddress()+":"+link.getPort();
					Thread ss=new Thread(new ServeServer(port,link,nameThread));
					ss.start();
					
				}
		}
	}
	
	/**
	 * questo metodo impedisce al server di rimanere in ascolto su quella porta.
	 */
	public void stop(){
		running=false;
		
		
	}
	
	
	
	/*
	 * Main del sever
	 */

	public static void main(String[] args) {
		if(new File("setup\\stanze.dat").exists() && new File("pwd.txt").exists()){
		RemoteManagementforHomes rmfh=new RemoteManagementforHomes(PORT);
		rmfh.start();
		}else{
			System.out.println("Errore di sistema: Password non impostata oppure setup non presente!");
		}
	}

}
