/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Constantes;

/**
 *
 * @author hector
 */
public class Constantes {

    /**
     * Flag que indica si el proceso esta activo y en ejecucion.
     */
    public static final short TASK_RUNNING = 1;
    /**
     * Flag que indica si el proceso esta en la cola de dispositivo.
     */
    public static final short TASK_INTERRUPTIBLE = 2;
    public static final short TASK_UNINTERRUPTIBLE = 3;
    /**
     * Flag que indica si el proceso esta detenido.
     */
    public static final short TASK_STOPPED = 4;
    public static final short TASK_TRACED = 5;
    /**
     * Flag que indica si el proceso termino en estado zombie.
     */
    public static final short EXIT_ZOMBIE = 6;
    /**
     * Flag que indica si el proceso esta termino en estado normal.
     */
    public static final short EXIT_DEAD = 7;
}