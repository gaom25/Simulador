import java.rmi.RemoteException;

/**
 *
 * @author gustavo
 */
public class AccionesImpl extends java.rmi.server.UnicastRemoteObject
        implements Acciones {

    public AccionesImpl() throws java.rmi.RemoteException {
        super();
    }

   // @Override
    public String commit() throws RemoteException {
        System.out.println("Haciendo commit");
        return ("El commiteo");
    }

    //@Override
    public String checkout() throws RemoteException {
        System.out.println("Haciendo checkout");
        return ("El chekeo");
    }

    //@Override
    public String update() throws RemoteException {
        System.out.println("Haciendo update");
        return ("El updateo");
    }
}
