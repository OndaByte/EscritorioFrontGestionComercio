
package com.OndaByte.MisterFront.controladores;

import com.OndaByte.MisterFront.modelos.Cliente;
import com.OndaByte.MisterFront.modelos.ItemRemito;
import com.OndaByte.MisterFront.modelos.Orden;
import com.OndaByte.MisterFront.modelos.Remito;
import com.OndaByte.MisterFront.servicios.RemitoService;
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

public class RemitoController {
    
    private SesionController sesionController = null;
    private static RemitoController instance;
    private static Logger logger = LogManager.getLogger(RemitoController.class.getName());
       
    static{
        if(logger.isDebugEnabled()){
            logger.debug("Init logger en RemitoController");
        }
    }

    private RemitoController() {
        this.sesionController = SesionController.getInstance();
    }

    public static RemitoController getInstance() {
        if (instance == null) {
            instance = new RemitoController();
        }
        return instance;
    }

    public void filtrar(String filtro, String desde, String hasta, String estado, String pagina, String cantElementos, DatosListener<List<HashMap<String, Object>>> listener) {
        JSONObject remitosRes = RemitoService.filtrar(filtro,desde,hasta, estado, pagina, cantElementos);
        if (remitosRes.getInt("status") == 200) {
            try {
                JSONArray remitosArray = new JSONArray(remitosRes.getString("data"));
                List<HashMap<String, Object>> remitosDTO = new ArrayList<>();

                for (int i = 0; i < remitosArray.length(); i++) {

                    JSONObject rJson = remitosArray.getJSONObject(i).getJSONObject("remito");
                    JSONObject cJson = remitosArray.getJSONObject(i).getJSONObject("cliente");
                    JSONObject oJson = remitosArray.getJSONObject(i).getJSONObject("orden");
//                  JSONObject preJson = remitosArray.getJSONObject(i).getJSONObject("presupuesto");

                    Remito r = new Remito();
                    r.setId(rJson.getInt("id"));
                    r.setFecha_emision(rJson.getString("fecha_emision"));
                    r.setFecha_pago(rJson.optString("fecha_pago",null));
                    r.setTotal(rJson.getFloat("total"));
                    r.setObservaciones(rJson.optString("observaciones",null));
                    r.setC_cuit_cuil(rJson.getString("cliente_cuit_cuil"));
                    r.setC_domicilio(rJson.getString("cliente_domicilio"));
                    r.setC_localidad(rJson.getString("cliente_localidad"));
                    r.setC_nombre(rJson.getString("cliente_nombre"));
                    r.setC_telefono(rJson.getString("cliente_telefono"));
                    r.setPunto_venta(rJson.getString("punto_venta"));
                    r.setNro_remito(rJson.getInt("nro_remito"));


                    Cliente c = new Cliente();
                    c.setId(cJson.getInt("id"));
                    c.setNombre(cJson.getString("nombre"));
                    c.setTelefono(cJson.getString("telefono"));
                    
//                    Presupuesto pre = new Presupuesto();
//                    if(!preJson.isNull("id")){
//                        pre.setId(preJson.optInt("id"));
//                        pre.setNombre(preJson.getString("nombre"));
//                        pre.setDescripcion(preJson.getString("descripcion"));
//                        pre.setEstado_presupuesto(preJson.getString("estado_presupuesto"));
//
//                    }
                    
                    Orden o = new Orden();
                    o.setId(oJson.getInt("id"));
                    o.setDescripcion(oJson.optString("descripcion",null));
                    o.setFecha_fin(oJson.optString("fecha_fin",null));
                    o.setTipo(oJson.getString("tipo"));
                    o.setEstado_orden(oJson.getString("estado_orden"));
                    o.setCreado(oJson.getString("creado"));

                    HashMap<String, Object> objeto = new HashMap<>();
                    objeto.put("orden", o);
                    objeto.put("cliente", c);
                 //   objeto.put("turno", t);
                    objeto.put("remito", r);
                    remitosDTO.add(objeto);
                }

                Paginado p = new Paginado();
                p.setPagina(remitosRes.getInt("pagina"));
                p.setTamPagina(remitosRes.getInt("elementos"));
                p.setTotalElementos(remitosRes.getInt("t_elementos"));
                p.setTotalPaginas(remitosRes.getInt("t_paginas"));

                listener.onSuccess(remitosDTO, p);
            } catch (Exception e) {
                e.printStackTrace();
                logger.debug(e.getMessage());
                listener.onError("Error al procesar remitos");
            }
        } else {
            listener.onError(remitosRes.optString("mensaje"));
        }
    }
    
    public void buscarRemito(int id, DatosListener<HashMap<String, Object>> listener) { 
        JSONObject res = RemitoService.buscarRemito(id);
        if (res.getInt("status") == 200) {
            try {
                JSONObject rDetalle = new JSONObject(res.getString("data"));
                HashMap<String, Object> remitoDTO = new HashMap<>();
                ArrayList<ItemRemito> itemsR = new ArrayList<>();
                
                JSONObject rJson = rDetalle.getJSONObject("remito");
                JSONObject oJson = rDetalle.getJSONObject("orden");
                JSONObject cJson = rDetalle.getJSONObject("cliente");
                JSONArray itemsRJson = rDetalle.getJSONArray("items");

                Remito r = new Remito();
                r.setId(rJson.getInt("id"));
                r.setFecha_emision(rJson.getString("fecha_emision"));
                r.setFecha_pago(rJson.optString("fecha_pago",null));
                r.setTotal(rJson.getFloat("total"));
                r.setObservaciones(rJson.optString("observaciones",null));
                    
                Cliente c = new Cliente();
                c.setId(cJson.getInt("id"));
                c.setNombre(cJson.getString("nombre"));
                c.setTelefono(cJson.getString("telefono"));

//                Presupuesto pre = new Presupuesto();
//                pre.setId(preJson.getInt("id"));
//                pre.setNombre(preJson.getString("nombre"));
//                pre.setDescripcion(preJson.getString("descripcion"));
//                pre.setEstado_presupuesto(preJson.getString("estado_presupuesto"));
//                pre.setTotal(preJson.getFloat("total"));

                for (int i = 0; i < itemsRJson.length(); i++) {
                    ItemRemito ir = new ItemRemito();
                    ir.setId(itemsRJson.getJSONObject(i).getInt("id"));
                    ir.setDescripcion(itemsRJson.getJSONObject(i).getString("descripcion"));
                    ir.setCantidad(itemsRJson.getJSONObject(i).optIntegerObject("cantidad",null));
                    ir.setPrecio(itemsRJson.getJSONObject(i).getFloat("precio"));
                    itemsR.add(ir);
                }
//                presupuestoDTO.put("pedido", p);
                remitoDTO.put("remito", r);
                remitoDTO.put("cliente", c);
                remitoDTO.put("items", itemsR); 

                listener.onSuccess(remitoDTO);
            } catch (Exception e) {
                e.printStackTrace();
                logger.debug(e.getMessage());
                listener.onError("Error al procesar remitos");
            }
        } else {
            listener.onError(res.optString("mensaje"));
        }
    }
    
    /**
     *
     */
    public void actualizarRemito(Remito r, DatosListener<String> listener) { 
        JSONObject res = RemitoService.actualizarRemito(r);
        if (res.getInt("status") == 201) {
            listener.onSuccess(res.optString("mensaje"));
        } else {
            listener.onError(res.optString("mensaje"));
        }
    }
     
    /**
     * Crea un nuevo remito.
     */
    public void crearRemito(Remito remito,List<ItemRemito> items, DatosListener<String> listener) {
        JSONObject res = RemitoService.crearRemito(remito,items);
        if (res.getInt("status") == 201) {
            listener.onSuccess(res.optString("mensaje"));
        } else {    
            listener.onError(res.optString("mensaje"));
        }
    }

    /**
     * Crea un nuevo remito.
     */
    public void editarRemito(Remito remito, List<ItemRemito> items, DatosListener<String> listener) {
        JSONObject res = RemitoService.editarRemito(remito,items);
        if (res.getInt("status") == 201) {
            listener.onSuccess(res.optString("mensaje"));
        } else {    
            listener.onError(res.optString("mensaje"));
        }
    }

    /**
     * Elimina un remito por ID.
     */
    public void eliminarRemito(int id, DatosListener<String> listener) {
        JSONObject res = RemitoService.eliminarRemito(id);
        if (res.getInt("status") == 200) {
            listener.onSuccess(res.optString("mensaje"));
        } else {
            listener.onError(res.optString("mensaje"));
        }
    }
}
