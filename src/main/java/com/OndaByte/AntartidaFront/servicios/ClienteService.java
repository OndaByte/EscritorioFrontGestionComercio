
package com.OndaByte.AntartidaFront.servicios;

import com.OndaByte.AntartidaFront.modelos.Cliente;
import static com.OndaByte.AntartidaFront.servicios.APIRequest.enviarRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class ClienteService {
    
    private static Logger logger = LogManager.getLogger(ClienteService.class.getName());
        
    static{
        if(logger.isDebugEnabled()){
            logger.debug("Init logger en ClienteService");
        }
    }
    
    public static JSONObject filtrar(String filtro){
        try {
            String filtroCodificado = URLEncoder.encode(filtro, StandardCharsets.UTF_8.toString());
            return enviarRequest("/p/e/cliente/filtrar?filtro=" + filtroCodificado, "GET", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject().put("status", 500).put("mensaje", "Error al codificar el filtro");
        }
    }

    public static JSONObject filtrar(String filtro, String pagina,String cantElementos){
        try {
            String filtroCodificado = URLEncoder.encode(filtro, StandardCharsets.UTF_8.toString());
            return enviarRequest("/p/e/cliente?filtro=" + filtroCodificado + "&pagina="+pagina+"&elementos="+cantElementos, "GET", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject().put("status", 500).put("mensaje", "Error al codificar el filtro");
        }
    }

    /**
     * Obtiene la lista de clientes desde el API.
     * */
    /*

    public static JSONObject getClientes() {
        return enviarRequest("/p/e/clientes", "GET", null);
    }
     */

    /**
     * Obtiene la lista de clientes desde el API.
     */
    public static JSONObject getClienteById(int id) {
        return enviarRequest("/p/e/cliente/"+id, "GET", null);
    }

    /**
     * Crea un cliente en la API.
     */
    public static JSONObject crearCliente(Cliente cliente) {
        return enviarRequest("/p/e/cliente", "POST", new JSONObject(cliente));
    }

    /**
     * Edita un cliente en la API.
     */
    public static JSONObject editarCliente(Cliente cliente) {
        return enviarRequest("/p/e/cliente/"+cliente.getId(), "PUT", new JSONObject(cliente));
    }

    /**
     * Elimina un cliente por ID en la API.
     */
    public static JSONObject eliminarCliente(int id) {
        return enviarRequest("/p/e/cliente/"+id, "DELETE", null);
    } 
}
