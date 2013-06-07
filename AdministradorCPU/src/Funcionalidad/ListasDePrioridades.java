/**
 * Sistemas de Operacion II.
 * Proyecto # 1
 * Simulador del kernel 2.6 de Linux
 * Hector Dominguez 09-10241
 * Carlos Aponte 09-10041
 * Krysler Pinto 09-10661
 * Gustavo Ortega 09-10590
 */

/**
 * Simula la estructura prior_array_t
 */
package Funcionalidad;

import java.util.ArrayList;

/**
 *
 * @author hector
 */
public class ListasDePrioridades {

    /**
     * Entero con la menor prioridad disponible de los procesos.
     */
    private int menorPrioridadNoVacia;
    /**
     * Numero con la cantidad de procesos en la simulacion.
     */
    private int numProcesos;
    /**
     * Arreglo de 140 listas de procesos, cada lista es de un prioridad.
     */
    private ArrayList<Proceso>[] listas = new ArrayList[140];

    public ListasDePrioridades() {
        this.numProcesos = 0;
        for (int i = 0; i < this.listas.length; i++) {
            this.listas[i] = new ArrayList<Proceso>();
        }
        this.menorPrioridadNoVacia = 140;
    }

    public ListasDePrioridades(ArrayList<Proceso> procesos) {
        Proceso p;
        int prioridad;

        this.numProcesos = procesos.size();
        this.menorPrioridadNoVacia = 140;
        /**
         * Se crean las listas por prioridad.
         */
        for (int i = 0; i < this.listas.length; i++) {
            this.listas[i] = new ArrayList<Proceso>();
        }
        /**
         * Se agregan los procesos a las colas respectivas y se actualiza la
         * menorPrioridadNoVacia.
         */
        for (int i = 0; i < this.numProcesos; i++) {
            p = procesos.get(i);
            prioridad = p.getPrioridadDinamica();
            if (prioridad < this.menorPrioridadNoVacia) {
                this.menorPrioridadNoVacia = prioridad;
            }
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

    public ArrayList<Proceso>[] getListas() {
        return listas;
    }

    public void setListas(ArrayList<Proceso>[] listas) {
        this.listas = listas;
    }

// ========================     FIN Getters/Setters     ========================
    /**
     * Se inserta en el proceso en la lista respectiva a la prioridad y se
     * actualiza la menorPrioridadNoVacia.
     */
    public boolean insertarProceso(Proceso p) {
        int prioridad = p.getPrioridadDinamica();
        boolean agrego = false;

        if (this.listas[prioridad].indexOf(p) == -1) {

            if (prioridad < this.menorPrioridadNoVacia) {
                this.menorPrioridadNoVacia = prioridad;
            }

            agrego = this.listas[prioridad].add(p);
            if (agrego) {
                this.numProcesos++;
            }
        }

        return agrego;
    }

    /**
     * Se remueve el proceso p de su lista respectiva y se actualiza la
     * menorPrioridadNoVacia.
     */
    public boolean eliminarProceso(Proceso p) {
        int prioridad = p.getPrioridadDinamica();
        boolean elimino = this.listas[prioridad].remove(p);

        if (elimino) {
            this.numProcesos--;

            if (this.numProcesos == 0) {
                this.menorPrioridadNoVacia = 140;
            } else {
                if (this.listas[prioridad].size() == 0) {
                    for (int i = prioridad + 1; i < listas.length; i++) {
                        if (this.listas[i].size() != 0) {
                            this.menorPrioridadNoVacia = i;
                            break;
                        }
                    }
                }
            }
        }

        return elimino;
    }

    /**
     * Se obtiene el mejor proceso disponibles en las colas.
     */
    public Proceso obtenerMejorProceso() {
        if (this.menorPrioridadNoVacia != 140) {
            return this.listas[this.menorPrioridadNoVacia].get(0);
        }
        return null;
    }

    /**
     * Se aumenta el tiempo de espera de todos los procesos en las las listas.
     */
    public void aumentarTiempoEnEspera() {
        if (numProcesos != 0) {
            for (int i = menorPrioridadNoVacia; i < listas.length; i++) {
                for (int j = 0; j < listas[i].size(); j++) {
                    listas[i].get(j).aumentarTiempoEnEspera();
                }
            }
        }
    }

    @Override
    public String toString() {
        String s = "{ ";
        int aux = 0;
        for (int i = 0; i < listas.length; i++) {
            for (int j = 0; j < listas[i].size(); j++) {
                s += listas[i].get(j).toString() + ", ";
                if (++aux == 5) {
                    s += "\n";
                }
            }
        }
        return s + "}";
    }
}