import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;

/**
*  Clase MyHTTPServer : Crea hilos de respuesta al cliente
*  @author ivanmmurciaua
*/
public class MyHTTPServer {
	@SuppressWarnings("resource")
	/**
	 * @param args
	 * Main de la clase donde crea hilos
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
			while(Thread.activeCount() < conexiones_max){ //Concurrente	
		
                Socket skCliente = skServidor.accept(); // Crea objeto
		        System.out.println("SIRVIENDO CLIENTE");
		        //Crea el hilo
		        Thread t = new ServerThread(skCliente,ip_controller,puerto_controller);
		        t.start();
		        
			}
		}
		catch(Exception e){
			System.out.println("Error: " + e.toString());
		}
	}
}