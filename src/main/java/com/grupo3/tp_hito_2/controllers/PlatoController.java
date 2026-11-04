package com.grupo3.tp_hito_2.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import com.grupo3.tp_hito_2.services.IPlatoService;

import jakarta.validation.Valid;

import com.grupo3.tp_hito_2.dtos.PlatoDTO;

@Controller 
@RequestMapping("/responsable/platos")
public class PlatoController {
    private final IPlatoService platoService;

    public PlatoController(IPlatoService platoService){
        this.platoService=platoService;
    }

    @GetMapping
    public String listarPlatos(Authentication authentication, Model model){
        List<PlatoDTO> platos = platoService.listarPlatosResponsable(authentication.getName());
        model.addAttribute("platos", platos);
        return "platos";
    }

    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model){
        model.addAttribute("platoDTO", new PlatoDTO());
        return "plato-form";
    }

    @PostMapping("/guardar")
    public String guardarPlato(@Valid @ModelAttribute("platoDTO")PlatoDTO dto,
            BindingResult result,
            Authentication authentication,
            RedirectAttributes redirectAttributes,
            Model model){
        if(result.hasErrors()){
            return "plato-form";
        }
        try{
            platoService.registrarPlato(dto,authentication.getName());
            redirectAttributes.addFlashAttribute("mensaje","Plato registrado correctamente");
            return "redirect:/responsable/platos";
        }catch(IllegalArgumentException e){
            model.addAttribute("error",e.getMessage());
            return "plato-form";
        }
    }    

}
