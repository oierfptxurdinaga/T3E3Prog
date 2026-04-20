package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import dao.PartidaDAO;
import dao.DenboraldiaDAO;
import dao.JardunaldiaDAO;
import pojos.Partida;
import pojos.Denboraldia;
import pojos.Jardunaldia;
import util.LoggerUtil;
import util.LoggerUtil.DataAccessException;

/**
 * Emaitzak ikusteko panela kudeatzen duen klasea.
 */
public class EmaitzakMetodo implements ActionListener {

	private JPanel panela;
	private Color urdina;
	private JComboBox<Denboraldia> denboraldiaCombo;
	private JComboBox<Object> jardunaldiaCombo;
	private JTable taula;
	private DefaultTableModel taulaModeloa;

	private JLabel titulua;
	private JLabel denboraldiaEtiketa;
	private JLabel jardunaldiaEtiketa;

	private JScrollPane korritzePanela;
	private JButton saioaAmaituBotoia;

	private PartidaDAO partidaDAO;
	private DenboraldiaDAO denboraldiaDAO;
	private JardunaldiaDAO jardunaldiaDAO;

	/**
	 * EmaitzakMetodo klasearen eraikitzailea.
	 * 
	 * @param urdina Atzeko planoaren kolorea.
	 */
	public EmaitzakMetodo(Color urdina) {
		this.urdina = urdina;
		partidaDAO = new PartidaDAO();
		denboraldiaDAO = new DenboraldiaDAO();
		jardunaldiaDAO = new JardunaldiaDAO();

		// Panela konfiguratu
		panela = new JPanel(null);
		panela.setBackground(urdina);

		titulua = new JLabel("EMAITZAK", SwingConstants.CENTER);
		titulua.setForeground(Color.WHITE);
		titulua.setFont(new Font("Arial", Font.BOLD, 32));
		titulua.setBounds(0, 40, 900, 40);
		panela.add(titulua);

		// Denboraldia aukeratzeko
		denboraldiaEtiketa = new JLabel("Denboraldia:");
		denboraldiaEtiketa.setForeground(Color.WHITE);
		denboraldiaEtiketa.setFont(new Font("Arial", Font.BOLD, 16));
		denboraldiaEtiketa.setBounds(100, 120, 150, 30);
		panela.add(denboraldiaEtiketa);

		denboraldiaCombo = new JComboBox<>();
		denboraldiakKargatu();
		denboraldiaCombo.setBounds(100, 160, 180, 35);
		denboraldiaCombo.addActionListener(this);
		panela.add(denboraldiaCombo);

		// Jardunaldia aukeratzeko
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

		// Taula konfiguratu
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

		// Saioa amaitzeko botoia
		saioaAmaituBotoia = new JButton("Saioa amaitu");
		saioaAmaituBotoia.setFont(new Font("Arial", Font.BOLD, 18));
		saioaAmaituBotoia.setBackground(Color.RED);
		saioaAmaituBotoia.setForeground(Color.WHITE);
		saioaAmaituBotoia.setBounds(700, 480, 170, 40);
		saioaAmaituBotoia.addActionListener(this);
		panela.add(saioaAmaituBotoia);

		// Hasierako karga
		if (denboraldiaCombo.getItemCount() > 0) {
			denboraldiaCombo.setSelectedIndex(0);
			kargatuJardunaldiak();
		}
		kargatuEmaitzak();
	}

	/**
	 * Denboraldien zerrenda kargatzen du combo-an.
	 */
	private void denboraldiakKargatu() {
		denboraldiaCombo.removeAllItems();
		denboraldiaCombo.addItem(new Denboraldia(0, "Guztiak", null, null, false, null, false));
		try {
			List<Denboraldia> denboraldiak = denboraldiaDAO.denboraldiakAtera();
			for (Denboraldia d : denboraldiak) {
				denboraldiaCombo.addItem(d);
			}
		} catch (DataAccessException e) {
			LoggerUtil.log("ERROR denboraldiak kargatzean: " + e.getMessage());
			JOptionPane.showMessageDialog(panela, "Errorea denboraldiak kargatzerakoan: " + e.getMessage(), "Errorea",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Denboraldien zerrenda eguneratzen du (kanpotik deitzeko).
	 */
	public void eguneratuDenboraldiak() {
		Denboraldia selected = (Denboraldia) denboraldiaCombo.getSelectedItem();
		int selectedId = (selected != null) ? selected.getDenboraldiaKod() : 0;
		denboraldiakKargatu();
		if (selectedId != 0) {
			for (int i = 0; i < denboraldiaCombo.getItemCount(); i++) {
				Denboraldia d = denboraldiaCombo.getItemAt(i);
				if (d.getDenboraldiaKod() == selectedId) {
					denboraldiaCombo.setSelectedIndex(i);
					return;
				}
			}
		}
		if (denboraldiaCombo.getItemCount() > 0) {
			denboraldiaCombo.setSelectedIndex(0);
		}
	}

	/**
	 * Jardunaldien zerrenda kargatzen du aukeratutako denboraldiaren arabera.
	 */
	private void kargatuJardunaldiak() {
		jardunaldiaCombo.removeAllItems();
		jardunaldiaCombo.addItem("Guztiak");
		Denboraldia selected = (Denboraldia) denboraldiaCombo.getSelectedItem();
		if (selected != null && selected.getDenboraldiaKod() != 0) {
			try {
				List<Jardunaldia> jardunaldiak = jardunaldiaDAO
						.lostuJardunaldiDenboraldiBidez(selected.getDenboraldiaKod());
				jardunaldiak.sort((a, b) -> a.getHasieraData().compareTo(b.getHasieraData()));
				int zenbakia = 1;
				for (Jardunaldia j : jardunaldiak) {
					final int num = zenbakia++;
					Jardunaldia jWrapper = new Jardunaldia(j.getJardunaldiKod(), j.getHasieraData(),
							j.getAmaieraData()) {
						@Override
						public String toString() {
							return "Jardunaldia " + num;
						}
					};
					jardunaldiaCombo.addItem(jWrapper);
				}
			} catch (DataAccessException e) {
				LoggerUtil.log("ERROR jardunaldiak kargatzean: " + e.getMessage());
				JOptionPane.showMessageDialog(panela, "Errorea jardunaldiak kargatzerakoan: " + e.getMessage(),
						"Errorea", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Emaitzak iragazi eta taulan kargatzen ditu.
	 */
	private void kargatuEmaitzak() {
		taulaModeloa.setRowCount(0);
		try {
			List<Partida> partidak = partidaDAO.partidaGuztiak();

			Denboraldia selectedDenb = (Denboraldia) denboraldiaCombo.getSelectedItem();
			Object selectedJard = jardunaldiaCombo.getSelectedItem();

			for (Partida p : partidak) {
				// Saltatu talderik gabeko partidak
				if (p.getEtxekoTaldea() == null || p.getKanpokoTaldea() == null)
					continue;

				// 1) Denboraldiaren iragazketa
				if (selectedDenb != null && selectedDenb.getDenboraldiaKod() != 0) {
					if (p.getJardunaldia() == null)
						continue;
					List<Jardunaldia> denbJard = jardunaldiaDAO
							.lostuJardunaldiDenboraldiBidez(selectedDenb.getDenboraldiaKod());
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

				// 2) Jardunaldiaren iragazketa
				if (selectedJard != null && !selectedJard.equals("Guztiak")) {
					Jardunaldia jSel = (Jardunaldia) selectedJard;
					if (p.getJardunaldia() == null || p.getJardunaldia().getJardunaldiKod() != jSel.getJardunaldiKod())
						continue;
				}

				// 3) Setak banatu
				String[] setak = p.getEmaitza().split("-");
				String setakLokala = setak.length > 0 ? setak[0] : "";
				String setakKanpokoa = setak.length > 1 ? setak[1] : "";

				// 4) Denboraldi izena lortu
				String denbIzena = "";
				if (selectedDenb != null && selectedDenb.getDenboraldiaKod() != 0) {
					denbIzena = selectedDenb.getIzena();
				} else {
					if (p.getJardunaldia() != null) {
						List<Denboraldia> denbGuztiak = denboraldiaDAO.denboraldiakAtera();
						for (Denboraldia d : denbGuztiak) {
							List<Jardunaldia> jardList = jardunaldiaDAO
									.lostuJardunaldiDenboraldiBidez(d.getDenboraldiaKod());
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

				// 5) Errenkada gehitu
				Object[] row = { p.getEtxekoTaldea().getIzena(), setakLokala, setakKanpokoa,
						p.getKanpokoTaldea().getIzena(),
						p.getJardunaldia() != null ? p.getJardunaldia().toString() : "", denbIzena };
				taulaModeloa.addRow(row);
			}
		} catch (DataAccessException e) {
			LoggerUtil.log("ERROR emaitzak kargatzean: " + e.getMessage());
			JOptionPane.showMessageDialog(panela, "Errorea emaitzak kargatzerakoan: " + e.getMessage(), "Errorea",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Taula eguneratzen du (kanpotik deitzeko).
	 */
	public void eguneratuTaula() {
		kargatuEmaitzak();
	}

	/**
	 * @return Panela.
	 */
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
			JFrame frame = (JFrame) SwingUtilities.getWindowAncestor((Component) e.getSource());
			if (frame != null) {
				frame.dispose();
			}
		}
	}
}