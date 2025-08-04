package com.OndaByte.AntartidaFront.modelos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ItemPresupuesto {
    @JsonProperty("id")
    private Integer id;   
    @JsonProperty("presupuesto_id")
    private Integer presupuesto_id;
    @JsonProperty("descripcion")
    private String descripcion;
    @JsonProperty("cantidad") 
    private Integer cantidad;
    @JsonProperty("precio") 
    private Float precio;
    

    public ItemPresupuesto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPresupuesto_id() {
        return presupuesto_id;
    }

    public void setPresupuesto_id(Integer presupuesto_id) {
        this.presupuesto_id = presupuesto_id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
