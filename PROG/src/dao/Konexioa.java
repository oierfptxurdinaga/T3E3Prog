package dao;

import java.sql.*;

/**
 * Datu-basearen konexioa kudeatzeko klasea.
 * MySQL datu-basearekin konexioa ireki eta ixteko metodoak eskaintzen ditu,
 * JDBC driverra erabiliz.
 */
public class Konexioa {
    
    // Datu-basearen konexiorako datuak (volleyball datu-basea)
    private static final String DB_URL = "jdbc:mysql://localhost:3306/volleyball?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    
    private Connection konexioa;
    
    /**
     * Konexioa klasearen eraikitzailea.
     * Hasieratzean, konexio objektua null gisa definitzen du.
     */
    public Konexioa() {
        this.konexioa = null;
    }
    
    /**
     * Datu-basearekin konexioa irekitzen saiatzen da MySQL JDBC driverra kargatuz.
     * * @return true konexioa ondo ireki bada, false errore bat egon bada (driverra ez da aurkitu edo SQL errorea).
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
     * Konexioa ixten du, irekita badago.
     * SQLException erroreak kudeatzen ditu konexioa ixtean arazoren bat egonez gero.
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
     * Uneko konexio objektua lortzeko metodoa.
     * * @return {@link Connection} objektua, edo null konexioa ireki ez bada.
     */
    public Connection getKonexioa() {
        return konexioa;
    }
    
    /**
     * Konexioa une honetan irekita eta erabilgarri dagoen egiaztatzen du.
     * * @return true konexioa irekita badago, false itxita badago edo null bada.
     */
    public boolean konexioaIrekitaDago() {
        try {
            return konexioa != null && !konexioa.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}