
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
 *
 * Trae archivos de uno o varios repo si existe o no el repo aqui
 */
public class checkout {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String host = null;
        int tam = 0;

        if (args.length < 1) {
            System.err.print("Parametros incorrectos: ");
            System.err.println("java checkout <hostName> <repositorio>* [-d] [carpeta]");
            System.exit(1);
        }

        boolean hayDestino = false;
        int ultimoRepo = args.length;

        if (args[args.length - 2].compareToIgnoreCase("-d") == 0) {
            hayDestino = true;
            ultimoRepo = args.length - 2;
            File f = new File(args[args.length - 1]);

            if (!f.exists()) {
                System.err.println("Destino NO existe!");
                System.exit(1);
            }
        }
        
        System.out.println("Por favor introduzca su nombre:");
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);

        ArrayList<Actualizacion> actualizaciones = new ArrayList<Actualizacion>();
        Actualizacion actualiza;
        ArrayList<String> repos = new ArrayList<String>();
        

        for (int i = 1; i < ultimoRepo; i++) {
            repos.add(args[i]);
        }

        try {
            String nombre = br.readLine();
            host = args[0];

            /**
             * Primero nos conectamos con el DNS
             */
            DNSI d = (DNSI) Naming.lookup("rmi://" + host + ":" + 44444 + "/DNS");
            Servidor serv = d.quienEsCoord();
            

            /**
             * Luego buscamos el servicio como tal
             */
            Acciones c = (Acciones) Naming.lookup("rmi://" + serv.getHost() + ":" + 55555 + "/REPO");
            actualizaciones = c.checkout(nombre, repos);
            
            if (actualizaciones == null) {
                System.err.print("Error, algun repo no exsite ");
                System.exit(1);
            }

            ArrayList<File> archivos;
            int auxRepo = 0;
            
            for (Actualizacion act : actualizaciones) {
                archivos = act.getArchivos();

                for (int i = 0; i < archivos.size(); i++) {
                    File f1 = archivos.get(i);

                    // hasta aqui se que [carpeta] existe
                    File f2 = new File((hayDestino ? args[args.length - 1] : ".") + "/" + repos.get(auxRepo));

                    if (!f2.exists()) { // Veo si no existe repo
                        if (!f2.mkdir()) { // Si no se puede crear repo
                            System.err.print("Error, No se pudo crear carpeta");
                            System.exit(1);
                        }
                    }

                    f2 = new File((hayDestino ? args[args.length - 1] : ".") + "/" + repos.get(auxRepo) + "/" + f1.getName());


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

                auxRepo++;
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
