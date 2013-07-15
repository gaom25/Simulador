import java.rmi.RemoteException;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
/**
 *
 * @author krys
 */
public class DNSImpl extends java.rmi.server.UnicastRemoteObject 
	implements DNSI {

	// Lista de servidores, se agregan a medida
    // que se inician
    ArrayList<Servidor> servidores;

    // Para no buscar siempre en la lista, aqui se mantiene
    // el servidor que es actualmente el coordinador
    Servidor coordinador;

	// Constructor 
	public DNSImpl() throws java.rmi.RemoteException {
        super();
        servidores = new ArrayList<Servidor>();
    }

    
    /* Metodo registro
     * se encarga de registrar un servidor , agregandolo a la lista
     * de servidores que posee el servidor dns.
     * Parametro de entrada: el servidor a registrar
     * Valor de retorno : Retorna el ID que posee el servidor.
    */
     public int registro(Servidor serv) throws java.rmi.RemoteException{
        int retorno = servidores.size();

        // Le colocamos su id al servidor antes de agregarlo
        serv.setID(Integer.toString(retorno));

     	 // Verificamos si el que vamos a agregar es coordinador
        if (serv.getEsCoordinador()==true)
            coordinador=serv;

        // agregamos a la lista de servidores
        servidores.add(serv);
        System.out.println("Registrado en DNS el servidor de ID="+serv.getID());
        return retorno;
     }

    /* Metodo robtenerServidores
     * Se encarga de obtener la lista de servidores que posee el servidor dns
     * Parametro de entrada: no posee
     * Valor de retorno: retorna una lista con los servidores
    */
    public ArrayList<Servidor> obtenerServidores() throws java.rmi.RemoteException{
    	return this.servidores;
    }

    /* Metodo quienEsCoord
     * Se encarga de indicar quien es el coordinador actual.
     * Parametros de entrada: no posee
     * Valor de retorno: el servidor que es el coordinador actual
    */
    public Servidor quienEsCoord() throws java.rmi.RemoteException{
    	return coordinador;    
    }

    public Servidor getCoordinador(){
        return this.coordinador;
    }

}