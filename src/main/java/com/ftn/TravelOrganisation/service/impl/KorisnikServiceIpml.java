package com.ftn.TravelOrganisation.service.impl;

import org.springframework.stereotype.Service;

import com.ftn.TravelOrganisation.model.Korisnik;
import com.ftn.TravelOrganisation.repository.KorisnikRepository;
import com.ftn.TravelOrganisation.service.KorisnikService;

@Service
public class KorisnikServiceIpml implements KorisnikService{
	
	KorisnikRepository korisnikRepository;
	
	public KorisnikServiceIpml(KorisnikRepository korisnikRepository) {
		this.korisnikRepository = korisnikRepository;
	}

	@Override
	public void handleBlock(Long id) {
		
		Korisnik korisnik = korisnikRepository.findOne(id);
		korisnikRepository.toggleBlock(korisnik);
		
		System.out.println(id);
	}

}
