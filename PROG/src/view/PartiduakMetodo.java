package view;

import javax.swing.*;
import model.*;
import dao.DenboraldiaDAO;
import dao.JardunaldiaDAO;
import dao.PartidaDAO;
import dao.TaldeaDAO;
import pojos.Denboraldia;
import pojos.Jardunaldia;
import pojos.Partida;
import pojos.Taldea;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Partiduak kudeatzeko interfaze grafikoa eskaintzen duen klasea.
 */
public class PartiduakMetodo implements ActionListener, MouseListener {

	private JPanel partiduakPanela;
	private JTextField etxekoSetak;
	private JTextField kanpokoSetak;
	private JComboBox<Denboraldia> denboraldiaCombo;
	private JComboBox<Jardunaldia> jardunaldiaCombo;
	private JComboBox<Taldea> etxekoCombo;
	private JComboBox<Taldea> kanpokoCombo;
	private JButton puntuakSartuBotoia;
	private JButton saioaAmaituBotoia;
	private JLabel titulua;
	private JLabel denboraldiaEtiketa;
	private JLabel jardunaldiaEtiketa;
	private JLabel etxekoEtiketa;
	private JLabel etxekoSetakEtiketa;
	private JLabel kanpokoEtiketa;
	private JLabel kanpokoSetakEtiketa;
	private JButton hasiDenboraldiaBotoia;
	private Main leihoNagusia;

	private TaldeaDAO taldeaDAO;
	private PartidaDAO partidaDAO;
	private DenboraldiaDAO denboraldiaDAO;
	private JardunaldiaDAO jardunaldiaDAO;

	public PartiduakMetodo(Color kolorea, Main leihoNagusia) {
		this.leihoNagusia = leihoNagusia;
		taldeaDAO = new TaldeaDAO();
		partidaDAO = new PartidaDAO();
		denboraldiaDAO = new DenboraldiaDAO();
		jardunaldiaDAO = new JardunaldiaDAO();

		partiduakPanela = new JPanel(null);
		partiduakPanela.setBackground(new Color(0, 0, 160));

		titulua = new JLabel("PARTIDUAK SARTU");
		titulua.setForeground(Color.WHITE);
		titulua.setFont(new Font("Segoe UI", Font.BOLD, 36));
		titulua.setHorizontalAlignment(SwingConstants.CENTER);
		titulua.setBounds(200, 30, 400, 50);
		partiduakPanela.add(titulua);

		hasiDenboraldiaBotoia = new JButton("Hasi Denboraldia");
		hasiDenboraldiaBotoia.setFont(new Font("Segoe UI", Font.BOLD, 16));
		hasiDenboraldiaBotoia.setBounds(600, 90, 200, 40);
		hasiDenboraldiaBotoia.setBackground(new Color(46, 204, 113));
		hasiDenboraldiaBotoia.setForeground(Color.WHITE);
		hasiDenboraldiaBotoia.setFocusPainted(false);
		hasiDenboraldiaBotoia.setBorderPainted(false);
		hasiDenboraldiaBotoia.addActionListener(this);
		partiduakPanela.add(hasiDenboraldiaBotoia);

		denboraldiaEtiketa = new JLabel("Aukeratu denboraldia:");
		denboraldiaEtiketa.setForeground(Color.WHITE);
		denboraldiaEtiketa.setFont(new Font("Segoe UI", Font.BOLD, 16));
		denboraldiaEtiketa.setBounds(100, 140, 200, 30);
		partiduakPanela.add(denboraldiaEtiketa);

		denboraldiaCombo = new JComboBox<>();
		denboraldiaCombo.setBounds(100, 180, 200, 35);
		denboraldiaCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		denboraldiaCombo.addActionListener(this);
		partiduakPanela.add(denboraldiaCombo);

		jardunaldiaEtiketa = new JLabel("Aukeratu jardunaldia:");
		jardunaldiaEtiketa.setForeground(Color.WHITE);
		jardunaldiaEtiketa.setFont(new Font("Segoe UI", Font.BOLD, 16));
		jardunaldiaEtiketa.setBounds(320, 140, 220, 30);
		partiduakPanela.add(jardunaldiaEtiketa);

		jardunaldiaCombo = new JComboBox<>();
		jardunaldiaCombo.setBounds(320, 180, 200, 35);
		jardunaldiaCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		partiduakPanela.add(jardunaldiaCombo);

		etxekoEtiketa = new JLabel("Etxeko Taldea:");
		etxekoEtiketa.setForeground(Color.WHITE);
		etxekoEtiketa.setFont(new Font("Segoe UI", Font.BOLD, 16));
		etxekoEtiketa.setBounds(100, 240, 200, 30);
		partiduakPanela.add(etxekoEtiketa);

		etxekoCombo = new JComboBox<>();
		etxekoCombo.setBounds(100, 280, 200, 35);
		etxekoCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		partiduakPanela.add(etxekoCombo);

		kanpokoEtiketa = new JLabel("Kanpoko Taldea:");
		kanpokoEtiketa.setForeground(Color.WHITE);
		kanpokoEtiketa.setFont(new Font("Segoe UI", Font.BOLD, 16));
		kanpokoEtiketa.setBounds(320, 240, 200, 30);
		partiduakPanela.add(kanpokoEtiketa);

		kanpokoCombo = new JComboBox<>();
		kanpokoCombo.setBounds(320, 280, 200, 35);
		kanpokoCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		partiduakPanela.add(kanpokoCombo);

		etxekoSetakEtiketa = new JLabel("Setak (0-3):");
		etxekoSetakEtiketa.setForeground(Color.WHITE);
		etxekoSetakEtiketa.setFont(new Font("Segoe UI", Font.BOLD, 14));
		etxekoSetakEtiketa.setBounds(100, 340, 120, 25);
		partiduakPanela.add(etxekoSetakEtiketa);

		etxekoSetak = new JTextField();
		etxekoSetak.setBounds(180, 340, 120, 30);
		etxekoSetak.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		partiduakPanela.add(etxekoSetak);

		kanpokoSetakEtiketa = new JLabel("Setak (0-3):");
		kanpokoSetakEtiketa.setForeground(Color.WHITE);
		kanpokoSetakEtiketa.setFont(new Font("Segoe UI", Font.BOLD, 14));
		kanpokoSetakEtiketa.setBounds(320, 340, 120, 25);
		partiduakPanela.add(kanpokoSetakEtiketa);

		kanpokoSetak = new JTextField();
		kanpokoSetak.setBounds(400, 340, 120, 30);
		kanpokoSetak.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		partiduakPanela.add(kanpokoSetak);

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

		saioaAmaituBotoia = new JButton("Saioa amaitu");
		saioaAmaituBotoia.setFont(new Font("Segoe UI", Font.BOLD, 16));
		saioaAmaituBotoia.setBackground(new Color(231, 76, 60));
		saioaAmaituBotoia.setForeground(Color.WHITE);
		saioaAmaituBotoia.setFocusPainted(false);
		saioaAmaituBotoia.setBorderPainted(false);
		saioaAmaituBotoia.setBounds(650, 480, 180, 40);
		saioaAmaituBotoia.addActionListener(this);
		partiduakPanela.add(saioaAmaituBotoia);

		// Kargatu datuak
		denboraldiakKargatu();
		taldeakKargatu();

		// Hautatu denboraldi aktiboa automatikoki (existitzen bada)
		Denboraldia aktiboa = denboraldiaDAO.denboraldiaAktiboaLortu();
		if (aktiboa != null) {
			for (int i = 0; i < denboraldiaCombo.getItemCount(); i++) {
				Denboraldia item = denboraldiaCombo.getItemAt(i);
				if (item.getDenboraldiaKod() == aktiboa.getDenboraldiaKod()) {
					denboraldiaCombo.setSelectedIndex(i);
					break;
				}
			}
		} else if (denboraldiaCombo.getItemCount() > 0) {
			denboraldiaCombo.setSelectedIndex(0);
		}
	}

	private void denboraldiakKargatu() {
		denboraldiaCombo.removeAllItems();
		List<Denboraldia> denboraldiak = denboraldiaDAO.denboraldiakAtera();
		for (Denboraldia d : denboraldiak) {
			denboraldiaCombo.addItem(d);
		}
		// Ez badago denboraldirik, jarri item berezi bat
		if (denboraldiaCombo.getItemCount() == 0) {
			denboraldiaCombo.addItem(new Denboraldia(0, "Ez dago denboraldirik", null, null, false, null, false));
		}
	}

	private void jardunaldiakKargatu(int denboraldiaKod) {
		jardunaldiaCombo.removeAllItems();
		if (denboraldiaKod == 0) {
			// Ez badago denboraldi errealik, ez kargatu jardunaldirik
			return;
		}
		List<Jardunaldia> jardunaldiak = jardunaldiaDAO.lostuJardunaldiDenboraldiBidez(denboraldiaKod);
		// Ordenatu dataren arabera
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
	}

	private void taldeakKargatu() {
		List<Taldea> taldeak = taldeaDAO.taldeGuztiakLortu();
		for (Taldea t : taldeak) {
			if (t.getTaldeaKod() != 0) {
				etxekoCombo.addItem(t);
				kanpokoCombo.addItem(t);
			}
		}
	}

	private void hasiDenboraldiaBerria() throws Exception {
		// ... (aurreko bertsio berdina, ez da aldatu)
		String izena = JOptionPane.showInputDialog(partiduakPanela,
				"Sartu denboraldiaren izena (adibidez: 2025/2026):");
		if (izena == null || izena.trim().isEmpty())
			return;

		String[] parteak = izena.split("/");
		if (parteak.length != 2) {
			JOptionPane.showMessageDialog(partiduakPanela, "Formato okerra. 'AAAA/AAAA' izan behar du.");
			return;
		}
		int urteaHasiera, urteaAmaiera;
		try {
			urteaHasiera = Integer.parseInt(parteak[0].trim());
			urteaAmaiera = Integer.parseInt(parteak[1].trim());
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(partiduakPanela, "Urteak zenbakiak izan behar dira.");
			return;
		}

		Denboraldia aktiboa = denboraldiaDAO.denboraldiaAktiboaLortu();
		if (aktiboa != null) {
			int resp = JOptionPane.showConfirmDialog(partiduakPanela,
					"Dagoeneko badago denboraldi aktibo bat (" + aktiboa.getIzena() + "). Amaitu eta berria hasi?",
					"Denboraldia aktiboa", JOptionPane.YES_NO_OPTION);
			if (resp != JOptionPane.YES_OPTION) {
				return;
			} else {
				denboraldiaDAO.denboraldiaAmaitu(null);
			}
		}

		Denboraldia d = new Denboraldia();
		d.setIzena(izena);
		Calendar cal = Calendar.getInstance();
		cal.set(urteaHasiera, Calendar.SEPTEMBER, 1, 0, 0, 0);
		d.setHasieraData(cal.getTime());
		cal.set(urteaAmaiera, Calendar.JUNE, 30, 23, 59, 59);
		d.setAmaieraData(cal.getTime());

		int newId = denboraldiaDAO.denboraldiaHasi(d);
		if (newId == -1) {
			throw new Exception("Errorea denboraldia sortzean.");
		}

		List<Taldea> taldeak = taldeaDAO.taldeGuztiakLortu();
		taldeak.removeIf(t -> t.getTaldeaKod() == 0);
		if (taldeak.size() < 2) {
			JOptionPane.showMessageDialog(partiduakPanela, "Ez dago talde nahikorik denboraldia hasteko.", "Errorea",
					JOptionPane.ERROR_MESSAGE);
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
				throw new Exception("Errorea jardunaldia sortzean.");
			boolean lotuta = jardunaldiaDAO.denboraldiaAsoziatu(newId, jardunaldiKod);
			if (!lotuta)
				throw new Exception("Errorea jardunaldia denboraldiarekin lotzean.");
		}

		JOptionPane.showMessageDialog(partiduakPanela, "Denboraldia ondo sortu da: " + izena);
		
		// Berriz kargatu denboraldien zerrenda eta hautatu berria
		denboraldiakKargatu();
		for (int i = 0; i < denboraldiaCombo.getItemCount(); i++) {
			Denboraldia item = denboraldiaCombo.getItemAt(i);
			if (item.getDenboraldiaKod() == newId) {
				denboraldiaCombo.setSelectedIndex(i);
				break;
			}
		}
		if (leihoNagusia != null) {
			leihoNagusia.eguneratuPestanenEgoera();
		}
	}

	private void prozesatuPartidua() throws Exception {
		// Denboraldi aktiboa egiaztatu
		Denboraldia aktiboa = denboraldiaDAO.denboraldiaAktiboaLortu();
		if (aktiboa == null) {
			JOptionPane.showMessageDialog(partiduakPanela,
					"Ezin da partidarik sartu. Ez dago denboraldi aktiborik.\nMesedez, hasi denboraldia lehenengo.",
					"Errorea", JOptionPane.ERROR_MESSAGE);
			return;
		}

		Denboraldia denboraldia = (Denboraldia) denboraldiaCombo.getSelectedItem();
		Jardunaldia jardunaldia = (Jardunaldia) jardunaldiaCombo.getSelectedItem();
		Taldea etxekoTaldea = (Taldea) etxekoCombo.getSelectedItem();
		Taldea kanpokoTaldea = (Taldea) kanpokoCombo.getSelectedItem();

		if (denboraldia == null || denboraldia.getDenboraldiaKod() == 0) {
			throw new IllegalStateException("Mesedez, aukeratu baliozko denboraldi bat.");
		}
		if (jardunaldia == null) {
			throw new IllegalStateException("Mesedez, aukeratu jardunaldi bat.");
		}
		if (etxekoTaldea == null || kanpokoTaldea == null) {
			throw new IllegalStateException("Mesedez, aukeratu etxeko eta kanpoko taldeak.");
		}
		if (etxekoTaldea.getTaldeaKod() == kanpokoTaldea.getTaldeaKod()) {
			throw new IllegalArgumentException("Etxeko eta kanpoko taldea ezin dira berdinak izan.");
		}

		int etxekoS, kanpokoS;
		try {
			etxekoS = Integer.parseInt(etxekoSetak.getText().trim());
			kanpokoS = Integer.parseInt(kanpokoSetak.getText().trim());
		} catch (NumberFormatException ex) {
			throw new NumberFormatException("Mesedez, sartu zenbaki baliodunak setetan.");
		}
		if (etxekoS < 0 || etxekoS > 3 || kanpokoS < 0 || kanpokoS > 3) {
			throw new IllegalArgumentException("Setak 0 eta 3 artean egon behar dira.");
		}
		if (!((etxekoS == 3 && kanpokoS <= 2) || (kanpokoS == 3 && etxekoS <= 2))) {
			throw new IllegalArgumentException("Partidu batek 3 set irabazi behar ditu (bestea 0, 1 edo 2).");
		}
		if ((etxekoS + kanpokoS) > 5) {
			throw new IllegalArgumentException("Set guztien batura ezin da 5 baino handiagoa izan.");
		}

		Partida partida = new Partida();
		partida.setData(new Date());
		partida.setOrdua(new SimpleDateFormat("HH:mm").format(new Date()));
		partida.setEmaitza(etxekoS + "-" + kanpokoS);
		partida.setZigorrak(0);
		partida.setTxartelak(0);
		partida.setEtxekoTaldea(etxekoTaldea);
		partida.setKanpokoTaldea(kanpokoTaldea);
		partida.setJardunaldia(jardunaldia);

		boolean txertatuta = partidaDAO.sortuPartida(partida);
		if (!txertatuta) {
			throw new Exception("Errorea partidua gordetzean.");
		}

		JOptionPane.showMessageDialog(partiduakPanela, "Partidua ondo gorde da.", "Ondo",
				JOptionPane.INFORMATION_MESSAGE);
		etxekoSetak.setText("");
		kanpokoSetak.setText("");

		if (leihoNagusia != null) {
			leihoNagusia.eguneratuDena();
		}
	}

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
			try {
				hasiDenboraldiaBerria();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(partiduakPanela, "Errorea denboraldia hasteko: " + ex.getMessage(),
						"Errorea", JOptionPane.ERROR_MESSAGE);
				ex.printStackTrace();
			}
		} else if (src == puntuakSartuBotoia) {
			try {
				prozesatuPartidua();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(partiduakPanela, "Errorea partidua sartzerakoan: " + ex.getMessage(),
						"Errorea", JOptionPane.ERROR_MESSAGE);
				ex.printStackTrace();
			}
		} else if (src == saioaAmaituBotoia) {
			try {
				SwingUtilities.invokeLater(() -> new Login().setVisible(true));
				JFrame frame = (JFrame) SwingUtilities.getWindowAncestor((Component) e.getSource());
				frame.dispose();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(partiduakPanela, "Errorea saioa amaitzerakoan: " + ex.getMessage(),
						"Errorea", JOptionPane.ERROR_MESSAGE);
				ex.printStackTrace();
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {
		puntuakSartuBotoia.setBackground(new Color(41, 128, 185));
	}
	@Override
	public void mouseExited(MouseEvent e) {
		puntuakSartuBotoia.setBackground(new Color(52, 152, 219));
	}
}