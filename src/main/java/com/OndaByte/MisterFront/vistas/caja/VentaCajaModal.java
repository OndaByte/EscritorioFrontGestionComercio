
package com.OndaByte.MisterFront.vistas.caja;

import com.OndaByte.MisterFront.modelos.Cliente;
import net.miginfocom.swing.MigLayout;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class VentaCajaModal extends JDialog {
    private JComboBox<String> formaPagoCombo;
    private JComboBox<Cliente> clienteCombo;
    private JTextArea observaciones;
    private JButton btnConfirmar, btnCancelar;


    /*
    public VentaCajaModal(JFrame parent) {
        super(parent, "Confirmar Venta", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);

        formaPagoCombo = new JComboBox<>(new String[]{"EFECTIVO", "DÉBITO", "CRÉDITO", "TRANSFERENCIA"});
        clienteCombo = new JComboBox<>(); // rellenar con datos reales
        observaciones = new JTextArea(4, 20);
        btnConfirmar = new JButton("Confirmar");
        btnCancelar = new JButton("Cancelar");

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; mainPanel.add(new JLabel("Cliente:"), gbc);
        gbc.gridx = 1; mainPanel.add(clienteCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; mainPanel.add(new JLabel("Forma de pago:"), gbc);
        gbc.gridx = 1; mainPanel.add(formaPagoCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        mainPanel.add(new JLabel("Observaciones:"), gbc);

        gbc.gridy = 3; mainPanel.add(new JScrollPane(observaciones), gbc);

        gbc.gridy = 4; gbc.gridwidth = 1; gbc.gridx = 0; mainPanel.add(btnCancelar, gbc);
        gbc.gridx = 1; mainPanel.add(btnConfirmar, gbc);

        add(mainPanel);
    }
     */

    public VentaCajaModal(JFrame parent) {
        super(parent, "Confirmar Venta", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);

        formaPagoCombo = new JComboBox<>(new String[]{"EFECTIVO", "DÉBITO", "CRÉDITO", "TRANSFERENCIA"});
        clienteCombo = new JComboBox<>(); // rellenar con datos reales
        observaciones = new JTextArea(4, 20);
        btnConfirmar = new JButton("Confirmar");
        btnCancelar = new JButton("Cancelar");

        // Panel principal con MigLayout
        JPanel mainPanel = new JPanel(new MigLayout("wrap 2, insets 10","[right][grow, fill]", ""));

        // Primera fila: Cliente
        mainPanel.add(new JLabel("Cliente:"));
        mainPanel.add(clienteCombo);

        // Segunda fila: Forma de pago
        mainPanel.add(new JLabel("Forma de pago:"));
        mainPanel.add(formaPagoCombo);

        // Tercera fila: Observaciones (etiqueta y campo ocupando 2 columnas)
        mainPanel.add(new JLabel("Observaciones:"), "span 2");
        mainPanel.add(new JScrollPane(observaciones), "span 2, grow, h 80!");

        // Cuarta fila: Botones
        mainPanel.add(btnCancelar, "right");
        mainPanel.add(btnConfirmar, "left");

        add(mainPanel);
    }


    // Getters para recuperar datos seleccionados
    public Cliente getCliente() { return (Cliente) clienteCombo.getSelectedItem(); }
    public String getFormaPago() { return (String) formaPagoCombo.getSelectedItem(); }
    public String getObservaciones() { return observaciones.getText(); }
    public JButton getBtnConfirmar() { return btnConfirmar; }
    public JButton getBtnCancelar() { return btnCancelar; }

    public void setClientes(java.util.List<Cliente> clientes) {
        for (Cliente c : clientes) clienteCombo.addItem(c);
    }
}
