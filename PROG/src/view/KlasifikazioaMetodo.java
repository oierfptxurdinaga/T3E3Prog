package view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import model.Klasifikazioa;
import dao.DenboraldiaDAO;
import dao.Konexioa;
import pojos.Denboraldia;
import pojos.TaldearenKlasifikazioa;
import util.LoggerUtil;
import util.LoggerUtil.DataAccessException;

/**
 * Ikuspegiaren (View) geruzako klasea. Boleibol ligako sailkapena ikusteko eta
 * kudeatzeko pantaila.
 */
public class KlasifikazioaMetodo implements ActionListener, MouseListener {

	private JPanel panela;
	private JComboBox<Denboraldia> denboraldiaCombo;
	private JTable klasifikazioaTaula;
	private DefaultTableModel taulaModeloa;
	private JButton gordeBotoia;
	private JButton kargatuBotoia;
	private JButton ezabatuDenboraldiaBotoia;
	private JButton saioaAmaituBotoia;
	private Klasifikazioa klasifikazioaService;
	private DenboraldiaDAO denboraldiaDAO;
	private boolean adminDa = false;

	/**
	 * KlasifikazioaMetodo klasearen eraikitzailea.
	 * 
	 * @param urdina Atzeko planoaren kolorea.
	 */
	public KlasifikazioaMetodo(Color urdina) {
		this(urdina, false);
	}

	/**
	 * KlasifikazioaMetodo klasearen eraikitzailea admin parametroarekin.
	 * 
	 * @param urdina  Atzeko planoaren kolorea.
	 * @param adminDa true admin bada.
	 */
	public KlasifikazioaMetodo(Color urdina, boolean adminDa) {
		this.adminDa = adminDa;
		klasifikazioaService = new Klasifikazioa();
		denboraldiaDAO = new DenboraldiaDAO();

		// Panela konfiguratu
		panela = new JPanel(null);
		panela.setBackground(urdina);

		JLabel titulua = new JLabel("KLASIFIKAZIOA");
		titulua.setForeground(Color.WHITE);
		titulua.setFont(new Font("Arial", Font.BOLD, 40));
		titulua.setBounds(320, 20, 400, 50);
		panela.add(titulua);

		// Denboraldia aukeratzeko
		denboraldiaCombo = new JComboBox<>();
		denboraldiakKargatu();
		denboraldiaCombo.setFont(new Font("Arial", Font.BOLD, 18));
		denboraldiaCombo.setBounds(100, 90, 200, 40);
		denboraldiaCombo.addActionListener(this);
		panela.add(denboraldiaCombo);

		// Admin bada, ezabatu botoia gehitu
		if (adminDa) {
			ezabatuDenboraldiaBotoia = new JButton("Ezabatu Denboraldia");
			ezabatuDenboraldiaBotoia.setFont(new Font("Arial", Font.BOLD, 14));
			ezabatuDenboraldiaBotoia.setBackground(new Color(200, 0, 0));
			ezabatuDenboraldiaBotoia.setForeground(Color.WHITE);
			ezabatuDenboraldiaBotoia.setBounds(320, 90, 180, 40);
			ezabatuDenboraldiaBotoia.addActionListener(this);
			panela.add(ezabatuDenboraldiaBotoia);
		}

		// Taula konfiguratu
		String[] zutabeak = { "Posizioa", "Taldea", "PJ", "PG", "PP", "Puntuak", "SI", "SG", "SD" };
		taulaModeloa = new DefaultTableModel(zutabeak, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		klasifikazioaTaula = new JTable(taulaModeloa);
		klasifikazioaTaula.setFont(new Font("Arial", Font.PLAIN, 14));
		klasifikazioaTaula.setFillsViewportHeight(true);
		klasifikazioaTaula.setShowGrid(true);
		klasifikazioaTaula.setGridColor(Color.LIGHT_GRAY);

		// Edukia zentratu
		DefaultTableCellRenderer zentratu = new DefaultTableCellRenderer();
		zentratu.setHorizontalAlignment(JLabel.CENTER);
		for (int i = 0; i < klasifikazioaTaula.getColumnCount(); i++) {
			klasifikazioaTaula.getColumnModel().getColumn(i).setCellRenderer(zentratu);
		}

		JScrollPane korritzePanela = new JScrollPane(klasifikazioaTaula);
		korritzePanela.setBounds(100, 150, 700, 250);
		panela.add(korritzePanela);

		// Botoiak
		gordeBotoia = new JButton("Gorde Klasifikazioa");
		gordeBotoia.setFont(new Font("Arial", Font.BOLD, 14));
		gordeBotoia.setBackground(new Color(0, 150, 0));
		gordeBotoia.setForeground(Color.WHITE);
		gordeBotoia.setBounds(150, 420, 180, 40);
		gordeBotoia.addActionListener(this);
		panela.add(gordeBotoia);

		kargatuBotoia = new JButton("Kargatu Klasifikazioa");
		kargatuBotoia.setFont(new Font("Arial", Font.BOLD, 14));
		kargatuBotoia.setBackground(new Color(0, 100, 200));
		kargatuBotoia.setForeground(Color.WHITE);
		kargatuBotoia.setBounds(350, 420, 180, 40);
		kargatuBotoia.addActionListener(this);
		panela.add(kargatuBotoia);

		saioaAmaituBotoia = new JButton("Saioa amaitu");
		saioaAmaituBotoia.setFont(new Font("Arial", Font.BOLD, 18));
		saioaAmaituBotoia.setBackground(Color.RED);
		saioaAmaituBotoia.setForeground(Color.WHITE);
		saioaAmaituBotoia.setBounds(700, 480, 170, 40);
		saioaAmaituBotoia.addActionListener(this);
		panela.add(saioaAmaituBotoia);

		if (denboraldiaCombo.getItemCount() > 0) {
			denboraldiaCombo.setSelectedIndex(0);
		}
	}

	/**
	 * Denboraldien zerrenda kargatzen du combo-an.
	 */
	private void denboraldiakKargatu() {
		denboraldiaCombo.removeAllItems();
		try {
			List<Denboraldia> denboraldiak = denboraldiaDAO.denboraldiakAtera();
			for (Denboraldia d : denboraldiak) {
				denboraldiaCombo.addItem(d);
			}
		} catch (DataAccessException e) {
			LoggerUtil.log("ERROR denboraldiak kargatzean: " + e.getMessage());
		}
	}

	/**
	 * Denboraldien zerrenda eguneratzen du.
	 */
	public void eguneratuDenboraldiak() {
		Denboraldia selected = (Denboraldia) denboraldiaCombo.getSelectedItem();
		int selectedId = (selected != null) ? selected.getDenboraldiaKod() : -1;
		denboraldiaCombo.removeAllItems();
		try {
			List<Denboraldia> denboraldiak = denboraldiaDAO.denboraldiakAtera();
			for (Denboraldia d : denboraldiak) {
				denboraldiaCombo.addItem(d);
			}
		} catch (DataAccessException e) {
			LoggerUtil.log("ERROR denboraldiak eguneratzean: " + e.getMessage());
		}
		if (selectedId != -1) {
			for (int i = 0; i < denboraldiaCombo.getItemCount(); i++) {
				Denboraldia d = denboraldiaCombo.getItemAt(i);
				if (d.getDenboraldiaKod() == selectedId) {
					denboraldiaCombo.setSelectedIndex(i);
					return;
				}
			}
		}
		if (denboraldiaCombo.getItemCount() > 0) {
			denboraldiaCombo.setSelectedIndex(0);
		}
	}

	/**
	 * Sailkapena kargatzen du taulan.
	 */
	private void kargatuKlasifikazioa() {
		taulaModeloa.setRowCount(0);
		Denboraldia denboraldia = (Denboraldia) denboraldiaCombo.getSelectedItem();
		if (denboraldia == null)
			return;

		try {
			List<TaldearenKlasifikazioa> klasifikazioa = klasifikazioaService
					.lortuKlasifikazioaDenboraldian(denboraldia.getDenboraldiaKod());

			int pos = 1;
			for (TaldearenKlasifikazioa tk : klasifikazioa) {
				Object[] row = { pos++, tk.getTaldea().getIzena(), tk.getPartidaJokatuak(), tk.getPartidaIrabaziak(),
						tk.getPartidaGalduak(), tk.getPuntuak(), tk.getSetakIrabaziak(), tk.getSetakGalduak(),
						tk.getSetDiferentzia() };
				taulaModeloa.addRow(row);
			}
		} catch (Exception e) {
			LoggerUtil.log("ERROR klasifikazioa kargatzean: " + e.getMessage());
			JOptionPane.showMessageDialog(panela, "Errorea klasifikazioa kargatzerakoan: " + e.getMessage(), "Errorea",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Taula eguneratzen du.
	 */
	public void eguneratuTaula() {
		kargatuKlasifikazioa();
	}

	/**
	 * XML fitxategia gordetzen du.
	 */
	private void gordeXML() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Gorde XML fitxategia");
		fileChooser.setSelectedFile(new File("klasifikazioa.xml"));

		if (fileChooser.showSaveDialog(panela) == JFileChooser.APPROVE_OPTION) {
			File fitxategia = fileChooser.getSelectedFile();
			try {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document doc = builder.newDocument();

				Element root = doc.createElement("klasifikazioa");
				doc.appendChild(root);

				for (int i = 0; i < taulaModeloa.getRowCount(); i++) {
					Element taldea = doc.createElement("taldea");

					Element pos = doc.createElement("posizioa");
					pos.appendChild(doc.createTextNode(taulaModeloa.getValueAt(i, 0).toString()));
					taldea.appendChild(pos);

					Element izena = doc.createElement("izena");
					izena.appendChild(doc.createTextNode(taulaModeloa.getValueAt(i, 1).toString()));
					taldea.appendChild(izena);

					Element pj = doc.createElement("pj");
					pj.appendChild(doc.createTextNode(taulaModeloa.getValueAt(i, 2).toString()));
					taldea.appendChild(pj);

					Element pg = doc.createElement("pg");
					pg.appendChild(doc.createTextNode(taulaModeloa.getValueAt(i, 3).toString()));
					taldea.appendChild(pg);

					Element pp = doc.createElement("pp");
					pp.appendChild(doc.createTextNode(taulaModeloa.getValueAt(i, 4).toString()));
					taldea.appendChild(pp);

					Element puntuak = doc.createElement("puntuak");
					puntuak.appendChild(doc.createTextNode(taulaModeloa.getValueAt(i, 5).toString()));
					taldea.appendChild(puntuak);

					Element si = doc.createElement("si");
					si.appendChild(doc.createTextNode(taulaModeloa.getValueAt(i, 6).toString()));
					taldea.appendChild(si);

					Element sg = doc.createElement("sg");
					sg.appendChild(doc.createTextNode(taulaModeloa.getValueAt(i, 7).toString()));
					taldea.appendChild(sg);

					Element sd = doc.createElement("sd");
					sd.appendChild(doc.createTextNode(taulaModeloa.getValueAt(i, 8).toString()));
					taldea.appendChild(sd);

					root.appendChild(taldea);
				}

				Transformer transformer = TransformerFactory.newInstance().newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.transform(new DOMSource(doc), new StreamResult(fitxategia));

				JOptionPane.showMessageDialog(panela, "XML ondo gorde da: " + fitxategia.getName());
				LoggerUtil.log("XML gorde da: " + fitxategia.getAbsolutePath());

			} catch (Exception ex) {
				LoggerUtil.log("ERROR XML gordetzean: " + ex.getMessage());
				JOptionPane.showMessageDialog(panela, "Errorea XML gordetzean: " + ex.getMessage());
			}
		}
	}

	/**
	 * XML fitxategia kargatzen du.
	 */
	private void kargatuXML() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Aukeratu XML fitxategia");

		if (fileChooser.showOpenDialog(panela) == JFileChooser.APPROVE_OPTION) {
			File fitxategia = fileChooser.getSelectedFile();
			try {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document doc = builder.parse(fitxategia);

				taulaModeloa.setRowCount(0);
				NodeList taldeak = doc.getElementsByTagName("taldea");
				for (int i = 0; i < taldeak.getLength(); i++) {
					Element taldea = (Element) taldeak.item(i);
					String posizioa = getTextValue(taldea, "posizioa");
					String izena = getTextValue(taldea, "izena");
					String pj = getTextValue(taldea, "pj");
					String pg = getTextValue(taldea, "pg");
					String pp = getTextValue(taldea, "pp");
					String puntuak = getTextValue(taldea, "puntuak");
					String si = getTextValue(taldea, "si");
					String sg = getTextValue(taldea, "sg");
					String sd = getTextValue(taldea, "sd");

					taulaModeloa.addRow(new Object[] { posizioa, izena, pj, pg, pp, puntuak, si, sg, sd });
				}
				JOptionPane.showMessageDialog(panela, "XML kargatu da: " + fitxategia.getName());
				LoggerUtil.log("XML kargatu da: " + fitxategia.getAbsolutePath());

			} catch (Exception ex) {
				LoggerUtil.log("ERROR XML kargatzean: " + ex.getMessage());
				JOptionPane.showMessageDialog(panela, "Errorea XML kargatzean: " + ex.getMessage());
			}
		}
	}

	/**
	 * XML elementu baten testua lortzen du.
	 */
	private String getTextValue(Element parent, String tagName) {
		NodeList list = parent.getElementsByTagName(tagName);
		if (list.getLength() > 0) {
			return list.item(0).getTextContent();
		}
		return "";
	}

	/**
	 * Denboraldia eta bere datu guztiak ezabatzen ditu.
	 */
	private void ezabatuDenboraldia() {
		Denboraldia selected = (Denboraldia) denboraldiaCombo.getSelectedItem();
		if (selected == null) {
			JOptionPane.showMessageDialog(panela, "Ez dago denboraldirik aukeratuta.");
			return;
		}

		if (selected.isAktiboa()) {
			JOptionPane.showMessageDialog(panela,
					"Ezin da denboraldi aktiboa ezabatu.\nAmaitu denboraldia lehenengo Partiduak atalean.",
					"Ezin da ezabatu", JOptionPane.WARNING_MESSAGE);
			return;
		}

		int confirm = JOptionPane.showConfirmDialog(panela, "Ziur zaude \"" + selected.getIzena()
				+ "\" denboraldia eta bere datu GUZTIAK ezabatu nahi dituzula?\n\n"
				+ "Honek ezabatuko ditu:\n- Denboraldi honetako partida guztiak\n- Denboraldi honetako jardunaldiak\n"
				+ "- Denboraldia bera\n\nEragiketa hau EZIN da desegin.", "⚠️ KONTUZ - Ezabatu denboraldia",
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

		if (confirm != JOptionPane.YES_OPTION)
			return;

		int confirm2 = JOptionPane.showConfirmDialog(panela, "AZKEN ABISUA: Ziur zaude?", "Baieztatu",
				JOptionPane.YES_NO_OPTION);
		if (confirm2 != JOptionPane.YES_OPTION)
			return;

		try {
			boolean emaitza = ezabatuDenboraldiaDatuGuztiekin(selected.getDenboraldiaKod());
			if (emaitza) {
				JOptionPane.showMessageDialog(panela, "Denboraldia ondo ezabatu da.");
				LoggerUtil.log("Denboraldia ezabatu da: " + selected.getIzena() + " (ID: "
						+ selected.getDenboraldiaKod() + ")");
				eguneratuDenboraldiak();
				eguneratuTaula();
			} else {
				JOptionPane.showMessageDialog(panela, "Errorea denboraldia ezabatzean.");
			}
		} catch (Exception ex) {
			LoggerUtil.log("ERROR denboraldia ezabatzean: " + ex.getMessage());
			JOptionPane.showMessageDialog(panela, "Errorea: " + ex.getMessage());
		}
	}

	/**
	 * Denboraldia eta bere datu guztiak ezabatzen ditu (transakzioan).
	 * 
	 * @param denboraldiaKod Ezabatu beharreko denboraldiaren IDa.
	 * @return true ondo ezabatu bada.
	 */
	private boolean ezabatuDenboraldiaDatuGuztiekin(int denboraldiaKod) {
		Konexioa konex = new Konexioa();
		Connection conn = null;

		try {
			konex.konexioaIreki();
			conn = konex.getKonexioa();
			conn.setAutoCommit(false);

			// 1. Lortu jardunaldien IDak
			String sqlJardunaldiak = "SELECT jaurdunaldi_kod FROM denboraldia_jaurdunaldia WHERE denboraldia_kod = ?";
			List<Integer> jardunaldiIds = new ArrayList<>();
			try (PreparedStatement ps = conn.prepareStatement(sqlJardunaldiak)) {
				ps.setInt(1, denboraldiaKod);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					jardunaldiIds.add(rs.getInt("jaurdunaldi_kod"));
				}
				rs.close();
			}

			// 2. Ezabatu partidak eta erlazioak
			if (!jardunaldiIds.isEmpty()) {
				StringBuilder inClause = new StringBuilder("(");
				for (int i = 0; i < jardunaldiIds.size(); i++) {
					if (i > 0)
						inClause.append(",");
					inClause.append(jardunaldiIds.get(i));
				}
				inClause.append(")");
				String inClauseStr = inClause.toString();

				// taldea_partida erlazioak
				String sqlTaldeaPartida = "DELETE FROM taldea_partida WHERE partida_kod IN "
						+ "(SELECT partida_kod FROM partida_jaurdunaldia WHERE jaurdunaldi_kod IN " + inClauseStr + ")";
				try (PreparedStatement ps = conn.prepareStatement(sqlTaldeaPartida)) {
					ps.executeUpdate();
				}

				// partida_jaurdunaldia erlazioak
				String sqlPartidaJardunaldia = "DELETE FROM partida_jaurdunaldia WHERE jaurdunaldi_kod IN "
						+ inClauseStr;
				try (PreparedStatement ps = conn.prepareStatement(sqlPartidaJardunaldia)) {
					ps.executeUpdate();
				}

				// Partidak
				String sqlPartidak = "DELETE p FROM partida p "
						+ "LEFT JOIN partida_jaurdunaldia pj ON p.partida_kod = pj.partida_kod "
						+ "WHERE pj.partida_kod IS NULL";
				try (PreparedStatement ps = conn.prepareStatement(sqlPartidak)) {
					ps.executeUpdate();
				}
			}

			// 3. denboraldia_jaurdunaldia erlazioak
			String sqlDenbJard = "DELETE FROM denboraldia_jaurdunaldia WHERE denboraldia_kod = ?";
			try (PreparedStatement ps = conn.prepareStatement(sqlDenbJard)) {
				ps.setInt(1, denboraldiaKod);
				ps.executeUpdate();
			}

			// 4. Erabiltzen ez diren jardunaldiak
			String sqlJardunaldiakEzabatu = "DELETE FROM jaurdunaldia WHERE jaurdunaldi_kod NOT IN "
					+ "(SELECT DISTINCT jaurdunaldi_kod FROM denboraldia_jaurdunaldia)";
			try (PreparedStatement ps = conn.prepareStatement(sqlJardunaldiakEzabatu)) {
				ps.executeUpdate();
			}

			// 5. Denboraldia
			String sqlDenboraldia = "DELETE FROM denboraldia WHERE denboraldia_kod = ?";
			int affected;
			try (PreparedStatement ps = conn.prepareStatement(sqlDenboraldia)) {
				ps.setInt(1, denboraldiaKod);
				affected = ps.executeUpdate();
			}

			conn.commit();
			return affected > 0;

		} catch (SQLException e) {
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException ex) {
				}
			}
			LoggerUtil.log("ERROR denboraldia ezabatzean (SQL): " + e.getMessage());
			return false;
		} finally {
			if (conn != null) {
				try {
					conn.setAutoCommit(true);
				} catch (SQLException e) {
				}
			}
			konex.konexioaItxi();
		}
	}

	/**
	 * @return Panela.
	 */
	public JPanel getPanela() {
		return panela;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();

		if (src == denboraldiaCombo) {
			kargatuKlasifikazioa();
		} else if (src == kargatuBotoia) {
			kargatuXML();
		} else if (src == gordeBotoia) {
			gordeXML();
		} else if (src == ezabatuDenboraldiaBotoia) {
			ezabatuDenboraldia();
		} else if (src == saioaAmaituBotoia) {
			SwingUtilities.invokeLater(() -> new Login().setVisible(true));
			JFrame frame = (JFrame) SwingUtilities.getWindowAncestor((Component) e.getSource());
			if (frame != null)
				frame.dispose();
		}
	}

	// MouseListener metodoak (erabili gabe)
	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}