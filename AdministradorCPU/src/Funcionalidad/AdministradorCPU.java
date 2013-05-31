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
        while(interfaz.getReloj()==null){
          try {
                // Tiempo que tarda en actualizar la pantalla
                Thread.sleep(50);
          } catch ( java.lang.InterruptedException ie) {
                System.out.println(ie);
            }
        };
        
       
        // Ciclo infinito hasta que se termine la simulación
        while(true){
            Proceso procesoActual;
            ListasDePrioridades activos;    // active
            ListasDePrioridades expirados;  // expired
            ArrayList<Proceso> bloqueadosIO;
            
           try {
                // Tiempo que tarda en actualizar la pantalla
                Thread.sleep(50);
                if( (procesoActual = interfaz.getPlanificador().getCpu().getProcesoActual()) != null  ){
                     // obtenemos el proceso que esta en cpu y lo muestra en 
                    // la interfaz
                    interfaz.jLabel10.setText( procesoActual.toString() );
                    
                    activos = interfaz.getPlanificador().getCpu().getRunqueue().getActivos();
                    expirados = interfaz.getPlanificador().getCpu().getRunqueue().getExpirados();
                    bloqueadosIO = interfaz.getDispositivo().getColaBloqueados();
                    
                    // Limpio la tabla de activos
                    for(int k=0;k<interfaz.getModeloTabla1().getRowCount();k++){
                        interfaz.getModeloTabla1().borraProceso(k);
                    }
                    
                    // Limpio la tabla de expirados
                    for(int k=0;k<interfaz.getModeloTabla2().getRowCount();k++){
                        interfaz.getModeloTabla2().borraProceso(k);
                    }
                    
                    // Limpio la tabla de bloqueados en IO
                    for(int k=0;k<interfaz.getModeloTabla3().getRowCount();k++){
                        interfaz.getModeloTabla3().borraProceso(k);
                    }
                    
                    // Actualizamos lista de activos
                    ArrayList<Proceso>[] p = activos.getListas();
                    for(int i = 0; i < p.length;i++ ){
                        if(!(p[i].isEmpty())){
                            for(int j = 0; j<p[i].size(); j++){
                                interfaz.getModeloTabla1().anhadeProceso(p[i].get(j));
                            }
                        }
                    }
                    // Actualizamos lista de expirados
                    ArrayList<Proceso>[] pExp = expirados.getListas();
                    for(int i = 0; i < pExp.length;i++ ){
                        if(!(pExp[i].isEmpty())){
                            for(int j = 0; j<pExp[i].size(); j++){
                                interfaz.getModeloTabla2().anhadeProceso(pExp[i].get(j));
                            }
                        }
                    }
                    
                    // Actualizamos lista de bloqueados en IO
                    for(int j = 0; j< bloqueadosIO.size(); j++){
                        interfaz.getModeloTabla3().anhadeProceso(bloqueadosIO.get(j));
                    }
                    
                }else interfaz.jLabel10.setText( "" );
            
            } catch ( java.lang.InterruptedException ie) {
                System.out.println(ie);
            }
        }        
    }
}
