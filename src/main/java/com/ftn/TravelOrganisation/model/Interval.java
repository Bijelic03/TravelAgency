package com.ftn.TravelOrganisation.model;

import java.time.LocalDateTime;

public class Interval {

	private Long id;
	private Long idPutovanja;
	private LocalDateTime vremePolaska;
	private LocalDateTime vremePovratka;

	public Interval(Long id, Long idPutovanja, LocalDateTime vremePolaska, LocalDateTime vremePovratka) {
		super();
		this.id = id;
		this.setIdPutovanja(idPutovanja);
		this.vremePolaska = vremePolaska;
		this.vremePovratka = vremePovratka;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getVremePolaska() {
		return vremePolaska;
	}

	public void setVremePolaska(LocalDateTime vremePolaska) {
		this.vremePolaska = vremePolaska;
	}

	public LocalDateTime getVremePovratka() {
		return vremePovratka;
	}

	public void setVremePovratka(LocalDateTime vremePovratka) {
		this.vremePovratka = vremePovratka;
	}

	public Long getIdPutovanja() {
		return idPutovanja;
	}

	public void setIdPutovanja(Long idPutovanja) {
		this.idPutovanja = idPutovanja;
	}

}
