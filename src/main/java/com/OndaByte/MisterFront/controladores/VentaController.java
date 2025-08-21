
package com.OndaByte.MisterFront.controladores;

import com.OndaByte.MisterFront.modelos.Cliente;
import com.OndaByte.MisterFront.modelos.ItemVenta;
import com.OndaByte.MisterFront.modelos.Orden;
import com.OndaByte.MisterFront.modelos.Venta;
import com.OndaByte.MisterFront.servicios.MovimientoService;
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
//                    JSONObject oJson = ventasArray.getJSONObject(i).getJSONObject("orden");
//                  JSONObject preJson = ventasArray.getJSONObject(i).getJSONObject("presupuesto");

                    Venta v = new Venta();
                    v.setId(rJson.getInt("id"));
                    v.setCreado(rJson.getString("creado"));
                    v.setForma_pago(rJson.getString("forma_pago"));
                    v.setSubtotal(rJson.getFloat("subtotal"));
                    v.setPorcentaje_descuento(rJson.getInt("porcentaje_descuento"));
                    v.setTotal(rJson.getFloat("total"));
                    v.setPunto_venta(rJson.getString("punto_venta"));
                    v.setObservaciones(rJson.optString("observaciones",null));
                    v.setNro_comprobante(rJson.getInt("nro_comprobante"));

                    Cliente c = new Cliente();
                    if(!cJson.isNull("id")){
                        c.setId(cJson.getInt("id"));
                        c.setNombre(cJson.getString("nombre"));
                        c.setTelefono(cJson.getString("telefono"));
                    }

                    HashMap<String, Object> objeto = new HashMap<>();
                    objeto.put("cliente", c);
                    objeto.put("venta", v);
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
                JSONObject vDetalle = new JSONObject(res.getString("data"));
                HashMap<String, Object> ventaDTO = new HashMap<>();
                ArrayList<ItemVenta> itemsV = new ArrayList<>();
                
                JSONObject vJson = vDetalle.getJSONObject("venta");
                JSONObject cJson = vDetalle.getJSONObject("cliente");
                JSONArray itemsRJson = vDetalle.getJSONArray("items");

                Venta v = new Venta();
                v.setId(vJson.getInt("id"));
                v.setCreado(vJson.getString("creado"));
                v.setForma_pago(vJson.getString("forma_pago"));
                v.setSubtotal(vJson.getFloat("subtotal"));
                v.setPorcentaje_descuento(vJson.getInt("porcentaje_descuento"));
                v.setTotal(vJson.getFloat("total"));
                v.setPunto_venta(vJson.getString("punto_venta"));
                v.setObservaciones(vJson.optString("observaciones",null));
                v.setNro_comprobante(vJson.getInt("nro_comprobante"));
                    
                Cliente c = new Cliente();
                c.setId(cJson.getInt("id"));
                c.setNombre(cJson.getString("nombre"));
                c.setTelefono(cJson.getString("telefono"));

                for (int i = 0; i < itemsRJson.length(); i++) {
                    ItemVenta iv = new ItemVenta();
                    iv.setId(itemsRJson.getJSONObject(i).getInt("id"));
                    iv.setNombre(itemsRJson.getJSONObject(i).getString("descripcion"));
                    iv.setCantidad(itemsRJson.getJSONObject(i).optIntegerObject("cantidad",null));
                    iv.setSubtotal(itemsRJson.getJSONObject(i).getFloat("subtotal"));
                    itemsV.add(iv);
                }
//                presupuestoDTO.put("pedido", p);
                ventaDTO.put("venta", v);
                ventaDTO.put("cliente", c);
                ventaDTO.put("items", itemsV); 

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
    public boolean crearVenta(Venta venta, List<ItemVenta> items, DatosListener<String> listener) {
        JSONObject res = VentaService.crearVenta(venta, items);
        if (res.getInt("status") == 201) {
            listener.onSuccess(res.optString("mensaje"));
            return true;
        } else {
            listener.onError(res.optString("mensaje"));
            return false;
        }
    }

 
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
    public void eliminarVenta(int id, DatosListener<String> listener) {
        JSONObject res = VentaService.eliminarVenta(id);
        if (res.getInt("status") == 200) {
            listener.onSuccess(res.optString("mensaje"));
        } else {
            listener.onError(res.optString("mensaje"));
        }
    }
}
