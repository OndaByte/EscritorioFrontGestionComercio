
package com.OndaByte.AntartidaFront.controladores;

import com.OndaByte.AntartidaFront.modelos.Cliente;
import com.OndaByte.AntartidaFront.modelos.Orden;
import com.OndaByte.AntartidaFront.modelos.Pedido;
import com.OndaByte.AntartidaFront.vistas.DatosListener;
import com.OndaByte.AntartidaFront.modelos.Turno;
import com.OndaByte.AntartidaFront.servicios.TurnoService;
import com.OndaByte.AntartidaFront.sesion.SesionController;
import com.OndaByte.AntartidaFront.vistas.util.Paginado;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TurnoController {

    private SesionController sesionController = null;
    private static TurnoController controller;
    private static Logger logger = LogManager.getLogger(TurnoController.class.getName());

    static{
        if(logger.isDebugEnabled()){
            logger.debug("Init logger en TurnoController");
        }
    }

    private TurnoController() {
        this.sesionController = SesionController.getInstance();
    }

    public static TurnoController getInstance() {
        if (controller == null) {
            controller = new TurnoController();
        }
        return controller;
    }

    /*
     * SIN USO
     */
    private String calcularEstado(Turno t){
        String e = t.getEstado();
        logger.info(t.getFechaFinE());
        //La fecha.
        return e;
    }

    /**
     * Filtra turnos con sus clientes asociados, usando paginado.
     */
    public void filtrar(String filtro, String desde, String hasta, String tipo, String pagina, String cantElementos, DatosListener<List<HashMap<String, Object>>> listener) {
        JSONObject turnosRes = TurnoService.filtrar(filtro,desde,hasta, tipo, pagina, cantElementos);
        if (turnosRes.getInt("status") == 200) {
            try {
                JSONArray turnosArray = new JSONArray(turnosRes.getString("data"));
                List<HashMap<String, Object>> allTurnos = new ArrayList<>();

                for (int i = 0; i < turnosArray.length(); i++) {

                    HashMap<String, Object> objeto = new HashMap<>();
                    JSONObject tJson = turnosArray.getJSONObject(i).getJSONObject("turno");
                    JSONObject cJson = turnosArray.getJSONObject(i).getJSONObject("cliente");
                    JSONObject pJson = null;
                    JSONObject oJson = null;
                    Pedido p = null;
                    Orden o = null;

                    Turno t = new Turno();
                    t.setId(tJson.getInt("id"));
                    t.setCreado(tJson.getString("creado").split(" ")[0]);
                    t.setTipo(tJson.getString("tipo"));
                    t.setObservaciones(tJson.optString("observaciones",null));
                    t.setEstadoTurno(tJson.optString("estado_turno","PENDIENTE"));
                    t.setFechaInicio(tJson.getString("fecha_inicio"));
                    t.setFechaFinE(tJson.getString("fecha_fin_e"));
                    objeto.put("turno", t);

                    Cliente c = new Cliente();
                    c.setId(cJson.getInt("id"));
                    c.setNombre(cJson.getString("nombre"));
                    c.setTelefono(cJson.getString("telefono"));
                    objeto.put("cliente", c);

                    if(turnosArray.getJSONObject(i).has("pedido")){
                        pJson = turnosArray.getJSONObject(i).getJSONObject("pedido");
                        p = new Pedido();
                        p.setId(pJson.getInt("id"));
                        p.setDescripcion(pJson.getString("descripcion"));
                        p.setFecha_fin_estimada(pJson.optString("fecha_fin_estimada", null));
                        p.setEstado_pedido(pJson.getString("estado_pedido"));
                        p.setCreado(pJson.getString("creado"));
                        objeto.put("pedido", p);
                    }

                    if(turnosArray.getJSONObject(i).has("orden")){
                        oJson = turnosArray.getJSONObject(i).getJSONObject("orden");
                        o = new Orden();
                        o.setId(oJson.getInt("id"));
                        o.setDescripcion(oJson.getString("descripcion"));
                        o.setEstado_orden(oJson.getString("estado_orden"));
                        o.setTipo(oJson.getString("tipo"));
                        o.setCreado(oJson.getString("creado"));
                        objeto.put("orden", o);
                    }

                    allTurnos.add(objeto);
                }

                Paginado p = new Paginado();
                p.setPagina(turnosRes.getInt("pagina"));
                p.setTamPagina(turnosRes.getInt("elementos"));
                p.setTotalElementos(turnosRes.getInt("t_elementos"));
                p.setTotalPaginas(turnosRes.getInt("t_paginas"));

                listener.onSuccess(allTurnos, p);
            } catch (Exception e) {
                e.printStackTrace();
                logger.debug(e.getMessage());
                listener.onError("Error al procesar turnos");
            }
        } else {
            listener.onError(turnosRes.optString("mensaje"));
        }
    }

    /**
     * Crea un nuevo turno.
     */
    public void crearTurnoPedido(Turno turno, int pedidoId, DatosListener<String> listener) {
        JSONObject turnoRes = TurnoService.crearTurnoPedido(turno, pedidoId);
        if (turnoRes.getInt("status") == 201) {
            listener.onSuccess(turnoRes.optString("mensaje"));
        } else {
            listener.onError(turnoRes.optString("mensaje"));
        }
    }

    /**
     * Crea un nuevo turno.
     */
    public void crearTurnoOrden(Turno turno, int ordenId, DatosListener<String> listener) {
        JSONObject turnoRes = TurnoService.crearTurnoOrden(turno, ordenId);
        if (turnoRes.getInt("status") == 201) {
            listener.onSuccess(turnoRes.optString("mensaje"));
        } else {
            listener.onError(turnoRes.optString("mensaje"));
        }
    }

    /**
     * Edita un turno existente.
     */
    public void editarTurno(Turno turno, DatosListener<String> listener) {
        JSONObject turnoRes = TurnoService.editarTurno(turno);
        if (turnoRes.getInt("status") == 201) {
            listener.onSuccess(turnoRes.optString("mensaje"));
        } else {
            listener.onError(turnoRes.optString("mensaje"));
        }
    }

    /**
     * Elimina un turno por su ID.
    public void eliminarTurno(int id, DatosListener<String> listener) {
        JSONObject turnoRes = TurnoService.eliminarTurno(id);
        if (turnoRes.getInt("status") == 201) {
            listener.onSuccess(turnoRes.optString("mensaje"));
        } else {
            listener.onError(turnoRes.optString("mensaje"));
        }
    }
     */

}