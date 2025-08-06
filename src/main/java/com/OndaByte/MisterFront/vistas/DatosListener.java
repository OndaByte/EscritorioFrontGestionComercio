
package com.OndaByte.MisterFront.vistas;

import com.OndaByte.MisterFront.vistas.util.Paginado;

/**
 * Interfaz para manejar el resultado de una operación que devuelve datos.
 *
 * @param <T> Tipo de dato esperado en caso de éxito
 */
public interface DatosListener<T> {

    /**
     * @param datos Datos recibidos
     */
    void onSuccess(T datos);

    /**
     * @param datos Datos recibidos
     */
    void onSuccess(T datos, Paginado p);

    /**
     * @param mensajeError Mensaje descriptivo del error
     */
    void onError(String mensajeError);
}
