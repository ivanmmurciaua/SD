import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
*  Clase de las sondas
*  @author ivanmmurciaua
*/
public class Sensor{ 
	
	private String t_id; //id sonda
	private String t_volumen; //volumen de la sonda
	private String t_fecha; //ultima fecha de modificación de la sonda
	private String t_led; //luz de la sonda 0-36535
	
	public int iniciar(String id, String usuario, String contrasenya, String ip) throws IOException,UnsupportedEncodingException {
		String directorio= "";

		File fichero;
		String volumen;
		String fecha;
		String led;
		
		//Creamos el TXT de la sonda si no existe
		directorio="C:\\Users\\IVAN\\eclipse-workspace\\Sensor\\src\\sensor"+id+".txt";
		fichero = new File(directorio);
		if (!fichero.exists()) {
		    volumen="0";
		    fecha=this.fecha();
		    led="0";
		    FileWriter escribir=new FileWriter(fichero,true);      
		    escribir.write("Volumen="+volumen+"\r\nUltimaFecha="+fecha+"\r\nLed="+led);
		    escribir.close();
		}
		return 1;
	}
	
	public String leer(String busca, String id) throws IOException,UnsupportedEncodingException{
		String sloots="";
		
		try(BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\IVAN\\eclipse-workspace\\Sensor\\src\\sensor"+id+".txt"))){
			String fich = br.readLine();
			while(fich != null){
				if(fich.contains(busca)){
					sloots = fich.split("=")[1];
				}
				else {}
				fich = br.readLine();
			}
		}
		catch(Exception ex) { return ""; }
		
		return sloots;
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
	*  Getter del volumen
	*  @return Volumen del sensor
	*/
	public String volumen(String id) throws IOException,UnsupportedEncodingException{
		
		return leer("Volumen=",id);
	}
	
	/**
	*  Getter del led
	*  @return Valor del led del sensor
	*/
	public String luz(String id) throws IOException,UnsupportedEncodingException{
		return leer("Led=",id);
	}
	
	/**
	*  Getter de la ultima fecha de modificación del sensor
	*  @return Ultima fecha de modificación del sensor
	*/
	public String ultimaFecha(String id) throws IOException,UnsupportedEncodingException{
		return leer("UltimaFecha=",id);
	}
	
	/**
	*  Setter de la fecha, utilizado por otros métodos
	*  @return String para saber si ha cambiado
	*/
	public String setfecha(int sonda) throws UnsupportedEncodingException, IOException{
		Date fechaActual = new Date();
    	DateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
   	 	DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
    	String val = formatoFecha.format(fechaActual)+" "+formatoHora.format(fechaActual);
    	if(writeSetters("UltimaFecha=",val,sonda)) return "Variable cambiada correctamente";
    	else return "Variable no cambiada";
	}
	
	/**
	*  @param sonda - id de la sonda(no se utiliza, solo para la respuesta)
	*  @param val - valor a cambiar
	*  @return String para saber si ha cambiado
	*  Setter del led de la sonda
	*/
	public String setled(int sonda, String val) throws UnsupportedEncodingException, IOException {
		System.out.println(setfecha(sonda));
		if(writeSetters("Led=",val,sonda)) return "Luz led cambiada de la sonda "+sonda+" a "+val;
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
		setfecha(sonda);
		if(Integer.parseInt(val)>=30) {  setled(sonda,"50000");   }
		else {  setled(sonda,"0");   }
		if(writeSetters("Volumen=",val,sonda)) return "Volumen cambiado de la sonda "+sonda+" a "+val;
		else return "Variable no cambiada";
	}
	
	/**
	*  @param busca - String a buscar en el fichero
	*  @param valor - Valor a cambiar en el fichero
	*  @return Boolean para saber si se ha escrito bien
	*  Método para escribir en el TXT
	*/
	private boolean writeSetters(String busca, String valor, int ids) throws UnsupportedEncodingException, IOException{
		File sensoor = new File("C:\\Users\\IVAN\\eclipse-workspace\\Sensor\\src\\sensor"+ids+".txt");
		try(BufferedReader br = new BufferedReader(new FileReader("./sensor"+ids+".txt"))){
			String sensorfalso = "C:\\Users\\IVAN\\eclipse-workspace\\Sensor\\src\\sensor"+ids+".txt";
			
			File falso = new File(sensorfalso);
			FileWriter escribir = new FileWriter(falso);
			StringBuilder sb = new StringBuilder();
			String fich = br.readLine();

			while(fich != null){
				if(fich.contains(busca)){
					escribir.write(busca+valor+"\r\n");
				}
				else {
					escribir.write(fich+"\r\n");
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

