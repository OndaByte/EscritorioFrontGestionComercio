package com.OndaByte.AntartidaFront.vistas.gastosFijos;
import com.OndaByte.AntartidaFront.controladores.PeriodoController;
import com.OndaByte.AntartidaFront.modelos.Gasto;
import com.OndaByte.AntartidaFront.modelos.Periodo;
import com.OndaByte.AntartidaFront.estilos.MisEstilos;
import com.OndaByte.AntartidaFront.sesion.SesionController;
import com.OndaByte.AntartidaFront.vistas.DatosListener;
import com.OndaByte.AntartidaFront.vistas.MiFrame;
import com.OndaByte.AntartidaFront.vistas.util.Dialogos;
import com.OndaByte.AntartidaFront.vistas.util.FechaUtils;
import com.OndaByte.AntartidaFront.vistas.util.IconSVG;
import com.OndaByte.AntartidaFront.vistas.util.Paginado;
import com.OndaByte.AntartidaFront.vistas.util.tabla.TablaBuilder;
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
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
import com.toedter.calendar.JDateChooser;
import java.beans.PropertyChangeListener;

public class GastoFijoPanel extends JPanel {
   
    private JTable tabla;
    private JScrollPane scroll;
    private JTextField txtBuscar;
    private JDateChooser fechaDesde,fechaHasta;
    private JButton btnNuevo, btnEditar, btnPausar;
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

    private PeriodoController periodoControlador;
    HashSet<String> permisos = null;
    private ArrayList<HashMap<String, Object>> periodosDetallado;
    Periodo periodoSeleccionado = null;
    Gasto gastoSeleccionado = null;
    
    private JLabel labelCostoTotal = new JLabel();

    
    private GastoFijoPanel esta;

    private static Logger logger = LogManager.getLogger(GastoFijoPanel.class.getName());

     static{
        if(logger.isDebugEnabled()){
            logger.debug("Init logger en PeriodoPanel");
        }
    }
    
    public GastoFijoPanel(){   
        this.esta = this;
        this.permisos = (HashSet<String>) SesionController.getInstance().getSesionPermisos();
        periodoControlador = PeriodoController.getInstance();
        totalElementos=0;
        filtro="";
        filtroEstado="";
        filtroDesde="";
        filtroHasta="";
        pagina = 1;
        tamPagina = 20;
        periodoControlador.filtrar(filtro,filtroDesde,filtroHasta,filtroEstado, "" + pagina, "" + tamPagina,new DatosListener<List<HashMap<String,Object>>>(){
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
                periodosDetallado = new ArrayList<>(datos);
                esta.pagina=p.getPagina();
                esta.tamPagina=p.getTamPagina();
                esta.totalElementos=p.getTotalElementos();
                esta.totalPaginas=p.getTotalPaginas();
               
                esta.labelCostoTotal.setText("Total: "+p.getCosto_total()+" $");
                initTabla();
                setVisibleByPermisos(tabla,"GASTO_LISTAR");
                
                initVista();
                initEstilos();
                addAcciones();
            }
        });
    }

    private void reload(){
        periodoControlador.filtrar(filtro,filtroDesde,filtroHasta,filtroEstado, "" + pagina, "" + tamPagina, new DatosListener<List<HashMap<String, Object>>>() {
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
                periodosDetallado = new ArrayList<>(datos);
                gastoSeleccionado = null;
                esta.pagina=p.getPagina();
                esta.tamPagina=p.getTamPagina();
                esta.totalElementos=p.getTotalElementos();
                esta.totalPaginas=p.getTotalPaginas();
                esta.labelCostoTotal.setText("Total: "+p.getCosto_total() + " $");
                remove(scroll);
                initTabla(); //tabla
                setVisibleByPermisos(tabla,"GASTO_LISTAR");
                add(scroll, BorderLayout.CENTER);
                actualizarPaginado();
                revalidate();
                repaint();
            }
        });
    };
    
    public void initVista() {

        if(txtBuscar==null){
            txtBuscar = new JTextField();
        }
        setVisibleByPermisos(txtBuscar,"GASTO_LISTAR");
        if(btnNuevo==null){
            btnNuevo = new JButton("Nuevo");

        }
        setVisibleByPermisos(btnNuevo,"GASTO_ALTA");
        if(btnEditar==null){
            btnEditar = new JButton("Editar");
        }
        
        setVisibleByPermisos(btnEditar,"GASTO_MODIFICAR");
        if(btnPausar==null){
            btnPausar = new JButton("Pausar");
        }
        setVisibleByPermisos(btnPausar,"GASTO_BAJA");
        
        if (fechaDesde == null){
            fechaDesde = new com.toedter.calendar.JDateChooser();
            fechaDesde.setPreferredSize(new Dimension(111, 30));
        }
        if (fechaHasta == null){
            fechaHasta = new com.toedter.calendar.JDateChooser();
            fechaHasta.setPreferredSize(new Dimension(111, 30));
        }

        buscarPanel = new JPanel(new MigLayout("insets 0, fillx", "[grow]"));
        buscarPanel.add(txtBuscar, "growx");

        botonesPanel = new JPanel(new MigLayout("insets 0", "[]10[]10[]"));
        botonesPanel.add(btnNuevo);
        botonesPanel.add(btnEditar);
        botonesPanel.add(btnPausar);
        
        JPanel filtrosPanel = new JPanel(new MigLayout("insets 0", "[]20[]20[]"));
        filtrosPanel.add(new JLabel("Desde:"));
        filtrosPanel.add(fechaDesde);
        filtrosPanel.add(new JLabel("Hasta:"));
        filtrosPanel.add(fechaHasta);

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

    private void setVisibleByPermisos(JComponent c, String permiso){
        c.setVisible(permisos.contains(permiso));
    }

    private void initEstilos() {
        // Estilos del Panel Principal
        this.putClientProperty(FlatClientProperties.STYLE, MisEstilos.PANEL_CENTRAL);

        // Estilos de la tabla
        tabla.getTableHeader().putClientProperty(FlatClientProperties.STYLE,MisEstilos.HEADER_TABLA);
        tabla.putClientProperty(FlatClientProperties.STYLE,MisEstilos.TABLA);

        // Estilos de la barra de búsqueda
        txtBuscar.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, MisEstilos.PLACEHOLDER_BUSQUEDA);
        MisEstilos.aplicarEstilo(txtBuscar, MisEstilos.BUSQUEDA);

        txtBuscar.setPreferredSize(new Dimension(200, 60));
        txtBuscar.setMaximumSize(new Dimension(250, 60));
        txtBuscar.setMinimumSize(new Dimension(150, 60));

        txtBuscar.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, new IconSVG(IconSVG.LUPA));

        btnNuevo.setIcon(new IconSVG(IconSVG.NUEVO,2));
        btnEditar.setIcon(new IconSVG(IconSVG.EDITAR,2));
        btnPausar.setIcon(new IconSVG(IconSVG.ELIMINAR,2));

        MisEstilos.aplicarEstilo(btnNuevo, MisEstilos.BOTONCITO);
        MisEstilos.aplicarEstilo(btnEditar, MisEstilos.BOTONCITO);
        MisEstilos.aplicarEstilo(btnPausar, MisEstilos.BOTONCITO);

    }
    
    private void addAcciones(){
        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {filtrarPeriodo();}

            public void removeUpdate(DocumentEvent e) {filtrarPeriodo();}

            public void changedUpdate(DocumentEvent e) {filtrarPeriodo();}

            private void filtrarPeriodo() {
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
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GastoFijoModal modal = new GastoFijoModal(MiFrame.getInstance());
                modal.setVisible(true); // bloquea el thread hasta que es cerrado
                filtro="";
                pagina = 1;
                reload();
            }
        });
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if(periodoSeleccionado != null){
                    GastoFijoModal modal = new GastoFijoModal(MiFrame.getInstance(),gastoSeleccionado,periodoSeleccionado);
                    modal.setVisible(true); // bloquea el thread hasta que es cerrado
                    filtro="";
                    pagina = 1;
                    reload();
                }
            }
        });
        btnPausar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                   if(gastoSeleccionado != null){
                    if(Dialogos.confirmar("Al pausar un gasto, no se generaran los periodos correspondientes al tiempo que el gasto este pausado.\nPuede reanudar el gasto en cualquier momento.", "Pausar Gasto")){
                        periodoControlador.eliminarGastoFijo(gastoSeleccionado.getId(),
                                new DatosListener(){
                                    @Override
                                    public void onSuccess(Object datos) {
                                        Dialogos.mostrarExito("" + datos);
                                        filtro="";
                                        pagina = 1;
                                        reload();
                                    }

                                    @Override
                                    public void onError(String mensajeError) {
                                        Dialogos.mostrarError(mensajeError);
                                        filtro="";
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

        PropertyChangeListener fechaListener = evt -> {
            if ("date".equals(evt.getPropertyName())) {
                filtrarFechas();
            }
        };
        fechaDesde.getDateEditor().addPropertyChangeListener(fechaListener);
        fechaHasta.getDateEditor().addPropertyChangeListener(fechaListener);
    }

    private void filtrarFechas() {
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
        txtPaginaActual.setText(""+ pagina);
        labelPaginas.setText("Página " + pagina + " de " + totalPaginas);

        paginadoPanel.add(labelCostoTotal);
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
        txtPaginaActual.setText(""+ pagina);
        labelPaginas.setText("Página " + pagina + " de " + totalPaginas);
    }
    
    
    public void initTabla() {
      String[] headers = new String[]{"Gasto","Repeticion","Fecha","Costo","Estado"};
        List<Object[]> rows = generarData();
        TablaBuilder builder = new TablaBuilder(headers,rows,headers.length - 1,null);
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
                if (row >= 0 ){
                   periodoSeleccionado = (Periodo) periodosDetallado.get(row).get("periodo");
                   gastoSeleccionado = (Gasto) periodosDetallado.get(row).get("gasto");
                }
            }
        });
    }
    
    private List<Object[]> generarData() {
        List<Object[]> rows = new ArrayList<>();
        for (HashMap<String, Object> map : periodosDetallado) {
            Periodo p = (Periodo) map.get("periodo");
            Gasto g = (Gasto) map.get("gasto");
            rows.add(new Object[]{g.getNombre(), FechaUtils.repeticionString(g.getRepeticion()),p.getPeriodo(),p.getCosto(),g.getEstado()});
        }
        return rows;
    }   
}
