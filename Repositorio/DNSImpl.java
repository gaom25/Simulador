import java.util.ArrayList;
import java.rmi.registry.LocateRegistry;
import java.net.*;
import java.rmi.*;
import java.lang.*;



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
        coordinador = null; 
    }


    /* Metodo actualizarServidores
     * Este metodo se encarga de actualizar la lista de servidores del coordinador
     * estableciendo una conexión rmi con el coordinador y enviandosela.
     * Parametros de entrada: No posee
     * Parametros de salida: no posee
    */
    public void actualizarServidores(String host){
        try {
            Acciones a = (Acciones) Naming.lookup("rmi://" +host+ ":" + 55555 + "/REPO");
            a.nuevoEsclavo(this.servidores);
        } catch (MalformedURLException murle) {
            System.out.println();
            System.out.println(
                    "MalformedURLException");
            System.out.println(murle);
        } catch (RemoteException re) {
            System.out.println();
            System.out.println("RemoteException");
            System.out.println(re);

        } catch (NotBoundException nbe) {
            System.out.println();
            System.out.println(
                    "NotBoundException");
            System.out.println(nbe);

        } catch (Exception e) {
            System.out.println();
            System.out.println("java.lang.Exception");
            System.out.println(e);
        }

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
        if (serv.getID().compareTo("0") == 0){
            coordinador=serv;
            System.out.println("llego coordinador");
        }

        // agregamos a la lista de servidores
        servidores.add(serv);
        /** Si existe un coordinador debemos enviarle la nueva lista
        * de esclavos para que le lleguen las nuevas actualizaciones
        * revizan tambien que solo se envie la lista cuando haya mas servidores
        * ademas del coordinador
        */
        if(coordinador!=null && servidores.size() > 1){
            actualizarServidores(coordinador.getHost());
        }            
        
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
