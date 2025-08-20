
package com.OndaByte.MisterFront.controladores;

import com.OndaByte.MisterFront.modelos.Cliente;
import com.OndaByte.MisterFront.modelos.ItemVenta;
import com.OndaByte.MisterFront.modelos.Orden;
import com.OndaByte.MisterFront.modelos.Venta;
import com.OndaByte.MisterFront.servicios.VentaService;
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

public class VentaController {

    /*
    private SesionController sesionController = null;
    private static VentaController instance;
    private static Logger logger = LogManager.getLogger(VentaController.class.getName());
       
    static{
        if(logger.isDebugEnabled()){
            logger.debug("Init logger en VentaController");
        }
    }

    private VentaController() {
        this.sesionController = SesionController.getInstance();
    }

    public static VentaController getInstance() {
        if (instance == null) {
            instance = new VentaController();
        }
        return instance;
    }

    public void filtrar(String filtro, String desde, String hasta, String estado, String pagina, String cantElementos, DatosListener<List<HashMap<String, Object>>> listener) {
        JSONObject ventasRes = VentaService.filtrar(filtro,desde,hasta, estado, pagina, cantElementos);
        if (ventasRes.getInt("status") == 200) {
            try {
                JSONArray ventasArray = new JSONArray(ventasRes.getString("data"));
                List<HashMap<String, Object>> ventasDTO = new ArrayList<>();

                for (int i = 0; i < ventasArray.length(); i++) {

                    JSONObject rJson = ventasArray.getJSONObject(i).getJSONObject("venta");
                    JSONObject cJson = ventasArray.getJSONObject(i).getJSONObject("cliente");
                    JSONObject oJson = ventasArray.getJSONObject(i).getJSONObject("orden");
//                  JSONObject preJson = ventasArray.getJSONObject(i).getJSONObject("presupuesto");

                    Venta r = new Venta();
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
                    r.setNro_venta(rJson.getInt("nro_venta"));


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
                    objeto.put("venta", r);
                    ventasDTO.add(objeto);
                }

                Paginado p = new Paginado();
                p.setPagina(ventasRes.getInt("pagina"));
                p.setTamPagina(ventasRes.getInt("elementos"));
                p.setTotalElementos(ventasRes.getInt("t_elementos"));
                p.setTotalPaginas(ventasRes.getInt("t_paginas"));

                listener.onSuccess(ventasDTO, p);
            } catch (Exception e) {
                e.printStackTrace();
                logger.debug(e.getMessage());
                listener.onError("Error al procesar ventas");
            }
        } else {
            listener.onError(ventasRes.optString("mensaje"));
        }
    }
    
    public void buscarVenta(int id, DatosListener<HashMap<String, Object>> listener) { 
        JSONObject res = VentaService.buscarVenta(id);
        if (res.getInt("status") == 200) {
            try {
                JSONObject rDetalle = new JSONObject(res.getString("data"));
                HashMap<String, Object> ventaDTO = new HashMap<>();
                ArrayList<ItemVenta> itemsR = new ArrayList<>();
                
                JSONObject rJson = rDetalle.getJSONObject("venta");
                JSONObject oJson = rDetalle.getJSONObject("orden");
                JSONObject cJson = rDetalle.getJSONObject("cliente");
                JSONArray itemsRJson = rDetalle.getJSONArray("items");

                Venta r = new Venta();
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
                    ItemVenta ir = new ItemVenta();
                    ir.setId(itemsRJson.getJSONObject(i).getInt("id"));
                    ir.setDescripcion(itemsRJson.getJSONObject(i).getString("descripcion"));
                    ir.setCantidad(itemsRJson.getJSONObject(i).optIntegerObject("cantidad",null));
                    ir.setPrecio(itemsRJson.getJSONObject(i).getFloat("precio"));
                    itemsR.add(ir);
                }
//                presupuestoDTO.put("pedido", p);
                ventaDTO.put("venta", r);
                ventaDTO.put("cliente", c);
                ventaDTO.put("items", itemsR); 

                listener.onSuccess(ventaDTO);
            } catch (Exception e) {
                e.printStackTrace();
                logger.debug(e.getMessage());
                listener.onError("Error al procesar ventas");
            }
        } else {
            listener.onError(res.optString("mensaje"));
        }
    }
    
//    /**
//     *
//     */
//    public void actualizarVenta(Venta r, DatosListener<String> listener) { 
//        JSONObject res = VentaService.actualizarVenta(r);
//        if (res.getInt("status") == 201) {
//            listener.onSuccess(res.optString("mensaje"));
//        } else {
//            listener.onError(res.optString("mensaje"));
//        }
//    }

    /**
     * Crea un nuevo venta.
     */

    /*
    public void crearVenta(Venta venta,List<ItemVenta> items, DatosListener<String> listener) {
        JSONObject res = VentaService.crearVenta(venta,items);
        if (res.getInt("status") == 201) {
            listener.onSuccess(res.optString("mensaje"));
        } else {    
            listener.onError(res.optString("mensaje"));
        }
    }

    /**
     * Crea un nuevo venta.
     */

    /*
    public void editarVenta(Venta venta, List<ItemVenta> items, DatosListener<String> listener) {
        JSONObject res = VentaService.editarVenta(venta,items);
        if (res.getInt("status") == 201) {
            listener.onSuccess(res.optString("mensaje"));
        } else {    
            listener.onError(res.optString("mensaje"));
        }
    }

    /**
     * Elimina un venta por ID.
     */

    /*
    public void eliminarVenta(int id, DatosListener<String> listener) {
        JSONObject res = VentaService.eliminarVenta(id);
        if (res.getInt("status") == 200) {
            listener.onSuccess(res.optString("mensaje"));
        } else {
            listener.onError(res.optString("mensaje"));
        }
    }

     */
}
