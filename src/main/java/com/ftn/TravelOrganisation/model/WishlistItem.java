package com.ftn.TravelOrganisation.model;

import java.util.List;

public class WishlistItem {

	private Long id;

	private Korisnik korisnik;

	private Putovanje putovanje;

	public WishlistItem(Long id, Korisnik korisnik, Putovanje putovanje) {
		super();
		this.id = id;
		this.korisnik = korisnik;
		this.putovanje = putovanje;
	}
	
	public WishlistItem(Korisnik korisnik, Putovanje putovanje) {
		super();
		this.korisnik = korisnik;
		this.putovanje = putovanje;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Korisnik getKorisnik() {
		return korisnik;
	}

	public void setKorisnik(Korisnik korisnik) {
		this.korisnik = korisnik;
	}

	public Putovanje getPutovanje() {
		return putovanje;
	}

	public void setPutovanje(Putovanje putovanje) {
		this.putovanje = putovanje;
	}



}
