/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Funcionalidad;
import Interfaz.*;
import java.util.ArrayList;
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
        
        // Mientras no se seleccione un archivo no pasa de aqui
        while(interfaz.getReloj()==null);
        
       
        // Ciclo infinito hasta que se termine la simulación
        while(true){
            Proceso procesoActual;
            ListasDePrioridades activos;    // active
            ListasDePrioridades expirados;  // expired
           try {
                // Tiempo que tarda en actualizar la pantalla
                Thread.sleep(600);
                if((procesoActual=interfaz.getPlanificador().getCpu().getProcesoActual())!=null  ){
                     // obtenemos el proceso que esta en cpu y lo muestra en 
                    // la interfaz
                    interfaz.jLabel10.setText(""+procesoActual.getPid()+"");
                    
                    activos = interfaz.getPlanificador().getCpu().getRunqueue().getActivos();
                    expirados = interfaz.getPlanificador().getCpu().getRunqueue().getExpirados();
                    
                    // Limpio la tabla
                    for(int k=0;k<interfaz.getModeloTabla1().getRowCount();k++){
                        interfaz.getModeloTabla1().borraProceso(k);
                    }
                    
                    
                    ArrayList<Proceso>[] p = activos.getListas();
                    for(int i = 0; i < p.length;i++ ){
                        if(!(p[i].isEmpty())){
                            for(int j = 0; j<p[i].size(); j++){
                                interfaz.getModeloTabla1().anhadeProceso(p[i].get(j));
                            }
                        }
                    }
                }
            
            } catch ( java.lang.InterruptedException ie) {
                System.out.println(ie);
            }
        }
        
       
    
        
        
        
    }
    
}
