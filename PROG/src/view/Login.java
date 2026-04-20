package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import dao.ErabiltzaileakDAO;
import pojos.Erabiltzailea;
import util.LoggerUtil;

/**
 * Login leihoa - Erabiltzailearen saioa hasteko interfazea.
 */
public class Login extends JFrame implements ActionListener, MouseListener {

	private static final long serialVersionUID = 1L;

	// Osagai grafikoak
	private JTextField erabiltzaileEremua;
	private JPasswordField pasahitzaEremua;
	private JButton erakutsiBotoia;
	private JButton sartuBotoia;
	private JButton irtenBotoia;
	private JButton erregistratuBotoia;

	/**
	 * Eraikitzailea - Leihoa konfiguratu eta osagai grafikoak gehitzen ditu.
	 */
	public Login() {
		super("Boleibol Federazioa - Saioa hasi");

		try {
			// Leihoaren konfigurazioa
			setSize(500, 400);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setLocationRelativeTo(null);
			getContentPane().setLayout(null);
			getContentPane().setBackground(new Color(0, 0, 160));

			// Logoa kargatzen saiatu
			try {
				ImageIcon logoIkurra = new ImageIcon(getClass().getResource("/images/Logo_sinFondo.png"));
				if (logoIkurra == null) {
					throw new Exception("Ezin izan da logoa kargatu.");
				}
				Image logoIrudia = logoIkurra.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
				JLabel logoEtiketa = new JLabel(new ImageIcon(logoIrudia));
				logoEtiketa.setBounds(32, 36, 100, 100);
				getContentPane().add(logoEtiketa);
			} catch (Exception e) {
				System.err.println("Errorea logo kargatzerakoan: " + e.getMessage());
				JLabel logoEtiketa = new JLabel("[LOGOA]");
				logoEtiketa.setForeground(Color.WHITE);
				logoEtiketa.setFont(new Font("Arial", Font.BOLD, 14));
				logoEtiketa.setBounds(32, 36, 100, 100);
				getContentPane().add(logoEtiketa);
			}

			// Izenburua
			JLabel izenEtiketa = new JLabel("Boleibol ");
			izenEtiketa.setHorizontalAlignment(SwingConstants.CENTER);
			izenEtiketa.setFont(new Font("Arial", Font.BOLD, 30));
			izenEtiketa.setForeground(Color.WHITE);
			izenEtiketa.setBounds(160, 47, 208, 50);
			getContentPane().add(izenEtiketa);

			JLabel lblFederazioa = new JLabel("\nFederazioa");
			lblFederazioa.setHorizontalAlignment(SwingConstants.CENTER);
			lblFederazioa.setForeground(Color.WHITE);
			lblFederazioa.setFont(new Font("Arial", Font.BOLD, 30));
			lblFederazioa.setBounds(160, 84, 213, 50);
			getContentPane().add(lblFederazioa);

			// Erabiltzailea eremua
			JLabel erabiltzaileEtiketa = new JLabel("Erabiltzailea:");
			erabiltzaileEtiketa.setFont(new Font("Tahoma", Font.BOLD, 13));
			erabiltzaileEtiketa.setForeground(Color.WHITE);
			erabiltzaileEtiketa.setBounds(50, 160, 100, 25);
			getContentPane().add(erabiltzaileEtiketa);

			erabiltzaileEremua = new JTextField();
			erabiltzaileEremua.setBounds(160, 160, 200, 25);
			getContentPane().add(erabiltzaileEremua);

			// Pasahitza eremua
			JLabel pasahitzaEtiketa = new JLabel("Pasahitza:");
			pasahitzaEtiketa.setFont(new Font("Tahoma", Font.BOLD, 13));
			pasahitzaEtiketa.setForeground(Color.WHITE);
			pasahitzaEtiketa.setBounds(50, 200, 100, 25);
			getContentPane().add(pasahitzaEtiketa);

			pasahitzaEremua = new JPasswordField();
			pasahitzaEremua.setBounds(160, 200, 200, 25);
			pasahitzaEremua.setEchoChar('*');
			getContentPane().add(pasahitzaEremua);

			// Pasahitza erakutsi botoia
			erakutsiBotoia = new JButton("\uD83D\uDC41"); // Begi ikonoa
			erakutsiBotoia.setBackground(new Color(255, 255, 255));
			erakutsiBotoia.setBounds(370, 200, 56, 25);
			erakutsiBotoia.addMouseListener(this);
			getContentPane().add(erakutsiBotoia);

			// Botoiak
			sartuBotoia = new JButton("Sartu");
			sartuBotoia.setFont(new Font("Tahoma", Font.BOLD, 12));
			sartuBotoia.setBackground(new Color(255, 255, 255));
			sartuBotoia.addActionListener(this);
			sartuBotoia.setBounds(101, 286, 100, 25);
			getContentPane().add(sartuBotoia);

			erregistratuBotoia = new JButton("Erregistratu");
			erregistratuBotoia.setFont(new Font("Tahoma", Font.BOLD, 12));
			erregistratuBotoia.setBackground(Color.WHITE);
			erregistratuBotoia.setBounds(285, 286, 119, 25);
			erregistratuBotoia.addActionListener(this);
			getContentPane().add(erregistratuBotoia);

			irtenBotoia = new JButton("Irten");
			irtenBotoia.setBackground(Color.RED);
			irtenBotoia.setForeground(Color.WHITE);
			irtenBotoia.setBounds(400, 10, 80, 30);
			irtenBotoia.addActionListener(this);
			getContentPane().add(irtenBotoia);

			setVisible(true);

		} catch (HeadlessException e) {
			System.err.println("Errorea interfazea sortzerakoan (Headless): " + e.getMessage());
			JOptionPane.showMessageDialog(null,
					"Errorea interfazea sortzerakoan. Ziurtatu sistema leihoak onartzen dituela.", "Errorea Larria",
					JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			System.err.println("Errorea Login frame-a sortzerakoan: " + e.getMessage());
			JOptionPane.showMessageDialog(null, "Errorea aplikazioa hasieratzerakoan: " + e.getMessage(),
					"Errorea Larria", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Aplikazioa abiarazteko metodo nagusia.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new Login());
	}

	// ==================== EVENTOAK ====================

	@Override
	public void mousePressed(MouseEvent e) {
		// Pasahitza erakutsi sakatzean
		pasahitzaEremua.setEchoChar((char) 0);
		erakutsiBotoia.setText("-");
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// Pasahitza ezkutatu askatzean
		pasahitzaEremua.setEchoChar('*');
		erakutsiBotoia.setText("\uD83D\uDC41");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();

		// ===== SAIOA HASI =====
		if (src == sartuBotoia) {
			String erabiltzailea = erabiltzaileEremua.getText().trim();
			String pasahitza = new String(pasahitzaEremua.getPassword()).trim();

			// Eremuak beteta daudela egiaztatu
			if (erabiltzailea.isEmpty() || pasahitza.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Erabiltzailea eta pasahitza bete behar dira.", "Errorea",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			ErabiltzaileakDAO dao = new ErabiltzaileakDAO();
			Erabiltzailea u = dao.login(erabiltzailea, pasahitza);

			if (u != null) {
				JOptionPane.showMessageDialog(this, "Ongi etorri " + u.getErabiltzailea() + " (" + u.getRola() + ")");
				SwingUtilities.invokeLater(() -> new Main(u).setVisible(true));
				dispose();
			} else {
				JOptionPane.showMessageDialog(null, "Erabiltzailea edo pasahitza okerra", "Errorea",
						JOptionPane.ERROR_MESSAGE);
			}
		}

		// ===== IRTEN =====
		if (src == irtenBotoia) {
			LoggerUtil.closeLogger();
			System.exit(0);
		}

		// ===== ERREGISTRATU =====
		if (src == erregistratuBotoia) {
			String erabiltzailea = erabiltzaileEremua.getText().trim();
			String pasahitza = new String(pasahitzaEremua.getPassword()).trim();

			if (erabiltzailea.isEmpty() || pasahitza.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Erabiltzailea eta pasahitza bete behar dira.", "Errorea",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			ErabiltzaileakDAO dao = new ErabiltzaileakDAO();
			boolean sortuta = dao.erregistratu(erabiltzailea, pasahitza);

			if (sortuta) {
				JOptionPane.showMessageDialog(this, "Erabiltzailea ondo erregistratu da.");
			} else {
				JOptionPane.showMessageDialog(this, "Erabiltzailea existitzen da, mesedez sartu beste bat.");
			}
		}
	}

	// ===== Gainerako MouseListener metodoak (erabili gabe) =====
	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	/**
	 * Leihoa ixtean loggerra ixten du.
	 */
	@Override
	public void dispose() {
		super.dispose();
	}
}