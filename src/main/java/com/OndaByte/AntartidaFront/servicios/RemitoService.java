
package com.OndaByte.AntartidaFront.servicios;

import com.OndaByte.AntartidaFront.modelos.ItemRemito;
import com.OndaByte.AntartidaFront.modelos.Remito;
import static com.OndaByte.AntartidaFront.servicios.APIRequest.enviarRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class RemitoService {
    private static Logger logger = LogManager.getLogger(RemitoService.class.getName());

    static {
        if (logger.isDebugEnabled()) {
            logger.debug("Init logger en RemitoService");
        }
    }

    /**
     * Obtiene la lista de Remitos desde el API.
     */
    public static JSONObject getRemitos() {
        return enviarRequest("/p/e/remitos", "GET", null);
    }

    /**
     * Crea un Remito en la API.
     */
    public static JSONObject crearRemito(Remito remito, List<ItemRemito> items) {
        //hacer json 
        JSONObject jo = new JSONObject();
        JSONObject pre = new JSONObject(remito);
        JSONArray jaItems= new JSONArray();
        for(ItemRemito i : items){
            jaItems.put(new JSONObject(i));
        }
        jo.put("remito",pre);
        jo.put("items",jaItems);
        return enviarRequest("/p/e/remito", "POST", jo);
    }

    /**
     * Crea un Remito en la API.
     */
    public static JSONObject editarRemito(Remito remito, List<ItemRemito> items) {
        //hacer json 
        JSONObject jo = new JSONObject();
        JSONObject r = new JSONObject(remito);
        JSONArray jaItems= new JSONArray();
        for(ItemRemito i : items){
            jaItems.put(new JSONObject(i));
        }
        jo.put("remito",r);
        jo.put("items",jaItems);
        return enviarRequest("/p/e/remito/"+remito.getId(), "PUT", jo);
    }
    /**
     * Elimina un Remito por ID en la API.
     */
    public static JSONObject eliminarRemito(int id) {
        return enviarRequest("/p/a/remito/" + id + "/baja", "DELETE", null);
    }

    public static JSONObject filtrar(String filtro, String desde, String hasta, String estado, String pagina, String cantElementos) {
        try {
            String filtroCodificado = URLEncoder.encode(filtro, StandardCharsets.UTF_8.toString());
            String desdeCodificado = URLEncoder.encode(desde, StandardCharsets.UTF_8.toString());
            String hastaCodificado = URLEncoder.encode(hasta, StandardCharsets.UTF_8.toString());
            String estadoCodificado = URLEncoder.encode(estado, StandardCharsets.UTF_8.toString());
            return enviarRequest(
                "/p/e/remito?filtro=" + filtroCodificado + "&desde=" + desdeCodificado + "&hasta=" + hastaCodificado + "&estado=" + estadoCodificado + "&pagina=" + pagina + "&elementos=" + cantElementos,
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
    
    /**
     * Crea un Pedido en la API.
     */
    public static JSONObject actualizarRemito(Remito r) {
        JSONObject jo = new JSONObject();
        jo.put("fecha_pago",r.getFecha_pago());
        return enviarRequest("/p/e/remito/"+r.getId()+"/actualizar", "PUT", jo);
    }

    public static JSONObject buscarRemito(int id) {
        return enviarRequest("/p/e/remito/" +id, "get", null);
    }
}
