/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * @author Angelo Antonio Salatino n° matr 541532
 * Cdl di Ingegneria Informatica L3
 * Telematica 2
 *
 * Progetto di fine anno.
 * Telegestione delle abitazioni.
 *
 */

package package4;

/*
 * Importo le classi utili: una per il midlet e una per il display
 */
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

import java.io.*;
import javax.microedition.io.*;
import java.security.*;


/**
 * @author Angelo
 */
public class PalmClient extends MIDlet implements CommandListener {
    private TextField ipOrHostname = new TextField(" Inserisci indirizzo IP oppure l'hostname","",40,TextField.ANY);
    private TextField command = new TextField(" Inserisci comando ","",20,TextField.ANY);
    private TextField password = new TextField(" Inserire password per connettersi al server","",16,TextField.ANY);
    private Form  firstForm = new Form(" Connection ");
    private Form  secondForm = new Form(" Password ");
    private Form  thirdForm = new Form(" Request ");
    private Form  fourthForm = new Form(" Response ");
    private Form fifthForm = new Form(" Quit ");
    private Command esci = new Command(" Esci ",Command.EXIT,1);
    private Command invia = new Command(" Invia ",Command.ITEM,2);
    private Command inviaPass = new Command(" Invia ",Command.ITEM,2);
    private Command inviaCommand = new Command(" Invia ",Command.ITEM,2);
    private Command indietro = new Command(" Indietro ",Command.ITEM,2);
    private Display display;
    private static final int PORT=2718;
    private int status;
    private String hostname;
    private SocketConnection sc;
    private InputStream is;
    private OutputStream os;
    private boolean login;
    private int count;



    /**
     * Metodo che distrugge l'applicazione
     * @param unconditional
     */
    public void destroyApp(boolean unconditional) {
        notifyDestroyed();
    }
    /**
     * Inserisce le varie componenti nei form
     */
    public void startApp() {
        display = Display.getDisplay(this);
        firstForm.append(ipOrHostname);
        firstForm.append(password);
        firstForm.addCommand(esci);
        firstForm.addCommand(invia);
        firstForm.setCommandListener(this); //ascolta la pressione dei comandi
        thirdForm.append(command);
        thirdForm.addCommand(esci);
        thirdForm.addCommand(inviaCommand);
        thirdForm.setCommandListener(this);
        fourthForm.addCommand(indietro);
        fourthForm.setCommandListener(this);
        fifthForm.addCommand(esci);
        fifthForm.setCommandListener(this);
        display.setCurrent(firstForm);
        status=0;
        count=0;
    }
    /**
     * Legge il comando ed agisce in base alle istruzioni date
     * @param c
     * @param d
     */
    public void commandAction (Command c, Displayable d) {

        if(c==invia){
                hostname=ipOrHostname.getString();
             try{
                 
               
                sc = (SocketConnection)Connector.open("socket://"+hostname+":"+PORT);
               
                os = sc.openOutputStream();
                    is = sc.openInputStream();
                    
                    /*
                     * Comunico con il server di servizio
                     */

                    SocketConnection sc1= (SocketConnection)Connector.open("socket://"+hostname+":"+7777);
                       OutputStream os1 = sc1.openOutputStream();
                        InputStream is1 = sc1.openInputStream();
                        String pass=password.getString();
                        os1.write((pass+"#").getBytes());
                        os1.flush();
                        os1.close();
                         int ch=0;
                             StringBuffer sb = new StringBuffer();
                            while ((ch = is1.read()) != -1 & ch != 35) {
                               //System.out.print((char) ch);
                                sb.append((char) ch + "");
                            }
                            String md5=sb.toString();
                            is1.close();
                       sc1.close();

                      //ora comunico con il server principale
                            md5=md5+"#";
                            os.write(md5.getBytes());
                            os.flush();

                            ch=0;
                            sb.delete(0, sb.length());
                                while ((ch = is.read()) != -1 & ch != 35) {
                                    //System.out.print((char) ch);
                                    sb.append((char) ch + "");
                                }




                         if(count<3){
                             fourthForm.append("Connessione avvenuta con: "+hostname+":"+PORT);
                            if(sb.toString().startsWith("230")){
                                login=true;
                                status=2;
                                fourthForm.append(sb+"");
                            }else{
                                 closeConn();
                                status=1;
                                count++;
                                fourthForm.append(sb.toString().substring(0, sb.length()-1)+(3-count));
                            }                       
                            display.setCurrent(fourthForm);
                         }else{
                               closeConn();
                                fifthForm.append("Connessione avvenuta con: "+hostname+":"+PORT);
                               fifthForm.append(sb.toString().substring(0, sb.length()-1)+"0");
                               fifthForm.append("Connessione in chiusura, perche si sono superati il numero massimo di tentativi");
                               display.setCurrent(fifthForm);
                         }
                   
             }catch (IOException ex) {
                Alert alert = new Alert("Error", ex.toString(), null, null);
                 alert.setTimeout(Alert.FOREVER);
                 alert.setType(AlertType.ERROR);
                 display.setCurrent(alert);
            }finally{
                   
                }
        }else{

              if(c==inviaPass){


                   
                    
                }else{
        
                        if(c==indietro){
                            
                            if(status==2){
                                display.setCurrent(thirdForm);
                                fourthForm.deleteAll();
                            }else{
                                if(status==1){
                                display.setCurrent(firstForm);
                                fourthForm.deleteAll();
                            }else{

                            }
                            }
                        }else{

                                if(c==inviaCommand){
                                    try {
                                        String comando;
                                        StringBuffer sb = null;
                                    
                                        comando=command.getString();
                                        comando=comando.toUpperCase();
                                        comando=comando+"#";
                                         //System.out.println(comando);
                                        byte[] data = comando.getBytes();
                                        os.write(data);
                                        os.flush();
                                         //System.out.println(data);
                                        //os.close();
                                        int ch=0;
                                        sb = new StringBuffer();
                                        while ((ch = is.read()) != -1 & ch != 35) {
                                            //System.out.print((char) ch);
                                            sb.append((char) ch + "");
                                        }
                                        //System.out.println(sb);
                                    
                                        if(comando.startsWith("QUIT")){
                                            closeConn();
                                            fifthForm.append(sb+"");
                                                display.setCurrent(fifthForm);
                                        }else{
                                                fourthForm.append(sb+"");
                                                display.setCurrent(fourthForm);
                                        }

                                        if(login){
                                            status=2;
                                        }
                                    } catch (IOException ex) {
                                        Alert alert = new Alert("Error", ex.toString(), null, null);
                                         alert.setTimeout(Alert.FOREVER);
                                         alert.setType(AlertType.ERROR);
                                         display.setCurrent(alert);
                                    }


                                }else{

                                    if(c==esci){
                                        destroyApp(true);
                                      }
                                }
                        }
                }
        }
    }//chiude commandAction

    public void pauseApp() {
    }
    /**
     * Metodo che consente la chiusura della connessione. Chiude stream e socket.
     */
    public void closeConn(){
        try {
            os.close();
            is.close();
            sc.close();
        } catch (IOException ex) {
            Alert alert = new Alert("Error", ex.toString(), null, null);
                 alert.setTimeout(Alert.FOREVER);
                 alert.setType(AlertType.ERROR);
                 display.setCurrent(alert);
        }
    }

}
