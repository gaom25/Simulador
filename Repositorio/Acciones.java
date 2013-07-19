
import java.util.ArrayList;

/**
 *
 * @author gustavo
 */
/**
 * Interfaz para el pase de parametros por RMI.
 */
public interface Acciones
        extends java.rmi.Remote {

    public String commit(Actualizacion a) throws java.rmi.RemoteException;

    public ArrayList<Actualizacion> checkout(String cliente, ArrayList<String> repos) throws java.rmi.RemoteException;

    public Actualizacion update(String cliente,String repo) throws java.rmi.RemoteException;
    
    public String mkdir(String name,String user) throws java.rmi.RemoteException;

    public Boolean estasVivo() throws java.rmi.RemoteException;

    public void nuevoEsclavo(ArrayList<Servidor> esclavos)throws java.rmi.RemoteException;

    public Boolean asignacionCoord() throws java.rmi.RemoteException;
    
    public void respuestaTPC(Boolean rsp,Servidor server) throws java.rmi.RemoteException;
}
