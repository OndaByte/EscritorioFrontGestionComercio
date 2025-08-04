
package com.OndaByte.AntartidaFront.vistas.turnos;

import com.OndaByte.AntartidaFront.controladores.TurnoController;
import com.OndaByte.AntartidaFront.modelos.Orden;
import com.OndaByte.AntartidaFront.modelos.Pedido;
import com.OndaByte.AntartidaFront.modelos.Turno;
import com.OndaByte.AntartidaFront.vistas.DatosListener;
import com.OndaByte.AntartidaFront.vistas.util.Dialogos;
import com.OndaByte.AntartidaFront.vistas.util.FechaUtils;
import com.OndaByte.AntartidaFront.vistas.util.FormularioBuilder;
import com.OndaByte.AntartidaFront.vistas.util.Paginado;
import com.toedter.calendar.JDateChooser;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Frame;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.swing.JComboBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TurnoModal extends JDialog {

    private Turno turno;
    private Pedido p = null;
    private Orden o = null;
    private FormularioBuilder builder;
    private JButton btnGuardar;
    private JButton btnCancelar;

    private List<Turno> clientes;
    private TurnoController turnoController; 

    
    private static Logger logger = LogManager.getLogger(TurnoModal.class.getName());

    static{
        if(logger.isDebugEnabled()){
            logger.debug("Init logger en TurnoModal");
        }
    }
    /**
     * Crear turno para asignarlo a Pedido. 
     */
    public TurnoModal(Frame parent,Pedido p) {
        super(parent, "Crear Turno para inspección de Pedido", true);
        turnoController = TurnoController.getInstance();
        builder = new FormularioBuilder();
        this.turno = null;
        this.p = p;
        crearForm(600, 440);
    }
    public TurnoModal(Frame parent,Orden o) {
        super(parent, "Crear Turno para Orden", true);
        turnoController = TurnoController.getInstance();
        builder = new FormularioBuilder();
        this.turno = null;
        this.o=o;
        crearForm(600, 440);
    }
    
    /**
     * Editar Turno.
     */
    public TurnoModal(Frame parent, Turno turno) {
        super(parent, "Editar Turno", true);
        turnoController = TurnoController.getInstance();
        builder = new FormularioBuilder();
        this.turno = turno;
        crearForm(600, 440);
        cargarDatos(turno, true);
    }
    
    private String getStringTipo(Turno t){
        if(t.getTipo().equals("INSPECCION")){
            return "Inspección";
        }else if(t.getTipo().equals("REPARACION")){
            return "Reparación";
        }else
            return  "Mantenimiento";
    }
    
    private void crearForm(int width, int height) {
        builder.agregarMenuBotones();
        builder.getBtnGuardar().addActionListener(e -> guardar());
        builder.getBtnCancelar().addActionListener(e -> dispose());

        builder.agregarTitulo("lblID", "Turno: ");

        //Turno
        if(this.turno == null){
          if(this.p != null)
            builder.agregarComponente("cbTipo", "combobox", "Descripción del turno: *", new String[]{"Inspección"}, 0);
          else
            builder.agregarComponente("cbTipo", "combobox", "Descripción del turno: *", new String[]{"Reparación", "Mantenimiento"}, 0);
        }
        else {
            builder.agregarComponente("cbTipo", "combobox", "Descripción del turno: *", new String[]{this.getStringTipo(this.turno)}, 0);
        }
        builder.agregarComponente("jcFechaInicio", "calendar", "Fecha Inicio Turno : *", null, 0); // 
        builder.agregarComponente("jcFechaFin", "calendar", "Fecha Fin : *", null, 0); // Para finalizar el turno
        // builder.agregarComponente("cdPrioridad", "combobox", "Prioridad:", new Integer[]{0,1,2,3,4,5}, 0);

        setContentPane(builder.construir());
        setSize(width, height);
        setLocationRelativeTo(null);
    }

    private void guardar() {
        String errores = validarFormulario();
        if (errores.isEmpty()) {
            Turno nuevot = this.crearTurno();
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
            if (this.turno == null) {
                if(this.p!=null)            
                    turnoController.crearTurnoPedido(nuevot,p.getId(),listener);
                else // orden != null
                    turnoController.crearTurnoOrden(nuevot,o.getId(),listener);
            } else {
                logger.warn("se va a editar");
                nuevot.setId(this.turno.getId());
                turnoController.editarTurno(nuevot,
                    listener);
            }
            this.dispose();

        } else {
            Dialogos.mostrarError(errores);
        }
    }
    
    private Turno crearTurno() {
        String tipo = ((JComboBox) builder.getComponente("cbTipo")).getSelectedItem().toString();
        JDateChooser fechaInicio = builder.getComponenteByClass("jcFechaInicio");
        LocalDate fechaI = FechaUtils.dateToLocalDate(fechaInicio.getDate());                
        JDateChooser fechaFin = builder.getComponenteByClass("jcFechaFin");
        LocalDate fechaF = null;
        if(fechaFin.getDate()!=null){
          fechaF = FechaUtils.dateToLocalDate(fechaFin.getDate());
        }
       // String prioridad = ((JComboBox) builder.getComponente("cbPrioridad")).getSelectedItem().toString();

        Turno t = new Turno();
        if(tipo.contains("nspec"))
            t.setTipo("INSPECCION");
        else if(tipo.contains("epara"))
            t.setTipo("REPARACION");
        else
            t.setTipo("MANTENIMIENTO");
        
        t.setFechaInicio(FechaUtils.ldToString(fechaI));
        if(fechaFin.getDate()!=null){
          t.setFechaFinE(FechaUtils.ldToString(fechaF));
        }
       // t.setPrioridad(Integer.valueOf(prioridad));
        
        return t;
    }

    private String validarFormulario() {
        StringBuilder errores = new StringBuilder("<html>");

        JDateChooser fechaInicio = builder.getComponenteByClass("jcFechaInicio");
        LocalDate fecha1 = null;
        try {
            fecha1 = FechaUtils.dateToLocalDate(fechaInicio.getDate());
//            if (!FechaUtils.esFechaPresenteOFuturo(fecha1)) {
//                errores.append("* No puede elegir una fecha pasada.<br>");
//                fechaInicio.setBorder(BorderFactory.createLineBorder(Color.RED));
//            }
        } catch (NullPointerException e) {
            errores.append("* Debe elegir una fecha.<br>");
            fechaInicio.setBorder(BorderFactory.createLineBorder(Color.RED));
        }

        JDateChooser fechaFin = builder.getComponenteByClass("jcFechaFin");
        LocalDate fecha2 = null;

        try {
            fecha2 = FechaUtils.dateToLocalDate(fechaFin.getDate());
//            if (!FechaUtils.esFechaPresenteOFuturo(fecha2)) {
//                errores.append("* No puede elegir una fecha pasada.<br>");
//                fechaFin.setBorder(BorderFactory.createLineBorder(Color.RED));
//            }
            if (fecha1.isAfter(fecha2)) {
                errores.append("* La fecha de Finalización del turno no puede ser anterior a la de inicio.<br>");
                fechaFin.setBorder(BorderFactory.createLineBorder(Color.RED));
            }
        } catch (NullPointerException e) {
            errores.append("* Error en fecha.<br>");
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
     * Trae los datos del Turno
     *
     * @param turno
     * @param editable
     */
    private void cargarDatos(Turno turno, boolean editable) {
        ((JLabel) builder.getComponente("lblID")).setText("Turno ID: " + turno.getId());
//        ((JComboBox)builder.getComponente("cbTipo")).setEditable(editable);
        ((JComboBox)builder.getComponente("cbTipo")).setSelectedItem(this.getStringTipo(turno));
       
        Date di = FechaUtils.stringToDate(turno.getFechaInicio().split(" ")[0]); // string a localdate y localdate a date 
        ((JDateChooser) builder.getComponente("jcFechaInicio")).setDate(di); 
        
        Date df = FechaUtils.stringToDate(turno.getFechaFinE().split(" ")[0]); // string a localdate y localdate a date
        ((JDateChooser) builder.getComponente("jcFechaFin")).setDate(df); 
        
        // ((JComboBox)builder.getComponente("cbPrioridad")).setSelectedItem(turno.getPrioridad());

//        builder.agregarComponente("txtEstado", "textfield", "Estado del turno: ", null, 0);
//        JTextField txtEstado = builder.getComponenteByClass("txtEstado");
//        txtEstado.setText(turno.getEstado());
//        txtEstado.setEditable(false);

        if (!editable) {
            builder.getBtnGuardar().setVisible(false);
        }

        this.getContentPane().repaint();
    }
}
