package com.OndaByte.MisterFront.vistas.ordenes;

import com.OndaByte.MisterFront.controladores.ClienteController;
import com.OndaByte.MisterFront.modelos.Cliente;
import com.OndaByte.MisterFront.controladores.OrdenController;
import com.OndaByte.MisterFront.modelos.Orden;
import com.OndaByte.MisterFront.modelos.Turno;
import com.OndaByte.MisterFront.vistas.DatosListener;
import com.OndaByte.MisterFront.vistas.util.Dialogos;
import com.OndaByte.MisterFront.vistas.util.FechaUtils;
import com.OndaByte.MisterFront.vistas.util.FormularioBuilder;
import com.OndaByte.MisterFront.vistas.util.Paginado;
import com.OndaByte.MisterFront.vistas.util.PanelBuscador;
import com.toedter.calendar.JDateChooser;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTextArea;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Frame;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OrdenModal extends JDialog {

    private Orden orden;
    private FormularioBuilder builder;
    private JButton btnGuardar;
    private JButton btnCancelar;

    private List<Cliente> clientes;
    private ClienteController clienteController; // Lucho : para buscar el cliente_id
    private OrdenController ordenController;
    
    private static Logger logger = LogManager.getLogger(OrdenModal.class.getName());

    static{
        if(logger.isDebugEnabled()){
            logger.debug("Init logger en OrdenModal");
        }
    }
    
    public OrdenModal(Frame parent) {
        super(parent, "Crear Orden", true);
        clienteController = ClienteController.getInstance();
        ordenController = OrdenController.getInstance();
        builder = new FormularioBuilder();
        this.orden = null;
        crearForm(600, 500);
    }

    public OrdenModal(Frame parent, Orden orden) {
        super(parent, "Editar Orden", true);
        clienteController = ClienteController.getInstance();
        ordenController = OrdenController.getInstance();
        builder = new FormularioBuilder();
        this.orden = orden;
        crearForm(600, 500);
        cargarDatos(orden, true);
    }


    public OrdenModal(Frame parent, Orden orden, boolean b) {
        super(parent, "Ver Detalles", true);
        clienteController = ClienteController.getInstance();
        ordenController = OrdenController.getInstance();
        builder = new FormularioBuilder();
        this.orden = orden;
        crearForm(600, 500);
        cargarDatos(orden, false);
    }


    private void crearForm(int width, int height) {
        builder.agregarMenuBotones();
        builder.getBtnGuardar().addActionListener(e -> guardar());
        builder.getBtnCancelar().addActionListener(e -> dispose());

        builder.agregarTitulo("lblID", "Orden: ");

        PanelBuscador pb = crearPanelBuscador();
        builder.agregarComponenteCustom("pbCliente", pb, "Buscar Cliente: ");
        //Turno
        builder.agregarComponente("cbDescripcion", "combobox", "Descripción del turno:", new String[]{"Inspección","Reparación","Otro"}, 0);        
        builder.agregarComponente("jcFechaInicio", "calendar", "Fecha Inicio Turno: ", null, 0); // 
//        builder.agregarComponente("jcFechaFin", "calendar", "Fecha Fin estimada: ", null, 0); // 
       // builder.agregarComponente("spHoraInicio", "timespinner", "Hora inicio:", null, 0);
//orden
        builder.agregarComponente("taDescripcion", "textarea", "Descripción: ", null, 5);
        builder.agregarComponente("jcFechaFin", "calendar", "Fecha Fin Estimada: ", null, 0); // esta fecha que onda es para presupuestar o para cuando lo quiere ? 
//        builder.agregarComponente("txtTurnoId", "textfield", "Código del Turno: ", null, 0);

        setContentPane(builder.construir());
        setSize(width, height);
        setLocationRelativeTo(null);
    }

    private PanelBuscador crearPanelBuscador() {
        PanelBuscador pb = new PanelBuscador("Buscar Cliente: ");
        pb.setPlaceholder("Nombre-dni-cuil...");
        pb.configurarEventoBuscar(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                filtrarCliente();
            }

            public void removeUpdate(DocumentEvent e) {
                filtrarCliente();
            }

            public void changedUpdate(DocumentEvent e) {
                filtrarCliente();
            }

            private void filtrarCliente() {
                System.out.println("evento minimanente");
                JTextField b = pb.getBuscador();
                if (b.getText().trim().length() > 2) {
                    clienteController.filtrar(b.getText(),
                            new DatosListener<List<Cliente>>() {
                        @Override
                        public void onSuccess(List<Cliente> datos) {
                            clientes = datos;
                            ArrayList<String> clientesString = new ArrayList<>();
                            for (Cliente c : clientes) {
                                clientesString.add(c.toString());
                            }
                            pb.setDatos(clientesString);
                        }

                        @Override
                        public void onError(String mesajeError) {
                            Dialogos.mostrarError(mesajeError);
                        }

                        @Override
                        public void onSuccess(List<Cliente> datos, Paginado p) {
                     //       throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                        }
                    });
                }
            }

        });
        return pb;
    }

    private void guardar() {
        String errores = validarFormulario();
        if (errores.isEmpty()) {
            Orden nuevop = this.crearOrden();
            DatosListener listener = new DatosListener<String>() {
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
               //     throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                }
            };
            if (this.orden == null) {
              //  ordenController.crearOrden(nuevop,nuevot,listener);
            } else {
                logger.warn("se va a editar");
                nuevop.setId(this.orden.getId());
                ordenController.editarOrden(nuevop,
                    listener);
            }
            this.dispose();

        } else {
            Dialogos.mostrarError(errores);
        }
    }

    private Orden crearOrden() {
        PanelBuscador pb = ((PanelBuscador) builder.getComponente("pbCliente"));
        Cliente c = clientes.get(pb.getSeleccionado());
        String descripcion = ((JTextArea) builder.getComponente("taDescripcion")).getText();

        JDateChooser fechaFin = builder.getComponenteByClass("jcFechaFin");
        LocalDate fecha = fechaFin.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        Orden p = new Orden();
//        p.setCliente_id(c.getId());
//        p.setDescripcion(descripcion);
//        p.setFecha_fin_estimada(FechaUtils.ldToString(fecha));
//        p.setTurno(this.crearTurno());
        return p;
    }

    private String validarFormulario() {
        StringBuilder errores = new StringBuilder("<html>");
        PanelBuscador pb = ((PanelBuscador) builder.getComponente("pbCliente"));
        Cliente clienteSeleccionado = null;
        try {
            clienteSeleccionado = clientes.get(pb.getSeleccionado());
            if (clienteSeleccionado == null) {
                errores.append("- Debe seleccionar un cliente válido.<br>");
                pb.setBorder(BorderFactory.createLineBorder(Color.RED));
            }
        } catch (IndexOutOfBoundsException ioe) {
            errores.append("- Debe seleccionar un cliente válido.<br>");
            pb.setBorder(BorderFactory.createLineBorder(Color.RED));
        }

        JTextArea txtDescripcion = builder.getComponenteByClass("taDescripcion");

        if (txtDescripcion.getText().trim().isEmpty()) {
            errores.append("- El campo Descripcioón es obligatorio. <br>");
            txtDescripcion.setBorder(BorderFactory.createLineBorder(Color.RED));
        }

        JDateChooser fechaFin = builder.getComponenteByClass("jcFechaFin");
        LocalDate fecha = null;
        try {
            fecha = fechaFin.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (!FechaUtils.esFechaPresenteOFuturo(fecha)) {
                errores.append("- Debe elegir una fecha actual o futura.<br>");
                fechaFin.setBorder(BorderFactory.createLineBorder(Color.RED));
            }
        } catch (NullPointerException e) {
            errores.append("- Debe elegir una fecha actual o futura.<br>");
            fechaFin.setBorder(BorderFactory.createLineBorder(Color.RED));
        }
        
        if (errores.length() > 6) {
            errores.append("</html>");
            return errores.toString();
        } else {
            return "";
        }
    }

    /**
     * Trae los datos del cliente
     *
     * @param orden
     * @param editable
     */
    private void cargarDatos(Orden orden, boolean editable) {
//        PanelBuscador pb = ((PanelBuscador) builder.getComponente("pbCliente"));
//        pb.getBuscador().setEditable(false);
//        pb.getBuscador().setEnabled(false);
////        pb.getBuscador().setVisible(false);
//        pb.getCombo().setEditable(false);
//        clienteController.getClienteById(orden.getCliente_id(),
//                new DatosListener<Cliente>(){
//                    @Override
//                    public void onSuccess(Cliente datos){
//                        clientes = new ArrayList<>();
//                        clientes.add(datos);
//                        ArrayList<String> clientesString = new ArrayList<>();
//                        for (Cliente c : clientes) {
//                            clientesString.add(c.toString());
//                        }
//                        pb.setDatos(clientesString); //Stetteo una lista de 1 y automaticamente se settea el único como seleccionado.    
//                    }
//
//                    @Override
//                    public void onError(String m){
//                        Dialogos.mostrarError(m);
//                    }
//
//            @Override
//            public void onSuccess(Cliente datos, Paginado p) {
//            //    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
//            }
//                });
//        Cliente cte = clientes.get(pb.getSeleccionado()); no hace falta

        (builder.getComponente("cbDescripcion")).setVisible(false);
        (builder.getComponente("jcFechaInicio")).setVisible(false);
        
        ((JLabel) builder.getComponente("lblID")).setText("Orden ID: " + orden.getId());
        ((JTextArea) builder.getComponente("taDescripcion")).setText(orden.getDescripcion());
        
        ((JLabel) builder.getComponente("lblID")).setText("Orden ID: " + orden.getId());
        ((JTextArea) builder.getComponente("taDescripcion")).setText(orden.getDescripcion());
        
        //Fecha Handler
        LocalDate ld = FechaUtils.stringTold(orden.getFecha_fin().split(" ")[0]); // solo la fecha sin hh:mm
        Date d = Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());    // Parseo no deprecated
        ((JDateChooser) builder.getComponente("jcFechaFin")).setDate(d); // Setteado
        //End fecha handler
        builder.agregarComponente("txtEstado", "textfield", "Estado del orden: ", null, 0);
        JTextField txtEstado = builder.getComponenteByClass("txtEstado");
        txtEstado.setText(orden.getEstado_orden());
        txtEstado.setEditable(false);

        if (!editable) {
            builder.getBtnGuardar().setVisible(false);
        }

        this.getContentPane().repaint();
    }
}
