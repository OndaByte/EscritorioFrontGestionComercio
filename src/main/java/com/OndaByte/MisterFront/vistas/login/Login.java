
package com.OndaByte.MisterFront.vistas.login;

import com.OndaByte.MisterFront.controladores.LoginController;
import com.OndaByte.MisterFront.vistas.util.IconSVG;
import com.OndaByte.config.Constantes;
import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import com.OndaByte.MisterFront.vistas.MiFrame;
import com.OndaByte.MisterFront.estilos.MisEstilos;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import java.awt.*;
import java.io.IOException;

public class Login extends JPanel implements LoginView {

    // Declaración de variables al principio
    private javax.swing.JButton btnLogin;
    private javax.swing.JLabel lbPass;
    private javax.swing.JLabel lbTitle;
    private javax.swing.JLabel lbMensajes;
    private javax.swing.JLabel lbUser;
    private JPanel panelLogin1;
    private javax.swing.JPasswordField txtPass;
    private javax.swing.JTextField txtUser;
    private javax.swing.JCheckBox chkRecordarme;
    private LoginController loginControlador;
    private String clave;

 
    public Login() {
        panelLogin1 = new JPanel();
       
        panelLogin1.setLayout(new MigLayout("fillx,wrap,insets 30 40 50 40, width 320", "[fill]", "[]20[][]15[][]30[]"));
        loginControlador = LoginController.getInstance();
        loginControlador.setvista(this);
        initComponents();
        styleComponents();
        
    }

    /*@Override
    public void paintComponents(Graphics g){
        super.paintComponents(g);
    }*/
    
    
    private void styleComponents() {
        setLayout(new MigLayout("al center center"));
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "font:$h1.font");

        txtPass.putClientProperty(FlatClientProperties.STYLE, MisEstilos.CONTRASENIA);
        txtUser.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Ingrese su usuario");
        txtPass.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Ingrese su contraseña");

    }
                        
    private void initComponents() {

     //   panelLogin1 = new Login();
        lbTitle = new JLabel();
        lbUser = new JLabel();
        txtUser = new JTextField();
        lbPass = new JLabel();
        txtPass = new JPasswordField();
        btnLogin = new JButton();
        lbMensajes = new JLabel();
        chkRecordarme = new JCheckBox("Recordarme");

        lbTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbTitle.setText(" INCIAR SESION");
        IconSVG icon = new IconSVG(Constantes.LOGO);
        icon.setFiltro(2);
        lbTitle.setIcon(icon);
        panelLogin1.add(lbTitle);

        lbUser.setText("Nombre de Usuario");
        panelLogin1.add(lbUser);
        panelLogin1.add(txtUser);

        lbPass.setText("Contraseña");
        panelLogin1.add(lbPass);
        panelLogin1.add(txtPass);

        panelLogin1.add(chkRecordarme);

        lbMensajes.setHorizontalAlignment(javax.swing.SwingConstants.SOUTH_EAST);
        lbMensajes.setText("");
        panelLogin1.add(lbMensajes);
        
        btnLogin.setText("INGRESAR");
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdLoginActionPerformed(evt);
            }
        });
        panelLogin1.add(btnLogin);

        //this.setBackground(Color.decode("#24689B")); //USAR MisEstilos.AplicarEstilo
        MisEstilos.aplicarEstilo(this, MisEstilos.MENU_LATERAL);
        //MisEstilos.aplicarEstilo(panelLogin1, MisEstilos.MENU_LATERAL);
        layoutMaqueta();
    }                        
    
    private void layoutMaqueta(){
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(218, Short.MAX_VALUE)
                .addComponent(panelLogin1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(197, 197, 197))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addComponent(panelLogin1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(96, Short.MAX_VALUE))
        );
    }
    
    private boolean validarForm(){
        try{
            String user = txtUser.getText();
            clave = new String(txtPass.getPassword());
            return user.length() > 0 && clave.length() > 0;
        }catch(Exception e){
            return false;
        }
          
    }
        
    private void cmdLoginActionPerformed(java.awt.event.ActionEvent evt) {
        lbMensajes.setText("");
        if(validarForm()){
            loginControlador.iniciarSesion(txtUser.getText(),new String(txtPass.getPassword()));
        }else{
            lbMensajes.setText("Campos incorrectos");
            this.lbMensajes.setForeground(Color.RED); // Acá se cambia el color del texto
        }
    }                                         

    @Override
    public void iniciarSesion() {
        MiFrame.login();
    }
    
    @Override
    public void setMensaje(boolean error, String msj){
        if(error){
        this.lbMensajes.setForeground(Color.RED); // Acá se cambia el color del texto
        this.lbMensajes.setText(msj);
        }
    }

    public JButton getBtnLogin() {
        return btnLogin;
    }

    public boolean getChkRecordarme() {
        return chkRecordarme.isSelected();
    }

    public String getClave(){
        return clave;
    }

    public void apagar() throws IOException {
        loginControlador.apagar();
    }
}
