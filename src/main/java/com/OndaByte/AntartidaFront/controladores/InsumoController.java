
package com.OndaByte.AntartidaFront.controladores;

import com.OndaByte.AntartidaFront.modelos.Insumo;
import com.OndaByte.AntartidaFront.servicios.InsumoService;
import com.OndaByte.AntartidaFront.sesion.SesionController;
import com.OndaByte.AntartidaFront.vistas.DatosListener;
import com.OndaByte.AntartidaFront.vistas.util.Paginado;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;

public class InsumoController {

    private SesionController sesionController = null;
    private static InsumoController controller;
    private static Logger logger = LogManager.getLogger(InsumoController.class.getName());
    
    static{
        if(logger.isDebugEnabled()){
            logger.debug("Init logger en InsumoController");
        }
    }

    private InsumoController(){
	        this.sesionController = SesionController.getInstance();
	}

    public static InsumoController getInstance(){
        if(controller == null){
            controller = new InsumoController();
        }
        return controller;
    }
    
    /**
     * Filtra insumos por texto de búsqueda
     * @param filtro Texto para buscar
     * @param listener Listener que maneja la respuesta
     */
    public void filtrar(String filtro,String pagina,String cantElementos, DatosListener<List<Insumo>> listener) {
        
        JSONObject res = InsumoService.filtrar(filtro,pagina,cantElementos);
        if (res.getInt("status") == 200) {
            try {
                List<Insumo> insumos = new ObjectMapper().readValue(
                        new JSONArray(res.getString("data")).toString(),
                        new TypeReference<List<Insumo>>() {}
                );
                Paginado p = new Paginado();
                p.setPagina(res.getInt("pagina"));
                p.setTamPagina(res.getInt("elementos"));
                p.setTotalElementos(res.getInt("t_elementos"));
                p.setTotalPaginas(res.getInt("t_paginas"));
                listener.onSuccess(insumos,p);
            } catch (Exception e) {
                listener.onError("Error procesando insumos");
            }
        } else {
            listener.onError(res.optString("mensaje"));
        }
    }

    /**
     * Crea un nuevo insumo.
     */
    public boolean crearInsumo(Insumo insumo, DatosListener<String> listener) {
        JSONObject insumoRes = InsumoService.crearInsumo(insumo);
        if (insumoRes.getInt("status") == 201) {
            listener.onSuccess(insumoRes.optString("mensaje"));
            return true;
        } else {
            listener.onError(insumoRes.optString("mensaje"));
            return false;
        }
    }

    /**
     * Edita un insumo existente.
     */
    public boolean editarInsumo(Insumo insumo, DatosListener<String> listener) {
        JSONObject insumoRes = InsumoService.editarInsumo(insumo);
        if (insumoRes.getInt("status") == 201) {
            listener.onSuccess(insumoRes.optString("mensaje"));
            return true;
        } else {
            listener.onError(insumoRes.optString("mensaje"));
            return false;
        }
    }

    /**
     * Elimina un insumo por ID.
     */
    public void eliminarInsumo(int id, DatosListener<String> listener) {
        JSONObject insumoRes = InsumoService.eliminarInsumo(id);
        if (insumoRes.getInt("status") == 201) {
            listener.onSuccess(insumoRes.optString("mensaje"));
        } else {
            listener.onError(insumoRes.optString("mensaje"));
        }
    }
}