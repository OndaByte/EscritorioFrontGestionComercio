
package com.OndaByte.MisterFront.vistas.ventas;

import com.OndaByte.MisterFront.controladores.VentaController;
import com.OndaByte.MisterFront.estilos.MisEstilos;
import com.OndaByte.MisterFront.modelos.Cliente;
import com.OndaByte.MisterFront.modelos.Venta;
import com.OndaByte.MisterFront.vistas.DatosListener;
import com.OndaByte.MisterFront.vistas.util.Dialogos;
import com.OndaByte.MisterFront.vistas.util.FechaUtils;
import com.OndaByte.MisterFront.vistas.util.IconSVG;
import com.OndaByte.MisterFront.vistas.util.Paginado;
import com.OndaByte.MisterFront.vistas.util.tabla.PanelAccion;
import com.OndaByte.MisterFront.vistas.util.tabla.TablaBuilder;
import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;

import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTextArea;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JComboBox;
import java.awt.Font;
import java.awt.Color;
import java.awt.Frame;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VentaModal extends JDialog {
 /*
    private Venta venta;
    private ArrayList<ItemVenta> items;
    private Cliente cliente;
    private JTable tabla;
    private DefaultTableModel modelTabla;
    private JScrollPane scroll;
    private JTextField inputPrecio;
    private JTextArea textAreaObservaciones;
    private JButton btnAgregar, btnGuardar;
    private JSpinner inputCantidad;
    private JComboBox<String> comboEstado;
    private JLabel lblPrecioTotal;
    private JLabel titulo;
    private VentaController ventaController;
    private JPanel panelCentro;
    private boolean editar;
    
    private static Logger logger = LogManager.getLogger(VentaModal.class.getName());

    public VentaModal(Frame parent, ArrayList<ItemVenta> items, Integer orden_id, Cliente c) {
        super(parent, "Crear Venta", true);
        this.venta = new Venta();
        this.cliente = c;
        this.items = items;
        this.venta.setOrden_id(orden_id);
        this.editar = false;
        
        ventaController = VentaController.getInstance();
        initVista();
        setSize(600, 640);
        setLocationRelativeTo(null);
    }
    
    public VentaModal(Frame parent,Venta r, ArrayList<ItemVenta> items, Cliente c) {
        super(parent, "Editar Venta", true);
        this.venta = r;
        this.cliente = c;
        this.items = items;
        this.editar = true;
        
        ventaController = VentaController.getInstance();
        initVista();
        setSize(600, 640);
        setLocationRelativeTo(null);
    }

    private void initVista() {
        setLayout(new MigLayout("fillx, wrap 1", "[grow]", "[]10[]5[]5"));
        // ---------- Panel Top ----------
        JPanel panelTop = renderPanelTop();
        // ---------- Panel Centro ----------
        panelCentro = renderPanelCentro();
        // ---------- Panel Bottom ----------
        JPanel panelBottom = renderPanelBottom();
        // ---------- Armado Final ----------
        add(panelTop, "grow, height 200!");
        add(panelCentro, "grow, height 240!");
        add(panelBottom, "growx");
        actualizarPrecioTotal();

        btnGuardar.addActionListener(e -> validarYGuardar());
    }

    private JPanel renderPanelTop() {
        JPanel panelTop = new JPanel(new MigLayout("fillx, wrap 1", "[grow]", "[]20[]20[]20[]10[]"));

        titulo = new JLabel("Venta de: " + cliente.getNombre());
        titulo.setFont(new Font("Arial", Font.BOLD, 16));

        // Nuevo JTextArea para el item
        JTextArea areaDescripcionItem = new JTextArea(3, 30);
        areaDescripcionItem.setLineWrap(true);
        areaDescripcionItem.setWrapStyleWord(true);
        JScrollPane scrollDescripcionItem = new JScrollPane(areaDescripcionItem);

        inputPrecio = new JTextField();
        inputCantidad = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
        btnAgregar = new JButton("Agregar");

        MisEstilos.aplicarEstilo(btnAgregar, MisEstilos.BOTON_LOGIN);

        JPanel panelInputs = new JPanel(new MigLayout("insets 0, wrap", "[60%][25%][15%]", "[]5[]5"));
        // Fila 1: 
        panelInputs.add(new JLabel("  Ítem:"), "align left");
        panelInputs.add(new JLabel("  Precio:"), "align left");
        panelInputs.add(new JLabel("  Cantidad:"), "align left");
        // Fila 2: 
        panelInputs.add(scrollDescripcionItem, "growx, height 60::80");
        panelInputs.add(inputPrecio, "growx, gapleft 5");
        panelInputs.add(inputCantidad, "growx, gapleft 5");
        panelInputs.add(btnAgregar, "growx, gapleft 5");

        panelTop.add(titulo, "align left, gaptop 10, gapbottom 10");
        panelTop.add(panelInputs, "growx");
        // ---------- Acciones ----------
        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String descripcion = areaDescripcionItem.getText().trim();
                String precioStr = inputPrecio.getText().trim();
                String cantidadStr = inputCantidad.getValue().toString();
                if (!descripcion.isEmpty() && !precioStr.isEmpty()) {
                    try {
                        Float precio = Float.parseFloat(precioStr);
                        Integer cantidad = Integer.parseInt(cantidadStr);
                        //pasó el control
                        ItemVenta ir = new ItemVenta();
                        ir.setDescripcion(descripcion);
                        ir.setPrecio(precio);
                        ir.setCantidad(cantidad);
                        items.add(ir);
                        modelTabla.addRow(new Object[]{descripcion, precioStr, cantidad});
                        areaDescripcionItem.setText("");
                        inputPrecio.setText("");
                        actualizarPrecioTotal();
                    } catch (NumberFormatException exception) {
                        Dialogos.mostrarError("Debes ingresar un precio válido");
                    }
                } else {
                    Dialogos.mostrarError("Debes completar ambos campos.");
                }
            }
        });
        return panelTop;
    }

    private JPanel renderPanelCentro() {
        JPanel panelCentro = new JPanel(new MigLayout("fill, wrap", "[grow]", "[grow]"));

        // Cabeceras
        String[] headers = new String[]{"Item", "Precio", "Cantidad", "Acciones"};

        // Cargar datos en rows
        List<Object[]> rows = new ArrayList<>();
        for (ItemVenta ir : items) {
            rows.add(new Object[]{ir.getDescripcion(), ir.getPrecio(), ir.getCantidad()});
        }

        // Configurar columna de acciones
        BiConsumer<PanelAccion, Integer> configurador = (panelAccion, row) -> {
            panelAccion.agregarBoton("eliminar", IconSVG.RECHAZAR, e -> {
                 if (tabla.isEditing()) {
                    tabla.getCellEditor().stopCellEditing();
                }

                int r = row;
                // Eliminar de la lista
                if (r >= 0 && r < items.size()) {
                    items.remove(r);
                }

                // Eliminar fila visual de la tabla
                DefaultTableModel model = (DefaultTableModel) tabla.getModel();
                if (r >= 0 && r < model.getRowCount()) {
                    model.removeRow(r);
                }
                actualizarPrecioTotal();
                // Refrescar tabla
                tabla.revalidate();
                tabla.repaint();
            });
        };

        // Crear tabla con builder
        TablaBuilder builder = new TablaBuilder(headers, rows, headers.length - 1, configurador);
        scroll = builder.crearTabla();
        tabla = builder.getTable();
        modelTabla = (DefaultTableModel) tabla.getModel();

        // Estilos (si usás FlatLaf u otros)
        tabla.getTableHeader().putClientProperty(FlatClientProperties.STYLE, MisEstilos.HEADER_TABLA);
        tabla.putClientProperty(FlatClientProperties.STYLE, MisEstilos.TABLA);

        // Agregar tabla al panel
        panelCentro.add(scroll, "grow");

        // Observaciones
        panelCentro.add(new JLabel("Observaciones:"), "grow");
        textAreaObservaciones = new JTextArea(2, 20);
        textAreaObservaciones.setText(this.venta.getObservaciones());
        panelCentro.add(new JScrollPane(textAreaObservaciones), "growx, height 50::80");

        return panelCentro;
    }

    private JPanel renderPanelBottom() {
        JPanel panelBottom = new JPanel(new MigLayout("fillx, wrap 3", "[grow][pref!][pref!]", "[]15[]"));

        lblPrecioTotal = new JLabel("Precio Total: $0.00");
        comboEstado = new JComboBox<>(new String[]{"Pendiente"});
        btnGuardar = new JButton("Guardar");

        panelBottom.add(lblPrecioTotal, "align left");
        panelBottom.add(comboEstado, "align right");
        panelBottom.add(btnGuardar, "split 2, align right");
        return panelBottom;
    }

    private void actualizarPrecioTotal() {
        double total = 0.0;
        for (int i = 0; i < modelTabla.getRowCount(); i++) {
            total += (Float.parseFloat(modelTabla.getValueAt(i, 1).toString()) * Integer.parseInt(modelTabla.getValueAt(i, 2).toString())); //ta asegurado que no se rompe por que ya lo valide en el agregar
        }
        lblPrecioTotal.setText(String.format("Precio Total: $%.2f", total));
    }

    private void validarYGuardar() {
        String errores = validarFormulario();
        if (errores.isEmpty()) {
            Venta nuevoVenta = this.crearVenta();
            if(!editar){
                ventaController.crearVenta(nuevoVenta, items,
                    new DatosListener<String>() {
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
            }else{
                nuevoVenta.setId(this.venta.getId());
                ventaController.editarVenta(nuevoVenta, items,
                    new DatosListener<String>() {
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
            }
            this.dispose();
        } else {
            Dialogos.mostrarError(errores);
        }
    }

    private Venta crearVenta() {
        Venta r = new Venta();
        r.setOrden_id(venta.getOrden_id());
        r.setFecha_emision(FechaUtils.ldToString(LocalDate.now()));
        r.setC_cuit_cuil(this.cliente.getCuit_cuil());
        r.setC_domicilio(this.cliente.getDireccion());
        r.setC_localidad(this.cliente.getLocalidad());
        r.setC_nombre(this.cliente.getNombre());
        r.setC_telefono(this.cliente.getTelefono());
        r.setCliente_id(this.cliente.getId());
        r.setPunto_venta("00006");
        if (textAreaObservaciones != null && !textAreaObservaciones.getText().isEmpty())
            r.setObservaciones(textAreaObservaciones.getText());
        Float total = Float.parseFloat(lblPrecioTotal.getText().split("\\$")[1].replace(",", "."));
        logger.debug(total);
        r.setTotal(total);
        return r;
    }

    private String validarFormulario() {
        StringBuilder errores = new StringBuilder("<html>");

//        if (taObservaciones == null || taObservaciones.getText().trim().isEmpty()) {
//            errores.append("- El campo Descripción es obligatorio.<br>");
//            if (taObservaciones != null) {
//                taObservaciones.setBorder(BorderFactory.createLineBorder(Color.RED));
//            }
//        }

        if (tabla.getRowCount() == 0) {
            errores.append("- Debe agregar al menos un item al presupuesto.<br>");
            tabla.setBorder(BorderFactory.createLineBorder(Color.RED));
        }

        if (errores.length() > 6) { // Hay errores
            errores.append("</html>");
            return errores.toString();
        } else { // Todo OK
            return "";
        }
    }


  */
}
