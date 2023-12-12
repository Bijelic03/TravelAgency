package com.ftn.TravelOrganisation.model;

import java.time.LocalDateTime;

public class Putovanje {

	private Long id;
	private Long sifraPutovanja;
	private Destinacija destinacija;
	private PrevoznoSredstvo prevoznoSredstvo;
	private SmestajnaJedinica smestajnaJedinica;
	private KategorijaPutovanjaEnum kategorijaPutovanja;
	private LocalDateTime vremePolaska;
	private LocalDateTime vremePovratka;
	private int brojNocenja;
	private Double cenaAranzmana;
	
	
	public Putovanje(Long id, Long sifraPutovanja, Destinacija destinacija, PrevoznoSredstvo prevoznoSredstvo,
			SmestajnaJedinica smestajnaJedinica, KategorijaPutovanjaEnum kategorijaPutovanja,
			LocalDateTime vremePolaska, LocalDateTime vremePovratka, int brojNocenja, Double cenaAranzmana) {
		super();
		this.id = id;
		this.sifraPutovanja = sifraPutovanja;
		this.destinacija = destinacija;
		this.prevoznoSredstvo = prevoznoSredstvo;
		this.smestajnaJedinica = smestajnaJedinica;
		this.kategorijaPutovanja = kategorijaPutovanja;
		this.vremePolaska = vremePolaska;
		this.vremePovratka = vremePovratka;
		this.brojNocenja = brojNocenja;
		this.cenaAranzmana = cenaAranzmana;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Long getSifraPutovanja() {
		return sifraPutovanja;
	}


	public void setSifraPutovanja(Long sifraPutovanja) {
		this.sifraPutovanja = sifraPutovanja;
	}


	public Destinacija getDestinacija() {
		return destinacija;
	}


	public void setDestinacija(Destinacija destinacija) {
		this.destinacija = destinacija;
	}


	public PrevoznoSredstvo getPrevoznoSredstvo() {
		return prevoznoSredstvo;
	}


	public void setPrevoznoSredstvo(PrevoznoSredstvo prevoznoSredstvo) {
		this.prevoznoSredstvo = prevoznoSredstvo;
	}


	public SmestajnaJedinica getSmestajnaJedinica() {
		return smestajnaJedinica;
	}


	public void setSmestajnaJedinica(SmestajnaJedinica smestajnaJedinica) {
		this.smestajnaJedinica = smestajnaJedinica;
	}


	public KategorijaPutovanjaEnum getKategorijaPutovanja() {
		return kategorijaPutovanja;
	}


	public void setKategorijaPutovanja(KategorijaPutovanjaEnum kategorijaPutovanja) {
		this.kategorijaPutovanja = kategorijaPutovanja;
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


	public int getBrojNocenja() {
		return brojNocenja;
	}


	public void setBrojNocenja(int brojNocenja) {
		this.brojNocenja = brojNocenja;
	}


	public Double getCenaAranzmana() {
		return cenaAranzmana;
	}


	public void setCenaAranzmana(Double cenaAranzmana) {
		this.cenaAranzmana = cenaAranzmana;
	}
	
	
	
}
