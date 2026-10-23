package com.grupo3.tp_hito_2.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeTemporalController {
    @GetMapping("/index")
    @ResponseBody
    public String index() { return "Inicio"; }

    @GetMapping("/admin/home")
    @ResponseBody
    public String adminHome() { return "Home del admin"; }

    @GetMapping("/responsable/home")
    @ResponseBody
    public String responsableHome() { return "Home del responsable"; }

    @GetMapping("/empleado/home")
    @ResponseBody
    public String empleadoHome() { return "Home del empleado"; }
}
