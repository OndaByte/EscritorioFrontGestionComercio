/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.OndaByte.MisterFront.servicios;

import com.OndaByte.MisterFront.modelos.Caja;
import com.OndaByte.MisterFront.modelos.ItemVenta;
import com.OndaByte.MisterFront.modelos.Movimiento;
import com.OndaByte.MisterFront.modelos.Venta;
import static com.OndaByte.MisterFront.servicios.APIRequest.enviarRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import netscape.javascript.JSException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author luciano
 */
public class CajaService {

    private static Logger logger = LogManager.getLogger(CajaService.class.getName());

    static{
        if(logger.isDebugEnabled()){
            logger.debug("Init logger en CajaService");
        }
    }

    /**
     * Elimina un Pedido por ID en la API.
     */
    public static JSONObject eliminar(int id) {
        return enviarRequest("/p/e/caja/" +id, "DELETE", null);
    }
    
    public static JSONObject filtrar(String filtro,String desde,String hasta, String estado, String pagina, String cantElementos) {
        try {
            String filtroCodificado = URLEncoder.encode(filtro, StandardCharsets.UTF_8.toString()); 
            String desdeCodificado = URLEncoder.encode(desde, StandardCharsets.UTF_8.toString()); 
            String hastaCodificado = URLEncoder.encode(hasta, StandardCharsets.UTF_8.toString()); 
            String estadoCodificado = URLEncoder.encode(estado, StandardCharsets.UTF_8.toString()); 
            return enviarRequest("/p/e/caja/sesiones?filtro=" + filtroCodificado + "&desde=" + desdeCodificado + "&hasta=" + hastaCodificado +  "&estado="+ estadoCodificado + "&pagina=" + pagina + "&elementos=" + cantElementos,
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
    
    public static JSONObject resumen(String filtro, String desde, String hasta) {
        try {
            String filtroCodificado = URLEncoder.encode(filtro, StandardCharsets.UTF_8.toString());
            String desdeCodificado = URLEncoder.encode(desde, StandardCharsets.UTF_8.toString());
            String hastaCodificado = URLEncoder.encode(hasta, StandardCharsets.UTF_8.toString());

            return enviarRequest(
                "/p/e/caja/resumen?filtro=" + filtroCodificado
                    + "&desde=" + desdeCodificado
                    + "&hasta=" + hastaCodificado,
                "GET",
                null
            );
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject()
                .put("status", 500)
                .put("mensaje", "Error al codificar los parámetros del resumen");
        }
    }
    

    public static JSONObject abrir(Float montoI){
        JSONObject jo = new JSONObject();
        jo.put("monto", montoI);
        return enviarRequest("/p/e/caja/1","POST",jo); 
    }

    public static JSONObject cerrar(){ return enviarRequest("/p/e/caja/1","PUT",null); }

    public static JSONObject obtenerUltimaSesion(int cajaID){
        return enviarRequest("/p/e/caja/"+cajaID+"/ultimaSesion","GET",null);
    }
}
