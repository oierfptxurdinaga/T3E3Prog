package dao;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import pojos.Jokalaria;
import pojos.Taldea;

/**
 * Jokalaria entitatearentzako Datuetarako Sarbide Objektua (DAO - Data Access Object).
 * Datu-basean jokalariekin lotutako eragiketak kudeatzen ditu, hala nola jokalariak 
 * sortzea, zerrendatzea, bilatzea, datuak eguneratzea eta ezabatzea.
 */
public class JokalariaDAO {
    private Konexioa konexioa; // Datu-basearekin konektatzeko objektua

    /**
     * JokalariaDAO klasearen eraikitzailea.
     * Datu-basearekiko konexioa kudeatzen duen objektua hasieratzen du.
     */
    public JokalariaDAO() {
        konexioa = new Konexioa(); // Konstruktorean konekzioa sortu
    }

    /**
     * Jokalari berri bat sortzen du datu-basean eta automatikoki esleitutako IDa lortzen du.
     * @param jokalaria Sortu nahi den jokalariaren datuak dituen {@link Jokalaria} objektua.
     * @return true jokalaria modu egokian sortu bada, edo false arazoren bat egon bada.
     */
    // Jokalari berri bat sortzeko metodoa
    public boolean jokalariaSortu(Jokalaria jokalaria) {
        String sql = "INSERT INTO jokalariak (izena, abizena, NAN, posizioa, pisua, altuera, herritartasuna, taldea_kod, argazkia) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            konexioa.konexioaIreki(); // DB konexioa ireki
            PreparedStatement ps = konexioa.getKonexioa().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Parametroak jarri
            ps.setString(1, jokalaria.getIzena());
            ps.setString(2, jokalaria.getAbizena());
            ps.setString(3, jokalaria.getNan());
            ps.setString(4, jokalaria.getPosizioa());
            ps.setBigDecimal(5, jokalaria.getPisua());
            ps.setBigDecimal(6, jokalaria.getAltuera());
            ps.setString(7, jokalaria.getHerritartasuna());

            // Taldea ez bada null, ID-a jarri, bestela null
            if (jokalaria.getTaldea() != null) {
                ps.setInt(8, jokalaria.getTaldea().getTaldeaKod());
            } else {
                ps.setNull(8, java.sql.Types.INTEGER);
            }

            // Argazkiaren bidea (null izan daiteke)
            ps.setString(9, jokalaria.getArgazkia());

            int affected = ps.executeUpdate(); // Datuak gehitu

            // ID autogeneratua jokalari objektuan gorde
            if (affected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                
                if (rs.next())  {
                    jokalaria.setJokalariakKod(rs.getInt(1));
                    rs.close();
                }
            }

            ps.close();
            return affected > 0; // True itzuli insert ondo egon bada
        } catch (SQLException e) {
            System.err.println("Errorea jokalaria gehitzean: " + e.getMessage());
            return false;
        } finally {
            konexioa.konexioaItxi(); // Beti konexioa itxi
        }
    }

    /**
     * Datu-basean erregistratuta dauden jokalari guztiak eskuratzen ditu, bakoitzaren taldearen datuekin (baleuka).
     * @return {@link Jokalaria} objektuen zerrenda (List).
     */
    // Datu-baseko jokalari guztiak lortu
    public List<Jokalaria> jokalariGuztiakLortu() {
        List<Jokalaria> zerrenda = new ArrayList<>();
        String sql = "SELECT j.jokalariak_kod, j.izena, j.abizena, j.NAN, j.posizioa, j.pisua, j.altuera, j.herritartasuna, j.taldea_kod, j.argazkia, "
                   + "t.izena as talde_izena FROM jokalariak j LEFT JOIN taldea t ON j.taldea_kod = t.taldea_kod";

        try {
            konexioa.konexioaIreki();
            Statement st = konexioa.getKonexioa().createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                Jokalaria j = new Jokalaria();
                j.setJokalariakKod(rs.getInt("jokalariak_kod"));
                j.setIzena(rs.getString("izena"));
                j.setAbizena(rs.getString("abizena"));
                j.setNan(rs.getString("NAN"));
                j.setPosizioa(rs.getString("posizioa"));
                j.setPisua(rs.getBigDecimal("pisua"));
                j.setAltuera(rs.getBigDecimal("altuera"));
                j.setHerritartasuna(rs.getString("herritartasuna"));
                j.setArgazkia(rs.getString("argazkia"));

                // Taldea ez bada null, jokalariari jarri
                int taldeaKod = rs.getInt("taldea_kod");
                if (!rs.wasNull()) {
                    Taldea t = new Taldea();
                    t.setTaldeaKod(taldeaKod);
                    t.setIzena(rs.getString("talde_izena"));
                    j.setTaldea(t);
                }

                zerrenda.add(j); // Zerrendara gehitu
            }

            rs.close();
            st.close();
            
        } catch (SQLException e) {
            System.err.println("Errorea jokalariak irakurtzean: " + e.getMessage());
        } finally {
            konexioa.konexioaItxi();
        }

        return zerrenda;
    }

    /**
     * Jokalari zehatz bat bilatzen du datu-basean bere identifikatzaile bakarra erabiliz.
     * @param jokalariakKod Bilatu nahi den jokalariaren identifikatzailea (ID).
     * @return Aurkitutako {@link Jokalaria} objektua datu guztiekin, edo null ez bada aurkitzen.
     */
    // IDz jokalaria lortu
    public Jokalaria jokalariaLortuIdBidez(int jokalariakKod) {
        Jokalaria j = null;
        String sql = "SELECT j.jokalariak_kod, j.izena, j.abizena, j.NAN, j.posizioa, j.pisua, j.altuera, j.herritartasuna, j.taldea_kod, j.argazkia, "
                   + "t.izena as talde_izena FROM jokalariak j LEFT JOIN taldea t ON j.taldea_kod = t.taldea_kod "
                   + "WHERE j.jokalariak_kod = ?";

        try {
            konexioa.konexioaIreki();
            PreparedStatement ps = konexioa.getKonexioa().prepareStatement(sql);
            ps.setInt(1, jokalariakKod);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                j = new Jokalaria();
                j.setJokalariakKod(rs.getInt("jokalariak_kod"));
                j.setIzena(rs.getString("izena"));
                j.setAbizena(rs.getString("abizena"));
                j.setNan(rs.getString("NAN"));
                j.setPosizioa(rs.getString("posizioa"));
                j.setPisua(rs.getBigDecimal("pisua"));
                j.setAltuera(rs.getBigDecimal("altuera"));
                j.setHerritartasuna(rs.getString("herritartasuna"));
                j.setArgazkia(rs.getString("argazkia"));

                int taldeaKod = rs.getInt("taldea_kod");
                if (!rs.wasNull()) {
                    Taldea t = new Taldea();
                    t.setTaldeaKod(taldeaKod);
                    t.setIzena(rs.getString("talde_izena"));
                    j.setTaldea(t);
                }
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.err.println("Errorea jokalaria IDz irakurtzean: " + e.getMessage());
        } finally {
            konexioa.konexioaItxi();
        }

        return j;
    }

    /**
     * Talde zehatz bateko jokalari guztiak lortzen ditu datu-basetik.
     * @param taldeaKod Bilatu nahi diren jokalarien taldearen identifikatzailea.
     * @return Ematen den taldeari lotutako {@link Jokalaria} objektuen zerrenda (List).
     */
    // Talde bateko jokalariak lortu
    public List<Jokalaria> jokalariaLortuTaldeBidez(int taldeaKod) {
        List<Jokalaria> zerrenda = new ArrayList<>();
        String sql = "SELECT jokalariak_kod, izena, abizena, NAN, posizioa, pisua, altuera, herritartasuna, argazkia "
                   + "FROM jokalariak WHERE taldea_kod = ?";

        try {
            konexioa.konexioaIreki();
            PreparedStatement ps = konexioa.getKonexioa().prepareStatement(sql);
            ps.setInt(1, taldeaKod);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Jokalaria j = new Jokalaria();
                j.setJokalariakKod(rs.getInt("jokalariak_kod"));
                j.setIzena(rs.getString("izena"));
                j.setAbizena(rs.getString("abizena"));
                j.setNan(rs.getString("NAN"));
                j.setPosizioa(rs.getString("posizioa"));
                j.setPisua(rs.getBigDecimal("pisua"));
                j.setAltuera(rs.getBigDecimal("altuera"));
                j.setHerritartasuna(rs.getString("herritartasuna"));
                j.setArgazkia(rs.getString("argazkia"));
                zerrenda.add(j);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.err.println("Errorea jokalariak taldearen arabera irakurtzean: " + e.getMessage());
        } finally {
            konexioa.konexioaItxi();
        }

        return zerrenda;
    }

    /**
     * Existitzen den jokalari baten datuak eguneratzen ditu datu-basean.
     * @param jokalaria Eguneratu nahi diren datu berriak dituen {@link Jokalaria} objektua.
     * @return true eguneraketa modu egokian burutu bada, edo false arazoren bat egon bada.
     */
    // Jokalari baten datuak eguneratu
    public boolean eguneratu(Jokalaria jokalaria) {
        String sql = "UPDATE jokalariak SET izena = ?, abizena = ?, NAN = ?, posizioa = ?, pisua = ?, altuera = ?, herritartasuna = ?, taldea_kod = ?, argazkia = ? "
                   + "WHERE jokalariak_kod = ?";

        try {
            konexioa.konexioaIreki();
            PreparedStatement ps = konexioa.getKonexioa().prepareStatement(sql);

            ps.setString(1, jokalaria.getIzena());
            ps.setString(2, jokalaria.getAbizena());
            ps.setString(3, jokalaria.getNan());
            ps.setString(4, jokalaria.getPosizioa());
            ps.setBigDecimal(5, jokalaria.getPisua());
            ps.setBigDecimal(6, jokalaria.getAltuera());
            ps.setString(7, jokalaria.getHerritartasuna());

            if (jokalaria.getTaldea() != null)
                ps.setInt(8, jokalaria.getTaldea().getTaldeaKod());
            else
                ps.setNull(8, java.sql.Types.INTEGER);

            ps.setString(9, jokalaria.getArgazkia());
            ps.setInt(10, jokalaria.getJokalariakKod());

            int affected = ps.executeUpdate();
            ps.close();
            return affected > 0;
        } catch (SQLException e) {
            System.err.println("Errorea jokalaria eguneratzean: " + e.getMessage());
            return false;
        } finally {
            konexioa.konexioaItxi();
        }
    }

    /**
     * Jokalari bat talde berri batera aldatzen du (traspasoa). Egiaztatzen du jokalaria 
     * eta taldea null ez direla eta jokalaria ez dagoela jada talde horretan bertan.
     * @param jokalaria Traspasatu nahi den {@link Jokalaria} objektua.
     * @param taldeHelburua Jokalaria joango den {@link Taldea} objektu berria.
     * @return true traspasoa ondo burutu bada, edo false arazoren bat egon bada.
     * @throws IllegalArgumentException Jokalaria edo taldea null badira, edo jokalaria jada talde horretan badago.
     */
    // Jokalaria talde berri batera traspasatu
    public boolean traspasatu(Jokalaria jokalaria, Taldea taldeHelburua) {
        if (jokalaria == null || taldeHelburua == null)
            throw new IllegalArgumentException("Jokalaria eta talde helburua ezin dira null izan.");
        if (jokalaria.getTaldea() != null && jokalaria.getTaldea().getTaldeaKod() == taldeHelburua.getTaldeaKod())
            throw new IllegalArgumentException("Jokalaria dagoeneko talde horretan dago.");

        jokalaria.setTaldea(taldeHelburua); // 🔹 Taldea aldatu
        return eguneratu(jokalaria);           // 🔹 Datu-basean eguneratu
    }

    /**
     * Jokalari bat datu-basetik guztiz ezabatzen du bere identifikatzailea erabiliz.
     * @param jokalariakKod Ezabatu nahi den jokalariaren identifikatzailea (ID).
     * @return true ezabaketa modu egokian burutu bada, edo false arazoren bat egon bada.
     */
    // 🔹 Jokalaria ezabatu
    public boolean jokalariaEzabatu(int jokalariakKod) {
        String sql = "DELETE FROM jokalariak WHERE jokalariak_kod = ?";
        
        try {
            konexioa.konexioaIreki();
            PreparedStatement ps = konexioa.getKonexioa().prepareStatement(sql);
            
            ps.setInt(1, jokalariakKod);
            int affected = ps.executeUpdate();
            ps.close();
            return affected > 0;
            
        } catch (SQLException e) {
            System.err.println("Errorea jokalaria ezabatzean: " + e.getMessage());
            return false;
        } finally {
            konexioa.konexioaItxi();
        }
    }
}