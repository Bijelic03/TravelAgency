package com.ftn.TravelOrganisation.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ftn.TravelOrganisation.model.Korisnik;
import com.ftn.TravelOrganisation.model.LoyaltyKartica;
import com.ftn.TravelOrganisation.model.Rezervacija;
import com.ftn.TravelOrganisation.model.RezervacijaStatus;
import com.ftn.TravelOrganisation.repository.LoyaltyKarticaRepository;
import com.ftn.TravelOrganisation.repository.PrevoznoSredstvoRepository;
import com.ftn.TravelOrganisation.repository.RezervacijaRepository;
import com.ftn.TravelOrganisation.repository.SmestajnaJedinicaRepository;
import com.ftn.TravelOrganisation.service.RezervacijeService;

@Service
public class RezervacijeServiceImpl implements RezervacijeService {

	private final SmestajnaJedinicaRepository smestajnaJedinicaRepository;

	private final PrevoznoSredstvoRepository prevoznoSredstvoRepository;

	private final RezervacijaRepository rezervacijaRepository;

	private final LoyaltyKarticaRepository loyaltyKarticaRepository;

	public RezervacijeServiceImpl(SmestajnaJedinicaRepository smestajnaJedinicaRepository,
			PrevoznoSredstvoRepository prevoznoSredstvoRepository, RezervacijaRepository rezervacijaRepository,
			LoyaltyKarticaRepository loyaltyKarticaRepository) {
		this.smestajnaJedinicaRepository = smestajnaJedinicaRepository;
		this.prevoznoSredstvoRepository = prevoznoSredstvoRepository;
		this.rezervacijaRepository = rezervacijaRepository;
		this.loyaltyKarticaRepository = loyaltyKarticaRepository;
	}

	@Override
	public ResponseEntity<String> handleReservations(List<Rezervacija> rezervacije, int brojBodova,
			Korisnik prijavljeniKorisnik) {
		double ukupnaCena = 0;
		for (Rezervacija rezervacija : rezervacije) {
			rezervacija.setKorisnik(prijavljeniKorisnik);
			rezervacija.setVremeRezervacije(LocalDateTime.now());
			rezervacija.setStatusRezervacije(RezervacijaStatus.KREIRANA);
			int noviKapacitetSmestaj = rezervacija.getSmestajnaJedinica().getKapacitet() - rezervacija.getBrojPutnika();
			int noviKapacitetPrevoz = rezervacija.getPutovanje().getPrevoznoSredstvo().getBrojSedista()
					- rezervacija.getBrojPutnika();
			if (brojBodova != 0) {
				double cena = rezervacija.getCena() - rezervacija.getCena() * 0.05 * brojBodova;
				rezervacija.setCena(cena);
			}
			ukupnaCena = ukupnaCena + rezervacija.getCena();

			//smestajnaJedinicaRepository.updateKapacitet(rezervacija.getSmestajnaJedinica(), noviKapacitetSmestaj);
			//prevoznoSredstvoRepository.updateBrojSedista(rezervacija.getPutovanje().getPrevoznoSredstvo(),
			//		noviKapacitetPrevoz);
		
			rezervacijaRepository.save(rezervacija);
		}
		
		LoyaltyKartica loyaltyKartica = loyaltyKarticaRepository.findByKorisnikId(prijavljeniKorisnik.getId());
		if(loyaltyKartica != null) {
			loyaltyKarticaRepository.updateBrojPoena(loyaltyKartica, loyaltyKartica.getBrojPoena() - brojBodova);
			int noviBrojPoena = loyaltyKartica.getBrojPoena() + (int) ukupnaCena / 10000;
			loyaltyKarticaRepository.updateBrojPoena(loyaltyKartica,  noviBrojPoena);
		}



		return ResponseEntity.ok("Uspesno rezervisano!");
	}
}
