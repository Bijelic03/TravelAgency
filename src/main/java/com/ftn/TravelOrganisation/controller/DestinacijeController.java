package com.ftn.TravelOrganisation.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

import com.ftn.TravelOrganisation.bean.SecondConfiguration.ApplicationMemory;
import com.ftn.TravelOrganisation.model.Destinacija;
import com.ftn.TravelOrganisation.repository.DestinacijaRepository;
import com.ftn.TravelOrganisation.service.DestinacijeService;

@Controller
@RequestMapping("")
public class DestinacijeController {

	private final String bURL;

	private final DestinacijeService destinacijeService;

	private final DestinacijaRepository destinacijaRepository;

	@Autowired
	public DestinacijeController(ServletContext servletContext, DestinacijeService destinacijeService,
			DestinacijaRepository destinacijaRepository) {
		this.destinacijeService = destinacijeService;
		this.destinacijaRepository = destinacijaRepository;
		this.bURL = servletContext.getContextPath();
	}

	@GetMapping("destinacije")
	@ResponseBody
	public ModelAndView sveDestinacije() {
		ModelAndView rezultat = new ModelAndView("destinacije");
		List<Destinacija> listaDestinacija = destinacijaRepository.findAll();
		rezultat.addObject("listaDestinacija", listaDestinacija); // Dodajemo listu pod imenom "listaDestinacija"
		return rezultat;
	}

	@GetMapping("destinacije/dodaj")
	@ResponseBody
	public ModelAndView dodajDestinaciju(HttpServletResponse response) throws IOException {

		ModelAndView rezultat = new ModelAndView("addDestinacija");
		return rezultat;
		
	}
	@PostMapping("destinacije/add")
	public void addDestinacju(@ModelAttribute Destinacija destinacija, HttpServletResponse response)
			throws IOException {
		destinacijaRepository.save(destinacija);
		response.sendRedirect(bURL + "/destinacije");
	}

	@PostMapping("destinacije/edit")
	public void editDestinacju(@ModelAttribute Destinacija destinacija, HttpServletResponse response)
			throws IOException {
		destinacijaRepository.update(destinacija);
		response.sendRedirect(bURL + "/destinacije");
	}

	@PostMapping(value = "destinacije/delete")
	public void delete(@RequestParam Long id, HttpServletResponse response) throws IOException {
		destinacijaRepository.delete(id);
		response.sendRedirect(bURL + "/destinacije");
	}

	@PostMapping("destinacije/izmeni")
	@ResponseBody
	public ModelAndView izmeniDestinaciju(HttpServletResponse response, Long id) throws IOException {

		// Destinacije destinacije = (Destinacije)
		// memorijaAplikacije.get(DestinacijeService.DESTINACIJE_KEY);

		Destinacija destinacija = destinacijaRepository.findOne(id);

		ModelAndView rezultat = new ModelAndView("editDestinacija");
		rezultat.addObject("destinacija", destinacija); 
		return rezultat;


	}

}
