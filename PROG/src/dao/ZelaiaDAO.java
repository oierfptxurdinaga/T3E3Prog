package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import pojos.Zelaia;

/**
 * Zelaia taularen gaineko CRUD operazioak kudeatzen dituen klasea.
 */
public class ZelaiaDAO {

    private Konexioa konexioa;

    public ZelaiaDAO() {
        konexioa = new Konexioa();
    }

    /**
     * Zelaia guztiak lortzen ditu.
     * @return Zelaien zerrenda
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