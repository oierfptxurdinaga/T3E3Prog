package pojos;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.Date;

class JardunaldiaTest {

    @Test
    void eraikitzaileaEtaGetterrakTest() {
        Date hasiera = new Date();
        Date amaiera = new Date();
        Jardunaldia j = new Jardunaldia(5, hasiera, amaiera);

        assertEquals(5, j.getJardunaldiKod(), "Jardunaldiaren kodea okerra da.");
        assertEquals(hasiera, j.getHasieraData(), "Hasiera data ez da zuzena.");
        assertEquals(amaiera, j.getAmaieraData(), "Amaiera data ez da zuzena.");
    }

    @Test
    void setterrakTest() {
        Jardunaldia j = new Jardunaldia();
        j.setJardunaldiKod(10);
        
        assertEquals(10, j.getJardunaldiKod(), "Setterrak ez du kodea ondo aldatu.");
    }

    @Test
    void toStringTest() {
        Jardunaldia j = new Jardunaldia();
        j.setJardunaldiKod(3);
        assertEquals("Jardunaldia 3", j.toString(), "toString formatua ez da espero zena.");
    }
}