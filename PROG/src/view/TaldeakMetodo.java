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
import pojos.Erabiltzailea;
import pojos.Jokalaria;
import pojos.Taldea;
import util.LoggerUtil;
import util.LoggerUtil.DataAccessException;

/**
 * Ikuspegiaren (View) geruzako klasea. Talde guztiak sare formatuan erakusten
 * dituen panela. Admin erabiltzaileek taldeak eta jokalariak editatu/ezabatu
 * ditzakete.
 */
public class TaldeakMetodo implements ActionListener {

	private JPanel taldeakPanela;
	private JButton saioaAmaituBotoia;
	private TaldeaDAO taldeaDAO;
	private JokalariaDAO jokalariaDAO;
	private Erabiltzailea erabiltzailea;
	private Color atzekoKolorea;

	/**
	 * TaldeakMetodo klasearen eraikitzailea.
	 * 
	 * @param kolorea Atzeko planoaren kolorea.
	 */
	public TaldeakMetodo(Color kolorea) {
		this.atzekoKolorea = kolorea;
		taldeaDAO = new TaldeaDAO();
		jokalariaDAO = new JokalariaDAO();
	}

	/**
	 * Erabiltzailea ezartzen du (rola jakiteko).
	 * 
	 * @param erabiltzailea Saioa hasi duen erabiltzailea.
	 */
	public void setErabiltzailea(Erabiltzailea erabiltzailea) {
		this.erabiltzailea = erabiltzailea;
	}

	/**
	 * Panela eraikitzen edo berreraikitzen du.
	 */
	public void eraikiPanela() {
		if (taldeakPanela == null) {
			taldeakPanela = new JPanel(null);
		} else {
			taldeakPanela.removeAll();
		}
		taldeakPanela.setBackground(atzekoKolorea);

		// Izenburua
		JLabel titulua = new JLabel("TALDEAK");
		titulua.setForeground(Color.WHITE);
		titulua.setFont(new Font("Arial", Font.BOLD, 40));
		titulua.setHorizontalAlignment(SwingConstants.CENTER);
		titulua.setBounds(300, 20, 300, 50);
		taldeakPanela.add(titulua);

		// Sare panela (2 errenkada, 3 zutabe)
		JPanel sarePanela = new JPanel(new GridLayout(2, 3, 30, 30));
		sarePanela.setBackground(atzekoKolorea);
		sarePanela.setBounds(50, 90, 800, 350);

		try {
			List<Taldea> taldeak = taldeaDAO.taldeGuztiakLortu();
			for (Taldea t : taldeak) {
				if (t.getTaldeaKod() != 0) {
					// Taldearen panela sortu
					JPanel taldePanela = new JPanel(new BorderLayout());
					taldePanela.setOpaque(false);

					// Taldearen botoia (logoarekin)
					JButton botoia = taldeBotoiaSortu(t.getIzena(), t.getTaldeaKod());
					taldePanela.add(botoia, BorderLayout.CENTER);

					// Admin bada, editatu eta ezabatu botoiak gehitu
					if (erabiltzailea != null && "ADMIN".equals(erabiltzailea.getRola())) {
						JPanel botoiPanela = new JPanel(new GridLayout(1, 2, 5, 5));
						botoiPanela.setOpaque(false);

						JButton editatuBotoia = new JButton("Editatu");
						editatuBotoia.setFont(new Font("Arial", Font.BOLD, 12));
						editatuBotoia.setBackground(new Color(255, 200, 0));
						editatuBotoia.setForeground(Color.BLACK);
						editatuBotoia.addActionListener(e -> editatuTaldea(t));

						JButton ezabatuBotoia = new JButton("Ezabatu");
						ezabatuBotoia.setFont(new Font("Arial", Font.BOLD, 12));
						ezabatuBotoia.setBackground(Color.RED);
						ezabatuBotoia.setForeground(Color.WHITE);
						ezabatuBotoia.addActionListener(e -> ezabatuTaldea(t));

						botoiPanela.add(editatuBotoia);
						botoiPanela.add(ezabatuBotoia);
						taldePanela.add(botoiPanela, BorderLayout.SOUTH);
					}

					sarePanela.add(taldePanela);
				}
			}
		} catch (DataAccessException e) {
			LoggerUtil.log("ERROR taldeak kargatzean: " + e.getMessage());
			JOptionPane.showMessageDialog(taldeakPanela, "Errorea taldeak kargatzerakoan: " + e.getMessage(), "Errorea",
					JOptionPane.ERROR_MESSAGE);
		}

		taldeakPanela.add(sarePanela);

		// Saioa amaitu botoia
		saioaAmaituBotoia = new JButton("Saioa amaitu");
		saioaAmaituBotoia.setFont(new Font("Arial", Font.BOLD, 18));
		saioaAmaituBotoia.setBackground(Color.RED);
		saioaAmaituBotoia.setForeground(Color.WHITE);
		saioaAmaituBotoia.setBounds(700, 480, 170, 40);
		saioaAmaituBotoia.addActionListener(this);
		taldeakPanela.add(saioaAmaituBotoia);

		taldeakPanela.revalidate();
		taldeakPanela.repaint();
	}

	/**
	 * Taldearen botoia sortzen du (logoarekin).
	 * 
	 * @param izena    Taldearen izena.
	 * @param taldeKod Taldearen IDa.
	 * @return Konfiguratutako botoia.
	 */
	private JButton taldeBotoiaSortu(String izena, int taldeKod) {
		JButton botoia = new JButton(izena);
		try {
			ImageIcon icono = null;
			String logoBidea = "/images/LogosEquipos/" + izena.replace(" ", "") + ".png";
			java.net.URL url = getClass().getResource(logoBidea);

			if (url != null) {
				icono = new ImageIcon(url);
			} else {
				// Saiatu fitxategi sistematik kargatzen
				java.io.File file = new java.io.File("images/LogosEquipos", izena.replace(" ", "") + ".png");
				if (file.exists()) {
					icono = new ImageIcon(file.getAbsolutePath());
				}
			}

			if (icono != null) {
				Image irudia = icono.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
				botoia.setIcon(new ImageIcon(irudia));
			}
		} catch (Exception e) {
			// Logoa ez bada aurkitzen, testua bakarrik erakutsiko da
		}

		botoia.setVerticalTextPosition(SwingConstants.BOTTOM);
		botoia.setHorizontalTextPosition(SwingConstants.CENTER);
		botoia.setFont(new Font("Arial", Font.BOLD, 16));
		botoia.setForeground(Color.WHITE);
		botoia.setBorderPainted(false);
		botoia.setFocusPainted(false);
		botoia.setContentAreaFilled(false);
		botoia.setCursor(new Cursor(Cursor.HAND_CURSOR));

		botoia.addActionListener(e -> {
			try {
				erakutsiJokalariak(taldeKod, izena);
			} catch (Exception ex) {
				LoggerUtil.log("ERROR jokalariak erakustean: " + ex.getMessage());
				JOptionPane.showMessageDialog(taldeakPanela, "Errorea jokalariak erakusteko: " + ex.getMessage(),
						"Errorea", JOptionPane.ERROR_MESSAGE);
			}
		});

		return botoia;
	}

	/**
	 * Jokalarien zerrenda erakusten du dialog batean. Admin bada, editatu/ezabatu
	 * aukerak gehitzen ditu.
	 * 
	 * @param taldeKod   Taldearen IDa.
	 * @param taldeIzena Taldearen izena.
	 */
	private void erakutsiJokalariak(int taldeKod, String taldeIzena) throws DataAccessException {
		List<Jokalaria> jokalariak = jokalariaDAO.jokalariaLortuTaldeBidez(taldeKod);

		if (jokalariak == null || jokalariak.isEmpty()) {
			JOptionPane.showMessageDialog(taldeakPanela, "Ez dago jokalaririk talde honetan", "Informazioa",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		// Dialog-a sortu
		JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(taldeakPanela),
				taldeIzena + " - Jokalariak", true);
		dialog.setSize(500, 400);
		dialog.setLocationRelativeTo(taldeakPanela);
		dialog.setLayout(new BorderLayout());

		// Jokalarien zerrenda
		DefaultListModel<Jokalaria> modeloa = new DefaultListModel<>();
		for (Jokalaria j : jokalariak) {
			modeloa.addElement(j);
		}

		JList<Jokalaria> zerrenda = new JList<>(modeloa);
		zerrenda.setFont(new Font("Arial", Font.PLAIN, 14));
		JScrollPane korritzePanela = new JScrollPane(zerrenda);
		dialog.add(korritzePanela, BorderLayout.CENTER);

		// Admin bada, botoiak gehitu
		if (erabiltzailea != null && "ADMIN".equals(erabiltzailea.getRola())) {
			JPanel botoiPanela = new JPanel(new FlowLayout());

			JButton editatuBotoia = new JButton("Editatu");
			editatuBotoia.addActionListener(e -> {
				Jokalaria selected = zerrenda.getSelectedValue();
				if (selected != null) {
					editatuJokalaria(selected, dialog);
				} else {
					JOptionPane.showMessageDialog(dialog, "Aukeratu jokalari bat.");
				}
			});

			JButton ezabatuBotoia = new JButton("Ezabatu");
			ezabatuBotoia.setBackground(Color.RED);
			ezabatuBotoia.setForeground(Color.WHITE);
			ezabatuBotoia.addActionListener(e -> {
				Jokalaria selected = zerrenda.getSelectedValue();
				if (selected != null) {
					ezabatuJokalaria(selected, modeloa, zerrenda);
				} else {
					JOptionPane.showMessageDialog(dialog, "Aukeratu jokalari bat.");
				}
			});

			JButton itxiBotoia = new JButton("Itxi");
			itxiBotoia.addActionListener(e -> dialog.dispose());

			botoiPanela.add(editatuBotoia);
			botoiPanela.add(ezabatuBotoia);
			botoiPanela.add(itxiBotoia);
			dialog.add(botoiPanela, BorderLayout.SOUTH);
		} else {
			JButton itxiBotoia = new JButton("Itxi");
			itxiBotoia.addActionListener(e -> dialog.dispose());
			JPanel botoiPanela = new JPanel();
			botoiPanela.add(itxiBotoia);
			dialog.add(botoiPanela, BorderLayout.SOUTH);
		}

		dialog.setVisible(true);
	}

	/**
	 * Taldea editatzeko dialog-a irekitzen du.
	 * 
	 * @param taldea Editatu nahi den taldea.
	 */
	private void editatuTaldea(Taldea taldea) {
		JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(taldeakPanela),
				"Taldea editatu: " + taldea.getIzena(), true);
		dialog.setSize(450, 300);
		dialog.setLocationRelativeTo(taldeakPanela);
		dialog.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);

		// Izena
		gbc.gridx = 0;
		gbc.gridy = 0;
		dialog.add(new JLabel("Izena:"), gbc);
		gbc.gridx = 1;
		JTextField izenaField = new JTextField(taldea.getIzena(), 20);
		dialog.add(izenaField, gbc);

		// Sortze data
		gbc.gridx = 0;
		gbc.gridy = 1;
		dialog.add(new JLabel("Sortze data (yyyy-MM-dd):"), gbc);
		gbc.gridx = 1;
		JTextField dataField = new JTextField(
				taldea.getSortzeData() != null ? new SimpleDateFormat("yyyy-MM-dd").format(taldea.getSortzeData()) : "",
				20);
		dialog.add(dataField, gbc);

		// Zelaia (konpontzeko: ZelaiaDAO erabili beharko litzateke)
		gbc.gridx = 0;
		gbc.gridy = 2;
		dialog.add(new JLabel("Zelaia:"), gbc);
		gbc.gridx = 1;
		JTextField zelaiaField = new JTextField(taldea.getZelaia() != null ? taldea.getZelaia().getIzena() : "", 20);
		zelaiaField.setEditable(false);
		dialog.add(zelaiaField, gbc);

		// Gorde botoia
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		JButton gordeBotoia = new JButton("Gorde");
		gordeBotoia.setBackground(new Color(0, 150, 0));
		gordeBotoia.setForeground(Color.WHITE);
		gordeBotoia.addActionListener(e -> {
			try {
				taldea.setIzena(izenaField.getText().trim());
				if (!dataField.getText().trim().isEmpty()) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					taldea.setSortzeData(sdf.parse(dataField.getText().trim()));
				}
				taldeaDAO.TaldeaEguneratu(taldea);
				JOptionPane.showMessageDialog(dialog, "Taldea eguneratu da.");
				dialog.dispose();
				eraikiPanela();
			} catch (ParseException ex) {
				JOptionPane.showMessageDialog(dialog, "Data formatua okerra. Erabili yyyy-MM-dd.");
			} catch (DataAccessException ex) {
				LoggerUtil.log("ERROR taldea eguneratzean: " + ex.getMessage());
				JOptionPane.showMessageDialog(dialog, "Errorea: " + ex.getMessage());
			}
		});
		dialog.add(gordeBotoia, gbc);

		dialog.setVisible(true);
	}

	/**
	 * Jokalaria editatzeko dialog-a irekitzen du.
	 * 
	 * @param jokalaria    Editatu nahi den jokalaria.
	 * @param parentDialog Guraso dialog-a (editatu ondoren ixteko).
	 */
	private void editatuJokalaria(Jokalaria jokalaria, JDialog parentDialog) {
		JDialog dialog = new JDialog(parentDialog, "Jokalaria editatu", true);
		dialog.setSize(450, 400);
		dialog.setLocationRelativeTo(parentDialog);
		dialog.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);

		// Izena
		gbc.gridx = 0;
		gbc.gridy = 0;
		dialog.add(new JLabel("Izena:"), gbc);
		gbc.gridx = 1;
		JTextField izenaField = new JTextField(jokalaria.getIzena(), 20);
		dialog.add(izenaField, gbc);

		// Abizena
		gbc.gridx = 0;
		gbc.gridy = 1;
		dialog.add(new JLabel("Abizena:"), gbc);
		gbc.gridx = 1;
		JTextField abizenaField = new JTextField(jokalaria.getAbizena(), 20);
		dialog.add(abizenaField, gbc);

		// Posizioa
		gbc.gridx = 0;
		gbc.gridy = 2;
		dialog.add(new JLabel("Posizioa:"), gbc);
		gbc.gridx = 1;
		JTextField posizioaField = new JTextField(jokalaria.getPosizioa(), 20);
		dialog.add(posizioaField, gbc);

		// Pisua
		gbc.gridx = 0;
		gbc.gridy = 3;
		dialog.add(new JLabel("Pisua (kg):"), gbc);
		gbc.gridx = 1;
		JTextField pisuaField = new JTextField(jokalaria.getPisua() != null ? jokalaria.getPisua().toString() : "", 20);
		dialog.add(pisuaField, gbc);

		// Altuera
		gbc.gridx = 0;
		gbc.gridy = 4;
		dialog.add(new JLabel("Altuera (m):"), gbc);
		gbc.gridx = 1;
		JTextField altueraField = new JTextField(
				jokalaria.getAltuera() != null ? jokalaria.getAltuera().toString() : "", 20);
		dialog.add(altueraField, gbc);

		// Gorde botoia
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = 2;
		JButton gordeBotoia = new JButton("Gorde");
		gordeBotoia.setBackground(new Color(0, 150, 0));
		gordeBotoia.setForeground(Color.WHITE);
		gordeBotoia.addActionListener(e -> {
			try {
				jokalaria.setIzena(izenaField.getText().trim());
				jokalaria.setAbizena(abizenaField.getText().trim());
				jokalaria.setPosizioa(posizioaField.getText().trim());

				if (!pisuaField.getText().trim().isEmpty()) {
					jokalaria.setPisua(new BigDecimal(pisuaField.getText().trim()));
				}
				if (!altueraField.getText().trim().isEmpty()) {
					jokalaria.setAltuera(new BigDecimal(altueraField.getText().trim()));
				}

				jokalariaDAO.eguneratu(jokalaria);
				JOptionPane.showMessageDialog(dialog, "Jokalaria eguneratu da.");
				dialog.dispose();
				parentDialog.dispose();
				// Berriro ireki jokalarien zerrenda
				erakutsiJokalariak(jokalaria.getTaldea().getTaldeaKod(), jokalaria.getTaldea().getIzena());
			} catch (DataAccessException ex) {
				LoggerUtil.log("ERROR jokalaria eguneratzean: " + ex.getMessage());
				JOptionPane.showMessageDialog(dialog, "Errorea: " + ex.getMessage());
			}
		});
		dialog.add(gordeBotoia, gbc);

		dialog.setVisible(true);
	}

	/**
	 * Jokalaria ezabatzen du baieztapenarekin.
	 * 
	 * @param jokalaria Ezabatu nahi den jokalaria.
	 * @param modeloa   Zerrendaren modeloa.
	 * @param zerrenda  JList-a.
	 */
	private void ezabatuJokalaria(Jokalaria jokalaria, DefaultListModel<Jokalaria> modeloa, JList<Jokalaria> zerrenda) {
		int confirm = JOptionPane.showConfirmDialog(taldeakPanela,
				"Ziur zaude " + jokalaria.getIzena() + " " + jokalaria.getAbizena() + " ezabatu nahi duzula?",
				"Jokalaria ezabatu", JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION) {
			try {
				jokalariaDAO.jokalariaEzabatu(jokalaria.getJokalariakKod());
				modeloa.removeElement(jokalaria);
				JOptionPane.showMessageDialog(taldeakPanela, "Jokalaria ezabatu da.");
				LoggerUtil.log("Jokalaria ezabatu da: " + jokalaria.getIzena() + " " + jokalaria.getAbizena());
			} catch (DataAccessException ex) {
				LoggerUtil.log("ERROR jokalaria ezabatzean: " + ex.getMessage());
				JOptionPane.showMessageDialog(taldeakPanela, "Errorea: " + ex.getMessage());
			}
		}
	}

	/**
	 * Taldea ezabatzen du baieztapenarekin.
	 * 
	 * @param taldea Ezabatu nahi den taldea.
	 */
	private void ezabatuTaldea(Taldea taldea) {
		int confirm = JOptionPane.showConfirmDialog(taldeakPanela,
				"Ziur zaude " + taldea.getIzena()
						+ " taldea ezabatu nahi duzula?\nJokalari guztiak ere ezabatuko dira.",
				"Taldea ezabatu", JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION) {
			try {
				taldeaDAO.TaldeEzabatu(taldea.getTaldeaKod());
				JOptionPane.showMessageDialog(taldeakPanela, "Taldea ondo ezabatu da.");
				LoggerUtil.log("Taldea ezabatu da: " + taldea.getIzena());
				eraikiPanela();
			} catch (DataAccessException ex) {
				LoggerUtil.log("ERROR taldea ezabatzean: " + ex.getMessage());
				JOptionPane.showMessageDialog(taldeakPanela, "Errorea taldea ezabatzean: " + ex.getMessage());
			}
		}
	}

	/**
	 * Panela eguneratzen du (kanpotik deitzeko).
	 */
	public void eguneratuPanela() {
		eraikiPanela();
	}

	/**
	 * @return Taldeen panela.
	 */
	public JPanel getPanela() {
		return taldeakPanela;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		SwingUtilities.invokeLater(() -> new Login().setVisible(true));
		JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(taldeakPanela);
		if (frame != null)
			frame.dispose();
	}
}