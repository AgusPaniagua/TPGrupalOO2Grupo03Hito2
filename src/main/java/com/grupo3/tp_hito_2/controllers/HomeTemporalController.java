package com.grupo3.tp_hito_2.controllers;

import java.security.Principal;

import org.springframework.security.core.Authentication;

import org.springframework.security.core.GrantedAuthority;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.grupo3.tp_hito_2.services.ICierreCajaService;

@Controller
public class HomeTemporalController {

    private final ICierreCajaService cierreCajaService;

    public HomeTemporalController(ICierreCajaService cierreCajaService) {
        this.cierreCajaService = cierreCajaService;
    }

    @GetMapping({"/home", "/index"})
    public String home(Authentication auth) {
        if(auth==null){
            return "redirect:/login";
        }
        for(GrantedAuthority a : auth.getAuthorities()){
            switch (a.getAuthority()) {
                case "ROLE_ADMIN"       ->{return "redirect:/admin/home"; }
                case "ROLE_RESPONSABLE" ->{return "redirect:/responsable/home"; }
                case "ROLE_EMPLEADO"    ->{return "redirect:/empleado/home"; }
                default -> {}
            }
        }
        return "redirect:/login";  
    }

    @GetMapping("/admin/home")
    public String adminHome() { return "admin-home"; }

    @GetMapping("/responsable/home")
    public String responsableHome() { return "responsable-home"; }

    @GetMapping("/empleado/home")
    public String empleadoHome(Principal principal, Model model) {
        // Solo los cajeros ven la opción de cerrar caja
        model.addAttribute("esCajero", cierreCajaService.esCajero(principal.getName()));
        return "empleado-home";
    }
}
