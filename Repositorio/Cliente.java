import java.util.ArrayList;

/**
 *
 * @author gustavo
 */
public class Cliente {

    /**
     * Identificador del cliente.
     */
    private String ID;
    /**
     * Arraylist con los difenrentes repos del cliente.
     */
    private ArrayList<String> Repositorios;

 //     // Llave publica 
//     private PublicKey publica;
//     
//     //Llave privada 
//     private PrivateKey privada;

    /**
     * Constructor se le pasa el ID  para registrar al cliente y se crea
     * su arraylist de repositorios.
     */
    public Cliente(String id){
        this.ID = id;
        this.Repositorios = new ArrayList<String>();
//      //Generacion de las claves privada y publica del cliente.
//      KeyPairGenerator parGenerado = KeyPairGenerator.getInstance("RSA");
//      parGenerado.initialize(1024); // Llave de 1024
//      KeyPair par = parGenerado.generateKeyPair();
//      this.setPublica(par.getPublic());
//      this.setPrivada(par.getPrivate());
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public ArrayList<String> getRepositorios() {
        return Repositorios;
    }

    public void setRepositorios(ArrayList<String> Repositorios) {
        this.Repositorios = Repositorios;
    }

//     public PublicKey getPublica(){
//  return publica
//     }
//     
//     public void setPublica(PublicKey p)
//  publica = p;
//     }
//     
//     private PrivateKey getPrivada(){
//  return privada
//     }
//     
//     private void setPrivada(PrivateKey p)
//  privada = p;
//     }

    /**
     * Cuando el cliente desee crear un nuevo repo por checkout, se le agrega
     * al proceso cuales repo conoce.
     */
    public void agregarRepositorio(String repo) {
        this.Repositorios.add(0, repo);
    }
}
