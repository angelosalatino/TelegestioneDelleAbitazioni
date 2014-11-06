package package1;

import java.net.*;
import java.security.*;
import java.io.*;

public class MD5 extends Thread {
	private ServerSocket ss;
	private int PORT=7777;
	private boolean running;
	
	
	public MD5(){
		
	}
	
	public void run(){
		try {
			ss = new ServerSocket(PORT);	
			System.out.println("Server secondario in ascolto sulla porta: " + PORT);
			running = true;
		} catch(IOException ioe) {
			System.out.println("Impossibile accedere alla porta "+PORT);
		}
		
		while (running) {
			serve(); 		
		}
		
		try {
			ss.close();		
			System.out.println("Server secondario non in ascolto");
		} catch(IOException ioe) {
			System.out.println("Anomalia server secondario: Impossibile rilasciare la porta");
			System.exit(1);			// uscita dal programma con codice di errore 1
		}
		
		
	}
	
	public void serve(){
		Socket link = null;		
		try{
			link = ss.accept();					
			BufferedReader in = new BufferedReader(new InputStreamReader(link.getInputStream()));
			
			PrintWriter out = new PrintWriter(link.getOutputStream(),true);
			InetAddress clientAddr = link.getInetAddress();
			int clientPort = link.getPort();
			//System.out.println("Connessione aperta con il client TCP "+ clientAddr + ":" + clientPort);

						
			StringBuffer sb = new StringBuffer(); 
			int ch = 0; 

			while ((ch = in.read()) != -1 & ch != 35){
			sb.append((char)ch);
			}
			
			
			//System.out.println("Ricevuto: " + sb);
			String coded=code(sb.toString());
				
			System.out.println(coded);
			out.println(coded+"#");
			out.flush();
			out.close();
			in.close();
		} catch(IOException e) {
			e.printStackTrace();
			// qualsiasi eccesione di tipo IOException provoca l'arresto del server
		} finally { 
			try{
				//System.out.println("Chiusura connessione.");
				
				link.close();				// Passo 5 --> chiusura del socket
				//running=false;  //chiusura prematura del socket
			} catch(IOException e) {
				//System.out.println("Impossibile disconnettersi.");
				// provoca l'arresto del server
			}
		}
		
	}
	
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
	
	public void stopServer() {
		running = false;			
	}
	
	

}
