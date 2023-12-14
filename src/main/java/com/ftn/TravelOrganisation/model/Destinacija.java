package com.ftn.TravelOrganisation.model;


public class Destinacija {

	private Long id;
	private String grad;
	private String drzava;
	private String kontinent;

	public Destinacija() {

	}

	public Destinacija(Long id, String grad, String drzava, String kontinent) {
		super();
		this.id = id;
		this.grad = grad;
		this.drzava = drzava;
		this.kontinent = kontinent;

	}

	@Override
	public String toString() {
		return id + ";" + grad + ";" + drzava + ";" + kontinent;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGrad() {
		return grad;
	}

	public void setGrad(String grad) {
		this.grad = grad;
	}

	public String getDrzava() {
		return drzava;
	}

	public void setDrzava(String drzava) {
		this.drzava = drzava;
	}

	public String getKontinent() {
		return kontinent;
	}

	public void setKontinent(String kontinent) {
		this.kontinent = kontinent;
	}

}
