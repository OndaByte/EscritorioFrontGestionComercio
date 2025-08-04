
package com.OndaByte.AntartidaFront.vistas.presupuestos;

import com.OndaByte.AntartidaFront.controladores.PresupuestoController;
import com.OndaByte.AntartidaFront.estilos.MisEstilos;
import com.OndaByte.AntartidaFront.modelos.*;
import com.OndaByte.AntartidaFront.vistas.DatosListener;
import com.OndaByte.AntartidaFront.vistas.util.Dialogos;
import com.OndaByte.AntartidaFront.vistas.util.IconSVG;
import com.OndaByte.AntartidaFront.vistas.util.Paginado;
import com.OndaByte.AntartidaFront.vistas.util.tabla.PanelAccion;
import com.OndaByte.AntartidaFront.vistas.util.tabla.TablaBuilder;
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
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PresupuestoModal extends JDialog {

    private Presupuesto presupuesto;
    private ArrayList<ItemPresupuesto> items;
    private Pedido pedido;
    private Cliente cliente;
    private JTable tabla;
    private DefaultTableModel modelTabla;
    private JScrollPane scroll;
    private JTextArea textAreaDescripcion;
    private JTextField inputPrecio;
    private JSpinner inputCantidad;
    private JButton btnAgregar, btnGuardar;
    private JComboBox<String> comboEstado;
    private JLabel lblPrecioTotal;
    private JLabel titulo;
    private PresupuestoController presupuestoController;
    private boolean editar;
    
    
    private static Logger logger = LogManager.getLogger(PresupuestoModal.class.getName());

    public PresupuestoModal(Frame parent, Pedido p, Cliente c) {
        super(parent, "Crear Presupuesto", true);
        this.editar = false;
        this.pedido = p;
        this.cliente = c;
        this.presupuesto = new Presupuesto();
        items = new ArrayList<>();
        this.presupuesto.setPedido_id(p.getId());
        this.presupuesto.setNombre(this.cliente.getNombre());
        this.presupuesto.setDescripcion(this.pedido.getDescripcion());
        presupuestoController = PresupuestoController.getInstance();
        initVista();
        setSize(600, 640);
        setLocationRelativeTo(null);
    }

    public PresupuestoModal(Frame parent, Cliente c, Presupuesto pre, ArrayList<ItemPresupuesto> items ) {
        super(parent, "Editar Presupuesto", true);
        this.editar = true;
        this.pedido = null;
        this.cliente = c;
        this.presupuesto = pre;
        this.items = items;

        presupuestoController = PresupuestoController.getInstance();
        initVista();
        setSize(600, 640);
        setLocationRelativeTo(null);
    }

    /*
    public PresupuestoModal(Frame parent, Presupuesto pre) {
        super(parent, "Editar Presupuesto", true);
        this.presupuesto = pre;
        this.presupuesto.setPedido_id(pre.getPedido_id());
        this.presupuesto.setNombre(pre.getNombre());
        this.presupuesto.setDescripcion(pre.getDescripcion());
        presupuestoController = PresupuestoController.getInstance();
        initVista();
        setSize(600, 600);
    }
     */

    private void initVista() {
        setLayout(new MigLayout("fillx, wrap 1", "[grow]", "[]10[]10[]5"));
        // ---------- Panel Top ----------
        JPanel panelTop = renderPanelTop();
        // ---------- Panel Centro ----------
        JPanel panelCentro = renderPanelCentro();
        // ---------- Panel Bottom ----------
        JPanel panelBottom = renderPanelBottom();
        // ---------- Armado Final ----------
        add(panelTop, "growx");
        add(panelCentro, "grow, height 180!");
        add(panelBottom, "growx");
        actualizarPrecioTotal();

        btnGuardar.addActionListener(e -> validarYGuardar());
    }

    private JPanel renderPanelTop() {
        //JPanel panelTop = new JPanel(new MigLayout("fillx, wrap 1", "[grow]", "[]15[]15[]5"));
        JPanel panelTop = new JPanel(new MigLayout("fillx, wrap 1", "[grow]", "[]20[]20[]20[]10[]"));


        titulo = new JLabel("Presupuesto para: " + cliente.getNombre());
        titulo.setFont(new Font("Arial", Font.BOLD, 16));

        textAreaDescripcion = new JTextArea(2, 20);
        textAreaDescripcion.setText(presupuesto.getDescripcion());

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

        //panelTop.add(titulo, "align left");
        panelTop.add(titulo, "align left, gaptop 10, gapbottom 10");
        panelTop.add(new JLabel("Descripción general del presupuesto:"), "align left");
        panelTop.add(new JScrollPane(textAreaDescripcion), "growx, height 50::70");
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
                        ItemPresupuesto ip = new ItemPresupuesto();
                        ip.setDescripcion(descripcion);
                        ip.setPrecio(precio);
                        ip.setCantidad(cantidad);

                        items.add(ip);
                        modelTabla.addRow(new Object[]{descripcion, precioStr, cantidadStr});
                        areaDescripcionItem.setText("");
                        inputPrecio.setText("");
                        inputCantidad.setValue(1);
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
        for (ItemPresupuesto ip : items) {
            rows.add(new Object[]{ip.getDescripcion(), ip.getPrecio(), ip.getCantidad()});
        }

        // Configurar columna de acciones
        BiConsumer<PanelAccion, Integer> configurador = (panelAccion, row) -> {
            panelAccion.agregarBoton("eliminar", IconSVG.RECHAZAR, e -> {
                // Detener cualquier edición activa antes de modificar el modelo
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

        return panelCentro;
    }


    private JPanel renderPanelBottom() {
        //JPanel panelBottom = new JPanel(new MigLayout("fillx, wrap 3", "[grow][pref!][pref!]", "[]5[]"));
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
            Presupuesto nuevoPresupuesto = this.crearPresupuesto();
            if(!editar){
                presupuestoController.crearPresupuesto(nuevoPresupuesto, items,
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
                nuevoPresupuesto.setId(this.presupuesto.getId());
                presupuestoController.editarPresupuesto(nuevoPresupuesto, items,
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

    private Presupuesto crearPresupuesto() {
        Presupuesto p = new Presupuesto();
        if(editar)
                p.setPedido_id(presupuesto.getPedido_id());
            else
                p.setPedido_id(pedido.getId());
        
        p.setDescripcion(textAreaDescripcion.getText());
        p.setNombre(titulo.getText());
        p.setEstado_presupuesto("PENDIENTE");
        Float total = Float.parseFloat(lblPrecioTotal.getText().split("\\$")[1].replace(",", "."));
        logger.debug(total);
        p.setTotal(total);
        return p;
    }

    private String validarFormulario() {
        StringBuilder errores = new StringBuilder("<html>");

        if (textAreaDescripcion == null || textAreaDescripcion.getText().trim().isEmpty()) {
            errores.append("- El campo Descripción es obligatorio.<br>");
            if (textAreaDescripcion != null) {
                textAreaDescripcion.setBorder(BorderFactory.createLineBorder(Color.RED));
            }
        }

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

}
