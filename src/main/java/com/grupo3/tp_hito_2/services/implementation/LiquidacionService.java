package com.grupo3.tp_hito_2.services.implementation;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.stereotype.Service;

import com.grupo3.tp_hito_2.configuration.LiquidacionConfig;
import com.grupo3.tp_hito_2.dtos.ReciboSueldoDTO;
import com.grupo3.tp_hito_2.entities.Cajero;
import com.grupo3.tp_hito_2.entities.Cocinero;
import com.grupo3.tp_hito_2.entities.Festival;
import com.grupo3.tp_hito_2.entities.Staff;
import com.grupo3.tp_hito_2.entities.UnidadDeVenta;
import com.grupo3.tp_hito_2.helpers.FestivalHelper;
import com.grupo3.tp_hito_2.helpers.StaffHelper;
import com.grupo3.tp_hito_2.services.ILiquidacionService;

@Service("liquidacionService")
public class LiquidacionService implements ILiquidacionService {

    private final LiquidacionConfig config;

    public LiquidacionService(LiquidacionConfig config) {
        this.config = config;
    }

    // Total = cantidadIntervalos * porcentajePorIntervalo * (sueldoBase + plus)
    //   - Cocina: plus = plus fijo de su categoría
    //   - Cajero: plus = años de antigüedad * plus por año
    // cantidadIntervalos = cuántos tramos de 15 días (o fracción) dura el festival:
    //   hasta 15 días -> 1 (50%), de 16 a 30 -> 2 (100%), 31 a 45 -> 3 (150%), etc.
    @Override
    public ReciboSueldoDTO calcularHaberes(Staff staff, Festival festival) {
        if (staff == null) {
            throw new IllegalArgumentException("Se necesita un empleado para liquidar el sueldo.");
        }
        if (festival == null) {
            throw new IllegalArgumentException("Se necesita un festival para liquidar el sueldo.");
        }

        Staff empleado = StaffHelper.real(staff);
        long dias = FestivalHelper.diasDeDuracion(festival);
        int intervalos = cantidadDeIntervalos(dias);
        double porcentaje = intervalos * config.getPorcentajePorIntervalo();

        double sueldoBase = empleado.getSueldoBase() > 0 ? empleado.getSueldoBase() : config.getSueldoBase();
        int anios = aniosDeAntiguedadAl(empleado, festival.getFechaFin());

        double plusCategoria = 0;
        double plusAntiguedad = 0;
        if (empleado instanceof Cocinero) {
            plusCategoria = ((Cocinero) empleado).getPlusFijo();
        } else if (empleado instanceof Cajero) {
            plusAntiguedad = anios * config.getPlusPorAnioDeAntiguedad();
        }

        double subtotal = sueldoBase + plusCategoria + plusAntiguedad;
        double total = redondear(porcentaje * subtotal);

        UnidadDeVenta unidad = empleado.getUnidadDeVenta();

        return ReciboSueldoDTO.builder()
                .idStaff(empleado.getIdStaff())
                .nombreCompleto(StaffHelper.nombreCompleto(empleado))
                .dni(empleado.getDni())
                .tipoPersonal(StaffHelper.tipo(empleado))
                .detalleRol(StaffHelper.detalle(empleado))
                .aniosAntiguedad(anios)
                .idFestival(festival.getIdFestival())
                .nombreFestival(festival.getNombre())
                .fechaInicio(festival.getFechaInicio())
                .fechaFin(festival.getFechaFin())
                .diasFestival(dias)
                .estadoFestival(FestivalHelper.estadoDe(festival, LocalDate.now()))
                .nombreUnidad(unidad != null ? unidad.getNombreComercial() : "-")
                .codigoUnidad(unidad != null ? unidad.getCodigoUnico() : "-")
                .sueldoBase(sueldoBase)
                .plusCategoria(plusCategoria)
                .plusAntiguedad(plusAntiguedad)
                .subtotal(subtotal)
                .cantidadIntervalos(intervalos)
                .porcentajeAplicado(redondear(porcentaje * 100))
                .totalACobrar(total)
                .build();
    }

    private int cantidadDeIntervalos(long dias) {
        return (int) Math.ceil(dias / (double) config.getDiasPorIntervalo());
    }

    // Años completos entre el ingreso y la fecha de referencia (fin del festival). Nunca negativo.
    private int aniosDeAntiguedadAl(Staff empleado, LocalDate referencia) {
        if (empleado.getFechaDeIngreso() == null || referencia == null) {
            return 0;
        }
        return Math.max(0, Period.between(empleado.getFechaDeIngreso(), referencia).getYears());
    }

    private double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}
