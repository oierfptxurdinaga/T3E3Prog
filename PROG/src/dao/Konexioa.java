package dao;

import java.sql.*;

/**
 * MySQL datu-basearekin konexioa kudeatzeko klasea.
 */
public class Konexioa {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/volleyball?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    private Connection konexioa;

    public Konexioa() {
        this.konexioa = null;
    }

    /**
     * Datu-basearekin konexioa irekitzen du.
     * 
     * @return true ondo ireki bada, false bestela.
     */
    public boolean konexioaIreki() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            konexioa = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            return true;
        } catch (ClassNotFoundException e) {
            System.err.println("Errorea: MySQL JDBC driver-a ez da aurkitu.");
            return false;
        } catch (SQLException e) {
            System.err.println("Errorea datu-basearekin konektatzerakoan.");
            return false;
        }
    }

    /**
     * Konexioa ixten du irekita badago.
     */
    public void konexioaItxi() {
        if (konexioa != null) {
            try {
                konexioa.close();
            } catch (SQLException e) {
                System.err.println("Errorea konexioa ixtean.");
            }
        }
    }

    /**
     * @return Uneko konexio objektua, edo null itxita badago.
     */
    public Connection getKonexioa() {
        return konexioa;
    }

    /**
     * @return true konexioa irekita badago.
     */
    public boolean konexioaIrekitaDago() {
        try {
            return konexioa != null && !konexioa.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}