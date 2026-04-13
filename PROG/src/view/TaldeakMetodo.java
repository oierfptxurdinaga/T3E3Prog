package view;

import model.*;
import dao.JokalariaDAO;
import dao.TaldeaDAO;
import pojos.Jokalaria;
import pojos.Taldea;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Ikuspegiaren (View) geruzako klasea.
 * Aplikazioko talde guztiak sare (grid) formatuan erakusten dituen panela kudeatzen du.
 * Talde bakoitzaren armarria botoi gisa erakusten da, eta bertan klik egitean, 
 * talde horretako jokalarien zerrenda bistaratzen du leiho gainerakor (pop-up) batean.
 */
public class TaldeakMetodo implements ActionListener {

	private JPanel taldeakPanela;
	private JPanel sarePanela;
	private JButton saioaAmaituBotoia;
	private DefaultListModel<Jokalaria> modeloa;
	private ImageIcon ikonoa;
	private List<Jokalaria> jokalariak;
	private JList<Jokalaria> zerrenda;
	private JScrollPane korritzePanela;
	private Image irudia;

	private TaldeaDAO taldeaDAO;
	private JokalariaDAO jokalariaDAO;

	/**
	 * TaldeakMetodo klasearen eraikitzailea.
	 * Panela hasieratzen du, datu-basetik talde guztiak lortzen ditu eta bakoitzarentzat 
	 * botoi grafiko bat (armarriarekin) sortzen du sare (grid) egitura batean.
	 * @param kolorea Aplikazioaren atzeko planoaren kolore nagusia.
	 */
	public TaldeakMetodo(Color kolorea) {
		taldeaDAO = new TaldeaDAO();
		jokalariaDAO = new JokalariaDAO();

		taldeakPanela = new JPanel(null);
		taldeakPanela.setBackground(kolorea);

		sarePanela = new JPanel(new GridLayout(2, 3, 30, 30));
		sarePanela.setBackground(kolorea);
		sarePanela.setBounds(50, 50, 800, 380);

		List<Taldea> taldeak = taldeaDAO.taldeGuztiakLortu();
		for (Taldea t : taldeak) {
			if (t.getTaldeaKod() != 0) {
				String nombreArchivo = t.getIzena().replace(" ", "") + ".png";
				String logoBidea = "/images/LogosEquipos/" + nombreArchivo;
				sarePanela.add(taldeBotoiaSortu(t.getIzena(), logoBidea, t.getTaldeaKod()));
			}
		}

		taldeakPanela.add(sarePanela);

		saioaAmaituBotoia = new JButton("Saioa amaitu");
		saioaAmaituBotoia.setFont(new Font("Arial", Font.BOLD, 18));
		saioaAmaituBotoia.setBackground(Color.RED);
		saioaAmaituBotoia.setForeground(Color.WHITE);
		saioaAmaituBotoia.setBounds(700, 480, 170, 40);
		saioaAmaituBotoia.addActionListener(this);

		taldeakPanela.add(saioaAmaituBotoia);
	}

	/**
	 * Talde baten botoi grafikoa sortzen du, bere izena eta armarria (logoa) ezarriz.
	 * @param izena Taldearen izena, botoiaren testuan erakusteko.
	 * @param logoBidea Armarriaren irudiaren fitxategi-bidea (fitxategi-sistematik edo baliabideetatik kargatzeko).
	 * @param taldeKod Taldearen identifikatzailea (ID-a), jokalariak bilatzeko erabiliko dena.
	 * @return Konfiguratutako {@link JButton} objektua, klik egiterakoan jokalariak erakutsiko dituena.
	 */
	private JButton taldeBotoiaSortu(String izena, String logoBidea, int taldeKod) {
		JButton botoia = new JButton(izena);
		try {
			ImageIcon icono = null;

			// Saiatu kargatzen resource-tik (src)
			java.net.URL url = getClass().getResource(logoBidea);

			if (url != null) {
				icono = new ImageIcon(url);
			} else {
				// Ez bada existitzen
				java.io.File file = new java.io.File(logoBidea);
				if (file.exists()) {
					icono = new ImageIcon(logoBidea);
				} else {
					System.err.println("Logo ez da aurkitu: " + logoBidea);
				}
			}

			// Irudia aurkitzen denean
			if (icono != null) {
				Image irudia = icono.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
				botoia.setIcon(new ImageIcon(irudia));
			}
		} catch (Exception e) {
			System.err.println("Errorea logo kargatzerakoan: " + e.getMessage());
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
				JOptionPane.showMessageDialog(taldeakPanela, "Errorea jokalariak erakusteko: " + ex.getMessage(),
						"Errorea", JOptionPane.ERROR_MESSAGE);
				ex.printStackTrace();
			}
		});

		return botoia;
	}

	/**
	 * Talde jakin bateko jokalarien zerrenda lortzen du datu-basetik eta 
	 * mezu-leiho batean (JOptionPane) bistaratzen ditu zerrenda korrigarri (JScrollPane) batean.
	 * @param taldeKod Taldearen identifikatzailea, jokalariak iragazteko.
	 * @param taldeIzena Taldearen izena, leihoaren izenburuan erakusteko.
	 * @throws Exception Datu-basetik jokalariak lortzerakoan erroren bat gertatzen bada.
	 */
	private void erakutsiJokalariak(int taldeKod, String taldeIzena) throws Exception {
		List<Jokalaria> jokalariak = jokalariaDAO.jokalariaLortuTaldeBidez(taldeKod);

		if (jokalariak == null || jokalariak.isEmpty()) {
			JOptionPane.showMessageDialog(taldeakPanela, "Ez dago jokalaririk talde honetan", "Informazioa",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		modeloa = new DefaultListModel<>();
		for (Jokalaria j : jokalariak) {
			modeloa.addElement(j);
		}

		zerrenda = new JList<>(modeloa);
		zerrenda.setFont(new Font("Arial", Font.PLAIN, 16));

		korritzePanela = new JScrollPane(zerrenda);
		korritzePanela.setPreferredSize(new Dimension(350, 250));

		JOptionPane.showMessageDialog(taldeakPanela, korritzePanela, taldeIzena + " - Jokalariak",
				JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * TaldeakMetodo klaseak sortutako panel nagusia itzultzen du, 
	 * aplikazioaren leiho nagusian txertatu ahal izateko.
	 * @return Taldeak ikusteko pantailako {@link JPanel} objektua.
	 */
	public JPanel getPanela() {
		return taldeakPanela;
	}

	/**
	 * "Saioa amaitu" botoiaren klikak kudeatzen ditu.
	 * Uneko leihoa ixten du eta berriro saioa hasteko pantaila (Login) irekitzen du.
	 * @param e Botoiaren sakatze-gertaera.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			SwingUtilities.invokeLater(() -> new Login().setVisible(true));
			JFrame frame = (JFrame) SwingUtilities.getWindowAncestor((Component) e.getSource());
			frame.dispose();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(taldeakPanela, "Errorea saioa amaitzerakoan: " + ex.getMessage(), "Errorea",
					JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}

	}
}