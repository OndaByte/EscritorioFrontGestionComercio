
package com.OndaByte.AntartidaFront.vistas.gastosFijos;

import com.OndaByte.AntartidaFront.controladores.PeriodoController;
import com.OndaByte.AntartidaFront.modelos.Gasto;
import com.OndaByte.AntartidaFront.modelos.Periodo;
import com.OndaByte.AntartidaFront.vistas.DatosListener;
import com.OndaByte.AntartidaFront.vistas.util.Dialogos;
import com.OndaByte.AntartidaFront.vistas.util.FormularioBuilder;
import com.OndaByte.AntartidaFront.vistas.util.Paginado;
import com.OndaByte.AntartidaFront.vistas.util.FechaUtils;
import java.awt.Color;
import java.awt.Frame;
import java.text.SimpleDateFormat;

import com.toedter.calendar.JDateChooser;

import java.time.LocalDate;

import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.toedter.calendar.JDateChooser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GastoFijoModal extends JDialog {

    //Modelo
    private Periodo periodo;
    private Gasto gasto;
    private PeriodoController controller;
    //Form
    FormularioBuilder builder;

    private static Logger logger = LogManager.getLogger(GastoFijoModal.class.getName());

    static {
        if (logger.isDebugEnabled()) {
            logger.debug("Init logger en PeriodoModal");
        }
    }

    public GastoFijoModal(Frame parent) {
        super(parent, "Nuevo Gasto Fijo", true);
        controller = PeriodoController.getInstance();
        builder = new FormularioBuilder();
        crearForm(600,400);
    }

    public GastoFijoModal(Frame parent, Gasto gasto, Periodo periodo) {
        super(parent, "Editar Gasto Fijo");
        //setIconImage(MiFrame.getInstance().getIconImage());
        controller = PeriodoController.getInstance();
        builder = new FormularioBuilder();

        this.periodo = periodo;
        this.gasto = gasto;
        crearForm(600,400);
        cargarDatos(gasto,periodo, true);
    }

    private void crearForm(int width, int height) {
        builder.agregarMenuBotones();
        builder.getBtnGuardar().addActionListener(e -> guardar());
        builder.getBtnCancelar().addActionListener(e -> dispose());

        builder.agregarTitulo("lblID", "Gasto Fijo: ");
        builder.agregarComponente("Nombre", "textfield", "Nombre", null,0);
        builder.agregarComponente("lblNombre", "label", "", null , 0);
        
        builder.agregarComponente("Costo", "textfield", "Costo", null,0);
        builder.agregarComponente("lblCosto", "label", "", null , 0);
        
        String[] auxiliar = {"DIARIO","SEMANAL","QUINCENAL","MENSUAL","BIMESTRAL","TRIMESTRAL","CUATRIMESTRAL","SEMESTRAL","ANUAL"};
        builder.agregarComponente("Repeticion", "combobox", "Repeticion", auxiliar,0);
        builder.agregarComponente("lblRepeticion", "label", "", null , 0);
        
        builder.agregarComponente("Inicio", "calendar","Inicio: ", null, 0);

        setContentPane(builder.construir());
        
        setSize(width, height);
        setLocationRelativeTo(null);
    }

    private void guardar() {
        String errores = validarFormulario();
        if (errores.isEmpty()) {
            Periodo pNuevo = this.crearPeriodo();
            Gasto gNuevo = this.crearGasto();
            if(gNuevo == null){
              return;
            }
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
                    }
                };
            if(this.gasto == null){
                if(controller.crearGastoFijo(gNuevo, pNuevo, listener)){this.dispose();}
            }
            else{
                gNuevo.setId(gasto.getId());
                pNuevo.setId(periodo.getId());
                if(controller.editarGastoFijo(gNuevo, pNuevo, listener)){this.dispose();}
            }
            
        } else {
            Dialogos.mostrarError(errores);
        }
    }

    private Gasto crearGasto(){
        JDateChooser inicio = builder.getComponenteByClass("Inicio");
        // JLabel lblError = builder.getComponenteByClass("lblError");
        JTextField nombre = builder.getComponenteByClass("Nombre");
        Integer repeticion = ((JComboBox) builder.getComponente("Repeticion")).getSelectedIndex();
        
        LocalDate ldInicio = FechaUtils.dateToLocalDate(inicio.getDate());
        // lblError.setText("");
        Gasto g = new Gasto();

        g.setNombre(nombre.getText());
        g.setInicio(FechaUtils.ldToString(ldInicio));
        g.setRepeticion(repeticion);
        return g;
    }

    private Periodo crearPeriodo(){
        JTextField costoInicial = builder.getComponenteByClass("Costo");

        Periodo p = new Periodo();
        p.setCosto(Float.parseFloat(costoInicial.getText()));
        return p;
    }
    
    
    private String validarFormulario() {
        StringBuilder errores = new StringBuilder("<html>");
        JTextField txt;
        JDateChooser inicio;
        try{
            txt = builder.getComponenteByClass("Nombre");
            if(txt.getText().trim().isEmpty()) {
                errores.append("* El campo Nombre es obligatorio.<br>");
                txt.setBorder(BorderFactory.createLineBorder(Color.RED));
            }
            txt = builder.getComponenteByClass("Costo");
            if (!txt.getText().trim().matches("(\\d+(\\.\\d*)?|\\.\\d+)")){
                errores.append("* El campo Costo Inicial es obligatorio y debe contener solo números.<br>");
                txt.setBorder(BorderFactory.createLineBorder(Color.RED));
            }


            
         inicio = builder.getComponenteByClass("Inicio");
         if(inicio.getDate() == null){
             errores.append("* El campo Inicio es obligatorio.<br>");
             inicio.setBorder(BorderFactory.createLineBorder(Color.RED));
         }
         else{
           LocalDate ldInicio = FechaUtils.dateToLocalDate(inicio.getDate());
           if(!FechaUtils.esFechaPresenteOFuturo(ldInicio)){
             errores.append("No puede dar de alta un nuevo gasto en el pasado.<br>");
             inicio.setBorder(BorderFactory.createLineBorder(Color.RED));
           }
         }

        } catch (NullPointerException e){
            errores.append(e.getMessage());
        }

        if (errores.length() > 6 ) {
            errores.append("</html>");
            return errores.toString();
        }else{
            return "";
        }
    }

    private void cargarDatos(Gasto gasto, Periodo periodo, boolean editable) {

        ((JLabel) builder.getComponente("lblID")).setText("Gasto Fijo ID: "+gasto.getId()+"  Periodo ID: "+periodo.getId());

        ((JTextField) builder.getComponenteByClass("Nombre")).setText(gasto.getNombre());

        
        JComboBox aux = (JComboBox) builder.getComponente("Repeticion");
        aux.setSelectedIndex(gasto.getRepeticion());
        aux.setEnabled(false);

        ((JTextField) builder.getComponente("Costo")).setText(periodo.getCosto()+"");

        ((JLabel) builder.getComponente("lblNombre")).setText("Se actualiza el nombre en todos los gastos");
        ((JLabel) builder.getComponente("lblCosto")).setText("Se actualiza el costo en el gasto seleccionado");

        JDateChooser jdc = (JDateChooser) builder.getComponente("Inicio");
        jdc.setDate(FechaUtils.stringToDate(periodo.getPeriodo()));
        jdc.setEnabled(false);
        

        if (!editable) {
            builder.getBtnGuardar().setVisible(false);
        }
        this.getContentPane().repaint();
    }
}
