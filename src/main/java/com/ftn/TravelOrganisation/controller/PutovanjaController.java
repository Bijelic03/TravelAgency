package com.ftn.TravelOrganisation.controller;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.ftn.TravelOrganisation.model.Destinacija;
import com.ftn.TravelOrganisation.model.Interval;
import com.ftn.TravelOrganisation.model.KategorijaPutovanjaEnum;
import com.ftn.TravelOrganisation.model.Korisnik;
import com.ftn.TravelOrganisation.model.LoyaltyKartica;
import com.ftn.TravelOrganisation.model.PrevoznoSredstvo;
import com.ftn.TravelOrganisation.model.PrevoznoSredstvoTipEnum;
import com.ftn.TravelOrganisation.model.Putovanje;
import com.ftn.TravelOrganisation.model.Rezervacija;
import com.ftn.TravelOrganisation.model.RezervacijaStatus;
import com.ftn.TravelOrganisation.model.SmestajnaJedinica;
import com.ftn.TravelOrganisation.model.SmestajnaJedinicaTipEnum;
import com.ftn.TravelOrganisation.model.WishlistItem;
import com.ftn.TravelOrganisation.repository.DestinacijaRepository;
import com.ftn.TravelOrganisation.repository.LoyaltyKarticaRepository;
import com.ftn.TravelOrganisation.repository.PrevoznoSredstvoRepository;
import com.ftn.TravelOrganisation.repository.PutovanjeRepository;
import com.ftn.TravelOrganisation.repository.RezervacijaRepository;
import com.ftn.TravelOrganisation.repository.SmestajnaJedinicaRepository;
import com.ftn.TravelOrganisation.repository.WishlistRepository;
import com.ftn.TravelOrganisation.service.DestinacijeService;
import com.ftn.TravelOrganisation.service.PutovanjaService;
import com.ftn.TravelOrganisation.service.RegisterService;
import com.ftn.TravelOrganisation.service.RezervacijeService;

@Controller
@RequestMapping("")
public class PutovanjaController {

	public static final String PRIJAVLJENI_KORISNIK = "prijavljeni_korisnik";

	private final String bURL;

	private final ObjectMapper objectMapper;

	private final PrevoznoSredstvoRepository prevoznoSredstvoRepository;

	private final PutovanjeRepository putovanjeRepository;

	private final LoyaltyKarticaRepository loyaltyKarticaRepository;

	private DestinacijaRepository destinacijaRepository;

	private final SmestajnaJedinicaRepository smestajnaJedinicaRepository;

	private final ServletContext servletContext;

	private final RezervacijeService rezervacijeService;

	private final RezervacijaRepository rezervacijaRepository;

	private final WishlistRepository wishlistRepository;

	@Autowired
	public PutovanjaController(ServletContext servletContext, ObjectMapper objectMapper,
			PrevoznoSredstvoRepository prevoznoSredstvoRepository, PutovanjeRepository putovanjeRepository,
			DestinacijaRepository destinacijaRepository, SmestajnaJedinicaRepository smestajnaJedinicaRepository,
			LoyaltyKarticaRepository loyaltyKarticaRepository, RezervacijeService rezervacijeService,
			RezervacijaRepository rezervacijaRepository, WishlistRepository wishlistRepository) {
		this.servletContext = servletContext;
		this.prevoznoSredstvoRepository = prevoznoSredstvoRepository;
		this.objectMapper = objectMapper;
		this.putovanjeRepository = putovanjeRepository;
		this.destinacijaRepository = destinacijaRepository;
		this.smestajnaJedinicaRepository = smestajnaJedinicaRepository;
		this.bURL = servletContext.getContextPath();
		this.loyaltyKarticaRepository = loyaltyKarticaRepository;
		this.rezervacijeService = rezervacijeService;
		this.rezervacijaRepository = rezervacijaRepository;
		this.wishlistRepository = wishlistRepository;
	}

	@GetMapping("/putovanja")
	public ModelAndView prikaziPutovanja() {
		ModelAndView rezultat = new ModelAndView("putovanjaPage");
		List<Putovanje> putovanja = putovanjeRepository.findAll();
		System.out.println("putovanjaa");
		rezultat.addObject("listaPutovanja", putovanja);
		return rezultat;

	}

	@GetMapping("/putovanja/dodaj")
	public ModelAndView dodajPutovanje() {
		ModelAndView rezultat = new ModelAndView("fragments/addPutovanje");
		List<Destinacija> destinacije = destinacijaRepository.findAll();
		rezultat.addObject("destinacije", destinacije);
		return rezultat;

	}

	@PostMapping("/putovanja/odobriRezervaciju")
	public String odobriRezervaciju(@RequestParam("idRezervacije") Long idRezervacije) {
		rezervacijaRepository.updateStatus(idRezervacije, RezervacijaStatus.ODOBRENA);
		return "redirect:/";
	}

	@GetMapping("/putovanja/shoppingCart")
	public ModelAndView prikaziShoppingCart(HttpSession session, HttpServletRequest request) {
		Korisnik ulogovaniKorisnik = (Korisnik) session.getAttribute(PRIJAVLJENI_KORISNIK);
		ModelAndView rezultat = new ModelAndView("shoppingCart");

		if (ulogovaniKorisnik != null) {
			LoyaltyKartica loyaltyKartica = loyaltyKarticaRepository.findByKorisnikId(ulogovaniKorisnik.getId());
			rezultat.addObject("kartica", loyaltyKartica);

		}
		return rezultat;
	}

	@PostMapping("/putovanja/wishlist/remove")
	public String removeWishlistItem(@RequestParam("idWishlist") Long idWishlist) {

		wishlistRepository.remove(idWishlist);

		return "redirect:/profil";
	}

	@PostMapping("/putovanja/reservation/buy")
	public ResponseEntity<String> rezervisi(HttpSession session, HttpServletRequest request,
			@RequestParam int brojBodova, @RequestParam double ukupnaCifra) {
		Korisnik ulogovaniKorisnik = (Korisnik) session.getAttribute(PRIJAVLJENI_KORISNIK);
		List<Rezervacija> rezervacije = (List<Rezervacija>) session.getAttribute("shoppingCart");

		ResponseEntity<String> response = rezervacijeService.handleReservations(rezervacije, brojBodova,
				ulogovaniKorisnik);
		if (response.getStatusCode() == HttpStatus.OK) {

			session.removeAttribute("shoppingCart");
		}

		return null;
	}

	@PostMapping("/putovanja/addToWishLIst")
	public String handlePutovanjeRequest(@RequestParam("putovanjeId") Long putovanjeId, HttpSession session,
			HttpServletRequest request) {
		Korisnik ulogovaniKorisnik = (Korisnik) session.getAttribute(PRIJAVLJENI_KORISNIK);
		Putovanje putovanje = putovanjeRepository.findOne(putovanjeId);
		List<WishlistItem> wishlist = wishlistRepository.findAllByKorisnik(ulogovaniKorisnik.getId());

		for (WishlistItem wishlistItem : wishlist) {
			if (wishlistItem.getPutovanje().getId() == putovanjeId) {
				return "redirect:/";

			}
		}
		WishlistItem wishlistItem = new WishlistItem(ulogovaniKorisnik, putovanje);
		wishlistRepository.save(wishlistItem);

		return "redirect:/";
	}

	@PostMapping("/putovanja/add")
	public ResponseEntity<String> dodajPutovanje(@RequestBody String putovanjeRequest, HttpServletResponse response) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(putovanjeRequest);

			Long destinacijaId = jsonNode.get("destinacijaId").asLong();
			Long prevoznoSredstvoId = jsonNode.get("prevoznoSredstvoId").asLong();
			String selectedSmestaji = jsonNode.get("selectedSmestaji").asText();
			String intervaliStr = jsonNode.get("intervali").asText();
			Double cena = jsonNode.get("cena").asDouble();
			String kategorija = jsonNode.get("kategorija").asText();

			Destinacija destinacija = destinacijaRepository.findOne(destinacijaId);
			PrevoznoSredstvo prevoznoSredstvo = prevoznoSredstvoRepository.findOne(prevoznoSredstvoId);

			String selectedSmestajiJson = selectedSmestaji;
			List<String> smestajiStr = objectMapper.readValue(selectedSmestajiJson, new TypeReference<List<String>>() {
			});
			List<Long> smestajiId = smestajiStr.stream().map(Long::parseLong).collect(Collectors.toList());

			List<SmestajnaJedinica> smestajnaJedinice = smestajnaJedinicaRepository
					.getSmestajneJediniceByIds(smestajiId);

			List<Interval> intervali = new ArrayList<>();

			try {

				// Koristite TypeReference za mapiranje u List<Map<String, String>>
				List<Map<String, String>> intervali1 = objectMapper.readValue(intervaliStr,
						new TypeReference<List<Map<String, String>>>() {
						});

				// Sada možete raditi sa listom intervala
				for (Map<String, String> interval : intervali1) {
					String datumPolaskaStr = interval.get("datumPolaska");
					String datumPovratkaStr = interval.get("datumPovratka");

					LocalDate datumPolaska = LocalDate.parse(datumPolaskaStr);
					LocalDate datumPovratka = LocalDate.parse(datumPovratkaStr);

					int brojNocenja = (int) datumPolaska.until(datumPovratka, ChronoUnit.DAYS);

					Interval noviTermin = new Interval(datumPolaska, datumPovratka, brojNocenja);
					intervali.add(noviTermin);

				}
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}

			KategorijaPutovanjaEnum kategorijaEnum = KategorijaPutovanjaEnum.fromDisplayName(kategorija);

			Putovanje novoPutovanje = new Putovanje(destinacija, prevoznoSredstvo, smestajnaJedinice, kategorijaEnum,
					intervali, cena);

			putovanjeRepository.savePutovanje(novoPutovanje);

			return ResponseEntity.ok("Putovanje uspešno dodato!");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Greška prilikom parsiranja JSON-a.");
		}

	}

	@PostMapping("/putovanja/reservation/addToShoppingCart")
	public ResponseEntity<String> dodajRezervacijuUWishlist(@RequestBody String reservationRequest, HttpSession session,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			String decodedReservationRequest = URLDecoder.decode(reservationRequest, StandardCharsets.UTF_8.toString());

			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(decodedReservationRequest);

			int brojPutnika = jsonNode.get("brojPutnika").asInt();
			int brojNocenja = jsonNode.get("brojNocenja").asInt();
			long smestajId = jsonNode.get("smestajId").asLong();

			Putovanje putovanje = putovanjeRepository.findOne(jsonNode.get("putovanjeId").asLong());
			Interval termin = putovanjeRepository.findOneTermin(jsonNode.get("intervalId").asLong());
			System.out.println(smestajId);
			SmestajnaJedinica smestajnaJedinica = smestajnaJedinicaRepository.findOne(smestajId);
			Double cena = putovanje.getCenaAranzmana() * brojNocenja * brojPutnika;
			Rezervacija rezervacija = new Rezervacija(putovanje, brojPutnika, termin, smestajnaJedinica, cena);

			session = request.getSession(true);
			List<Rezervacija> shoppingCart = (List<Rezervacija>) session.getAttribute("shoppingCart");

			if (shoppingCart == null) {
				shoppingCart = new ArrayList<>();
				session.setAttribute("shoppingCart", shoppingCart);
			}

			long rezervacijaId = shoppingCart.size() + 1;
			rezervacija.setId(rezervacijaId);
			shoppingCart.add(rezervacija);

			return ResponseEntity.ok("Putovanje uspešno dodato!");

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Greška prilikom parsiranja JSON-a.");
		}

	}

	@PostMapping("/putovanja/reservation/changePassangerNumberWishlist")
	public ResponseEntity<String> promeniBrojPutnikaWishlist(@RequestParam Long rezervacijaId,
			@RequestParam Integer newPassengerCount, HttpSession session, HttpServletRequest request,
			HttpServletResponse response) {
		try {

			Rezervacija rezervacija = null;

			session = request.getSession(true);
			List<Rezervacija> shoppingCart = (List<Rezervacija>) session.getAttribute("shoppingCart");
			for (Rezervacija rezervacijaToChange : shoppingCart) {
				if (rezervacijaToChange.getId() == rezervacijaId) {
					rezervacija = rezervacijaToChange;
				}
			}
			Double cena = rezervacija.getPutovanje().getCenaAranzmana() * rezervacija.getTermin().getBrojNocenja()
					* newPassengerCount;
			rezervacija.setCena(cena);
			rezervacija.setBrojPutnika(newPassengerCount);
			if (shoppingCart == null) {
				shoppingCart = new ArrayList<>();
			}
			session.setAttribute("shoppingCart", shoppingCart);

			return ResponseEntity.ok("Putovanje uspešno dodato!");

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Greška prilikom parsiranja JSON-a.");
		}

	}

	@PostMapping("/putovanja/reservation/removeFromShoppingCart")
	public ResponseEntity<String> obrisiRezervacijuWishlist(@RequestParam Long rezervacijaId, HttpSession session,
			HttpServletRequest request, HttpServletResponse response) {
		try {

			Rezervacija rezervacija = null;

			session = request.getSession(true);
			List<Rezervacija> shoppingCart = (List<Rezervacija>) session.getAttribute("shoppingCart");

			shoppingCart.removeIf(rezervacijaa -> rezervacijaa.getId().equals(rezervacijaId));

			session.setAttribute("shoppingCart", shoppingCart);

			return ResponseEntity.ok("Putovanje uspešno dodato!");

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Greška prilikom parsiranja JSON-a.");
		}

	}

	@GetMapping("/putovanja/detalji/{id}")
	public ModelAndView prikaziPutovanje(@PathVariable Long id) {
		ModelAndView rezultat = new ModelAndView("fragments/putovanje");
		Putovanje putovanje = putovanjeRepository.findOne(id);
		List<Rezervacija> rezervacije = rezervacijaRepository.findByPutovanjeId(id);
		List<Interval> termini = putovanjeRepository.getTerminiByPutovanjeId(id);
		rezultat.addObject("putovanje", putovanje);
		rezultat.addObject("rezervacije", rezervacije);
		rezultat.addObject("termini", termini);
		return rezultat;

	}

	@GetMapping("/putovanja/getPrevoznoSredstvoOptions")
	@ResponseBody
	public String getPrevoznoSredstvoOptions(@RequestParam Long destinacijaId) {
		// Implementirajte logiku za dohvat podataka na osnovu izabrane destinacije
		List<PrevoznoSredstvo> prevoznoSredstva = prevoznoSredstvoRepository.findByDestinacijeId(destinacijaId);
		try {
			String json = objectMapper.writeValueAsString(prevoznoSredstva);
			return json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "Greška prilikom pretvaranja u JSON.";
		}

	}

	@GetMapping("/putovanja/getSmestajOptions")
	@ResponseBody
	public String getSmestajOptions(@RequestParam Long destinacijaId) {
		// Implementirajte logiku za dohvat podataka na osnovu izabrane destinacije
		List<SmestajnaJedinica> smestajnaJedinice = smestajnaJedinicaRepository.findByDestinacijeId(destinacijaId);
		try {
			String json = objectMapper.writeValueAsString(smestajnaJedinice);
			return json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "Greška prilikom pretvaranja u JSON.";
		}

	}

	@PostMapping("putovanja/filter")
	public String getPutovanjaByFilter(@RequestBody String filterJson, Model model) {

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(filterJson);

			Double cenaDo = jsonNode.get("cenaDo").asDouble();
			Double cenaOd = jsonNode.get("cenaOd").asDouble();
			LocalDate datumPolaska = jsonNode.get("datumPolaska").asText() == "" ? null
					: LocalDate.parse(jsonNode.get("datumPolaska").asText());

			LocalDate datumPovratka = jsonNode.get("datumPovratka").asText() == "" ? null
					: LocalDate.parse(jsonNode.get("datumPovratka").asText());

			String nazivDestinacije = jsonNode.get("nazivDestinacije") != null
					? jsonNode.get("nazivDestinacije").asText()
					: null;
			String sifraPutovanja = jsonNode.get("sifraPutovanja") != null ? jsonNode.get("sifraPutovanja").asText()
					: null;
			String selectedPrevozi = jsonNode.get("selectedPrevozi").asText();
			String selectedSmestaji = jsonNode.get("selectedSmestaji").asText();
			String selectedKategorije = jsonNode.get("selectedkategorije").asText();
			int brNocenja = jsonNode.get("brNocenja").asInt();
			int brOsoba = jsonNode.get("brOsoba").asInt();
			List<SmestajnaJedinicaTipEnum> smestajiEnum = objectMapper.readValue(selectedSmestaji,
					new TypeReference<List<SmestajnaJedinicaTipEnum>>() {
					});

			List<PrevoznoSredstvoTipEnum> prevoziEnum = objectMapper.readValue(selectedPrevozi,
					new TypeReference<List<PrevoznoSredstvoTipEnum>>() {
					});

			List<KategorijaPutovanjaEnum> kategorijeEnum = objectMapper.readValue(selectedKategorije,
					new TypeReference<List<KategorijaPutovanjaEnum>>() {
					});
			if (cenaDo == 0) {
				cenaDo = null;
			}
			if (cenaOd == 0) {
				cenaOd = null;
			}

			if (smestajiEnum.isEmpty()) {
				smestajiEnum = null;
			}
			if (prevoziEnum.isEmpty()) {
				prevoziEnum = null;
			}
			if (kategorijeEnum.isEmpty()) {
				kategorijeEnum = null;
			}

			if (sifraPutovanja == "null") {
				sifraPutovanja = null;
			}

			if (nazivDestinacije == "null") {
				nazivDestinacije = null;
			}

			System.out.println(cenaDo);
			System.out.println(cenaOd);
			System.out.println(datumPolaska);
			System.out.println(datumPovratka);
			System.out.println(nazivDestinacije);
			System.out.println(smestajiEnum);
			System.out.println(prevoziEnum);
			System.out.println(kategorijeEnum);
			System.out.println(brOsoba);
			System.out.println(brNocenja);
			List<Putovanje> filteredPutovanja = putovanjeRepository.filterBy(cenaDo, cenaOd, datumPolaska,
					datumPovratka, nazivDestinacije, sifraPutovanja, smestajiEnum, prevoziEnum, kategorijeEnum, brOsoba,
					brNocenja);

			model.addAttribute("listaPutovanja", filteredPutovanja);
			return "fragments/putovanja :: listaPutovanja";
		} catch (Exception e) {

			e.printStackTrace();
			List<Putovanje> putovanja = putovanjeRepository.findAll();
			model.addAttribute("listaPutovanja", putovanja);
			return "fragments/putovanja :: listaPutovanja";

		}

	}

}
