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

/**
*  Clase de las sondas
*  @author ivanmmurciaua
*/
public class ObjetoRemoto extends UnicastRemoteObject
implements InterfazRemoto, Serializable{ 
	
	private String t_id; //id sonda
	private String t_volumen; //volumen de la sonda
	private String t_fecha; //ultima fecha de modificación de la sonda
	private String t_led; //luz de la sonda 0-36535

	/**
	*  @param id - Id de la sonda
	*  Constructor de la sonda
	*/
	public ObjetoRemoto (String id) throws RemoteException, IOException {
		super();
		
		this.t_id=id;
		String directorio= "";
		String texto;

		File fichero;
		Date fechaActual = new Date();
		DateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
		DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
		
		//Creamos el TXT de la sonda si no existe
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
		//Si no, lo recuperamos
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

	/**
	*  Getter del volumen
	*  @return Volumen del sensor
	*/
	public String volumen(){
		return this.t_volumen;
	}
	
	/**
	*  Getter del led
	*  @return Valor del led del sensor
	*/
	public String luz() {
		return this.t_led;
	}
	
	/**
	*  Getter de la fecha del sistema
	*  @return Fecha actual
	*/
	public String fecha() {
		Date fechaActual = new Date();
        DateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
       	DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        return formatoFecha.format(fechaActual)+" "+formatoHora.format(fechaActual);
	}
	
	/**
	*  Getter de la ultima fecha de modificación del sensor
	*  @return Ultima fecha de modificación del sensor
	*/
	public String ultimaFecha() {
		return this.t_fecha;
	}
	
	/**
	*  Setter de la fecha, utilizado por otros métodos
	*  @return String para saber si ha cambiado
	*/
	public String setfecha() throws UnsupportedEncodingException, IOException{
		Date fechaActual = new Date();
    	DateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
   	 	DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
    	this.t_fecha = formatoFecha.format(fechaActual)+" "+formatoHora.format(fechaActual);
    	if(writeSetters("UltimaFecha=",this.t_fecha)) return "Variable cambiada correctamente en la sonda"+this.t_id;
    	else return "Variable no cambiada";
	}
	
	/**
	*  @param sonda - id de la sonda(no se utiliza, solo para la respuesta)
	*  @param val - valor a cambiar
	*  @return String para saber si ha cambiado
	*  Setter del led de la sonda
	*/
	public String setled(int sonda, String val) throws UnsupportedEncodingException, IOException {
		this.t_led = val;
		setfecha();
		if(writeSetters("Led=",this.t_led)) return "Luz cambiada de la sonda "+sonda+" a "+val;
		else return "Variable no cambiada";
	}
	
	/**
	*  @param sonda - id de la sonda(no se usa, solo para la respuesta)
	*  @param val - valor a cambiar
	*  @return String para saber si ha cambiado
	*  Setter del volumen de la sonda
	*/
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

	/**
	* Método para el index del controlador
	*/
	public String info(){
		return this.t_id+";"+this.t_led+";"+this.t_volumen+";"+this.t_fecha;
	}
	
	/**
	*  @param busca - String a buscar en el fichero
	*  @param valor - Valor a cambiar en el fichero
	*  @return Boolean para saber si se ha escrito bien
	*  Método para escribir en el TXT
	*/
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

