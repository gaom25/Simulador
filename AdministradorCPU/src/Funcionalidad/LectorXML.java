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

    public static ArrayList<Proceso> obtenerProcesos(){
        
        ArrayList<Proceso> procesos = new ArrayList<Proceso>();
        
        try{
            
            File archivo = new File("src/Entrada_Salida/procesos.xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document documento = db.parse(archivo);
            documento.getDocumentElement().normalize();
            
            NodeList nodos = documento.getElementsByTagName("proceso");
            for (int i = 0; i < nodos.getLength(); i++) {
                Node nodo = nodos.item(i);
                if (nodo.getNodeType() == Node.ELEMENT_NODE){
                    Proceso proceso = new Proceso(
                        Integer.parseInt(    obtenerValor("pid",              (Element) nodo)),
                        Boolean.parseBoolean(obtenerValor("esTiempoReal",     (Element) nodo)),
                        Integer.parseInt(    obtenerValor("prioridadEstatica",(Element) nodo)),
                        Integer.parseInt(    obtenerValor("tiempoCPU",        (Element) nodo)),
                        Integer.parseInt(    obtenerValor("tiempoEntrada",    (Element) nodo))
                     );
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
}