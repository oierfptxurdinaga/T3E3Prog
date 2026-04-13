package dao;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import pojos.Jokalaria;
import pojos.Taldea;

public class JokalariaDAO {
    private Konexioa konexioa; // Datu-basearekin konektatzeko objektua

    public JokalariaDAO() {
        konexioa = new Konexioa(); // Konstruktorean konekzioa sortu
    }

    // Jokalari berri bat sortzeko metodoa
    public boolean jokalariaSortu(Jokalaria jokalaria) {
        String sql = "INSERT INTO jokalariak (izena, abizena, NAN, posizioa, pisua, altuera, herritartasuna, taldea_kod) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
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

    // Datu-baseko jokalari guztiak lortu
    public List<Jokalaria> jokalariGuztiakLortu() {
        List<Jokalaria> zerrenda = new ArrayList<>();
        String sql = "SELECT j.jokalariak_kod, j.izena, j.abizena, j.NAN, j.posizioa, j.pisua, j.altuera, j.herritartasuna, j.taldea_kod, "
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

    // IDz jokalaria lortu
    public Jokalaria jokalariaLortuIdBidez(int jokalariakKod) {
        Jokalaria j = null;
        String sql = "SELECT j.jokalariak_kod, j.izena, j.abizena, j.NAN, j.posizioa, j.pisua, j.altuera, j.herritartasuna, j.taldea_kod, "
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

    // Talde bateko jokalariak lortu
    public List<Jokalaria> jokalariaLortuTaldeBidez(int taldeaKod) {
        List<Jokalaria> zerrenda = new ArrayList<>();
        String sql = "SELECT jokalariak_kod, izena, abizena, NAN, posizioa, pisua, altuera, herritartasuna "
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

    // Jokalari baten datuak eguneratu
    public boolean eguneratu(Jokalaria jokalaria) {
        String sql = "UPDATE jokalariak SET izena = ?, abizena = ?, NAN = ?, posizioa = ?, pisua = ?, altuera = ?, herritartasuna = ?, taldea_kod = ? "
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

            ps.setInt(9, jokalaria.getJokalariakKod());

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

    // Jokalaria talde berri batera traspasatu
    public boolean traspasatu(Jokalaria jokalaria, Taldea taldeHelburua) {
        if (jokalaria == null || taldeHelburua == null)
            throw new IllegalArgumentException("Jokalaria eta talde helburua ezin dira null izan.");
        if (jokalaria.getTaldea() != null && jokalaria.getTaldea().getTaldeaKod() == taldeHelburua.getTaldeaKod())
            throw new IllegalArgumentException("Jokalaria dagoeneko talde horretan dago.");

        jokalaria.setTaldea(taldeHelburua); // 🔹 Taldea aldatu
        return eguneratu(jokalaria);           // 🔹 Datu-basean eguneratu
    }

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