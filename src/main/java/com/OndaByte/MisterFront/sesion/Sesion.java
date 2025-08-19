
package com.OndaByte.MisterFront.sesion;

import com.OndaByte.MisterFront.modelos.Caja;
import com.OndaByte.MisterFront.vistas.caja.VentaCajaPanel;
import java.util.HashMap;
import java.util.Set;
import javax.swing.JTabbedPane;

class Sesion {
    
    private static Sesion sesion;

    private String token;
    private Set<String> permisos;
    private String rol;
    private String nombreUsuario;
    private Integer usuario_id;
    private Caja sesionCaja;
    private JTabbedPane tabsVentas;
    private HashMap<String,VentaCajaPanel> ventasActivas;
    protected static Sesion getSesion() {
        return sesion;
    }

    protected static void setSesion(Sesion sesion) {
        Sesion.sesion = sesion;
    }

    protected Set<String> getPermisos() {
        return permisos;
    }

    protected void setPermisos(Set<String> permisos) {
        this.permisos = permisos;
    }

    protected String getRol() {
        return rol;
    }

    protected void setRol(String rol) {
        this.rol = rol;
    }

    protected String getNombreUsuario() {
        return nombreUsuario;
    }

    protected void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    private Sesion() {
        this.tabsVentas = new JTabbedPane();
        this.ventasActivas = new HashMap<>();
    }

    protected static Sesion getInstance() {
        if (sesion == null) {
            sesion = new Sesion();
        }
        return sesion;
    }

    protected String getToken() {
        return token;
    }

    protected void setToken(String token) {
        this.token = token;
    }

    public Caja getSesionCaja() {
        return sesionCaja;
    }

    public void setSesionCaja(Caja sesionCaja) {
        this.sesionCaja = sesionCaja;
    }

    public Integer getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(Integer usuario_id) {
        this.usuario_id = usuario_id;
    }

    public JTabbedPane getTabsVentas() {
        return tabsVentas;
    }

    public HashMap<String, VentaCajaPanel> getVentasActivas() {
        return ventasActivas;
    }
       
}