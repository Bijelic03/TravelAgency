package com.ftn.TravelOrganisation.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ftn.TravelOrganisation.model.Destinacija;
import com.ftn.TravelOrganisation.model.Korisnik;
import com.ftn.TravelOrganisation.model.Recenzija;
import com.ftn.TravelOrganisation.model.SmestajnaJedinica;
import com.ftn.TravelOrganisation.model.SmestajnaJedinicaTipEnum;
import com.ftn.TravelOrganisation.model.SmestajnaJedinicaUslugaEnum;
import com.ftn.TravelOrganisation.repository.DestinacijaRepository;
import com.ftn.TravelOrganisation.repository.RecenzijaRepository;
import com.ftn.TravelOrganisation.repository.SmestajnaJedinicaRepository;
import com.ftn.TravelOrganisation.repository.impl.PutovanjeRepositoryImpl.SmestajPutovanjaRowCallBackHandler;

@Repository
public class SmestajnaJedinicaRepositoryImpl implements SmestajnaJedinicaRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private DestinacijaRepository destinacijaRepository;

	@Lazy
	@Autowired
	private RecenzijaRepository recenzijaRepository;

	private class SmestajnaJedinicaRowCallBackHandler implements RowCallbackHandler {

		private Map<Long, SmestajnaJedinica> smestajneJedinice = new LinkedHashMap<>();

		@Override
		public void processRow(ResultSet resultSet) throws SQLException {
			int index = 1;
			Long id = resultSet.getLong(index++);
			String naziv = resultSet.getString(index++);
			int kapacitet = resultSet.getInt(index++);
			Long destinacijaId = resultSet.getLong(index++);
			String opis = resultSet.getString(index++);
			String tip = resultSet.getString(index++);
			String uslugeStr = resultSet.getString(index++);

			Destinacija destinacija = destinacijaRepository.findOne(destinacijaId);

			SmestajnaJedinicaTipEnum smestajnaJedinicaTipEnum = SmestajnaJedinicaTipEnum.valueOf(tip);

			String[] uslugeId = uslugeStr.split(", ");

			List<SmestajnaJedinicaUslugaEnum> usluge = new ArrayList<SmestajnaJedinicaUslugaEnum>();
			for (String usluga : uslugeId) {
				SmestajnaJedinicaUslugaEnum novaUsluga = SmestajnaJedinicaUslugaEnum.valueOf(usluga);
				usluge.add(novaUsluga);
			}

			List<Recenzija> recenzije = recenzijaRepository.getBySmestajId(id);

			SmestajnaJedinica smestajnaJedinica = smestajneJedinice.get(id);
			if (smestajnaJedinica == null) {
				smestajnaJedinica = new SmestajnaJedinica(id, naziv, kapacitet, destinacija, recenzije, usluge, opis,
						smestajnaJedinicaTipEnum);
				smestajneJedinice.put(smestajnaJedinica.getId(), smestajnaJedinica);
			}
		}

		public List<SmestajnaJedinica> getSmestajneJedinice() {
			return new ArrayList<>(smestajneJedinice.values());
		}

	}

	@Override
	public SmestajnaJedinica findOne(Long id) {
		String sql = "SELECT * FROM smestajne_jedinice " + "WHERE id = ? ";

		SmestajnaJedinicaRowCallBackHandler rowCallbackHandler = new SmestajnaJedinicaRowCallBackHandler();
		jdbcTemplate.query(sql, rowCallbackHandler, id);

		return rowCallbackHandler.getSmestajneJedinice().get(0);
	}

	@Override
	public List<SmestajnaJedinica> findByDestinacijeId(Long id) {
		String sql = "SELECT * FROM smestajne_jedinice WHERE destinacija_id = ? ";

		SmestajnaJedinicaRowCallBackHandler rowCallbackHandler = new SmestajnaJedinicaRowCallBackHandler();
		jdbcTemplate.query(sql, rowCallbackHandler, id);

		return rowCallbackHandler.getSmestajneJedinice();
	}

	@Override
	public List<SmestajnaJedinica> getSmestajneJediniceByIds(List<Long> ids) {
		String placeholders = String.join(",", Collections.nCopies(ids.size(), "?"));

		String sql = "SELECT * FROM smestajne_jedinice WHERE id IN (" + placeholders + ")";

		SmestajnaJedinicaRowCallBackHandler rowCallbackHandler = new SmestajnaJedinicaRowCallBackHandler();
		jdbcTemplate.query(sql, rowCallbackHandler, ids.toArray());

		return rowCallbackHandler.getSmestajneJedinice();
	}

	@Transactional
	@Override
	public int save(SmestajnaJedinica smestajnaJedinica) {
		PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				String sql = "INSERT INTO smestajne_jedinice (naziv, kapacitet, destinacija_id, opis, tip, usluge) VALUES (?, ?, ?, ?, ?, ?)";

				PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				int index = 1;

				String uslugeStr = smestajnaJedinica.getUsluge().toString();
				
				uslugeStr = uslugeStr.substring(1, uslugeStr.length() - 1);
						
					
				
				preparedStatement.setString(index++, smestajnaJedinica.getNaziv());
				preparedStatement.setInt(index++, smestajnaJedinica.getKapacitet());
				preparedStatement.setLong(index++, smestajnaJedinica.getDestinacija().getId());
				preparedStatement.setString(index++, smestajnaJedinica.getOpis());
				preparedStatement.setString(index++, smestajnaJedinica.getTipSmestajneJedinice().toString());
				preparedStatement.setString(index++, uslugeStr);

				return preparedStatement;
			}

		};
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		boolean success = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;
		return success ? 1 : 0;
	}

}
