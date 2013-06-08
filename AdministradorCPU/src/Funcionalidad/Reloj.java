
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

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hector
 */
public class Reloj extends Thread{
    private int tick;   /* frecuencia de alarma de tiempo (ms)*/
    private int numTicks; /* tiempo transcurrido */
    private Cpu cpu;    /* CPU al que se le notifica c/tick */
    private DispositivoIO dispositivo;
    private boolean simulacion;

    public Reloj(int tick) {
        this.tick = tick;
        this.cpu = null;
        this.dispositivo = null;
        numTicks = -1;
        
        simulacion = true;
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

    public DispositivoIO getDispositivo() {
        return dispositivo;
    }

    public void setDispositivo(DispositivoIO dispositivo) {
        this.dispositivo = dispositivo;
    }
    
    public int getNumTicks() {
        return numTicks;
    }

    public void setNumTicks(int numTicks) {
        this.numTicks = numTicks;
    }
    
    public void setSimulacion(boolean simulacion) {
        this.simulacion = simulacion;
    }
// ========================     FIN Getters/Setters     ========================
    
    /*Hilo principal del Reloj se encarga de llevar el paso del tiempo asi como
     * notificar a los componentes de dihco paso
     */
    @Override
    public void run() {
        cpu.start();
        dispositivo.start();
        if (cpu != null){
            while(simulacion){
                numTicks++; /* Paso del tiempo */
                /* Notificacion del paso del tiempo */
                cpu.notifica(); 
                dispositivo.notifica();
                try {
                    Reloj.sleep(tick);
                } catch (InterruptedException ex) {
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
