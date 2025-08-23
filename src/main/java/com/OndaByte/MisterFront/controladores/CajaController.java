package com.OndaByte.MisterFront.controladores;

import com.OndaByte.MisterFront.modelos.Caja; 
import com.OndaByte.MisterFront.modelos.Movimiento; 
import com.OndaByte.MisterFront.servicios.CajaService;
import com.OndaByte.MisterFront.sesion.SesionController;
import com.OndaByte.MisterFront.vistas.DatosListener; 
import com.OndaByte.MisterFront.vistas.util.Paginado;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class CajaController {

    private SesionController sesionController = null;
    private static CajaController instance;
    private static Logger logger = LogManager.getLogger(CajaController.class.getName());

    static {
        if (logger.isDebugEnabled()) {
            logger.debug("Init logger en CajaController");
        }
    }

    private CajaController() {
        this.sesionController = SesionController.getInstance();
    }

    public static CajaController getInstance() {
        if (instance == null) {
            instance = new CajaController();
        }
        return instance;
    }
    /**
     * Sesiones
     */
    public void filtrar(String filtro, String desde, String hasta, String estado, String pagina, String cantElementos, DatosListener<List<HashMap<String, Object>>> listener) {
        JSONObject cajasRes = CajaService.filtrar(filtro, desde, hasta, estado, pagina, cantElementos);
        if (cajasRes.getInt("status") == 200) {
            try {
                JSONArray sesionesArray = new JSONArray(cajasRes.getString("data"));
                List<HashMap<String, Object>> cajasDTO = new ArrayList<>();

                for (int i = 0; i < sesionesArray.length(); i++) {
                    JSONObject scJson = sesionesArray.getJSONObject(i).getJSONObject("sesion");
                    Caja sc = new Caja();
                    sc.setId(scJson.getInt("id"));
                    sc.setMonto_inicial(scJson.getFloat("monto_inicial"));
                    sc.setMonto_final(scJson.optFloatObject("monto_final",null)); 
                    sc.setApertura(scJson.getString("apertura"));
                    sc.setCierre(scJson.optString("cierre",null));          
                    sc.setCajero_id(scJson.getInt("cajero_id"));
                    //sc.setCaja_id(scJson.getInt("scaja_id"));

                  
                    HashMap<String, Object> objeto = new HashMap<>();
//                    objeto.put("orden", o);
//                    objeto.put("cliente", c);
                    //   objeto.put("turno", t);
                    objeto.put("sesion", sc);
                    cajasDTO.add(objeto);
                }

                Paginado p = new Paginado();
                p.setPagina(cajasRes.getInt("pagina"));
                p.setTamPagina(cajasRes.getInt("elementos"));
                p.setTotalElementos(cajasRes.getInt("t_elementos"));
                p.setTotalPaginas(cajasRes.getInt("t_paginas"));

                listener.onSuccess(cajasDTO, p);
            } catch (Exception e) {
                e.printStackTrace();
                logger.debug(e.getMessage());
                listener.onError("Error al procesar cajas");
            }
        } else {
            listener.onError(cajasRes.optString("mensaje"));
        }
    }
    
    public void resumenSesionesCaja(String filtro, String desde, String hasta, DatosListener<HashMap<String, Object>> listener) {
        JSONObject res = CajaService.resumen(filtro, desde, hasta);
        if (res != null && res.getInt("status") == 200) {
            try {
                JSONObject data = new JSONObject(res.getString("data"));
                HashMap<String, Object> resumen = new HashMap<>();
                resumen.put("total_sesiones", data.getInt("total_sesiones"));
                resumen.put("sesiones_abiertas", data.getInt("sesiones_abiertas"));
                resumen.put("sesiones_cerradas", data.getInt("sesiones_cerradas"));
                resumen.put("monto_inicial_total", data.getFloat("monto_inicial_total"));
                resumen.put("monto_final_total", data.getFloat("monto_final_total"));
                resumen.put("promedio_monto_inicial", data.getFloat("promedio_monto_inicial"));
                resumen.put("promedio_monto_final", data.getFloat("promedio_monto_final"));
                resumen.put("primera_apertura", data.getString("primera_apertura"));
                resumen.put("ultima_actividad", data.getString("ultima_actividad"));
                resumen.put("diferencia_total_cerradas", data.getFloat("diferencia_total_cerradas"));
                resumen.put("diferencia_promedio_cerradas", data.getFloat("diferencia_promedio_cerradas"));
                resumen.put("monto_inicial_total_cerradas", data.getFloat("monto_inicial_total_cerradas"));
                resumen.put("monto_final_total_cerradas", data.getFloat("monto_final_total_cerradas"));

                listener.onSuccess(resumen);
            } catch (Exception e) {
                listener.onError("Error al parsear resumen");
            }
        } else {
            listener.onError(res != null ? res.optString("mensaje") : "Error de conexión");
        }
    }

    public void abrirCaja(Float montoI, DatosListener<String> listener) {
        //CajaService.cerrar();
        JSONObject res = CajaService.abrir(montoI);
        if (res.getInt("status") == 201) {
            Caja aux = new Caja();
            aux.setId((new JSONObject(res.getString("data"))).getInt("id"));
            aux.setMonto_inicial(montoI);
            aux.setMonto_actual(montoI);
            SesionController.getInstance().setSesionCaja(aux);
            listener.onSuccess(res.optString("mensaje"));

        } else {
            listener.onError(res.optString("mensaje"));
        }
    }

    public void cerrarCaja(DatosListener<String> listener) {
        Caja aux = new Caja();
        JSONObject res = CajaService.cerrar();
        if (res.getInt("status") == 201) {
            SesionController.getInstance().setSesionCaja(null);
            listener.onSuccess(res.optString("mensaje"));

        } else {
            listener.onError(res.optString("mensaje"));
        }
    }

    public void obtenerUltimaSesion(int id, DatosListener<String> listener) {
        JSONObject res = CajaService.obtenerUltimaSesion(id);
        if (res.getInt("status") == 200) {
            JSONObject data = new JSONObject(res.getString("data"));
            int sesionID = data.getInt("id");
            listener.onSuccess(""+sesionID);
        } else {
            listener.onError(res.optString("mensaje"));
        }
    }
}
