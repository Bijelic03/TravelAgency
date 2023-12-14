package com.ftn.TravelOrganisation.model;

import java.time.LocalDateTime;
import java.util.List;



//@Entity
public class Putovanje {

	//@Id
	private Long id;
	private Long sifraPutovanja;
	//@OneToOne
	//@JoinColumn(name = "destinacija_id")
	private Destinacija destinacija;
	//@OneToOne
	//@JoinColumn(name = "prevozno_sredstvo_id")
	private PrevoznoSredstvo prevoznoSredstvo;
	
	private List<SmestajnaJedinica> smestajnaJedinice;
	private KategorijaPutovanjaEnum kategorijaPutovanja;
	private LocalDateTime vremePolaska;
	private LocalDateTime vremePovratka;
	private int brojNocenja;
	private Double cenaAranzmana;

	public Putovanje(Long id, Long sifraPutovanja, Destinacija destinacija, PrevoznoSredstvo prevoznoSredstvo,
			List<SmestajnaJedinica> smestajnaJedinice, KategorijaPutovanjaEnum kategorijaPutovanja,
			LocalDateTime vremePolaska, LocalDateTime vremePovratka, int brojNocenja, Double cenaAranzmana) {
		super();
		this.id = id;
		this.sifraPutovanja = sifraPutovanja;
		this.destinacija = destinacija;
		this.prevoznoSredstvo = prevoznoSredstvo;
		this.smestajnaJedinice = smestajnaJedinice;
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

	public List<SmestajnaJedinica> getSmestajneJedinice() {
		return smestajnaJedinice;
	}

	public void setSmestajneJedinice(List<SmestajnaJedinica> smestajnaJedinice) {
		this.smestajnaJedinice = smestajnaJedinice;
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
