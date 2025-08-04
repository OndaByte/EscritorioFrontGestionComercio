
package com.OndaByte.AntartidaFront.servicios;

import com.OndaByte.AntartidaFront.sesion.SesionController;
import com.OndaByte.config.ConfiguracionGeneral;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class APIRequest {
    
    private static Logger logger = LogManager.getLogger(APIRequest.class.getName());

    private static String ruta = ConfiguracionGeneral.getCONFIG_HTTP_API_URL()
            + ":"
            + ConfiguracionGeneral.getCONFIG_HTTP_API_PORT();

    static{
        if(logger.isDebugEnabled()){
            logger.debug("Init logger en APIRequest");
        }
    }

    /**
     * Método genérico para hacer requests HTTP
     */
    public static JSONObject enviarRequest(String endpoint, String metodo, JSONObject body) {
        logger.debug("APIRequest.SendRequest Enviando petición: {} a {}", metodo, ruta + endpoint);
        
        if(body!=null) {
            logger.debug("APIRequest.SendRequest body:" + body.toString());
        }
        
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(ruta + endpoint))
                .header("Content-Type", "application/json")
                .header("token", SesionController.getInstance().getSesionToken());

            switch (metodo) {
                case "POST":
                case "PUT":
                    requestBuilder.method(metodo, HttpRequest.BodyPublishers.ofString(body != null ? body.toString() : "", StandardCharsets.UTF_8));
                    break;
                case "DELETE":
                case "GET":
                default:
                    requestBuilder.method(metodo, HttpRequest.BodyPublishers.noBody());
                    break;
            }
        
            
            HttpResponse<String> response = client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
            
            if(response == null || response.body() == null || response.body().length() < 1){
                logger.error("APIRequest: Response NULL en sendRequest endpoint: {}", endpoint);
                return new JSONObject().put("status", 404).put("mensaje", "No se encontró información en la respuesta"); 
            }

            logger.debug("APIRequest: Respuesta obtenida: {}", response.body());
            return new JSONObject(response.body());

        } catch (IOException | InterruptedException e) {
            logger.error("APIRequest: Error en sendRequest: {}", e.getMessage());
            return new JSONObject().put("status", 500).put("mensaje", "Error al conectar con el servidor");
        }
    }
}
