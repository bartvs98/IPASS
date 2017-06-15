package nl.hu.ipass.model;

import java.util.ArrayList;
import java.util.List;

public class Belegging {
	private int id;
	private String rekeningNr;
	private String date;
	private double koers;
	private int aantal;
	private double totaal;
	private String naam;
	private List<Koersverandering> koersverandering = new ArrayList<Koersverandering>();
	
	public Belegging(int id, double totaal, String rekeningNr) {
		super();
		this.id = id;
		this.totaal = totaal;
		this.rekeningNr = rekeningNr;
	}

	public Belegging(int id, String date, double koers, int aantal, double totaal, String naam, List<Koersverandering> koersverandering) {
		super();
		this.id = id;
		this.date = date;
		this.koers = koers;
		this.aantal = aantal;
		this.totaal = totaal;
		this.naam = naam;
		this.koersverandering = koersverandering;
	}
	
	public Belegging(int id, String date, double koers, int aantal, double totaal) {
		super();
		this.id = id;
		this.date = date;
		this.koers = koers;
		this.aantal = aantal;
		this.totaal = totaal;
	}

	public Belegging(String rekeningNr, String date, double koers, int aantal, double totaal, String naam) {
		super();
		this.rekeningNr = rekeningNr;
		this.date = date;
		this.koers = koers;
		this.aantal = aantal;
		this.totaal = totaal;
		this.naam = naam;
	}
	
	public Belegging(int id, String rekeningNr, String date, double koers, int aantal, double totaal, String naam) {
		super();
		this.id = id;
		this.rekeningNr = rekeningNr;
		this.date = date;
		this.koers = koers;
		this.aantal = aantal;
		this.totaal = totaal;
		this.naam = naam;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRekeningNr() {
		return rekeningNr;
	}

	public void setRekeningNr(String rekeningNr) {
		this.rekeningNr = rekeningNr;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public double getKoers() {
		return koers;
	}

	public void setKoers(double koers) {
		this.koers = koers;
	}

	public int getAantal() {
		return aantal;
	}

	public void setAantal(int aantal) {
		this.aantal = aantal;
	}

	public double getTotaal() {
		return totaal;
	}

	public void setTotaal(double totaal) {
		this.totaal = totaal;
	}

	public String getNaam() {
		return naam;
	}

	public void setNaam(String naam) {
		this.naam = naam;
	}

	public List<Koersverandering> getKoersverandering() {
		return koersverandering;
	}

	public void setKoersverandering(List<Koersverandering> koersverandering) {
		this.koersverandering = koersverandering;
	}
	
}
