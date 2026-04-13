package dao;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import pojos.Partida;
import java.util.List;
import java.util.Date;

class PartidaDAOTest {

    private PartidaDAO partidaDAO;

    @BeforeEach
    void hasieratu() {
        partidaDAO = new PartidaDAO();
    }

    @Test
    void partidaLortuDenboraldiBitartezTest() {
        // Ziurtatu datu-basean denboraldiren bat dagoela ID honekin
        List<Partida> partidak = partidaDAO.partidaLortuDenboraldiBitartez(1);
        assertNotNull(partidak, "Zerrenda ezin da null izan.");
    }

    @Test
    void sortuPartidaTest() {
        Partida p = new Partida();
        p.setData(new Date());
        p.setOrdua("18:00");
        p.setEmaitza("0-0");
        p.setZigorrak(0);
        p.setTxartelak(0);
        
        // Epailea null utziko dugu (DAO-ak onartzen du)
        p.setEpailea(null);

        // ZUZENKETA: Metodoak true bueltatu beharko luke lerroa 'partida' taulan txertatzen delako
        boolean emaitza = partidaDAO.sortuPartida(p);
        
        assertTrue(emaitza, "Partida ondo sortu beharko litzateke oinarrizko datuekin.");
        assertTrue(p.getPartidaKod() > 0, "Partidaren ID-a autogeneratua izan beharko litzateke.");
    }
}