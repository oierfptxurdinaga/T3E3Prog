package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import pojos.Taldea;
import pojos.Zelaia;
import util.LoggerUtil;
import util.LoggerUtil.DataAccessException;

/**
 * Taldea entitatearen DAO klasea. Taldeekin lotutako datu-base eragiketak
 * kudeatzen ditu.
 */
public class TaldeaDAO {
	private Konexioa konexioa;

	public TaldeaDAO() {
		konexioa = new Konexioa();
	}

	/**
	 * Talde berri bat datu-basean gordetzen du.
	 * 
	 * @param taldea Gorde nahi den taldea.
	 * @throws DataAccessException Errorea badago datu-basean.
	 */
	public void taldeaSortu(Taldea taldea) throws DataAccessException {
		String sql = "INSERT INTO taldea (izena, sortze_data, zelaia) VALUES (?, ?, ?)";
		try {
			konexioa.konexioaIreki();
			Connection conn = konexioa.getKonexioa();
			try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
				ps.setString(1, taldea.getIzena());
				if (taldea.getSortzeData() != null) {
					ps.setDate(2, new java.sql.Date(taldea.getSortzeData().getTime()));
				} else {
					ps.setNull(2, Types.DATE);
				}
				if (taldea.getZelaia() != null) {
					ps.setInt(3, taldea.getZelaia().getZelaiaKod());
				} else {
					ps.setNull(3, Types.INTEGER);
				}

				int affected = ps.executeUpdate();
				if (affected == 0) {
					throw new DataAccessException("Ez da taldea txertatu.");
				}
				try (ResultSet rs = ps.getGeneratedKeys()) {
					if (rs.next()) {
						taldea.setTaldeaKod(rs.getInt(1));
					}
				}
			}
			LoggerUtil.log("Taldea sortu da: " + taldea.getIzena() + " (ID: " + taldea.getTaldeaKod() + ")");
		} catch (SQLException e) {
			LoggerUtil.log("ERROR taldea sortzean: " + e.getMessage());
			throw new DataAccessException("Errorea taldea sortzean", e);
		} finally {
			konexioa.konexioaItxi();
		}
	}

	/**
	 * Talde guztiak lortzen ditu zelaiaren datuekin batera.
	 * 
	 * @return Taldeen zerrenda.
	 * @throws DataAccessException Errorea badago datu-basean.
	 */
	public List<Taldea> taldeGuztiakLortu() throws DataAccessException {
		List<Taldea> zerrenda = new ArrayList<>();
		String sql = "SELECT t.taldea_kod, t.izena, t.sortze_data, t.zelaia, "
				+ "z.izena AS zelai_izena, z.kokapena, z.kapazitatea "
				+ "FROM taldea t LEFT JOIN zelaia z ON t.zelaia = z.zelaia_kod";

		try {
			konexioa.konexioaIreki();
			try (Statement st = konexioa.getKonexioa().createStatement(); ResultSet rs = st.executeQuery(sql)) {
				while (rs.next()) {
					Taldea t = mapeatuTaldea(rs);
					zerrenda.add(t);
				}
			}
		} catch (SQLException e) {
			LoggerUtil.log("ERROR taldeak lortzean: " + e.getMessage());
			throw new DataAccessException("Errorea taldeak lortzean", e);
		} finally {
			konexioa.konexioaItxi();
		}
		return zerrenda;
	}

	/**
	 * Talde bat bilatzen du bere IDaren arabera.
	 * 
	 * @param taldeaKod Taldearen identifikatzailea.
	 * @return Aurkitutako taldea, edo null.
	 * @throws DataAccessException Errorea badago datu-basean.
	 */
	public Taldea TaldeLortuIdBidez(int taldeaKod) throws DataAccessException {
		String sql = "SELECT t.taldea_kod, t.izena, t.sortze_data, t.zelaia, "
				+ "z.izena AS zelai_izena, z.kokapena, z.kapazitatea "
				+ "FROM taldea t LEFT JOIN zelaia z ON t.zelaia = z.zelaia_kod " + "WHERE t.taldea_kod = ?";
		try {
			konexioa.konexioaIreki();
			try (PreparedStatement ps = konexioa.getKonexioa().prepareStatement(sql)) {
				ps.setInt(1, taldeaKod);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						return mapeatuTaldea(rs);
					}
				}
			}
		} catch (SQLException e) {
			LoggerUtil.log("ERROR taldea IDz lortzean: " + e.getMessage());
			throw new DataAccessException("Errorea taldea lortzean", e);
		} finally {
			konexioa.konexioaItxi();
		}
		return null;
	}

	/**
	 * Talde baten datuak eguneratzen ditu.
	 * 
	 * @param taldea Eguneratutako datuak dituen taldea.
	 * @throws DataAccessException Errorea badago datu-basean.
	 */
	public void TaldeaEguneratu(Taldea taldea) throws DataAccessException {
		String sql = "UPDATE taldea SET izena = ?, sortze_data = ?, zelaia = ? WHERE taldea_kod = ?";
		try {
			konexioa.konexioaIreki();
			try (PreparedStatement ps = konexioa.getKonexioa().prepareStatement(sql)) {
				ps.setString(1, taldea.getIzena());
				if (taldea.getSortzeData() != null) {
					ps.setDate(2, new java.sql.Date(taldea.getSortzeData().getTime()));
				} else {
					ps.setNull(2, Types.DATE);
				}
				if (taldea.getZelaia() != null) {
					ps.setInt(3, taldea.getZelaia().getZelaiaKod());
				} else {
					ps.setNull(3, Types.INTEGER);
				}
				ps.setInt(4, taldea.getTaldeaKod());

				int affected = ps.executeUpdate();
				if (affected == 0) {
					throw new DataAccessException("Ez da talderik eguneratu (ID ez da aurkitu).");
				}
			}
			LoggerUtil.log("Taldea eguneratu da: " + taldea.getIzena() + " (ID: " + taldea.getTaldeaKod() + ")");
		} catch (SQLException e) {
			LoggerUtil.log("ERROR taldea eguneratzean: " + e.getMessage());
			throw new DataAccessException("Errorea taldea eguneratzean", e);
		} finally {
			konexioa.konexioaItxi();
		}
	}

	/**
	 * Talde bat eta bere jokalari guztiak ezabatzen ditu.
	 * 
	 * @param taldeaKod Ezabatu nahi den taldearen IDa.
	 * @throws DataAccessException Errorea badago datu-basean.
	 */
	public void TaldeEzabatu(int taldeaKod) throws DataAccessException {
		String sqlDeleteJokalariak = "DELETE FROM jokalariak WHERE taldea_kod = ?";
		String sqlDeleteTaldea = "DELETE FROM taldea WHERE taldea_kod = ?";
		Connection conn = null;
		try {
			konexioa.konexioaIreki();
			conn = konexioa.getKonexioa();
			conn.setAutoCommit(false);

			// 1. Taldeko jokalariak ezabatu
			try (PreparedStatement psJok = conn.prepareStatement(sqlDeleteJokalariak)) {
				psJok.setInt(1, taldeaKod);
				psJok.executeUpdate();
			}

			// 2. Taldea ezabatu
			int affected;
			try (PreparedStatement psTal = conn.prepareStatement(sqlDeleteTaldea)) {
				psTal.setInt(1, taldeaKod);
				affected = psTal.executeUpdate();
			}

			if (affected == 0) {
				conn.rollback();
				throw new DataAccessException("Ez da talderik ezabatu (ID ez da aurkitu).");
			}
			conn.commit();
			LoggerUtil.log("Taldea ezabatu da (ID: " + taldeaKod + ") eta bere jokalariak");
		} catch (SQLException e) {
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException ex) {
				}
			}
			LoggerUtil.log("ERROR taldea ezabatzean: " + e.getMessage());
			throw new DataAccessException("Errorea taldea ezabatzean", e);
		} finally {
			if (conn != null) {
				try {
					conn.setAutoCommit(true);
				} catch (SQLException e) {
				}
			}
			konexioa.konexioaItxi();
		}
	}

	/**
	 * ResultSet batetik Taldea objektua sortzen du (zelaiarekin).
	 */
	private Taldea mapeatuTaldea(ResultSet rs) throws SQLException {
		Taldea t = new Taldea();
		t.setTaldeaKod(rs.getInt("taldea_kod"));
		t.setIzena(rs.getString("izena"));
		t.setSortzeData(rs.getDate("sortze_data"));

		int zelaiaKod = rs.getInt("zelaia");
		if (!rs.wasNull()) {
			Zelaia z = new Zelaia();
			z.setZelaiaKod(zelaiaKod);
			z.setIzena(rs.getString("zelai_izena"));
			z.setKokapena(rs.getString("kokapena"));
			z.setKapazitatea(rs.getInt("kapazitatea"));
			t.setZelaia(z);
		}
		return t;
	}
}