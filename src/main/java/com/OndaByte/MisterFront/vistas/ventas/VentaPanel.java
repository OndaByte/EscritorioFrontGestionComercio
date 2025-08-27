package com.OndaByte.MisterFront.vistas.ventas;

import com.OndaByte.MisterFront.controladores.VentaController;
import com.OndaByte.MisterFront.modelos.Cliente;
import com.OndaByte.MisterFront.vistas.util.VistaPreviaImpresion;
import com.OndaByte.MisterFront.estilos.MisEstilos;
import com.OndaByte.MisterFront.modelos.ItemVenta;
import com.OndaByte.MisterFront.modelos.Venta;
import com.OndaByte.MisterFront.sesion.SesionController;
import com.OndaByte.MisterFront.vistas.DatosListener;
import com.OndaByte.MisterFront.vistas.MiFrame;
import com.OndaByte.MisterFront.vistas.util.Dialogos;
import com.OndaByte.MisterFront.vistas.util.FechaUtils;
import com.OndaByte.MisterFront.vistas.util.IconSVG;
import com.OndaByte.MisterFront.vistas.util.Paginado;
import com.OndaByte.MisterFront.vistas.util.tabla.PanelAccion;
import com.OndaByte.MisterFront.vistas.util.tabla.TablaBuilder;
import com.formdev.flatlaf.FlatClientProperties;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.miginfocom.swing.MigLayout;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VentaPanel extends JPanel {

    private JTable tabla;
    private JScrollPane scroll;
    private JTextField txtBuscar;
    private JDateChooser fechaDesde, fechaHasta;
    private JButton btnEliminar, btnEditar;
    private JPanel topPanel, bottomPanel, buscarPanel, botonesPanel;
    private JComboBox<Integer> comboEstado;

    private JButton btnPrimero = new JButton("|<");
    private JButton btnAnterior = new JButton("<");
    private JTextField txtPaginaActual = new JTextField("");
    private JButton btnSiguiente = new JButton(">");
    private JButton btnUltimo = new JButton(">|");
    private JComboBox<Integer> comboTamanioPagina = new JComboBox<>(new Integer[]{10, 20, 50, 100});
    private JLabel labelPaginas = new JLabel("Página 1 de 1");

    // Resumen financiero
    private JLabel lblEfectivo;
    private JLabel lblTransferencias;
    private JLabel lblTotal;
    private JLabel lblCantEfec;
    private JLabel lblCantTransfer;
    private JLabel lblCantTotal;

    //Paginado
    private String filtro;
    private String filtroDesde;
    private String filtroHasta;
    private String filtroEstado;

    private String estado;
    private Integer pagina;
    private Integer tamPagina;
    private Integer totalElementos;
    private Integer totalPaginas;
    private Timer timer;

    private VentaController ventaControlador;
    private HashSet<String> permisos;
    private ArrayList<HashMap<String, Object>> ventasDetallado;
    private Venta ventaSeleccionada;

    private VentaPanel esta;

    private static Logger logger = LogManager.getLogger(VentaPanel.class.getName());

    static {
        if (logger.isTraceEnabled()) {
            logger.trace("Init logger en VentaPanel");
        }
    }

    public VentaPanel() {
        this.esta = this;
        this.permisos = (HashSet<String>) SesionController.getInstance().getSesionPermisos();
        ventaControlador = VentaController.getInstance();
        totalElementos = 0;
        filtro = "";
        filtroEstado = "";
        filtroDesde = "";
        filtroHasta = "";
        pagina = 1;
        tamPagina = 20;

        ventaControlador.filtrar(filtro, filtroDesde, filtroHasta, filtroEstado, "" + pagina, "" + tamPagina, new DatosListener<List<HashMap<String, Object>>>() {
            @Override
            public void onSuccess(List<HashMap<String, Object>> datos) {
            }

            @Override
            public void onError(String mensajeError) {
                Dialogos.mostrarError(mensajeError);
                revalidate();
                repaint();
            }

            @Override
            public void onSuccess(List<HashMap<String, Object>> datos, Paginado p) {
                ventasDetallado = new ArrayList<>(datos);
                esta.pagina = p.getPagina();
                esta.tamPagina = p.getTamPagina();
                esta.totalElementos = p.getTotalElementos();
                esta.totalPaginas = p.getTotalPaginas();
                initTabla(); //tabla
                setVisibleByPermisos(tabla, "VENTA_LISTAR");

                initVista(); // Inicializa la vista segun los permisos disponibles menos la tabla
                initEstilos(); //estilos menos los de la tabla
                addAcciones(); // acciones de buscador, botoones abm, etc. menos la tabla
            }
        });
            ventaControlador.resumenVenta(filtro, filtroDesde, filtroHasta, new DatosListener<HashMap<String, Object>>() {
            @Override
            public void onSuccess(HashMap<String, Object> resumen) {
                float efectivo = ((Number) resumen.get("total_efectivo")).floatValue();
                float transferencias = ((Number) resumen.get("total_transferencia")).floatValue();
                float total = ((Number) resumen.get("total_ventas")).floatValue();
                int cantEfec = ((Number) resumen.get("cant_efectivo")).intValue();
                int cantTransfer = ((Number) resumen.get("cant_transferencia")).intValue();
                actualizarResumenPanel(efectivo, transferencias, total, cantEfec, cantTransfer);
                revalidate();
                repaint();
            }

            @Override
            public void onError(String mensajeError) {
                Dialogos.mostrarError(mensajeError);
                revalidate();
                repaint();
            }

            @Override
            public void onSuccess(HashMap<String, Object> datos, Paginado p) {
            } // no usamos
        });

    }

    /**
     * Renderiza de nuevo los elementos de la tabla y paginado con el filtro
     * actual.
     */
    private void reload() {
        ventaControlador.filtrar(filtro, filtroDesde, filtroHasta, filtroEstado, "" + pagina, "" + tamPagina, new DatosListener<List<HashMap<String, Object>>>() {
            @Override
            public void onSuccess(List<HashMap<String, Object>> datos) {
            }

            @Override
            public void onError(String mensajeError) {
                Dialogos.mostrarError(mensajeError);
                revalidate();
                repaint();
            }

            @Override
            public void onSuccess(List<HashMap<String, Object>> datos, Paginado p) {
                ventasDetallado = new ArrayList<>(datos);
                esta.pagina = p.getPagina();
                esta.tamPagina = p.getTamPagina();
                esta.totalElementos = p.getTotalElementos();
                esta.totalPaginas = p.getTotalPaginas();
                remove(scroll);
                initTabla(); //tabla
                setVisibleByPermisos(tabla, "VENTA_LISTAR");
                add(scroll, BorderLayout.CENTER);
                actualizarPaginado();
                revalidate();
                repaint();
            }
        });
         ventaControlador.resumenVenta(filtro, filtroDesde, filtroHasta, new DatosListener<HashMap<String, Object>>() {
            @Override
            public void onSuccess(HashMap<String, Object> resumen) {
                float efectivo = ((Number) resumen.get("total_efectivo")).floatValue();
                float transferencias = ((Number) resumen.get("total_transferencia")).floatValue();
                float total = ((Number) resumen.get("total_ventas")).floatValue();
                int cantEfec = ((Number) resumen.get("cant_efectivo")).intValue();
                int cantTransfer = ((Number) resumen.get("cant_transferencia")).intValue();
                actualizarResumenPanel(efectivo, transferencias, total, cantEfec, cantTransfer);
                revalidate();
                repaint();
            }

            @Override
            public void onError(String mensajeError) {
                Dialogos.mostrarError(mensajeError);
                revalidate();
                repaint();
            }

            @Override
            public void onSuccess(HashMap<String, Object> datos, Paginado p) {
            } 
        });
    }

    private void initVista() {

        if (txtBuscar == null) {
            txtBuscar = new JTextField();

        }

        if (btnEliminar == null) {
            btnEliminar = new JButton("Eliminar");
        }

        setVisibleByPermisos(btnEliminar, "VENTA_BAJA");
        
        if (btnEditar == null) {
            btnEditar = new JButton("Editar");
        }
        setVisibleByPermisos(btnEditar, "VENTA_MODIFICAR");

        buscarPanel = new JPanel(new MigLayout("insets 0, fillx", "[grow]"));
        buscarPanel.add(txtBuscar, "growx");

        if (fechaDesde == null) {
            fechaDesde = new com.toedter.calendar.JDateChooser();
            fechaDesde.setPreferredSize(new Dimension(111, 30));
        }
        if (fechaHasta == null) {
            fechaHasta = new com.toedter.calendar.JDateChooser();
            fechaHasta.setPreferredSize(new Dimension(111, 30));
        }
        if (comboEstado == null) {
            comboEstado = new JComboBox(new String[]{"Todos", "Efectivo", "Transferencia"});
        }

        // ---- filtrosPanel (fechas y estado) ----
        JPanel filtrosPanel = new JPanel(new MigLayout("insets 0", "[]20[]20[]"));
        filtrosPanel.add(new JLabel("Desde:"));
        filtrosPanel.add(fechaDesde);
        filtrosPanel.add(new JLabel("Hasta:"));
        filtrosPanel.add(fechaHasta);
        filtrosPanel.add(new JLabel("Estado:"));
        filtrosPanel.add(comboEstado);

        botonesPanel = new JPanel(new MigLayout("insets 0", "[]10[]10[]"));
        //botonesPanel.add(btnEliminar);
        //botonesPanel.add(btnEditar);

        topPanel = new JPanel(new MigLayout("insets 5, fillx", "[grow][grow][right]"));
        topPanel.add(buscarPanel, "growx");
        topPanel.add(filtrosPanel, "growx");
        topPanel.add(botonesPanel, "right");

        JPanel resumenPanel = new JPanel(new MigLayout("insets 10, fillx", "[grow][grow][grow][grow][grow]"));
        resumenPanel.setBorder(BorderFactory.createTitledBorder("Resumen"));
        lblEfectivo = new JLabel("Efectivo: $0");
        lblTransferencias = new JLabel("Transferencia: $0");
        lblTotal = new JLabel("Total: $0");
        lblCantEfec = new JLabel("Ventas en Efectivo : 0");
        lblCantTransfer = new JLabel("Ventas por Transferencia: 0");
        lblCantTotal = new JLabel("Ventas totales: 0");

        resumenPanel.add(lblEfectivo);
        resumenPanel.add(lblTransferencias);
        resumenPanel.add(lblTotal);
        resumenPanel.add(lblCantEfec);
        resumenPanel.add(lblCantTransfer);
        resumenPanel.add(lblCantTotal);

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        bottomPanel = armarPaginado();
        JPanel panle = new JPanel();
        panle.setLayout(new BorderLayout());
        panle.add(resumenPanel, BorderLayout.NORTH);
        panle.add(bottomPanel, BorderLayout.CENTER);
        this.add(panle, BorderLayout.SOUTH);
    }

    private void setVisibleByPermisos(JComponent c, String permiso) {
        c.setVisible(true); // Puedes poner permisos.contains(permiso) en producción
        //c.setVisible(permisos.contains(permiso));
    }

    private void initEstilos() {
        // Estilos del Panel Principal

        this.putClientProperty(FlatClientProperties.STYLE, MisEstilos.PANEL_CENTRAL);
        // Estilos de la barra de búsqueda
        txtBuscar.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, MisEstilos.PLACEHOLDER_BUSQUEDA);
        MisEstilos.aplicarEstilo(txtBuscar, MisEstilos.BUSQUEDA);

        // Estilos de los iconos
        txtBuscar.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, new IconSVG(IconSVG.LUPA));
        txtBuscar.setPreferredSize(new Dimension(200, 60));
        txtBuscar.setMaximumSize(new Dimension(250, 60));
        txtBuscar.setMinimumSize(new Dimension(150, 60));

        btnEditar.setIcon(new IconSVG(IconSVG.EDITAR, 2));
        btnEliminar.setIcon(new IconSVG(IconSVG.ELIMINAR, 2));
        MisEstilos.aplicarEstilo(btnEditar, MisEstilos.BOTONCITO);
        MisEstilos.aplicarEstilo(btnEliminar, MisEstilos.BOTONCITO);
    }

    private void addAcciones() {
        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                filtrar();
            }

            public void removeUpdate(DocumentEvent e) {
                filtrar();
            }

            public void changedUpdate(DocumentEvent e) {
                filtrar();
            }

            private void filtrar() {
                pagina = 1;
                filtro = txtBuscar.getText();
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

        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (ventaSeleccionada != null) {
                    //logger.debug("fecha de pago == null ?" + ventaSeleccionada.getFecha_pago());
//                    if(ventaSeleccionada.getFecha_pago()==null){
//                        VentaController.getInstance().buscarVenta(ventaSeleccionada.getId(),
//                        new DatosListener<HashMap<String, Object>>() {
//                            @Override
//                            public void onSuccess(HashMap<String, Object> resultado) {
//                                Cliente c = (Cliente)resultado.get("cliente");
//                                ArrayList<ItemVenta> items = (ArrayList<ItemVenta>)resultado.get("items");
//                                VentaModal modal = new VentaModal(MiFrame.getInstance(),ventaSeleccionada, items,c);
//                                modal.setVisible(true); // bloquea el thread hasta que es cerrado
//                                filtro = "";
//                                pagina = 1;
//                                reload();
//                            }
//
//                            @Override
//                            public void onError(String mensajeError) {
//                                System.err.println("Error al imprimir: " + mensajeError);
//                            }
//
//                            @Override
//                            public void onSuccess(HashMap<String, Object> datos, Paginado p) {
//                                // throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
//                            }
//                        });
//                    }else{
//                        Dialogos.mostrarInfo("Solo se pueden editar presupuestos en estado 'PENDIENTE' ");
//                    }
                }
            }
        });

        btnEliminar.addActionListener(e -> {
            if (ventaSeleccionada != null) {
                ventaControlador.eliminarVenta(ventaSeleccionada.getId(), new DatosListener<String>() {
                    @Override
                    public void onSuccess(String resultado) {
                        System.out.println("Eliminado OK: " + resultado);
                    }

                    @Override
                    public void onError(String mensajeError) {
                        System.err.println("Error al eliminar: " + mensajeError);
                    }

                    @Override
                    public void onSuccess(String datos, Paginado p) {
                        // throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                    }
                });
            }
        });

        //Panel Paginado
        comboTamanioPagina.addActionListener(e -> {
            tamPagina = (Integer) comboTamanioPagina.getSelectedItem();
            pagina = 1;
            reload();
        });

        btnPrimero.addActionListener(e -> {
            pagina = 1;
            reload();
        });

        btnAnterior.addActionListener(e -> {
            if (pagina > 1) {
                pagina--;
                reload();
            }
        });

        btnSiguiente.addActionListener(e -> {
            if (pagina < totalPaginas) {
                pagina++;
                reload();
            }
        });

        btnUltimo.addActionListener(e -> {
            pagina = totalPaginas;
            reload();
        });
        // Listener para fechas
        PropertyChangeListener fechaListener = evt -> {
            if ("date".equals(evt.getPropertyName())) {
                filtrarConFechasYEstado();
            }
        };
        fechaDesde.getDateEditor().addPropertyChangeListener(fechaListener);
        fechaHasta.getDateEditor().addPropertyChangeListener(fechaListener);

        // ✅ Listener para estado (combo box)
        comboEstado.addActionListener(e -> {
            filtrarConFechasYEstado();
        });
    }
    // Métodos auxiliares para actualizar resumen

    public void actualizarResumenPanel(float efec, float transfer, float total, int cEfec, int cTransfer) {
        int cTotal = cEfec + cTransfer;
        lblEfectivo.setText("Efectivo: $" + efec);
        lblTransferencias.setText("Transferencia: $" + transfer);
        lblTotal.setText("Total: $" + total);
        lblCantEfec.setText("Ventas en Efectivo : " + cEfec); // solo movimientos, sin gastos fijos, de neto no tiene nada
        lblCantTransfer.setText("Ventas por Transferencia: " + cTransfer);
        lblCantTotal.setText("Ventas totales: " + cTotal);
    }

    private JPanel armarPaginado() {

        JPanel paginadoPanel = new JPanel(new MigLayout("insets 5, align center", "[]10[]10[]10[]10[]10[]20[]"));
        txtPaginaActual.setHorizontalAlignment(JTextField.CENTER);
        txtPaginaActual.setEditable(false);
        comboTamanioPagina.setSelectedItem(tamPagina);
        Dimension btnDim = new Dimension(45, 30);
        btnPrimero.setPreferredSize(btnDim);
        btnAnterior.setPreferredSize(btnDim);
        txtPaginaActual.setPreferredSize(new Dimension(50, 30));
        btnSiguiente.setPreferredSize(btnDim);
        btnUltimo.setPreferredSize(btnDim);
        comboTamanioPagina.setPreferredSize(new Dimension(70, 30));
        txtPaginaActual.setText("" + pagina);
        labelPaginas.setText("Página " + pagina + " de " + totalPaginas);

        paginadoPanel.add(btnPrimero);
        paginadoPanel.add(btnAnterior);
        paginadoPanel.add(txtPaginaActual);
        paginadoPanel.add(btnSiguiente);
        paginadoPanel.add(btnUltimo);
        paginadoPanel.add(comboTamanioPagina);
        paginadoPanel.add(labelPaginas);

        return paginadoPanel;
    }

    private void actualizarPaginado() {
        txtPaginaActual.setText("" + pagina);
        labelPaginas.setText("Página " + pagina + " de " + totalPaginas);
    }

    private void initTabla() {
        String[] headers = {"Cód:", "Cliente", "Teléfono", "Fecha Emisión", "Forma de Pago", "Subtotal", "Descuento", "Total", "Observaciones","Acciones: "};
        List<Object[]> rows = generarData();
        BiConsumer<PanelAccion, Integer> configurador = (panel, row) -> {
            Venta ven = (Venta) ventasDetallado.get(row).get("venta");
            panel.agregarBoton("Imprimir", IconSVG.IMPRIMIR, e -> {
                imprimirVenta(ven);
            });
        };

        TablaBuilder builder = new TablaBuilder(headers, rows, headers.length - 1, configurador);
        scroll = builder.crearTabla();
        tabla = builder.getTable();
        this.repaint();
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = tabla.rowAtPoint(evt.getPoint());
                if (row >= 0) {
                    ventaSeleccionada = (Venta) ventasDetallado.get(row).get("venta");
                    logger.trace("Venta seleccionado: " + ventaSeleccionada.getId());
                }
            }
        });
    }

    private List<Object[]> generarData() {
        List<Object[]> rows = new ArrayList<>();
        for (HashMap<String, Object> map : ventasDetallado) {
            Venta v = (Venta) map.get("venta");
            Cliente c = (Cliente) map.get("cliente");
            rows.add(new Object[]{
                v.getId(),
                c.getNombre(),
                c.getTelefono(),
                v.getCreado(),
                v.getForma_pago(),
                v.getSubtotal(),
                v.getPorcentaje_descuento(),
                v.getTotal(),
                v.getObservaciones()
            });
        }
        return rows;
    }

    private void filtrarConFechasYEstado() {
        this.pagina = 1;
        this.filtro = txtBuscar.getText();
        Date desde = fechaDesde.getDate();
        Date hasta = fechaHasta.getDate();
        if (desde != null && hasta != null && !desde.after(hasta)) {
            LocalDate ldDesde = FechaUtils.dateToLocalDate(desde);
            LocalDate ldHasta = FechaUtils.dateToLocalDate(hasta);
            this.filtroDesde = FechaUtils.ldToString(ldDesde);
            this.filtroHasta = FechaUtils.ldToString(ldHasta);
        } else {
            this.filtroDesde = "";
            this.filtroHasta = "";
        }

        if (!comboEstado.getSelectedItem().toString().equals("Todos")) {
            this.filtroEstado = comboEstado.getSelectedItem().toString();
        } else {
            this.filtroEstado = "";
        }

        if (timer != null) {
            timer.cancel();
        }

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                reload();
            }
        }, 300);
    }

    private void imprimirVenta(Venta venta) {
        ventaControlador.buscarVenta(
                venta.getId(),
                new DatosListener<HashMap<String, Object>>() {

            @Override
            public void onSuccess(HashMap<String, Object> resultado) {
                Venta ven = (Venta) resultado.get("venta");
//                Orden o = (Orden)resultado.get("orden");
                Cliente c = (Cliente) resultado.get("cliente");
                ArrayList<ItemVenta> itemsVen = (ArrayList<ItemVenta>) resultado.get("items");

                System.out.println("Imprimir OK: " + resultado);
                // 1. Generar HTML de filas de la tabla
                String tablaHTML = generarFilasTabla(itemsVen);

                // 2. Cargar plantilla base
                String template = cargarTemplate();

                // 3. Reemplazar los placeholders
                String htmlFinal = template
                        .replace("{{fecha}}", java.time.LocalDate.now().toString())
                        .replace("{{numero}}", ven.getId() + "")
                        .replace("{{cliente_nombre}}", c.getNombre()!= null ? c.getLocalidad() : "-")
                        .replace("{{cliente_telefono}}", c.getTelefono()!= null ? c.getLocalidad() : "-")
                        .replace("{{cliente_localidad}}", c.getLocalidad() != null ? c.getLocalidad() : "-")
                        .replace("{{cliente_domicilio}}", c.getDireccion() != null ? c.getDireccion() : "-")
                        .replace("{{cliente_cp}}", c.getCodigo_postal() != null ? c.getCodigo_postal() : "-")
                        .replace("{{cliente_provincia}}", c.getLocalidad() != null ? c.getLocalidad() : "-")
                        .replace("{{observaciones}}", ven.getObservaciones() != null ? ven.getObservaciones() : "-")
                        .replace("{{total}}", ven.getTotal() + "")
                        .replace("{{tabla_items}}", tablaHTML);
                // 4. Mostrar Vista Previa
                VistaPreviaImpresion vista = new VistaPreviaImpresion(
                        MiFrame.getInstance(),
                        htmlFinal,
                        "venta_" + ven.getId()+"_"+ven.getCreado().split(" ")[0]
                );
                vista.setVisible(true);
            }

            @Override
            public void onError(String mensajeError) {
                System.err.println("Error al imprimir: " + mensajeError);
            }

            @Override
            public void onSuccess(HashMap<String, Object> datos, Paginado p) {
                // throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }
        });
    }

    public static String generarFilasTabla(List<ItemVenta> items) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            ItemVenta iv = items.get(i);
            sb.append("<tr>");
            sb.append("<td>")
                    .append(iv.getNombre())
                    .append("</td>")
                    .append("<td>")
                    .append(iv.getProducto_precio())
                    .append("</td>")
                    .append("<td>")
                    .append(iv.getPorcentaje_descuento())
                    .append("</td>")
                    .append("<td>")
                    .append(iv.getCantidad())
                    .append("</td>")
                    .append("<td>")
                    .append(iv.getSubtotal())
                    .append("</td>");

            sb.append("</tr>");
        }
        return sb.toString();
    }

    private String cargarTemplate() {
        try {
            InputStream is = VentaPanel.class.getClassLoader().getResourceAsStream("templates/template_venta.html");
            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            return br.lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            logger.error("VentaModal: ", e.getMessage(), e);
        }
        return "";
    }

}
