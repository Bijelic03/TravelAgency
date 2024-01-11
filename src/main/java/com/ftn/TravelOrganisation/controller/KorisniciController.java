package com.ftn.TravelOrganisation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ftn.TravelOrganisation.model.Korisnik;
import com.ftn.TravelOrganisation.model.KorisnikUloga;
import com.ftn.TravelOrganisation.repository.KorisnikRepository;
import com.ftn.TravelOrganisation.service.KorisnikService;

@Controller
@RequestMapping("korisnici")
public class KorisniciController {

	KorisnikRepository korisnikRepository;
	KorisnikService korisnikService;

	@Autowired
	public KorisniciController(KorisnikRepository korisnikRepository, KorisnikService korisnikService) {
		this.korisnikService = korisnikService;
		this.korisnikRepository = korisnikRepository;
	}

	@GetMapping("")
	public ModelAndView pregledKorisnika() {
		ModelAndView rezultat = new ModelAndView("korisnici");
		List<Korisnik> listaKorisnika = korisnikRepository.findAll();
		rezultat.addObject("listaKorisnika", listaKorisnika);
		return rezultat;
	}

	@PostMapping("/block")
	@ResponseBody
	public ResponseEntity<Korisnik> blockKorisnika(@RequestParam Long id) {
		Korisnik updatedKorisnik = korisnikRepository.findOne(id);
		korisnikRepository.toggleBlock(updatedKorisnik);
		updatedKorisnik.setBlokiran(!updatedKorisnik.isBlokiran());
		return ResponseEntity.ok(updatedKorisnik);

	}

	@GetMapping("/getByUloga")
	public String getByUloga(@RequestParam String selectedUloga, Model model) {
		KorisnikUloga uloga = KorisnikUloga.valueOf(selectedUloga);
		List<Korisnik> updatedLista = korisnikRepository.findByUloga(uloga);
		model.addAttribute("listaKorisnika", updatedLista);
		return "fragments/korisniciTableRows :: table";
	}

	@GetMapping("/getByKorisnickoIme")
	public String getByKorisnickoIme(@RequestParam String userSearch, Model model) {

		List<Korisnik> updatedLista = korisnikRepository.findByKorisnickoImeContains(userSearch);
		model.addAttribute("listaKorisnika", updatedLista);
		return "fragments/korisniciTableRows :: table";
	}

}
