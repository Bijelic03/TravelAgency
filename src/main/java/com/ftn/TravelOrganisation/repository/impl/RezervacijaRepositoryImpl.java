package com.ftn.TravelOrganisation.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ftn.TravelOgranisation.util.DateUtil;
import com.ftn.TravelOrganisation.model.Interval;
import com.ftn.TravelOrganisation.model.Korisnik;
import com.ftn.TravelOrganisation.model.LoyaltyKartica;
import com.ftn.TravelOrganisation.model.PrevoznoSredstvo;
import com.ftn.TravelOrganisation.model.Putovanje;
import com.ftn.TravelOrganisation.model.Rezervacija;
import com.ftn.TravelOrganisation.model.RezervacijaStatus;
import com.ftn.TravelOrganisation.model.SmestajnaJedinica;
import com.ftn.TravelOrganisation.repository.KorisnikRepository;
import com.ftn.TravelOrganisation.repository.PutovanjeRepository;
import com.ftn.TravelOrganisation.repository.RezervacijaRepository;
import com.ftn.TravelOrganisation.repository.SmestajnaJedinicaRepository;

@Repository
public class RezervacijaRepositoryImpl implements RezervacijaRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	KorisnikRepository korisnikRepository;
	PutovanjeRepository putovanjeRepository;
	SmestajnaJedinicaRepository smestajnaJedinicaRepository;

	public RezervacijaRepositoryImpl(KorisnikRepository korisnikRepository, PutovanjeRepository putovanjeRepository,
			SmestajnaJedinicaRepository smestajnaJedinicaRepository) {
		this.korisnikRepository = korisnikRepository;
		this.putovanjeRepository = putovanjeRepository;
		this.smestajnaJedinicaRepository = smestajnaJedinicaRepository;
	}

	private class RezervacijaRowCallBackHandler implements RowCallbackHandler {

		private Map<Long, Rezervacija> rezervacije = new LinkedHashMap<>();

		@Override
		public void processRow(ResultSet resultSet) throws SQLException {
			int index = 1;
			Long id = resultSet.getLong(index++);
			Long korisnikId = resultSet.getLong(index++);
			Long putovanjeId = resultSet.getLong(index++);
			Long terminId = resultSet.getLong(index++);
			Long smestajnaJedinicaId = resultSet.getLong(index++);
			int brojOsoba = resultSet.getInt(index++);
			double ukupnaCena = resultSet.getDouble(index++);
			String vremeRegistracijeStr = resultSet.getString(index++);
			String statusStr = resultSet.getString(index++);

			Korisnik korisnik = korisnikRepository.findOne(korisnikId);
			Putovanje putovanje = putovanjeRepository.findOne(putovanjeId);
			Interval termin = putovanjeRepository.findOneTermin(terminId);
			SmestajnaJedinica smestajnaJedinica = smestajnaJedinicaRepository.findOne(smestajnaJedinicaId);
			LocalDateTime vremeRegistracije = DateUtil.stringToLocalDateTime(vremeRegistracijeStr);
			RezervacijaStatus status = RezervacijaStatus.valueOf(statusStr);

			Rezervacija rezervacija = new Rezervacija(id, korisnik, putovanje, brojOsoba, vremeRegistracije, termin,
					ukupnaCena, smestajnaJedinica, status);

			rezervacije.put(rezervacija.getId(), rezervacija);
		}

		public List<Rezervacija> getRezervacije() {
			return new ArrayList<>(rezervacije.values());
		}
	}

	@Override
	public Rezervacija findOne(Long id) {
		String sql = "SELECT * " + "FROM rezervacije WHERE id = ?";

		RezervacijaRowCallBackHandler rowCallbackHandler = new RezervacijaRowCallBackHandler();
		jdbcTemplate.query(sql, rowCallbackHandler, id);

		return rowCallbackHandler.getRezervacije().get(0);
	}

	@Transactional
	@Override
	public int save(Rezervacija rezervacija) {
		PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				String sql = "INSERT INTO rezervacije (korisnik_id, putovanje_id, termin_id, smestajna_jedinica_id, "
						+ "broj_osoba, ukupna_cena, vreme_rezervacije, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

				PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				int index = 1;
				preparedStatement.setLong(index++, rezervacija.getKorisnik().getId());
				preparedStatement.setLong(index++, rezervacija.getPutovanje().getId());
				preparedStatement.setLong(index++, rezervacija.getTermin().getId());
				preparedStatement.setLong(index++, rezervacija.getSmestajnaJedinica().getId());
				preparedStatement.setInt(index++, rezervacija.getBrojPutnika());
				preparedStatement.setDouble(index++, rezervacija.getCena());
				preparedStatement.setTimestamp(index++, java.sql.Timestamp.valueOf(rezervacija.getVremeRezervacije()));
				preparedStatement.setString(index++, rezervacija.getStatusRezervacije().toString());

				return preparedStatement;
			}

		};
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		boolean success = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;
		return success ? 1 : 0;
	}

	@Override
	public List<Rezervacija> findAll() {
		String sql = "SELECT * " + " FROM rezervacije ORDER BY id ";

		RezervacijaRowCallBackHandler rowCallbackHandler = new RezervacijaRowCallBackHandler();
		jdbcTemplate.query(sql, rowCallbackHandler);

		return rowCallbackHandler.getRezervacije();
	}

	@Override
	public List<Rezervacija> findAllFiltered(LocalDate pocetniDatum, LocalDate krajnjiDatum) {
		StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM rezervacije WHERE 1=1");

		if (pocetniDatum != null) {
			sqlBuilder.append(" AND vreme_rezervacije >= '").append(pocetniDatum).append("'");
		}

		if (krajnjiDatum != null) {
			sqlBuilder.append(" AND vreme_rezervacije <= '").append(krajnjiDatum).append("'");
		}

		RezervacijaRowCallBackHandler rowCallbackHandler = new RezervacijaRowCallBackHandler();
		jdbcTemplate.query(sqlBuilder.toString(), rowCallbackHandler);
		return rowCallbackHandler.getRezervacije();
	}

	@Override
	public List<Rezervacija> findByPutovanjeId(Long putovanjeId) {
		String sql = "SELECT * " + " FROM rezervacije WHERE putovanje_id = ? ORDER BY id ";

		RezervacijaRowCallBackHandler rowCallbackHandler = new RezervacijaRowCallBackHandler();
		jdbcTemplate.query(sql, rowCallbackHandler, putovanjeId);

		return rowCallbackHandler.getRezervacije();
	}

	@Override
	public List<Rezervacija> findByKorisnikId(Long korisnikId) {
		String sql = "SELECT * " + " FROM rezervacije WHERE korisnik_id = ? ORDER BY id ";

		RezervacijaRowCallBackHandler rowCallbackHandler = new RezervacijaRowCallBackHandler();
		jdbcTemplate.query(sql, rowCallbackHandler, korisnikId);

		return rowCallbackHandler.getRezervacije();
	}

	@Override
	public boolean updateStatus(Long idRezervacije, RezervacijaStatus noviStatus) {
		String sql = "UPDATE rezervacije SET status = ? WHERE id = ?";

		int affectedRows = jdbcTemplate.update(sql, noviStatus.toString(), idRezervacije);

		return affectedRows > 0;
	}
}
