package com.ftn.TravelOrganisation.controller;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
import com.ftn.TravelOrganisation.model.PrevoznoSredstvo;
import com.ftn.TravelOrganisation.model.PrevoznoSredstvoTipEnum;
import com.ftn.TravelOrganisation.model.Putovanje;
import com.ftn.TravelOrganisation.model.SmestajnaJedinica;
import com.ftn.TravelOrganisation.model.SmestajnaJedinicaTipEnum;
import com.ftn.TravelOrganisation.repository.DestinacijaRepository;
import com.ftn.TravelOrganisation.repository.PrevoznoSredstvoRepository;
import com.ftn.TravelOrganisation.repository.PutovanjeRepository;
import com.ftn.TravelOrganisation.repository.SmestajnaJedinicaRepository;
import com.ftn.TravelOrganisation.service.DestinacijeService;
import com.ftn.TravelOrganisation.service.RegisterService;

@Controller
@RequestMapping("")
public class PutovanjaController {

	private final String bURL;

	private final ObjectMapper objectMapper;

	private final PrevoznoSredstvoRepository prevoznoSredstvoRepository;

	private final PutovanjeRepository putovanjeRepository;

	private DestinacijaRepository destinacijaRepository;

	private final SmestajnaJedinicaRepository smestajnaJedinicaRepository;

	private final ServletContext servletContext;

	@Autowired
	public PutovanjaController(ServletContext servletContext, ObjectMapper objectMapper,
			PrevoznoSredstvoRepository prevoznoSredstvoRepository, PutovanjeRepository putovanjeRepository,
			DestinacijaRepository destinacijaRepository, SmestajnaJedinicaRepository smestajnaJedinicaRepository) {
		this.servletContext = servletContext;
		this.prevoznoSredstvoRepository = prevoznoSredstvoRepository;
		this.objectMapper = objectMapper;
		this.putovanjeRepository = putovanjeRepository;
		this.destinacijaRepository = destinacijaRepository;
		this.smestajnaJedinicaRepository = smestajnaJedinicaRepository;
		this.bURL = servletContext.getContextPath();
	}

	@GetMapping("/putovanja")
	public ModelAndView prikaziPutovanja() {
		ModelAndView rezultat = new ModelAndView("putovanjaPage");
		List<Putovanje> putovanja = putovanjeRepository.findAll();
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

	@GetMapping("/putovanja/detalji/{id}")
	public ModelAndView prikaziPutovanje(@PathVariable Long id) {
		ModelAndView rezultat = new ModelAndView("fragments/putovanje");
		Putovanje putovanje = putovanjeRepository.findOne(id);
		rezultat.addObject("putovanje", putovanje);
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
			System.out.println("ovde");
			List<Putovanje> putovanja = putovanjeRepository.findAll();
			model.addAttribute("listaPutovanja", putovanja);
			return "fragments/putovanja :: listaPutovanja";

		}

	}

}
