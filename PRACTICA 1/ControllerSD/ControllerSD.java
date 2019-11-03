import java.io.*;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.net.*;
import java.lang.reflect.*;

/**
*  Clase Controlador SD
*  @author ivanmmurciaua
*/
public class ControllerSD {
	
	private String RMI_Port; //Puerto Maquina RMI
	private String RMI_IP; //IP Maquina RMI
	
	/**
	*  @param RMI_P  - Puerto RMI pasado por argumentos
	*  @param RMI_ip - IP de la Máquina RMI pasada por argumentos
	*  Constructor de la clase ControllerSD 
	*/
	public ControllerSD(String RMI_P, String RMI_ip) {
		this.RMI_Port = RMI_P;
		this.RMI_IP = RMI_ip;
	}

	/**
	* @param p_sk - Socket usado.
	* @param p_Datos - Cadena para escribir lo que lee.
	* Lee datos del socket. Supone que se le pasa un buffer con hueco 
	*	suficiente para los datos. Devuelve el numero de bytes leidos o
	* 0 si se cierra fichero o -1 si hay error.
	* Lee de HTTP
	*  @return Lo que lee del socket
	*/
	public String leeSocket (Socket p_sk, String p_Datos){
		try{
			InputStream aux = p_sk.getInputStream();
			DataInputStream flujo = new DataInputStream( aux );
			p_Datos = new String();
			p_Datos = flujo.readUTF();
		}
		catch (Exception e){
			System.out.println("Error: " + e.toString());
		}
      return p_Datos;
	}

	/**
	* @param p_sk - Socket usado.
	* @param p_Datos - Cadena a escribir en el socket
	* Escribe dato en el socket cliente. Devuelve numero de bytes escritos,
	* o -1 si hay error.
	* Escribe a HTTPServer 
	*/
	public void escribeSocket (Socket p_sk, String p_Datos){
		try{
			OutputStream aux = p_sk.getOutputStream();
			DataOutputStream flujo= new DataOutputStream( aux );
			flujo.writeUTF(p_Datos);      
		}
		catch (Exception e){
			System.out.println("Error: " + e.toString());
		}
		return;
	}
	
	/**
	*  @param method - Nombre del método remoto
	*  @param val - Valor del posible set, si es get es 0
	*  @param sonda - Numero de sonda
	*  Método para comunicarse con el RMI
	*  @return Respuesta RMI en formato de HTML construido dentro
	*/
	@SuppressWarnings("deprecation")
	public String RMICall(String method, Integer val, Integer sonda) {
		
		String respRMI = "";
		InterfazRemoto objetoRemoto = null;
		//Index
		if((method.toUpperCase()).equals("INDEX") && val == 0 && sonda == -1) {

			respRMI="<HTML>\n" +
			"<head>"
			+"<link href='https://fonts.googleapis.com/css?family=Abel' rel='stylesheet'>\n"
			+ "<style type='text/css'>"
			+ "body{"
			+ "background-color: AliceBlue;"
			+ "}"
			+ " a{ font-family: 'Abel', cursive; font-size: 1.2em; }"
			+ "</style>\n"+
			"</head>\n" +
			"<body>\n";
			
			try{
				final Registry reg = LocateRegistry.getRegistry(this.RMI_IP,Registry.REGISTRY_PORT);
				final String[] remoteObjNames = reg.list();
				
				for(String remObj : remoteObjNames) {
					try {
						Object obj = reg.lookup(remObj);
						if(obj instanceof InterfazRemoto) {
							final InterfazRemoto server = (InterfazRemoto)obj;
							String[] infos = (server.info()).split(";");
							
							if(Integer.parseInt(infos[1])<21845) infos[1]="LawnGreen";
							else if(Integer.parseInt(infos[1])>21846 && Integer.parseInt(infos[1])<43690) infos[1]="yellow";
							else infos[1]="red";
							
							respRMI += "<p><div id=led style='width:25px; height:25px; background-color:"+infos[1]+"; border-radius: 100% ; border: solid 1.5px;'></div> <br><a> El sensor "+infos[0]+" tiene un volumen de un "+infos[2]+"% y se modifico por ultima vez el "+infos[3]+"</a></p>\n";
						}
					}catch(Exception ex) {}
					
				}
			}catch(Exception ex) {}
			
			/*
			LOCURA MUY LOCA, ESTO NO VA
			while(encontrado) {
				String server = "rmi://"+this.RMI_IP+":"+this.RMI_Port+"/ObjetoRemoto"+i;
				
				try{
					System.setSecurityManager(new RMISecurityManager());
					objetoRemoto = (InterfazRemoto) Naming.lookup(server);
					respRMI += "<p>"+objetoRemoto.info()+"</p>\n";
					objetoRemoto = null;
					i++;
					
				}catch(Exception ex) {
					encontrado=false;
					objetoRemoto = null;
				}
			}
			*/
			
			respRMI += "</body>\n" +
					"</HTML>";
		}
		else {
			String server = "rmi://"+this.RMI_IP+":"+this.RMI_Port+"/ObjetoRemoto"+sonda;
			
			try{	
				System.setSecurityManager(new RMISecurityManager());
				objetoRemoto = (InterfazRemoto) Naming.lookup(server);
				
				
				if(val == 0 && sonda != -1) {
					//GETTERS
					switch(method){
							case "volumen": {
									respRMI = "<HTML>\n" +
			                            			"<head><link href='https://fonts.googleapis.com/css?family=Abel' rel='stylesheet'><style type='text/css'> body{background-color: AliceBlue;} a{font-size: 2.2em; font-family: 'Abel', cursive;} </style>\n" +
			                            			"</head>\n" +
			                            			"<body>\n" +
			                            			"<a>La sonda "+sonda+" tiene un volumen de "+objetoRemoto.volumen()+"</a>\n" +
			                            			"</body>\n" +
			                            			"</HTML>";
							}
							break;
							case "fecha": {
									respRMI = "<HTML>\n" +
											"<head><style type='text/css'> body{background-color: AliceBlue;} a{font-size: 2.2em; font-family: 'Abel', cursive;} </style>\n" +
											"</head>\n" +
											"<body>\n" +
											"<a>"+objetoRemoto.fecha()+"</a>\n" +
											"</body>\n" +
											"</HTML>";
							}
							break;
							case "ultimafecha": {
								      respRMI = "<HTML>\n" +
			                            				"<head><link href='https://fonts.googleapis.com/css?family=Abel' rel='stylesheet'><style type='text/css'> body{background-color: AliceBlue;} a{font-size: 2.2em; font-family: 'Abel', cursive;} </style>\n" +
			                            				"</head>\n" +
			                           				"<body>\n" +
			                           	 			"<a>La sonda "+sonda+" tiene se modifico por ultima vez en "+objetoRemoto.ultimaFecha()+"</a>\n" +
			                            				"</body>\n" +
			                            				"</HTML>";
							}
							break;
							case "luz": {
								String l=objetoRemoto.luz();
									if(Integer.parseInt(l)<21845) l="LawnGreen";
									else if(Integer.parseInt(l)>21846 && Integer.parseInt(l)<43690) l="yellow";
									else l="red";
								
								      respRMI = "<HTML>\n" +
			                            				"<head><link href='https://fonts.googleapis.com/css?family=Abel' rel='stylesheet'><style type='text/css'> body{background-color: AliceBlue;} a{font-size: 2.2em; font-family: 'Abel', cursive;} </style>\n" +
			                            				"</head>\n" +
			                            				"<body>\n" +
			                            				"<div id=luz style='width:500px; height:500px; background-color: "+l+"'></div>\n" +
			                            				"</body>\n" +
			                            				"</HTML>";
							}
							break;
							default: respRMI = "<HTML>\n" +
			                            			"<head><link href='https://fonts.googleapis.com/css?family=Abel' rel='stylesheet'><style type='text/css'> body{background-color: AliceBlue;} a{font-size: 2.2em; font-family: 'Abel', cursive;} </style>\n" +
			                            			"</head>\n" +
			                            			"<body>\n" +
			                            			"<a>No existe ese metodo</a>\n" +
			                            			"</body>\n" +
			                            			"</HTML>";
							break;
					}
				}
				else {
					//SETTERS
					switch(method){
						case "setvolumen": {
								respRMI = "<HTML>\n" +
		                            			"<head><link href='https://fonts.googleapis.com/css?family=Abel' rel='stylesheet'><style type='text/css'> body{background-color: AliceBlue;} a{font-size: 2.2em; font-family: 'Abel', cursive;} </style>\n" +
		                            			"</head>\n" +
		                            			"<body>\n" +
		                            			"<a>"+objetoRemoto.setvolumen(sonda,val.toString())+"</a>\n" +
		                            			"</body>\n" +
		                            			"</HTML>";
						}
						break;
						case "setled": {
								respRMI = "<HTML>\n" +
										"<head><link href='https://fonts.googleapis.com/css?family=Abel' rel='stylesheet'><style type='text/css'> body{background-color: AliceBlue;} a{font-size: 2.2em; font-family: 'Abel', cursive;} </style>\n" +
										"</head>\n" +
										"<body>\n" +
										"<a>"+objetoRemoto.setled(sonda,val.toString())+"</a>\n" +
										"</body>\n" +
										"</HTML>";
						}
						break;
						default: respRMI = "<HTML>\n" +
		                            			"<head><link href='https://fonts.googleapis.com/css?family=Abel' rel='stylesheet'><style type='text/css'> body{background-color: AliceBlue;} a{font-size: 2.2em; font-family: 'Abel', cursive;} </style>\n" +
		                            			"</head>\n" +
		                            			"<body>\n" +
		                            			"<a>No existe ese metodo</a>\n" +
		                            			"</body>\n" +
		                            			"</HTML>";
						break;
					}
				}
	        }
	        catch(Exception ex){
	            System.out.println("Error al instanciar el objeto remoto "+ex);
	            respRMI = "<HTML>\n" +
	            		  "<head><link href='https://fonts.googleapis.com/css?family=Abel' rel='stylesheet'><style type='text/css'> body{background-color: AliceBlue;} a{font-size: 2.2em; font-family: 'Abel', cursive;} </style>\n" +
	                      "</head>\n" +
	                      "<body>\n" +
	                      "<a>Sonda no encontrada</a>\n" +
	                      "</body>\n" +
	                      "</HTML>";
	            	//System.exit(0);
	        }
		}
		return respRMI;
	}
	/**
	* @param Cadena - Cadena procedente del servidor HTTP 
	* Método que separa la cadena que le llega del HTTPServer
	* @return Cadena a responder en HTTP
	*/
	public String controllerRunning(String Cadena) {
		System.out.println(Cadena);
		//if(Cadena=="") Cadena = "index.html";
		if(Cadena.contains("?")){
			//Separamos por ?
			String[] a = Cadena.split("\\?");
			
			//SI le falta alguna de las dos sentencias
			if(a.length<2) {   Cadena+="faltaalgo"; return Cadena;   }
			else {
				String method=""; 
				Integer val=0; Integer valsonda=0;  
				
				for(int i=0; i<a.length;i++) {
					
					if(i==0 && !a[i].contains("=")) {   method=a[i];  }
					else {
						//Dividimos las partes en 2
						String[] b = a[i].split("=");
						
						//Si estamos en la parte del método y de su valor
						if(i==0 && b.length>1) {   method = b[0];  val=Integer.parseInt(b[1]);   }
						else {
							//Si la cadena no es sonda, decimos que es imposible, sino, le asignamos un numero a esa sonda
							if(!(b[0].toUpperCase()).equals("SONDA")) {   Cadena+="estonoesunasonda"; return Cadena;  }
							else {   valsonda= Integer.parseInt(b[1]);   }
						}
					}
				}
				
				//Aquí ya tendríamos todos los datos de la sonda y su metodo y sus valores
				System.out.println(method);System.out.println(val);System.out.println(valsonda);
				Cadena = RMICall(method,val,valsonda);
			}			
		}
		else{
			if(Cadena.equals("index.html")) {
				Cadena = RMICall("index",0,-1);
			}
			else {
				Cadena = "";
			}
		}
		return Cadena;
	}
	
	/**
	 * @param args
	 * Main que inicia el Controlador
	 */
	public static void main(String[] args) {
		
		int resultado=0;
		String Cadena="";
		String puerto="";

		try{
			if (args.length < 3) {
				System.out.println("Debe indicar el puerto de escucha del servidor");
				System.out.println("$./Servidor puerto_servidor IP_RMIServer puerto_RMIServer");
				System.exit (1);
			}
			
			puerto = args[0];
			// Creamos un objeto Controlador
			ControllerSD sr = new ControllerSD(args[2],args[1]);
			ServerSocket skServidor = new ServerSocket(Integer.parseInt(puerto));
		    System.out.println("Escucho el puerto " + puerto);
	
			/*
			* Mantenemos la comunicacion con el cliente
			*/	
			for(;;){
				/*
				* Se espera un cliente que quiera conectarse
				*/
				Socket skCliente = skServidor.accept(); // Crea objeto
		        System.out.println("Sirviendo cliente...");
				
				Cadena = sr.leeSocket (skCliente, Cadena);
				//System.out.println(Cadena);
				Cadena=sr.controllerRunning(Cadena);
				//Escribimos respuesta
				sr.escribeSocket (skCliente, Cadena);
						
			}
		}
		catch(Exception e){
			System.out.println("Error: " + e.toString());
		}
	}
}