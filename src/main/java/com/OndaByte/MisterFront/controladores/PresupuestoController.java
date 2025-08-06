
package com.OndaByte.MisterFront.controladores;

import com.OndaByte.MisterFront.modelos.Cliente;
import com.OndaByte.MisterFront.modelos.ItemPresupuesto;
import com.OndaByte.MisterFront.modelos.Pedido;
import com.OndaByte.MisterFront.modelos.Presupuesto;
import com.OndaByte.MisterFront.servicios.PresupuestoService;
import com.OndaByte.MisterFront.sesion.SesionController;
import com.OndaByte.MisterFront.vistas.DatosListener;
import com.OndaByte.MisterFront.vistas.util.Paginado;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class PresupuestoController {

    private SesionController sesionController = null;
    private static PresupuestoController instance;
    private static Logger logger = LogManager.getLogger(PresupuestoController.class.getName());
       
    static{
        if(logger.isDebugEnabled()){
            logger.debug("Init logger en PresupuestoController");
        }
    }

    private PresupuestoController() {
        this.sesionController = SesionController.getInstance();
    }

    public static PresupuestoController getInstance() {
        if (instance == null) {
            instance = new PresupuestoController();
        }
        return instance;
    }

    public void filtrar(String filtro, String desde, String hasta, String estado, String pagina, String cantElementos, DatosListener<List<HashMap<String, Object>>> listener) {
        JSONObject pedidosRes = PresupuestoService.filtrar(filtro,desde,hasta, estado, pagina, cantElementos);
        if (pedidosRes.getInt("status") == 200) {
            try {
                JSONArray pedidosArray = new JSONArray(pedidosRes.getString("data"));
                List<HashMap<String, Object>> presupuestosDTO = new ArrayList<>();

                for (int i = 0; i < pedidosArray.length(); i++) {

                    JSONObject cJson = pedidosArray.getJSONObject(i).getJSONObject("cliente");
                    JSONObject pJson = pedidosArray.getJSONObject(i).getJSONObject("pedido");
                    JSONObject tJson = pedidosArray.getJSONObject(i).getJSONObject("turno");
                    JSONObject preJson = pedidosArray.getJSONObject(i).getJSONObject("presupuesto");

                    Cliente c = new Cliente();
                    c.setId(cJson.getInt("id"));
                    c.setNombre(cJson.getString("nombre"));
                    c.setLocalidad(cJson.getString("localidad"));
                    c.setDireccion(cJson.getString("direccion"));
                    c.setProvincia(cJson.getString("provincia"));
                    c.setCuit_cuil(cJson.getString("cuit_cuil"));
                    c.setTelefono(cJson.getString("telefono"));

//                    Turno t = new Turno();
//                    t.setId(tJson.getInt("id"));
//                    t.setDescripcion(tJson.optString("descripcion",null));
//                    t.setEstadoTurno(tJson.optString("estado_turno","PENDIENTE"));
//                    t.setFechaInicio(tJson.getString("fecha_inicio"));
//                    t.setFechaFinE(tJson.getString("fecha_fin_e"));
                    
                    Presupuesto pre = new Presupuesto();
                    pre.setId(preJson.getInt("id"));
                    pre.setPedido_id(pJson.getInt("id"));
                    pre.setNombre(preJson.getString("nombre"));
                    pre.setDescripcion(preJson.getString("descripcion"));
                    pre.setTotal(preJson.getFloat("total"));
                    pre.setEstado_presupuesto(preJson.getString("estado_presupuesto"));
                    pre.setCreado(preJson.getString("creado"));

                    
                    Pedido p = new Pedido();
                    p.setId(pJson.getInt("id"));
                    p.setDescripcion(pJson.optString("descripcion", "No disponible"));
                    p.setFecha_fin_estimada(pJson.optString("fecha_fin_estimada", "No disponible"));
               //     p.setEstado_pedido(calcularEstado(p,pre));
                    p.setEstado_pedido(pJson.optString("estado_pedido", "No disponible"));
                    p.setCliente_id(pJson.getInt("cliente_id"));
                    p.setCreado(pJson.getString("creado"));

                    HashMap<String, Object> objeto = new HashMap<>();
                    objeto.put("pedido", p);
                    objeto.put("cliente", c);
                 //   objeto.put("turno", t);
                    objeto.put("presupuesto", pre);
                    presupuestosDTO.add(objeto);
                }

                Paginado p = new Paginado();
                p.setPagina(pedidosRes.getInt("pagina"));
                p.setTamPagina(pedidosRes.getInt("elementos"));
                p.setTotalElementos(pedidosRes.getInt("t_elementos"));
                p.setTotalPaginas(pedidosRes.getInt("t_paginas"));

                listener.onSuccess(presupuestosDTO, p);
            } catch (Exception e) {
                e.printStackTrace();
                logger.debug(e.getMessage());
                listener.onError("Error al procesar pedidos y clientes");
            }
        } else {
            listener.onError(pedidosRes.optString("mensaje"));
        }
    }
     
    public void buscarPresupuesto(int id, DatosListener<HashMap<String, Object>> listener) { 
        JSONObject res = PresupuestoService.buscarPresupuesto(id);
        if (res.getInt("status") == 200) {
            try {
                JSONObject preDetalle = new JSONObject(res.getString("data"));
                HashMap<String, Object> presupuestoDTO = new HashMap<>();
                ArrayList<ItemPresupuesto> itemsPre = new ArrayList<>();
                
                JSONObject cJson = preDetalle.getJSONObject("cliente");
//                JSONObject pJson = preDetalle.getJSONObject("pedido");
                JSONObject preJson = preDetalle.getJSONObject("presupuesto");
                JSONArray itemsPreJson = preDetalle.getJSONArray("items");

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
                pre.setNombre(preJson.getString("nombre"));
                pre.setDescripcion(preJson.getString("descripcion"));
                pre.setEstado_presupuesto(preJson.getString("estado_presupuesto"));
                pre.setTotal(preJson.getFloat("total"));


//                Pedido p = new Pedido();
//                p.setId(pJson.getInt("id"));
//                p.setDescripcion(pJson.optString("descripcion", "No disponible"));
//                p.setFecha_fin_estimada(pJson.optString("fecha_fin_estimada", "No disponible"));
//                p.setEstado_pedido(pJson.getString("estado_pedido"));
//                p.setCliente_id(pJson.getInt("cliente_id"));
//                p.setCreado(pJson.getString("creado"));
                    
                for (int i = 0; i < itemsPreJson.length(); i++) {
                    ItemPresupuesto ip = new ItemPresupuesto();
                    ip.setId(itemsPreJson.getJSONObject(i).getInt("id"));
                    ip.setDescripcion(itemsPreJson.getJSONObject(i).getString("descripcion"));
                    ip.setCantidad(itemsPreJson.getJSONObject(i).optIntegerObject("cantidad",null));
                    ip.setPrecio(itemsPreJson.getJSONObject(i).getFloat("precio"));
                    itemsPre.add(ip);
                }
//                presupuestoDTO.put("pedido", p);
                presupuestoDTO.put("cliente", c);
                presupuestoDTO.put("presupuesto", pre);
                presupuestoDTO.put("items", itemsPre); 

                listener.onSuccess(presupuestoDTO);
            } catch (Exception e) {
                e.printStackTrace();
                logger.debug(e.getMessage());
                listener.onError("Error al procesar presupuestos");
            }
        } else {
            listener.onError(res.optString("mensaje"));
        }
    }
    
    /**
     * Crea un nuevo presupuesto.
     */
    public void actualizarPresupuesto(Presupuesto p, DatosListener<String> listener) { 
        JSONObject res = PresupuestoService.actualizarPresupuesto(p);
        if (res.getInt("status") == 201) {
            listener.onSuccess(res.optString("mensaje"));
        } else {
            listener.onError(res.optString("mensaje"));
        }
    }
     
    /**
     * Crea un nuevo presupuesto.
     */
    public void crearPresupuesto(Presupuesto presupuesto, List<ItemPresupuesto> items, DatosListener<String> listener) {
        JSONObject res = PresupuestoService.crearPresupuesto(presupuesto,items);
        if (res.getInt("status") == 201) {
            listener.onSuccess(res.optString("mensaje"));
        } else {    
            listener.onError(res.optString("mensaje"));
        }
    }

    /**
     * Edita un presupuesto existente.
     */
    public void editarPresupuesto(Presupuesto presupuesto, List<ItemPresupuesto> items, DatosListener<String> listener) {
        JSONObject res = PresupuestoService.editarPresupuesto(presupuesto,items);
        if (res.getInt("status") == 201) {
            listener.onSuccess(res.optString("mensaje"));
        } else {
            listener.onError(res.optString("mensaje"));
        }
    }

    /**
     * Elimina un presupuesto por ID.
     */
    public void eliminarPresupuesto(int id, DatosListener<String> listener) {
        JSONObject res = PresupuestoService.eliminarPresupuesto(id);
        if (res.getInt("status") == 200) {
            listener.onSuccess(res.optString("mensaje"));
        } else {
            listener.onError(res.optString("mensaje"));
        }
    }
}