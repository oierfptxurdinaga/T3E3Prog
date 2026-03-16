package Erronka2.model.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Erronka2.model.pojos.Epailea;
import Erronka2.model.pojos.Jardunaldia;
import Erronka2.model.pojos.Partida;
import Erronka2.model.pojos.Taldea;

public class PartidaDAO {
    private Konexioa konexioa;

    public PartidaDAO() {
        konexioa = new Konexioa();
    }

    public boolean insert(Partida partida) {
        String sql = "INSERT INTO partida (data, ordua, emaitza, zigorrak, txartelak, epailea_kod) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            konexioa.konexioaIreki();
            PreparedStatement ps = konexioa.getKonexioa().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setDate(1, new java.sql.Date(partida.getData().getTime()));
            ps.setString(2, partida.getOrdua());
            ps.setString(3, partida.getEmaitza());
            ps.setInt(4, partida.getZigorrak());
            ps.setInt(5, partida.getTxartelak());
            if (partida.getEpailea() != null)
                ps.setInt(6, partida.getEpailea().getEpaileaKod());
            else
                ps.setNull(6, java.sql.Types.INTEGER);
            int affected = ps.executeUpdate();
            if (affected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    partida.setPartidaKod(rs.getInt(1));
                    insertTaldeaPartida(partida);
                    insertPartidaJardunaldia(partida);
                }
                rs.close();
            }
            ps.close();
            return affected > 0;
        } catch (SQLException e) {
            System.err.println("Errorea partida gehitzean: " + e.getMessage());
            return false;
        } finally {
            konexioa.konexioaItxi();
        }
    }

    private void insertTaldeaPartida(Partida partida) throws SQLException {
        if (partida.getEtxekoTaldea() != null && partida.getKanpokoTaldea() != null) {
            String sql = "INSERT INTO taldea_partida (taldea_kod, partida_kod) VALUES (?, ?)";
            PreparedStatement ps = konexioa.getKonexioa().prepareStatement(sql);
            ps.setInt(1, partida.getEtxekoTaldea().getTaldeaKod());
            ps.setInt(2, partida.getPartidaKod());
            ps.executeUpdate();
            ps.setInt(1, partida.getKanpokoTaldea().getTaldeaKod());
            ps.executeUpdate();
            ps.close();
        }
    }

    private void insertPartidaJardunaldia(Partida partida) throws SQLException {
        if (partida.getJardunaldia() != null) {
            String sql = "INSERT INTO partida_jaurdunaldia (partida_kod, jaurdunaldi_kod) VALUES (?, ?)";
            PreparedStatement ps = konexioa.getKonexioa().prepareStatement(sql);
            ps.setInt(1, partida.getPartidaKod());
            ps.setInt(2, partida.getJardunaldia().getJardunaldiKod());
            ps.executeUpdate();
            ps.close();
        }
    }

    private Partida mapPartida(ResultSet rs) throws SQLException {
        Partida p = new Partida();
        p.setPartidaKod(rs.getInt("partida_kod"));
        p.setData(rs.getDate("data"));
        p.setOrdua(rs.getString("ordua"));
        p.setEmaitza(rs.getString("emaitza"));
        p.setZigorrak(rs.getInt("zigorrak"));
        p.setTxartelak(rs.getInt("txartelak"));
        if (rs.getObject("epailea_kod") != null) {
            Epailea e = new Epailea();
            e.setEpaileaKod(rs.getInt("epailea_kod"));
            e.setIzena(rs.getString("epaile_izena"));
            e.setAbizena(rs.getString("epaile_abizena"));
            p.setEpailea(e);
        }
        if (rs.getObject("jaurdunaldi_kod") != null) {
            Jardunaldia j = new Jardunaldia();
            j.setJardunaldiKod(rs.getInt("jaurdunaldi_kod"));
            j.setHasieraData(rs.getDate("j_hasiera"));
            j.setAmaieraData(rs.getDate("j_amaiera"));
            p.setJardunaldia(j);
        }
        return p;
    }

    private void addEquipoToPartida(Partida p, ResultSet rs) throws SQLException {
        if (rs.getObject("taldea_kod") != null) {
            Taldea t = new Taldea();
            t.setTaldeaKod(rs.getInt("taldea_kod"));
            t.setIzena(rs.getString("talde_izena"));
            if (p.getEtxekoTaldea() == null) {
                p.setEtxekoTaldea(t);
            } else if (p.getKanpokoTaldea() == null) {
                p.setKanpokoTaldea(t);
            }
        }
    }

    public List<Partida> getAll() {
        List<Partida> zerrenda = new ArrayList<>();
        String sql = "SELECT p.partida_kod, p.data, p.ordua, p.emaitza, p.zigorrak, p.txartelak, p.epailea_kod, " +
                     "e.izena as epaile_izena, e.abizena as epaile_abizena, " +
                     "tp.taldea_kod, t.izena as talde_izena, " +
                     "pj.jaurdunaldi_kod, j.hasiera_data as j_hasiera, j.amaiera_data as j_amaiera " +
                     "FROM partida p " +
                     "LEFT JOIN epailea e ON p.epailea_kod = e.epailea_kod " +
                     "LEFT JOIN taldea_partida tp ON p.partida_kod = tp.partida_kod " +
                     "LEFT JOIN taldea t ON tp.taldea_kod = t.taldea_kod " +
                     "LEFT JOIN partida_jaurdunaldia pj ON p.partida_kod = pj.partida_kod " +
                     "LEFT JOIN jaurdunaldia j ON pj.jaurdunaldi_kod = j.jaurdunaldi_kod " +
                     "ORDER BY p.partida_kod";
        try {
            konexioa.konexioaIreki();
            Statement st = konexioa.getKonexioa().createStatement();
            ResultSet rs = st.executeQuery(sql);
            Partida partida = null;
            int lastId = -1;
            while (rs.next()) {
                int id = rs.getInt("partida_kod");
                if (id != lastId) {
                    partida = mapPartida(rs);
                    zerrenda.add(partida);
                    lastId = id;
                }
                addEquipoToPartida(partida, rs);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.err.println("Errorea partidak irakurtzean: " + e.getMessage());
        } finally {
            konexioa.konexioaItxi();
        }
        return zerrenda;
    }

    public List<Partida> getByDenboraldia(int denboraldiaKod) {
        List<Partida> zerrenda = new ArrayList<>();
        String sql = "SELECT p.partida_kod, p.data, p.ordua, p.emaitza, p.zigorrak, p.txartelak, p.epailea_kod, " +
                     "e.izena as epaile_izena, e.abizena as epaile_abizena, " +
                     "tp.taldea_kod, t.izena as talde_izena, " +
                     "pj.jaurdunaldi_kod, j.hasiera_data as j_hasiera, j.amaiera_data as j_amaiera " +
                     "FROM partida p " +
                     "LEFT JOIN epailea e ON p.epailea_kod = e.epailea_kod " +
                     "LEFT JOIN taldea_partida tp ON p.partida_kod = tp.partida_kod " +
                     "LEFT JOIN taldea t ON tp.taldea_kod = t.taldea_kod " +
                     "LEFT JOIN partida_jaurdunaldia pj ON p.partida_kod = pj.partida_kod " +
                     "LEFT JOIN jaurdunaldia j ON pj.jaurdunaldi_kod = j.jaurdunaldi_kod " +
                     "WHERE pj.jaurdunaldi_kod IN (SELECT jaurdunaldi_kod FROM denboraldia_jaurdunaldia WHERE denboraldia_kod = ?) " +
                     "ORDER BY p.partida_kod";
        try {
            konexioa.konexioaIreki();
            PreparedStatement ps = konexioa.getKonexioa().prepareStatement(sql);
            ps.setInt(1, denboraldiaKod);
            ResultSet rs = ps.executeQuery();
            Partida partida = null;
            int lastId = -1;
            while (rs.next()) {
                int id = rs.getInt("partida_kod");
                if (id != lastId) {
                    partida = mapPartida(rs);
                    zerrenda.add(partida);
                    lastId = id;
                }
                addEquipoToPartida(partida, rs);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.err.println("Errorea partidak denboraldiaren arabera irakurtzean: " + e.getMessage());
        } finally {
            konexioa.konexioaItxi();
        }
        return zerrenda;
    }
}