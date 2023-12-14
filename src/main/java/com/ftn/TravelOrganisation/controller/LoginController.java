package com.ftn.TravelOrganisation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ftn.TravelOrganisation.service.RegisterService;

@Controller
public class LoginController {

	@Autowired
	RegisterService registerService;
	
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String korisnickoIme, @RequestParam String sifra) {
    	
    	registerService.login(korisnickoIme, sifra);
        return "redirect:/";
    }
    
    
}
