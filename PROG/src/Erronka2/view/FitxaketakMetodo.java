package Erronka2.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import Erronka2.model.*;
import Erronka2.model.dao.JokalariaDAO;
import Erronka2.model.dao.TaldeaDAO;
import Erronka2.model.pojos.Jokalaria;
import Erronka2.model.pojos.Taldea;

public class FitxaketakMetodo {

    private JPanel panela;
    private Color urdina;
    private JComboBox<Taldea> taldeaCombo;
    private JList<Jokalaria> jokalariakZerrenda;
    private DefaultListModel<Jokalaria> zerrendaModeloa;
    private JComboBox<Taldea> helburuTaldeaCombo; 
    
    private JButton saioaAmaituBotoia;
    private JButton traspasatuBotoia;
    
    private JLabel titulua;
    private JLabel taldeaEtiketa;
    private JLabel jokalariakEtiketa;
    private JLabel helburuTaldeaEtiketa;
    
    private JScrollPane korritzePanelaJokalariak;
    private JLabel abisuaEtiketa;

    // DAOs
    private TaldeaDAO taldeaDAO;
    private JokalariaDAO jokalariaDAO;

    public FitxaketakMetodo(Color urdina) {
        this.urdina = urdina;
        taldeaDAO = new TaldeaDAO();
        jokalariaDAO = new JokalariaDAO();

        panela = new JPanel(null);
        panela.setBackground(urdina);

        titulua = new JLabel("FITXAKETAK");
        titulua.setForeground(Color.WHITE);
        titulua.setFont(new Font("Arial", Font.BOLD, 32));
        titulua.setBounds(350, 40, 300, 40);
        panela.add(titulua);
        
        abisuaEtiketa = new JLabel("");
        abisuaEtiketa.setForeground(Color.YELLOW);
        abisuaEtiketa.setFont(new Font("Arial", Font.BOLD, 16));
        abisuaEtiketa.setBounds(100, 100, 700, 30);
        abisuaEtiketa.setHorizontalAlignment(SwingConstants.CENTER);
        panela.add(abisuaEtiketa);

        taldeaEtiketa = new JLabel("Aukeratu taldea:");
        taldeaEtiketa.setForeground(Color.WHITE);
        taldeaEtiketa.setFont(new Font("Arial", Font.BOLD, 18));
        taldeaEtiketa.setBounds(100, 150, 200, 30);
        panela.add(taldeaEtiketa);

        taldeaCombo = new JComboBox<>();
        // Cargar equipos desde BD
        List<Taldea> taldeak = taldeaDAO.getAll();
        for (Taldea t : taldeak) {
            // Ignorar el placeholder "-" si existe (código 0)
            if (t.getTaldeaKod() != 0) {
                taldeaCombo.addItem(t);
            }
        }
        taldeaCombo.setBounds(100, 190, 250, 35);
        panela.add(taldeaCombo);

        jokalariakEtiketa = new JLabel("Jokalariak:");
        jokalariakEtiketa.setForeground(Color.WHITE);
        jokalariakEtiketa.setFont(new Font("Arial", Font.BOLD, 18));
        jokalariakEtiketa.setBounds(100, 240, 200, 30);
        panela.add(jokalariakEtiketa);

        zerrendaModeloa = new DefaultListModel<>();
        jokalariakZerrenda = new JList<>(zerrendaModeloa);
        korritzePanelaJokalariak = new JScrollPane(jokalariakZerrenda);
        korritzePanelaJokalariak.setBounds(100, 280, 250, 200);
        panela.add(korritzePanelaJokalariak);

        helburuTaldeaEtiketa = new JLabel("Traspasatu nahi den taldea:");
        helburuTaldeaEtiketa.setForeground(Color.WHITE);
        helburuTaldeaEtiketa.setFont(new Font("Arial", Font.BOLD, 18));
        helburuTaldeaEtiketa.setBounds(450, 150, 300, 30);
        panela.add(helburuTaldeaEtiketa);

        helburuTaldeaCombo = new JComboBox<>();
        for (Taldea t : taldeak) {
            if (t.getTaldeaKod() != 0) {
                helburuTaldeaCombo.addItem(t);
            }
        }
        helburuTaldeaCombo.setBounds(450, 190, 250, 35);
        panela.add(helburuTaldeaCombo);

        traspasatuBotoia = new JButton("Traspasatu");
        traspasatuBotoia.setBounds(475, 280, 200, 40);
        traspasatuBotoia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    traspasatuJokalariaGUI();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panela, 
                        "Errorea jokalaria traspasatzerakoan: " + ex.getMessage(), 
                        "Errorea", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });
        panela.add(traspasatuBotoia);

        saioaAmaituBotoia = new JButton("Saioa amaitu");
        saioaAmaituBotoia.setFont(new Font("Arial", Font.BOLD, 18));
        saioaAmaituBotoia.setBackground(Color.RED);
        saioaAmaituBotoia.setForeground(Color.WHITE);
        saioaAmaituBotoia.setBounds(700, 480, 170, 40);
        saioaAmaituBotoia.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    SwingUtilities.invokeLater(() -> new Login().setVisible(true));
                    JFrame frame = (JFrame) SwingUtilities.getWindowAncestor((Component) e.getSource());
                    frame.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panela, 
                        "Errorea saioa amaitzerakoan: " + ex.getMessage(), 
                        "Errorea", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });
        panela.add(saioaAmaituBotoia);

        taldeaCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    kargatuJokalariak();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panela, 
                        "Errorea jokalariak kargatzerakoan: " + ex.getMessage(), 
                        "Errorea", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        try {
            kargatuJokalariak();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panela, 
                "Errorea hasierako jokalariak kargatzerakoan: " + e.getMessage(), 
                "Errorea", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        eguneratuInterfazeaDenboraldia();
    }

    private void eguneratuInterfazeaDenboraldia() {
        // Aquí deberías obtener el estado de la temporada desde DenboraldiaDAO
        // Por ahora, mantenemos un booleano estático o lo dejamos siempre habilitado
        // Para simplificar, asumimos que siempre se puede traspasar (ajusta según tu lógica)
        traspasatuBotoia.setEnabled(true);
        taldeaCombo.setEnabled(true);
        helburuTaldeaCombo.setEnabled(true);
        abisuaEtiketa.setText("Fitxaketak egin daitezke.");
        abisuaEtiketa.setForeground(Color.YELLOW);
    }

    private void kargatuJokalariak() throws Exception {
        zerrendaModeloa.clear();

        Taldea hautatutakoTaldea = (Taldea) taldeaCombo.getSelectedItem();

        if (hautatutakoTaldea == null) {
            throw new IllegalStateException("Ez da talderik aukeratu.");
        }

        if (hautatutakoTaldea.getTaldeaKod() != 0) {
            List<Jokalaria> jokalariak = jokalariaDAO.getByTaldea(hautatutakoTaldea.getTaldeaKod());

            for (Jokalaria j : jokalariak) {
                zerrendaModeloa.addElement(j);
            }
        }
    }

    private void traspasatuJokalariaGUI() throws Exception {
        // Aquí deberías comprobar si la temporada está activa (similar a antes)
        // Por ahora, asumimos permitido.

        Jokalaria hautatutakoJokalaria = jokalariakZerrenda.getSelectedValue();

        if (hautatutakoJokalaria == null) {
            throw new IllegalArgumentException("Mesedez, aukeratu jokalari bat traspasatzeko.");
        }

        Taldea helburuTaldea = (Taldea) helburuTaldeaCombo.getSelectedItem();

        if (helburuTaldea == null || helburuTaldea.getTaldeaKod() == 0) {
            throw new IllegalArgumentException("Mesedez, aukeratu talde helburu balido bat.");
        }

        if (hautatutakoJokalaria.getTaldea() != null && hautatutakoJokalaria.getTaldea().getTaldeaKod() == helburuTaldea.getTaldeaKod()) {
            throw new IllegalArgumentException("Jokalaria hau talde honetan dago.");
        }

        int erantzuna = JOptionPane.showConfirmDialog(panela,
                hautatutakoJokalaria.getIzena() + " jokalaria " + helburuTaldea.getIzena() + " taldera traspasatu nahi duzu?",
                "Traspasoa baieztatu",
                JOptionPane.YES_NO_OPTION);

        if (erantzuna == JOptionPane.YES_OPTION) {
            String taldeJatorriIzena = hautatutakoJokalaria.getTaldea() != null ? hautatutakoJokalaria.getTaldea().getIzena() : "Talde gabe";

            boolean traspasoArrakastatsua = jokalariaDAO.traspasatu(hautatutakoJokalaria, helburuTaldea);

            if (!traspasoArrakastatsua) {
                throw new Exception("Traspasoa huts egin du.");
            }

            kargatuJokalariak();

            JOptionPane.showMessageDialog(null,
                    hautatutakoJokalaria.getIzena() + " traspasatu da " + taldeJatorriIzena
                            + " taldetik " + helburuTaldea.getIzena() + " taldera.",
                    "Traspasoa burututa",
                    JOptionPane.INFORMATION_MESSAGE);

            System.out.println("TRASPASOA: " + hautatutakoJokalaria.getIzena() + " " + taldeJatorriIzena
                    + "-tik " + helburuTaldea.getIzena() + "-ra Erabiltzaileak");
        }
    }

    public JPanel getPanela() {
        return panela;
    }

    // Getters...
}