
package com.OndaByte.MisterFront.servicios;

import static com.OndaByte.MisterFront.servicios.APIRequest.enviarRequest;
import com.OndaByte.MisterFront.modelos.Categoria;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class CategoriaService {

    private static Logger logger = LogManager.getLogger(CategoriaService.class.getName());

    static{
        if(logger.isDebugEnabled()){
            logger.debug("Init logger en CategoriaService");
        }
    }

    public static JSONObject filtrar(String filtro, String pagina,String cantElementos){
        try {

            String filtroCodificado = URLEncoder.encode(filtro, StandardCharsets.UTF_8.toString());
            return enviarRequest("/p/e/categoria?filtro=" + filtroCodificado + "&pagina="+pagina+"&elementos="+cantElementos, "GET", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject().put("status", 500).put("mensaje", "Error al codificar el filtro");
        }
    }

    /**
     * Crea un Pedido en la API.
     */
    public static JSONObject crearCategoria(Categoria categoria) {
        return enviarRequest("/p/e/categoria", "POST", new JSONObject(categoria));
    }

    /**
     * Edita un Pedido en la API.
     */
    public static JSONObject editarCategoria(Categoria categoria) {
        return enviarRequest("/p/e/categoria/"+categoria.getId(), "PUT", new JSONObject(categoria));
    }

    /**
     * Elimina un Pedido por ID en la API.
     */
    public static JSONObject eliminarCategoria(int id) {
        return enviarRequest("/p/e/categoria/"+id, "DELETE", null);
    }

}
