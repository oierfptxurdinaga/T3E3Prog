package Erronka2.model.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Erronka2.model.pojos.Jardunaldia;

public class JardunaldiaDAO {
    private Konexioa konexioa;

    public JardunaldiaDAO() {
        konexioa = new Konexioa();
    }

   

    public List<Jardunaldia> getByDenboraldia(int denboraldiaKod) {
        List<Jardunaldia> zerrenda = new ArrayList<>();
        String sql = "SELECT j.jaurdunaldi_kod, j.hasiera_data, j.amaiera_data " +
                     "FROM jaurdunaldia j " +
                     "INNER JOIN denboraldia_jaurdunaldia dj ON j.jaurdunaldi_kod = dj.jaurdunaldi_kod " +
                     "WHERE dj.denboraldia_kod = ?";
        try {
            konexioa.konexioaIreki();
            PreparedStatement ps = konexioa.getKonexioa().prepareStatement(sql);
            ps.setInt(1, denboraldiaKod);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Jardunaldia j = new Jardunaldia();
                j.setJardunaldiKod(rs.getInt("jaurdunaldi_kod"));
                j.setHasieraData(rs.getDate("hasiera_data"));
                j.setAmaieraData(rs.getDate("amaiera_data"));
                zerrenda.add(j);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.err.println("Errorea jardunaldiak denboraldiaren arabera irakurtzean: " + e.getMessage());
        } finally {
            konexioa.konexioaItxi();
        }
        return zerrenda;
    }

    // Nuevo: insertar una jornada y devolver su ID generado
    public int insert(Jardunaldia jardunaldia) {
        String sql = "INSERT INTO jaurdunaldia (hasiera_data, amaiera_data) VALUES (?, ?)";
        try {
            konexioa.konexioaIreki();
            PreparedStatement ps = konexioa.getKonexioa().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setDate(1, new java.sql.Date(jardunaldia.getHasieraData().getTime()));
            ps.setDate(2, new java.sql.Date(jardunaldia.getAmaieraData().getTime()));
            int affected = ps.executeUpdate();
            if (affected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    jardunaldia.setJardunaldiKod(id);
                    return id;
                }
                rs.close();
            }
            ps.close();
        } catch (SQLException e) {
            System.err.println("Errorea jardunaldia gehitzean: " + e.getMessage());
        } finally {
            konexioa.konexioaItxi();
        }
        return -1;
    }

    // Nuevo: asociar una jornada a una temporada
    public boolean asociarATemporada(int denboraldiaKod, int jardunaldiKod) {
        String sql = "INSERT INTO denboraldia_jaurdunaldia (denboraldia_kod, jaurdunaldi_kod) VALUES (?, ?)";
        try {
            konexioa.konexioaIreki();
            PreparedStatement ps = konexioa.getKonexioa().prepareStatement(sql);
            ps.setInt(1, denboraldiaKod);
            ps.setInt(2, jardunaldiKod);
            int affected = ps.executeUpdate();
            ps.close();
            return affected > 0;
        } catch (SQLException e) {
            System.err.println("Errorea jardunaldia temporadarekin lotzean: " + e.getMessage());
            return false;
        } finally {
            konexioa.konexioaItxi();
        }
    }
}