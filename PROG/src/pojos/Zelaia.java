package pojos;

public class Zelaia {
	private int zelaiaKod;
	private String izena;
	private String kokapena;
	private int kapazitatea;

	public Zelaia() {
	}

	public Zelaia(int zelaiaKod, String izena, String kokapena, int kapazitatea) {
		this.zelaiaKod = zelaiaKod;
		this.izena = izena;
		this.kokapena = kokapena;
		this.kapazitatea = kapazitatea;
	}

	public int getZelaiaKod() {
		return zelaiaKod;
	}

	public void setZelaiaKod(int zelaiaKod) {
		this.zelaiaKod = zelaiaKod;
	}

	public String getIzena() {
		return izena;
	}

	public void setIzena(String izena) {
		this.izena = izena;
	}

	public String getKokapena() {
		return kokapena;
	}

	public void setKokapena(String kokapena) {
		this.kokapena = kokapena;
	}

	public int getKapazitatea() {
		return kapazitatea;
	}

	public void setKapazitatea(int kapazitatea) {
		this.kapazitatea = kapazitatea;
	}

	@Override
	public String toString() {
		return izena + " (" + kokapena + ")";
	}
}