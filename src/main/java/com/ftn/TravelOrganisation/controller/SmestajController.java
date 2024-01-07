package com.ftn.TravelOrganisation.controller;

import java.util.ArrayList;
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
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftn.TravelOrganisation.model.Destinacija;
import com.ftn.TravelOrganisation.model.Interval;
import com.ftn.TravelOrganisation.model.PrevoznoSredstvo;
import com.ftn.TravelOrganisation.model.SmestajnaJedinica;
import com.ftn.TravelOrganisation.model.SmestajnaJedinicaTipEnum;
import com.ftn.TravelOrganisation.model.SmestajnaJedinicaUslugaEnum;
import com.ftn.TravelOrganisation.repository.DestinacijaRepository;
import com.ftn.TravelOrganisation.repository.PutovanjeRepository;
import com.ftn.TravelOrganisation.repository.SmestajnaJedinicaRepository;

@Controller
@RequestMapping("smestaj")
public class SmestajController {

	DestinacijaRepository destinacijaRepository;
	SmestajnaJedinicaRepository smestajnaJedinicaRepository;

	@Autowired
	public SmestajController(DestinacijaRepository destinacijaRepository, SmestajnaJedinicaRepository smestajnaJedinicaRepository) {
		this.smestajnaJedinicaRepository = smestajnaJedinicaRepository;
		this.destinacijaRepository = destinacijaRepository;
	}

	@GetMapping("/dodaj")
	public ModelAndView dodajSmestaj() {
		ModelAndView rezultat = new ModelAndView("fragments/addSmestaj");

		List<Destinacija> destinacije = destinacijaRepository.findAll();
		rezultat.addObject("destinacije", destinacije);
		return rezultat;

	}

	@PostMapping("/add")
	public ResponseEntity<String> dodajSmestaj(@RequestBody String smestajRequest, HttpServletResponse response) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(smestajRequest);

			String naziv = jsonNode.get("naziv").asText();
			String tipSmestajaStr = jsonNode.get("tipSmestaja").asText();
			Long destinacijaId = jsonNode.get("chosenDestinacijaId").asLong();
			String selectedUslugeJson = jsonNode.get("selectedUsluge").asText();
			String opis = jsonNode.get("opis").asText();
			int kapacitet = jsonNode.get("kapacitet").asInt();

			Destinacija destinacija = destinacijaRepository.findOne(destinacijaId);
			SmestajnaJedinicaTipEnum tipSmestaja = SmestajnaJedinicaTipEnum.valueOf(tipSmestajaStr);

			List<String> uslugeStr;
			try {
				uslugeStr = objectMapper.readValue(selectedUslugeJson, new TypeReference<List<String>>() {
				});
				List<SmestajnaJedinicaUslugaEnum> usluge = uslugeStr.stream()
						.map(usluga -> SmestajnaJedinicaUslugaEnum.valueOf(usluga)).collect(Collectors.toList());

				SmestajnaJedinica smestajnaJedinica = new SmestajnaJedinica(naziv, kapacitet, destinacija, usluge, opis,
						tipSmestaja);
				
				System.out.println(usluge.toString());
				smestajnaJedinicaRepository.save(smestajnaJedinica);

			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Greška prilikom parsiranja JSON-a.");
		}

		return ResponseEntity.ok("Putovanje uspešno dodato!");

	}

}
