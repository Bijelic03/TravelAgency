package com.ftn.TravelOrganisation.repository;

import java.util.List;

import com.ftn.TravelOrganisation.model.Korisnik;
import com.ftn.TravelOrganisation.model.KorisnikUloga;

public interface KorisnikRepository {

    public Korisnik findOne(Long id);
    
    public Korisnik findByKorisnickoImeAndSifra(String korisnickoIme, String sifra);

    public List<Korisnik> findAll();

    public int save(Korisnik korisnik);

    public int update(Korisnik korisnik);

    public int delete(Long id);

	public int toggleBlock(Korisnik korisnik);

	public List<Korisnik> findByUloga(KorisnikUloga uloga);

	public List<Korisnik> findByKorisnickoImeContains(String userSearch);
}
