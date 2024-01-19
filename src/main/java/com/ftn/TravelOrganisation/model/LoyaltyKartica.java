package com.ftn.TravelOrganisation.model;

public class LoyaltyKartica {

	private Long id;
	private int brojPoena;
	private Korisnik korisnik;
	private boolean odobrena;

	public LoyaltyKartica(Long id, int brojPoena, Korisnik korisnik, boolean odobrena) {
		super();
		this.id = id;
		this.brojPoena = brojPoena;
		this.korisnik = korisnik;
		this.odobrena = odobrena;
	}

	public LoyaltyKartica(int brojPoena, Korisnik korisnik, boolean odobrena) {
		this.brojPoena = brojPoena;
		this.korisnik = korisnik;
		this.odobrena = odobrena;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getBrojPoena() {
		return brojPoena;
	}

	public void setBrojPoena(int brojPoena) {
		this.brojPoena = brojPoena;
	}

	public Korisnik getKorisnik() {
		return korisnik;
	}

	public void setKorisnik(Korisnik korisnik) {
		this.korisnik = korisnik;
	}

	public boolean isOdobrena() {
		return odobrena;
	}

	public void setOdobrena(boolean odobrena) {
		this.odobrena = odobrena;
	}

}
