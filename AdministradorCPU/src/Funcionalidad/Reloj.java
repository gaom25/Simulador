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
public class Reloj extends Thread{
    private int tick;   // frecuencia de alarma de tiempo (ms)
    private int numTicks;
    private Cpu cpu;    // CPU al que se le notifica c/tick

    public Reloj(int tick) {
        this.tick = tick;
        this.cpu = null;
        numTicks = 0;
    }

// ========================     Getters/Setters         ========================    
    public int getTick() {
        return tick;
    }

    public void setTick(int tick) {
        this.tick = tick;
    }

    public Cpu getCpu() {
        return cpu;
    }

    public void setCpu(Cpu cpu) {
        this.cpu = cpu;
    }

    public int getNumTicks() {
        return numTicks;
    }

    public void setNumTicks(int numTicks) {
        this.numTicks = numTicks;
    }
// ========================     FIN Getters/Setters     ========================
    
    @Override
    public void run() {
        cpu.start();
        System.out.println("hola");
        if (cpu != null){
            System.out.println("hola2");
            numTicks++;
            while(true){
                System.out.println("hola3");
                cpu.notifica();
                try {
                    Reloj.sleep(tick);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Reloj.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("ERROR durmiendo Reloj");
                    return;
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Reloj{" + "tick=" + tick + ", numTicks=" + numTicks + 
                ", cpuId=" + cpu.getCpuId() + '}' + "\n" +
                cpu.getPlanificador().toString();
    }
}