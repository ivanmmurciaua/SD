import java.io.*;
import java.rmi.*;
import java.net.*;
import java.lang.reflect.*;

public class ControllerSD {
	
	private String RMI_Port;
	private String RMI_IP;
	
	public ControllerSD(String RMI_P, String RMI_ip) {
		this.RMI_Port = RMI_P;
		this.RMI_IP = RMI_ip;
	}

	/*
	* Lee datos del socket. Supone que se le pasa un buffer con hueco 
	*	suficiente para los datos. Devuelve el numero de bytes leidos o
	* 0 si se cierra fichero o -1 si hay error.
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

	/*
	* Escribe dato en el socket cliente. Devuelve numero de bytes escritos,
	* o -1 si hay error.
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
	
	@SuppressWarnings("deprecation")
	public String RMICall(String method, Integer val, Integer sonda) {
		
		String respRMI = "";
		InterfazRemoto objetoRemoto = null;
		if((method.toUpperCase()).equals("INDEX") && val == 0 && sonda == -1) {
			int i=1;
			boolean encontrado = true;
			respRMI="<HTML>\n" +
			"<head><style type='text/css'> body{background-color: AliceBlue;} </style>\n" +
			"</head>\n" +
			"<body>\n";
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
			
			respRMI += "</body>\n" +
					"</HTML>";

		}
		else {
			String server = "rmi://"+this.RMI_IP+":"+this.RMI_Port+"/ObjetoRemoto"+sonda;
			
			try{	
				System.setSecurityManager(new RMISecurityManager());
				objetoRemoto = (InterfazRemoto) Naming.lookup(server);
				
				
				if(val == 0 && sonda != -1) {
					switch(method){
							case "volumen": {
									respRMI = "<HTML>\n" +
			                            			"<head><style type='text/css'> body{background-color: AliceBlue;} </style>\n" +
			                            			"</head>\n" +
			                            			"<body>\n" +
			                            			"<a>La sonda "+sonda+" tiene un volumen de "+objetoRemoto.volumen()+"</a>\n" +
			                            			"</body>\n" +
			                            			"</HTML>";
							}
							break;
							case "fecha": {
									respRMI = "<HTML>\n" +
											"<head><style type='text/css'> body{background-color: AliceBlue;} </style>\n" +
											"</head>\n" +
											"<body>\n" +
											"<a>"+objetoRemoto.fecha()+"</a>\n" +
											"</body>\n" +
											"</HTML>";
							}
							break;
							case "ultimafecha": {
								      respRMI = "<HTML>\n" +
			                            				"<head><style type='text/css'> body{background-color: AliceBlue;} </style>\n" +
			                            				"</head>\n" +
			                           				"<body>\n" +
			                           	 			"<a>La sonda "+sonda+" tiene se modifico por ultima vez en "+objetoRemoto.ultimaFecha()+"</a>\n" +
			                            				"</body>\n" +
			                            				"</HTML>";
							}
							break;
							case "luz": {
								String l=objetoRemoto.luz();
									if(Integer.parseInt(l)<21845) l="black";
									else if(Integer.parseInt(l)>21846 && Integer.parseInt(l)<43690) l="yellow";
									else l="LawnGreen";
								
								      respRMI = "<HTML>\n" +
			                            				"<head><style type='text/css'> body{background-color: AliceBlue;} </style>\n" +
			                            				"</head>\n" +
			                            				"<body>\n" +
			                            				"<div id=luz style='width:500px; height:500px; background-color: "+l+"'></div>\n" +
			                            				"</body>\n" +
			                            				"</HTML>";
			
							}
							break;
							default: respRMI = "<HTML>\n" +
			                            			"<head><style type='text/css'> body{background-color: AliceBlue;} </style>\n" +
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
		                            			"<head><style type='text/css'> body{background-color: AliceBlue;} </style>\n" +
		                            			"</head>\n" +
		                            			"<body>\n" +
		                            			"<a>La sonda "+sonda+" tiene un volumen de "+objetoRemoto.setvolumen(sonda,val.toString())+"</a>\n" +
		                            			"</body>\n" +
		                            			"</HTML>";
						}
						break;
						case "setled": {
								respRMI = "<HTML>\n" +
										"<head><style type='text/css'> body{background-color: AliceBlue;} </style>\n" +
										"</head>\n" +
										"<body>\n" +
										"<a>"+objetoRemoto.setled(sonda,val.toString())+"</a>\n" +
										"</body>\n" +
										"</HTML>";
						}
						break;
						default: respRMI = "<HTML>\n" +
		                            			"<head><style type='text/css'> body{background-color: AliceBlue;} </style>\n" +
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
	            		  "<head><style type='text/css'> body{background-color: AliceBlue;} </style>\n" +
	                      "</head>\n" +
	                      "<body>\n" +
	                      "<a>No existe esa sonda</a>\n" +
	                      "</body>\n" +
	                      "</HTML>";
	            	//System.exit(0);
	        	}
			
		}
		
		
		
		return respRMI;
	}
	
	public String controllerRunning(String Cadena) {
		System.out.println(Cadena);
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
				
				//Aquí ya tendríamos todos los datos de la sonda y su metodo y sus valores y voy al baño que me estoy cagando CtrlS pls
				
				System.out.println(method);System.out.println(val);System.out.println(valsonda);
				Cadena = RMICall(method,val,valsonda);
			}
			
		}
		else{
			if(Cadena.equals("") || Cadena.equals("index.html")) {
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
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
		* Descriptores de socket servidor y de socket con el cliente
		*/
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
				System.out.println(Cadena);
				Cadena=sr.controllerRunning(Cadena);
				sr.escribeSocket (skCliente, Cadena);
						
			}
		}
		catch(Exception e){
			System.out.println("Error: " + e.toString());
		}
	}

}
