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
        }
            catch(Exception ex) {
        }
        System.out.println("host ->"+computerName);

        Servidor server = new Servidor();
        server.setHost(computerName);
        hostDNS=args[1];
        
        // Debemos registrar el coordinador en el dns
        if (registrarmeDNS(server,hostDNS)==0){
        	 // El servidor es coordinador
            server.setEsCoordinador(true);
            
            try {

                // Crea un Registry en el puerto especificado
                LocateRegistry.createRegistry(55555);
            } catch (RemoteException re) {
                System.out.println();
                System.out.println("RemoteException");
                System.out.println(re);
            } catch (Exception e) {
                System.out.println();
                System.out.println("java.lang.Exception");
                System.out.println(e);
            }

            try {
                Acciones c = new AccionesImpl();

                // Registra con el nombre CalculatorService al objeto c 
                // en el Registry que se encuentra el el host <localhost>
                // y puerto <port>

                Naming.rebind("rmi://localhost:" + 55555 + "/REPO", c);

            } catch (Exception e) {
                System.out.println("Trouble: " + e);
            }
            
        }else{
        	// No es coordinador
            server.setEsCoordinador(false);
         
            
             //Si accion es diferente de 1 entonces sera un esclavo y se mandara
             //el mensaje por el socketmulticast a los demas servidores.
            
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
            String[] comando = accion.split("::");
            String cliente = comando[1];
            String repo = comando[2];
            
            if(comando[0].compareToIgnoreCase("mkdir") == 0){
             
	             AccionesServer.crearRepo(cliente, repo);
            
            }else if(comando[0].compareToIgnoreCase("commit") == 0){
            
              ArrayList<File> archivos = acc.getArchivos();
              Date hora = acc.getTiempAct();
              AccionesServer.actualizarRepo(cliente,repo,archivos, hora);
            
            }else if(comando[0].compareToIgnoreCase("update") == 0){
                
//               actualizarCliente(cliente,repo);
            
            }else if(comando[0].compareToIgnoreCase("checkout") == 0){
              
              String[] repos = new String[comando.length - 1]; 

              for(int i=1; i < comando.length; i++)
		          repos[i-1] = comando[i];
            
//               actualizacionMultiple(cliente,repos);
            }
        }
        
        
    }
}
