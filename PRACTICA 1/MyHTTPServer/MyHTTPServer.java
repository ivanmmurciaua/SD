import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;

public class MyHTTPServer {
	@SuppressWarnings("resource")
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		int total = 0;
		/*
		* Descriptores de socket servidor y de socket con el cliente
		*/
		Integer puerto=0;
                String ip_controller="";
                Integer puerto_controller=0;
                Integer conexiones_max=0;
		

		try {
			
			if (args.length < 4) {
				System.out.println("Formato obligado");
				System.out.println("$./Servidor puerto_servidor conexiones_max ip_controller puerto_controller");
				System.exit (1);
			}
                        
			puerto = Integer.parseInt(args[0]);
                        conexiones_max = Integer.parseInt(args[1]);
                        ip_controller = args[2];
                        puerto_controller = Integer.parseInt(args[3]);
                        
                        
			ServerSocket skServidor = new ServerSocket(puerto);
		    	System.out.println("SERVIDOR WEB ABIERTO POR EL PUERTO " + puerto);
	
			/*
			* Mantenemos la comunicacion con el cliente
			*/	
                        
			while(Thread.activeCount() < conexiones_max){ //active.count	
		
                        Socket skCliente = skServidor.accept(); // Crea objeto
		        System.out.println("SIRVIENDO CLIENTE");

		        Thread t = new ServerThread(skCliente,ip_controller,puerto_controller);
		        t.start();
		        
			}
		}
		catch(Exception e){
			System.out.println("Error: " + e.toString());
			
		}
	}
	
}

/*public class MyHTTPServer {

    public static void main(String args[]) throws IOException {

    	ServerSocket server = new ServerSocket(9799);
        System.out.println("Escuchando puerto 9799 ....");
        
        
        while (true) {
            try (Socket socket = server.accept()) {
            	InputStreamReader isr =  new InputStreamReader(socket.getInputStream());
                BufferedReader reader = new BufferedReader(isr);
                String line = reader.readLine();
                String[] cabecera = line.split(" ");
                System.out.println(cabecera[0]);
            	
                if(!cabecera[0].equals("GET")) {
                	String httpResponse = cabecera[2]+" 405 Method Now Allowed\r\n\r\n";
                    socket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
                }
                else {
                	if(cabecera[1].equals("/") || cabecera[1].equals("/index.html")) {
                		String httpResponse = cabecera[2]+" 200 OK\r\n\r\n";
                		//
                		try(BufferedReader br = new BufferedReader(new FileReader("/home/sd/eclipse-workspace/MyHTTPServer/src/MyHTTPServer/file.html"))) {
                		    StringBuilder sb = new StringBuilder();
                		    String ll = br.readLine();

                		    while (ll != null) {
                		        sb.append(ll);
                		        sb.append(System.lineSeparator());
                		        ll = br.readLine();
                		    }
                		    String everything = sb.toString();
                		    httpResponse += everything;
                            socket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
                		}
                	}
                	else {
                		String httpResponse = cabecera[2]+" 404 Not Found\r\n\r\n";
                        socket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
                	}
                }
            }
        }
    }

}*/
