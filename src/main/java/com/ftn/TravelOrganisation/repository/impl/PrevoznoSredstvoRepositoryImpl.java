package com.ftn.TravelOrganisation.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.ftn.TravelOgranisation.util.DateUtil;
import com.ftn.TravelOrganisation.model.Destinacija;
import com.ftn.TravelOrganisation.model.PrevoznoSredstvo;
import com.ftn.TravelOrganisation.model.PrevoznoSredstvoTipEnum;
import com.ftn.TravelOrganisation.model.Putovanje;
import com.ftn.TravelOrganisation.model.PrevoznoSredstvo;
import com.ftn.TravelOrganisation.model.SmestajnaJedinica;
import com.ftn.TravelOrganisation.repository.DestinacijaRepository;
import com.ftn.TravelOrganisation.repository.PrevoznoSredstvoRepository;
import com.ftn.TravelOrganisation.repository.impl.PutovanjeRepositoryImpl.PutovanjeRowCallBackHandler;

@Repository
public class PrevoznoSredstvoRepositoryImpl implements PrevoznoSredstvoRepository {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Lazy
	@Autowired
	private DestinacijaRepository destinacijaRepository;

	public class PrevoznoSredstvoRowCallBackHandler implements RowCallbackHandler {

		private Map<Long, PrevoznoSredstvo> prevoznaSredstva = new LinkedHashMap<>();

		@Override
		public void processRow(ResultSet resultSet) throws SQLException {
			int index = 1;
			Long id = resultSet.getLong(index++);
			int brojSedista = resultSet.getInt(index++);
			Long destinacijaId = resultSet.getLong(index++);
			String opis = resultSet.getString(index++);
			String prevoznoSredstvoStr = resultSet.getString(index++);

			PrevoznoSredstvoTipEnum prevoznoSredstvoTipEnum = PrevoznoSredstvoTipEnum.valueOf(prevoznoSredstvoStr);

			Destinacija destinacija = destinacijaId != null ? destinacijaRepository.findOne(destinacijaId) : null;

			PrevoznoSredstvo PrevoznoSredstvo = new PrevoznoSredstvo(id, brojSedista, destinacija, opis,
					prevoznoSredstvoTipEnum);

			// Dodajte PrevoznoSredstvo u mapu prevoznaSredstva sa proverom null vrednosti
			if (id != null && PrevoznoSredstvo != null) {
				prevoznaSredstva.put(id, PrevoznoSredstvo);
			}
		}

		public List<PrevoznoSredstvo> getprevoznaSredstva() {

			return new ArrayList<>(prevoznaSredstva.values());
		}

	}

	@Override
	public PrevoznoSredstvo findOne(Long id) {
		String sql = "SELECT * FROM prevozna_sredstva  " + "WHERE id = ? " + "ORDER BY id";

		PrevoznoSredstvoRowCallBackHandler rowCallbackHandler = new PrevoznoSredstvoRowCallBackHandler();
		jdbcTemplate.query(sql, rowCallbackHandler, id);

		return rowCallbackHandler.getprevoznaSredstva().get(0);

	}

	@Override
	public List<PrevoznoSredstvo> findAll() {
		String sql = "SELECT * FROM putovanja  ORDER BY id";
		PrevoznoSredstvoRowCallBackHandler rowCallbackHandler = new PrevoznoSredstvoRowCallBackHandler();
		jdbcTemplate.query(sql, rowCallbackHandler);

		List<PrevoznoSredstvo> prevoznaSredstva = rowCallbackHandler.getprevoznaSredstva();

		return prevoznaSredstva;

	}

}
