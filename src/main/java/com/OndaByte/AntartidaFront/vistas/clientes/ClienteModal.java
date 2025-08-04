
package com.OndaByte.AntartidaFront.vistas.clientes;

import com.OndaByte.AntartidaFront.controladores.ClienteController;
import com.OndaByte.AntartidaFront.modelos.Cliente;
import com.OndaByte.AntartidaFront.vistas.DatosListener;
import com.OndaByte.AntartidaFront.vistas.util.Dialogos;
import com.OndaByte.AntartidaFront.vistas.util.FormularioBuilder;
import com.OndaByte.AntartidaFront.vistas.util.Paginado;
import java.awt.Color;
import java.awt.Frame;
import javax.swing.JDialog;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.SwingUtilities;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClienteModal extends JDialog {
    //Modelo
    private Cliente cliente;
    private ClienteController clienteController;
    //Form
    private FormularioBuilder builder;

    private static Logger logger = LogManager.getLogger(ClienteModal.class.getName());

    static{
        if(logger.isDebugEnabled()){
            logger.debug("Init logger en ClienteModal");
        }
    }
    public ClienteModal(Frame parent) {
        super(parent, "Crear Cliente", true);
		clienteController = ClienteController.getInstance();
        this.cliente = null;
        builder = new FormularioBuilder();
        crearForm(600,500, true);
    }

    /*

     */
    public ClienteModal(Frame parent, Cliente cliente) {
        super(parent, "Editar Cliente", true);
		clienteController = ClienteController.getInstance();
        builder = new FormularioBuilder();
        this.cliente = cliente;
        crearForm(600,500, false);
        cargarDatos(cliente, true);
    }

    private void crearForm(int width, int height, boolean b) {
        builder.agregarMenuBotones();
        builder.getBtnGuardar().addActionListener(e -> guardar(b));
        builder.getBtnCancelar().addActionListener(e -> dispose());
 
        builder.agregarTitulo("lblID","Cliente: "); 
        builder.agregarComponente("txtNombre", "textfield", "Nombre y Apellido/Razon Social: * ", null, 0);
        builder.agregarComponente("txtEmail", "textfield", "Email: ", null, 0);
        builder.agregarComponente("txtDni", "textfield", "DNI: * ", null, 0);
        builder.agregarComponente("txtCuitCuil", "textfield", "CUIL-CUIL: * ", null, 0);
        builder.agregarComponente("txtTelefono", "textfield", "Teléfono: * ", null, 0);
        builder.agregarComponente("txtDireccion", "textfield", "Dirección: *", null, 0);
        builder.agregarComponente("txtProvincia", "textfield", "Provincia: ", null, 0);
        builder.agregarComponente("txtLocalidad", "textfield", "Localidad: ", null, 0);
        builder.agregarComponente("txtCodigoPostal", "textfield", "Código Postal: ", null, 0);
        builder.agregarComponente("cmbCondIva", "combobox", "Condición de IVA: ", new String[]{"MONOTRIBUTISTA","RESPONSABLE INSCRIPTO","EXENTO"}, 0);

        ((JTextField) builder.getComponente("txtCuitCuil")).getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                autocompletarCuitSiCorresponde();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });

        setContentPane(builder.construir());
        setSize(width, height);
        setLocationRelativeTo(null);
    }

    private void autocompletarCuitSiCorresponde() {
        JTextField txtCuit = builder.getComponenteByClass("txtCuitCuil");
        JTextField txtDni = builder.getComponenteByClass("txtDni");

        String cuitInput = txtCuit.getText().trim();
        String dni = txtDni.getText().trim();

        // Solo actuar si se ingresaron los primeros 2 dígitos del CUIT y el DNI es válido
        if (cuitInput.length() == 2 ) {
            try {
                String cuitCompleto = generarCuitConVerificador(cuitInput, dni);
                SwingUtilities.invokeLater(() -> txtCuit.setText(cuitCompleto));

            } catch (Exception ex) {
                logger.warn("Error generando CUIT automáticamente: " + ex.getMessage());
            }
        }
    }

    private String generarCuitConVerificador(String prefijo, String dni) {
        String base = prefijo + dni;
        int[] pesos = {5, 4, 3, 2, 7, 6, 5, 4, 3, 2};
        int suma = 0;

        for (int i = 0; i < pesos.length; i++) {
            suma += Character.getNumericValue(base.charAt(i)) * pesos[i];
        }

        int resto = suma % 11;
        int verificador;

        if (resto == 0) {
            verificador = 0;
        } else if (resto == 1) {
            // Excepciones: si es 1, y prefijo es 20 o 27, se cambia a 23
            prefijo = "23";
            base = prefijo + dni;
            suma = 0;
            for (int i = 0; i < pesos.length; i++) {
                suma += Character.getNumericValue(base.charAt(i)) * pesos[i];
            }
            resto = suma % 11;
            verificador = 11 - resto;
        } else {
            verificador = 11 - resto;
        }

        return prefijo + dni + verificador;
    }


    private void guardar(boolean b) {
        String errores = validarFormulario(b);
        if(errores.isEmpty()){
            Cliente nuevo = this.crearCliente();
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
            if (this.cliente == null) {
                if(clienteController.crearCliente(nuevo, listener)){
                    this.dispose();
                }
            } else {
                nuevo.setId(this.cliente.getId());
                if(clienteController.editarCliente(nuevo, listener)){
                    this.dispose();
                }
            }

        }else{
            Dialogos.mostrarError(errores);
        }
    }

    private Cliente crearCliente(){
        String nombre = ((JTextField) builder.getComponente("txtNombre")).getText();
        String email = ((JTextField) builder.getComponente("txtEmail")).getText();
        String dni = ((JTextField) builder.getComponente("txtDni")).getText();
        String cuitCuil = ((JTextField) builder.getComponente("txtCuitCuil")).getText();
        String telefono = ((JTextField) builder.getComponente("txtTelefono")).getText();
        String direccion = ((JTextField) builder.getComponente("txtDireccion")).getText();
        String provincia = ((JTextField) builder.getComponente("txtProvincia")).getText();
        String localidad = ((JTextField) builder.getComponente("txtLocalidad")).getText();
        String codigoPostal = ((JTextField) builder.getComponente("txtCodigoPostal")).getText();
        String condIva = ((JComboBox) builder.getComponente("cmbCondIva")).getSelectedItem().toString();

        Cliente c = new Cliente();
        c.setNombre(nombre);
        c.setEmail(email);
        c.setDni(dni);
        c.setCuit_cuil(cuitCuil);
        c.setTelefono(telefono);
        c.setDireccion(direccion);
        c.setProvincia(provincia);
        c.setLocalidad(localidad);
        c.setCodigo_postal(codigoPostal);
        c.setCond_iva(condIva);

        return c;
    }

    private String validarFormulario(boolean b) {
        StringBuilder errores = new StringBuilder("<html>");

        JTextField txtNombre = builder.getComponenteByClass("txtNombre");
        if (txtNombre.getText().trim().isEmpty()) {
            errores.append("- El campo Nombre es obligatorio.<br>");
            txtNombre.setBorder(BorderFactory.createLineBorder(Color.RED));
        }

        if (b) {
            JTextField txtDni = builder.getComponenteByClass("txtDni");
            if (!txtDni.getText().trim().matches("\\d+")){
                errores.append("- El campo DNI es obligatorio y debe contener solo números.<br>");
                txtDni.setBorder(BorderFactory.createLineBorder(Color.RED));
            }

            JTextField txtCuitCuil = builder.getComponenteByClass("txtCuitCuil");
            if (!txtCuitCuil.getText().trim().matches("\\d+")) {
                errores.append("- El campo CUIT/CUIL es obligatorio y debe contener solo números.<br>");
                txtCuitCuil.setBorder(BorderFactory.createLineBorder(Color.RED));
            }
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

    public void cargarDatos(Cliente cliente, boolean editable) {

        ((JLabel) builder.getComponente("lblID")).setText("Cliente con ID: " + cliente.getId());
        ((JTextField) builder.getComponente("txtNombre")).setText(cliente.getNombre());
        ((JTextField) builder.getComponente("txtEmail")).setText(cliente.getEmail());
        ((JTextField) builder.getComponente("txtTelefono")).setText(cliente.getTelefono());
        ((JTextField) builder.getComponente("txtDireccion")).setText(cliente.getDireccion());
        ((JTextField) builder.getComponente("txtDni")).setText(cliente.getDni());
        ((JTextField) builder.getComponente("txtDni")).setEditable(false);
        ((JTextField) builder.getComponente("txtCuitCuil")).setText(cliente.getCuit_cuil());
        ((JTextField) builder.getComponente("txtCuitCuil")).setEditable(false);
        ((JTextField) builder.getComponente("txtLocalidad")).setText(cliente.getLocalidad());
        ((JTextField) builder.getComponente("txtCodigoPostal")).setText(cliente.getCodigo_postal());
        ((JTextField) builder.getComponente("txtProvincia")).setText(cliente.getProvincia());
        ((JComboBox) builder.getComponente("cmbCondIva")).setSelectedItem(cliente.getCond_iva());

        if (!editable) {
            builder.getBtnGuardar().setVisible(false);
        }
        this.getContentPane().repaint();
    }

}