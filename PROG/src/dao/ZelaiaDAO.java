package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import pojos.Zelaia;
import util.LoggerUtil;
import util.LoggerUtil.DataAccessException;

/**
 * Zelaia entitatearen DAO klasea. Zelaiekin lotutako irakurketa eragiketak
 * kudeatzen ditu.
 */
public class ZelaiaDAO {
	private Konexioa konexioa;

	public ZelaiaDAO() {
		konexioa = new Konexioa();
	}

	/**
	 * Datu-basean dauden zelai guztiak lortzen ditu.
	 * 
	 * @return Zelaien zerrenda.
	 * @throws DataAccessException Errorea badago datu-basean.
	 */
	public List<Zelaia> zelaiGutziakLortu() throws DataAccessException {
		List<Zelaia> zerrenda = new ArrayList<>();
		String sql = "SELECT zelaia_kod, izena, kokapena, kapazitatea FROM zelaia";

		try {
			konexioa.konexioaIreki();
			try (Statement st = konexioa.getKonexioa().createStatement(); ResultSet rs = st.executeQuery(sql)) {
				while (rs.next()) {
					Zelaia z = new Zelaia();
					z.setZelaiaKod(rs.getInt("zelaia_kod"));
					z.setIzena(rs.getString("izena"));
					z.setKokapena(rs.getString("kokapena"));
					z.setKapazitatea(rs.getInt("kapazitatea"));
					zerrenda.add(z);
				}
			}
		} catch (SQLException e) {
			LoggerUtil.log("ERROR zelaiak lortzean: " + e.getMessage());
			throw new DataAccessException("Errorea zelaiak lortzean", e);
		} finally {
			konexioa.konexioaItxi();
		}
		return zerrenda;
	}
}