
import java.util.ArrayList;

/**
 *
 * @author krys
 */
/**
 * Interfaz para el pase de parametros por RMI del DNS.
 */
public interface DNSI
        extends java.rmi.Remote {

    // Un servidor desea registrarse 
    public int registro(Servidor serv) throws java.rmi.RemoteException;

    // Metodo que permite conocer todos los servidores esclavos y al maestro
    public ArrayList<Servidor> obtenerServidores() throws java.rmi.RemoteException;

    // Un cliente desea conocer quien es el servidor
    public Servidor quienEsCoord() throws java.rmi.RemoteException;

 }