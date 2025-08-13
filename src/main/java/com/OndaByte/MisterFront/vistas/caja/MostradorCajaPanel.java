
package com.OndaByte.MisterFront.vistas.caja;

import com.OndaByte.MisterFront.controladores.MovimientoController;
import com.OndaByte.MisterFront.estilos.MisEstilos;
import com.OndaByte.MisterFront.modelos.Caja;
import com.OndaByte.MisterFront.modelos.ItemVenta;
import com.OndaByte.MisterFront.sesion.SesionController;
import com.OndaByte.MisterFront.vistas.DatosListener;
import com.OndaByte.MisterFront.vistas.util.Dialogos;
import com.OndaByte.MisterFront.vistas.util.IconSVG;
import com.OndaByte.MisterFront.vistas.util.Paginado;
import com.formdev.flatlaf.FlatClientProperties;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.swing.*;

import net.miginfocom.swing.MigLayout;

public class MostradorCajaPanel extends JPanel {

    private JPanel topPanel, centerPanel, bottomPanel;
    private JLabel lblEstadoCaja, lblOperador, lblHoraInicio;
    private JButton btnAbrirCerrarCaja;
    private JLabel lblEstadoOperacion, lblHora;
    private JButton btnNuevaVenta;

    private Caja caja;
    private MovimientoController cajaController;
    HashSet<String> permisos = null;

    private JTabbedPane tabbedPane;
    private List<VentaCajaPanel> ventas;
    private int contadorVentas = 1;
    private List<ItemVenta> carrito = new ArrayList<>();

    public MostradorCajaPanel() {
        this.permisos = (HashSet<String>) SesionController.getInstance().getSesionPermisos();
        this.cajaController = MovimientoController.getInstance();
        this.setLayout(new BorderLayout());
        initTopPanel();
        initCenterPanel();
        initBottomPanel();
        agregarNuevoPanelVenta(); // inicializamos con venta abierta al abrir caja 
        this.add(topPanel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);
        this.putClientProperty(FlatClientProperties.STYLE, MisEstilos.PANEL_CENTRAL);
    }


    /*
    private void initTopPanel() {
        topPanel = new JPanel(new MigLayout("insets 10, fillx", "[][grow][]20[][]"));
        //Labels
        lblEstadoCaja = new JLabel("CAJA: CERRADA");
        lblEstadoCaja.putClientProperty("FlatLaf.styleClass", "estado.abierto");

        lblOperador = new JLabel("OPERADOR: -");
        lblHoraInicio = new JLabel("INICIO: --:--");

        if(btnAbrirCerrarCaja==null){
            btnAbrirCerrarCaja = new JButton("ABRIR CAJA");
        }
        setVisibleByPermisos(btnAbrirCerrarCaja,"ABRIR_CAJA");
        btnAbrirCerrarCaja.addActionListener(e -> abrirCerrarCaja());

        topPanel.add(lblEstadoCaja);
        topPanel.add(lblOperador, "center");
        topPanel.add(lblHoraInicio, "left, growx");
        topPanel.add(btnAbrirCerrarCaja, "left");
    }

     */

    private void initTopPanel() {
        topPanel = new JPanel(new MigLayout("insets 10, fillx", "[][grow][]20[][]"));

        // ====== Labels ======
        lblEstadoCaja = new JLabel("CAJA: CERRADA");
        //lblEstadoCaja.setFont(new Font("Courier New", Font.BOLD, 16));
        //lblEstadoCaja.setOpaque(true);
        //lblEstadoCaja.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
        //lblEstadoCaja.putClientProperty("JComponent.roundRect", true); // FlatLaf esquinas redondeadas
        actualizarEstadoCaja(false); // Estado inicial cerrado

        lblOperador = new JLabel("OPERADOR: -");
        lblOperador.setFont(new Font("Courier New", Font.PLAIN, 14));
        lblOperador.setForeground(new Color(90, 90, 90));

        lblHoraInicio = new JLabel("INICIO: --:--");
        lblHoraInicio.setFont(new Font("Courier New", Font.PLAIN, 14));
        lblHoraInicio.setForeground(new Color(90, 90, 90));

        lblEstadoCaja.putClientProperty("FlatLaf.styleClass", "TopPanel.estado.cerrado");
        lblOperador.putClientProperty("FlatLaf.styleClass", "TopPanel.sub");
        lblHoraInicio.putClientProperty("FlatLaf.styleClass", "TopPanel.sub");

        lblOperador.setIcon(new IconSVG(IconSVG.OPERADOR));
        lblHoraInicio.setIcon(new IconSVG(IconSVG.RELOJ));


        // ====== Botón Abrir/Cerrar Caja ======
        if (btnAbrirCerrarCaja == null) {
            btnAbrirCerrarCaja = new JButton("ABRIR CAJA");
        }
        setVisibleByPermisos(btnAbrirCerrarCaja, "ABRIR_CAJA");
        btnAbrirCerrarCaja.addActionListener(e -> abrirCerrarCaja());

        // ====== Añadir al panel ======
        topPanel.add(lblEstadoCaja, "left");
        topPanel.add(lblOperador, "center");
        topPanel.add(lblHoraInicio, "left, growx");
        topPanel.add(btnAbrirCerrarCaja, "left");
    }

    private void actualizarEstadoCaja(boolean abierta) {
        if (abierta) {
            lblEstadoCaja.setText("CAJA: ABIERTA");
            lblEstadoCaja.setIcon(new IconSVG(IconSVG.CAJA_ABIERTA, 3));
            MisEstilos.aplicarEstilo(lblEstadoCaja, MisEstilos.CAJA_LABEL_CAJA_ABIERTA);
            //lblEstadoCaja.setBackground(new Color(200, 255, 200)); // verde claro
            //lblEstadoCaja.setForeground(new Color(0, 120, 0));     // verde oscuro
        } else {
            lblEstadoCaja.setText("CAJA: CERRADA");
            lblEstadoCaja.setIcon(new IconSVG(IconSVG.CAJA_CERRADA, 3));
            MisEstilos.aplicarEstilo(lblEstadoCaja, MisEstilos.CAJA_LABEL_CAJA_CERRADA);
            //lblEstadoCaja.setBackground(new Color(255, 220, 220)); // rojo claro
            //lblEstadoCaja.setForeground(new Color(150, 0, 0));     // rojo oscuro
        }
    }


    private void initCenterPanel() {
        centerPanel = new JPanel(new BorderLayout());
        tabbedPane = new JTabbedPane();
        ventas = new ArrayList<>();
        if(btnNuevaVenta==null){
            btnNuevaVenta = new JButton("+ Nueva Venta");
        }
        setVisibleByPermisos(btnNuevaVenta,"VENTA_ALTA");
        btnNuevaVenta.addActionListener(e -> agregarNuevoPanelVenta());
        centerPanel.add(btnNuevaVenta, BorderLayout.NORTH);
        centerPanel.add(tabbedPane, BorderLayout.CENTER);
    }

    private void initBottomPanel() {
        bottomPanel = new JPanel(new MigLayout("insets 5,fillx", "[]"));
        lblEstadoOperacion = new JLabel("ESTADO: listo");
        lblHora = new JLabel("Hora: -");

        bottomPanel.add(lblEstadoOperacion, "left,growx");
        bottomPanel.add(lblHora, "right");
    }

    private void setVisibleByPermisos(JComponent c, String permiso){
        c.setVisible(permisos.contains(permiso));
        c.setVisible(true);
    }

    private void agregarNuevoPanelVenta() {
        VentaCajaPanel nuevaVenta = new VentaCajaPanel("Venta " + contadorVentas);
        ventas.add(nuevaVenta);
        tabbedPane.addTab(nuevaVenta.getNombre(), nuevaVenta);
        contadorVentas++;
    }

    private void abrirCerrarCaja() {

        if (SesionController.getInstance().getSesionCaja() == null) {
            String input = Dialogos.mostrarInput("Ingrese monto inicial de la caja:");
            try {
                actualizarEstadoCaja(true);
                //lblEstadoCaja.setForeground(new Color(0, 150, 0));
                float montoInicial = Float.parseFloat(input);
                DatosListener listener = new DatosListener<String>() {
                    @Override
                    public void onSuccess(String datos) {
                        Dialogos.mostrarExito("Lógica, permitir vender bboton ahabilitar ");

                        lblEstadoCaja.setText("CAJA: ABIERTA");
                        btnAbrirCerrarCaja.setText("CERRAR CAJA");
                        setVisibleByPermisos(btnAbrirCerrarCaja,"CERRAR_CAJA");
                        lblHoraInicio.setText("INICIO: " + java.time.LocalTime.now().withNano(0));
                        lblEstadoOperacion.setText("ESTADO: operativa");
                    }

                    @Override
                    public void onError(String mensajeError) {
                        Dialogos.mostrarError(mensajeError);
                    }

                    @Override
                    public void onSuccess(String datos, Paginado p) {
                        //     throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                    }
                };
      //         this.cajaController.abrirCaja(montoInicial, listener);

            } catch (NumberFormatException ex) {
                Dialogos.mostrarError("Monto inválido");
            }

        } else {
            Dialogos.confirmar("¿Esta seguro que quiere cerrar la caja?", "CERRAR CAJA");
       //     this.cajaController.cerrarCaja(new DatosListener<String>() {
//                @Override
//                public void onSuccess(String datos) {
//                    Dialogos.mostrarExito("Lógica, permitir vender bboton ahabilitar ");
//
//                    lblEstadoCaja.setText("CAJA: ABIERTA");
//                    btnAbrirCerrarCaja.setText("CERRAR CAJA");
//                    lblHoraInicio.setText("INICIO: " + java.time.LocalTime.now().withNano(0));
//                    lblEstadoOperacion.setText("ESTADO: operativa");
//                }
//
//                @Override
//                public void onError(String mensajeError) {
//                    Dialogos.mostrarError(mensajeError);
//                }
//
//                @Override
//                public void onSuccess(String datos, Paginado p) {
//                    //     throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
//                }
//            });
        }
    }
}
