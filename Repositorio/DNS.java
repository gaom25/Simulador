import java.util.ArrayList;
import java.rmi.registry.LocateRegistry;
import java.net.*;
import java.rmi.*;
import java.lang.*;
import java.util.Date;

/**
 *
 * @author krys
 */
public class DNS {

    /* Metodo areYouAlive
    * Se encarga de preguntarle al coordinador si se encuentra vivo
    * Parametros de entrada: El servidor a consultar
    * Parametros de salida: True si el servidor se encuentra vivo
    * o false en caso contrario
    */
    public static Boolean areYouAlive(Servidor coordinador){
        Boolean valorRetorno = false;
        try {
            Acciones a = (Acciones) Naming.lookup("rmi://" +coordinador.getHost()+ ":" + 55555 + "/REPO");
            valorRetorno=a.estasVivo();
        } catch (MalformedURLException murle) {
            System.out.println();
            System.out.println(
                    "MalformedURLException");
            System.out.println(murle);
        } catch (RemoteException re) {
            return false;
           // System.out.println();
            //System.out.println(
           //         "RemoteException");
           // System.out.println(re);

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
        return valorRetorno;
    }

	/**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    	// Puerto por el cual escuchara el DNS
    	int puerto = 44444;
        DNSImpl d=null;

        try {
             d = new DNSImpl();
        } catch (Exception e) {
            System.out.println("Trouble: " + e);
        }
       

    	// Verificamos el numero de argumentos
    	if (0 !=args.length) {
            System.err.print("Parametros incorrectos: ");
            System.err.println("java DNS");
            System.exit(1);
        }

        // Crea un Registry en el puerto especificado
        try {    
            LocateRegistry.createRegistry(puerto);
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
            // Registra con el nombre DNS al objeto D
            // en el Registry que se encuentra el el host <localhost>
            // y puerto <port>

            Naming.rebind("rmi://localhost:44444"+ "/DNS", d);

            } catch (Exception e) {
                System.out.println("Trouble: " + e);
            }
        System.out.println("DNS escuchando...");

        while(d.getCoordinador()==null);

        // Ciclo infinito que se encargara de preguntar al servidor
        // coordinador si sigue vivo 
        while (true){
            try{
              Thread.currentThread().sleep(2000);
            }
            catch(InterruptedException ie){
            //If this thread was intrrupted by nother thread
            }

            
            // Preguntar si esta vivo el coordinador
            if (areYouAlive(d.getCoordinador())==false){
                System.out.println("Murio el coordinador");
                // Aqui debe iniciar el algoritmo de seleccion de coordinador
                // Debemos seleccionar el próximo servidor esclavo con menor
                // ID para que sea el coordinador ahora
                if (!d.setNuevoCoordinador()){
                    System.out.println("No hay mas servidores vivos!");
                    System.exit(0);
                }

            }else{
                System.out.println("Sigue vivo el coordinador");
            }
            
        }

    } // fin main


}
