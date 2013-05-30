/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 * Esta clase simula la estructura Process Descriptor
 */
package Funcionalidad;

import Constantes.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hector
 */
public class Proceso {

    private int pid;
    private boolean esTiempoReal;
    private int prioridadEstatica;          // static_prior
    private ArrayList<Integer> tiemposCPU;  // Cada tiempo que pasa en CPU
    private ArrayList<Integer> tiemposIO;   // Cada tiempo que pasa en IO
    private int tiempoEntrada;
    private short estado;                   // state
    private int prioridadDinamica;          // prior
    private int tiempoDurmiendo;            // sleep_avg (ticks)
    private int quantum;                    // time_slice
    private boolean esPrimerQuantum;        // first_time_slice
    private int tiempoEsperando;            // (ticks)
    private boolean terminado;              // Indica si esperar o finalizar
    
    private int tiempoTotalDurmiendo;       // (ticks)

    public Proceso(int pid, boolean esTiempoReal, int prioridadEstatica, ArrayList<Integer> tiempoCPU, ArrayList<Integer> tiempoIO, int tiempoEntrada) {
        this.pid = pid;
        this.esTiempoReal = esTiempoReal;
        this.prioridadEstatica = prioridadEstatica;
        this.tiemposCPU = tiempoCPU;
        this.tiemposIO = tiempoIO;
        
        /*Con estos dos ponemos un 0 al final de la lista, esto para cuidar
         que agarre tiempo raros*/
        if (this.tiemposCPU != null) {
            this.tiemposCPU.add(0);
        }
        if (this.tiemposIO != null) {
            this.tiemposIO.add(0);
        }
        
        this.tiempoEntrada = tiempoEntrada;
        this.estado = Constantes.TASK_RUNNING;
        if (esTiempoReal) {
            this.prioridadDinamica = 100;
        } else {
            this.prioridadDinamica = prioridadEstatica;
        }
        this.tiempoDurmiendo = 0;
        this.esPrimerQuantum = true;

        this.quantum = (140 - prioridadEstatica) * (prioridadEstatica < 120 ? 20 : 5);
        this.quantum = java.lang.Math.min(this.quantum, tiempoCPU.get(0));
        
        tiempoTotalDurmiendo = 0;
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

    public int getTiemposCPU() {
        
        if (tiemposCPU.get(0) != null){
            return (int) tiemposCPU.get(0);
        }else{
            return -1;
        }
    }
    
    public void setTiemposCPU(int tiemposCPU) {
        if (tiemposCPU == 0) {
            this.tiemposCPU.remove(0);
        } else {
            this.tiemposCPU.set(0, tiemposCPU);
        }
    }
    
    public int getTiemposIO() {
        
        if (tiemposIO != null){
            return (int) tiemposIO.get(0);
        }else{
            return -1;
        }
    }
    
    public void setTiemposIO(int tiemposIO) {
       
        if (tiemposIO == 0) {
            this.tiemposIO.remove(0);
        } else {
            this.tiemposIO.set(0, tiemposIO);
        }
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

    public boolean getTerminado() {
        return terminado;
    }

    public void setTerminado(boolean terminado) {
        this.terminado = terminado;
    }

    public int getTiempoTotalDurmiendo() {
        return tiempoTotalDurmiendo;
    }

// ========================     FIN Getters/Setters     ========================
    
    public void aumentarTiempoTotalDurmiendo(){
        tiempoTotalDurmiendo++;
    }
    public void decrementarTiempoDurmiendo(){
        if (tiempoDurmiendo > 0) tiempoDurmiendo--;
    }
    
    public void aumentarTiempoDurmiendo(){
        if (tiempoDurmiendo < 1000) tiempoDurmiendo++;
    }
    
    public void aumentarTiempoEnEspera(){
        tiempoEsperando++;
    }
    
    @Override
    public String toString() {
/*
        return "[" + pid + "," + prioridadEstatica + "," + prioridadDinamica + ","
                + quantum + ","+ tiemposCPU.toString()+ "," + tiemposIO.toString() + "]";
*/      
        
        return "[" + pid + "," + prioridadEstatica + "," + prioridadDinamica + ","
                + quantum + ","+ tiemposCPU.get(0) + "]";
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