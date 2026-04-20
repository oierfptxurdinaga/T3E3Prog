package dao;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import pojos.Jokalaria;
import util.LoggerUtil.DataAccessException;
import java.math.BigDecimal;
import java.util.List;

class JokalariaDAOTest {

	private JokalariaDAO jokalariaDAO;

	@BeforeEach
	void setup() {
		jokalariaDAO = new JokalariaDAO();
	}

	@Test
	void jokalariGuztiakLortuTest() throws DataAccessException {
		List<Jokalaria> zerrenda = jokalariaDAO.jokalariGuztiakLortu();
		assertNotNull(zerrenda);
	}

	@Test
	void jokalariaSortuEtaEzabatuTest() throws DataAccessException {
		Jokalaria j = new Jokalaria();
		j.setIzena("Test");
		j.setAbizena("User");
		j.setNan("99999999X");
		j.setPosizioa("Aurrelaria");
		j.setPisua(new BigDecimal("75.5"));
		j.setAltuera(new BigDecimal("1.80"));

		// Sortu (void metodoa da)
		jokalariaDAO.jokalariaSortu(j);
		assertTrue(j.getJokalariakKod() > 0, "IDa esleitu behar zitzaion sortu ondoren");

		// Ezabatu (void metodoa da)
		jokalariaDAO.jokalariaEzabatu(j.getJokalariakKod());

		// Egiaztatu ezabatu dela (ez luke aurkitu behar)
		Jokalaria ezabatua = jokalariaDAO.jokalariaLortuIdBidez(j.getJokalariakKod());
		assertNull(ezabatua, "Jokalaria ez litzateke existitu behar ezabatu ondoren");
	}
}