package Erronka2.view;

import Erronka2.model.*;
import Erronka2.model.dao.JokalariaDAO;
import Erronka2.model.dao.TaldeaDAO;
import Erronka2.model.pojos.Jokalaria;
import Erronka2.model.pojos.Taldea;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class JokalariakGehituMetodo {

    private JPanel panela;
    private JTextField izenaField;
    private JTextField abizenaField;
    private JTextField nanField;
    private JTextField posizioaField;
    private JTextField pisuaField;
    private JTextField altueraField;
    private JTextField herritartasunaField;
    private JComboBox<Taldea> taldeaCombo;
    private JButton argazkiaBotoia;
    private JLabel argazkiaLabel;
    private JButton gordeBotoia;
    private JButton ezeztatuBotoia;

    private JokalariaDAO jokalariaDAO;
    private TaldeaDAO taldeaDAO;

    public JokalariakGehituMetodo(Color kolorea) {
        jokalariaDAO = new JokalariaDAO();
        taldeaDAO = new TaldeaDAO();

        panela = new JPanel(null);
        panela.setBackground(kolorea);

        // Título
        JLabel titulua = new JLabel("JOKALARI BERRIA GEHITU");
        titulua.setForeground(Color.WHITE);
        titulua.setFont(new Font("Arial", Font.BOLD, 32));
        titulua.setBounds(200, 40, 600, 40);
        panela.add(titulua);

        int y = 100;
        int labelX = 200;
        int fieldX = 350;
        int fieldWidth = 250;
        int rowHeight = 40;

        // Izena
        JLabel izenaLabel = new JLabel("Izena:");
        izenaLabel.setForeground(Color.WHITE);
        izenaLabel.setFont(new Font("Arial", Font.BOLD, 16));
        izenaLabel.setBounds(labelX, y, 120, 30);
        panela.add(izenaLabel);

        izenaField = new JTextField();
        izenaField.setBounds(fieldX, y, fieldWidth, 30);
        panela.add(izenaField);
        y += rowHeight;

        // Abizena
        JLabel abizenaLabel = new JLabel("Abizena:");
        abizenaLabel.setForeground(Color.WHITE);
        abizenaLabel.setFont(new Font("Arial", Font.BOLD, 16));
        abizenaLabel.setBounds(labelX, y, 120, 30);
        panela.add(abizenaLabel);

        abizenaField = new JTextField();
        abizenaField.setBounds(fieldX, y, fieldWidth, 30);
        panela.add(abizenaField);
        y += rowHeight;

        // NAN
        JLabel nanLabel = new JLabel("NAN:");
        nanLabel.setForeground(Color.WHITE);
        nanLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nanLabel.setBounds(labelX, y, 120, 30);
        panela.add(nanLabel);

        nanField = new JTextField();
        nanField.setBounds(fieldX, y, fieldWidth, 30);
        panela.add(nanField);
        y += rowHeight;

        // Posizioa
        JLabel posizioaLabel = new JLabel("Posizioa:");
        posizioaLabel.setForeground(Color.WHITE);
        posizioaLabel.setFont(new Font("Arial", Font.BOLD, 16));
        posizioaLabel.setBounds(labelX, y, 120, 30);
        panela.add(posizioaLabel);

        posizioaField = new JTextField();
        posizioaField.setBounds(fieldX, y, fieldWidth, 30);
        panela.add(posizioaField);
        y += rowHeight;

        // Pisua (kg)
        JLabel pisuaLabel = new JLabel("Pisua (kg):");
        pisuaLabel.setForeground(Color.WHITE);
        pisuaLabel.setFont(new Font("Arial", Font.BOLD, 16));
        pisuaLabel.setBounds(labelX, y, 120, 30);
        panela.add(pisuaLabel);

        pisuaField = new JTextField();
        pisuaField.setBounds(fieldX, y, fieldWidth, 30);
        panela.add(pisuaField);
        y += rowHeight;

        // Altuera (m)
        JLabel altueraLabel = new JLabel("Altuera (m):");
        altueraLabel.setForeground(Color.WHITE);
        altueraLabel.setFont(new Font("Arial", Font.BOLD, 16));
        altueraLabel.setBounds(labelX, y, 120, 30);
        panela.add(altueraLabel);

        altueraField = new JTextField();
        altueraField.setBounds(fieldX, y, fieldWidth, 30);
        panela.add(altueraField);
        y += rowHeight;

        // Herritartasuna
        JLabel herritartasunaLabel = new JLabel("Herritartasuna:");
        herritartasunaLabel.setForeground(Color.WHITE);
        herritartasunaLabel.setFont(new Font("Arial", Font.BOLD, 16));
        herritartasunaLabel.setBounds(labelX, y, 120, 30);
        panela.add(herritartasunaLabel);

        herritartasunaField = new JTextField();
        herritartasunaField.setBounds(fieldX, y, fieldWidth, 30);
        panela.add(herritartasunaField);
        y += rowHeight;

        // Taldea
        JLabel taldeaLabel = new JLabel("Taldea:");
        taldeaLabel.setForeground(Color.WHITE);
        taldeaLabel.setFont(new Font("Arial", Font.BOLD, 16));
        taldeaLabel.setBounds(labelX, y, 120, 30);
        panela.add(taldeaLabel);

        taldeaCombo = new JComboBox<>();
        // Añadir opción "Sin equipo" (null)
        taldeaCombo.addItem(null);
        List<Taldea> taldeak = taldeaDAO.getAll();
        for (Taldea t : taldeak) {
            if (t.getTaldeaKod() != 0) { // Ignorar placeholder si existe
                taldeaCombo.addItem(t);
            }
        }
        taldeaCombo.setBounds(fieldX, y, fieldWidth, 30);
        panela.add(taldeaCombo);
        y += rowHeight;

        // Botón para seleccionar foto (placeholder)
        argazkiaBotoia = new JButton("Aukeratu argazkia");
        argazkiaBotoia.setFont(new Font("Arial", Font.BOLD, 14));
        argazkiaBotoia.setBounds(fieldX, y, 200, 35);
        argazkiaBotoia.addActionListener(e ->
            JOptionPane.showMessageDialog(panela, "Argazkia aukeratzeko funtzioa (placeholder)")
        );
        panela.add(argazkiaBotoia);

        argazkiaLabel = new JLabel("Ez da argazkirik hautatu");
        argazkiaLabel.setForeground(Color.LIGHT_GRAY);
        argazkiaLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        argazkiaLabel.setBounds(fieldX, y + 40, 250, 20);
        panela.add(argazkiaLabel);
        y += 60;

        // Botones Gorde y Ezeztatu
        gordeBotoia = new JButton("Gorde");
        gordeBotoia.setFont(new Font("Arial", Font.BOLD, 16));
        gordeBotoia.setBackground(new Color(0, 150, 0));
        gordeBotoia.setForeground(Color.WHITE);
        gordeBotoia.setBounds(280, y, 150, 40);
        gordeBotoia.addActionListener(e -> gordeJokalaria());
        panela.add(gordeBotoia);

        ezeztatuBotoia = new JButton("Ezeztatu");
        ezeztatuBotoia.setFont(new Font("Arial", Font.BOLD, 16));
        ezeztatuBotoia.setBackground(Color.RED);
        ezeztatuBotoia.setForeground(Color.WHITE);
        ezeztatuBotoia.setBounds(480, y, 150, 40);
        ezeztatuBotoia.addActionListener(e -> {
            izenaField.setText("");
            abizenaField.setText("");
            nanField.setText("");
            posizioaField.setText("");
            pisuaField.setText("");
            altueraField.setText("");
            herritartasunaField.setText("");
            taldeaCombo.setSelectedIndex(0);
            argazkiaLabel.setText("Ez da argazkirik hautatu");
        });
        panela.add(ezeztatuBotoia);
    }

    private void gordeJokalaria() {
        try {
            // Validar campos obligatorios
            if (izenaField.getText().trim().isEmpty() ||
                abizenaField.getText().trim().isEmpty() ||
                posizioaField.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("Izena, abizena eta posizioa ezin dira hutsik egon.");
            }

            // Crear objeto Jokalaria
            Jokalaria j = new Jokalaria();
            j.setIzena(izenaField.getText().trim());
            j.setAbizena(abizenaField.getText().trim());
            j.setNan(nanField.getText().trim().isEmpty() ? null : nanField.getText().trim());
            j.setPosizioa(posizioaField.getText().trim());

            // Pisua (opcional, pero si se introduce debe ser entre 50 y 120)
            if (!pisuaField.getText().trim().isEmpty()) {
                try {
                    double pisua = Double.parseDouble(pisuaField.getText().trim());
                    if (pisua < 50 || pisua > 120) {
                        throw new IllegalArgumentException("Pisua 50 eta 120 artean egon behar da.");
                    }
                    j.setPisua(BigDecimal.valueOf(pisua));
                } catch (NumberFormatException ex) {
                    throw new IllegalArgumentException("Pisua zenbaki bat izan behar da.");
                }
            }

            // Altuera (opcional, pero si se introduce debe ser entre 1.50 y 2.20)
            if (!altueraField.getText().trim().isEmpty()) {
                try {
                    double altuera = Double.parseDouble(altueraField.getText().trim());
                    if (altuera < 1.50 || altuera > 2.20) {
                        throw new IllegalArgumentException("Altuera 1.50 eta 2.20 artean egon behar da.");
                    }
                    j.setAltuera(BigDecimal.valueOf(altuera));
                } catch (NumberFormatException ex) {
                    throw new IllegalArgumentException("Altuera zenbaki bat izan behar da.");
                }
            }

            j.setHerritartasuna(herritartasunaField.getText().trim().isEmpty() ? null : herritartasunaField.getText().trim());

            // Taldea (puede ser null)
            Taldea taldea = (Taldea) taldeaCombo.getSelectedItem();
            j.setTaldea(taldea);

            // Guardar en BD
            boolean insertado = jokalariaDAO.insertJokalaria(j);
            if (!insertado) {
                throw new Exception("Errorea jokalaria gordetzean.");
            }

            JOptionPane.showMessageDialog(panela, "Jokalaria ondo gorde da.", "Ondo", JOptionPane.INFORMATION_MESSAGE);
            // Limpiar campos
            ezeztatuBotoia.doClick();

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(panela, ex.getMessage(), "Errorea", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panela, "Errorea: " + ex.getMessage(), "Errorea", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public JPanel getPanela() {
        return panela;
    }
}