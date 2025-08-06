
package com.OndaByte.MisterFront.servicios;

import com.OndaByte.MisterFront.modelos.Usuario;
import com.OndaByte.MisterFront.sesion.SesionController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import static com.OndaByte.MisterFront.servicios.APIRequest.enviarRequest;

public class UsuarioService {

    private static Logger logger = LogManager.getLogger(UsuarioService.class.getName());

    static{
        if(logger.isDebugEnabled()){
            logger.debug("Init logger en UsuarioService");
        }
    }

    public static JSONObject filtrar(String filtro){
        try {
            String filtroCodificado = URLEncoder.encode(filtro, StandardCharsets.UTF_8.toString());
            return enviarRequest("/p/a/usuario/filtrar?filtro=" + filtroCodificado, "GET", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject().put("status", 500).put("mensaje", "Error al codificar el filtro");
        }
    }

    public static JSONObject filtrar(String filtro, String pagina,String cantElementos){
        try {

            String filtroCodificado = URLEncoder.encode(filtro, StandardCharsets.UTF_8.toString());
            return enviarRequest("/p/a/usuario?filtro=" + filtroCodificado + "&pagina="+pagina+"&elementos="+cantElementos, "GET", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject().put("status", 500).put("mensaje", "Error al codificar el filtro");
        }
    }

    /**
     * Obtiene la lista de usuarios desde el API.
     */
    public static JSONObject getUsuarios() {
        return enviarRequest("/p/a/usuarios", "GET", null);
    }

    /**
     * Obtiene la lista de usuarios desde el API.
     */
    public static JSONObject getUsuarioById(int id) {
        return enviarRequest("/p/a/usuario/"+id, "GET", null);
    }

    /**
     * Crea un usuario en la API.
     */
    public static JSONObject crearUsuario(Usuario usuario) {
        return enviarRequest("/registrar", "POST", new JSONObject(usuario));
        //return enviarRequest("/p/a/usuario/alta", "POST", new JSONObject(usuario)); /// Solo los administradores deberian crear nuevos usaurios
    }

    /**
     * Edita un usuario en la API.
     */
    public static JSONObject editarUsuario(Usuario usuario) {
        return enviarRequest("/p/a/usuario/"+usuario.getId(), "PUT", new JSONObject(usuario));
        //return enviarRequest("/p/actualizar", "PUT", usuario);
    }


    /**
     * Edita un usuario en la API.
     */
    public static JSONObject cambiarRolUsuario(Usuario usuario, String rol) {
        JSONObject body = new JSONObject();
        body.put("rol", rol);
        return enviarRequest("/p/a/usuario/"+usuario.getId()+"/rol", "PUT", body);
        //return enviarRequest("/p/actualizar", "PUT", usuario);
    }

    /**
     * Elimina un usuario por ID en la API.
     */
    public static JSONObject eliminarUsuario(int id) {
        return enviarRequest("/p/a/usuario/"+id, "DELETE", null);
    }

    /**
     * Cambiar contraseña un usuario en la API.
     */
    public static JSONObject cambiarContraUsuario(String pass, String nueva) {

        JSONObject usuario = new JSONObject();
        usuario.put("pass", pass);
        usuario.put("nueva", nueva);
        usuario.put("token", SesionController.getInstance().getSesionToken());
        return enviarRequest("/p/actualizar", "PUT", usuario);
    }
}
