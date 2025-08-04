
package com.OndaByte.AntartidaFront.vistas.util.tabla;

import javax.swing.table.DefaultTableModel;

/**
 * Crear una clase para modificar este metodo xD 
 *
 */
public class CustomTableModel extends DefaultTableModel {
    
    public CustomTableModel(){super();}
    
    public CustomTableModel(Object[] columnNames,int rowCont){
        super(columnNames,rowCont);
    }
    
    public CustomTableModel(Object[][] data, Object[] columnNames){
        super(data,columnNames);
    }
    @Override
    public boolean isCellEditable(int row, int col) {
        return col!=0;
    }    
} 
