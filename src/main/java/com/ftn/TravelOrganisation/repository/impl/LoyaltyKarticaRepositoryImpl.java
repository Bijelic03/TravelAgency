package com.ftn.TravelOrganisation.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
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
import com.ftn.TravelOrganisation.model.Korisnik;
import com.ftn.TravelOrganisation.model.LoyaltyKartica;
import com.ftn.TravelOrganisation.model.Recenzija;
import com.ftn.TravelOrganisation.model.Rezervacija;
import com.ftn.TravelOrganisation.model.RezervacijaStatus;
import com.ftn.TravelOrganisation.model.SmestajnaJedinica;
import com.ftn.TravelOrganisation.repository.KorisnikRepository;
import com.ftn.TravelOrganisation.repository.LoyaltyKarticaRepository;

@Repository
public class LoyaltyKarticaRepositoryImpl implements LoyaltyKarticaRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private KorisnikRepository korisnikRepository;

	private class LoyaltyKarticaRowCallBackHandler implements RowCallbackHandler {

		private Map<Long, LoyaltyKartica> kartice = new LinkedHashMap<>();

		@Override
		public void processRow(ResultSet resultSet) throws SQLException {
			int index = 1;
			Long id = resultSet.getLong(index++);
			int brojPoena = resultSet.getInt(index++);
			Long korisnikId = resultSet.getLong(index++);
			boolean odobrena = resultSet.getBoolean(index++);

			Korisnik korisnik = korisnikRepository.findOne(korisnikId);

			LoyaltyKartica loyaltyKartica = kartice.get(id);
			if (loyaltyKartica == null) {
				loyaltyKartica = new LoyaltyKartica(id, brojPoena, korisnik, odobrena);
				kartice.put(loyaltyKartica.getId(), loyaltyKartica); // dodavanje u kolekciju
			}
		}

		public List<LoyaltyKartica> getLoyaltyKartice() {
			return new ArrayList<>(kartice.values());

		}

	}

	@Override
	public LoyaltyKartica findByKorisnikId(Long korisnikId) {
		String sql = "SELECT * from loyalty_kartice WHERE korisnik_id = ? ";

		LoyaltyKarticaRowCallBackHandler rowCallbackHandler = new LoyaltyKarticaRowCallBackHandler();
		jdbcTemplate.query(sql, rowCallbackHandler, korisnikId);
		if (!rowCallbackHandler.getLoyaltyKartice().isEmpty()) {
			return rowCallbackHandler.getLoyaltyKartice().get(0);
		} else {
			return null;
		}
	}

	@Transactional
	@Override
	public int save(LoyaltyKartica loyaltyKartica) {
		PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				String sql = "INSERT INTO loyalty_kartice (broj_poena, korisnik_id, odobrena) VALUES (?, ?, ?)";

				PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				int index = 1;
				preparedStatement.setInt(index++, loyaltyKartica.getBrojPoena());
				preparedStatement.setLong(index++, loyaltyKartica.getKorisnik().getId());
				preparedStatement.setBoolean(index++, loyaltyKartica.isOdobrena());

				System.out.println(preparedStatement);
				return preparedStatement;
			}

		};
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		boolean uspeh = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;
		return uspeh ? 1 : 0;
	}

	@Override
	public List<LoyaltyKartica> findAll() {
			String sql = "SELECT * FROM loyalty_kartice  " + "ORDER BY id";

			LoyaltyKarticaRowCallBackHandler rowCallbackHandler = new LoyaltyKarticaRowCallBackHandler();
			jdbcTemplate.query(sql, rowCallbackHandler);

			return rowCallbackHandler.getLoyaltyKartice();
		

	}
	
	@Override
	public void acceptKartica(Long karticaId) {
		String sql = "UPDATE loyalty_kartice SET  odobrena = TRUE " + "WHERE id = ?";
		jdbcTemplate.update(sql, karticaId);

	}
	
	@Override
	public void rejectKartica(Long karticaId) {
		String sql = "DELETE FROM loyalty_kartice "+ "WHERE id = ?";
		jdbcTemplate.update(sql, karticaId);

	}
	
	@Override
	public boolean updateBrojPoena(LoyaltyKartica loyaltyKartica, int brojPoena) {
		String sql = "UPDATE loyalty_kartice SET broj_poena = ? WHERE id = ?";

		int affectedRows = jdbcTemplate.update(sql, brojPoena, loyaltyKartica.getId());

		return affectedRows > 0;
	}

}
