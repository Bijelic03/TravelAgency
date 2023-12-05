package com.ftn.TravelOrganisation.service.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.ftn.TravelOrganisation.bean.SecondConfiguration.ApplicationMemory;
import com.ftn.TravelOrganisation.controller.DestinacijeController;
import com.ftn.TravelOrganisation.model.Destinacija;
import com.ftn.TravelOrganisation.service.DestinacijeService;



@Service
public class DestinacijeServiceImpl implements DestinacijeService {

	@Value("${destinacije.pathToFile}")
	private String pathToFile;
	
	
	public static final String DESTINACIJE_KEY = "destinacije";
	
		



		

	
    private Map<Long, Destinacija> readDestinacijeFromTxt() {

    	Map<Long, Destinacija> destinacije = new HashMap<>();
    	Long nextId = 1L;
    	
    	try {
			Path path = Paths.get(pathToFile);
			System.out.println(path.toFile().getAbsolutePath());
			List<String> lines = Files.readAllLines(path, Charset.forName("UTF-8"));

			for (String line : lines) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				
				   String[] tokens = line.split(";");
	                Long id = Long.parseLong(tokens[0]);
	                String grad = tokens[1];
	                String drzava = tokens[2];
	                String kontinent = tokens[3];
				
				destinacije.put(id, new Destinacija(id, grad, drzava, kontinent));

				if(nextId<id)
					nextId=id;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return destinacije;
    }
    
	
     private Map<Long, Destinacija> saveToFile(Map<Long, Destinacija> destinacije) {
    	
    	Map<Long, Destinacija> ret = new HashMap<>();
    	try {
    		
			Path path = Paths.get(pathToFile);
			System.out.println(path.toFile().getAbsolutePath());
			List<String> lines = new ArrayList<>();
			
			for (Destinacija destinacija : destinacije.values()) {

				lines.add(destinacija.toString());
				ret.put(destinacija.getId(), destinacija);
			}
			Files.write(path, lines, Charset.forName("UTF-8"));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return ret;
    }
    

    
	@Override
	public Destinacija update(Destinacija destinacija){
		Map<Long, Destinacija> destinacije = readDestinacijeFromTxt();	
		
		destinacije.put(destinacija.getId(), destinacija);
		saveToFile(destinacije);
		return destinacija;
	}
	

	
	@Override
	public List<Destinacija> findAll() {
		Map<Long, Destinacija> destinacije = readDestinacijeFromTxt();	
		return new ArrayList<>(destinacije.values());
	}

    @Override
	   public Destinacija returnOne(Long id){
		Map<Long, Destinacija> destinacije = readDestinacijeFromTxt();	

		   return destinacije.get(id);
	   }
	   
	@Override
	public Destinacija save(Destinacija destinacija) {
		Map<Long, Destinacija> destinacije = readDestinacijeFromTxt();	
		Long nextId = nextId(destinacije); 
		
		//u slučaju da knjiga nema id
		//tada treba da se dodeli id
		if (destinacija.getId() == null) {
			destinacija.setId(nextId++);
			
		}
		destinacije.put(destinacija.getId(), destinacija);
		saveToFile(destinacije);
		return destinacija;
	}
	
    private Long nextId(Map<Long, Destinacija> destinacije) {
    	Long nextId = 1L;
    	for (Long id : destinacije.keySet()) {
    		if(nextId<id)
				nextId=id;
		}
    	return ++nextId;
    }
	   
	   public List<Destinacija> findByGrad(String grad) {
		   Map<Long, Destinacija> destinacije = readDestinacijeFromTxt();
	       List<Destinacija> ret = null;

	       for (Destinacija d : destinacije.values()) {
	           if (d.getGrad().startsWith(grad)) {
	               ret.add(d);
	           }
	       }

	       return ret;
	   }
	   
	   @Override
		public Destinacija delete(Long id) {
			Map<Long, Destinacija> destinacije = readDestinacijeFromTxt();	
			
			if (!destinacije.containsKey(id)) {
				throw new IllegalArgumentException("tried to remove non existing book");
			}
			
			Destinacija destinacija = destinacije.get(id);
			if (destinacija != null) {
				destinacije.remove(id);
			}
			saveToFile(destinacije);
			return destinacija;
		}



}
