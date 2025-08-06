
package com.OndaByte.MisterFront.controladores;

import com.OndaByte.MisterFront.vistas.DatosListener;
import com.OndaByte.MisterFront.modelos.Cliente;
import com.OndaByte.MisterFront.modelos.Pedido;
import com.OndaByte.MisterFront.modelos.Presupuesto;
import com.OndaByte.MisterFront.modelos.Turno;
import com.OndaByte.MisterFront.servicios.PedidoService;
import com.OndaByte.MisterFront.sesion.SesionController;
import com.OndaByte.MisterFront.vistas.util.Paginado;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PedidoController {

    private SesionController sesionController = null;
    private static PedidoController controller;
    private static Logger logger = LogManager.getLogger(PedidoController.class.getName());

    static{
        if(logger.isDebugEnabled()){
            logger.debug("Init logger en PedidoController");
        }
    }
   
    private PedidoController() {
        this.sesionController = SesionController.getInstance();
    }

    public static PedidoController getInstance() {
        if (controller == null) {
            controller = new PedidoController();
        }
        return controller;
    }

    private String calcularEstado(Pedido p,Presupuesto pre){
        String e = p.getEstado_pedido();
        logger.info(pre.getId());
        //SI no existe el presupuesto pendiente de presupuestar.
        return e;
    }

    /**
     * Filtra pedidos con sus relaciones, usando paginado.
     */
    public void filtrar(String filtro, String desde, String hasta, String estado, String pagina, String cantElementos, DatosListener<List<HashMap<String, Object>>> listener) {
        JSONObject pedidosRes = PedidoService.filtrar(filtro,desde,hasta, estado, pagina, cantElementos);
        if (pedidosRes.getInt("status") == 200) {
            try {
                JSONArray pedidosArray = new JSONArray(pedidosRes.getString("data"));
                List<HashMap<String, Object>> allPedidosYClientes = new ArrayList<>();

                for (int i = 0; i < pedidosArray.length(); i++) {
                    
                    HashMap<String, Object> objeto = new HashMap<>();
                    JSONObject cJson = pedidosArray.getJSONObject(i).getJSONObject("cliente");
                    JSONObject pJson = pedidosArray.getJSONObject(i).getJSONObject("pedido");
                    JSONObject tJson = pedidosArray.getJSONObject(i).getJSONObject("turno");
                    JSONObject preJson = pedidosArray.getJSONObject(i).getJSONObject("presupuesto");

                    Cliente c = new Cliente();
                    c.setId(cJson.getInt("id"));
                    c.setNombre(cJson.getString("nombre"));
                    c.setTelefono(cJson.getString("telefono"));
                    
                    Turno t = new Turno();
                    if(!tJson.isNull("id")){
                        t.setId(tJson.getInt("id"));
                        t.setTipo(tJson.getString("tipo"));
                        t.setObservaciones(tJson.optString("observaciones",null));
                        t.setEstadoTurno(tJson.optString("estado_turno","PENDIENTE"));
                        t.setFechaInicio(tJson.getString("fecha_inicio"));
                        t.setFechaFinE(tJson.getString("fecha_fin_e"));
                    }
                    
                    Presupuesto pre = new Presupuesto();
                    if(!preJson.isNull("id")){
                        
                        pre.setId(preJson.optInt("id"));
                        pre.setNombre(preJson.getString("nombre"));
                        pre.setDescripcion(preJson.getString("descripcion"));
                        pre.setEstado_presupuesto(preJson.getString("estado_presupuesto"));

                    }
                    
                    Pedido p = new Pedido();
                    p.setId(pJson.getInt("id"));
                    p.setDescripcion(pJson.optString("descripcion", "No disponible"));
                    p.setFecha_fin_estimada(pJson.optString("fecha_fin_estimada", "No disponible"));
                    p.setEstado_pedido(calcularEstado(p,pre));
                    p.setEstado_pedido(pJson.optString("estado_pedido", "No disponible"));
                    p.setCliente_id(pJson.getInt("cliente_id"));
                    p.setCreado(pJson.getString("creado"));

                    
                    objeto.put("pedido", p);
                    objeto.put("cliente", c);
                    objeto.put("turno", t);
                    objeto.put("presupuesto", pre);
                    allPedidosYClientes.add(objeto);
                }

                Paginado p = new Paginado();
                p.setPagina(pedidosRes.getInt("pagina"));
                p.setTamPagina(pedidosRes.getInt("elementos"));
                p.setTotalElementos(pedidosRes.getInt("t_elementos"));
                p.setTotalPaginas(pedidosRes.getInt("t_paginas"));

                listener.onSuccess(allPedidosYClientes, p);
            } catch (Exception e) {
                e.printStackTrace();
                logger.debug(e.getMessage());
                listener.onError("Error al procesar pedidos y clientes");
            }
        } else {
            listener.onError(pedidosRes.optString("mensaje"));
        }
    }

    /**
     * Crea un nuevo pedido.
     */
    public void crearPedido(Pedido pedido, DatosListener<String> listener) {
        
        JSONObject p = new JSONObject(pedido);
        JSONObject pedidoReq = new JSONObject();
        pedidoReq.put("pedido", p);
        JSONObject pedidoRes = PedidoService.crearPedido(pedidoReq);
        if (pedidoRes.getInt("status") == 201) {
            listener.onSuccess(pedidoRes.optString("mensaje"));
        } else {
            listener.onError(pedidoRes.optString("mensaje"));
        }
    }

    /**
     * Edita un pedido existente.
     */
    public void editarPedido(Pedido pedido, DatosListener<String> listener) {
        JSONObject pedidoRes = PedidoService.editarPedido(pedido);
        if (pedidoRes.getInt("status") == 201) {
            listener.onSuccess(pedidoRes.optString("mensaje"));
        } else {
            listener.onError(pedidoRes.optString("mensaje"));
        }
    }

    /**
     * Elimina un pedido por su ID.
     */
    public void eliminarPedido(int id, DatosListener<String> listener) {
        JSONObject pedidoRes = PedidoService.eliminarPedido(id);
        if (pedidoRes.getInt("status") == 201) {
            listener.onSuccess(pedidoRes.optString("mensaje"));
        } else {
            listener.onError(pedidoRes.optString("mensaje"));
        }
    }
}
