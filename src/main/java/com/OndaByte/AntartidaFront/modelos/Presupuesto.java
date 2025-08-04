
package com.OndaByte.AntartidaFront.modelos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Presupuesto {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("pedido_id")
    private Integer pedido_id;
    @JsonProperty("nombre")
    private String nombre;
    @JsonProperty("descripcion")
    private String descripcion;
    @JsonProperty("estado_presupuesto")
    private String estado_presupuesto;
    @JsonProperty("total")
    private Float total;
    @JsonProperty("creado")
    private String creado; 
    
    public Presupuesto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado_presupuesto() {
        return estado_presupuesto;
    }

    public void setEstado_presupuesto(String estado_presupuesto) {
        this.estado_presupuesto = estado_presupuesto;
    }

    public Integer getPedido_id() {
        return pedido_id;
    }

    public void setPedido_id(Integer pedido_id) {
        this.pedido_id = pedido_id;
    }
    
    public Float getTotal() {
        return total;
    }
    
    public void setTotal(Float total) {
        this.total = total;
    }

    public String getCreado() {
        return creado;
    }

    public void setCreado(String creado) {
        this.creado = creado;
    }

    
}
