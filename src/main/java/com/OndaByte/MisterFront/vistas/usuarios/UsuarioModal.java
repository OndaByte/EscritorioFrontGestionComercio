package com.OndaByte.MisterFront.vistas.usuarios;

import com.OndaByte.MisterFront.controladores.UsuarioController;
import com.OndaByte.MisterFront.modelos.Usuario;
import com.OndaByte.MisterFront.vistas.DatosListener;
import com.OndaByte.MisterFront.vistas.MiFrame;
import com.OndaByte.MisterFront.vistas.util.Dialogos;
import com.OndaByte.MisterFront.vistas.util.FormularioBuilder;
import com.OndaByte.MisterFront.vistas.util.Paginado;

import java.awt.Color;
import java.awt.Frame;
import javax.swing.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UsuarioModal extends JDialog{

    //Modelo
    private Usuario usuario;
    private UsuarioController usuarioController;
    //Form
    FormularioBuilder builder;

    private static Logger logger = LogManager.getLogger(UsuarioModal.class.getName());

    static {
        if (logger.isDebugEnabled()) {
            logger.debug("Init logger en UsuarioModal");
        }
    }

    public UsuarioModal(Frame parent) {
        super(parent, "Crear Usuario", true);
        usuarioController = UsuarioController.getInstance();
        builder = new FormularioBuilder();
        crearForm(600,300);
    }

    public UsuarioModal(Frame parent, Usuario usuario) {
        super(parent, "Editar Usuario", true);
        usuarioController = UsuarioController.getInstance();
        builder = new FormularioBuilder();

        this.usuario = usuario;
        crearForm(600,300);
        cargarDatos(usuario, true);
    }

    public UsuarioModal(Frame parent, boolean cambiarPass){
        super(parent, "Cambiar Contraseña", cambiarPass);
        cambiarPasswordModal();
    }

    //
//    public UsuarioModal(Frame parent, Usuario usuario, boolean soloLectura) {
//        super(parent, "Ver Usuario", true);
//        builder = new FormularioBuilder();
//        crearForm();
//        cargarDatos(usuario, false);
//        setCamposSoloLectura();
//        this.setVisible(true);
//    }
//
    private void crearForm(int width, int height) {
        builder.agregarMenuBotones();
        builder.getBtnGuardar().addActionListener(e -> guardar());
        builder.getBtnCancelar().addActionListener(e -> dispose());

        builder.agregarTitulo("lblID", "Usuario: ");
        builder.agregarComponente("txtUsuario", "textfield", "Usuario: * ", null, 0);
        builder.agregarComponente("txtContra", "textfield", "Contraseña: * ", null, 0);
        builder.agregarComponente("cmbRol", "combobox", "Rol: ", new String[]{"","USUARIO","EMPLEADO", "ADMIN"}, 0);

        setContentPane(builder.construir());
        
        setSize(width, height);
        setLocationRelativeTo(null);
    }

    private void guardar() {
        String errores = validarFormulario();
        if (errores.isEmpty()) {
            Usuario nuevo = this.crearUsuario();
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
            boolean resultado = false;
            if (this.usuario == null){
                if(usuarioController.crearUsuario(nuevo, listener)){resultado = true;}
            } else {
                nuevo.setId(this.usuario.getId());
                if(usuarioController.editarUsuario(nuevo, listener)){resultado = true;}
                int rol = ((JComboBox)builder.getComponente("cmbRol")).getSelectedIndex();
                System.out.println(rol);
                if(rol>0){
                    String nombreRol = "";
                    switch (rol){
                        case 1:
                            nombreRol = "USUARIO";
                            break;
                        case 2:
                            nombreRol = "EMPLEADO";
                            break;
                        case 3:
                            nombreRol = "ADMIN";
                            break;
                    }
                    if(usuarioController.editarUsuario(nuevo, nombreRol, listener)){resultado = true;}
                }
            }
            if(resultado){this.dispose();}
        } else {
            Dialogos.mostrarError(errores);
        }
    }

    private Usuario crearUsuario(){
        String usuario = ((JTextField) builder.getComponente("txtUsuario")).getText();
        String contra = ((JTextField) builder.getComponente("txtContra")).getText();
        Usuario u = new Usuario();
        u.setUsuario(usuario);
        u.setContra(contra);
        return u;
    }

    private String validarFormulario() {
        StringBuilder errores = new StringBuilder("<html>");


        JTextField txtUsuario = builder.getComponenteByClass("txtUsuario");
        if (txtUsuario.getText().trim().isEmpty()){
            errores.append("- El campo Usuario es obligatorio.<br>");
            txtUsuario.setBorder(BorderFactory.createLineBorder(Color.RED));
        }

        JTextField txtContra = builder.getComponenteByClass("txtContra");
        if (txtContra.getText().trim().isEmpty() && txtContra.getText().length()>5){
            errores.append("- El campo Contraseña es obligatorio y debe contener almenos 6 caracteres.<br>");
            txtContra.setBorder(BorderFactory.createLineBorder(Color.RED));
        }

        if (errores.length() > 6 ) {
            errores.append("</html>");
            return errores.toString();
        }else{
            return "";
        }
    }

    private void cargarDatos(Usuario usuario, boolean editable) {

        ((JLabel) builder.getComponente("lblID")).setText("Usuario con ID: " + usuario.getId());
        ((JTextField) builder.getComponente("txtUsuario")).setText(usuario.getUsuario());
        ((JTextField) builder.getComponente("txtContra")).setText("");

        if (!editable) {
            builder.getBtnGuardar().setVisible(false);
        }
        this.getContentPane().repaint();
    }


    public void cambiarPasswordModal() {
        usuarioController = UsuarioController.getInstance();
        builder = new FormularioBuilder();
        crearForm2(600,300);
    }

    private void crearForm2(int width, int height) {
        builder.agregarMenuBotones();
        builder.getBtnGuardar().addActionListener(e -> guardar2());
        builder.getBtnCancelar().addActionListener(e -> dispose());

        builder.agregarTitulo("lblID", "Usuario: ");
        builder.agregarComponente("passActual", "passwordfield", "Contraseña actual: * ", null, 0);
        builder.agregarComponente("passNueva", "passwordfield", "Nueva contraseña: * ", null, 0);
        builder.agregarComponente("passConfirmar", "passwordfield", "Confirmar nueva contraseña: * ", null, 0);
        setContentPane(builder.construir());

        setSize(width, height);
        setLocationRelativeTo(null);
    }

    private void guardar2() {
        String errores = validarFormulario2();
        if (errores.isEmpty()) {
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
            JPasswordField passActual = builder.getComponenteByClass("passActual");
            JPasswordField passNueva = builder.getComponenteByClass("passNueva");

            if(Dialogos.confirmar("¿Está seguro que desea cambiar su contraseña?", "Cambiar Contraseña")){
                if(usuarioController.cambiarPassword(new String (passActual.getPassword()),
                        new String(passNueva.getPassword()),
                        new DatosListener<String>() {
                            @Override
                            public void onSuccess(String datos) {
                                MiFrame.logout();
                            }
                            @Override
                            public void onSuccess(String datos, Paginado p) {
                            }
                            @Override
                            public void onError(String mensajeError) {
                            }
                        })){this.dispose();}
            }

        } else {
            Dialogos.mostrarError(errores);
        }
    }


    private String validarFormulario2() {
        String claveActual = MiFrame.getInstance().getLogin().getClave();
        StringBuilder errores = new StringBuilder("<html>");

        JPasswordField jpassActual = builder.getComponenteByClass("passActual");
        String passActual = new String(jpassActual.getPassword());
        if (!claveActual.equals(passActual)){
            errores.append("La contraseña actual es incorrecta.<br>");
            jpassActual.setBorder(BorderFactory.createLineBorder(Color.RED));
        }

        JPasswordField jpassNueva = builder.getComponenteByClass("passNueva");
        String passNueva = new String(jpassNueva.getPassword());
        if (passNueva.trim().isEmpty()){
            errores.append("La nueva contraseña debe tener al menos 6 caracteres.<br>");
            jpassNueva.setBorder(BorderFactory.createLineBorder(Color.RED));
        }

        JPasswordField jpassConfirmar = builder.getComponenteByClass("passConfirmar");
        String passConfirmar = new String(jpassConfirmar.getPassword());
        if (passConfirmar.trim().isEmpty()){
            errores.append("<br>");
            jpassConfirmar.setBorder(BorderFactory.createLineBorder(Color.RED));
        }

        if (!passConfirmar.equals(passNueva)){
            errores.append("Las contraseñas no coinciden.<br>");
            jpassNueva.setBorder(BorderFactory.createLineBorder(Color.RED));
            jpassConfirmar.setBorder(BorderFactory.createLineBorder(Color.RED));
        }
        if (errores.length() > 6 ) {
            errores.append("</html>");
            return errores.toString();
        }else{
            return "";
        }
    }
}
