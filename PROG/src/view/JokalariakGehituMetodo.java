package view;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

import dao.JokalariaDAO;
import dao.TaldeaDAO;
import pojos.Jokalaria;
import pojos.Taldea;
import util.LoggerUtil;
import util.LoggerUtil.DataAccessException;

/**
 * Ikuspegiaren (View) geruzako klasea. Jokalari berri bat datu-basean
 * erregistratzeko interfaze grafikoa.
 */
public class JokalariakGehituMetodo {

	private JPanel panela;
	private JTextField izenaField;
	private JTextField abizenaField;
	private JTextField nanField;
	private JTextField posizioaField;
	private JTextField pisuaField;
	private JTextField altueraField;
	private JTextField herritartasunaField;
	private JComboBox<Taldea> taldeaCombo;
	private JButton argazkiaBotoia;
	private JLabel argazkiaLabel;
	private JLabel argazkiIrudiaLabel;
	private String aukeratutakoArgazkiPath = null;
	private JokalariaDAO jokalariaDAO;
	private TaldeaDAO taldeaDAO;

	/**
	 * JokalariakGehituMetodo klasearen eraikitzailea.
	 * 
	 * @param kolorea Atzeko planoaren kolorea.
	 */
	public JokalariakGehituMetodo(Color kolorea) {
		jokalariaDAO = new JokalariaDAO();
		taldeaDAO = new TaldeaDAO();

		// Panela konfiguratu
		panela = new JPanel(null);
		panela.setBackground(kolorea);

		JLabel titulua = new JLabel("JOKALARI BERRIA GEHITU");
		titulua.setForeground(Color.WHITE);
		titulua.setFont(new Font("Arial", Font.BOLD, 32));
		titulua.setBounds(200, 40, 600, 40);
		panela.add(titulua);

		int y = 100;
		int labelX = 200;
		int fieldX = 350;
		int fieldWidth = 250;
		int rowHeight = 40;

		// Izena
		JLabel izenaLabel = new JLabel("Izena:");
		izenaLabel.setForeground(Color.WHITE);
		izenaLabel.setFont(new Font("Arial", Font.BOLD, 16));
		izenaLabel.setBounds(labelX, y, 120, 30);
		panela.add(izenaLabel);
		izenaField = new JTextField();
		izenaField.setBounds(fieldX, y, fieldWidth, 30);
		panela.add(izenaField);
		y += rowHeight;

		// Abizena
		JLabel abizenaLabel = new JLabel("Abizena:");
		abizenaLabel.setForeground(Color.WHITE);
		abizenaLabel.setFont(new Font("Arial", Font.BOLD, 16));
		abizenaLabel.setBounds(labelX, y, 120, 30);
		panela.add(abizenaLabel);
		abizenaField = new JTextField();
		abizenaField.setBounds(fieldX, y, fieldWidth, 30);
		panela.add(abizenaField);
		y += rowHeight;

		// NAN
		JLabel nanLabel = new JLabel("NAN:");
		nanLabel.setForeground(Color.WHITE);
		nanLabel.setFont(new Font("Arial", Font.BOLD, 16));
		nanLabel.setBounds(labelX, y, 120, 30);
		panela.add(nanLabel);
		nanField = new JTextField();
		nanField.setBounds(fieldX, y, fieldWidth, 30);
		panela.add(nanField);
		y += rowHeight;

		// Posizioa
		JLabel posizioaLabel = new JLabel("Posizioa:");
		posizioaLabel.setForeground(Color.WHITE);
		posizioaLabel.setFont(new Font("Arial", Font.BOLD, 16));
		posizioaLabel.setBounds(labelX, y, 120, 30);
		panela.add(posizioaLabel);
		posizioaField = new JTextField();
		posizioaField.setBounds(fieldX, y, fieldWidth, 30);
		panela.add(posizioaField);
		y += rowHeight;

		// Pisua
		JLabel pisuaLabel = new JLabel("Pisua (kg):");
		pisuaLabel.setForeground(Color.WHITE);
		pisuaLabel.setFont(new Font("Arial", Font.BOLD, 16));
		pisuaLabel.setBounds(labelX, y, 120, 30);
		panela.add(pisuaLabel);
		pisuaField = new JTextField();
		pisuaField.setBounds(fieldX, y, fieldWidth, 30);
		panela.add(pisuaField);
		y += rowHeight;

		// Altuera
		JLabel altueraLabel = new JLabel("Altuera (m):");
		altueraLabel.setForeground(Color.WHITE);
		altueraLabel.setFont(new Font("Arial", Font.BOLD, 16));
		altueraLabel.setBounds(labelX, y, 120, 30);
		panela.add(altueraLabel);
		altueraField = new JTextField();
		altueraField.setBounds(fieldX, y, fieldWidth, 30);
		panela.add(altueraField);
		y += rowHeight;

		// Herritartasuna
		JLabel herritartasunaLabel = new JLabel("Herritartasuna:");
		herritartasunaLabel.setForeground(Color.WHITE);
		herritartasunaLabel.setFont(new Font("Arial", Font.BOLD, 16));
		herritartasunaLabel.setBounds(labelX, y, 120, 30);
		panela.add(herritartasunaLabel);
		herritartasunaField = new JTextField();
		herritartasunaField.setBounds(fieldX, y, fieldWidth, 30);
		panela.add(herritartasunaField);
		y += rowHeight;

		// Taldea
		JLabel taldeaLabel = new JLabel("Taldea:");
		taldeaLabel.setForeground(Color.WHITE);
		taldeaLabel.setFont(new Font("Arial", Font.BOLD, 16));
		taldeaLabel.setBounds(labelX, y, 120, 30);
		panela.add(taldeaLabel);
		taldeaCombo = new JComboBox<>();
		taldeaCombo.addItem(null);
		try {
			List<Taldea> taldeak = taldeaDAO.taldeGuztiakLortu();
			for (Taldea t : taldeak) {
				if (t.getTaldeaKod() != 0) {
					taldeaCombo.addItem(t);
				}
			}
		} catch (DataAccessException e) {
			LoggerUtil.log("ERROR taldeak kargatzean: " + e.getMessage());
		}
		taldeaCombo.setBounds(fieldX, y, fieldWidth, 30);
		panela.add(taldeaCombo);
		y += rowHeight;

		// Argazkia aukeratzeko botoia
		argazkiaBotoia = new JButton("Aukeratu argazkia");
		argazkiaBotoia.setFont(new Font("Arial", Font.BOLD, 14));
		argazkiaBotoia.setBounds(fieldX, y, 200, 35);
		argazkiaBotoia.addActionListener(e -> aukeratuArgazkia());
		panela.add(argazkiaBotoia);

		argazkiaLabel = new JLabel("Ez da argazkirik hautatu");
		argazkiaLabel.setForeground(Color.LIGHT_GRAY);
		argazkiaLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		argazkiaLabel.setBounds(fieldX, y + 40, 250, 20);
		panela.add(argazkiaLabel);

		argazkiIrudiaLabel = new JLabel();
		argazkiIrudiaLabel.setBounds(fieldX + 220, y - 10, 100, 100);
		argazkiIrudiaLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		argazkiIrudiaLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panela.add(argazkiIrudiaLabel);
		y += 70;

		// Gorde botoia
		JButton gordeBotoia = new JButton("Gorde");
		gordeBotoia.setFont(new Font("Arial", Font.BOLD, 16));
		gordeBotoia.setBackground(new Color(0, 150, 0));
		gordeBotoia.setForeground(Color.WHITE);
		gordeBotoia.setBounds(280, y, 150, 40);
		gordeBotoia.addActionListener(e -> gordeJokalaria());
		panela.add(gordeBotoia);

		// Ezeztatu botoia
		JButton ezeztatuBotoia = new JButton("Ezeztatu");
		ezeztatuBotoia.setFont(new Font("Arial", Font.BOLD, 16));
		ezeztatuBotoia.setBackground(Color.RED);
		ezeztatuBotoia.setForeground(Color.WHITE);
		ezeztatuBotoia.setBounds(480, y, 150, 40);
		ezeztatuBotoia.addActionListener(e -> garbituFormularioa());
		panela.add(ezeztatuBotoia);
	}

	/**
	 * Irudi bat aukeratzeko fitxategi-esploratzailea irekitzen du.
	 */
	private void aukeratuArgazkia() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Aukeratu jokalariaren argazkia");
		fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Irudiak (JPG, PNG, GIF)", "jpg",
				"jpeg", "png", "gif"));

		if (fileChooser.showOpenDialog(panela) == JFileChooser.APPROVE_OPTION) {
			File fitxategia = fileChooser.getSelectedFile();
			aukeratutakoArgazkiPath = fitxategia.getAbsolutePath();

			// Irudia eskalatu eta erakutsi
			ImageIcon originalIcon = new ImageIcon(aukeratutakoArgazkiPath);
			Image scaledImage = originalIcon.getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH);
			argazkiIrudiaLabel.setIcon(new ImageIcon(scaledImage));
			argazkiaLabel.setText("Aukeratua: " + fitxategia.getName());
			argazkiaLabel.setForeground(Color.WHITE);
		}
	}

	/**
	 * Formularioa garbitzen du.
	 */
	private void garbituFormularioa() {
		izenaField.setText("");
		abizenaField.setText("");
		nanField.setText("");
		posizioaField.setText("");
		pisuaField.setText("");
		altueraField.setText("");
		herritartasunaField.setText("");
		taldeaCombo.setSelectedIndex(0);
		argazkiaLabel.setText("Ez da argazkirik hautatu");
		argazkiaLabel.setForeground(Color.LIGHT_GRAY);
		argazkiIrudiaLabel.setIcon(null);
		aukeratutakoArgazkiPath = null;
	}

	/**
	 * Jokalari berria datu-basean gordetzen du.
	 */
	private void gordeJokalaria() {
		try {
			// Derrigorrezko eremuak egiaztatu
			if (izenaField.getText().trim().isEmpty() || abizenaField.getText().trim().isEmpty()
					|| posizioaField.getText().trim().isEmpty()) {
				throw new IllegalArgumentException("Izena, abizena eta posizioa ezin dira hutsik egon.");
			}

			Jokalaria j = new Jokalaria();
			j.setIzena(izenaField.getText().trim());
			j.setAbizena(abizenaField.getText().trim());
			j.setNan(nanField.getText().trim().isEmpty() ? null : nanField.getText().trim());
			j.setPosizioa(posizioaField.getText().trim());

			// Pisua balidatu (50-120 kg)
			if (!pisuaField.getText().trim().isEmpty()) {
				try {
					double pisua = Double.parseDouble(pisuaField.getText().trim());
					if (pisua < 50 || pisua > 120) {
						throw new IllegalArgumentException("Pisua 50 eta 120 artean egon behar da.");
					}
					j.setPisua(BigDecimal.valueOf(pisua));
				} catch (NumberFormatException ex) {
					throw new IllegalArgumentException("Pisua zenbaki bat izan behar da.");
				}
			}

			// Altuera balidatu (1.50-2.20 m)
			if (!altueraField.getText().trim().isEmpty()) {
				try {
					double altuera = Double.parseDouble(altueraField.getText().trim());
					if (altuera < 1.50 || altuera > 2.20) {
						throw new IllegalArgumentException("Altuera 1.50 eta 2.20 artean egon behar da.");
					}
					j.setAltuera(BigDecimal.valueOf(altuera));
				} catch (NumberFormatException ex) {
					throw new IllegalArgumentException("Altuera zenbaki bat izan behar da.");
				}
			}

			j.setHerritartasuna(
					herritartasunaField.getText().trim().isEmpty() ? null : herritartasunaField.getText().trim());
			j.setTaldea((Taldea) taldeaCombo.getSelectedItem());

			// Argazkia gorde
			String argazkiRelativePath = null;
			if (aukeratutakoArgazkiPath != null && !aukeratutakoArgazkiPath.isEmpty()) {
				String karpetaPath = "images/Jugadores";
				File karpeta = new File(karpetaPath);
				if (!karpeta.exists()) {
					karpeta.mkdirs();
				}
				String extension = aukeratutakoArgazkiPath.substring(aukeratutakoArgazkiPath.lastIndexOf('.'));
				String izenBerria = "jug_" + System.currentTimeMillis() + extension;
				File destino = new File(karpeta, izenBerria);
				Files.copy(new File(aukeratutakoArgazkiPath).toPath(), destino.toPath(),
						StandardCopyOption.REPLACE_EXISTING);
				argazkiRelativePath = karpetaPath + "/" + izenBerria;
			}
			j.setArgazkia(argazkiRelativePath);

			// Datu-basean gorde
			jokalariaDAO.jokalariaSortu(j);

			JOptionPane.showMessageDialog(panela, "Jokalaria ondo gorde da.", "Ondo", JOptionPane.INFORMATION_MESSAGE);
			garbituFormularioa();

		} catch (IllegalArgumentException ex) {
			JOptionPane.showMessageDialog(panela, ex.getMessage(), "Errorea", JOptionPane.WARNING_MESSAGE);
		} catch (DataAccessException ex) {
			LoggerUtil.log("ERROR jokalaria gordetzean: " + ex.getMessage());
			JOptionPane.showMessageDialog(panela, "Errorea datu-basean: " + ex.getMessage(), "Errorea",
					JOptionPane.ERROR_MESSAGE);
		} catch (Exception ex) {
			LoggerUtil.log("ERROR orokorra jokalaria gordetzean: " + ex.getMessage());
			JOptionPane.showMessageDialog(panela, "Errorea: " + ex.getMessage(), "Errorea", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * @return Jokalariak gehitzeko panela.
	 */
	public JPanel getPanela() {
		return panela;
	}
}