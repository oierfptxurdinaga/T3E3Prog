package pojos;

public class Epailea {
	private int epaileaKod;
	private String izena;
	private String abizena;
	private String nan;
	private String herritartasuna;

	public Epailea() {
	}

	public Epailea(int epaileaKod, String izena, String abizena, String nan, String herritartasuna) {
		this.epaileaKod = epaileaKod;
		this.izena = izena;
		this.abizena = abizena;
		this.nan = nan;
		this.herritartasuna = herritartasuna;
	}

	public int getEpaileaKod() {
		return epaileaKod;
	}

	public void setEpaileaKod(int epaileaKod) {
		this.epaileaKod = epaileaKod;
	}

	public String getIzena() {
		return izena;
	}

	public void setIzena(String izena) {
		this.izena = izena;
	}

	public String getAbizena() {
		return abizena;
	}

	public void setAbizena(String abizena) {
		this.abizena = abizena;
	}

	public String getNan() {
		return nan;
	}

	public void setNan(String nan) {
		this.nan = nan;
	}

	public String getHerritartasuna() {
		return herritartasuna;
	}

	public void setHerritartasuna(String herritartasuna) {
		this.herritartasuna = herritartasuna;
	}

}