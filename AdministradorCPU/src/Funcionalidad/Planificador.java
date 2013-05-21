/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 * Simula el sistema de computacion
 */
package Funcionalidad;

import java.util.ArrayList;
import Constantes.*;

/**
 *
 * @author hector
 */
public class Planificador extends Thread {

    Cpu cpu;
    ArrayList<Proceso> listaFinalizados;
    DispositivoIO dispositivoIO;
    Reloj reloj;

    public Planificador(String NombreArch, Reloj reloj) {
        // Tiene todos los procesos del archivo procesos.xml
        ArrayList<Proceso> procesos = LectorXML.obtenerProcesos(NombreArch);
        // Crea 140 listas: una para c/prioridad. Activos tendra los procesos en procesos.xml
        ListasDePrioridades listasActivos = new ListasDePrioridades(procesos);
        ListasDePrioridades listasExpirados = new ListasDePrioridades();
        // Crea "cola de listos"
        Runqueue colaListos = new Runqueue(listasActivos, listasExpirados);
        // Asocia al CPU su runqueue: es una por c/CPU.
        cpu = new Cpu((short) 1, colaListos);
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

    public boolean insertarListaFinalizados(Proceso p) {
        boolean agrego = false;
        if (listaFinalizados.indexOf(p) == -1) {
            p.setEstado(Constantes.EXIT_DEAD);
            agrego = this.listaFinalizados.add(p);
        }
        return agrego;
    }

    public int obtenerBono(Proceso p) {
        return p.getTiempoDurmiendo() / 100;
    }

    public int actualizarPrioridadDinamica(Proceso p) {
        int prioridadDinamica = java.lang.Math.max(100,
                java.lang.Math.min(p.getPrioridadEstatica() - this.obtenerBono(p) + 5, 139));

        p.setPrioridadDinamica(prioridadDinamica);
        return prioridadDinamica;
    }

    // Funcion: scheduler_tick()                                           *****
    public int actualizarQuantum(Proceso p) {
        // OJO: 4: adquiere lock de runqueue
        // 5:
        int prioridadEstatica = p.getPrioridadEstatica();
        int quantum = p.getQuantum();

        // Siempre es eliminado el proceso de Activos
        p.setQuantum(--quantum);
        // OJO: HAY que decrementar p.sleep_avg en Reloj.tick
        if (quantum == 0) {
            // Suponiendo que tiempoCPU es el numero de veces que el proceso 
            // necesita utilizar el CPU:
            p.setTiempoCPU(p.getTiempoCPU() - 1);
            // Fin

            quantum = (140 - prioridadEstatica) * (prioridadEstatica < 120 ? 20 : 5);
            p.setQuantum(quantum);
            p.setEsPrimerQuantum(false);

            if (p.esTiempoReal()) { // Suponemos que son Round Robin                
                // set_tsk_need_resched(current):
                //cpu.setProcesoActual(null);                   *** Creo que no hace falta
                //cpu.getRunqueue().setProcesoActual(null);     *** Creo que no hace falta
                // FIN set_tsk_need_resched(current):
                // Se inserta a activos:
                cpu.getRunqueue().getActivos().insertarProceso(p);
            } else {
                this.actualizarPrioridadDinamica(p);

                if (cpu.getRunqueue().getTiempoPrimerExpirado() == 0) {
                    cpu.getRunqueue().setTiempoPrimerExpirado(reloj.getNumTicks());
                }
                // f: Asumimos que siempre añadiremos a expirados
                cpu.getRunqueue().insertarProcesoExpirado(p);
            }

            this.asignarCPU(); // OJO
        }

        return quantum;
    }

    // Funcion: try_to_wake_up()                                          *****
    public boolean despiertaProceso(Proceso p, int numTicks) {
        boolean despertado = false;
        int prioridadDinamica;

        if (p.getEstado() == Constantes.TASK_INTERRUPTIBLE) {
            prioridadDinamica = this.reCalcularPrioridadDinamica(p, numTicks);
            p.setEstado(Constantes.TASK_RUNNING);
            cpu.getRunqueue().insertarProcesoActivo(p);
            if (cpu.getProcesoActual().getPrioridadDinamica() < prioridadDinamica) {
                cpu.setProcesoActual(null);
                cpu.getRunqueue().setProcesoActual(null);
                this.asignarCPU();
            }
            despertado = true;
        }

        return despertado;
    }

    // Funcion: recalc_task_prior()                                        *****
    public int reCalcularPrioridadDinamica(Proceso p, int numTicks) {
        int prioridadDinamica = this.actualizarPrioridadDinamica(p);
        /*NOOOOOOOOO creo que sea necesaria esta funcion*/
        return prioridadDinamica;

    }

    // Funcion: schedule()                                                 *****
    public void asignarCPU() {
        Proceso p = cpu.getRunqueue().obtenerMejorProceso();
        if (p != null) {
            cpu.setProcesoActual(p);
            cpu.getRunqueue().setProcesoActual(p);
        } else {
            cpu.getRunqueue().intercambioActivosExpirados();
            p = cpu.getRunqueue().obtenerMejorProceso();

            if (p != null) {
                // OJO: Procesos convecionales: pag 281
                cpu.setProcesoActual(p);
                cpu.getRunqueue().setProcesoActual(p);
            } else {
                this.finalizarSimulacion();
            }
        }
        // OJO: Se debe terminar la ejecucion en finalizarSimulacion()
        cpu.getRunqueue().aumentarNumProcesosCambiados();
    }

    public void finalizarSimulacion() {
        System.out.println("TERMINO SIMULACION!!");
        // Detener todos los hilos
        // Mostrar estadisticas
        // Terminar simulacion
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
