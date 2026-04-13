package pojos;

import java.util.Date;

public class Denboraldia {
	private int denboraldiaKod;
	private String izena;
	private Date hasieraData;
	private Date amaieraData;
	private boolean amaituta;
	private String txapelduna;

	public Denboraldia() {
	}

	public Denboraldia(int denboraldiaKod, String izena, Date hasieraData, Date amaieraData, boolean amaituta, String txapelduna) {
	    this.denboraldiaKod = denboraldiaKod;
	    this.izena = izena;
	    this.hasieraData = hasieraData;
	    this.amaieraData = amaieraData;
	    this.amaituta = amaituta;
	    this.txapelduna = txapelduna;
	}
	public int getDenboraldiaKod() {
		return denboraldiaKod;
	}

	public void setDenboraldiaKod(int denboraldiaKod) {
		this.denboraldiaKod = denboraldiaKod;
	}

	public String getIzena() {
		return izena;
	}

	public void setIzena(String izena) {
		this.izena = izena;
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
	
	public boolean isAmaituta() {
	    return amaituta;
	}

	public void setAmaituta(boolean amaituta) {
	    this.amaituta = amaituta;
	}

	public String getTxapelduna() {
	    return txapelduna;
	}

	public void setTxapelduna(String txapelduna) {
	    this.txapelduna = txapelduna;
	}

	@Override
	public String toString() {
		return izena;
	}
}