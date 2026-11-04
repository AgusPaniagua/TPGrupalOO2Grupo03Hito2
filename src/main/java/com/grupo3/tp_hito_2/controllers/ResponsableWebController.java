package com.grupo3.tp_hito_2.controllers;
import com.grupo3.tp_hito_2.entities.Staff;
import com.grupo3.tp_hito_2.repositories.IStaffRepository;
import com.grupo3.tp_hito_2.services.implementation.RentabilidadService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;


@Controller
public class ResponsableWebController {

    private final IStaffRepository staffRepository;
    private final RentabilidadService rentabilidadService;

    public ResponsableWebController(
            IStaffRepository staffRepository,
            RentabilidadService rentabilidadService) {

        this.staffRepository = staffRepository;
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
}