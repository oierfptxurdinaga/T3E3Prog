package dao;
	
	import java.sql.*;
	import java.util.ArrayList;
	import java.util.List;
	
	import pojos.Epailea;
	import pojos.Jardunaldia;
	import pojos.Partida;
	import pojos.Taldea;
	
	/**
	 * Partida entitatearentzako Datuetarako Sarbide Objektua (DAO).
	 * Datu-basean partiden informazioa kudeatzeaz arduratzen da: sorkuntza,
	 * taldeen eta jardunaldien arteko erlazioak, eta partiden bilaketa ezberdinak.
	 */
	public class PartidaDAO {
		private Konexioa konexioa; // Datu-basearekin konektatzeko objektua
	
		/**
		 * PartidaDAO klasearen eraikitzailea.
		 * Datu-basearekiko konexioa kudeatuko duen objektua hasieratzen du.
		 */
		public PartidaDAO() {
			konexioa = new Konexioa(); 
		}
	
		/**
		 * Partida berri bat txertatzen du datu-basean, eta ondoren, 
		 * taldeekin eta jardunaldiarekin dituen erlazioak gordetzen ditu.
		 * @param partida Gorde nahi den {@link Partida} objektua.
		 * @return true txertaketa ondo burutu bada, false bestela.
		 */
		// Partida berri bat sortu datu-basean
		public boolean sortuPartida(Partida partida) {
			String sql = "INSERT INTO partida (data, ordua, emaitza, zigorrak, txartelak, epailea_kod) VALUES (?, ?, ?, ?, ?, ?)";
			try {
				konexioa.konexioaIreki(); //Konektatu DB-ra
				PreparedStatement ps = konexioa.getKonexioa().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	
				// Parametroak jarri
				ps.setDate(1, new java.sql.Date(partida.getData().getTime()));
				ps.setString(2, partida.getOrdua());
				ps.setString(3, partida.getEmaitza());
				ps.setInt(4, partida.getZigorrak());
				ps.setInt(5, partida.getTxartelak());
	
				if (partida.getEpailea() != null)
					ps.setInt(6, partida.getEpailea().getEpaileaKod());
				else
					ps.setNull(6, java.sql.Types.INTEGER);
	
				int affected = ps.executeUpdate(); // Insert exekutatu
	
				// ID autogeneratua lortu eta erlazioak gorde
				if (affected > 0) {
					ResultSet rs = ps.getGeneratedKeys();
					if (rs.next()) {
						partida.setPartidaKod(rs.getInt(1));
						insertTaldeaPartida(partida); // Etxeko eta kanpoko taldeen erlazioa
						insertatuPartidaJardunaldia(partida); // Partida-jardunaldia erlazioa
					}
					rs.close();
				}
	
				ps.close();
				return affected > 0;
			} catch (SQLException e) {
				System.err.println("Errorea partida gehitzean: " + e.getMessage());
				return false;
			} finally {
				konexioa.konexioaItxi(); // Konexioa itxi
			}
		}
	
		/**
		 * Partida baten etxeko eta kanpoko taldeen erlazioak gordetzen ditu 
		 * datu-baseko 'taldea_partida' bitarteko taulan.
		 * @param partida Taldeak esleituta dituen {@link Partida} objektua.
		 * @throws SQLException Datu-basearekin arazoren bat badago.
		 */
		// Etxeko eta kanpoko taldeen erlazioa datu-basean gehitu
		private void insertTaldeaPartida(Partida partida) throws SQLException {
			if (partida.getEtxekoTaldea() != null && partida.getKanpokoTaldea() != null) {
				String sql = "INSERT INTO taldea_partida (taldea_kod, partida_kod) VALUES (?, ?)";
				PreparedStatement ps = konexioa.getKonexioa().prepareStatement(sql);
	
				// 🔹 Etxeko taldea gehitu
				ps.setInt(1, partida.getEtxekoTaldea().getTaldeaKod());
				ps.setInt(2, partida.getPartidaKod());
				ps.executeUpdate();
	
				// 🔹 Kanpoko taldea gehitu
				ps.setInt(1, partida.getKanpokoTaldea().getTaldeaKod());
				ps.executeUpdate();
	
				ps.close();
			}
		}
	
		/**
		 * Partida bat bere jardunaldiarekin lotzen du datu-baseko 
		 * 'partida_jaurdunaldia' bitarteko taulan.
		 * @param partida Jardunaldia esleituta duen {@link Partida} objektua.
		 * @throws SQLException Datu-basearekin arazoren bat badago.
		 */
		// Partida-jardunaldia erlazioa gehitu
		private void insertatuPartidaJardunaldia(Partida partida) throws SQLException {
			if (partida.getJardunaldia() != null) {
				String sql = "INSERT INTO partida_jaurdunaldia (partida_kod, jaurdunaldi_kod) VALUES (?, ?)";
				PreparedStatement ps = konexioa.getKonexioa().prepareStatement(sql);
				ps.setInt(1, partida.getPartidaKod());
				ps.setInt(2, partida.getJardunaldia().getJardunaldiKod());
				ps.executeUpdate();
				ps.close();
			}
		}
	
		/**
		 * SQL kontsulta baten emaitzatik (ResultSet) partiden oinarrizko datuak, 
		 * epailea eta jardunaldia ateratzen ditu eta {@link Partida} objektu bat sortzen du.
		 * @param rs Datu-baseko kontsultaren emaitza duen {@link ResultSet} objektua.
		 * @return Datu-basetik irakurritako {@link Partida} objektua.
		 * @throws SQLException Datu-baseko zutabeak irakurtzean arazoren bat badago.
		 */
		//  ResultSet batetik Partida objektu bat sortu
		private Partida mapPartida(ResultSet rs) throws SQLException {
			Partida p = new Partida();
			p.setPartidaKod(rs.getInt("partida_kod"));
			p.setData(rs.getDate("data"));
			p.setOrdua(rs.getString("ordua"));
			p.setEmaitza(rs.getString("emaitza"));
			p.setZigorrak(rs.getInt("zigorrak"));
			p.setTxartelak(rs.getInt("txartelak"));
	
			// Epailea sortu, baldin eta datuak dauden
			if (rs.getObject("epailea_kod") != null) {
				Epailea e = new Epailea();
				e.setEpaileaKod(rs.getInt("epailea_kod"));
				e.setIzena(rs.getString("epaile_izena"));
				e.setAbizena(rs.getString("epaile_abizena"));
				p.setEpailea(e);
			}
	
			// Jardunaldia sortu, baldin eta datuak dauden
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
		 * Ematen den {@link Partida} objektuari etxeko edo kanpoko taldea gehitzen dio,
		 * ResultSet-etik irakurritako datuen arabera.
		 * @param p Taldea gehituko zaion {@link Partida} objektua.
		 * @param rs Datu-baseko kontsultaren emaitza duen {@link ResultSet} objektua.
		 * @throws SQLException Datu-baseko zutabeak irakurtzean arazoren bat badago.
		 */
		// Partidari taldea gehitu (etxeko edo kanpoko)
		private void partidariTaldeGehitu(Partida p, ResultSet rs) throws SQLException {
			if (rs.getObject("taldea_kod") != null) {
				Taldea t = new Taldea();
				t.setTaldeaKod(rs.getInt("taldea_kod"));
				t.setIzena(rs.getString("talde_izena"));
	
				// Lehenengo hutsunea etxeko taldea da
				if (p.getEtxekoTaldea() == null) {
					p.setEtxekoTaldea(t);
				} else if (p.getKanpokoTaldea() == null) {
					p.setKanpokoTaldea(t);
				}
			}
		}
	
		/**
		 * Datu-basean dauden partida guztien zerrenda eskuratzen du, beraien 
		 * epaile, jardunaldi eta taldeen informazioarekin batera.
		 * @return {@link Partida} objektuen zerrenda bat.
		 */
		// Datu-baseko partida guztiak lortu
		public List<Partida> partidaGuztiak() {
			List<Partida> zerrenda = new ArrayList<>();
			String sql = "SELECT p.partida_kod, p.data, p.ordua, p.emaitza, p.zigorrak, p.txartelak, p.epailea_kod, "
					+ "e.izena as epaile_izena, e.abizena as epaile_abizena, " + "tp.taldea_kod, t.izena as talde_izena, "
					+ "pj.jaurdunaldi_kod, j.hasiera_data as j_hasiera, j.amaiera_data as j_amaiera " + "FROM partida p "
					+ "LEFT JOIN epailea e ON p.epailea_kod = e.epailea_kod "
					+ "LEFT JOIN taldea_partida tp ON p.partida_kod = tp.partida_kod "
					+ "LEFT JOIN taldea t ON tp.taldea_kod = t.taldea_kod "
					+ "LEFT JOIN partida_jaurdunaldia pj ON p.partida_kod = pj.partida_kod "
					+ "LEFT JOIN jaurdunaldia j ON pj.jaurdunaldi_kod = j.jaurdunaldi_kod " + "ORDER BY p.partida_kod";
	
			try {
				konexioa.konexioaIreki();
				Statement st = konexioa.getKonexioa().createStatement();
				ResultSet rs = st.executeQuery(sql);
	
				Partida partida = null;
				int lastId = -1;
	
				while (rs.next()) {
					int id = rs.getInt("partida_kod");
					// Partida berri bat sortu, aurrekoarekin alderatuta
					if (id != lastId) {
						partida = mapPartida(rs);
						zerrenda.add(partida);
						lastId = id;
					}
					partidariTaldeGehitu(partida, rs); // Taldeak gehitu
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
	
		/**
		 * Denboraldi zehatz bateko partida guztiak berreskuratzen ditu, 
		 * lotutako talde, epaile eta jardunaldiaren informazioarekin batera.
		 * @param denboraldiaKod Bilatu nahi den denboraldiaren identifikatzailea.
		 * @return Ematen den denboraldiari dagozkion {@link Partida} guztien zerrenda.
		 */
		// Denboraldi bateko partidak lortu
		public List<Partida> partidaLortuDenboraldiBitartez(int denboraldiaKod) {
			List<Partida> zerrenda = new ArrayList<>();
			String sql = "SELECT p.partida_kod, p.data, p.ordua, p.emaitza, p.zigorrak, p.txartelak, p.epailea_kod, "
					+ "e.izena as epaile_izena, e.abizena as epaile_abizena, " + "tp.taldea_kod, t.izena as talde_izena, "
					+ "pj.jaurdunaldi_kod, j.hasiera_data as j_hasiera, j.amaiera_data as j_amaiera " + "FROM partida p "
					+ "LEFT JOIN epailea e ON p.epailea_kod = e.epailea_kod "
					+ "LEFT JOIN taldea_partida tp ON p.partida_kod = tp.partida_kod "
					+ "LEFT JOIN taldea t ON tp.taldea_kod = t.taldea_kod "
					+ "LEFT JOIN partida_jaurdunaldia pj ON p.partida_kod = pj.partida_kod "
					+ "LEFT JOIN jaurdunaldia j ON pj.jaurdunaldi_kod = j.jaurdunaldi_kod "
					+ "WHERE pj.jaurdunaldi_kod IN (SELECT jaurdunaldi_kod FROM denboraldia_jaurdunaldia WHERE denboraldia_kod = ?) "
					+ "ORDER BY p.partida_kod";
	
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
					partidariTaldeGehitu(partida, rs);
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