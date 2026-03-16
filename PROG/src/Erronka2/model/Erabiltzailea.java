package Erronka2.model;

/**
 * Erabiltzaile baten oinarrizko informazioa gordetzen duen klasea.
 * <p>
 * Klase honek erabiltzailearen izena eta pasahitza gordetzen ditu.
 * </p>
 */
public class Erabiltzailea {

    private String izena;       // Erabiltzailearen izena (erabiltzaile-izena)
    private String pasahitza;   // Erabiltzailearen pasahitza

    /**
     * Eraikitzaile hutsak, objektua sortzeko erabil daiteke hasierarik gabe.
     */
    public Erabiltzailea() {
    }

    /**
     * Eraikitzaile parametrizatuak, erabiltzailearen izena eta pasahitza ezartzeko.
     *
     * @param izena     Erabiltzailearen izena
     * @param pasahitza Erabiltzailearen pasahitza
     */
    public Erabiltzailea(String izena, String pasahitza) {
        this.izena = izena;
        this.pasahitza = pasahitza;
    }

    /**
     * Erabiltzailearen izena itzultzen du.
     *
     * @return Erabiltzailearen izena
     */
    public String getIzena() {
        return izena;
    }

    /**
     * Erabiltzailearen izena ezartzen du.
     *
     * @param izena Erabiltzailearen izena berria
     */
    public void setIzena(String izena) {
        this.izena = izena;
    }

    /**
     * Erabiltzailearen pasahitza itzultzen du.
     *
     * @return Erabiltzailearen pasahitza
     */
    public String getPasahitza() {
        return pasahitza;
    }

    /**
     * Erabiltzailearen pasahitza ezartzen du.
     *
     * @param pasahitza Erabiltzailearen pasahitza berria
     */
    public void setPasahitza(String pasahitza) {
        this.pasahitza = pasahitza;
    }
}
