package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import pojos.Denboraldia;

public class DenboraldiaDAO {
    private Konexioa konexioa; // Konexio objektua DBrekin lan egiteko

    public DenboraldiaDAO() {
        // Konstruktorean konexioa sortu
        konexioa = new Konexioa();
    }
    
    // Denboraldi guztien zerrenda lortzeko metodoa
    public List<Denboraldia> denboraldiakAtera() {
        List<Denboraldia> zerrenda = new ArrayList<>();
        String sql = "SELECT denboraldia_kod, izena, hasiera_data, amaiera_data FROM denboraldia";
        
        try {
            konexioa.konexioaIreki(); // DBrekin konexioa ireki
            Statement st = konexioa.getKonexioa().createStatement();
            ResultSet rs = st.executeQuery(sql); // SQL kontsulta exekutatu
            
            while (rs.next()) { //ResultSet barruan zeuden datuak itzuli
                Denboraldia d = new Denboraldia();
                d.setDenboraldiaKod(rs.getInt("denboraldia_kod"));
                d.setIzena(rs.getString("izena"));
                d.setHasieraData(rs.getDate("hasiera_data"));
                d.setAmaieraData(rs.getDate("amaiera_data"));
                zerrenda.add(d); // Zerrendara gehitu
            }
            
            rs.close();
            st.close();
            
        } catch (SQLException e) {
            System.err.println("Errorea denboraldiak irakurtzean: " + e.getMessage());
        } finally {
            konexioa.konexioaItxi(); // Konexioa itxi
        }
        
        return zerrenda; // Denboraldi guztien zerrenda itzuli
    }

    // Denboraldi bat IDaren arabera lortzeko metodoa
    public Denboraldia denboraldiakAteraId(int denboraldiaKod) {
        Denboraldia d = null;
        String sql = "SELECT denboraldia_kod, izena, hasiera_data, amaiera_data FROM denboraldia WHERE denboraldia_kod = ?";
        
        try {
            konexioa.konexioaIreki(); // DB konexioa ireki
            PreparedStatement ps = konexioa.getKonexioa().prepareStatement(sql);
            ps.setInt(1, denboraldiaKod); // 🔹 Parametroa jarri (ID)
            
            ResultSet rs = ps.executeQuery(); // Kontsulta exekutatu
            if (rs.next()) { // Balio bat aurkitu bada
                d = new Denboraldia();
                d.setDenboraldiaKod(rs.getInt("denboraldia_kod"));
                d.setIzena(rs.getString("izena"));
                d.setHasieraData(rs.getDate("hasiera_data"));
                d.setAmaieraData(rs.getDate("amaiera_data"));
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.err.println("Errorea denboraldia IDz irakurtzean: " + e.getMessage());
        } finally {
            konexioa.konexioaItxi(); // Konexioa itxi
        }
        
        return d; // Denboraldia itzuli (aurkitu bada)
    }
    

    // Denboraldi berria datu-basean gehitzeko metodoa
    public boolean denboraldiBerria(Denboraldia denboraldia) {
        String sql = "INSERT INTO denboraldia (izena, hasiera_data, amaiera_data) VALUES (?, ?, ?)";
        
        try {
            konexioa.konexioaIreki(); // DB konexioa ireki
            PreparedStatement ps = konexioa.getKonexioa()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); // Autogeneratutako IDa lortzeko
            
            ps.setString(1, denboraldia.getIzena()); // Izena jarri
            ps.setDate(2, new java.sql.Date(denboraldia.getHasieraData().getTime())); // Hasiera data jarri
            ps.setDate(3, new java.sql.Date(denboraldia.getAmaieraData().getTime())); // Amaiera data jarri
            
            int affected = ps.executeUpdate(); // Datuak gehitu
            
            if (affected > 0) {
                // Autogeneratutako IDa lortu
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) denboraldia.setDenboraldiaKod(rs.getInt(1));
                rs.close();
            }
            
            ps.close();
            return affected > 0; // True itzuli, insert egokia bada
        } catch (SQLException e) {
            System.err.println("Errorea denboraldia gehitzean: " + e.getMessage());
            return false;
        } finally {
            konexioa.konexioaItxi(); // Konexioa itxi
        }
    }
    
    public boolean eguneratu(Denboraldia denboraldia) {
        String sql = "UPDATE denboraldia SET amaituta = ?, txapelduna = ? WHERE denboraldia_kod = ?";

        try {
            konexioa.konexioaIreki();
            PreparedStatement ps = konexioa.getKonexioa().prepareStatement(sql);

            ps.setBoolean(1, denboraldia.isAmaituta());
            ps.setString(2, denboraldia.getTxapelduna());
            ps.setInt(3, denboraldia.getDenboraldiaKod());

            int rows = ps.executeUpdate();

            ps.close();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("Errorea denboraldia eguneratzean: " + e.getMessage());
            return false;
        } finally {
            konexioa.konexioaItxi();
        }
    }
}