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

import com.ftn.TravelOrganisation.model.Korisnik;
import com.ftn.TravelOrganisation.model.Putovanje;
import com.ftn.TravelOrganisation.model.WishlistItem;
import com.ftn.TravelOrganisation.repository.KorisnikRepository;
import com.ftn.TravelOrganisation.repository.PutovanjeRepository;
import com.ftn.TravelOrganisation.repository.WishlistRepository;

@Repository
public class WishlistRepositoryImpl implements WishlistRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private PutovanjeRepository putovanjeRepository;

	private KorisnikRepository korisnikRepository;

	public WishlistRepositoryImpl(PutovanjeRepository putovanjeRepository, KorisnikRepository korisnikRepository) {
		this.putovanjeRepository = putovanjeRepository;
		this.korisnikRepository = korisnikRepository;
	}

	private class WishlistRowCallBackHandler implements RowCallbackHandler {
		private Map<Long, WishlistItem> wishlist = new LinkedHashMap<>();

		@Override
		public void processRow(ResultSet resultSet) throws SQLException {
			int index = 1;
			Long id = resultSet.getLong(index++);
			Long korisnikId = resultSet.getLong(index++);
			Long putovanjeId = resultSet.getLong(index++);

			Putovanje putovanje = putovanjeRepository.findOne(putovanjeId);
			Korisnik korisnik = korisnikRepository.findOne(korisnikId);

			WishlistItem wishlistItem = wishlist.get(id);
			if (wishlistItem == null) {
				wishlistItem = new WishlistItem(id, korisnik, putovanje);
				wishlist.put(wishlistItem.getId(), wishlistItem);
			}
		}

		public List<WishlistItem> getWishlist() {
			return new ArrayList<>(wishlist.values());
		}
	}

	@Override
	public List<WishlistItem> findAllByKorisnik(Long korisnikId) {
		String sql = "SELECT * FROM wishlist where korisnik_id = ? ORDER BY id ";

		WishlistRowCallBackHandler rowCallbackHandler = new WishlistRowCallBackHandler();
		jdbcTemplate.query(sql, rowCallbackHandler, korisnikId);

		return rowCallbackHandler.getWishlist();
	}

	@Transactional
	@Override
	public int save(WishlistItem wishlistItem) {
		PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				String sql = "INSERT INTO wishlist (korisnik_id, putovanje_id) VALUES (?, ?)";

				PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				int index = 1;
				preparedStatement.setLong(index++, wishlistItem.getKorisnik().getId());
				preparedStatement.setLong(index++, wishlistItem.getPutovanje().getId());

				return preparedStatement;
			}

		};
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		boolean success = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;
		return success ? 1 : 0;
	}

	@Override
	public void remove(Long idWishlist) {
	    String sql = "DELETE FROM wishlist WHERE id = ?";
	    jdbcTemplate.update(sql, idWishlist);
	}


}
