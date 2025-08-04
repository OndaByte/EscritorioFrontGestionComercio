/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.OndaByte.AntartidaFront.vistas.util;

import com.OndaByte.AntartidaFront.vistas.util.tabla.CustomTableModel;
import com.OndaByte.AntartidaFront.vistas.util.tabla.TablaBuilder;
import com.OndaByte.AntartidaFront.vistas.util.tabla.TableActionCellEditor;
import com.OndaByte.AntartidaFront.vistas.util.tabla.TableActionCellRender;
import com.OndaByte.AntartidaFront.vistas.util.tabla.TableActionEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableModel;

/**
 *
 * @author luciano
 */
public class Maqueta extends javax.swing.JPanel {
JTable jTableResultados;
    /**
     * Creates new form Maqueta
     */
    public Maqueta() {
        initComponents();
        initTabla();
    }
    
    public void initTabla() {
        TableActionEvent event = new TableActionEvent() {
            @Override
            public void onAdd(int row) {
                System.out.println("add fila " + row);
            }
            @Override
            public void onEdit(int row) {
                System.out.println("Editando fila " + row);
            }

            @Override
            public void onDelete(int row) {
                System.out.println("Eliminando fila " + row);
            }

            @Override
            public void onView(int row) {
                System.out.println("Viendo fila " + row);
            }
        };
        String[] headers =new String[]{"Instancias :", "Resultados :", "Acciones :"};
        List<Object[]> rows = generateData();
    //    TablaBuilder builder = new TablaBuilder(new String[]{"Instancias :", "Resultados :", "Acciones :"},rows,headers.length - 1,event);
//        jScrollPane1 = builder.crearTabla();
//        jPanel1.setLayout(new BorderLayout());      
//        jPanel1.add(jScrollPane1,BorderLayout.CENTER);
        // this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.repaint();
    }

    private List<Object[]> generateData() {
        List<Object[]> rows = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            rows.add(new Object[]{"atr1", "atr2"}); // <3
        }
        return rows;
    }

// ublic void initTablaResultados(){
//        jTableResultados = new JTable();
//        jTableResultados.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
//        configTableLookAndFeel(jTableResultados);                               //vista 
//        
//        CustomTableModel model2 = (CustomTableModel) new CustomTableModel(); //modelo 
//        model2.addColumn("Instancias :");
//        model2.addColumn("Resultados :");
//        model2.addColumn("Acciones :"); 
//        jTableResultados.setModel(model2);
//        TableActionEvent event = new TableActionEvent() {                       //listenner
//            @Override
//            public void onEdit(int row) {
//            }
//            @Override
//            public void onDelete(int row) {
//            }
//            @Override
//            public void onView(int row) {
//                //mostrarResultadosParciales(row);                System.out.println("eventing");
//                //System.out.println("eventing");
//
//            }
//        };
//        addActionPanelToColumn(jTableResultados,2,event);
//        
//        JScrollPane jScrollPane2 = new JScrollPane(jTableResultados);
//        jScrollPane2.getViewport().setBackground(Color.decode("#EFEBCE"));
//        jPanel1.setLayout(new BorderLayout());      
//        jPanel1.add(jScrollPane2,BorderLayout.CENTER);
//      //  vista.repintar();
//      mostrarResultados();
//      this.repaint();
//    }
//    public void addActionPanelToColumn(JTable table, int actionColumn,TableActionEvent event){
//        table.getColumnModel().getColumn(actionColumn).setCellRenderer(new TableActionCellRender());
//        table.getColumnModel().getColumn(actionColumn).setCellEditor(new TableActionCellEditor(event));
//    }
//private void configTableLookAndFeel(JTable table){
//     //   table.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
//       //h table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        table.setColumnSelectionAllowed(true);
//      //  table.setRowSelectionAllowed(true);
//     //   table.getTableHeader().setReorderingAllowed(false);
//      //  table.getTableHeader().setBackground(new Color(250, 250, 250));
//    //    table.getTableHeader().setDefaultRenderer(new TableHeaderCustomCellRender(null));
//     //   table.setSelectionBackground(Color.decode("#D8A48F"));  
//        table.setRowHeight(40);
//    }
//   public void mostrarResultados() {
//        CustomTableModel model = (CustomTableModel) jTableResultados.getModel(); //modelo  
//        model.setRowCount(0);
//        //Filas
//        int cols = model.getColumnCount();
//        for (int i = 0; i < 5; i++) {
//            Object[] fila = new Object[cols]; 
//            fila[0] = "eia";
//            fila[1] = "euax2";
////                fila[2] = ""; // no hace falta por que el render de la tabla lo complleta con los botones ... 
//            model.addRow(fila);           
//        }
//        jTableResultados.setModel(model);
//        //return model;
//    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jTextField1 = new javax.swing.JTextField();

        setLayout(null);

        jLabel1.setText("PRESUPUESTO NOMBRE ");
        add(jLabel1);
        jLabel1.setBounds(123, 55, 159, 17);

        jLabel2.setText("TOTAL");
        add(jLabel2);
        jLabel2.setBounds(890, 653, 199, 34);

        jLabel3.setText("PENDIENTE");
        add(jLabel3);
        jLabel3.setBounds(560, 55, 196, 17);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jTextField1.setText("jTextField1");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(85, Short.MAX_VALUE)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 446, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(66, 66, 66))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(120, 120, 120)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(163, 163, 163)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(213, Short.MAX_VALUE))
        );

        add(jPanel1);
        jPanel1.setBounds(100, 80, 845, 424);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
