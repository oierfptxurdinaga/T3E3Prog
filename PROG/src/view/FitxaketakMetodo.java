package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import model.*;
import dao.JokalariaDAO;
import dao.TaldeaDAO;
import pojos.Jokalaria;
import pojos.Taldea;

// Klase honek fitxaketak (traspasoak) kudeatzen ditu GUI bidez
public class FitxaketakMetodo implements ActionListener {

	private JPanel panela; // Pantaila nagusia
	private Color urdina; // Atzeko kolorea

	// Talde aukeratzeko desplegablea 
	private JComboBox<Taldea> taldeaCombo;
	private JComboBox<Taldea> helburuTaldeaCombo;

	// Jokalari zerrenda
	private JList<Jokalaria> jokalariakZerrenda;
	private DefaultListModel<Jokalaria> zerrendaModeloa;

	// Botoiak
	private JButton saioaAmaituBotoia;
	private JButton traspasatuBotoia;

	// Etiketak
	private JLabel titulua;
	private JLabel taldeaEtiketa;
	private JLabel jokalariakEtiketa;
	private JLabel helburuTaldeaEtiketa;
	private JLabel abisuaEtiketa;

	private JScrollPane korritzePanelaJokalariak;

	// DAO-ak (datu-basearekin komunikatzeko)
	private TaldeaDAO taldeaDAO;
	private JokalariaDAO jokalariaDAO;

	public FitxaketakMetodo(Color urdina) {
		this.urdina = urdina;

		// DAO instantziak sortu
		taldeaDAO = new TaldeaDAO();
		jokalariaDAO = new JokalariaDAO();

		// Panel nagusia sortu
		panela = new JPanel(null);
		panela.setBackground(urdina);

		// Titulua
		titulua = new JLabel("FITXAKETAK");
		titulua.setForeground(Color.WHITE);
		titulua.setFont(new Font("Arial", Font.BOLD, 32));
		titulua.setBounds(350, 40, 300, 40);
		panela.add(titulua);

		// Abisu mezua
		abisuaEtiketa = new JLabel("");
		abisuaEtiketa.setForeground(Color.YELLOW);
		abisuaEtiketa.setFont(new Font("Arial", Font.BOLD, 16));
		abisuaEtiketa.setBounds(100, 100, 700, 30);
		abisuaEtiketa.setHorizontalAlignment(SwingConstants.CENTER);
		panela.add(abisuaEtiketa);

		// Taldea aukeratzeko etiketa
		taldeaEtiketa = new JLabel("Aukeratu taldea:");
		taldeaEtiketa.setForeground(Color.WHITE);
		taldeaEtiketa.setFont(new Font("Arial", Font.BOLD, 18));
		taldeaEtiketa.setBounds(100, 150, 200, 30);
		panela.add(taldeaEtiketa);

		// Taldeen zerrenda (BDtik kargatuta)
		taldeaCombo = new JComboBox<>();
		List<Taldea> taldeak = taldeaDAO.taldeGuztiakLortu();

		for (Taldea t : taldeak) {
			if (t.getTaldeaKod() != 0) { // 0 balioa baztertu
				taldeaCombo.addItem(t);
			}
		}

		taldeaCombo.setBounds(100, 190, 250, 35);
		panela.add(taldeaCombo);

		// Jokalariak etiketa
		jokalariakEtiketa = new JLabel("Jokalariak:");
		jokalariakEtiketa.setForeground(Color.WHITE);
		jokalariakEtiketa.setFont(new Font("Arial", Font.BOLD, 18));
		jokalariakEtiketa.setBounds(100, 240, 200, 30);
		panela.add(jokalariakEtiketa);

		// Jokalari zerrenda
		zerrendaModeloa = new DefaultListModel<>();
		jokalariakZerrenda = new JList<>(zerrendaModeloa);
		korritzePanelaJokalariak = new JScrollPane(jokalariakZerrenda);
		korritzePanelaJokalariak.setBounds(100, 280, 250, 200);
		panela.add(korritzePanelaJokalariak);

		// Helburu taldea etiketa
		helburuTaldeaEtiketa = new JLabel("Traspasatu nahi den taldea:");
		helburuTaldeaEtiketa.setForeground(Color.WHITE);
		helburuTaldeaEtiketa.setFont(new Font("Arial", Font.BOLD, 18));
		helburuTaldeaEtiketa.setBounds(450, 150, 300, 30);
		panela.add(helburuTaldeaEtiketa);

		// Helburu taldeen combo
		helburuTaldeaCombo = new JComboBox<>();
		for (Taldea t : taldeak) {
			if (t.getTaldeaKod() != 0) {
				helburuTaldeaCombo.addItem(t);
			}
		}

		helburuTaldeaCombo.setBounds(450, 190, 250, 35);
		panela.add(helburuTaldeaCombo);

		// Traspasatzeko botoia
		traspasatuBotoia = new JButton("Traspasatu");
		traspasatuBotoia.setBounds(475, 280, 200, 40);
		traspasatuBotoia.addActionListener(this);
		panela.add(traspasatuBotoia);

		// Saioa amaitzeko botoia
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
	}

	// Interfazearen egoera eguneratu (denboraldiaren arabera)
	private void eguneratuInterfazeaDenboraldia() {
		traspasatuBotoia.setEnabled(true);
		taldeaCombo.setEnabled(true);
		helburuTaldeaCombo.setEnabled(true);

		abisuaEtiketa.setText("Fitxaketak egin daitezke.");
	}

	// Hautatutako taldeko jokalariak kargatu
	private void kargatuJokalariak() throws Exception {
		zerrendaModeloa.clear();

		Taldea taldea = (Taldea) taldeaCombo.getSelectedItem();

		if (taldea == null) {
			throw new IllegalStateException("Ez da talderik aukeratu.");
		}

		List<Jokalaria> jokalariak = jokalariaDAO.jokalariaLortuTaldeBidez(taldea.getTaldeaKod());

		for (Jokalaria j : jokalariak) {
			zerrendaModeloa.addElement(j);
		}
	}

	// Jokalaria traspasatzeko logika
	private void traspasatuJokalariaGUI() throws Exception {

		Jokalaria jokalaria = jokalariakZerrenda.getSelectedValue();

		if (jokalaria == null) {
			throw new IllegalArgumentException("Aukeratu jokalari bat.");
		}

		Taldea helburua = (Taldea) helburuTaldeaCombo.getSelectedItem();

		if (helburua == null) {
			throw new IllegalArgumentException("Aukeratu helburu taldea.");
		}

		// Baieztapena
		int erantzuna = JOptionPane.showConfirmDialog(panela, "Ziur zaude traspasoa egin nahi duzula?", "Baieztatu",
				JOptionPane.YES_NO_OPTION);

		if (erantzuna == JOptionPane.YES_OPTION) {

			// DAO-ra deitu (hemen egiten da BD-ko UPDATE-a)
			boolean ondo = jokalariaDAO.traspasatu(jokalaria, helburua);

			if (!ondo) {
				throw new Exception("Errorea traspasoan");
			}

			// Zerrenda berriro kargatu
			kargatuJokalariak();

			JOptionPane.showMessageDialog(null, "Traspasoa eginda!");
		}
	}

	public JPanel getPanela() {
		return panela;
	}

	// Botoien ekintzak
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
