/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Funcionalidad;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hector
 */
public class Cpu extends Thread{
    private short           cpuId;
    private Proceso         procesoActual;
    private Runqueue        runqueue;
    private Planificador    planificador;
    private int             tiempoOcioso; // numTicks ocioso
    
    private ArrayList<Proceso>[] todosProcesos;
    private int totalProcesos;
    
    private boolean simulacion;

    public Cpu(short id, Runqueue runqueue, ArrayList<Proceso>[] procesos) {
        this.cpuId = id;
        this.runqueue = runqueue;

        procesoActual = null;
        planificador = null;
        tiempoOcioso = 0;
        
        // Para los distintos tiempos de Entrada
        todosProcesos = procesos;
        totalProcesos = 0;
        for (int i = 0; i < procesos.length; i++) totalProcesos += procesos[i].size();
        
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
   
    public void agregarProcesosNuevos(){
        int numTicks = planificador.getReloj().getNumTicks();
        if (!todosProcesos[numTicks].isEmpty())
            for (int i = 0; i < todosProcesos[numTicks].size(); i++)
                planificador.agregarNuevoProceso(todosProcesos[numTicks].get(i));
    }
    
    @Override
    public void run() {
        synchronized(this){
            while(simulacion){
                try {
                    wait();
                }catch (InterruptedException ex) {
                    System.out.println("ERROR DURMIENDO CPU");
                    return;
                }
                
                runqueue.aumentarTiempoEnEspera();
                if (planificador.getReloj().getNumTicks() <= 100) agregarProcesosNuevos();
                
                if (procesoActual != null) {
                    planificador.actualizarQuantum(procesoActual);
                }
//                else if (planificador.hayDormido()) {
//                    planificador.BuscaDespierta();
                /*}*/ else tiempoOcioso++;
            }
        }
    }

    public synchronized void notifica() {
        this.notifyAll();
    }

    @Override
    public String toString() {
        return "Cpu{" + "id=" + cpuId + ", procesoActual=" + procesoActual
                + ", runqueue=\n\t\t" + runqueue + '}';
    }
}