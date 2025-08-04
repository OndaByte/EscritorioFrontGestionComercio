
package com.OndaByte.AntartidaFront.modelos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Pedido {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("cliente_id")
    private int cliente_id;
    @JsonProperty("presupuesto_id")
    private int presupuesto_id;
    @JsonProperty("descripcion")
    private String descripcion;
    @JsonProperty("fecha_fin_estimada")
    private String fecha_fin_estimada;
    @JsonProperty("estado_pedido")
    private String estado_pedido; 
    @JsonProperty("creado")
    private String creado; 
    
//    private Turno turno;
    
    public Pedido() {}


    public Integer getCliente_id() {
        return cliente_id;
    }

    public void setCliente_id(Integer cliente_id) {
        this.cliente_id = cliente_id;
    }

    /*
     * SIN USO
     */
    public Integer getPresupuesto_id() {
        return presupuesto_id;
    }

    /*
     * SIN USO
     */
    public void setPresupuesto_id(Integer presupuesto_id) {
        this.presupuesto_id = presupuesto_id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha_fin_estimada() {
        return fecha_fin_estimada;
    }

    public void setFecha_fin_estimada(String fecha_fin_estimada) {
        this.fecha_fin_estimada = fecha_fin_estimada;
    }

    public String getEstado_pedido() {
        return estado_pedido;
    }

    public void setEstado_pedido(String estado_pedido) {
        this.estado_pedido = estado_pedido;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreado() {
        return creado;
    }

    public void setCreado(String creado) {
        this.creado = creado;
    }
}
