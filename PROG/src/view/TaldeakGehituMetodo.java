package view;

import model.*;
import dao.TaldeaDAO;
import dao.ZelaiaDAO;
import pojos.Taldea;
import pojos.Zelaia;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TaldeakGehituMetodo implements ActionListener {

	private JPanel panela;

	private JTextField izenaField;
	private JTextField sortzeDataField;

	private JComboBox<Zelaia> zelaiaCombo;

	private JButton eskudoaBotoia;
	private JButton gordeBotoia;
	private JButton ezeztatuBotoia;

	private JLabel titulua;
	private JLabel izenaLabel;
	private JLabel sortzeDataLabel;
	private JLabel zelaiaLabel;
	private JLabel eskudoaLabel;

	private TaldeaDAO taldeaDAO;
	private ZelaiaDAO zelaiaDAO;

	public TaldeakGehituMetodo(Color kolorea) {
		taldeaDAO = new TaldeaDAO();
		zelaiaDAO = new ZelaiaDAO();

		panela = new JPanel(null);
		panela.setBackground(kolorea);

		titulua = new JLabel("TALDE BERRIA GEHITU");
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
		izenaLabel = new JLabel("Taldearen izena:");
		izenaLabel.setForeground(Color.WHITE);
		izenaLabel.setFont(new Font("Arial", Font.BOLD, 18));
		izenaLabel.setBounds(labelX, y, 150, 30);
		panela.add(izenaLabel);

		izenaField = new JTextField();
		izenaField.setBounds(fieldX, y, fieldWidth, 35);
		panela.add(izenaField);
		y += rowHeight;

		// Sortze data
		sortzeDataLabel = new JLabel("Sortze data (yyyy-MM-dd):");
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
		zelaiaLabel = new JLabel("Zelaia:");
		zelaiaLabel.setForeground(Color.WHITE);
		zelaiaLabel.setFont(new Font("Arial", Font.BOLD, 18));
		zelaiaLabel.setBounds(labelX, y, 150, 30);
		panela.add(zelaiaLabel);

		zelaiaCombo = new JComboBox<>();
		zelaiaCombo.addItem(null);
		List<Zelaia> zelaiak = zelaiaDAO.zelaiGutziakLortu();
		for (Zelaia z : zelaiak) {
			zelaiaCombo.addItem(z);
		}
		zelaiaCombo.setBounds(fieldX, y, fieldWidth, 35);
		panela.add(zelaiaCombo);
		y += rowHeight;

		eskudoaBotoia = new JButton("Aukeratu eskudoa");
		eskudoaBotoia.setFont(new Font("Arial", Font.BOLD, 14));
		eskudoaBotoia.setBounds(fieldX, y, 200, 35);
		eskudoaBotoia.addActionListener(this);

		panela.add(eskudoaBotoia);

		eskudoaLabel = new JLabel("Ez da eskudoirik hautatu");
		eskudoaLabel.setForeground(Color.LIGHT_GRAY);
		eskudoaLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		eskudoaLabel.setBounds(fieldX, y + 40, 250, 20);
		panela.add(eskudoaLabel);
		y += 60;

		gordeBotoia = new JButton("Gorde");
		gordeBotoia.setFont(new Font("Arial", Font.BOLD, 16));
		gordeBotoia.setBackground(new Color(0, 150, 0));
		gordeBotoia.setForeground(Color.WHITE);
		gordeBotoia.setBounds(270, y, 150, 40);
		gordeBotoia.addActionListener(e -> gordeTaldea());
		panela.add(gordeBotoia);

		ezeztatuBotoia = new JButton("Ezeztatu");
		ezeztatuBotoia.setFont(new Font("Arial", Font.BOLD, 16));
		ezeztatuBotoia.setBackground(Color.RED);
		ezeztatuBotoia.setForeground(Color.WHITE);
		ezeztatuBotoia.setBounds(470, y, 150, 40);
		ezeztatuBotoia.addActionListener(e -> {
			izenaField.setText("");
			sortzeDataField.setText("");
			zelaiaCombo.setSelectedIndex(0);
			eskudoaLabel.setText("Ez da eskudoirik hautatu");
		});
		panela.add(ezeztatuBotoia);
	}

	private void gordeTaldea() {
		try {
			// Balidatu hutsik ez huzteko
			if (izenaField.getText().trim().isEmpty()) {
				throw new IllegalArgumentException("Taldearen izena ezin da hutsik egon.");
			}

			// Taldea objetua sortu
			Taldea t = new Taldea();
			t.setIzena(izenaField.getText().trim());

			// Sortze data 
			if (!sortzeDataField.getText().trim().isEmpty()) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				sdf.setLenient(false);
				try {
					Date data = sdf.parse(sortzeDataField.getText().trim());
					t.setSortzeData(data);
				} catch (ParseException ex) {
					throw new IllegalArgumentException(
							"Data formatua ez da zuzena. Erabili yyyy-MM-dd (adibidez: 2020-01-15)");
				}
			}

			// Zelaia 
			Zelaia zelaia = (Zelaia) zelaiaCombo.getSelectedItem();
			t.setZelaia(zelaia);

			// Gorde BD
			boolean insertado = taldeaDAO.taldeaSortu(t);
			if (!insertado) {
				throw new Exception("Errorea taldea gordetzean.");
			}

			JOptionPane.showMessageDialog(panela, "Taldea ondo gorde da.", "Ondo", JOptionPane.INFORMATION_MESSAGE);
			// Garbitu kanpoak
			ezeztatuBotoia.doClick();

		} catch (IllegalArgumentException ex) {
			JOptionPane.showMessageDialog(panela, ex.getMessage(), "Errorea", JOptionPane.WARNING_MESSAGE);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(panela, "Errorea: " + ex.getMessage(), "Errorea", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}

	public JPanel getPanela() {
		return panela;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();

		if (src == eskudoaBotoia) {
		    JFileChooser fileChooser = new JFileChooser();
		    fileChooser.setDialogTitle("Aukeratu eskudoa");
		    fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
		        "Irudiak (JPG, PNG, GIF)", "jpg", "jpeg", "png"));

		    int aukeraketa = fileChooser.showOpenDialog(panela);
		    if (aukeraketa == JFileChooser.APPROVE_OPTION) {
		        java.io.File fitxategia = fileChooser.getSelectedFile();

		        // Eskalatu eta jarri JLabel-era
		        ImageIcon originalIcon = new ImageIcon(fitxategia.getAbsolutePath());
		        Image scaledImage = originalIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		        ImageIcon scaledIcon = new ImageIcon(scaledImage);
		        eskudoaLabel.setIcon(scaledIcon);
		        eskudoaLabel.setText("");
		        eskudoaLabel.setHorizontalAlignment(SwingConstants.LEFT);
		        eskudoaLabel.setVerticalAlignment(SwingConstants.CENTER);

		        // Karpeta non gorde nahi duzun
		        String karpetaPath = "/images/LogosEquipos";
		        java.io.File karpeta = new java.io.File(karpetaPath);
		        if (!karpeta.exists()) {
		            karpeta.mkdirs(); // sortu karpeta, existitzen ez bada
		        }

		        // Fitxategia kopiatu karpetara
		        java.io.File destinationFile = new java.io.File(karpeta, fitxategia.getName());
		        try {
		            java.nio.file.Files.copy(fitxategia.toPath(), destinationFile.toPath(),
		                                     java.nio.file.StandardCopyOption.REPLACE_EXISTING);
		            // Path erlatiboa edo izena gorde Taldea objektuan
		            // Adibidez:
		            // t.setLogoPath("images/LogosEquipos/" + fitxategia.getName());
		            System.out.println("Eskudoa gorde da: " + destinationFile.getAbsolutePath());
		        } catch (Exception ex) {
		            JOptionPane.showMessageDialog(panela, "Errorea eskudoa gordetzean: " + ex.getMessage(),
		                                          "Errorea", JOptionPane.ERROR_MESSAGE);
		            ex.printStackTrace();
		        }
		    }
		}

	}
}