
package com.OndaByte.AntartidaFront.vistas.util;

import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Color;
import java.awt.Dimension;
import java.util.List; 
import net.miginfocom.swing.MigLayout;

public class PanelBuscador extends JPanel {

    private final JTextField buscador;
    private final JComboBox<String> combo;
    private int seleccionado = -1;

    public PanelBuscador(String labelTexto) {
        setLayout(new MigLayout("insets 0, wrap 1", "[grow]"));

        buscador = new JTextField();
        combo = new JComboBox<>();

        // Estilo inicial
        Dimension tamCampo = new Dimension(230, 30);
        buscador.setPreferredSize(tamCampo);
        combo.setPreferredSize(tamCampo);

        // Placeholder simple (se puede mejorar con PromptSupport si querés usarlo más pro)
        buscador.setForeground(Color.GRAY);
        buscador.setText("Buscar...");

        buscador.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (buscador.getText().equals("Buscar...")) {
                    buscador.setText("");
                    buscador.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent e) {
                if (buscador.getText().isEmpty()) {
                    buscador.setText("Buscar...");
                    buscador.setForeground(Color.GRAY);
                }
            }
        });

        // Limpiar selección si se modifica el texto
        buscador.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { seleccionado = -1; }
            public void removeUpdate(DocumentEvent e) { seleccionado = -1; }
            public void changedUpdate(DocumentEvent e) { seleccionado = -1; }
        });

        combo.addActionListener(e -> seleccionado = combo.getSelectedIndex());
        // Estructura visual
        add(buscador, "growx");
        add(combo, "growx"); // combo alineado al input
    }

    // Para configurar búsqueda externa
    public void configurarEventoBuscar(DocumentListener listener) {
        buscador.getDocument().addDocumentListener(listener);
    }

    public void setDatos(List<String> datos) {
        combo.removeAllItems();
        for (String d : datos) {
            combo.addItem(d);
        }
//        combo.setSelectedIndex(-1);
//        seleccionado = -1;
    }

    public void setPlaceholder(String texto) {
        buscador.setText(texto);
        buscador.setForeground(Color.GRAY);

        buscador.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (buscador.getText().equals(texto)) {
                    buscador.setText("");
                    buscador.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent e) {
                if (buscador.getText().isEmpty()) {
                    buscador.setText(texto);
                    buscador.setForeground(Color.GRAY);
                }
            }
        });
    }

    public JTextField getBuscador() {
        return buscador;
    }

    public JComboBox<String> getCombo() {
        return combo;
    }
    public JComboBox<String> getLabel() {
        return combo;
    }
    
    public int getSeleccionado() {
        return combo.getSelectedIndex();
    }
    
}