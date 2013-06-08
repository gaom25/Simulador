/**
 * Sistemas de Operacion II.
 * Proyecto # 1
 * Simulador del kernel 2.6 de Linux
 * Hector Dominguez 09-10241
 * Carlos Aponte 09-10041
 * Krysler Pinto 09-10661
 * Gustavo Ortega 09-10590
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
 * @author hector
 */
public class LectorXML {

    /* Funcion para determinar cunatos procesos hay en el .xml, sus caracteristicas,
     tiempos IO, tiempo CPU, etc. */
    public static ArrayList<Proceso>[] obtenerProcesos(String nombreArch) {

        ArrayList<Proceso>[] procesos = new ArrayList[101];
        for (int i = 0; i < procesos.length; i++) {
            procesos[i] = new ArrayList<Proceso>();
        }

        try {

            File archivo = new File(nombreArch);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document documento = db.parse(archivo);
            documento.getDocumentElement().normalize();

            /*Se desgloza el .xml en funcion del tag proceso*/
            NodeList nodos = documento.getElementsByTagName("proceso");
            for (int i = 0; i < nodos.getLength(); i++) {
                Node nodo = nodos.item(i);

                /* Para cada proceso se le obtienen y almacenan sus datos*/
                if (nodo.getNodeType() == Node.ELEMENT_NODE) {

                    /* Obtencion del pid, tipo del proceso, prioridad estatica */
                    int pid = Integer.parseInt(obtenerValor("pid", (Element) nodo));
                    boolean esTiempoReal = Boolean.parseBoolean(obtenerValor("esTiempoReal", (Element) nodo));
                    int prioridadEstatica = Integer.parseInt(obtenerValor("prioridadEstatica", (Element) nodo));

                    /* Obtencion de los tiempos tanto de entrada, cpu como de io */
                    ArrayList<Integer> tiemposCPU = obtenerTiempos("tiempoCPU", (Element) nodo);
                    ArrayList<Integer> tiemposIO = obtenerTiempos("tiempoIO", (Element) nodo);

                    int tiempoEntrada = Integer.parseInt(obtenerValor("tiempoEntrada", (Element) nodo));

                    /* Se agrega el proceso a nuestra lista de procesos a planificar */
                    Proceso proceso = new Proceso(pid, esTiempoReal, prioridadEstatica, tiemposCPU, tiemposIO, tiempoEntrada);
                    if (0 <= tiempoEntrada && tiempoEntrada <= 100) {
                        procesos[tiempoEntrada].add(proceso);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return procesos;
    }

    /*Devuelve el valor asociado a una etiqueta dada*/
    private static String obtenerValor(String etiqueta, Element elemento) {
        return elemento.getElementsByTagName(etiqueta).item(0).getChildNodes().item(0).getNodeValue();
    }

    /*Retorna una lista con todos los tiempos de un proceso, en concreto funcion para 
     * obtener ordenadamente todos los tiempos de CPU e IO.     */
    private static ArrayList<Integer> obtenerTiempos(String etiqueta, Element elemento) {
      
        ArrayList<Integer> t = null;

        /*Desgloza los tiempos en funcion de la etiqueda deseada*/
        NodeList e = elemento.getElementsByTagName(etiqueta);

        int n = e.getLength();

        if (e.getLength() > 0 && e != null) {

            t = new ArrayList<Integer>();

            /*Toma todos los tiempos que fueron desglozados anteriormente*/
            for (int i = 0; i < e.getLength(); i++) {
                Element eletmp = (Element) e.item(i);
                NodeList listatmp = eletmp.getChildNodes();
                t.add(Integer.parseInt(listatmp.item(0).getNodeValue()));
            }
        }

        return t;
    }
}
