package pojos;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.Date;

class PartidaTest {

    @Test
    void eraikitzaileaEtaObjektuErlazioakTest() {
        Partida p = new Partida();
        Epailea e = new Epailea();
        Taldea etxekoa = new Taldea();
        etxekoa.setIzena("Athletic");
        Taldea kanpokoa = new Taldea();
        kanpokoa.setIzena("Real Sociedad");
        
        p.setPartidaKod(101);
        p.setEpailea(e);
        p.setEtxekoTaldea(etxekoa);
        p.setKanpokoTaldea(kanpokoa);
        p.setEmaitza("2-1");
        p.setTxartelak(4);

        assertAll("Partidaren datuak egiaztatu",
            () -> assertEquals(101, p.getPartidaKod()),
            () -> assertEquals(e, p.getEpailea()),
            () -> assertEquals(etxekoa, p.getEtxekoTaldea()),
            () -> assertEquals(kanpokoa, p.getKanpokoTaldea()),
            () -> assertEquals("2-1", p.getEmaitza()),
            () -> assertEquals(4, p.getTxartelak())
        );
    }

    @Test
    void toStringTest() {
        Partida p = new Partida();
        Taldea t1 = new Taldea(); t1.setIzena("Athletic");
        Taldea t2 = new Taldea(); t2.setIzena("Eibar");
        
        p.setEtxekoTaldea(t1);
        p.setKanpokoTaldea(t2);
        p.setEmaitza("3-0");
        
        String esperoDenTestua = "Athletic vs Eibar (3-0)";
        assertEquals(esperoDenTestua, p.toString(), "toString formatua ez dator bat.");
    }
}