package dao;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import pojos.Denboraldia;
import util.LoggerUtil.DataAccessException;
import java.util.List;
import java.util.Date;
import java.util.Calendar;

class DenboraldiaDAOTest {

	private DenboraldiaDAO denboraldiaDAO;

	@BeforeEach
	void setup() {
		denboraldiaDAO = new DenboraldiaDAO();
	}

	@Test
	void denboraldiakAteraTest() throws DataAccessException {
		List<Denboraldia> zerrenda = denboraldiaDAO.denboraldiakAtera();
		assertNotNull(zerrenda, "Zerrenda ezin da null izan.");
	}

	@Test
	void denboraldiaHasiEtaBilatuTest() throws DataAccessException {
		Denboraldia d = new Denboraldia();
		d.setIzena("2025-2026 Test " + System.currentTimeMillis());
		Calendar cal = Calendar.getInstance();
		cal.set(2025, Calendar.SEPTEMBER, 1);
		d.setHasieraData(cal.getTime());
		cal.set(2026, Calendar.JUNE, 30);
		d.setAmaieraData(cal.getTime());

		int id = denboraldiaDAO.denboraldiaHasi(d);
		assertNotEquals(-1, id, "Denboraldia ondo hasi beharko litzateke, IDa ez da -1 izan behar.");
		assertTrue(id > 0, "IDak positiboa izan behar du.");
		assertEquals(id, d.getDenboraldiaKod());

		Denboraldia aurkitua = denboraldiaDAO.denboraldiakAteraId(id);
		assertNotNull(aurkitua);
		assertEquals(d.getIzena(), aurkitua.getIzena());
		assertTrue(aurkitua.isAktiboa(), "Denboraldia berria aktibo egon behar da.");

		// Amaitu denboraldia garbitzeko
		denboraldiaDAO.denboraldiaAmaitu(null);
	}

	@Test
	void denboraldiaAktiboaLortuTest() throws DataAccessException {
		Denboraldia aktiboa = denboraldiaDAO.denboraldiaAktiboaLortu();
		// No se fuerza aserción porque puede no haber temporada activa
		System.out.println("Denboraldi aktiboa: " + (aktiboa != null ? aktiboa.getIzena() : "null"));
	}

	@Test
	void eguneratuTest() throws DataAccessException {
		Denboraldia d = new Denboraldia();
		d.setIzena("Test Eguneratu " + System.currentTimeMillis());
		Calendar cal = Calendar.getInstance();
		cal.set(2025, Calendar.SEPTEMBER, 1);
		d.setHasieraData(cal.getTime());
		cal.set(2026, Calendar.JUNE, 30);
		d.setAmaieraData(cal.getTime());

		int id = denboraldiaDAO.denboraldiaHasi(d);
		if (id == -1) {
			fail("Ezin izan da probarako denboraldia sortu.");
		}

		d.setTxapelduna("Talde Test");
		boolean emaitza = denboraldiaDAO.eguneratu(d);
		assertTrue(emaitza, "Eguneraketak ondo joan beharko luke.");

		Denboraldia eguneratua = denboraldiaDAO.denboraldiakAteraId(id);
		assertEquals("Talde Test", eguneratua.getTxapelduna());

		// Amaitu denboraldia garbitzeko
		denboraldiaDAO.denboraldiaAmaitu(null);
	}
}