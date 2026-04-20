package pojos;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.Date;

class TaldeaTest {

    @Test
    void eraikitzaileaEtaGetterrakTest() {
        Zelaia z = new Zelaia(1, "San Mames", "Bilbo", 53000);
        Date data = new Date();
        Taldea t = new Taldea(1, "Athletic", data, z);

        assertAll("Taldearen datuak egiaztatu",
            () -> assertEquals(1, t.getTaldeaKod(), "Kodea okerra da."),
            () -> assertEquals("Athletic", t.getIzena(), "Izena okerra da."),
            () -> assertEquals(data, t.getSortzeData(), "Data ez da zuzena."),
            () -> assertEquals(z, t.getZelaia(), "Zelaia ez da ondo esleitu.")
        );
    }

    @Test
    void toStringTest() {
        Taldea t = new Taldea();
        t.setIzena("Eibar");
        assertEquals("Eibar", t.toString(), "toString-ak izena soilik itzuli behar luke.");
    }
}