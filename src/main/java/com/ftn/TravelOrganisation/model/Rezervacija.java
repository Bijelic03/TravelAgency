package com.ftn.TravelOrganisation.model;

public class Rezervacija {

	
	private Long id;
	private Korisnik korisnik;
	private Putovanje putovanje;
	private int brojPutnika;
	
	public Rezervacija(Long id, Korisnik korisnik, Putovanje putovanje, int brojPutnika) {
		super();
		this.id = id;
		this.korisnik = korisnik;
		this.putovanje = putovanje;
		this.brojPutnika = brojPutnika;
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

	public int getBrojPutnika() {
		return brojPutnika;
	}

	public void setBrojPutnika(int brojPutnika) {
		this.brojPutnika = brojPutnika;
	}
	
	
	
}
