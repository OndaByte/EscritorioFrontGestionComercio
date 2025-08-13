
package com.OndaByte.MisterFront.modelos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Producto {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("categoria_id")
    private Integer categoria_id;
    
    @JsonProperty("nombre")
    private String nombre;
    @JsonProperty("codigo_barra")
    private String codigo_barra;
    @JsonProperty("descripcion")
    private String descripcion;
    @JsonProperty("precio_costo")
    private Float precio_costo;
    @JsonProperty("porcentaje_ganancia")
    private Float porcentaje_ganancia;
    @JsonProperty("porcentaje_descuento")
    private Float porcentaje_descuento;
    @JsonProperty("stock")
    private Integer stock;
    @JsonProperty("creado")
    private String creado;
    @JsonProperty("ultMod")
    private String ultMod;
    @JsonProperty("estado")
    private String estado;

    public Producto() {
        this.creado = "";
        this.ultMod = "";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCategoria_id() {
        return categoria_id;
    }

    public void setCategoria_id(Integer categoria_id) {
        this.categoria_id = categoria_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo_barra() {
        return codigo_barra;
    }

    public void setCodigo_barra(String codigo_barra) {
        this.codigo_barra = codigo_barra;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Float getPrecio_costo() {
        return precio_costo;
    }

    public void setPrecio_costo(Float precio_costo) {
        this.precio_costo = precio_costo;
    }

    public Float getPorcentaje_ganancia() {
        return porcentaje_ganancia;
    }

    public void setPorcentaje_ganancia(Float porcentaje_ganancia) {
        this.porcentaje_ganancia = porcentaje_ganancia;
    }

    public Float getPorcentaje_descuento() {
        return porcentaje_descuento;
    }

    public void setPorcentaje_descuento(Float porcentaje_descuento) {
        this.porcentaje_descuento = porcentaje_descuento;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
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
    /**
     * Precio con descuento
     * @return 
     */
    public Float getProductoPrecioUnitario(){
        Float costo = this.getPrecio_costo();
        Float ganancia = costo * (this.getPorcentaje_ganancia() / 100f);
        Float descuento = costo * (this.getPorcentaje_descuento() / 100f);
        Float result =  costo + ganancia - descuento ;
        return result;
    }

    /**
     * Precio sin descuento
     * @return 
     */
    public Float getProductoPrecioSinDesc(){
        Float costo = this.getPrecio_costo();
        Float ganancia = costo * (this.getPorcentaje_ganancia() / 100f);
        Float result =  costo + ganancia  ;
        return result;
    }
    
}
