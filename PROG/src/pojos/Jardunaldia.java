package pojos;

import java.util.Date;

public class Jardunaldia {
	private int jardunaldiKod;
	private Date hasieraData;
	private Date amaieraData;

	public Jardunaldia() {
	}

	public Jardunaldia(int jardunaldiKod, Date hasieraData, Date amaieraData) {
		this.jardunaldiKod = jardunaldiKod;
		this.hasieraData = hasieraData;
		this.amaieraData = amaieraData;
	}

	public int getJardunaldiKod() {
		return jardunaldiKod;
	}

	public void setJardunaldiKod(int jardunaldiKod) {
		this.jardunaldiKod = jardunaldiKod;
	}

	public Date getHasieraData() {
		return hasieraData;
	}

	public void setHasieraData(Date hasieraData) {
		this.hasieraData = hasieraData;
	}

	public Date getAmaieraData() {
		return amaieraData;
	}

	public void setAmaieraData(Date amaieraData) {
		this.amaieraData = amaieraData;
	}

	@Override
	public String toString() {
		return "Jardunaldia " + jardunaldiKod;
	}
}