
package com.OndaByte.MisterFront.vistas.caja;

import com.OndaByte.MisterFront.controladores.MovimientoController;
import com.OndaByte.MisterFront.controladores.ProductoController;
import com.OndaByte.MisterFront.modelos.ItemVenta;
import com.OndaByte.MisterFront.modelos.Producto;
import com.OndaByte.MisterFront.vistas.DatosListener;
import com.OndaByte.MisterFront.vistas.util.Dialogos;
import com.OndaByte.MisterFront.vistas.util.Paginado;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.event.ChangeListener;

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
    //private Map<String, JSpinner> mapaCarrito = new HashMap<>();
    private Map<String, FilaCarrito> mapaCarrito = new HashMap<>();

    private MovimientoController cajaController;
    private ProductoController productoController;

//                    dar de alta el movimiento daleee
//                            daleeeee
//                            asdsajjskadjdasjsajlasdl
//                                    
//                                    nankanlnlnlsdandsannals
//                                    falta crear el proyecto nuevop ... y esto no hace falta para el Mister ...
//                                    da de alta un movimiento y listo y hace la relacion con el remito y chau . 


    public VentaCajaPanel(String nombre) {
        this.nombre = nombre;
        this.setLayout(new GridLayout(1, 2));

        // IZQUIERDA: productos
        JPanel izquierda = new JPanel(new BorderLayout());
        txtBuscarProducto = new JTextField();
        productosPanel = new JPanel(new MigLayout("insets 10, fillx", "[grow][right]"));
        scrollProductos = new JScrollPane(productosPanel);
        scrollProductos.setBorder(BorderFactory.createTitledBorder("Productos"));


        izquierda.add(txtBuscarProducto, BorderLayout.NORTH);
        izquierda.add(scrollProductos, BorderLayout.CENTER);

        // DERECHA: carrito y resumen
        JPanel derecha = new JPanel(new BorderLayout());
        carritoPanel = new JPanel(new MigLayout("insets 10, fillx", "[grow][right]"));
        scrollCarrito = new JScrollPane(carritoPanel);
        scrollCarrito.setBorder(BorderFactory.createTitledBorder("Carrito"));


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

        //JPanel carritoLabelPanel = new JPanel();
        //carritoLabelPanel.setLayout(new MigLayout());

        //carritoLabelPanel.add(new JLabel(""), "span, wrap");1
        //carritoLabelPanel.add(new JLabel("CARRITO: "), "span, wrap");

        //derecha.add(carritoLabelPanel, BorderLayout.NORTH);
        derecha.add(scrollCarrito, BorderLayout.CENTER);
        derecha.add(resumenPanel, BorderLayout.SOUTH);

        // Agregamos ambos lados al panel principal
        this.add(izquierda);
        this.add(derecha);
        // c                                supongo que es suficiente margen
        productoController.filtrar("","" + 1, "" + 1000, new DatosListener<List<Producto>>(){
            @Override
            public void onSuccess(List<Producto> datos) {
            }

            @Override
            public void onError(String mensajeError) {
                Dialogos.mostrarError(mensajeError);
                revalidate();
                repaint();
            }

            @Override
            public void onSuccess(List<Producto> datos, Paginado p) {
                cargarProductosSimulados(datos);
//                turnos = new ArrayList<>(datos);
//                renderEventos();
//                renderEventos();
            }
        });
    }

    public String getNombre() {
        return nombre;
    }
//    private void cargarProductosDummy() {
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
        JButton btnEliminar = new JButton("x");

        // Labels de precio y subtotal
        JLabel lblPrecioUnitario = new JLabel("Precio unitario: $" + precioUnitario);
        JLabel lblSubtotal = new JLabel("Subtotal: $" + precioUnitario);

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
            lblSubtotal.setText("Subtotal: $" + String.format("%.2f", subtotal));
        };
        spinnerCantidad.addChangeListener(recalcular);
        spinnerDescuento.addChangeListener(recalcular);

        // Acción eliminar
        btnEliminar.addActionListener(e -> {
            carritoPanel.remove(contenedorFila);
            carritoPanel.revalidate();
            carritoPanel.repaint();
            mapaCarrito.remove(producto);
        });

        // Agregar fila principal (nombre, cantidad, descuento, eliminar)
        contenedorFila.add(lblNombre, "growx");
        contenedorFila.add(lblCant);
        contenedorFila.add(spinnerCantidad);
        contenedorFila.add(btnEliminar, "wrap");

        // Agregar precio unitario debajo
        contenedorFila.add(lblPrecioUnitario);
        contenedorFila.add(lblDesc);
        contenedorFila.add(spinnerDescuento, "span, wrap");

        // Agregar subtotal debajo
        contenedorFila.add(lblSubtotal, "span");

        // Añadir al panel del carrito
        carritoPanel.add(contenedorFila, "wrap, growx");
        carritoPanel.revalidate();
        carritoPanel.repaint();
    }


    private void cargarProductosSimulados(List<Producto> productos) {
        for (Producto p : productos) {
            JPanel fila = new JPanel(new MigLayout("fillx, insets 10", "[grow]10[60!]10"));
            JLabel lblNombre = new JLabel(p.getNombre());
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
