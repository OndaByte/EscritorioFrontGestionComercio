
package com.OndaByte.AntartidaFront.vistas.util.tabla;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.event.MouseEvent;
import java.awt.Point;
import java.awt.Component;
import java.awt.event.MouseMotionAdapter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class TablaBuilder {

    private JTable table;
    private JScrollPane scrollPane;
    private CustomTableModel tableModel;
    private Object[] headers;
//    private Object[][]data;
    private List<Object[]> data ;
    private boolean hasActionPanel = false; // For the action buttons column
    private int actionColumnIndex = -1;
    private Map<String, TableActionEvent> actionsMap = new HashMap<>();
    final Point[] lastEditCell = { null };


    /**
     * Construye una tabla estilizada con los siguientes parametros.
     * @param headers nombre de columnas.
     * @param data filas.
     * @param actionColumnIndex indice de la columna accion (si no existe -1).
     * @param event eventos del panel de acción.
     */
    public TablaBuilder(String[] headers, List<Object[]> data, int actionColumnIndex, BiConsumer<PanelAccion, Integer> configurador) {
        table = new JTable() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == actionColumnIndex;
            }
        };

        tableModel = new CustomTableModel();
        tableModel.setColumnIdentifiers(headers);
        table.setModel(tableModel);
        table.getTableHeader().setReorderingAllowed(false);

        if (actionColumnIndex != -1 && configurador != null) {
            agregarPanelAccionAColumna(actionColumnIndex, configurador);
        }

        table.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());

                if (col == actionColumnIndex) {
                    if (lastEditCell[0] == null || !lastEditCell[0].equals(new Point(row, col))) {
                        table.editCellAt(row, col);
                        lastEditCell[0] = new Point(row, col);

                        Component editor = table.getEditorComponent();
                        if (editor != null) {
                            editor.dispatchEvent(SwingUtilities.convertMouseEvent(table, e, editor));
                        }
                    }
                } else {
                    if (table.getCellEditor() != null) {
                        table.getCellEditor().stopCellEditing();
                        lastEditCell[0] = null;
                    }
                }
            }
        });

        configEstiloTabla();
        this.data = data;
        cargarDatos();
        centrarContenidoCeldas(actionColumnIndex);
    }


    private void centrarContenidoCeldas(int actionColumnIndex) {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < table.getColumnCount(); i++) {
            if (i != actionColumnIndex) { // ❗️ No pisar la columna de botones
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }
    }


    /**
     * Devuelve un ScrollPanel con la tabla.
     * @return 
     */
   public JScrollPane crearTabla() {
        // Configurar la apariencia de la tabla
        scrollPane = new JScrollPane(table);
        return scrollPane;
   }

   public JTable getTable() {
        return table;
    }
    
   private void cargarDatos() {
        tableModel.setRowCount(0);
        int cols = tableModel.getColumnCount();
        for (int i = 0; i < data.size(); i++) {
            tableModel.addRow(data.get(i));           
        }
   }
   private void configEstiloTabla() {
        table.setRowHeight(40);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setColumnSelectionAllowed(true);
//        table.setBackground(new Color(250, 250, 250));
    }

//    private void agregarPanelAccionAColumna(int actionColumn, TableActionEvent event) {
//        table.getColumnModel().getColumn(actionColumn).setCellRenderer(new TableActionCellRender(ver,editar,eliminar,agregar));
//        table.getColumnModel().getColumn(actionColumn).setCellEditor(new TableActionCellEditor(event,ver,editar,eliminar,agregar));
//    }

    private void agregarPanelAccionAColumna(int actionColumnIndex, BiConsumer<PanelAccion, Integer> configurador) {
        table.getColumnModel().getColumn(actionColumnIndex).setCellRenderer(
            new TableActionCellRender(configurador)
        );
        table.getColumnModel().getColumn(actionColumnIndex).setCellEditor(
            new TableActionCellEditor(configurador)
        );
    }
    
}
