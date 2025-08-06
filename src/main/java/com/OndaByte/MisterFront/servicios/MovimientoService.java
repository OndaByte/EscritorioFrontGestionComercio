/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.OndaByte.MisterFront.servicios;

import com.OndaByte.MisterFront.modelos.Caja;
import com.OndaByte.MisterFront.modelos.Movimiento;
import static com.OndaByte.MisterFront.servicios.APIRequest.enviarRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import netscape.javascript.JSException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

/**
 *
 * @author luciano
 */
public class MovimientoService {

    private static Logger logger = LogManager.getLogger(MovimientoService.class.getName());

    static{
        if(logger.isDebugEnabled()){
            logger.debug("Init logger en CajaService");
        }
    }

    /**
     * Crea un Pedido en la API.
     */
    public static JSONObject crear(Movimiento movimiento) {
        return enviarRequest("/p/e/caja/movimiento", "POST", new JSONObject(movimiento));
    }


    /**
     * Edita un Pedido en la API.
     */
    public static JSONObject editar(Movimiento movimiento) {
        JSONObject jo = new JSONObject(movimiento);
        jo.put("sesion_caja_id",1);
        return enviarRequest("/p/e/caja/" +movimiento.getId(), "PUT", jo );
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
            return enviarRequest("/p/e/caja/movimientos?filtro=" + filtroCodificado + "&desde=" + desdeCodificado + "&hasta=" + hastaCodificado +  "&estado="+ estadoCodificado + "&pagina=" + pagina + "&elementos=" + cantElementos,
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
    

    public static JSONObject abrir(){ return enviarRequest("/p/e/caja/1","POST",null); }

    public static JSONObject cerrar(){ return enviarRequest("/p/e/caja/1","PUT",null); }
}
