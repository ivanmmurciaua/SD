import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
/**
*  Clase HiloServidor que usa Thread(Java)
*  @author ivanmmurciaua
*/
public class ServerThread extends Thread{
	
	private final Socket skClient; //Socket hacia el navegador
    private final String ip_controller; //IP de la máquina ControllerSD
    private final Integer puerto_controller; //Puerto de la máquina ControllerSD
        
	/**
    *  @param p_client - Socket hacia el navegador
    *  @param ip_c - IP de la máquina ControllerSD
    *  @param p_controller - Puerto del ControllerSD
    *  Constructor de la clase
    */
	public ServerThread(Socket p_client, String ip_c, Integer p_controller){
		this.skClient = p_client;
        this.ip_controller = ip_c;
        this.puerto_controller = p_controller;
	}
	
	/**
    * @param p_sk - Socket a leer
    * @param p_Data - Cadena donde escribir lo que lee
    * @param x - Booleano para diferenciar HTTP(0) y cadena normal controlador(1)
	* Lee datos del socket. Supone que se le pasa un buffer con hueco 
	*	suficiente para los datos. Devuelve el numero de bytes leidos o
	* 0 si se cierra fichero o -1 si hay error.
	* @return Cadena leida
	*/
	public String readSocket (Socket p_sk, String p_Data, Boolean x){
		try{
            if(x){
                System.out.println("Leo Controller");
                InputStream aux = p_sk.getInputStream();
                DataInputStream line = new DataInputStream(aux);
                p_Data = new String();
		        p_Data = line.readUTF();
            }
            else{
                System.out.println("Leo HTTP");
                InputStreamReader isr =  new InputStreamReader(this.skClient.getInputStream());
                BufferedReader reader = new BufferedReader(isr);
                String line = reader.readLine();
                p_Data = new String();
	            p_Data = line;
            }
		}
		catch (Exception e){
			System.out.println("Error: " + e.toString());
		}
        return p_Data;
	}
        
    /**
    * @param p_sk - Socket donde va a escribir
    * @param p_Data - Cadena a escribir en el socket
    * @param x - Booleano para diferenciar HTTP(0) y cadena normal controlador(1)
	* Escribe dato en el socket cliente. Devuelve numero de bytes escritos,
	* o -1 si hay error.
	*/
	public void writeSocket(Socket p_sk, String p_Data, Boolean x){
            
		try{
            if(x){
                System.out.println("Escribo Controller");
                OutputStream aux = p_sk.getOutputStream();
	            DataOutputStream flujo = new DataOutputStream(aux);
	            flujo.writeUTF(p_Data);
            }
            else{
                System.out.println("Escribo HTTP");
                p_sk.getOutputStream().write(p_Data.getBytes("UTF-8"));
            }
        }			
		catch (Exception e){
			System.out.println("Error: " + e.toString());
		}
		return;
	}
    /**
    *  @param file - Nombre del archivo a leer
    *  Método que lee un archivo y devuelve su lectura
    *  @return Fichero leido
    */
    public String readFile(String file) throws UnsupportedEncodingException, IOException{
        String htmll = "";
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
		    StringBuilder sb = new StringBuilder();
		    htmll = br.readLine();
                    
		    while (htmll != null) { 
		        sb.append(htmll);
		        sb.append(System.lineSeparator());
		        htmll = br.readLine();
            }
		    
            htmll += sb.toString();            
        }
        catch(Exception ex) {
            htmll = "";
        }

        //Problemilla con un null travieso
        return htmll.replace("null","");  
    }

    /**
    *  @param p_Cadena - Cabecera leida del navegador
    *  Método que se encarga de hacer el control de toda la cadena y comunicarse con el controlador
    *  @return Cadena a escribir de respuesta en el HTTP
    */
	public String realizarOperacion(String p_Cadena) throws UnsupportedEncodingException, IOException{

		System.out.println("Pensando a donde enviar");
		String httpResponse = "";
		String[] cabecera = p_Cadena.split(" ");
		//Método distinto de GET
		if(!(cabecera[0].toUpperCase()).equals("GET")) {
            httpResponse = cabecera[2]+" 405 Method Now Allowed\r\n\r\n";
        }
        else {
            //INDEX
            if(cabecera[1].equals("/") || cabecera[1].equals("/index.html")) {
		      httpResponse = cabecera[2]+" 200 OK\r\n\r\n";
              httpResponse += readFile("content/index.html");
            }
            else if((cabecera[1].toUpperCase()).contains("CONTROLADORSD")){
                //Separamos la cadena que sigue a controladorSD
                String[] controlador = cabecera[1].split("/");
		        if((controlador[1].toUpperCase()).equals("CONTROLADORSD")) {
                    try(Socket pk_controller = new Socket(this.ip_controller,this.puerto_controller)){
                        String responseController="";
                        //Le vamos pasando la cadena al controlador
                        for(int i=2;i<controlador.length;i++) { 
                            System.out.println(controlador[i]);
                            this.writeSocket(pk_controller, controlador[i], true);
                            String s = "";
                            responseController += this.readSocket(pk_controller, s, true);
                        }
                        //No responde nada el controlador
                        if(responseController.equals("")){
                            httpResponse = cabecera[2]+" 404 Not Found\r\n\r\n";
                            httpResponse += readFile("content/error404.html");
                        }
                        else{
                            httpResponse = cabecera[2]+" 200 OK\r\n\r\n";
                            httpResponse += responseController;
                        }
                    }catch(Exception ex){
                        httpResponse = cabecera[2]+" 409 Controller Not Found\r\n\r\n";
                        httpResponse += readFile("content/error409.html");
                    }        
		        }
                //Le pasas controladorSD y no le dices nada? OK, pos 404 pa ti
                else {
                    httpResponse = cabecera[2]+" 404 Not Found\r\n\r\n";
                    httpResponse += readFile("content/error404.html");
		        }
            }
            //Recurso estático
            else {
                String res =readFile("content"+cabecera[1]); 
                if(res.equals("")){
                    httpResponse = cabecera[2]+" 404 Not Found\r\n\r\n";
                    httpResponse += readFile("content/error404.html");
                }
                //Recurso estático devuelto correctamente
                else{
                    httpResponse = cabecera[2]+" 202 OK\r\n\r\n";
                    httpResponse += res;
                }
            }
        }
		
		return httpResponse;
	}
	
    /**
    *  Método para ejecutar el hilo
    */
	public void run() {

        String Cadena="";
        System.out.println("RUNNING");
        try {
            Cadena = this.readSocket(this.skClient, Cadena,false);
            System.out.println(this.skClient.toString());
            //Realizamos cadena
            Cadena = this.realizarOperacion(Cadena);
            this.writeSocket(this.skClient,Cadena,false);
            //Cortamos socket
            this.skClient.close();

        }
        catch (Exception e) {
          System.out.println("Error: " + e.toString());
        }
    }
}