
package com.OndaByte.MisterFront.servicios;

import com.OndaByte.MisterFront.modelos.ItemVenta;
import com.OndaByte.MisterFront.modelos.Venta;
import static com.OndaByte.MisterFront.servicios.APIRequest.enviarRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class VentaService {
    private static Logger logger = LogManager.getLogger(VentaService.class.getName());

    static {
        if (logger.isDebugEnabled()) {
            logger.debug("Init logger en VentaService");
        }
    }

    /**
     * Obtiene la lista de Ventas desde el API.
     */
    public static JSONObject getVentas() {
        return enviarRequest("/p/e/ventas", "GET", null);
    }

    /**
     * Crea un Venta en la API.
     */
    public static JSONObject crearVenta(Venta venta, List<ItemVenta> items) {
        //hacer json 
        JSONObject jo = new JSONObject();
        JSONObject ven = new JSONObject(venta);
        JSONArray jaItems= new JSONArray();
        for(ItemVenta i : items){
            jaItems.put(new JSONObject(i));
        }
        jo.put("venta",ven);
        jo.put("items",jaItems);
        return enviarRequest("/p/e/venta", "POST", jo);
    }

    /**
     * Crea un Venta en la API.
     */
    public static JSONObject editarVenta(Venta venta, List<ItemVenta> items) {
        //hacer json 
        JSONObject jo = new JSONObject();
        JSONObject r = new JSONObject(venta);
        JSONArray jaItems= new JSONArray();
        for(ItemVenta i : items){
            jaItems.put(new JSONObject(i));
        }
        jo.put("venta",r);
        jo.put("items",jaItems);
        return enviarRequest("/p/e/venta/"+venta.getId(), "PUT", jo);
    }
    /**
     * Elimina un Venta por ID en la API.
     */
    public static JSONObject eliminarVenta(int id) {
        return enviarRequest("/p/a/venta/" + id + "/baja", "DELETE", null);
    }

    public static JSONObject filtrar(String filtro, String desde, String hasta, String estado, String pagina, String cantElementos) {
        try {
            String filtroCodificado = URLEncoder.encode(filtro, StandardCharsets.UTF_8.toString());
            String desdeCodificado = URLEncoder.encode(desde, StandardCharsets.UTF_8.toString());
            String hastaCodificado = URLEncoder.encode(hasta, StandardCharsets.UTF_8.toString());
            String estadoCodificado = URLEncoder.encode(estado, StandardCharsets.UTF_8.toString());
            return enviarRequest(
                "/p/e/venta?filtro=" + filtroCodificado + "&desde=" + desdeCodificado + "&hasta=" + hastaCodificado + "&estado=" + estadoCodificado + "&pagina=" + pagina + "&elementos=" + cantElementos,
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
    
//    /**
//     * Crea un Pedido en la API.
//     */
//    public static JSONObject actualizarVenta(Venta v) {
//        JSONObject jo = new JSONObject();
//        jo.put("fecha_pago",r.getFecha_pago());
//        return enviarRequest("/p/e/venta/"+v.getId()+"/actualizar", "PUT", jo);
//    }

    public static JSONObject buscarVenta(int id) {
        return enviarRequest("/p/e/venta/" +id, "get", null);
    }
}
