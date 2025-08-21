package com.OndaByte.MisterFront.vistas.caja;

import com.OndaByte.MisterFront.controladores.CajaController;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import javax.swing.*;

import net.miginfocom.swing.MigLayout;

public class MostradorCajaPanel extends JPanel {

    private JPanel topPanel, centerPanel, bottomPanel;
    private JLabel lblEstadoCaja, lblOperador, lblHoraInicio;
    private JLabel lblEstadoOperacion, lblHora;
    private JButton btnAbrirCerrarCaja;
    private JButton btnNuevaVenta;
    private JButton btnVerInfo;

    private Caja caja;
    private CajaController cajaController;
    HashSet<String> permisos = null;

    private JTabbedPane tabbedPane;
    private HashMap<String,VentaCajaPanel> ventas;
    private int contadorVentas = 1;
    private List<ItemVenta> carrito = new ArrayList<>();

    public MostradorCajaPanel() {
        this.permisos = (HashSet<String>) SesionController.getInstance().getSesionPermisos();
        this.cajaController = CajaController.getInstance();
        this.setLayout(new BorderLayout());
        initTopPanel();
        initCenterPanel();
        initBottomPanel();
        this.actualizarEstadoCaja(SesionController.getInstance().getSesionCaja() != null);

        this.add(topPanel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);
        this.putClientProperty(FlatClientProperties.STYLE, MisEstilos.PANEL_CENTRAL);
    }

    private void initTopPanel() {
        topPanel = new JPanel(new MigLayout("insets 10, fillx", "[][grow][]20[][]"));

        // ====== Labels ======
        lblEstadoCaja = new JLabel("CAJA: CERRADA");
        lblEstadoCaja.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblEstadoCaja.setOpaque(true);
        lblEstadoCaja.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
        lblEstadoCaja.putClientProperty("JComponent.roundRect", true); // FlatLaf esquinas redondeadas

        lblOperador = new JLabel("OPERADOR: -");
        lblOperador.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblOperador.setForeground(new Color(90, 90, 90));

        lblHoraInicio = new JLabel("INICIO: --:--");
        lblHoraInicio.setFont(new Font("SansSerif", Font.PLAIN, 14));
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
        setVisibleByPermisos(btnAbrirCerrarCaja, "CAJA_ABRIR");
        btnAbrirCerrarCaja.addActionListener(e -> abrirCerrarCaja());

        // ====== Botón Ver Info Caja ======
        if (btnVerInfo == null) {
            btnVerInfo = new JButton("", new IconSVG(IconSVG.OJO));
        }
        setVisibleByPermisos(btnVerInfo, "CAJA_VER_INFO");
        btnVerInfo.addActionListener(e -> verInfoCaja());

        // ====== Añadir al panel ======
        topPanel.add(lblEstadoCaja, "left");
        topPanel.add(btnVerInfo, "left");
        topPanel.add(lblOperador, "center");
        topPanel.add(lblHoraInicio, "left, growx");
        topPanel.add(btnAbrirCerrarCaja, "left");
    }

    private void actualizarEstadoCaja(boolean abierta) {
        btnVerInfo.setEnabled(abierta);
        if (abierta) {
            lblEstadoCaja.setText("CAJA: ABIERTA");
            lblEstadoCaja.setIcon(new IconSVG(IconSVG.CAJA_ABIERTA, 3));
            lblEstadoCaja.setBackground(new Color(200, 255, 200)); // verde claro
            lblEstadoCaja.setForeground(new Color(0, 120, 0));     // verde oscuro
        } else {
            lblEstadoCaja.setText("CAJA: CERRADA");
            lblEstadoCaja.setIcon(new IconSVG(IconSVG.CAJA_CERRADA, 3));
            lblEstadoCaja.setBackground(new Color(255, 220, 220)); // rojo claro
            lblEstadoCaja.setForeground(new Color(150, 0, 0));     // rojo oscuro
        }
    }

    private void initCenterPanel() {
        centerPanel = new JPanel(new BorderLayout());
        tabbedPane = SesionController.getInstance().getSesionTabsVentas();
        ventas = SesionController.getInstance().getSesionVentasActivas();
        if (btnNuevaVenta == null) {
            btnNuevaVenta = new JButton("+ Nueva Venta");
        }
        btnNuevaVenta.setEnabled(SesionController.getInstance().getSesionCaja() != null);
        setVisibleByPermisos(btnNuevaVenta, "CAJA_VENTA");
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

    private void setVisibleByPermisos(JComponent c, String permiso) {
        c.setVisible(permisos.contains(permiso));
    }

    private void agregarNuevoPanelVenta() {
        VentaCajaPanel nuevaVenta = new VentaCajaPanel(this, "Venta ", contadorVentas);
        ventas.put(nuevaVenta.getNombre(),nuevaVenta);
        tabbedPane.addTab(nuevaVenta.getNombre(), nuevaVenta);
        contadorVentas++;
    }

    public void quitarNuevoPanelVenta(VentaCajaPanel tab) {
        // VentaCajaPanel nuevaVenta = new VentaCajaPanel("Venta " + contadorVentas);
        tabbedPane.remove(tab);
        ventas.remove(tab.getNombre());
        this.repaint();
    }

    private void onSuccessAbrirCaja(String datos) {
        lblEstadoCaja.setText("CAJA: ABIERTA");
        btnAbrirCerrarCaja.setText("CERRAR CAJA");
        setVisibleByPermisos(btnVerInfo, "CAJA_VER_INFO");
        setVisibleByPermisos(btnAbrirCerrarCaja, "CAJA_CERRAR");
        lblHoraInicio.setText("INICIO: " + java.time.LocalTime.now().withNano(0));
        lblEstadoOperacion.setText("ESTADO: operativa");
        btnNuevaVenta.setEnabled(true);
        actualizarEstadoCaja(true);
        Dialogos.mostrarExito(datos);
        agregarNuevoPanelVenta();
    }
    private void verInfoCaja() {

        Caja c = SesionController.getInstance().getSesionCaja();

        if (c == null) {
            Dialogos.mostrarError("No hay ninguna caja abierta.");
            return;
        }

        // Obtener datos
        String montoInicial = c.getMonto_inicial();
        String montoActual = c.getMonto_actual();

        // Crear el modal
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Información de Caja", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new MigLayout("wrap 2", "10[right]5[grow,fill]10"));

        panel.add(new JLabel("Monto Inicial:"));
        panel.add(new JLabel(montoInicial));

        panel.add(new JLabel("Monto Actual:"));
        panel.add(new JLabel(montoActual));

        //panel.add(new JLabel("Cantidad de Ventas:"));
        //panel.add(new JLabel(String.valueOf("fictisiop")));

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dialog.dispose());

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(btnCerrar, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void abrirCerrarCaja() {

        if (SesionController.getInstance().getSesionCaja() == null) {
            String input = Dialogos.mostrarInput("Ingrese monto inicial de la caja:");
            try {
                //lblEstadoCaja.setForeground(new Color(0, 150, 0));
                float montoInicial = Float.parseFloat(input);
                this.cajaController.abrirCaja(montoInicial, new DatosListener<String>() {
                    @Override
                    public void onSuccess(String datos) {
                        SesionController.getInstance().getSesionCaja().setMonto_inicial(input);
                        onSuccessAbrirCaja(datos);
                    }

                    @Override
                    public void onError(String mensajeError) {
                        cajaController.cerrarCaja(new DatosListener<String>() {
                            @Override
                            public void onSuccess(String resultado) {
                                //Dialogos.mostrarExito(resultado);
                                cajaController.abrirCaja(montoInicial, new DatosListener<String>() {
                                    @Override
                                    public void onSuccess(String datos) {
                                        onSuccessAbrirCaja(datos);
                                    }

                                    @Override
                                    public void onError(String mensajeError) {

                                        Dialogos.mostrarError(mensajeError);
                                    }

                                    @Override
                                    public void onSuccess(String datos, Paginado p) {
                                        //      throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                                    }
                                });
                            }

                            @Override
                            public void onError(String mensajeError) {
                                Dialogos.mostrarError(mensajeError);
                            }

                            @Override
                            public void onSuccess(String datos, Paginado p) {
                                //      throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                            }
                        });
                        //Dialogos.mostrarError(mensajeError);
                    }

                    @Override
                    public void onSuccess(String datos, Paginado p) {
                        //     throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                    }
                });

            } catch (NumberFormatException ex) {
                Dialogos.mostrarError("Monto inválido");
            }

        } else {
            boolean confirmacion = Dialogos.confirmar("¿Esta seguro que quiere cerrar la caja?", "CERRAR CAJA");
            if (confirmacion) {
                this.cajaController.cerrarCaja(new DatosListener<String>() {
                    @Override
                    public void onSuccess(String datos) {
                        lblEstadoCaja.setText("CAJA: CERRADA");
                        btnAbrirCerrarCaja.setText("ABRIR CAJA");
                        lblHoraInicio.setText("INICIO: --:--");
                        lblEstadoOperacion.setText("ESTADO: operativa");
                        btnNuevaVenta.setEnabled(false);
                        actualizarEstadoCaja(false);
                        Dialogos.mostrarExito(datos);
                        tabbedPane.removeAll();
                    }

                    @Override
                    public void onError(String mensajeError) {
                        Dialogos.mostrarError(mensajeError);
                    }

                    @Override
                    public void onSuccess(String datos, Paginado p) {
                        //     throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                    }
                });
            }
        }
    }
}