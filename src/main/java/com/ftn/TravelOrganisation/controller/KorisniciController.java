package com.ftn.TravelOrganisation.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftn.TravelOrganisation.model.Korisnik;
import com.ftn.TravelOrganisation.model.KorisnikUloga;
import com.ftn.TravelOrganisation.model.LoyaltyKartica;
import com.ftn.TravelOrganisation.model.Putovanje;
import com.ftn.TravelOrganisation.repository.KorisnikRepository;
import com.ftn.TravelOrganisation.repository.LoyaltyKarticaRepository;
import com.ftn.TravelOrganisation.repository.PutovanjeRepository;
import com.ftn.TravelOrganisation.service.KorisnikService;
import com.mysql.cj.Session;

@Controller
@RequestMapping("")
public class KorisniciController {

	public static final String PRIJAVLJENI_KORISNIK = "prijavljeni_korisnik";

	KorisnikRepository korisnikRepository;
	KorisnikService korisnikService;
	PutovanjeRepository putovanjeRepository;
	LoyaltyKarticaRepository loyaltyKarticaRepository;
	HttpSession session;

	@Autowired
	public KorisniciController(KorisnikRepository korisnikRepository, KorisnikService korisnikService,
			PutovanjeRepository putovanjeRepository, LoyaltyKarticaRepository loyaltyKarticaRepository,
			HttpSession session) {
		this.korisnikService = korisnikService;
		this.korisnikRepository = korisnikRepository;
		this.putovanjeRepository = putovanjeRepository;
		this.loyaltyKarticaRepository = loyaltyKarticaRepository;
		this.session = session;
	}

	@GetMapping("korisnici")
	public ModelAndView pregledKorisnika() {
		ModelAndView rezultat = new ModelAndView("korisnici");
		List<LoyaltyKartica> listaKartica = loyaltyKarticaRepository.findAll();
		List<Korisnik> listaKorisnika = korisnikRepository.findAll();
		rezultat.addObject("listaKartica", listaKartica);
		rezultat.addObject("listaKorisnika", listaKorisnika);
		return rezultat;
	}

	@PostMapping("korisnici/acceptKartica")
	public ResponseEntity<String> acceptKartica(@RequestParam Long id) {
		loyaltyKarticaRepository.acceptKartica(id);
		return ResponseEntity.ok("Kartica accepted successfully");
	}
	
	@PostMapping("korisnici/rejectKartica")
	public ResponseEntity<String> rejectKartica(@RequestParam Long id) {
		loyaltyKarticaRepository.rejectKartica(id);
		return ResponseEntity.ok("Kartica rejected successfully");
	}

	@GetMapping("profil")
	public ModelAndView pregledProfila(HttpSession session) {
		Korisnik prijavljeniKorisnik = (Korisnik) session.getAttribute(PRIJAVLJENI_KORISNIK);
		if (prijavljeniKorisnik == null) {

			return new ModelAndView("redirect:/login");
		}

		LoyaltyKartica loyaltyKartica = loyaltyKarticaRepository.findByKorisnikId(prijavljeniKorisnik.getId());
		ModelAndView rezultat = new ModelAndView("profil");
		List<Putovanje> listaPutovanja = putovanjeRepository.findAll();

		rezultat.addObject("listaPutovanja", listaPutovanja);
		rezultat.addObject("kartica", loyaltyKartica);

		return rezultat;
	}

	@PostMapping("korisnici/block")
	@ResponseBody
	public ResponseEntity<Korisnik> blockKorisnika(@RequestParam Long id) {
		Korisnik updatedKorisnik = korisnikRepository.findOne(id);
		korisnikRepository.toggleBlock(updatedKorisnik);
		updatedKorisnik.setBlokiran(!updatedKorisnik.isBlokiran());
		return ResponseEntity.ok(updatedKorisnik);

	}

	@PostMapping("korisnici/loyaltyCardApplication")
	@ResponseBody
	public ResponseEntity<Boolean> loyaltyCardApplication(@RequestParam String id) {

		Korisnik korisnik = korisnikRepository.findOne(Long.parseLong(id));

		LoyaltyKartica novaKartica = new LoyaltyKartica(5, korisnik, false);
		if (loyaltyKarticaRepository.save(novaKartica) == 1) {
			System.out.println("dodan");

			return ResponseEntity.ok(true);
		} else {
			return ResponseEntity.ok(false);

		}

	}

	@PostMapping("korisnici/edit")
	public ResponseEntity<String> edit(@RequestBody String jsonData, HttpSession session, HttpServletRequest request) {

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(jsonData);

			String ime = jsonNode.get("ime").asText();
			String prezime = jsonNode.get("prezime").asText();
			String email = jsonNode.get("email").asText();
			String sifra = jsonNode.get("sifra").asText();
			String brTelefona = jsonNode.get("brTelefona").asText();

			Korisnik prijavljeniKorisnik = (Korisnik) session.getAttribute(PRIJAVLJENI_KORISNIK);

			prijavljeniKorisnik.setIme(ime);
			prijavljeniKorisnik.setPrezime(prezime);
			prijavljeniKorisnik.setEmail(email);
			prijavljeniKorisnik.setSifra(sifra);
			prijavljeniKorisnik.setBrojTelefona(brTelefona);

			session.setAttribute(PRIJAVLJENI_KORISNIK, prijavljeniKorisnik);

			korisnikRepository.update(prijavljeniKorisnik);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Greška prilikom parsiranja JSON-a.");
		}

		return ResponseEntity.ok("Putovanje uspešno dodato!");
	}

	@GetMapping("korisnici/getByUloga")
	public String getByUloga(@RequestParam String selectedUloga, Model model) {
		KorisnikUloga uloga = KorisnikUloga.valueOf(selectedUloga);
		List<Korisnik> updatedLista = korisnikRepository.findByUloga(uloga);
		model.addAttribute("listaKorisnika", updatedLista);
		return "fragments/korisniciTableRows :: table";
	}

	@GetMapping("korisnici/getByKorisnickoIme")
	public String getByKorisnickoIme(@RequestParam String userSearch, Model model) {

		List<Korisnik> updatedLista = korisnikRepository.findByKorisnickoImeContains(userSearch);
		model.addAttribute("listaKorisnika", updatedLista);
		return "fragments/korisniciTableRows :: table";
	}

}
