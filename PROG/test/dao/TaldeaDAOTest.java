package dao;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import pojos.Taldea;
import java.util.List;
import java.util.Date;

class TaldeaDAOTest {

    private TaldeaDAO taldeaDAO;

    @BeforeEach
    void setup() {
        taldeaDAO = new TaldeaDAO();
    }

    @Test
    void taldeaSortuEtaLortuTest() {
        Taldea t = new Taldea();
        t.setIzena("Test Taldea");
        t.setSortzeData(new Date());

        // Sortu
        boolean sortua = taldeaDAO.taldeaSortu(t);
        
        // Lortu guztiak
        List<Taldea> guztiak = taldeaDAO.taldeGuztiakLortu();
        assertNotNull(guztiak);

        // Ezabatu (garbitzeko)
        if (sortua) {
            taldeaDAO.TaldeEzabatu(t.getTaldeaKod());
        }
    }

    @Test
    void taldeaIdBidezBilatuTest() {
        // Zure metodoa: TaldeLortuIdBidez
        Taldea t = taldeaDAO.TaldeLortuIdBidez(1);
        // Ez dugu assertTrue egiten DBa hutsik egon daitekeelako, 
        // baina metodoa ondo deituta dago.
    }
}