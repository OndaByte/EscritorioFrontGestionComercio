
package com.OndaByte.MisterFront.vistas.caja;

import com.OndaByte.MisterFront.controladores.MovimientoController;
import com.OndaByte.MisterFront.estilos.MisEstilos;
import com.OndaByte.MisterFront.modelos.Caja;
import com.OndaByte.MisterFront.modelos.ItemVenta;
import com.OndaByte.MisterFront.sesion.SesionController;
import com.OndaByte.MisterFront.vistas.DatosListener;
import com.OndaByte.MisterFront.vistas.util.Dialogos;
import com.OndaByte.MisterFront.vistas.util.Paginado;
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import net.miginfocom.swing.MigLayout;

public class MostradorCajaPanel extends JPanel {

    private JPanel topPanel, centerPanel, bottomPanel;
    private JLabel lblEstadoCaja, lblOperador, lblHoraInicio;
    private JButton btnAbrirCerrarCaja;
    private JLabel lblEstadoOperacion, lblHora;
    private JButton btnNuevaVenta;

    private Caja caja;
    private MovimientoController cajaController;

    private JTabbedPane tabbedPane;
    private List<VentaCajaPanel> ventas;
    private int contadorVentas = 1;
    private List<ItemVenta> carrito = new ArrayList<>();

    public MostradorCajaPanel() {
        this.setLayout(new BorderLayout());
        initTopPanel();
        initCenterPanel();
        initBottomPanel();
        agregarNuevoPanelVenta(); // inicializamos con venta abierta al abrir caja 
        this.add(topPanel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);
        this.putClientProperty(FlatClientProperties.STYLE, MisEstilos.PANEL_CENTRAL);
        this.cajaController = MovimientoController.getInstance();
    }

    private void initTopPanel() {
        topPanel = new JPanel(new MigLayout("insets 10, fillx", "[][grow][]20[][]"));
        //Labels
        lblEstadoCaja = new JLabel("CAJA: CERRADA");
        lblOperador = new JLabel("OPERADOR: -");
        lblHoraInicio = new JLabel("INICIO: --:--");
        btnAbrirCerrarCaja = new JButton("ABRIR CAJA");
        btnAbrirCerrarCaja.addActionListener(e -> abrirCerrarCaja());

        topPanel.add(lblEstadoCaja);
        topPanel.add(lblOperador, "center");
        topPanel.add(lblHoraInicio, "left, growx");
        topPanel.add(btnAbrirCerrarCaja, "left");
    }

    private void initCenterPanel() {
        centerPanel = new JPanel(new BorderLayout());
        tabbedPane = new JTabbedPane();
        ventas = new ArrayList<>();
        btnNuevaVenta = new JButton("+ Nueva Venta");
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
                float montoInicial = Float.parseFloat(input);
                DatosListener listener = new DatosListener<String>() {
                    @Override
                    public void onSuccess(String datos) {
                        Dialogos.mostrarExito("Lógica, permitir vender bboton ahabilitar ");

                        lblEstadoCaja.setText("CAJA: ABIERTA");
                        btnAbrirCerrarCaja.setText("CERRAR CAJA");
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
