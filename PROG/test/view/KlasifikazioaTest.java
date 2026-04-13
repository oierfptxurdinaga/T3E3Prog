package view;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pojos.Taldea;
import pojos.TaldearenKlasifikazioa;
import pojos.Partida;

class KlasifikazioaTest {

    private Taldea etxekoTaldea;
    private TaldearenKlasifikazioa klasifikazioa;

    @BeforeEach
    void setUp() {
        // Talde bat eta bere sailkapen objektua hasieratu
        etxekoTaldea = new Taldea();
        etxekoTaldea.setIzena("Donostia VK");
        klasifikazioa = new TaldearenKlasifikazioa(etxekoTaldea);
    }

    @Test
    void testGaraipenArrunta() {
        // 3-0 irabazi duela simulatu
        klasifikazioa.gehitupartidaIrabazia();
        
        // Puntuak kudeatzeko metodoa baduzu, hemen probatu
        // klasifikazioa.setPuntuak(klasifikazioa.getPuntuak() + 3);

        assertEquals(1, klasifikazioa.getPartidaIrabaziak(), "Irabazitako partidak 1 izan behar du");
        assertEquals(0, klasifikazioa.getPartidaGalduak(), "Galdutako partidak 0 izan behar du");
    }

    @Test
    void testIzenaOndoDago() {
        assertEquals("Donostia VK", klasifikazioa.getTaldea().getIzena(), "Taldearen izenak bat etorri behar du");
    }
}