import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.net.*;
import java.io.*;

public class Registro {         
    public static void main (String args[])     
    {            
	if (args.length < 2) {
		System.out.println("Debe indicar el puerto de escucha del servidor");
		System.out.println("$./Registro IP_Maquina_local ID_sonda");
		System.exit (1);
			}
        String URLRegistro;
        try           
        {   
		String idsonda = args[1];        	
		System.setSecurityManager(new RMISecurityManager());
		System.setProperty("java.rmi.server.hostname",args[0]);
        	ObjetoRemoto objetoRemoto = new ObjetoRemoto(idsonda);                  
        	URLRegistro = "/ObjetoRemoto"+idsonda;
            	Naming.rebind (URLRegistro, objetoRemoto);            
            	System.out.println("Sonda "+idsonda+" ready, steady and go");
        }            
        catch (Exception ex)            
        {                  
            System.out.println(ex);            
        }     
    }
}
