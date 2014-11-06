package package3;
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
import java.io.*;
import java.security.*;

public class ClientRMFH {

		private InetAddress servAddress;
		private int port;
		private static final int PORT = 2718;
		private boolean login;//variabile che mi indica se il login è stato effettuato
		
		
		
		//costruttore		
		public ClientRMFH(InetAddress servAddress, int port){
						
			this.servAddress = servAddress;
			this.port = port;
			login=false;
			
		}
		
		/**
		 * Questo metodo connette il client al server, richiede all'utente la pass, la fa codificare al metodo pass e 
		 * la invia al server per l'autenticazione(max 3 tentativi)
		 */
		
		public void connect(){
			Socket link=null;
			BufferedReader tast=new BufferedReader(new InputStreamReader(System.in));
			
			try{
				link=new Socket(servAddress,port);
				System.out.println("Connessione stabilita con: "+link.getInetAddress()+":"+link.getPort());
				BufferedReader in=new BufferedReader(new InputStreamReader(link.getInputStream()));
				PrintWriter out= new PrintWriter(link.getOutputStream(),true);
				int i=0;
				
			do{	
				System.out.println("Inserisci la password per connettersi al server:");
				String pass=tast.readLine();
				i++;
				//decodifico la password in MD5
				pass=code(pass);
				out.println(pass+"*");
				String answer=in.readLine();
				System.out.println("SERVER>: "+answer.substring(0, answer.length()-1));
				
				if(answer.startsWith("230"))login();//setto la variabile flag login a true
					
				}while(!login&&i<3);
			
			
			//chiamo il membro che si occupa di acquisire i comandi e inviarli
					while(login)serve(in,out);
					
			}catch(IOException e){
				
			}finally{
				try {
					link.close();							
					System.out.println("Connessione chiusa");
				} catch(IOException ioe) {
					System.out.println("Impossibile rilasciare la connessione");
					System.exit(1);
				
				}
			}
		}
		/**
		 * Questo metodo si occupa dell'intero dialogo tra client e server, ovvero acquisione dei comandi da tastiera, invio, ricezione e stampa della risposta
		 * 
		 */
		public void serve(BufferedReader in, PrintWriter out){
			//metodo che consente di inserire un comando per volta, inviato il comando e ricevuta
			// la risposta esso terminera, pero poi verra chiamato nuovamento per un nuovo inserimento 
			// finche on->false
			
			BufferedReader tast=new BufferedReader(new InputStreamReader(System.in));
			try{
				System.out.println("Inserisci comando");
				String messaggio=tast.readLine();
				messaggio=messaggio.toUpperCase();//nel caso in cui si voglia inserire il comando in minuscolo
				out.println(messaggio+"*");//aggiungo carattere speciale per la fine trasmissione 
				String answer;
				/*
				 * Per la risposta mi avvalgo delle seguenti istruzioni perchè il server puoi inviare piu di una risposta 
				 * per volta. si veda il comando state
				 */
				do{
					answer=in.readLine();
				if(!answer.equals("#")){
					System.out.println("SERVER>: "+answer);
					}
				}while(!answer.equals("#"));
				/*
				 * 
				 */
				if(messaggio.startsWith("QUIT")||messaggio.startsWith("CLOSE"))logout();//imposta il login=false; ed evita che si richiami la serve
				
			}catch(Exception e){
				
			}
			
		}
		
		/**
		 * Questo metodo riceve in ingresso la stringa pass, la codifica in md5 e la restituisce al metodo chiamante
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
			
			//pass=hexs.toString();
			pass=new String(hexs);
			}catch(NoSuchAlgorithmException nsae){
				
			}
		
			
			return pass;
		}
		/**
		 * Metodo che effettua il logout
		 */
		public void logout(){
			login=false;
			System.out.println("Logout Effettuato");
		}
		/**
		 * Metodo che effettua il login
		 */
		public void login(){
			login=true;
			System.out.println("Login Effettuato");
		}
	

	
	public static void main(String[] args) {
		
		BufferedReader tast=new BufferedReader(new InputStreamReader(System.in));
		String message;
		InetAddress ina;
		try{
		System.out.println("Programma di Telegestione per le Abitazioni.\nInserisci indirizzo ip, oppure l'hostname del server.");
		message=tast.readLine();
		ina=InetAddress.getByName(message);
		
		ClientRMFH pc=new ClientRMFH(ina, PORT);
		
		pc.connect();
		
		}catch(UnknownHostException e) {
			System.out.println("Indirizzo del server non trovato!");
			e.printStackTrace();
		}catch(IOException ioe){
			System.out.println("Stringa non acquisita correttamente");
			
		}
		

	}

}
