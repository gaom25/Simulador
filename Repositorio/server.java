
import java.net.*;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.io.*;

/**
 *
 * @author gustavo
 */
public class server {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int port = 0;
        int accion = 0;
        if (!((0 < args.length) && (args.length < 3))) {
            System.err.print("Parametros incorrectos: ");
            System.err.println("CalculatorServer <port>");
            System.exit(1);
        }

        accion = Integer.parseInt(args[1]);
        /**
         * Si accion es 1 quiere decir que es el coordinador
         */
        if (accion == 1) {
            try {
                port = Integer.parseInt(args[0]);

                // Crea un Registry en el puerto especificado
                LocateRegistry.createRegistry(port);
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

                Naming.rebind("rmi://localhost:" + port + "/CalculatorService", c);

            } catch (Exception e) {
                System.out.println("Trouble: " + e);
            }
        } else {

            /**
             * Si accion es diferente de 1 entonces sera un esclavo y se mandara
             * el mensaje por el socketmulticast a los demas servidores.
             */
            Actualizacion acc = null;
            try {
                byte[] dato = new byte[1000];
                MulticastSocket escucha = new MulticastSocket(55557);

                /**
                 * Nos poner a escuchar por la misma IP que se envio en mensaje
                 */
                escucha.joinGroup(InetAddress.getByName("230.0.0.5"));

                DatagramPacket dpg = new DatagramPacket(dato, dato.length);
                /**
                 * hacemos el recive para escuchar el dato que se envio
                 */
                escucha.receive(dpg);

                /**
                 * obtencion del dato
                 */
                dato = dpg.getData();

                /**
                 * Deserealizamos el objeto como ponerlo trabajar mejor
                 */
                ByteArrayInputStream bs = new ByteArrayInputStream(dato);
                ObjectInputStream in = new ObjectInputStream(bs);
                acc = (Actualizacion) in.readObject();
                in.close();
            } catch (Exception e) {
                System.out.println("Toruble: " + e);
            }

            System.out.println(acc.toString());
        }
    }
}
