/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.OndaByte.MisterFront.modelos;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author luciano
 */
public class ItemVenta {
    
    @JsonProperty("id")
    private Integer id;   
    @JsonProperty("movimiento_id")
    private Integer movimiento_id;
    @JsonProperty("producto_id")
    private Integer producto_id;
    @JsonProperty("descripcion")
    private String descripcion;
    @JsonProperty("cantidad") 
    private Integer cantidad;
    @JsonProperty("precio") 
    private Float precio;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMovimiento_id() {
        return movimiento_id;
    }

    public void setMovimiento_id(Integer movimiento_id) {
        this.movimiento_id = movimiento_id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public Integer getProducto_id() {
        return producto_id;
    }

    public void setProducto_id(Integer producto_id) {
        this.producto_id = producto_id;
    }

}
