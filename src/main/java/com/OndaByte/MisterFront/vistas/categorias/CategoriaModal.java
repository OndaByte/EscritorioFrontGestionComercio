package com.OndaByte.MisterFront.vistas.categorias;

import com.OndaByte.MisterFront.controladores.CategoriaController;
import com.OndaByte.MisterFront.modelos.Categoria;
import com.OndaByte.MisterFront.vistas.DatosListener;
import com.OndaByte.MisterFront.vistas.util.Dialogos;
import com.OndaByte.MisterFront.vistas.util.FormularioBuilder;
import com.OndaByte.MisterFront.vistas.util.Paginado;

import java.awt.Color;
import java.awt.Frame;
import javax.swing.JDialog;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.JLabel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CategoriaModal extends JDialog {

    //Modelo
    private Categoria categoria;
    private CategoriaController categoriaController;
    //Form
    FormularioBuilder builder;

    private static Logger logger = LogManager.getLogger(CategoriaModal.class.getName());

    static {
        if (logger.isDebugEnabled()) {
            logger.debug("Init logger en CategoriaModal");
        }
    }

    public CategoriaModal(Frame parent) {
        super(parent, "Crear Categoria", true);
        categoriaController = CategoriaController.getInstance();
        builder = new FormularioBuilder();
        crearForm(600,300);
    }

    public CategoriaModal(Frame parent, Categoria categoria) {
        super(parent, "Editar Categoria", true);
        //setIconImage(MiFrame.getInstance().getIconImage());
        categoriaController = CategoriaController.getInstance();
        builder = new FormularioBuilder();

        this.categoria = categoria;
        crearForm(600,300);
        cargarDatos(categoria, true);
    }

    //
//    public CategoriaModal(Frame parent, Categoria categoria, boolean soloLectura) {
//        super(parent, "Ver Categoria", true);
//        builder = new FormularioBuilder();
//        crearForm();
//        cargarDatos(categoria, false);
//        setCamposSoloLectura();
//        this.setVisible(true);
//    }
//
    private void crearForm(int width, int height) {
        builder.agregarMenuBotones();
        builder.getBtnGuardar().addActionListener(e -> guardar());
        builder.getBtnCancelar().addActionListener(e -> dispose());

        builder.agregarTitulo("lblID", "Categoria: ");
        builder.agregarComponente("txtNombre", "textfield", "Nombre de la categoría: * ", null, 0);

        setContentPane(builder.construir());
        
        setSize(width, height);
        setLocationRelativeTo(null);
    }

    private void guardar() {
        String errores = validarFormulario();
        if (errores.isEmpty()) {
            Categoria nuevo = this.crearCategoria();
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
            if (this.categoria == null){
                if(categoriaController.crearCategoria(nuevo, listener)){this.dispose();}
            } else {
                nuevo.setId(this.categoria.getId());
                if(categoriaController.editarCategoria(nuevo, listener)){this.dispose();}
            }
        } else {
            Dialogos.mostrarError(errores);
        }
    }

    private Categoria crearCategoria(){
        String nombre = ((JTextField) builder.getComponente("txtNombre")).getText();
        Categoria c = new Categoria();
        c.setNombre(nombre);
        c.setPorcentaje_descuento(0);
        c.setTipo("PRODUCTO");
        return c;
    }

    private String validarFormulario() {
        StringBuilder errores = new StringBuilder("<html>");

        JTextField txtNombre = builder.getComponenteByClass("txtNombre");
        if (txtNombre.getText().trim().isEmpty()) {
            errores.append("- El campo Nombre es obligatorio.<br>");
            txtNombre.setBorder(BorderFactory.createLineBorder(Color.RED));
        }
//
//        JTextField txtDni = builder.getComponenteByClass("txtDni");
//        if (!txtDni.getText().trim().matches("\\d+")){
//            errores.append("- El campo DNI es obligatorio y debe contener solo números.<br>");
//            txtDni.setBorder(BorderFactory.createLineBorder(Color.RED));
//        }
//
//        JTextField txtTelefono = builder.getComponenteByClass("txtTelefono");
//        if (!txtTelefono.getText().trim().matches("\\d+")) {
//            errores.append("- El campo Telefono es obligatorio y debe contener solo números.<br>");
//            txtTelefono.setBorder(BorderFactory.createLineBorder(Color.RED));
//        }
//
//        JTextField txtDireccion = builder.getComponenteByClass("txtDireccion");
//        if (txtDireccion.getText().trim().isEmpty()) {
//            errores.append("- El campo Direccion es obligatorio.<br>");
//            txtDireccion.setBorder(BorderFactory.createLineBorder(Color.RED));
//        }

        if (errores.length() > 6 ) {
            errores.append("</html>");
            return errores.toString();
        }else{
            return "";
        }
    }

    private void cargarDatos(Categoria categoria, boolean editable) {

        ((JLabel) builder.getComponente("lblID")).setText("Categoria con ID: " + categoria.getId());
        ((JTextField) builder.getComponente("txtNombre")).setText(categoria.getNombre());

        if (!editable) {
            builder.getBtnGuardar().setVisible(false);
        }
        this.getContentPane().repaint();
    }

}
