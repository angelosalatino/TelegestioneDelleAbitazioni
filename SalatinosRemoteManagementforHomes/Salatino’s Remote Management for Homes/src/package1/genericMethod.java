package package1;

import java.io.*;
import java.util.*;

import package2.contentFile;



public class genericMethod {
	/**
	 * Metodo che riceve l'array list, con il nome del file ed effettua operazioni di scrittura
	 * 
	 */
	public static void writeOnFile(ArrayList<contentFile> al, String file){
		try{
			ObjectOutputStream out=new ObjectOutputStream(new FileOutputStream("setup\\"+file));
			out.writeObject(al);
			out.flush();
			out.close();
		}catch(IOException e){
			
		}
		
	}
	/**
	 * Metodo che riceve in ingresso una stringa e con dovute operazioni di lettura restituisce un arraylist
	 * @param file
	 * @return
	 */
	
	public static ArrayList<contentFile> readOnFile(String file){
		ArrayList<contentFile> al=new ArrayList<contentFile>();
		try{
			ObjectInputStream fin=new ObjectInputStream(new FileInputStream("setup\\"+file));
			al=(ArrayList<contentFile>)fin.readObject();
			fin.close();
		}catch(IOException e){
			
		} catch (ClassNotFoundException e) {
		
		}
		return al;
	}
	/**
	 * Effettua una semplice scrittura su file. Riceve come parametri il dato e il titolo del file
	 * @param value
	 * @param file
	 */
	public static void simpleWriteOnFile(int value, String file){
		try{
			ObjectOutputStream out=new ObjectOutputStream(new FileOutputStream("setup\\"+file));
			out.writeInt(value);
			out.flush();
			out.close();
		}catch(IOException e){
			
		}
		
	}
	
	/**
	 * Effettua una semplice lettura da file. Riceve come parametri il titolo del file
	 * @param value
	 * @param file
	 */
	
	public static int simpleReadOnFile(String file){
		int value=0;
		try{
			ObjectInputStream fin=new ObjectInputStream(new FileInputStream("setup\\"+file));
			value=fin.readInt();
			fin.close();
		}catch(IOException e){
			
		}
		return value;
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
	
	/**
	 * Restituisce l'intero presente all'interno del comando
	 * @param value
	 * @param file
	 */
	public static int returnInt(String mess){
		int value;
		int a=0,b=0,c=0;
		for(int i=0;i<mess.length()&& c==0;i++){
			if(mess.charAt(i)>=48 && mess.charAt(i)<=57){
				if(a>0){
					b++;
				}else{
					a=i;
					b=i+1;
					}
			}else{
				if(a>0)c=1;
			}
		}
		value=Integer.parseInt(mess.substring(a, b));
		return value;
	}
	
	
	
	

}
