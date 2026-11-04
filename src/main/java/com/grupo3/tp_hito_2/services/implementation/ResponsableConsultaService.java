package com.grupo3.tp_hito_2.services.implementation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.grupo3.tp_hito_2.dtos.EmpleadoListadoDTO;
import com.grupo3.tp_hito_2.dtos.FiltroPlatoDTO;
import com.grupo3.tp_hito_2.dtos.FiltroStaffDTO;
import com.grupo3.tp_hito_2.dtos.LiquidacionUnidadDTO;
import com.grupo3.tp_hito_2.dtos.PlatoDTO;
import com.grupo3.tp_hito_2.dtos.ReciboSueldoDTO;
import com.grupo3.tp_hito_2.entities.Cajero;
import com.grupo3.tp_hito_2.entities.Cocinero;
import com.grupo3.tp_hito_2.entities.Festival;
import com.grupo3.tp_hito_2.entities.Plato;
import com.grupo3.tp_hito_2.entities.Staff;
import com.grupo3.tp_hito_2.entities.UnidadDeVenta;
import com.grupo3.tp_hito_2.helpers.FestivalHelper;
import com.grupo3.tp_hito_2.helpers.StaffHelper;
import com.grupo3.tp_hito_2.repositories.IPlatoRepository;
import com.grupo3.tp_hito_2.repositories.IStaffRepository;
import com.grupo3.tp_hito_2.repositories.IUnidadDeVentaRepository;
import com.grupo3.tp_hito_2.services.ILiquidacionService;
import com.grupo3.tp_hito_2.services.IResponsableConsultaService;

@Service("responsableConsultaService")
@Transactional(readOnly = true)
public class ResponsableConsultaService implements IResponsableConsultaService {

    private final IUnidadDeVentaRepository unidadDeVentaRepository;
    private final IStaffRepository staffRepository;
    private final IPlatoRepository platoRepository;
    private final ILiquidacionService liquidacionService;

    public ResponsableConsultaService(IUnidadDeVentaRepository unidadDeVentaRepository,
                                      IStaffRepository staffRepository,
                                      IPlatoRepository platoRepository,
                                      ILiquidacionService liquidacionService) {
        this.unidadDeVentaRepository = unidadDeVentaRepository;
        this.staffRepository = staffRepository;
        this.platoRepository = platoRepository;
        this.liquidacionService = liquidacionService;
    }

    @Override
    public List<EmpleadoListadoDTO> filtrarEmpleados(String username, FiltroStaffDTO filtro) {
        validarFiltro(filtro);
        UnidadDeVenta unidad = obtenerUnidadDelResponsable(username);

        // Siempre parte de los empleados NO eliminados lógicamente de SU unidad.
        List<Staff> activos = staffRepository
                .findByUnidadDeVenta_IdUnidadDeVentaAndActivoTrue(unidad.getIdUnidadDeVenta());

        List<EmpleadoListadoDTO> resultado = new ArrayList<>();
        for (Staff staff : activos) {
            Staff empleado = StaffHelper.real(staff);
            if (cumpleFiltro(empleado, filtro)) {
                resultado.add(EmpleadoListadoDTO.builder()
                        .idStaff(empleado.getIdStaff())
                        .nombre(empleado.getNombre())
                        .apellido(empleado.getApellido())
                        .dni(empleado.getDni())
                        .tipo(StaffHelper.tipo(empleado))
                        .detalle(StaffHelper.detalle(empleado))
                        .fechaDeIngreso(empleado.getFechaDeIngreso())
                        .antiguedad(empleado.getAntiguedad())
                        .build());
            }
        }
        resultado.sort(Comparator.comparing(EmpleadoListadoDTO::getApellido, String.CASE_INSENSITIVE_ORDER)
                .thenComparing(EmpleadoListadoDTO::getNombre, String.CASE_INSENSITIVE_ORDER));
        return resultado;
    }

    @Override
    public List<PlatoDTO> filtrarPlatos(String username, FiltroPlatoDTO filtro) {
        if (filtro.getPrecioMinimo() != null && filtro.getPrecioMinimo() < 0) {
            throw new IllegalArgumentException("El precio mínimo no puede ser negativo.");
        }
        if (filtro.getPrecioMinimo() != null && filtro.getPrecioMaximo() != null
                && filtro.getPrecioMinimo() > filtro.getPrecioMaximo()) {
            throw new IllegalArgumentException("El precio mínimo no puede ser mayor al precio máximo.");
        }
        UnidadDeVenta unidad = obtenerUnidadDelResponsable(username);

        List<PlatoDTO> resultado = new ArrayList<>();
        for (Plato plato : platoRepository.findByUnidadDeVenta(unidad)) {
            boolean cumple = contiene(plato.getNombre(), filtro.getNombre())
                    && (filtro.getPrecioMinimo() == null || plato.getPrecio() >= filtro.getPrecioMinimo())
                    && (filtro.getPrecioMaximo() == null || plato.getPrecio() <= filtro.getPrecioMaximo());
            if (cumple) {
                resultado.add(new PlatoDTO(plato.getIdPlato(), plato.getNombre(), plato.getPrecio(),
                        plato.getCostoDeProduccion(), unidad.getIdUnidadDeVenta()));
            }
        }
        resultado.sort(Comparator.comparing(PlatoDTO::getNombre, String.CASE_INSENSITIVE_ORDER));
        return resultado;
    }

    @Override
    public LiquidacionUnidadDTO liquidarHaberes(String username) {
        UnidadDeVenta unidad = obtenerUnidadDelResponsable(username);
        Festival festival = unidad.getFestival();
        if (festival == null) {
            throw new IllegalArgumentException("Tu unidad de venta no está asociada a ningún festival.");
        }

        List<ReciboSueldoDTO> recibos = new ArrayList<>();
        double total = 0;
        for (Staff staff : staffRepository
                .findByUnidadDeVenta_IdUnidadDeVentaAndActivoTrue(unidad.getIdUnidadDeVenta())) {
            ReciboSueldoDTO recibo = liquidacionService.calcularHaberes(staff, festival);
            recibos.add(recibo);
            total += recibo.getTotalACobrar();
        }
        return LiquidacionUnidadDTO.builder()
                .nombreUnidad(unidad.getNombreComercial())
                .nombreFestival(festival.getNombre())
                .diasFestival(FestivalHelper.diasDeDuracion(festival))
                .recibos(recibos)
                .totalAPagar(Math.round(total * 100.0) / 100.0)
                .build();
    }

    private UnidadDeVenta obtenerUnidadDelResponsable(String username) {
        return unidadDeVentaRepository.findByResponsableUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Tu usuario no tiene una unidad de venta asignada."));
    }

    // Validaciones de los criterios de búsqueda
    private void validarFiltro(FiltroStaffDTO filtro) {
        if (filtro.getDni() != null && filtro.getDni() <= 0) {
            throw new IllegalArgumentException("El DNI debe ser un número positivo.");
        }
        if (filtro.getAntiguedadMinima() != null && filtro.getAntiguedadMinima() < 0) {
            throw new IllegalArgumentException("La antigüedad mínima no puede ser negativa.");
        }
        if (filtro.getIngresoDesde() != null && filtro.getIngresoHasta() != null
                && filtro.getIngresoDesde().isAfter(filtro.getIngresoHasta())) {
            throw new IllegalArgumentException("La fecha 'ingreso desde' no puede ser posterior a 'ingreso hasta'.");
        }
    }

    // Todos los criterios informados deben cumplirse (AND). Los vacíos se ignoran.
    private boolean cumpleFiltro(Staff empleado, FiltroStaffDTO filtro) {
        if (!contiene(empleado.getNombre(), filtro.getNombre())
                || !contiene(empleado.getApellido(), filtro.getApellido())) {
            return false;
        }
        if (filtro.getDni() != null && empleado.getDni() != filtro.getDni().intValue()) {
            return false;
        }
        if (StringUtils.hasText(filtro.getTipo())) {
            boolean esCocinero = empleado instanceof Cocinero;
            boolean esCajero = empleado instanceof Cajero;
            if ("COCINERO".equalsIgnoreCase(filtro.getTipo()) && !esCocinero) {
                return false;
            }
            if ("CAJERO".equalsIgnoreCase(filtro.getTipo()) && !esCajero) {
                return false;
            }
        }
        if (StringUtils.hasText(filtro.getTurno())) {
            if (!(empleado instanceof Cajero)
                    || !filtro.getTurno().trim().equalsIgnoreCase(((Cajero) empleado).getTurnoDeTrabajo())) {
                return false;
            }
        }
        if (StringUtils.hasText(filtro.getEspecialidad())) {
            if (!(empleado instanceof Cocinero)
                    || !contiene(((Cocinero) empleado).getEspecialidad(), filtro.getEspecialidad())) {
                return false;
            }
        }
        if (filtro.getIngresoDesde() != null
                && (empleado.getFechaDeIngreso() == null || empleado.getFechaDeIngreso().isBefore(filtro.getIngresoDesde()))) {
            return false;
        }
        if (filtro.getIngresoHasta() != null
                && (empleado.getFechaDeIngreso() == null || empleado.getFechaDeIngreso().isAfter(filtro.getIngresoHasta()))) {
            return false;
        }
        if (filtro.getAntiguedadMinima() != null && empleado.getAntiguedad() < filtro.getAntiguedadMinima()) {
            return false;
        }
        return true;
    }

    // true si no hay criterio, o si el texto contiene el criterio (sin distinguir mayúsculas)
    private boolean contiene(String texto, String criterio) {
        if (!StringUtils.hasText(criterio)) {
            return true;
        }
        return texto != null && texto.toLowerCase().contains(criterio.trim().toLowerCase());
    }
}
