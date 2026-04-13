package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import model.*;
import dao.DenboraldiaDAO;
import dao.JokalariaDAO;
import dao.TaldeaDAO;
import pojos.Denboraldia;
import pojos.Jokalaria;
import pojos.Taldea;

/**
 * Ikuspegiaren (View) geruzako klasea.
 * Jokalarien fitxaketak (traspasoak) kudeatzeko interfaze grafikoa ordezkatzen du.
 * Erabiltzaileari jatorrizko talde bat aukeratu, bertako jokalariak ikusi 
 * eta jokalari bat beste helburu-talde batera transferitzeko aukera ematen dio.
 */
public class FitxaketakMetodo implements ActionListener {

	private JPanel panela;
	private Color urdina;

	private JComboBox<Taldea> taldeaCombo;
	private JComboBox<Taldea> helburuTaldeaCombo;

	private JList<Jokalaria> jokalariakZerrenda;
	private DefaultListModel<Jokalaria> zerrendaModeloa;

	private JButton saioaAmaituBotoia;
	private JButton traspasatuBotoia;

	private JLabel titulua;
	private JLabel taldeaEtiketa;
	private JLabel jokalariakEtiketa;
	private JLabel helburuTaldeaEtiketa;
	private JLabel abisuaEtiketa;

	private JScrollPane korritzePanelaJokalariak;

	private TaldeaDAO taldeaDAO;
	private JokalariaDAO jokalariaDAO;
	private DenboraldiaDAO denboraldiaDAO;

	/**
	 * FitxaketakMetodo klasearen eraikitzailea.
	 * Interfaze grafikoa eraikitzen du, taldeen datuak datu-basetik kargatzen ditu 
	 * eta osagaien ekintzak (botoiak, zerrendak) prestatzen ditu.
	 * @param urdina Aplikazioaren diseinu-patroiari jarraitzen dion atzeko planoaren kolorea.
	 */
	public FitxaketakMetodo(Color urdina) {
		this.urdina = urdina;

		taldeaDAO = new TaldeaDAO();
		jokalariaDAO = new JokalariaDAO();
		denboraldiaDAO = new DenboraldiaDAO();

		panela = new JPanel(null);
		panela.setBackground(urdina);

		titulua = new JLabel("FITXAKETAK");
		titulua.setForeground(Color.WHITE);
		titulua.setFont(new Font("Arial", Font.BOLD, 32));
		titulua.setBounds(350, 40, 300, 40);
		panela.add(titulua);

		abisuaEtiketa = new JLabel("", SwingConstants.CENTER);
		abisuaEtiketa.setFont(new Font("Arial", Font.BOLD, 16));
		abisuaEtiketa.setBounds(50, 100, 800, 30);
		panela.add(abisuaEtiketa);

		taldeaEtiketa = new JLabel("Aukeratu taldea:");
		taldeaEtiketa.setForeground(Color.WHITE);
		taldeaEtiketa.setFont(new Font("Arial", Font.BOLD, 18));
		taldeaEtiketa.setBounds(100, 150, 200, 30);
		panela.add(taldeaEtiketa);

		taldeaCombo = new JComboBox<>();
		List<Taldea> taldeak = taldeaDAO.taldeGuztiakLortu();
		for (Taldea t : taldeak) {
			if (t.getTaldeaKod() != 0) {
				taldeaCombo.addItem(t);
			}
		}
		taldeaCombo.setBounds(100, 190, 250, 35);
		panela.add(taldeaCombo);

		jokalariakEtiketa = new JLabel("Jokalariak:");
		jokalariakEtiketa.setForeground(Color.WHITE);
		jokalariakEtiketa.setFont(new Font("Arial", Font.BOLD, 18));
		jokalariakEtiketa.setBounds(100, 240, 200, 30);
		panela.add(jokalariakEtiketa);

		zerrendaModeloa = new DefaultListModel<>();
		jokalariakZerrenda = new JList<>(zerrendaModeloa);
		korritzePanelaJokalariak = new JScrollPane(jokalariakZerrenda);
		korritzePanelaJokalariak.setBounds(100, 280, 250, 200);
		panela.add(korritzePanelaJokalariak);

		helburuTaldeaEtiketa = new JLabel("Traspasatu nahi den taldea:");
		helburuTaldeaEtiketa.setForeground(Color.WHITE);
		helburuTaldeaEtiketa.setFont(new Font("Arial", Font.BOLD, 18));
		helburuTaldeaEtiketa.setBounds(450, 150, 300, 30);
		panela.add(helburuTaldeaEtiketa);

		helburuTaldeaCombo = new JComboBox<>();
		for (Taldea t : taldeak) {
			if (t.getTaldeaKod() != 0) {
				helburuTaldeaCombo.addItem(t);
			}
		}
		helburuTaldeaCombo.setBounds(450, 190, 250, 35);
		panela.add(helburuTaldeaCombo);

		traspasatuBotoia = new JButton("Traspasatu");
		traspasatuBotoia.setFont(new Font("Arial", Font.BOLD, 16));
		traspasatuBotoia.setBackground(new Color(46, 204, 113));
		traspasatuBotoia.setForeground(Color.WHITE);
		traspasatuBotoia.setFocusPainted(false);
		traspasatuBotoia.setBorderPainted(false);
		traspasatuBotoia.setBounds(475, 280, 200, 40);
		traspasatuBotoia.addActionListener(this);
		panela.add(traspasatuBotoia);

		saioaAmaituBotoia = new JButton("Saioa amaitu");
		saioaAmaituBotoia.setFont(new Font("Arial", Font.BOLD, 18));
		saioaAmaituBotoia.setBackground(Color.RED);
		saioaAmaituBotoia.setForeground(Color.WHITE);
		saioaAmaituBotoia.setBounds(700, 480, 170, 40);
		saioaAmaituBotoia.addActionListener(this);
		panela.add(saioaAmaituBotoia);

		taldeaCombo.addActionListener(e -> {
			try {
				kargatuJokalariak();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(panela, "Errorea jokalariak kargatzerakoan: " + ex.getMessage());
			}
		});

		try {
			kargatuJokalariak();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(panela, "Errorea hasieran kargatzean: " + e.getMessage());
		}

		eguneratuInterfazeaDenboraldia();
	}

	/**
	 * Interfazearen egoera eguneratzen du, abisu mezua kolorez erakutsiz.
	 * - Berdea: Fitxaketak BAIMENDUTA (ez dago denboraldi aktiborik).
	 * - Gorria: Fitxaketak BLOKEATUTA (denboraldia aktibo dago).
	 */
	private void eguneratuInterfazeaDenboraldia() {
		Denboraldia aktiboa = denboraldiaDAO.denboraldiaAktiboaLortu();
		boolean fitxaketakBaimenduta = (aktiboa == null);

		// Osagaiak beti gaituta mantendu
		traspasatuBotoia.setEnabled(true);
		taldeaCombo.setEnabled(true);
		helburuTaldeaCombo.setEnabled(true);
		jokalariakZerrenda.setEnabled(true);

		if (fitxaketakBaimenduta) {
			abisuaEtiketa.setText("Fitxaketak IREKITA daude. Traspasoak egin daitezke.");
			abisuaEtiketa.setForeground(new Color(0, 180, 0)); // Berdea
		} else {
			abisuaEtiketa.setText("Fitxaketak ITXITA daude. Denboraldia aktibo dago: " + aktiboa.getIzena());
			abisuaEtiketa.setForeground(Color.RED); // Gorria
		}
	}

	public void eguneratuEgoera() {
		eguneratuInterfazeaDenboraldia();
	}

	/**
	 * Hautatutako taldeko jokalariak kargatu
	 */
	private void kargatuJokalariak() throws Exception {
		zerrendaModeloa.clear();
		Taldea taldea = (Taldea) taldeaCombo.getSelectedItem();
		if (taldea == null) {
			return;
		}
		List<Jokalaria> jokalariak = jokalariaDAO.jokalariaLortuTaldeBidez(taldea.getTaldeaKod());
		for (Jokalaria j : jokalariak) {
			zerrendaModeloa.addElement(j);
		}
	}

	/**
	 * Jokalaria traspasatzeko logika
	 */
	private void traspasatuJokalariaGUI() throws Exception {
		// BALIDAZIOA: Denboraldi aktiboa badago, ezin da fitxatu
		Denboraldia aktiboa = denboraldiaDAO.denboraldiaAktiboaLortu();
		if (aktiboa != null) {
			JOptionPane.showMessageDialog(panela,
					"Ezin da fitxaketarik egin denboraldia aktibo dagoen bitartean.\n" +
					"Itxaron denboraldia amaitu arte edo amaitu denboraldia lehenengo.",
					"Fitxaketak blokeatuta", JOptionPane.WARNING_MESSAGE);
			return;
		}

		Jokalaria jokalaria = jokalariakZerrenda.getSelectedValue();
		if (jokalaria == null) {
			throw new IllegalArgumentException("Aukeratu jokalari bat.");
		}

		Taldea helburua = (Taldea) helburuTaldeaCombo.getSelectedItem();
		if (helburua == null) {
			throw new IllegalArgumentException("Aukeratu helburu taldea.");
		}

		int erantzuna = JOptionPane.showConfirmDialog(panela,
				"Ziur zaude " + jokalaria.getIzena() + " " + jokalaria.getAbizena() + 
				" " + helburua.getIzena() + " taldera traspasatu nahi duzula?",
				"Baieztatu", JOptionPane.YES_NO_OPTION);

		if (erantzuna == JOptionPane.YES_OPTION) {
			boolean ondo = jokalariaDAO.traspasatu(jokalaria, helburua);
			if (!ondo) {
				throw new Exception("Errorea traspasoan");
			}
			kargatuJokalariak();
			JOptionPane.showMessageDialog(panela, "Traspasoa ondo egin da!");
		}
	}

	public JPanel getPanela() {
		return panela;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();

		if (src == saioaAmaituBotoia) {
			SwingUtilities.invokeLater(() -> new Login().setVisible(true));
			JFrame frame = (JFrame) SwingUtilities.getWindowAncestor((Component) e.getSource());
			frame.dispose();
		}

		if (src == traspasatuBotoia) {
			try {
				traspasatuJokalariaGUI();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(panela, "Errorea: " + ex.getMessage());
			}
		}
	}
}