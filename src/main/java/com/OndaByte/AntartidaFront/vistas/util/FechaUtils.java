
package com.OndaByte.AntartidaFront.vistas.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class FechaUtils {

    public static final DateTimeFormatter FORMATO_FECHA_HORA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Convierte LocalDate a String (yyyy-MM-dd)
     */
    public static String ldToString(LocalDate fecha) {
        if (fecha == null) return null;
        return fecha.format(FORMATO_FECHA);
    }

    /**
     * Convierte LocalDateTime a String (yyyy-MM-dd HH:mm)
     */
    public static String ldtToString(LocalDateTime fecha) {
        if (fecha == null) return null;
        return fecha.format(FORMATO_FECHA_HORA);
    }

    /**
     * Convierte String yyyy-MM-dd a LocalDate
     */
    public static LocalDate stringTold(String fechaTexto) {
        if (fechaTexto == null || fechaTexto.isBlank()) return null;
        return LocalDate.parse(fechaTexto, FORMATO_FECHA);
    }
    
    /**
     * Convierte String yyyy-MM-dd HH:mm a LocalDateTime
     */
    public static LocalDateTime stringToldt(String fechaTexto) {
        if (fechaTexto == null || fechaTexto.isBlank()) return null;
        return LocalDateTime.parse(fechaTexto, FORMATO_FECHA_HORA);
    }

    public static LocalDate dateToLocalDate(Date fecha){
        return fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
    
    public static LocalDateTime dateToLocalDateTime(Date fecha){
        return fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
    
    /**
     * Valida fechas que deben ser hoy o futuras (para LocalDate o LocalDateTime).
     */
    public static boolean esFechaPresenteOFuturo(Object fecha) {
        if (fecha == null) return false;

        if (fecha instanceof LocalDate f) {
            return !f.isBefore(LocalDate.now());
        } else if (fecha instanceof LocalDateTime fdt) {
            return !fdt.isBefore(LocalDateTime.now());
        } else {
            return false;
        }
    }
    
    /**
     * Valida fechas que deben ser hoy o futuras (para LocalDate o LocalDateTime).
     */
    public static boolean fecha1EsMenor(LocalDate fecha1, LocalDate fecha2) {
       return fecha1.isBefore(fecha2);
    }
    
    /**
     * Valida fechas que deben ser hoy o futuras (para LocalDate o LocalDateTime).
     */
    public static boolean fechaTime1EsMenor(LocalDateTime fecha1, LocalDateTime fecha2) {
       return fecha1.isBefore(fecha2);
    }
    

    /**
     * Verifica que un texto sea una fecha válida en formato dd/MM/yyyy.
     */
    public static boolean esFechaTextoValida(String fecha) {
        if (fecha == null || fecha.isBlank())
            return false;
        try {
            LocalDate.parse(fecha, FechaUtils.FORMATO_FECHA);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

/**
 * Convierte un {@link LocalDate} a {@link Date}.
 * La conversión se realiza utilizando el inicio del día en la zona horaria del sistema.
 *
 * @param localDate la fecha en formato {@link LocalDate}
 * @return la fecha convertida en formato {@link Date}, o {@code null} si la entrada es {@code null}
 */
public static Date localDateToDate(LocalDate localDate) {
    if (localDate == null) return null;
    return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
}

/**
 * Convierte un {@link LocalDateTime} a {@link Date}.
 * La conversión se realiza en la zona horaria del sistema.
 *
 * @param localDateTime la fecha y hora en formato {@link LocalDateTime}
 * @return la fecha convertida en formato {@link Date}, o {@code null} si la entrada es {@code null}
 */
public static Date localDateTimeToDate(LocalDateTime localDateTime) {
    if (localDateTime == null) return null;
    return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
}

/**
 * Convierte un {@link String} con formato "yyyy-MM-dd" o "yyyy-MM-dd HH:mm" a {@link Date}.
 * Si el texto contiene solo la fecha, se asume el inicio del día. Si contiene fecha y hora, se respeta la hora.
 *
 * @param fechaTexto la fecha como cadena de texto
 * @return la fecha convertida en formato {@link Date}, o {@code null} si la entrada es inválida
 */
public static Date stringToDate(String fechaTexto) {
    if (fechaTexto == null || fechaTexto.isBlank()) return null;

    try {
        if (fechaTexto.length() > 10) { // Tiene hora
            LocalDateTime ldt = stringToldt(fechaTexto);
            return localDateTimeToDate(ldt);
        } else { // Solo fecha
            LocalDate ld = stringTold(fechaTexto);
            return localDateToDate(ld);
        }
    } catch (DateTimeParseException e) {
        return null;
    }
}


public static String repeticionString(int repeticion) {
        switch (repeticion) {
            case 0:
                return "DIARIO";    // Diario
            case 1:
                return "SEMANAL";   // Semanal
            case 2:
                return "QUINCENAL";   // 2 semanas
            case 3:
                return "MENSUAL";  // Mensual
            case 4:
                return "BIMESTRAL";  // 2 meses
            case 5:
                return "TRIMESTRAL";
            case 6:
                return "CUATRIMESTRAL";
            case 7:
                return "SEMESTRAL";
            case 8:
                return "ANUAL";   // Anual
            default:
                throw new IllegalArgumentException("Repetición inválida");
        }
    }
}