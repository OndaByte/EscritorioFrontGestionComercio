
package com.OndaByte.MisterFront.controladores;

import com.OndaByte.MisterFront.sesion.SesionController;
import com.OndaByte.MisterFront.vistas.DatosListener;
import com.OndaByte.MisterFront.vistas.util.Paginado;
import com.OndaByte.MisterFront.modelos.Categoria;
import com.OndaByte.MisterFront.servicios.CategoriaService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;

public class CategoriaController {

    private SesionController sesionController = null;
    private static CategoriaController controller;
    private static Logger logger = LogManager.getLogger(CategoriaController.class.getName());
    
    static{
        if(logger.isDebugEnabled()){
            logger.debug("Init logger en CategoriaController");
        }
    }

    private CategoriaController(){
	        this.sesionController = SesionController.getInstance();
	}

    public static CategoriaController getInstance(){
        if(controller == null){
            controller = new CategoriaController();
        }
        return controller;
    }
    
    /**
     * Filtra insumos por texto de búsqueda
     * @param filtro Texto para buscar
     * @param listener Listener que maneja la respuesta
     */
    public void filtrar(String filtro,String pagina,String cantElementos, DatosListener<List<Categoria>> listener) {
        
        JSONObject res = CategoriaService.filtrar(filtro,pagina,cantElementos);
        if (res.getInt("status") == 200) {
            try {
                List<Categoria> categorias = new ObjectMapper().readValue(
                        new JSONArray(res.getString("data")).toString(),
                        new TypeReference<List<Categoria>>() {}
                );
                Paginado p = new Paginado();
                p.setPagina(res.getInt("pagina"));
                p.setTamPagina(res.getInt("elementos"));
                p.setTotalElementos(res.getInt("t_elementos"));
                p.setTotalPaginas(res.getInt("t_paginas"));
                listener.onSuccess(categorias,p);
            } catch (Exception e) {
                listener.onError("Error procesando insumos");
            }
        } else {
            listener.onError(res.optString("mensaje"));
        }
    }
    
    /**
     * Filtra insumos por texto de búsqueda
     * @param filtro Texto para buscar
     * @param listener Listener que maneja la respuesta
     */
    public void filtrar(String filtro,String categoria,String pagina,String cantElementos, DatosListener<List<Categoria>> listener) {
        
        JSONObject res = CategoriaService.filtrar(filtro,pagina,cantElementos);
        if (res.getInt("status") == 200) {
            try {
                List<Categoria> categorias = new ObjectMapper().readValue(
                        new JSONArray(res.getString("data")).toString(),
                        new TypeReference<List<Categoria>>() {}
                );
                Paginado p = new Paginado();
                p.setPagina(res.getInt("pagina"));
                p.setTamPagina(res.getInt("elementos"));
                p.setTotalElementos(res.getInt("t_elementos"));
                p.setTotalPaginas(res.getInt("t_paginas"));
                listener.onSuccess(categorias,p);
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
    public boolean crearCategoria(Categoria insumo, DatosListener<String> listener) {
        JSONObject insumoRes = CategoriaService.crearCategoria(insumo);
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
    public boolean editarCategoria(Categoria insumo, DatosListener<String> listener) {
        JSONObject insumoRes = CategoriaService.editarCategoria(insumo);
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
    public void eliminarCategoria(int id, DatosListener<String> listener) {
        JSONObject insumoRes = CategoriaService.eliminarCategoria(id);
        if (insumoRes.getInt("status") == 201) {
            listener.onSuccess(insumoRes.optString("mensaje"));
        } else {
            listener.onError(insumoRes.optString("mensaje"));
        }
    }
}