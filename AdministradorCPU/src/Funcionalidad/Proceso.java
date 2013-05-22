/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 * Esta clase simula la estructura Process Descriptor
 */
package Funcionalidad;
import Constantes.*;

/**
 *
 * @author hector
 */
public class Proceso {
    private int     pid;
    
/** Este campo se puede quitar segun lo que dijo Yudith. peor implica quitarlo de todos los 
 get, set, y de las llamas a procesos. */  private boolean esTiempoReal;
 
    private int     prioridadEstatica;  // static_prior
    private int     tiempoCPU;
    private int     tiempoEntrada;
    private short   estado;             // state
    private int     prioridadDinamica;  // prior
    private int     tiempoDurmiendo;    // sleep_avg (ms)
    private int     quantum;            // time_slice
    private boolean esPrimerQuantum;    // first_time_slice
    private int     tiempoEsperando;    // (ms)
    
    public Proceso(int pid, boolean esTiempoReal, int prioridadEstatica, int tiempoCPU, int tiempoEntrada) {
        this.pid = pid;
        this.esTiempoReal = esTiempoReal;
        this.prioridadEstatica = prioridadEstatica;
        this.tiempoCPU = tiempoCPU;
        this.tiempoEntrada = tiempoEntrada;
        
        this.estado = Constantes.TASK_RUNNING;
        if (esTiempoReal)   this.prioridadDinamica = 100;
        else                this.prioridadDinamica = prioridadEstatica;
        this.tiempoDurmiendo = 0;
        this.quantum = java.lang.Math.min( tiempoCPU, (140 - prioridadEstatica)*(prioridadEstatica < 120 ? 20 : 5) );
        this.esPrimerQuantum = true;
    }

// ========================     Getters/Setters         ========================    
    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public boolean esTiempoReal() {
        return esTiempoReal;
    }

    public void setEsTiempoReal(boolean esTiempoReal) {
        this.esTiempoReal = esTiempoReal;
    }

    public int getPrioridadEstatica() {
        return prioridadEstatica;
    }

    public void setPrioridadEstatica(int prioridadEstatica) {
        this.prioridadEstatica = prioridadEstatica;
    }

    public int getTiempoCPU() {
        return tiempoCPU;
    }

    public void setTiempoCPU(int tiempoCPU) {
        this.tiempoCPU = tiempoCPU;
    }

    public int getTiempoEntrada() {
        return tiempoEntrada;
    }

    public void setTiempoEntrada(int tiempoEntrada) {
        this.tiempoEntrada = tiempoEntrada;
    }

    public short getEstado() {
        return estado;
    }

    public void setEstado(short estado) {
        this.estado = estado;
    }

    public int getPrioridadDinamica() {
        return prioridadDinamica;
    }

    public void setPrioridadDinamica(int prioridadDinamica) {
        this.prioridadDinamica = prioridadDinamica;
    }

    public int getTiempoDurmiendo() {
        return tiempoDurmiendo;
    }

    public void setTiempoDurmiendo(int tiempoDurmiendo) {
        this.tiempoDurmiendo = tiempoDurmiendo;
    }

    public int getQuantum() {
        return quantum;
    }

    public void setQuantum(int quantum) {
        this.quantum = quantum;
    }

    public boolean esPrimerQuantum() {
        return esPrimerQuantum;
    }

    public void setEsPrimerQuantum(boolean esPrimerQuantum) {
        this.esPrimerQuantum = esPrimerQuantum;
    }

    public int getTiempoEsperando() {
        return tiempoEsperando;
    }

    public void setTiempoEsperando(int tiempoEsperando) {
        this.tiempoEsperando = tiempoEsperando;
    }
// ========================     FIN Getters/Setters     ========================
    
    @Override
    public String toString() {
/*        return "Proceso{" + "pid=" + pid + ", esTiempoReal=" + esTiempoReal + 
                ", prioridadEstatica=" + prioridadEstatica + ", tiempoCPU=" + 
                tiempoCPU + ", tiempoEntrada=" + tiempoEntrada + ", estado=" + 
                estado + ", prioridadDinamica=" + prioridadDinamica + 
                ", tiempoDurmiendo=" + tiempoDurmiendo + ", quantum=" + 
                quantum + ", esPrimerQuantum=" + esPrimerQuantum + '}';
*/
        return "[" + pid + "," + prioridadEstatica + "," + prioridadDinamica + "," 
                   + quantum + "," + tiempoCPU +"]";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Proceso other = (Proceso) obj;
        if (this.pid != other.pid) {
            return false;
        }
        return true;
    }
    
}