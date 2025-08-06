
package com.OndaByte.MisterFront.modelos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Turno {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("tipo")
    private String tipo;
    @JsonProperty("observaciones")
    private String observaciones;
    @JsonProperty("fecha_inicio")
    private String fechaInicio;
    @JsonProperty("fecha_fin_e")
    private String fechaFinE;
    @JsonProperty("estado_turno")
    private String estadoTurno;
    @JsonProperty("patron_repeticion")
    private Integer patron_repeticion;
    @JsonProperty("prioridad")
    private Integer prioridad = 0;
    @JsonProperty("creado")
    private String creado;
    @JsonProperty("estado")
    private String estado;
    @JsonProperty("ultMod")
    private String modificado;
    
    
    public Turno() {
        this.estadoTurno = "PENDIENTE";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    /*
     * SIN USO
     */
    public Integer getPatron_repeticion() {
        return patron_repeticion;
    }

    /*
     * SIN USO
     */
    public void setPatron_repeticion(Integer patron_repeticion) {
        this.patron_repeticion = patron_repeticion;
    }

    public Integer getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Integer prioridad) {
        this.prioridad = prioridad;
    }

    public String getCreado() {
        return creado;
    }

    public void setCreado(String creado) {
        this.creado = creado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    /*
     * SIN USO
     */
    public String getModificado() {
        return modificado;
    }

    /*
     * SIN USO
     */
    public void setModificado(String modificado) {
        this.modificado = modificado;
    }

    public String getEstadoTurno() {
        return estadoTurno;
    }

    public void setEstadoTurno(String estadoTurno) {
        this.estadoTurno = estadoTurno;
    }

    public String getFechaFinE() {
        return fechaFinE;
    }

    public void setFechaFinE(String fechaFinE) {
        this.fechaFinE = fechaFinE;
    }
}
