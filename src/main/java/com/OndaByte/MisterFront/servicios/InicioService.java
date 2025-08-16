package com.OndaByte.MisterFront.servicios;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.OndaByte.MisterFront.modelos.Usuario;
import com.OndaByte.MisterFront.modelos.Empresa;

import org.json.JSONObject;
import com.OndaByte.config.ConfiguracionGeneral;

public class InicioService {

    private static Logger logger = LogManager.getLogger(InicioService.class.getName());

    public static JSONObject inicializar(Empresa e, Usuario u){
        String ruta = ConfiguracionGeneral.getCONFIG_HTTP_API_URL() + ":" + ConfiguracionGeneral.getCONFIG_HTTP_API_PORT() ;
        
        logger.debug("InicioService.ruta: " + ruta);
        
        JSONObject usuario = new JSONObject();
        usuario.put("usuario", u.getUsuario());
        usuario.put("contra", u.getContra());
        
        JSONObject empresa = new JSONObject();
        empresa.put("nombre", e.getNombre());
        empresa.put("telefono", e.getTelefono());
        empresa.put("email", e.getEmail());
        empresa.put("direccion", e.getDireccion());

        
        JSONObject json = new JSONObject();
        json.put("empresa",empresa);
        json.put("admin",usuario);
        
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(ruta +"/inicializar"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(json.toString(), StandardCharsets.UTF_8))
            .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException | IOException ex) {
            logger.error("Error en InicioService.inicializar" + ex);            
        }
        
        if(response != null && response.body() != null && response.body().length()>0){
            logger.debug("Body en InicioService.inicializar" + response.body());            
            return new JSONObject(response.body());
        }
        logger.warn("Null Response en InicioService.inicializar");            
        return null; 
    }
	
}
