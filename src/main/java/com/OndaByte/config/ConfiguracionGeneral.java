
package com.OndaByte.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfiguracionGeneral {

    private final static Properties properties = new Properties();

    private final static String CONFIG_FILE_NAME = "config/generalconfig.properties";
    
    private final static String CONST_CONFIG_HTTP_API_URL = "HTTP_API_URL"; 
    private final static String CONST_CONFIG_HTTP_API_PORT = "HTTP_API_PORT"; 
    
    private static String CONFIG_HTTP_API_URL =  "http://localhost";
    private static String CONFIG_HTTP_API_PORT = "4567"; 

    private static final int GLOBAL_MAX_TIMEOUT_REQ = 1000;
 
    private static Logger logger = LogManager.getLogger(ConfiguracionGeneral.class.getName());

    static{
        if(logger.isDebugEnabled()){
            logger.debug("Init logger in ConfiguracionGeneral");
        }
    }

    private static boolean configInit = false;

    public static boolean isConfigInit() {
        return configInit;
    }

    public static void setConfigInit(boolean configInit) {
        ConfiguracionGeneral.configInit = configInit;
    }

    public static void setCONFIG_HTTP_API_URL(String CONFIG_HTTP_API_URL) {
        ConfiguracionGeneral.CONFIG_HTTP_API_URL = CONFIG_HTTP_API_URL;
    }

    public static String getCONFIG_HTTP_API_URL() {
        return CONFIG_HTTP_API_URL;
    }

    public static void setCONFIG_HTTP_API_PORT(String CONFIG_HTTP_API_PORT) {
        ConfiguracionGeneral.CONFIG_HTTP_API_PORT = CONFIG_HTTP_API_PORT;
    }

    public static String getCONFIG_HTTP_API_PORT() {
        return CONFIG_HTTP_API_PORT;
    }
    
    public static void init() {
        if (!isConfigInit()){
            
           // String strAbsolutePath = strPath + CONFIG_FILE_NAME;
             if(logger.isInfoEnabled()){
                logger.info("ConfiguracionGeneral config file: " + CONFIG_FILE_NAME);
            }

            FileInputStream inStream = null;
            try {
                logger.debug("ConfiguracionGeneral config file: " + CONFIG_FILE_NAME);
                properties.load(ConfiguracionGeneral.class.getClassLoader().getResourceAsStream(CONFIG_FILE_NAME));
                if (!properties.getProperty(ConfiguracionGeneral.CONST_CONFIG_HTTP_API_URL).equals(""))
                    ConfiguracionGeneral.CONFIG_HTTP_API_URL = properties.getProperty(ConfiguracionGeneral.CONST_CONFIG_HTTP_API_URL);

                if (!properties.getProperty(ConfiguracionGeneral.CONST_CONFIG_HTTP_API_PORT).equals(""))
                    ConfiguracionGeneral.CONFIG_HTTP_API_PORT = properties.getProperty(ConfiguracionGeneral.CONST_CONFIG_HTTP_API_PORT);
                
            } catch (FileNotFoundException ex) {
                logger.fatal(ConfiguracionGeneral.class.getName() + " Error al inicializar configuraciones generales " + ex.getMessage());
            } catch (IOException ex) {
                logger.fatal(ConfiguracionGeneral.class.getName() + " Error al inicializar configuraciones generales " + ex.getMessage());
                //se debe llamar a este metodo para terminar la aplicacion
                System.exit(0);
            } catch (NumberFormatException ex) {
                logger.fatal(ConfiguracionGeneral.class.getName() + " Error al inicializar configuraciones generales " + ex.getMessage());
                //se debe llamar a este metodo para terminar la aplicacion
                System.exit(0);
            } catch (Exception ex) {
                logger.fatal(ConfiguracionGeneral.class.getName() + " Error al inicializar configuraciones generales " + ex.getMessage());
                //se debe llamar a este metodo para terminar la aplicacion
                System.exit(0);
            }finally{
                setConfigInit(true);

                try {
                    if (inStream != null)
                        inStream.close();
                } catch (IOException e) {
                    logger.fatal(ConfiguracionGeneral.class.getName() + " Error al cerrar archivo de configuracion " + e.getMessage());
                }
            }
        }
    }
}
