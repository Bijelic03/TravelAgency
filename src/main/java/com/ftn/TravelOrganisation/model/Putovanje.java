package com.ftn.TravelOrganisation.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class Putovanje {

	private Long id;

	private Destinacija destinacija;

	private PrevoznoSredstvo prevoznoSredstvo;

	private List<SmestajnaJedinica> smestajnaJedinice;
	private KategorijaPutovanjaEnum kategorijaPutovanja;
	private List<Interval> listaTermina;

	private Double cenaAranzmana;

	public Putovanje(Long id, Destinacija destinacija, PrevoznoSredstvo prevoznoSredstvo,
			List<SmestajnaJedinica> smestajnaJedinice, KategorijaPutovanjaEnum kategorijaPutovanja,
			List<Interval> listaTermina, Double cenaAranzmana) {
		super();
		this.id = id;
		this.destinacija = destinacija;
		this.prevoznoSredstvo = prevoznoSredstvo;
		this.smestajnaJedinice = smestajnaJedinice;
		this.kategorijaPutovanja = kategorijaPutovanja;
		this.setListaTermina(listaTermina);
		this.cenaAranzmana = cenaAranzmana;
	}
	
	public Putovanje( Destinacija destinacija, PrevoznoSredstvo prevoznoSredstvo,
			List<SmestajnaJedinica> smestajnaJedinice, KategorijaPutovanjaEnum kategorijaPutovanja,
			List<Interval> listaTermina, Double cenaAranzmana) {
		super();
		this.destinacija = destinacija;
		this.prevoznoSredstvo = prevoznoSredstvo;
		this.smestajnaJedinice = smestajnaJedinice;
		this.kategorijaPutovanja = kategorijaPutovanja;
		this.setListaTermina(listaTermina);
		this.cenaAranzmana = cenaAranzmana;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Double getCenaAranzmana() {
		return cenaAranzmana;
	}

	public void setCenaAranzmana(Double cenaAranzmana) {
		this.cenaAranzmana = cenaAranzmana;
	}

	public List<Interval> getListaTermina() {
		return listaTermina;
	}

	public void setListaTermina(List<Interval> listaTermina) {
		this.listaTermina = listaTermina;
	}

}
