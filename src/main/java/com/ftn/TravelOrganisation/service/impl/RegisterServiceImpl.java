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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ftn.TravelOrganisation.model.Destinacija;
import com.ftn.TravelOrganisation.model.Korisnik;
import com.ftn.TravelOrganisation.model.KorisnikUloga;
import com.ftn.TravelOrganisation.service.RegisterService;

@Service
public class RegisterServiceImpl implements RegisterService {

	@Value("${korisnici.pathToFile}")
	private String pathToFile;

	private Map<Long, Korisnik> readKorisniciFromTxt() {

		Map<Long, Korisnik> korisnici = new HashMap<>();
		Long nextId = 1L;

		try {
			Path path = Paths.get(pathToFile);
			System.out.println(path.toFile().getAbsolutePath());
			List<String> lines = Files.readAllLines(path, Charset.forName("UTF-8"));

			for (String line : lines) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;

				String[] tokens = line.split(";");
				Long id = Long.parseLong(tokens[0]);
				String korisnickoIme = tokens[1];
				String sifra = tokens[2];
				String email = tokens[3];
				String ime = tokens[4];
				String prezime = tokens[5];
				String adresa = tokens[6];
				String brojTelefona = tokens[7];
				String datumRodjenja = tokens[8];
				String datumVremeRegistracije = tokens[9];
				String uloga = tokens[10];


				korisnici.put(id, new Korisnik(id, korisnickoIme, sifra, email, ime, prezime, adresa, brojTelefona,
						datumRodjenja, datumVremeRegistracije, uloga));

				if (nextId < id)
					nextId = id;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return korisnici;
	}

	private Map<Long, Korisnik> saveToFile(Map<Long, Korisnik> korisnici) {
		System.out.println("cuva u fajl");
		Map<Long, Korisnik> ret = new HashMap<>();
		try {

			Path path = Paths.get(pathToFile);
			System.out.println(path.toFile().getAbsolutePath());
			List<String> lines = new ArrayList<>();

			for (Korisnik korisnik : korisnici.values()) {
				
		        System.out.println(korisnik.getId());

				lines.add(korisnik.toString());
				ret.put(korisnik.getId(), korisnik);
			}
			Files.write(path, lines, Charset.forName("UTF-8"));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	@Override
	public List<Korisnik> findAll() {
		Map<Long, Korisnik> korisnici = readKorisniciFromTxt();
		return new ArrayList<>(korisnici.values());
	}

	@Override
	public Korisnik returnById(Long id) {
		Map<Long, Korisnik> korisnici = readKorisniciFromTxt();

		return korisnici.get(id);
	}

	public boolean alreadyRegistered(String korisnickoIme) {
		List<Korisnik> korisnici = findAll();
		for (Korisnik korisnik : korisnici) {
			if (korisnik.getKorisnickoIme().equals(korisnickoIme)) {
				return true;
			}
		}
		return false;
	}

	private Korisnik save(Korisnik korisnik) {
	    Map<Long, Korisnik> korisnici = readKorisniciFromTxt();
	    Long nextId = nextId(korisnici);

	    if (korisnik.getId() == null) {
	        korisnik.setId(nextId);
	        korisnik.setDatumVremeRegistracije(LocalDateTime.now());
	        korisnik.setUloga(KorisnikUloga.PUTNIK);
	        nextId++; // Povećajte vrednost za sledeći put
	    }

	    korisnici.put(korisnik.getId(), korisnik);
	    saveToFile(korisnici);

	    return korisnik;
	}


	@Override
	public void register(Korisnik korisnik) {

			save(korisnik);
		
	}


	
	private Long nextId(Map<Long, Korisnik> korisnici) {
	    Long nextId = 1L;

	    while (korisnici.containsKey(nextId)) {
	        nextId++;
	    }

	    return nextId;
	}


}
