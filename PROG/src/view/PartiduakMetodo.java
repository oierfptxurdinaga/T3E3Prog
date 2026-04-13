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

	// DAOs
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
		partiduakPanela.setBackground(new Color(0, 0, 160)); // Gure programaren kolorea

		titulua = new JLabel("PARTIDUAK SARTU");
		titulua.setForeground(Color.WHITE);
		titulua.setFont(new Font("Segoe UI", Font.BOLD, 36));
		titulua.setHorizontalAlignment(SwingConstants.CENTER);
		titulua.setBounds(200, 30, 400, 50);
		partiduakPanela.add(titulua);

		// HASI DENBORALDIA BOTOIA
		hasiDenboraldiaBotoia = new JButton("Hasi Denboraldia");
		hasiDenboraldiaBotoia.setFont(new Font("Segoe UI", Font.BOLD, 16));
		hasiDenboraldiaBotoia.setBounds(600, 90, 200, 40);
		hasiDenboraldiaBotoia.setBackground(new Color(46, 204, 113));
		hasiDenboraldiaBotoia.setForeground(Color.WHITE);
		hasiDenboraldiaBotoia.setFocusPainted(false);
		hasiDenboraldiaBotoia.setBorderPainted(false);
		hasiDenboraldiaBotoia.addActionListener(this);

		partiduakPanela.add(hasiDenboraldiaBotoia);

		// DENBORALDIA
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

		// JARDUNALDIA
		jardunaldiaEtiketa = new JLabel("Aukeratu jardunaldia:");
		jardunaldiaEtiketa.setForeground(Color.WHITE);
		jardunaldiaEtiketa.setFont(new Font("Segoe UI", Font.BOLD, 16));
		jardunaldiaEtiketa.setBounds(320, 140, 220, 30);
		partiduakPanela.add(jardunaldiaEtiketa);

		jardunaldiaCombo = new JComboBox<>();
		jardunaldiaCombo.setBounds(320, 180, 200, 35);
		jardunaldiaCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));

		partiduakPanela.add(jardunaldiaCombo);

		// ETXEKO TALDEA
		etxekoEtiketa = new JLabel("Etxeko Taldea:");
		etxekoEtiketa.setForeground(Color.WHITE);
		etxekoEtiketa.setFont(new Font("Segoe UI", Font.BOLD, 16));
		etxekoEtiketa.setBounds(100, 240, 200, 30);
		partiduakPanela.add(etxekoEtiketa);

		etxekoCombo = new JComboBox<>();
		etxekoCombo.setBounds(100, 280, 200, 35);
		etxekoCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		partiduakPanela.add(etxekoCombo);

		// KANPOKO TALDEA
		kanpokoEtiketa = new JLabel("Kanpoko Taldea:");
		kanpokoEtiketa.setForeground(Color.WHITE);
		kanpokoEtiketa.setFont(new Font("Segoe UI", Font.BOLD, 16));
		kanpokoEtiketa.setBounds(320, 240, 200, 30);
		partiduakPanela.add(kanpokoEtiketa);

		kanpokoCombo = new JComboBox<>();
		kanpokoCombo.setBounds(320, 280, 200, 35);
		kanpokoCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		partiduakPanela.add(kanpokoCombo);

		// SETAK
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

		// SARTU PARTIDU BOTOIA
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

		// SAIOA AMAITU BOTOIA
		saioaAmaituBotoia = new JButton("Saioa amaitu");
		saioaAmaituBotoia.setFont(new Font("Segoe UI", Font.BOLD, 16));
		saioaAmaituBotoia.setBackground(new Color(231, 76, 60));
		saioaAmaituBotoia.setForeground(Color.WHITE);
		saioaAmaituBotoia.setFocusPainted(false);
		saioaAmaituBotoia.setBorderPainted(false);
		saioaAmaituBotoia.setBounds(650, 480, 180, 40);
		saioaAmaituBotoia.addActionListener(this);

		partiduakPanela.add(saioaAmaituBotoia);

		denboraldiakKargatu();
		taldeakKargatu();

		// Konprobatu ea 1 denboraldiko jardunaldiak existitzen diren esta kargatu
		if (denboraldiaCombo.getItemCount() > 0) {
			denboraldiaCombo.setSelectedIndex(0);
		}
	}

	private void denboraldiakKargatu() {
		List<Denboraldia> denboraldiak = denboraldiaDAO.denboraldiakAtera();
		for (Denboraldia d : denboraldiak) {
			denboraldiaCombo.addItem(d);
		}
	}

	private void jardunaldiakKargatu(int denboraldiaKod) {
		// Aurreko elementuak ezabatu
		jardunaldiaCombo.removeAllItems();

		// Denboraldi jakin bateko jardunaldiak lortu DAO bidez
		List<Jardunaldia> jardunaldiak = jardunaldiaDAO.lostuJardunaldiDenboraldiBidez(denboraldiaKod);

		// Jardunaldiak dataren arabera ordenatu (lehenengo data zaharrena)
		jardunaldiak.sort((a, b) -> a.getHasieraData().compareTo(b.getHasieraData()));

		// Jardunaldi bakoitzari zenbakia jarri
		int zenbakia = 1;
		for (Jardunaldia j : jardunaldiak) {
			final int num = zenbakia++;

			// ComboBox-ean "Jardunaldia 1", "Jardunaldia 2", ... erakusten du
			Jardunaldia jWrapper = new Jardunaldia(j.getJardunaldiKod(), j.getHasieraData(), j.getAmaieraData()) {
				@Override
				public String toString() {
					return "Jardunaldia " + num; // ComboBox-en erakutsiko den testua
				}
			};

			// ComboBox-ean gehitu
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
		// Erabiltzaileari denboraldiaren izena galdetu
		String izena = JOptionPane.showInputDialog(partiduakPanela,
				"Sartu denboraldiaren izena (adibidez: 2025/2026):");
		if (izena == null || izena.trim().isEmpty()) // Erabiltzaileak ez badu ezer sartu, irten
			return;

		// Talde guztiak lortu (0 kodeko placeholder-a kenduta)
		List<Taldea> taldeak = taldeaDAO.taldeGuztiakLortu();
		taldeak.removeIf(t -> t.getTaldeaKod() == 0);

		// Talde nahikorik dagoen egiaztatu (gutxienez 2 behar dira)
		if (taldeak.size() < 2) {
			JOptionPane.showMessageDialog(partiduakPanela,
					"Ez dago talde nahikorik denboraldia hasteko. Gutxienez 2 talde behar dira.", "Errorea",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		int taldeKopurua = taldeak.size();
		int jardunaldiKopurua = (taldeKopurua - 1) * 2; // Joan eta etorriko partiduak

		// 4Denboraldi berriaren objektua sortu
		Denboraldia d = new Denboraldia();
		d.setIzena(izena);

		// Denboraldiaren hasiera eta amaiera datak kalkulatu izenetik ("2023/2024")
		try {
			String[] parteak = izena.split("/");
			if (parteak.length != 2)
				throw new IllegalArgumentException("Formato okerra. 'AAAA/AAAA' izan behar du.");

			int urteaHasiera = Integer.parseInt(parteak[0].trim());
			int urteaAmaiera = Integer.parseInt(parteak[1].trim());

			Calendar cal = Calendar.getInstance();

			// Hasiera data: irailaren 1eko data
			cal.set(urteaHasiera, Calendar.SEPTEMBER, 1, 0, 0, 0);
			cal.set(Calendar.MILLISECOND, 0);
			d.setHasieraData(cal.getTime());

			// Amaiera data: ekainaren 30eko data
			cal.set(urteaAmaiera, Calendar.JUNE, 30, 23, 59, 59);
			cal.set(Calendar.MILLISECOND, 0);
			d.setAmaieraData(cal.getTime());

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(partiduakPanela,
					"Errorea denboraldiaren datak kalkulatzean: " + ex.getMessage(), "Errorea",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Denboraldia gorde DB-an
		boolean txertatuta = denboraldiaDAO.denboraldiBerria(d);
		if (!txertatuta) {
			throw new Exception("Errorea denboraldia sortzean.");
		}

		int denboraldiaKod = d.getDenboraldiaKod(); // Denboraldiaren kodea gero erabiltzeko

		// Jardunaldiak sortu (asteko oinarrizko logika)
		java.util.Calendar cal = java.util.Calendar.getInstance();
		Date gaur = new Date();
		cal.setTime(gaur);

		for (int i = 1; i <= jardunaldiKopurua; i++) {
			Jardunaldia j = new Jardunaldia();

			// Data kalkulatu: gaurtik aurrera i-1 aste pasatu
			cal.setTime(gaur);
			cal.add(java.util.Calendar.DAY_OF_YEAR, (i - 1) * 7);
			j.setHasieraData(cal.getTime());
			j.setAmaieraData(cal.getTime()); // egun berdina

			// Jardunaldiak DB-an gorde eta denboraldiarekin lotu
			int jardunaldiKod = jardunaldiaDAO.jardunaldiaSortu(j);
			if (jardunaldiKod == -1) {
				throw new Exception("Errorea " + i + ". jardunaldia sortzean.");
			}

			boolean lotuta = jardunaldiaDAO.denboraldiaAsoziatu(denboraldiaKod, jardunaldiKod);
			if (!lotuta) {
				throw new Exception("Errorea " + i + ". jardunaldia denboraldiarekin lotzean.");
			}
		}

		// Mezu bat erakutsi denboraldia ondo sortu dela adierazteko
		JOptionPane.showMessageDialog(partiduakPanela, "Denboraldia ondo sortu da: " + izena + "\n" + jardunaldiKopurua + " jardunaldirekin.");

		// ComboBox berrabiarazi denboraldi berria ikusteko
		denboraldiaCombo.removeAllItems();
		denboraldiakKargatu();

		// Automatikoki denboraldi berria aukeratu ComboBox-ean
		for (int i = 0; i < denboraldiaCombo.getItemCount(); i++) {
			Denboraldia item = denboraldiaCombo.getItemAt(i);
			if (item.getDenboraldiaKod() == denboraldiaKod) {
				denboraldiaCombo.setSelectedIndex(i);
				break;
			}
		}
	}

	private void prozesatuPartidua() throws Exception {
		// Aukeratutako denboraldia, jardunaldia eta taldeen objektuak hartu
		Denboraldia denboraldia = (Denboraldia) denboraldiaCombo.getSelectedItem();
		Jardunaldia jardunaldia = (Jardunaldia) jardunaldiaCombo.getSelectedItem();
		Taldea etxekoTaldea = (Taldea) etxekoCombo.getSelectedItem();
		Taldea kanpokoTaldea = (Taldea) kanpokoCombo.getSelectedItem();

		// Eremu guztiak bete direla egiaztatzeko
		if (denboraldia == null || jardunaldia == null || etxekoTaldea == null || kanpokoTaldea == null) {
			throw new IllegalStateException("Mesedez, bete eremu guztiak.");
		}

		// Egiaztatu ea etxeko eta kanpoko taldea ez direla berdinak
		if (etxekoTaldea.getTaldeaKod() == kanpokoTaldea.getTaldeaKod()) {
			throw new IllegalArgumentException("Etxeko eta kanpoko taldea ezin dira berdinak izan.");
		}

		int etxekoS, kanpokoS;
		try {
			// Setak zenbakizko balioak direla egiaztatu
			etxekoS = Integer.parseInt(etxekoSetak.getText().trim());
			kanpokoS = Integer.parseInt(kanpokoSetak.getText().trim());
		} catch (NumberFormatException ex) {
			throw new NumberFormatException("Mesedez, sartu zenbaki baliodunak setetan.");
		}

		// Setak 0 eta 3 artekoak izan behar dira
		if (etxekoS < 0 || etxekoS > 3 || kanpokoS < 0 || kanpokoS > 3) {
			throw new IllegalArgumentException("Setak 0 eta 3 artean egon behar dira.");
		}

		// Partidu batean gutxienez talde baten 3 set irabazi behar ditu (bestea 0, 1
		// edo 2)
		if (!((etxekoS == 3 && kanpokoS <= 2) || (kanpokoS == 3 && etxekoS <= 2))) {
			throw new IllegalArgumentException("Partidu batek 3 set irabazi behar ditu (bestea 0, 1 edo 2).");
		}

		// Set guztien batura ezin da 5 baino handiagoa izan
		if ((etxekoS + kanpokoS) > 5) {
			throw new IllegalArgumentException("Set guztien batura ezin da 5 baino handiagoa izan.");
		}

		// Partida objektua sortu eta datuak ezarri
		Partida partida = new Partida();
		partida.setData(new Date()); // gaurko data
		partida.setOrdua(new SimpleDateFormat("HH:mm").format(new Date())); // ordua
		partida.setEmaitza(etxekoS + "-" + kanpokoS); // emaitza testu gisa
		partida.setZigorrak(0); // lehenik 0
		partida.setTxartelak(0); // lehenik 0
		partida.setEtxekoTaldea(etxekoTaldea);
		partida.setKanpokoTaldea(kanpokoTaldea);
		partida.setJardunaldia(jardunaldia);

		// Partida gordetzea DB-an
		boolean txertatuta = partidaDAO.sortuPartida(partida);
		if (!txertatuta) {
			throw new Exception("Errorea partidua gordetzean.");
		}

		// Dena ondo joan denean mezua aterako da
		JOptionPane.showMessageDialog(partiduakPanela, "Partidua ondo gorde da.", "Ondo",
				JOptionPane.INFORMATION_MESSAGE);

		// Eremuak hutzik egongo dira
		etxekoSetak.setText("");
		kanpokoSetak.setText("");

		// Leiho nagusia eguneratzen da datuak ikusteko
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
			if (selected != null) {
				jardunaldiakKargatu(selected.getDenboraldiaKod());
			}

		}
		if (src == hasiDenboraldiaBotoia) {
			try {
				hasiDenboraldiaBerria();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(partiduakPanela, "Errorea denboraldia hasteko: " + ex.getMessage(),
						"Errorea", JOptionPane.ERROR_MESSAGE);
				ex.printStackTrace();
			}
		}
		if (src == puntuakSartuBotoia) {
			try {
				prozesatuPartidua();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(partiduakPanela, "Errorea partidua sartzerakoan: " + ex.getMessage(),
						"Errorea", JOptionPane.ERROR_MESSAGE);
				ex.printStackTrace();
			}

		}
		if (src == saioaAmaituBotoia) {
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
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
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