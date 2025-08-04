package com.OndaByte.AntartidaFront.controladores;

import com.OndaByte.AntartidaFront.modelos.Empleado;
import com.OndaByte.AntartidaFront.servicios.EmpleadoService;
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

public class EmpleadoController {

    private SesionController sesionController = null;
    private static EmpleadoController instance;
    private static Logger logger = LogManager.getLogger(EmpleadoController.class.getName());

    static{
        if(logger.isDebugEnabled()){
            logger.debug("Init logger en EmpleadoController");
        }
    }

    private EmpleadoController() {
	        this.sesionController = SesionController.getInstance();
	}

    public static EmpleadoController getInstance() {
        if (instance == null) {
            instance = new EmpleadoController();
        }
        return instance;
    }

    /**
     * Filtra empleados por texto de búsqueda
     * @param filtro Texto para buscar
     * @param listener Listener que maneja la respuesta
     */
    public void filtrar(String filtro, DatosListener<List<Empleado>> listener) {
        JSONObject res = EmpleadoService.filtrar(filtro);
        if (res.getInt("status") == 200) {
            try {
                List<Empleado> empleados = new ObjectMapper().readValue(
                        new JSONArray(res.getString("data")).toString(),
                        new TypeReference<List<Empleado>>() {}
                );
                listener.onSuccess(empleados);
            } catch (Exception e) {
                listener.onError("Error procesando empleados");
            }
        } else {
            listener.onError(res.optString("mensaje"));
        }
    }

    public void filtrar(String filtro,String pagina,String cantElementos, DatosListener<List<Empleado>> listener) {

        JSONObject res = EmpleadoService.filtrar(filtro,pagina,cantElementos);
        if (res.getInt("status") == 200) {
            try {
                List<Empleado> empleados = new ObjectMapper().readValue(
                        new JSONArray(res.getString("data")).toString(),
                        new TypeReference<List<Empleado>>() {}
                );
                Paginado p = new Paginado();
                p.setPagina(res.getInt("pagina"));
                p.setTamPagina(res.getInt("elementos"));
                p.setTotalElementos(res.getInt("t_elementos"));
                p.setTotalPaginas(res.getInt("t_paginas"));
                listener.onSuccess(empleados,p);
            } catch (Exception e) {
                listener.onError("Error procesando empleados");
            }
        } else {
            listener.onError(res.optString("mensaje"));
        }
    }

    /**
     * Obtiene un empleado por su ID
     * @param id ID del empleado
     * @param listener Listener que maneja la respuesta
     */

    /*
     * SIN USO
     */
    public void getEmpleadoById(int id, DatosListener<Empleado> listener) {
        JSONObject res = EmpleadoService.getEmpleadoById(id);
        if (res.getInt("status") == 200) {
            try {
                Empleado empleado = new ObjectMapper().readValue(
                        new JSONObject(res.getString("data")).toString(),
                        new TypeReference<Empleado>() {}
                );
                listener.onSuccess(empleado);
            } catch (Exception e) {
                listener.onError("Error procesando empleado");
            }
        } else {
            listener.onError(res.optString("mensaje"));
        }
    }

    /**
     * Crea un nuevo empleado
     * @param empleado Objeto Empleado
     * @param listener Listener que maneja la respuesta
     */
    public boolean crearEmpleado(Empleado empleado, DatosListener<String> listener) {
        JSONObject res = EmpleadoService.crearEmpleado(empleado);
        if (res.getInt("status") == 201) {
            listener.onSuccess(res.optString("mensaje"));
            return true;
        } else {
            listener.onError(res.optString("mensaje"));
            return false;
        }
    }

    /**
     * Edita un empleado existente
     * @param empleado Objeto Empleado con datos modificados
     * @param listener Listener que maneja la respuesta
     */
    public boolean editarEmpleado(Empleado empleado, DatosListener<String> listener) {
        JSONObject res = EmpleadoService.editarEmpleado(empleado);
        if (res.getInt("status") == 201) {
            listener.onSuccess(res.optString("mensaje"));
            return true;
        } else {
            listener.onError(res.optString("mensaje"));
            return false;
        }
    }

    /**
     * Elimina un empleado por ID
     * @param id ID del empleado a eliminar
     * @param listener Listener que maneja la respuesta
     */
    public void eliminarEmpleado(int id, DatosListener<String> listener) {
        JSONObject res = EmpleadoService.eliminarEmpleado(id);
        if (res.getInt("status") == 201) {
            listener.onSuccess(res.optString("mensaje"));
        } else {
            listener.onError(res.optString("mensaje"));
        }
    }

}
