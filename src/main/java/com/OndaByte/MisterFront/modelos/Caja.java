package com.OndaByte.MisterFront.modelos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Caja { // EN REALIDAD SERIA UNA SESION CAJA DEL MODELOw
    @JsonProperty("id")
    private Integer id = -1;
    @JsonProperty("monto_inicial")
    private Float monto_inicial;
    @JsonProperty("monto_final")
    private Float monto_final;
    private Float monto_actual; //Local
    @JsonProperty("apertura")
    private String apertura;
    @JsonProperty("cierre")
    private String cierre;
    @JsonProperty("cajero_id")
    private Integer cajero_id;
//    @JsonProperty("caja_id")
//    private String caja_id; 
//   Las SesionesCaja son Cajas en este entorno.
    
    public Caja() {}    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getMonto_inicial() {
        return monto_inicial;
    }

    public void setMonto_inicial(Float monto_inicial) {
        this.monto_inicial = monto_inicial;
    }

    public Float getMonto_final() {
        return monto_final;
    }

    public void setMonto_final(Float monto_final) {
        this.monto_final = monto_final;
    }

    public String getApertura() {
        return apertura;
    }

    public void setApertura(String apertura) {
        this.apertura = apertura;
    }

    public String getCierre() {
        return cierre;
    }

    public void setCierre(String cierre) {
        this.cierre = cierre;
    }

    public Integer getCajero_id() {
        return cajero_id;
    }

    public void setCajero_id(Integer cajero_id) {
        this.cajero_id = cajero_id;
    }

    public Float getMonto_actual() {
        return monto_actual;
    }

    public void setMonto_actual(Float monto_actual) {
        this.monto_actual = monto_actual;
    }
}