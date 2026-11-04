package com.grupo3.tp_hito_2.services.implementation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grupo3.tp_hito_2.dtos.MiInformacionDTO;
import com.grupo3.tp_hito_2.dtos.ParticipacionDTO;
import com.grupo3.tp_hito_2.dtos.ReciboSueldoDTO;
import com.grupo3.tp_hito_2.entities.Festival;
import com.grupo3.tp_hito_2.entities.Staff;
import com.grupo3.tp_hito_2.entities.UnidadDeVenta;
import com.grupo3.tp_hito_2.helpers.FestivalHelper;
import com.grupo3.tp_hito_2.helpers.StaffHelper;
import com.grupo3.tp_hito_2.repositories.IStaffRepository;
import com.grupo3.tp_hito_2.services.IEmpleadoPortalService;
import com.grupo3.tp_hito_2.services.ILiquidacionService;

@Service("empleadoPortalService")
@Transactional(readOnly = true)
public class EmpleadoPortalService implements IEmpleadoPortalService {

    private final IStaffRepository staffRepository;
    private final ILiquidacionService liquidacionService;

    public EmpleadoPortalService(IStaffRepository staffRepository, ILiquidacionService liquidacionService) {
        this.staffRepository = staffRepository;
        this.liquidacionService = liquidacionService;
    }

    // CU Empleado 1: ver su información (festivales y unidades de venta en las que trabaja/trabajó)
    @Override
    public MiInformacionDTO verMiInformacion(String username) {
        Staff staff = StaffHelper.real(obtenerStaff(username));
        return MiInformacionDTO.builder()
                .nombre(staff.getNombre())
                .apellido(staff.getApellido())
                .dni(staff.getDni())
                .fechaDeNacimiento(staff.getFechaDeNacimiento())
                .fechaDeIngreso(staff.getFechaDeIngreso())
                .antiguedad(staff.getAntiguedad())
                .tipoPersonal(StaffHelper.tipo(staff))
                .detalleRol(StaffHelper.detalle(staff))
                .participaciones(participacionesDe(staff))
                .build();
    }

    // CU Empleado 2: sueldo que le corresponde en cada festival
    @Override
    public List<ReciboSueldoDTO> verSueldosPorFestival(String username) {
        Staff staff = obtenerStaff(username);
        List<ReciboSueldoDTO> recibos = new ArrayList<>();
        UnidadDeVenta unidad = staff.getUnidadDeVenta();
        if (unidad != null && unidad.getFestival() != null) {
            recibos.add(liquidacionService.calcularHaberes(staff, unidad.getFestival()));
        }
        return recibos;
    }

    // Recibo de un festival puntual (para descargar el PDF). Solo de los festivales en los que trabaja el empleado.
    @Override
    public ReciboSueldoDTO reciboDeFestival(String username, Long idFestival) {
        Staff staff = obtenerStaff(username);
        UnidadDeVenta unidad = staff.getUnidadDeVenta();
        if (unidad != null && unidad.getFestival() != null
                && unidad.getFestival().getIdFestival() == idFestival.longValue()) {
            return liquidacionService.calcularHaberes(staff, unidad.getFestival());
        }
        throw new IllegalArgumentException("No tenés un recibo de sueldo para ese festival.");
    }

    // CU Empleado 3: ver si hoy tiene que trabajar en un festival activo y en qué unidad
    @Override
    public Optional<ParticipacionDTO> verAsignacionActual(String username) {
        Staff staff = obtenerStaff(username);
        for (ParticipacionDTO p : participacionesDe(staff)) {
            if (p.isActivo()) {
                return Optional.of(p);
            }
        }
        return Optional.empty();
    }

    private Staff obtenerStaff(String username) {
        Staff staff = staffRepository.findByUser_Username(username)
                .orElseThrow(() -> new IllegalArgumentException("Tu usuario no está asociado a ningún empleado."));
        if (!staff.isActivo()) {
            throw new IllegalArgumentException("Tu legajo fue dado de baja.");
        }
        return staff;
    }

    // Con el modelo actual un Staff pertenece a una única unidad (y por lo tanto a un único festival),
    // por eso la lista tiene a lo sumo un elemento. Si más adelante el grupo modela el historial de
    // asignaciones, solo hay que cambiar este método.
    private List<ParticipacionDTO> participacionesDe(Staff staff) {
        List<ParticipacionDTO> participaciones = new ArrayList<>();
        UnidadDeVenta unidad = staff.getUnidadDeVenta();
        if (unidad == null || unidad.getFestival() == null) {
            return participaciones;
        }
        Festival festival = unidad.getFestival();
        LocalDate hoy = LocalDate.now();
        participaciones.add(ParticipacionDTO.builder()
                .idFestival(festival.getIdFestival())
                .nombreFestival(festival.getNombre())
                .fechaInicio(festival.getFechaInicio())
                .fechaFin(festival.getFechaFin())
                .estado(FestivalHelper.estadoDe(festival, hoy))
                .activo(FestivalHelper.estaActivo(festival, hoy))
                .idUnidadDeVenta(unidad.getIdUnidadDeVenta())
                .nombreUnidad(unidad.getNombreComercial())
                .codigoUnico(unidad.getCodigoUnico())
                .tipoUnidad(unidad.getTipoUnidad())
                .build());
        return participaciones;
    }
}
