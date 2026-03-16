package Erronka2.view;

import javax.swing.*;
import java.awt.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Main extends JFrame {
    private JPanel panelNagusia;
    private JTabbedPane fitxaPanela;
    private EmaitzakMetodo emaitzakPanela;
    private KlasifikazioaMetodo klasifikazioaPanela;
    private PartiduakMetodo partiduakPanela;
    private FitxaketakMetodo fitxaketakPanela;
    private TaldeakMetodo taldeakPanela;

    // Nuevas pestañas
    private JokalariakGehituMetodo jokalariakGehituPanela;
    private TaldeakGehituMetodo taldeakGehituPanela;

    public Main() {
        try {
            setTitle("Boleibol Federazioa - 3. Taldea - Sistema");
            setSize(1000, 600);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            Color urdina = new Color(0, 70, 160);

            panelNagusia = new JPanel(new BorderLayout());
            panelNagusia.setBackground(urdina);
            setContentPane(panelNagusia);

            fitxaPanela = new JTabbedPane();
            fitxaPanela.setFont(new Font("Arial", Font.BOLD, 14));

            try {
                partiduakPanela = new PartiduakMetodo(urdina, this);
                fitxaketakPanela = new FitxaketakMetodo(urdina);
                taldeakPanela = new TaldeakMetodo(urdina);
                klasifikazioaPanela = new KlasifikazioaMetodo(urdina);
                emaitzakPanela = new EmaitzakMetodo(urdina);
                jokalariakGehituPanela = new JokalariakGehituMetodo(urdina);
                taldeakGehituPanela = new TaldeakGehituMetodo(urdina);
            } catch (Exception e) {
                System.err.println("Errorea panelak sortzerakoan: " + e.getMessage());
                JOptionPane.showMessageDialog(this, 
                    "Errorea interfazearen osagaiak kargatzerakoan: " + e.getMessage(), 
                    "Errorea", JOptionPane.ERROR_MESSAGE);
            }

            // Añadir pestañas
            fitxaPanela.addTab("Partiduak", partiduakPanela.getPanela());
            fitxaPanela.addTab("Fitxaketak", fitxaketakPanela.getPanela());
            fitxaPanela.addTab("Taldeak", taldeakPanela.getPanela());
            fitxaPanela.addTab("Klasifikazioa", klasifikazioaPanela.getPanela());
            fitxaPanela.addTab("Emaitzak", emaitzakPanela.getPanela());
            fitxaPanela.addTab("Jokalariak gehitu", jokalariakGehituPanela.getPanela());
            fitxaPanela.addTab("Taldeak gehitu", taldeakGehituPanela.getPanela());

            fitxaPanela.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    int index = fitxaPanela.getSelectedIndex();
                    if (index == 3 && klasifikazioaPanela != null) { // Klasifikazioa
                        klasifikazioaPanela.eguneratuDenboraldiak(); // Recargar temporadas
                        klasifikazioaPanela.eguneratuTaula();
                    } else if (index == 4 && emaitzakPanela != null) { // Emaitzak
                        emaitzakPanela.eguneratuTaula();
                    }
                }
            });

            panelNagusia.add(fitxaPanela, BorderLayout.CENTER);
            System.out.println("Aplikazioa Erabiltzaileak hasita");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Errorea aplikazio nagusia hasieratzerakoan: " + e.getMessage(), 
                "Errorea Larria", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void eguneratuDena() {
        if (klasifikazioaPanela != null) klasifikazioaPanela.eguneratuTaula();
        if (emaitzakPanela != null) emaitzakPanela.eguneratuTaula();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }
}