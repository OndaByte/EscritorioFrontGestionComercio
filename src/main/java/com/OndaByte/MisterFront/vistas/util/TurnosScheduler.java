
package com.OndaByte.MisterFront.vistas.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
 
public class TurnosScheduler extends JPanel {
     private JTable table;
    private DefaultTableModel model;
    
    public TurnosScheduler() {
        setLayout(new BorderLayout());
        
        // Definir columnas y filas
        String[] columnNames = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes"};
        Object[][] data = new Object[4][5]; // 4 semanas, 5 días
        
        model = new DefaultTableModel(data, columnNames);
        table = new JTable(model);
        
        // Aplicar renderizador de celda personalizado con JPanel dentro de cada celda
        table.setDefaultRenderer(Object.class, new TurnoPanelCellRenderer());
        
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
        
        // Asignar turnos de prueba
        asignarTurno(0, 0, List.of("Baja", "Alta"));
        asignarTurno(0, 2, List.of("Ejecución Baja"));
        asignarTurno(1, 1, List.of("Ejecución Alta", "Finalizado Baja"));
        asignarTurno(2, 0, List.of("Finalizado Alta"));
    }
    
    private class TurnoPanelCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(0, 1)); // Varias filas, una columna
            
            if (value instanceof List) {
                for (String turno : (List<String>) value) {
                    JLabel label = new JLabel(turno);
                    label.setOpaque(true);
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    
                    // Colorear según el estado del turno
                    if ("Baja".equals(turno)) {
                        label.setBackground(new Color(173, 216, 230)); // Azul claro
                    } else if ("Alta".equals(turno)) {
                        label.setBackground(new Color(255, 140, 0)); // Naranja
                    } else if ("Ejecución Baja".equals(turno)) {
                        label.setBackground(new Color(173, 216, 230));
                        label.setText("\u25B6 " + turno); // Marca de ejecución
                    } else if ("Ejecución Alta".equals(turno)) {
                        label.setBackground(new Color(255, 140, 0));
                        label.setText("\u25B6 " + turno);
                    } else if ("Finalizado Baja".equals(turno)) {
                        label.setBackground(new Color(100, 149, 237)); // Azul medio
                        label.setText("\u2713 " + turno); // Marca de finalizado
                    } else if ("Finalizado Alta".equals(turno)) {
                        label.setBackground(new Color(255, 69, 0)); // Rojo oscuro
                        label.setText("\u2713 " + turno);
                    }
                    
                    panel.add(label);
                }
            }
            return panel;
        }
    }
    
    public void asignarTurno(int semana, int dia, List<String> turnos) {
        model.setValueAt(turnos, semana, dia);
    }
}
