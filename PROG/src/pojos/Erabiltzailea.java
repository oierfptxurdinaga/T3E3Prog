package pojos;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Erabiltzailea entitatea. Saioa hasteko erabiltzen da. Rolak: ADMIN, EPAILEA,
 * ERABILTZAILEA
 */
@Entity
public class Erabiltzailea implements Serializable {

	@Id
	@GeneratedValue
	private Long id;

	private String erabiltzailea;
	private String pasahitza;
	private String rola; // ADMIN, EPAILEA, ERABILTZAILEA

	public Erabiltzailea() {
	}

	public Erabiltzailea(String erabiltzailea, String pasahitza, String rola) {
		this.erabiltzailea = erabiltzailea;
		this.pasahitza = pasahitza;
		this.rola = rola;
	}

	public Long getId() {
		return id;
	}

	public String getErabiltzailea() {
		return erabiltzailea;
	}

	public void setErabiltzailea(String erabiltzailea) {
		this.erabiltzailea = erabiltzailea;
	}

	public String getPasahitza() {
		return pasahitza;
	}

	public void setPasahitza(String pasahitza) {
		this.pasahitza = pasahitza;
	}

	public String getRola() {
		return rola;
	}

	public void setRola(String rola) {
		this.rola = rola;
	}

	/**
	 * @return true ADMIN bada.
	 */
	public boolean isAdmin() {
		return "ADMIN".equals(rola);
	}

	/**
	 * @return true EPAILEA bada.
	 */
	public boolean isEpailea() {
		return "EPAILEA".equals(rola);
	}

	/**
	 * @return true partidak sartzeko baimena badu (ADMIN edo EPAILEA).
	 */
	public boolean partidakSartuDezake() {
		return isAdmin() || isEpailea();
	}

	@Override
	public String toString() {
		return "Erabiltzaileak [erabiltzailea=" + erabiltzailea + ", rola=" + rola + "]";
	}
}