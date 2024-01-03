package com.ftn.TravelOrganisation.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ftn.TravelOrganisation.model.Destinacija;
import com.ftn.TravelOrganisation.model.Korisnik;
import com.ftn.TravelOrganisation.model.Putovanje;
import com.ftn.TravelOrganisation.repository.DestinacijaRepository;
import com.ftn.TravelOrganisation.repository.PutovanjeRepository;
import com.ftn.TravelOrganisation.service.RegisterService;

@Controller
@RequestMapping("")
public class PutovanjaController {

	@Autowired
	private	PutovanjeRepository putovanjeRepository;

	@GetMapping("/putovanja")
	public ModelAndView prikaziPutovanja() {
		ModelAndView rezultat = new ModelAndView("putovanjaPage");
		List<Putovanje> putovanja = putovanjeRepository.findAll();
		rezultat.addObject("listaPutovanja", putovanja);
		return rezultat;

	}
	
	@GetMapping("/putovanje/detalji/{id}")
	public ModelAndView prikaziPutovanje(@PathVariable Long id) {
		ModelAndView rezultat = new ModelAndView("fragments/putovanje");
		System.out.println(id);
		Putovanje putovanje = putovanjeRepository.findOne(id);
		rezultat.addObject("putovanje", putovanje);
		return rezultat;

	}
	
}
