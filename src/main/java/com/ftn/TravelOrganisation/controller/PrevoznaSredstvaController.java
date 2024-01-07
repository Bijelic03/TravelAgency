package com.ftn.TravelOrganisation.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftn.TravelOrganisation.model.Destinacija;
import com.ftn.TravelOrganisation.model.PrevoznoSredstvo;
import com.ftn.TravelOrganisation.model.PrevoznoSredstvoTipEnum;
import com.ftn.TravelOrganisation.model.SmestajnaJedinica;
import com.ftn.TravelOrganisation.model.SmestajnaJedinicaTipEnum;
import com.ftn.TravelOrganisation.model.SmestajnaJedinicaUslugaEnum;
import com.ftn.TravelOrganisation.repository.DestinacijaRepository;
import com.ftn.TravelOrganisation.repository.PrevoznoSredstvoRepository;

@Controller
@RequestMapping("prevoz")
public class PrevoznaSredstvaController {

	DestinacijaRepository destinacijaRepository;
	PrevoznoSredstvoRepository prevoznoSredstvoRepository;

	@Autowired
	public PrevoznaSredstvaController(DestinacijaRepository destinacijaRepository,
			PrevoznoSredstvoRepository prevoznoSredstvoRepository) {

		this.prevoznoSredstvoRepository = prevoznoSredstvoRepository;
		this.destinacijaRepository = destinacijaRepository;
	}

	@GetMapping("/dodaj")
	public ModelAndView dodajPrevoz() {
		ModelAndView rezultat = new ModelAndView("fragments/addPrevoz");

		List<Destinacija> destinacije = destinacijaRepository.findAll();
		rezultat.addObject("destinacije", destinacije);
		return rezultat;
	}

	@PostMapping("/add")
	public ResponseEntity<String> addPrevoz(@RequestBody String prevozRequest, HttpServletResponse response) {
		try {
			System.out.println("ovde");

			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(prevozRequest);

			String tip = jsonNode.get("tip").asText();
			Long destinacijaId = jsonNode.get("chosenDestinacijaId").asLong();
			String opis = jsonNode.get("opis").asText();
			int brojSedista = jsonNode.get("brojSedista").asInt();

			Destinacija destinacija = destinacijaRepository.findOne(destinacijaId);
			PrevoznoSredstvoTipEnum prevoznoSredstvoTipEnum = PrevoznoSredstvoTipEnum.valueOf(tip);

			PrevoznoSredstvo prevoznoSredstvo = new PrevoznoSredstvo(brojSedista, destinacija, opis,
					prevoznoSredstvoTipEnum);

			prevoznoSredstvoRepository.save(prevoznoSredstvo);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Greška prilikom parsiranja JSON-a.");
		}

		return ResponseEntity.ok("Putovanje uspešno dodato!");

	}

}
