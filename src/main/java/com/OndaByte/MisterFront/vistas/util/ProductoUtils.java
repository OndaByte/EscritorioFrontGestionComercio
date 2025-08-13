/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.OndaByte.MisterFront.vistas.util;

import com.OndaByte.MisterFront.modelos.Producto;

/**
 *
 * @author luciano
 */
public class ProductoUtils {
    
    private static Float getProductoPrecioUnitario(Producto p){
        Float costo = p.getPrecio_costo();
        Float ganancia = costo * (p.getPorcentaje_ganancia() / 100f);
        Float descuento = costo * (p.getPorcentaje_descuento() / 100f);
        Float result =  costo + ganancia - descuento ;
        return result;
    }
}
