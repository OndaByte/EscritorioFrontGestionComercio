package com.OndaByte.AntartidaFront.vistas.empleados;

import com.OndaByte.AntartidaFront.controladores.EmpleadoController;
import com.OndaByte.AntartidaFront.modelos.Empleado;
import com.OndaByte.AntartidaFront.vistas.DatosListener;
import com.OndaByte.AntartidaFront.vistas.util.Dialogos;
import com.OndaByte.AntartidaFront.vistas.util.FormularioBuilder;
import com.OndaByte.AntartidaFront.vistas.util.Paginado;

import java.awt.Color;
import java.awt.Frame;
import javax.swing.JDialog;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.JLabel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EmpleadoModal extends JDialog {

    //Modelo
    private Empleado empleado;
    private EmpleadoController empleadoController;
    //Form
    FormularioBuilder builder;

    private static Logger logger = LogManager.getLogger(EmpleadoModal.class.getName());

    static {
        if (logger.isDebugEnabled()) {
            logger.debug("Init logger en EmpleadoModal");
        }
    }

    public EmpleadoModal(Frame parent) {
        super(parent, "Crear Empleado", true);
        empleadoController = EmpleadoController.getInstance();
        builder = new FormularioBuilder();
        crearForm(600,300);
    }

    public EmpleadoModal(Frame parent, Empleado empleado) {
        super(parent, "Editar Empleado", true);
        //setIconImage(MiFrame.getInstance().getIconImage());
        empleadoController = EmpleadoController.getInstance();
        builder = new FormularioBuilder();

        this.empleado = empleado;
        crearForm(600,300);
        cargarDatos(empleado, true);
    }

    //
//    public EmpleadoModal(Frame parent, Empleado empleado, boolean soloLectura) {
//        super(parent, "Ver Empleado", true);
//        builder = new FormularioBuilder();
//        crearForm();
//        cargarDatos(empleado, false);
//        setCamposSoloLectura();
//        this.setVisible(true);
//    }
//
    private void crearForm(int width, int height) {
        builder.agregarMenuBotones();
        builder.getBtnGuardar().addActionListener(e -> guardar());
        builder.getBtnCancelar().addActionListener(e -> dispose());

        builder.agregarTitulo("lblID", "Empleado: ");
        builder.agregarComponente("txtNombre", "textfield", "Nombre y Apellido: * ", null, 0);
        builder.agregarComponente("txtDni", "textfield", "DNI: * ", null, 0);
        builder.agregarComponente("txtTelefono", "textfield", "Teléfono: *", null, 0);
        builder.agregarComponente("txtDireccion", "textfield", "Dirección: *", null, 0);

        setContentPane(builder.construir());
        
        setSize(width, height);
        setLocationRelativeTo(null);
    }

    private void guardar() {
        String errores = validarFormulario();
        if (errores.isEmpty()) {
            Empleado nuevo = this.crearEmpleado();
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
            if (this.empleado == null){
                if(empleadoController.crearEmpleado(nuevo, listener)){this.dispose();}
            } else {
                nuevo.setId(this.empleado.getId());
                if(empleadoController.editarEmpleado(nuevo, listener)){this.dispose();}
            }
        } else {
            Dialogos.mostrarError(errores);
        }
    }

    private Empleado crearEmpleado(){
        String nombre = ((JTextField) builder.getComponente("txtNombre")).getText();
        String dni = ((JTextField) builder.getComponente("txtDni")).getText();
        String telefono = ((JTextField) builder.getComponente("txtTelefono")).getText();
        String direccion = ((JTextField) builder.getComponente("txtDireccion")).getText();
        Empleado e = new Empleado();
        e.setNombre(nombre);
        e.setDni(dni);
        e.setTelefono(telefono);
        e.setDireccion(direccion);
        return e;
    }

    private String validarFormulario() {
        StringBuilder errores = new StringBuilder("<html>");

        JTextField txtNombre = builder.getComponenteByClass("txtNombre");
        if (txtNombre.getText().trim().isEmpty()) {
            errores.append("- El campo Nombre es obligatorio.<br>");
            txtNombre.setBorder(BorderFactory.createLineBorder(Color.RED));
        }

        JTextField txtDni = builder.getComponenteByClass("txtDni");
        if (!txtDni.getText().trim().matches("\\d+")){
            errores.append("- El campo DNI es obligatorio y debe contener solo números.<br>");
            txtDni.setBorder(BorderFactory.createLineBorder(Color.RED));
        }

        JTextField txtTelefono = builder.getComponenteByClass("txtTelefono");
        if (!txtTelefono.getText().trim().matches("\\d+")) {
            errores.append("- El campo Telefono es obligatorio y debe contener solo números.<br>");
            txtTelefono.setBorder(BorderFactory.createLineBorder(Color.RED));
        }

        JTextField txtDireccion = builder.getComponenteByClass("txtDireccion");
        if (txtDireccion.getText().trim().isEmpty()) {
            errores.append("- El campo Direccion es obligatorio.<br>");
            txtDireccion.setBorder(BorderFactory.createLineBorder(Color.RED));
        }

        if (errores.length() > 6 ) {
            errores.append("</html>");
            return errores.toString();
        }else{
            return "";
        }
    }

    private void cargarDatos(Empleado empleado, boolean editable) {

        ((JLabel) builder.getComponente("lblID")).setText("Empleado con ID: " + empleado.getId());
        ((JTextField) builder.getComponente("txtNombre")).setText(empleado.getNombre());
        ((JTextField) builder.getComponente("txtDni")).setText(empleado.getDni());
        ((JTextField) builder.getComponente("txtDni")).setEditable(false);
        ((JTextField) builder.getComponente("txtTelefono")).setText(empleado.getTelefono());
        ((JTextField) builder.getComponente("txtDireccion")).setText(empleado.getDireccion());

        if (!editable) {
            builder.getBtnGuardar().setVisible(false);
        }
        this.getContentPane().repaint();
    }

}
