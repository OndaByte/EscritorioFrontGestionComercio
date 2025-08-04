
package com.OndaByte.AntartidaFront.vistas.presupuestos;

import com.OndaByte.AntartidaFront.vistas.util.VistaPreviaImpresion;
import com.OndaByte.AntartidaFront.controladores.PresupuestoController;
import com.OndaByte.AntartidaFront.modelos.Cliente;
import com.OndaByte.AntartidaFront.modelos.ItemPresupuesto;
import com.OndaByte.AntartidaFront.modelos.Presupuesto;
import com.OndaByte.AntartidaFront.estilos.MisEstilos;
import com.OndaByte.AntartidaFront.sesion.SesionController;
import com.OndaByte.AntartidaFront.vistas.DatosListener;
import com.OndaByte.AntartidaFront.vistas.MiFrame;
import com.OndaByte.AntartidaFront.vistas.util.Dialogos;
import com.OndaByte.AntartidaFront.vistas.util.FechaUtils;
import com.OndaByte.AntartidaFront.vistas.util.IconSVG;
import com.OndaByte.AntartidaFront.vistas.util.Paginado;
import com.OndaByte.AntartidaFront.vistas.util.tabla.PanelAccion;
import com.OndaByte.AntartidaFront.vistas.util.tabla.TablaBuilder;
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
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.miginfocom.swing.MigLayout;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PresupuestoPanel extends JPanel {

    private JTable tabla;
    private JScrollPane scroll;
    private JTextField txtBuscar;
    private JDateChooser fechaDesde, fechaHasta;
    private JButton btnEditar, btnEliminar;
    private JPanel topPanel, bottomPanel, buscarPanel, botonesPanel;
    private JComboBox<Integer> comboEstado;

    private JButton btnPrimero = new JButton("|<");
    private JButton btnAnterior = new JButton("<");
    private JTextField txtPaginaActual = new JTextField("");
    private JButton btnSiguiente = new JButton(">");
    private JButton btnUltimo = new JButton(">|");
    private JComboBox<Integer> comboTamanioPagina = new JComboBox<>(new Integer[]{10, 20, 50, 100});
    private JLabel labelPaginas = new JLabel("Página 1 de 1");

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

    private PresupuestoController presupuestoControlador;
    private HashSet<String> permisos;
    private ArrayList<HashMap<String, Object>> presupuestosDetallado;
    
    private Presupuesto presupuestoSeleccionado;
    private Cliente c = null;

    private PresupuestoPanel esta;

    private static Logger logger = LogManager.getLogger(PresupuestoPanel.class.getName());

    static {
        if (logger.isTraceEnabled()) {
            logger.trace("Init logger en PresupuestoPanel");
        }
    }

    public PresupuestoPanel() {
        this.esta = this;
        this.permisos = (HashSet<String>) SesionController.getInstance().getSesionPermisos();
        presupuestoControlador = PresupuestoController.getInstance();
        totalElementos = 0;
        filtro = "";
        filtroEstado = "";
        filtroDesde = "";
        filtroHasta = "";
        pagina = 1;
        tamPagina = 20;

        presupuestoControlador.filtrar(filtro, filtroDesde, filtroHasta, filtroEstado, "" + pagina, "" + tamPagina, new DatosListener<List<HashMap<String, Object>>>() {
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
                presupuestosDetallado = new ArrayList<>(datos);
                esta.pagina = p.getPagina();
                esta.tamPagina = p.getTamPagina();
                esta.totalElementos = p.getTotalElementos();
                esta.totalPaginas = p.getTotalPaginas();
                initTabla(); //tabla
                setVisibleByPermisos(tabla, "PRESUPUESTO_LISTAR");

                initVista(); // Inicializa la vista segun los permisos disponibles menos la tabla
                initEstilos(); //estilos menos los de la tabla
                addAcciones(); // acciones de buscador, botoones abm, etc. menos la tabla
            }
        });
    }

    /**
     * Renderiza de nuevo los elementos de la tabla y paginado con el filtro
     * actual.
     */
    private void reload() {
        presupuestoControlador.filtrar(filtro, filtroDesde, filtroHasta, filtroEstado, "" + pagina, "" + tamPagina, new DatosListener<List<HashMap<String, Object>>>() {
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
                presupuestosDetallado = new ArrayList<>(datos);
                presupuestoSeleccionado = null;
                esta.pagina = p.getPagina();
                esta.tamPagina = p.getTamPagina();
                esta.totalElementos = p.getTotalElementos();
                esta.totalPaginas = p.getTotalPaginas();
                remove(scroll);
                initTabla(); //tabla
                setVisibleByPermisos(tabla, "PRESUPUESTO_LISTAR");
                add(scroll, BorderLayout.CENTER);
                actualizarPaginado();
                revalidate();
                repaint();
            }
        });
    }

    private void initVista() {

        if (txtBuscar == null) {
            txtBuscar = new JTextField();

        }
        if (btnEditar == null) {
            btnEditar = new JButton("Editar");
        }
        //setVisibleByPermisos(btnEditar, "PRESUPUESTO_BAJA");
        if (btnEliminar == null) {
            btnEliminar = new JButton("Eliminar");
        }
        //setVisibleByPermisos(btnEliminar, "PRESUPUESTO_BAJA");
        btnEliminar.setVisible(false);

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
            comboEstado = new JComboBox(new String[]{"Todos", "Pendiente", "Aprobado", "Cancelado"});
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
        botonesPanel.add(btnEditar);
        botonesPanel.add(btnEliminar);

        topPanel = new JPanel(new MigLayout("insets 5, fillx", "[grow][grow][right]"));
        topPanel.add(buscarPanel, "growx");
        topPanel.add(filtrosPanel, "growx");
        topPanel.add(botonesPanel, "right");

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        bottomPanel = armarPaginado();
        this.add(bottomPanel, BorderLayout.SOUTH);
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
        // txtBuscar.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, new FlatSVGIcon(getClass().getClassLoader().getResource("icon/buscar.svg")));
        //txtBuscar.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, new FlatSVGIcon("sample/icon/search.svg"));
        MisEstilos.aplicarEstilo(txtBuscar, MisEstilos.BUSQUEDA);

        // Estilos de los iconos
        txtBuscar.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, new IconSVG(IconSVG.LUPA));
        txtBuscar.setPreferredSize(new Dimension(200, 60));
        txtBuscar.setMaximumSize(new Dimension(250, 60));
        txtBuscar.setMinimumSize(new Dimension(150, 60));

        btnEditar.setIcon(new IconSVG(IconSVG.EDITAR,2));
        btnEliminar.setIcon(new IconSVG(IconSVG.ELIMINAR,2));
        MisEstilos.aplicarEstilo(btnEditar, MisEstilos.BOTONCITO);
        MisEstilos.aplicarEstilo(btnEliminar, MisEstilos.BOTONCITO);
    }


    private void addAcciones() {
        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filtrar(); }
            public void removeUpdate(DocumentEvent e) { filtrar(); }
            public void changedUpdate(DocumentEvent e) { filtrar(); }

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
                if(presupuestoSeleccionado != null ){
                    if(presupuestoSeleccionado.getEstado_presupuesto().equals("PENDIENTE")){
                        PresupuestoController.getInstance().buscarPresupuesto(
                        presupuestoSeleccionado.getId(),
                        new DatosListener<HashMap<String, Object>>() {
                            @Override
                            public void onSuccess(HashMap<String, Object> resultado) {
                                Cliente c = (Cliente)resultado.get("cliente");
                                ArrayList<ItemPresupuesto> items = (ArrayList<ItemPresupuesto>)resultado.get("items");
                                PresupuestoModal modal = new PresupuestoModal(MiFrame.getInstance(), c ,presupuestoSeleccionado,items);
                                modal.setVisible(true); // bloquea el thread hasta que es cerrado
                                filtro = "";
                                pagina = 1;
                                reload();
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
                    }else{
                        Dialogos.mostrarInfo(" Solo se pueden editar presupuestos en estado 'PENDIENTE' ");
                    }
                    
                }
            }
        });
        btnEliminar.addActionListener(e -> {
            if (presupuestoSeleccionado != null) {
                presupuestoControlador.eliminarPresupuesto(presupuestoSeleccionado.getPedido_id(), new DatosListener<String>() {
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
        String[] headers = {"Cód:", "Cliente", "Teléfono","Fecha", "Descripción","Total", "Estado", "Acciones"};
        List<Object[]> rows = generarData();
        BiConsumer<PanelAccion, Integer> configurador = (panel, row) -> {
            Presupuesto pre = (Presupuesto) presupuestosDetallado.get(row).get("presupuesto");
            if(pre.getEstado_presupuesto().equals("PENDIENTE")){
                panel.agregarBoton("Aprobar", IconSVG.ACEPTAR, e -> {
                    boolean res = Dialogos.confirmar("Al aprobar presupuesto, se marcará el Pedido 'APROBADO' y Se dará de Alta una Orden de Reparación", "Aprobar presupuesto");
                    if (res) {
                        Presupuesto aux = new Presupuesto();
                        String estado_presupuesto = "APROBADO";
                        aux.setEstado_presupuesto(estado_presupuesto);
                        aux.setId(pre.getId());
                        presupuestoControlador.actualizarPresupuesto(aux,
                                new DatosListener() {
                                    @Override
                                    public void onSuccess(Object datos) {
                                        Dialogos.mostrarExito("" + datos);
                                        filtro = "";
                                        pagina = 1;
                                        reload();
                                    }
                                    @Override
                                    public void onError(String mensajeError) {
                                        Dialogos.mostrarError(mensajeError);
                                        filtro = "";
                                        pagina = 1;
                                        reload();
                                    }
                                    @Override
                                    public void onSuccess(Object datos, Paginado p) {
                                    }
                                });
                    }
                });

                panel.agregarBoton("Rechazar", IconSVG.RECHAZAR, e -> {
                    boolean res = Dialogos.confirmar("¿Esta seguro que deséa rechazar este presupuesto?", "Rechazar presupuesto");
                    if (res) {
                        Presupuesto aux = new Presupuesto();
                        String estado_presupuesto = "RECHAZADO";
                        aux.setEstado_presupuesto(estado_presupuesto);
                        aux.setId(pre.getId());
                        presupuestoControlador.actualizarPresupuesto(aux,
                                new DatosListener() {
                                    @Override
                                    public void onSuccess(Object datos) {
                                        Dialogos.mostrarExito("" + datos);
                                        filtro = "";
                                        pagina = 1;
                                        reload();
                                    }

                                    @Override
                                    public void onError(String mensajeError) {
                                        Dialogos.mostrarError(mensajeError);
                                        filtro = "";
                                        pagina = 1;
                                        reload();
                                    }

                                    @Override
                                    public void onSuccess(Object datos, Paginado p) {
                                    }
                                }
                        );
                    }
                });
            }
            panel.agregarBoton("Imprimir", IconSVG.IMPRIMIR, e -> {
                imprimirPresupuesto(pre);
            });

        };

        TablaBuilder builder = new TablaBuilder(headers, rows, headers.length - 1, configurador);
        scroll = builder.crearTabla();
        tabla = builder.getTable();
        this.repaint();
        // Estilos de la tabla
        tabla.getTableHeader().putClientProperty(FlatClientProperties.STYLE,MisEstilos.HEADER_TABLA);
        tabla.putClientProperty(FlatClientProperties.STYLE,MisEstilos.TABLA);
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {   // listenner
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = tabla.rowAtPoint(evt.getPoint());
                logger.trace(row);
                if ( row >= 0 ){
                    c = (Cliente) presupuestosDetallado.get(row).get("cliente");
                    presupuestoSeleccionado = (Presupuesto) presupuestosDetallado.get(row).get("presupuesto");
                    System.out.println(presupuestoSeleccionado.getId());
                }
            }
        });
    }

    private List<Object[]> generarData() {
        List<Object[]> rows = new ArrayList<>();
        for (HashMap<String, Object> map : presupuestosDetallado) {
            Cliente c = (Cliente) map.get("cliente");
            Presupuesto p = (Presupuesto) map.get("presupuesto");
            rows.add(new Object[]{
                p.getId(),
                c.getNombre(),
                c.getTelefono(),
                p.getCreado(),
                p.getDescripcion(),
                p.getTotal(),
                p.getEstado_presupuesto()
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
        }else{
            this.filtroDesde = "";
            this.filtroHasta = "";
        }
        
        if(!comboEstado.getSelectedItem().toString().equals("Todos")){
            this.filtroEstado = comboEstado.getSelectedItem().toString();
        }else{
            this.filtroEstado = "";
        }
        
        if (timer != null) timer.cancel();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                reload();
            }
        }, 300);
    }
    
    private void imprimirPresupuesto(Presupuesto presupuesto) {
        presupuestoControlador.buscarPresupuesto(
                presupuesto.getId(), 
                new DatosListener<HashMap<String, Object>>() {
                    
            @Override
            public void onSuccess(HashMap<String, Object> resultado) {
                Presupuesto pre = (Presupuesto)resultado.get("presupuesto");
//                Pedido p = (Pedido)resultado.get("pedido");
                Cliente c = (Cliente)resultado.get("cliente");
                ArrayList<ItemPresupuesto> itemsPre = (ArrayList<ItemPresupuesto>)resultado.get("items");
                
                System.out.println("Imprimir OK: " + resultado);
                // 1. Generar HTML de filas de la tabla
                String tablaHTML = generarFilasTabla(itemsPre);

                // 2. Cargar plantilla base
                String template = cargarTemplate();

                // 3. Reemplazar los placeholders
                String htmlFinal = template
                  .replace("{{fecha}}", java.time.LocalDate.now().toString())
                  .replace("{{numero}}", pre.getId()+"")
                  .replace("{{cliente_nombre}}", c.getNombre())
                  .replace("{{cliente_telefono}}", c.getTelefono())
                  .replace("{{cliente_localidad}}", c.getLocalidad() != null ? c.getLocalidad() : "No informado")
                  .replace("{{cliente_domicilio}}", c.getDireccion() != null ? c.getDireccion() : "No informado")
                  .replace("{{cliente_cp}}", c.getCodigo_postal() !=null ? c.getCodigo_postal() : "No informado")
                  .replace("{{cliente_provincia}}", c.getProvincia() != null ? c.getProvincia() : "No informado")
                  .replace("{{total}}", pre.getTotal()+"")
                  .replace("{{descripcion}}",pre.getDescripcion())
                  .replace("{{tabla_items}}", tablaHTML);

                // 4. Mostrar Vista Previa
                VistaPreviaImpresion vista = new VistaPreviaImpresion(
                        MiFrame.getInstance(),
                        htmlFinal,
                        "presupuesto_" + c.getNombre().replace(" ", "_")
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

    public static String generarFilasTabla(List<ItemPresupuesto> items) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            ItemPresupuesto ip = items.get(i);
            sb.append("<tr>");
            
            sb.append("<td style=\"border:1px solid #000;\">")
                .append(ip.getDescripcion())
                .append("</td>")
                .append("<td style=\"border:1px solid #000; text-align:right;\">")
                .append(ip.getCantidad())
                .append("</td>")
                .append("<td style=\"border:1px solid #000; text-align:right;\">")
                .append(ip.getPrecio())
                .append("</td>");
            sb.append("</tr>");
        }
        return sb.toString();
    }

    private String cargarTemplate() {
        try {
            InputStream is = PresupuestoModal.class.getClassLoader().getResourceAsStream("templates/template_presupuesto.html");
            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            return br.lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            logger.error("PresupuestoModal: ", e.getMessage(), e);
        }
        return "";
    }
}
