package com.grupo3.tp_hito_2.controllers;
import com.grupo3.tp_hito_2.entities.Staff;
import com.grupo3.tp_hito_2.entities.UnidadDeVenta;
import com.grupo3.tp_hito_2.repositories.IStaffRepository;
import com.grupo3.tp_hito_2.repositories.IUnidadDeVentaRepository;
import com.grupo3.tp_hito_2.services.implementation.RentabilidadService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.time.LocalDate;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ResponsableWebController {

    private final IStaffRepository staffRepository;
    private final RentabilidadService rentabilidadService;
    private final IUnidadDeVentaRepository unidadDeVentaRepository;

    public ResponsableWebController(
            IStaffRepository staffRepository,
            IUnidadDeVentaRepository unidadDeVentaRepository,
            RentabilidadService rentabilidadService) {

        this.staffRepository = staffRepository;
        this.unidadDeVentaRepository = unidadDeVentaRepository;
        this.rentabilidadService = rentabilidadService;
    }

    @GetMapping("/responsable/rentabilidad")
    public String consultarRentabilidad(
            Authentication auth,
            Model model) {

        Staff staff = staffRepository
                .findByUser_Username(auth.getName())
                .orElseThrow(() ->
                        new RuntimeException("No se encontró el responsable"));

        Long idUnidad = staff.getUnidadDeVenta().getIdUnidadDeVenta();

        model.addAttribute("festival",
                staff.getUnidadDeVenta().getFestival());

        // Calcular cada componente
        double recaudacion = rentabilidadService.calcularRecaudacion(idUnidad);
        double costoProduccion = rentabilidadService.calcularCostoProduccion(idUnidad);
        double sueldos = rentabilidadService.calcularSueldos(idUnidad);
        double canon = rentabilidadService.calcularCanon(idUnidad);

        // Calcular rentabilidad neta
        double rentabilidad = rentabilidadService.calcularRentabilidadNeta(idUnidad);

        // Enviar los datos a la vista
        model.addAttribute("unidad", staff.getUnidadDeVenta());
        model.addAttribute("recaudacion", recaudacion);
        model.addAttribute("costoProduccion", costoProduccion);
        model.addAttribute("sueldos", sueldos);
        model.addAttribute("canon", canon);
        model.addAttribute("rentabilidad", rentabilidad);

        return "rentabilidad";
    }

    @GetMapping("/responsable/rentabilidad/fechas")
    public String mostrarFormularioRentabilidadFechas(
            Authentication auth,
            Model model) {

        Staff staff = staffRepository
                .findByUser_Username(auth.getName())
                .orElseThrow(() ->
                        new RuntimeException("No se encontró el responsable"));

        // Obtener el festival del responsable
        Long idFestival = staff.getUnidadDeVenta()
                .getFestival().getIdFestival();

        // Buscar todas las unidades de venta de ese festival
        List<UnidadDeVenta> unidades =
                unidadDeVentaRepository.findByFestival_IdFestival(idFestival);

        model.addAttribute("unidades", unidades);
        model.addAttribute("festival",
                staff.getUnidadDeVenta().getFestival());

        return "rentabilidad-fechas";
    }

    @PostMapping("/responsable/rentabilidad/fechas")
    public String calcularRentabilidadFechas(
            Authentication auth,
            @RequestParam Long idUnidadDeVenta,
            @RequestParam LocalDate fechaDesde,
            @RequestParam LocalDate fechaHasta,
            Model model) {

        Staff staff = staffRepository
                .findByUser_Username(auth.getName())
                .orElseThrow(() ->
                        new RuntimeException("No se encontró el responsable"));

        // Obtener el festival asignado al responsable
        Long idFestival = staff.getUnidadDeVenta()
                .getFestival().getIdFestival();

        // IMPORTANTE: enviar el festival a la vista también en el POST
        model.addAttribute("festival",
                staff.getUnidadDeVenta().getFestival());

        // Obtener las unidades de ese festival
        List<UnidadDeVenta> unidades =
                unidadDeVentaRepository.findByFestival_IdFestival(idFestival);

        model.addAttribute("unidades", unidades);

        // Buscar la unidad seleccionada
        UnidadDeVenta unidad = unidadDeVentaRepository.findById(idUnidadDeVenta)
                .orElseThrow(() ->
                        new RuntimeException("No se encontró la unidad de venta"));

        // Verificar que la unidad pertenezca al festival del responsable
        if (unidad.getFestival().getIdFestival() != idFestival) {
            throw new RuntimeException(
                    "La unidad seleccionada no pertenece a tu festival");
        }

        Long idUnidad = unidad.getIdUnidadDeVenta();

        LocalDate inicioFestival = unidad.getFestival().getFechaInicio();
        LocalDate finFestival = unidad.getFestival().getFechaFin();

        // Validar las fechas
        if (fechaDesde.isAfter(fechaHasta)
                || fechaDesde.isBefore(inicioFestival)
                || fechaHasta.isAfter(finFestival)) {

            model.addAttribute("unidadSeleccionada", idUnidad);
            model.addAttribute("unidad", unidad);
            model.addAttribute("error",
                    "Las fechas deben estar dentro del período del festival: "
                            + inicioFestival + " al " + finFestival);

            return "rentabilidad-fechas";
        }

        // Calcular los componentes de rentabilidad
        double recaudacion =
                rentabilidadService.calcularRecaudacionEntreFechas(
                        idUnidad, fechaDesde, fechaHasta);

        double costoProduccion =
                rentabilidadService.calcularCostoProduccionEntreFechas(
                        idUnidad, fechaDesde, fechaHasta);

        double sueldos =
                rentabilidadService.calcularSueldosEntreFechas(
                        idUnidad, fechaDesde, fechaHasta);

        double canon =
                rentabilidadService.calcularCanonEntreFechas(
                        idUnidad, fechaDesde, fechaHasta);

        double rentabilidad =
                rentabilidadService.calcularRentabilidadNetaEntreFechas(
                        idUnidad, fechaDesde, fechaHasta);

        // Enviar los resultados a la vista
        model.addAttribute("unidad", unidad);
        model.addAttribute("unidadSeleccionada", idUnidad);
        model.addAttribute("fechaDesde", fechaDesde);
        model.addAttribute("fechaHasta", fechaHasta);
        model.addAttribute("recaudacion", recaudacion);
        model.addAttribute("costoProduccion", costoProduccion);
        model.addAttribute("sueldos", sueldos);
        model.addAttribute("canon", canon);
        model.addAttribute("rentabilidad", rentabilidad);

        return "rentabilidad-fechas";
    }
}
