package com.ftn.TravelOrganisation.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ftn.TravelOrganisation.model.Interval;
import com.ftn.TravelOrganisation.model.Putovanje;
import com.ftn.TravelOrganisation.model.Rezervacija;
import com.ftn.TravelOrganisation.model.SmestajnaJedinica;

public interface PutovanjaService {

	public int getKapacitet(Long putovanjeId, Long smestajnaJedinicaId);

	public int getBrojZauzetihMesta(Long putovanjeId, Long intervalId, Long smestajnaJedinicaId);

	public int getPreostaloSlobodnihMesta(Long putovanjeId, Long intervalId, Long smestajnaJedinicaId);

	public int getBrojZauzetihMesta(Long putovanjeId);

	public List<Putovanje> getNepopunjenaPutovanja(List<Putovanje> putovanja);

}
