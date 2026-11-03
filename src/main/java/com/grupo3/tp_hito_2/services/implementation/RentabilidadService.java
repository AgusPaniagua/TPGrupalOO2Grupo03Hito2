package com.grupo3.tp_hito_2.services.implementation;



import com.grupo3.tp_hito_2.entities.*;
import com.grupo3.tp_hito_2.repositories.IStaffRepository;
import com.grupo3.tp_hito_2.repositories.IUnidadDeVentaRepository;
import com.grupo3.tp_hito_2.services.IPedidoPlatoService;
import com.grupo3.tp_hito_2.services.IPedidoService;
import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;


@Service
public class RentabilidadService {

    private final IUnidadDeVentaRepository unidadDeVentaRepository;
    private final IPedidoService pedidoService;
    private final IPedidoPlatoService pedidoPlatoService;
    private final IStaffRepository staffRepository;

    public RentabilidadService(
            IUnidadDeVentaRepository unidadDeVentaRepository,
            IPedidoService pedidoService,
            IPedidoPlatoService pedidoPlatoService,IStaffRepository staffRepository) {

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
                baseCalculo = cajero.getSueldoBase()
                        + (cajero.getAntiguedad() * 5000);
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
}