import java.rmi.*;
import java.net.MalformedURLException;

/**
 *
 * @author gustavo
 */
public class commit {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String host = null;
        int port = 0;

        if (!((0 < args.length) && (args.length < 2))) {
            System.err.print("Parametros incorrectos: ");
            System.err.println("java commit <hostName> -m \"<mensaje>\"");
            System.exit(1);
        }
        System.out.print(args.toString());

        try {
            host = args[0];

            Acciones c = (Acciones) Naming.lookup("rmi://" + host + ":" + 55555 + "/CalculatorService");
            System.out.println(c.commit());
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
    }
}
