package Erronka2.view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import Erronka2.model.*;
import Erronka2.model.dao.DenboraldiaDAO;
import Erronka2.model.pojos.Denboraldia;
import Erronka2.model.pojos.TaldearenKlasifikazioa;

public class KlasifikazioaMetodo {

    private JPanel panela;
    private JButton saioaAmaituBotoia;
    private JComboBox<Denboraldia> denboraldiaCombo;
    private JTable klasifikazioaTaula;
    private DefaultTableModel taulaModeloa;
    private JButton gordeBotoia;
    private JButton kargatuBotoia;
    private JButton amaituDenboraldiaBotoia;

    private KlasifikazioaService klasifikazioaService;
    private DenboraldiaDAO denboraldiaDAO;

    public KlasifikazioaMetodo(Color urdina) {
        klasifikazioaService = new KlasifikazioaService();
        denboraldiaDAO = new DenboraldiaDAO();

        panela = new JPanel(null);
        panela.setBackground(urdina);

        JLabel titulua = new JLabel("KLASIFIKAZIOA");
        titulua.setForeground(Color.WHITE);
        titulua.setFont(new Font("Arial", Font.BOLD, 40));
        titulua.setBounds(320, 20, 400, 50);
        panela.add(titulua);

        denboraldiaCombo = new JComboBox<>();
        cargarDenboraldiak();
        denboraldiaCombo.setFont(new Font("Arial", Font.BOLD, 18));
        denboraldiaCombo.setBounds(100, 90, 200, 40);
        denboraldiaCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    kargatuKlasifikazioa();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panela, 
                        "Errorea klasifikazioa kargatzerakoan: " + ex.getMessage(), 
                        "Errorea", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });
        panela.add(denboraldiaCombo);

        amaituDenboraldiaBotoia = new JButton("Amaitu Denboraldia");
        amaituDenboraldiaBotoia.setFont(new Font("Arial", Font.BOLD, 16));
        amaituDenboraldiaBotoia.setBackground(Color.ORANGE);
        amaituDenboraldiaBotoia.setForeground(Color.BLACK);
        amaituDenboraldiaBotoia.setBounds(350, 90, 200, 40);
        amaituDenboraldiaBotoia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    amaituDenboraldia();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panela, 
                        "Errorea denboraldia amaitzerakoan: " + ex.getMessage(), 
                        "Errorea", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });
        panela.add(amaituDenboraldiaBotoia);

        String[] zutabeak = {"Posizioa", "Taldea", "PJ", "PG", "PP", "Puntuak", "SI", "SG", "SD"};
        taulaModeloa = new DefaultTableModel(zutabeak, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        klasifikazioaTaula = new JTable(taulaModeloa);
        klasifikazioaTaula.setFont(new Font("Arial", Font.PLAIN, 14));
        klasifikazioaTaula.setFillsViewportHeight(true);
        klasifikazioaTaula.setShowGrid(true);
        klasifikazioaTaula.setGridColor(Color.LIGHT_GRAY);

        DefaultTableCellRenderer zentratu = new DefaultTableCellRenderer();
        zentratu.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < klasifikazioaTaula.getColumnCount(); i++) {
            klasifikazioaTaula.getColumnModel().getColumn(i).setCellRenderer(zentratu);
        }

        JScrollPane korritzePanela = new JScrollPane(klasifikazioaTaula);
        korritzePanela.setBounds(100, 150, 700, 250);
        panela.add(korritzePanela);

        gordeBotoia = new JButton("Gorde Klasifikazioa");
        gordeBotoia.setFont(new Font("Arial", Font.BOLD, 16));
        gordeBotoia.setBackground(new Color(0, 150, 0));
        gordeBotoia.setForeground(Color.WHITE);
        gordeBotoia.setBounds(200, 420, 200, 40);
        gordeBotoia.addActionListener(e -> {
            // Lógica de guardado (opcional)
        });
        panela.add(gordeBotoia);

        kargatuBotoia = new JButton("Kargatu Klasifikazioa");
        kargatuBotoia.setFont(new Font("Arial", Font.BOLD, 16));
        kargatuBotoia.setBackground(new Color(0, 100, 200));
        kargatuBotoia.setForeground(Color.WHITE);
        kargatuBotoia.setBounds(450, 420, 200, 40);
        kargatuBotoia.addActionListener(e -> {
            try {
                kargatuKlasifikazioa();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panela, 
                    "Errorea klasifikazioa kargatzerakoan: " + ex.getMessage(), 
                    "Errorea", JOptionPane.ERROR_MESSAGE);
            }
        });
        panela.add(kargatuBotoia);

        saioaAmaituBotoia = new JButton("Saioa amaitu");
        saioaAmaituBotoia.setFont(new Font("Arial", Font.BOLD, 18));
        saioaAmaituBotoia.setBackground(Color.RED);
        saioaAmaituBotoia.setForeground(Color.WHITE);
        saioaAmaituBotoia.setBounds(700, 480, 170, 40);
        saioaAmaituBotoia.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> new Login().setVisible(true));
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor((Component) e.getSource());
            frame.dispose();
        });
        panela.add(saioaAmaituBotoia);

        if (denboraldiaCombo.getItemCount() > 0) {
            denboraldiaCombo.setSelectedIndex(0);
        }
    }

    private void cargarDenboraldiak() {
        List<Denboraldia> denboraldiak = denboraldiaDAO.getAll();
        for (Denboraldia d : denboraldiak) {
            denboraldiaCombo.addItem(d);
        }
    }

    public void eguneratuDenboraldiak() {
        // Guardar selección actual
        Denboraldia selected = (Denboraldia) denboraldiaCombo.getSelectedItem();
        int selectedId = (selected != null) ? selected.getDenboraldiaKod() : -1;

        // Recargar items
        denboraldiaCombo.removeAllItems();
        cargarDenboraldiak();

        // Restaurar selección si es posible
        if (selectedId != -1) {
            for (int i = 0; i < denboraldiaCombo.getItemCount(); i++) {
                Denboraldia d = denboraldiaCombo.getItemAt(i);
                if (d.getDenboraldiaKod() == selectedId) {
                    denboraldiaCombo.setSelectedIndex(i);
                    return;
                }
            }
        }
        // Si no, seleccionar el primero
        if (denboraldiaCombo.getItemCount() > 0) {
            denboraldiaCombo.setSelectedIndex(0);
        }
    }

    private void kargatuKlasifikazioa() throws Exception {
        taulaModeloa.setRowCount(0);
        Denboraldia denboraldia = (Denboraldia) denboraldiaCombo.getSelectedItem();
        if (denboraldia == null) return;

        List<TaldearenKlasifikazioa> klasifikazioa = klasifikazioaService.getKlasifikazioaDenboraldian(denboraldia.getDenboraldiaKod());
        int pos = 1;
        for (TaldearenKlasifikazioa tk : klasifikazioa) {
            Object[] row = {
                pos++,
                tk.getTaldea().getIzena(),
                tk.getPartidaJokatuak(),
                tk.getPartidaIrabaziak(),
                tk.getPartidaGalduak(),
                tk.getPuntuak(),
                tk.getSetakIrabaziak(),
                tk.getSetakGalduak(),
                tk.getSetDiferentzia()
            };
            taulaModeloa.addRow(row);
        }
    }

    private void amaituDenboraldia() throws Exception {
        JOptionPane.showMessageDialog(panela, "Denboraldia amaituta (funtzioa ez dago guztiz inplementatuta)");
    }

    public void eguneratuTaula() {
        try {
            kargatuKlasifikazioa();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panela, 
                "Errorea taula eguneratzerakoan: " + e.getMessage(), 
                "Errorea", JOptionPane.ERROR_MESSAGE);
        }
    }

    public JPanel getPanela() {
        return panela;
    }
}