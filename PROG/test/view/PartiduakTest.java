package view;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import pojos.Partida;

class PartiduakTest {

    @Test
    void testEmaitzaFormatuZuzena() {
        Partida p = new Partida();
        p.setEmaitza("3-1");
        
        String[] zatiak = p.getEmaitza().split("-");
        int etxekoak = Integer.parseInt(zatiak[0]);
        int kanpokoak = Integer.parseInt(zatiak[1]);

        assertEquals(3, etxekoak);
        assertEquals(1, kanpokoak);
    }

    @Test
    void testEmaitzaErroreaLetrekin() {
        Partida p = new Partida();
        p.setEmaitza("3-a"); // Errore boluntarioa

        String[] zatiak = p.getEmaitza().split("-");
        
        // Egiaztatu sistemak errore bat jaurtitzen duela letra bat parseatzean
        assertThrows(NumberFormatException.class, () -> {
            Integer.parseInt(zatiak[1]);
        }, "Letra bat ezin da zenbaki bihurtu, errorea eman behar du");
    }
}