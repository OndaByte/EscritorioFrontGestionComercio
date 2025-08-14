/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.OndaByte.MisterFront.modelos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author luciano
 */
@JsonIgnoreProperties(ignoreUnknown = true) // Ignorá cualquier campo extra en el JSON
public class Venta {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("movimiento_id")
    private Integer movimiento_id;
    @JsonProperty("cliente_id")
    private Integer cliente_id;
    @JsonProperty("subtotal")
    private Float subtotal;
    @JsonProperty("porcentaje_descuento")
    private Integer porcentaje_descuento;
    @JsonProperty("total")
    private Float total;
    
    @JsonProperty("forma_pago") // esto para cdo escale
    private String forma_pago;
    @JsonProperty("punto_venta")
    private String punto_venta;
    @JsonProperty("nro_comprobante")
    private Integer nro_comprobante;
    @JsonProperty("observaciones")
    private String observaciones;    
    @JsonProperty("creado")
    private String creado;    
    @JsonProperty("ultMod")
    private String ultMod;    
    @JsonProperty("estado")
    private String estado;    

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

    public Integer getCliente_id() {
        return cliente_id;
    }

    public void setCliente_id(Integer cliente_id) {
        this.cliente_id = cliente_id;
    }

    public Float getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Float subtotal) {
        this.subtotal = subtotal;
    }

    public Integer getPorcentaje_descuento() {
        return porcentaje_descuento;
    }

    public void setPorcentaje_descuento(Integer porcentaje_descuento) {
        this.porcentaje_descuento = porcentaje_descuento;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public String getForma_pago() {
        return forma_pago;
    }

    public void setForma_pago(String forma_pago) {
        this.forma_pago = forma_pago;
    }

    public String getPunto_venta() {
        return punto_venta;
    }

    public void setPunto_venta(String punto_venta) {
        this.punto_venta = punto_venta;
    }

    public Integer getNro_comprobante() {
        return nro_comprobante;
    }

    public void setNro_comprobante(Integer nro_comprobante) {
        this.nro_comprobante = nro_comprobante;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getCreado() {
        return creado;
    }

    public void setCreado(String creado) {
        this.creado = creado;
    }

    public String getUltMod() {
        return ultMod;
    }

    public void setUltMod(String ultMod) {
        this.ultMod = ultMod;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
