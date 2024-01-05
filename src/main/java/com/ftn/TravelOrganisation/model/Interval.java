package com.ftn.TravelOrganisation.model;

import java.time.LocalDate;

public class Interval {

	private Long id;
	private Long idPutovanja;
	private LocalDate vremePolaska;
	private LocalDate vremePovratka;
	private int brojNocenja;

	public Interval(Long id, Long idPutovanja, LocalDate vremePolaska, LocalDate vremePovratka, int brojNocenja) {
		super();
		this.id = id;
		this.idPutovanja =idPutovanja;
		this.vremePolaska = vremePolaska;
		this.vremePovratka = vremePovratka;
		this.brojNocenja = brojNocenja;
	}

	public Interval(LocalDate datumPolaska, LocalDate datumPovratka, int brojNocenja) {
		this.vremePolaska = datumPolaska;
		this.vremePovratka = datumPovratka;
		this.brojNocenja = brojNocenja;

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getVremePolaska() {
		return vremePolaska;
	}

	public void setVremePolaska(LocalDate vremePolaska) {
		this.vremePolaska = vremePolaska;
	}

	public LocalDate getVremePovratka() {
		return vremePovratka;
	}

	public void setVremePovratka(LocalDate vremePovratka) {
		this.vremePovratka = vremePovratka;
	}

	public Long getIdPutovanja() {
		return idPutovanja;
	}

	public void setIdPutovanja(Long idPutovanja) {
		this.idPutovanja = idPutovanja;
	}

	public int getBrojNocenja() {
		return brojNocenja;
	}

	public void setBrojNocenja(int brojNocenja) {
		this.brojNocenja = brojNocenja;
	}

}
