/**
 *
 * @author gustavo
 */
/**
 * Interfaz para el pase de parametros por RMI.
 */
public interface Acciones
        extends java.rmi.Remote {

    public String commit() throws java.rmi.RemoteException;

    public String checkout() throws java.rmi.RemoteException;

    public String update() throws java.rmi.RemoteException;
}
