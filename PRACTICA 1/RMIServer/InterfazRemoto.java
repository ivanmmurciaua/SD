import java.rmi.Remote;

public interface InterfazRemoto extends Remote {
    public String volumen (int sonda) throws java.rmi.RemoteException;
    public String luz(int sonda) throws java.rmi.RemoteException;
    public String fecha() throws java.rmi.RemoteException;
    public String ultimaFecha(int sonda) throws java.rmi.RemoteException;
}

