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
        public String consultarRentabilidad(Authentication auth, Model model) {

                UnidadDeVenta unidad = unidadDeVentaRepository
                                .findByResponsableUsername(auth.getName())
                                .orElseThrow(() -> new RuntimeException(
                                                "No se encontró la unidad de venta del responsable"));

                Long idUnidad = unidad.getIdUnidadDeVenta();

                model.addAttribute("festival", unidad.getFestival());
                model.addAttribute("unidad", unidad);
                model.addAttribute("recaudacion", rentabilidadService.calcularRecaudacion(idUnidad));
                model.addAttribute("costoProduccion", rentabilidadService.calcularCostoProduccion(idUnidad));
                model.addAttribute("sueldos", rentabilidadService.calcularSueldos(idUnidad));
                model.addAttribute("canon", rentabilidadService.calcularCanon(idUnidad));
                model.addAttribute("rentabilidad", rentabilidadService.calcularRentabilidadNeta(idUnidad));

                return "rentabilidad";
        }

        @GetMapping("/responsable/rentabilidad/fechas")
public String mostrarFormularioRentabilidadFechas(
        Authentication auth,
        Model model) {

    UnidadDeVenta unidadResponsable = unidadDeVentaRepository
            .findByResponsableUsername(auth.getName())
            .orElseThrow(() -> new RuntimeException(
                    "No se encontró la unidad de venta del responsable"));

    Long idFestival = unidadResponsable.getFestival().getIdFestival();

    List<UnidadDeVenta> unidades =
            unidadDeVentaRepository.findByFestival_IdFestival(idFestival);

    model.addAttribute("unidades", unidades);
    model.addAttribute("festival", unidadResponsable.getFestival());

    return "rentabilidad-fechas";
}

@PostMapping("/responsable/rentabilidad/fechas")
public String calcularRentabilidadFechas(
        Authentication auth,
        @RequestParam Long idUnidadDeVenta,
        @RequestParam LocalDate fechaDesde,
        @RequestParam LocalDate fechaHasta,
        Model model) {

    UnidadDeVenta unidadResponsable = unidadDeVentaRepository
            .findByResponsableUsername(auth.getName())
            .orElseThrow(() -> new RuntimeException(
                    "No se encontró la unidad de venta del responsable"));

    Long idFestival = unidadResponsable.getFestival().getIdFestival();

    model.addAttribute("festival", unidadResponsable.getFestival());

    List<UnidadDeVenta> unidades =
            unidadDeVentaRepository.findByFestival_IdFestival(idFestival);
    model.addAttribute("unidades", unidades);

    UnidadDeVenta unidad = unidadDeVentaRepository.findById(idUnidadDeVenta)
            .orElseThrow(() -> new RuntimeException("No se encontró la unidad de venta"));

        if (unidad.getFestival().getIdFestival() != idFestival) {
        throw new RuntimeException("La unidad seleccionada no pertenece a tu festival");
    }

    Long idUnidad = unidad.getIdUnidadDeVenta();

    LocalDate inicioFestival = unidad.getFestival().getFechaInicio();
    LocalDate finFestival = unidad.getFestival().getFechaFin();

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

    double recaudacion = rentabilidadService.calcularRecaudacionEntreFechas(idUnidad, fechaDesde, fechaHasta);
    double costoProduccion = rentabilidadService.calcularCostoProduccionEntreFechas(idUnidad, fechaDesde, fechaHasta);
    double sueldos = rentabilidadService.calcularSueldosEntreFechas(idUnidad, fechaDesde, fechaHasta);
    double canon = rentabilidadService.calcularCanonEntreFechas(idUnidad, fechaDesde, fechaHasta);
    double rentabilidad = rentabilidadService.calcularRentabilidadNetaEntreFechas(idUnidad, fechaDesde, fechaHasta);

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
