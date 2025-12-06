package budgetTracker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class MyDatabase {
    private Connection conn;

    /**
     * Database object to connect to database
     * 
     * @param dbFile Name of .db file with relative location
     * @throws SQLException 
     */
    public MyDatabase(String dbFile) throws SQLException {
        String url = "jdbc:sqlite:" + dbFile;
        conn = DriverManager.getConnection(url);
    }

    /**
     * Returns the connection to the database
     * 
     * @return Connection conn to database
     */
    public Connection getConnection() {
        return conn;
    }
    
    /**
     * Returns users in database list 'Users'
     * 
     * @return ResultSet of Username, and Password_Hash
     * @throws SQLException
     */
    public ResultSet getUsers() throws SQLException {
		// SQL statement for selecting user names 
		String sql = "SELECT Username, Password_Hash \r\n"
				+ "FROM Users\r\n"
				+ "ORDER by Username ASC;";	
		
		// Create statement
        Statement stmt = conn.createStatement();
        
        // Execute query
        return stmt.executeQuery(sql);
    }

    /**
     * Adds user to database
     * 
     * @param user User being added to database
     * @throws SQLException
     */
	public void addUser(NewUser user) throws SQLException {
		LocalDate today = LocalDate.now();
		
		// SQL statement for inserting user name
		String sqlName = "INSERT INTO Users (Username, Password_Hash)\r\n"
				+ String.format("VALUES ('%s', '%s');", 
						user.getFullName(),
						user.getHashedPassword());	
		
		// SQL statement for adding initial transaction
		String sqlTransaction = "INSERT INTO Transactions (Amount, Category, Note, Income, Date, Owner)\r\n"
				+ String.format("VALUES (%.2f, 'Miscellaneous','Initial Transaction',1, '%s','%s');", 
						user.getInitialAmount(), today, user.getFullName());	
				
				
		// Push new user data to database
		// --- Create statement
	    Statement stmt = conn.createStatement();
	        
	    // --- Execute statements
        stmt.execute(sqlName);
        stmt.execute(sqlTransaction);
	}
}
