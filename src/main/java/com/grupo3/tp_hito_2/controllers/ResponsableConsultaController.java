package com.grupo3.tp_hito_2.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.grupo3.tp_hito_2.dtos.FiltroPlatoDTO;
import com.grupo3.tp_hito_2.dtos.FiltroStaffDTO;
import com.grupo3.tp_hito_2.services.IResponsableConsultaService;

// Consultas del RESPONSABLE sobre su unidad de venta (/responsable/** -> ROLE_RESPONSABLE).
@Controller
@RequestMapping("/responsable")
public class ResponsableConsultaController {

    private final IResponsableConsultaService responsableConsultaService;

    public ResponsableConsultaController(IResponsableConsultaService responsableConsultaService) {
        this.responsableConsultaService = responsableConsultaService;
    }

    // CU Responsable 2: filtros de empleados por uno o varios atributos
    @GetMapping("/empleados/filtrar")
    public String filtrarEmpleados(@ModelAttribute("filtro") FiltroStaffDTO filtro,
                                   BindingResult binding, Principal principal, Model model) {
        if (binding.hasErrors()) {
            model.addAttribute("error", "Alguno de los filtros tiene un formato inválido.");
            model.addAttribute("empleados", List.of());
            return "responsable-empleados-filtro";
        }
        try {
            model.addAttribute("empleados", responsableConsultaService.filtrarEmpleados(principal.getName(), filtro));
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("empleados", List.of());
        }
        return "responsable-empleados-filtro";
    }

    // CU Responsable 2: filtros de platos de la unidad
    @GetMapping("/platos/filtrar")
    public String filtrarPlatos(@ModelAttribute("filtro") FiltroPlatoDTO filtro,
                                BindingResult binding, Principal principal, Model model) {
        if (binding.hasErrors()) {
            model.addAttribute("error", "Alguno de los filtros tiene un formato inválido.");
            model.addAttribute("platos", List.of());
            return "responsable-platos-filtro";
        }
        try {
            model.addAttribute("platos", responsableConsultaService.filtrarPlatos(principal.getName(), filtro));
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("platos", List.of());
        }
        return "responsable-platos-filtro";
    }

    // CU Responsable 4: liquidación de haberes del personal de la unidad
    @GetMapping("/liquidacion")
    public String liquidacion(Principal principal, Model model) {
        try {
            model.addAttribute("liquidacion", responsableConsultaService.liquidarHaberes(principal.getName()));
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "responsable-liquidacion";
    }
}
