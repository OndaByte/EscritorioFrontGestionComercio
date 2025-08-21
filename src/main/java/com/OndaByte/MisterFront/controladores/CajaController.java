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
                JSONArray movimientosArray = new JSONArray(cajasRes.getString("data"));
                List<HashMap<String, Object>> cajasDTO = new ArrayList<>();

                for (int i = 0; i < movimientosArray.length(); i++) {
                    JSONObject mJson = movimientosArray.getJSONObject(i);
                    Movimiento m = new Movimiento();
                    m.setId(mJson.getInt("id"));
                    m.setDescripcion(mJson.getString("descripcion"));
                    m.setCliente_id(mJson.optIntegerObject("cliente_id", null));
                    m.setTipo_mov(mJson.optString("tipo_mov", null));
                    m.setTotal(mJson.getFloat("total"));
                    m.setCreado(mJson.getString("creado"));
                    m.setUltMod(mJson.getString("ultMod"));

                    HashMap<String, Object> objeto = new HashMap<>();
//                    objeto.put("orden", o);
//                    objeto.put("cliente", c);
                    //   objeto.put("turno", t);
                    objeto.put("movimiento", m);
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
                resumen.put("total_ingresos", data.getDouble("total_ingresos"));
                resumen.put("total_egresos", data.getDouble("total_egresos"));
                resumen.put("total_periodos", data.getDouble("total_periodos"));
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
            aux.setMonto_inicial(String.valueOf(montoI));
            aux.setMonto_actual(String.valueOf(montoI));
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
