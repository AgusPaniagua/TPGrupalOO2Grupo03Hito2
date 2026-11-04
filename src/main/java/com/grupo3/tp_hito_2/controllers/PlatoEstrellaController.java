package com.grupo3.tp_hito_2.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.grupo3.tp_hito_2.dtos.PlatoDTO;
import com.grupo3.tp_hito_2.entities.Festival;
import com.grupo3.tp_hito_2.entities.Staff;
import com.grupo3.tp_hito_2.entities.UnidadDeVenta;
import com.grupo3.tp_hito_2.repositories.IStaffRepository;
import com.grupo3.tp_hito_2.services.IPlatoService;

import org.springframework.transaction.annotation.Transactional;

@Controller
@RequestMapping("/empleado/plato-estrella")
public class PlatoEstrellaController {

    private final IPlatoService platoService;
    private final IStaffRepository staffRepository;

    public PlatoEstrellaController(IPlatoService platoService, IStaffRepository staffRepository) {
        this.platoService = platoService;
        this.staffRepository = staffRepository;
    }

    @GetMapping
    @Transactional(readOnly = true)
    public String consultar(Authentication authentication, Model model) {
        Staff empleado = staffRepository.findByUser_Username(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
        UnidadDeVenta unidad = empleado.getUnidadDeVenta();
        if (unidad == null || !empleado.isActivo()) {
            model.addAttribute("error", "No tenes una unidad de venta asignada");
            return "plato-estrella";
        }
        Festival festival = unidad.getFestival();
        if (festival == null) {
            model.addAttribute("error", "Tu unidad no tiene un festival asignado");
            return "plato-estrella";
        }
        model.addAttribute("festival", festival);
        model.addAttribute("unidad", unidad);
        PlatoDTO platoEstrella = platoService.obtenerPlatoEstrella(
                unidad.getIdUnidadDeVenta(),
                festival.getIdFestival());

        model.addAttribute("platoEstrella", platoEstrella);
        return "plato-estrella";
    }
}