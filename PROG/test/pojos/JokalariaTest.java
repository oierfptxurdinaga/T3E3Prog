package pojos;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

class JokalariaTest {

    @Test
    void eraikitzaileaEtaGetterrakTest() {
        Taldea t = new Taldea();
        t.setIzena("Athletic");
        
        BigDecimal pisua = new BigDecimal("75.5");
        BigDecimal altuera = new BigDecimal("1.82");
        
        // Nuevo constructor completo (9 parámetros, argazkia = null)
        Jokalaria j = new Jokalaria(1, "Iñaki", "Williams", "12345678A", "Aurrelaria", 
                                    pisua, altuera, "Ghanatarra", t, null);

        assertAll("Jokalariaren atributuak egiaztatu",
            () -> assertEquals(1, j.getJokalariakKod()),
            () -> assertEquals("Iñaki", j.getIzena()),
            () -> assertEquals("Aurrelaria", j.getPosizioa()),
            () -> assertEquals(pisua, j.getPisua()),
            () -> assertEquals(altuera, j.getAltuera()),
            () -> assertEquals(t, j.getTaldea()),
            () -> assertNull(j.getArgazkia(), "Argazkia null izan beharko litzateke.")
        );
    }

    @Test
    void toStringTest() {
        Jokalaria j = new Jokalaria();
        j.setIzena("Unai");
        j.setAbizena("Simon");
        j.setPosizioa("Atezaina");
        
        assertEquals("Unai Simon (Atezaina)", j.toString(), "toString formatua okerra da.");
    }
}
