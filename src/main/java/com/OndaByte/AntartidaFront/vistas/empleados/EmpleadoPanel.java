package com.OndaByte.AntartidaFront.vistas.empleados;

import com.OndaByte.AntartidaFront.controladores.EmpleadoController;
import com.OndaByte.AntartidaFront.modelos.Empleado;
import com.OndaByte.AntartidaFront.estilos.MisEstilos;
import com.OndaByte.AntartidaFront.sesion.SesionController;
import com.OndaByte.AntartidaFront.vistas.DatosListener;
import com.OndaByte.AntartidaFront.vistas.MiFrame;
import com.OndaByte.AntartidaFront.vistas.util.Dialogos;
import com.OndaByte.AntartidaFront.vistas.util.IconSVG;
import com.OndaByte.AntartidaFront.vistas.util.Paginado;
import com.OndaByte.AntartidaFront.vistas.util.tabla.TablaBuilder;
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

public class EmpleadoPanel extends JPanel {

    private JTable tabla;
    private JScrollPane scroll;
    private JTextField txtBuscar;
    private JButton btnNuevo, btnEditar, btnEliminar;
    private JPanel topPanel, bottomPanel, buscarPanel, botonesPanel;

    private JButton btnPrimero = new JButton("|<");
    private JButton btnAnterior = new JButton("<");
    private JTextField txtPaginaActual = new JTextField("");
    private JButton btnSiguiente = new JButton(">");
    private JButton btnUltimo = new JButton(">|");
    private JComboBox<Integer> comboTamanioPagina = new JComboBox<>(new Integer[]{10, 20, 50, 100});
    private JLabel labelPaginas = new JLabel("Página 1 de 1");

    //Paginado
    private String filtro;
    private Integer pagina;
    private Integer tamPagina;
    private Integer totalElementos;
    private Integer totalPaginas;
    private Timer timer;

    private EmpleadoController empleadoControlador;
    HashSet<String> permisos = null;
    ArrayList<Empleado> empleados = null;
    Empleado empleadoSeleccionado = null;

    private EmpleadoPanel esta;

    private static Logger logger = LogManager.getLogger(EmpleadoPanel.class.getName());

    static{
        if(logger.isDebugEnabled()){
            logger.debug("Init logger en EmpleadoPanel");
        }
    }

    public EmpleadoPanel() {
        this.esta = this;
        this.permisos = (HashSet<String>) SesionController.getInstance().getSesionPermisos();
        empleadoControlador = EmpleadoController.getInstance();
        totalElementos=0;
        filtro="";
        pagina = 1;
        tamPagina = 20;
        empleadoControlador.filtrar(filtro, "" + pagina, "" + tamPagina,new DatosListener<List<Empleado>>(){
		
            @Override
            public void onSuccess(List<Empleado> datos) {
            }

            @Override
            public void onError(String mensajeError) {
                Dialogos.mostrarError(mensajeError);
                revalidate();
                repaint();
            }

            @Override
            public void onSuccess(List<Empleado> datos, Paginado p) {
                empleados = new ArrayList<>(datos);
                esta.pagina=p.getPagina();
                esta.tamPagina=p.getTamPagina();
                esta.totalElementos=p.getTotalElementos();
                esta.totalPaginas=p.getTotalPaginas();
                initTabla(); //tabla
                setVisibleByPermisos(tabla,"EMPLEADO_LISTAR");
                initVista(); // Inicializa la vista segun los permisos disponibles menos la tabla
                initEstilos(); //estilos menos los de la tabla
                addAcciones(); // acciones de buscador, botoones abm, etc. menos la tabla
            }
        });
    }

    /**
     * Renderiza de nuevo los elementos de la tabla con el filtro actual.
     */
    private void reload(){

        empleadoControlador.filtrar(filtro,"" +pagina,"" +tamPagina,new DatosListener<List<Empleado>>(){
            @Override
            public void onSuccess(List<Empleado> datos) {
            }

            @Override
            public void onError(String mensajeError) {
                Dialogos.mostrarError(mensajeError);
                revalidate();
                repaint();
            }

            @Override
            public void onSuccess(List<Empleado> datos, Paginado p) {
                empleados = new ArrayList<>(datos);
                empleadoSeleccionado = null;
                esta.pagina=p.getPagina();
                esta.tamPagina=p.getTamPagina();
                esta.totalElementos=p.getTotalElementos();
                esta.totalPaginas=p.getTotalPaginas();
                remove(scroll);
                initTabla(); //tabla
                setVisibleByPermisos(tabla,"EMPLEADO_LISTAR");
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
        setVisibleByPermisos(txtBuscar,"EMPLEADO_LISTAR");
        if(btnNuevo==null){
            btnNuevo = new JButton("Nuevo");

        }
        setVisibleByPermisos(btnNuevo,"EMPLEADO_ALTA");
        if(btnEditar==null){
            btnEditar = new JButton("Editar");

        }
        setVisibleByPermisos(btnEditar,"EMPLEADO_MODIFICAR");
        if(btnEliminar==null){
            btnEliminar = new JButton("Eliminar");
        }
        setVisibleByPermisos(btnEliminar,"EMPLEADO_BAJA");

        buscarPanel = new JPanel(new MigLayout("insets 0, fillx", "[grow]"));
        buscarPanel.add(txtBuscar, "growx");

        botonesPanel = new JPanel(new MigLayout("insets 0", "[]10[]10[]"));
        botonesPanel.add(btnNuevo);
        botonesPanel.add(btnEditar);
        botonesPanel.add(btnEliminar);

        topPanel = new JPanel(new MigLayout("insets 5, fillx", "[grow][right]"));
        topPanel.add(buscarPanel, "growx, left");
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
        btnEliminar.setIcon(new IconSVG(IconSVG.ELIMINAR,2));

        MisEstilos.aplicarEstilo(btnNuevo, MisEstilos.BOTONCITO);
        MisEstilos.aplicarEstilo(btnEditar, MisEstilos.BOTONCITO);
        MisEstilos.aplicarEstilo(btnEliminar, MisEstilos.BOTONCITO);
    }


private void addAcciones(){
        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {filtrarEmpleado();}

            public void removeUpdate(DocumentEvent e) {filtrarEmpleado();}

            public void changedUpdate(DocumentEvent e) {filtrarEmpleado();}

            private void filtrarEmpleado() {
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
                EmpleadoModal modal = new EmpleadoModal(MiFrame.getInstance());
                modal.setVisible(true); // bloquea el thread hasta que es cerrado
                filtro="";
                pagina = 1;
                reload();
            }
        });
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if(empleadoSeleccionado != null){
                    EmpleadoModal modal = new EmpleadoModal(MiFrame.getInstance(),empleadoSeleccionado);
                    modal.setVisible(true); // bloquea el thread hasta que es cerrado
                    filtro="";
                    pagina = 1;
                    reload();
                }
            }
        });
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if(empleadoSeleccionado != null){
                    if(Dialogos.confirmar("¿Está seguro que desea eliminar este empleado?", "Eliminar Empleado")){
                        empleadoControlador.eliminarEmpleado(empleadoSeleccionado.getId(),
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
                                    public void onSuccess(Object datos, Paginado p  ) {
                                        //          throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
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
        txtPaginaActual.setText(""+ pagina);
        labelPaginas.setText("Página " + pagina + " de " + totalPaginas);
    }

    public void initTabla() {
        String[] headers = new String[]{"Nombre", "Dni",  "Telefono", "Direccion"};
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
                if ( row >= 0 ){
                    empleadoSeleccionado = (Empleado) empleados.get(row);
                }
            }
        });
    }

    private List<Object[]> generarData() {

        List<Object[]> rows = new ArrayList<>();
        for (int i = 0; i < empleados.size(); i++) {
            Empleado p = (Empleado) empleados.get(i);
            rows.add(new Object[]{p.getNombre(),p.getDni(),p.getTelefono(), p.getDireccion()});
        }
        return rows;
    }
}


