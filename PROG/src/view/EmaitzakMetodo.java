package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import model.*;
import dao.PartidaDAO;
import dao.DenboraldiaDAO;
import dao.JardunaldiaDAO;
import pojos.Partida;
import pojos.Denboraldia;
import pojos.Jardunaldia;

public class EmaitzakMetodo implements ActionListener {

	private JPanel panela;
	private Color urdina;
	private JComboBox<Denboraldia> denboraldiaCombo;
	private JComboBox<Object> jardunaldiaCombo; // Jardunaldia edo String ("Guztiak")
	private JTable taula;
	private DefaultTableModel taulaModeloa;

	private JLabel titulua;
	private JLabel denboraldiaEtiketa;
	private JLabel jardunaldiaEtiketa;

	private JScrollPane korritzePanela;

	private JButton saioaAmaituBotoia;

	private JFrame frame;

	private PartidaDAO partidaDAO;
	private DenboraldiaDAO denboraldiaDAO;
	private JardunaldiaDAO jardunaldiaDAO;

	public EmaitzakMetodo(Color urdina) {
		this.urdina = urdina;
		partidaDAO = new PartidaDAO();
		denboraldiaDAO = new DenboraldiaDAO();
		jardunaldiaDAO = new JardunaldiaDAO();

		panela = new JPanel(null);
		panela.setBackground(urdina);

		titulua = new JLabel("EMAITZAK", SwingConstants.CENTER);
		titulua.setForeground(Color.WHITE);
		titulua.setFont(new Font("Arial", Font.BOLD, 32));
		titulua.setBounds(0, 40, 900, 40);
		panela.add(titulua);

		denboraldiaEtiketa = new JLabel("Denboraldia:");
		denboraldiaEtiketa.setForeground(Color.WHITE);
		denboraldiaEtiketa.setFont(new Font("Arial", Font.BOLD, 16));
		denboraldiaEtiketa.setBounds(100, 120, 150, 30);
		panela.add(denboraldiaEtiketa);

		denboraldiaCombo = new JComboBox<>();
		denboraldiaCombo.addItem(new Denboraldia(0, "Guztiak", null, null, false, null, false));
		List<Denboraldia> denboraldiak = denboraldiaDAO.denboraldiakAtera();
		for (Denboraldia d : denboraldiak) {
			denboraldiaCombo.addItem(d);
		}
		denboraldiaCombo.setBounds(100, 160, 180, 35);
		denboraldiaCombo.addActionListener(this);
		panela.add(denboraldiaCombo);

		jardunaldiaEtiketa = new JLabel("Jardunaldia:");
		jardunaldiaEtiketa.setForeground(Color.WHITE);
		jardunaldiaEtiketa.setFont(new Font("Arial", Font.BOLD, 16));
		jardunaldiaEtiketa.setBounds(300, 120, 150, 30);
		panela.add(jardunaldiaEtiketa);

		jardunaldiaCombo = new JComboBox<>();
		jardunaldiaCombo.addItem("Guztiak");
		jardunaldiaCombo.setBounds(300, 160, 180, 35);
		jardunaldiaCombo.addActionListener(this);
		panela.add(jardunaldiaCombo);

		String[] zutabeak = { "Talde lokala", "Setak lokala", "Setak kanpokoa", "Talde kanpokoa", "Jardunaldia",
				"Denboraldia" };
		taulaModeloa = new DefaultTableModel(zutabeak, 0) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		taula = new JTable(taulaModeloa);
		taula.setRowHeight(35);
		taula.setFont(new Font("Arial", Font.PLAIN, 14));
		taula.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

		korritzePanela = new JScrollPane(taula);
		korritzePanela.setBounds(100, 230, 700, 220);
		panela.add(korritzePanela);

		saioaAmaituBotoia = new JButton("Saioa amaitu");
		saioaAmaituBotoia.setFont(new Font("Arial", Font.BOLD, 18));
		saioaAmaituBotoia.setBackground(Color.RED);
		saioaAmaituBotoia.setForeground(Color.WHITE);
		saioaAmaituBotoia.setBounds(700, 480, 170, 40);
		saioaAmaituBotoia.addActionListener(this);
		panela.add(saioaAmaituBotoia);

		// Hasierako jardunaldiak kargatu (lehen denboraldia hautatuta badago)
		if (denboraldiaCombo.getItemCount() > 0) {
			denboraldiaCombo.setSelectedIndex(0);
			kargatuJardunaldiak();
		}
		kargatuEmaitzak();
	}

	private void kargatuJardunaldiak() {
		jardunaldiaCombo.removeAllItems();
		jardunaldiaCombo.addItem("Guztiak");
		Denboraldia selected = (Denboraldia) denboraldiaCombo.getSelectedItem();
		if (selected != null && selected.getDenboraldiaKod() != 0) {
			List<Jardunaldia> jardunaldiak = jardunaldiaDAO.lostuJardunaldiDenboraldiBidez(selected.getDenboraldiaKod());
			jardunaldiak.sort((a, b) -> a.getHasieraData().compareTo(b.getHasieraData()));
			int zenbakia = 1;
			for (Jardunaldia j : jardunaldiak) {
				final int num = zenbakia++;
				Jardunaldia jWrapper = new Jardunaldia(j.getJardunaldiKod(), j.getHasieraData(), j.getAmaieraData()) {
					@Override
					public String toString() {
						return "Jardunaldia " + num;
					}
				};
				jardunaldiaCombo.addItem(jWrapper);
			}
		}
	}

	private void kargatuEmaitzak() {
		taulaModeloa.setRowCount(0);
		List<Partida> partidak = partidaDAO.partidaGuztiak();

		Denboraldia selectedDenb = (Denboraldia) denboraldiaCombo.getSelectedItem();
		Object selectedJard = jardunaldiaCombo.getSelectedItem();

		for (Partida p : partidak) {
			if (p.getEtxekoTaldea() == null || p.getKanpokoTaldea() == null)
				continue;

			// Denboraldiaren iragazketa
			if (selectedDenb != null && selectedDenb.getDenboraldiaKod() != 0) {
				// Partidak denboraldi honetakoa den egiaztatu (jardunaldiaren bidez)
				if (p.getJardunaldia() == null)
					continue;
				// Egiaztatu ea jardunaldi hau denboraldi honi lotuta dagoen (DAO bidez
				// egiaztatu genezake, baina errazago: partida lortzeko erabili dugun
				// partidaGuztiak() metodoak ez du denboraldia ekartzen; hobe
				// partidaLortuDenboraldiBitartez erabiltzea. Hemen, sinpleago, iragazketa
				// egingo dugu jardunaldia zerrendan badagoen begiratuz.)
				// Eguneraketa: hobe da partidaDAO.partidaLortuDenboraldiBitartez() erabiltzea.
				// Baina kodea argiago mantentzeko, hemen iragazketa egiten dugu
				// jardunaldiaren IDa konparatuz kargatutakoekin.
				List<Jardunaldia> denbJard = jardunaldiaDAO.lostuJardunaldiDenboraldiBidez(selectedDenb.getDenboraldiaKod());
				boolean found = false;
				for (Jardunaldia j : denbJard) {
					if (j.getJardunaldiKod() == p.getJardunaldia().getJardunaldiKod()) {
						found = true;
						break;
					}
				}
				if (!found)
					continue;
			}

			// Jardunaldiaren iragazketa
			if (selectedJard != null && !selectedJard.equals("Guztiak")) {
				Jardunaldia jSel = (Jardunaldia) selectedJard;
				if (p.getJardunaldia() == null || p.getJardunaldia().getJardunaldiKod() != jSel.getJardunaldiKod())
					continue;
			}

			String[] setak = p.getEmaitza().split("-");
			String setakLokala = setak.length > 0 ? setak[0] : "";
			String setakKanpokoa = setak.length > 1 ? setak[1] : "";

			// Denboraldiaren izena lortu (partidak ez du denboraldia zuzenean, baina
			// jardunaldiaren bitartez lor genezake. Sinplifikatzeko, utzi hutsik edo
			// kalkulatu)
			String denbIzena = "";
			if (selectedDenb != null && selectedDenb.getDenboraldiaKod() != 0) {
				denbIzena = selectedDenb.getIzena();
			} else {
				// Saiatu jardunaldiari lotutako denboraldia aurkitzen (aukera bat)
				if (p.getJardunaldia() != null) {
					List<Denboraldia> denbGuztiak = denboraldiaDAO.denboraldiakAtera();
					for (Denboraldia d : denbGuztiak) {
						List<Jardunaldia> jardList = jardunaldiaDAO.lostuJardunaldiDenboraldiBidez(d.getDenboraldiaKod());
						for (Jardunaldia j : jardList) {
							if (j.getJardunaldiKod() == p.getJardunaldia().getJardunaldiKod()) {
								denbIzena = d.getIzena();
								break;
							}
						}
						if (!denbIzena.isEmpty())
							break;
					}
				}
			}

			Object[] row = {
				p.getEtxekoTaldea().getIzena(),
				setakLokala,
				setakKanpokoa,
				p.getKanpokoTaldea().getIzena(),
				p.getJardunaldia() != null ? p.getJardunaldia().toString() : "",
				denbIzena
			};
			taulaModeloa.addRow(row);
		}
	}

	public void eguneratuTaula() {
		kargatuEmaitzak();
	}

	public JPanel getPanela() {
		return panela;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		if (src == denboraldiaCombo) {
			kargatuJardunaldiak();
			kargatuEmaitzak();
		} else if (src == jardunaldiaCombo) {
			kargatuEmaitzak();
		} else if (src == saioaAmaituBotoia) {
			SwingUtilities.invokeLater(() -> new Login().setVisible(true));
			frame = (JFrame) SwingUtilities.getWindowAncestor((Component) e.getSource());
			frame.dispose();
		}
	}
}