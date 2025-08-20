package com.OndaByte.MisterFront.vistas.caja;

import com.OndaByte.MisterFront.controladores.MovimientoController;
import com.OndaByte.MisterFront.modelos.Cliente;
import com.OndaByte.MisterFront.modelos.ItemVenta;
import com.OndaByte.MisterFront.modelos.Venta;
import com.OndaByte.MisterFront.vistas.DatosListener;
import com.OndaByte.MisterFront.vistas.util.Dialogos;
import com.OndaByte.MisterFront.vistas.util.IconSVG;
import com.OndaByte.MisterFront.vistas.util.Paginado;
import net.miginfocom.swing.MigLayout;

import java.awt.Color;
import java.awt.Font;
import java.util.List;

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
    
    private Float subtotal,total;
    private Integer porcentaje_descuento;
    
    private List<ItemVenta> items;
    private MovimientoController cajaController;
            
    public VentaCajaModal(JFrame parent, List<ItemVenta> items,  Float subtotal, Float total, Integer porcentaje_descuento) {
        super(parent, "Confirmar Venta", true);
        setSize(600, 540);
        setLocationRelativeTo(parent);
        this.cajaController = MovimientoController.getInstance();
        
        this.subtotal=subtotal;
        this.total=total;
        this.porcentaje_descuento = porcentaje_descuento;
        this.items=items;
        
        formaPagoCombo = new JComboBox<>(new String[]{"EFECTIVO", "DÉBITO", "CRÉDITO", "TRANSFERENCIA"});
        clienteCombo = new JComboBox<>(); // rellenar con datos reales
        observaciones = new JTextArea(4, 20);
        btnConfirmar = new JButton("CONFIRMAR", new IconSVG(IconSVG.ACEPTAR));
        btnCancelar = new JButton("CANCELAR", new IconSVG(IconSVG.RECHAZAR));

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

        btnConfirmar.addActionListener(e -> {
            validarYGuardar();
        });

        btnCancelar.addActionListener(e -> dispose());

        // Botones
        mainPanel.add(btnCancelar, "right");
        mainPanel.add(btnConfirmar, "left");

        add(mainPanel);
    }
    
    private void validarYGuardar() {
        String errores = validarFormulario();
        if (errores.isEmpty()) {
            Venta nuevaVenta = this.crearVenta();
            cajaController.crearVenta(nuevaVenta, items, new DatosListener<String>() {
                    @Override
                    public void onSuccess(String datos) {
                        Dialogos.mostrarExito(datos);
                    }

                    @Override
                    public void onError(String mensajeError) {
                        Dialogos.mostrarError(mensajeError);
                    }

                    @Override
                    public void onSuccess(String datos, Paginado p) {
                        //   throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                    }
                });                
            this.dispose();
        } else {
            Dialogos.mostrarError(errores);
        }
    }

 
    private Venta crearVenta() {
        Venta v = new Venta();
        
        v.setSubtotal(this.subtotal);
        v.setPorcentaje_descuento(this.porcentaje_descuento);
        v.setTotal(this.total);
        v.setForma_pago((String) formaPagoCombo.getSelectedItem());

        if(observaciones.getText()!= null && !observaciones.getText().isEmpty())
            v.setObservaciones(observaciones.getText());
        return v;
    }

    private String validarFormulario() {
        StringBuilder errores = new StringBuilder("<html>");

        // cliente
//        if (clienteCombo.getSelectedItem() == null) {
//            errores.append("- Debe seleccionar un cliente.<br>");
//            clienteCombo.setBorder(BorderFactory.createLineBorder(Color.RED));
//        } else {
//            clienteCombo.setBorder(null);
//        }

        // forma de pago
        if (formaPagoCombo.getSelectedItem() == null) {
            errores.append("- Debe seleccionar la forma de pago.<br>");
            formaPagoCombo.setBorder(BorderFactory.createLineBorder(Color.RED));
        } else {
            formaPagoCombo.setBorder(null);
        }

        // items
        if (items == null || items.isEmpty()) {
            errores.append("- Debe agregar al menos un ítem a la venta.<br>");
        }

        // importes (consistencia básica)
        if (subtotal < 0f) errores.append("- El subtotal no puede ser negativo.<br>");
        if (total < 0f) errores.append("- El total no puede ser negativo.<br>");
        if (porcentaje_descuento < 0 || porcentaje_descuento > 100)
            errores.append("- El porcentaje de descuento debe estar entre 0 y 100.<br>");

        if (errores.length() > 6) { // hay errores
            errores.append("</html>");
            return errores.toString();
        }
        return "";
    }
    
//    Getters para recuperar datos seleccionados
//    public Cliente getCliente() { return (Cliente) clienteCombo.getSelectedItem(); }
//    public String getFormaPago() { return (String) formaPagoCombo.getSelectedItem(); }
//    public String getObservaciones() { return observaciones.getText(); }
//    public JButton getBtnConfirmar() { return btnConfirmar; }
//    public JButton getBtnCancelar() { return btnCancelar; }

//    public void setClientes(java.util.List<Cliente> clientes) {
//        for (Cliente c : clientes) clienteCombo.addItem(c);
//    }
}