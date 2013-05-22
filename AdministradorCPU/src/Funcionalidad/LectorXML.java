/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Funcionalidad;

import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author hector
 */
public class LectorXML {

    public static ArrayList<Proceso> obtenerProcesos(String nombreArch){
        
        ArrayList<Proceso> procesos = new ArrayList<Proceso>();
        
        try{
            
            File archivo = new File(nombreArch);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document documento = db.parse(archivo);
            documento.getDocumentElement().normalize();
            
            NodeList nodos = documento.getElementsByTagName("proceso");
            for (int i = 0; i < nodos.getLength(); i++) {
                Node nodo = nodos.item(i);
                if (nodo.getNodeType() == Node.ELEMENT_NODE){
                   
                    int pid = Integer.parseInt(obtenerValor("pid", (Element) nodo));
                    boolean esTiempoReal = Boolean.parseBoolean(obtenerValor("esTiempoReal", (Element) nodo));
                    int prioridadEstatica = Integer.parseInt( obtenerValor("prioridadEstatica",(Element) nodo));
                   
                    ArrayList<int[]> tiemposCPU = obtenerTiempos("tiempoCPU",(Element) nodo);
                    ArrayList<int[]> tiemposIO = obtenerTiempos("tiempoIO", (Element) nodo);
                    
                    int tiempoEntrada = Integer.parseInt( obtenerValor("tiempoEntrada",(Element) nodo));
                   
                   
                     Proceso proceso = new Proceso(pid, esTiempoReal, prioridadEstatica, tiemposCPU, tiemposIO, tiempoEntrada);
                     procesos.add(proceso);
                }
            }
            
        }catch (Exception e){
            e.printStackTrace();
        }
        
        return procesos;
    }
    
    private static String obtenerValor(String etiqueta, Element elemento){
        return elemento.getElementsByTagName(etiqueta).item(0).getChildNodes().item(0).getNodeValue();
    }
    
    private static ArrayList<int[]> obtenerTiempos(String etiqueta, Element elemento){
        ArrayList<int[]> t = null;
        
        NodeList e = elemento.getElementsByTagName(etiqueta);
          
        int n = e.getLength();
        int a[] = new int[n]; 
      
        if ( e.getLength() > 0 && e != null){
          
            t = new ArrayList<int[]>();
            for (int i = 0; i < e.getLength(); i++) {
 
                Element eletmp = (Element) e.item(i);
                NodeList listatmp  = eletmp.getChildNodes();
                a[i] =  Integer.parseInt(listatmp.item(0).getNodeValue());
            }
            
    
            t.add(0,a);
        }
        
        
            
       return t;
    }
}