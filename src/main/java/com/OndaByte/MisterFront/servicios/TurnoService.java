
package com.OndaByte.MisterFront.servicios;

import com.OndaByte.MisterFront.modelos.Turno;
import static com.OndaByte.MisterFront.servicios.APIRequest.enviarRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class TurnoService {
    private static Logger logger = LogManager.getLogger(TurnoService.class.getName());

    static {
        if (logger.isDebugEnabled()) {
            logger.debug("Init logger en TurnoService");
        }
    }

    /**
     * Obtiene la lista de Turnos desde el API.
     */
    public static JSONObject getTurnos() {
        return enviarRequest("/p/e/turnos", "GET", null);
    }

    /**
     * Crea un Turno en la API.
     */
    public static JSONObject crearTurnoPedido(Turno turno,int pedidoId) {        
        JSONObject turnoReq = new JSONObject();
        JSONObject turnoJson = new JSONObject();
        turnoJson.put("tipo",turno.getTipo());        
        turnoJson.put("observaciones",turno.getObservaciones());        
        turnoJson.put("fecha_inicio",turno.getFechaInicio());        
        turnoJson.put("fecha_fin_e", turno.getFechaFinE());
        turnoJson.put("estado_turno", turno.getEstadoTurno());
        turnoReq.put("turno", turnoJson);
        turnoReq.put("pedido_id", pedidoId);
        return enviarRequest("/p/e/turno/pedido", "POST", turnoReq);
    }

    /**
     * Crea un Turno en la API.
     */
    public static JSONObject crearTurnoOrden(Turno turno,int ordenId) {
        JSONObject turnoReq = new JSONObject();
        JSONObject turnoJson = new JSONObject();
        turnoReq.put("orden_id", ordenId);
        turnoJson.put("tipo",turno.getTipo());
        turnoJson.put("observaciones",turno.getObservaciones());
        turnoJson.put("fecha_inicio",turno.getFechaInicio());
        turnoJson.put("fecha_fin_e", turno.getFechaFinE());
        turnoJson.put("estado_turno", turno.getEstadoTurno());
        turnoReq.put("turno", turnoJson);
        return enviarRequest("/p/e/turno/orden", "POST", turnoReq);
    }
    
    /**
     * Edita un Turno en la API.
     */
    public static JSONObject editarTurno(Turno turno) {
        return enviarRequest("/p/e/turno/" + turno.getId() , "PUT", new JSONObject(turno));
    }

    /**
     * Elimina un Turno por ID en la API.
     */
    public static JSONObject eliminarTurno(int id) {
        return enviarRequest("/p/e/turno/" + id , "DELETE", null);
    }

    public static JSONObject filtrar(String filtro, String desde, String hasta, String tipo, String pagina, String cantElementos) {
        try {
            String filtroCodificado = URLEncoder.encode(filtro, StandardCharsets.UTF_8.toString());
            String desdeCodificado = URLEncoder.encode(desde, StandardCharsets.UTF_8.toString());
            String hastaCodificado = URLEncoder.encode(hasta, StandardCharsets.UTF_8.toString());
            String tipoCodificado = URLEncoder.encode(tipo, StandardCharsets.UTF_8.toString());
            return enviarRequest(
                "/p/e/turno?filtro=" + filtroCodificado + "&desde=" + desdeCodificado + "&hasta=" + hastaCodificado + "&tipo=" + tipoCodificado + "&pagina=" + pagina + "&elementos=" + cantElementos,
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
