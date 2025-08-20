package com.OndaByte.MisterFront.modelos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Caja {
    @JsonProperty("id")
    private Integer id = -1;
    @JsonProperty("monto_inicial")
    private String monto_inicial;
    @JsonProperty("monto_final")
    private String monto_final;
    private String monto_actual;
    @JsonProperty("apertura")
    private String apertura;
    @JsonProperty("cierre")
    private String cierre;
    @JsonProperty("cajero_id")
    private String cajero_id;
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

    public String getMonto_inicial() {
        return monto_inicial;
    }

    public void setMonto_inicial(String monto_inicial) {
        this.monto_inicial = monto_inicial;
    }

    public String getMonto_final() {
        return monto_final;
    }

    public void setMonto_final(String monto_final) {
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

    public String getCajero_id() {
        return cajero_id;
    }

    public void setCajero_id(String cajero_id) {
        this.cajero_id = cajero_id;
    }

    public String getMonto_actual() {
        return monto_actual;
    }

    public void setMonto_actual(String monto_actual) {
        this.monto_actual = monto_actual;
    }
}