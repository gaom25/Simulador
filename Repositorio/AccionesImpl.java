
import java.rmi.RemoteException;
import java.net.*;
import java.io.*;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;

/**
 *
 * @author gustavo
 */
public class AccionesImpl extends java.rmi.server.UnicastRemoteObject
        implements Acciones {

    Servidor server = new Servidor();

    public AccionesImpl() throws java.rmi.RemoteException {
        super();
    }

    // @Override
    public String commit() throws RemoteException {
        System.out.println("Haciendo commit");
        try {
            MulticastSocket enviador = new MulticastSocket();

            Actualizacion dato = new Actualizacion("Commit");
            dato.setTiempAct(new Date());

            /**
             * Serealizamos el objeto para poder enviarlo por la red
             */
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(bs);
            os.writeObject(dato);  // this es de tipo DatoUdp
            os.close();
            byte[] bytes = bs.toByteArray(); // devuelve byte[]

            /**
             * Usamos la direccion Multicast 230.0.0.5, por poner alguna dentro
             * del rango y el puerto 77775, uno cualquiera que esté libre.
             */
            DatagramPacket dgp;
            dgp = new DatagramPacket(bytes, bytes.length, InetAddress.getByName("230.0.0.5"), 55557);

            enviador.send(dgp);
        } catch (Exception e) {
            System.out.println("Error" + e);
        }
        return ("El commiteo");
    }

    //@Override
    //@Override
    public ArrayList<Actualizacion> checkout(String cliente, ArrayList<String> repos) throws RemoteException {
        System.out.println("Haciendo checkout");
        
        ArrayList<Actualizacion> actualizaciones = new ArrayList<Actualizacion> ();
        Actualizacion actualiza;
        
        try {
        
	  for (String repo: repos){

	    actualiza = update(cliente,repo);
	    
	    if (actualiza == null){
	      System.out.println("Error en repo" + repo);
	      return null;
	    }else{
	      actualizaciones.add(actualiza);
	    }
	    
	  }

        } catch (Exception e) {
            System.out.println("Error" + e);
        }
        
        System.out.println("Terminando checkout");
        return actualizaciones;
    }


    //@Override
    public Actualizacion update(String cliente, String repo) throws RemoteException {
        System.out.println("Haciendo update");
        Actualizacion actua = null;
        File[] ficheros = null;
        File max;
        Date tmpMax;
        Date tmpAct;
        
        try {

            /* No es necesario hacer multicast jeje*/
            DateFormat df = DateFormat.getDateTimeInstance(
                           DateFormat.DEFAULT, 
                           DateFormat.DEFAULT,new Locale("es","ES"));
            File f = new File("./" + cliente + "/" + repo);
            actua = new Actualizacion("update::" + repo);
            
            if (f.exists()) {

                ficheros = f.listFiles();
                max = ficheros[0];
                tmpMax = df.parse(max.getName());
                for (int i = 1; i < ficheros.length; i++) {

                    /*obtenmos el nombre del fichero y lo llevamos a date*/
                    tmpAct = df.parse(ficheros[i].getName());

                    /*Si el fichero que se esta revizando tiene mayor tiempo
                     * que max se cambia
                     */
                    if (tmpAct.after(tmpMax)) {
                        max = ficheros[i];
                        tmpMax = tmpAct;
                    }
                }
                /*copiamos los archivos que esten en max a la actualizacion en forma 
                 * de arraylist
                 */
                 

                actua.setArchivos(new ArrayList<File>(Arrays.asList(max.listFiles())));
                

                System.out.println("Terminando update");
                return actua;
            } else {
            
                actua.setID("Repositorio inexsistente");
            }
            
        } catch (Exception e) {
            System.out.println("Error");
            e.printStackTrace();
            actua.setID(e.toString());
        }
        
        System.out.println("Terminando update");
        return actua;
    }

    /**
     * Funcion por hacer aun no esta ready.
     */
    public String mkdir(String name, String user) throws RemoteException {
        System.out.println("Creando Repo");
        String resultado = "Fallo";
        try {
            MulticastSocket enviador = new MulticastSocket();

            Actualizacion dato = new Actualizacion("mkdir::" + user + "::" + name);
            dato.setTiempAct(new Date());
            /**
             * Serealizamos el objeto para poder enviarlo por la red
             */
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(bs);
            os.writeObject(dato);  // this es de tipo DatoUdp
            os.close();
            byte[] bytes = bs.toByteArray(); // devuelve byte[]

            /**
             * Usamos la direccion Multicast 230.0.0.5, por poner alguna dentro
             * del rango y el puerto 55557, uno cualquiera que esté libre.
             */
            DatagramPacket dgp;
            dgp = new DatagramPacket(bytes, bytes.length, InetAddress.getByName("230.0.0.5"), 55557);
            /**
             * Se crea la carpeta con el repositorio
             */
            resultado = AccionesServer.crearRepo(user, name);
            enviador.send(dgp);
            /**
             * TPC, TWO PHASE COMMIT
             */
        } catch (Exception e) {
            System.out.println("Error" + e);
        }
        return resultado;
    }

    // Metodo que permite conocer si el servidor se encuentra activo
    // o esta caido, si no responde .
    public Boolean estasVivo() throws RemoteException {

        return true;
    }

    /* Metodo nuevoEsclavo
     * Este metodo permite actualizar la lista de servidores esclavos del coordinador
     * Parametros de entrada : La nueva lista de esclavos
     * Parametros de salida: Booleano que indica si se realizaron los cambios con éxito.
     */
    public void nuevoEsclavo(ArrayList<Servidor> esclavos) throws RemoteException {
        server.setServidores(esclavos);
        System.out.println("Actualizacion de lista de servidores");
    }

    /* Metodo asignacionCoord
     * Este metodo se encarga de asignar al servidor como coordinador 
     */
    public Boolean asignacionCoord() throws java.rmi.RemoteException {
        server.setEsCoordinador(true);
        return true;
    }
}
