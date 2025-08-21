/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.OndaByte.MisterFront.vistas.caja;

import com.OndaByte.MisterFront.controladores.CajaController;
import com.OndaByte.MisterFront.estilos.MisEstilos;
import com.OndaByte.MisterFront.modelos.Caja;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.BiConsumer;
import javax.swing.BorderFactory;
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
 
/**
 *
 * @author luciano
 */
public class CajaPanel extends JPanel {

   
    private JTable tabla;
    private JScrollPane scroll;
    private JTextField txtBuscar;
    private JDateChooser fechaDesde,fechaHasta;
    private JButton btnNuevo, btnEditar, btnEliminar;
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
    private JLabel lblIngresos;
    private JLabel lblEgresos;
    private JLabel lblGastosFijos;
    private JLabel lblBalance;
    private JLabel lblBalanceSinGastos;
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

    private CajaController cajaControlador;
    private HashSet<String> permisos;
    private ArrayList<HashMap<String, Object>> cajasDetallado;
    private Caja cajaSeleccionada;

    private CajaPanel esta;

    private static Logger logger = LogManager.getLogger(CajaPanel.class.getName());

    static{
        if(logger.isDebugEnabled()){
            logger.debug("Init logger en PedidosPanel");
        }
    } 
 
    public CajaPanel() {
        this.esta = this;
        this.permisos = (HashSet<String>) SesionController.getInstance().getSesionPermisos();
        cajaControlador = CajaController.getInstance();
        totalElementos=0;
        filtro="";
        filtroEstado="";
        filtroDesde="";
        filtroHasta="";
        pagina = 1;
        tamPagina = 20;
        cajaControlador.filtrar(filtro,filtroDesde,filtroHasta,filtroEstado, "" + pagina, "" + tamPagina,new DatosListener<List<HashMap<String,Object>>>(){
            @Override
            public void onSuccess(List<HashMap<String,Object>> datos) {
            }

            @Override
            public void onError(String mensajeError) {
                Dialogos.mostrarError(mensajeError);
                revalidate();
                repaint();
            }

            @Override
            public void onSuccess(List<HashMap<String,Object>> datos, Paginado p) {
                cajasDetallado = new ArrayList<>(datos);
                esta.pagina=p.getPagina();
                esta.tamPagina=p.getTamPagina();
                esta.totalElementos=p.getTotalElementos();
                esta.totalPaginas=p.getTotalPaginas();
                initTabla(); //tabla
                setVisibleByPermisos(tabla,"CAJA_LISTAR");
                
                initVista(); // Inicializa la vista segun los permisos disponibles menos la tabla
                initEstilos(); //estilos menos los de la tabla
                addAcciones(); // acciones de buscador, botoones abm, etc. menos la tabla
            }
        });
        cajaControlador.resumenSesionesCaja(filtro, filtroDesde, filtroHasta, new DatosListener<HashMap<String, Object>>() {
            @Override
            public void onSuccess(HashMap<String, Object> resumen) {
                float ingresos = ((Number) resumen.get("total_ingresos")).floatValue();
                float egresos = ((Number) resumen.get("total_egresos")).floatValue();
                float periodos = ((Number) resumen.get("total_periodos")).floatValue();
                actualizarResumenPanel(ingresos, egresos, periodos);
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
            public void onSuccess(HashMap<String, Object> datos, Paginado p) {} // no usamos
        });
    }
    
    /**
     * Renderiza de nuevo los elementos de la tabla y paginado con el filtro actual. 
     */
    private void reload() {
        cajaControlador.filtrar(filtro,filtroDesde,filtroHasta,filtroEstado, "" + pagina, "" + tamPagina, new DatosListener<List<HashMap<String, Object>>>() {
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
                cajasDetallado = new ArrayList<>(datos);
                esta.pagina=p.getPagina();
                esta.tamPagina=p.getTamPagina();
                esta.totalElementos=p.getTotalElementos();
                esta.totalPaginas=p.getTotalPaginas();
                remove(scroll);
                initTabla(); //tabla
                setVisibleByPermisos(tabla,"CAJA_LISTAR");
                add(scroll, BorderLayout.CENTER);
                actualizarPaginado();
                revalidate();
                repaint();
            }
        });
        cajaControlador.resumenSesionesCaja(filtro, filtroDesde, filtroHasta, new DatosListener<HashMap<String, Object>>() {
            @Override
            public void onSuccess(HashMap<String, Object> resumen) {
                float ingresos = ((Number) resumen.get("total_ingresos")).floatValue();
                float egresos = ((Number) resumen.get("total_egresos")).floatValue();
                float periodos = ((Number) resumen.get("total_periodos")).floatValue();
                actualizarResumenPanel(ingresos, egresos, periodos);
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
            public void onSuccess(HashMap<String, Object> datos, Paginado p) {} // no usamos
        });
    }

    private void initVista() {
        if (txtBuscar == null){
            txtBuscar = new JTextField();
          //  setVisibleByPermisos(txtBuscar, "CAJA_LISTAR");
        }
        if (btnNuevo == null){
            btnNuevo = new JButton("Nuevo");
        }
        //setVisibleByPermisos(btnNuevo, "CAJA_ALTA");
        if (btnEditar == null){
            btnEditar = new JButton("Editar");
        }
        //setVisibleByPermisos(btnEditar, "CAJA_MODIFICAR");
        if (btnEliminar == null){
            btnEliminar = new JButton("Eliminar");
        }
        //setVisibleByPermisos(btnEliminar, "CAJA_BAJA");

        buscarPanel = new JPanel(new MigLayout("insets 0, fillx", "[grow]"));
        buscarPanel.add(txtBuscar, "growx");

        if (fechaDesde == null){
            fechaDesde = new com.toedter.calendar.JDateChooser();
            fechaDesde.setPreferredSize(new Dimension(111, 30));
        }
        if (fechaHasta == null){
            fechaHasta = new com.toedter.calendar.JDateChooser();
            fechaHasta.setPreferredSize(new Dimension(111, 30));
        }
        if (comboEstado == null) {
            comboEstado = new JComboBox(new String[] {"Todos", "Ingresos", "Egresos", "Remitos"});
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
        botonesPanel.add(btnNuevo);
        botonesPanel.add(btnEditar);
        botonesPanel.add(btnEliminar);
        
        topPanel = new JPanel(new MigLayout("insets 5, fillx", "[grow][grow][right]"));
        topPanel.add(buscarPanel, "growx");
        topPanel.add(filtrosPanel, "growx");
        topPanel.add(botonesPanel, "right");
                
        JPanel resumenPanel = new JPanel(new MigLayout("insets 10, fillx", "[grow][grow][grow][grow][grow]"));
        resumenPanel.setBorder(BorderFactory.createTitledBorder("Resumen"));
        lblIngresos = new JLabel("Ingresos: $0");
        lblEgresos = new JLabel("Egresos: $0");
        lblGastosFijos = new JLabel("Gastos Fijos: $0");
        lblBalanceSinGastos = new JLabel("Balance (sin GF): $0");
        lblBalance = new JLabel("Balance: $0");

        resumenPanel.add(lblIngresos);
        resumenPanel.add(lblEgresos);
        resumenPanel.add(lblGastosFijos);
        resumenPanel.add(lblBalanceSinGastos);
        resumenPanel.add(lblBalance);

        this.setLayout(new BorderLayout());
        this.add(topPanel, BorderLayout.NORTH);
        this.add(scroll, BorderLayout.CENTER);
        bottomPanel = armarPaginado();
        JPanel panle = new JPanel();
        panle.setLayout(new BorderLayout());
        panle.add(resumenPanel,BorderLayout.NORTH);
        panle.add(bottomPanel,BorderLayout.CENTER);
        this.add(panle, BorderLayout.SOUTH);
    }

    private void setVisibleByPermisos(JComponent c, String permiso) {
        c.setVisible(permisos.contains(permiso));
    }

    private void initEstilos() {
        // Estilos del Panel Principal
        
        this.putClientProperty(FlatClientProperties.STYLE, MisEstilos.PANEL_CENTRAL);
        // Estilos de la barra de búsqueda
        txtBuscar.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, MisEstilos.PLACEHOLDER_BUSQUEDA);
        MisEstilos.aplicarEstilo(txtBuscar, MisEstilos.BUSQUEDA);

        // Estilos de los iconos
        MisEstilos.aplicarEstilo(txtBuscar, MisEstilos.BUSQUEDA);
        txtBuscar.setPreferredSize(new Dimension(200, 60));
        txtBuscar.setMaximumSize(new Dimension(250, 60));
        txtBuscar.setMinimumSize(new Dimension(150, 60));

        txtBuscar.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, new IconSVG(IconSVG.LUPA));
        btnNuevo.setIcon(new IconSVG(IconSVG.NUEVO,2));
        btnEditar.setIcon(new IconSVG(IconSVG.EDITAR,2));
        btnEliminar.setIcon(new IconSVG(IconSVG.ELIMINAR,2));

        MisEstilos.aplicarEstilo(btnNuevo, MisEstilos.BOTONCITO);
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
//        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                CajaModal modal = new CajaModal(MiFrame.getInstance());
//                modal.setVisible(true); // bloquea el thread hasta que es cerrado
//                filtro="";
//                pagina = 1;
//                reload();            
//            }
//        });
//        btnEditar.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                if(cajaSeleccionada != null){
//                    CajaModal modal = new CajaModal(MiFrame.getInstance(),cajaSeleccionada);
//                    modal.setVisible(true); // bloquea el thread hasta que es cerrado
//                    filtro="";
//                    pagina = 1;
//                    reload();
//                }
//            }
//        });
//        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                if(cajaSeleccionada != null){
//                    if(Dialogos.confirmar("¿Está seguro que desea eliminar este caja?", "Eliminar Caja")){
//                        cajaControlador.eliminarCaja(cajaSeleccionada.getId(),
//                            new DatosListener(){
//                                @Override
//                                public void onSuccess(Object datos) {
//                                    Dialogos.mostrarExito("" + datos);
//                                    filtro="";
//                                    pagina = 1;
//                                    reload();
//                                }
//
//                                @Override
//                                public void onError(String mensajeError) {
//                                    Dialogos.mostrarError(mensajeError);
//                                    filtro="";
//                                    pagina = 1;
//                                    reload();
//                                } 
//
//                                @Override
//                                public void onSuccess(Object datos, Paginado p  ) {
//                                }
//                            }
//                        );
//                    }
//                }
//            }
//        });
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
    // Métodos auxiliares para actualizar resumen
    public void actualizarResumenPanel(float ingresos, float egresos, float gastosFijos) {
        float balance = ingresos - egresos - gastosFijos;
        float balanceNeto = ingresos - egresos ;
        lblIngresos.setText("Ingresos: $" + ingresos);
        lblEgresos.setText("Egresos: $" + egresos);
        lblGastosFijos.setText("Gastos Fijos: $" + gastosFijos);
        lblBalanceSinGastos.setText("Balance: (Sin GF) $" + balanceNeto); // solo cajas, sin gastos fijos, de neto no tiene nada
        lblBalance.setText("Balance: $" + balance);
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
        txtPaginaActual.setText(""+ pagina);
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
        String[] headers = new String[]{"Cód:","Usuario:","Apertura:","Cierre:", "Monto Inicial:", "Monto Final:", "Estado"};
        //ASIGNAR TURNO A PEDIDO
        List<Object[]> rows = generarData();
        BiConsumer<PanelAccion, Integer> configurador = (panel, row) -> {
            //logger.debug("Action" + row);
//            Pedido p = (Pedido) pedidosDetallado.get(row).get("pedido");
//            //logger.trace("Pedido row: " + p.getId());
//            if("PENDIENTE".equals(p.getEstado_pedido())){
//                panel.agregarBoton("Asignar_Turno", "icon/calendar2.svg", e -> {
//                    TurnoModal modal = new TurnoModal(MiFrame.getInstance(),p);
//                    modal.setVisible(true); // bloquea el thread hasta que es cerrado
//                    filtro="";
//                    pagina = 1;
//                    reload();                
//                });
//            }
//            if("ASIGNADO".equals(p.getEstado_pedido())){
//                Cliente c = (Cliente) pedidosDetallado.get(row).get("cliente");
//                panel.agregarBoton("Presupuestar", "icon/file.svg", e -> {
//                    PresupuestoModal modal = new PresupuestoModal(MiFrame.getInstance(),p,c);
//                    modal.setVisible(true); // bloquea el thread hasta que es cerrado
//                    filtro="";
//                    pagina = 1;
//                    reload();
//                });
//            }
//            panel.agregarBoton("cancelar", "icon/plus.svg", e -> {
//                System.out.println("❌ Cancelar fila " + row);
//            });

        };
        
        TablaBuilder builder = new TablaBuilder(headers, rows,  - 1, null);
        scroll = builder.crearTabla();
        tabla = builder.getTable();
        // Estilos de la tabla
        tabla.getTableHeader().putClientProperty(FlatClientProperties.STYLE,MisEstilos.HEADER_TABLA);
        tabla.putClientProperty(FlatClientProperties.STYLE,MisEstilos.TABLA);
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {   // listenner
            @Override   
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = tabla.rowAtPoint(evt.getPoint());
                logger.trace(row);
                if ( row >= 0 ){
                    cajaSeleccionada = (Caja) cajasDetallado.get(row).get("caja");
                    System.out.println(cajaSeleccionada.getId());
                }
            }
        });
    }

    private List<Object[]> generarData() {
        List<Object[]> rows = new ArrayList<>();
        for (HashMap<String, Object> map : cajasDetallado) {
            Caja c = (Caja) map.get("caja");
            // Caja c = (Caja) map.get("caja");
            String estado = c.getCierre() == null || c.getCierre().isEmpty() ? "CERRADA" : "ABIERTA";
            rows.add(new Object[]{c.getId(),c.getCajero_id(),c.getApertura(), c.getCierre(),c.getMonto_inicial(),c.getMonto_final(),estado});
        }
        return rows;
    }

    
}