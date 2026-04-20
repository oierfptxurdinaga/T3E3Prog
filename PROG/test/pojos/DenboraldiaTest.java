package pojos;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.Date;

class DenboraldiaTest {

    @Test
    void eraikitzaileaEtaGetterrakTest() {
        Date hasiera = new Date();
        Date amaiera = new Date();
        // Constructor completo con 7 parámetros (incluye aktiboa)
        Denboraldia d = new Denboraldia(1, "2023-24", hasiera, amaiera, true, "Athletic", false);

        assertAll("Denboraldiaren atributuak egiaztatu",
            () -> assertEquals(1, d.getDenboraldiaKod(), "Kodea ez dator bat."),
            () -> assertEquals("2023-24", d.getIzena(), "Izena ez da zuzena."),
            () -> assertEquals(hasiera, d.getHasieraData(), "Hasiera data ez da berdina."),
            () -> assertEquals(amaiera, d.getAmaieraData(), "Amaiera data ez da berdina."),
            () -> assertTrue(d.isAmaituta(), "Denboraldia amaituta egon beharko litzateke."),
            () -> assertEquals("Athletic", d.getTxapelduna(), "Txapeldunaren izena ez dator bat."),
            () -> assertFalse(d.isAktiboa(), "Aktiboa false izan beharko litzateke.")
        );
    }

    @Test
    void setterrakTest() {
        Denboraldia d = new Denboraldia();
        d.setIzena("Test Denboraldia");
        d.setAmaituta(false);
        d.setAktiboa(true);
        
        assertAll("Setterrak probatu",
            () -> assertEquals("Test Denboraldia", d.getIzena(), "Setterrak ez du izena ondo aldatu."),
            () -> assertFalse(d.isAmaituta(), "Setterrak ez du amaituta egoera ondo aldatu."),
            () -> assertTrue(d.isAktiboa(), "Setterrak ez du aktiboa egoera ondo aldatu.")
        );
    }

    @Test
    void toStringTest() {
        Denboraldia d = new Denboraldia();
        d.setIzena("2025-26");
        assertEquals("2025-26", d.toString(), "toString metodoak izena itzuli behar luke.");
    }
}
