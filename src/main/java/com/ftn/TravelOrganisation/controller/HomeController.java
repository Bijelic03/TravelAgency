package com.ftn.TravelOrganisation.controller;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ftn.TravelOrganisation.model.Putovanje;
import com.ftn.TravelOrganisation.repository.PutovanjeRepository;
import com.ftn.TravelOrganisation.service.PutovanjaService;

@Controller
public class HomeController {

	private final String bURL;

	public static final String PRIJAVLJENI_KORISNIK = "prijavljeni_korisnik";

	private final PutovanjaService putovanjaService;

	private final PutovanjeRepository putovanjeRepository;

	@Autowired
	public HomeController(ServletContext servletContext, PutovanjaService putovanjaService,
			PutovanjeRepository putovanjeRepository) {
		this.putovanjaService = putovanjaService;
		this.putovanjeRepository = putovanjeRepository;
		this.bURL = servletContext.getContextPath();
	}

	@GetMapping
	public ModelAndView getHome(HttpSession session) {

		ModelAndView rezultat = new ModelAndView("home");
		List<Putovanje> listaPutovanja = putovanjeRepository.findAll();

		rezultat.addObject("listaPutovanja", listaPutovanja);
		return rezultat;
	}

}
