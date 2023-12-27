package com.ftn.TravelOrganisation.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.ftn.TravelOrganisation.model.Destinacija;
import com.ftn.TravelOrganisation.model.KategorijaPutovanjaEnum;
import com.ftn.TravelOrganisation.model.PrevoznoSredstvo;
import com.ftn.TravelOrganisation.model.PrevoznoSredstvoTipEnum;
import com.ftn.TravelOrganisation.model.Putovanje;
import com.ftn.TravelOrganisation.repository.DestinacijaRepository;
import com.ftn.TravelOrganisation.repository.PutovanjeRepository;

@Repository
public class PutovanjeRepositoryImpl implements PutovanjeRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DestinacijaRepository destinacijaRepository;

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
            String vremePolaskaStr = resultSet.getString(index++);
            String vremePovratkaStr = resultSet.getString(index++);
            String brojNocenja = resultSet.getString(index++);
            String cenaAranzmana = resultSet.getString(index++);

            Destinacija destinacija = destinacijaId != null ? destinacijaRepository.findOne(destinacijaId) : null;
            PrevoznoSredstvo prevoznoSredstvo = new PrevoznoSredstvo((long) 6, 5, destinacija, "aa", PrevoznoSredstvoTipEnum.AUTOBUS);
            KategorijaPutovanjaEnum kategorijaPutovanja = KategorijaPutovanjaEnum.LETOVANJE;

           


         //   System.out.println(id + " " + destinacijaId + " " + prevoznoSredstvoId + " " + kategorijaPutovanjaStr + " "
         //           + vremePolaskaStr + " " + vremePovratkaStr + " " + brojNocenja + " " + cenaAranzmana);

            Putovanje putovanje = new Putovanje(id, destinacija, prevoznoSredstvo, kategorijaPutovanja, vremePolaskaStr,
                    vremePovratkaStr, brojNocenja, cenaAranzmana);

            // Dodajte putovanje u mapu putovanja sa proverom null vrednosti
            if (id != null && putovanje != null) {
                putovanja.put(id, putovanje);
            }
        }

        public List<Putovanje> getPutovanja() {
  
            return new ArrayList<>(putovanja.values());
        }

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
