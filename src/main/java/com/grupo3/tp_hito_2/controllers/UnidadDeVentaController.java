package com.grupo3.tp_hito_2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

import com.grupo3.tp_hito_2.dtos.RegistroUnidadDTO;
import com.grupo3.tp_hito_2.repositories.IFestivalRepository;
import com.grupo3.tp_hito_2.services.IUnidadDeVentaService;

@Controller
@RequestMapping("/unidades")
public class UnidadDeVentaController {
    
    @Autowired 
    private IUnidadDeVentaService unidadDeVentaService;
    
    @Autowired 
    private IFestivalRepository festivalRepository;

    @GetMapping("/registro")
    public String mostrarFormularioDeRegistro(Model model){
        model.addAttribute("registroUnidadDTO",new RegistroUnidadDTO());
        model.addAttribute("festivales", festivalRepository.findAll());
        return "registro-unidad";
    }

    @PostMapping("/guardar")
    public String registrarUnidad(@ModelAttribute("registroUnidadDTO") RegistroUnidadDTO dto, Model model){
        try{
            unidadDeVentaService.registrarConResponsable(dto);
            return "redirect:/index?exito=true";
        }catch(IllegalArgumentException e){
            model.addAttribute("error",e.getMessage());
            model.addAttribute("festivales",festivalRepository.findAll());
            return "registro-unidad";
        }
    }
}
