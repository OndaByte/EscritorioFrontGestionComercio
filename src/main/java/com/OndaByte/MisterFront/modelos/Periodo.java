
package com.OndaByte.MisterFront.modelos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Periodo {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("periodo")
    private String periodo;
    @JsonProperty("gasto_id")
    private Integer gasto_id;
    @JsonProperty("costo")
    private Float costo;

    public Periodo() {
    }

    /*
     * SIN USO
     */
    public Periodo(String periodo, Integer gasto_id, Float costo) {
        this.periodo = periodo;
        this.gasto_id = gasto_id;
        this.costo = costo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    /*
     * SIN USO
     */
    public Integer getGasto_id() {
        return gasto_id;
    }

    /*
     * SIN USO
     */
    public void setGasto_id(Integer gasto_id) {
        this.gasto_id = gasto_id;
    }

    public Float getCosto() {
        return costo;
    }

    public void setCosto(Float costo) {
        this.costo = costo;
    }
}
