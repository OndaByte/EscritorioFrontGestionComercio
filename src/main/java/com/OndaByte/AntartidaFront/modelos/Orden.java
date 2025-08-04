
package com.OndaByte.AntartidaFront.modelos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Orden {

    @JsonProperty("id") 
    private Integer id;
    @JsonProperty("pedido_id") 
    private Integer pedido_id;
    @JsonProperty("turno_id")
    private Integer turno_id;
    @JsonProperty("descripcion")
    private String descripcion;
    /*
    @JsonProperty("precio_final")
    private Float precio_final;
    @JsonProperty("costo_total")
    private Float costo_total;
     */
    @JsonProperty("fecha_fin")
    private String fecha_fin;
    @JsonProperty("tipo")
    private String tipo;
    @JsonProperty("estado_orden")
    private String estado_orden;
    @JsonProperty("creado")
    private String creado;

    public Orden(){}

    /*
     * SIN USO
     */
    public Orden(Integer pedido_id, String descripcion, Float precio_final, Float costo_total,
         String estado_orden, String fecha_fin, String tipo, Integer turno_id, String creado) {
        this.pedido_id = pedido_id;
        this.descripcion = descripcion;
        //this.precio_final = precio_final;
        //this.costo_total = costo_total;
        this.estado_orden = estado_orden;
        this.fecha_fin = fecha_fin;
        this.tipo = tipo;
        this.turno_id = turno_id;
        this.creado = creado;
    }

    /*
     * SIN USO
     */
    public Integer getTurno_id() {
            return turno_id;
    }

    public void setTurno_id(Integer turno_id) {
            this.turno_id = turno_id;
    }
    
    public String getFecha_fin() {
            return fecha_fin;
    }
    public void setFecha_fin(String fecha_fin) {
            this.fecha_fin = fecha_fin;
    }

    public Integer getPedido_id() {
            return pedido_id;
    }

    public void setPedido_id(Integer pedido_id) {
            this.pedido_id = pedido_id;
    }

    public String getDescripcion() {
            return descripcion;
    }

    public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
    }

    /*
    public Float getPrecio_final() {
            return precio_final;
    }

    public void setPrecio_final(Float precio_final) {
            this.precio_final = precio_final;
    }

    public Float getCosto_total() {
            return costo_total;
    }

    public void setCosto_total(Float costo_total) {
            this.costo_total = costo_total;
    }

     */

    public String getEstado_orden() {
            return estado_orden;
    }

    public void setEstado_orden(String estado_orden) {
            this.estado_orden = estado_orden;
    }
    
    public String getTipo() {
            return tipo;
    }

    public void setTipo(String tipo) {
            this.tipo = tipo;
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
