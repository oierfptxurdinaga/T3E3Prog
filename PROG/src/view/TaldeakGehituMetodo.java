package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import dao.JokalariaDAO;
import dao.TaldeaDAO;
import dao.ZelaiaDAO;
import pojos.Jokalaria;
import pojos.Taldea;
import pojos.Zelaia;
import util.LoggerUtil;
import util.LoggerUtil.DataAccessException;

/**
 * Ikuspegiaren (View) geruzako klasea. Talde berri bat datu-basean
 * erregistratzeko interfaze grafikoa.
 */
public class TaldeakGehituMetodo implements ActionListener {

	private JPanel panela;
	private JTextField izenaField;
	private JTextField sortzeDataField;
	private JComboBox<Zelaia> zelaiaCombo;
	private JButton eskudoaBotoia;
	private JLabel eskudoaLabel;
	private String aukeratutakoEskudoaPath = null;
	private TaldeaDAO taldeaDAO;
	private ZelaiaDAO zelaiaDAO;
	private JokalariaDAO jokalariaDAO;

	/**
	 * TaldeakGehituMetodo klasearen eraikitzailea.
	 * 
	 * @param kolorea Atzeko planoaren kolorea.
	 */
	public TaldeakGehituMetodo(Color kolorea) {
		taldeaDAO = new TaldeaDAO();
		zelaiaDAO = new ZelaiaDAO();
		jokalariaDAO = new JokalariaDAO();

		// Panela konfiguratu
		panela = new JPanel(null);
		panela.setBackground(kolorea);

		JLabel titulua = new JLabel("TALDE BERRIA GEHITU");
		titulua.setForeground(Color.WHITE);
		titulua.setFont(new Font("Arial", Font.BOLD, 32));
		titulua.setBounds(250, 40, 500, 40);
		panela.add(titulua);

		int y = 120;
		int labelX = 200;
		int fieldX = 370;
		int fieldWidth = 250;
		int rowHeight = 45;

		// Izena
		JLabel izenaLabel = new JLabel("Taldearen izena:");
		izenaLabel.setForeground(Color.WHITE);
		izenaLabel.setFont(new Font("Arial", Font.BOLD, 18));
		izenaLabel.setBounds(labelX, y, 150, 30);
		panela.add(izenaLabel);
		izenaField = new JTextField();
		izenaField.setBounds(fieldX, y, fieldWidth, 35);
		panela.add(izenaField);
		y += rowHeight;

		// Sortze data
		JLabel sortzeDataLabel = new JLabel("Sortze data (yyyy-MM-dd):");
		sortzeDataLabel.setForeground(Color.WHITE);
		sortzeDataLabel.setFont(new Font("Arial", Font.BOLD, 16));
		sortzeDataLabel.setBounds(labelX, y, 200, 30);
		panela.add(sortzeDataLabel);
		sortzeDataField = new JTextField();
		sortzeDataField.setBounds(fieldX, y, fieldWidth, 35);
		sortzeDataField.setToolTipText("Adibidez: 2020-01-15");
		panela.add(sortzeDataField);
		y += rowHeight;

		// Zelaia
		JLabel zelaiaLabel = new JLabel("Zelaia:");
		zelaiaLabel.setForeground(Color.WHITE);
		zelaiaLabel.setFont(new Font("Arial", Font.BOLD, 18));
		zelaiaLabel.setBounds(labelX, y, 150, 30);
		panela.add(zelaiaLabel);
		zelaiaCombo = new JComboBox<>();
		zelaiaCombo.addItem(null);
		try {
			List<Zelaia> zelaiak = zelaiaDAO.zelaiGutziakLortu();
			for (Zelaia z : zelaiak) {
				zelaiaCombo.addItem(z);
			}
		} catch (DataAccessException e) {
			LoggerUtil.log("ERROR zelaiak kargatzean: " + e.getMessage());
		}
		zelaiaCombo.setBounds(fieldX, y, fieldWidth, 35);
		panela.add(zelaiaCombo);
		y += rowHeight;

		// Eskudoa aukeratzeko botoia
		eskudoaBotoia = new JButton("Aukeratu eskudoa");
		eskudoaBotoia.setFont(new Font("Arial", Font.BOLD, 14));
		eskudoaBotoia.setBounds(fieldX, y, 200, 35);
		eskudoaBotoia.addActionListener(this);
		panela.add(eskudoaBotoia);

		eskudoaLabel = new JLabel("Ez da eskudoirik hautatu");
		eskudoaLabel.setForeground(Color.LIGHT_GRAY);
		eskudoaLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		eskudoaLabel.setBounds(fieldX, y + 40, 250, 50);
		eskudoaLabel.setHorizontalAlignment(SwingConstants.LEFT);
		eskudoaLabel.setVerticalAlignment(SwingConstants.CENTER);
		panela.add(eskudoaLabel);
		y += 70;

		// Gorde botoia
		JButton gordeBotoia = new JButton("Gorde");
		gordeBotoia.setFont(new Font("Arial", Font.BOLD, 16));
		gordeBotoia.setBackground(new Color(0, 150, 0));
		gordeBotoia.setForeground(Color.WHITE);
		gordeBotoia.setBounds(270, y, 150, 40);
		gordeBotoia.addActionListener(e -> gordeTaldea());
		panela.add(gordeBotoia);

		// Ezeztatu botoia
		JButton ezeztatuBotoia = new JButton("Ezeztatu");
		ezeztatuBotoia.setFont(new Font("Arial", Font.BOLD, 16));
		ezeztatuBotoia.setBackground(Color.RED);
		ezeztatuBotoia.setForeground(Color.WHITE);
		ezeztatuBotoia.setBounds(470, y, 150, 40);
		ezeztatuBotoia.addActionListener(e -> garbituFormularioa());
		panela.add(ezeztatuBotoia);
	}

	/**
	 * Formularioa garbitzen du.
	 */
	private void garbituFormularioa() {
		izenaField.setText("");
		sortzeDataField.setText("");
		zelaiaCombo.setSelectedIndex(0);
		aukeratutakoEskudoaPath = null;
		eskudoaLabel.setIcon(null);
		eskudoaLabel.setText("Ez da eskudoirik hautatu");
		eskudoaLabel.setForeground(Color.LIGHT_GRAY);
	}

	/**
	 * Taldea datu-basean gordetzen du.
	 */
	private void gordeTaldea() {
		try {
			if (izenaField.getText().trim().isEmpty()) {
				throw new IllegalArgumentException("Taldearen izena ezin da hutsik egon.");
			}

			Taldea t = new Taldea();
			t.setIzena(izenaField.getText().trim());

			// Sortze data balidatu
			if (!sortzeDataField.getText().trim().isEmpty()) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				sdf.setLenient(false);
				try {
					t.setSortzeData(sdf.parse(sortzeDataField.getText().trim()));
				} catch (ParseException ex) {
					throw new IllegalArgumentException("Data formatua ez da zuzena. Erabili yyyy-MM-dd.");
				}
			}

			t.setZelaia((Zelaia) zelaiaCombo.getSelectedItem());

			// Taldea gorde
			taldeaDAO.taldeaSortu(t);

			// Eskudoa gorde
			if (aukeratutakoEskudoaPath != null && !aukeratutakoEskudoaPath.isEmpty()) {
				try {
					String karpetaPath = "images/LogosEquipos";
					java.io.File karpeta = new java.io.File(karpetaPath);
					if (!karpeta.exists())
						karpeta.mkdirs();

					java.io.File jatorrizkoa = new java.io.File(aukeratutakoEskudoaPath);
					String izenBerria = t.getIzena().replace(" ", "") + "_" + System.currentTimeMillis()
							+ aukeratutakoEskudoaPath.substring(aukeratutakoEskudoaPath.lastIndexOf('.'));
					java.io.File destino = new java.io.File(karpeta, izenBerria);
					java.nio.file.Files.copy(jatorrizkoa.toPath(), destino.toPath(),
							java.nio.file.StandardCopyOption.REPLACE_EXISTING);
				} catch (Exception ex) {
					LoggerUtil.log("ERROR eskudoa gordetzean: " + ex.getMessage());
					JOptionPane.showMessageDialog(panela, "Errorea eskudoa gordetzean: " + ex.getMessage(), "Errorea",
							JOptionPane.ERROR_MESSAGE);
				}
			}

			JOptionPane.showMessageDialog(panela, "Taldea ondo gorde da.", "Ondo", JOptionPane.INFORMATION_MESSAGE);

			// Panel nagusia eguneratu
			Main leihoNagusia = (Main) SwingUtilities.getWindowAncestor(panela);
			if (leihoNagusia != null) {
				leihoNagusia.eguneratuDena();
				if (leihoNagusia.taldeakPanela != null) {
					leihoNagusia.taldeakPanela.eguneratuPanela();
				}
				if (leihoNagusia.partiduakPanela != null) {
					leihoNagusia.partiduakPanela.eguneratuTaldeak();
				}
				if (leihoNagusia.fitxaketakPanela != null) {
					leihoNagusia.fitxaketakPanela.eguneratuTaldeak();
				}
			}

			// Galdetu 10 jokalari generiko sortu nahi diren
			int resp = JOptionPane.showConfirmDialog(panela, "10 jokalari generiko sortu nahi dituzu talde honentzat?",
					"Jokalariak sortu", JOptionPane.YES_NO_OPTION);
			if (resp == JOptionPane.YES_OPTION) {
				sortuJokalariGenerikoak(t);
			}

			garbituFormularioa();

		} catch (IllegalArgumentException ex) {
			JOptionPane.showMessageDialog(panela, ex.getMessage(), "Errorea", JOptionPane.WARNING_MESSAGE);
		} catch (DataAccessException ex) {
			LoggerUtil.log("ERROR taldea gordetzean: " + ex.getMessage());
			JOptionPane.showMessageDialog(panela, "Errorea datu-basean: " + ex.getMessage(), "Errorea",
					JOptionPane.ERROR_MESSAGE);
		} catch (Exception ex) {
			LoggerUtil.log("ERROR orokorra taldea gordetzean: " + ex.getMessage());
			JOptionPane.showMessageDialog(panela, "Errorea: " + ex.getMessage(), "Errorea", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * 10 jokalari generiko sortzen ditu talde berriarentzat.
	 * 
	 * @param taldea Jokalariak gehituko zaizkion taldea.
	 */
	private void sortuJokalariGenerikoak(Taldea taldea) {
		String[] izenak = { "Iker", "Aitor", "Unai", "Mikel", "Jon", "Ander", "Xabi", "Oier", "Asier", "Gorka" };
		String[] abizenak = { "Etxebarria", "Zabala", "Urrutia", "Agirre", "Lopez", "Gonzalez", "Fernandez", "Martinez",
				"Sanchez", "Perez" };
		String[] posizioak = { "Aurrelaria", "Erdilaria", "Atzelaria", "Atezaina" };

		int sortutakoak = 0;
		for (int i = 0; i < 10; i++) {
			try {
				Jokalaria j = new Jokalaria();
				j.setIzena(izenak[i % izenak.length] + (i + 1));
				j.setAbizena(abizenak[i % abizenak.length]);
				j.setNan(String.format("%08d", (int) (Math.random() * 100000000)) + "A");
				j.setPosizioa(posizioak[i % posizioak.length]);
				j.setPisua(BigDecimal.valueOf(70 + (int) (Math.random() * 30)));
				j.setAltuera(BigDecimal.valueOf(1.70 + (Math.random() * 0.4)));
				j.setHerritartasuna("Euskal Herria");
				j.setTaldea(taldea);
				jokalariaDAO.jokalariaSortu(j);
				sortutakoak++;
			} catch (DataAccessException e) {
				LoggerUtil.log("ERROR jokalari generikoa sortzean: " + e.getMessage());
			}
		}
		LoggerUtil.log(sortutakoak + " jokalari generiko sortu dira " + taldea.getIzena() + " taldearentzat");
		JOptionPane.showMessageDialog(panela, sortutakoak + " jokalari generiko sortu dira.");
	}

	/**
	 * @return Taldeak gehitzeko panela.
	 */
	public JPanel getPanela() {
		return panela;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == eskudoaBotoia) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Aukeratu eskudoa");
			fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Irudiak (JPG, PNG, GIF)",
					"jpg", "jpeg", "png", "gif"));

			if (fileChooser.showOpenDialog(panela) == JFileChooser.APPROVE_OPTION) {
				java.io.File fitxategia = fileChooser.getSelectedFile();
				aukeratutakoEskudoaPath = fitxategia.getAbsolutePath();

				// Irudia eskalatu eta erakutsi
				ImageIcon originalIcon = new ImageIcon(fitxategia.getAbsolutePath());
				Image scaledImage = originalIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
				eskudoaLabel.setIcon(new ImageIcon(scaledImage));
				eskudoaLabel.setText("");
				eskudoaLabel.setForeground(Color.WHITE);
			}
		}
	}
}