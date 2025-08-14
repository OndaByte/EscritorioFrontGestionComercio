package com.OndaByte.MisterFront.controladores;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.OndaByte.MisterFront.modelos.Usuario;

import com.OndaByte.MisterFront.modelos.Empresa;
import com.OndaByte.MisterFront.servicios.InicioService;

import org.json.JSONObject;
public class InicioController {

    private static Logger logger = LogManager.getLogger(InicioController.class.getName());
    
    private static InicioController controller;
    
    private InicioController(){
    }
    
    public static InicioController getInstance(){
        if(controller == null){
            controller = new InicioController();
        }
        return controller;
    }

    public boolean inicializar(Empresa e, Usuario u){
        JSONObject res = InicioService.inicializar(e,u);
        return res.getInt("status")==201;
    }
}
