package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import dao.ErabiltzaileakDAO;
import pojos.Erabiltzailea;
import util.LoggerUtil;

/**
 * Ikuspegiaren (View) geruzako klasea. Erabiltzaileak kudeatzeko panela
 * ordezkatzen du.
 */
public class ErabiltzaileaKudeatu extends JPanel implements ActionListener {

	private JTable taula;
	private DefaultTableModel dtm;
	private JLabel lblErabiltzaileKop;
	private JLabel tituloa;
	private JButton btnEzabatu;
	private JButton btnIrten;

	/**
	 * ErabiltzaileaKudeatu klasearen eraikitzailea.
	 * 
	 * @param urdina Atzeko planoaren kolorea.
	 */
	public ErabiltzaileaKudeatu(Color urdina) {
		setLayout(new BorderLayout());
		setBackground(urdina);

		// Izenburua
		tituloa = new JLabel("ERABILTZAILEAREN KUDEAKETA");
		tituloa.setForeground(Color.WHITE);
		tituloa.setFont(new Font("Arial", Font.BOLD, 40));
		tituloa.setHorizontalAlignment(SwingConstants.CENTER);
		tituloa.setBorder(javax.swing.BorderFactory.createEmptyBorder(40, 0, 20, 0));
		add(tituloa, BorderLayout.NORTH);

		// Taularen zutabeak
		Vector<String> zutabeak = new Vector<>();
		zutabeak.add("Erabiltzailea");
		zutabeak.add("Rola");

		// Taula konfiguratu
		dtm = new DefaultTableModel(new Vector<>(), zutabeak);
		taula = new JTable(dtm);
		taula.setFont(new Font("Arial", Font.BOLD, 16));
		taula.setRowHeight(30);

		JScrollPane scroll = new JScrollPane(taula);
		scroll.setPreferredSize(new Dimension(700, 350));

		JPanel centerPanel = new JPanel();
		centerPanel.setBackground(urdina);
		centerPanel.setLayout(new java.awt.GridBagLayout());
		centerPanel.add(scroll);
		add(centerPanel, BorderLayout.CENTER);

		// Beheko panela
		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.setBackground(urdina);
		bottomPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 40, 20, 40));

		// Ezkerrean: erabiltzaile kopurua
		JPanel leftPanel = new JPanel();
		leftPanel.setBackground(urdina);
		JLabel lblErabiltzaileak = new JLabel("Erabiltzaileak:");
		lblErabiltzaileak.setFont(new Font("Arial", Font.BOLD, 18));
		lblErabiltzaileak.setForeground(Color.WHITE);
		lblErabiltzaileKop = new JLabel("0");
		lblErabiltzaileKop.setFont(new Font("Arial", Font.BOLD, 18));
		lblErabiltzaileKop.setForeground(Color.WHITE);
		leftPanel.add(lblErabiltzaileak);
		leftPanel.add(lblErabiltzaileKop);

		// Eskuinean: botoiak
		JPanel rightPanel = new JPanel();
		rightPanel.setBackground(urdina);
		btnEzabatu = new JButton("Ezabatu");
		btnEzabatu.setFont(new Font("Arial", Font.BOLD, 18));
		btnEzabatu.setBackground(Color.WHITE);
		btnEzabatu.setForeground(new Color(0, 0, 128));
		btnEzabatu.addActionListener(this);
		btnIrten = new JButton("Irten");
		btnIrten.setFont(new Font("Arial", Font.BOLD, 18));
		btnIrten.setBackground(Color.RED);
		btnIrten.setForeground(Color.BLACK);
		btnIrten.addActionListener(this);
		rightPanel.add(btnEzabatu);
		rightPanel.add(btnIrten);

		bottomPanel.add(leftPanel, BorderLayout.WEST);
		bottomPanel.add(rightPanel, BorderLayout.EAST);
		add(bottomPanel, BorderLayout.SOUTH);

		// Datuak kargatu
		kargatuErabiltzaileak();
	}

	/**
	 * Erabiltzaileak kargatu eta taulan erakusten ditu.
	 */
	private void kargatuErabiltzaileak() {
		ErabiltzaileakDAO dao = new ErabiltzaileakDAO();
		List<Erabiltzailea> lista = dao.getErabiltzaileak();
		dtm.setRowCount(0);
		for (Erabiltzailea e : lista) {
			Vector<Object> fila = new Vector<>();
			fila.add(e.getErabiltzailea());
			fila.add(e.getRola());
			dtm.addRow(fila);
		}
		lblErabiltzaileKop.setText(String.valueOf(lista.size()));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();

		// ===== EZABATU BOTOIA =====
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

			// Admin-a ezin da ezabatu
			if ("ADMIN".equalsIgnoreCase(rola)) {
				JOptionPane.showMessageDialog(this, "Errorea, Admin erabiltzaileak ezin dira ezabatu.", "Errorea",
						JOptionPane.ERROR_MESSAGE);
				LoggerUtil.log("Saiakera admin ezabatzeko: " + erabiltzailea);
				return;
			}

			// Baieztapena eskatu
			int confirm = JOptionPane.showConfirmDialog(this, "Ziur zaude erabiltzailea ezabatu nahi duzula?",
					"Egiaztapena", JOptionPane.YES_NO_OPTION);
			if (confirm != JOptionPane.YES_OPTION) {
				return;
			}

			// Ezabatu eta taula eguneratu
			boolean borrado = dao.ezabatuErabiltzailea(erabiltzailea);
			if (borrado) {
				dtm.removeRow(fila);
				lblErabiltzaileKop.setText(String.valueOf(dtm.getRowCount()));
				JOptionPane.showMessageDialog(this, "Erabiltzailea ondo ezabatu da.", "Ondo",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(this, "Errorea erabiltzailea ezabatzean.", "Errorea",
						JOptionPane.ERROR_MESSAGE);
			}
		}

		// ===== IRTEN BOTOIA =====
		if (src == btnIrten) {
			SwingUtilities.invokeLater(() -> new Login().setVisible(true));
			JFrame frame = (JFrame) SwingUtilities.getWindowAncestor((Component) e.getSource());
			if (frame != null) {
				frame.dispose();
			}
		}
	}
}