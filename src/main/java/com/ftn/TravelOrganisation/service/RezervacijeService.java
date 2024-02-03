package com.ftn.TravelOrganisation.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ftn.TravelOrganisation.model.Korisnik;
import com.ftn.TravelOrganisation.model.Rezervacija;

public interface RezervacijeService {

	public ResponseEntity<String> handleReservations(List<Rezervacija> rezervacije, int brojBodova,
			Korisnik prijavljeniKorisnik);

}