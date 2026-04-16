package pojos;

public class TaldearenKlasifikazioa {
	private Taldea taldea;
	private int partidaJokatuak;
	private int partidaIrabaziak;
	private int partidaGalduak;
	private int puntuak;
	private int setakIrabaziak;
	private int setakGalduak;
	private int setDiferentzia;

	public TaldearenKlasifikazioa(Taldea taldea) {
		this.taldea = taldea;
	}

	public Taldea getTaldea() {
		return taldea;
	}

	public int getPartidaJokatuak() {
		return partidaJokatuak;
	}

	public int getPartidaIrabaziak() {
		return partidaIrabaziak;
	}

	public int getPartidaGalduak() {
		return partidaGalduak;
	}

	public int getPuntuak() {
		return puntuak;
	}

	public int getSetakIrabaziak() {
		return setakIrabaziak;
	}

	public int getSetakGalduak() {
		return setakGalduak;
	}

	public int getSetDiferentzia() {
		return setDiferentzia;
	}

	public void gehitupartidaJokatua() {
		partidaJokatuak++;
	}

	public void gehitupartidaIrabazia() {
		partidaIrabaziak++;
		puntuak += 3;
	}

	public void gehitupartidaGaldua() {
		partidaGalduak++;
	}

	public void gehituSetakIrabaziak(int s) {
		setakIrabaziak += s;
		setDiferentzia = setakIrabaziak - setakGalduak;
	}

	public void gehituSetakGalduak(int s) {
		setakGalduak += s;
		setDiferentzia = setakIrabaziak - setakGalduak;
	}
}