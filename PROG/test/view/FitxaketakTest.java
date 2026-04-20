package view;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import pojos.Jokalaria;
import pojos.Taldea;

class FitxaketakTest {

    @Test
    void testJokalariarenTaldeAldaketa() {
        // Taldeak prestatu
        Taldea t1 = new Taldea();
        t1.setIzena("Antiguoko");
        
        Taldea t2 = new Taldea();
        t2.setIzena("Bera Bera");

        // Jokalaria hasieratu
        Jokalaria j = new Jokalaria();
        j.setIzena("Mikel");
        j.setTaldea(t1);

        // Aldaketa simulatu
        j.setTaldea(t2);

        // Baieztapenak
        assertEquals("Bera Bera", j.getTaldea().getIzena(), "Jokalariaren talde berria Bera Bera izan behar da");
        assertNotEquals("Antiguoko", j.getTaldea().getIzena(), "Jokalaria ez litzateke talde zaharrean egon behar");
    }

    @Test
    void testJokalariDatuakEzDiraGaltzen() {
        Jokalaria j = new Jokalaria();
        j.setIzena("Ane");
        
        assertNotNull(j.getIzena());
        assertEquals("Ane", j.getIzena());
    }
}