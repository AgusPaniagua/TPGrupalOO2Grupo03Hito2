package com.grupo3.tp_hito_2.services.implementation;



import com.grupo3.tp_hito_2.entities.*;
import com.grupo3.tp_hito_2.repositories.IStaffRepository;
import com.grupo3.tp_hito_2.repositories.IUnidadDeVentaRepository;
import com.grupo3.tp_hito_2.services.IPedidoPlatoService;
import com.grupo3.tp_hito_2.services.IPedidoService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;


@Service
public class RentabilidadService {

    private final IUnidadDeVentaRepository unidadDeVentaRepository;
    private final IPedidoService pedidoService;
    private final IPedidoPlatoService pedidoPlatoService;
    private final IStaffRepository staffRepository;

    public RentabilidadService(IUnidadDeVentaRepository unidadDeVentaRepository, IPedidoService pedidoService, IPedidoPlatoService pedidoPlatoService,IStaffRepository staffRepository) {

        this.unidadDeVentaRepository = unidadDeVentaRepository;
        this.pedidoService = pedidoService;
        this.pedidoPlatoService = pedidoPlatoService;
        this.staffRepository= staffRepository;
    }

    @Transactional(readOnly = true)
    public double calcularCostoProduccion(Long idUnidadDeVenta) {

        double costoTotal = 0;


        List<Pedido> pedidos = pedidoService.findByUnidadDeVenta(idUnidadDeVenta);


        for (Pedido pedido : pedidos) {

            List<Pedido_plato> platos =
                    pedidoPlatoService.findByPedido(pedido.getIdPedido());

            for (Pedido_plato detalle : platos) {

                costoTotal += detalle.getCantidad() * detalle.getPlato().getCostoDeProduccion();
            }
        }

        return costoTotal;
    }

    @Transactional(readOnly = true)
    public double calcularCanon(Long idUnidadDeVenta) {

        UnidadDeVenta unidad = unidadDeVentaRepository.findById(idUnidadDeVenta).orElseThrow(() ->
                new IllegalArgumentException("No se encontró la unidad de venta con ID: " + idUnidadDeVenta));

        return unidad.calcularCanon();
    }
    @Transactional(readOnly = true)
    public double calcularRecaudacion(Long idUnidadDeVenta) {

        double recaudacion = 0;

        List<Pedido> pedidos = pedidoService.findByUnidadDeVenta(idUnidadDeVenta);

        for (Pedido pedido : pedidos) {

            List<Pedido_plato> platos = pedidoPlatoService.findByPedido(pedido.getIdPedido());

            for (Pedido_plato detalle : platos) {
                recaudacion += detalle.getSubTotal();
            }
        }

        return recaudacion;
    }

    @Transactional(readOnly = true)
    public double calcularSueldos(Long idUnidadDeVenta) {

        // Buscamos la unidad de venta mediante su ID.
        UnidadDeVenta unidad = unidadDeVentaRepository.findById(idUnidadDeVenta).orElseThrow(() ->
                new IllegalArgumentException("No se encontró la unidad de venta con ID: " + idUnidadDeVenta));

        // Obtenemos el festival al que pertenece la unidad de venta.
        Festival festival = unidad.getFestival();

        // Calculamos la cantidad de días que dura el festival.
        // Sumamos 1 para incluir tanto el día de inicio como el de finalización.
        long diasFestival = java.time.temporal.ChronoUnit.DAYS.between(
                festival.getFechaInicio(), festival.getFechaFin()) + 1;

        // Calculamos cuántos períodos de 15 días corresponden.
        int periodos = (int) Math.ceil(diasFestival / 15.0);

        double totalSueldos = 0;

         /*Buscamos todo el personal asociado a la unidad de venta.*/
        List<Staff> personal = staffRepository.findByUnidadDeVenta_IdUnidadDeVenta(idUnidadDeVenta);

        // Recorremos cada empleado de la lista.
        for (Staff empleado : personal) {

            double baseCalculo = 0;

            // Comprobamos si el empleado es un cocinero.
            if (empleado instanceof Cocinero cocinero) {

                // Sumamos el sueldo base y el plus fijo de su categoría.
                baseCalculo = cocinero.getSueldoBase()
                        + cocinero.getPlusFijo();

                // Comprobamos si el empleado es un cajero.
            } else if (empleado instanceof Cajero cajero) {

                // Sumamos el sueldo base y el adicional por antigüedad.
                baseCalculo = cajero.getSueldoBase() + (cajero.getAntiguedad() * 5000);
            }

            // Calculamos el sueldo correspondiente a todos los períodos.
            totalSueldos += periodos * 0.5 * baseCalculo;
        }

        return totalSueldos;
    }

    // Calcula la rentabilidad neta de una unidad de venta.
    @Transactional(readOnly = true)
    public double calcularRentabilidadNeta(Long idUnidadDeVenta) {

        double recaudacion = calcularRecaudacion(idUnidadDeVenta);

        double costoProduccion = calcularCostoProduccion(idUnidadDeVenta);

        double sueldos = calcularSueldos(idUnidadDeVenta);

        double canon = calcularCanon(idUnidadDeVenta);

        return recaudacion - costoProduccion - sueldos - canon;
    }
    //Rentabilidad neta entre dos fechas
    @Transactional(readOnly = true)
    public double calcularRecaudacionEntreFechas(Long idUnidadDeVenta, LocalDate fechaDesde, LocalDate fechaHasta) {

        double recaudacion = 0;

        List<Pedido> pedidos = pedidoService.findByUnidadDeVentaYFechas(idUnidadDeVenta, fechaDesde, fechaHasta
        );

        for (Pedido pedido : pedidos) {

            List<Pedido_plato> platos = pedidoPlatoService.findByPedido(pedido.getIdPedido());

            for (Pedido_plato detalle : platos) {
                recaudacion += detalle.getSubTotal();
            }
        }

        return recaudacion;
    }
    @Transactional(readOnly = true)
    public double calcularCostoProduccionEntreFechas (Long idUnidadDeVenta, LocalDate fechaDesde, LocalDate fechaHasta) {

        double costoTotal = 0;

        List<Pedido> pedidos = pedidoService.findByUnidadDeVentaYFechas(idUnidadDeVenta, fechaDesde, fechaHasta);

        for (Pedido pedido : pedidos) {

            List<Pedido_plato> platos = pedidoPlatoService.findByPedido(pedido.getIdPedido());

            for (Pedido_plato detalle : platos) {
                costoTotal += detalle.getCantidad() * detalle.getPlato().getCostoDeProduccion();
            }
        }

        return costoTotal;
    }
    /*@Transactional(readOnly = true)
    public double calcularSueldosEntreFechas(Long idUnidadDeVenta, LocalDate fechaDesde, LocalDate fechaHasta) {

        if (fechaDesde == null || fechaHasta == null || fechaDesde.isAfter(fechaHasta)) {
            throw new IllegalArgumentException("El período de fechas no es válido");
        }

        long diasPeriodo = java.time.temporal.ChronoUnit.DAYS.between(fechaDesde, fechaHasta) + 1;

        int periodos = (int) Math.ceil(diasPeriodo / 15.0);

        double totalSueldos = 0;

        List<Staff> personal = staffRepository.findByUnidadDeVenta_IdUnidadDeVenta(idUnidadDeVenta);

        for (Staff empleado : personal) {

            double baseCalculo = 0;

            if (empleado instanceof Cocinero cocinero) {

                baseCalculo = cocinero.getSueldoBase() + cocinero.getPlusFijo();

            } else if (empleado instanceof Cajero cajero) {

                baseCalculo = cajero.getSueldoBase() + (cajero.getAntiguedad() * 5000);
            }

            totalSueldos += periodos * 0.5 * baseCalculo;
        }

        return totalSueldos;
    }*/
    @Transactional(readOnly = true)
    public double calcularSueldosEntreFechas(
            Long idUnidadDeVenta, LocalDate fechaDesde, LocalDate fechaHasta) {

        if (fechaDesde == null || fechaHasta == null ||
                fechaDesde.isAfter(fechaHasta)) {
            throw new IllegalArgumentException("El período de fechas no es válido");
        }

        long diasPeriodo = ChronoUnit.DAYS.between(fechaDesde, fechaHasta) + 1;

        double totalSueldos = 0;

        List<Staff> personal =
                staffRepository.findByUnidadDeVenta_IdUnidadDeVenta(idUnidadDeVenta);

        for (Staff empleado : personal) {

            double baseCalculo = 0;

            if (empleado instanceof Cocinero cocinero) {
                baseCalculo = cocinero.getSueldoBase() + cocinero.getPlusFijo();

            } else if (empleado instanceof Cajero cajero) {
                baseCalculo = cajero.getSueldoBase()
                        + (cajero.getAntiguedad() * 5000);
            }

            double sueldoPeriodo = baseCalculo * diasPeriodo / 30.0;

            totalSueldos += sueldoPeriodo;
        }

        return totalSueldos;
    }
    @Transactional(readOnly = true)
    public double calcularCanonEntreFechas(Long idUnidadDeVenta, LocalDate fechaDesde, LocalDate fechaHasta) {

        if (fechaDesde == null || fechaHasta == null || fechaDesde.isAfter(fechaHasta)) {
            throw new IllegalArgumentException("El período de fechas no es válido");
        }

        UnidadDeVenta unidad = unidadDeVentaRepository.findById(idUnidadDeVenta).orElseThrow(() ->
                        new IllegalArgumentException("No se encontró la unidad de venta con ID: " + idUnidadDeVenta));

        Festival festival = unidad.getFestival();

        long diasFestival = java.time.temporal.ChronoUnit.DAYS.between(festival.getFechaInicio(), festival.getFechaFin()) + 1;

        long diasConsultados = java.time.temporal.ChronoUnit.DAYS.between(fechaDesde, fechaHasta) + 1;

        double canonTotal = unidad.calcularCanon();

        return canonTotal * diasConsultados / diasFestival;
    }

    @Transactional(readOnly = true)
    public double calcularRentabilidadNetaEntreFechas(Long idUnidadDeVenta, LocalDate fechaDesde, LocalDate fechaHasta) {

        if (fechaDesde == null || fechaHasta == null || fechaDesde.isAfter(fechaHasta)) {
            throw new IllegalArgumentException("El período de fechas no es válido");
        }

        UnidadDeVenta unidad = unidadDeVentaRepository.findById(idUnidadDeVenta).orElseThrow(() ->
                        new IllegalArgumentException("No se encontró la unidad de venta"));

        Festival festival = unidad.getFestival();

        // Verificamos que las fechas estén dentro del festival.
        if (fechaDesde.isBefore(festival.getFechaInicio()) || fechaHasta.isAfter(festival.getFechaFin())) {
            throw new IllegalArgumentException("Las fechas deben estar dentro del período del festival");
        }

        double recaudacion = calcularRecaudacionEntreFechas(idUnidadDeVenta, fechaDesde, fechaHasta);

        double costoProduccion = calcularCostoProduccionEntreFechas(idUnidadDeVenta, fechaDesde, fechaHasta);

        double sueldos = calcularSueldosEntreFechas(idUnidadDeVenta, fechaDesde, fechaHasta);

        double canon = calcularCanonEntreFechas(idUnidadDeVenta, fechaDesde, fechaHasta);

        return recaudacion - costoProduccion - sueldos - canon;
    }
}