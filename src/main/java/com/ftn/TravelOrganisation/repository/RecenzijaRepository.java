package com.ftn.TravelOrganisation.repository;

import java.util.List;

import com.ftn.TravelOrganisation.model.Recenzija;

public interface RecenzijaRepository {

	public List<Recenzija> getBySmestajId(Long id);
}
