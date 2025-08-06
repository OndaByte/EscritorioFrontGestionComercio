package com.OndaByte.MisterFront.vistas.inicializacion;

import net.miginfocom.swing.MigLayout;
import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import java.awt.BorderLayout;

public class Inicializacion extends JPanel {
    private JLabel lbMensajes;

    private JPanel panelEmpresa;
    private JLabel lbNombreEmpresa;
    private JLabel lbTelefonoEmpresa;
    private JLabel lbEmailEmpresa;
    private JLabel lbDireccionEmpresa;
    private JTextField txtNombreEmpresa;
    private JTextField txtTelefonoEmpresa;
    private JTextField txtEmailEmpresa;
    private JTextField txtDireccionEmpresa;

    private JPanel panelAdmin;
    private JLabel lbNombreAdmin;
    private JLabel lbTelefonoAdmin;
    private JLabel lbEmailAdmin;
    private JLabel lbUsuarioAdmin;
    private JLabel lbContraAdmin;
    private JTextField txtNombreAdmin;
    private JTextField txtTelefonoAdmin;
    private JTextField txtEmailAdmin;
    private JTextField txtUsuarioAdmin;
    private JPasswordField txtContraAdmin;
   
      
    public Inicializacion(){
//        inicioControlador = InicioController.getInstance();
        initComponents();
        styleComponents();        
    }

    private void styleComponents(){
        setLayout(new MigLayout("insets 0,fill","[grow,fill][grow,fill]","[grow,fill]"));
        panelEmpresa.setLayout(new MigLayout("wrap 2", "[right][grow, fill]"));
        panelAdmin.setLayout(new MigLayout("wrap 2", "[right][grow, fill]"));
    }

    
    private void initComponents(){

        panelEmpresa = new JPanel();
        panelAdmin = new JPanel();
                
        panelEmpresa = new JPanel();
        panelEmpresa.setBorder(BorderFactory.createTitledBorder("Panel Empresa"));        
        
        lbNombreEmpresa = new JLabel("Empresa");
        txtNombreEmpresa = new JTextField();
        panelEmpresa.add(lbNombreEmpresa);
        panelEmpresa.add(txtNombreEmpresa);        
        
        lbTelefonoEmpresa = new JLabel("Telefono");
        txtTelefonoEmpresa = new JTextField();
        panelEmpresa.add(lbTelefonoEmpresa);
        panelEmpresa.add(txtTelefonoEmpresa);
        
        lbEmailEmpresa = new JLabel("Email");
        txtEmailEmpresa = new JTextField();
        panelEmpresa.add(lbEmailEmpresa);
        panelEmpresa.add(txtEmailEmpresa);

        lbDireccionEmpresa = new JLabel("Direccion");
        txtDireccionEmpresa = new JTextField();
        panelEmpresa.add(lbDireccionEmpresa);
        panelEmpresa.add(txtDireccionEmpresa);

        
        panelAdmin = new JPanel();
        panelAdmin.setBorder(BorderFactory.createTitledBorder("Panel Admin"));
        
        lbNombreAdmin = new JLabel("Nombre");
        txtNombreAdmin = new JTextField();
        panelAdmin.add(lbNombreAdmin);
        panelAdmin.add(txtNombreAdmin);        
        
        lbTelefonoAdmin = new JLabel("Telefono");
        txtTelefonoAdmin = new JTextField();
        panelAdmin.add(lbTelefonoAdmin);
        panelAdmin.add(txtTelefonoAdmin);
        
        lbEmailAdmin = new JLabel("Email");
        txtEmailAdmin = new JTextField();
        panelAdmin.add(lbEmailAdmin);
        panelAdmin.add(txtEmailAdmin);

        lbUsuarioAdmin = new JLabel("Usuario");
        txtUsuarioAdmin = new JTextField();
        panelAdmin.add(lbUsuarioAdmin);
        panelAdmin.add(txtUsuarioAdmin);

        lbContraAdmin = new JLabel("Contraseña");
        txtContraAdmin = new JPasswordField();
        panelAdmin.add(lbContraAdmin);
        panelAdmin.add(txtContraAdmin);
                        
        this.add(panelEmpresa,"grow, push");
        this.add(panelAdmin,"grow, push");
    }

    private void validarForm(){
        //Controles de campos y notificacion de errores
    }
    
}
