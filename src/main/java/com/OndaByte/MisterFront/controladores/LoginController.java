
package com.OndaByte.MisterFront.controladores;

import com.OndaByte.MisterFront.servicios.LoginService;
import com.OndaByte.MisterFront.sesion.SesionController;
import com.OndaByte.MisterFront.vistas.login.LoginView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashSet;
import org.json.JSONException;

public class LoginController {

    private LoginView vista;
    private SesionController sesionController = null;
    private static LoginController controller;
    private static Logger logger = LogManager.getLogger(LoginController.class.getName());
    
    private LoginController(){
        this.sesionController = SesionController.getInstance();
    }
    
    public static LoginController getInstance(){
        if(controller == null){
            controller = new LoginController();
        }
        return controller;
    }
	
    public void setvista(LoginView vista){
        this.vista = vista; 
    }
    
    public void iniciarSesion(String user, String pass){
        //Service 
        JSONObject loginRes = LoginService.iniciarSesion(user, pass);
        try{
            if(loginRes.getInt("status") == 200){
                JSONObject body = new JSONObject(loginRes.getString("data"));
                JSONArray permisos = body.getJSONArray("permisos");
                HashSet<String> nombrePermisos = new HashSet();
                for (int i = 0, size = permisos.length(); i < size; i++)
                {
                    String n = permisos.getJSONObject(i).getString("nombre");
                    nombrePermisos.add(n);
                }            
                JSONObject rol = new JSONObject(body.getString("rol"));                
                sesionController.crearSesion(
                    user,
                    rol.getString("nombre"),
                    nombrePermisos,
                    body.getString("token")
                );
                vista.iniciarSesion();
            }else{
                vista.setMensaje(true, loginRes.getString("mensaje"));
            }
        }catch(JSONException ex){ 
            logger.error("Error de parseo JSON en LoginController.iniciarSesion() " + ex.getMessage());       
        }  
    }

    public void apagar(){
        try {
            LoginService.apagar2();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
