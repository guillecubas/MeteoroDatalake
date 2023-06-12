package org.example;
import java.sql.*;

public class SQLiteDBHelper {
    private Statement statement;

    public SQLiteDBHelper() {
        //Create a database connection
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:datamart.db");
            statement = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTable(String tableName, String columns) {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " (" + columns + ");";
        try {
            statement.executeUpdate(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertData(String tableName, String columns, String values) {
        String insertDataSQL = "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + values + ");";
        try {
            statement.executeUpdate(insertDataSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public ResultSet selectData(String table, String columns, String fromDate, String toDate) throws SQLException {
        String selectSQL = "SELECT " + columns + " FROM " + table;
        if (fromDate != null && toDate != null) {
            selectSQL += " WHERE date BETWEEN '" + fromDate + "' AND '" + toDate + "'";
        }
        return statement.executeQuery(selectSQL);
    }

    public void dropTable(String table) throws SQLException {
        // Borrar las tablas si existen
        String dropTableSQL = "DROP TABLE IF EXISTS " + table;
        statement.executeUpdate(dropTableSQL);



    }
}