
package com.OndaByte.MisterFront.vistas;
import com.OndaByte.MisterFront.controladores.CajaController;
import com.OndaByte.MisterFront.sesion.SesionController;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.OndaByte.MisterFront.vistas.inicializacion.Inicializacion;
import com.OndaByte.MisterFront.vistas.login.Login;
import com.OndaByte.MisterFront.vistas.util.Dialogos;
import com.OndaByte.MisterFront.vistas.util.Paginado;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MiFrame extends JFrame {

    private static MiFrame miFrame;
    private static Logger logger = LogManager.getLogger(MiFrame.class.getName());
    
    private Login login=null;
    private Inicializacion inicializacion=null;
    private ContenedorPrincipal aplicacion=null;

    static {
        if (logger.isDebugEnabled()) {
            logger.debug("Init logger en Frame");
        }
    }
      
    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public ContenedorPrincipal getAplicacion() {
        return aplicacion;
    }

    public void setAplicacion(ContenedorPrincipal aplicacion) {
        this.aplicacion = aplicacion;
    }
    
    
    public static MiFrame getInstance(){
        if(miFrame != null){
            return miFrame;
        }
        miFrame = new MiFrame();
        return miFrame;
    }
    
    private MiFrame() {
        miFrame = this;
        miFrame.login = new Login();
        miFrame.inicializacion = new Inicializacion();
        miFrame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("¡Se está cerrando la ventana!");
                dispose();
                System.exit(0);
            }
        });
        miFrame.setSize(new Dimension(1366, 768));
        miFrame.setLocationRelativeTo(null);
        init(false);
        miFrame.setVisible(true);
    }

    private static void init(boolean esInicio){
        if(!esInicio){
            miFrame.setContentPane(miFrame.login);
            miFrame.getRootPane().setDefaultButton(miFrame.login.getBtnLogin());
        }
        else{
            miFrame.setContentPane(miFrame.inicializacion);
        }
        miFrame.getRootPane().putClientProperty(FlatClientProperties.FULL_WINDOW_CONTENT, true);
    }

    public static void login() {
        miFrame.aplicacion = new ContenedorPrincipal();
//        MovimientoController.getInstance().ultimaCaja(new DatosListener<String>() {
//            @Override
//            public void onSuccess(String resultado) {
//                logger.debug("Frame - caja obtenida con exito");
//                
//                //Dialogos.mostrarExito(resultado);
//            }
//
//            @Override
//            public void onError(String mensajeError) {
//                Dialogos.mostrarError(mensajeError);
//            }
//
//            @Override
//            public void onSuccess(String datos, Paginado p) {
//            }
//        });
        FlatAnimatedLafChange.showSnapshot();
        
        miFrame.setContentPane(miFrame.aplicacion);
        miFrame.aplicacion.applyComponentOrientation(miFrame.getComponentOrientation());
        miFrame.aplicacion.hideMenu();
        SwingUtilities.updateComponentTreeUI(miFrame.aplicacion);
        FlatAnimatedLafChange.hideSnapshotWithAnimation();
    }

    public static void logout() {
        
        FlatAnimatedLafChange.showSnapshot();
        if(!miFrame.login.getChkRecordarme()){
            miFrame.login = new Login();
        }
        init(false);
        
        if(SesionController.getInstance().getSesionCaja()!= null){
            System.out.println("si");
            CajaController.getInstance().cerrarCaja(new DatosListener<String>() {
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
            });
            SesionController.getInstance().limpiarSesion();
        }else
            System.out.println("no");

        miFrame.login.applyComponentOrientation(miFrame.getComponentOrientation());
        SwingUtilities.updateComponentTreeUI(miFrame.login);
        FlatAnimatedLafChange.hideSnapshotWithAnimation();
    
    }

	public static MiFrame getMiFrame() {
		return miFrame;
	}

	public static void setMiFrame(MiFrame miFrame) {
		MiFrame.miFrame = miFrame;
	}

	public Inicializacion getInicializacion() {
		return inicializacion;
	}

	public void setInicializacion(Inicializacion inicializacion) {
		this.inicializacion = inicializacion;
	}
}