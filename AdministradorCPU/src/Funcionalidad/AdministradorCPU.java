/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Funcionalidad;
import Interfaz.*;
import javax.swing.event.*;
/**
 *
 * @author krys
 */
public class AdministradorCPU {
    
    
    
    public static void  main(String[] args)  throws InterruptedException{ 
        /*Un planificador y un */
        AdministradorInterfaz interfaz = new AdministradorInterfaz();
        interfaz.setVisible(true);
        
        /*
        interfaz.getModeloTabla1().anhadeProceso(new Proceso(10,true,10,10,10));
        
        TableModelEvent evento = new TableModelEvent (interfaz.getModeloTabla1());
        
        // ... y pasándoselo a los suscriptores
        
        interfaz.getModeloTabla1().avisaSuscriptores(evento);
        System.out.println("hi");
        */
    }
    
}
