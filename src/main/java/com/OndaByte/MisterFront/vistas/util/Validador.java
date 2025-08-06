
package com.OndaByte.MisterFront.vistas.util;

public class Validador {
 
    /**
     * Valida que un campo de texto no esté vacío o nulo.
     */
    public static boolean noVacio(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }

    /**
     * Valida que un número sea positivo.
     */
    public static boolean esNumeroPositivo(Number numero) {
        return numero != null && numero.doubleValue() > 0;
    }

    /**
     * Valida que un número entero esté en un rango.
     */
    public static boolean estaEnRango(int valor, int minimo, int maximo) {
        return valor >= minimo && valor <= maximo;
    }
}
