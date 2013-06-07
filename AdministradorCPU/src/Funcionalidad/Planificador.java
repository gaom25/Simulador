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
 * Simula el sistema de computacion
 */
package Funcionalidad;

import java.util.ArrayList;
import Constantes.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Hector Dominguez
 * @version 1.0
 * @author Gustavo Ortega & Carlos Aponteector
 * @version 1.1
 */
public class Planificador extends Thread {

    /**
     * CPU del planificador.
     */
    Cpu cpu;
    /**
     * Lista con todos los procesos finalizados.
     */
    ArrayList<Proceso> listaFinalizados;
    /**
     * Dispositivo de Entrada/Salida.
     */
    DispositivoIO dispositivoIO;
    /**
     * Reloj para contar los ticks.
     */
    Reloj reloj;
    /**
     * Booleano que indica cuando termina la simulacion.
     */
    Boolean terminoSimulacion = false;
    /**
     * Numero con la estadistica del tiempo de espera en la cola de activos u
     * expirados.
     */
    double tiempoEsperaProm;
    /**
     * Numero con la estadistica del tiempo de espera en la cola de dispositivo.
     */
    double tiempoDormidoProm;

    public Boolean getTerminoSimulacion() {
        return terminoSimulacion;
    }

    public Planificador(String NombreArch, Reloj reloj) {
        // Obtiene todos los procesos ya parseados del archivo .xml
        ArrayList<Proceso>[] procesos = LectorXML.obtenerProcesos(NombreArch);
        // Crea 140 listas: 1 para c/prioridad.
        ListasDePrioridades listasActivos = new ListasDePrioridades(procesos[0]);
        ListasDePrioridades listasExpirados = new ListasDePrioridades();

        // Crea "cola de listos"
        Runqueue colaListos = new Runqueue(listasActivos, listasExpirados);

        // Asocia al CPU su runqueue: es una por c/CPU.
        cpu = new Cpu((short) 1, colaListos, procesos);
        cpu.setPlanificador(this);
        cpu.setName("Cpu");

        //Asocia el planificador al dispositivo
        dispositivoIO = new DispositivoIO();
        dispositivoIO.setPlanificador(this);

        listaFinalizados = new ArrayList<Proceso>();

        //Asocia el reloj al planificador
        this.reloj = reloj;
    }

// ========================     Getters/Setters         ========================
    public Reloj getReloj() {
        return reloj;
    }

    public Cpu getCpu() {
        return cpu;
    }

    public void setCpu(Cpu cpu) {
        this.cpu = cpu;
    }

    public ArrayList<Proceso> getListaFinalizados() {
        return listaFinalizados;
    }

    public void setListaFinalizados(ArrayList<Proceso> listaFinalizados) {
        this.listaFinalizados = listaFinalizados;
    }

    public DispositivoIO getDispositivoIO() {
        return dispositivoIO;
    }

    public void setDispositivoIO(DispositivoIO dispositivoIO) {
        this.dispositivoIO = dispositivoIO;
    }
// ========================     FIN Getters/Setters     ========================

    /* Mueve el proceso p a la cola de expirados*/
    public void insertarListaExpirados(Proceso p) {
        cpu.getRunqueue().insertarProcesoExpirado(p);
    }

    /**
     * Inserta el proceso p en la cola de finalizados y le coloca el estado de
     * terminado
     */
    public boolean insertarListaFinalizados(Proceso p) {
        boolean agrego = false;
        if (listaFinalizados.indexOf(p) == -1) {
            p.setEstado(Constantes.EXIT_DEAD);
            agrego = this.listaFinalizados.add(p);
        }
        return agrego;
    }

    /**
     * Calcula el bono que se le da un proceso despues de hacer su ciclo de
     * Entrad/Salida
     */
    public int obtenerBono(Proceso p) {
        return p.getTiempoDurmiendo() / 100;
    }

    /**
     * Se actualiza la prioridad de dinamica de un proceso
     */
    public int actualizarPrioridadDinamica(Proceso p) {
        int prioridadDinamica = java.lang.Math.max(100,
                java.lang.Math.min(p.getPrioridadEstatica() - this.obtenerBono(p) + 5, 139));

        p.setPrioridadDinamica(prioridadDinamica);
        return prioridadDinamica;
    }

    /**
     * Funcion para actualizar los valores por cada tick de reloj
     */
    public void actualizarQuantum(Proceso p) {
        Boolean asigno = false;
        int prioridadEstatica = p.getPrioridadEstatica();
        int quantum = p.getQuantum();
        int tcpu = p.getTiemposCPU();

        p.setQuantum(--quantum);
        // Disminuye num de Ticks que necesita para terminar ejecucion
        p.setTiemposCPU(--tcpu);
        p.decrementarTiempoDurmiendo();

        if (quantum <= 0) {

            /**
             * Se coloca el proceso actual en null para buscar un nuevo proceso
             * que use el cpu
             */
            cpu.setProcesoActual(null);
            cpu.getRunqueue().setProcesoActual(null);

            // Si todavia le queda trabajo al proceso
            if (tcpu > 0) {

                quantum = (140 - prioridadEstatica) * (prioridadEstatica < 120 ? 20 : 5);
                p.setQuantum(java.lang.Math.min(quantum, tcpu));
                p.setEsPrimerQuantum(false);

                /**
                 * Tanto en los procesos de tiempo real como los normales se usa
                 * la politica de RoundRobin
                 */
                if (p.esTiempoReal()) {
                    // Se inserta a activos:
                    cpu.getRunqueue().getActivos().insertarProceso(p);
                } else {
                    this.actualizarPrioridadDinamica(p);

                    if (cpu.getRunqueue().getTiempoPrimerExpirado() == 0) {
                        cpu.getRunqueue().setTiempoPrimerExpirado(reloj.getNumTicks());
                    }
                    // Se inserta en la cola de expirados
                    cpu.getRunqueue().insertarProcesoExpirado(p);
                }

                /**
                 * Si el proceso aun tiene tiempo de para hacer entrada y salida
                 * se despacha el dispositivo
                 */
            } else if (p.getTiemposIO() != 0 && p.getTiemposIO() != -1) {
                dispositivoIO.insertarColaBloqueados(p);
            } else {
                insertarListaFinalizados(p);
            }
            // Se pasa a buscar un nuevo proceso activo para el CPU
            asigno = this.asignarCPU();
        }
    }

    // Se obtiene la cantidad de procesos que fueron al dispositivo.
    public int cantProcesosIO() {
        int cant = 0;
        for (int i = 0; i < listaFinalizados.size(); i++) {
            if (listaFinalizados.get(i).getTiempoTotalDurmiendo() > 0) {
                cant++;
            }
        }
        return cant;
    }
    // Intenta despertar un proceso que esta en el dispositivo.

    public boolean despiertaProceso(Proceso p) {
        boolean despertado = false;
        int prioridadDinamica;
        int quantum = (140 - p.getPrioridadEstatica()) * (p.getPrioridadEstatica() < 120 ? 20 : 5);
        /**
         * Si el proceso aun no tiene tiempo de ejecucion para el CPU se
         * despacha a la cola de finalizados
         */
        if (p.getTiemposCPU() == 0) {
            insertarListaFinalizados(p);
            /**
             * Si ya todos los procesos estan en la cola de finalizados se
             * termina la simulacion
             */
            if (listaFinalizados.size() == cpu.getTotalProcesos()) {
                terminarSimulacion(cpu.getTiempoOcioso(), reloj.getNumTicks());
            }
        } else {
            p.setQuantum(java.lang.Math.min(quantum, p.getTiemposCPU()));

            if (p.getEstado() == Constantes.TASK_INTERRUPTIBLE) {
                prioridadDinamica = this.reCalcularPrioridadDinamica(p, reloj.getNumTicks());
                p.setEstado(Constantes.TASK_RUNNING);
                /**
                 * Si el CPU no tiene proceso actual se selecciona a p como el
                 * actual
                 */
                if (cpu.getProcesoActual() == null) {
                    cpu.setProcesoActual(p);
                    cpu.getRunqueue().setProcesoActual(p);
                } else {
                    /**
                     * En caso contrario se verifica si se puede expropiar el
                     * proceso activo en CPU
                     */
                    cpu.getRunqueue().insertarProcesoActivo(p);
                    // Se verifica si tiene mayor o menor prioridad que el que esta en CPU
                    if (cpu.getProcesoActual().getPrioridadDinamica() > prioridadDinamica) {
                        cpu.getRunqueue().insertarProcesoActivo(cpu.getProcesoActual());
                        cpu.setProcesoActual(null);
                        cpu.getRunqueue().setProcesoActual(null);
                        this.asignarCPU();
                    }
                }
                despertado = true;
            }
        }
        return despertado;
    }

    // Se recalcula la prioridad dinamica del proceso que viene de IO.
    public int reCalcularPrioridadDinamica(Proceso p, int numTicks) {
        int prioridadDinamica = this.actualizarPrioridadDinamica(p);
        return prioridadDinamica;
    }

    /**
     * Le asigna el CPU al "mejor" proceso de la cola del CPU, si no hay activos
     * hace swap con la cola de expirados.
     */
    public boolean asignarCPU() {
        Proceso p = cpu.getRunqueue().obtenerMejorProceso();

        /**
         * Se coloca el mejor proceso en la cola de activos como el activo en
         * CPU
         */
        if (p != null) {
            cpu.getRunqueue().eliminarProcesoActivo(p);
            cpu.setProcesoActual(p);
            cpu.getRunqueue().setProcesoActual(p);
        } else {
            /**
             * Si ya no hay procesos en la cola de activos se intercambian las
             * colas de activos y expirados
             */
            cpu.getRunqueue().intercambioActivosExpirados();
            p = cpu.getRunqueue().obtenerMejorProceso();

            if (p != null) {
                cpu.getRunqueue().eliminarProcesoActivo(p);
                cpu.setProcesoActual(p);
                cpu.getRunqueue().setProcesoActual(p);
            }
        }
        /**
         * Si todos los procesos ya estan en la cola de finalizados se termina
         * la simulacion
         */
        if (p == null) {
            if (listaFinalizados.size() == cpu.getTotalProcesos()) {
                terminarSimulacion(cpu.getTiempoOcioso(), reloj.getNumTicks());
            }
            return false;
        } else {
            cpu.getRunqueue().aumentarNumProcesosCambiados();
            return true;
        }
    }

    //Agrega un nuevo proceso a la cola de activos.
    public void agregarNuevoProceso(Proceso p) {

        int prioridadDinamica = p.getPrioridadDinamica();
        /**
         * Si no hay proceso activo en el CPU de coloca el proceso p como el
         * activo de CPU
         */
        if (cpu.getProcesoActual() == null) {
            cpu.setProcesoActual(p);
            cpu.getRunqueue().setProcesoActual(p);
        } else {
            cpu.getRunqueue().insertarProcesoActivo(p);
            // Se verifica si tiene mayor o menor prioridad que el que esta en CPU
            if (cpu.getProcesoActual().getPrioridadDinamica() > prioridadDinamica) {
                cpu.getRunqueue().insertarProcesoActivo(cpu.getProcesoActual());
                cpu.setProcesoActual(null);
                cpu.getRunqueue().setProcesoActual(null);
                this.asignarCPU();
            }
        }
    }

    @Override
    public void run() {
        super.run();
    }

    @Override
    // Vuelve un String la informacion del planificador.
    public String toString() {
        return "Planificador{\n" + "\tcpu=" + cpu + ",\n}";
    }

    /**
     * Finaliza la simulacion una vez que todos los procesos terminaron, termina
     * a los hilos y calcula estadisticas.
     */
    private void terminarSimulacion(int tiempoOciosoCPU, int numTicksReloj) {
        double tiempoEsperaPromedio = 0.0, tiempoDormidoPromedio = 0.0;
        int cant = cantProcesosIO();
        reloj.setSimulacion(false);

        /**
         * calcula la sumatoria de los tiempos esperando y dormidos de todos los
         * procesos
         */
        for (int i = 0; i < listaFinalizados.size(); i++) {
            tiempoEsperaPromedio += listaFinalizados.get(i).getTiempoEsperando();
            tiempoDormidoPromedio += listaFinalizados.get(i).getTiempoTotalDurmiendo();
        }

        tiempoEsperaProm = tiempoEsperaPromedio / listaFinalizados.size();
        tiempoDormidoProm = 0.0;

        /**
         * Si hubo procesos que hicieron Entrada/Salida se calcula su
         * estadistica de tiempo promedio dormido
         */
        if (cant > 0) {
            tiempoDormidoProm = tiempoDormidoPromedio / cant;
        }
        terminoSimulacion = true;
    }
}
