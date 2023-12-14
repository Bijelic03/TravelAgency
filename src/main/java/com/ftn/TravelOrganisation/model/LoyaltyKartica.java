package com.ftn.TravelOrganisation.model;



//@Entity
public class LoyaltyKartica {

	//@Id
	private Long id;
	private int popust;
	private int brojPoena;

	//@OneToOne
	//@JoinColumn(name = "korisnik_id")
	private Korisnik korisnik;
}
