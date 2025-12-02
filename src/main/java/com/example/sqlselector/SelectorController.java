package com.example.sqlselector;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.sql.*;

public class SelectorController {
    @FXML
    private Button execute;

    @FXML
    private TextField textfield;

    @FXML
    private TextArea textarea;

    @FXML
    public void executeQuery(MouseEvent event) {
        String query = textfield.getText().trim();

        // Check if query is empty
        if (query.isEmpty()) {
            textarea.setText("ERROR: Please enter a SQL query.");
            return;
        }

        // Validate query type
        String queryType = validateQueryType(query);
        if (queryType == null) {
            textarea.setText("ERROR: Only SELECT, UPDATE, and DELETE queries are allowed.");
            return;
        }

        // Execute the query
        try {
            if (queryType.equals("SELECT")) {
                executeSelectQuery(query);
            } else {
                executeUpdateOrDeleteQuery(query, queryType);
            }
        } catch (SQLException e) {
            textarea.setText("SQL ERROR: " + e.getMessage());
        } catch (Exception e) {
            textarea.setText("CONNECTION ERROR: " + e.getMessage());
        }
    }

    private String validateQueryType(String query) {
        String upperQuery = query.toUpperCase().trim();

        if (upperQuery.startsWith("SELECT")) {
            return "SELECT";
        } else if (upperQuery.startsWith("UPDATE")) {
            return "UPDATE";
        } else if (upperQuery.startsWith("DELETE")) {
            return "DELETE";
        }

        return null;
    }

    private void executeSelectQuery(String query) throws SQLException {
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            StringBuilder result = new StringBuilder();
            result.append("Query executed successfully!\n\n");

            // Display column headers
            for (int i = 1; i <= columnCount; i++) {
                result.append(String.format("%-20s", metaData.getColumnName(i)));
            }
            result.append("\n");
            result.append("-".repeat(columnCount * 20)).append("\n");

            // Display rows
            int rowCount = 0;
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    Object value = rs.getObject(i);
                    String displayValue = (value == null) ? "NULL" : value.toString();
                    result.append(String.format("%-20s", displayValue));
                }
                result.append("\n");
                rowCount++;
            }

            result.append("\n").append(rowCount).append(" row(s) returned.");
            textarea.setText(result.toString());
        }
    }

    private void executeUpdateOrDeleteQuery(String query, String queryType) throws SQLException {
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement()) {

            int rowsAffected = stmt.executeUpdate(query);

            textarea.setText(
                    "Query executed successfully!\n\n" +
                            queryType + " operation completed.\n" +
                            rowsAffected + " row(s) affected."
            );
        }
    }
}
