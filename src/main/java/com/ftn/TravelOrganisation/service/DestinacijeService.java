package com.ftn.TravelOrganisation.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.ftn.TravelOrganisation.model.Destinacija;

public interface DestinacijeService {
	
	public static final String DESTINACIJE_KEY = "";
	
	public Destinacija update(Destinacija destinacija);
	public Destinacija returnOne(Long id);
	public List<Destinacija> findAll();
	public Destinacija delete(Long id);
	public Destinacija save(Destinacija destinacija);
}
