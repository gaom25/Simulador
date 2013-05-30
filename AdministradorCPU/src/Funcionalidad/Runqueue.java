/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 * Simula listas de procesos listos para ejecutar.
 */
package Funcionalidad;
import Constantes.*;

/**
 *
 * @author hector
 */
public class Runqueue {
    private int numProcesos;                // nr_running = activos + expirados
    private int numProcesosCambiados;       // nr_switches   
    private int numProcesosDormidos;        // nr_uninterruptible + nr_iowait
    private int tiempoPrimerExpirado;       // expired_timestamp
    private Proceso procesoActual;          // curr
    private ListasDePrioridades activos;    // active
    private ListasDePrioridades expirados;  // expired
    private int prioridadMejorExpirado;     // best_expired_prior

    public Runqueue(ListasDePrioridades activos, ListasDePrioridades expirados ) {
        this.numProcesos = activos.getNumProcesos() + expirados.getNumProcesos();
        this.numProcesosCambiados = 0;
        this.numProcesosDormidos = 0;
        this.tiempoPrimerExpirado = 0;
        this.procesoActual = null;
        this.activos = activos;
        this.expirados = expirados;
        this.prioridadMejorExpirado = 140;
    }

// ========================     Getters/Setters         ========================    
    public int getNumProcesos() {
        return numProcesos;
    }

    public void setNumProcesos(int numProcesos) {
        this.numProcesos = numProcesos;
    }

    public int getNumProcesosCambiados() {
        return numProcesosCambiados;
    }

    public void setNumProcesosCambiados(int numProcesosCambiados) {
        this.numProcesosCambiados = numProcesosCambiados;
    }

    public int getNumProcesosDormidos() {
        return numProcesosDormidos;
    }

    public void setNumProcesosDormidos(int numProcesosDormidos) {
        this.numProcesosDormidos = numProcesosDormidos;
    }

    public int getTiempoPrimerExpirado() {
        return tiempoPrimerExpirado;
    }

    public void setTiempoPrimerExpirado(int tiempoPrimerExpirado) {
        this.tiempoPrimerExpirado = tiempoPrimerExpirado;
    }

    public Proceso getProcesoActual() {
        return procesoActual;
    }

    public void setProcesoActual(Proceso procesoActual) {
        this.procesoActual = procesoActual;
    }

    public ListasDePrioridades getActivos() {
        return activos;
    }

    public void setActivos(ListasDePrioridades activos) {
        this.activos = activos;
    }

    public ListasDePrioridades getExpirados() {
        return expirados;
    }

    public void setExpirados(ListasDePrioridades expirados) {
        this.expirados = expirados;
    }

    public int getPrioridadMejorExpirado() {
        return prioridadMejorExpirado;
    }

    public void setPrioridadMejorExpirado(int prioridadMejorExpirado) {
        this.prioridadMejorExpirado = prioridadMejorExpirado;
    }
// ========================     FIN Getters/Setters     ========================
    
    public synchronized void intercambioActivosExpirados(){
        ListasDePrioridades tmp = activos;
        activos = expirados;
        expirados = tmp;
        
        tiempoPrimerExpirado = 0;
        prioridadMejorExpirado = 140;
    }
    
    public synchronized  Proceso obtenerMejorProceso(){
        return activos.obtenerMejorProceso();
    }
 
    public synchronized  boolean insertarProcesoActivo(Proceso p){
        
        if (p.getEstado() == Constantes.TASK_RUNNING)
            if (activos.insertarProceso(p)){
                numProcesos++;
                return true;
            }
        
        return false;
    }
 
    public synchronized  boolean insertarProcesoExpirado(Proceso p){
        boolean insertado = false;
        
        if (p.getEstado() == Constantes.TASK_RUNNING || p.getEstado() == Constantes.TASK_INTERRUPTIBLE) {
            insertado = expirados.insertarProceso(p);
        }
        
        if (insertado && this.prioridadMejorExpirado > p.getPrioridadEstatica()){
            this.prioridadMejorExpirado = p.getPrioridadEstatica();
        }
        
        if (insertado){
            numProcesos++;
        }
            
        return insertado;
    }
 
    public synchronized  boolean eliminarProcesoActivo(Proceso p){
        if (activos.eliminarProceso(p)){
            numProcesos--;
            return true;
        }
        return false;
    }
    
    public synchronized  boolean eliminarProcesoExpirado(Proceso p){
        if (expirados.eliminarProceso(p)){
            numProcesos--;
            return true;
        }
        return false;
    }
    
    public synchronized  void aumentarNumProcesosCambiados(){
        numProcesosCambiados++;
    }
    
    public synchronized void aumentarTiempoEnEspera(){
        activos.aumentarTiempoEnEspera();
        expirados.aumentarTiempoEnEspera();
    }

    @Override
    public String toString() {
        return "Runqueue{" + "numProcesos=" + numProcesos + ", numProcesosCambiados=" + 
                numProcesosCambiados + ", numProcesosDormidos=" + numProcesosDormidos + 
                ", tiempoPrimerExpirado=" + tiempoPrimerExpirado + ", procesoActual=" + 
                procesoActual + ", prioridadMejorExpirado=" + prioridadMejorExpirado + 
                ", \n\t\t  activos=" + activos 
                + ", \n\t\t  expirados=" + expirados + '}';
    }
}