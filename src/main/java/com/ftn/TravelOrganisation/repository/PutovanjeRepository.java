package com.ftn.TravelOrganisation.repository;

import java.util.List;

import com.ftn.TravelOrganisation.model.Interval;
import com.ftn.TravelOrganisation.model.Putovanje;
import com.ftn.TravelOrganisation.model.SmestajnaJedinica;


public interface PutovanjeRepository {
    public Putovanje findOne(Long id);
    public List<Putovanje> findAll();
	public int savePutovanje(Putovanje putovanje);
	public int saveSmestajnaJedinicaPutovanje(SmestajnaJedinica smestajnaJedinica, Long idPutovanja);
	public int saveTermin(Interval interval, Long putovanjeId);
    
}
