import java.util.ArrayList;
import java.util.Hashtable;

/**
 *
 * @author gustavo
 */
public class Servidor {
    /**
     * Identificador del proceso.
     */
    private String ID;
    /**
     * Datos para saber quien es el coordinador actualmente.
     */
    private Servidor Coordinador;
    /**
     * Booleano que designa si el proceso actual es el servidor o no.
     */
    private Boolean esCoordinador;
    /**
     * Aqui se lleva cada uno de los ID de los otros servidores y se va 
     * actualizando al aparecerse las fallas.
     */
    private ArrayList<String> Servidores;
    /**
     * Tablas de hash que tendra los diferentes nombres de los repositorios.
     */
    private Hashtable<String,Repositorio> Repositorios;

    public Servidor() {
    }
    
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Servidor getCoordinador() {
        return Coordinador;
    }

    public void setCoordinador(Servidor Coordinador) {
        this.Coordinador = Coordinador;
    }

    public Boolean getEsCoordinador() {
        return esCoordinador;
    }

    public void setEsCoordinador(Boolean esCoordinador) {
        this.esCoordinador = esCoordinador;
    }

    public ArrayList<String> getServidores() {
        return Servidores;
    }

    public void setServidores(ArrayList<String> Servidores) {
        this.Servidores = Servidores;
    }

    public Hashtable<String, Repositorio> getRepositorios() {
        return Repositorios;
    }

    public void setRepositorios(Hashtable<String, Repositorio> Repositorios) {
        this.Repositorios = Repositorios;
    }
    
}
