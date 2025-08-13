package com.OndaByte.MisterFront.vistas.caja;

import com.OndaByte.MisterFront.modelos.Cliente;
import net.miginfocom.swing.MigLayout;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

public class VentaCajaModal extends JDialog {
    private JComboBox<String> formaPagoCombo;
    private JComboBox<Cliente> clienteCombo;
    private JTextArea observaciones;
    private JButton btnConfirmar, btnCancelar;

    // Labels de resumen (solo lectura)
    private JLabel lblSubtotalValor;
    private JLabel lblDescValor;
    private JLabel lblTotalValor;

    public VentaCajaModal(JFrame parent, Float subtotal, Float total, Integer porcentaje_descuento) {
        super(parent, "Confirmar Venta", true);
        setSize(460, 420);
        setLocationRelativeTo(parent);

        formaPagoCombo = new JComboBox<>(new String[]{"EFECTIVO", "DÉBITO", "CRÉDITO", "TRANSFERENCIA"});
        clienteCombo = new JComboBox<>(); // rellenar con datos reales
        observaciones = new JTextArea(4, 20);
        btnConfirmar = new JButton("Confirmar");
        btnCancelar = new JButton("Cancelar");

//        // ===== Formateadores (AR) =====
//        Locale ar = new Locale("es", "AR");
//        NumberFormat money = NumberFormat.getCurrencyInstance(ar);
//        DecimalFormat pct = new DecimalFormat("#0.##' %'");

        // Soporte: si viene 0.10 mostrar 10 %, si viene 10 mostrar 10 %
//        double pctValue = porcentaje_descuento == null ? 0.0
//                : (Math.abs(porcentaje_descuento) <= 1.0 ? porcentaje_descuento * 100.0 : porcentaje_descuento);

        // ===== Panel principal =====
        JPanel mainPanel = new JPanel(new MigLayout(
                "wrap 2, insets 14, gapx 12, gapy 10",
                "[right][grow, fill]",
                ""
        ));

        // Primera fila: Cliente
        mainPanel.add(new JLabel("Cliente:"));
        mainPanel.add(clienteCombo);

        // Segunda fila: Forma de pago
        mainPanel.add(new JLabel("Forma de pago:"));
        mainPanel.add(formaPagoCombo);

        // ===== Panel Resumen bonito =====
        JPanel resumenPanel = new JPanel(new MigLayout(
                "wrap 2, insets 12, gapx 10, gapy 8",
                "[right][grow, push]",
                ""
        ));
        TitledBorder tb = BorderFactory.createTitledBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(225, 225, 225), 1, true),
                        BorderFactory.createEmptyBorder(8, 8, 8, 8)
                ),
                "Resumen"
        );
        tb.setTitleFont(tb.getTitleFont().deriveFont(Font.BOLD, 13f));
        resumenPanel.setBorder(tb);
        resumenPanel.setBackground(new Color(250, 250, 250));

        Font labelFont = getFont().deriveFont(Font.PLAIN, 13f);
        Font valueFont = getFont().deriveFont(Font.BOLD, 16f);

        // Subtotal
        JLabel lblSubtotal = new JLabel("Subtotal:");
        lblSubtotal.setFont(labelFont);
        lblSubtotalValor = new JLabel("$ " + String.format("%.2f", subtotal));
        lblSubtotalValor.setFont(valueFont);

        // % Descuento
        JLabel lblDesc = new JLabel("Descuento:");
        lblDesc.setFont(labelFont);
        lblDescValor = new JLabel("% " + porcentaje_descuento);
        lblDescValor.setFont(valueFont);
        lblDescValor.setForeground(new Color(160, 0, 0));

        // Total
        JLabel lblTotal = new JLabel("Total:");
        lblTotal.setFont(labelFont);
        lblTotalValor = new JLabel("$ " + String.format("%.2f", total));
        lblTotalValor.setFont(valueFont);
        lblTotalValor.setForeground(new Color(24, 125, 53));

        resumenPanel.add(lblSubtotal);
        resumenPanel.add(lblSubtotalValor, "right");
        resumenPanel.add(lblDesc);
        resumenPanel.add(lblDescValor, "right");
        resumenPanel.add(lblTotal);
        resumenPanel.add(lblTotalValor, "right");

        // Agregar el resumen al main
        mainPanel.add(resumenPanel, "span 2, growx, gaptop 4");

        // Observaciones
        mainPanel.add(new JLabel("Observaciones:"), "span 2");
        mainPanel.add(new JScrollPane(observaciones), "span 2, grow, h 90!");

        // Botones
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