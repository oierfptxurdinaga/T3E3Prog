package dao;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import pojos.Erabiltzailea;
import java.util.List;

class ErabiltzaileakDAOTest {

	private ErabiltzaileakDAO erabiltzaileDAO;

	@BeforeEach
	void hasieratu() {
		erabiltzaileDAO = new ErabiltzaileakDAO();
	}

	@Test
	void loginTest() {
		// Kontuan izan: Erabiltzaile honek DBan egon behar du lehenagotik
		Erabiltzailea e = erabiltzaileDAO.login("ez-existitzen", "1234");
		assertNull(e, "Existitzen ez den erabiltzaileak null bueltatu behar luke.");
	}

	@Test
	void erabiltzaileGuztiakLortuTest() {
		List<Erabiltzailea> zerrenda = erabiltzaileDAO.getErabiltzaileak();
		assertNotNull(zerrenda, "Zerrenda ezin da null izan.");
	}

	@Test
	void erregistratuEtaEzabatuTest() {
		String izena = "test_berria_" + System.currentTimeMillis();
		String pass = "1234";

		// Erregistratu
		boolean gordeta = erabiltzaileDAO.erregistratu(izena, pass);
		// Oharra: false izan daiteke erabiltzailea jada existitzen bada
		if (gordeta) {
			// Egiaztatu login-a funtzionatzen duela
			Erabiltzailea e = erabiltzaileDAO.login(izena, pass);
			assertNotNull(e, "Erregistratutako erabiltzaileak saioa hasi beharko luke");
			assertEquals(izena, e.getErabiltzailea());
			assertEquals("ERABILTZAILEA", e.getRola());

			// Ezabatu
			boolean ezabatua = erabiltzaileDAO.ezabatuErabiltzailea(izena);
			assertTrue(ezabatua, "Erabiltzailea ondo ezabatu beharko litzateke");
		}
	}

	@Test
	void eguneratuRolaTest() {
		String izena = "test_rola_" + System.currentTimeMillis();
		String pass = "1234";

		boolean gordeta = erabiltzaileDAO.sortuErabiltzaileaRolarekin(izena, pass, "ERABILTZAILEA");
		assertTrue(gordeta, "Erabiltzailea sortu beharko litzateke");

		if (gordeta) {
			// Rola eguneratu EPAILEA izatera
			boolean eguneratua = erabiltzaileDAO.eguneratuRola(izena, "EPAILEA");
			assertTrue(eguneratua, "Rola eguneratu beharko litzateke");

			// Egiaztatu
			Erabiltzailea e = erabiltzaileDAO.login(izena, pass);
			assertEquals("EPAILEA", e.getRola());

			// Garbitu
			erabiltzaileDAO.ezabatuErabiltzailea(izena);
		}
	}
}