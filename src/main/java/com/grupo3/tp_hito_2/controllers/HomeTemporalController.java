package com.grupo3.tp_hito_2.controllers;

import org.springframework.security.core.Authentication;

import org.springframework.security.core.GrantedAuthority;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeTemporalController {

    @GetMapping("/")
    public String raiz(){
        return "redirect:/login";
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
    public String empleadoHome() { return "empleado-home"; }
}
