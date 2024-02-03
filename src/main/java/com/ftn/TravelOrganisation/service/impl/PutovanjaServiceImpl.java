package com.ftn.TravelOrganisation.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ftn.TravelOrganisation.model.Interval;
import com.ftn.TravelOrganisation.model.Putovanje;
import com.ftn.TravelOrganisation.model.Rezervacija;
import com.ftn.TravelOrganisation.model.SmestajnaJedinica;
import com.ftn.TravelOrganisation.repository.PutovanjeRepository;
import com.ftn.TravelOrganisation.repository.RezervacijaRepository;
import com.ftn.TravelOrganisation.repository.SmestajnaJedinicaRepository;
import com.ftn.TravelOrganisation.service.PutovanjaService;

@Service
public class PutovanjaServiceImpl implements PutovanjaService {

	private final RezervacijaRepository rezervacijaRepository;

	private final PutovanjeRepository putovanjeRepository;

	private final SmestajnaJedinicaRepository smestajnaJedinicaRepository;

	public PutovanjaServiceImpl(RezervacijaRepository rezervacijaRepository, PutovanjeRepository putovanjeRepository,
			SmestajnaJedinicaRepository smestajnaJedinicaRepository) {
		this.rezervacijaRepository = rezervacijaRepository;
		this.putovanjeRepository = putovanjeRepository;
		this.smestajnaJedinicaRepository = smestajnaJedinicaRepository;
	}

	@Override
	public int getPreostaloSlobodnihMesta(Long putovanjeId, Long intervalId, Long smestajnaJedinicaId) {
		int brojZauzetihMesta = getBrojZauzetihMesta(putovanjeId, intervalId, smestajnaJedinicaId);
		int brojMesta = getKapacitet(putovanjeId, smestajnaJedinicaId);

		return brojMesta - brojZauzetihMesta;
	}

	@Override
	public int getKapacitet(Long putovanjeId, Long smestajnaJedinicaId) {
		Putovanje putovanje = putovanjeRepository.findOne(putovanjeId);
		SmestajnaJedinica smestaj = smestajnaJedinicaRepository.findOne(smestajnaJedinicaId);
		int brojMesta = putovanje.getPrevoznoSredstvo().getBrojSedista() > smestaj.getKapacitet()
				? putovanje.getPrevoznoSredstvo().getBrojSedista()
				: smestaj.getKapacitet();

		return brojMesta;
	}

	@Override
	public int getBrojZauzetihMesta(Long putovanjeId, Long intervalId, Long smestajnaJedinicaId) {
		List<Rezervacija> rezervacije = rezervacijaRepository.findByPutovanjeId(putovanjeId);
		int brojZauzetihMesta = 0;
		for (Rezervacija rezervacija : rezervacije) {
			if (rezervacija.getSmestajnaJedinica().getId() == smestajnaJedinicaId
					&& rezervacija.getTermin().getId() == intervalId) {
				brojZauzetihMesta += rezervacija.getBrojPutnika();
			}
		}
		return brojZauzetihMesta;
	}

	@Override
	public int getBrojZauzetihMesta(Long putovanjeId) {
		List<Rezervacija> rezervacije = rezervacijaRepository.findByPutovanjeId(putovanjeId);
		int brojZauzetihMesta = 0;
		for (Rezervacija rezervacija : rezervacije) {
			brojZauzetihMesta += rezervacija.getBrojPutnika();
		}

		return brojZauzetihMesta;
	}

	@Override
	public List<Putovanje> getNepopunjenaPutovanja(List<Putovanje> putovanja) {
		List<Putovanje> filteredPutovanja = new ArrayList<Putovanje>();
		for (Putovanje putovanje : putovanja) {
			int brojZauzetihMesta = getBrojZauzetihMesta(putovanje.getId());

			if (putovanje.getPrevoznoSredstvo().getBrojSedista()
					* putovanje.getListaTermina().size() > brojZauzetihMesta) {
				filteredPutovanja.add(putovanje);
			}
		}
		return filteredPutovanja;
	}

}
