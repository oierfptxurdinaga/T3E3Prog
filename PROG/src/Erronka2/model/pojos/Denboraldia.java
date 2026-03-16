package Erronka2.model.pojos;

import java.util.Date;

public class Denboraldia {
    private int denboraldiaKod;
    private String izena;
    private Date hasieraData;
    private Date amaieraData;

    public Denboraldia() {}

    public Denboraldia(int denboraldiaKod, String izena, Date hasieraData, Date amaieraData) {
        this.denboraldiaKod = denboraldiaKod;
        this.izena = izena;
        this.hasieraData = hasieraData;
        this.amaieraData = amaieraData;
    }

    public int getDenboraldiaKod() { return denboraldiaKod; }
    public void setDenboraldiaKod(int denboraldiaKod) { this.denboraldiaKod = denboraldiaKod; }
    public String getIzena() { return izena; }
    public void setIzena(String izena) { this.izena = izena; }
    public Date getHasieraData() { return hasieraData; }
    public void setHasieraData(Date hasieraData) { this.hasieraData = hasieraData; }
    public Date getAmaieraData() { return amaieraData; }
    public void setAmaieraData(Date amaieraData) { this.amaieraData = amaieraData; }

    @Override
    public String toString() {
        return izena;
    }
}