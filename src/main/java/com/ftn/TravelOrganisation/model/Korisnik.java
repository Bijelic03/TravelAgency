package com.ftn.TravelOrganisation.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;

import com.ftn.TravelOgranisation.util.DateUtil;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Korisnik {

	private Long id;
	private String korisnickoIme;
	private String sifra;
	private String email;
	private String ime;
	private String prezime;
	private String adresa;
	private String brojTelefona;

	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate datumRodjenja;

	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime datumVremeRegistracije;
	private KorisnikUloga uloga;

	// private List<Putovanje> listaZelja;

//	private List<Rezervacija> rezervacije;
	// private LoyaltyKartica loyaltyKartica;
	private boolean blokiran;

	public Korisnik() {
		this.email = "aaa";
	}

	// konstruktor koriscen u citanju txt-a
	public Korisnik(Long id, String korisnickoIme, String sifra, String email, String ime, String prezime,
			String adresa, String brojTelefona, String datumRodjenja, String datumVremeRegistracije, String uloga,
			Boolean blokiran) {
		this.id = id;
		this.korisnickoIme = korisnickoIme;
		this.sifra = sifra;
		this.email = email;
		this.ime = ime;
		this.prezime = prezime;
		this.adresa = adresa;
		this.brojTelefona = brojTelefona;
		this.datumRodjenja = DateUtil.stringToDate(datumRodjenja);
		this.datumVremeRegistracije = DateUtil.stringToLocalDateTime(datumVremeRegistracije);
		this.uloga = stringToEnum(uloga);
		this.blokiran = blokiran;
	}

	// konstruktor koriscen u registraciji
	public Korisnik(String korisnickoIme, String sifra, String email, String ime, String prezime, String adresa,
			String brojTelefona, String datumRodjenja) {
		this.korisnickoIme = korisnickoIme;
		this.sifra = sifra;
		this.email = email;
		this.ime = ime;
		this.prezime = prezime;
		this.adresa = adresa;
		this.brojTelefona = brojTelefona;
		this.datumRodjenja = DateUtil.stringToDate(datumRodjenja);
	}

	public Korisnik(Long id, String korisnickoIme, String sifra, String email, String ime, String prezime,
			String adresa, String brojTelefona, LocalDate datumRodjenja, LocalDateTime datumVremeRegistracije,
			KorisnikUloga uloga, boolean blokiran) {
		this.id = id;
		this.korisnickoIme = korisnickoIme;
		this.sifra = sifra;
		this.email = email;
		this.ime = ime;
		this.prezime = prezime;
		this.adresa = adresa;
		this.brojTelefona = brojTelefona;
		this.datumRodjenja = datumRodjenja;
		this.datumVremeRegistracije = datumVremeRegistracije;
		this.uloga = uloga;
		this.blokiran = blokiran;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(id).append(";").append(korisnickoIme).append(";").append(sifra).append(";").append(email)
				.append(";").append(ime).append(";").append(prezime).append(";").append(adresa).append(";")
				.append(brojTelefona).append(";").append(datumRodjenja).append(";").append(datumVremeRegistracije)
				.append(";").append(uloga);
		return builder.toString();
	}

	private KorisnikUloga stringToEnum(String ulogaString) {
		return KorisnikUloga.valueOf(ulogaString);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKorisnickoIme() {
		return korisnickoIme;
	}

	public void setKorisnickoIme(String korisnickoIme) {
		this.korisnickoIme = korisnickoIme;
	}

	public String getSifra() {
		return sifra;
	}

	public void setSifra(String sifra) {
		this.sifra = sifra;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public String getAdresa() {
		return adresa;
	}

	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}

	public String getBrojTelefona() {
		return brojTelefona;
	}

	public void setBrojTelefona(String brojTelefona) {
		this.brojTelefona = brojTelefona;
	}

	public LocalDate getDatumRodjenja() {
		return datumRodjenja;
	}

	public void setDatumRodjenja(LocalDate datumRodjenja) {
		this.datumRodjenja = datumRodjenja;
	}

	public LocalDateTime getDatumVremeRegistracije() {
		return datumVremeRegistracije;
	}

	public void setDatumVremeRegistracije(LocalDateTime datumVremeRegistracije) {
		this.datumVremeRegistracije = datumVremeRegistracije;
	}

	public KorisnikUloga getUloga() {
		return uloga;
	}

	public void setUloga(KorisnikUloga uloga) {
		this.uloga = uloga;
	}

	public boolean isBlokiran() {
		return blokiran;
	}

	public void setBlokiran(boolean blokiran) {
		this.blokiran = blokiran;
	}

	public boolean getIsAdmin() {
	    return uloga == KorisnikUloga.ADMINISTRATOR;
	}

	public boolean getIsPutnik() {
	    return uloga == KorisnikUloga.PUTNIK;
	}

	public boolean getIsMenadzer() {
		return uloga == KorisnikUloga.MENADZER ? true : false;
	}

}
