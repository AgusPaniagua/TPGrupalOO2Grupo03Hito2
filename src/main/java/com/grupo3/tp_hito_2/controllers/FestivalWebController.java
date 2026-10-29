
package com.grupo3.tp_hito_2.controllers;

import com.grupo3.tp_hito_2.services.IFestivalService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FestivalWebController {

    private final IFestivalService festivalService;

    public FestivalWebController(IFestivalService festivalService) {
        this.festivalService = festivalService;
    }

    @GetMapping("/festivales")
    public String listarFestivales(Model model) {
        model.addAttribute("festivales", festivalService.getAll());
        return "festivales";
    }
}
