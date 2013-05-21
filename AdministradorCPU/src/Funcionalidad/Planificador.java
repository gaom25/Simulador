/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 * Simula el sistema de computacion
 */
package Funcionalidad;

import java.util.ArrayList;
import Constantes.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hector
 */
public class Planificador extends Thread{
    Cpu cpu;
    ArrayList<Proceso> listaFinalizados;
    DispositivoIO dispositivoIO;
    Reloj reloj;
    
    public Planificador(String NombreArch, Reloj reloj) {
        // Tiene todos los procesos del archivo procesos.xml
        ArrayList<Proceso> procesos = LectorXML.obtenerProcesos(NombreArch);
        // Crea 140 listas: una para c/prioridad. Activos tendra los procesos en procesos.xml
        ListasDePrioridades listasActivos   = new ListasDePrioridades(procesos);
        ListasDePrioridades listasExpirados = new ListasDePrioridades();
        // Crea "cola de listos"
        Runqueue colaListos = new Runqueue(listasActivos, listasExpirados);
        // Asocia al CPU su runqueue: es una por c/CPU.
        cpu = new Cpu( (short) 1, colaListos);
        cpu.setPlanificador(this);
        cpu.setName("Cpu");
        
        listaFinalizados = new ArrayList<Proceso>();
        dispositivoIO = new DispositivoIO();
        
        this.reloj = reloj;
    }

// ========================     Getters/Setters         ========================
    public Cpu getCpu() {
        return cpu;
    }

    public void setCpu(Cpu cpu) {
        this.cpu = cpu;
    }

    public ArrayList<Proceso> getListaFinalizados() {
        return listaFinalizados;
    }

    public void setListaFinalizados(ArrayList<Proceso> listaFinalizados) {
        this.listaFinalizados = listaFinalizados;
    }
    
    public DispositivoIO getDispositivoIO() {
        return dispositivoIO;
    }

    public void setDispositivoIO(DispositivoIO dispositivoIO) {
        this.dispositivoIO = dispositivoIO;
    }
// ========================     FIN Getters/Setters     ========================
    
    public boolean insertarListaFinalizados(Proceso p){
        boolean agrego = false;
        if (listaFinalizados.indexOf(p) == -1){
            p.setEstado(Constantes.EXIT_DEAD);
            agrego = this.listaFinalizados.add(p);
        }
        return agrego;
    }
    
    public int obtenerBono(Proceso p){
        return p.getTiempoDurmiendo()/100;
    }
    
    public int actualizarPrioridadDinamica(Proceso p){
        int prioridadDinamica = java.lang.Math.max(100,
                        java.lang.Math.min(p.getPrioridadEstatica() - this.obtenerBono(p) + 5, 139) );
                
        p.setPrioridadDinamica(prioridadDinamica);
        return prioridadDinamica;
    }
    
    // Funcion: scheduler_tick()                                           *****
    public void actualizarQuantum(Proceso p){
        Boolean asigno = false;
        // OJO: 4: adquiere lock de runqueue
        // 5:
        int prioridadEstatica = p.getPrioridadEstatica();
        int quantum = p.getQuantum();
        
        // Siempre es eliminado el proceso de Activos
        p.setQuantum(--quantum);
        // Disminuye num de Ticks que necesita para terminar ejecucion
        p.setTiempoCPU(p.getTiempoCPU()-1);
        // OJO: HAY que decrementar p.sleep_avg en Reloj.tick
        if (quantum == 0){
            // set_tsk_need_resched(current):
            cpu.setProcesoActual(null);
            cpu.getRunqueue().setProcesoActual(null);
            // FIN set_tsk_need_resched(current):
            
            // Si todavia le queda trabajo al proceso
            if (p.getTiempoCPU() != 0){
            
                quantum = (140 - prioridadEstatica)*(prioridadEstatica < 120 ? 20 : 5);
                p.setQuantum( java.lang.Math.min(quantum,p.getTiempoCPU()) );
                p.setEsPrimerQuantum(false);

                if (p.esTiempoReal()) { // Suponemos que son Round Robin                
                    // Se inserta a activos:
                    cpu.getRunqueue().getActivos().insertarProceso(p);
                } else {
                    this.actualizarPrioridadDinamica(p);

                    if (cpu.getRunqueue().getTiempoPrimerExpirado() == 0) 
                        cpu.getRunqueue().setTiempoPrimerExpirado(reloj.getNumTicks());
                    // f: Asumimos que siempre añadiremos a expirados
                    cpu.getRunqueue().insertarProcesoExpirado(p);
                }
                
            }else{
                listaFinalizados.add(p);
            }
            
            System.out.println("actualizacion valores: "+ p.toString());
            
            asigno = this.asignarCPU(); // OJO
        }
    }
    
    // Funcion: try_to_wake_up()                                          *****
    public boolean despiertaProceso(Proceso p){
        boolean despertado = false;
        int prioridadDinamica;
        
        if (p.getEstado() == Constantes.TASK_INTERRUPTIBLE){
            prioridadDinamica = this.reCalcularPrioridadDinamica(p, reloj.getNumTicks());
            p.setEstado(Constantes.TASK_RUNNING);
            // Si CPU esta ocioso (porque no runqueue esta vacia)
            if (cpu.getProcesoActual() == null){
                cpu.setProcesoActual(p);
                cpu.getRunqueue().setProcesoActual(p);
            }else{
                cpu.getRunqueue().insertarProcesoActivo(p);
                // Se verifica si tiene mayor o menor prioridad que el que esta en CPU
                if (cpu.getProcesoActual().getPrioridadDinamica() < prioridadDinamica){

                    cpu.getRunqueue().insertarProcesoActivo(cpu.getProcesoActual());
                    cpu.setProcesoActual(null);
                    cpu.getRunqueue().setProcesoActual(null);
                    this.asignarCPU();
                }
            }
            
            despertado = true;
        }
        
       return despertado;
    }
    
    // Funcion: recalc_task_prior()                                        *****
    public int reCalcularPrioridadDinamica(Proceso p, int numTicks){
        int prioridadDinamica = this.actualizarPrioridadDinamica(p);
        /*NOOOOOOOOO creo que sea necesaria esta funcion*/
        return prioridadDinamica;
        
    }

    // Funcion: schedule()                                                 *****
    public boolean asignarCPU(){
        Proceso p = cpu.getRunqueue().obtenerMejorProceso();
        
        if (p != null){
            cpu.getRunqueue().eliminarProcesoActivo(p);
            cpu.setProcesoActual(p);
            cpu.getRunqueue().setProcesoActual(p);
        }else{
            cpu.getRunqueue().intercambioActivosExpirados();
            p = cpu.getRunqueue().obtenerMejorProceso();
            
            if (p != null){
                cpu.getRunqueue().eliminarProcesoActivo(p);
                // OJO: Procesos convecionales: pag 281
                cpu.setProcesoActual(p);
                cpu.getRunqueue().setProcesoActual(p);
            }
        }
        
        // OJO: Se debe terminar la ejecucion en finalizarSimulacion()
        if (p == null){  System.out.println("RUNQUEUE VACIA!!"); return false; }
        else{            cpu.getRunqueue().aumentarNumProcesosCambiados(); return true; }
    }
    
    @Override
    public void run() {
        super.run(); // ***** OJO
    }

    @Override
    public String toString() {
        return "Planificador{\n" + "\tcpu=" + cpu + ",\n}";
    }
}
