package com.OndaByte.AntartidaFront.servicios;

import com.OndaByte.AntartidaFront.modelos.ItemPresupuesto;
import com.OndaByte.AntartidaFront.modelos.Presupuesto;
import static com.OndaByte.AntartidaFront.servicios.APIRequest.enviarRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class PresupuestoService {
    private static Logger logger = LogManager.getLogger(PresupuestoService.class.getName());

    static{
        if(logger.isDebugEnabled()){
            logger.debug("Init logger en PresupuestoService");
        }
    }

    public static JSONObject filtrar(String filtro,String desde,String hasta, String estado, String pagina, String cantElementos) {
        try {
            String filtroCodificado = URLEncoder.encode(filtro, StandardCharsets.UTF_8.toString());
            String desdeCodificado = URLEncoder.encode(desde, StandardCharsets.UTF_8.toString());
            String hastaCodificado = URLEncoder.encode(hasta, StandardCharsets.UTF_8.toString());
            String estadoCodificado = URLEncoder.encode(estado, StandardCharsets.UTF_8.toString());
            return enviarRequest(
                    "/p/e/presupuesto?filtro=" + filtroCodificado + "&desde=" + desdeCodificado + "&hasta=" + hastaCodificado +  "&estado="+ estadoCodificado + "&pagina=" + pagina + "&elementos=" + cantElementos,
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
    public static JSONObject crearPresupuesto(Presupuesto presupuesto, List<ItemPresupuesto> items) {
        //hacer json 
        JSONObject jo = new JSONObject();
        JSONObject pre = new JSONObject(presupuesto);
        JSONArray jaItems= new JSONArray();
        for(ItemPresupuesto i : items){
            jaItems.put(new JSONObject(i));
        }
        jo.put("presupuesto",pre);
        jo.put("items",jaItems);
        return enviarRequest("/p/e/presupuesto", "POST", jo);
    }
    /**
     * Crea una Orden en la API.
     */
    public static JSONObject actualizarPresupuesto(Presupuesto p) {
        JSONObject jo = new JSONObject();
        jo.put("estado_presupuesto",p.getEstado_presupuesto());
        return enviarRequest("/p/e/presupuesto/"+p.getId()+"/actualizar", "PUT", jo);
    }

    /**
     * Edita un Pedido en la API.
     */
    public static JSONObject editarPresupuesto(Presupuesto presupuesto, List<ItemPresupuesto> items) {
        //hacer json 
        JSONObject jo = new JSONObject();
        JSONObject r = new JSONObject(presupuesto);
        JSONArray jaItems= new JSONArray();
        for(ItemPresupuesto i : items){
            jaItems.put(new JSONObject(i));
        }
        jo.put("presupuesto",r);
        jo.put("items",jaItems);
        return enviarRequest("/p/e/presupuesto/"+presupuesto.getId(), "PUT", jo);
    }

    /**
     * Elimina un Pedido por ID en la API.
     */
    public static JSONObject eliminarPresupuesto(int id) {
        return enviarRequest("/p/e/presupuesto/" +id, "DELETE", null);
    }
    
    public static JSONObject buscarPresupuesto(int id) {
        return enviarRequest("/p/e/presupuesto/" +id, "get", null);
    }
}
