package dao;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import pojos.Zelaia;
import util.LoggerUtil.DataAccessException;
import java.util.List;

class ZelaiaDAOTest {

	@Test
	void zelaiGutziakLortuTest() throws DataAccessException {
		ZelaiaDAO dao = new ZelaiaDAO();
		List<Zelaia> zerrenda = dao.zelaiGutziakLortu();

		assertNotNull(zerrenda, "Zerrenda ezin da null izan.");
		// Datu-basean zerbait badago, egiaztatu lehenengoa ez dela null
		if (!zerrenda.isEmpty()) {
			assertNotNull(zerrenda.get(0).getIzena());
		}
	}
}