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
    public synchronized boolean insertarColaBloqueados(Proceso p) {
        boolean agrego = false;
        // Si no esta: lo agrego
        if (colaBloqueados.indexOf(p) == -1) {
            // NO VA! --> no estará ahi nunca segun lo pensado
            //planificador.getCpu().getRunqueue().getActivos().eliminarProceso(p);
            p.setEstado(Constantes.TASK_INTERRUPTIBLE);
            agrego = this.colaBloqueados.add(p);
        }
        return agrego;
    }

    public synchronized Proceso popProceso() {
        if ( !colaBloqueados.isEmpty() ) return colaBloqueados.remove(0);
        else                             return null;
    }

    public void aumentarTiempo() {
        if (colaBloqueados.size() > 0)
            for (int i = 0; i < colaBloqueados.size(); i++){
                colaBloqueados.get(i).aumentarTiempoDurmiendo();
                colaBloqueados.get(i).aumentarTiempoTotalDurmiendo();
                }
        // OJO: ???
        if (procesoActual != null){ 
            procesoActual.aumentarTiempoDurmiendo();
            procesoActual.aumentarTiempoTotalDurmiendo();
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
                
                if (procesoActual != null){
                    
                    procesoActual.setTiemposIO(procesoActual.getTiemposIO() - 1);
                    // Termino IO
                    if (procesoActual.getTiemposIO() == 0) {
                        // Tiene que hacer CPU ****         != -1 ??
                        if ( (procesoActual.getTiemposIO()  ==  0) && 
                             (procesoActual.getTiemposCPU() != -1)   ) {

                            System.out.println("Proceso "+ (planificador.despiertaProceso(procesoActual) ? "": "NO ") 
                                    + "Despertado "+procesoActual.toString() );
                        } else {
                            /*Lo mando a la cola de finalizados */
                            planificador.insertarListaFinalizados(procesoActual);
                            System.out.println("Proceso TERMINO");
                        }
                        procesoActual = null;
                    }
                } else procesoActual = popProceso();
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
