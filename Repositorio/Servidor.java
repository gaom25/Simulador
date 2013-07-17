import java.util.ArrayList;
import java.util.Hashtable;
import java.io.Serializable;

/**
 *
 * @author gustavo
 */
public class Servidor implements Serializable{
    /**
     * Identificador del proceso.
     */
    private String ID;
    /**
     * Datos para saber quien es el coordinador actualmente.
     */
    private Servidor Coordinador;

    // host donde corre el servidor
    private String host;

    // puerto del servidor
    private int port;

    /**
     * Booleano que designa si el proceso actual es el servidor o no.
     */
    private Boolean esCoordinador;

    /**
     * Aqui se lleva cada uno de los ID de los otros servidores y se va 
     * actualizando al aparecerse las fallas.
     */
    private ArrayList<Servidor> Servidores;
    /**
     * Tablas de hash que tendra los diferentes nombres de los repositorios.
     */
    private Hashtable<String,Repositorio> Repositorios;


	public Servidor(){
		esCoordinador=false;
        Servidores=new ArrayList<Servidor>();
	}

    public Servidor(String hostN,int puerto){
        this.host = hostN;
        this.port = puerto;
    }

    public String getHost(){
        return this.host;
    }

    public void setHost(String hostNew){
        this.host = hostNew;
    }

    public int getPort(){
        return port;
    }

    public void setPort(int puerto){
        this.port=puerto;
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

    public ArrayList<Servidor> getServidores() {
        return Servidores;
    }

    public void setServidores(ArrayList<Servidor> Servidores) {
        this.Servidores = Servidores;
    }

    public Hashtable<String, Repositorio> getRepositorios() {
        return Repositorios;
    }

    public void setRepositorios(Hashtable<String, Repositorio> Repositorios) {
        this.Repositorios = Repositorios;
    }

    public String toString(){
        return "Servidor{" + "ID=" + ID + ", Coordinador=" + esCoordinador + '}';
    }
    
}
