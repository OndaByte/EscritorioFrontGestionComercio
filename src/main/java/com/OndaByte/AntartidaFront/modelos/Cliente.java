
package com.OndaByte.AntartidaFront.modelos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Cliente { 
    
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("nombre")
    private String nombre;
    @JsonProperty("email")
    private String email;
    @JsonProperty("telefono")
    private String telefono;
    @JsonProperty("direccion")
    private String direccion;
    @JsonProperty("dni")
    private String dni;
    @JsonProperty("cuit_cuil")
    private String cuit_cuil;
    @JsonProperty("localidad")
    private String localidad;
    @JsonProperty("codigo_postal")
    private String codigo_postal;
    @JsonProperty("provincia")
    private String provincia;
    @JsonProperty("cond_iva")
    private String cond_iva;

    public Cliente() {}

    public Cliente(Integer id, String nombre, String email, String telefono, String direccion, String dni, String cuit_cuil,
                   String localidad, String codigo_postal, String provincia, String cond_iva) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
        this.dni = dni;
        this.cuit_cuil = cuit_cuil;
        this.localidad = localidad;
        this.codigo_postal = codigo_postal;
        this.provincia = provincia;
        this.cond_iva = cond_iva;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getCuit_cuil() {
        return cuit_cuil;
    }

    public void setCuit_cuil(String cuit_cuil) {
        this.cuit_cuil = cuit_cuil;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getCodigo_postal() {
        return codigo_postal;
    }

    public void setCodigo_postal(String codigo_postal) {
        this.codigo_postal = codigo_postal;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getCond_iva() {
        return cond_iva;
    }

    public void setCond_iva(String cond_iva) {
        this.cond_iva = cond_iva;
    }

    @Override
    public String toString() {
        return nombre +"-" + dni ;
    }

}
