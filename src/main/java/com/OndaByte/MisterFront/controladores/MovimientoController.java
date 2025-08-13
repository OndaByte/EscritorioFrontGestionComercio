package com.OndaByte.MisterFront.controladores;

import com.OndaByte.MisterFront.modelos.Caja;
import com.OndaByte.MisterFront.modelos.Movimiento;
import com.OndaByte.MisterFront.servicios.MovimientoService;
import com.OndaByte.MisterFront.sesion.SesionController;
import com.OndaByte.MisterFront.vistas.DatosListener;
import com.OndaByte.MisterFront.vistas.util.FechaUtils;
import com.OndaByte.MisterFront.vistas.util.Paginado;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author luciano
 */
public class MovimientoController {

    private SesionController sesionController = null;
    private static MovimientoController instance;
    private static Logger logger = LogManager.getLogger(MovimientoController.class.getName());

    static {
        if (logger.isDebugEnabled()) {
            logger.debug("Init logger en CajaController");
        }
    }

    private MovimientoController() {
        this.sesionController = SesionController.getInstance();
    }

    public static MovimientoController getInstance() {
        if (instance == null) {
            instance = new MovimientoController();
        }
        return instance;
    }

    public void filtrar(String filtro, String desde, String hasta, String estado, String pagina, String cantElementos, DatosListener<List<HashMap<String, Object>>> listener) {
        JSONObject cajasRes = MovimientoService.filtrar(filtro, desde, hasta, estado, pagina, cantElementos);
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

    /**
     * Crea un nuevo remito.
     */
    public boolean crearMovimiento(Movimiento movimiento, DatosListener<String> listener) {
        JSONObject res = MovimientoService.crear(movimiento);
        if (res.getInt("status") == 201) {
            listener.onSuccess(res.optString("mensaje"));
            return true;
        } else {
            listener.onError(res.optString("mensaje"));
            return false;
        }
    }

    /**
     * Crea un nuevo remito.
     */
    public boolean editarMovimiento(Movimiento movimiento, DatosListener<String> listener) {
        JSONObject res = MovimientoService.editar(movimiento);
        if (res.getInt("status") == 201) {
            listener.onSuccess(res.optString("mensaje"));
            return true;
        } else {
            listener.onError(res.optString("mensaje"));
            return false;
        }
    }

    /**
     * Elimina un remito por ID.
     */
    public void eliminarMovimiento(int id, DatosListener<String> listener) {
        JSONObject res = MovimientoService.eliminar(id);
        if (res.getInt("status") == 200) {
            listener.onSuccess(res.optString("mensaje"));
        } else {
            listener.onError(res.optString("mensaje"));
        }
    }

    public void resumenCaja(String filtro, String desde, String hasta, DatosListener<HashMap<String, Object>> listener) {
        JSONObject res = MovimientoService.resumen(filtro, desde, hasta);
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
        JSONObject res = MovimientoService.abrir(montoI);
        if (res.getInt("status") == 201) {
            Caja aux = new Caja();
            aux.setId((new JSONObject(res.getString("data"))).getInt("id"));
            SesionController.getInstance().setSesionCaja(aux);
            listener.onSuccess(res.optString("mensaje"));

        } else {
            listener.onError(res.optString("mensaje"));
        }
    }

    public void cerrarCaja(DatosListener<String> listener) {
        Caja aux = new Caja();
        JSONObject res = MovimientoService.cerrar();
        if (res.getInt("status") == 201) {
            SesionController.getInstance().setSesionCaja(null);
            listener.onSuccess(res.optString("mensaje"));

        } else {
            listener.onError(res.optString("mensaje"));
        }
    }
}
