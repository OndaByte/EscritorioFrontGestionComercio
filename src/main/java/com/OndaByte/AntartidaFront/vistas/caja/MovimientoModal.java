package com.OndaByte.AntartidaFront.vistas.caja;

import com.OndaByte.AntartidaFront.controladores.MovimientoController;
import com.OndaByte.AntartidaFront.modelos.Movimiento;
import com.OndaByte.AntartidaFront.sesion.SesionController;
import com.OndaByte.AntartidaFront.vistas.DatosListener;
import com.OndaByte.AntartidaFront.vistas.util.Dialogos;
import com.OndaByte.AntartidaFront.vistas.util.FormularioBuilder;
import com.OndaByte.AntartidaFront.vistas.util.Paginado;

import java.awt.Color;
import java.awt.Frame;
import javax.swing.JDialog;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author luciano
 */
public class MovimientoModal extends JDialog {

    //Modelo
    private Movimiento mov;
    private MovimientoController movimientoController;
    //Form
    FormularioBuilder builder;

    private static Logger logger = LogManager.getLogger(MovimientoModal.class.getName());

    static {
        if (logger.isDebugEnabled()) {
            logger.debug("Init logger en MovimientoModal");
        }
    }

    public MovimientoModal(Frame parent) {
        super(parent, "Crear Movimiento", true);
        movimientoController = MovimientoController.getInstance();
        builder = new FormularioBuilder();
        crearForm(600,300);
    }

    public MovimientoModal(Frame parent, Movimiento movimiento) {
        super(parent, "Editar Movimiento", true);
        //setIconImage(MiFrame.getInstance().getIconImage());
        movimientoController = MovimientoController.getInstance();
        builder = new FormularioBuilder();

        this.mov = movimiento;
        crearForm(600,300);
        cargarDatos(movimiento, true);
    }


    private void crearForm(int width, int height) {
        builder.agregarMenuBotones();
        builder.getBtnGuardar().addActionListener(e -> guardar());
        builder.getBtnCancelar().addActionListener(e -> dispose());

        builder.agregarTitulo("lblID", "Movimiento:"); 
        builder.agregarComponente("comboTipo", "combobox", "Tipo de Movimiento: *", new String[]{"Ingreso", "Egreso"}, 0);
        builder.agregarComponente("txtMonto", "textfield", "Monto: *", null, 0);
        builder.agregarComponente("txtDescripcion", "textarea", "Descripción:", null, 3);

        setContentPane(builder.construir());
        setSize(width, height);
        setLocationRelativeTo(null);
    }

    private void guardar() {
        String errores = validarFormulario();
        if (errores.isEmpty()) {
            Movimiento nuevo = this.crearMovimiento();
            DatosListener<String> listener = new DatosListener<String>() {
                @Override
                public void onSuccess(String resultado) {
                    Dialogos.mostrarExito(resultado);
                }

                @Override
                public void onError(String mensajeError) {
                    Dialogos.mostrarError(mensajeError);
                }

                @Override
                public void onSuccess(String datos, Paginado p) {
              //      throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                }
            };
            if (this.mov == null){
                if(movimientoController.crearMovimiento(nuevo, listener)){this.dispose();}
            } else {
                nuevo.setId(this.mov.getId());
                if(movimientoController.editarMovimiento(nuevo, listener)){this.dispose();}
            }
        } else {
            Dialogos.mostrarError(errores);
        }
    }

private Movimiento crearMovimiento() {
    String tipo = (String) ((JComboBox<String>) builder.getComponente("comboTipo")).getSelectedItem();
    String montoText = ((JTextField) builder.getComponente("txtMonto")).getText();
    String descripcion = ((JTextArea) builder.getComponente("txtDescripcion")).getText();

    Movimiento m = new Movimiento();
    m.setSesion_caja_id(SesionController.getInstance().getSesionCaja().getId());
    m.setTipo_mov(tipo);
    m.setTotal(Float.parseFloat(montoText));
    m.setDescripcion(descripcion);

    return m;
}

    private String validarFormulario() {
        StringBuilder errores = new StringBuilder("<html>");

        JTextArea txtDescripcion = builder.getComponenteByClass("txtDescripcion");
        if (txtDescripcion.getText().trim().isEmpty()) {
            errores.append("- El campo Descripción es obligatorio.<br>");
            txtDescripcion.setBorder(BorderFactory.createLineBorder(Color.RED));
        }
        
        JTextField txtMonto = builder.getComponenteByClass("txtMonto");
        if (!txtMonto.getText().trim().matches("\\d+(\\.\\d{1,2})?")) {
            errores.append("- El campo Monto es obligatorio y debe ser un número válido.<br>");
            txtMonto.setBorder(BorderFactory.createLineBorder(Color.RED));
        }
 
        if (errores.length() > 6 ) {
            errores.append("</html>");
            return errores.toString();
        }else{
            return "";
        }
    }

    private void cargarDatos(Movimiento movimiento, boolean editable) {
        ((JLabel) builder.getComponente("lblID")).setText("Movimiento ID: " + movimiento.getId());
        ((JComboBox<?>) builder.getComponente("comboTipo")).setSelectedItem(movimiento.getTipo_mov());
        ((JTextField) builder.getComponente("txtMonto")).setText(movimiento.getTotal() + "");
        ((JTextArea) builder.getComponente("txtDescripcion")).setText(movimiento.getDescripcion());

        if (!editable) {
            builder.getBtnGuardar().setVisible(false);
        }

        this.getContentPane().repaint();
    }

}
