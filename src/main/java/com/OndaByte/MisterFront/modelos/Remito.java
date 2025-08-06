package com.OndaByte.MisterFront.modelos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author luciano
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Remito {
    
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("orden_id")
    private Integer orden_id;
    @JsonProperty("cliente_id")
    private Integer cliente_id;
    
    @JsonProperty("fecha_emision")
    private String fecha_emision;
    @JsonProperty("fecha_pago")
    private String fecha_pago;
    @JsonProperty("nro_remito")
    private Integer nro_remito;
    @JsonProperty("punto_venta")
    private String punto_venta;
    @JsonProperty("total")
    private Float total;
    @JsonProperty("cliente_cuit_cuil")
    private String c_cuit_cuil;
    @JsonProperty("cliente_nombre")
    private String c_nombre;
    @JsonProperty("cliente_domicilio")
    private String c_domicilio;
    @JsonProperty("cliente_localidad")
    private String c_localidad;
    @JsonProperty("cliente_telefono")
    private String c_telefono;
    @JsonProperty("observaciones")
    private String observaciones;

    public Remito() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrden_id() {
        return orden_id;
    }

    public void setOrden_id(Integer orden_id) {
        this.orden_id = orden_id;
    }

    public Integer getCliente_id() {
        return cliente_id;
    }

    public void setCliente_id(Integer cliente_id) {
        this.cliente_id = cliente_id;
    }

    public String getFecha_emision() {
        return fecha_emision;
    }

    public void setFecha_emision(String fecha_emision) {
        this.fecha_emision = fecha_emision;
    }

    public Integer getNro_remito() {
        return nro_remito;
    }

    public void setNro_remito(Integer nro_remito) {
        this.nro_remito = nro_remito;
    }

    public String getPunto_venta() {
        return punto_venta;
    }

    public void setPunto_venta(String punto_venta) {
        this.punto_venta = punto_venta;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public String getFecha_pago() {
        return fecha_pago;
    }

    public void setFecha_pago(String fecha_pago) {
        this.fecha_pago = fecha_pago;
    }

    public String getC_cuit_cuil() {
        return c_cuit_cuil;
    }

    public void setC_cuit_cuil(String c_cuit_cuil) {
        this.c_cuit_cuil = c_cuit_cuil;
    }

    public String getC_nombre() {
        return c_nombre;
    }

    public void setC_nombre(String c_nombre) {
        this.c_nombre = c_nombre;
    }

    public String getC_domicilio() {
        return c_domicilio;
    }

    public void setC_domicilio(String c_domicilio) {
        this.c_domicilio = c_domicilio;
    }

    public String getC_localidad() {
        return c_localidad;
    }

    public void setC_localidad(String c_localidad) {
        this.c_localidad = c_localidad;
    }

    public String getC_telefono() {
        return c_telefono;
    }

    public void setC_telefono(String c_telefono) {
        this.c_telefono = c_telefono;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

}
