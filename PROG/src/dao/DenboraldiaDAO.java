package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import pojos.Denboraldia;

public class DenboraldiaDAO {
    private Konexioa konexioa;

    public DenboraldiaDAO() {
        konexioa = new Konexioa();
    }
    
    public List<Denboraldia> denboraldiakAtera() {
        List<Denboraldia> zerrenda = new ArrayList<>();
        // KENDU: amaituta zutabea
        String sql = "SELECT denboraldia_kod, izena, hasiera_data, amaiera_data, txapelduna, aktiboa FROM denboraldia ORDER BY hasiera_data DESC";
        
        try {
            konexioa.konexioaIreki();
            Statement st = konexioa.getKonexioa().createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            while (rs.next()) {
                Denboraldia d = new Denboraldia();
                d.setDenboraldiaKod(rs.getInt("denboraldia_kod"));
                d.setIzena(rs.getString("izena"));
                d.setHasieraData(rs.getDate("hasiera_data"));
                d.setAmaieraData(rs.getDate("amaiera_data"));
                // d.setAmaituta(rs.getBoolean("amaituta")); // KENDU
                d.setTxapelduna(rs.getString("txapelduna"));
                d.setAktiboa(rs.getBoolean("aktiboa"));
                zerrenda.add(d);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.err.println("Errorea denboraldiak irakurtzean: " + e.getMessage());
        } finally {
            konexioa.konexioaItxi();
        }
        return zerrenda;
    }

    public Denboraldia denboraldiakAteraId(int denboraldiaKod) {
        Denboraldia d = null;
        String sql = "SELECT denboraldia_kod, izena, hasiera_data, amaiera_data, txapelduna, aktiboa FROM denboraldia WHERE denboraldia_kod = ?";
        try {
            konexioa.konexioaIreki();
            PreparedStatement ps = konexioa.getKonexioa().prepareStatement(sql);
            ps.setInt(1, denboraldiaKod);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                d = new Denboraldia();
                d.setDenboraldiaKod(rs.getInt("denboraldia_kod"));
                d.setIzena(rs.getString("izena"));
                d.setHasieraData(rs.getDate("hasiera_data"));
                d.setAmaieraData(rs.getDate("amaiera_data"));
                // d.setAmaituta(rs.getBoolean("amaituta")); // KENDU
                d.setTxapelduna(rs.getString("txapelduna"));
                d.setAktiboa(rs.getBoolean("aktiboa"));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.err.println("Errorea denboraldia IDz irakurtzean: " + e.getMessage());
        } finally {
            konexioa.konexioaItxi();
        }
        return d;
    }
    
    public Denboraldia denboraldiaAktiboaLortu() {
        Denboraldia d = null;
        String sql = "SELECT denboraldia_kod, izena, hasiera_data, amaiera_data, txapelduna, aktiboa FROM denboraldia WHERE aktiboa = true";
        try {
            konexioa.konexioaIreki();
            Statement st = konexioa.getKonexioa().createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                d = new Denboraldia();
                d.setDenboraldiaKod(rs.getInt("denboraldia_kod"));
                d.setIzena(rs.getString("izena"));
                d.setHasieraData(rs.getDate("hasiera_data"));
                d.setAmaieraData(rs.getDate("amaiera_data"));
                // d.setAmaituta(rs.getBoolean("amaituta")); // KENDU
                d.setTxapelduna(rs.getString("txapelduna"));
                d.setAktiboa(true);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.err.println("Errorea denboraldi aktiboa lortzean: " + e.getMessage());
        } finally {
            konexioa.konexioaItxi();
        }
        return d;
    }

    public int denboraldiaHasi(Denboraldia denboraldia) {
        // KENDU: amaituta zutabea INSERT-etik
        String sqlInsert = "INSERT INTO denboraldia (izena, hasiera_data, amaiera_data, aktiboa) VALUES (?, ?, ?, true)";
        String sqlUpdate = "UPDATE denboraldia SET aktiboa = false WHERE aktiboa = true";
        Connection conn = null;
        try {
            konexioa.konexioaIreki();
            conn = konexioa.getKonexioa();
            conn.setAutoCommit(false);
            
            try (PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate)) {
                psUpdate.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Ez zegoen denboraldi aktiborik desaktibatzeko.");
            }
            
            int newId = -1;
            try (PreparedStatement psInsert = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
                psInsert.setString(1, denboraldia.getIzena());
                psInsert.setDate(2, new java.sql.Date(denboraldia.getHasieraData().getTime()));
                psInsert.setDate(3, new java.sql.Date(denboraldia.getAmaieraData().getTime()));
                psInsert.executeUpdate();
                
                ResultSet rs = psInsert.getGeneratedKeys();
                if (rs.next()) {
                    newId = rs.getInt(1);
                    denboraldia.setDenboraldiaKod(newId);
                    denboraldia.setAktiboa(true);
                }
                rs.close();
            }
            
            conn.commit();
            return newId;
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) {
                    System.err.println("Errorea rollback egitean: " + ex.getMessage());
                }
            }
            System.err.println("Errorea denboraldia hasterakoan: " + e.getMessage());
            e.printStackTrace();
            return -1;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); } catch (SQLException e) {}
            }
            konexioa.konexioaItxi();
        }
    }

    public boolean denboraldiaAmaitu(String txapelduna) {
        // KENDU: amaituta zutabea UPDATE-tik
        String sql = "UPDATE denboraldia SET aktiboa = false, txapelduna = ? WHERE aktiboa = true";
        try {
            konexioa.konexioaIreki();
            PreparedStatement ps = konexioa.getKonexioa().prepareStatement(sql);
            ps.setString(1, txapelduna);
            int rows = ps.executeUpdate();
            ps.close();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Errorea denboraldia amaitzean: " + e.getMessage());
            return false;
        } finally {
            konexioa.konexioaItxi();
        }
    }

    public boolean eguneratu(Denboraldia denboraldia) {
        // KENDU: amaituta zutabea UPDATE-tik
        String sql = "UPDATE denboraldia SET izena=?, hasiera_data=?, amaiera_data=?, txapelduna=?, aktiboa=? WHERE denboraldia_kod=?";
        try {
            konexioa.konexioaIreki();
            PreparedStatement ps = konexioa.getKonexioa().prepareStatement(sql);
            ps.setString(1, denboraldia.getIzena());
            ps.setDate(2, new java.sql.Date(denboraldia.getHasieraData().getTime()));
            ps.setDate(3, new java.sql.Date(denboraldia.getAmaieraData().getTime()));
            // ps.setBoolean(4, denboraldia.isAmaituta()); // KENDU
            ps.setString(4, denboraldia.getTxapelduna());
            ps.setBoolean(5, denboraldia.isAktiboa());
            ps.setInt(6, denboraldia.getDenboraldiaKod());
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