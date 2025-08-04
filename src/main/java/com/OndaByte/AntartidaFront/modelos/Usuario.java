
package com.OndaByte.AntartidaFront.modelos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Usuario {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("usuario")
    private String usuario;
    @JsonProperty("contra")
    private String contra;

    public Usuario() {}

    /*
     * SIN USO
     */
    public Usuario(Integer id, String usuario, String contra) {
        this.id = id;
        this.usuario = usuario;
        this.contra = contra;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /*
     * SIN USO
     */
    public String getContra() {
        return contra;
    }

    public void setContra(String contra) {
        this.contra = contra;
    }

}
