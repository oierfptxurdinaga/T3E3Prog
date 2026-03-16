package Erronka2.view;

import Erronka2.model.*;
import Erronka2.model.dao.TaldeaDAO;
import Erronka2.model.dao.ZelaiaDAO;
import Erronka2.model.pojos.Taldea;
import Erronka2.model.pojos.Zelaia;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TaldeakGehituMetodo {

    private JPanel panela;
    private JTextField izenaField;
    private JTextField sortzeDataField;
    private JComboBox<Zelaia> zelaiaCombo;
    private JButton eskudoaBotoia;
    private JLabel eskudoaLabel;
    private JButton gordeBotoia;
    private JButton ezeztatuBotoia;

    private TaldeaDAO taldeaDAO;
    private ZelaiaDAO zelaiaDAO;

    public TaldeakGehituMetodo(Color kolorea) {
        taldeaDAO = new TaldeaDAO();
        zelaiaDAO = new ZelaiaDAO();

        panela = new JPanel(null);
        panela.setBackground(kolorea);

        // Título
        JLabel titulua = new JLabel("TALDE BERRIA GEHITU");
        titulua.setForeground(Color.WHITE);
        titulua.setFont(new Font("Arial", Font.BOLD, 32));
        titulua.setBounds(250, 40, 500, 40);
        panela.add(titulua);

        int y = 120;
        int labelX = 200;
        int fieldX = 370;
        int fieldWidth = 250;
        int rowHeight = 45;

        // Izena
        JLabel izenaLabel = new JLabel("Taldearen izena:");
        izenaLabel.setForeground(Color.WHITE);
        izenaLabel.setFont(new Font("Arial", Font.BOLD, 18));
        izenaLabel.setBounds(labelX, y, 150, 30);
        panela.add(izenaLabel);

        izenaField = new JTextField();
        izenaField.setBounds(fieldX, y, fieldWidth, 35);
        panela.add(izenaField);
        y += rowHeight;

        // Sortze data
        JLabel sortzeDataLabel = new JLabel("Sortze data (yyyy-MM-dd):");
        sortzeDataLabel.setForeground(Color.WHITE);
        sortzeDataLabel.setFont(new Font("Arial", Font.BOLD, 16));
        sortzeDataLabel.setBounds(labelX, y, 200, 30);
        panela.add(sortzeDataLabel);

        sortzeDataField = new JTextField();
        sortzeDataField.setBounds(fieldX, y, fieldWidth, 35);
        sortzeDataField.setToolTipText("Adibidez: 2020-01-15");
        panela.add(sortzeDataField);
        y += rowHeight;

        // Zelaia (campo)
        JLabel zelaiaLabel = new JLabel("Zelaia:");
        zelaiaLabel.setForeground(Color.WHITE);
        zelaiaLabel.setFont(new Font("Arial", Font.BOLD, 18));
        zelaiaLabel.setBounds(labelX, y, 150, 30);
        panela.add(zelaiaLabel);

        zelaiaCombo = new JComboBox<>();
        // Añadir opción "Ez aukeratu" (null)
        zelaiaCombo.addItem(null);
        List<Zelaia> zelaiak = zelaiaDAO.getAll();
        for (Zelaia z : zelaiak) {
            zelaiaCombo.addItem(z);
        }
        zelaiaCombo.setBounds(fieldX, y, fieldWidth, 35);
        panela.add(zelaiaCombo);
        y += rowHeight;

        // Botón para seleccionar escudo (placeholder)
        eskudoaBotoia = new JButton("Aukeratu eskudoa");
        eskudoaBotoia.setFont(new Font("Arial", Font.BOLD, 14));
        eskudoaBotoia.setBounds(fieldX, y, 200, 35);
        eskudoaBotoia.addActionListener(e ->
            JOptionPane.showMessageDialog(panela, "Eskudoa aukeratzeko funtzioa (placeholder)")
        );
        panela.add(eskudoaBotoia);

        eskudoaLabel = new JLabel("Ez da eskudoirik hautatu");
        eskudoaLabel.setForeground(Color.LIGHT_GRAY);
        eskudoaLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        eskudoaLabel.setBounds(fieldX, y + 40, 250, 20);
        panela.add(eskudoaLabel);
        y += 60;

        // Botones Gorde y Ezeztatu
        gordeBotoia = new JButton("Gorde");
        gordeBotoia.setFont(new Font("Arial", Font.BOLD, 16));
        gordeBotoia.setBackground(new Color(0, 150, 0));
        gordeBotoia.setForeground(Color.WHITE);
        gordeBotoia.setBounds(270, y, 150, 40);
        gordeBotoia.addActionListener(e -> gordeTaldea());
        panela.add(gordeBotoia);

        ezeztatuBotoia = new JButton("Ezeztatu");
        ezeztatuBotoia.setFont(new Font("Arial", Font.BOLD, 16));
        ezeztatuBotoia.setBackground(Color.RED);
        ezeztatuBotoia.setForeground(Color.WHITE);
        ezeztatuBotoia.setBounds(470, y, 150, 40);
        ezeztatuBotoia.addActionListener(e -> {
            izenaField.setText("");
            sortzeDataField.setText("");
            zelaiaCombo.setSelectedIndex(0);
            eskudoaLabel.setText("Ez da eskudoirik hautatu");
        });
        panela.add(ezeztatuBotoia);
    }

    private void gordeTaldea() {
        try {
            // Validar campo obligatorio
            if (izenaField.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("Taldearen izena ezin da hutsik egon.");
            }

            // Crear objeto Taldea
            Taldea t = new Taldea();
            t.setIzena(izenaField.getText().trim());

            // Sortze data (opcional)
            if (!sortzeDataField.getText().trim().isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                sdf.setLenient(false);
                try {
                    Date data = sdf.parse(sortzeDataField.getText().trim());
                    t.setSortzeData(data);
                } catch (ParseException ex) {
                    throw new IllegalArgumentException("Data formatua ez da zuzena. Erabili yyyy-MM-dd (adibidez: 2020-01-15)");
                }
            }

            // Zelaia (puede ser null)
            Zelaia zelaia = (Zelaia) zelaiaCombo.getSelectedItem();
            t.setZelaia(zelaia);

            // Guardar en BD
            boolean insertado = taldeaDAO.insertTaldea(t);
            if (!insertado) {
                throw new Exception("Errorea taldea gordetzean.");
            }

            JOptionPane.showMessageDialog(panela, "Taldea ondo gorde da.", "Ondo", JOptionPane.INFORMATION_MESSAGE);
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