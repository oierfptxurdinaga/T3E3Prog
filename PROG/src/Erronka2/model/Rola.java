package Erronka2.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Erabiltzailearen rola kudeatzen duen klasea.
 * <p>
 * Erabiltzailea klasea hedatu eta rola motak barne hartzen ditu.
 * Rolak ADMIN, EPAILEA eta ERABILTZAILE dira.
 * </p>
 */
public class Rola extends Erabiltzailea {

    /** Erabiltzailearen rola (ADMIN, EPAILEA, ERABILTZAILE) */
    private RolMota rola;

    /** Erabiltzaile guztien zerrenda estatikoa */
    private static List<Rola> erabiltzaileak = new ArrayList<>();

    /**
     * Eraikitzaile huts.
     */
    public Rola() {
    }

    /**
     * Eraikitzaile datuak jasotzen dituena.
     * 
     * @param izena     Erabiltzailearen izena
     * @param pasahitza Erabiltzailearen pasahitza
     * @param rolMota   Erabiltzailearen rola
     */
    public Rola(String izena, String pasahitza, RolMota rolMota) {
        super(izena, pasahitza);
        this.rola = rolMota;
    }

    /**
     * Erabiltzailearen rola itzultzen du.
     * 
     * @return Erabiltzailearen rola
     */
    public RolMota getRola() {
        return rola;
    }

    /**
     * Erabiltzailearen rola ezartzen du.
     * 
     * @param rola Ezarri nahi den rola
     */
    public void setRola(RolMota rola) {
        this.rola = rola;
    }

    /**
     * Erabiltzaile zerrenda osoa itzultzen du.
     * 
     * @return Erabiltzaileak zerrendan
     */
    public static List<Rola> getErabiltzaileak() {
        return erabiltzaileak;
    }

    /**
     * Erabiltzaile zerrenda berria ezartzen du.
     * 
     * @param erabiltzaileak Erabiltzaileak zerrenda berria
     */
    public static void setErabiltzaileak(List<Rola> erabiltzaileak) {
        Rola.erabiltzaileak = erabiltzaileak;
    }

    /**
     * Hasierako erabiltzaile batzuk gehitzen ditu, zerrenda hutsik badago bakarrik.
     */
    public static void erabiltzaileakHasieratu() {
        if (erabiltzaileak.isEmpty()) {
            erabiltzaileak.add(new Rola("Urtzi", "admin123", RolMota.ADMIN));
            erabiltzaileak.add(new Rola("Ekaitz", "admin123", RolMota.ADMIN));
            erabiltzaileak.add(new Rola("Irati", "admin123", RolMota.ADMIN));
            erabiltzaileak.add(new Rola("Oier", "epaile123", RolMota.EPAILEA));
            erabiltzaileak.add(new Rola("user", "user123", RolMota.ERABILTZAILE));
        }
    }

    /**
     * Erabiltzaile baten izena eta pasahitza egiaztatzen ditu.
     * 
     * @param izena     Erabiltzailearen izena
     * @param pasahitza Pasahitza
     * @return Datuak baliozkoak badira Rola objektua, bestela null
     */
    public static Rola Egiaztatu(String izena, String pasahitza) {
        erabiltzaileakHasieratu();

        for (Rola r : erabiltzaileak) {
            if (r.getIzena().equals(izena) && r.getPasahitza().equals(pasahitza)) {
                return r;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Rola [rola=" + rola + "]";
    }

    /**
     * Erabiltzaile rolen enum mota, erabilgarri dauden rolen izenak.
     */
    public enum RolMota {
        ERABILTZAILE, ADMIN, EPAILEA
    }
}
