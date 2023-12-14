package com.ftn.TravelOrganisation.repository;

import java.util.List;

import com.ftn.TravelOrganisation.model.Destinacija;

public interface DestinacijaRepository {

public Destinacija findOne(Long id);

public List<Destinacija> findAll();

public int save(Destinacija destinacija);

public int update(Destinacija destinacija);

public int delete(Long id);
}
