/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.OndaByte.AntartidaFront.vistas.caja;

import com.OndaByte.AntartidaFront.modelos.ItemVenta;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author luciano
 */
public class VentaCajaPanel extends JPanel {

    private String nombre;
    private ArrayList<ItemVenta> items;
    private Float subtotal;
    private Float total;
    private JTextField txtBuscarProducto;
    private JPanel productosPanel, carritoPanel;
    private JLabel lblEstadoOperacion, lblSubtotal, lblTotal, lblHora;
    private JButton btnCobrar, btnNuevaVenta;
    private JScrollPane scrollProductos, scrollCarrito;
    
    private List<ItemVenta> carrito = new ArrayList<>();
    
//                    dar de alta el movimiento daleee
//                            daleeeee
//                            asdsajjskadjdasjsajlasdl
//                                    
//                                    nankanlnlnlsdandsannals
//                                    falta crear el proyecto nuevop ... y esto no hace falta para el antartida ...
//                                    da de alta un movimiento y listo y hace la relacion con el remito y chau . 
                                    

    public VentaCajaPanel(String nombre) {
        this.nombre = nombre;
        this.setLayout(new GridLayout(1, 2));

        // IZQUIERDA: productos
        JPanel izquierda = new JPanel(new BorderLayout());
        txtBuscarProducto = new JTextField("Buscar producto...");
        productosPanel = new JPanel(new MigLayout("insets 10, fillx", "[grow][right]"));
        scrollProductos = new JScrollPane(productosPanel);

        izquierda.add(txtBuscarProducto, BorderLayout.NORTH);
        izquierda.add(scrollProductos, BorderLayout.CENTER);

        // DERECHA: carrito y resumen
        JPanel derecha = new JPanel(new BorderLayout());
        carritoPanel = new JPanel(new MigLayout("insets 10, fillx", "[grow][right]"));
        scrollCarrito = new JScrollPane(carritoPanel);

        JPanel resumenPanel = new JPanel(new MigLayout("insets 10", "[grow][right]"));
        lblSubtotal = new JLabel("Subtotal: $0.00");
        lblTotal = new JLabel("Total: $0.00");
        btnCobrar = new JButton("COBRAR");
        JButton btnCancelar = new JButton("CANCELAR VENTA");

        btnCancelar.addActionListener(e -> {
            JTabbedPane tabbedPane = (JTabbedPane) this.getParent(); // VentaPanel está en el tabbedPane
            int index = tabbedPane.indexOfComponent(this);
            if (index != -1) {
                tabbedPane.remove(index);
            }
        });

        resumenPanel.add(lblSubtotal, "span, wrap");
        resumenPanel.add(lblTotal, "span, wrap");
        resumenPanel.add(btnCobrar, "span, growx");
        resumenPanel.add(btnCancelar, "growx");
        
        JPanel carritoLabelPanel = new JPanel();
        carritoLabelPanel.setLayout(new MigLayout());
        
        carritoLabelPanel.add(new JLabel(""), "span, wrap");
        carritoLabelPanel.add(new JLabel("CARRITO: "), "span, wrap");
       
        derecha.add(carritoLabelPanel, BorderLayout.NORTH);
        derecha.add(scrollCarrito, BorderLayout.CENTER);
        derecha.add(resumenPanel, BorderLayout.SOUTH);

        // Agregamos ambos lados al panel principal
        this.add(izquierda);
        this.add(derecha);
        cargarProductosSimulados();

    }

    public String getNombre() {
        return nombre;
    }
//        private void cargarProductosDummy() {
//    productosPanel.removeAll();
//
//    agregarProducto("Coca-Cola 500ml", 500f);
//    agregarProducto("Empanada", 250f);
//    agregarProducto("Galletitas Oreo", 300f);
//
//    productosPanel.revalidate();
//    productosPanel.repaint();
//}
//        
//        
//    private void agregarProducto(String nombre, float precio) {
//    JLabel lbl = new JLabel(nombre);
//    JButton btnAgregar = new JButton("Agregar");
//
//    btnAgregar.addActionListener(e -> {
//        agregarAlCarrito(nombre, 1);
//    });
//
//    productosPanel.add(lbl);
//    productosPanel.add(btnAgregar, "wrap");
//}
    
// Método para agregar ítems al carrito
private void agregarAlCarrito(String producto, int cantidad) {
    JPanel fila = new JPanel(new MigLayout("fillx", "[grow][right]"));
    JLabel lbl = new JLabel(producto + " x" + cantidad);
    JButton btnEliminar = new JButton("x");

    btnEliminar.addActionListener(e -> {
        carritoPanel.remove(fila);
        carritoPanel.revalidate();
        carritoPanel.repaint();
    });

    fila.add(lbl, "growx");
    fila.add(btnEliminar);

    carritoPanel.add(fila, "wrap, growx");
    carritoPanel.revalidate();
    carritoPanel.repaint();
}
private void cargarProductosSimulados() {
    String[] productos = {"Gaseosa", "Alfajor", "Pan", "Café"};
    
    for (String nombre : productos) {
        JPanel fila = new JPanel(new MigLayout("fillx", "[grow][60!][60!]"));
        JLabel lblNombre = new JLabel(nombre);
        JSpinner spinnerCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        JButton btnAgregar = new JButton("Agregar");

        btnAgregar.addActionListener(e -> {
            int cantidad = (int) spinnerCantidad.getValue();
            agregarAlCarrito(nombre, cantidad);
        });

        fila.add(lblNombre, "growx");
        fila.add(spinnerCantidad);
        fila.add(btnAgregar);

        productosPanel.add(fila, "wrap, growx");
    }
}
    
//    
//private void agregarAlCarrito(String nombre, float precio) {
//    // ¿Ya está en el carrito?
//    for (ItemVenta item : carrito) {
//        if (item.getNombreProducto().equals(nombre)) {
//            item.setCantidad(item.getCantidad() + 1);
//            renderCarrito();
//            return;
//        }
//    }
//
//    // Si no está, lo agregamos nuevo
//    carrito.add(new ItemVenta(nombre, 1, precio));
//    renderCarrito();
//}

private void renderCarrito() {
    carritoPanel.removeAll();
    float subtotal = 0f;

    for (ItemVenta item : carrito) {
        JLabel lbl = new JLabel(item.getCantidad() + "x " + item.getDescripcion());
        JLabel lblTotal = new JLabel("$" + item.getPrecio());

        carritoPanel.add(lbl);
        carritoPanel.add(lblTotal, "wrap");

        subtotal += item.getPrecio();
    }

    lblSubtotal.setText("Subtotal: $" + subtotal);
    lblTotal.setText("Total: $" + subtotal); // podrías aplicar descuentos, etc.

    carritoPanel.revalidate();
    carritoPanel.repaint();
}

}
