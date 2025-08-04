
package com.OndaByte.AntartidaFront.sesion;

import com.OndaByte.AntartidaFront.controladores.LoginController;
import com.OndaByte.AntartidaFront.modelos.Caja;
import com.OndaByte.AntartidaFront.vistas.ContenedorPrincipalView;
import java.util.HashSet;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SesionController {
    
    private static Logger logger = LogManager.getLogger(LoginController.class.getName());
    
    private ContenedorPrincipalView vista;
    private Sesion sesion = null;
        
    private static SesionController controller;
    
    private SesionController(){
        this.sesion = Sesion.getInstance();
    }
    
    public void setvista(ContenedorPrincipalView vista){
        this.vista = vista; 
    }
    
    public static SesionController getInstance(){
        if(controller == null){
            controller = new SesionController();
        }
        return controller;
    }
    
    public void initMenuRol(){
        vista.renderMenuLateral(sesion.getRol());
    }

    /*
     * SIN USO
     */
    public void limpiarSesion(){
        sesion = null;
    }
    
    public Set<String> getSesionPermisos() {
        return sesion.getPermisos();
    }
    
    public void crearSesion(String nombreUser, String rol, HashSet<String> nombrePermisos, String token){
        sesion = Sesion.getInstance();
        sesion.setNombreUsuario(nombreUser);
        sesion.setRol(rol);
        sesion.setPermisos(nombrePermisos);
        sesion.setToken(token);
        sesion.setToken(token);
    }
    
    public String getSesionToken(){
        return sesion.getToken();
    }

    public String getSesionNombreUsuario(){
        return sesion.getNombreUsuario();
    }
    
    public Caja getSesionCaja(){
        return sesion.getSesionCaja();
    }
    public void setSesionCaja(Caja sesionCaja){
        sesion.setSesionCaja(sesionCaja);
    }
    public Integer getSesionUsuarioId(){
        return sesion.getUsuario_id();
    }
    public void setSesionUsuarioId(Integer id){
        sesion.setUsuario_id(id);
    }
    
}
