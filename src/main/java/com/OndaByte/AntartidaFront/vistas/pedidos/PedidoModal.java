package com.OndaByte.AntartidaFront.vistas.pedidos;

import com.OndaByte.AntartidaFront.controladores.ClienteController;
import com.OndaByte.AntartidaFront.modelos.Cliente;
import com.OndaByte.AntartidaFront.modelos.Pedido;
import com.OndaByte.AntartidaFront.controladores.PedidoController;
import com.OndaByte.AntartidaFront.vistas.DatosListener;
import com.OndaByte.AntartidaFront.vistas.util.Dialogos;
import com.OndaByte.AntartidaFront.vistas.util.FechaUtils;
import com.OndaByte.AntartidaFront.vistas.util.FormularioBuilder;
import com.OndaByte.AntartidaFront.vistas.util.Paginado;
import com.OndaByte.AntartidaFront.vistas.util.PanelBuscador;
import com.OndaByte.AntartidaFront.vistas.clientes.ClienteModal;
import com.OndaByte.AntartidaFront.vistas.MiFrame;
import com.toedter.calendar.JDateChooser;
import javax.swing.JTextField;
import javax.swing.JDialog;
import javax.swing.JTextArea;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Frame;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PedidoModal extends JDialog {

    private Pedido pedido;
    private FormularioBuilder builder;

    private List<Cliente> clientes;
    private ClienteController clienteController; // Lucho : para buscar el cliente_id
    private PedidoController pedidoController;
  
    private String filtro;
    private Integer pagina;

  JButton btnNuevoCliente;
    
    private static Logger logger = LogManager.getLogger(PedidoModal.class.getName());

    static{
        if(logger.isDebugEnabled()){
            logger.debug("Init logger en PedidoModal");
        }
    }
    
    public PedidoModal(Frame parent) {
        super(parent, "Crear Pedido", true);
        clienteController = ClienteController.getInstance();
        pedidoController = PedidoController.getInstance();
        builder = new FormularioBuilder();
        this.pedido = null;
        crearForm(600, 440);
    }

    public PedidoModal(Frame parent, Pedido pedido) {
        super(parent, "Editar Pedido", true);
        clienteController = ClienteController.getInstance();
        pedidoController = PedidoController.getInstance();
        builder = new FormularioBuilder();
        this.pedido = pedido;
        crearForm(600, 440);
        cargarDatos(pedido, true);
    }

    public PedidoModal(Frame parent, Pedido pedido, boolean b) {
        super(parent, "Ver Detalles", true);
        clienteController = ClienteController.getInstance();
        pedidoController = PedidoController.getInstance();
        builder = new FormularioBuilder();
        this.pedido = pedido;
        crearForm(600, 440);
        cargarDatos(pedido, false);
    }

    private void crearForm(int width, int height) {
        builder.agregarMenuBotones();
        builder.getBtnGuardar().addActionListener(e -> guardar());
        builder.getBtnCancelar().addActionListener(e -> dispose());

        builder.agregarTitulo("lblID", "Pedido: ");

        PanelBuscador pb = crearPanelBuscador();
        builder.agregarComponenteCustom("pbCliente", pb, "Buscar Cliente: *");

        btnNuevoCliente = new JButton("Nuevo Cliente");
        btnNuevoCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClienteModal modal = new ClienteModal(MiFrame.getInstance());
                modal.setVisible(true); // bloquea el thread hasta que es cerrado
                filtro="";
                pagina = 1;
            }
        });
        builder.agregarComponenteCustom("btnNuevoCliente", btnNuevoCliente , "");
        //Turno
//        builder.agregarComponente("cbDescripcion", "combobox", "Descripción del turno:", new String[]{"Inspección","Reparación","Otro"}, 0);        
//        builder.agregarComponente("jcFechaInicio", "calendar", "Fecha Inicio Turno: ", null, 0); // 
//        builder.agregarComponente("jcFechaFin", "calendar", "Fecha Fin estimada: ", null, 0); // 
       // builder.agregarComponente("spHoraInicio", "timespinner", "Hora inicio:", null, 0);
//pedido
        builder.agregarComponente("taDescripcion", "textarea", "Descripción: *", null, 5);
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
            Pedido nuevop = this.crearPedido();
        //    Turno nuevot = this.crearTurno();
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
            if (this.pedido == null) {
                pedidoController.crearPedido(nuevop,listener);
            } else {
                logger.warn("se va a editar");
                nuevop.setId(this.pedido.getId());
                pedidoController.editarPedido(nuevop,listener);
            }
            this.dispose();

        } else {
            Dialogos.mostrarError(errores);
        }
    }

    private Pedido crearPedido() {
      
        JLabel lblError = builder.getComponenteByClass("lblError");
        PanelBuscador pb = ((PanelBuscador) builder.getComponente("pbCliente"));
        Cliente c = clientes.get(pb.getSeleccionado());
        String descripcion = ((JTextArea) builder.getComponente("taDescripcion")).getText();

        JDateChooser fechaFin = builder.getComponenteByClass("jcFechaFin");
        LocalDate fecha = null;
        if(fechaFin.getDate() != null)
            fecha = fechaFin.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        Pedido p = new Pedido();
        p.setCliente_id(c.getId());
        p.setDescripcion(descripcion);
        p.setFecha_fin_estimada(FechaUtils.ldToString(fecha));
        return p;
    }
    
//    private Turno crearTurno() {
//        if(this.pedido!=null)
//            return new Turno();
//        String descripcion = ((JComboBox) builder.getComponente("cbDescripcion")).getSelectedItem().toString();
//        JDateChooser fechaFin = builder.getComponenteByClass("jcFechaInicio");
//        LocalDate fecha = fechaFin.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//        Turno t = new Turno();
//        t.setDescripcion(descripcion);
//        t.setFechaInicio(FechaUtils.ldToString(fecha));
//        return t;
//    }

    private String validarFormulario() {
        StringBuilder errores = new StringBuilder("<html>");
        PanelBuscador pb = ((PanelBuscador) builder.getComponente("pbCliente"));
        Cliente clienteSeleccionado = null;
        try {
            clienteSeleccionado = clientes.get(pb.getSeleccionado());
            if (clienteSeleccionado == null) {
                errores.append("* Debe seleccionar un cliente válido.<br>");
                pb.setBorder(BorderFactory.createLineBorder(Color.RED));
            }
        } catch (IndexOutOfBoundsException ioe) {
            errores.append("* Debe seleccionar un cliente válido.<br>");
            pb.setBorder(BorderFactory.createLineBorder(Color.RED));
        }

        JTextArea txtDescripcion = builder.getComponenteByClass("taDescripcion");
        if (txtDescripcion.getText().trim().isEmpty()) {
            errores.append("* El campo Descripción es obligatorio. <br>");
            txtDescripcion.setBorder(BorderFactory.createLineBorder(Color.RED));
        }
        
        JDateChooser fechaFin = builder.getComponenteByClass("jcFechaFin");
        LocalDate fecha = null;
        if(fechaFin.getDate() != null){
          fecha = fechaFin.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
          if(!FechaUtils.esFechaPresenteOFuturo(fecha)){
            errores.append("* La fecha de fin estimada no puede ser en el pasado.");
            fechaFin.setBorder(BorderFactory.createLineBorder(Color.RED));
          }
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
     * @param pedido
     * @param editable
     */
    private void cargarDatos(Pedido pedido, boolean editable) {
        PanelBuscador pb = ((PanelBuscador) builder.getComponente("pbCliente"));
        pb.getBuscador().setEditable(false);
        pb.getBuscador().setEnabled(false);
//        pb.getBuscador().setVisible(false);
        pb.getCombo().setEditable(false);
        clienteController.getClienteById(pedido.getCliente_id(),
                new DatosListener<Cliente>(){
                    @Override
                    public void onSuccess(Cliente datos){
                        clientes = new ArrayList<>();
                        clientes.add(datos);
                        ArrayList<String> clientesString = new ArrayList<>();
                        for (Cliente c : clientes) {
                            clientesString.add(c.toString());
                        }
                        pb.setDatos(clientesString); //Stetteo una lista de 1 y automaticamente se settea el único como seleccionado.    
                    }

                    @Override
                    public void onError(String m){
                        Dialogos.mostrarError(m);
                    }

                    @Override
                    public void onSuccess(Cliente datos, Paginado p) {
                    //    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                    }
                });
//        Cliente cte = clientes.get(pb.getSeleccionado()); no hace falta

//        (builder.getComponente("cbDescripcion")).setVisible(false);
//        (builder.getComponente("jcFechaInicio")).setVisible(false);
        builder.getComponente("btnNuevoCliente").setEnabled(false);

        ((JLabel) builder.getComponente("lblID")).setText("Pedido ID: " + pedido.getId());
        ((JTextArea) builder.getComponente("taDescripcion")).setText(pedido.getDescripcion());
        
        ((JLabel) builder.getComponente("lblID")).setText("Pedido ID: " + pedido.getId());
        ((JTextArea) builder.getComponente("taDescripcion")).setText(pedido.getDescripcion());

        (builder.getComponente("lblID")).setEnabled(editable);
        (builder.getComponente("taDescripcion")).setEnabled(editable);

        //Fecha Handler
        LocalDate ld = null;
        if(!pedido.getFecha_fin_estimada().equals("No disponible")){
            ld = FechaUtils.stringTold(pedido.getFecha_fin_estimada().split(" ")[0]); // solo la fecha sin hh:mm
            Date d = Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());    // Parseo no deprecated
            ((JDateChooser) builder.getComponente("jcFechaFin")).setDate(d); // Setteado
        }//End fecha handler
        builder.agregarComponente("txtEstado", "textfield", "Estado del pedido: ", null, 0);
        JTextField txtEstado = builder.getComponenteByClass("txtEstado");
        txtEstado.setText(pedido.getEstado_pedido());
        txtEstado.setEditable(false);

        if (!editable) {
            builder.getBtnGuardar().setVisible(false);
        }

        this.getContentPane().repaint();
    }
}
