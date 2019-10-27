import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.rmi.*;
import java.rmi.server.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ObjetoRemoto extends UnicastRemoteObject
implements InterfazRemoto, Serializable{ 
	
	private String t_id;
	private String t_volumen;
	private String t_fecha;
	private String t_led;

	
	public ObjetoRemoto (String id) throws RemoteException, IOException {
		super();
		
		this.t_id=id;
		String directorio= "";
		String texto;

		File fichero;
		Date fechaActual = new Date();
		DateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
		DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
		
		directorio="./sensor"+id+".txt";
		fichero = new File(directorio);
		if (!fichero.exists()) {
		    this.t_volumen="0";
		    this.t_fecha=this.fecha();
		    this.t_led="0";
		    FileWriter escribir=new FileWriter(fichero,true);      
		    escribir.write("Volumen="+this.t_volumen+"\nUltimaFecha="+this.t_fecha+"\nLed="+this.t_led);
		    escribir.close();
		}
		else {
		    FileReader lector=new FileReader(directorio);
		    BufferedReader contenido=new BufferedReader(lector);
		    int i=0;
		    while((texto=contenido.readLine())!=null){
		        if (i==0)
		            this.t_volumen=texto.split("=")[1];
		        else if (i==1)
		            this.t_fecha=texto.split("=")[1];
		        else if (i==2)
		            this.t_led=texto.split("=")[1];
		        i++;
		    }
		}
	}

	
	public String volumen(){
		return this.t_volumen;
	}
	
	public String luz() {
		return this.t_led;
	}
	
	public String fecha() {
		Date fechaActual = new Date();
        DateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
       	DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        return formatoFecha.format(fechaActual)+" "+formatoHora.format(fechaActual);
	}
	
	public String ultimaFecha() {
		return this.t_fecha;
	}
	
	public String setfecha() throws UnsupportedEncodingException, IOException{
		Date fechaActual = new Date();
    	DateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
   	 	DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
    	this.t_fecha = formatoFecha.format(fechaActual)+" "+formatoHora.format(fechaActual);
    	if(writeSetters("UltimaFecha=",this.t_fecha)) return "Variable cambiada correctamente en la sonda"+this.t_id;
    	else return "Variable no cambiada";
	}
	
	public String setled(int sonda, String val) throws UnsupportedEncodingException, IOException {
		this.t_led = val;
		setfecha();
		if(writeSetters("Led=",this.t_led)) return "Luz cambiada de la sonda "+sonda+" a "+val;
		else return "Variable no cambiada";
	}
	
	public String setvolumen(int sonda, String val) throws UnsupportedEncodingException, IOException {
		if(Integer.parseInt(val)>100) {   val = "100";   }
		if(Integer.parseInt(val)<0) {   val = "0";   }
		this.t_volumen = val;
		setfecha();
		if(Integer.parseInt(val)>=30) {  setled(sonda,"50000");   }
		else {  setled(sonda,"0");   }
		if(writeSetters("Volumen=",this.t_volumen)) return "Volumen cambiado de la sonda "+sonda+" a "+val;
		else return "Variable no cambiada";
	}

	public String info(){
		return this.t_id+";"+this.t_led+";"+this.t_volumen+";"+this.t_fecha;
	}
	
	private boolean writeSetters(String busca, String valor) throws UnsupportedEncodingException, IOException{
		File sensoor = new File("./sensor"+this.t_id+".txt");
		try(BufferedReader br = new BufferedReader(new FileReader("./sensor"+this.t_id+".txt"))){
			String sensorfalso = "./sensorfalso"+this.t_id+".txt";
			File falso = new File(sensorfalso);
			FileWriter escribir = new FileWriter(falso);
			StringBuilder sb = new StringBuilder();
			String fich = br.readLine();

			while(fich != null){
				if(fich.contains(busca)){
					escribir.write(busca+valor+"\n");
				}
				else {
					escribir.write(fich+"\n");
				}
				fich = br.readLine();
			}
			sensoor.delete();
			falso.renameTo(sensoor);
			escribir.close();
			return true;
		}
		catch(Exception ex) { return false; }
	}

}

