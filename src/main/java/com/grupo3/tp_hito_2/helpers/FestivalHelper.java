package com.grupo3.tp_hito_2.helpers;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.grupo3.tp_hito_2.entities.Festival;

public final class FestivalHelper {

    public static final String PROXIMO = "PRÓXIMO";
    public static final String EN_CURSO = "EN CURSO";
    public static final String FINALIZADO = "FINALIZADO";

    private FestivalHelper() {
    }

    // Días que dura el festival, contando el día de inicio y el de fin.
    public static long diasDeDuracion(Festival festival) {
        if (festival == null || festival.getFechaInicio() == null || festival.getFechaFin() == null
                || festival.getFechaFin().isBefore(festival.getFechaInicio())) {
            throw new IllegalArgumentException("El período del festival no es válido.");
        }
        return ChronoUnit.DAYS.between(festival.getFechaInicio(), festival.getFechaFin()) + 1;
    }

    public static boolean estaActivo(Festival festival, LocalDate dia) {
        if (festival == null || festival.getFechaInicio() == null || festival.getFechaFin() == null) {
            return false;
        }
        return !dia.isBefore(festival.getFechaInicio()) && !dia.isAfter(festival.getFechaFin());
    }

    public static String estadoDe(Festival festival, LocalDate dia) {
        if (estaActivo(festival, dia)) {
            return EN_CURSO;
        }
        if (festival != null && festival.getFechaInicio() != null && dia.isBefore(festival.getFechaInicio())) {
            return PROXIMO;
        }
        return FINALIZADO;
    }
}
