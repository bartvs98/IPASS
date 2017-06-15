package nl.hu.ipass.model;

import java.util.ArrayList;
import java.util.List;

public class Rekening {
	private int userID;
	private String rekeningNr;
	private String depotNaam;
	private double saldo;
	private List<Bijafschrift> bijafschrift = new ArrayList<Bijafschrift>();
	private List<Belegging> belegging = new ArrayList<Belegging>();
	
	public Rekening(String rekeningNr, double saldo) {
		super();
		this.rekeningNr = rekeningNr;
		this.saldo = saldo;
	}

	public Rekening(int userID, String rekeningNr, String depotNaam, double saldo) {
		super();
		this.userID = userID;
		this.rekeningNr = rekeningNr;
		this.depotNaam = depotNaam;
		this.saldo = saldo;
	}

	public Rekening(int userID, String rekeningNr, String depotNaam, double saldo, List<Bijafschrift> bijafschrift,
			List<Belegging> belegging) {
		super();
		this.userID = userID;
		this.rekeningNr = rekeningNr;
		this.depotNaam = depotNaam;
		this.saldo = saldo;
		this.bijafschrift = bijafschrift;
		this.belegging = belegging;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getRekeningNr() {
		return rekeningNr;
	}

	public void setRekeningNr(String rekeningNr) {
		this.rekeningNr = rekeningNr;
	}

	public String getDepotNaam() {
		return depotNaam;
	}

	public void setDepotNaam(String depotNaam) {
		this.depotNaam = depotNaam;
	}

	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}

	public List<Bijafschrift> getBijafschrift() {
		return bijafschrift;
	}

	public void setBijafschrift(List<Bijafschrift> bijafschrift) {
		this.bijafschrift = bijafschrift;
	}

	public List<Belegging> getBelegging() {
		return belegging;
	}

	public void setBelegging(List<Belegging> belegging) {
		this.belegging = belegging;
	}

}
