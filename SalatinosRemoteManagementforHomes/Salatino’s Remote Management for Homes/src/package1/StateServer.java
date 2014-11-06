package package1;

import java.io.File;

public class StateServer {
	
	private Alarm alarm;
	private Clime clime;
	private Irrigate irrigate;
	private Lamp lamp;
	private Oven oven;
	private Scene scene;
	private Blind blind;
	
	public StateServer(){
		alarm=new Alarm();
		clime=new Clime();
		irrigate=new Irrigate();
		blind=new Blind();
		lamp=new Lamp();
		oven=new Oven();
		scene=new Scene();
	}
	
	public String serve(){
		
		StringBuffer sb= new StringBuffer();
		if(new File("setup\\alarm.dat").exists()){
			sb.append("Allarme:\n"+alarm.alarmState()+"\n");
		}else{
			sb.append("Allarme:\n"+"520 Errore sulla lettura dell段mpianto\n");
		}
		if(new File("setup\\condizionatori.dat").exists()){
			sb.append("Climatizzatori:\n"+clime.climeState());
		}else{
			sb.append("Climatizzatori:\n"+"520 Errore sulla lettura dell段mpianto");
		}
		if(new File("setup\\irrigazione.dat").exists()){
			sb.append("Irrigazione:\n"+irrigate.irrState());
		}else{
			sb.append("Irrigazione:\n"+"520 Errore sulla lettura dell段mpianto");
		}
		if(new File("setup\\tapparelle.dat").exists()){
			sb.append("Tapparelle:\n"+blind.blindState());
		}else{
			sb.append("Tapparelle:\n"+"520 Errore sulla lettura dell段mpianto");
		}
		if(new File("setup\\luci.dat").exists()){
			sb.append("Lampade:\n"+lamp.lampState());
		}else{
			sb.append("Lampade:\n"+"520 Errore sulla lettura dell段mpianto");
		}
		if(new File("setup\\oven.dat").exists()){
			sb.append("Forno:\n"+oven.ovenState());
		}else{
			sb.append("Forno:\n"+"520 Errore sulla lettura dell段mpianto");
		}
		if(new File("setup\\scene\\list.dat").exists()){
			sb.append("\nScenari:\n"+scene.sceneState());
		}else{
			sb.append("\nScenari:\n"+"520 Errore sulla lettura dell段mpianto");
		}
		return sb.toString();
	}

}
