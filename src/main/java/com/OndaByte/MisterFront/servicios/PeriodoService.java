
package com.OndaByte.MisterFront.servicios;

import com.OndaByte.MisterFront.modelos.Periodo;
import com.OndaByte.MisterFront.modelos.Gasto;
import static com.OndaByte.MisterFront.servicios.APIRequest.enviarRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class PeriodoService{
    
    private static Logger logger = LogManager.getLogger(ClienteService.class.getName());
        
    static{
        if(logger.isDebugEnabled()){
            logger.debug("Init logger en PeriodoService");
        }
    }
    
    public static JSONObject filtrar(String filtro){
        try {
            String filtroCodificado = URLEncoder.encode(filtro,StandardCharsets.UTF_8.toString());
            return enviarRequest("/p/e/periodo?filtro=" + filtroCodificado, "GET", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject().put("status", 500).put("mensaje", "Error al codificar el filtro");
        }
    }

    public static JSONObject filtrar(String filtro,String desde,String hasta, String estado, String pagina, String cantElementos) {
        try {
            String filtroCodificado = URLEncoder.encode(filtro, StandardCharsets.UTF_8.toString()); 
            String desdeCodificado = URLEncoder.encode(desde, StandardCharsets.UTF_8.toString()); 
            String hastaCodificado = URLEncoder.encode(hasta, StandardCharsets.UTF_8.toString());
            return enviarRequest(
                "/p/e/periodo?filtro=" + filtroCodificado + "&desde=" + desdeCodificado + "&hasta=" + hastaCodificado + "&pagina=" + pagina + "&elementos=" + cantElementos,
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

    /*
     * SIN USO
     */
    public static JSONObject getPeriodos() {
        return enviarRequest("/p/e/periodo", "GET", null);
    }
    
    public static JSONObject getPeriodoById(int id) {
        return enviarRequest("/p/e/periodo/"+id, "GET", null);
    }
    
    public static JSONObject crearGastoFijo(Gasto gasto, Periodo periodo) {
        JSONObject json = new JSONObject(gasto);
        json.put("costo",periodo.getCosto());
        return enviarRequest("/p/e/gasto", "POST", json);
    }

    public static JSONObject editarGastoFijo(Gasto gasto,Periodo periodo) {
        JSONObject json = new JSONObject();
        json.put("gasto",new JSONObject(gasto));
        json.put("periodo",new JSONObject(periodo));
        return enviarRequest("/p/e/gasto/"+periodo.getId(), "PUT", json);
    }

    public static JSONObject eliminarGastoFijo(int id) {
        return enviarRequest("/p/e/gasto/"+id, "DELETE", null);
    } 
}
