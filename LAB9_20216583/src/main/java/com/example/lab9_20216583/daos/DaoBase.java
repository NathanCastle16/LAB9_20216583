package com.example.lab9_20216583.daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DaoBase<T> {

    private static final String URL = "jdbc:mysql://localhost:3306/mydb?serverTimezone=America/Lima&useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "12345678";

    protected Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontró el driver JDBC de MySQL", e);
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public abstract void crear(T entidad) throws SQLException;

    public abstract void borrar(int id) throws SQLException;
}