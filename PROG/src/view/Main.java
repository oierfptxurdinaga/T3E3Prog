package view;

import javax.swing.*;
import java.awt.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import dao.DenboraldiaDAO;
import pojos.Denboraldia;
import pojos.Erabiltzailea;

/**
 * Ikuspegiaren (View) geruzako klase nagusia.
 * Erabiltzaileak saioa hasi ondoren agertzen den aplikazioaren leiho nagusia (JFrame) ordezkatzen du.
 * Fitxen bidezko (JTabbedPane) nabigazio-sistema bat darabil, eta saioa hasi duen 
 * erabiltzailearen rolaren arabera (adibidez, ADMIN bada) aukera eta pantaila gehiago erakusten ditu.
 */
public class Main extends JFrame {
	private JPanel panelNagusia;
	private JTabbedPane fitxaPanela;
	private EmaitzakMetodo emaitzakPanela;
	private KlasifikazioaMetodo klasifikazioaPanela;
	private PartiduakMetodo partiduakPanela;
	private FitxaketakMetodo fitxaketakPanela;
	private TaldeakMetodo taldeakPanela;
	private ErabiltzaileaKudeatu erabiltzailePanela;
	private Color urdina;

	// Panel gehigarriak ADMINentzat
	private JokalariakGehituMetodo jokalariakGehituPanela;
	private TaldeakGehituMetodo taldeakGehituPanela;
	private Erabiltzailea erabiltzailea;

	/**
	 * Main klasearen eraikitzailea.
	 * Leiho nagusiaren ezaugarriak (tamaina, kolorea, posizioa) ezartzen ditu, 
	 * aplikazioaren azpi-panel guztiak hasieratzen ditu eta fitxen (tab) egitura 
	 * sortzen du erabiltzailearen rolaren arabera.
	 * @param erabiltzailea Saioa hasi duen {@link Erabiltzailea} objektua, bere rola aztertu ahal izateko.
	 */
	public Main(Erabiltzailea erabiltzailea) {
		this.erabiltzailea = erabiltzailea;
		try {
			setTitle("Boleibol Federazioa - 3. Taldea - Sistema");
			setSize(1000, 600);
			setLocationRelativeTo(null);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			urdina = new Color(0, 70, 160);

			panelNagusia = new JPanel(new BorderLayout());
			panelNagusia.setBackground(urdina);
			setContentPane(panelNagusia);

			fitxaPanela = new JTabbedPane();
			fitxaPanela.setFont(new Font("Arial", Font.BOLD, 14));

			try {
				// Panel komunak sortu (rol guztientzat)
				taldeakPanela = new TaldeakMetodo(urdina);
				klasifikazioaPanela = new KlasifikazioaMetodo(urdina);
				emaitzakPanela = new EmaitzakMetodo(urdina);

				// ADMIN rolarentzako panel gehigarriak sortu
				if (erabiltzailea.getRola().equals("ADMIN")) {
					partiduakPanela = new PartiduakMetodo(urdina, this);
					fitxaketakPanela = new FitxaketakMetodo(urdina);
					jokalariakGehituPanela = new JokalariakGehituMetodo(urdina);
					taldeakGehituPanela = new TaldeakGehituMetodo(urdina);
					erabiltzailePanela = new ErabiltzaileaKudeatu(urdina);

					// Fitxak ordena zehatzean gehitu:
					// 1. Partiduak
					fitxaPanela.addTab("Partiduak", partiduakPanela.getPanela());
					// 2. Fitxaketak
					fitxaPanela.addTab("Fitxaketak", fitxaketakPanela.getPanela());
				}

				// Panel komunak gehitu (Taldeak, Klasifikazioa, Emaitzak)
				// Hauek beti agertuko dira, ADMIN zein erabiltzaile arruntentzat
				fitxaPanela.addTab("Taldeak", taldeakPanela.getPanela());
				fitxaPanela.addTab("Klasifikazioa", klasifikazioaPanela.getPanela());
				fitxaPanela.addTab("Emaitzak", emaitzakPanela.getPanela());

				// ADMIN panel gehigarriak jarraitu
				if (erabiltzailea.getRola().equals("ADMIN")) {
					fitxaPanela.addTab("Jokalariak gehitu", jokalariakGehituPanela.getPanela());
					fitxaPanela.addTab("Taldeak gehitu", taldeakGehituPanela.getPanela());
					fitxaPanela.addTab("Erabiltzaileak kudeatu", erabiltzailePanela);
				}

			} catch (Exception e) {
				System.err.println("Errorea panelak sortzerakoan: " + e.getMessage());
				JOptionPane.showMessageDialog(this, "Errorea interfazearen osagaiak kargatzerakoan: " + e.getMessage(),
						"Errorea", JOptionPane.ERROR_MESSAGE);
			}

			// Fitxa aldatzean datuak eguneratzeko entzulea
			fitxaPanela.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					int index = fitxaPanela.getSelectedIndex();
					String titulua = fitxaPanela.getTitleAt(index);
					
					if (titulua.equals("Klasifikazioa") && klasifikazioaPanela != null) {
						klasifikazioaPanela.eguneratuDenboraldiak();
						klasifikazioaPanela.eguneratuTaula();
					} else if (titulua.equals("Emaitzak") && emaitzakPanela != null) {
						emaitzakPanela.eguneratuTaula();
					}
				}
			});

			panelNagusia.add(fitxaPanela, BorderLayout.CENTER);

			// Fitxen gaitasuna eguneratu (Partiduak beti gaituta, Fitxaketak beti gaituta)
			eguneratuPestanenEgoera();

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Errorea aplikazio nagusia hasieratzerakoan: " + e.getMessage(),
					"Errorea Larria", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Aplikazioko panel nagusietako (Klasifikazioa eta Emaitzak) datuak eta taulak berritzen ditu.
	 * Datu-basean aldaketak egon badira informazioa eguneratuta agertzeko erabiltzen da.
	 */
	public void eguneratuDena() {
		if (klasifikazioaPanela != null)
			klasifikazioaPanela.eguneratuTaula();
		if (emaitzakPanela != null)
			emaitzakPanela.eguneratuTaula();
	}

	/**
	 * Fitxen gaitasuna eguneratzen du.
	 * Partiduak eta Fitxaketak beti gaituta egongo dira.
	 * Partida sartzeko edo fitxaketa egiteko balidazioa panel bakoitzean egiten da.
	 */
	public void eguneratuPestanenEgoera() {
		// Fitxa guztiak gaituta mantendu
		for (int i = 0; i < fitxaPanela.getTabCount(); i++) {
			fitxaPanela.setEnabledAt(i, true);
		}
	}

	/**
	 * Aplikazioa klase honetatik zuzenean abiarazteko metodoa. 
	 * Berez saioa hasteko leihoa (Login) irekitzen du.
	 * @param args Komando-lerroko argumentuak.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new Login().setVisible(true));
	}
}