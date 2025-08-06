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
public class Movimiento {
    @JsonProperty("id")
    private Integer id;   
    @JsonProperty("creado")
    private String creado;
    @JsonProperty("ultMod")
    private String ultMod;   
    @JsonProperty("cliente_id")
    private Integer cliente_id;   
    @JsonProperty("sesion_caja_id")
    private Integer sesion_caja_id;       
    @JsonProperty("tipo_mov")
    private String tipo_mov;
    @JsonProperty("descripcion")
    private String descripcion;
    @JsonProperty("total") 
    private Float total;
    @JsonProperty("precio") 
    private Float precio;

    public Movimiento() {
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

    public Integer getCliente_id() {
        return cliente_id;
    }

    public void setCliente_id(Integer cliente_id) {
        this.cliente_id = cliente_id;
    }

    public Integer getSesion_caja_id() {
        return sesion_caja_id;
    }

    public void setSesion_caja_id(Integer sesion_caja_id) {
        this.sesion_caja_id = sesion_caja_id;
    }

    public String getTipo_mov() {
        return tipo_mov;
    }

    public void setTipo_mov(String tipo_mov) {
        this.tipo_mov = tipo_mov;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public String getUltMod() {
        return ultMod;
    }

    public void setUltMod(String ultMod) {
        this.ultMod = ultMod;
    }
}