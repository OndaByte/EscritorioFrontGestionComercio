
package com.OndaByte.AntartidaFront.servicios;

import com.OndaByte.AntartidaFront.modelos.Orden;
import static com.OndaByte.AntartidaFront.servicios.APIRequest.enviarRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class OrdenService {
    private static Logger logger = LogManager.getLogger(OrdenService.class.getName());

    static {
        if (logger.isDebugEnabled()) {
            logger.debug("Init logger en OrdenService");
        }
    }

    /**
     * Obtiene la lista de Ordenes desde el API.
     */
    public static JSONObject getOrdenes() {
        return enviarRequest("/p/e/ordenes", "GET", null);
    }

    /**
     * Obtiene la lista de ordenes con los clientes asociados
     * @return 
     */
    public static JSONObject getOrdenesYClientes() {
        return enviarRequest("/p/e/ordenesYClientes?pagina=1&elementos=10", "GET", null);
    }

    /**
     * Crea una Orden en la API.
     */
    public static JSONObject crearOrden(JSONObject orden) {
        return enviarRequest("/p/e/orden/alta", "POST", orden);
    }

    /**
     * Edita una Orden en la API.
     */
    public static JSONObject editarOrden(Orden orden) {
        return enviarRequest("/p/e/orden/" + orden.getId() + "/modificar", "PUT", new JSONObject(orden));
    }

    /**
     * Elimina una Orden por ID en la API.
     */
    public static JSONObject eliminarOrden(int id) {
        return enviarRequest("/p/a/orden/" + id + "/baja", "DELETE", null);
    }
    /**
     * Crea un Pedido en la API.
     */
    public static JSONObject actualizarOrden(Orden o) {
        JSONObject jo = new JSONObject();
        jo.put("estado_orden",o.getEstado_orden());
        return enviarRequest("/p/e/orden/"+o.getId()+"/actualizar", "PUT", jo);
    }
    
    public static JSONObject filtrar(String filtro, String desde, String hasta, String estado, String pagina, String cantElementos) {
        try {
            String filtroCodificado = URLEncoder.encode(filtro, StandardCharsets.UTF_8.toString());
            String desdeCodificado = URLEncoder.encode(desde, StandardCharsets.UTF_8.toString());
            String hastaCodificado = URLEncoder.encode(hasta, StandardCharsets.UTF_8.toString());
            String estadoCodificado = URLEncoder.encode(estado, StandardCharsets.UTF_8.toString());
            return enviarRequest(
                "/p/e/orden?filtro=" + filtroCodificado + "&desde=" + desdeCodificado + "&hasta=" + hastaCodificado + "&estado=" + estadoCodificado + "&pagina=" + pagina + "&elementos=" + cantElementos,
                "GET",
                null
            );
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject()
                .put("status", 500)
                .put("mensaje", "Error al codificar el filtro");
        }
    }
}
