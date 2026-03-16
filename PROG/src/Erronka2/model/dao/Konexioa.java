package Erronka2.model.dao;

import java.sql.*;

/**
 * Datu-basearen konexioa kudeatzeko klasea.
 * MySQL datu-basearekin konexioa ireki eta ixteko metodoak eskaintzen ditu.
 */
public class Konexioa {
    
    // Datu-basearen konexiorako datuak (volleyball datu-basea)
    private static final String DB_URL = "jdbc:mysql://localhost:3306/volleyball?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    
    private Connection konexioa;
    
    /**
     * Eraikitzailea. Hasieran konexioa null da.
     */
    public Konexioa() {
        this.konexioa = null;
    }
    
    /**
     * Datu-basearekin konexioa irekitzen saiatzen da.
     * @return true konexioa ondo ireki bada, bestela false
     */
    public boolean konexioaIreki() {
        try {
            // MySQL JDBC driver-a kargatu (beharrezkoa Java 8 eta zaharragoentzat)
            Class.forName("com.mysql.cj.jdbc.Driver");
            konexioa = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            return true;
        } catch (ClassNotFoundException e) {
            System.err.println("Errorea: MySQL JDBC driver-a ez da aurkitu.");
            e.printStackTrace();
            return false;
        } catch (SQLException e) {
            System.err.println("Errorea datu-basearekin konektatzean.");
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Konexioa ixten du.
     */
    public void konexioaItxi() {
        if (konexioa != null) {
            try {
                konexioa.close();
            } catch (SQLException e) {
                System.err.println("Errorea konexioa ixtean.");
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Konexio objektua itzultzen du.
     * @return Connection objektua edo null
     */
    public Connection getKonexioa() {
        return konexioa;
    }
    
    /**
     * Konexioa irekita dagoen egiaztatzen du.
     * @return true irekita badago, bestela false
     */
    public boolean konexioaEgonean() {
        try {
            return konexioa != null && !konexioa.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}