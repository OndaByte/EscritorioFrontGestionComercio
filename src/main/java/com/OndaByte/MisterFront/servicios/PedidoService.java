
package com.OndaByte.MisterFront.servicios;

import com.OndaByte.MisterFront.modelos.Pedido;
import static com.OndaByte.MisterFront.servicios.APIRequest.enviarRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class PedidoService {

    private static Logger logger = LogManager.getLogger(PedidoService.class.getName());

    static{
        if(logger.isDebugEnabled()){
            logger.debug("Init logger en PedidoService");
        }
    }

    /**
     * Crea un Pedido en la API.
     */
    public static JSONObject crearPedido(JSONObject pedido) {
        return enviarRequest("/p/e/pedido", "POST", pedido);
    }

    /**
     * Edita un Pedido en la API.
     */
    public static JSONObject editarPedido(Pedido pedido) {
        return enviarRequest("/p/e/pedido/" +pedido.getId(), "PUT", new JSONObject(pedido));
    }

    /**
     * Elimina un Pedido por ID en la API.
     */
    public static JSONObject eliminarPedido(int id) {
        return enviarRequest("/p/e/pedido/" +id, "DELETE", null);
    }
    
    public static JSONObject filtrar(String filtro,String desde,String hasta, String estado, String pagina, String cantElementos) {
        try {
            String filtroCodificado = URLEncoder.encode(filtro, StandardCharsets.UTF_8.toString()); 
            String desdeCodificado = URLEncoder.encode(desde, StandardCharsets.UTF_8.toString()); 
            String hastaCodificado = URLEncoder.encode(hasta, StandardCharsets.UTF_8.toString()); 
            String estadoCodificado = URLEncoder.encode(estado, StandardCharsets.UTF_8.toString()); 
            return enviarRequest(
                "/p/e/pedido?filtro=" + filtroCodificado + "&desde=" + desdeCodificado + "&hasta=" + hastaCodificado +  "&estado="+ estadoCodificado + "&pagina=" + pagina + "&elementos=" + cantElementos,
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
