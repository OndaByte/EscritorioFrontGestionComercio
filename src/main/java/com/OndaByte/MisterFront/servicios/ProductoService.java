
package com.OndaByte.MisterFront.servicios;

import static com.OndaByte.MisterFront.servicios.APIRequest.enviarRequest;
import com.OndaByte.MisterFront.modelos.Producto;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class ProductoService {

    private static Logger logger = LogManager.getLogger(ProductoService.class.getName());

    static{
        if(logger.isDebugEnabled()){
            logger.debug("Init logger en ProductoService");
        }
    }

    public static JSONObject filtrar(String filtro, String pagina,String cantElementos){
        try {

            String filtroCodificado = URLEncoder.encode(filtro, StandardCharsets.UTF_8.toString());
            return enviarRequest("/p/e/producto?filtro=" + filtroCodificado + "&pagina="+pagina+"&elementos="+cantElementos, "GET", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject().put("status", 500).put("mensaje", "Error al codificar el filtro");
        }
    }

    /**
     * Crea un Pedido en la API.
     */
    public static JSONObject crearProducto(Producto producto) {
        return enviarRequest("/p/e/producto", "POST", new JSONObject(producto));
    }

    /**
     * Edita un Pedido en la API.
     */
    public static JSONObject editarProducto(Producto producto) {
        return enviarRequest("/p/e/producto/"+producto.getId(), "PUT", new JSONObject(producto));
    }

    /**
     * Elimina un Pedido por ID en la API.
     */
    public static JSONObject eliminarProducto(int id) {
        return enviarRequest("/p/e/producto/"+id, "DELETE", null);
    }

}
