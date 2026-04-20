package pojos;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ZelaiaTest {

    @Test
    void eraikitzaileaEtaGetterrakTest() {
        Zelaia z = new Zelaia(100, "Anoeta", "Donostia", 39000);

        assertAll("Zelaiaren atributuak egiaztatu",
            () -> assertEquals(100, z.getZelaiaKod()),
            () -> assertEquals("Anoeta", z.getIzena()),
            () -> assertEquals("Donostia", z.getKokapena()),
            () -> assertEquals(39000, z.getKapazitatea())
        );
    }

    @Test
    void setterrakTest() {
        Zelaia z = new Zelaia();
        z.setIzena("Ipurua");
        z.setKapazitatea(8164);
        
        assertEquals("Ipurua", z.getIzena());
        assertEquals(8164, z.getKapazitatea());
    }

    @Test
    void toStringTest() {
        Zelaia z = new Zelaia(1, "Mendizorrotza", "Gasteiz", 19840);
        // Espero den formatua: "Izena (Kokapena)"
        assertEquals("Mendizorrotza (Gasteiz)", z.toString(), "toString formatua okerra da.");
    }
}