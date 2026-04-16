package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import pojos.Denboraldia;
import util.LoggerUtil;
import util.LoggerUtil.DataAccessException;

/**
 * Denboraldia entitatearen DAO klasea. Denboraldiekin lotutako datu-base
 * eragiketak kudeatzen ditu.
 */
public class DenboraldiaDAO {
	public Konexioa konexioa;

	public DenboraldiaDAO() {
		konexioa = new Konexioa();
	}

	/**
	 * Denboraldi guztiak lortzen ditu.
	 * 
	 * @return Denboraldien zerrenda.
	 * @throws DataAccessException Errorea badago datu-basean.
	 */
	public List<Denboraldia> denboraldiakAtera() throws DataAccessException {
		List<Denboraldia> zerrenda = new ArrayList<>();
		String sql = "SELECT denboraldia_kod, izena, hasiera_data, amaiera_data, txapelduna, aktiboa "
				+ "FROM denboraldia ORDER BY hasiera_data DESC";
		try {
			konexioa.konexioaIreki();
			try (Statement st = konexioa.getKonexioa().createStatement(); ResultSet rs = st.executeQuery(sql)) {
				while (rs.next()) {
					Denboraldia d = mapeatuDenboraldia(rs);
					zerrenda.add(d);
				}
			}
		} catch (SQLException e) {
			LoggerUtil.log("ERROR denboraldiak lortzean: " + e.getMessage());
			throw new DataAccessException("Errorea denboraldiak lortzean", e);
		} finally {
			konexioa.konexioaItxi();
		}
		return zerrenda;
	}

	/**
	 * Denboraldi bat bilatzen du bere IDaren arabera.
	 * 
	 * @param denboraldiaKod Denboraldiaren identifikatzailea.
	 * @return Aurkitutako denboraldia, edo null.
	 * @throws DataAccessException Errorea badago datu-basean.
	 */
	public Denboraldia denboraldiakAteraId(int denboraldiaKod) throws DataAccessException {
		String sql = "SELECT denboraldia_kod, izena, hasiera_data, amaiera_data, txapelduna, aktiboa "
				+ "FROM denboraldia WHERE denboraldia_kod = ?";
		try {
			konexioa.konexioaIreki();
			try (PreparedStatement ps = konexioa.getKonexioa().prepareStatement(sql)) {
				ps.setInt(1, denboraldiaKod);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						return mapeatuDenboraldia(rs);
					}
				}
			}
		} catch (SQLException e) {
			LoggerUtil.log("ERROR denboraldia IDz lortzean: " + e.getMessage());
			throw new DataAccessException("Errorea denboraldia lortzean", e);
		} finally {
			konexioa.konexioaItxi();
		}
		return null;
	}

	/**
	 * Unean aktibo dagoen denboraldia lortzen du.
	 * 
	 * @return Denboraldi aktiboa, edo null.
	 * @throws DataAccessException Errorea badago datu-basean.
	 */
	public Denboraldia denboraldiaAktiboaLortu() throws DataAccessException {
		String sql = "SELECT denboraldia_kod, izena, hasiera_data, amaiera_data, txapelduna, aktiboa "
				+ "FROM denboraldia WHERE aktiboa = true";
		try {
			konexioa.konexioaIreki();
			try (Statement st = konexioa.getKonexioa().createStatement(); ResultSet rs = st.executeQuery(sql)) {
				if (rs.next()) {
					return mapeatuDenboraldia(rs);
				}
			}
		} catch (SQLException e) {
			LoggerUtil.log("ERROR denboraldi aktiboa lortzean: " + e.getMessage());
			throw new DataAccessException("Errorea denboraldi aktiboa lortzean", e);
		} finally {
			konexioa.konexioaItxi();
		}
		return null;
	}

	/**
	 * Denboraldi berri bat hasi eta aktibo bezala markatzen du. Aurretik aktibo
	 * zegoen denboraldia desaktibatzen du.
	 * 
	 * @param denboraldia Hasi nahi den denboraldia.
	 * @return Sortutako denboraldiaren IDa.
	 * @throws DataAccessException Errorea badago datu-basean.
	 */
	public int denboraldiaHasi(Denboraldia denboraldia) throws DataAccessException {
		String sqlUpdate = "UPDATE denboraldia SET aktiboa = false WHERE aktiboa = true";
		String sqlInsert = "INSERT INTO denboraldia (izena, hasiera_data, amaiera_data, aktiboa) VALUES (?, ?, ?, true)";
		Connection conn = null;
		try {
			konexioa.konexioaIreki();
			conn = konexioa.getKonexioa();
			conn.setAutoCommit(false);

			// 1. Desaktibatu uneko denboraldi aktiboa
			try (PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate)) {
				psUpdate.executeUpdate();
			}

			// 2. Sortu denboraldi berria
			int newId = -1;
			try (PreparedStatement psInsert = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
				psInsert.setString(1, denboraldia.getIzena());
				psInsert.setDate(2, new java.sql.Date(denboraldia.getHasieraData().getTime()));
				psInsert.setDate(3, new java.sql.Date(denboraldia.getAmaieraData().getTime()));
				psInsert.executeUpdate();

				try (ResultSet rs = psInsert.getGeneratedKeys()) {
					if (rs.next()) {
						newId = rs.getInt(1);
						denboraldia.setDenboraldiaKod(newId);
						denboraldia.setAktiboa(true);
					}
				}
			}

			if (newId == -1) {
				conn.rollback();
				throw new DataAccessException("Ez da denboraldi berria sortu.");
			}

			conn.commit();
			LoggerUtil.log("Denboraldia hasi da: " + denboraldia.getIzena() + " (ID: " + newId + ")");
			return newId;
		} catch (SQLException e) {
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException ex) {
				}
			}
			LoggerUtil.log("ERROR denboraldia hastean: " + e.getMessage());
			throw new DataAccessException("Errorea denboraldia hastean", e);
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
	 * Uneko denboraldi aktiboa amaitzen du.
	 * 
	 * @param txapelduna Denboraldiko txapeldunaren izena (null izan daiteke).
	 * @return true ondo amaitu bada, false bestela.
	 * @throws DataAccessException Errorea badago datu-basean.
	 */
	public boolean denboraldiaAmaitu(String txapelduna) throws DataAccessException {
		String sql = "UPDATE denboraldia SET aktiboa = false, txapelduna = ? WHERE aktiboa = true";
		try {
			konexioa.konexioaIreki();
			try (PreparedStatement ps = konexioa.getKonexioa().prepareStatement(sql)) {
				ps.setString(1, txapelduna);
				int rows = ps.executeUpdate();
				if (rows > 0) {
					LoggerUtil.log("Denboraldia amaitu da" + (txapelduna != null ? ", txapelduna: " + txapelduna : ""));
				}
				return rows > 0;
			}
		} catch (SQLException e) {
			LoggerUtil.log("ERROR denboraldia amaitzean: " + e.getMessage());
			throw new DataAccessException("Errorea denboraldia amaitzean", e);
		} finally {
			konexioa.konexioaItxi();
		}
	}

	/**
	 * Denboraldi baten datuak eguneratzen ditu.
	 * 
	 * @param denboraldia Eguneratutako datuak dituen denboraldia.
	 * @return true ondo eguneratu bada, false bestela.
	 * @throws DataAccessException Errorea badago datu-basean.
	 */
	public boolean eguneratu(Denboraldia denboraldia) throws DataAccessException {
		String sql = "UPDATE denboraldia SET izena=?, hasiera_data=?, amaiera_data=?, txapelduna=?, aktiboa=? "
				+ "WHERE denboraldia_kod=?";
		try {
			konexioa.konexioaIreki();
			try (PreparedStatement ps = konexioa.getKonexioa().prepareStatement(sql)) {
				ps.setString(1, denboraldia.getIzena());
				ps.setDate(2, new java.sql.Date(denboraldia.getHasieraData().getTime()));
				ps.setDate(3, new java.sql.Date(denboraldia.getAmaieraData().getTime()));
				ps.setString(4, denboraldia.getTxapelduna());
				ps.setBoolean(5, denboraldia.isAktiboa());
				ps.setInt(6, denboraldia.getDenboraldiaKod());
				int rows = ps.executeUpdate();
				if (rows > 0) {
					LoggerUtil.log("Denboraldia eguneratu da: " + denboraldia.getIzena() + " (ID: "
							+ denboraldia.getDenboraldiaKod() + ")");
				}
				return rows > 0;
			}
		} catch (SQLException e) {
			LoggerUtil.log("ERROR denboraldia eguneratzean: " + e.getMessage());
			throw new DataAccessException("Errorea denboraldia eguneratzean", e);
		} finally {
			konexioa.konexioaItxi();
		}
	}

	/**
	 * ResultSet batetik Denboraldia objektua sortzen du.
	 */
	private Denboraldia mapeatuDenboraldia(ResultSet rs) throws SQLException {
		Denboraldia d = new Denboraldia();
		d.setDenboraldiaKod(rs.getInt("denboraldia_kod"));
		d.setIzena(rs.getString("izena"));
		d.setHasieraData(rs.getDate("hasiera_data"));
		d.setAmaieraData(rs.getDate("amaiera_data"));
		d.setTxapelduna(rs.getString("txapelduna"));
		d.setAktiboa(rs.getBoolean("aktiboa"));
		return d;
	}
}