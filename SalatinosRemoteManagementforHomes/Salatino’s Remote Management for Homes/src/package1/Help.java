package package1;

import java.io.*;

public class Help {
	
	public String serve(){
		StringBuffer content = new StringBuffer();  // serve per gestire una stringa con i metodi di StringBuffer
        String answer = null,line = "";
        if(new File("setup\\condizionatori.dat").exists()){
				try{
					BufferedReader reader = new BufferedReader(new FileReader("HELP.txt")); 
			        do {    // Legge il file un rigo alla volta
			            line = reader.readLine();    // legge il rigo fino al \n senza inglobare il carattere \n stesso
			            if (line != null) {
			                content.append(line + "\n");  // metodo per appendere il rigo alla stringa content
			                //content.append("\n");
			            }
			        } while (line != null);
			        reader.close();
			        answer=content.toString();
				}catch(IOException e){
					
				}
				
        }else{
			answer="510 Errore del server, non è possibile gestire il comando";
		}
		return answer;
	}

}
