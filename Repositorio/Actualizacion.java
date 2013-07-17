
import java.io.File;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author gustavo
 */
public class Actualizacion implements java.io.Serializable {

    /**
     * Identificador de la actualizacion.
     */
    private String ID;
    /**
     * ArrayList con los archivos de esta actulizacion.
     */
    private ArrayList<File> archivos;
    /**
     * Tiempo en que se hizo la actualizacion.
     */
    private Date tiempAct;

    // Avisa quien es el nuevo coordinador
    private Servidor coordinador;

    // Lista de servidores
    private ArrayList<Servidor> servidores;

    /**
     * Constructor de la actualizacion, se le pasa el ID y el arraylist para los
     * archivos se setean.
     */
    public Actualizacion(String ID) {
        this.archivos = new ArrayList<File>();
        this.servidores = new ArrayList<Servidor>();
        this.ID = ID;
    }


    /**
     * Constructor de la actualizacion, se le pasa el ID y el arraylist para los
     * archivos se setean.
     */
    public Actualizacion(String ID,Servidor coord,ArrayList<Servidor> servidores) {
        this.archivos = new ArrayList<File>();
        this.servidores = servidores;
        this.coordinador=coord;
        this.ID = ID;
    }
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public ArrayList<File> getArchivos() {
        return archivos;
    }

    public void setArchivos(ArrayList<File> archivos) {
        this.archivos = archivos;
    }

    /**
     * Las funciones de getTiempAct y setTiemAct solo las usa el servidor pues
     * este quien setea el tiempo en que llego la actualizacion.
     */
    public Date getTiempAct() {
        return tiempAct;
    }

    public void setTiempAct(Date tiempAct) {
        this.tiempAct = tiempAct;
    }

    public void agregarArchivo(File archivo) {
        this.archivos.add(archivo);
    }

    @Override
    public String toString() {
        return "Actualizacion{" + "ID=" + ID + ", archivos=" + archivos + ", tiempAct=" + tiempAct + '}';
    }

    public Servidor getCoordinador() {
        return coordinador;
    }

    public void setCoordinador(Servidor nuevoCoord) {
        this.coordinador = nuevoCoord;
    }

    public ArrayList<Servidor> getServidores() {
        return servidores;
    }

    public void setServidores(ArrayList<Servidor> servs) {
        this.servidores = servs;
    }

}
