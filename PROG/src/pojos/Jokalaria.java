package pojos;

import java.math.BigDecimal;

public class Jokalaria {
	private int jokalariakKod;
	private String izena;
	private String abizena;
	private String nan;
	private String posizioa;
	private BigDecimal pisua;
	private BigDecimal altuera;
	private String herritartasuna;
	private Taldea taldea;

	public Jokalaria() {
	}

	public Jokalaria(int jokalariakKod, String izena, String abizena, String nan, String posizioa, BigDecimal pisua,
			BigDecimal altuera, String herritartasuna, Taldea taldea) {
		this.jokalariakKod = jokalariakKod;
		this.izena = izena;
		this.abizena = abizena;
		this.nan = nan;
		this.posizioa = posizioa;
		this.pisua = pisua;
		this.altuera = altuera;
		this.herritartasuna = herritartasuna;
		this.taldea = taldea;
	}

	public int getJokalariakKod() {
		return jokalariakKod;
	}

	public void setJokalariakKod(int jokalariakKod) {
		this.jokalariakKod = jokalariakKod;
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

	public String getPosizioa() {
		return posizioa;
	}

	public void setPosizioa(String posizioa) {
		this.posizioa = posizioa;
	}

	public BigDecimal getPisua() {
		return pisua;
	}

	public void setPisua(BigDecimal pisua) {
		this.pisua = pisua;
	}

	public BigDecimal getAltuera() {
		return altuera;
	}

	public void setAltuera(BigDecimal altuera) {
		this.altuera = altuera;
	}

	public String getHerritartasuna() {
		return herritartasuna;
	}

	public void setHerritartasuna(String herritartasuna) {
		this.herritartasuna = herritartasuna;
	}

	public Taldea getTaldea() {
		return taldea;
	}

	public void setTaldea(Taldea taldea) {
		this.taldea = taldea;
	}

	@Override
	public String toString() {
		return izena + " " + abizena + " (" + posizioa + ")";
	}
}