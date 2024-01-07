package com.ftn.TravelOrganisation.repository;

import java.util.List;

import com.ftn.TravelOrganisation.model.SmestajnaJedinica;

public interface SmestajnaJedinicaRepository {
	public SmestajnaJedinica findOne(Long id);
	public List<SmestajnaJedinica> findByDestinacijeId(Long id);
	List<SmestajnaJedinica> getSmestajneJediniceByIds(List<Long> ids);
	public int save(SmestajnaJedinica smestajnaJedinica);
}
