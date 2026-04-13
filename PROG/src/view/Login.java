package view;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.swing.*;

import java.io.File;
// LOG sortzeko
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import dao.Konexioa;
import pojos.Erabiltzailea;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;
import dao.ErabiltzaileakDAO;

import dao.Konexioa;

/**
 * Login leihoa - Erabiltzailearen saioa hasteko interfazea.
 * * JFrame oinarrian, erabiltzaile izena eta pasahitza sartzeko eremuak ditu,
 * baita pasahitza erakusteko botoia, saioa hasteko, erregistratzeko eta irteteko botoiak.
 */
public class Login extends JFrame implements ActionListener, MouseListener {

	private static final long serialVersionUID = 1L;

	private ImageIcon logoIkurra; // Logoaren ImageIcon
	private Image logoIrudia; // Eskalatutako irudia
	private ImageIcon eskalatutakoLogoIkurra; // Eskalatutako ImageIcon

	private JLabel erabiltzaileEtiketa; // "Erabiltzailea:" etiketa
	private JLabel logoEtiketa; // Logoaren etiketa
	private JLabel izenEtiketa; // Federazioaren izena erakusten duena
	private JLabel pasahitzaEtiketa; // "Pasahitza:" etiketa
	private JLabel lblFederazioa; // Federazioaren izena bigarren etiketa

	private JTextField erabiltzaileEremua; // Erabiltzaile izena sartzeko eremua

	private JPasswordField pasahitzaEremua; // Pasahitza sartzeko eremua

	private JButton erakutsiBotoia; // Pasahitza erakusteko botoia
	private JButton sartuBotoia; // Saioa hasteko botoia
	private JButton irtenBotoia; // Irteerako botoia
	private JButton erregistratuBotoia;
	private Konexioa konexioa;

	private PrintWriter logger;

	/**
	 * Eraikitzailea - Leihoa konfiguratu eta osagai grafiko guztiak gehitzen ditu.
	 * Datu-basearekiko konexioa prestatzen du eta logger-a martxan jartzen du.
	 */
	public Login() {
		// Framearen izena jarri
		super("Boleibol Federazioa - Saioa hasi");

		try {
			// Frame konfigurazioa
			setSize(500, 400);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setLocationRelativeTo(null); // Leihoa pantailaren erdian jarri
			getContentPane().setLayout(null); // Layout absolutua, posizioak zehatzak
			getContentPane().setBackground(new Color(0, 0, 160)); // Atzeko plano urdina

			// Logoaren karga eta erakusketa
			try {
				logoIkurra = new ImageIcon(getClass().getResource("/images/Logo_sinFondo.png"));

				if (logoIkurra == null) {
					throw new Exception("Ezin izan da logoa kargatu. Path okerra: /images/Logo_sinFondo.png");
				}

				// Logoaren irudia eskalatu 100x100 tamainara
				logoIrudia = logoIkurra.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
				eskalatutakoLogoIkurra = new ImageIcon(logoIrudia);

				logoEtiketa = new JLabel(eskalatutakoLogoIkurra);
				logoEtiketa.setBounds(32, 36, 100, 100);
				getContentPane().add(logoEtiketa);

			} catch (Exception e) {
				// Logoaren karga huts egiten badu, testu alternatiboa erakutsi
				System.err.println("Errorea logo kargatzerakoan: " + e.getMessage());
				logoEtiketa = new JLabel("[LOGOA]");
				logoEtiketa.setForeground(Color.WHITE);
				logoEtiketa.setFont(new Font("Arial", Font.BOLD, 14));
				logoEtiketa.setBounds(32, 36, 100, 100);
				getContentPane().add(logoEtiketa);
			}

			// Federazioaren izena label moduan
			izenEtiketa = new JLabel("Boleibol ");
			izenEtiketa.setHorizontalAlignment(SwingConstants.CENTER);
			izenEtiketa.setFont(new Font("Arial", Font.BOLD, 30));
			izenEtiketa.setForeground(Color.WHITE);
			izenEtiketa.setBounds(160, 47, 208, 50);
			getContentPane().add(izenEtiketa);

			// Erabiltzailearen etiketa
			erabiltzaileEtiketa = new JLabel("Erabiltzailea:");
			erabiltzaileEtiketa.setFont(new Font("Tahoma", Font.BOLD, 13));
			erabiltzaileEtiketa.setForeground(Color.WHITE);
			erabiltzaileEtiketa.setBounds(50, 160, 100, 25);
			getContentPane().add(erabiltzaileEtiketa);

			// Erabiltzaile izena sartzeko testu-eremua
			erabiltzaileEremua = new JTextField();
			erabiltzaileEremua.setBounds(160, 160, 200, 25);
			getContentPane().add(erabiltzaileEremua);

			// Pasahitzaren etiketa
			pasahitzaEtiketa = new JLabel("Pasahitza:");
			pasahitzaEtiketa.setFont(new Font("Tahoma", Font.BOLD, 13));
			pasahitzaEtiketa.setForeground(Color.WHITE);
			pasahitzaEtiketa.setBounds(50, 200, 100, 25);
			getContentPane().add(pasahitzaEtiketa);

			// Pasahitza sartzeko eremua
			pasahitzaEremua = new JPasswordField();
			pasahitzaEremua.setBounds(160, 200, 200, 25);
			pasahitzaEremua.setEchoChar('*'); // Lehenetsitako karakterea asteriskoak dira
			getContentPane().add(pasahitzaEremua);

			// Pasahitza erakusteko botoia ("👁")
			erakutsiBotoia = new JButton("👁");
			erakutsiBotoia.setBackground(new Color(255, 255, 255));
			erakutsiBotoia.setBounds(370, 200, 56, 25);
			erakutsiBotoia.addMouseListener(this);
			getContentPane().add(erakutsiBotoia);

			// Saioa hasteko botoia
			sartuBotoia = new JButton("Sartu");
			sartuBotoia.setFont(new Font("Tahoma", Font.BOLD, 12));
			sartuBotoia.setBackground(new Color(255, 255, 255));
			sartuBotoia.addActionListener(this); // Botoiari funtzioa emateko

			sartuBotoia.setBounds(101, 286, 100, 25);
			getContentPane().add(sartuBotoia);

			// Irteerako botoia - aplikazioa ixteko
			irtenBotoia = new JButton("Irten");

			irtenBotoia.setBackground(Color.RED);
			irtenBotoia.setForeground(Color.WHITE);
			irtenBotoia.setBounds(400, 10, 80, 30);
			getContentPane().add(irtenBotoia);
			irtenBotoia.addActionListener(this); // Botoiari funtzioa emateko

			// Bigarren etiketa: Federazioa
			lblFederazioa = new JLabel("\nFederazioa");
			lblFederazioa.setHorizontalAlignment(SwingConstants.CENTER);
			lblFederazioa.setForeground(Color.WHITE);
			lblFederazioa.setFont(new Font("Arial", Font.BOLD, 30));
			lblFederazioa.setBounds(160, 84, 213, 50);
			getContentPane().add(lblFederazioa);

			erregistratuBotoia = new JButton("Erregistratu");
			erregistratuBotoia.setFont(new Font("Tahoma", Font.BOLD, 12));
			erregistratuBotoia.setBackground(Color.WHITE);
			erregistratuBotoia.setBounds(285, 286, 119, 25);
			getContentPane().add(erregistratuBotoia);
			erregistratuBotoia.addActionListener(this); // Botoiari funtzioa emateko

			// Leihoa bistaratzea
			setVisible(true);
			initLogger();

		} catch (HeadlessException e) {
			System.err.println("Errorea interfazea sortzerakoan (Headless): " + e.getMessage());
			JOptionPane.showMessageDialog(null,
					"Errorea interfazea sortzerakoan. Ziurtatu sistema leihoak onartzen dituela.", "Errorea Larria",
					JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			System.err.println("Errorea Login frame-a sortzerakoan: " + e.getMessage());
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Errorea aplikazioa hasieratzerakoan: " + e.getMessage(),
					"Errorea Larria", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Main metodoa aplikazioa abiarazteko.
	 * * @param args Komando lerroko argumentuak (ez dira erabiltzen)
	 */
	public static void main(String[] args) {
		try {
			// Swing UI haria erabiliz, Login frame bat sortu
			SwingUtilities.invokeLater(() -> new Login());
		} catch (Exception e) {
			System.err.println("Errorea aplikazioa abiarazterakoan: " + e.getMessage());
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Errorea aplikazioa abiarazterakoan: " + e.getMessage(),
					"Errorea Larria", JOptionPane.ERROR_MESSAGE);
		}
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
			JOptionPane.showMessageDialog(this, "Errorea logger hasterakoan: " + e.getMessage(), "Errorea",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Mezu bat idazten du log fitxategian.
	 * * @param izena Ekintzaren arduraduna edo etiketa (adib. erabiltzaile izena).
	 * @param mezua Log-ean idazteko xehetasun mezua.
	 */
	private void log(String izena, String mezua) {
		if (logger != null) {
			LocalDateTime now = LocalDateTime.now();
			String timestamp = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			logger.println("[" + timestamp + "]: " + izena + " " + mezua);
			logger.flush();
		}
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * Pasahitza erakutsi sakatuta dagoenean exekutatzen da.
	 * Pasahitzaren karaktereak ikusgarri bihurtzen ditu.
	 * * @param e Saguaren gertaera.
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		try {
			pasahitzaEremua.setEchoChar((char) 0); // Pasahitza erakutsi
			erakutsiBotoia.setText("-");
		} catch (Exception ex) {
			System.err.println("Errorea pasahitza erakusteko: " + ex.getMessage());
		}

	}

	/**
	 * Pasahitza erakutsi botoia askatu denean exekutatzen da.
	 * Pasahitza berriro ezkutatzen du asteriskoak erabiliz.
	 * * @param e Saguaren gertaera.
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		try {
			pasahitzaEremua.setEchoChar('*'); // Pasahitza ezkutatu
			erakutsiBotoia.setText("👁");
		} catch (Exception ex) {
			System.err.println("Errorea pasahitza ezkutatzeko: " + ex.getMessage());
		}

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * Botoiei funtzionalitate ezberdinak emateko metodoa.
	 * Saioa hasteko, erregistratzeko eta aplikaziotik irteteko ekintzak kudeatzen ditu.
	 * * @param e Botoiaren sakatze-gertaera.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		if (src == sartuBotoia) {
			try {
				// Testu eremuetatik erabiltzailea eta pasahitza hartzen du
				String erabiltzailea = erabiltzaileEremua.getText().trim();
				String pasahitza = new String(pasahitzaEremua.getPassword()).trim();

				// testu eremuak hutzik daudenenan errore mezua ateratzen da
				if (erabiltzailea.isEmpty() || pasahitza.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Erabiltzailea eta pasahitza bete behar dira.", "Errorea",
							JOptionPane.ERROR_MESSAGE);
					return; // hemen gelditzen da exekuzioa
				}

				// DAO objektua sortu (datu-basera sartzeko)
				ErabiltzaileakDAO dao = new ErabiltzaileakDAO();

				// login metodoa deitu (ObjectDB-n bilaketa egiten du)
				Erabiltzailea u = dao.login(erabiltzailea, pasahitza);

				// Emaitza konprobatu
				if (u != null) {
					// Erabiltzailea existitzen daba ongi etorria eman eta programara sartuko da
					JOptionPane.showMessageDialog(this,
							"Ongi etorri " + u.getErabiltzailea() + " " + "(" + u.getRola() + ")");

					// Erabiltzailea sartzerakoan log fitxategian gordeko da
					log("[LOGUEATU]", u.getErabiltzailea() + " " + u.getRola() + " sartu da.");

					// Hurrengo leihoa ireki
					SwingUtilities.invokeLater(() -> new Main(u).setVisible(true));

					// login leihoa itxi
					dispose();

				} else {
					// Ez bada existitzen errore mezua aterako da
					JOptionPane.showMessageDialog(null, "Erabiltzailea edo pasahitza okerra", "Errorea",
							JOptionPane.ERROR_MESSAGE);
				}

			} catch (Exception ex) {
				// Beste errorerik ematen duenenan
				JOptionPane.showMessageDialog(this, "Errorea sistemara sartzerakoan: " + ex.getMessage(), "Errorea",
						JOptionPane.ERROR_MESSAGE);
				ex.printStackTrace();
			}

		}
		if (src == irtenBotoia) {
			try {
				System.exit(0);
			} catch (SecurityException ex) {
				JOptionPane.showMessageDialog(Login.this, "Ez dago baimenik aplikazioa ixteko: " + ex.getMessage(),
						"Errorea", JOptionPane.ERROR_MESSAGE);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(Login.this, "Errorea aplikazioa ixteko: " + ex.getMessage(), "Errorea",
						JOptionPane.ERROR_MESSAGE);
				ex.printStackTrace();
			}

		}
		if (src == erregistratuBotoia) {
			try {
				// Datuak hartu textu eremuetik
				String erabiltzailea = erabiltzaileEremua.getText().trim();
				String pasahitza = new String(pasahitzaEremua.getPassword()).trim();

				// Balidatu ea hutzik ez dauden
				if (erabiltzailea.isEmpty() || pasahitza.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Erabiltzailea eta pasahitza bete behar dira.", "Errorea",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				// DAO erabili
				ErabiltzaileakDAO dao = new ErabiltzaileakDAO();
				boolean sortuta = dao.erregistratu(erabiltzailea, pasahitza);

				if (sortuta) {
					// Ondo sortuta dagoenenan mezua erakutsi
					JOptionPane.showMessageDialog(this, "Erabiltzailea ondo erregistratu da.");

				} else {
					// Erabiltzaile hori existitzen bada mezua erakutsi
					JOptionPane.showMessageDialog(this, "Erabiltzailea existitzen da, mesedez sartu beste bat.");
				}

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Errorea erregistratzerakoan: " + ex.getMessage(), "Errorea",
						JOptionPane.ERROR_MESSAGE);
				ex.printStackTrace();
			}

		}

	}

	/**
	 * Logger sistema ixten du.
	 */
	public void closeLogger() {
		if (logger != null) {
			logger.close();
		}
	}

	/**
	 * Leihoa itxi aurretik logger sistema ixten du.
	 */
	public void dispose() {
		closeLogger();
		super.dispose();
	}

}