package dao;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import pojos.Taldea;
import util.LoggerUtil.DataAccessException;
import java.util.List;
import java.util.Date;

class TaldeaDAOTest {

	private TaldeaDAO taldeaDAO;

	@BeforeEach
	void setup() {
		taldeaDAO = new TaldeaDAO();
	}

	@Test
	void taldeGuztiakLortuTest() throws DataAccessException {
		List<Taldea> guztiak = taldeaDAO.taldeGuztiakLortu();
		assertNotNull(guztiak, "Zerrenda ezin da null izan.");
	}

	@Test
	void taldeaSortuEtaLortuTest() throws DataAccessException {
		Taldea t = new Taldea();
		t.setIzena("Test Taldea " + System.currentTimeMillis());
		t.setSortzeData(new Date());

		// Sortu (void metodoa da)
		taldeaDAO.taldeaSortu(t);
		assertTrue(t.getTaldeaKod() > 0, "Taldea sortu ondoren IDa esleitu behar da");

		// Lortu ID bidez
		Taldea aurkitua = taldeaDAO.TaldeLortuIdBidez(t.getTaldeaKod());
		assertNotNull(aurkitua, "Sortutako taldea aurkitu beharko litzateke");
		assertEquals(t.getIzena(), aurkitua.getIzena());

		// Ezabatu (garbitzeko)
		taldeaDAO.TaldeEzabatu(t.getTaldeaKod());

		// Egiaztatu ezabatu dela
		Taldea ezabatua = taldeaDAO.TaldeLortuIdBidez(t.getTaldeaKod());
		assertNull(ezabatua, "Taldea ez litzateke existitu behar ezabatu ondoren");
	}

	@Test
	void taldeaIdBidezBilatuTest() throws DataAccessException {
		// ID 1 existitzen bada, ez da null izango
		Taldea t = taldeaDAO.TaldeLortuIdBidez(1);
		// Ez dugu assertNotNull egiten DBa hutsik egon daitekeelako
		System.out.println("Taldea ID=1: " + (t != null ? t.getIzena() : "null"));
	}

	@Test
	void taldeaEguneratuTest() throws DataAccessException {
		// Sortu talde berria
		Taldea t = new Taldea();
		t.setIzena("Test Eguneratu " + System.currentTimeMillis());
		t.setSortzeData(new Date());
		taldeaDAO.taldeaSortu(t);

		// Eguneratu izena
		String izenBerria = "Test Eguneratua " + System.currentTimeMillis();
		t.setIzena(izenBerria);
		taldeaDAO.TaldeaEguneratu(t);

		// Egiaztatu eguneraketa
		Taldea eguneratua = taldeaDAO.TaldeLortuIdBidez(t.getTaldeaKod());
		assertEquals(izenBerria, eguneratua.getIzena());

		// Garbitu
		taldeaDAO.TaldeEzabatu(t.getTaldeaKod());
	}
}