/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 * Simula listas de procesos listos para ejecutar.
 */
package Funcionalidad;

/**
 *
 * @author hector
 */
public class Runqueue {
    private int numProcesos;                // nr_running
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
        this.prioridadMejorExpirado = 0;
    }

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
    
    public void intercambioActivosExpirados(){
        ListasDePrioridades tmp = this.activos;
        this.activos = this.expirados;
        this.expirados = tmp;
    }
    
    public Proceso obtenerMejorProceso(){
        return this.activos.obtenerMejorProceso();
    }
}