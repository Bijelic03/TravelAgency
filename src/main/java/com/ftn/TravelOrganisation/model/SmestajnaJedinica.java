package com.ftn.TravelOrganisation.model;

import java.util.List;


public class SmestajnaJedinica {
	private Long id;
	private String naziv;
	private int kapacitet;
	private Destinacija destinacija;
	private List<Recenzija>  recenzije;
	private List<SmestajnaJedinicaUslugaEnum> usluge;
	private String opis;
	private SmestajnaJedinicaTipEnum tipSmestajneJedinice;
	
	
	
	public SmestajnaJedinica(Long id, String naziv, int kapacitet, Destinacija destinacija, List<Recenzija> recenzije,
			List<SmestajnaJedinicaUslugaEnum> usluge, String opis, SmestajnaJedinicaTipEnum smestajnaJedinicaTipEnum) {
		super();
		this.id = id;
		this.naziv = naziv;
		this.kapacitet = kapacitet;
		this.destinacija = destinacija;
		this.recenzije = recenzije;
		this.usluge = usluge;
		this.opis = opis;
		this.tipSmestajneJedinice = smestajnaJedinicaTipEnum;
	}
	
	
	





	








	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public int getKapacitet() {
		return kapacitet;
	}

	public void setKapacitet(int kapacitet) {
		this.kapacitet = kapacitet;
	}

	public Destinacija getDestinacija() {
		return destinacija;
	}

	public void setDestinacija(Destinacija destinacija) {
		this.destinacija = destinacija;
	}

	public List<Recenzija> getRecenzije() {
		return recenzije;
	}

	public void setRecenzije(List<Recenzija> recenzije) {
		this.recenzije = recenzije;
	}

	public List<SmestajnaJedinicaUslugaEnum> getUsluge() {
		return usluge;
	}

	public void setUsluge(List<SmestajnaJedinicaUslugaEnum> usluge) {
		this.usluge = usluge;
	}

	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}
	
	
}
