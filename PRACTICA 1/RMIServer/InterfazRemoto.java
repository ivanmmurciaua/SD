import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.io.UnsupportedEncodingException;

public interface InterfazRemoto extends Remote {
    public String volumen () throws java.rmi.RemoteException;
    public String luz() throws java.rmi.RemoteException;
    public String fecha() throws java.rmi.RemoteException;
    public String ultimaFecha() throws java.rmi.RemoteException;
    public String info() throws java.rmi.RemoteException;
    public String setled(int sonda, String val) throws java.rmi.RemoteException,UnsupportedEncodingException, IOException;
    public String setvolumen(int sonda, String val) throws java.rmi.RemoteException,UnsupportedEncodingException, IOException;
    public String setfecha() throws java.rmi.RemoteException,UnsupportedEncodingException, IOException;
}
