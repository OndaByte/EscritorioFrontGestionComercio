
package com.OndaByte.MisterFront.controladores;

import com.OndaByte.MisterFront.modelos.Usuario;
import com.OndaByte.MisterFront.servicios.UsuarioService;
import com.OndaByte.MisterFront.sesion.SesionController;
import com.OndaByte.MisterFront.vistas.DatosListener;
import com.OndaByte.MisterFront.vistas.util.Paginado;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;

public class UsuarioController {

    private SesionController sesionController = null;
    private static UsuarioController instance;

    private static Logger logger = LogManager.getLogger(UsuarioController.class.getName());

    static{
        if(logger.isDebugEnabled()){
            logger.debug("Init logger en UsuarioController");
        }
    }

    private UsuarioController() {
        this.sesionController = SesionController.getInstance();
    }

    public static UsuarioController getInstance() {
        if (instance == null) {
            instance = new UsuarioController();
        }
        return instance;
    }

    /**
     * Filtra usuarios por texto de búsqueda
     * @param filtro Texto para buscar
     * @param listener Listener que maneja la respuesta
     */
    public void filtrar(String filtro, DatosListener<List<Usuario>> listener) {
        JSONObject res = UsuarioService.filtrar(filtro);
        if (res.getInt("status") == 200) {
            try {
                List<Usuario> usuarios = new ObjectMapper().readValue(
                        new JSONArray(res.getString("data")).toString(),
                        new TypeReference<List<Usuario>>() {}
                );
                listener.onSuccess(usuarios);
            } catch (Exception e) {
                listener.onError("Error procesando usuarios");
            }
        } else {
            listener.onError(res.optString("mensaje"));
        }
    }

    public void filtrar(String filtro,String pagina,String cantElementos, DatosListener<List<Usuario>> listener) {

        JSONObject res = UsuarioService.filtrar(filtro,pagina,cantElementos);
        if (res.getInt("status") == 200) {
            try {
                List<Usuario> usuarios = new ObjectMapper().readValue(
                        new JSONArray(res.getString("data")).toString(),
                        new TypeReference<List<Usuario>>() {}
                );
                Paginado p = new Paginado();
                p.setPagina(res.getInt("pagina"));
                p.setTamPagina(res.getInt("elementos"));
                p.setTotalElementos(res.getInt("t_elementos"));
                p.setTotalPaginas(res.getInt("t_paginas"));
                listener.onSuccess(usuarios,p);
            } catch (Exception e) {
                listener.onError("Error procesando usuarios");
            }
        } else {
            listener.onError(res.optString("mensaje"));
        }
    }

    /**
     * Obtiene un usuario por su ID
     * @param id ID del usuario
     * @param listener Listener que maneja la respuesta
     */

    /*
     * SIN USO
     */
    public void getUsuarioById(int id, DatosListener<Usuario> listener) {
        JSONObject res = UsuarioService.getUsuarioById(id);
        if (res.getInt("status") == 200) {
            try {
                Usuario usuario = new ObjectMapper().readValue(
                        new JSONObject(res.getString("data")).toString(),
                        new TypeReference<Usuario>() {}
                );
                listener.onSuccess(usuario);
            } catch (Exception e) {
                listener.onError("Error procesando usuario");
            }
        } else {
            listener.onError(res.optString("mensaje"));
        }
    }

    /**
     * Crea un nuevo usuario
     * @param usuario Objeto Usuario
     * @param listener Listener que maneja la respuesta
     */
    public boolean crearUsuario(Usuario usuario, DatosListener<String> listener) {
        JSONObject res = UsuarioService.crearUsuario(usuario);
        if (res.getInt("status") == 201) {
            listener.onSuccess(res.optString("mensaje"));
            return true;
        } else {
            listener.onError(res.optString("mensaje"));
            return false;
        }
    }

    /**
     * Edita un usuario existente
     * @param usuario Objeto Usuario con datos modificados
     * @param listener Listener que maneja la respuesta
     */
    public boolean editarUsuario(Usuario usuario, DatosListener<String> listener) {
        JSONObject res = UsuarioService.editarUsuario(usuario);
        if (res.getInt("status") == 201) {
            listener.onSuccess(res.optString("mensaje"));
            return true;
        } else {
            listener.onError(res.optString("mensaje"));
            return false;
        }
    }


    /**
     * Edita un usuario existente
     * @param usuario Objeto Usuario con datos modificados
     * @param listener Listener que maneja la respuesta
     */
    public boolean editarUsuario(Usuario usuario, String rol, DatosListener<String> listener) {
        //JSONObject res = UsuarioService.editarUsuario(usuario);
        JSONObject res = UsuarioService.cambiarRolUsuario(usuario, rol);
        if (res.getInt("status") == 201) {
            listener.onSuccess(res.optString("mensaje"));
            return true;
        } else {
            listener.onError(res.optString("mensaje"));
            return false;
        }
    }

    /**
     * Elimina un usuario por ID
     * @param id ID del usuario a eliminar
     * @param listener Listener que maneja la respuesta
     */
    public void eliminarUsuario(int id, DatosListener<String> listener) {
        JSONObject res = UsuarioService.eliminarUsuario(id);
        if (res.getInt("status") == 201) {
            listener.onSuccess(res.optString("mensaje"));
        } else {
            listener.onError(res.optString("mensaje"));
        }
    }


    /**
     * Cambia contraseña del usuario en sesion
     * @param listener Listener que maneja la respuesta
     */
    public boolean cambiarPassword(String pass, String nueva, DatosListener<String> listener) {
        JSONObject res = UsuarioService.cambiarContraUsuario(pass, nueva);
        if (res.getInt("status") == 201) {
            listener.onSuccess(res.optString("mensaje"));
            return true;
        } else {
            listener.onError(res.optString("mensaje"));
            return false;
        }
    }
}
