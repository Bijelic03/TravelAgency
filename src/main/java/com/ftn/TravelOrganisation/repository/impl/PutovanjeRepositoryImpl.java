package com.ftn.TravelOrganisation.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ftn.TravelOgranisation.util.DateUtil;
import com.ftn.TravelOrganisation.model.Destinacija;
import com.ftn.TravelOrganisation.model.Interval;
import com.ftn.TravelOrganisation.model.KategorijaPutovanjaEnum;
import com.ftn.TravelOrganisation.model.PrevoznoSredstvo;
import com.ftn.TravelOrganisation.model.PrevoznoSredstvoTipEnum;
import com.ftn.TravelOrganisation.model.Putovanje;
import com.ftn.TravelOrganisation.model.SmestajnaJedinica;
import com.ftn.TravelOrganisation.repository.DestinacijaRepository;
import com.ftn.TravelOrganisation.repository.PrevoznoSredstvoRepository;
import com.ftn.TravelOrganisation.repository.PutovanjeRepository;
import com.ftn.TravelOrganisation.repository.SmestajnaJedinicaRepository;

@Repository
public class PutovanjeRepositoryImpl implements PutovanjeRepository {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private DestinacijaRepository destinacijaRepository;

	@Autowired
	private PrevoznoSredstvoRepository prevoznoSredstvoRepository;

	@Autowired
	private SmestajnaJedinicaRepository smestajnaJedinicaRepository;

	private PrevoznoSredstvo prevoznoSredstvo;

	public class PutovanjeRowCallBackHandler implements RowCallbackHandler {

		private Map<Long, Putovanje> putovanja = new LinkedHashMap<>();

		@Override
		public void processRow(ResultSet resultSet) throws SQLException {
			int index = 1;
			Long id = resultSet.getLong(index++);
			Long destinacijaId = resultSet.getLong(index++);
			Long prevoznoSredstvoId = resultSet.getLong(index++);
			String kategorijaPutovanjaStr = resultSet.getString(index++);
			Double cenaAranzmana = resultSet.getDouble(index++);

			Destinacija destinacija = destinacijaId != null ? destinacijaRepository.findOne(destinacijaId) : null;

			PrevoznoSredstvo prevoznoSredstvo = prevoznoSredstvoRepository.findOne(prevoznoSredstvoId);

			if (prevoznoSredstvo == null) {
				System.out.println(id);
				System.out.println("prevozno sredstvo je null");

			}

			KategorijaPutovanjaEnum kategorijaPutovanja = KategorijaPutovanjaEnum.valueOf(kategorijaPutovanjaStr);

			List<SmestajnaJedinica> smestajneJedinice = getSmestajneJediniceByPutovanjeId(id);

			List<Interval> listaTermina = getTerminiByPutovanjeId(id);

			Putovanje putovanje = new Putovanje(id, destinacija, prevoznoSredstvo, smestajneJedinice,
					kategorijaPutovanja, listaTermina, cenaAranzmana);

			// Dodajte putovanje u mapu putovanja sa proverom null vrednosti
			if (id != null && putovanje != null) {
				putovanja.put(id, putovanje);
			}
		}

		public List<Putovanje> getPutovanja() {

			return new ArrayList<>(putovanja.values());
		}

	}

	public class ListaTerminaRowCallBackHandler implements RowCallbackHandler {

		private List<Interval> listaTermina = new ArrayList<Interval>();

		@Override
		public void processRow(ResultSet resultSet) throws SQLException {
			int index = 1;
			Long id = resultSet.getLong(index++);
			Long idPutovanja = resultSet.getLong(index++);
			String vremePolaskaStr = resultSet.getString(index++);
			String vremePovratkaStr = resultSet.getString(index++);
			int brojNocenja = resultSet.getInt(index++);

			LocalDate vremePolaska = DateUtil.stringToDate(vremePolaskaStr);
			LocalDate vremePovratka = DateUtil.stringToDate(vremePovratkaStr);

			Interval interval = new Interval(id, idPutovanja, vremePolaska, vremePovratka, brojNocenja);

			if (id != null && interval != null) {
				listaTermina.add(interval);
			}

		}

		protected List<Interval> getListaTermina() {
			return listaTermina;
		}

	}

	private List<Interval> getTerminiByPutovanjeId(Long id) {
		String sql = "SELECT * FROM termini " + "WHERE putovanje_id = ? ";

		ListaTerminaRowCallBackHandler rowCallbackHandler = new ListaTerminaRowCallBackHandler();
		jdbcTemplate.query(sql, rowCallbackHandler, id);

		return rowCallbackHandler.getListaTermina();
	}

	public class SmestajPutovanjaRowCallBackHandler implements RowCallbackHandler {

		private List<SmestajnaJedinica> smestajneJedinice = new ArrayList<SmestajnaJedinica>();

		@Override
		public void processRow(ResultSet resultSet) throws SQLException {
			int index = 1;
			Long putovanjeId = resultSet.getLong(index++);
			Long smestajId = resultSet.getLong(index++);

			smestajneJedinice.add(smestajnaJedinicaRepository.findOne(smestajId));

		}

		protected List<SmestajnaJedinica> getSmestajneJedinice() {
			return smestajneJedinice;
		}

	}

	private List<SmestajnaJedinica> getSmestajneJediniceByPutovanjeId(Long id) {
		String sql = "SELECT * FROM smestaj_putovanja " + "WHERE putovanje_id = ? ";

		SmestajPutovanjaRowCallBackHandler rowCallbackHandler = new SmestajPutovanjaRowCallBackHandler();
		jdbcTemplate.query(sql, rowCallbackHandler, id);

		return rowCallbackHandler.getSmestajneJedinice();
	}

	@Transactional
	@Override
	public int saveTermin(Interval interval, Long putovanjeId) {
		PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				String sql = "INSERT INTO termini (putovanje_id, vreme_polaska, vreme_povratka, broj_nocenja) VALUES (?, ?, ?, ?)";

				PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				int index = 1;
				preparedStatement.setLong(index++, putovanjeId);
				preparedStatement.setDate(index++, java.sql.Date.valueOf(interval.getVremePolaska()));
				preparedStatement.setDate(index++, java.sql.Date.valueOf(interval.getVremePovratka()));
				preparedStatement.setInt(index++, interval.getBrojNocenja());

				return preparedStatement;
			}

		};
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		boolean uspeh = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;
		return uspeh ? 1 : 0;
	}

	@Transactional
	@Override
	public int saveSmestajnaJedinicaPutovanje(SmestajnaJedinica smestajnaJedinica, Long idPutovanja) {
		PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				String sql = "INSERT INTO smestaj_putovanja (putovanje_id, smestaj_id) VALUES (?, ?)";

				PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				int index = 1;
				preparedStatement.setLong(index++, idPutovanja);
				preparedStatement.setLong(index++, smestajnaJedinica.getId());

				return preparedStatement;
			}

		};
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		boolean uspeh = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;
		return uspeh ? 1 : 0;
	}

	@Transactional
	@Override
	public int savePutovanje(Putovanje putovanje) {

		PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				String sql = "INSERT INTO putovanja (destinacija_id, prevozno_sredstvo_id, kategorija_putovanja, cena_aranzmana) VALUES (?, ?, ?, ?)";

				PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				int index = 1;
				preparedStatement.setLong(index++, putovanje.getDestinacija().getId());
				preparedStatement.setLong(index++, putovanje.getPrevoznoSredstvo().getId());
				preparedStatement.setString(index++, putovanje.getKategorijaPutovanja().name());
				preparedStatement.setDouble(index++, putovanje.getCenaAranzmana());

				return preparedStatement;
			}

		};

		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		boolean uspeh = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;

		// Dobijanje generisanog ID-a putovanja
		Long putovanjeId = keyHolder.getKey().longValue();

		for (Interval termin : putovanje.getListaTermina()) {
			saveTermin(termin, putovanjeId);
		}

		for (SmestajnaJedinica smestajnaJedinica : putovanje.getSmestajneJedinice()) {
			saveSmestajnaJedinicaPutovanje(smestajnaJedinica, putovanjeId);
		}

		return uspeh ? 1 : 0;
	}

	@Override
	public Putovanje findOne(Long id) {
		String sql = "SELECT * FROM putovanja p " + "WHERE p.id = ? " + "ORDER BY p.id";

		PutovanjeRowCallBackHandler rowCallbackHandler = new PutovanjeRowCallBackHandler();
		jdbcTemplate.query(sql, rowCallbackHandler, id);

		return rowCallbackHandler.getPutovanja().get(0);
	}

	@Override
	public List<Putovanje> findAll() {
		String sql = "SELECT * FROM putovanja  ORDER BY id";
		PutovanjeRowCallBackHandler rowCallbackHandler = new PutovanjeRowCallBackHandler();
		jdbcTemplate.query(sql, rowCallbackHandler);

		List<Putovanje> putovanja = rowCallbackHandler.getPutovanja();

		if (putovanja.isEmpty()) {
			System.out.println("Nema pronađenih putovanja.");
		} else {
			System.out.println("Pronađena putovanja: ");
			for (Putovanje putovanje : putovanja) {
				System.out.println(putovanje); // Dodajte ispisivanje po potrebi
			}
		}

		return putovanja;
	}

}
