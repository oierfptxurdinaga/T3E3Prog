package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import dao.DenboraldiaDAO;
import dao.JokalariaDAO;
import dao.TaldeaDAO;
import pojos.Denboraldia;
import pojos.Jokalaria;
import pojos.Taldea;
import util.LoggerUtil;
import util.LoggerUtil.DataAccessException;

/**
 * Ikuspegiaren (View) geruzako klasea. Jokalarien fitxaketak kudeatzeko
 * interfaze grafikoa.
 */
public class FitxaketakMetodo implements ActionListener {

	private JPanel panela;
	private JComboBox<Taldea> taldeaCombo;
	private JComboBox<Taldea> helburuTaldeaCombo;
	private JList<Jokalaria> jokalariakZerrenda;
	private DefaultListModel<Jokalaria> zerrendaModeloa;
	private JButton saioaAmaituBotoia;
	private JButton traspasatuBotoia;
	private JLabel abisuaEtiketa;
	private TaldeaDAO taldeaDAO;
	private JokalariaDAO jokalariaDAO;
	private DenboraldiaDAO denboraldiaDAO;
	private Timer eguneratuTimer;

	/**
	 * FitxaketakMetodo klasearen eraikitzailea.
	 * 
	 * @param urdina Atzeko planoaren kolorea.
	 */
	public FitxaketakMetodo(Color urdina) {
		taldeaDAO = new TaldeaDAO();
		jokalariaDAO = new JokalariaDAO();
		denboraldiaDAO = new DenboraldiaDAO();

		// Panela konfiguratu
		panela = new JPanel(null);
		panela.setBackground(urdina);

		JLabel titulua = new JLabel("FITXAKETAK");
		titulua.setForeground(Color.WHITE);
		titulua.setFont(new Font("Arial", Font.BOLD, 32));
		titulua.setBounds(350, 40, 300, 40);
		panela.add(titulua);

		// Abisu etiketa
		abisuaEtiketa = new JLabel("", SwingConstants.CENTER);
		abisuaEtiketa.setFont(new Font("Arial", Font.BOLD, 16));
		abisuaEtiketa.setBounds(50, 100, 800, 30);
		panela.add(abisuaEtiketa);

		// Jatorrizko taldea aukeratzeko
		JLabel taldeaEtiketa = new JLabel("Aukeratu taldea:");
		taldeaEtiketa.setForeground(Color.WHITE);
		taldeaEtiketa.setFont(new Font("Arial", Font.BOLD, 18));
		taldeaEtiketa.setBounds(100, 150, 200, 30);
		panela.add(taldeaEtiketa);

		taldeaCombo = new JComboBox<>();
		try {
			List<Taldea> taldeak = taldeaDAO.taldeGuztiakLortu();
			for (Taldea t : taldeak) {
				if (t.getTaldeaKod() != 0) {
					taldeaCombo.addItem(t);
				}
			}
		} catch (DataAccessException e) {
			LoggerUtil.log("ERROR taldeak kargatzean: " + e.getMessage());
			JOptionPane.showMessageDialog(panela, "Errorea taldeak kargatzerakoan: " + e.getMessage(), "Errorea",
					JOptionPane.ERROR_MESSAGE);
		}
		taldeaCombo.setBounds(100, 190, 250, 35);
		panela.add(taldeaCombo);

		// Jokalarien zerrenda
		JLabel jokalariakEtiketa = new JLabel("Jokalariak:");
		jokalariakEtiketa.setForeground(Color.WHITE);
		jokalariakEtiketa.setFont(new Font("Arial", Font.BOLD, 18));
		jokalariakEtiketa.setBounds(100, 240, 200, 30);
		panela.add(jokalariakEtiketa);

		zerrendaModeloa = new DefaultListModel<>();
		jokalariakZerrenda = new JList<>(zerrendaModeloa);
		JScrollPane korritzePanelaJokalariak = new JScrollPane(jokalariakZerrenda);
		korritzePanelaJokalariak.setBounds(100, 280, 250, 200);
		panela.add(korritzePanelaJokalariak);

		// Helburu taldea aukeratzeko
		JLabel helburuTaldeaEtiketa = new JLabel("Traspasatu nahi den taldea:");
		helburuTaldeaEtiketa.setForeground(Color.WHITE);
		helburuTaldeaEtiketa.setFont(new Font("Arial", Font.BOLD, 18));
		helburuTaldeaEtiketa.setBounds(450, 150, 300, 30);
		panela.add(helburuTaldeaEtiketa);

		helburuTaldeaCombo = new JComboBox<>();
		try {
			List<Taldea> taldeak = taldeaDAO.taldeGuztiakLortu();
			for (Taldea t : taldeak) {
				if (t.getTaldeaKod() != 0) {
					helburuTaldeaCombo.addItem(t);
				}
			}
		} catch (DataAccessException e) {
			LoggerUtil.log("ERROR helburu taldeak kargatzean: " + e.getMessage());
		}
		helburuTaldeaCombo.setBounds(450, 190, 250, 35);
		panela.add(helburuTaldeaCombo);

		// Traspasatu botoia
		traspasatuBotoia = new JButton("Traspasatu");
		traspasatuBotoia.setFont(new Font("Arial", Font.BOLD, 16));
		traspasatuBotoia.setBackground(new Color(46, 204, 113));
		traspasatuBotoia.setForeground(Color.WHITE);
		traspasatuBotoia.setFocusPainted(false);
		traspasatuBotoia.setBorderPainted(false);
		traspasatuBotoia.setBounds(475, 280, 200, 40);
		traspasatuBotoia.addActionListener(this);
		panela.add(traspasatuBotoia);

		// Saioa amaitu botoia
		saioaAmaituBotoia = new JButton("Saioa amaitu");
		saioaAmaituBotoia.setFont(new Font("Arial", Font.BOLD, 18));
		saioaAmaituBotoia.setBackground(Color.RED);
		saioaAmaituBotoia.setForeground(Color.WHITE);
		saioaAmaituBotoia.setBounds(700, 480, 170, 40);
		saioaAmaituBotoia.addActionListener(this);
		panela.add(saioaAmaituBotoia);

		// Taldea aldatzean jokalariak kargatu
		taldeaCombo.addActionListener(e -> {
			try {
				kargatuJokalariak();
			} catch (Exception ex) {
				LoggerUtil.log("ERROR jokalariak kargatzean: " + ex.getMessage());
				JOptionPane.showMessageDialog(panela, "Errorea jokalariak kargatzerakoan: " + ex.getMessage());
			}
		});

		// Hasierako karga
		try {
			kargatuJokalariak();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(panela, "Errorea hasieran kargatzean: " + e.getMessage());
		}

		eguneratuInterfazeaDenboraldia();

		// Timer-a egoera eguneratzeko (500ms)
		eguneratuTimer = new Timer(500, e -> eguneratuInterfazeaDenboraldia());
		eguneratuTimer.start();
	}

	/**
	 * Denboraldiaren arabera interfazea eguneratzen du.
	 */
	private void eguneratuInterfazeaDenboraldia() {
		try {
			Denboraldia aktiboa = denboraldiaDAO.denboraldiaAktiboaLortu();
			boolean fitxaketakBaimenduta = (aktiboa == null);

			traspasatuBotoia.setEnabled(true);
			taldeaCombo.setEnabled(true);
			helburuTaldeaCombo.setEnabled(true);
			jokalariakZerrenda.setEnabled(true);

			if (fitxaketakBaimenduta) {
				abisuaEtiketa.setText("Fitxaketak IREKITA daude. Traspasoak egin daitezke.");
				abisuaEtiketa.setForeground(new Color(0, 180, 0));
			} else {
				abisuaEtiketa.setText("Fitxaketak ITXITA daude. Denboraldia aktibo dago: " + aktiboa.getIzena());
				abisuaEtiketa.setForeground(Color.RED);
			}
		} catch (DataAccessException e) {
			LoggerUtil.log("ERROR denboraldi egoera eguneratzean: " + e.getMessage());
		}
	}

	/**
	 * Kanpotik egoera eguneratzeko.
	 */
	public void eguneratuEgoera() {
		eguneratuInterfazeaDenboraldia();
	}

	/**
	 * Hautatutako taldeko jokalariak kargatzen ditu.
	 */
	private void kargatuJokalariak() throws DataAccessException {
		zerrendaModeloa.clear();
		Taldea taldea = (Taldea) taldeaCombo.getSelectedItem();
		if (taldea == null)
			return;

		List<Jokalaria> jokalariak = jokalariaDAO.jokalariaLortuTaldeBidez(taldea.getTaldeaKod());
		for (Jokalaria j : jokalariak) {
			zerrendaModeloa.addElement(j);
		}
	}

	/**
	 * Taldeen zerrenda eguneratzen du (talde berria sortzean).
	 */
	public void eguneratuTaldeak() {
		Taldea selectedTaldea = (Taldea) taldeaCombo.getSelectedItem();
		Taldea selectedHelburu = (Taldea) helburuTaldeaCombo.getSelectedItem();

		taldeaCombo.removeAllItems();
		helburuTaldeaCombo.removeAllItems();

		try {
			List<Taldea> taldeak = taldeaDAO.taldeGuztiakLortu();
			for (Taldea t : taldeak) {
				if (t.getTaldeaKod() != 0) {
					taldeaCombo.addItem(t);
					helburuTaldeaCombo.addItem(t);
				}
			}
		} catch (DataAccessException e) {
			LoggerUtil.log("ERROR taldeak eguneratzean: " + e.getMessage());
		}

		if (selectedTaldea != null) {
			taldeaCombo.setSelectedItem(selectedTaldea);
		}
		if (selectedHelburu != null) {
			helburuTaldeaCombo.setSelectedItem(selectedHelburu);
		}
	}

	/**
	 * Traspasoa gauzatzen du.
	 */
	private void traspasatuJokalariaGUI() {
		try {
			// Denboraldi aktiboa badago, ezin da fitxaketarik egin
			Denboraldia aktiboa = denboraldiaDAO.denboraldiaAktiboaLortu();
			if (aktiboa != null) {
				JOptionPane.showMessageDialog(panela,
						"Ezin da fitxaketarik egin denboraldia aktibo dagoen bitartean.\n"
								+ "Itxaron denboraldia amaitu arte edo amaitu denboraldia lehenengo.",
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

			int erantzuna = JOptionPane.showConfirmDialog(
					panela, "Ziur zaude " + jokalaria.getIzena() + " " + jokalaria.getAbizena() + " "
							+ helburua.getIzena() + " taldera traspasatu nahi duzula?",
					"Baieztatu", JOptionPane.YES_NO_OPTION);

			if (erantzuna == JOptionPane.YES_OPTION) {
				jokalariaDAO.traspasatu(jokalaria, helburua);
				kargatuJokalariak();
				JOptionPane.showMessageDialog(panela, "Traspasoa ondo egin da!");
			}
		} catch (DataAccessException e) {
			LoggerUtil.log("ERROR traspasoa egitean: " + e.getMessage());
			JOptionPane.showMessageDialog(panela, "Errorea datu-basean: " + e.getMessage(), "Errorea",
					JOptionPane.ERROR_MESSAGE);
		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(panela, "Errorea: " + e.getMessage(), "Errorea", JOptionPane.WARNING_MESSAGE);
		}
	}

	/**
	 * @return Panela.
	 */
	public JPanel getPanela() {
		return panela;
	}

	/**
	 * Timer-a gelditzen du.
	 */
	public void stopTimer() {
		if (eguneratuTimer != null) {
			eguneratuTimer.stop();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();

		if (src == saioaAmaituBotoia) {
			stopTimer();
			SwingUtilities.invokeLater(() -> new Login().setVisible(true));
			JFrame frame = (JFrame) SwingUtilities.getWindowAncestor((Component) e.getSource());
			if (frame != null) {
				frame.dispose();
			}
		}

		if (src == traspasatuBotoia) {
			traspasatuJokalariaGUI();
		}
	}
}