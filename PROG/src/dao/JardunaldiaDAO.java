package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import pojos.Jardunaldia;

public class JardunaldiaDAO {
	private Konexioa konexioa; // DBrekin konektatzeko objektua

	public JardunaldiaDAO() {
		konexioa = new Konexioa();
	}

	// Denboraldi baten jardunaldi guztiak lortzeko metodoa
	public List<Jardunaldia> lostuJardunaldiDenboraldiBidez(int denboraldiaKod) {
		List<Jardunaldia> zerrenda = new ArrayList<>();
		String sql = "SELECT j.jaurdunaldi_kod, j.hasiera_data, j.amaiera_data " + "FROM jaurdunaldia j "
				+ "INNER JOIN denboraldia_jaurdunaldia dj ON j.jaurdunaldi_kod = dj.jaurdunaldi_kod "
				+ "WHERE dj.denboraldia_kod = ?";

		try {
			konexioa.konexioaIreki();
			PreparedStatement ps = konexioa.getKonexioa().prepareStatement(sql);
			ps.setInt(1, denboraldiaKod); // Parametroa jarri (denboraldia ID)
			ResultSet rs = ps.executeQuery(); // Kontsulta exekutatzeko

			while (rs.next()) { // ResultSetetik datuak irakurri
				Jardunaldia j = new Jardunaldia();
				j.setJardunaldiKod(rs.getInt("jaurdunaldi_kod"));
				j.setHasieraData(rs.getDate("hasiera_data"));
				j.setAmaieraData(rs.getDate("amaiera_data"));
				zerrenda.add(j); // Zerrendara gehitu
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.err.println("Errorea jardunaldiak denboraldiaren arabera irakurtzean: " + e.getMessage());
		} finally {
			konexioa.konexioaItxi(); // Konexioa itxi
		}

		return zerrenda; // Jardunaldi guztien zerrenda itzuli
	}

	// Jardunaldia sortu eta IDa lortu
	public int jardunaldiaSortu(Jardunaldia jardunaldia) {
		String sql = "INSERT INTO jaurdunaldia (hasiera_data, amaiera_data) VALUES (?, ?)";

		try {
			konexioa.konexioaIreki(); // DB konexioa ireki
			PreparedStatement ps = konexioa.getKonexioa().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); // ID autogeneratua lortzeko

			ps.setDate(1, new java.sql.Date(jardunaldia.getHasieraData().getTime())); // Hasiera data jarri
			ps.setDate(2, new java.sql.Date(jardunaldia.getAmaieraData().getTime())); // Amaiera data jarri

			int affected = ps.executeUpdate(); // Datuak gehitu
			if (affected > 0) {
				ResultSet rs = ps.getGeneratedKeys(); // ID autogeneratua lortu
				if (rs.next()) {
					int id = rs.getInt(1);
					jardunaldia.setJardunaldiKod(id); // Jardunaldi objektuan gorde
					return id; // ID itzuli
				}
				rs.close();
			}
			ps.close();
		} catch (SQLException e) {
			System.err.println("Errorea jardunaldia gehitzean: " + e.getMessage());
		} finally {
			konexioa.konexioaItxi(); // Konexioa itxi
		}

		return -1; // Arazo bat egon bada, -1 itzuli
	}

	// Jardunaldi bat denboraldi batekin lotzeko metodoa
	public boolean denboraldiaAsoziatu(int denboraldiaKod, int jardunaldiKod) {
		String sql = "INSERT INTO denboraldia_jaurdunaldia (denboraldia_kod, jaurdunaldi_kod) VALUES (?, ?)";

		try {
			konexioa.konexioaIreki(); // DB konexioa ireki
			PreparedStatement ps = konexioa.getKonexioa().prepareStatement(sql);
			ps.setInt(1, denboraldiaKod); // Denboraldi ID jarri
			ps.setInt(2, jardunaldiKod); // Jardunaldi ID jarri

			int affected = ps.executeUpdate(); // Txertaketa exekutatu
			ps.close();
			return affected > 0; // True itzuli txertaketa ondo egon bada
		} catch (SQLException e) {
			System.err.println("Errorea jardunaldia temporadarekin lotzean: " + e.getMessage());
			return false;
		} finally {
			konexioa.konexioaItxi(); // Konexioa itxi
		}
	}
}