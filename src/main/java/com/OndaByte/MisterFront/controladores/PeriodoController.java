
package com.OndaByte.MisterFront.controladores;

import com.OndaByte.MisterFront.modelos.Gasto;
import com.OndaByte.MisterFront.modelos.Periodo;
import com.OndaByte.MisterFront.servicios.PeriodoService;
import com.OndaByte.MisterFront.sesion.SesionController;
import com.OndaByte.MisterFront.vistas.DatosListener;
import com.OndaByte.MisterFront.vistas.util.Paginado;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class PeriodoController {

    private SesionController sesionController = null;
    private static PeriodoController instance;
    private static Logger logger = LogManager.getLogger(PedidoController.class.getName());
    
    static{
        if(logger.isDebugEnabled()){
            logger.debug("Init logger en PeriodoController");
        }
    }
        
    private PeriodoController() {
        this.sesionController = SesionController.getInstance();
    }

    public static PeriodoController getInstance() {
        if (instance == null) {
            instance = new PeriodoController();
        }
        return instance;
    }

    public void filtrar(String filtro, DatosListener<List<Periodo>> listener) {
        JSONObject res = PeriodoService.filtrar(filtro);
        if (res.getInt("status") == 200) {
            try {
                List<Periodo> periodos = new ObjectMapper().readValue(new JSONArray(res.getString("data")).toString(),
                                                                    new TypeReference<List<Periodo>>() {}
                                                                    );
                listener.onSuccess(periodos);
            } catch (Exception e) {
                listener.onError("Error procesando clientes");
            }
        } else {
            listener.onError(res.optString("mensaje"));
        }
    }

    public void filtrar(String filtro, String desde, String hasta, String estado, String pagina, String cantElementos, DatosListener<List<HashMap<String, Object>>> listener) {
        JSONObject periodos = PeriodoService.filtrar(filtro,desde,hasta, estado, pagina, cantElementos);
        if (periodos.getInt("status") == 200) {
            try {
                JSONArray periodosArray = new JSONArray(periodos.getString("data"));
                List<HashMap<String, Object>> periodosDetalle = new ArrayList<>();

                for (int i = 0; i < periodosArray.length(); i++) {
                    
                    HashMap<String, Object> objeto = new HashMap<>();
                    JSONObject pJson = periodosArray.getJSONObject(i).getJSONObject("periodo");
                    JSONObject gJson = periodosArray.getJSONObject(i).getJSONObject("gasto");               
                    
                    Gasto g = new Gasto();
                    g.setId(gJson.getInt("id"));
                    g.setNombre(gJson.getString("nombre"));
                    g.setRepeticion(gJson.getInt("repeticion"));
                    g.setEstado(gJson.getString("estado"));


                    Periodo p = new Periodo();
                    p.setId(pJson.getInt("id"));
                    p.setCosto(pJson.getFloat("costo"));
                    p.setPeriodo(pJson.getString("periodo"));

                    objeto.put("periodo", p);
                    objeto.put("gasto", g);
                   
                    periodosDetalle.add(objeto);
                }

                Paginado p = new Paginado();
                p.setPagina(periodos.getInt("pagina"));
                p.setTamPagina(periodos.getInt("elementos"));
                p.setTotalElementos(periodos.getInt("t_elementos"));
                p.setTotalPaginas(periodos.getInt("t_paginas"));
                p.setCosto_total(periodos.getFloat("costo_total"));

                listener.onSuccess(periodosDetalle, p);
            } catch (Exception e) {
                e.printStackTrace();
                logger.debug(e.getMessage());
                listener.onError("Error al procesar pedidos y clientes");
            }
        } else {
            listener.onError(periodos.optString("mensaje"));
        }
    }

/*
 * SIN USO
 */
    public void getPeriodoById(int id, DatosListener<Periodo> listener) {
        JSONObject res = PeriodoService.getPeriodoById(id);
        if (res.getInt("status") == 200) {
            try {
                Periodo periodo = new ObjectMapper().readValue(new JSONObject(res.getString("data")).toString(),
                                                           new TypeReference<Periodo>() {}
                                                           );
                listener.onSuccess(periodo);
            } catch (Exception e) {
                listener.onError("Error procesando cliente");
            }
        } else {
            listener.onError(res.optString("mensaje"));
        }
    }
    
    public boolean crearGastoFijo(Gasto gasto, Periodo periodo, DatosListener<String> listener) {
        JSONObject res = PeriodoService.crearGastoFijo(gasto,periodo);
        if (res.getInt("status") == 201) {
            listener.onSuccess(res.optString("mensaje"));
            return true;
        } else {
            listener.onError(res.optString("mensaje"));
            return false;
        }
    }

    
    public boolean editarGastoFijo(Gasto gasto, Periodo periodo, DatosListener<String> listener) {
        JSONObject res = PeriodoService.editarGastoFijo(gasto,periodo);
        if (res.getInt("status") == 201) {
            listener.onSuccess(res.optString("mensaje"));
            return true;
        } else {
            listener.onError(res.optString("mensaje"));
            return false;
        }
    }

    public void eliminarGastoFijo(int id, DatosListener<String> listener) {
        JSONObject res = PeriodoService.eliminarGastoFijo(id);
        if (res.getInt("status") == 201) {
            listener.onSuccess(res.optString("mensaje"));
        } else {
            listener.onError(res.optString("mensaje"));
        }
    }
}
