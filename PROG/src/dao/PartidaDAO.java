package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import pojos.Epailea;
import pojos.Jardunaldia;
import pojos.Partida;
import pojos.Taldea;
import util.LoggerUtil;
import util.LoggerUtil.DataAccessException;

/**
 * Partida entitatearen DAO klasea. Partidekin lotutako datu-base eragiketak
 * kudeatzen ditu.
 */
public class PartidaDAO {
	private Konexioa konexioa;

	public PartidaDAO() {
		konexioa = new Konexioa();
	}

	/**
	 * Partida berri bat gordetzen du, talde eta jardunaldiarekiko erlazioekin.
	 * 
	 * @param partida Gorde nahi den partida.
	 * @throws DataAccessException Errorea badago datu-basean.
	 */
	public void sortuPartida(Partida partida) throws DataAccessException {
		String sql = "INSERT INTO partida (data, ordua, emaitza, zigorrak, txartelak, epailea_kod) VALUES (?, ?, ?, ?, ?, ?)";
		try {
			konexioa.konexioaIreki();
			Connection conn = konexioa.getKonexioa();
			try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
				ps.setDate(1, new java.sql.Date(partida.getData().getTime()));
				ps.setString(2, partida.getOrdua());
				ps.setString(3, partida.getEmaitza());
				ps.setInt(4, partida.getZigorrak());
				ps.setInt(5, partida.getTxartelak());

				if (partida.getEpailea() != null) {
					ps.setInt(6, partida.getEpailea().getEpaileaKod());
				} else {
					ps.setNull(6, Types.INTEGER);
				}

				int affected = ps.executeUpdate();
				if (affected == 0) {
					throw new DataAccessException("Ez da partida txertatu.");
				}

				try (ResultSet rs = ps.getGeneratedKeys()) {
					if (rs.next()) {
						partida.setPartidaKod(rs.getInt(1));
					}
				}

				// Taldeekiko erlazioa gorde
				insertatuTaldeaPartida(partida, conn);
				// Jardunaldiarekiko erlazioa gorde
				insertatuPartidaJardunaldia(partida, conn);
			}

			LoggerUtil.log("Partida gorde da: " + partida.getEtxekoTaldea().getIzena() + " vs "
					+ partida.getKanpokoTaldea().getIzena() + " (" + partida.getEmaitza() + ")");
		} catch (SQLException e) {
			LoggerUtil.log("ERROR partida sortzean: " + e.getMessage());
			throw new DataAccessException("Errorea partida sortzean", e);
		} finally {
			konexioa.konexioaItxi();
		}
	}

	/**
	 * Etxeko eta kanpoko taldeen erlazioa gordetzen du (barneko metodoa).
	 */
	private void insertatuTaldeaPartida(Partida partida, Connection conn) throws SQLException {
		if (partida.getEtxekoTaldea() != null && partida.getKanpokoTaldea() != null) {
			String sql = "INSERT INTO taldea_partida (taldea_kod, partida_kod) VALUES (?, ?)";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setInt(1, partida.getEtxekoTaldea().getTaldeaKod());
				ps.setInt(2, partida.getPartidaKod());
				ps.executeUpdate();

				ps.setInt(1, partida.getKanpokoTaldea().getTaldeaKod());
				ps.executeUpdate();
			}
		}
	}

	/**
	 * Partida-jardunaldia erlazioa gordetzen du (barneko metodoa).
	 */
	private void insertatuPartidaJardunaldia(Partida partida, Connection conn) throws SQLException {
		if (partida.getJardunaldia() != null) {
			String sql = "INSERT INTO partida_jaurdunaldia (partida_kod, jaurdunaldi_kod) VALUES (?, ?)";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setInt(1, partida.getPartidaKod());
				ps.setInt(2, partida.getJardunaldia().getJardunaldiKod());
				ps.executeUpdate();
			}
		}
	}

	/**
	 * Partida guztiak lortzen ditu, epaile, jardunaldi eta taldeekin.
	 * 
	 * @return Partiden zerrenda.
	 * @throws DataAccessException Errorea badago datu-basean.
	 */
	public List<Partida> partidaGuztiak() throws DataAccessException {
		List<Partida> zerrenda = new ArrayList<>();
		String sql = "SELECT p.partida_kod, p.data, p.ordua, p.emaitza, p.zigorrak, p.txartelak, p.epailea_kod, "
				+ "e.izena AS epaile_izena, e.abizena AS epaile_abizena, " + "tp.taldea_kod, t.izena AS talde_izena, "
				+ "pj.jaurdunaldi_kod, j.hasiera_data AS j_hasiera, j.amaiera_data AS j_amaiera " + "FROM partida p "
				+ "LEFT JOIN epailea e ON p.epailea_kod = e.epailea_kod "
				+ "LEFT JOIN taldea_partida tp ON p.partida_kod = tp.partida_kod "
				+ "LEFT JOIN taldea t ON tp.taldea_kod = t.taldea_kod "
				+ "LEFT JOIN partida_jaurdunaldia pj ON p.partida_kod = pj.partida_kod "
				+ "LEFT JOIN jaurdunaldia j ON pj.jaurdunaldi_kod = j.jaurdunaldi_kod " + "ORDER BY p.partida_kod";

		try {
			konexioa.konexioaIreki();
			try (Statement st = konexioa.getKonexioa().createStatement(); ResultSet rs = st.executeQuery(sql)) {

				Partida partida = null;
				int azkenId = -1;

				while (rs.next()) {
					int id = rs.getInt("partida_kod");
					if (id != azkenId) {
						partida = mapeatuPartida(rs);
						zerrenda.add(partida);
						azkenId = id;
					}
					gehituTaldeaPartidari(partida, rs);
				}
			}
		} catch (SQLException e) {
			LoggerUtil.log("ERROR partidak lortzean: " + e.getMessage());
			throw new DataAccessException("Errorea partidak lortzean", e);
		} finally {
			konexioa.konexioaItxi();
		}
		return zerrenda;
	}

	/**
	 * Denboraldi bateko partida guztiak lortzen ditu.
	 * 
	 * @param denboraldiaKod Denboraldiaren identifikatzailea.
	 * @return Denboraldiko partiden zerrenda.
	 * @throws DataAccessException Errorea badago datu-basean.
	 */
	public List<Partida> partidaLortuDenboraldiBitartez(int denboraldiaKod) throws DataAccessException {
		List<Partida> zerrenda = new ArrayList<>();
		String sql = "SELECT p.partida_kod, p.data, p.ordua, p.emaitza, p.zigorrak, p.txartelak, p.epailea_kod, "
				+ "e.izena AS epaile_izena, e.abizena AS epaile_abizena, " + "tp.taldea_kod, t.izena AS talde_izena, "
				+ "pj.jaurdunaldi_kod, j.hasiera_data AS j_hasiera, j.amaiera_data AS j_amaiera " + "FROM partida p "
				+ "LEFT JOIN epailea e ON p.epailea_kod = e.epailea_kod "
				+ "LEFT JOIN taldea_partida tp ON p.partida_kod = tp.partida_kod "
				+ "LEFT JOIN taldea t ON tp.taldea_kod = t.taldea_kod "
				+ "LEFT JOIN partida_jaurdunaldia pj ON p.partida_kod = pj.partida_kod "
				+ "LEFT JOIN jaurdunaldia j ON pj.jaurdunaldi_kod = j.jaurdunaldi_kod "
				+ "WHERE pj.jaurdunaldi_kod IN (SELECT jaurdunaldi_kod FROM denboraldia_jaurdunaldia WHERE denboraldia_kod = ?) "
				+ "ORDER BY p.partida_kod";

		try {
			konexioa.konexioaIreki();
			try (PreparedStatement ps = konexioa.getKonexioa().prepareStatement(sql)) {
				ps.setInt(1, denboraldiaKod);
				try (ResultSet rs = ps.executeQuery()) {
					Partida partida = null;
					int azkenId = -1;

					while (rs.next()) {
						int id = rs.getInt("partida_kod");
						if (id != azkenId) {
							partida = mapeatuPartida(rs);
							zerrenda.add(partida);
							azkenId = id;
						}
						gehituTaldeaPartidari(partida, rs);
					}
				}
			}
		} catch (SQLException e) {
			LoggerUtil.log("ERROR partidak denboraldika lortzean: " + e.getMessage());
			throw new DataAccessException("Errorea partidak denboraldika lortzean", e);
		} finally {
			konexioa.konexioaItxi();
		}
		return zerrenda;
	}

	/**
	 * ResultSet batetik Partida objektua sortzen du (epailea eta jardunaldiarekin).
	 */
	private Partida mapeatuPartida(ResultSet rs) throws SQLException {
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

	/**
	 * Partidari taldea gehitzen dio (etxekoa edo kanpokoa).
	 */
	private void gehituTaldeaPartidari(Partida p, ResultSet rs) throws SQLException {
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
}