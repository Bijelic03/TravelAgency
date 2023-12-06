package com.ftn.TravelOrganisation.model;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class Korisnik {
    private Long id;
    private String korisnickoIme;
    private String sifra;
    private String email;
    private String ime;
    private String prezime;
    private String adresa;
    private String brojTelefona;
    private LocalDate datumRodjenja;
    private LocalDateTime datumVremeRegistracije;
    private KorisnikUloga uloga;

    public Korisnik() {
    }

    
    //konstruktor koriscen u citanju txt-a
    public Korisnik(Long id, String korisnickoIme, String sifra, String email, String ime,
            String prezime, String adresa, String brojTelefona, String datumRodjenja,
            String datumVremeRegistracije, String uloga) {
    	this.id = id;
        this.korisnickoIme = korisnickoIme;
        this.sifra = sifra;
        this.email = email;
        this.ime = ime;
        this.prezime = prezime;
        this.adresa = adresa;
        this.brojTelefona = brojTelefona;
        this.datumRodjenja = stringToDate(datumRodjenja);
        this.datumVremeRegistracije = convertStringToLocalDateTime(datumVremeRegistracije);
        this.uloga = stringToEnum(uloga);
    }

    
    //konstruktor koriscen u registraciji txt-a
    public Korisnik(String korisnickoIme, String sifra, String email, String ime,
            String prezime, String adresa, String brojTelefona, String datumRodjenja) {
        this.korisnickoIme = korisnickoIme;
        this.sifra = sifra;
        this.email = email;
        this.ime = ime;
        this.prezime = prezime;
        this.adresa = adresa;
        this.brojTelefona = brojTelefona;
        this.datumRodjenja = stringToDate(datumRodjenja);
    }
    
    
    public Korisnik(Long id, String korisnickoIme, String sifra, String email, String ime,
                    String prezime, String adresa, String brojTelefona, LocalDate datumRodjenja,
                    LocalDateTime datumVremeRegistracije, KorisnikUloga uloga) {
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
    }
    
    private LocalDateTime convertStringToLocalDateTime(String localDateTimeString) {
        if (localDateTimeString == null || localDateTimeString.equalsIgnoreCase("null")) {
            return null;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS");
            return LocalDateTime.parse(localDateTimeString, formatter);
        } catch (DateTimeParseException e) {
            System.err.println("Nije moguće konvertovati string u LocalDateTime: " + localDateTimeString);
            e.printStackTrace();
            return null;
        }
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

    
    public KorisnikUloga stringToEnum(String ulogaString) {
    	return KorisnikUloga.valueOf(ulogaString);
    }
    
    
    public LocalDate stringToDate(String datumString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(datumString, formatter);
    }
    
    public LocalDateTime stringToLocalDateTime(String localDateTimeString) {
    	
    	   DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

           return LocalDateTime.parse(localDateTimeString, formatter);

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
}
