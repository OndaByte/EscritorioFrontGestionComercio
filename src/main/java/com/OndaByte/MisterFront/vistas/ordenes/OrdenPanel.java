
package com.OndaByte.MisterFront.vistas.ordenes;

import com.OndaByte.MisterFront.controladores.OrdenController;
import com.OndaByte.MisterFront.controladores.PresupuestoController;
import com.OndaByte.MisterFront.estilos.MisEstilos;
import com.OndaByte.MisterFront.modelos.Orden;
import com.OndaByte.MisterFront.modelos.Presupuesto;
import com.OndaByte.MisterFront.modelos.Cliente;
import com.OndaByte.MisterFront.modelos.ItemRemito;
import com.OndaByte.MisterFront.modelos.ItemPresupuesto;
import com.OndaByte.MisterFront.modelos.Turno;
import com.OndaByte.MisterFront.sesion.SesionController;
import com.OndaByte.MisterFront.vistas.DatosListener;
import com.OndaByte.MisterFront.vistas.MiFrame;
import com.OndaByte.MisterFront.vistas.remitos.RemitoModal;
import com.OndaByte.MisterFront.vistas.turnos.TurnoModal;
import com.OndaByte.MisterFront.vistas.util.Dialogos;
import com.OndaByte.MisterFront.vistas.util.Paginado;
import com.OndaByte.MisterFront.vistas.util.IconSVG;
import com.OndaByte.MisterFront.vistas.util.FechaUtils;
import com.OndaByte.MisterFront.vistas.util.tabla.PanelAccion;
import com.OndaByte.MisterFront.vistas.util.tabla.TablaBuilder;
import com.formdev.flatlaf.FlatClientProperties;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.toedter.calendar.JDateChooser;

import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.BiConsumer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.miginfocom.swing.MigLayout;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OrdenPanel extends JPanel {

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

    private OrdenController ordenControlador;
    private HashSet<String> permisos;
    private ArrayList<HashMap<String, Object>> ordenesDetalladas; // DTO: Ordenes con las otras entidades asociadas.
    private Orden ordenSeleccionada;

    private OrdenPanel esta; //sinonimo de this

    private static Logger logger = LogManager.getLogger(OrdenPanel.class.getName());

    static {
        if (logger.isDebugEnabled()) {
            logger.debug("Init logger en OrdenPanel");
        }
    }

    public OrdenPanel() {
        this.esta = this;
        this.permisos = (HashSet<String>) SesionController.getInstance().getSesionPermisos();
        ordenControlador = OrdenController.getInstance();
        totalElementos = 0;
        filtro = "";
        filtroEstado = "";
        filtroDesde = "";
        filtroHasta = "";
        pagina = 1;
        tamPagina = 20;
        ordenControlador.filtrar(filtro, filtroDesde, filtroHasta, filtroEstado, "" + pagina, "" + tamPagina, new DatosListener<List<HashMap<String, Object>>>() {
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
                ordenesDetalladas = new ArrayList<>(datos);
                esta.pagina = p.getPagina();
                esta.tamPagina = p.getTamPagina();
                esta.totalElementos = p.getTotalElementos();
                esta.totalPaginas = p.getTotalPaginas();
                initTabla(); //tabla
                setVisibleByPermisos(tabla, "ORDEN_LISTAR");

                initVista(); // Inicializa la vista segun los permisos disponibles menos la tabla
                initEstilos(); //estilos menos los de la tabla, llamar despues de initVista
                addAcciones(); // acciones de buscador, botoones abm, etc. menos la tabla
            }
        });
    }

    /**
     * Renderiza de nuevo los elementos de la tabla y paginado con el filtro actual.
     */
    private void reload() {
        ordenControlador.filtrar(filtro, filtroDesde, filtroHasta, filtroEstado, "" + pagina, "" + tamPagina, new DatosListener<List<HashMap<String, Object>>>() {
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
                ordenesDetalladas = new ArrayList<>(datos);
                esta.pagina = p.getPagina();
                esta.tamPagina = p.getTamPagina();
                esta.totalElementos = p.getTotalElementos();
                esta.totalPaginas = p.getTotalPaginas();
                remove(scroll);
                initTabla(); //tabla
                setVisibleByPermisos(tabla, "ORDEN_LISTAR");
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
        setVisibleByPermisos(txtBuscar, "ORDEN_LISTAR");
        if (btnEditar == null) {
            btnEditar = new JButton("Editar");
        }
        setVisibleByPermisos(btnEditar, "");//OCULTADO
        if (btnEliminar == null) {
            btnEliminar = new JButton("Eliminar");
        }
        setVisibleByPermisos(btnEliminar, "");//OCULTADO

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
            comboEstado = new JComboBox(new String[]{"Todos", "Pendiente", "Asignada", "Listas", "Cancelado", "Entregada"});
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
        c.setVisible(permisos.contains(permiso));
    }

    private void initEstilos() {
        // Estilos del Panel Principal

        this.putClientProperty(FlatClientProperties.STYLE, MisEstilos.PANEL_CENTRAL);
        // Estilos de la barra de búsqueda
        txtBuscar.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, MisEstilos.PLACEHOLDER_BUSQUEDA);
        //  txtBuscar.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, new FlatSVGIcon(getClass().getClassLoader().getResource("icon/buscar.svg")));
        //txtBuscar.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, new FlatSVGIcon("sample/icon/search.svg"));
        MisEstilos.aplicarEstilo(txtBuscar, MisEstilos.BUSQUEDA);

        // Estilos de los iconos
        MisEstilos.aplicarEstilo(txtBuscar, MisEstilos.BUSQUEDA);
        txtBuscar.setPreferredSize(new Dimension(200, 60));
        txtBuscar.setMaximumSize(new Dimension(250, 60));
        txtBuscar.setMinimumSize(new Dimension(150, 60));

        txtBuscar.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, new IconSVG(IconSVG.LUPA));
        btnEditar.setIcon(new IconSVG(IconSVG.EDITAR,2));
        btnEliminar.setIcon(new IconSVG(IconSVG.ELIMINAR,2));
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
                if (ordenSeleccionada != null) {
                    OrdenModal modal = new OrdenModal(MiFrame.getInstance(), ordenSeleccionada);
                    modal.setVisible(true); // bloquea el thread hasta que es cerrado
                    filtro = "";
                    pagina = 1;
                    reload();
                }
            }
        });
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (ordenSeleccionada != null) {
                    if (Dialogos.confirmar("¿Está seguro que desea eliminar esta orden?", "Eliminar Orden")) {
                        ordenControlador.eliminarOrden(ordenSeleccionada.getId(),
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
                }
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


        if (timer != null) timer.cancel();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                reload();
            }
        }, 300);
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
        String[] headers = new String[]{"Cód:","Fecha Solicitud", "Nombre", "Telefono", "Descripción","Inicio Turno", "Fin Turno", "Tipo","Estado", "Acciones"};
        List<Object[]> rows = generarData();
        BiConsumer<PanelAccion, Integer> configurador = (panel, row) -> {
            Orden o = (Orden) ordenesDetalladas.get(row).get("orden");
            if (o.getEstado_orden().equals("PENDIENTE")) {
                panel.agregarBoton("Asignar_Turno", "icon/calendar2.svg", e -> {
                    TurnoModal modal = new TurnoModal(MiFrame.getInstance(), o);
                    modal.setVisible(true); // bloquea el thread hasta que es cerrado
                    filtro = "";
                    pagina = 1;
                    reload();
                });

            }
            if (o.getEstado_orden().equals("ASIGNADA") || o.getEstado_orden().equals("PROCESANDO")) {
                panel.agregarBoton("Completar", IconSVG.ACEPTAR, e -> {
                    boolean res = Dialogos.confirmar("Al aprobar la orden, se marcará como completada ", "Completar Orden");
                    if (res) {
                        Orden aux = new Orden();
                        String estado_orden = "COMPLETADA";
                        aux.setEstado_orden(estado_orden);
                        aux.setId(o.getId());
                        ordenControlador.actualizarOrden(aux,
                                new DatosListener() {
                                    @Override
                                    public void onSuccess(Object datos) {
                                        Dialogos.mostrarExito("" + datos);
                                        boolean res = Dialogos.confirmar("¿Desea generar el Remito ahora?", "Generar Remito");
                                        if (res) {
                                            esta.mostrarCrearRemito(panel, row);
                                        }
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
            if(o.getEstado_orden().equals("COMPLETADA")){
                panel.agregarBoton("remitear", "icon/file.svg", e -> {
                    esta.mostrarCrearRemito(panel, row);
                    filtro = "";
                    pagina = 1;
                    reload();
                });

            }
        };

        TablaBuilder builder = new TablaBuilder(headers, rows, headers.length - 1, configurador);
        scroll = builder.crearTabla();
        tabla = builder.getTable();
        // Estilos de la tabla
        tabla.getTableHeader().putClientProperty(FlatClientProperties.STYLE, MisEstilos.HEADER_TABLA);
        tabla.putClientProperty(FlatClientProperties.STYLE, MisEstilos.TABLA);
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {   // listenner
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = tabla.rowAtPoint(evt.getPoint());
                logger.trace(row);
                if (row >= 0) {
                    ordenSeleccionada = (Orden) ordenesDetalladas.get(row).get("orden");
                    System.out.println(ordenSeleccionada.getId());
                }
            }
        });
    }

    private void mostrarCrearRemito(PanelAccion panel, Integer row) {
        Orden o = (Orden) ordenesDetalladas.get(row).get("orden");
        Presupuesto pre = (Presupuesto) ordenesDetalladas.get(row).get("presupuesto");
        PresupuestoController.getInstance().buscarPresupuesto(
                pre.getId(),
                new DatosListener<HashMap<String, Object>>() {
                    @Override
                    public void onSuccess(HashMap<String, Object> resultado) {
                        Cliente c = (Cliente)resultado.get("cliente");
                        ArrayList<ItemPresupuesto> itemsPre = (ArrayList<ItemPresupuesto>)resultado.get("items");
                        ArrayList<ItemRemito> itemsR = new ArrayList<>();
                        for (ItemPresupuesto ip : itemsPre) {
                            ItemRemito ir = new ItemRemito();
                            ir.setDescripcion(ip.getDescripcion());
                            ir.setCantidad(ip.getCantidad());
                            ir.setPrecio(ip.getPrecio());
                            itemsR.add(ir);
                        }
                        RemitoModal modal = new RemitoModal(MiFrame.getInstance(), itemsR, o.getId(), c );
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
        }

    private List<Object[]> generarData() {
        List<Object[]> rows = new ArrayList<>();
        for (HashMap<String, Object> map : ordenesDetalladas) {
            Orden o = (Orden) map.get("orden");
            Cliente c = (Cliente) map.get("cliente");
            Turno t = (Turno) map.get("turno");
            rows.add(new Object[]{o.getId(),o.getCreado(), c.getNombre(), c.getTelefono(), o.getDescripcion(), t.getFechaInicio(), t.getFechaFinE(),o.getTipo(), o.getEstado_orden()});
        }
        return rows;
    }
}
