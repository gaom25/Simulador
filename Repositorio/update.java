
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 *
 * @author gustavo
 */
public class update {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String host = null;
        int port = 0;

        if (args.length != 3) {
            System.err.print("Parametros incorrectos: ");
            System.err.println("java update <hostName> -r repo ");
            System.exit(1);
        }
        System.out.println("Por favor introduzca su nombre:");
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        Actualizacion actualiza;
        String resultado;
        try {
            String nombre = br.readLine();
            host = args[0];

            /** 
             * Antes de todo revizamos que este la carpeta del repo en nuestro
             * directorio
             */

            File f1 = new File("./" + args[2]);

            if(!f1.exists()){

                System.out.println("Repositorio \""+args[2]+"\" no encontrado, por favor haga checkout o mkdir");
                System.exit(0);
            }

            /**
             * Primero nos conectamos con el DNS
             */
            DNSI d = (DNSI) Naming.lookup("rmi://" + host + ":" + 44444 + "/DNS");
            Servidor serv = d.quienEsCoord();

            /**
             * Luego buscamos el servicio como tal
             */
            Acciones c = (Acciones) Naming.lookup("rmi://" + serv.getHost() + ":" + 55555 + "/REPO");
            actualiza = c.update(nombre, args[2]);
            resultado = actualiza.getID();

            /*Revizamos que no haya ocurrido ningun error*/
            if(resultado.toLowerCase().contains("ArrayIndexOutOfBoundsException".toLowerCase())){
                System.out.println("Repositorio vacio");
                System.exit(0);
            }
            
            /*Actualizamos el repo actual*/
            ArrayList<File> archivos = actualiza.getArchivos();
            for (int i = 0; i < archivos.size(); i++) {
                f1 = archivos.get(i);
                File f2 = new File("./" + args[2] + "/" + f1.getName());
                InputStream in = new FileInputStream(f1);
                OutputStream out = new FileOutputStream(f2);

                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            }

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
