package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Vector;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import dao.ErabiltzaileakDAO;
import pojos.Erabiltzailea;

/**
 * Ikuspegiaren (View) geruzako klasea.
 * Erabiltzaileak kudeatzeko pantaila (panela) ordezkatzen du.
 * Hemen datu-baseko erabiltzaileak taula batean erakusten dira, 
 * administratzaileak ez direnak ezabatzeko aukera ematen da eta 
 * ekintza horien erregistroa (log) testu-fitxategi batean gordetzen da.
 */
public class ErabiltzaileaKudeatu extends JPanel implements ActionListener {

	private JTable taula;
	private JScrollPane scroll;
	private DefaultTableModel dtm;

	private JLabel lblErabiltzaileKop;
	private JLabel tituloa;
	private JLabel lblErabiltzaileak;

	private Vector<String> zutabeak;
	private Vector<Vector<Object>> datuakTaula;

	private JPanel centerPanel;
	private JPanel bottomPanel;
	private JPanel leftPanel;
	private JPanel rightPanel;

	private JButton btnEzabatu;
	private JButton btnIrten;
	public Component getPanela;

	private PrintWriter logger;

	/**
	 * ErabiltzaileaKudeatu klasearen eraikitzailea.
	 * Interfaze grafikoa eraikitzen du, erabiltzaileen datuak kargatzen ditu 
	 * eta log fitxategia prestatzen du.
	 * * @param urdina Aplikazioaren diseinu-patroiari jarraitzen dion atzeko planoaren kolorea.
	 */
	public ErabiltzaileaKudeatu(Color urdina) {
		setLayout(new BorderLayout());
		setBackground(urdina);

		tituloa = new JLabel("ERABILTZAILEAREN KUDEAKETA");
		tituloa.setForeground(Color.WHITE);
		tituloa.setFont(new Font("Arial", Font.BOLD, 40));
		tituloa.setHorizontalAlignment(SwingConstants.CENTER);
		tituloa.setBorder(javax.swing.BorderFactory.createEmptyBorder(40, 0, 20, 0));
		add(tituloa, BorderLayout.NORTH);

		// Taularen zutabeak
		zutabeak = new Vector<>();
		zutabeak.add("Erabiltzailea");
		zutabeak.add("Rola");

		datuakTaula = new Vector<>();

		// Taularen modeloa
		dtm = new DefaultTableModel(datuakTaula, zutabeak);

		// Taula
		taula = new JTable(dtm);
		taula.setFont(new Font("Arial", Font.BOLD, 16));
		taula.setRowHeight(30);

		scroll = new JScrollPane(taula);
		scroll.setPreferredSize(new Dimension(700, 350));

		centerPanel = new JPanel();
		centerPanel.setBackground(urdina);
		centerPanel.setLayout(new java.awt.GridBagLayout());

		centerPanel.add(scroll);

		add(centerPanel, BorderLayout.CENTER);

		bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.setBackground(urdina);
		bottomPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 40, 20, 40));

		leftPanel = new JPanel();
		leftPanel.setBackground(urdina);

		lblErabiltzaileak = new JLabel("Erabiltzaileak:");
		lblErabiltzaileak.setFont(new Font("Arial", Font.BOLD, 18));
		lblErabiltzaileak.setForeground(Color.WHITE);

		lblErabiltzaileKop = new JLabel("0");
		lblErabiltzaileKop.setFont(new Font("Arial", Font.BOLD, 18));
		lblErabiltzaileKop.setForeground(Color.WHITE);

		leftPanel.add(lblErabiltzaileak);
		leftPanel.add(lblErabiltzaileKop);

		rightPanel = new JPanel();
		rightPanel.setBackground(urdina);

		btnEzabatu = new JButton("Ezabatu");
		btnEzabatu.setFont(new Font("Arial", Font.BOLD, 18));
		btnEzabatu.setBackground(Color.WHITE);
		btnEzabatu.setForeground(new Color(0, 0, 128));
		btnEzabatu.addActionListener(this); // Botoiari funtzioa emateko

		btnIrten = new JButton("Irten");
		btnIrten.setFont(new Font("Arial", Font.BOLD, 18));
		btnIrten.setBackground(Color.RED);
		btnIrten.setForeground(Color.BLACK);
		btnIrten.addActionListener(this); // Botoiari funtzioa emateko

		rightPanel.add(btnEzabatu);
		rightPanel.add(btnIrten);

		bottomPanel.add(leftPanel, BorderLayout.WEST);
		bottomPanel.add(rightPanel, BorderLayout.EAST);

		add(bottomPanel, BorderLayout.SOUTH);

		// Metodoari deitu
		kargatuErabiltzaileak();
		 initLogger();
	}

	/**
	 * Log fitxategia ("log.txt") irekitzen edo sortzen du, 
	 * administratzaileak egiten dituen ezabaketak bertan erregistratzeko.
	 */
	private void initLogger() {
		try {
			File logFile = new File("log.txt");
			FileWriter fw = new FileWriter(logFile, true); // true para añadir al archivo
			logger = new PrintWriter(fw);
//	         logger.println("Logger iniciado.");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Errorea logger hasterakoan: " + e.getMessage(), "Errorea",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Mezu bat idazten du log fitxategian une horretako data eta orduarekin.
	 * * @param izena Ekintzari lotutako erabiltzailearen izena (edo mezua).
	 * @param message Ekintzaren xehetasuna (adibidez, erabiltzailearen rola).
	 */
	private void log(String izena, String message) {
		if (logger != null) {
			LocalDateTime now = LocalDateTime.now();
			String timestamp = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			logger.println("[" + timestamp + "]: " + izena + " " + message);
			logger.flush();
		}
	}

	/**
	 * Datu-basetik erabiltzaile guztiak eskuratu eta GUI-ko taulan erakusten ditu.
	 * Halaber, bistaratutako erabiltzaile kopuru osoa eguneratzen du.
	 */
	private void kargatuErabiltzaileak() {

		ErabiltzaileakDAO dao = new ErabiltzaileakDAO();
		List<Erabiltzailea> lista = dao.getErabiltzaileak();

		dtm.setRowCount(0); // Taula garbitu

		for (Erabiltzailea e : lista) {
			Vector<Object> fila = new Vector<>();
			fila.add(e.getErabiltzailea());
			fila.add(e.getRola());
			dtm.addRow(fila);
		}

		// Zenbat erabiltzaile dauden programan ikusteko Labela
		lblErabiltzaileKop.setText(String.valueOf(lista.size()));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		if (src == btnEzabatu) {

			ErabiltzaileakDAO dao = new ErabiltzaileakDAO();

			int fila = taula.getSelectedRow();

			if (fila == -1) {
				JOptionPane.showMessageDialog(this, "Mesedez, erabiltzaile bat hautatu.", "Kontuz",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			String erabiltzailea = dtm.getValueAt(fila, 0).toString();
			String rola = dtm.getValueAt(fila, 1).toString();

			// Balidatzen du admin bat ez ezabatzea
			if ("ADMIN".equalsIgnoreCase(rola)) {
				JOptionPane.showMessageDialog(this, "Errorea, Admin erabiltzaileak ezin dira ezabatu.", "Errorea", JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Lehengo galdetzen dizu ea ziur zauden erabiltzailea ezabatu nahi duzun
			int confirm = JOptionPane.showConfirmDialog(this, "Ziur zaude erabiltzailea ezabatu nahi duzula?",
					"Egiaztapena", JOptionPane.YES_NO_OPTION);

			if (confirm != JOptionPane.YES_OPTION) {
				// Erantzuna ez denean ez du ezbatuko eta metodotik aterako da
				return;
			}

			// ErabiltzaileakDAO-ko meotodoa deitu
			boolean borrado = dao.ezabatuErabiltzailea(erabiltzailea);

			if (borrado) {
				// Taula eta zenbat erabiltzaile dauden eguneratuko da
				dtm.removeRow(fila);
				lblErabiltzaileKop.setText(String.valueOf(dtm.getRowCount()));

				// Dena ondo badagoen
				JOptionPane.showMessageDialog(this, "Erabiltzailea ondo ezabatu da.", "Ondo",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				// Ze o zer gertatu denenan
				JOptionPane.showMessageDialog(this, "Errorea erabiltzailea ezabatzean.", "Errorea",
						JOptionPane.ERROR_MESSAGE);
			}
			// Log fitxategira sartzerakoan, mezu hau ikusiko da
			log(erabiltzailea + " ezabatu da.", rola);

		}
		if (src == btnIrten) {
			System.exit(-1); // Programatik irteten da
		}

	}

	/**
	 * Log fitxategiaren idazketa-korrontea modu seguruan ixten du.
	 */
	public void closeLogger() {
		if (logger != null) {
//        logger.println("Logger cerrado.");
			logger.close();
		}
	}
	
	public void dispose() {
		closeLogger();
		super.disable();;
	}
}