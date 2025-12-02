
package com.example.sqlselector;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

    private static final String URL = "jdbc:mysql://cis3270project.mysql.database.azure.com:3306/cheepskies";
    private static final String USER = "cis3270user";
    private static final String PASSWORD = "Password!";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}