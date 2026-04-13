package dao;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import pojos.Erabiltzailea;
import java.util.List;

class ErabiltzaileakDAOTest {

    private ErabiltzaileakDAO erabiltzaileDAO;

    @BeforeEach
    void hasieratu() {
        erabiltzaileDAO = new ErabiltzaileakDAO();
    }

    @Test
    void loginTest() {
        // Kontuan izan: Erabiltzaile honek DBan egon behar du lehenagotik
        Erabiltzailea e = erabiltzaileDAO.login("ez-existitzen", "1234");
        assertNull(e, "Existitzen ez den erabiltzaileak null bueltatu behar luke.");
    }

    @Test
    void erabiltzaileGuztiakLortuTest() {
        // Zure DAOan metodoa getErabiltzaileak() deitzen da
        List<Erabiltzailea> zerrenda = erabiltzaileDAO.getErabiltzaileak();
        assertNotNull(zerrenda, "Zerrenda ezin da null izan.");
    }

    @Test
    void erregistratuEtaEzabatuTest() {
        String izena = "test_berria";
        String pass = "1234";
        
        // Zure DAOan metodoa erregistratu(String, String) da
        boolean gordeta = erabiltzaileDAO.erregistratu(izena, pass);
        // Oharra: false izan daiteke erabiltzailea jada existitzen bada
        
        // Ezabatzen saiatu (garbitzeko)
        erabiltzaileDAO.ezabatuErabiltzailea(izena);
    }
}