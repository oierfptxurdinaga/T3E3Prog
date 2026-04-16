package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import pojos.TaldearenKlasifikazioa;
import util.LoggerUtil.DataAccessException;
import java.util.List;

/**
 * Klasifikazioa klasearen unitate-testak. Kontuan izan: Metodo honek DAOak
 * erabiltzen dituenez, ingurune kontrolatu batean exekutatu behar da
 * (datu-basea prest egon behar da).
 */
class KlasifikazioaTest {

	@Test
	void lortuKlasifikazioaDenboraldianTest() throws DataAccessException {
		Klasifikazioa klasifikazioaModel = new Klasifikazioa();

		// Suposatuz 1 ID-a duen denboraldia existitzen dela datu-basean
		int denboraldiaKod = 1;

		List<TaldearenKlasifikazioa> emaitza = klasifikazioaModel.lortuKlasifikazioaDenboraldian(denboraldiaKod);

		// 1. Egiaztatu zerrenda ez dela null
		assertNotNull(emaitza, "Sailkapen zerrenda ezin da null izan.");

		// 2. Egiaztatu zerrenda ordenatuta dagoela (Puntuen arabera)
		if (emaitza.size() >= 2) {
			for (int i = 0; i < emaitza.size() - 1; i++) {
				int puntuak1 = emaitza.get(i).getPuntuak();
				int puntuak2 = emaitza.get(i + 1).getPuntuak();

				assertTrue(puntuak1 >= puntuak2, "Sailkapena ez dago ondo ordenatuta puntuen arabera. Posizioa " + i
						+ ": " + puntuak1 + " < " + puntuak2);
			}
		}
	}

	@Test
	void klasifikazioaHasieratzeaTest() throws DataAccessException {
		Klasifikazioa k = new Klasifikazioa();
		// Denboraldi ez-existente batekin probatu
		List<TaldearenKlasifikazioa> zerrenda = k.lortuKlasifikazioaDenboraldian(9999);

		// Nahiz eta partidarik ez egon, zerrenda hutsa edo taldeekin bueltatu behar da
		assertNotNull(zerrenda, "Nahiz eta partidarik ez egon, zerrenda bueltatu behar da (ez null).");

		for (TaldearenKlasifikazioa tk : zerrenda) {
			assertEquals(0, tk.getPuntuak(), "Partidarik gabeko taldeak 0 puntu izan behar ditu.");
			assertEquals(0, tk.getPartidaJokatuak(), "Partida jokatuak 0 izan behar dira.");
			assertEquals(0, tk.getPartidaIrabaziak(), "Partida irabaziak 0 izan behar dira.");
		}
	}
}