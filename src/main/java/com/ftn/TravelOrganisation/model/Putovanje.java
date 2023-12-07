package com.ftn.TravelOrganisation.model;

import java.time.LocalDateTime;

public class Putovanje {

	private Long id;
	private Long sifraPutovanja;
	private Destinacija destinacija;
	private PrevoznoSredstvo prevoznoSredstvo;
	private SmestajnaJedinica smestajnaJedinica;
	private KategorijaPutovanjaEnum kategorijaPutovanja;
	private LocalDateTime vremePolaska;
	private LocalDateTime vremePovratka;
	private int brojNocenja;
	private Double cenaAranzmana;
	
	
	
}
