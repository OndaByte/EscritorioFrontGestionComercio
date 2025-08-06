
package com.OndaByte.MisterFront.servicios;

import com.OndaByte.MisterFront.modelos.Insumo;
import static com.OndaByte.MisterFront.servicios.APIRequest.enviarRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class InsumoService {

    private static Logger logger = LogManager.getLogger(InsumoService.class.getName());

    static{
        if(logger.isDebugEnabled()){
            logger.debug("Init logger en InsumoService");
        }
    }

    public static JSONObject filtrar(String filtro, String pagina,String cantElementos){
        try {

            String filtroCodificado = URLEncoder.encode(filtro, StandardCharsets.UTF_8.toString());
            return enviarRequest("/p/e/insumo?filtro=" + filtroCodificado + "&pagina="+pagina+"&elementos="+cantElementos, "GET", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject().put("status", 500).put("mensaje", "Error al codificar el filtro");
        }
    }

    /**
     * Crea un Pedido en la API.
     */
    public static JSONObject crearInsumo(Insumo insumo) {
        return enviarRequest("/p/e/insumo", "POST", new JSONObject(insumo));
    }

    /**
     * Edita un Pedido en la API.
     */
    public static JSONObject editarInsumo(Insumo insumo) {
        return enviarRequest("/p/e/insumo/"+insumo.getId(), "PUT", new JSONObject(insumo));
    }

    /**
     * Elimina un Pedido por ID en la API.
     */
    public static JSONObject eliminarInsumo(int id) {
        return enviarRequest("/p/e/insumo/"+id, "DELETE", null);
    }

}
