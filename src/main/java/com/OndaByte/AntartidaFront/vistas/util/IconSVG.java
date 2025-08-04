
package com.OndaByte.AntartidaFront.vistas.util;

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
    public static final String ACEPTAR = "icon/aceptar.svg";
    public static final String RECHAZAR = "icon/rechazar.svg";
    public static final String GUARDAR = "icon/floppy-disk.svg";
    public static final String LUPA = "icon/loop.svg";
    public static final String LOGO = "icon/snowflake.svg";
    public static final String CLARO = "icon/light.svg";
    public static final String OSCURO = "icon/dark.svg";
    /*
    */

    public IconSVG(){
        super(DEAFAULT);
        //super(getClass().getClassLoader().getResource(rutaSVG));
        Color lightColor = null;
        Color darkColor = null;
        lightColor = FlatUIUtils.getUIColor("$Menu.icon.lightColor", Color.white);
        darkColor = FlatUIUtils.getUIColor("$Menu.icon.darkColor", Color.lightGray);
        FlatSVGIcon.ColorFilter filter = new FlatSVGIcon.ColorFilter();
        filter.add(Color.decode("#969696"), lightColor, darkColor);
        setColorFilter(filter);
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
                lightColor = FlatUIUtils.getUIColor("$Menu.icon.lightColor", Color.white);
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
                darkColor = FlatUIUtils.getUIColor("$Menu.icon.darkColor", Color.white);
                break;
        }
        FlatSVGIcon.ColorFilter filter = new FlatSVGIcon.ColorFilter();
        filter.add(Color.decode("#969696"), lightColor, darkColor);
        setColorFilter(filter);
    }


}
