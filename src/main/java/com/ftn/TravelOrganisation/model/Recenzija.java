package com.ftn.TravelOrganisation.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

//@Entity
public class Recenzija {
	//@Id
	private Long id;
	private int ocena;
	private String komentar;
	private LocalDate datumRecenzije;
	// @ManyToOne
	// @JoinColumn(name = "korisnik_id")
	private Korisnik autorRecenzije;

	public Recenzija(Long id, int ocena, String komentar, LocalDate datumRecenzije, Korisnik autorRecenzije) {
		super();
		this.id = id;
		this.ocena = ocena;
		this.komentar = komentar;
		this.datumRecenzije = datumRecenzije;
		this.autorRecenzije = autorRecenzije;
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

}
