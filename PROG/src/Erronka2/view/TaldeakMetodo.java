package Erronka2.view;

import Erronka2.model.*;
import Erronka2.model.dao.JokalariaDAO;
import Erronka2.model.dao.TaldeaDAO;
import Erronka2.model.pojos.Jokalaria;
import Erronka2.model.pojos.Taldea;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class TaldeakMetodo {

    private JPanel taldeakPanela;
    private JPanel sarePanela;
    private JButton saioaAmaituBotoia;
    private DefaultListModel<Jokalaria> modeloa;
    private ImageIcon ikonoa;
    private List<Jokalaria> jokalariak;
    private JList<Jokalaria> zerrenda;
    private JScrollPane korritzePanela;
    private Image irudia;

    private TaldeaDAO taldeaDAO;
    private JokalariaDAO jokalariaDAO;

    public TaldeakMetodo(Color kolorea) {
        taldeaDAO = new TaldeaDAO();
        jokalariaDAO = new JokalariaDAO();

        taldeakPanela = new JPanel(null);
        taldeakPanela.setBackground(kolorea);

        sarePanela = new JPanel(new GridLayout(2, 3, 30, 30));
        sarePanela.setBackground(kolorea);
        sarePanela.setBounds(50, 50, 800, 380);

        List<Taldea> taldeak = taldeaDAO.getAll();
        for (Taldea t : taldeak) {
            if (t.getTaldeaKod() != 0) {
                String nombreArchivo = t.getIzena().replace(" ", "") + ".png";
                String logoBidea = "/Erronka2/images/LogosEquipos/" + nombreArchivo;
                sarePanela.add(taldeBotoiaSortu(t.getIzena(), logoBidea, t.getTaldeaKod()));
            }
        }

        taldeakPanela.add(sarePanela);

        saioaAmaituBotoia = new JButton("Saioa amaitu");
        saioaAmaituBotoia.setFont(new Font("Arial", Font.BOLD, 18));
        saioaAmaituBotoia.setBackground(Color.RED);
        saioaAmaituBotoia.setForeground(Color.WHITE);
        saioaAmaituBotoia.setBounds(700, 480, 170, 40);
        saioaAmaituBotoia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    SwingUtilities.invokeLater(() -> new Login().setVisible(true));
                    JFrame frame = (JFrame) SwingUtilities.getWindowAncestor((Component) e.getSource());
                    frame.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(taldeakPanela,
                        "Errorea saioa amaitzerakoan: " + ex.getMessage(),
                        "Errorea", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });
        taldeakPanela.add(saioaAmaituBotoia);
    }

    private JButton taldeBotoiaSortu(String izena, String logoBidea, int taldeKod) {
        JButton botoia = new JButton(izena);
        try {
            java.net.URL url = getClass().getResource(logoBidea);
            if (url != null) {
                ikonoa = new ImageIcon(url);
                irudia = ikonoa.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                botoia.setIcon(new ImageIcon(irudia));
            } else {
                System.err.println("Logo ez da aurkitu: " + logoBidea + " - Testuarekin jarraitzen da.");
            }
        } catch (Exception e) {
            System.err.println("Errorea logo kargatzerakoan: " + e.getMessage());
        }

        botoia.setVerticalTextPosition(SwingConstants.BOTTOM);
        botoia.setHorizontalTextPosition(SwingConstants.CENTER);
        botoia.setFont(new Font("Arial", Font.BOLD, 16));
        botoia.setForeground(Color.WHITE);
        botoia.setBorderPainted(false);
        botoia.setFocusPainted(false);
        botoia.setContentAreaFilled(false);
        botoia.setCursor(new Cursor(Cursor.HAND_CURSOR));

        botoia.addActionListener(e -> {
            try {
                erakutsiJokalariak(taldeKod, izena);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(taldeakPanela,
                    "Errorea jokalariak erakusteko: " + ex.getMessage(),
                    "Errorea", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        return botoia;
    }

    private void erakutsiJokalariak(int taldeKod, String taldeIzena) throws Exception {
        List<Jokalaria> jokalariak = jokalariaDAO.getByTaldea(taldeKod);

        if (jokalariak == null || jokalariak.isEmpty()) {
            JOptionPane.showMessageDialog(taldeakPanela,
                    "Ez dago jokalaririk talde honetan",
                    "Informazioa",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        modeloa = new DefaultListModel<>();
        for (Jokalaria j : jokalariak) {
            modeloa.addElement(j);
        }

        zerrenda = new JList<>(modeloa);
        zerrenda.setFont(new Font("Arial", Font.PLAIN, 16));

        korritzePanela = new JScrollPane(zerrenda);
        korritzePanela.setPreferredSize(new Dimension(350, 250));

        JOptionPane.showMessageDialog(
                taldeakPanela,
                korritzePanela,
                taldeIzena + " - Jokalariak",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public JPanel getPanela() {
        return taldeakPanela;
    }
}