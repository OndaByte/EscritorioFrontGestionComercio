
package com.OndaByte.MisterFront.controladores;

import com.OndaByte.MisterFront.modelos.*;
import com.OndaByte.MisterFront.vistas.DatosListener;
import com.OndaByte.MisterFront.servicios.OrdenService;
import com.OndaByte.MisterFront.sesion.SesionController;
import com.OndaByte.MisterFront.vistas.util.Paginado;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrdenController {

    private SesionController sesionController = null;
    private static OrdenController controller;
    private static Logger logger = LogManager.getLogger(OrdenController.class.getName());

    static{
        if(logger.isDebugEnabled()){
            logger.debug("Init logger en OrdenController");
        }
    }
   
    private OrdenController() {
        this.sesionController = SesionController.getInstance();
    }

    public static OrdenController getInstance() {
        if (controller == null) {
            controller = new OrdenController();
        }
        return controller;
    }

/*
 * SIN USO
 */
    private String calcularEstado(Orden o,Pedido pre){
        String e = o.getEstado_orden();
        logger.info(pre.getId());
        //SI no existe el presupuesto pendiente de presupuestar.
        return e;
    }

    /**
     * Filtra ordenes con sus clientes,turnos,pedidos,etc. asociados, usando paginado.
     */
    public void filtrar(String filtro, String desde, String hasta, String estado, String pagina, String cantElementos, DatosListener<List<HashMap<String, Object>>> listener) {
        JSONObject ordenesRes = OrdenService.filtrar(filtro,desde,hasta, estado, pagina, cantElementos);
        if (ordenesRes.getInt("status") == 200) {
            try {
                JSONArray ordenesArray = new JSONArray(ordenesRes.getString("data"));
                List<HashMap<String, Object>> ordenesDetalladas = new ArrayList<>();

                for (int i = 0; i < ordenesArray.length(); i++) {
                    HashMap<String, Object> objeto = new HashMap<>();
                    JSONObject cJson = ordenesArray.getJSONObject(i).getJSONObject("cliente");
                    JSONObject pJson = ordenesArray.getJSONObject(i).getJSONObject("pedido");
                    JSONObject tJson = ordenesArray.getJSONObject(i).getJSONObject("turno");
                    JSONObject preJson = ordenesArray.getJSONObject(i).getJSONObject("presupuesto");
                    JSONObject oJson = ordenesArray.getJSONObject(i).getJSONObject("orden");

                    Cliente c = new Cliente();
                    c.setId(cJson.getInt("id"));
                    c.setNombre(cJson.getString("nombre"));
                    c.setLocalidad(cJson.getString("localidad"));
                    c.setDireccion(cJson.getString("direccion"));
                    c.setProvincia(cJson.getString("provincia"));
                    c.setCuit_cuil(cJson.getString("cuit_cuil"));
                    c.setTelefono(cJson.getString("telefono"));

                    Presupuesto pre = new Presupuesto();
                        pre.setId(preJson.getInt("id"));
                        pre.setPedido_id(pJson.getInt("id"));
                        pre.setNombre(preJson.getString("nombre"));
                        pre.setDescripcion(preJson.getString("descripcion"));
                        pre.setEstado_presupuesto(preJson.getString("estado_presupuesto"));

                    Turno t = new Turno();
                    if(!tJson.isNull("id")){
                        t.setId(tJson.getInt("id"));
                        t.setCreado(tJson.getString("creado").split(" ")[0]);
                        t.setTipo(tJson.getString("tipo"));
                        t.setObservaciones(tJson.optString("observaciones",null));
                        t.setEstadoTurno(tJson.optString("estado_turno","PENDIENTE"));
                        t.setFechaInicio(tJson.getString("fecha_inicio"));
                        t.setFechaFinE(tJson.getString("fecha_fin_e"));
                    }

                    Pedido p = new Pedido();
                    p.setId(pJson.getInt("id"));
                    p.setDescripcion(pJson.optString("descripcion", "No disponible"));
                    p.setFecha_fin_estimada(pJson.optString("fecha_fin_estimada", "No disponible"));
                    p.setEstado_pedido(pJson.optString("estado_pedido", "No disponible"));
//                    p.setCliente_id(pJson.getInt("cliente_id"));
                    p.setCreado(pJson.getString("creado"));

                    Orden o = new Orden();
                    o.setId(oJson.getInt("id"));
                    o.setDescripcion(oJson.getString("descripcion"));
                    o.setTipo(oJson.getString("tipo"));
                    o.setEstado_orden(oJson.getString("estado_orden"));
                    o.setCreado(oJson.getString("creado"));
                    
                //    turnos de mantenimiento.
                            
                    objeto.put("pedido", p);
                    objeto.put("cliente", c);
                    objeto.put("turno", t);
                    objeto.put("presupuesto", pre);
                    objeto.put("orden", o);
                    ordenesDetalladas.add(objeto);
                }

                Paginado p = new Paginado();
                p.setPagina(ordenesRes.getInt("pagina"));
                p.setTamPagina(ordenesRes.getInt("elementos"));
                p.setTotalElementos(ordenesRes.getInt("t_elementos"));
                p.setTotalPaginas(ordenesRes.getInt("t_paginas"));

                listener.onSuccess(ordenesDetalladas, p);
            } catch (Exception e) {
                e.printStackTrace();
                logger.debug(e.getMessage());
                listener.onError("Error al procesar ordenes detalladas");
            }
        } else {
            listener.onError(ordenesRes.optString("mensaje"));
        }
    }

    
    /**
     * Crea un nuevo orden.
     */
    public void crearOrden(Orden orden,DatosListener<String> listener) {
        
        JSONObject o = new JSONObject(orden);
        JSONObject ordenReq = new JSONObject();
        ordenReq.put("orden", o);
        JSONObject ordenRes = OrdenService.crearOrden(ordenReq);
        if (ordenRes.getInt("status") == 201) {
            listener.onSuccess(ordenRes.optString("mensaje"));
        } else {
            listener.onError(ordenRes.optString("mensaje"));
        }
    }

    /**
     * Edita un orden existente.
     */
    public void editarOrden(Orden orden, DatosListener<String> listener) {
        JSONObject ordenRes = OrdenService.editarOrden(orden);
        if (ordenRes.getInt("status") == 201) {
            listener.onSuccess(ordenRes.optString("mensaje"));
        } else {
            listener.onError(ordenRes.optString("mensaje"));
        }
    }
    
    /**
     * Crea un nuevo presupuesto.
     */
    public void actualizarOrden(Orden o, DatosListener<String> listener) { 
        JSONObject res = OrdenService.actualizarOrden(o);
        if (res.getInt("status") == 201) {
            listener.onSuccess(res.optString("mensaje"));
        } else {
            listener.onError(res.optString("mensaje"));
        }
    }
    
    /**
     * Elimina un orden por su ID.
     */
    public void eliminarOrden(int id, DatosListener<String> listener) {
        JSONObject ordenRes = OrdenService.eliminarOrden(id);
        if (ordenRes.getInt("status") == 201) {
            listener.onSuccess(ordenRes.optString("mensaje"));
        } else {
            listener.onError(ordenRes.optString("mensaje"));
        }
    }
}