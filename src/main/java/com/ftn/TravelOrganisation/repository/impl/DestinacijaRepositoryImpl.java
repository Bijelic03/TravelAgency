package com.ftn.TravelOrganisation.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

import com.ftn.TravelOrganisation.model.Destinacija;
import com.ftn.TravelOrganisation.repository.DestinacijaRepository;

@Repository
public class DestinacijaRepositoryImpl implements DestinacijaRepository {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private class DestinacijaRowCallBackHandler implements RowCallbackHandler {

		private Map<Long, Destinacija> destinacije = new LinkedHashMap<>();

		@Override
		public void processRow(ResultSet resultSet) throws SQLException {
			int index = 1;
			Long id = resultSet.getLong(index++);
			String grad = resultSet.getString(index++);
			String drzava = resultSet.getString(index++);
			String kontinent = resultSet.getString(index++);

			Destinacija destinacija = destinacije.get(id);
			if (destinacija == null) {
				destinacija = new Destinacija(id, grad, drzava, kontinent);
				destinacije.put(destinacija.getId(), destinacija); // dodavanje u kolekciju
			}
		}

		public List<Destinacija> getDestinacije() {
			return new ArrayList<>(destinacije.values());
		}

	}
	
	@Override
	public Destinacija findOne(Long id) {
		String sql = "SELECT d.id, d.grad, d.drzava, d.kontinent FROM destinacije d " + "WHERE d.id = ? "
				+ "ORDER BY d.id";

		DestinacijaRowCallBackHandler rowCallbackHandler = new DestinacijaRowCallBackHandler();
		jdbcTemplate.query(sql, rowCallbackHandler, id);

		return rowCallbackHandler.getDestinacije().get(0);
	}

	@Override
	public List<Destinacija> findAll() {
		String sql = "SELECT d.id, d.grad, d.drzava, d.kontinent FROM destinacije d  " + "ORDER BY d.id";

		DestinacijaRowCallBackHandler rowCallbackHandler = new DestinacijaRowCallBackHandler();
		jdbcTemplate.query(sql, rowCallbackHandler);

		return rowCallbackHandler.getDestinacije();
	}

	@Transactional
	@Override
	public int save(Destinacija destinacija) {
		PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				String sql = "INSERT INTO destinacije (grad, drzava, kontinent) VALUES (?, ?, ?)";

				PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				int index = 1;
				preparedStatement.setString(index++, destinacija.getGrad());
				preparedStatement.setString(index++, destinacija.getDrzava());
				preparedStatement.setString(index++, destinacija.getKontinent());

				return preparedStatement;
			}

		};
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		boolean uspeh = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;
		return uspeh ? 1 : 0;
	}

	@Transactional
	@Override
	public int update(Destinacija destinacija) {
		String sql = "UPDATE destinacije SET grad = ?, drzava = ?, kontinent = ? WHERE id = ?";
		boolean uspeh = jdbcTemplate.update(sql, destinacija.getGrad(), destinacija.getDrzava(),
				destinacija.getKontinent(), destinacija.getId()) == 1;

		return uspeh ? 1 : 0;
	}

	@Transactional
	@Override
	public int delete(Long id) {
		String sql = "DELETE FROM destinacije WHERE id = ?";
		return jdbcTemplate.update(sql, id);
	}

	@Override
	public Destinacija findByGrad(String nazivDestinacije) {
		String sql = "SELECT d.id, d.grad, d.drzava, d.kontinent FROM destinacije d " + "WHERE d.grad = ? "
				+ "ORDER BY d.id";

		DestinacijaRowCallBackHandler rowCallbackHandler = new DestinacijaRowCallBackHandler();
		jdbcTemplate.query(sql, rowCallbackHandler, nazivDestinacije);

		return rowCallbackHandler.getDestinacije().get(0);
	}

}
