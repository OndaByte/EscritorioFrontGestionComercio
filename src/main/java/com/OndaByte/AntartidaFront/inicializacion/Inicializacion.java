package com.OndaByte.AntartidaFront.inicializacion;

import net.miginfocom.swing.MigLayout;
import com.formdev.flatlaf.FlatClientProperties;

import java.awt.*;
import javax.swing.*;

public class Inicializacion extends JPanel {

    private JPanel panelPrincipal;
    
    private JLabel lbNombre;
    private JLabel lbTelefono;
    private JLabel lbEmail;
    private JLabel lbDireccion;
    private JTextField txtNombre;
    private JTextField txtTelefono;
    private JTextField txtEmail;
    private JTextField txtDireccion;

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
    }
    
    private void initComponents(){
        panelPrincipal = new JPanel(new MigLayout("insets 10","[fill]", "[]"));
                
        lbNombre = new JLabel("Empresa");
        txtNombre = new JTextField();
        
        lbNombreAdmin = new JLabel("Usuario");
        txtNombreAdmin = new JTextField();
        
        System.out.println("jkhadsjkhfsdhjklsdfkhlksdjfgwoierfgh2");
        
        panelPrincipal.add(lbNombre);
        
    }
    
}
