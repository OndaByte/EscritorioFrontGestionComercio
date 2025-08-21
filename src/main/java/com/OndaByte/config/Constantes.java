
package com.OndaByte.config;

import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Constantes {

    //Datos de la empresa
    public static final String TITULO_PRINCIPAL = "<HTML>Mister Queso</HTML>";
    public static final String LOGO = "icon/marketplace.svg";

    //constantes del mad viejo
    //public static final String VERSION = "ver 77.9.13.13";//{web}.{firm}.{bd}.{propio}{rc*}INCREMENTE TODOS LOS INDICES y RESET de rc
    //public static final String RELEASE_DATE = "29/11/2023 07:30";

    public static final String RELEASE_DESCRIPTION = "";
    public static final String RELEASE_PENDING = "";

    //constantes
    public static final String CONFIG_LOG_FILE = "config/logConfig.xml";
    public static final String JAR_FILE = "GestionComercio.jar";
    //public static final String JAR_FILE_TEST = "EVAJackpotTest.jar";
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String TIME_FORMAT = "HH:mm:ss";

    public static final String RESOURCES_PATH = Constantes.class.getProtectionDomain().getCodeSource()
            .getLocation().getFile()
            .replace(Constantes.JAR_FILE + "classes/", "");
    public static final String RESOURCES_FILE_PATH = "file:"+ RESOURCES_PATH;
    ;
    public static final Map<String, String[][]> componentesCategoriaGrupo = new LinkedHashMap<>();// Mantiene el orden deinsersion
    public static final Map<String, String[][]> secretariaMenu = new LinkedHashMap<>();
    public static final Map<String, String[][]> vendedorMenu = new LinkedHashMap<>();
    public static final Map<String, String[][]> usuarioMenu = new LinkedHashMap<>();

    private static Logger logger = LogManager.getLogger(Constantes.class.getName());


    static{
        if(logger.isDebugEnabled()){
            logger.debug("Init logger en Constantes");
        }

        String[][] grupo1 = {{"Dashboard"},{"Caja"}};
        String[][] grupo11 = {{"Caja"}};
        String[][] grupo2 = {{"Finanzas","Resumen Cajas","Movimientos","Ventas"}/*,"Historial/Reportes"}*/};
        String[][] grupo3  = {/*{"Clientes"},{"Empleados"},*/{"Productos"},{"Categorias"}/*,{"Gastos Fijos"}*/};
        String[][] grupo31  = {{"Productos"},{"Categorias"}};
        String[][] grupo4 = { {"Perfil"},{"Cerrar Sesion"}};

        componentesCategoriaGrupo.put("~PRINCIPAL~",grupo1);
        componentesCategoriaGrupo.put("~PROCESOS~",grupo2);
        componentesCategoriaGrupo.put("~EMPRESA~",grupo3);
        componentesCategoriaGrupo.put("~OTROS~",grupo4);


        vendedorMenu.put("~PRINCIPAL~",grupo11);
        vendedorMenu.put("~EMPRESA~",grupo31);
        vendedorMenu.put("~OTROS~",grupo4);

        secretariaMenu.put("~PROCESOS~",grupo2);
        secretariaMenu.put("~EMPRESA~",grupo3);
        secretariaMenu.put("~OTROS~",grupo4);

        String[][] grupo3Usuario  = {{"Clientes"},{"Empleados"}};

        usuarioMenu.put("~EMPRESA~",grupo3Usuario);
        usuarioMenu.put("~OTROS~",grupo4);

    }
    public static Map<String, String[][]> getMenuItems(String role) {
        Map<String, String[][]> conponentesByRol = null;
        switch(role){
            case "ADMIN":{
                conponentesByRol = componentesCategoriaGrupo;
                break;
            }
            case "EMPLEADO":{
                //conponentesByRol = secretariaMenu;
                conponentesByRol = vendedorMenu;
                break;
            }
            case "USUARIO":{
                conponentesByRol = usuarioMenu;
                break;
            }
            default:{
                logger.warn("Constantes.getMenuitems : El nombre de rol no coincidió con ningun caso de rol");
            }
        }
        return conponentesByRol;//menu;
    }

}