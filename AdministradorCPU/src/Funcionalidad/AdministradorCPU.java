/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Funcionalidad;

import java.util.ArrayList;

/**
 *
 * @author hector
 */
public class AdministradorCPU {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ArrayList<Proceso> procesos = LectorXML.obtenerProcesos();
        System.out.println(procesos.toString());
        System.out.println("PRUEBA ListasDePrioridades");
        ListasDePrioridades listas = new ListasDePrioridades(procesos);
        System.out.println(listas.toString());
        
        System.out.println("PRUEBA Eliminar Proceso");
        listas.eliminarProceso(procesos.get(0));
        System.out.println(listas.toString());
        
        System.out.println("PRUEBA Insertar Proceso");
        listas.insertarProceso(new Proceso(0, false, 139, 23, 234));
        listas.insertarProceso(new Proceso(100, true, 73, 23, 234));
        listas.insertarProceso(new Proceso(101, false, 0, 23, 234));
        System.out.println(listas.toString());
        
        System.out.println("PRUEBA Eliminar Proceso");
        listas.eliminarProceso(new Proceso(101, false, 0, 23, 234));
        listas.eliminarProceso(new Proceso(2, true, 73, 19, 2));
        listas.eliminarProceso(new Proceso(100, true, 73, 23, 234));
        listas.eliminarProceso(new Proceso(3, false, 107, 1, 234));
        listas.eliminarProceso(new Proceso(0, false, 139, 23, 234));
        System.out.println(listas.toString());
    }
}