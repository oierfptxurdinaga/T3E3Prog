package Erronka2.model.pojos;

import java.util.Date;

public class Taldea {
    private int taldeaKod;
    private String izena;
    private Date sortzeData;
    private Zelaia zelaia;

    public Taldea() {}

    public Taldea(int taldeaKod, String izena, Date sortzeData, Zelaia zelaia) {
        this.taldeaKod = taldeaKod;
        this.izena = izena;
        this.sortzeData = sortzeData;
        this.zelaia = zelaia;
    }

    public int getTaldeaKod() { return taldeaKod; }
    public void setTaldeaKod(int taldeaKod) { this.taldeaKod = taldeaKod; }
    public String getIzena() { return izena; }
    public void setIzena(String izena) { this.izena = izena; }
    public Date getSortzeData() { return sortzeData; }
    public void setSortzeData(Date sortzeData) { this.sortzeData = sortzeData; }
    public Zelaia getZelaia() { return zelaia; }
    public void setZelaia(Zelaia zelaia) { this.zelaia = zelaia; }

    @Override
    public String toString() {
        return izena;
    }
}