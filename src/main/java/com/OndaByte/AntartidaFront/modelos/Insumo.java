
package com.OndaByte.AntartidaFront.modelos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Insumo {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("nombre") 
    private String nombre;
    @JsonProperty("precio") 
    private Float precio;
    @JsonProperty("stock") 
    private Integer stock;
    @JsonProperty("creado")
    private String creado;
    @JsonProperty("ultMod")
    private String ultMod;

    public Insumo() {
        this.creado = "";
        this.ultMod = "";
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
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

    public String getUltMod() {
        return ultMod;
    }

    public void setUltMod(String ultMod) {
        this.ultMod = ultMod;
    }
}
