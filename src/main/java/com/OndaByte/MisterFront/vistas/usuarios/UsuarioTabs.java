
package com.OndaByte.MisterFront.vistas.usuarios;

import com.OndaByte.MisterFront.estilos.MisEstilos;
import com.OndaByte.MisterFront.sesion.SesionController;
import com.OndaByte.MisterFront.vistas.MiFrame;
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.HashSet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import net.miginfocom.swing.MigLayout;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UsuarioTabs extends JPanel {

    HashSet<String> permisos = null;
    private static Logger logger = LogManager.getLogger(UsuarioPanel.class.getName());

    static{
        if(logger.isDebugEnabled()){
            logger.debug("Init logger en UsuarioPanel");
        }
    }

    public UsuarioTabs(){
        this.permisos = (HashSet<String>) SesionController.getInstance().getSesionPermisos();
        setLayout(new BorderLayout());
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Perfil", crearPanelBienvenida());
        if(permisos.contains("USUARIO_LISTAR")){
            tabbedPane.addTab("Usuarios", new UsuarioPanel());
        }
        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel crearPanelBienvenida() {
        JPanel panel = new JPanel(new MigLayout("wrap 1, align center", "[center]", "[]20[]"));
        panel.putClientProperty(FlatClientProperties.STYLE, MisEstilos.PANEL_CENTRAL);

        // Obtener el nombre del usuario desde la sesión (ajustar si tu modelo lo requiere)
        String nombreUsuario = SesionController.getInstance().getSesionNombreUsuario();

        JLabel labelBienvenida = new JLabel("Bienvenido, " + nombreUsuario);
        labelBienvenida.setFont(labelBienvenida.getFont().deriveFont(24f)); // Tamaño grande

        JButton btnCambiarPassword = new JButton("Cambiar contraseña");
        btnCambiarPassword.setPreferredSize(new Dimension(200, 40));

        // Acción del botón
        btnCambiarPassword.addActionListener(e -> {
            UsuarioModal modal = new UsuarioModal(MiFrame.getInstance(), true);
            modal.setVisible(true);
        });

        panel.add(labelBienvenida, "gapbottom 20");
        panel.add(btnCambiarPassword);

        return panel;
    }
}
