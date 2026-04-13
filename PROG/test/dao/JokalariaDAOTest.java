package dao;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import pojos.Jokalaria;
import java.math.BigDecimal;
import java.util.List;

class JokalariaDAOTest {

    private JokalariaDAO jokalariaDAO;

    @BeforeEach
    void setup() {
        jokalariaDAO = new JokalariaDAO();
    }

    @Test
    void jokalariGuztiakLortuTest() {
        List<Jokalaria> zerrenda = jokalariaDAO.jokalariGuztiakLortu();
        assertNotNull(zerrenda);
    }

    @Test
    void jokalariaSortuEtaEzabatuTest() {
        Jokalaria j = new Jokalaria();
        j.setIzena("Test");
        j.setAbizena("User");
        j.setNan("99999999X");
        j.setPisua(new BigDecimal("75.5"));
        j.setAltuera(new BigDecimal("1.80"));

        // Sortu
        boolean sortua = jokalariaDAO.jokalariaSortu(j);
        
        // Ezabatu
        if (sortua) {
            boolean ezabatua = jokalariaDAO.jokalariaEzabatu(j.getJokalariakKod());
            assertTrue(ezabatua);
        }
    }
}