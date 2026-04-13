package pojos;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ErabiltzaileaTest {

    @Test
    void eraikitzaileaEtaRolakTest() {
        Erabiltzailea u = new Erabiltzailea("admin", "1234", "ADMIN");
        
        assertEquals("admin", u.getErabiltzailea(), "Erabiltzaile izena ez da zuzena.");
        assertEquals("1234", u.getPasahitza(), "Pasahitza ez da zuzena.");
        assertEquals("ADMIN", u.getRola(), "Rola ez da zuzena.");
    }

    @Test
    void toStringFormatuaTest() {
        Erabiltzailea u = new Erabiltzailea("epaile1", "p@ss", "EPAILEA");
        String esperoDenTestua = "Erabiltzaileak [erabiltzailea=epaile1, pasahitza=p@ss, rola=EPAILEA]";
        assertEquals(esperoDenTestua, u.toString(), "toString-ak ez du espero zen formatua.");
    }

    @Test
    void idNullHasieranTest() {
        // IDa @GeneratedValue denez, DBan gorde aurretik null izan behar da
        Erabiltzailea u = new Erabiltzailea();
        assertNull(u.getId(), "Objektu berri baten IDak null izan behar luke.");
    }
}