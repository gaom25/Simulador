/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Funcionalidad;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hector
 */
public class Cpu extends Thread {

    private short cpuId;
    private Proceso procesoActual;
    private Runqueue runqueue;
    private Planificador planificador;
    private int tiempoOcioso; // (ms)

    public Cpu(short id, Runqueue runqueue) {
        this.cpuId = id;
        this.runqueue = runqueue;

        procesoActual = null;
        planificador = null;
        tiempoOcioso = 0;
    }

// ========================     Getters/Setters         ========================
    public short getCpuId() {
        return cpuId;
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

    // ========================     FIN Getters/Setters     ========================
    @Override
    public void run() {
        synchronized (this) {
            planificador.asignarCPU();
            while (true) {
                try {
                    wait();
                } catch (InterruptedException ex) {
                    System.out.println("ERROR DURMIENDO CPU");
                    return;
                }
                System.out.println("CPU: hola Reloj");
                planificador.actualizarQuantum(procesoActual);
                System.out.println(this.toString());
            }
        }
    }

    public synchronized void notifica() {
        this.notify();
    }

    @Override
    public String toString() {
        return "Cpu{" + "id=" + cpuId + ", procesoActual=" + procesoActual
                + ", runqueue=\n\t\t" + runqueue + '}';
    }
}