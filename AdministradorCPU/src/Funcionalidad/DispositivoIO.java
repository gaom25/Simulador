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

import Constantes.Constantes;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author hector
 */
public class DispositivoIO extends Thread {

    ArrayList<Proceso> colaBloqueados;
    Proceso procesoActual;
    Planificador planificador;

    public DispositivoIO() {
        // Crea "cola de bloqueados"
        colaBloqueados = new ArrayList<Proceso>();
        procesoActual = null;
        planificador = null;
    }

// ========================     Getters/Setters         ========================
    public ArrayList<Proceso> getColaBloqueados() {
        return colaBloqueados;
    }

    public synchronized void setColaBloqueados(ArrayList<Proceso> colaBloqueados) {
        this.colaBloqueados = colaBloqueados;
    }

    public Proceso getProcesoActual() {
        return procesoActual;
    }

    public void setProcesoActual(Proceso procesoActual) {
        this.procesoActual = procesoActual;
    }

    public Planificador getPlanificador() {
        return planificador;
    }

    public void setPlanificador(Planificador planificador) {
        this.planificador = planificador;
    }

// ========================     FIN Getters/Setters     ========================
   
    /*Se agrega un proceso especifico a la cola de bloqueados*/
    public synchronized boolean insertarColaBloqueados(Proceso p) {
        boolean agrego = false;

        /* Si el proceso no esta ya bloqueado, lo agrega a la cola.*/
        if (colaBloqueados.indexOf(p) == -1) {
            p.setEstado(Constantes.TASK_INTERRUPTIBLE);
            agrego = this.colaBloqueados.add(p);
        }
        return agrego;
    }

    /*Se saca el primer proceso de la cola de bloqueados y se retorna ese proceso */
    public synchronized Proceso popProceso() {
        if (!colaBloqueados.isEmpty()) {
            return colaBloqueados.remove(0);
        } else {
            return null;
        }
    }

    /*Se aumenta el tiempo que ha estado dormido un proceso (ticks) */
    public void aumentarTiempo() {
        if (colaBloqueados.size() > 0) {
            for (int i = 0; i < colaBloqueados.size(); i++) {
                colaBloqueados.get(i).aumentarTiempoDurmiendo();
                colaBloqueados.get(i).aumentarTiempoTotalDurmiendo();
            }
        }
    }

    /*Hilo principal del dispositivo I/O
     * Maneja que proceso esta en el dispositivo, como se maneja la cola de
     * bloqueados, que tiempo llevan esperando, etc.
     */
    @Override
    public void run() {
        synchronized (this) {
            while (true) {
                try {
                    wait();
                } catch (InterruptedException ex) {
                    System.out.println("ERROR DURMIENDO DISPOSITIVO");
                    return;
                }
                /*Paso un tick, aumentamos el tiempo dormidos de los que esperan*/
                this.aumentarTiempo();

                /*Cuando hay un proceso en el dispositivo */
                if (procesoActual != null) {

                    /*Decrementamos el tiempo I/O del proceso*/
                    int tmpActual = procesoActual.getTiemposIO() - 1;
                    procesoActual.setTiemposIO(tmpActual);
                  
                    /* Termino IO, lo despertamos para que vuelva al CPU */
                    if (tmpActual == 0) {
                        System.out.println("Proceso " + (planificador.despiertaProceso(procesoActual) ? "" : "NO ")
                                + "Despertado " + procesoActual.toString());
                        procesoActual = popProceso();
                    }
                } else {
                    procesoActual = popProceso();
                }
            }
        }
    }

    public synchronized void notifica() {
        this.notify();
    }

    @Override
    public String toString() {
        return "DispositivoIO{" + "colaBloqueados=" + colaBloqueados + '}';
    }
}
