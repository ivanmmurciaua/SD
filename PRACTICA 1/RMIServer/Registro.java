import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.net.*;
import java.io.*;

public class Registro {         
    public static void main (String args[])     
    {            
	if (args.length < 1) {
		System.out.println("Debe indicar el puerto de escucha del servidor");
		System.out.println("$./Registro IP_Maquina_local");
		System.exit (1);
			}
        String URLRegistro;
        try           
        {   
        	System.setSecurityManager(new RMISecurityManager());
		System.setProperty("java.rmi.server.hostname",args[0]);
        	ObjetoRemoto objetoRemoto = new ObjetoRemoto();                  
        	URLRegistro = "/ObjetoRemoto";
            Naming.rebind (URLRegistro, objetoRemoto);            
            System.out.println("Servidor de objeto preparado.");
        }            
        catch (Exception ex)            
        {                  
            System.out.println(ex);            
        }     
    }
}
