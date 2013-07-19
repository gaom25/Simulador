import java.net.*;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author gustavo
 */
public class server {

    /* 
    * Metodo respuestaTPC
    */
    public static void respuestaTPC(Boolean miResp,Servidor serv){
    
      ArrayList<Servidor> servidores = serv.getServidores();
      String host = "";
    
      // buscamos el coordinador
      for (int i = 0; i<servidores.size();i++){
    if (servidores.get(i).getEsCoordinador()){
      host = servidores.get(i).getHost();
      break;
    }
      
      }
    
        try {
            Acciones a = (Acciones) Naming.lookup("rmi://" +host+ ":" + 55555 + "/REPO");
            a.respuestaTPC(miResp,serv);
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


    /* Metodo registrarServicio
    *  Este metodo se encarga de registrar el servicio RMI que 
    * presta el coordinador para que se comuniquen con el
    * Parámetros de entrada: el servidor en si
    * Parametros de salida : True si logro registrar el servicio
    * con exito , false en caso contrario
    */
    public static Boolean registrarServicio(Servidor server){
         // El servidor es coordinador
        server.setEsCoordinador(true);
        
        try {
            // Crea un Registry en el puerto del coordinador
            LocateRegistry.createRegistry(55555);
        } catch (RemoteException re) {
            System.out.println();
            System.out.println("RemoteException");
            System.out.println(re);
            return false;
        } catch (Exception e) {
            System.out.println();
            System.out.println("java.lang.Exception");
            System.out.println(e);
            return false;
        }

        try {
            Acciones c = new AccionesImpl();

            // Registra con el nombre REPO al objeto c 
            // en el Registry que se encuentra el el host <localhost>
            // y puerto <port>

            Naming.rebind("rmi://localhost:" + 55555 + "/REPO", c);

        } catch (Exception e) {
            System.out.println("Trouble: " + e);
            return false;
        }

        return true;
    }

    /* Metodo registrarmeDNS
     * Este metodo se encarga de registrar el servidor con el servidor DNS
     * Parametros de entrada :  EL servidor a registrar
     * Parametros de salida : String que será el id del servidor
    */
    public static int registrarmeDNS(Servidor serv,String hostDNS){
        // ID del servidor
        int ID = -1;

        // Puerto del DNS
        int port = 44444;

        try {
            DNSI d = (DNSI) Naming.lookup("rmi://" + hostDNS + ":" + 44444 + "/DNS");
            
            ID=d.registro(serv);
        } catch (MalformedURLException murle) {
            System.out.println();
            System.out.println(
                    "MalformedURLException");
            System.out.println(murle);
        } catch (RemoteException re) {
            System.out.println();
            System.out.println(
                    "RemoteException");
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
        return ID;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String hostDNS ="";
        String computerName="";

        if (!((0 < args.length) && (args.length < 4))) {
            System.err.print("Parametros incorrectos: ");
            System.err.println("java server -dns <hostDNS>");
            System.exit(1);
        }


        // Obtenemos el host de la maquina local, solo el nombre no el IP
        try {
            computerName = InetAddress.getLocalHost().getHostName();
        } catch(Exception ex) {
            System.out.println("Error obteniendo el hostname");
        }

        Servidor server = new Servidor();
        server.setHost(computerName);

//         //Generacion de las claves privada y publica del servidor.
//         KeyPairGenerator parGenerado = KeyPairGenerator.getInstance("RSA");
//  parGenerado.initialize(1024); // Llave de 1024
//  KeyPair par = parGenerado.generateKeyPair();
//  server.setPublica(par.getPublic());
//  server.setPrivada(par.getPrivate());

        hostDNS=args[1];

        // Debemos registrar el coordinador en el dns
        int miID = registrarmeDNS(server,hostDNS);
        server.setID(String.valueOf(miID));

        if (miID==0){
        	
            // El servidor debe registrar el servicio RMI
            if (!registrarServicio(server))
                System.out.println("Error del servidor al registrar el servicio");
            
            
        }else{
            // No es coordinador
            server.setEsCoordinador(false);
            System.out.println("Escuchando...");

            String clienteTPC = "";
            String repoTPC = "";
            ArrayList<File> archivosTPC = new ArrayList<File>();
            Date horaTPC = new Date();

            //Si accion es diferente de 1 entonces sera un esclavo y se mandara
            //el mensaje por el socketmulticast a los demas servidores.
            while (true) {
                Actualizacion acc = null;
                try {
                    byte[] dato = new byte[1000];
                    MulticastSocket escucha = new MulticastSocket(55557);


                    //Nos pone a escuchar por la misma IP que se envio en mensaje

                    escucha.joinGroup(InetAddress.getByName("230.0.0.5"));

                    DatagramPacket dpg = new DatagramPacket(dato, dato.length);

                    //hacemos el recive para escuchar el dato que se envio
                    escucha.receive(dpg);

                    //obtencion del dato
                    dato = dpg.getData();

                    //Des-serealizamos el objeto como ponerlo trabajar mejor
                    ByteArrayInputStream bs = new ByteArrayInputStream(dato);
                    ObjectInputStream in = new ObjectInputStream(bs);
                    acc = (Actualizacion) in.readObject();
                    in.close();
                } catch (Exception e) {
                    System.out.println("Trouble: " + e);
                }

                String accion = acc.getID();

                // Si la accion es coordinador es porque hay un nuevo coordinador
                if (accion.compareToIgnoreCase("coordinador") == 0) {
                    ArrayList<Servidor> newServidores = acc.getServidores();
                    Servidor newCoord = acc.getCoordinador();

                    if (newCoord != null) {
                        if (newCoord.getID().equals(server.getID())) {
                            // Si soy el nuevo coordinador
                            System.out.println("Soy el nuevo coordinador");
                            server.setEsCoordinador(true);
                            // El servidor debe registrar el servicio RMI
                            if (!registrarServicio(server)) {
                                System.out.println("Error del servidor al registrar el servicio");
                            }
                        }
                    }

                    // Actualizo lista de servidores
                    server.setServidores(newServidores);

                } else if (accion.compareToIgnoreCase("tpc") == 0) {
                    System.out.println("me llaman para votar");
                    // el mensaje es prepared para el two phase commit 
                    // obtenemos los datos de la actualizacion
                    clienteTPC = acc.getCliente();
                    repoTPC = acc.getRepo();
                    horaTPC = acc.getTiempAct();
                    archivosTPC = acc.getArchivos();

                    // VER QUE PUEDA ESCRIBIR Y CAMBIARLO POR EL TRUE
                    respuestaTPC(true, server);

                } else if (accion.compareToIgnoreCase("tpc-commit") == 0) {

                    System.out.println("me llaman para hacer commit");
                    // Los datos ya se tienen cuando se recibe el mensaje de votacion
                    AccionesServer.actualizarRepo(clienteTPC, repoTPC, archivosTPC, horaTPC);

                } else {

                    String[] comando = accion.split("::");
                    String cliente = comando[1];
                    String repo = comando[2];

                    if (comando[0].compareToIgnoreCase("mkdir") == 0) {

                        AccionesServer.crearRepo(cliente, repo);

                    } else if (comando[0].compareToIgnoreCase("update") == 0) {
                        //               actualizarCliente(cliente,repo);
                    } else if (comando[0].compareToIgnoreCase("checkout") == 0) {

                        String[] repos = new String[comando.length - 1];

                        for (int i = 1; i < comando.length; i++) {
                            repos[i - 1] = comando[i];
                        }

                        //               actualizacionMultiple(cliente,repos);
                    } else {
                        System.out.println("No hago nada... abort");
                    }
                }
            } 
        } // cierre if de maestro o esclavo
    }
}
