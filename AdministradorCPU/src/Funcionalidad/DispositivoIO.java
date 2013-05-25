/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Funcionalidad;

import Constantes.Constantes;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
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

    public void setColaBloqueados(ArrayList<Proceso> colaBloqueados) {
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
    public boolean insertarColaBloqueados(Proceso p) {
        boolean agrego = false;
        if (colaBloqueados.indexOf(p) == -1) {
            planificador.getCpu().getRunqueue().getActivos().eliminarProceso(p);
            p.setEstado(Constantes.TASK_INTERRUPTIBLE);
            agrego = this.colaBloqueados.add(p);
        }
        return agrego;
    }

    public boolean eliminarColaBloqueados(Proceso p) {
        return colaBloqueados.remove(p);
    }

    public void popProceso() {
        this.colaBloqueados.remove(0);
    }

    /*Es desde uno pues el primero no se supone que esta dormido esta
     activo haciendo I/O*/
    public void aumentarTiempo() {
        /*aumentaria el tiempo si es que hay algo en las cola, si no pasa de largo*/
        if (colaBloqueados.size() > 0) {
            for (int i = 1; i < colaBloqueados.size(); i++) {
                Proceso p = colaBloqueados.get(i);
                p.setTiempoDurmiendo(p.getTiempoDurmiendo() + 1);
            }

            Proceso enIO = colaBloqueados.get(0);
            int Tactual = enIO.getTiemposIO();

            enIO.setTiemposIO(Tactual - 1);

                    
            if ((enIO.getTiemposIO() == 0) && (enIO.getTiemposCPU() != 0)) {
                /*Lo mando a la cola de expirados del cpu */
                this.popProceso();
                planificador.insertarListaExpirados(enIO);
                System.out.println("Proceso movido a la cola de expirados" + enIO.toString());
            } else if (enIO.getTiemposIO() == 0) {
                /*Lo mando a la cola de finalizados */
                this.popProceso();
                planificador.insertarListaFinalizados(enIO);
                System.out.println("Proceso terminado en IO" + enIO.toString());
            }
        }
    }

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

                this.aumentarTiempo();
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
