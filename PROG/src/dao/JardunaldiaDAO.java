package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import pojos.Jardunaldia;
import util.LoggerUtil;
import util.LoggerUtil.DataAccessException;

/**
 * Jardunaldia entitatearen DAO klasea. Jardunaldiekin lotutako datu-base
 * eragiketak kudeatzen ditu.
 */
public class JardunaldiaDAO {
	private Konexioa konexioa;

	public JardunaldiaDAO() {
		konexioa = new Konexioa();
	}

	/**
	 * Denboraldi zehatz bati dagozkion jardunaldi guztiak eskuratzen ditu.
	 * 
	 * @param denboraldiaKod Denboraldiaren identifikatzailea.
	 * @return Lotutako jardunaldien zerrenda.
	 * @throws DataAccessException Errorea badago datu-basean.
	 */
	public List<Jardunaldia> lostuJardunaldiDenboraldiBidez(int denboraldiaKod) throws DataAccessException {
		List<Jardunaldia> zerrenda = new ArrayList<>();
		String sql = "SELECT j.jaurdunaldi_kod, j.hasiera_data, j.amaiera_data " + "FROM jaurdunaldia j "
				+ "INNER JOIN denboraldia_jaurdunaldia dj ON j.jaurdunaldi_kod = dj.jaurdunaldi_kod "
				+ "WHERE dj.denboraldia_kod = ?";

		try {
			konexioa.konexioaIreki();
			try (PreparedStatement ps = konexioa.getKonexioa().prepareStatement(sql)) {
				ps.setInt(1, denboraldiaKod);
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						Jardunaldia j = new Jardunaldia();
						j.setJardunaldiKod(rs.getInt("jaurdunaldi_kod"));
						j.setHasieraData(rs.getDate("hasiera_data"));
						j.setAmaieraData(rs.getDate("amaiera_data"));
						zerrenda.add(j);
					}
				}
			}
		} catch (SQLException e) {
			LoggerUtil.log("ERROR jardunaldiak lortzean: " + e.getMessage());
			throw new DataAccessException("Errorea jardunaldiak lortzean", e);
		} finally {
			konexioa.konexioaItxi();
		}
		return zerrenda;
	}

	/**
	 * Jardunaldi berri bat sortzen du datu-basean.
	 * 
	 * @param jardunaldia Sortu nahi den jardunaldia.
	 * @return Sortutako jardunaldiaren IDa.
	 * @throws DataAccessException Errorea badago datu-basean.
	 */
	public int jardunaldiaSortu(Jardunaldia jardunaldia) throws DataAccessException {
		String sql = "INSERT INTO jaurdunaldia (hasiera_data, amaiera_data) VALUES (?, ?)";

		try {
			konexioa.konexioaIreki();
			try (PreparedStatement ps = konexioa.getKonexioa().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
				ps.setDate(1, new java.sql.Date(jardunaldia.getHasieraData().getTime()));
				ps.setDate(2, new java.sql.Date(jardunaldia.getAmaieraData().getTime()));

				int affected = ps.executeUpdate();
				if (affected == 0) {
					throw new DataAccessException("Ez da jardunaldia sortu.");
				}

				try (ResultSet rs = ps.getGeneratedKeys()) {
					if (rs.next()) {
						int id = rs.getInt(1);
						jardunaldia.setJardunaldiKod(id);
						return id;
					}
				}
			}
		} catch (SQLException e) {
			LoggerUtil.log("ERROR jardunaldia sortzean: " + e.getMessage());
			throw new DataAccessException("Errorea jardunaldia sortzean", e);
		} finally {
			konexioa.konexioaItxi();
		}
		throw new DataAccessException("Ez da jardunaldiaren IDa lortu.");
	}

	/**
	 * Jardunaldi bat denboraldi batekin lotzen du.
	 * 
	 * @param denboraldiaKod Denboraldiaren identifikatzailea.
	 * @param jardunaldiKod  Jardunaldiaren identifikatzailea.
	 * @return true lotura ondo sortu bada.
	 * @throws DataAccessException Errorea badago datu-basean.
	 */
	public boolean denboraldiaAsoziatu(int denboraldiaKod, int jardunaldiKod) throws DataAccessException {
		String sql = "INSERT INTO denboraldia_jaurdunaldia (denboraldia_kod, jaurdunaldi_kod) VALUES (?, ?)";

		try {
			konexioa.konexioaIreki();
			try (PreparedStatement ps = konexioa.getKonexioa().prepareStatement(sql)) {
				ps.setInt(1, denboraldiaKod);
				ps.setInt(2, jardunaldiKod);
				int affected = ps.executeUpdate();
				return affected > 0;
			}
		} catch (SQLException e) {
			LoggerUtil.log("ERROR jardunaldia denboraldiarekin lotzean: " + e.getMessage());
			throw new DataAccessException("Errorea jardunaldia denboraldiarekin lotzean", e);
		} finally {
			konexioa.konexioaItxi();
		}
	}
}