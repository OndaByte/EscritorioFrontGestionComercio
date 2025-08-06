package com.OndaByte.MisterFront.vistas.insumos;

import com.OndaByte.MisterFront.controladores.InsumoController;
import com.OndaByte.MisterFront.modelos.Insumo;
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

public class InsumoModal extends JDialog {
    //Modelo
    private Insumo insumo;
    private InsumoController insumoController;
    //Form
    private FormularioBuilder builder;

    private static Logger logger = LogManager.getLogger(InsumoModal.class.getName());

    static{
        if(logger.isDebugEnabled()){
            logger.debug("Init logger en InsumoModal");
        }
    }
    public InsumoModal(Frame parent) {
        super(parent, "Crear Insumo", true);
        insumoController = InsumoController.getInstance();
        this.insumo = null;
        builder = new FormularioBuilder();
        crearForm(600,300);
    }

    public InsumoModal(Frame parent, Insumo insumo) {
        super(parent, "Editar Insumo", true);
        insumoController = InsumoController.getInstance();
        builder = new FormularioBuilder();
        this.insumo = insumo;
        crearForm(600,300);
        cargarDatos(insumo, true);
    }

    private void crearForm(int width, int height) {
        builder.agregarMenuBotones();
        builder.getBtnGuardar().addActionListener(e -> guardar());
        builder.getBtnCancelar().addActionListener(e -> dispose());

        builder.agregarTitulo("lblID", "Insumo: ");
        builder.agregarComponente("txtNombre", "textfield","Nombre: ", null, 0);
        builder.agregarComponente("txtPrecio", "textfield","Precio :", null, 0);
        builder.agregarComponente("txtStock", "textfield", "Stock: ", null, 0); 

        setContentPane(builder.construir());
        setSize(width, height);
        setLocationRelativeTo(null);
    }
        
    private void guardar() {
        String errores = validarFormulario();
        if(errores.isEmpty()){
            Insumo nuevo = this.crearInsumo();
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
            if (this.insumo == null) {
                if(insumoController.crearInsumo(nuevo, listener)){this.dispose();}
            } else {
                nuevo.setId(this.insumo.getId());
                if(insumoController.editarInsumo(nuevo, listener)){this.dispose();}
            }

        }else{
            Dialogos.mostrarError(errores);
        }
    }
    
    private Insumo crearInsumo(){
        String nombre = ((JTextField) builder.getComponente("txtNombre")).getText();
        String precio = ((JTextField) builder.getComponente("txtPrecio")).getText();
        String stock = ((JTextField) builder.getComponente("txtStock")).getText();
        
        Insumo i = new Insumo();
        i.setNombre(nombre);
        i.setPrecio(Float.valueOf(precio));
        i.setStock(Integer.valueOf(stock)); 

        return i;
    }
    
    private String validarFormulario() {
        StringBuilder errores = new StringBuilder("<html>");
        
        JTextField txtNombre = builder.getComponenteByClass("txtNombre");
        if (txtNombre.getText().trim().isEmpty()) {
            errores.append("- El campo Nombre es obligatorio.<br>");
            txtNombre.setBorder(BorderFactory.createLineBorder(Color.RED));
        }
        
        JTextField txtPrecio = builder.getComponenteByClass("txtPrecio"); 
        Float precio = null;
        try{
            precio = Float.valueOf(txtPrecio.getText());
            if (precio<=0) {
                errores.append("- El precio debe ser mayor a 0.<br>");
                txtPrecio.setBorder(BorderFactory.createLineBorder(Color.RED));
            }
        }catch(NumberFormatException e){
            errores.append("- Debe elegir un precio correcto.<br>");
            txtPrecio.setBorder(BorderFactory.createLineBorder(Color.RED));
        }
        
        JTextField txtStock = builder.getComponenteByClass("txtStock"); 
        Integer stock = null;
        try{
            stock = Integer.valueOf(txtStock.getText());
            if (stock<=0) {
                errores.append("- El stock debe ser mayor a 0.<br>");
                txtStock.setBorder(BorderFactory.createLineBorder(Color.RED));
            }
        }catch(NumberFormatException e){
            errores.append("- Debe elegir un stock correcto.<br>");
            txtStock.setBorder(BorderFactory.createLineBorder(Color.RED));
        }
        
        if (errores.length() > 6 ) {
            errores.append("</html>");
            return errores.toString();
        }else{
            return "";
        } 
    }

    private void cargarDatos(Insumo insumo, boolean editable) {      
        ((JLabel) builder.getComponente("lblID")).setText("Insumo ID: " + insumo.getId());
        ((JTextField) builder.getComponente("txtNombre")).setText(insumo.getNombre());
        ((JTextField) builder.getComponente("txtPrecio")).setText(insumo.getPrecio()+""); // Setteado
        ((JTextField) builder.getComponente("txtStock")).setText(insumo.getStock()+"");
        if (!editable) {
            builder.getBtnGuardar().setVisible(false);
        }

        this.getContentPane().repaint();
    }
}