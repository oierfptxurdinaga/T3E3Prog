package view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import model.*;
import dao.DenboraldiaDAO;
import pojos.Denboraldia;
import pojos.TaldearenKlasifikazioa;

public class KlasifikazioaMetodo implements ActionListener, MouseListener {

	private JPanel panela;

	private JComboBox<Denboraldia> denboraldiaCombo; // Denboraldia aukeratzeko desplegablea
	private JTable klasifikazioaTaula; // Klasifikazioa erakusteko taula
	private DefaultTableModel taulaModeloa; // Taularen datuen modeloa

	private JButton gordeBotoia;
	private JButton kargatuBotoia;
	private JButton amaituDenboraldiaBotoia;
	private JButton saioaAmaituBotoia;

	private Klasifikazioa klasifikazioaService;
	private DenboraldiaDAO denboraldiaDAO;

	private JLabel titulua;

	private DefaultTableCellRenderer zentratu;
	private JScrollPane korritzePanela;
	private JFrame frame;

	private PrintWriter logger;

	public KlasifikazioaMetodo(Color urdina) {

		// Zerbitzuak hasieratu
		klasifikazioaService = new Klasifikazioa();
		denboraldiaDAO = new DenboraldiaDAO();

		// Panel nagusia sortu
		panela = new JPanel(null);
		panela.setBackground(urdina);

		// Izenburua
		titulua = new JLabel("KLASIFIKAZIOA");
		titulua.setForeground(Color.WHITE);
		titulua.setFont(new Font("Arial", Font.BOLD, 40));
		titulua.setBounds(320, 20, 400, 50);
		panela.add(titulua);

		// Denboraldiak aukeratzeko comboa
		denboraldiaCombo = new JComboBox<>();
		denboraldiakKargatu(); // Deitu denboraldiak kargatu metodoa
		denboraldiaCombo.setFont(new Font("Arial", Font.BOLD, 18));
		denboraldiaCombo.setBounds(100, 90, 200, 40);
		denboraldiaCombo.addActionListener(this); // Botoiari funtzioa emateko

		panela.add(denboraldiaCombo);

		// Denboraldia amaitzeko botoia
		amaituDenboraldiaBotoia = new JButton("Amaitu Denboraldia");
		amaituDenboraldiaBotoia.setFont(new Font("Arial", Font.BOLD, 16));
		amaituDenboraldiaBotoia.setBackground(Color.ORANGE);
		amaituDenboraldiaBotoia.setForeground(Color.BLACK);
		amaituDenboraldiaBotoia.setBounds(350, 90, 200, 40);
		amaituDenboraldiaBotoia.addActionListener(this); // Botoiari funtzioa emateko

		panela.add(amaituDenboraldiaBotoia);

		// Taularen zutabeak
		String[] zutabeak = { "Posizioa", "Taldea", "PJ", "PG", "PP", "Puntuak", "SI", "SG", "SD" };

		// Taularen modeloa (editagarria ez dena)
		taulaModeloa = new DefaultTableModel(zutabeak, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		// Taula sortu
		klasifikazioaTaula = new JTable(taulaModeloa);
		klasifikazioaTaula.setFont(new Font("Arial", Font.PLAIN, 14));
		klasifikazioaTaula.setFillsViewportHeight(true);
		klasifikazioaTaula.setShowGrid(true);
		klasifikazioaTaula.setGridColor(Color.LIGHT_GRAY);

		// Testua zentratu zutabe guztietan
		zentratu = new DefaultTableCellRenderer();
		zentratu.setHorizontalAlignment(JLabel.CENTER);
		for (int i = 0; i < klasifikazioaTaula.getColumnCount(); i++) {
			klasifikazioaTaula.getColumnModel().getColumn(i).setCellRenderer(zentratu);
		}

		// Scroll panela (korritzeko)
		korritzePanela = new JScrollPane(klasifikazioaTaula);
		korritzePanela.setBounds(100, 150, 700, 250);
		panela.add(korritzePanela);

		// Klasifikazioa gordetzeko botoia (oraindik ez inplementatuta)
		gordeBotoia = new JButton("Gorde Klasifikazioa");
		gordeBotoia.setFont(new Font("Arial", Font.BOLD, 16));
		gordeBotoia.setBackground(new Color(0, 150, 0));
		gordeBotoia.setForeground(Color.WHITE);
		gordeBotoia.setBounds(200, 420, 200, 40);
		gordeBotoia.addActionListener(this); // Botoiari funtzioa emateko

		panela.add(gordeBotoia);

		// Klasifikazioa kargatzeko botoia
		kargatuBotoia = new JButton("Kargatu Klasifikazioa");
		kargatuBotoia.setFont(new Font("Arial", Font.BOLD, 16));
		kargatuBotoia.setBackground(new Color(0, 100, 200));
		kargatuBotoia.setForeground(Color.WHITE);
		kargatuBotoia.setBounds(450, 420, 200, 40);
		kargatuBotoia.addActionListener(this); // Botoiari funtzioa emateko

		panela.add(kargatuBotoia);

		// Saioa amaitzeko botoia
		saioaAmaituBotoia = new JButton("Saioa amaitu");
		saioaAmaituBotoia.setFont(new Font("Arial", Font.BOLD, 18));
		saioaAmaituBotoia.setBackground(Color.RED);
		saioaAmaituBotoia.setForeground(Color.WHITE);
		saioaAmaituBotoia.setBounds(700, 480, 170, 40);
		saioaAmaituBotoia.addActionListener(this); // Botoiari funtzioa emateko

		panela.add(saioaAmaituBotoia);

		// Hasieran lehenengo denboraldia aukeratu
		if (denboraldiaCombo.getItemCount() > 0) {
			denboraldiaCombo.setSelectedIndex(0);
		}
		
		 initLogger();
	}

	/**
	 * Logger sistema hasieratzen du, log.txt fitxategian gordetzeko.
	 */
	private void initLogger() {
		try {
			File logFile = new File("log.txt");
			FileWriter fw = new FileWriter(logFile, true);
			logger = new PrintWriter(fw);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(panela, "Errorea logger hasterakoan: " + e.getMessage(), "Errorea",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Mezu bat idazten du log fitxategian.
	 * 
	 * @param 
 Log-ean idazteko mezua
	 */
	private void log(String izena, String mezua) {
		if (logger != null) {
			LocalDateTime now = LocalDateTime.now();
			String timestamp = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			logger.println("[" + timestamp + "]: " + izena + " " + mezua);
			logger.flush();
		}
	}

	// Denboraldi guztiak kargatu combora
	private void denboraldiakKargatu() {
		List<Denboraldia> denboraldiak = denboraldiaDAO.denboraldiakAtera();
		for (Denboraldia d : denboraldiak) {
			denboraldiaCombo.addItem(d);
		}
	}

	// Denboraldiak eguneratu (berkargatu)
	public void eguneratuDenboraldiak() {
		Denboraldia selected = (Denboraldia) denboraldiaCombo.getSelectedItem();
		int selectedId = (selected != null) ? selected.getDenboraldiaKod() : -1;

		denboraldiaCombo.removeAllItems();
		denboraldiakKargatu();

		// Aurreko hautaketa berreskuratzen saiatu
		if (selectedId != -1) {
			for (int i = 0; i < denboraldiaCombo.getItemCount(); i++) {
				Denboraldia d = denboraldiaCombo.getItemAt(i);
				if (d.getDenboraldiaKod() == selectedId) {
					denboraldiaCombo.setSelectedIndex(i);
					return;
				}
			}
		}

		// Bestela, lehenengoa aukeratu
		if (denboraldiaCombo.getItemCount() > 0) {
			denboraldiaCombo.setSelectedIndex(0);
		}
	}

	// Klasifikazioa kalkulatu eta taulan erakutsi
	private void kargatuKlasifikazioa() throws Exception {
		taulaModeloa.setRowCount(0); // Taula garbitu

		Denboraldia denboraldia = (Denboraldia) denboraldiaCombo.getSelectedItem();
		if (denboraldia == null)
			return;

		List<TaldearenKlasifikazioa> klasifikazioa = klasifikazioaService
				.lortuKlasifikazioaDenboraldian(denboraldia.getDenboraldiaKod());

		int pos = 1;

		// Taulan lerroak gehitu
		for (TaldearenKlasifikazioa tk : klasifikazioa) {
			Object[] row = { pos++, tk.getTaldea().getIzena(), tk.getPartidaJokatuak(), tk.getPartidaIrabaziak(),
					tk.getPartidaGalduak(), tk.getPuntuak(), tk.getSetakIrabaziak(), tk.getSetakGalduak(),
					tk.getSetDiferentzia() };
			taulaModeloa.addRow(row);
		}
	}

	// Denboraldia amaitzeko funtzioa (oraindik sinplea)
	private void amaituDenboraldia() throws Exception {
		JOptionPane.showMessageDialog(panela, "Denboraldia amaituta (funtzioa ez dago guztiz inplementatuta)");
	}

	// Taula berriro kargatu
	public void eguneratuTaula() {
		try {
			kargatuKlasifikazioa();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(panela, "Errorea taula eguneratzerakoan: " + e.getMessage(), "Errorea",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	// Metodo honek aukeratutako denboraldia klasifikazioa xml-batean gordeko da
	private void gordeXML() {
		try {
			// XML dokumentua sortu
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.newDocument();

			// Root nodoa
			Element root = doc.createElement("klasifikazioa");
			doc.appendChild(root);

			// Taulako lerro guztiak hartu
			for (int i = 0; i < taulaModeloa.getRowCount(); i++) {

				Element taldea = doc.createElement("taldea");

				// Zutabe bakoitza XML etiketa bezala
				Element pos = doc.createElement("posizioa");
				pos.appendChild(doc.createTextNode(taulaModeloa.getValueAt(i, 0).toString()));
				taldea.appendChild(pos);

				Element izena = doc.createElement("izena");
				izena.appendChild(doc.createTextNode(taulaModeloa.getValueAt(i, 1).toString()));
				taldea.appendChild(izena);

				Element pj = doc.createElement("pj");
				pj.appendChild(doc.createTextNode(taulaModeloa.getValueAt(i, 2).toString()));
				taldea.appendChild(pj);

				Element pg = doc.createElement("pg");
				pg.appendChild(doc.createTextNode(taulaModeloa.getValueAt(i, 3).toString()));
				taldea.appendChild(pg);

				Element pp = doc.createElement("pp");
				pp.appendChild(doc.createTextNode(taulaModeloa.getValueAt(i, 4).toString()));
				taldea.appendChild(pp);

				Element puntuak = doc.createElement("puntuak");
				puntuak.appendChild(doc.createTextNode(taulaModeloa.getValueAt(i, 5).toString()));
				taldea.appendChild(puntuak);

				Element si = doc.createElement("si");
				si.appendChild(doc.createTextNode(taulaModeloa.getValueAt(i, 6).toString()));
				taldea.appendChild(si);

				Element sg = doc.createElement("sg");
				sg.appendChild(doc.createTextNode(taulaModeloa.getValueAt(i, 7).toString()));
				taldea.appendChild(sg);

				Element sd = doc.createElement("sd");
				sd.appendChild(doc.createTextNode(taulaModeloa.getValueAt(i, 8).toString()));
				taldea.appendChild(sd);

				root.appendChild(taldea);
			}

			// XML fitxategian gorde
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("klasifikazioa.xml"));

			transformer.transform(source, result);

			JOptionPane.showMessageDialog(panela, "XML ondo gorde da!");
			log("XML sortu |", "XML fitxategia sortu da");

		} catch (Exception e) {
			JOptionPane.showMessageDialog(panela, "Errorea XML gordetzean: " + e.getMessage(), "Errorea",
					JOptionPane.ERROR_MESSAGE);
			log("[XML]", "Errorea XML sortzean: " + e.getMessage());
			e.printStackTrace();

		}
	}

	// Panel nagusia lortzeko metodoa
	public JPanel getPanela() {
		return panela;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();

		// Denboraldia aldatzean klasifikazioa kargatu
		if (src == denboraldiaCombo) {
			try {
				kargatuKlasifikazioa();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(panela, "Errorea klasifikazioa kargatzerakoan: " + ex.getMessage(),
						"Errorea", JOptionPane.ERROR_MESSAGE);
				ex.printStackTrace();
			}

		}
		// Klik egitean denboraldia amaitzeko funtzioa
		if (src == amaituDenboraldiaBotoia) {
			try {
				amaituDenboraldia();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(panela, "Errorea denboraldia amaitzerakoan: " + ex.getMessage(),
						"Errorea", JOptionPane.ERROR_MESSAGE);
				ex.printStackTrace();
			}

		}

		if (src == kargatuBotoia) {
			try {
				kargatuKlasifikazioa();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(panela, "Errorea klasifikazioa kargatzerakoan: " + ex.getMessage(),
						"Errorea", JOptionPane.ERROR_MESSAGE);
			}
		}

		if (src == gordeBotoia) {
			gordeXML();

		}

		if (src == saioaAmaituBotoia) {
			// Login pantaila berriro ireki
			SwingUtilities.invokeLater(() -> new Login().setVisible(true));

			// Uneko leihoa itxi
			frame = (JFrame) SwingUtilities.getWindowAncestor((Component) e.getSource());
			frame.dispose();

		}

	}

	/**
	 * Logger sistema itxitzen du.
	 */
	public void closeLogger() {
		if (logger != null) {
			logger.close();
		}
	}

	/**
	 * Leihoa itxi aurretik logger sistema itxitzen du.
	 */
	public void dispose() {
		closeLogger();
	}
}