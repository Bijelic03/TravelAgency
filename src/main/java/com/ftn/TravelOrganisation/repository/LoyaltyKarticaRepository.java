package com.ftn.TravelOrganisation.repository;

import java.util.List;

import com.ftn.TravelOrganisation.model.LoyaltyKartica;

public interface LoyaltyKarticaRepository {

	public LoyaltyKartica findByKorisnikId(Long korisnikId);

	public int save(LoyaltyKartica loyaltyKartica);

	public List<LoyaltyKartica> findAll();

	public void acceptKartica(Long karticaId);

	public void rejectKartica(Long id);

}
