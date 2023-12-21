package com.ftn.TravelOrganisation.service;

import java.util.List;
import org.springframework.stereotype.Component;

import com.ftn.TravelOrganisation.model.Korisnik;

public interface RegisterService {
	public void register(Korisnik korisnik);
	public boolean alreadyRegistered(String korisnickoIme);
	public Korisnik login(String korisnickoIme, String sifra);
}
