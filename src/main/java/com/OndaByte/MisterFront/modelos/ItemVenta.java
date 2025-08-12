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
    @JsonProperty("nombre")
    private String nombre;
    @JsonProperty("cantidad") 
    private Integer cantidad;
    @JsonProperty("porcentaje_descuento") 
    private Float porcentaje_descuento;
    @JsonProperty("subtotal") 
    private Float subtotal;

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

    public Integer getProducto_id() {
        return producto_id;
    }

    public void setProducto_id(Integer producto_id) {
        this.producto_id = producto_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Float getPorcentaje_descuento() {
        return porcentaje_descuento;
    }

    public void setPorcentaje_descuento(Float porcentaje_descuento) {
        this.porcentaje_descuento = porcentaje_descuento;
    }

    public Float getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Float subtotal) {
        this.subtotal = subtotal;
    }

    
}
