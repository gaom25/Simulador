import java.rmi.*;
import java.net.MalformedURLException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
/**
 *
 * @author carlos
 */
public class mkdir {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String host = null;
        int port = 0;

        if (args.length != 2) {
            System.err.print("Parametros incorrectos: ");
            System.err.println("java mkdir <hostName> <repo>");
            System.exit(1);
        }
        System.out.println("Por favor introduzca su nombre:");
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader (isr);
        try {
            String nombre = br.readLine();
            host = args[0];

            /**Primero nos conectamos con el DNS*/
            DNSI d = (DNSI) Naming.lookup("rmi://" + host + ":" + 44444 + "/DNS");
            Servidor serv = d.quienEsCoord();

            /**Luego buscamos el servicio como tal*/
            System.out.println(serv.getHost());
            Acciones c = (Acciones) Naming.lookup("rmi://" + serv.getHost() + ":" + 55555 + "/REPO");
            System.out.println(c.mkdir(args[1],nombre));
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
            e.printStackTrace();
        }
    }
}