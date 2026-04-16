package view;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import pojos.Erabiltzailea;
import util.LoggerUtil;

/**
 * Ikuspegiaren (View) geruzako klase nagusia. Erabiltzaileak saioa hasi ondoren
 * agertzen den aplikazioaren leiho nagusia.
 */
public class Main extends JFrame {

	private JPanel panelNagusia;
	private JTabbedPane fitxaPanela;

	// Panelak
	private EmaitzakMetodo emaitzakPanela;
	private KlasifikazioaMetodo klasifikazioaPanela;
	PartiduakMetodo partiduakPanela;
	FitxaketakMetodo fitxaketakPanela;
	TaldeakMetodo taldeakPanela;
	private ErabiltzaileaKudeatu erabiltzailePanela;
	private JokalariakGehituMetodo jokalariakGehituPanela;
	private TaldeakGehituMetodo taldeakGehituPanela;

	private Color urdina;
	private Erabiltzailea erabiltzailea;

	/**
	 * Main klasearen eraikitzailea.
	 * 
	 * @param erabiltzailea Saioa hasi duen erabiltzailea.
	 */
	public Main(Erabiltzailea erabiltzailea) {
		this.erabiltzailea = erabiltzailea;

		try {
			// Leihoa konfiguratu
			setTitle("Boleibol Federazioa - 3. Taldea - Sistema");
			setSize(1000, 600);
			setLocationRelativeTo(null);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			urdina = new Color(0, 70, 160);

			panelNagusia = new JPanel(new BorderLayout());
			panelNagusia.setBackground(urdina);
			setContentPane(panelNagusia);

			// Fitxen panela sortu
			fitxaPanela = new JTabbedPane();
			fitxaPanela.setFont(new Font("Arial", Font.BOLD, 14));

			try {
				// ===== PANEL KOMUNAK (guztientzat) =====
				taldeakPanela = new TaldeakMetodo(urdina);
				taldeakPanela.setErabiltzailea(erabiltzailea);
				taldeakPanela.eraikiPanela();

				klasifikazioaPanela = new KlasifikazioaMetodo(urdina, erabiltzailea.isAdmin());
				emaitzakPanela = new EmaitzakMetodo(urdina);

				// ===== EPAILEA PANELAK =====
				// Epaileek partidak sartzeko baimena dute
				if (erabiltzailea.partidakSartuDezake()) {
					partiduakPanela = new PartiduakMetodo(urdina, this);
					fitxaPanela.addTab("Partiduak", partiduakPanela.getPanela()); 
				}

				// Panel komunak gehitu (después de Partiduak)
				fitxaPanela.addTab("Klasifikazioa", klasifikazioaPanela.getPanela()); 
				fitxaPanela.addTab("Emaitzak", emaitzakPanela.getPanela());
				fitxaPanela.addTab("Taldeak", taldeakPanela.getPanela()); 

				// ===== ADMIN PANELAK =====
				if (erabiltzailea.isAdmin()) {
					fitxaketakPanela = new FitxaketakMetodo(urdina);
					jokalariakGehituPanela = new JokalariakGehituMetodo(urdina);
					taldeakGehituPanela = new TaldeakGehituMetodo(urdina);
					erabiltzailePanela = new ErabiltzaileaKudeatu(urdina);

					fitxaPanela.addTab("Fitxaketak", fitxaketakPanela.getPanela());
					fitxaPanela.addTab("Jokalariak gehitu", jokalariakGehituPanela.getPanela());
					fitxaPanela.addTab("Taldeak gehitu", taldeakGehituPanela.getPanela());
					fitxaPanela.addTab("Erabiltzaileak kudeatu", erabiltzailePanela);
				}

			} catch (Exception e) {
				LoggerUtil.log("ERROR panelak sortzerakoan: " + e.getMessage());
				JOptionPane.showMessageDialog(this, "Errorea interfazearen osagaiak kargatzerakoan: " + e.getMessage(),
						"Errorea", JOptionPane.ERROR_MESSAGE);
			}

			// ===== FITXA ALDATZEAN DATUAK EGUNERATU =====
			fitxaPanela.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					String titulua = fitxaPanela.getTitleAt(fitxaPanela.getSelectedIndex());

					if (titulua.equals("Klasifikazioa") && klasifikazioaPanela != null) {
						klasifikazioaPanela.eguneratuDenboraldiak();
						klasifikazioaPanela.eguneratuTaula();
					} else if (titulua.equals("Emaitzak") && emaitzakPanela != null) {
						emaitzakPanela.eguneratuDenboraldiak();
						emaitzakPanela.eguneratuTaula();
					} else if (titulua.equals("Taldeak") && taldeakPanela != null) {
						taldeakPanela.eguneratuPanela();
					} else if (titulua.equals("Fitxaketak") && fitxaketakPanela != null) {
						fitxaketakPanela.eguneratuEgoera();
					} else if (titulua.equals("Partiduak") && partiduakPanela != null) {
						partiduakPanela.eguneratuTaldeak();
					}
				}
			});

			panelNagusia.add(fitxaPanela, BorderLayout.CENTER);
			eguneratuPestanenEgoera();

			LoggerUtil.log("Aplikazio nagusia ireki da - Erabiltzailea: " + erabiltzailea.getErabiltzailea() + " ("
					+ erabiltzailea.getRola() + ")");

		} catch (Exception e) {
			LoggerUtil.log("ERROR aplikazio nagusia hasieratzerakoan: " + e.getMessage());
			JOptionPane.showMessageDialog(null, "Errorea aplikazio nagusia hasieratzerakoan: " + e.getMessage(),
					"Errorea Larria", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Klasifikazioa eta Emaitzak panelak eguneratzen ditu.
	 */
	public void eguneratuDena() {
		if (klasifikazioaPanela != null)
			klasifikazioaPanela.eguneratuTaula();
		if (emaitzakPanela != null)
			emaitzakPanela.eguneratuTaula();
	}

	/**
	 * Fitxa guztiak gaituta mantentzen ditu.
	 */
	public void eguneratuPestanenEgoera() {
		for (int i = 0; i < fitxaPanela.getTabCount(); i++) {
			fitxaPanela.setEnabledAt(i, true);
		}
	}

	/**
	 * Aplikazioa abiarazteko metodoa.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new Login().setVisible(true));
	}
}