package com.ftn.TravelOrganisation.model;

import java.time.LocalDate;


public class Recenzija {
	private Long id;
	private int ocena;
	private String komentar;
	private LocalDate datumRecenzije;
	private SmestajnaJedinica smestajnaJedinica;
	private Korisnik autorRecenzije;

	public Recenzija(Long id, int ocena, String komentar, LocalDate datumRecenzije, Korisnik autorRecenzije, SmestajnaJedinica smestajnaJedinica) {
		super();
		this.id = id;
		this.ocena = ocena;
		this.komentar = komentar;
		this.datumRecenzije = datumRecenzije;
		this.autorRecenzije = autorRecenzije;
		this.smestajnaJedinica =smestajnaJedinica;
	}

	public Recenzija(Long id, int ocena, String komentar, String datumRecenzijeStr, Korisnik autorRecenzije, SmestajnaJedinica smestajnaJedinica) {
		this.id = id;
		this.ocena = ocena;
		this.komentar = komentar;
		this.datumRecenzije = datumRecenzije;
		this.autorRecenzije = autorRecenzije;
		this.smestajnaJedinica =smestajnaJedinica;	
		}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getOcena() {
		return ocena;
	}

	public void setOcena(int ocena) {
		this.ocena = ocena;
	}

	public String getKomentar() {
		return komentar;
	}

	public void setKomentar(String komentar) {
		this.komentar = komentar;
	}

	public LocalDate getDatumRecenzije() {
		return datumRecenzije;
	}

	public void setDatumRecenzije(LocalDate datumRecenzije) {
		this.datumRecenzije = datumRecenzije;
	}

	public Korisnik getAutorRecenzije() {
		return autorRecenzije;
	}

	public void setAutorRecenzije(Korisnik autorRecenzije) {
		this.autorRecenzije = autorRecenzije;
	}

	public SmestajnaJedinica getSmestajnaJedinica() {
		return smestajnaJedinica;
	}

	public void setSmestajnaJedinica(SmestajnaJedinica smestajnaJedinica) {
		this.smestajnaJedinica = smestajnaJedinica;
	}

}
