package com.ftn.TravelOrganisation.model;

import java.time.LocalDateTime;

public class Rezervacija {

	
	private Long id;
	private Korisnik korisnik;
	private Putovanje putovanje;
	private LocalDateTime vremeRezervacije;
	private int brojPutnika;
	private RezervacijaStatus statusRezervacije;
	private Interval termin;
	private Double cena;
	private SmestajnaJedinica smestajnaJedinica;
	
	public Rezervacija(Long id, Korisnik korisnik, Putovanje putovanje, int brojPutnika,  LocalDateTime vremeRezervacije, Interval termin, Double cena, SmestajnaJedinica smestajnaJedinica, RezervacijaStatus statusRezervacije) {
		super();
		this.id = id;
		this.korisnik = korisnik;
		this.putovanje = putovanje;
		this.brojPutnika = brojPutnika;
		this.vremeRezervacije = vremeRezervacije;
		this.termin = termin;
		this.cena = cena;
		this.statusRezervacije = statusRezervacije;
		this.smestajnaJedinica = smestajnaJedinica;
	}
	
	public Rezervacija(Putovanje putovanje, int brojPutnika, Interval termin, SmestajnaJedinica smestajnaJedinica, Double cena) {
		super();
		this.putovanje = putovanje;
		this.brojPutnika = brojPutnika;
		this.termin = termin;
		this.smestajnaJedinica = smestajnaJedinica;
		this.cena = cena;
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



	public LocalDateTime getVremeRezervacije() {
		return vremeRezervacije;
	}



	public void setVremeRezervacije(LocalDateTime vremeRezervacije) {
		this.vremeRezervacije = vremeRezervacije;
	}



	public RezervacijaStatus getStatusRezervacije() {
		return statusRezervacije;
	}



	public void setStatusRezervacije(RezervacijaStatus statusRezervacije) {
		this.statusRezervacije = statusRezervacije;
	}

	public Interval getTermin() {
		return termin;
	}

	public void setTermin(Interval termin) {
		this.termin = termin;
	}

	public Double getCena() {
		return cena;
	}

	public void setCena(Double cena) {
		this.cena = cena;
	}

	public SmestajnaJedinica getSmestajnaJedinica() {
		return smestajnaJedinica;
	}

	public void setSmestajnaJedinica(SmestajnaJedinica smestajnaJedinica) {
		this.smestajnaJedinica = smestajnaJedinica;
	}
	
	
	
}
