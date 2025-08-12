
package com.OndaByte.MisterFront.vistas.caja;

import com.OndaByte.MisterFront.estilos.MisEstilos;
import com.OndaByte.MisterFront.modelos.ItemVenta;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.event.ChangeListener;

import com.OndaByte.MisterFront.vistas.util.IconSVG;
import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;

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
    private Map<String, FilaCarrito> mapaCarrito = new HashMap<>();
    private JSpinner spinnerDescuentoExtra;

//                    dar de alta el movimiento daleee
//                            daleeeee
//                            asdsajjskadjdasjsajlasdl
//                                    
//                                    nankanlnlnlsdandsannals
//                                    falta crear el proyecto nuevop ... y esto no hace falta para el Mister ...
//                                    da de alta un movimiento y listo y hace la relacion con el remito y chau . 


    public VentaCajaPanel(String nombre) {
        this.nombre = nombre;
        this.total = 0f;
        this.subtotal = 0f;

        // MigLayout: 2 columnas, ambas crecen, espacio entre columnas
        this.setLayout(new MigLayout("insets 5, gap 10", "[grow]10[grow]", "[grow]"));

        // ======== IZQUIERDA: productos ========
        JPanel izquierda = new JPanel(new MigLayout("insets 5, fill", "[grow]", "[]10[grow]"));

        txtBuscarProducto = new JTextField();
        txtBuscarProducto.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, MisEstilos.PLACEHOLDER_BUSQUEDA);
        MisEstilos.aplicarEstilo(txtBuscarProducto, MisEstilos.BUSQUEDA);
        txtBuscarProducto.setPreferredSize(new Dimension(200, 40));
        txtBuscarProducto.setMaximumSize(new Dimension(250, 40));
        txtBuscarProducto.setMinimumSize(new Dimension(150, 40));

        txtBuscarProducto.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, new IconSVG(IconSVG.LUPA));
        productosPanel = new JPanel(new MigLayout("insets 5, fillx", "[grow][right]"));
        scrollProductos = new JScrollPane(productosPanel);
        scrollProductos.setBorder(BorderFactory.createTitledBorder("PRODUCTOS"));

        izquierda.add(txtBuscarProducto, "growx, wrap");
        izquierda.add(scrollProductos, "grow");

        // ======== DERECHA: carrito y resumen ========
        JPanel derecha = new JPanel(new MigLayout("insets 5, fill", "[grow]", "[grow][]"));

        carritoPanel = new JPanel(new MigLayout("insets 10, fillx", "[grow][right]"));
        scrollCarrito = new JScrollPane(carritoPanel);
        scrollCarrito.setBorder(BorderFactory.createTitledBorder("CARRITO"));

        JPanel resumenPanel = new JPanel(new MigLayout("insets 10", "[grow][right]"));
        lblSubtotal = new JLabel("Subtotal: $0.00");
        lblSubtotal.setFont(new Font("Courier New", Font.BOLD, 16));

        JLabel lblDescuentoExtra = new JLabel("Descuento (%):");
        lblDescuentoExtra.setFont(new Font("Courier New", Font.PLAIN, 14));
        spinnerDescuentoExtra = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        spinnerDescuentoExtra.addChangeListener(e -> actualizarTotales());

        lblTotal = new JLabel("Total: $0.00");
        lblTotal.setFont(new Font("Courier New", Font.BOLD, 16));
        btnCobrar = new JButton("COBRAR");
        JButton btnCancelar = new JButton("CANCELAR VENTA");

        btnCancelar.addActionListener(e -> {
            JTabbedPane tabbedPane = (JTabbedPane) this.getParent();
            int index = tabbedPane.indexOfComponent(this);
            if (index != -1) {
                tabbedPane.remove(index);
            }
        });

        resumenPanel.add(lblSubtotal);
        resumenPanel.add(lblDescuentoExtra);
        resumenPanel.add(spinnerDescuentoExtra, "wrap");
        resumenPanel.add(lblTotal, "span, wrap");
        resumenPanel.add(btnCobrar, "span, growx");
        resumenPanel.add(btnCancelar, "span, growx");

        derecha.add(scrollCarrito, "grow, wrap");
        derecha.add(resumenPanel, "growx");

        // ======== Agregar ambos lados al panel principal ========
        this.add(izquierda, "grow");
        this.add(derecha, "grow");

        cargarProductosSimulados();
    }



    private void actualizarTotales(){
        // Aplicar descuento extra
        int descuentoExtra = (int) spinnerDescuentoExtra.getValue();
        this.total = subtotal * (1 - descuentoExtra / 100f);

        lblTotal.setText("Total: $" + String.format("%.2f", total));
    }

    private void actualizarSubTotales() {
        this.subtotal = 0f;

        // Sumar subtotales de cada fila (precioUnitario × cantidad × (1 - descuento))
        for (Map.Entry<String, FilaCarrito> entry : mapaCarrito.entrySet()) {
            FilaCarrito fila = entry.getValue();
            int cantidad = (int) fila.spinnerCantidad.getValue();
            int descuento = (int) fila.spinnerDescuento.getValue();
            float precioUnitario = fila.precioUnitario;

            float subtotalItem = precioUnitario * cantidad * (1 - descuento / 100f);
            subtotal += subtotalItem;
        }

        // Mostrar subtotal
        lblSubtotal.setText("Subtotal: $" + String.format("%.2f", subtotal));

        // Aplicar descuento extra
        int descuentoExtra = (int) spinnerDescuentoExtra.getValue();
        float totalConExtra = subtotal * (1 - descuentoExtra / 100f);

        lblTotal.setText("Total: $" + String.format("%.2f", totalConExtra));
    }

    public String getNombre() {
        return nombre;
    }

    // Clase interna para guardar referencias a los componentes de una fila
    private class FilaCarrito {
        JSpinner spinnerCantidad;
        JSpinner spinnerDescuento;
        JLabel lblSubtotal;
        float precioUnitario;
    }

    private void agregarAlCarrito(String producto) {
        float precioUnitario = 100f; // fijo por ahora

        // Si ya existe → actualizamos cantidad
        if (mapaCarrito.containsKey(producto)) {
            FilaCarrito filaExistente = mapaCarrito.get(producto);
            int cantidadActual = (int) filaExistente.spinnerCantidad.getValue();
            filaExistente.spinnerCantidad.setValue(cantidadActual + 1);
            return;
        }

        // Panel contenedor de la fila (para todo el bloque de info)
        JPanel contenedorFila = new JPanel(new MigLayout("fillx, insets 5", "[grow][100!]10[60!]10[40!]"));
        contenedorFila.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

        // Label principal con nombre del producto
        JLabel lblNombre = new JLabel(producto);
        JLabel lblCant = new JLabel("Unidades:");
        JLabel lblDesc = new JLabel("Descuento (%):");

        // Spinner cantidad
        JSpinner spinnerCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));

        // Spinner descuento (%)
        JSpinner spinnerDescuento = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));

        // Botón eliminar
        JButton btnEliminar = new JButton(new IconSVG(IconSVG.ELIMINAR));

        // Labels de precio y subtotal
        JLabel lblPrecioUnitario = new JLabel("Precio unitario: $" + precioUnitario);
        JLabel lblSubtotal = new JLabel("" + precioUnitario);
        lblSubtotal.setHorizontalAlignment(SwingConstants.CENTER);

        // Guardar referencias en mapa
        FilaCarrito filaCarrito = new FilaCarrito();
        filaCarrito.spinnerCantidad = spinnerCantidad;
        filaCarrito.spinnerDescuento = spinnerDescuento;
        filaCarrito.lblSubtotal = lblSubtotal;
        filaCarrito.precioUnitario = precioUnitario;
        mapaCarrito.put(producto, filaCarrito);

        // Listeners para recalcular subtotal
        ChangeListener recalcular = e -> {
            int cant = (int) spinnerCantidad.getValue();
            int desc = (int) spinnerDescuento.getValue();
            float subtotal = precioUnitario * cant * (1 - desc / 100f);
            lblSubtotal.setText(String.format("%.2f", subtotal));
            actualizarSubTotales();
        };
        spinnerCantidad.addChangeListener(recalcular);
        spinnerDescuento.addChangeListener(recalcular);

        // Acción eliminar
        btnEliminar.addActionListener(e -> {
            carritoPanel.remove(contenedorFila);
            carritoPanel.revalidate();
            carritoPanel.repaint();
            mapaCarrito.remove(producto);
            actualizarSubTotales();
        });

        // Agregar fila principal (nombre, cantidad, descuento, eliminar)
        contenedorFila.add(lblNombre, "growx");
        contenedorFila.add(lblCant);
        contenedorFila.add(spinnerCantidad);
        contenedorFila.add(btnEliminar, "spany 3, growy, wrap");

        // Agregar precio unitario debajo
        contenedorFila.add(lblPrecioUnitario);
        contenedorFila.add(lblDesc);
        contenedorFila.add(spinnerDescuento, "span, wrap");

        // Agregar subtotal debajo
        contenedorFila.add(new Label());
        contenedorFila.add(new Label("Subtotal: $"));
        contenedorFila.add(lblSubtotal, "span, wrap");

        // Añadir al panel del carrito
        carritoPanel.add(contenedorFila, "wrap, growx");
        carritoPanel.revalidate();
        carritoPanel.repaint();
        actualizarSubTotales();
    }


    private void cargarProductosSimulados() {
        String[] productos = {"Gaseosa", "Alfajor", "Pan", "Café"};

        for (String nombre : productos) {
            JPanel fila = new JPanel(new MigLayout("fillx, insets 10", "[grow]10[60!]10"));
            JLabel lblNombre = new JLabel(nombre);
            lblNombre.setFont(new Font("Courier New", Font.BOLD, 16));
            JButton btnAgregar = new JButton("Agregar");
            btnAgregar.setPreferredSize(new Dimension(80, 30));
            btnAgregar.setBackground(new Color(0x4CAF50)); // verde material
            btnAgregar.setForeground(Color.WHITE);
            btnAgregar.setFocusPainted(false);
            btnAgregar.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));


            btnAgregar.addActionListener(e -> {
                agregarAlCarrito(nombre);
            });

            fila.add(lblNombre, "growx");
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
