package dao;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import pojos.Partida;
import util.LoggerUtil.DataAccessException;
import java.util.List;
import java.util.Date;

class PartidaDAOTest {

	private PartidaDAO partidaDAO;

	@BeforeEach
	void hasieratu() {
		partidaDAO = new PartidaDAO();
	}

	@Test
	void partidaLortuDenboraldiBitartezTest() throws DataAccessException {
		// Ziurtatu datu-basean denboraldiren bat dagoela ID honekin
		List<Partida> partidak = partidaDAO.partidaLortuDenboraldiBitartez(1);
		assertNotNull(partidak, "Zerrenda ezin da null izan.");
	}

	@Test
	void partidaGuztiakTest() throws DataAccessException {
		List<Partida> partidak = partidaDAO.partidaGuztiak();
		assertNotNull(partidak, "Zerrenda ezin da null izan.");
	}

	@Test
	void sortuPartidaTest() throws DataAccessException {
		Partida p = new Partida();
		p.setData(new Date());
		p.setOrdua("18:00");
		p.setEmaitza("0-0");
		p.setZigorrak(0);
		p.setTxartelak(0);

		// Epailea null utziko dugu (DAO-ak onartzen du)
		p.setEpailea(null);

		// Metodoa void da
		partidaDAO.sortuPartida(p);

		assertTrue(p.getPartidaKod() > 0, "Partidaren ID-a autogeneratua izan beharko litzateke.");
	}
}