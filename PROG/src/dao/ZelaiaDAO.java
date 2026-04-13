package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import pojos.Zelaia;

/**
 * Zelaia entitatearentzako Datuetarako Sarbide Objektua (DAO).
 * Zelaia taularen gaineko CRUD operazioak kudeatzen dituen klasea.
 * Datu-basean zelaiekin lotutako irakurketa eragiketak kudeatzen ditu.
 */
public class ZelaiaDAO {

    private Konexioa konexioa;

    /**
     * ZelaiaDAO klasearen eraikitzailea.
     * Datu-basearekiko konexioa kudeatuko duen objektua hasieratzen du.
     */
    public ZelaiaDAO() {
        konexioa = new Konexioa();
    }

    /**
     * Datu-basean erregistratuta dauden zelaia guztiak lortzen ditu.
     * Zelaia guztiak lortzen ditu.
     * * @return Datu-basetik irakurritako {@link Zelaia} objektuen zerrenda (List).
     */
    public List<Zelaia> zelaiGutziakLortu() {
        List<Zelaia> zerrenda = new ArrayList<>();
        String sql = "SELECT zelaia_kod, izena, kokapena, kapazitatea FROM zelaia";
        try {
            konexioa.konexioaIreki();
            Statement st = konexioa.getKonexioa().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Zelaia z = new Zelaia();
                z.setZelaiaKod(rs.getInt("zelaia_kod"));
                z.setIzena(rs.getString("izena"));
                z.setKokapena(rs.getString("kokapena"));
                z.setKapazitatea(rs.getInt("kapazitatea"));
                zerrenda.add(z);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.err.println("Errorea zelaiak irakurtzean: " + e.getMessage());
        } finally {
            konexioa.konexioaItxi();
        }
        return zerrenda;
    }

  
}