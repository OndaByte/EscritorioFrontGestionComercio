
package com.OndaByte.MisterFront.vistas.util;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.ui.FlatUIUtils;
import java.awt.Color;

public class IconSVG extends FlatSVGIcon{
    
    public static final String DEAFAULT = "icon/plus.svg";
    public static final String NUEVO = "icon/plus.svg";
    public static final String EDITAR = "icon/pen.svg";
    public static final String ELIMINAR = "icon/trash.svg";
    public static final String IMPRIMIR = "icon/printer.svg";
    public static final String CANCELAR = "icon/cancel.svg";
    public static final String COBRAR = "icon/cobrar.svg";
    public static final String ACEPTAR = "icon/aceptar.svg";
    public static final String RECHAZAR = "icon/rechazar.svg";
    public static final String GUARDAR = "icon/floppy-disk.svg";
    public static final String LUPA = "icon/loop.svg";
    public static final String OJO = "icon/ojo.svg";
    public static final String CLARO = "icon/light.svg";
    public static final String OSCURO = "icon/dark.svg";
    public static final String OPERADOR = "icon/operador.svg";
    public static final String CAJA_ABIERTA = "icon/caja-abierta.svg";
    public static final String CAJA_CERRADA = "icon/caja-cerrada.svg";
    public static final String RELOJ = "icon/clock.svg";
    /*
    */

    public IconSVG(){
        super(DEAFAULT);
        setFiltro(1);
    }

    public IconSVG(String rutaSVG){
        super(rutaSVG);
        setFiltro(1);
    }

    public IconSVG(String rutaSVG, int filtro){
        super(rutaSVG);
        setFiltro(filtro);
    }

    public void setFiltro(int filtro){
        Color lightColor = null;
        Color darkColor = null;
        switch (filtro){
            case 1:
                lightColor = FlatUIUtils.getUIColor("$Menu.icon.lightColor", Color.darkGray);
                darkColor = FlatUIUtils.getUIColor("$Menu.icon.darkColor", Color.lightGray);
                break;
            case 2:
                lightColor = FlatUIUtils.getUIColor("$Menu.icon.lightColor", Color.darkGray);
                darkColor = FlatUIUtils.getUIColor("$Menu.icon.darkColor", Color.lightGray);
                break;
            case 3:
                lightColor = FlatUIUtils.getUIColor("$Menu.icon.lightColor", Color.black);
                darkColor = FlatUIUtils.getUIColor("$Menu.icon.darkColor", Color.black);
                break;
            default:
                lightColor = FlatUIUtils.getUIColor("$Menu.icon.lightColor", Color.GRAY);
                darkColor = FlatUIUtils.getUIColor("$Menu.icon.darkColor", Color.lightGray);
                break;
        }
        FlatSVGIcon.ColorFilter filter = new FlatSVGIcon.ColorFilter();
        filter.add(Color.decode("#969696"), lightColor, darkColor);
        setColorFilter(filter);
    }


}
