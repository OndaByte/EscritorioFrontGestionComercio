package com.OndaByte.MisterFront.vistas.util;

public class Paginado {
    Integer totalElementos;
    Integer totalPaginas;
    Integer tamPagina;
    Integer pagina;

    Float costo_total; //Esto esta aca xq me da paja cambiar todo el codigo.

    public Paginado(){}
    
    public Integer getTotalElementos() {
        return totalElementos;
    }

    public void setTotalElementos(Integer totalElementos) {
        this.totalElementos = totalElementos;
    }

    public Integer getTotalPaginas() {
        return totalPaginas;
    }

    public void setTotalPaginas(Integer totalPaginas) {
        this.totalPaginas = totalPaginas;
    }

    public Integer getTamPagina() {
        return tamPagina;
    }

    public void setTamPagina(Integer tamPagina) {
        this.tamPagina = tamPagina;
    }

    public Integer getPagina() {
        return pagina;
    }

    public void setPagina(Integer pagina) {
        this.pagina = pagina;
    }

    public Float getCosto_total() {
            return costo_total;
    }

    public void setCosto_total(Float costo_total) {
            this.costo_total = costo_total;
    }
    
}
