package com.ftn.TravelOrganisation.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.helper.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.ftn.TravelOgranisation.util.DateUtil;
import com.ftn.TravelOrganisation.model.Korisnik;
import com.ftn.TravelOrganisation.model.Recenzija;
import com.ftn.TravelOrganisation.model.SmestajnaJedinica;
import com.ftn.TravelOrganisation.model.Recenzija;
import com.ftn.TravelOrganisation.repository.KorisnikRepository;
import com.ftn.TravelOrganisation.repository.RecenzijaRepository;
import com.ftn.TravelOrganisation.repository.SmestajnaJedinicaRepository;

@Repository
public class RecenzijaRepositoryImpl implements RecenzijaRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private KorisnikRepository korisnikRepository;

	@Autowired
	private SmestajnaJedinicaRepository smestajnaJedinicaRepository;

	private class RecenzijaRowCallBackHandler implements RowCallbackHandler {

		private Map<Long, Recenzija> recenzije = new LinkedHashMap<>();

		@Override
		public void processRow(ResultSet resultSet) throws SQLException {
			int index = 1;
			Long id = resultSet.getLong(index++);
			int ocena = resultSet.getInt(index++);
			String komentar = resultSet.getString(index++);
			String datumRecenzijeStr = resultSet.getString(index++);
			Long smestajnaJedinicaId = resultSet.getLong(index++);
			Long autorRecenzijeId = resultSet.getLong(index++);

			Korisnik autor = korisnikRepository.findOne(autorRecenzijeId);
			SmestajnaJedinica smestajnaJedinica = smestajnaJedinicaRepository.findOne(smestajnaJedinicaId);

			LocalDate datumRecenzije = DateUtil.stringToDate(datumRecenzijeStr);

			Recenzija Recenzija = recenzije.get(id);
			if (Recenzija == null) {
				Recenzija = new Recenzija(id, ocena, komentar, datumRecenzije, autor, smestajnaJedinica);
				recenzije.put(Recenzija.getId(), Recenzija); // dodavanje u kolekciju
			}
		}

		public List<Recenzija> getrecenzije() {
			return new ArrayList<>(recenzije.values());

		}

	}

	@Override
	public List<Recenzija> getBySmestajId(Long id) {

		// izmeni upit
		String sql = "SELECT * FROM recenzije" + " WHERE smestajna_jedinica_id = ? " + "ORDER BY id";;
		

		RecenzijaRowCallBackHandler rowCallbackHandler = new RecenzijaRowCallBackHandler();
		jdbcTemplate.query(sql, rowCallbackHandler, id);

		return rowCallbackHandler.getrecenzije();

	}

}
