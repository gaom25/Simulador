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

        dispositivoIO = new DispositivoIO();
        dispositivoIO.setPlanificador(this);

        listaFinalizados = new ArrayList<Proceso>();

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

    /* Mueve el proceso p a la cola de expirados*/
    public void insertarListaExpirados(Proceso p) {
        boolean b = cpu.getRunqueue().insertarProcesoExpirado(p);
    }

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
    public void actualizarQuantum(Proceso p) {
        Boolean asigno = false;
        // OJO: 4: adquiere lock de runqueue
        // 5:
        int prioridadEstatica = p.getPrioridadEstatica();
        int quantum = p.getQuantum();
        int tcpu = p.getTiemposCPU();

        // Siempre es eliminado el proceso de Activos
        p.setQuantum(--quantum);
        // Disminuye num de Ticks que necesita para terminar ejecucion
        p.setTiemposCPU(--tcpu);
        // OJO: HAY que decrementar p.sleep_avg en Reloj.tick
        p.decrementarTiempoDurmiendo();

        if ( quantum <= 0 ) {

            // set_tsk_need_resched(current):
            cpu.setProcesoActual(null);
            cpu.getRunqueue().setProcesoActual(null);
            // FIN set_tsk_need_resched(current):

            // Si todavia le queda trabajo al proceso
            if (tcpu > 0) {

                quantum = (140 - prioridadEstatica) * (prioridadEstatica < 120 ? 20 : 5);
                p.setQuantum( java.lang.Math.min(quantum, tcpu) );
                p.setEsPrimerQuantum(false);

                if (p.esTiempoReal()) { // Suponemos que son Round Robin                
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

                /*p.getTiemposIO() devuelve un entero si es -1 es que no hay 
                 * lista de tiempos para entrada salida*/
            } else if (p.getTiemposIO() != 0 && p.getTiemposIO() != -1) {
                // Mandar proceso actual a la cola IO
                if (dispositivoIO.insertarColaBloqueados(p)){
                    System.out.println("Insertado en cola de dispositivo");
                    System.out.println(dispositivoIO.getColaBloqueados().toString());
                } else System.out.println("NO insertado");
            } else {
                listaFinalizados.add(p);
            }

            asigno = this.asignarCPU(); // OJO
        }
        
        System.out.println("actualizacion valores: " + p.toString());
    }

    /*Funciones que busca los procesos que vinieron de IO para ponerlos activos*/
    public void BuscaDespierta() {
        ArrayList<Proceso>[] expirados = this.getCpu().getRunqueue().getExpirados().getListas();
        for (int i = 0; i < expirados.length; i++) {
            boolean a = expirados[i].isEmpty();
            if (!expirados[i].isEmpty()) {
                for (int j = 0; j < expirados[i].size(); j++) {
                    if (expirados[i].get(j).getEstado() == Constantes.TASK_INTERRUPTIBLE) {
                        this.despiertaProceso(expirados[i].get(j));
                    }
                }
            }
        }
    }

    /*Funcion que busca si hay algun proceso que haya terminado su tiempo de IO*/
    // OJO: ?? no es necesaria =S. En expirados SOLO deben estar los despiertos
    public boolean hayDormido() {
        ArrayList<Proceso>[] expirados = this.getCpu().getRunqueue().getExpirados().getListas();
        for (int i = 0; i < expirados.length; i++) {
            if (expirados[i].size() >= 0) {
                for (int j = 0; j < expirados[i].size(); j++) {
                    if (expirados[i].get(j).getEstado() == Constantes.TASK_INTERRUPTIBLE) {
                        return true;
                    }

                }
            }
        }
        return false;

    }

    // Funcion: try_to_wake_up()                                          *****
    public boolean despiertaProceso(Proceso p) {
        boolean despertado = false;
        int prioridadDinamica;
        int quantum = (140 - p.getPrioridadEstatica()) * (p.getPrioridadEstatica() < 120 ? 20 : 5);
        
        if (p.getTiemposCPU() == 0){
            p.setEstado(Constantes.EXIT_DEAD);
            listaFinalizados.add(p);
            System.out.println("LISTA FINALIZADOS");
            System.out.println(listaFinalizados);
        }else{
            p.setQuantum( java.lang.Math.min(quantum, p.getTiemposCPU()) );

            if (p.getEstado() == Constantes.TASK_INTERRUPTIBLE) {
                System.out.println("RECALCULA PRIORIDAD DINAMICA");
                prioridadDinamica = this.reCalcularPrioridadDinamica(p, reloj.getNumTicks());
                p.setEstado(Constantes.TASK_RUNNING);
                // Si CPU esta ocioso (porque runqueue esta vacia)
                if (cpu.getProcesoActual() == null) {
                    cpu.setProcesoActual(p);
                    cpu.getRunqueue().setProcesoActual(p);
                } else {
                    cpu.getRunqueue().insertarProcesoActivo(p);
                    // Se verifica si tiene mayor o menor prioridad que el que esta en CPU
                    if (cpu.getProcesoActual().getPrioridadDinamica() > prioridadDinamica) {
                        System.out.println("EXPROPIANDO");
                        cpu.getRunqueue().insertarProcesoActivo(cpu.getProcesoActual());
                        cpu.setProcesoActual(null);
                        cpu.getRunqueue().setProcesoActual(null);
                        this.asignarCPU();
                    }
                }

                despertado = true;
            }
            
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
    public boolean asignarCPU() {
        Proceso p = cpu.getRunqueue().obtenerMejorProceso();

        if (p != null) {
            cpu.getRunqueue().eliminarProcesoActivo(p);
            cpu.setProcesoActual(p);
            cpu.getRunqueue().setProcesoActual(p);
        } else {
            cpu.getRunqueue().intercambioActivosExpirados();
            System.out.println("Hubo Intercambio!!");
            p = cpu.getRunqueue().obtenerMejorProceso();

            if (p != null) {
                cpu.getRunqueue().eliminarProcesoActivo(p);
                // OJO: Procesos convecionales: pag 281
                cpu.setProcesoActual(p);
                cpu.getRunqueue().setProcesoActual(p);
            }
        }

        // OJO: Se debe terminar la ejecucion en finalizarSimulacion()
        if (p == null) {
            System.out.println("RUNQUEUE VACIA!!");
            System.out.println("FINALIZADOS: " + listaFinalizados.size() );
            System.out.println( listaFinalizados.toString() );
            return false;
        } else {
            cpu.getRunqueue().aumentarNumProcesosCambiados();
            return true;
        }
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
