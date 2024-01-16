package com.ftn.TravelOrganisation.repository;

import java.time.LocalDate;
import java.util.List;

import com.ftn.TravelOrganisation.model.Interval;
import com.ftn.TravelOrganisation.model.KategorijaPutovanjaEnum;
import com.ftn.TravelOrganisation.model.PrevoznoSredstvoTipEnum;
import com.ftn.TravelOrganisation.model.Putovanje;
import com.ftn.TravelOrganisation.model.SmestajnaJedinica;
import com.ftn.TravelOrganisation.model.SmestajnaJedinicaTipEnum;

public interface PutovanjeRepository {
	public Putovanje findOne(Long id);

	public List<Putovanje> findAll();

	public int savePutovanje(Putovanje putovanje);

	public int saveSmestajnaJedinicaPutovanje(SmestajnaJedinica smestajnaJedinica, Long idPutovanja);

	public int saveTermin(Interval interval, Long putovanjeId);

	public List<Putovanje> filterBy(Double cenaDo, Double cenaOd, LocalDate datumPolaska, LocalDate datumPovratka,
			String nazivDestinacije, String sifraPutovanja, List<SmestajnaJedinicaTipEnum> smestajiEnum,
			List<PrevoznoSredstvoTipEnum> prevoziEnum, List<KategorijaPutovanjaEnum> kategorijeEnum, int brOsoba, int brNocenja);

}
