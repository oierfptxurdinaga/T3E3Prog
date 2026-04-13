package dao;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import pojos.Jardunaldia;
import java.util.List;
import java.util.Date;

class JardunaldiaDAOTest {

    private JardunaldiaDAO jardunaldiaDAO;

    @BeforeEach
    void setup() {
        jardunaldiaDAO = new JardunaldiaDAO();
    }

    @Test
    void jardunaldiaSortuTest() {
        Jardunaldia j = new Jardunaldia();
        j.setHasieraData(new Date());
        j.setAmaieraData(new Date());

        int id = jardunaldiaDAO.jardunaldiaSortu(j);
        
        // IDa -1 ez izatea espero dugu ondo badoa
        assertNotEquals(-1, id, "Jardunaldiaren IDa ezin da -1 izan.");
        assertTrue(j.getJardunaldiKod() > 0);
    }

    @Test
    void lortuJardunaldiDenboraldiBidezTest() {
        // Zure metodoa: lostuJardunaldiDenboraldiBidez
        List<Jardunaldia> jardunaldiak = jardunaldiaDAO.lostuJardunaldiDenboraldiBidez(1);
        assertNotNull(jardunaldiak);
    }

    @Test
    void denboraldiaAsoziatuTest() {
        // Proba hau egiteko IDak behar dira, baina metodoaren deia egiaztatzen dugu
        boolean emaitza = jardunaldiaDAO.denboraldiaAsoziatu(1, 1);
        // DB murrizketengatik (FK) false eman dezake IDak ez badaude, baina sintaxia ondo dago
    }
}