
import java.rmi.RemoteException;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;

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
    public ArrayList<Repositorio> checkout(ArrayList<String> repos) throws RemoteException {
        System.out.println("Haciendo checkout");
        try {
            MulticastSocket enviador = new MulticastSocket();

            Actualizacion dato = new Actualizacion("Checkout");
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
        return ("El chekeo");
    }

    //@Override
    public String update() throws RemoteException {
        System.out.println("Haciendo update");
        try {
            MulticastSocket enviador = new MulticastSocket();

            Actualizacion dato = new Actualizacion("Update");
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
        return ("El updateo");
    }
    
    /**
    Funcion por hacer aun no esta ready.
    */
    
    public String mkdir(String name) throws RemoteException {
        System.out.println("Creando Repo");
        try {
            MulticastSocket enviador = new MulticastSocket();

            Actualizacion dato = new Actualizacion("mkdir::"+name);
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
            /**TPC, TWO PHASE COMMIT*/
            
        } catch (Exception e) {
            System.out.println("Error" + e);
        }
        return ("");
    }
    
}