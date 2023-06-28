import java.sql.*;

public class SQLiteDBHelper {
    private Statement statement;

    public SQLiteDBHelper() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:datamart.db");
            statement = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
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

}