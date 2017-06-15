package nl.hu.ipass.model;

public class Bijafschrift {
	private String rekeningNr;
	private String date;
	private double bedrag;

	public Bijafschrift(String date, double bedrag) {
		super();
		this.date = date;
		this.bedrag = bedrag;
	}
	
	public Bijafschrift(String rekeningNr, String date, double bedrag) {
		super();
		this.rekeningNr = rekeningNr;
		this.date = date;
		this.bedrag = bedrag;
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

	public double getBedrag() {
		return bedrag;
	}

	public void setBedrag(double bedrag) {
		this.bedrag = bedrag;
	}
}
