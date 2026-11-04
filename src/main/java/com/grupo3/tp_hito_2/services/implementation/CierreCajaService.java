package com.grupo3.tp_hito_2.services.implementation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grupo3.tp_hito_2.dtos.CajaDelDiaDTO;
import com.grupo3.tp_hito_2.dtos.CierreCajaDTO;
import com.grupo3.tp_hito_2.entities.Cajero;
import com.grupo3.tp_hito_2.entities.CierreCaja;
import com.grupo3.tp_hito_2.entities.Festival;
import com.grupo3.tp_hito_2.entities.Staff;
import com.grupo3.tp_hito_2.entities.UnidadDeVenta;
import com.grupo3.tp_hito_2.helpers.FestivalHelper;
import com.grupo3.tp_hito_2.helpers.StaffHelper;
import com.grupo3.tp_hito_2.repositories.ICierreCajaRepository;
import com.grupo3.tp_hito_2.repositories.IPedidoPlatoRepository;
import com.grupo3.tp_hito_2.repositories.IPedidoRepository;
import com.grupo3.tp_hito_2.repositories.IStaffRepository;
import com.grupo3.tp_hito_2.services.ICierreCajaService;

@Service("cierreCajaService")
public class CierreCajaService implements ICierreCajaService {

    private final IStaffRepository staffRepository;
    private final IPedidoRepository pedidoRepository;
    private final IPedidoPlatoRepository pedidoPlatoRepository;
    private final ICierreCajaRepository cierreCajaRepository;

    public CierreCajaService(IStaffRepository staffRepository,
                             IPedidoRepository pedidoRepository,
                             IPedidoPlatoRepository pedidoPlatoRepository,
                             ICierreCajaRepository cierreCajaRepository) {
        this.staffRepository = staffRepository;
        this.pedidoRepository = pedidoRepository;
        this.pedidoPlatoRepository = pedidoPlatoRepository;
        this.cierreCajaRepository = cierreCajaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean esCajero(String username) {
        return staffRepository.findByUser_Username(username)
                .map(s -> s.isActivo() && StaffHelper.real(s) instanceof Cajero)
                .orElse(false);
    }

    @Override
    @Transactional(readOnly = true)
    public CajaDelDiaDTO verCajaDelDia(String username) {
        Cajero cajero = obtenerCajero(username);
        UnidadDeVenta unidad = unidadDe(cajero);
        LocalDate hoy = LocalDate.now();
        Festival festival = unidad.getFestival();

        List<CierreCajaDTO> historial = cierreCajaRepository
                .findTop10ByUnidadDeVenta_IdUnidadDeVentaOrderByFechaDesc(unidad.getIdUnidadDeVenta())
                .stream().map(this::convertirADTO).collect(Collectors.toList());

        Optional<CierreCaja> cierreDeHoy = cierreCajaRepository
                .findByUnidadDeVenta_IdUnidadDeVentaAndFecha(unidad.getIdUnidadDeVenta(), hoy);

        return CajaDelDiaDTO.builder()
                .nombreUnidad(unidad.getNombreComercial())
                .nombreFestival(festival != null ? festival.getNombre() : "-")
                .fecha(hoy)
                .festivalActivo(FestivalHelper.estaActivo(festival, hoy))
                .totalDelDia(totalDelDia(unidad, hoy))
                .cantidadPedidos(cantidadDePedidos(unidad, hoy))
                .cierreDeHoy(cierreDeHoy.isPresent() ? convertirADTO(cierreDeHoy.get()) : null)
                .historial(historial)
                .build();
    }

    // CU Empleado 4 (Cajero): cerrar la caja del día.
    // Validaciones de carga:
    //   - solo un Cajero activo puede cerrar la caja
    //   - el cajero debe tener una unidad asignada y su festival debe estar activo hoy
    //   - la caja de una unidad se cierra una única vez por día
    @Override
    @Transactional
    public CierreCajaDTO cerrarCaja(String username) {
        Cajero cajero = obtenerCajero(username);
        UnidadDeVenta unidad = unidadDe(cajero);
        LocalDate hoy = LocalDate.now();

        if (!FestivalHelper.estaActivo(unidad.getFestival(), hoy)) {
            throw new IllegalArgumentException("No se puede cerrar la caja: tu unidad no tiene un festival activo hoy.");
        }

        Optional<CierreCaja> yaCerrada = cierreCajaRepository
                .findByUnidadDeVenta_IdUnidadDeVentaAndFecha(unidad.getIdUnidadDeVenta(), hoy);
        if (yaCerrada.isPresent()) {
            throw new IllegalArgumentException("La caja de hoy ya fue cerrada por "
                    + StaffHelper.nombreCompleto(yaCerrada.get().getCajero()) + ".");
        }

        CierreCaja cierre = new CierreCaja(cajero, unidad, hoy,
                totalDelDia(unidad, hoy), cantidadDePedidos(unidad, hoy));
        try {
            return convertirADTO(cierreCajaRepository.saveAndFlush(cierre));
        } catch (DataIntegrityViolationException e) {
            // Dos cajeros cerrando a la vez: la restricción única de la tabla evita el duplicado.
            throw new IllegalArgumentException("La caja de hoy ya fue cerrada.");
        }
    }

    private Cajero obtenerCajero(String username) {
        Staff staff = staffRepository.findByUser_Username(username)
                .orElseThrow(() -> new IllegalArgumentException("Tu usuario no está asociado a ningún empleado."));
        Staff real = StaffHelper.real(staff);
        if (!real.isActivo() || !(real instanceof Cajero)) {
            throw new IllegalArgumentException("Solo los cajeros pueden cerrar la caja.");
        }
        return (Cajero) real;
    }

    private UnidadDeVenta unidadDe(Cajero cajero) {
        if (cajero.getUnidadDeVenta() == null) {
            throw new IllegalArgumentException("No tenés una unidad de venta asignada.");
        }
        return cajero.getUnidadDeVenta();
    }

    private double totalDelDia(UnidadDeVenta unidad, LocalDate dia) {
        Double total = pedidoPlatoRepository.totalVendidoPorUnidadYFecha(unidad.getIdUnidadDeVenta(), dia);
        return total == null ? 0 : total;
    }

    private int cantidadDePedidos(UnidadDeVenta unidad, LocalDate dia) {
        return (int) pedidoRepository.countByUnidadDeVenta_IdUnidadDeVentaAndFechaDeTransaccion(
                unidad.getIdUnidadDeVenta(), dia);
    }

    private CierreCajaDTO convertirADTO(CierreCaja cierre) {
        return CierreCajaDTO.builder()
                .idCierreCaja(cierre.getIdCierreCaja())
                .fecha(cierre.getFecha())
                .fechaHoraCierre(cierre.getFechaHoraCierre())
                .nombreUnidad(cierre.getUnidadDeVenta().getNombreComercial())
                .cerradoPor(StaffHelper.nombreCompleto(cierre.getCajero()))
                .totalRecaudado(cierre.getTotalRecaudado())
                .cantidadPedidos(cierre.getCantidadPedidos())
                .build();
    }
}
