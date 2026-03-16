package Erronka2.model.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Erronka2.model.pojos.Denboraldia;

public class DenboraldiaDAO {
    private Konexioa konexioa;

    public DenboraldiaDAO() {
        konexioa = new Konexioa();
    }

    public List<Denboraldia> getAll() {
        List<Denboraldia> zerrenda = new ArrayList<>();
        String sql = "SELECT denboraldia_kod, izena, hasiera_data, amaiera_data FROM denboraldia";
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

    public Denboraldia getById(int denboraldiaKod) {
        Denboraldia d = null;
        String sql = "SELECT denboraldia_kod, izena, hasiera_data, amaiera_data FROM denboraldia WHERE denboraldia_kod = ?";
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

    public boolean insert(Denboraldia denboraldia) {
        String sql = "INSERT INTO denboraldia (izena, hasiera_data, amaiera_data) VALUES (?, ?, ?)";
        try {
            konexioa.konexioaIreki();
            PreparedStatement ps = konexioa.getKonexioa().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, denboraldia.getIzena());
            ps.setDate(2, new java.sql.Date(denboraldia.getHasieraData().getTime()));
            ps.setDate(3, new java.sql.Date(denboraldia.getAmaieraData().getTime()));
            int affected = ps.executeUpdate();
            if (affected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) denboraldia.setDenboraldiaKod(rs.getInt(1));
                rs.close();
            }
            ps.close();
            return affected > 0;
        } catch (SQLException e) {
            System.err.println("Errorea denboraldia gehitzean: " + e.getMessage());
            return false;
        } finally {
            konexioa.konexioaItxi();
        }
    }
}