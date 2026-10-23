package com.grupo3.tp_hito_2.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller 
public class AuthController {
    
    @GetMapping("/login")
    public String login(
        @RequestParam(value = "error", required = false)String error,
        @RequestParam(value = "logout", required = false)String logout,
        Model model){
            if(error!= null){
                model.addAttribute("error", "Usuario o contraseña incorrectos");
            }
            if(logout!=null){
                model.addAttribute("logout", "Has cerrado sesion correctamente");
            }
            return "login";
        }
}
