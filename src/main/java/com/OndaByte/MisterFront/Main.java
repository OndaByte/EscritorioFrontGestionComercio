
package com.OndaByte.MisterFront;

import com.OndaByte.MisterFront.controladores.MovimientoController;
import com.OndaByte.MisterFront.sesion.SesionController;
import com.OndaByte.MisterFront.vistas.DatosListener;
import com.OndaByte.MisterFront.vistas.MiFrame;
import com.OndaByte.MisterFront.vistas.util.Dialogos;
import com.OndaByte.MisterFront.vistas.util.Paginado;
import com.OndaByte.config.ConfiguracionGeneral;
import com.OndaByte.config.Constantes;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import java.awt.Font;
import java.io.File;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

public class Main {
    
    private static Logger logger = LogManager.getLogger(Main.class.getName());

    public static void main(String[] args) {
        // Cargar configuración Log4j desde el archivo XML
        ConfiguracionGeneral.init();
        String strPath = Main.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        String log4jConfigPath = strPath.replace(Constantes.JAR_FILE, "classes" + File.separator) + Constantes.CONFIG_LOG_FILE;
        Configurator.initialize(null, log4jConfigPath);
        
        logger.debug("Init Front");


//        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//            if(SesionController.getInstance().getSesionCaja() != null && SesionController.getInstance().getSesionCaja().getId() != -1)
//                MovimientoController.getInstance().cerrarCaja(new DatosListener<String>() {
//                    @Override
//                    public void onSuccess(String resultado) {
//                        Dialogos.mostrarExito(resultado);
//                    }
//
//                    @Override
//                    public void onError(String mensajeError) {
//                        Dialogos.mostrarError(mensajeError);
//                    }
//
//                    @Override
//                    public void onSuccess(String datos, Paginado p) {
//                    }
//                });
//        }));

        try {
            FlatRobotoFont.install(); // Instalar la fuente si es necesaria
            FlatLaf.registerCustomDefaultsSource("config/flatlaf"); // Registrar el directorio donde está flatlaf.properties
            UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
            FlatMacLightLaf.setup(); // Aplicar el Look and Feel. Arranca en modo claro.
            // FlatMacDarkLaf.setup(); // Arranca en modo oscuro.

            logger.info("FlatLaf cargado con éxito.");
        } catch (Exception e) {
            logger.error("Error cargando FlatLaf", e);
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> MiFrame.getInstance());
    }
}
