package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import pojos.TaldearenKlasifikazioa;
import java.util.List;

/**
 * Klasifikazioa klasearen unitate-testak.
 * Kontuan izan: Metodo honek DAOak erabiltzen dituenez, ingurune kontrolatu 
 * batean exekutatu behar da (datu-basea prest egon behar da).
 */
class KlasifikazioaTest {

    @Test
    void lortuKlasifikazioaDenboraldianTest() {
        Klasifikazioa klasifikazioaModel = new Klasifikazioa();
        
        // Suposatuz 1 ID-a duen denboraldia existitzen dela datu-basean
        int denboraldiaKod = 1;
        
        List<TaldearenKlasifikazioa> emaitza = klasifikazioaModel.lortuKlasifikazioaDenboraldian(denboraldiaKod);

        // 1. Egiaztatu zerrenda ez dela null
        assertNotNull(emaitza, "Sailkapen zerrenda ezin da null izan.");

        // 2. Egiaztatu zerrenda ordenatuta dagoela (Puntuen arabera)
        // Lehenengoak bigarrenak baino puntu gehiago edo berdinak izan behar ditu.
        if (emaitza.size() >= 2) {
            int lehenengoPuntuak = emaitza.get(0).getPuntuak();
            int bigarrenPuntuak = emaitza.get(1).getPuntuak();
            
            assertTrue(lehenengoPuntuak >= bigarrenPuntuak, 
                "Sailkapena ez dago ondo ordenatuta puntuen arabera.");
        }
    }

    @Test
    void klasifikazioaHasieratzeaTest() {
        Klasifikazioa k = new Klasifikazioa();
        // Denboraldi ez-existente batekin probatu
        List<TaldearenKlasifikazioa> zerrenda = k.lortuKlasifikazioaDenboraldian(-1);
        
        // Nahiz eta partidarik ez egon, talde guztiak agertu beharko lirateke 0 punturekin
        assertNotNull(zerrenda, "Nahiz eta partidarik ez egon, zerrenda bueltatu behar da.");
        
        for (TaldearenKlasifikazioa tk : zerrenda) {
            assertEquals(0, tk.getPuntuak(), "Partidarik gabeko taldeak 0 puntu izan behar ditu.");
        }
    }
}