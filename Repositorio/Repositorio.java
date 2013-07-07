import java.util.ArrayList;

/**
 *
 * @author gustavo
 */
public class Repositorio {
    /**
     * Identificador del repositorio.
     */
    private String ID;
    /**
     * Lista con las diferentes actualizaciones hechas.
     */
    private ArrayList<Actualizacion> Actualizaciones;

    /**
     * Constructor para el repositorio.
     */
    public Repositorio(String ID) {
        this.ID = ID;
        this.Actualizaciones = new ArrayList<Actualizacion>();
    }
    
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public ArrayList<Actualizacion> getActualizaciones() {
        return Actualizaciones;
    }

    public void setActualizaciones(ArrayList<Actualizacion> Actualizaciones) {
        this.Actualizaciones = Actualizaciones;
    }

    public void agregarActualizacion(Actualizacion act){
        this.Actualizaciones.add(0, act);
    }

}
