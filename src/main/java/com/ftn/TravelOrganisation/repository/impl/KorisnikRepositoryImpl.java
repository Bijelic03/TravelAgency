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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ftn.TravelOrganisation.model.Korisnik;
import com.ftn.TravelOrganisation.model.KorisnikUloga;
import com.ftn.TravelOrganisation.repository.KorisnikRepository;

@Repository
public class KorisnikRepositoryImpl implements KorisnikRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private class KorisnikRowCallBackHandler implements RowCallbackHandler {
		private Map<Long, Korisnik> korisnici = new LinkedHashMap<>();

		@Override
		public void processRow(ResultSet resultSet) throws SQLException {
			int index = 1;
			Long id = resultSet.getLong(index++);
			String korisnickoIme = resultSet.getString(index++);
			String sifra = resultSet.getString(index++);
			String email = resultSet.getString(index++);
			String ime = resultSet.getString(index++);
			String prezime = resultSet.getString(index++);
			String adresa = resultSet.getString(index++);
			String brojTelefona = resultSet.getString(index++);
			String datumRodjenja = resultSet.getString(index++);
			String datumVremeRegistracije = resultSet.getString(index++);
			String uloga = resultSet.getString(index++);
			Boolean blokiran = resultSet.getBoolean(index++);

			Korisnik korisnik = korisnici.get(id);
			if (korisnik == null) {
				korisnik = new Korisnik(id, korisnickoIme, sifra, email, ime, prezime, adresa, brojTelefona,
						datumRodjenja, datumVremeRegistracije, uloga, blokiran);
				korisnici.put(korisnik.getId(), korisnik);
			}
		}

		public List<Korisnik> getKorisnici() {
			return new ArrayList<>(korisnici.values());
		}
	}

	@Override
	public Korisnik findOne(Long id) {
		String sql = "SELECT k.id, k.korisnicko_ime, k.sifra, k.email, k.ime, k.prezime, k.adresa, k.broj_telefona, "
				+ "k.datum_rodjenja, k.datum_vreme_registracije, k.uloga, k.blokiran "
				+ "FROM korisnici k WHERE k.id = ? ORDER BY k.id";

		KorisnikRowCallBackHandler rowCallbackHandler = new KorisnikRowCallBackHandler();
		jdbcTemplate.query(sql, rowCallbackHandler, id);

		return rowCallbackHandler.getKorisnici().get(0);
	}

	@Override
	public List<Korisnik> findAll() {
		String sql = "SELECT k.id, k.korisnicko_ime, k.sifra, k.email, k.ime, k.prezime, k.adresa, k.broj_telefona, "
				+ "k.datum_rodjenja, k.datum_vreme_registracije, k.uloga, k.blokiran FROM korisnici k ORDER BY k.id ";

		KorisnikRowCallBackHandler rowCallbackHandler = new KorisnikRowCallBackHandler();
		jdbcTemplate.query(sql, rowCallbackHandler);

		return rowCallbackHandler.getKorisnici();
	}

	@Transactional
	@Override
	public int save(Korisnik korisnik) {
		PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				String sql = "INSERT INTO korisnici (korisnicko_ime, sifra, email, ime, prezime, adresa, broj_telefona, "
						+ "datum_rodjenja, datum_vreme_registracije, uloga, blokiran) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				int index = 1;
				preparedStatement.setString(index++, korisnik.getKorisnickoIme());
				preparedStatement.setString(index++, korisnik.getSifra());
				preparedStatement.setString(index++, korisnik.getEmail());
				preparedStatement.setString(index++, korisnik.getIme());
				preparedStatement.setString(index++, korisnik.getPrezime());
				preparedStatement.setString(index++, korisnik.getAdresa());
				preparedStatement.setString(index++, korisnik.getBrojTelefona());
				preparedStatement.setDate(index++, java.sql.Date.valueOf(korisnik.getDatumRodjenja()));
				preparedStatement.setTimestamp(index++,
						java.sql.Timestamp.valueOf(korisnik.getDatumVremeRegistracije()));
				preparedStatement.setString(index++, korisnik.getUloga().toString());
				preparedStatement.setBoolean(index++, korisnik.isBlokiran());

				return preparedStatement;
			}

		};
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		boolean success = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;
		return success ? 1 : 0;
	}

	@Override
	public Korisnik findByKorisnickoImeAndSifra(String korisnickoIme, String sifra) {
		String sql = "SELECT * FROM korisnici WHERE korisnicko_ime = ? AND sifra = ?";
		try {
			return jdbcTemplate.queryForObject(sql, new Object[] { korisnickoIme, sifra }, (resultSet, rowNum) -> {
				return mapResultSetToKorisnik(resultSet);
			});
		} catch (EmptyResultDataAccessException e) {
			return null; // Ako nema rezultata, vraćamo null
		}
	}

	@Override
	public List<Korisnik> findByKorisnickoImeContains(String korisnickoIme) {
		 String sql = "SELECT k.id, k.korisnicko_ime, k.sifra, k.email, k.ime, k.prezime, k.adresa, k.broj_telefona, "
		            + "k.datum_rodjenja, k.datum_vreme_registracije, k.uloga, k.blokiran FROM korisnici k WHERE k.korisnicko_ime LIKE ? ORDER BY k.id";

		    String parameterizedKorisnickoIme = "%" + korisnickoIme + "%";

		KorisnikRowCallBackHandler rowCallbackHandler = new KorisnikRowCallBackHandler();
		jdbcTemplate.query(sql, rowCallbackHandler, parameterizedKorisnickoIme);

		return rowCallbackHandler.getKorisnici();
	}

	private Korisnik mapResultSetToKorisnik(ResultSet resultSet) throws SQLException {
		Long id = resultSet.getLong("id");
		String korisnickoIme = resultSet.getString("korisnicko_ime");
		String sifra = resultSet.getString("sifra");
		String email = resultSet.getString("email");
		String ime = resultSet.getString("ime");
		String prezime = resultSet.getString("prezime");
		String adresa = resultSet.getString("adresa");
		String brojTelefona = resultSet.getString("broj_telefona");
		LocalDate datumRodjenja = resultSet.getObject("datum_rodjenja", LocalDate.class);
		LocalDateTime datumVremeRegistracije = resultSet.getObject("datum_vreme_registracije", LocalDateTime.class);
		KorisnikUloga uloga = KorisnikUloga.valueOf(resultSet.getString("uloga"));
		boolean blokiran = resultSet.getBoolean("blokiran");

		return new Korisnik(id, korisnickoIme, sifra, email, ime, prezime, adresa, brojTelefona, datumRodjenja,
				datumVremeRegistracije, uloga, blokiran);
	}

	@Transactional
	@Override
	public int update(Korisnik korisnik) {
		String sql = "UPDATE korisnici SET korisnicko_ime = ?, sifra = ?, email = ?, ime = ?, prezime = ?, adresa = ?, "
				+ "broj_telefona = ?, datum_rodjenja = ?, datum_vreme_registracije = ?, uloga = ?, blokiran = ? "
				+ "WHERE id = ?";
		boolean uspeh = jdbcTemplate.update(sql, korisnik.getKorisnickoIme(), korisnik.getSifra(), korisnik.getEmail(),
				korisnik.getIme(), korisnik.getPrezime(), korisnik.getAdresa(), korisnik.getBrojTelefona(),
				korisnik.getDatumRodjenja(), korisnik.getDatumVremeRegistracije(), korisnik.getUloga(),
				korisnik.isBlokiran(), korisnik.getId()) == 1;

		return uspeh ? 1 : 0;
	}

	@Transactional
	@Override
	public int toggleBlock(Korisnik korisnik) {
		String sql = "UPDATE korisnici SET  blokiran = ? " + "WHERE id = ?";
		boolean uspeh = jdbcTemplate.update(sql, !korisnik.isBlokiran(), korisnik.getId()) == 1;

		return uspeh ? 1 : 0;
	}

	@Transactional
	@Override
	public int delete(Long id) {
		String sql = "DELETE FROM korisnici WHERE id = ?";
		return jdbcTemplate.update(sql, id);
	}

	@Override
	public List<Korisnik> findByUloga(KorisnikUloga uloga) {
		String sql = "SELECT k.id, k.korisnicko_ime, k.sifra, k.email, k.ime, k.prezime, k.adresa, k.broj_telefona, "
				+ "k.datum_rodjenja, k.datum_vreme_registracije, k.uloga, k.blokiran FROM korisnici k WHERE k.uloga = ? ORDER BY k.id";

		KorisnikRowCallBackHandler rowCallbackHandler = new KorisnikRowCallBackHandler();
		jdbcTemplate.query(sql, rowCallbackHandler, uloga.toString());

		return rowCallbackHandler.getKorisnici();
	}

}
