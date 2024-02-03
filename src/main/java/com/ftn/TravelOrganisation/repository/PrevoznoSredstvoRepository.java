package com.ftn.TravelOrganisation.repository;

import java.util.List;

import com.ftn.TravelOrganisation.model.PrevoznoSredstvo;
import com.ftn.TravelOrganisation.model.Putovanje;

public interface PrevoznoSredstvoRepository {
public PrevoznoSredstvo findOne(Long id);
public List<PrevoznoSredstvo> findAll();
public List<PrevoznoSredstvo> findByDestinacijeId(Long id);
public int save(PrevoznoSredstvo prevoznoSredstvo);
public boolean updateBrojSedista(PrevoznoSredstvo prevoznoSredstvo, int noviKapacitetPrevoz);
}
