package Erronka2.view;

import javax.swing.*;

import Erronka2.model.Rola;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;

/**
 * Login leihoa - Erabiltzailearen saioa hasteko interfazea.
 * 
 * JFrame oinarrian, erabiltzaile izena eta pasahitza sartzeko eremuak ditu,
 * baita pasahitza erakusteko botoia, saioa hasteko eta irteteko botoiak.
 */
public class Login extends JFrame {

    private static final long serialVersionUID = 1L;

    private ImageIcon logoIkurra;           // Logoaren ImageIcon
    private Image logoIrudia;                // Eskalatutako irudia
    private ImageIcon eskalatutakoLogoIkurra; // Eskalatutako ImageIcon
    private JLabel erabiltzaileEtiketa;     // "Erabiltzailea:" etiketa
    private JLabel logoEtiketa;             // Logoaren etiketa
    private JLabel izenEtiketa;             // Federazioaren izena erakusten duena
    private JTextField erabiltzaileEremua;  // Erabiltzaile izena sartzeko eremua
    private JLabel pasahitzaEtiketa;        // "Pasahitza:" etiketa
    private JPasswordField pasahitzaEremua; // Pasahitza sartzeko eremua
    private JButton erakutsiBotoia;          // Pasahitza erakusteko botoia
    private JButton sartuBotoia;             // Saioa hasteko botoia
    private JButton irtenBotoia;             // Irteerako botoia
    private JLabel lblFederazioa;            // Federazioaren izena bigarren etiketa

    /**
     * Eraikitzailea - Leihoa konfiguratu eta osagaiak gehitzen ditu.
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
            getContentPane().setBackground(new Color(60, 72, 191)); // Atzeko plano urdina
            
            // Logoaren karga eta erakusketa
            try {
                logoIkurra = new ImageIcon(
                    getClass().getResource("/Erronka2/images/Logo_sinFondo.png")
                );
                
                if (logoIkurra == null) {
                    throw new Exception("Ezin izan da logoa kargatu. Path okerra: /Erronka2/images/Logo_sinFondo.png");
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
            erakutsiBotoia.addMouseListener(new MouseAdapter() {
                /**
                 * Pasahitza erakutsi sakatuta dagoenean
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
                 * Pasahitza ezkutatu botoia utzi denean
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
            });
            getContentPane().add(erakutsiBotoia);
           
            // Saioa hasteko botoia
            sartuBotoia = new JButton("Sartu");
            sartuBotoia.setFont(new Font("Tahoma", Font.BOLD, 12));
            sartuBotoia.setBackground(new Color(255, 255, 255));
            sartuBotoia.addActionListener(new ActionListener() {
                /**
                 * Saioa hasteko botoia sakatzean erabiltzailearen autentikazioa egiaztatzen du.
                 * @param e ActionEvent
                 */
                public void actionPerformed(ActionEvent e) {
                    try {
                        String erabiltzailea = erabiltzaileEremua.getText().trim();
                        String pasahitza = new String(pasahitzaEremua.getPassword()).trim();

                        // Erabiltzaile eta pasahitza hutsik badaude, salbuespena sortu
                        if (erabiltzailea.isEmpty() || pasahitza.isEmpty()) {
                            throw new IllegalArgumentException("Mesedez, bete erabiltzailea eta pasahitza");
                        }

                        // Rola klasearekin autentikatu
                        Rola rol = Rola.Egiaztatu(erabiltzailea, pasahitza);

                        if (rol != null) {
                            // Erabiltzaile ondo sartu bada, mezu ongi etorria erakutsi eta Main leihoa ireki
                            JOptionPane.showMessageDialog(Login.this, "Ongi etorri, " + erabiltzailea + " !!");
                            SwingUtilities.invokeLater(() -> new Main().setVisible(true));
                            dispose(); // Login leihoa itxi
                        } else {
                            // Erabiltzaile edo pasahitza okerra bada
                            throw new SecurityException("Erabiltzailea edo pasahitza okerra");
                        }
                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(Login.this, ex.getMessage(), "Abisua", 
                            JOptionPane.WARNING_MESSAGE);
                    } catch (SecurityException ex) {
                        JOptionPane.showMessageDialog(Login.this, ex.getMessage(), "Errorea", 
                            JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(Login.this, 
                            "Errorea sistemara sartzerakoan: " + ex.getMessage(), 
                            "Errorea", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }
            });

            sartuBotoia.setBounds(196, 284, 100, 25);
            getContentPane().add(sartuBotoia);

            // Irteerako botoia - aplikazioa ixteko
            irtenBotoia = new JButton("Irten");
            irtenBotoia.addActionListener(new ActionListener() {
                /**
                 * Irteerako botoia sakatzean aplikazioa ixteko saiakera egiten du.
                 * @param e ActionEvent
                 */
                public void actionPerformed(ActionEvent e) {
                    try {
                        System.exit(0);
                    } catch (SecurityException ex) {
                        JOptionPane.showMessageDialog(Login.this, 
                            "Ez dago baimenik aplikazioa ixteko: " + ex.getMessage(),
                            "Errorea", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(Login.this, 
                            "Errorea aplikazioa ixteko: " + ex.getMessage(),
                            "Errorea", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }
            });
            irtenBotoia.setBackground(Color.RED);
            irtenBotoia.setForeground(Color.WHITE);
            irtenBotoia.setBounds(400, 10, 80, 30);
            getContentPane().add(irtenBotoia);
            
            // Bigarren etiketa: Federazioa
            lblFederazioa = new JLabel("\nFederazioa");
            lblFederazioa.setHorizontalAlignment(SwingConstants.CENTER);
            lblFederazioa.setForeground(Color.WHITE);
            lblFederazioa.setFont(new Font("Arial", Font.BOLD, 30));
            lblFederazioa.setBounds(160, 84, 213, 50);
            getContentPane().add(lblFederazioa);

            // Leihoa bistaratzea
            setVisible(true);
            
        } catch (HeadlessException e) {
            System.err.println("Errorea interfazea sortzerakoan (Headless): " + e.getMessage());
            JOptionPane.showMessageDialog(null, 
                "Errorea interfazea sortzerakoan. Ziurtatu sistema leihoak onartzen dituela.", 
                "Errorea Larria", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            System.err.println("Errorea Login frame-a sortzerakoan: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Errorea aplikazioa hasieratzerakoan: " + e.getMessage(), 
                "Errorea Larria", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Main metodoa aplikazioa abiarazteko.
     * @param args Komando lerroko argumentuak (ez dira erabiltzen)
     */
    public static void main(String[] args) {
        try {
            // Swing UI haria erabiliz, Login frame bat sortu
            SwingUtilities.invokeLater(() -> new Login());
        } catch (Exception e) {
            System.err.println("Errorea aplikazioa abiarazterakoan: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Errorea aplikazioa abiarazterakoan: " + e.getMessage(), 
                "Errorea Larria", JOptionPane.ERROR_MESSAGE);
        }
    }
}
