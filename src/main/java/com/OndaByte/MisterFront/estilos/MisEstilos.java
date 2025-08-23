
package com.OndaByte.MisterFront.estilos;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.JComponent;

/**
 *
$Component.accentColor → Color principal del tema.
$Component.focusColor → Color de enfoque.
$Table.selectionBackground → Color de selección en tablas.
$Actions.Green → Verde estándar.
$Actions.Red → Rojo estándar.
$Actions.Yellow → Amarillo estándar.
$Actions.Blue → Azul estándar.
*/
public class MisEstilos {
    // Constantes de estilo para los componentes
    
    // Login
    public static final String CONTRASENIA = "showRevealButton:true; showCapsLock:true;";
    public static final String TEXT_FIELD = "borderWidth:0; arc:10;";

    //Menú_Lateral
    public static String MENU_LATERAL = "border:40,4,4,4;"
                + "background:$Menu.lateral.background;"
                + "arc:10";
    public static String MENU_LATERAL_HEADER = "font:$Menu.header.font;"
                + "foreground:$Menu.lateral.foreground";
    public static String MENU_LATERAL_MENU = "border:5,5,5,5;"
                + "background:$Menu.lateral.background";
    
    
    public static String MENU_LATERAL_SCROLL = "width:5;"  // Ancho de la barra
                + "trackInsets:2,0,2,1;"  // Insets para el track
                + "thumbInsets:2,0,2,1;"  // Insets para el thumb
                + "background:$Menu.ScrollBar.background;"  // Fondo de la barra
                + "thumb:$Menu.ScrollBar.thumb;";

    //MenusItems
    public static String MENU_LATERAL_ITEM = "font:$Menu.label.font; foreground:$Menu.title.foreground;";    
    public static String BOTON_MENU_ITEM = "background:$Menu.background;"
                + "foreground:$Menu.foreground;"
                + "selectedBackground:$Menu.button.selectedBackground;"
                + "selectedForeground:$Menu.button.selectedForeground;"
                + "borderWidth:0;"
                + "focusWidth:0;"
                + "innerFocusWidth:0;"
                + "arc:10;"
                + "iconTextGap:10;"
                + "margin:3,11,3,11";    

    public static String BOTON_MENU_ITEM_POPUP = "background:$Menu.background;"
                + "foreground:$Menu.foreground;"
                + "selectedBackground:$Menu.button.selectedBackground;"
                + "selectedForeground:$Menu.button.selectedForeground;"
                + "borderWidth:0;"
                + "arc:10;"
                + "focusWidth:0;"
                + "iconTextGap:10;"
                + "margin:5,11,5,11";

    // ----- Panel Central -----
    public static String PANEL_CENTRAL = "arc:25;";
    
    
    // ----- Estilos de botones ----- 
    
    //public static final String BOTON_PRIMARIO = "arc:10; background:$Menu.lightdark.button.background; foreground:$Menu.foreground;";
    // public static final String BOTON_SECUNDARIO = "arc:10; background:$Button.seccondary.background; foreground:$Button.seccondary.foreground;"; hacer con variable global dark.
    //public static final String BOTON_PELIGRO = "arc:10; background:$Button.danger.background; foreground:$Button.danger.foreground;";
   
    public static final String BOTON_LOGIN = "focusWidth:0; arc:15;";

    //Menu Lateral
    public static final String BOTONCITO = "background:$Menu.button.background;"
                + "arc:999;"
                + "focusWidth:0;"
                + "borderWidth:0";
    public static final String BOTON_ACCION =  "arc:15;"
                + "borderWidth:0;"
                + "focusWidth:0;"
                + "innerFocusWidth:0;"
                + "margin:5,20,5,20;"
                + "background:$Panel.background";
    
    //  -----   FIN BOTONES -----
    //Label 
    public static String TITULO = "font:$h1.font";
    
    // Tabla
    public static final String TABLA = 
            "rowHeight:40; showHorizontalLines:true; intercellSpacing:0,1; " +
            "cellFocusColor:$TableHeader.hoverBackground; selectionBackground:$TableHeader.hoverBackground; " +
            "selectionForeground:$Table.foreground;";
    public static final String HEADER_TABLA = 
            "height:30; font:bold; hoverBackground:null; pressedBackground:null; separatorColor:$TableHeader.background;";

    // Estilos de barra de búsqueda
    public static final String BUSQUEDA = 
            "arc:15; borderWidth:0; focusWidth:0; innerFocusWidth:0; margin:5,20,5,20; background:$Panel.background";

    public static final String PLACEHOLDER_BUSQUEDA = "Buscar...";


    //Estilos caja
    public static final String CAJA_LABEL_CAJA_ABIERTA =
            "arc:15; "
                    + "background:$Panel.top.caja.abierta.background"
                    + "foreground:$Panel.top.caja.abierta.foreground"
            //+ "borderWidth:0; "
            //+ "focusWidth:0; "
            //+ "innerFocusWidth:0; "
            + "margin:5,20,5,20; ";


    public static final String CAJA_LABEL_CAJA_CERRADA =
            "arc:15; "
                    + "background:$PanelCaja.caja.cerrada.background"
                    + "foreground:$PanelCaja.caja.cerrada.foreground"
                    //+ "borderWidth:0; "
                    //+ "focusWidth:0; "
                    //+ "innerFocusWidth:0; "
                    + "margin:5,20,5,20; ";

    // Método para aplicar estilos a un componente
    public static void aplicarEstilo(JComponent componente, String estilo) {
        componente.putClientProperty(FlatClientProperties.STYLE, estilo);
    }


}
