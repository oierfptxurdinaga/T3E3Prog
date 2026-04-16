package pojos;

import java.util.Date;

public class Partida {
	private int partidaKod;
	private Date data;
	private String ordua;
	private String emaitza;
	private int zigorrak;
	private int txartelak;
	private Epailea epailea;

	private Taldea etxekoTaldea;
	private Taldea kanpokoTaldea;
	private Jardunaldia jardunaldia;

	public Partida() {
	}

	// getters y setters...
	public int getPartidaKod() {
		return partidaKod;
	}

	public void setPartidaKod(int partidaKod) {
		this.partidaKod = partidaKod;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getOrdua() {
		return ordua;
	}

	public void setOrdua(String ordua) {
		this.ordua = ordua;
	}

	public String getEmaitza() {
		return emaitza;
	}

	public void setEmaitza(String emaitza) {
		this.emaitza = emaitza;
	}

	public int getZigorrak() {
		return zigorrak;
	}

	public void setZigorrak(int zigorrak) {
		this.zigorrak = zigorrak;
	}

	public int getTxartelak() {
		return txartelak;
	}

	public void setTxartelak(int txartelak) {
		this.txartelak = txartelak;
	}

	public Epailea getEpailea() {
		return epailea;
	}

	public void setEpailea(Epailea epailea) {
		this.epailea = epailea;
	}

	public Taldea getEtxekoTaldea() {
		return etxekoTaldea;
	}

	public void setEtxekoTaldea(Taldea etxekoTaldea) {
		this.etxekoTaldea = etxekoTaldea;
	}

	public Taldea getKanpokoTaldea() {
		return kanpokoTaldea;
	}

	public void setKanpokoTaldea(Taldea kanpokoTaldea) {
		this.kanpokoTaldea = kanpokoTaldea;
	}

	public Jardunaldia getJardunaldia() {
		return jardunaldia;
	}

	public void setJardunaldia(Jardunaldia jardunaldia) {
		this.jardunaldia = jardunaldia;
	}

	@Override
	public String toString() {
		return etxekoTaldea + " vs " + kanpokoTaldea + " (" + emaitza + ")";
	}
}