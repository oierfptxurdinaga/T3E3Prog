package Erronka2.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import Erronka2.model.*;
import Erronka2.model.dao.PartidaDAO;
import Erronka2.model.pojos.Partida;

public class EmaitzakMetodo {
    
    private JPanel panela;
    private Color urdina;
    private JComboBox<String> denboraldiaCombo;
    private JComboBox<String> jardunaldiaCombo;
    private JTable taula;
    private DefaultTableModel taulaModeloa;

    private PartidaDAO partidaDAO;

    public EmaitzakMetodo(Color urdina) {
        this.urdina = urdina;
        partidaDAO = new PartidaDAO();

        panela = new JPanel(null);
        panela.setBackground(urdina);
        
        JLabel titulua = new JLabel("EMAITZAK", SwingConstants.CENTER);
        titulua.setForeground(Color.WHITE);
        titulua.setFont(new Font("Arial", Font.BOLD, 32));
        titulua.setBounds(0, 40, 900, 40);
        panela.add(titulua);
        
        JLabel denboraldiaEtiketa = new JLabel("Denboraldia:");
        denboraldiaEtiketa.setForeground(Color.WHITE);
        denboraldiaEtiketa.setFont(new Font("Arial", Font.BOLD, 16));
        denboraldiaEtiketa.setBounds(100, 120, 150, 30);
        panela.add(denboraldiaEtiketa);
        
        denboraldiaCombo = new JComboBox<>();
        denboraldiaCombo.addItem("Guztiak");
        denboraldiaCombo.addItem("2022/2023");
        denboraldiaCombo.addItem("2023/2024");
        denboraldiaCombo.addItem("2024/2025");
        denboraldiaCombo.setBounds(100, 160, 180, 35);
        denboraldiaCombo.addActionListener(e -> kargatuEmaitzak());
        panela.add(denboraldiaCombo);
        
        JLabel jardunaldiaEtiketa = new JLabel("Jardunaldia:");
        jardunaldiaEtiketa.setForeground(Color.WHITE);
        jardunaldiaEtiketa.setFont(new Font("Arial", Font.BOLD, 16));
        jardunaldiaEtiketa.setBounds(300, 120, 150, 30);
        panela.add(jardunaldiaEtiketa);
        
        jardunaldiaCombo = new JComboBox<>();
        jardunaldiaCombo.addItem("Guztiak");
        for (int i = 1; i <= 10; i++) {
            jardunaldiaCombo.addItem("Jardunaldia " + i);
        }
        jardunaldiaCombo.setBounds(300, 160, 180, 35);
        jardunaldiaCombo.addActionListener(e -> kargatuEmaitzak());
        panela.add(jardunaldiaCombo);
        
        String[] zutabeak = {"Talde lokala", "Setak lokala", "Setak kanpokoa", "Talde kanpokoa", "Jardunaldia", "Denboraldia"};
        taulaModeloa = new DefaultTableModel(zutabeak, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        taula = new JTable(taulaModeloa);
        taula.setRowHeight(35);
        taula.setFont(new Font("Arial", Font.PLAIN, 14));
        taula.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        
        JScrollPane korritzePanela = new JScrollPane(taula);
        korritzePanela.setBounds(100, 230, 700, 220);
        panela.add(korritzePanela);
        
        JButton saioaAmaituBotoia = new JButton("Saioa amaitu");
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
        
        kargatuEmaitzak();
    }

    private void kargatuEmaitzak() {
        taulaModeloa.setRowCount(0);
        List<Partida> partidak = partidaDAO.getAll();
        // Filtrar según selección (simplificado)
        for (Partida p : partidak) {
            if (p.getEtxekoTaldea() != null && p.getKanpokoTaldea() != null) {
                Object[] row = {
                    p.getEtxekoTaldea().getIzena(),
                    p.getEmaitza().split("-")[0],
                    p.getEmaitza().split("-")[1],
                    p.getKanpokoTaldea().getIzena(),
                    p.getJardunaldia() != null ? p.getJardunaldia().toString() : "",
                    p.getData() != null ? String.valueOf(p.getData().getYear() + 1900) : ""
                };
                taulaModeloa.addRow(row);
            }
        }
    }

    public void eguneratuTaula() {
        kargatuEmaitzak();
    }

    public JPanel getPanela() {
        return panela;
    }
}