/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 * Simula la estructura prior_array_t
 */
package Funcionalidad;

import java.util.ArrayList;

/**
 *
 * @author hector
 */
public class ListasDePrioridades {
    private int menorPrioridadNoVacia;
    private int numProcesos; // nr_active
    private ArrayList<Proceso>[] listas = new ArrayList[140]; // queue

    public ListasDePrioridades() {
        this.numProcesos = 0;
        for (int i = 0; i < this.listas.length; i++) {
            this.listas[i] = new ArrayList<Proceso>();
        }
        this.menorPrioridadNoVacia = 140;
    }
    
    public ListasDePrioridades(ArrayList<Proceso> procesos){
        Proceso p;
        int prioridad;
        
        this.numProcesos = procesos.size();
        this.menorPrioridadNoVacia = 140;
        
        for (int i = 0; i < this.listas.length; i++)
            this.listas[i] = new ArrayList<Proceso>();
        
        for (int i = 0; i < this.numProcesos; i++) {
            p = procesos.get(i);
            prioridad = p.getPrioridadDinamica();
            if (prioridad < this.menorPrioridadNoVacia)
                this.menorPrioridadNoVacia = prioridad;
            listas[prioridad].add(p);
        }
    }

// ========================     Getters/Setters         ========================
    public int getNumProcesos() {
        return numProcesos;
    }

    public void setNumProcesos(int numProcesos) {
        this.numProcesos = numProcesos;
    }
// ========================     FIN Getters/Setters     ========================

    public boolean insertarProceso(Proceso p){
        int prioridad = p.getPrioridadDinamica();
        boolean agrego = false;
        
        if (this.listas[prioridad].indexOf(p) == -1){
            
            if (prioridad < this.menorPrioridadNoVacia)
                this.menorPrioridadNoVacia = prioridad;

            agrego = this.listas[prioridad].add(p);
            if (agrego) this.numProcesos++;
        }

        return agrego;
    }
    
    public boolean eliminarProceso(Proceso p){
        int prioridad = p.getPrioridadDinamica();
        boolean elimino = this.listas[prioridad].remove(p);
        
        if (elimino){
            this.numProcesos--;
        
            if (this.numProcesos == 0) this.menorPrioridadNoVacia = 140;
            else{
                if (this.listas[prioridad].size() == 0)
                    for (int i = prioridad+1; i < listas.length; i++) {
                        if (this.listas[i].size() != 0){
                            this.menorPrioridadNoVacia = i;
                            break;
                        }
                    }
            }
        }
        
        return elimino;
    }
    
    public Proceso obtenerMejorProceso() {
        if (this.menorPrioridadNoVacia != 140)
            return this.listas[this.menorPrioridadNoVacia].get(0);
        return null;
    }
    
    @Override
    public String toString() {
/*        String s = "ListasDePrioridades{"+ "menorPrioridadNoVacia=" + 
                menorPrioridadNoVacia + ", numProcesos=" + numProcesos + ", listas= \n";
        for (int i = 0; i < listas.length; i++)
            s += "\t\t\t\t\t\t"+ i + ": {" + listas[i].toString() + "}\n";    
        
        s += "\t\t}";
*/
        String s = "{ ";
        int aux = 0;
        for (int i = 0; i < listas.length; i++) {
            for (int j = 0; j < listas[i].size(); j++) {
                s += listas[i].get(j).toString() + ", ";
                if( ++aux == 5) s += "\n";
            }
        }
        return s + "}";
    }
}