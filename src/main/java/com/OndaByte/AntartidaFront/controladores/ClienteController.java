
package com.OndaByte.AntartidaFront.controladores;

import com.OndaByte.AntartidaFront.modelos.Cliente;
import com.OndaByte.AntartidaFront.servicios.ClienteService;
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

public class ClienteController {

    private SesionController sesionController = null;
    private static ClienteController instance;
    private static Logger logger = LogManager.getLogger(ClienteController.class.getName());

    static{
        if(logger.isDebugEnabled()){
            logger.debug("Init logger en ClienteController");
        }
    }
	
    private ClienteController() {
	        this.sesionController = SesionController.getInstance();
	}

    public static ClienteController getInstance() {
        if (instance == null) {
            instance = new ClienteController();
        }
        return instance;
    }

    /**
     * Filtra clientes por texto de búsqueda
     * @param filtro Texto para buscar
     * @param listener Listener que maneja la respuesta
     */
    public void filtrar(String filtro, DatosListener<List<Cliente>> listener) {
        JSONObject res = ClienteService.filtrar(filtro);
        if (res.getInt("status") == 200) {
            try {
                List<Cliente> clientes = new ObjectMapper().readValue(
                        new JSONArray(res.getString("data")).toString(),
                        new TypeReference<List<Cliente>>() {}
                );
                listener.onSuccess(clientes);
            } catch (Exception e) {
                listener.onError("Error procesando clientes");
            }
        } else {
            listener.onError(res.optString("mensaje"));
        }
    }

    /**
     * Filtra clientes por texto de búsqueda
     * @param filtro Texto para buscar
     * @param listener Listener que maneja la respuesta
     */
    public void filtrar(String filtro,String pagina,String cantElementos, DatosListener<List<Cliente>> listener) {

        JSONObject res = ClienteService.filtrar(filtro,pagina,cantElementos);
        if (res.getInt("status") == 200) {
            try {
                List<Cliente> clientes = new ObjectMapper().readValue(
                        new JSONArray(res.getString("data")).toString(),
                        new TypeReference<List<Cliente>>() {}
                );
                Paginado p = new Paginado();
                p.setPagina(res.getInt("pagina"));
                p.setTamPagina(res.getInt("elementos"));
                p.setTotalElementos(res.getInt("t_elementos"));
                p.setTotalPaginas(res.getInt("t_paginas"));
                listener.onSuccess(clientes,p);
            } catch (Exception e) {
                listener.onError("Error procesando clientes");
            }
        } else {
            listener.onError(res.optString("mensaje"));
        }
    }

    /**
     * Obtiene un cliente por su ID
     * @param id ID del cliente
     * @param listener Listener que maneja la respuesta
     */
    public void getClienteById(int id, DatosListener<Cliente> listener) {
        JSONObject res = ClienteService.getClienteById(id);
        if (res.getInt("status") == 200) {
            try {
                Cliente cliente = new ObjectMapper().readValue(
                        new JSONObject(res.getString("data")).toString(),
                        new TypeReference<Cliente>() {}
                );
                listener.onSuccess(cliente);
            } catch (Exception e) {
                listener.onError("Error procesando cliente");
            }
        } else {
            listener.onError(res.optString("mensaje"));
        }
    }

    /**
     * Crea un nuevo cliente
     * @param cliente Objeto Cliente
     * @param listener Listener que maneja la respuesta
     */
    public boolean crearCliente(Cliente cliente, DatosListener<String> listener) {
        JSONObject res = ClienteService.crearCliente(cliente);
        if (res.getInt("status") == 201) {
            listener.onSuccess(res.optString("mensaje"));
            return true;
        } else {
            listener.onError(res.optString("mensaje"));
            return false;
        }
    }

    /**
     * Edita un cliente existente
     * @param cliente Objeto Cliente con datos modificados
     * @param listener Listener que maneja la respuesta
     */
    public boolean editarCliente(Cliente cliente, DatosListener<String> listener) {
        JSONObject res = ClienteService.editarCliente(cliente);
        if (res.getInt("status") == 201) {
            listener.onSuccess(res.optString("mensaje"));
            return true;
        } else {
            listener.onError(res.optString("mensaje"));
            return false;
        }
    }

    /**
     * Elimina un cliente por ID
     * @param id ID del cliente a eliminar
     * @param listener Listener que maneja la respuesta
     */
    public void eliminarCliente(int id, DatosListener<String> listener) {
        JSONObject res = ClienteService.eliminarCliente(id);
        if (res.getInt("status") == 201) {
            listener.onSuccess(res.optString("mensaje"));
        } else {
            listener.onError(res.optString("mensaje"));
        }
    }
}