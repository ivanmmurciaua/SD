import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Serializable; 
import java.rmi.*;
import java.rmi.server.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ObjetoRemoto extends UnicastRemoteObject
implements InterfazRemoto, Serializable{ 
	
	private Integer t_volumen;
	private String t_fecha;
	private Integer t_led;
	String lectura[]; 
	
	public ObjetoRemoto () throws RemoteException {
		super();
		this.t_fecha = "19/10/2019 11:11:11";
		this.t_led = 3984;
		this.t_volumen = 834;
		this.lectura = new String[3];
		for(int i=0;i<3;i++) this.lectura[i] = "1";
	}
	
	private Boolean leerFichi(int sonda) {
		try(BufferedReader br = new BufferedReader(new FileReader("./sensor"+sonda+".txt"))) {
			Integer fich=0;
			StringBuilder sb = new StringBuilder();
			String lecfich = br.readLine();
			while(lecfich != null) {
				this.lectura[fich] = lecfich.split("=")[1];
				lecfich = br.readLine();
				fich++;
			}
			return true;
		}catch(Exception ex) {System.out.println(ex); return false;}
	}
	
	public String volumen(int sonda){
		//System.out.println(leerFichi(sonda));
		if(leerFichi(sonda)) return this.lectura[0];
		else return "";
	}
	
	public String luz(int sonda) {
		if(leerFichi(sonda)) return this.lectura[2];
		else return "";
	}
	
	public String fecha() {
		Date fechaActual = new Date();
        DateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
        DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        return formatoFecha.format(fechaActual)+" "+formatoHora.format(fechaActual);
	}
	
	public String ultimaFecha(int sonda) {
		if(leerFichi(sonda)) return this.lectura[1];
		else return "";
	}

}

