package com.ftn.TravelOrganisation.service.impl;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ftn.TravelOrganisation.model.Destinacija;
import com.ftn.TravelOrganisation.model.Korisnik;
import com.ftn.TravelOrganisation.model.KorisnikUloga;
import com.ftn.TravelOrganisation.repository.KorisnikRepository;
import com.ftn.TravelOrganisation.service.RegisterService;

@Service
public class RegisterServiceImpl implements RegisterService {

	@Autowired
	private KorisnikRepository korisnikRepository;

	public boolean alreadyRegistered(String korisnickoIme) {
		List<Korisnik> korisnici = korisnikRepository.findAll();
		for (Korisnik korisnik : korisnici) {
			if (korisnik.getKorisnickoIme().equals(korisnickoIme)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void register(Korisnik korisnik) {
		korisnik.setDatumVremeRegistracije(LocalDateTime.now());
		korisnik.setUloga(KorisnikUloga.PUTNIK);
		korisnik.setBlokiran(false);
		korisnikRepository.save(korisnik);
	}

	@Override
	public void login(String korisnickoIme, String sifra) {
		Korisnik prijavljeniKorisnik = korisnikRepository.findByKorisnickoImeAndSifra(korisnickoIme, sifra);
		if (prijavljeniKorisnik != null) {
			System.out.println("korisnik se prijavio");
		} else {
			System.out.println("korisnik se nije prijavio");
		}
	}

}
