
package com.OndaByte.MisterFront.modelos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ItemRemito {
    
    @JsonProperty("id")
    private Integer id;   
    @JsonProperty("remito_id")
    private Integer remito_id;
    @JsonProperty("descripcion")
    private String descripcion;
    @JsonProperty("cantidad") 
    private Integer cantidad;
    @JsonProperty("precio") 
    private Float precio;

    public ItemRemito() {
        descripcion = "";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRemito_id() {
        return remito_id;
    }

    public void setRemito_id(Integer remito_id) {
        this.remito_id = remito_id;
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
