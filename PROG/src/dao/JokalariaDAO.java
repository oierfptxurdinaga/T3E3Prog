package dao;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import pojos.Jokalaria;
import pojos.Taldea;
import util.LoggerUtil;
import util.LoggerUtil.DataAccessException;

/**
 * Jokalaria entitatearen DAO klasea. Jokalariekin lotutako datu-base eragiketak
 * kudeatzen ditu.
 */
public class JokalariaDAO {
	private Konexioa konexioa;

	public JokalariaDAO() {
		konexioa = new Konexioa();
	}

	/**
	 * Jokalari berri bat datu-basean gordetzen du.
	 * 
	 * @param jokalaria Gorde nahi den jokalaria.
	 * @throws DataAccessException Errorea badago datu-basean.
	 */
	public void jokalariaSortu(Jokalaria jokalaria) throws DataAccessException {
		String sql = "INSERT INTO jokalariak (izena, abizena, NAN, posizioa, pisua, altuera, herritartasuna, taldea_kod, argazkia) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			konexioa.konexioaIreki();
			Connection conn = konexioa.getKonexioa();
			try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
				ps.setString(1, jokalaria.getIzena());
				ps.setString(2, jokalaria.getAbizena());
				ps.setString(3, jokalaria.getNan());
				ps.setString(4, jokalaria.getPosizioa());
				ps.setBigDecimal(5, jokalaria.getPisua());
				ps.setBigDecimal(6, jokalaria.getAltuera());
				ps.setString(7, jokalaria.getHerritartasuna());

				if (jokalaria.getTaldea() != null) {
					ps.setInt(8, jokalaria.getTaldea().getTaldeaKod());
				} else {
					ps.setNull(8, Types.INTEGER);
				}
				ps.setString(9, jokalaria.getArgazkia());

				int affected = ps.executeUpdate();
				if (affected == 0) {
					throw new DataAccessException("Ez da jokalaria txertatu.");
				}
				try (ResultSet rs = ps.getGeneratedKeys()) {
					if (rs.next()) {
						jokalaria.setJokalariakKod(rs.getInt(1));
					}
				}
			}
			LoggerUtil.log("Jokalaria sortu da: " + jokalaria.getIzena() + " " + jokalaria.getAbizena() + " (ID: "
					+ jokalaria.getJokalariakKod() + ")");
		} catch (SQLException e) {
			LoggerUtil.log("ERROR jokalaria sortzean: " + e.getMessage());
			throw new DataAccessException("Errorea jokalaria sortzean", e);
		} finally {
			konexioa.konexioaItxi();
		}
	}

	/**
	 * Datu-basean dauden jokalari guztiak lortzen ditu, taldearen datuekin.
	 * 
	 * @return Jokalarien zerrenda.
	 * @throws DataAccessException Errorea badago datu-basean.
	 */
	public List<Jokalaria> jokalariGuztiakLortu() throws DataAccessException {
		List<Jokalaria> zerrenda = new ArrayList<>();
		String sql = "SELECT j.jokalariak_kod, j.izena, j.abizena, j.NAN, j.posizioa, j.pisua, j.altuera, "
				+ "j.herritartasuna, j.taldea_kod, j.argazkia, t.izena AS talde_izena "
				+ "FROM jokalariak j LEFT JOIN taldea t ON j.taldea_kod = t.taldea_kod";

		try {
			konexioa.konexioaIreki();
			try (Statement st = konexioa.getKonexioa().createStatement(); ResultSet rs = st.executeQuery(sql)) {
				while (rs.next()) {
					Jokalaria j = mapeatuJokalaria(rs);
					zerrenda.add(j);
				}
			}
		} catch (SQLException e) {
			LoggerUtil.log("ERROR jokalariak lortzean: " + e.getMessage());
			throw new DataAccessException("Errorea jokalariak lortzean", e);
		} finally {
			konexioa.konexioaItxi();
		}
		return zerrenda;
	}

	/**
	 * Jokalari bat bilatzen du bere IDaren arabera.
	 * 
	 * @param jokalariakKod Jokalariaren identifikatzailea.
	 * @return Aurkitutako jokalaria, edo null.
	 * @throws DataAccessException Errorea badago datu-basean.
	 */
	public Jokalaria jokalariaLortuIdBidez(int jokalariakKod) throws DataAccessException {
		String sql = "SELECT j.jokalariak_kod, j.izena, j.abizena, j.NAN, j.posizioa, j.pisua, j.altuera, "
				+ "j.herritartasuna, j.taldea_kod, j.argazkia, t.izena AS talde_izena "
				+ "FROM jokalariak j LEFT JOIN taldea t ON j.taldea_kod = t.taldea_kod " + "WHERE j.jokalariak_kod = ?";

		try {
			konexioa.konexioaIreki();
			try (PreparedStatement ps = konexioa.getKonexioa().prepareStatement(sql)) {
				ps.setInt(1, jokalariakKod);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						return mapeatuJokalaria(rs);
					}
				}
			}
		} catch (SQLException e) {
			LoggerUtil.log("ERROR jokalaria IDz lortzean: " + e.getMessage());
			throw new DataAccessException("Errorea jokalaria lortzean", e);
		} finally {
			konexioa.konexioaItxi();
		}
		return null;
	}

	/**
	 * Talde bateko jokalari guztiak lortzen ditu.
	 * 
	 * @param taldeaKod Taldearen identifikatzailea.
	 * @return Taldeko jokalarien zerrenda.
	 * @throws DataAccessException Errorea badago datu-basean.
	 */
	public List<Jokalaria> jokalariaLortuTaldeBidez(int taldeaKod) throws DataAccessException {
		List<Jokalaria> zerrenda = new ArrayList<>();
		String sql = "SELECT jokalariak_kod, izena, abizena, NAN, posizioa, pisua, altuera, herritartasuna, argazkia "
				+ "FROM jokalariak WHERE taldea_kod = ?";

		try {
			konexioa.konexioaIreki();
			try (PreparedStatement ps = konexioa.getKonexioa().prepareStatement(sql)) {
				ps.setInt(1, taldeaKod);
				try (ResultSet rs = ps.executeQuery()) {
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
						j.setArgazkia(rs.getString("argazkia"));
						zerrenda.add(j);
					}
				}
			}
		} catch (SQLException e) {
			LoggerUtil.log("ERROR jokalariak taldeka lortzean: " + e.getMessage());
			throw new DataAccessException("Errorea jokalariak taldeka lortzean", e);
		} finally {
			konexioa.konexioaItxi();
		}
		return zerrenda;
	}

	/**
	 * Jokalari baten datuak eguneratzen ditu.
	 * 
	 * @param jokalaria Eguneratutako datuak dituen jokalaria.
	 * @throws DataAccessException Errorea badago datu-basean.
	 */
	public void eguneratu(Jokalaria jokalaria) throws DataAccessException {
		String sql = "UPDATE jokalariak SET izena = ?, abizena = ?, NAN = ?, posizioa = ?, pisua = ?, "
				+ "altuera = ?, herritartasuna = ?, taldea_kod = ?, argazkia = ? WHERE jokalariak_kod = ?";

		try {
			konexioa.konexioaIreki();
			try (PreparedStatement ps = konexioa.getKonexioa().prepareStatement(sql)) {
				ps.setString(1, jokalaria.getIzena());
				ps.setString(2, jokalaria.getAbizena());
				ps.setString(3, jokalaria.getNan());
				ps.setString(4, jokalaria.getPosizioa());
				ps.setBigDecimal(5, jokalaria.getPisua());
				ps.setBigDecimal(6, jokalaria.getAltuera());
				ps.setString(7, jokalaria.getHerritartasuna());

				if (jokalaria.getTaldea() != null) {
					ps.setInt(8, jokalaria.getTaldea().getTaldeaKod());
				} else {
					ps.setNull(8, Types.INTEGER);
				}

				ps.setString(9, jokalaria.getArgazkia());
				ps.setInt(10, jokalaria.getJokalariakKod());

				int affected = ps.executeUpdate();
				if (affected == 0) {
					throw new DataAccessException("Ez da jokalaririk eguneratu (ID ez da aurkitu).");
				}
			}
			LoggerUtil.log("Jokalaria eguneratu da: " + jokalaria.getIzena() + " " + jokalaria.getAbizena() + " (ID: "
					+ jokalaria.getJokalariakKod() + ")");
		} catch (SQLException e) {
			LoggerUtil.log("ERROR jokalaria eguneratzean: " + e.getMessage());
			throw new DataAccessException("Errorea jokalaria eguneratzean", e);
		} finally {
			konexioa.konexioaItxi();
		}
	}

	/**
	 * Jokalari bat talde berri batera traspasatzen du.
	 * 
	 * @param jokalaria     Traspasatu nahi den jokalaria.
	 * @param taldeHelburua Jokalaria joango den taldea.
	 * @throws DataAccessException      Errorea badago datu-basean.
	 * @throws IllegalArgumentException Jokalaria edo taldea null badira, edo jada
	 *                                  talde horretan badago.
	 */
	public void traspasatu(Jokalaria jokalaria, Taldea taldeHelburua) throws DataAccessException {
		if (jokalaria == null || taldeHelburua == null) {
			throw new IllegalArgumentException("Jokalaria eta talde helburua ezin dira null izan.");
		}
		if (jokalaria.getTaldea() != null && jokalaria.getTaldea().getTaldeaKod() == taldeHelburua.getTaldeaKod()) {
			throw new IllegalArgumentException("Jokalaria dagoeneko talde horretan dago.");
		}

		String taldeZaharraIzena = jokalaria.getTaldea() != null ? jokalaria.getTaldea().getIzena() : "Talde gabe";
		jokalaria.setTaldea(taldeHelburua);
		eguneratu(jokalaria);

		LoggerUtil.log("Traspasoa: " + jokalaria.getIzena() + " " + jokalaria.getAbizena() + " -> " + taldeZaharraIzena
				+ "-tik " + taldeHelburua.getIzena() + "-ra");
	}

	/**
	 * Jokalari bat ezabatzen du bere IDaren arabera.
	 * 
	 * @param jokalariakKod Ezabatu nahi den jokalariaren IDa.
	 * @throws DataAccessException Errorea badago datu-basean.
	 */
	public void jokalariaEzabatu(int jokalariakKod) throws DataAccessException {
		String sql = "DELETE FROM jokalariak WHERE jokalariak_kod = ?";

		try {
			konexioa.konexioaIreki();
			try (PreparedStatement ps = konexioa.getKonexioa().prepareStatement(sql)) {
				ps.setInt(1, jokalariakKod);
				int affected = ps.executeUpdate();
				if (affected == 0) {
					throw new DataAccessException("Ez da jokalaririk ezabatu (ID ez da aurkitu).");
				}
			}
			LoggerUtil.log("Jokalaria ezabatu da (ID: " + jokalariakKod + ")");
		} catch (SQLException e) {
			LoggerUtil.log("ERROR jokalaria ezabatzean: " + e.getMessage());
			throw new DataAccessException("Errorea jokalaria ezabatzean", e);
		} finally {
			konexioa.konexioaItxi();
		}
	}

	/**
	 * ResultSet batetik Jokalaria objektua sortzen du (taldearekin).
	 */
	private Jokalaria mapeatuJokalaria(ResultSet rs) throws SQLException {
		Jokalaria j = new Jokalaria();
		j.setJokalariakKod(rs.getInt("jokalariak_kod"));
		j.setIzena(rs.getString("izena"));
		j.setAbizena(rs.getString("abizena"));
		j.setNan(rs.getString("NAN"));
		j.setPosizioa(rs.getString("posizioa"));
		j.setPisua(rs.getBigDecimal("pisua"));
		j.setAltuera(rs.getBigDecimal("altuera"));
		j.setHerritartasuna(rs.getString("herritartasuna"));
		j.setArgazkia(rs.getString("argazkia"));

		int taldeaKod = rs.getInt("taldea_kod");
		if (!rs.wasNull()) {
			Taldea t = new Taldea();
			t.setTaldeaKod(taldeaKod);
			t.setIzena(rs.getString("talde_izena"));
			j.setTaldea(t);
		}
		return j;
	}
}