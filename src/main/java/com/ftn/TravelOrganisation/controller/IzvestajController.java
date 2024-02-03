package com.ftn.TravelOrganisation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/izvestaj")
public class IzvestajController {

	
	@GetMapping
	public String prikaziIzvestaj() {
		return "izvestaj";
	}
	
	
}
