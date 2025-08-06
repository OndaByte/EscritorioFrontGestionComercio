
package com.OndaByte.MisterFront.modelos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Gasto {
  @JsonProperty("id")
  private Integer id;
  @JsonProperty("nombre")
  private String nombre;
  @JsonProperty("inicio")
  private String inicio;
  @JsonProperty("repeticion")
  private Integer repeticion;
  @JsonProperty("estado")
  private String estado;
    
  public Gasto() {}

  /*
   * SIN USO
   */
  public Gasto(String nombre, String inicio,Integer repeticion, String estado) {
    this.nombre=nombre;
    this.inicio=inicio;
    this.repeticion=repeticion;
    this.estado=estado;
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

  public String getInicio() {
    return inicio;
  }

  public void setInicio(String inicio) {
    this.inicio = inicio;
  }

  public Integer getRepeticion() {
    return repeticion;
  }

  public void setRepeticion(Integer repeticion) {
    this.repeticion = repeticion;
  }

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
}
