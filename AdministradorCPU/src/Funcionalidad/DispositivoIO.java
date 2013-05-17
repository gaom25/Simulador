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
    
    public boolean insertarColaBloqueados(Proceso p){
        boolean agrego = false;
        if (colaBloqueados.indexOf(p) == -1){
            p.setEstado(Constantes.TASK_INTERRUPTIBLE);
            agrego = this.colaBloqueados.add(p);
        }
        return agrego;
    }
    
    public boolean eliminarColaBloqueados (Proceso p){
        return colaBloqueados.remove(p);
    }

    @Override
    public String toString() {
        return "DispositivoIO{" + "colaBloqueados=" + colaBloqueados + '}';
    }
}
