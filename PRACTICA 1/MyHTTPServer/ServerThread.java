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

public class ServerThread extends Thread{
	
	private final Socket skClient;
        private final String ip_controller;
        private final Integer puerto_controller;
        
	
	public ServerThread(Socket p_client, String ip_c, Integer p_controller){
		this.skClient = p_client;
                this.ip_controller = ip_c;
                this.puerto_controller = p_controller;
	}
	
	/*
	* Lee datos del socket. Supone que se le pasa un buffer con hueco 
	*	suficiente para los datos. Devuelve el numero de bytes leidos o
	* 0 si se cierra fichero o -1 si hay error.
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
        
        /*
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
           
            return htmll.replace("null","");
            
        }

	public String realizarOperacion(String p_Cadena) throws UnsupportedEncodingException, IOException{
		System.out.println("Pensando a donde enviar");
		String httpResponse = "";
		String[] cabecera = p_Cadena.split(" ");
		
		if(!(cabecera[0].toUpperCase()).equals("GET")) {
                    httpResponse = cabecera[2]+" 405 Method Now Allowed\r\n\r\n";
                }
                else {
                    if(cabecera[1].equals("/") || cabecera[1].equals("/index.html")) {
        		httpResponse = cabecera[2]+" 200 OK\r\n\r\n";
			//for(int i=0;i<cabecera.length;i++) { System.out.println(cabecera[i]);}
                        httpResponse += readFile("content/index.html");
                    }
                    else if((cabecera[1].toUpperCase()).contains("CONTROLADORSD")){
                        String[] controlador = cabecera[1].split("/");
        		if((controlador[1].toUpperCase()).equals("CONTROLADORSD")) {
                                Socket pk_controller = new Socket(this.ip_controller,this.puerto_controller);
                                String responseController="";
        			for(int i=2;i<controlador.length;i++) { 
        				System.out.println(controlador[i]);
                                        this.writeSocket(pk_controller, controlador[i], true);
                                        String s = "";
                                        responseController += this.readSocket(pk_controller, s, true);
                                }
                                if(responseController.equals("")){
                                    httpResponse = cabecera[2]+" 404 Not Found\r\n\r\n";
                                    httpResponse += readFile("content/error404.html");
                                }
                                else{
                                    httpResponse = cabecera[2]+" 200 OK\r\n\r\n";
                                    httpResponse += responseController;
                                }
        		}
                        else {
                            httpResponse = cabecera[2]+" 404 Not Found\r\n\r\n";
                            httpResponse += readFile("content/error404.html");
        		}
                    }
                    else {
                        String res =readFile("content"+cabecera[1]); 
                        if(res.equals("")){
                            httpResponse = cabecera[2]+" 404 Not Found\r\n\r\n";
                            httpResponse += readFile("content/error404.html");
                        }
                        else{
                            httpResponse = cabecera[2]+" 202 OK\r\n\r\n";
                            httpResponse += res;
                        }
        		
                    }
                }
		
		return httpResponse;
	}
	
	public void run() {
            String Cadena="";
            System.out.println("RUNNING");
		
            try {

                    Cadena = this.readSocket(this.skClient, Cadena,false);
                    System.out.println(this.skClient.toString());
                    /*
                    * Se escribe en pantalla la informacion que se ha recibido del
                    * cliente
                    */
                    Cadena = this.realizarOperacion(Cadena);
                    this.writeSocket(this.skClient,Cadena,false);
                    this.skClient.close();

            }
            catch (Exception e) {
              System.out.println("Error: " + e.toString());
            }
       }

}

