package com.ftn.TravelOrganisation.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ftn.TravelOgranisation.util.DateUtil;
import com.ftn.TravelOrganisation.model.Rezervacija;
import com.ftn.TravelOrganisation.repository.RezervacijaRepository;

@Controller
@RequestMapping("/izvestaj")
public class IzvestajController {

	RezervacijaRepository rezervacijaRepository;

	public IzvestajController(RezervacijaRepository rezervacijaRepository) {
		this.rezervacijaRepository = rezervacijaRepository;
	}

	@GetMapping
	public ModelAndView prikaziIzvestaj() {

		List<Rezervacija> rezervacije = rezervacijaRepository.findAll();

		ModelAndView rezultat = new ModelAndView("izvestaj");

		rezultat.addObject("rezervacije", rezervacije);

		return rezultat;
	}

	@GetMapping("/pretraga")
	public ModelAndView prikaziIzvestajFiltered(@RequestParam(required = false) String datumPolaskaStr,
			@RequestParam(required = false) String datumPovratkaStr, Model model) {
		LocalDate datumPolaska = null;
		LocalDate datumPovratka = null;
		Double ukupnaCena = (double) 0;
		if (datumPolaskaStr != "") {
			datumPolaska = DateUtil.stringToDate(datumPolaskaStr);
		}
		if (datumPovratkaStr != "") {
			datumPovratka = DateUtil.stringToDate(datumPovratkaStr);
		}
		List<Rezervacija> rezultatiPretrage = rezervacijaRepository.findAllFiltered(datumPolaska, datumPovratka);

		for (Rezervacija rezervacija : rezultatiPretrage) {
			ukupnaCena += rezervacija.getCena();
		}
		System.out.println(rezultatiPretrage);

		ModelAndView rezultat = new ModelAndView("izvestaj");
		rezultat.addObject("rezervacije", rezultatiPretrage);
		rezultat.addObject("ukupnaCena", ukupnaCena);
		rezultat.addObject("brojRezervacija", rezultatiPretrage.size());

		return rezultat;
	}

}
