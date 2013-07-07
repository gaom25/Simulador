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

    /**
     * Constructor se le pasa el ID  para registrar al cliente y se crea
     * su arraylist de repositorios.
     */
    public Cliente(String id){
        this.ID = id;
        this.Repositorios = new ArrayList<String>();
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

    /**
     * Cuando el cliente desee crear un nuevo repo por checkout, se le agrega
     * al proceso cuales repo conoce.
     */
    public void agregarRepositorio(String repo) {
        this.Repositorios.add(0, repo);
    }
}
