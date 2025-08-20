package com.OndaByte.MisterFront.vistas.productos;


import com.OndaByte.MisterFront.controladores.CategoriaController;
import com.OndaByte.MisterFront.controladores.ProductoController;
import com.OndaByte.MisterFront.modelos.Categoria;
import com.OndaByte.MisterFront.modelos.Producto;
import com.OndaByte.MisterFront.vistas.DatosListener;
import com.OndaByte.MisterFront.vistas.util.Dialogos;
import com.OndaByte.MisterFront.vistas.util.FormularioBuilder;
import com.OndaByte.MisterFront.vistas.util.Paginado;

import java.awt.Color;
import java.awt.Frame;
import java.util.List;
import javax.swing.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProductoModal extends JDialog {
    //Modelo
    private Producto producto;
    private List<Categoria> categorias;
    private ProductoController productoController;
    private CategoriaController categoriaController;
    //Form
    private FormularioBuilder builder;

    private static Logger logger = LogManager.getLogger(ProductoModal.class.getName());

    static {
        if (logger.isDebugEnabled()) {
            logger.debug("Init logger en ProductoModal");
        }
    }

    public ProductoModal(Frame parent) {
        super(parent, "Crear Producto", true);
        productoController = ProductoController.getInstance();
        categoriaController = CategoriaController.getInstance();
        this.producto = null;
        builder = new FormularioBuilder();
        crearForm(600, 600);
    }

    public ProductoModal(Frame parent, Producto producto) {
        super(parent, "Editar Producto", true);
        productoController = ProductoController.getInstance();
        categoriaController = CategoriaController.getInstance();
        builder = new FormularioBuilder();
        this.producto = producto;
        crearForm(600, 600);
        cargarDatos(producto, true);
    }

    private void crearForm(int width, int height) {
        builder.agregarMenuBotones();
        builder.getBtnGuardar().addActionListener(e -> guardar());
        builder.getBtnCancelar().addActionListener(e -> dispose());

        builder.agregarTitulo("lblID", "Producto: ");
        builder.agregarComponente("txtNombre", "textfield", "Nombre: *", null, 0);
        builder.agregarComponente("txtCodBarra", "textfield", "codigoBarra :", null, 0);
        builder.agregarComponente("txtPrecio", "textfield", "Precio (costo) : *", null, 0);
        builder.agregarComponente("txtPorcentajeGanancia", "textfield", "Porcentaje Ganancia : *", null, 0);
        builder.agregarComponente("txtPorcentajeDescuento", "textfield", "Porcentaje Descuento : *", null, 0);
        ((JTextField) builder.getComponente("txtPorcentajeDescuento")).setText(0 + ""); // Setteado
        builder.agregarComponente("txtStock", "textfield", "Stock: *", null, 0);
        ((JTextField) builder.getComponente("txtStock")).setText(1 + ""); // Setteado

        JComboBox cmbCategorias = new JComboBox();
        categoriaController.filtrar("" , "1", "100",
                new DatosListener<List<Categoria>>() {
                    @Override
                    public void onSuccess(List<Categoria> datos) {
                    }

                    @Override
                    public void onError(String mesajeError) {
                        Dialogos.mostrarError(mesajeError);
                    }

                    @Override
                    public void onSuccess(List<Categoria> datos, Paginado p) {
                        categorias = datos;
                        cmbCategorias.removeAllItems(); // limpio antes de cargar
                        for (Categoria c : categorias) {
                            cmbCategorias.addItem(c.getNombre()); // agregar solo el nombre
                        }
                    }
                });
        builder.agregarComponenteCustom("cmbCategorias", cmbCategorias, "Seleccionar Categoría: *");


        setContentPane(builder.construir());
        setSize(width, height);
        setLocationRelativeTo(null);
    }

    private void guardar() {
        String errores = validarFormulario();
        if (errores.isEmpty()) {
            Producto nuevo = this.crearProducto();
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
            if (this.producto == null) {
                if (productoController.crearProducto(nuevo, listener)) {
                    this.dispose();
                }
            } else {
                nuevo.setId(this.producto.getId());
                if (productoController.editarProducto(nuevo, listener)) {
                    this.dispose();
                }
            }
        } else {
            Dialogos.mostrarError(errores);
        }
    }


    private Producto crearProducto() {

        Producto p = new Producto();
        p.setCategoria_id(1);
        p.setStock(Integer.valueOf(1));

        String nombre = ((JTextField) builder.getComponente("txtNombre")).getText();
        String precio = ((JTextField) builder.getComponente("txtPrecio")).getText();
        String ganancia = ((JTextField) builder.getComponente("txtPorcentajeGanancia")).getText();
        String descuento = ((JTextField) builder.getComponente("txtPorcentajeDescuento")).getText();
        String codBarra = ((JTextField) builder.getComponente("txtCodBarra")).getText();
        String stock = ((JTextField) builder.getComponente("txtStock")).getText();
        int categoria = ((JComboBox) builder.getComponente("cmbCategorias")).getSelectedIndex();
        categoria++;
        p.setNombre(nombre);
        p.setPrecio_costo(Float.valueOf(precio));
        p.setPorcentaje_ganancia(Integer.valueOf(ganancia));
        p.setPorcentaje_descuento(Integer.valueOf(descuento));
        if(codBarra!= null && !codBarra.isEmpty())
            p.setCodigo_barra(codBarra);
        p.setStock(Integer.valueOf(stock));
        p.setCategoria_id(categoria);

        return p;
    }

    private String validarFormulario() {
        StringBuilder errores = new StringBuilder("<html>");

        JTextField txtNombre = builder.getComponenteByClass("txtNombre");
        if (txtNombre.getText().trim().isEmpty()) {
            errores.append("- El campo Nombre es obligatorio.<br>");
            txtNombre.setBorder(BorderFactory.createLineBorder(Color.RED));
        }

//        JTextField txtCodBarra = builder.getComponenteByClass("txtCodBarra");
//        if (txtCodBarra.getText().trim().isEmpty()) {
//            errores.append("- El campo Código de barra es obligatorio.<br>");
//            txtCodBarra.setBorder(BorderFactory.createLineBorder(Color.RED));
//        }

        JTextField txtPrecio = builder.getComponenteByClass("txtPrecio");
        Float precio = null;
        try {
            precio = Float.valueOf(txtPrecio.getText());
            if (precio <= 0) {
                errores.append("- El precio debe ser mayor a 0.<br>");
                txtPrecio.setBorder(BorderFactory.createLineBorder(Color.RED));
            }
        } catch (NumberFormatException e) {
            errores.append("- Debe elegir un precio correcto.<br>");
            txtPrecio.setBorder(BorderFactory.createLineBorder(Color.RED));
        }

        JTextField txtPorcentajeGanancia = builder.getComponenteByClass("txtPorcentajeGanancia");
        Integer pg = null;
        try {
            pg = Integer.valueOf(txtPorcentajeGanancia.getText());
            if (pg < 0) {
                errores.append("- El porcentaje debe ser de 0 a 100.<br>");
                txtPorcentajeGanancia.setBorder(BorderFactory.createLineBorder(Color.RED));
            }
        } catch (NumberFormatException e) {
            errores.append("- Debe elegir un porcentaje de ganancia correcto.<br>");
            txtPorcentajeGanancia.setBorder(BorderFactory.createLineBorder(Color.RED));
        }

        JTextField txtPorcentajeDescuento = builder.getComponenteByClass("txtPorcentajeDescuento");
        Integer pd = null;
        try {
            pd = Integer.valueOf(txtPorcentajeDescuento.getText());
            if (pd < 0) {
                errores.append("- El porcentaje de descuento no puede ser menor que 0.<br>");
                txtPorcentajeDescuento.setBorder(BorderFactory.createLineBorder(Color.RED));
            }
        } catch (NumberFormatException e) {
            errores.append("- Debe elegir un porcentaje de descuento correcto.<br>");
            txtPorcentajeDescuento.setBorder(BorderFactory.createLineBorder(Color.RED));
        }

        JTextField txtStock = builder.getComponenteByClass("txtStock");
        Integer stock = null;
        try {
            stock = Integer.valueOf(txtStock.getText());
            if (stock <= 0) {
                errores.append("- El stock debe ser mayor a 0.<br>");
                txtStock.setBorder(BorderFactory.createLineBorder(Color.RED));
            }
        } catch (NumberFormatException e) {
            errores.append("- Debe elegir un stock correcto.<br>");
            txtStock.setBorder(BorderFactory.createLineBorder(Color.RED));
        }

        if (errores.length() > 6) {
            errores.append("</html>");
            return errores.toString();
        } else {
            return "";
        }
    }

    private void cargarDatos(Producto producto, boolean editable) {
        ((JLabel) builder.getComponente("lblID")).setText("Producto ID: " + producto.getId());
        ((JTextField) builder.getComponente("txtNombre")).setText(producto.getNombre());
        ((JTextField) builder.getComponente("txtPrecio")).setText(producto.getPrecio_costo() + ""); // Setteado
        ((JTextField) builder.getComponente("txtPorcentajeGanancia")).setText(producto.getPorcentaje_ganancia() + ""); // Setteado
        ((JTextField) builder.getComponente("txtPorcentajeDescuento")).setText(producto.getPorcentaje_descuento() + ""); // Setteado
        ((JTextField) builder.getComponente("txtStock")).setText(producto.getStock() + "");
        ((JTextField) builder.getComponente("txtCodBarra")).setText(producto.getCodigo_barra());
        int categoria = producto.getCategoria_id();
        ((JComboBox) builder.getComponente("cmbCategorias")).setSelectedIndex(categoria-1);
        ((JComboBox) builder.getComponente("cmbCategorias")).setEditable(false);

        if (!editable) {
            builder.getBtnGuardar().setVisible(false);
        }

        this.getContentPane().repaint();
    }
}