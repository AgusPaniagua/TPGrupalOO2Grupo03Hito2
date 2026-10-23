package com.grupo3.tp_hito_2.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.grupo3.tp_hito_2.helpers.ViewRouteHelper;

@Controller 
public class UserController {

    

	@GetMapping("/logout")
	public String logout(Model model) {
		return ViewRouteHelper.USER_LOGOUT;
	}

	@GetMapping("/loginsuccess")
	public String loginCheck() {
		return "redirect:/index";
	}

}
