
package com.OndaByte.MisterFront.vistas.caja;

import com.OndaByte.MisterFront.controladores.MovimientoController;
import com.OndaByte.MisterFront.controladores.ProductoController;
import com.OndaByte.MisterFront.estilos.MisEstilos;
import com.OndaByte.MisterFront.modelos.ItemVenta;
import com.OndaByte.MisterFront.modelos.Producto;
import com.OndaByte.MisterFront.sesion.SesionController;
import com.OndaByte.MisterFront.vistas.DatosListener;
import com.OndaByte.MisterFront.vistas.MiFrame;
import com.OndaByte.MisterFront.vistas.util.Dialogos;
import com.OndaByte.MisterFront.vistas.util.Paginado;

import java.util.*;
import javax.swing.*;
import javax.swing.event.ChangeListener;
import com.OndaByte.MisterFront.vistas.util.IconSVG;
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.miginfocom.swing.MigLayout;

public class VentaCajaPanel extends JPanel {

    private int idTab;
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

    private MovimientoController cajaController;
    private ProductoController productoController;
    HashSet<String> permisos = null;

    private JPanel izquierda, derecha;
    
    private String filtro;
    private java.util.Timer timer;

    private JComponent parent;
    
    public VentaCajaPanel(JComponent parent,String nombre,int idTab) {
        this.permisos = (HashSet<String>) SesionController.getInstance().getSesionPermisos();
        this.idTab=idTab;
        this.nombre = nombre + " " + idTab;
        this.total = 0f;
        this.subtotal = 0f;
        this.parent=parent;
        this.setLayout(new MigLayout("insets 5, gap 10", "[grow 50]10[grow 50]", "[grow]"));
        this.initPanelIzquierda();
        this.initPanelDerecha();
        this.add(izquierda, "grow");
        this.add(derecha, "grow");
        this.productoController = ProductoController.getInstance();
        this.cajaController = MovimientoController.getInstance();
        this.filtro="";

        productoController.filtrar(filtro,"" + 1, "" + 1000, new DatosListener<List<Producto>>(){
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
                addAcciones();
                cargarProductos(datos);
                revalidate();
                repaint();
            }
        });
    }

    /**
     * Renderiza de nuevo los elementos de la tabla y paginado con el filtro actual. 
     */
    private void reload(){
        productoController.filtrar(filtro,"" + 1, "" + 1000, new DatosListener<List<Producto>>(){
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
                cargarProductos(datos);
                revalidate();
                repaint();
            }
        });
    };
    
    private void cerrar(){
        MostradorCajaPanel mcp = (MostradorCajaPanel) this.parent;
        mcp.quitarNuevoPanelVenta(this);//tema arreglos
    }
    private void initPanelIzquierda(){
        izquierda = new JPanel(new MigLayout("insets 5, fill", "[grow]", "[]10[grow]"));

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
        scrollProductos.getVerticalScrollBar().setUnitIncrement(30);
        
        izquierda.add(txtBuscarProducto, "growx, wrap");
        izquierda.add(scrollProductos, "grow");
    }

    private void initPanelDerecha(){
        derecha = new JPanel(new MigLayout("insets 5, fill", "[grow]", "[grow][]"));

        carritoPanel = new JPanel(new MigLayout("insets 10, fillx", "[grow][right]"));
        scrollCarrito = new JScrollPane(carritoPanel);
        scrollCarrito.setBorder(BorderFactory.createTitledBorder("CARRITO"));
        scrollCarrito.getVerticalScrollBar().setUnitIncrement(30);

        JPanel resumenPanel = new JPanel(new MigLayout("insets 10", "[grow][right]"));
        lblSubtotal = new JLabel("Subtotal: $0.00");
        lblSubtotal.setFont(new Font("Courier New", Font.BOLD, 16));

        JLabel lblDescuentoExtra = new JLabel("Descuento (%):");
        setVisibleByPermisos(lblDescuentoExtra, "DESCUENTO_SOBRE_TOTAL");
        lblDescuentoExtra.setFont(new Font("Courier New", Font.PLAIN, 14));
        if(spinnerDescuentoExtra == null){
            spinnerDescuentoExtra = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        }
        setVisibleByPermisos(spinnerDescuentoExtra, "DESCUENTO_SOBRE_TOTAL");
        spinnerDescuentoExtra.addChangeListener(e -> actualizarTotales());

        lblTotal = new JLabel("Total: $0.00");
        lblTotal.setFont(new Font("Courier New", Font.BOLD, 16));
        btnCobrar = new JButton("COBRAR", new IconSVG(IconSVG.COBRAR));
        JButton btnCancelar = new JButton("CANCELAR VENTA", new IconSVG(IconSVG.CANCELAR));

        btnCancelar.addActionListener(e -> {
            cerrar();
        });
        
        btnCobrar.addActionListener(e -> {
            vender();
        });
        resumenPanel.add(lblSubtotal);
        resumenPanel.add(lblDescuentoExtra);
        resumenPanel.add(spinnerDescuentoExtra, "wrap");
        resumenPanel.add(lblTotal, "span, wrap");
        resumenPanel.add(btnCancelar, "spany 3, grow");
        resumenPanel.add(btnCobrar, "spany 3, span, growy, growx");

        derecha.add(scrollCarrito, "grow, wrap");
        derecha.add(resumenPanel, "growx");
    }

    private void setVisibleByPermisos(JComponent c, String permiso){
        c.setVisible(permisos.contains(permiso));
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
        total = subtotal * (1 - descuentoExtra / 100f);

        lblTotal.setText("Total: $" + String.format("%.2f", total));
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
        int producto_id;
    }
    
    private void agregarAlCarrito(Producto p) {
        float precioUnitario = p.getProductoPrecioUnitario();
        String producto = p.getNombre();
        int unidades = 1;
        int stock = p.getStock();

        // Si ya existe → actualizamos cantidad
        if (mapaCarrito.containsKey(producto)) {
            FilaCarrito filaExistente = mapaCarrito.get(p.getNombre());
            unidades = (int) filaExistente.spinnerCantidad.getValue();
            if(stock>unidades){
                unidades++;
                //stock--;
                filaExistente.spinnerCantidad.setValue(unidades);
            }
            return;
        }
        //int stockTotal = unidades + stock;
        //p.setStock(stockTotal-unidades);

        // Panel contenedor de la fila (para todo el bloque de info)
        JPanel contenedorFila = new JPanel(new MigLayout("fillx, insets 5", "[grow][100!]10[60!]10[40!]"));
        contenedorFila.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

        // Label principal con nombre del producto
        JLabel lblNombre = new JLabel(producto);
        JLabel lblCant = new JLabel("Unidades:");
        JLabel lblDesc = new JLabel("Descuento (%):");
        setVisibleByPermisos(lblDesc, "DESCUENTO_PRECIO_UNITARIO");

        // Spinner cantidad
        JSpinner spinnerCantidad = new JSpinner(new SpinnerNumberModel(1, 1, stock, 1));

        // Spinner descuento (%)
        JSpinner spinnerDescuento = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        setVisibleByPermisos(spinnerDescuento, "DESCUENTO_PRECIO_UNITARIO");

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
        filaCarrito.producto_id = p.getId();
        mapaCarrito.put(producto, filaCarrito);

        // Listeners para recalcular subtotal
        ChangeListener recalcular = e -> {
            int cant = (int) spinnerCantidad.getValue();
            //p.setStock(stockTotal-cant);
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
            //p.setStock(stockTotal);
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
        contenedorFila.add(new JLabel());
        contenedorFila.add(new JLabel("Subtotal: $"));
        contenedorFila.add(lblSubtotal, "span, wrap");

        // Añadir al panel del carrito
        carritoPanel.add(contenedorFila, "wrap, growx");
        carritoPanel.revalidate();
        carritoPanel.repaint();
        actualizarSubTotales();
    }

 
    private void cargarProductos(List<Producto> productos) {
        productosPanel.removeAll();
        for (Producto p : productos) {

            JPanel fila = new JPanel(new MigLayout("fillx, insets 10", "[grow]10[80!]10[80!]"));
    
            // Nombre
            JLabel lblNombre = new JLabel(p.getNombre());
            lblNombre.setFont(new Font("Courier New", Font.BOLD, 16));

            // Stock
            JLabel lblStock = new JLabel("Stock: " + p.getStock());
            lblStock.setFont(new Font("Courier New", Font.PLAIN, 14));

            // Botón Agregar
            JButton btnAgregar = new JButton("Agregar");
            btnAgregar.setPreferredSize(new Dimension(80, 30));
            btnAgregar.setBackground(new Color(0x4CAF50)); // verde material
            btnAgregar.setForeground(Color.WHITE);
            btnAgregar.setFocusPainted(false);
            btnAgregar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

            btnAgregar.addActionListener(e -> {
                agregarAlCarrito(p);
            });

            // Agregar componentes a la fila
            fila.add(lblNombre, "growx");   // columna 1: nombre
            fila.add(lblStock, "center");   // columna 2: stock
            fila.add(btnAgregar, "center"); // columna 3: botón

            productosPanel.add(fila, "wrap, growx");
        }
    }

    private void addAcciones(){
        txtBuscarProducto.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filtrarProducto();}

            public void removeUpdate(DocumentEvent e) { filtrarProducto();}

            public void changedUpdate(DocumentEvent e) { filtrarProducto(); }

            private void filtrarProducto() {
                filtro = txtBuscarProducto.getText(); 
                if (timer != null) {
                    timer.cancel(); // Cancela el intento anterior
                }
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        reload();
                    }
                }, 300); 
            }

        });
    }
    

    private void renderCarrito() {
        carritoPanel.removeAll();
        float subtotal = 0f;

        for (ItemVenta item : carrito) {
            JLabel lbl = new JLabel(item.getCantidad() + "x " + item.getNombre());
            JLabel lblTotal = new JLabel("$" + item.getSubtotal());

            carritoPanel.add(lbl);
            carritoPanel.add(lblTotal, "wrap");

            subtotal += item.getSubtotal();
        }

        lblSubtotal.setText("Subtotal: $" + subtotal);
        lblTotal.setText("Total: $" + subtotal); // podrías aplicar descuentos, etc.

        carritoPanel.revalidate();
        carritoPanel.repaint();
    }
    
    private void vender(){
        
        ArrayList<String> productos = new ArrayList<>(mapaCarrito.keySet());
        ArrayList<ItemVenta> items = new ArrayList<>();
        for (String nombre : productos){
            FilaCarrito fc = mapaCarrito.get(nombre);
            ItemVenta iv = new ItemVenta();
            iv.setNombre(nombre);
            iv.setCantidad((int) fc.spinnerCantidad.getValue());
            iv.setPorcentaje_descuento((int) fc.spinnerDescuento.getValue());
            iv.setSubtotal(Float.valueOf(fc.lblSubtotal.getText().replace(",", ".")));
            iv.setProducto_id( fc.producto_id );
            items.add(iv);
        }
        VentaCajaModal modal = new VentaCajaModal(MiFrame.getInstance(),items,subtotal,total,(int) spinnerDescuentoExtra.getValue());//itemsR, o.getId(), c
        modal.setVisible(true); // bloquea el thread hasta que es cerrado
        filtro = "";
//        pagina = 1;
        reload();
        cerrar();
        //cancelar venta.doclick();
    }

}