package budgetTracker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDatabase {
    private Connection conn;

    public MyDatabase(String dbFile) throws SQLException {
        String url = "jdbc:sqlite:" + dbFile;
        conn = DriverManager.getConnection(url);
    }

    public Connection getConnection() {
        return conn;
    }
}
