package Erronka2.view;

import javax.swing.*;

import Erronka2.model.*;
import Erronka2.model.dao.DenboraldiaDAO;
import Erronka2.model.dao.JardunaldiaDAO;
import Erronka2.model.dao.PartidaDAO;
import Erronka2.model.dao.TaldeaDAO;
import Erronka2.model.pojos.Denboraldia;
import Erronka2.model.pojos.Jardunaldia;
import Erronka2.model.pojos.Partida;
import Erronka2.model.pojos.Taldea;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PartiduakMetodo {

    private JPanel partiduakPanela;

    private JTextField etxekoSetak;
    private JTextField kanpokoSetak;

    private JComboBox<Denboraldia> denboraldiaCombo;
    private JComboBox<Jardunaldia> jardunaldiaCombo;
    private JComboBox<Taldea> etxekoCombo;
    private JComboBox<Taldea> kanpokoCombo;

    private JButton puntuakSartuBotoia;
    private JButton saioaAmaituBotoia;

    private JLabel titulua;
    private JLabel denboraldiaEtiketa;
    private JLabel jardunaldiaEtiketa; 
    private JLabel etxekoEtiketa;
    private JLabel etxekoSetakEtiketa;
    private JLabel kanpokoEtiketa;
    private JLabel kanpokoSetakEtiketa;
    
    private JButton hasiDenboraldiaBotoia;
    
    private Main leihoNagusia;

    // DAOs
    private TaldeaDAO taldeaDAO;
    private PartidaDAO partidaDAO;
    private DenboraldiaDAO denboraldiaDAO;
    private JardunaldiaDAO jardunaldiaDAO;

    public PartiduakMetodo(Color kolorea, Main leihoNagusia) {
        this.leihoNagusia = leihoNagusia;
        taldeaDAO = new TaldeaDAO();
        partidaDAO = new PartidaDAO();
        denboraldiaDAO = new DenboraldiaDAO();
        jardunaldiaDAO = new JardunaldiaDAO();

        partiduakPanela = new JPanel(null);
        partiduakPanela.setBackground(kolorea);

        // Crear todos los componentes primero
        titulua = new JLabel("PARTIDUAK SARTU");
        titulua.setForeground(Color.WHITE);
        titulua.setFont(new Font("Arial", Font.BOLD, 40));
        titulua.setBounds(250, 40, 400, 50);
        partiduakPanela.add(titulua);
        
        hasiDenboraldiaBotoia = new JButton("Hasi Denboraldia");
        hasiDenboraldiaBotoia.setFont(new Font("Arial", Font.BOLD, 16));
        hasiDenboraldiaBotoia.setBackground(Color.GREEN);
        hasiDenboraldiaBotoia.setForeground(Color.BLACK);
        hasiDenboraldiaBotoia.setBounds(600, 90, 200, 40);
        // ActionListener se añade después de crear todos los componentes
        partiduakPanela.add(hasiDenboraldiaBotoia);

        denboraldiaEtiketa = new JLabel("Aukeratu denboraldia:");
        denboraldiaEtiketa.setForeground(Color.WHITE);
        denboraldiaEtiketa.setFont(new Font("Arial", Font.BOLD, 18));
        denboraldiaEtiketa.setBounds(100, 120, 200, 30);
        partiduakPanela.add(denboraldiaEtiketa);

        denboraldiaCombo = new JComboBox<>();
        denboraldiaCombo.setBounds(100, 160, 200, 35);
        partiduakPanela.add(denboraldiaCombo);

        jardunaldiaEtiketa = new JLabel("Aukeratu jardunaldia:");
        jardunaldiaEtiketa.setForeground(Color.WHITE);
        jardunaldiaEtiketa.setFont(new Font("Arial", Font.BOLD, 18));
        jardunaldiaEtiketa.setBounds(320, 120, 200, 30);
        partiduakPanela.add(jardunaldiaEtiketa);

        jardunaldiaCombo = new JComboBox<>();
        jardunaldiaCombo.setBounds(320, 160, 200, 35);
        partiduakPanela.add(jardunaldiaCombo);

        etxekoEtiketa = new JLabel("Etxeko Taldea:");
        etxekoEtiketa.setForeground(Color.WHITE);
        etxekoEtiketa.setFont(new Font("Arial", Font.BOLD, 18));
        etxekoEtiketa.setBounds(100, 220, 200, 30);
        partiduakPanela.add(etxekoEtiketa);

        etxekoCombo = new JComboBox<>();
        etxekoCombo.setBounds(100, 260, 200, 35);
        partiduakPanela.add(etxekoCombo);

        kanpokoEtiketa = new JLabel("Kanpoko Taldea:");
        kanpokoEtiketa.setForeground(Color.WHITE);
        kanpokoEtiketa.setFont(new Font("Arial", Font.BOLD, 18));
        kanpokoEtiketa.setBounds(320, 220, 200, 30);
        partiduakPanela.add(kanpokoEtiketa);

        kanpokoCombo = new JComboBox<>();
        kanpokoCombo.setBounds(320, 260, 200, 35);
        partiduakPanela.add(kanpokoCombo);

        etxekoSetakEtiketa = new JLabel("Setak (0-3):");
        etxekoSetakEtiketa.setForeground(Color.WHITE);
        etxekoSetakEtiketa.setFont(new Font("Arial", Font.BOLD, 16));
        etxekoSetakEtiketa.setBounds(100, 320, 100, 25);
        partiduakPanela.add(etxekoSetakEtiketa);

        etxekoSetak = new JTextField();
        etxekoSetak.setBounds(180, 320, 120, 30);
        partiduakPanela.add(etxekoSetak);

        kanpokoSetakEtiketa = new JLabel("Setak (0-3):");
        kanpokoSetakEtiketa.setForeground(Color.WHITE);
        kanpokoSetakEtiketa.setFont(new Font("Arial", Font.BOLD, 16));
        kanpokoSetakEtiketa.setBounds(320, 320, 100, 25);
        partiduakPanela.add(kanpokoSetakEtiketa);

        kanpokoSetak = new JTextField();
        kanpokoSetak.setBounds(400, 320, 120, 30);
        partiduakPanela.add(kanpokoSetak);

        puntuakSartuBotoia = new JButton("Sartu partidua");
        puntuakSartuBotoia.setBounds(200, 380, 200, 40);
        puntuakSartuBotoia.setFont(new Font("Arial", Font.BOLD, 16));
        partiduakPanela.add(puntuakSartuBotoia);

        saioaAmaituBotoia = new JButton("Saioa amaitu");
        saioaAmaituBotoia.setFont(new Font("Arial", Font.BOLD, 18));
        saioaAmaituBotoia.setBackground(Color.RED);
        saioaAmaituBotoia.setForeground(Color.WHITE);
        saioaAmaituBotoia.setBounds(700, 480, 170, 40);
        partiduakPanela.add(saioaAmaituBotoia);

        // Ahora que todos los componentes están creados, podemos cargar datos y añadir listeners
        cargarDenboraldiak();
        cargarTaldeak();

        // Listeners
        hasiDenboraldiaBotoia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    hasiDenboraldiaBerria();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(partiduakPanela, 
                        "Errorea denboraldia hasteko: " + ex.getMessage(), 
                        "Errorea", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        denboraldiaCombo.addActionListener(e -> {
            Denboraldia selected = (Denboraldia) denboraldiaCombo.getSelectedItem();
            if (selected != null) {
                cargarJardunaldiak(selected.getDenboraldiaKod());
            }
        });

        puntuakSartuBotoia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    procesarPartidua();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(partiduakPanela, 
                        "Errorea partidua sartzerakoan: " + ex.getMessage(), 
                        "Errorea", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        saioaAmaituBotoia.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    SwingUtilities.invokeLater(() -> new Login().setVisible(true));
                    JFrame frame = (JFrame) SwingUtilities.getWindowAncestor((Component) e.getSource());
                    frame.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(partiduakPanela, 
                        "Errorea saioa amaitzerakoan: " + ex.getMessage(), 
                        "Errorea", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        // Cargar jornadas de la primera temporada si existe
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

    private void cargarJardunaldiak(int denboraldiaKod) {
        jardunaldiaCombo.removeAllItems();
        List<Jardunaldia> jardunaldiak = jardunaldiaDAO.getByDenboraldia(denboraldiaKod);
        // Ordenar por fecha para asegurar orden (asumiendo que las fechas están en orden)
        jardunaldiak.sort((a, b) -> a.getHasieraData().compareTo(b.getHasieraData()));
        
        int zenbakia = 1;
        for (Jardunaldia j : jardunaldiak) {
            final int num = zenbakia++;
            // Creamos un objeto anónimo que sobrescribe toString para mostrar el número de jornada
            Jardunaldia jWrapper = new Jardunaldia(j.getJardunaldiKod(), j.getHasieraData(), j.getAmaieraData()) {
                @Override
                public String toString() {
                    return "Jardunaldia " + num;
                }
            };
            jardunaldiaCombo.addItem(jWrapper);
        }
    }

    private void cargarTaldeak() {
        List<Taldea> taldeak = taldeaDAO.getAll();
        for (Taldea t : taldeak) {
            if (t.getTaldeaKod() != 0) {
                etxekoCombo.addItem(t);
                kanpokoCombo.addItem(t);
            }
        }
    }

    private void hasiDenboraldiaBerria() throws Exception {
        String izena = JOptionPane.showInputDialog(partiduakPanela, "Sartu denboraldiaren izena (adibidez: 2025/2026):");
        if (izena == null || izena.trim().isEmpty()) return;

        // Obtener todos los equipos (excluyendo el placeholder con código 0)
        List<Taldea> taldeak = taldeaDAO.getAll();
        taldeak.removeIf(t -> t.getTaldeaKod() == 0);
        if (taldeak.size() < 2) {
            JOptionPane.showMessageDialog(partiduakPanela, "Ez dago talde nahikorik denboraldia hasteko. Gutxienez 2 talde behar dira.");
            return;
        }

        int taldeKopurua = taldeak.size();
        int jardunaldiKopurua = (taldeKopurua - 1) * 2; // Ida y vuelta

        // Crear la temporada
        Denboraldia d = new Denboraldia();
        d.setIzena(izena);
        d.setHasieraData(new Date()); // fecha actual como placeholder
        d.setAmaieraData(new Date());

        boolean insertado = denboraldiaDAO.insert(d);
        if (!insertado) {
            throw new Exception("Errorea denboraldia sortzean.");
        }

        int denboraldiaKod = d.getDenboraldiaKod();

        // Generar jornadas con fechas aproximadas (una por semana)
        java.util.Calendar cal = java.util.Calendar.getInstance();
        Date today = new Date();
        cal.setTime(today);

        for (int i = 1; i <= jardunaldiKopurua; i++) {
            Jardunaldia j = new Jardunaldia();
            cal.setTime(today);
            cal.add(java.util.Calendar.DAY_OF_YEAR, (i-1) * 7);
            j.setHasieraData(cal.getTime());
            j.setAmaieraData(cal.getTime()); // mismo día

            int jardunaldiKod = jardunaldiaDAO.insert(j);
            if (jardunaldiKod == -1) {
                throw new Exception("Errorea " + i + ". jardunaldia sortzean.");
            }

            boolean lotuta = jardunaldiaDAO.asociarATemporada(denboraldiaKod, jardunaldiKod);
            if (!lotuta) {
                throw new Exception("Errorea " + i + ". jardunaldia denboraldiarekin lotzean.");
            }
        }

        JOptionPane.showMessageDialog(partiduakPanela, "Denboraldia ondo sortu da: " + izena + "\n" + jardunaldiKopurua + " jardunaldirekin.");

        // Recargar combo de temporadas
        denboraldiaCombo.removeAllItems();
        cargarDenboraldiak();

        // Seleccionar la nueva temporada automáticamente
        for (int i = 0; i < denboraldiaCombo.getItemCount(); i++) {
            Denboraldia item = denboraldiaCombo.getItemAt(i);
            if (item.getDenboraldiaKod() == denboraldiaKod) {
                denboraldiaCombo.setSelectedIndex(i);
                break;
            }
        }
    }

    private void procesarPartidua() throws Exception {
        Denboraldia denboraldia = (Denboraldia) denboraldiaCombo.getSelectedItem();
        Jardunaldia jardunaldia = (Jardunaldia) jardunaldiaCombo.getSelectedItem();
        Taldea etxekoTaldea = (Taldea) etxekoCombo.getSelectedItem();
        Taldea kanpokoTaldea = (Taldea) kanpokoCombo.getSelectedItem();

        if (denboraldia == null || jardunaldia == null || etxekoTaldea == null || kanpokoTaldea == null) {
            throw new IllegalStateException("Mesedez, bete eremu guztiak.");
        }

        if (etxekoTaldea.getTaldeaKod() == kanpokoTaldea.getTaldeaKod()) {
            throw new IllegalArgumentException("Etxeko eta kanpoko taldea ezin dira berdinak izan.");
        }

        int etxekoS, kanpokoS;
        try {
            etxekoS = Integer.parseInt(etxekoSetak.getText().trim());
            kanpokoS = Integer.parseInt(kanpokoSetak.getText().trim());
        } catch (NumberFormatException ex) {
            throw new NumberFormatException("Mesedez, sartu zenbaki baliodunak setetan.");
        }

        if (etxekoS < 0 || etxekoS > 3 || kanpokoS < 0 || kanpokoS > 3) {
            throw new IllegalArgumentException("Setak 0 eta 3 artean egon behar dira.");
        }
        if (!((etxekoS == 3 && kanpokoS <= 2) || (kanpokoS == 3 && etxekoS <= 2))) {
            throw new IllegalArgumentException("Partidu batek 3 set irabazi behar ditu (bestea 0, 1 edo 2).");
        }
        if ((etxekoS + kanpokoS) > 5) {
            throw new IllegalArgumentException("Set guztien batura ezin da 5 baino handiagoa izan.");
        }

        Partida partida = new Partida();
        partida.setData(new Date());
        partida.setOrdua(new SimpleDateFormat("HH:mm").format(new Date()));
        partida.setEmaitza(etxekoS + "-" + kanpokoS);
        partida.setZigorrak(0);
        partida.setTxartelak(0);
        partida.setEtxekoTaldea(etxekoTaldea);
        partida.setKanpokoTaldea(kanpokoTaldea);
        partida.setJardunaldia(jardunaldia);

        boolean insertado = partidaDAO.insert(partida);
        if (!insertado) {
            throw new Exception("Errorea partidua gordetzean.");
        }

        JOptionPane.showMessageDialog(partiduakPanela, "Partidua ondo gorde da.", "Ondo", JOptionPane.INFORMATION_MESSAGE);
        etxekoSetak.setText("");
        kanpokoSetak.setText("");
        if (leihoNagusia != null) {
            leihoNagusia.eguneratuDena();
        }
    }

    public JPanel getPanela() {
        return partiduakPanela;
    }
}