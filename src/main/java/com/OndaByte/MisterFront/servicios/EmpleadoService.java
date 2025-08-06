
package com.OndaByte.MisterFront.servicios;

import com.OndaByte.MisterFront.modelos.Empleado;
import static com.OndaByte.MisterFront.servicios.APIRequest.enviarRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class EmpleadoService {

    private static Logger logger = LogManager.getLogger(EmpleadoService.class.getName());

    static{
        if(logger.isDebugEnabled()){
            logger.debug("Init logger en EmpleadoService");
        }
    }
    
    public static JSONObject filtrar(String filtro){
        try {
            String filtroCodificado = URLEncoder.encode(filtro, StandardCharsets.UTF_8.toString());
            return enviarRequest("/p/e/empleado/filtrar?filtro=" + filtroCodificado, "GET", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject().put("status", 500).put("mensaje", "Error al codificar el filtro");
        }
    }

    public static JSONObject filtrar(String filtro, String pagina,String cantElementos){
        try {

            String filtroCodificado = URLEncoder.encode(filtro, StandardCharsets.UTF_8.toString());
            return enviarRequest("/p/e/empleado?filtro=" + filtroCodificado + "&pagina="+pagina+"&elementos="+cantElementos, "GET", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject().put("status", 500).put("mensaje", "Error al codificar el filtro");
        }
    }

    /**
     * Obtiene la lista de empleados desde el API.
     */
    /*
    public static JSONObject getEmpleados() {
        return enviarRequest("/p/e/empleados", "GET", null);
    }*/

    /**
     * Obtiene la lista de empleados desde el API.
     */
    public static JSONObject getEmpleadoById(int id) {
        return enviarRequest("/p/e/empleado/"+id, "GET", null);
    }

    /**
     * Crea un empleado en la API.
     */
    public static JSONObject crearEmpleado(Empleado empleado) {
        return enviarRequest("/p/e/empleado", "POST", new JSONObject(empleado));
    }

    /**
     * Edita un empleado en la API.
     */
    public static JSONObject editarEmpleado(Empleado empleado) {
        return enviarRequest("/p/e/empleado/"+empleado.getId(), "PUT", new JSONObject(empleado));
    }

    /**
     * Elimina un empleado por ID en la API.
     */
    public static JSONObject eliminarEmpleado(int id) {
        return enviarRequest("/p/e/empleado/"+id, "DELETE", null);
    }
}
