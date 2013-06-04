package Interfaz;

/**
 * @author krys
 *
 * ModeloTabla.java
 *
 * Modelo de tabla para el ejmplo de uso del JTable
 */
import java.util.ArrayList;
import javax.swing.table.*;
import javax.swing.event.*;
import java.util.LinkedList;
import Funcionalidad.*;

/** 
 * Modelo de procesos. Cada fila es un proceso y las columnas son los datos
 * de tales procesos.
 * Implementa TableModel y dos métodos para añadir y eliminar Procesos del
 * modelo 
 */
public class ModeloTabla implements TableModel
{
    int numeroFilas = 0;
    /** Retorna el numero de columnas en el modelo. Un Jtable utiliza este
     * metodo para determinar cuantas columnas debe crear y mostrar por defecto
     *
     * @return numero de columnas del modelo
     *
     */
    public int getColumnCount() {
        // Devuelve el número de columnas del modelo, que coincide con el
        // número de datos que tenemos de cada proceso.
        return 4;
    }
    
    /** Retorna el numero de filas del modelo. Un Jtable utiliza este
     * metodo para determinar cuantas filas debe mostrar por defecto.
     *
     * @return el numero de filas del modelo
     *
     */
    public int getRowCount() {
        // Devuelve el número de procesos en el modelo, es decir, el número
        // de filas en la tabla.
        return numeroFilas;
    }
    
    /** Retorna el valor de la celda en una columna y fila determinada
     *
     * @param	rowIndex	la fila del dato deseado
     * @param	columnIndex 	la columna del dato deseado
     * @return                  El objeto en la celda especificada
     *
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        Proceso aux;
        
        // Se obtiene la persona de la fila indicada
        aux = (Proceso)(datos.get(rowIndex));
        
        // Se obtiene el campo apropiado según el valor de columnIndex
        switch (columnIndex)
        {
            case 0:
                return aux.getPid();
           case 1:
                return aux.getPrioridadDinamica();
           case 2:
                return aux.getPrioridadEstatica();
           case 3:
                return aux.getQuantum();
            default:
                return null;
        }
    }
    
    /**
     * Borra del modelo el proceso en la fila indicada 
     */
    public void borraProceso (int fila)
    {
        // Se borra la fila 
        datos.remove(fila);
        numeroFilas--;
        // Y se avisa a los suscriptores, creando un TableModelEvent...
        TableModelEvent evento = new TableModelEvent (this, fila, fila, 
            TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE);
        
        // ... y pasándoselo a los suscriptores
        avisaSuscriptores (evento);
        
    }

    /*Obtiene el numero de filas del modelo */
    public int getNumeroFilas() {
        return numeroFilas;
    }
    
    /**
     * Añade una persona al final de la tabla
     */
    public void anhadeProceso (Proceso nuevaProceso)
    {
        // Añade la persona al modelo 
        datos.add (nuevaProceso);
        numeroFilas++;
        // Avisa a los suscriptores creando un TableModelEvent...
        TableModelEvent evento;
        evento = new TableModelEvent (this, this.getRowCount()-1,
            this.getRowCount()-1, TableModelEvent.ALL_COLUMNS,
            TableModelEvent.INSERT);

        // ... y avisando a los suscriptores
        avisaSuscriptores (evento);
    }
    
    
    /**Añade un suscriptor a la lista y es notificado cada vez que ocurre 
     * una modificacion en los datos del modelo
     *
     * @param	l		the TableModelListener
     *
     */
    public void addTableModelListener(TableModelListener l) {
        // Añade el suscriptor a la lista de suscriptores
        listeners.add (l);
    }
    
    /** Returns the most specific superclass for all the cell values
     * in the column.  This is used by the <code>JTable</code> to set up a
     * default renderer and editor for the column.
     *
     * @param columnIndex  the index of the column
     * @return the common ancestor class of the object values in the model.
     *
     */
    public Class getColumnClass(int columnIndex) {
        // Devuelve la clase que hay en cada columna.
        switch (columnIndex)
        {
            case 0:
                // La columna cero contiene el pid del proceso, que es
                // un Entero
                return Integer.class;
          case 1:
                // La columna cero contiene la prioridad del proceso, que es
                // un Entero
                return Integer.class;
          case 2:
                // La columna cero contiene el quantum del proceso, que es
                // un Entero
                return Integer.class;
            default:
                // Devuelve una clase Object por defecto.
                return Object.class;
        }
    }
    
    /** Returns the name of the column at <code>columnIndex</code>.  This is used
     * to initialize the table's column header name.  Note: this name does
     * not need to be unique; two columns in a table can have the same name.
     *
     * @param	columnIndex	the index of the column
     * @return  the name of the column
     *
     */
    public String getColumnName(int columnIndex) 
    {
        // Devuelve el nombre de cada columna. Este texto aparecerá en la
        // cabecera de la tabla.
        switch (columnIndex)
        {
            case 0:
                return "PID";
            case 1:
                return "PD";
            case 2:
                return "PE";
            case 3:
                return "Quantum";
            default:
                return null;
        }
    }
    
    /** Returns true if the cell at <code>rowIndex</code> and
     * <code>columnIndex</code>
     * is editable.  Otherwise, <code>setValueAt</code> on the cell will not
     * change the value of that cell.
     *
     * @param	rowIndex	the row whose value to be queried
     * @param	columnIndex	the column whose value to be queried
     * @return	true if the cell is editable
     * @see #setValueAt
     *
     */
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        // NO Permite que la celda sea editable.
        return false;
    }
    
    /** Removes a listener from the list that is notified each time a
     * change to the data model occurs.
     *
     * @param	l		the TableModelListener
     *
     */
    public void removeTableModelListener(TableModelListener l) {
        // Elimina los suscriptores.
        listeners.remove(l);
    }
    
    /** Sets the value in the cell at <code>columnIndex</code> and
     * <code>rowIndex</code> to <code>aValue</code>.
     *
     * @param	aValue		 the new value
     * @param	rowIndex	 the row whose value is to be changed
     * @param	columnIndex 	 the column whose value is to be changed
     * @see #getValueAt
     * @see #isCellEditable
     *
     */
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) 
    {
        // Obtiene la persona de la fila indicada
        Proceso aux;
        aux = (Proceso)(datos.get(rowIndex));
        
        // Cambia el campo del proceso a que indica columnIndex, poniendole el 
        // aValue que se nos pasa.
        switch (columnIndex)
        {
            case 0:
                aux.setPid((Integer)aValue);
                break;
            default:
                break;
        }
        
        // Avisa a los suscriptores del cambio, creando un TableModelEvent ...
        TableModelEvent evento = new TableModelEvent (this, rowIndex, rowIndex, 
            columnIndex);

        // ... y pasándoselo a los suscriptores.
        avisaSuscriptores (evento);
    }
    
    /**
     * Pasa a los suscriptores el evento.
     */
    public void avisaSuscriptores (TableModelEvent evento)
    {
        int i;
        
        // Bucle para todos los suscriptores en la lista, se llama al metodo
        // tableChanged() de los mismos, pasándole el evento.
        for (i=0; i<listeners.size(); i++)
            ((TableModelListener)listeners.get(i)).tableChanged(evento);
//        System.out.println("Paso");
    }
    
    /** Lista con los datos. Cada elemento de la lista es una instancia de
     * Proceso */
    ArrayList<Proceso> datos = new ArrayList<Proceso>();
    
    // List<String> myList = new ArrayList<String>(myQueue);
    
    /** Lista de suscriptores. El JTable será un suscriptor de este modelo de
     * datos */
    private LinkedList listeners = new LinkedList();
}