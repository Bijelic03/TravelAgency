package com.ftn.TravelOrganisation.repository;

import java.util.List;

import com.ftn.TravelOrganisation.model.Rezervacija;
import com.ftn.TravelOrganisation.model.RezervacijaStatus;

public interface RezervacijaRepository {

	public int save(Rezervacija rezervacija);
	public Rezervacija findOne(Long id);
	public List<Rezervacija> findAll();
	public List<Rezervacija> findByPutovanjeId(Long putovanjeId);
	public List<Rezervacija> findByKorisnikId(Long korisnikId);
	public boolean updateStatus(Long idRezervacije, RezervacijaStatus noviStatus);
}
