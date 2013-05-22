/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Funcionalidad;

import Constantes.Constantes;
import java.util.ArrayList;

/**
 *
 * @author hector
 */
public class DispositivoIO extends Thread {

    ArrayList<Proceso> colaBloqueados;
    Proceso procesoActual;

    public DispositivoIO() {
        // Crea "cola de bloqueados"
        colaBloqueados = new ArrayList<Proceso>();
        procesoActual = null;
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
// ========================     FIN Getters/Setters     ========================

    public boolean insertarColaBloqueados(Proceso p) {
        boolean agrego = false;
        if (colaBloqueados.indexOf(p) == -1) {
            p.setEstado(Constantes.TASK_INTERRUPTIBLE);
            agrego = this.colaBloqueados.add(p);
        }
        return agrego;
    }

    public boolean eliminarColaBloqueados(Proceso p) {
        return colaBloqueados.remove(p);
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
            /*Menor o igual a 1 pues lo linea de arriba lo rebajaria a 0 y significaria
             que termino su IO*/
            if (Tactual <= 1) {
                /*SACAR LE PROCESO DE AQUI, es decir despertarlo*/
            }
        }
    }

    @Override
    public String toString() {
        return "DispositivoIO{" + "colaBloqueados=" + colaBloqueados + '}';
    }
}
