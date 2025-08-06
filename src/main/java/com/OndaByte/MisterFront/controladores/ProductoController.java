
package com.OndaByte.MisterFront.controladores;

import com.OndaByte.MisterFront.sesion.SesionController;
import com.OndaByte.MisterFront.vistas.DatosListener;
import com.OndaByte.MisterFront.vistas.util.Paginado;
import com.OndaByte.MisterFront.modelos.Producto;
import com.OndaByte.MisterFront.servicios.ProductoService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;

public class ProductoController {

    private SesionController sesionController = null;
    private static ProductoController controller;
    private static Logger logger = LogManager.getLogger(ProductoController.class.getName());
    
    static{
        if(logger.isDebugEnabled()){
            logger.debug("Init logger en ProductoController");
        }
    }

    private ProductoController(){
	        this.sesionController = SesionController.getInstance();
	}

    public static ProductoController getInstance(){
        if(controller == null){
            controller = new ProductoController();
        }
        return controller;
    }
    
    /**
     * Filtra insumos por texto de búsqueda
     * @param filtro Texto para buscar
     * @param listener Listener que maneja la respuesta
     */
    public void filtrar(String filtro,String pagina,String cantElementos, DatosListener<List<Producto>> listener) {
        
        JSONObject res = ProductoService.filtrar(filtro,pagina,cantElementos);
        if (res.getInt("status") == 200) {
            try {
                List<Producto> productos = new ObjectMapper().readValue(
                        new JSONArray(res.getString("data")).toString(),
                        new TypeReference<List<Producto>>() {}
                );
                Paginado p = new Paginado();
                p.setPagina(res.getInt("pagina"));
                p.setTamPagina(res.getInt("elementos"));
                p.setTotalElementos(res.getInt("t_elementos"));
                p.setTotalPaginas(res.getInt("t_paginas"));
                listener.onSuccess(productos,p);
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
    public boolean crearProducto(Producto insumo, DatosListener<String> listener) {
        JSONObject insumoRes = ProductoService.crearProducto(insumo);
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
    public boolean editarProducto(Producto insumo, DatosListener<String> listener) {
        JSONObject insumoRes = ProductoService.editarProducto(insumo);
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
    public void eliminarProducto(int id, DatosListener<String> listener) {
        JSONObject insumoRes = ProductoService.eliminarProducto(id);
        if (insumoRes.getInt("status") == 201) {
            listener.onSuccess(insumoRes.optString("mensaje"));
        } else {
            listener.onError(insumoRes.optString("mensaje"));
        }
    }
}