package pojos;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class TaldearenKlasifikazioaTest {

    @Test
    void hasierakoBalioakTest() {
        Taldea t = new Taldea();
        TaldearenKlasifikazioa k = new TaldearenKlasifikazioa(t);

        assertEquals(t, k.getTaldea());
        assertEquals(0, k.getPuntuak(), "Hasieran puntuek 0 izan behar dute.");
        assertEquals(0, k.getPartidaJokatuak());
    }

    @Test
    void logikaGehituTest() {
        Taldea t = new Taldea();
        TaldearenKlasifikazioa k = new TaldearenKlasifikazioa(t);

        // Partida bat irabazi
        k.gehitupartidaJokatua();
        k.gehitupartidaIrabazia();
        
        // Set-ak kudeatu
        k.gehituSetakIrabaziak(3);
        k.gehituSetakGalduak(1);

        assertAll("Klasifikazioaren kalkuluak egiaztatu",
            () -> assertEquals(1, k.getPartidaJokatuak(), "Jokatutako partidak ez dira ondo zenbatu."),
            () -> assertEquals(3, k.getPuntuak(), "Irabaztean 3 puntu gehitu behar dira."),
            () -> assertEquals(2, k.getSetDiferentzia(), "Set diferentzia (3-1) ez da zuzena.")
        );
    }
}