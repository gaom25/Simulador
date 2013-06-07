/**
 * Sistemas de Operacion II.
 * Proyecto # 1
 * Simulador del kernel 2.6 de Linux
 * Hector Dominguez 09-10241
 * Carlos Aponte 09-10041
 * Krysler Pinto 09-10661
 * Gustavo Ortega 09-10590
 */
package Funcionalidad;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hector
 */
public class Cpu extends Thread {

    /**
     * Identicador del proceso.
     */
    private short cpuId;
    /**
     * Proceso activo del CPU.
     */
    private Proceso procesoActual;
    /**
     * Lista con las colas de activos y expirados.
     */
    private Runqueue runqueue;
    /**
     * Planificador de procesos.
     */
    private Planificador planificador;
    /**
     * Numero de ticks ociosos.
     */
    private int tiempoOcioso;
    /**
     * Lista con todos los procesos que se van a ejecutar en la simulacion.
     */
    private ArrayList<Proceso>[] todosProcesos;
    /**
     * Cantidad de procesos a simular.
     */
    private int totalProcesos;
    /**
     * Booleano que indica cuando detener la simulacion.
     */
    private boolean simulacion;

    public Cpu(short id, Runqueue runqueue, ArrayList<Proceso>[] procesos) {
        this.cpuId = id;
        this.runqueue = runqueue;
        procesoActual = null;
        planificador = null;
        tiempoOcioso = 0;

        /**
         * Para que los procesos puedan entrar en distintos tiempos.
         */
        todosProcesos = procesos;
        totalProcesos = 0;
        for (int i = 0; i < procesos.length; i++) {
            totalProcesos += procesos[i].size();
        }
        simulacion = true;
    }

// ========================     Getters/Setters         ========================
    public short getCpuId() {
        return cpuId;
    }

    public int getTotalProcesos() {
        return totalProcesos;
    }

    public void setCpuId(short id) {
        this.cpuId = id;
    }

    public Proceso getProcesoActual() {
        return procesoActual;
    }

    public void setProcesoActual(Proceso procesoActual) {
        this.procesoActual = procesoActual;
    }

    public Runqueue getRunqueue() {
        return runqueue;
    }

    public void setRunqueue(Runqueue runqueue) {
        this.runqueue = runqueue;
    }

    public Planificador getPlanificador() {
        return planificador;
    }

    public void setPlanificador(Planificador planificador) {
        this.planificador = planificador;
    }

    public int getTiempoOcioso() {
        return tiempoOcioso;
    }

    public void setTiempoOcioso(int tiempoOcioso) {
        this.tiempoOcioso = tiempoOcioso;
    }

    public void setSimulacion(boolean simulacion) {
        this.simulacion = simulacion;
    }

    // ========================     FIN Getters/Setters     ========================
    /**
     * Procedimientos para agregar los procesos en difentes ticks del reloj.
     */
    public void agregarProcesosNuevos() {
        int numTicks = planificador.getReloj().getNumTicks();
        if (!todosProcesos[numTicks].isEmpty()) {
            for (int i = 0; i < todosProcesos[numTicks].size(); i++) {
                planificador.agregarNuevoProceso(todosProcesos[numTicks].get(i));
            }
        }
    }

    @Override
    public void run() {
        /**
         * Monitor para que se ejecute la funcion cada tick de reloj.
         */
        synchronized (this) {
            while (simulacion) {
                try {
                    wait();
                } catch (InterruptedException ex) {
                    System.out.println("ERROR DURMIENDO CPU");
                    return;
                }
                /**
                 * Se aumenta el tiempo de espera de los procesos no activos del
                 * CPU
                 */
                runqueue.aumentarTiempoEnEspera();
                /**
                 * Si aun no ha llegado a los 100 ticks, se van despertando los
                 * procesos paulatinamente
                 */
                if (planificador.getReloj().getNumTicks() <= 100) {
                    agregarProcesosNuevos();
                }

                /**
                 * Se actualiza el quantum del proceso actual, si lo hay
                 */
                if (procesoActual != null) {
                    planificador.actualizarQuantum(procesoActual);
                    /**
                     * Se aumenta el tiempo ocioso del CPU si no tiene proceso
                     * activo
                     */
                } else {
                    tiempoOcioso++;
                }
            }
        }
    }

    /**
     * Notifacion para que el CPU se active y haga su ejecucion
     */
    public synchronized void notifica() {
        this.notifyAll();
    }

    @Override
    public String toString() {
        return "Cpu{" + "id=" + cpuId + ", procesoActual=" + procesoActual
                + ", runqueue=\n\t\t" + runqueue + '}';
    }
}