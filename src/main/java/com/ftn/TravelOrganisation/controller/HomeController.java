package com.ftn.TravelOrganisation.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ftn.TravelOrganisation.model.Korisnik;

@Controller
public class HomeController {

	public static final String PRIJAVLJENI_KORISNIK = "prijavljeni_korisnik";

	@GetMapping
	public ModelAndView getHome(HttpSession session) {

				ModelAndView rezultat = new ModelAndView("home");
		

		return rezultat;
	}

}
