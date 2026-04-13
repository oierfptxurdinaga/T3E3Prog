package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import pojos.Taldea;
import pojos.Zelaia;

/**
 * Taldea entitatearentzako Datuetarako Sarbide Objektua (DAO).
 * Datu-basean taldeekin lotutako eragiketak kudeatzen ditu:
 * taldeak sortu, zerrendatu, ID bidez bilatu, eguneratu eta ezabatu.
 */
public class TaldeaDAO {
	private Konexioa konexioa; // Datu-basearekin konektatzeko objektua

	/**
	 * TaldeaDAO klasearen eraikitzailea.
	 * Datu-basearekiko konexioa kudeatuko duen objektua hasieratzen du.
	 */
	public TaldeaDAO() {
		konexioa = new Konexioa(); 
	}

	/**
	 * Talde berri bat txertatzen du datu-basean.
	 * Bere izena, sortze-data eta zelaia (baditu) gordetzen ditu.
	 * @param taldea Gorde nahi den {@link Taldea} objektua.
	 * @return true txertaketa ondo burutu bada, false arazoren bat egon bada.
	 */
	// Talde berri bat sortzeko metodoa
	public boolean taldeaSortu(Taldea taldea) {
		String sql = "INSERT INTO taldea (izena, sortze_data, zelaia) VALUES (?, ?, ?)";
		try {
			konexioa.konexioaIreki(); // DB konexioa ireki
			PreparedStatement ps = konexioa.getKonexioa().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			ps.setString(1, taldea.getIzena()); // Taldearen izena

			// Sortze data badago, gorde, bestela null
			if (taldea.getSortzeData() != null) {
				ps.setDate(2, new java.sql.Date(taldea.getSortzeData().getTime()));
			} else {
				ps.setNull(2, java.sql.Types.DATE);
			}

			// Zelaia badago, bere ID-a gorde
			if (taldea.getZelaia() != null) {
				ps.setInt(3, taldea.getZelaia().getZelaiaKod());
			} else {
				ps.setNull(3, java.sql.Types.INTEGER);
			}

			int affected = ps.executeUpdate(); // Insert exekutatu

			// ID autogeneratua lortu
			if (affected > 0) {
				ResultSet rs = ps.getGeneratedKeys();
				if (rs.next())
					taldea.setTaldeaKod(rs.getInt(1));
				rs.close();
			}

			ps.close();
			return affected > 0; // True itzuli ondo joan bada
		} catch (SQLException e) {
			System.err.println("Errorea taldea gehitzean: " + e.getMessage());
			return false;
		} finally {
			konexioa.konexioaItxi(); // konexioa itxi
		}
	}

	/**
	 * Datu-basean dauden talde guztiak eskuratzen ditu, bakoitzaren zelaiaren
	 * datuekin batera (honi esleitutako zelairik badago).
	 * @return {@link Taldea} objektuen zerrenda bat.
	 */
	// Talde guztiak lortu (eta zelaiarekin batera)
	public List<Taldea> taldeGuztiakLortu() {
		List<Taldea> zerrenda = new ArrayList<>();
		String sql = "SELECT t.taldea_kod, t.izena, t.sortze_data, t.zelaia, "
				+ "z.izena as zelai_izena, z.kokapena, z.kapazitatea "
				+ "FROM taldea t LEFT JOIN zelaia z ON t.zelaia = z.zelaia_kod";

		try {
			konexioa.konexioaIreki();
			Statement st = konexioa.getKonexioa().createStatement();
			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				Taldea t = new Taldea();
				t.setTaldeaKod(rs.getInt("taldea_kod"));
				t.setIzena(rs.getString("izena"));
				t.setSortzeData(rs.getDate("sortze_data"));

				// Zelaia badago, objektua sortu
				int zelaiaKod = rs.getInt("zelaia");
				if (!rs.wasNull()) {
					Zelaia z = new Zelaia();
					z.setZelaiaKod(zelaiaKod);
					z.setIzena(rs.getString("zelai_izena"));
					z.setKokapena(rs.getString("kokapena"));
					z.setKapazitatea(rs.getInt("kapazitatea"));
					t.setZelaia(z);
				}

				zerrenda.add(t); // Zerrendara gehitu
			}

			rs.close();
			st.close();
		} catch (SQLException e) {
			System.err.println("Errorea taldeak irakurtzean: " + e.getMessage());
		} finally {
			konexioa.konexioaItxi();
		}

		return zerrenda;
	}

	/**
	 * Talde zehatz bat bilatzen du datu-basean bere identifikatzailearen arabera,
	 * zelaiaren datuekin batera.
	 * @param taldeaKod Bilatu nahi den taldearen identifikatzailea (ID).
	 * @return Datu-basetik irakurritako {@link Taldea} objektua, edo null ez bada aurkitzen.
	 */
	// Talde bat IDaren arabera lortu
	public Taldea TaldeLortuIdBidez(int taldeaKod) {
		Taldea taldea = null;
		String sql = "SELECT t.taldea_kod, t.izena, t.sortze_data, t.zelaia, "
				+ "z.izena as zelai_izena, z.kokapena, z.kapazitatea "
				+ "FROM taldea t LEFT JOIN zelaia z ON t.zelaia = z.zelaia_kod WHERE t.taldea_kod = ?";

		try {
			konexioa.konexioaIreki();
			PreparedStatement ps = konexioa.getKonexioa().prepareStatement(sql);
			ps.setInt(1, taldeaKod);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				taldea = new Taldea();
				taldea.setTaldeaKod(rs.getInt("taldea_kod"));
				taldea.setIzena(rs.getString("izena"));
				taldea.setSortzeData(rs.getDate("sortze_data"));

				int zelaiaKod = rs.getInt("zelaia");
				if (!rs.wasNull()) {
					Zelaia z = new Zelaia();
					z.setZelaiaKod(zelaiaKod);
					z.setIzena(rs.getString("zelai_izena"));
					z.setKokapena(rs.getString("kokapena"));
					z.setKapazitatea(rs.getInt("kapazitatea"));
					taldea.setZelaia(z);
				}
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.err.println("Errorea taldea IDz irakurtzean: " + e.getMessage());
		} finally {
			konexioa.konexioaItxi();
		}

		return taldea;
	}

	/**
	 * Existitzen den talde baten datuak eguneratzen ditu datu-basean.
	 * @param taldea Eguneratu nahi diren datu berriak dituen {@link Taldea} objektua.
	 * @return true eguneraketa modu egokian burutu bada, false arazoren bat egon bada.
	 */
	// Talde baten datuak eguneratu
	public boolean TaldeaEguneratu(Taldea taldea) {
		String sql = "UPDATE taldea SET izena = ?, sortze_data = ?, zelaia = ? WHERE taldea_kod = ?";

		try {
			konexioa.konexioaIreki();
			PreparedStatement ps = konexioa.getKonexioa().prepareStatement(sql);

			ps.setString(1, taldea.getIzena());

			// Data eguneratu edo null jarri
			if (taldea.getSortzeData() != null) {
				ps.setDate(2, new java.sql.Date(taldea.getSortzeData().getTime()));
			} else {
				ps.setNull(2, java.sql.Types.DATE);
			}

			// Zelaia eguneratu edo null jarri
			if (taldea.getZelaia() != null) {
				ps.setInt(3, taldea.getZelaia().getZelaiaKod());
			} else {
				ps.setNull(3, java.sql.Types.INTEGER);
			}

			ps.setInt(4, taldea.getTaldeaKod());

			int affected = ps.executeUpdate();
			ps.close();

			return affected > 0;
		} catch (SQLException e) {
			System.err.println("Errorea taldea eguneratzean: " + e.getMessage());
			return false;
		} finally {
			konexioa.konexioaItxi();
		}
	}

	/**
	 * Talde zehatz bat datu-basetik guztiz ezabatzen du bere IDa erabiliz.
	 * @param taldeaKod Ezabatu nahi den taldearen identifikatzailea (ID).
	 * @return true ezabaketa ondo burutu bada, false arazoren bat egon bada.
	 */
	// Talde bat ezabatu
	public boolean TaldeEzabatu(int taldeaKod) {
		String sql = "DELETE FROM taldea WHERE taldea_kod = ?";
		try {
			konexioa.konexioaIreki();
			PreparedStatement ps = konexioa.getKonexioa().prepareStatement(sql);
			ps.setInt(1, taldeaKod);
			
			int affected = ps.executeUpdate();
			ps.close();
			return affected > 0;
		} catch (SQLException e) {
			System.err.println("Errorea taldea ezabatzean: " + e.getMessage());
			return false;
		} finally {
			konexioa.konexioaItxi();
		}
	}
}