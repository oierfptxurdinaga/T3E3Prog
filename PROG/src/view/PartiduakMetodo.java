package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dao.DenboraldiaDAO;
import dao.JardunaldiaDAO;
import dao.PartidaDAO;
import dao.TaldeaDAO;
import pojos.Denboraldia;
import pojos.Jardunaldia;
import pojos.Partida;
import pojos.Taldea;
import util.LoggerUtil;
import util.LoggerUtil.DataAccessException;

/**
 * Partiduak kudeatzeko interfaze grafikoa eskaintzen duen klasea.
 */
public class PartiduakMetodo implements ActionListener, MouseListener {

	private JPanel partiduakPanela;
	private JTextField etxekoSetak;
	private JTextField kanpokoSetak;
	private JComboBox<Denboraldia> denboraldiaCombo;
	private JLabel denboraldiaLabel;
	private JComboBox<Jardunaldia> jardunaldiaCombo;
	private JComboBox<Taldea> etxekoCombo;
	private JComboBox<Taldea> kanpokoCombo;
	private JButton puntuakSartuBotoia;
	private JButton saioaAmaituBotoia;
	private JButton hasiDenboraldiaBotoia;
	private JButton amaituDenboraldiaBotoia;
	private Main leihoNagusia;

	private TaldeaDAO taldeaDAO;
	private PartidaDAO partidaDAO;
	private DenboraldiaDAO denboraldiaDAO;
	private JardunaldiaDAO jardunaldiaDAO;

	/**
	 * PartiduakMetodo klasearen eraikitzailea.
	 * 
	 * @param kolorea      Atzeko planoaren kolorea.
	 * @param leihoNagusia Leiho nagusiaren erreferentzia.
	 */
	public PartiduakMetodo(Color kolorea, Main leihoNagusia) {
		this.leihoNagusia = leihoNagusia;
		taldeaDAO = new TaldeaDAO();
		partidaDAO = new PartidaDAO();
		denboraldiaDAO = new DenboraldiaDAO();
		jardunaldiaDAO = new JardunaldiaDAO();

		// Panela konfiguratu
		partiduakPanela = new JPanel(null);
		partiduakPanela.setBackground(new Color(0, 0, 160));

		JLabel titulua = new JLabel("PARTIDUAK SARTU");
		titulua.setForeground(Color.WHITE);
		titulua.setFont(new Font("Segoe UI", Font.BOLD, 36));
		titulua.setHorizontalAlignment(SwingConstants.CENTER);
		titulua.setBounds(200, 30, 400, 50);
		partiduakPanela.add(titulua);

		// Hasi denboraldia botoia
		hasiDenboraldiaBotoia = new JButton("Hasi Denboraldia");
		hasiDenboraldiaBotoia.setFont(new Font("Segoe UI", Font.BOLD, 16));
		hasiDenboraldiaBotoia.setBounds(500, 90, 160, 40);
		hasiDenboraldiaBotoia.setBackground(new Color(46, 204, 113));
		hasiDenboraldiaBotoia.setForeground(Color.WHITE);
		hasiDenboraldiaBotoia.setFocusPainted(false);
		hasiDenboraldiaBotoia.setBorderPainted(false);
		hasiDenboraldiaBotoia.addActionListener(this);
		partiduakPanela.add(hasiDenboraldiaBotoia);

		// Amaitu denboraldia botoia
		amaituDenboraldiaBotoia = new JButton("Amaitu Denboraldia");
		amaituDenboraldiaBotoia.setFont(new Font("Segoe UI", Font.BOLD, 16));
		amaituDenboraldiaBotoia.setBounds(670, 90, 160, 40);
		amaituDenboraldiaBotoia.setBackground(Color.ORANGE);
		amaituDenboraldiaBotoia.setForeground(Color.BLACK);
		amaituDenboraldiaBotoia.setFocusPainted(false);
		amaituDenboraldiaBotoia.setBorderPainted(false);
		amaituDenboraldiaBotoia.addActionListener(this);
		partiduakPanela.add(amaituDenboraldiaBotoia);

		// Denboraldia aukeratzeko
		JLabel denboraldiaEtiketa = new JLabel("Denboraldia:");
		denboraldiaEtiketa.setForeground(Color.WHITE);
		denboraldiaEtiketa.setFont(new Font("Segoe UI", Font.BOLD, 16));
		denboraldiaEtiketa.setBounds(100, 140, 200, 30);
		partiduakPanela.add(denboraldiaEtiketa);

		denboraldiaCombo = new JComboBox<>();
		denboraldiaCombo.setBounds(100, 180, 200, 35);
		denboraldiaCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		denboraldiaCombo.addActionListener(this);

		denboraldiaLabel = new JLabel("", SwingConstants.CENTER);
		denboraldiaLabel.setForeground(Color.WHITE);
		denboraldiaLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
		denboraldiaLabel.setBounds(100, 180, 200, 35);
		denboraldiaLabel.setOpaque(true);
		denboraldiaLabel.setBackground(new Color(0, 0, 100));

		// Jardunaldia aukeratzeko
		JLabel jardunaldiaEtiketa = new JLabel("Aukeratu jardunaldia:");
		jardunaldiaEtiketa.setForeground(Color.WHITE);
		jardunaldiaEtiketa.setFont(new Font("Segoe UI", Font.BOLD, 16));
		jardunaldiaEtiketa.setBounds(320, 140, 220, 30);
		partiduakPanela.add(jardunaldiaEtiketa);

		jardunaldiaCombo = new JComboBox<>();
		jardunaldiaCombo.setBounds(320, 180, 200, 35);
		jardunaldiaCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		partiduakPanela.add(jardunaldiaCombo);

		// Etxeko taldea
		JLabel etxekoEtiketa = new JLabel("Etxeko Taldea:");
		etxekoEtiketa.setForeground(Color.WHITE);
		etxekoEtiketa.setFont(new Font("Segoe UI", Font.BOLD, 16));
		etxekoEtiketa.setBounds(100, 240, 200, 30);
		partiduakPanela.add(etxekoEtiketa);

		etxekoCombo = new JComboBox<>();
		etxekoCombo.setBounds(100, 280, 200, 35);
		etxekoCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		partiduakPanela.add(etxekoCombo);

		// Kanpoko taldea
		JLabel kanpokoEtiketa = new JLabel("Kanpoko Taldea:");
		kanpokoEtiketa.setForeground(Color.WHITE);
		kanpokoEtiketa.setFont(new Font("Segoe UI", Font.BOLD, 16));
		kanpokoEtiketa.setBounds(320, 240, 200, 30);
		partiduakPanela.add(kanpokoEtiketa);

		kanpokoCombo = new JComboBox<>();
		kanpokoCombo.setBounds(320, 280, 200, 35);
		kanpokoCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		partiduakPanela.add(kanpokoCombo);

		// Setak sartzeko eremuak
		JLabel etxekoSetakEtiketa = new JLabel("Setak (0-3):");
		etxekoSetakEtiketa.setForeground(Color.WHITE);
		etxekoSetakEtiketa.setFont(new Font("Segoe UI", Font.BOLD, 14));
		etxekoSetakEtiketa.setBounds(100, 340, 120, 25);
		partiduakPanela.add(etxekoSetakEtiketa);

		etxekoSetak = new JTextField();
		etxekoSetak.setBounds(180, 340, 120, 30);
		etxekoSetak.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		partiduakPanela.add(etxekoSetak);

		JLabel kanpokoSetakEtiketa = new JLabel("Setak (0-3):");
		kanpokoSetakEtiketa.setForeground(Color.WHITE);
		kanpokoSetakEtiketa.setFont(new Font("Segoe UI", Font.BOLD, 14));
		kanpokoSetakEtiketa.setBounds(320, 340, 120, 25);
		partiduakPanela.add(kanpokoSetakEtiketa);

		kanpokoSetak = new JTextField();
		kanpokoSetak.setBounds(400, 340, 120, 30);
		kanpokoSetak.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		partiduakPanela.add(kanpokoSetak);

		// Sartu partidua botoia
		puntuakSartuBotoia = new JButton("Sartu partidua");
		puntuakSartuBotoia.setBounds(200, 400, 200, 40);
		puntuakSartuBotoia.setFont(new Font("Segoe UI", Font.BOLD, 16));
		puntuakSartuBotoia.setBackground(new Color(52, 152, 219));
		puntuakSartuBotoia.setForeground(Color.WHITE);
		puntuakSartuBotoia.setFocusPainted(false);
		puntuakSartuBotoia.setBorderPainted(false);
		puntuakSartuBotoia.addActionListener(this);
		puntuakSartuBotoia.addMouseListener(this);
		partiduakPanela.add(puntuakSartuBotoia);

		// Saioa amaitu botoia
		saioaAmaituBotoia = new JButton("Saioa amaitu");
		saioaAmaituBotoia.setFont(new Font("Segoe UI", Font.BOLD, 16));
		saioaAmaituBotoia.setBackground(new Color(231, 76, 60));
		saioaAmaituBotoia.setForeground(Color.WHITE);
		saioaAmaituBotoia.setFocusPainted(false);
		saioaAmaituBotoia.setBorderPainted(false);
		saioaAmaituBotoia.setBounds(650, 480, 180, 40);
		saioaAmaituBotoia.addActionListener(this);
		partiduakPanela.add(saioaAmaituBotoia);

		// Hasierako karga
		taldeakKargatu();
		eguneratuDenboraldiEgoera();

		try {
			Denboraldia aktiboa = denboraldiaDAO.denboraldiaAktiboaLortu();
			if (aktiboa != null) {
				denboraldiaLabel.setText(aktiboa.getIzena());
				jardunaldiakKargatu(aktiboa.getDenboraldiaKod());
			} else {
				denboraldiakKargatuCombo();
			}
		} catch (DataAccessException e) {
			LoggerUtil.log("ERROR denboraldia kargatzean: " + e.getMessage());
		}
	}

	/**
	 * Denboraldien zerrenda kargatzen du combo-an.
	 */
	private void denboraldiakKargatuCombo() {
		denboraldiaCombo.removeAllItems();
		try {
			List<Denboraldia> denboraldiak = denboraldiaDAO.denboraldiakAtera();
			for (Denboraldia d : denboraldiak) {
				denboraldiaCombo.addItem(d);
			}
			if (denboraldiaCombo.getItemCount() == 0) {
				denboraldiaCombo.addItem(new Denboraldia(0, "Ez dago denboraldirik", null, null, false, null, false));
			}
		} catch (DataAccessException e) {
			LoggerUtil.log("ERROR denboraldiak kargatzean: " + e.getMessage());
		}
	}

	/**
	 * Jardunaldiak kargatzen ditu denboraldiaren arabera.
	 * 
	 * @param denboraldiaKod Denboraldiaren IDa.
	 */
	private void jardunaldiakKargatu(int denboraldiaKod) {
		jardunaldiaCombo.removeAllItems();
		if (denboraldiaKod == 0)
			return;

		try {
			List<Jardunaldia> jardunaldiak = jardunaldiaDAO.lostuJardunaldiDenboraldiBidez(denboraldiaKod);
			jardunaldiak.sort((a, b) -> a.getHasieraData().compareTo(b.getHasieraData()));
			int zenbakia = 1;
			for (Jardunaldia j : jardunaldiak) {
				final int num = zenbakia++;
				Jardunaldia jWrapper = new Jardunaldia(j.getJardunaldiKod(), j.getHasieraData(), j.getAmaieraData()) {
					@Override
					public String toString() {
						return "Jardunaldia " + num;
					}
				};
				jardunaldiaCombo.addItem(jWrapper);
			}
		} catch (DataAccessException e) {
			LoggerUtil.log("ERROR jardunaldiak kargatzean: " + e.getMessage());
		}
	}

	/**
	 * Taldeak kargatzen ditu combo-etan.
	 */
	private void taldeakKargatu() {
		try {
			List<Taldea> taldeak = taldeaDAO.taldeGuztiakLortu();
			for (Taldea t : taldeak) {
				if (t.getTaldeaKod() != 0) {
					etxekoCombo.addItem(t);
					kanpokoCombo.addItem(t);
				}
			}
		} catch (DataAccessException e) {
			LoggerUtil.log("ERROR taldeak kargatzean: " + e.getMessage());
		}
	}

	/**
	 * Taldeen zerrenda eguneratzen du (kanpotik deitzeko).
	 */
	public void eguneratuTaldeak() {
		etxekoCombo.removeAllItems();
		kanpokoCombo.removeAllItems();
		taldeakKargatu();
	}

	/**
	 * Denboraldiaren egoeraren arabera interfazea eguneratzen du.
	 */
	private void eguneratuDenboraldiEgoera() {
		try {
			Denboraldia aktiboa = denboraldiaDAO.denboraldiaAktiboaLortu();
			boolean aktibo = (aktiboa != null);

			if (aktibo) {
				partiduakPanela.remove(denboraldiaCombo);
				denboraldiaLabel.setText(aktiboa.getIzena());
				partiduakPanela.add(denboraldiaLabel);
			} else {
				partiduakPanela.remove(denboraldiaLabel);
				denboraldiakKargatuCombo();
				partiduakPanela.add(denboraldiaCombo);
				denboraldiaLabel.setText("Ez dago denboraldi aktiborik");
			}

			hasiDenboraldiaBotoia.setEnabled(!aktibo);
			amaituDenboraldiaBotoia.setEnabled(aktibo);
			puntuakSartuBotoia.setEnabled(aktibo);
			jardunaldiaCombo.setEnabled(aktibo);
			etxekoCombo.setEnabled(aktibo);
			kanpokoCombo.setEnabled(aktibo);
			etxekoSetak.setEnabled(aktibo);
			kanpokoSetak.setEnabled(aktibo);

			partiduakPanela.revalidate();
			partiduakPanela.repaint();
		} catch (DataAccessException e) {
			LoggerUtil.log("ERROR denboraldi egoera eguneratzean: " + e.getMessage());
		}
	}

	/**
	 * Denboraldia hasi (berria sortu edo existitzen dena aktibatu).
	 */
	private void hasiDenboraldiaBerria() {
		try {
			List<Denboraldia> existitzenDirenak = denboraldiaDAO.denboraldiakAtera();

			// Aukerak prestatu
			String[] aukerak;
			if (existitzenDirenak.isEmpty()) {
				aukerak = new String[] { "Berria sortu" };
			} else {
				aukerak = new String[existitzenDirenak.size() + 1];
				for (int i = 0; i < existitzenDirenak.size(); i++) {
					aukerak[i] = existitzenDirenak.get(i).getIzena();
				}
				aukerak[existitzenDirenak.size()] = "Berria sortu";
			}

			String aukeratua = (String) JOptionPane.showInputDialog(partiduakPanela,
					"Aukeratu denboraldi bat hasteko edo sortu berri bat:", "Denboraldia hasi",
					JOptionPane.QUESTION_MESSAGE, null, aukerak, aukerak[0]);

			if (aukeratua == null)
				return;

			Denboraldia aukeratutakoDenboraldia = null;
			String izenBerria = null;

			if (aukeratua.equals("Berria sortu")) {
				izenBerria = JOptionPane.showInputDialog(partiduakPanela,
						"Sartu denboraldiaren izena (adibidez: 2025/2026):");
				if (izenBerria == null || izenBerria.trim().isEmpty())
					return;

				// Formatoa balidatu
				String[] parteak = izenBerria.split("/");
				if (parteak.length != 2) {
					JOptionPane.showMessageDialog(partiduakPanela, "Formato okerra. 'AAAA/AAAA' izan behar du.");
					return;
				}
				try {
					Integer.parseInt(parteak[0].trim());
					Integer.parseInt(parteak[1].trim());
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(partiduakPanela, "Urteak zenbakiak izan behar dira.");
					return;
				}

				// Izena existitzen den egiaztatu
				for (Denboraldia d : existitzenDirenak) {
					if (d.getIzena().equals(izenBerria)) {
						JOptionPane.showMessageDialog(partiduakPanela,
								"Izen hori duen denboraldia jada existitzen da. Aukeratu beste izen bat.", "Errorea",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
			} else {
				for (Denboraldia d : existitzenDirenak) {
					if (d.getIzena().equals(aukeratua)) {
						aukeratutakoDenboraldia = d;
						break;
					}
				}
			}

			// Denboraldi aktiboa badago, amaitu
			Denboraldia aktiboa = denboraldiaDAO.denboraldiaAktiboaLortu();
			if (aktiboa != null) {
				int resp = JOptionPane.showConfirmDialog(partiduakPanela,
						"Dagoeneko badago denboraldi aktibo bat (" + aktiboa.getIzena() + "). Amaitu eta berria hasi?",
						"Denboraldia aktiboa", JOptionPane.YES_NO_OPTION);
				if (resp != JOptionPane.YES_OPTION)
					return;
				denboraldiaDAO.denboraldiaAmaitu(null);
			}

			int newId;
			String azkenIzena;

			if (izenBerria != null) {
				// Denboraldi berria sortu
				Denboraldia d = new Denboraldia();
				d.setIzena(izenBerria);
				String[] parteak = izenBerria.split("/");
				int urteaHasiera = Integer.parseInt(parteak[0].trim());
				int urteaAmaiera = Integer.parseInt(parteak[1].trim());

				Calendar cal = Calendar.getInstance();
				cal.set(urteaHasiera, Calendar.SEPTEMBER, 1, 0, 0, 0);
				d.setHasieraData(cal.getTime());
				cal.set(urteaAmaiera, Calendar.JUNE, 30, 23, 59, 59);
				d.setAmaieraData(cal.getTime());

				newId = denboraldiaDAO.denboraldiaHasi(d);
				if (newId == -1)
					throw new DataAccessException("Errorea denboraldia sortzean.");
				azkenIzena = izenBerria;

				// Jardunaldiak sortu
				List<Taldea> taldeak = taldeaDAO.taldeGuztiakLortu();
				taldeak.removeIf(t -> t.getTaldeaKod() == 0);
				if (taldeak.size() < 2) {
					JOptionPane.showMessageDialog(partiduakPanela, "Ez dago talde nahikorik denboraldia hasteko.",
							"Errorea", JOptionPane.ERROR_MESSAGE);
					return;
				}

				int taldeKopurua = taldeak.size();
				int jardunaldiKopurua = (taldeKopurua - 1) * 2;
				Calendar calJard = Calendar.getInstance();
				Date gaur = new Date();
				for (int i = 1; i <= jardunaldiKopurua; i++) {
					Jardunaldia j = new Jardunaldia();
					calJard.setTime(gaur);
					calJard.add(Calendar.DAY_OF_YEAR, (i - 1) * 7);
					j.setHasieraData(calJard.getTime());
					j.setAmaieraData(calJard.getTime());
					int jardunaldiKod = jardunaldiaDAO.jardunaldiaSortu(j);
					if (jardunaldiKod == -1)
						throw new DataAccessException("Errorea jardunaldia sortzean.");
					boolean lotuta = jardunaldiaDAO.denboraldiaAsoziatu(newId, jardunaldiKod);
					if (!lotuta)
						throw new DataAccessException("Errorea jardunaldia denboraldiarekin lotzean.");
				}
			} else {
				// Existitzen den denboraldia aktibatu
				newId = aukeratutakoDenboraldia.getDenboraldiaKod();
				azkenIzena = aukeratutakoDenboraldia.getIzena();

				String sqlUpdate = "UPDATE denboraldia SET aktiboa = false WHERE aktiboa = true";
				String sqlActivate = "UPDATE denboraldia SET aktiboa = true WHERE denboraldia_kod = ?";

				java.sql.Connection conn = null;
				try {
					denboraldiaDAO.konexioa.konexioaIreki();
					conn = denboraldiaDAO.konexioa.getKonexioa();
					conn.setAutoCommit(false);

					try (java.sql.PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate)) {
						psUpdate.executeUpdate();
					}
					try (java.sql.PreparedStatement psActivate = conn.prepareStatement(sqlActivate)) {
						psActivate.setInt(1, newId);
						psActivate.executeUpdate();
					}
					conn.commit();
				} catch (Exception e) {
					if (conn != null)
						conn.rollback();
					throw e;
				} finally {
					if (conn != null)
						conn.setAutoCommit(true);
					denboraldiaDAO.konexioa.konexioaItxi();
				}
			}

			JOptionPane.showMessageDialog(partiduakPanela, "Denboraldia aktibatuta: " + azkenIzena);
			LoggerUtil.log("Denboraldia hasi da: " + azkenIzena);
			eguneratuDenboraldiEgoera();
			jardunaldiakKargatu(newId);

			if (leihoNagusia != null) {
				leihoNagusia.eguneratuPestanenEgoera();
			}
		} catch (DataAccessException e) {
			LoggerUtil.log("ERROR denboraldia hastean: " + e.getMessage());
			JOptionPane.showMessageDialog(partiduakPanela, "Errorea denboraldia hasteko: " + e.getMessage(), "Errorea",
					JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			LoggerUtil.log("ERROR orokorra denboraldia hastean: " + e.getMessage());
			JOptionPane.showMessageDialog(partiduakPanela, "Errorea: " + e.getMessage(), "Errorea",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Denboraldia amaitzen du.
	 */
	private void amaituDenboraldia() {
		try {
			Denboraldia aktiboa = denboraldiaDAO.denboraldiaAktiboaLortu();
			if (aktiboa == null) {
				JOptionPane.showMessageDialog(partiduakPanela, "Ez dago denboraldi aktiborik amaitzeko.");
				return;
			}
			int resp = JOptionPane.showConfirmDialog(partiduakPanela,
					"Ziur zaude \"" + aktiboa.getIzena() + "\" denboraldia amaitu nahi duzula?", "Denboraldia amaitu",
					JOptionPane.YES_NO_OPTION);
			if (resp == JOptionPane.YES_OPTION) {
				boolean amaitua = denboraldiaDAO.denboraldiaAmaitu(null);
				if (amaitua) {
					JOptionPane.showMessageDialog(partiduakPanela, "Denboraldia amaitu da.");
					LoggerUtil.log("Denboraldia amaitu da: " + aktiboa.getIzena());
					eguneratuDenboraldiEgoera();
					if (leihoNagusia != null) {
						leihoNagusia.eguneratuPestanenEgoera();
					}
				} else {
					JOptionPane.showMessageDialog(partiduakPanela, "Errorea denboraldia amaitzean.");
				}
			}
		} catch (DataAccessException e) {
			LoggerUtil.log("ERROR denboraldia amaitzean: " + e.getMessage());
			JOptionPane.showMessageDialog(partiduakPanela, "Errorea: " + e.getMessage(), "Errorea",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Partida berria prozesatzen du.
	 */
	private void prozesatuPartidua() {
		try {
			Denboraldia aktiboa = denboraldiaDAO.denboraldiaAktiboaLortu();
			if (aktiboa == null) {
				JOptionPane.showMessageDialog(partiduakPanela,
						"Ezin da partidarik sartu. Ez dago denboraldi aktiborik.\nMesedez, hasi denboraldia lehenengo.",
						"Errorea", JOptionPane.ERROR_MESSAGE);
				return;
			}

			Jardunaldia jardunaldia = (Jardunaldia) jardunaldiaCombo.getSelectedItem();
			Taldea etxekoTaldea = (Taldea) etxekoCombo.getSelectedItem();
			Taldea kanpokoTaldea = (Taldea) kanpokoCombo.getSelectedItem();

			if (jardunaldia == null)
				throw new IllegalStateException("Mesedez, aukeratu jardunaldi bat.");
			if (etxekoTaldea == null || kanpokoTaldea == null)
				throw new IllegalStateException("Mesedez, aukeratu etxeko eta kanpoko taldeak.");
			if (etxekoTaldea.getTaldeaKod() == kanpokoTaldea.getTaldeaKod())
				throw new IllegalArgumentException("Etxeko eta kanpoko taldea ezin dira berdinak izan.");

			int etxekoS, kanpokoS;
			try {
				etxekoS = Integer.parseInt(etxekoSetak.getText().trim());
				kanpokoS = Integer.parseInt(kanpokoSetak.getText().trim());
			} catch (NumberFormatException ex) {
				throw new IllegalArgumentException("Mesedez, sartu zenbaki baliodunak setetan.");
			}

			// Seten balidazioa
			if (etxekoS < 0 || etxekoS > 3 || kanpokoS < 0 || kanpokoS > 3)
				throw new IllegalArgumentException("Setak 0 eta 3 artean egon behar dira.");
			if (!((etxekoS == 3 && kanpokoS <= 2) || (kanpokoS == 3 && etxekoS <= 2)))
				throw new IllegalArgumentException("Partidu batek 3 set irabazi behar ditu.");
			if ((etxekoS + kanpokoS) > 5)
				throw new IllegalArgumentException("Set guztien batura ezin da 5 baino handiagoa izan.");

			Partida partida = new Partida();
			partida.setData(new Date());
			partida.setOrdua(new SimpleDateFormat("HH:mm").format(new Date()));
			partida.setEmaitza(etxekoS + "-" + kanpokoS);
			partida.setZigorrak(0);
			partida.setTxartelak(0);
			partida.setEtxekoTaldea(etxekoTaldea);
			partida.setKanpokoTaldea(kanpokoTaldea);
			partida.setJardunaldia(jardunaldia);

			partidaDAO.sortuPartida(partida);

			JOptionPane.showMessageDialog(partiduakPanela, "Partidua ondo gorde da.", "Ondo",
					JOptionPane.INFORMATION_MESSAGE);
			etxekoSetak.setText("");
			kanpokoSetak.setText("");

			if (leihoNagusia != null) {
				leihoNagusia.eguneratuDena();
			}
		} catch (DataAccessException e) {
			LoggerUtil.log("ERROR partidua gordetzean: " + e.getMessage());
			JOptionPane.showMessageDialog(partiduakPanela, "Errorea datu-basean: " + e.getMessage(), "Errorea",
					JOptionPane.ERROR_MESSAGE);
		} catch (IllegalStateException | IllegalArgumentException e) {
			JOptionPane.showMessageDialog(partiduakPanela, "Errorea: " + e.getMessage(), "Errorea",
					JOptionPane.WARNING_MESSAGE);
		} catch (Exception e) {
			LoggerUtil.log("ERROR orokorra partidua prozesatzean: " + e.getMessage());
			JOptionPane.showMessageDialog(partiduakPanela, "Errorea partidua sartzerakoan: " + e.getMessage(),
					"Errorea", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * @return Partiduak panela.
	 */
	public JPanel getPanela() {
		return partiduakPanela;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();

		if (src == denboraldiaCombo) {
			Denboraldia selected = (Denboraldia) denboraldiaCombo.getSelectedItem();
			if (selected != null && selected.getDenboraldiaKod() != 0) {
				jardunaldiakKargatu(selected.getDenboraldiaKod());
			} else {
				jardunaldiaCombo.removeAllItems();
			}
		} else if (src == hasiDenboraldiaBotoia) {
			hasiDenboraldiaBerria();
		} else if (src == amaituDenboraldiaBotoia) {
			amaituDenboraldia();
		} else if (src == puntuakSartuBotoia) {
			prozesatuPartidua();
		} else if (src == saioaAmaituBotoia) {
			SwingUtilities.invokeLater(() -> new Login().setVisible(true));
			JFrame frame = (JFrame) SwingUtilities.getWindowAncestor((Component) e.getSource());
			if (frame != null)
				frame.dispose();
		}
	}

	// MouseListener metodoak (hover efektua)
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
		puntuakSartuBotoia.setBackground(new Color(41, 128, 185));
	}

	@Override
	public void mouseExited(MouseEvent e) {
		puntuakSartuBotoia.setBackground(new Color(52, 152, 219));
	}
}