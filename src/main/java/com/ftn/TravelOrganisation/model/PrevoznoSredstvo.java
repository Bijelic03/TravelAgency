package com.ftn.TravelOrganisation.model;

//@Entity
public class PrevoznoSredstvo {

	//@Id
	private Long id;
	private int brojSedista;

	// za ovo nisam siguran
	//@OneToOne(mappedBy = "prevoznoSredstvo")
	private Destinacija krajnjaDestinacija;
	private String opis;
	private PrevoznoSredstvoTipEnum prevoznoSredstvo;



	public PrevoznoSredstvo(Long id, int brojSedista, Destinacija krajnjaDestinacija, String opis,
			PrevoznoSredstvoTipEnum prevoznoSredstvo) {
		super();
		this.id = id;
		this.brojSedista = brojSedista;
		this.krajnjaDestinacija = krajnjaDestinacija;
		this.opis = opis;
		this.prevoznoSredstvo = prevoznoSredstvo;
	}
	
	public PrevoznoSredstvo( int brojSedista, Destinacija krajnjaDestinacija, String opis,
			PrevoznoSredstvoTipEnum prevoznoSredstvo) {
		super();
		this.brojSedista = brojSedista;
		this.krajnjaDestinacija = krajnjaDestinacija;
		this.opis = opis;
		this.prevoznoSredstvo = prevoznoSredstvo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getBrojSedista() {
		return brojSedista;
	}

	public void setBrojSedista(int brojSedista) {
		this.brojSedista = brojSedista;
	}

	public Destinacija getKrajnjaDestinacija() {
		return krajnjaDestinacija;
	}

	public void setKrajnjaDestinacija(Destinacija krajnjaDestinacija) {
		this.krajnjaDestinacija = krajnjaDestinacija;
	}

	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	public PrevoznoSredstvoTipEnum getPrevoznoSredstvo() {
		return prevoznoSredstvo;
	}

	public void setPrevoznoSredstvo(PrevoznoSredstvoTipEnum prevoznoSredstvo) {
		this.prevoznoSredstvo = prevoznoSredstvo;
	}

}
