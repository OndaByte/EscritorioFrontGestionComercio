
package com.OndaByte.MisterFront.servicios;

import com.OndaByte.config.ConfiguracionGeneral;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.ProtocolException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class LoginService {
    
    private static Logger logger = LogManager.getLogger(LoginService.class.getName());
    
    static{
        if(logger.isDebugEnabled()){
            logger.debug("Init logger en LoginService");
        }
    }
    
    public static JSONObject iniciarSesion(String user, String pass){
        
        String ruta = ConfiguracionGeneral.getCONFIG_HTTP_API_URL()
                + ":"
                + ConfiguracionGeneral.getCONFIG_HTTP_API_PORT() ;
        
        logger.debug("LoginService.ruta: " + ruta);
        
        //Armar Body
        JSONObject json = new JSONObject();
        json.put("user", user);
        json.put("pass", pass);
        
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ruta +"/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json.toString(), StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException | IOException ex) {
            logger.error("Error en LoginService.iniciarSesion" + ex);            
        }
        
        if(response != null && response.body() != null && response.body().length()>0){
            logger.debug("Body en LoginService.iniciarSesion" + response.body());            
            return new JSONObject(response.body());
        }
        logger.warn("Null Response en LoginService.iniciarSesion");            
        return null; 
           
    }

    /*
     * SIN USO
     */
    public static JSONObject apagar() throws IOException {

        String ruta = ConfiguracionGeneral.getCONFIG_HTTP_API_URL()
                + ":"
                + ConfiguracionGeneral.getCONFIG_HTTP_API_PORT() ;

        logger.debug("LoginService.ruta: " + ruta);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ruta +"/salir"))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException | IOException ex) {
            logger.error("Error en LoginService.iniciarSesion" + ex);
        }

        if(response != null && response.body() != null && response.body().length()>0){
            logger.debug("Body en LoginService.iniciarSesion" + response.body());
            return new JSONObject(response.body());
        }
        logger.warn("Null Response en LoginService.iniciarSesion");
        return null;

    }

    /**
     *
     */
    public static void apagar2() {
        URL url = null;
        try {
            url = new URL("http://localhost:4567/salir");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            con.setRequestMethod("GET");
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        }

        int responseCode = 0;
        try {
            responseCode = con.getResponseCode();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
