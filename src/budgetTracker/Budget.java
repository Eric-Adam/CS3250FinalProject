package budgetTracker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import myGUI.RunGUI;

public class Budget {	
	private ArrayList<Transaction> income = new ArrayList<Transaction>();
	private ArrayList<Transaction> expenses = new ArrayList<Transaction>();
	private String name;
	
	public  ObservableList<Transaction> transactions = FXCollections.observableArrayList();
	public static ObservableList<Transaction> staticTransactions = FXCollections.observableArrayList();
	
	private static Stage primaryStage;
	
	public static MyDatabase db;
	public static final String[] incomeCategories = 
			{"Gifts","Household","Insurance","Loan","Retirement",
			 "Salary","Transfer","Miscellaneous"};
	public static final String[] expenseCategories = 
		{"Books","Car","Clothing","Credit Card","Entertainment",
		 "Events","Gifts","Groceries","Health","Household",
		 "Insurance","Internet","Loan","Personal Care","Pets",
		 "Phone","Rent","Retirement","Savings","School",
		 "Spouse","Subscriptions","Takeout/Delivery",
		 "Transfer","Travel/Transport","Utilities","Miscellaneous"};
	public static final String[] categories = 
		{"Books","Car","Clothing","Credit Card","Entertainment",
   		 "Events","Gifts","Groceries","Health","Household","Insurance",
   		 "Internet","Loan","Personal Care","Pets","Phone",
   		 "Rent","Retirement","Salary", "Savings","School","Spouse",
   		 "Subscriptions","Takeout/Delivery","Transfer","Travel","Utilities",
   		 "Miscellaneous"};
	
	/**
	 * Budget containing the transaction data for user
	 * 
	 * @param runGUI Instance of RunGUI connected to stage
	 * @param name Name of the user 
	 */
	public Budget(RunGUI runGUI, String name) {
		setDB(runGUI.getDB());
		setName(name);
		setStage(runGUI.primaryStage);
		getTransactions();
	}

	/**
	 * Calculates and returns current balance
	 * 
	 * @return current balance
	 */
    public double getOverallBalance() {
    	double balance = 0;
    	double tempAmount =0;
    	
    	for (Transaction transaction : transactions) {
        		tempAmount = transaction.getTransactionAmount();
        		balance += (transaction.isIncome()) ? tempAmount : -tempAmount;
    	}
    	
    	if(balance < 0.0)
			balance = 0.0;
    	return balance;
    }
    
    /**
     * Calculates and returns balance from an earlier date 
     * 
     * @param date Date of desired balance
     * @return Balance on the date given
     */
    public double getEarlierBalance(LocalDate date) {
    	double balance = 0;
    	double tempAmount =0;
    	
    	for (Transaction transaction : transactions) {
        	if (transaction.getDate().isBefore(date)) {
        		tempAmount = transaction.getTransactionAmount();
        		balance += (transaction.isIncome()) ? tempAmount : -tempAmount;
        	}
    	}
    	if(balance < 0.0)
			balance = 0.0;
        
    	return balance;
    }
    
    /**
     * Sums transactions and returns total
     * 
     * @param list List of transactions to be summed
     * @return total Total of summed transactions
     */
    private double sumTransactions(ArrayList<Transaction> list) {
		double total = 0;
    	
    	for (Transaction item : list) {
    		total += item.getTransactionAmount();
    	}
        
    	return total;
    }
    
    /**
     * Returns sum of expense transactions
     * 
     * @return Sum of expense transactions
     */
    public double getTotalExpenses() {
    	return sumTransactions(expenses);
    }
    
    /**
     *  Returns sum of income transactions
     *  
     * @return Sum of income transactions
     */
    public double getTotalIncome() {
    	return sumTransactions(income);
    }
    
    /**
     * determines budget status based on ratio of remaining balance and total income
     * 
     * @return Status of budget
     */
    public String getBudgetStatus() {
    	String status="";
    	double currentRemaining = getOverallBalance();
    	if (currentRemaining > (0.4 * getTotalIncome()))
    		status = "UNDER BUDGET";
    	else if (currentRemaining > (0.2 * getTotalIncome()))
    		status = "TIGHT";
    	else if (currentRemaining > (0.05 * getTotalIncome()))
    		status = "AT RISK";
    	else
    		status = "OVER BUDGET";
    	
    	return status;
    }
	
    /**
     * Fill income/expense lists
     */
	private void fillIncomeExpense() {
		
		for (Transaction t : transactions) {
			if (t.isIncome()) {
				income.add(t);
			}
			else 
				expenses.add(t);
		}
	}

	/**
	 * Load transactions from SQL database
	 */
	public void getTransactions() {
		// Start fresh
		transactions.clear();
		
		// SQL statement for selecting transactions
		String sql = "SELECT Amount, Category, Note, Income, Date, ID \r\n"
				+ "FROM Transactions\r\n"
				+ "WHERE owner = '" + name + "'\r\n"
				+ "ORDER by Date DESC;";	
		
		// Pull transactions from database
		try {
			Connection conn = db.getConnection();
	        Statement stmt = conn.createStatement();
	        ResultSet rs = stmt.executeQuery(sql);
	        
			double amount;
			String category, note;
			Boolean income;
			LocalDate date;
			int id;
			
	        while (rs.next()) {
	        	amount   = rs.getDouble("Amount");
	            category = rs.getString("Category");
	            note     = rs.getString("Note");
	            income   = (rs.getInt("Income")==1) ? true: false;
	            date     = LocalDate.parse(rs.getString("Date"));
	            id       = rs.getInt("ID");
	            
	            // Add transactions to observable list
	            transactions.add(new Transaction(amount, category, note, income, date, id));
	        }
	        
		} catch (Exception e) {
	    	System.out.println("Failed to load transaction from database");
	        e.printStackTrace();
	    }
		
		fillIncomeExpense();
	}
	
	/**
	 * Refreshes data for when changes are made
	 */
    public void refreshData() {
    	// Clear old data and reload
    	income.clear();
    	expenses.clear();
    	
    	getTransactions();
    }
         
	/**
	 * Adds new transaction to database
	 * 
	 * @param transaction Transaction to be edited
	 * @param owner User who made the transaction
	 */
	public static void addTransaction(Transaction transaction, String owner) {
		int income = transaction.isIncome()? 1:0;
		
		// SQL statement for adding transaction
		String sqlTransaction = "INSERT INTO Transactions (Amount, Category, Note, Income, Date, Owner)\r\n"
				+ String.format("VALUES (%.2f, '%s', '%s', %d, '%s', '%s');",
						transaction.getTransactionAmount(),
						transaction.getCategory(),
						transaction.getNote(),
						income,
						transaction.getDate().toString(),
						owner);	
				
				
		// Push new transaction to database
		try {
			Connection conn = db.getConnection();
	        Statement stmt = conn.createStatement();

	        stmt.execute(sqlTransaction);
			
	        
		} catch (Exception e) {
	    	System.out.println("Failed to add transaction to database");
	        e.printStackTrace();
	    }
	}
	
	
	/**
	 * Edits transaction in database
	 * 
	 * @param transaction Transaction to be edited
	 */
	public static void editTransaction(Transaction transaction) {
		int income = transaction.isIncome()? 1:0;
				
		// SQL statement for editing a transaction
		String sqlEdit = "UPDATE Transactions \r\n"
				+"SET Amount = ?, Category = ?, Note = ?, Income = ?, Date = ? \r\n"
				+"WHERE ID = ?";
	
	
		// Push new transaction to database
		try {
			Connection conn = db.getConnection();
		    PreparedStatement stmt = conn.prepareStatement(sqlEdit);
		    
		    stmt.setDouble(1, transaction.getTransactionAmount());
		    stmt.setString(2, transaction.getCategory());
		    stmt.setString(3, transaction.getNote());
		    stmt.setInt(4, income);
		    stmt.setString(5, transaction.getDate().toString());
		    stmt.setInt(6, transaction.getId());
		
		    stmt.executeUpdate();
			
		    
		} catch (Exception e) {
			System.out.println("Failed to edit transaction");
		    e.printStackTrace();
		}
		
	}
	
	/**
	 * Removes transaction from database
	 * 
	 * @param transaction Transaction to be deleted
	 */
	public static void deleteTransaction(Transaction transaction) {
				
		// SQL statement for deleting a transaction
		String sqlDelete = "DELETE FROM Transactions \r\n"
				+String.format("WHERE ID = %d", transaction.getId());
	
		// Push deletion to database
		try {
			Connection conn = db.getConnection();
		    Statement stmt = conn.createStatement();

		    stmt.execute(sqlDelete);	
		    
		} catch (Exception e) {
			System.out.println("Failed to edit transaction");
		    e.printStackTrace();
		}
		
	}

	
	/**
	 * Exports static transaction list to CSV file
	 */
	public static void exportCSV() {
		// Set necessary variables
		String[] header = {"transactionAmount","category","note","income","date"};
    	List<String> transStrings = new ArrayList<String>();
    	DateTimeFormatter  formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		// Let user select location and name
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save File");
        fileChooser.setInitialFileName("Transactions.csv");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home"), "Desktop"));
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV File", "*.csv")
            );
        
		try {
			File file = fileChooser.showSaveDialog(primaryStage);
	    	
	    	// Catch user cancellation
	    	if (file == null) 
	    		return;
	    	
	    	// Add header
	    	FileWriter writer = new FileWriter(file, false);
			for (int i = 0; i < header.length; i++) {
		        writer.append(header[i]);
		        if (i < header.length - 1) {
		            writer.append(",");
		        }
			}
		    writer.append("\n");
		     		    
		    // Run through every transaction
			for (Transaction trans : staticTransactions) {
				transStrings.clear();
				transStrings.add(Double.toString(trans.getTransactionAmount()));
				transStrings.add(escapeForCSV(trans.getCategory()));
				transStrings.add(escapeForCSV(trans.getNote()));
				transStrings.add(Boolean.toString(trans.isIncome()));
		 		transStrings.add(formatter.format(trans.getDate()));
				
		 		// Add transaction to file
				for (int i = 0; i < transStrings.size(); i++) {
			        writer.append(transStrings.get(i));
			        if (i < transStrings.size() - 1) {
			            writer.append(",");
			        }
			    }
			    writer.append("\n");
		 	}
			
			writer.close();
			showAlert(String.format("Export to %s Successfull", file.getName()),"Export Success");
			
 		} catch (IOException e) {
 			showAlert("Failed to save to CSV", "Save Failure");
 			e.printStackTrace();
 		}    	
	}
	
 	/**
 	 * Escapes double quotes and removes commas for exporting to CSV 
 	 * 
 	 * @param value String being adjusted
 	 * @return String with suitable double quotes and no commas
 	 */
    private static String escapeForCSV(String value) {
 	    if (value.contains("\"")) {
 	        value = value.replace("\"", "\"\"");
 	        return "\"" + value + "\"";
 	    }	
 	    if (value.contains(","))
 	    		value = value.replace(",", "");
 	    return value;
 	}
	

	
	/**
	 * Display alert for given message and title
	 * @param message Message to be delivered
	 * @param title Title for alert
	 */
	public static void showAlert(String message, String title) {
		Alert alert = new Alert(AlertType.NONE, message, ButtonType.OK);
		
		alert.setTitle(title);
		alert.initOwner(primaryStage);
		alert.showAndWait();
	}

	
	// Getters and setters
	public Stage getStage() {return primaryStage;}
	public String getName() {return name;}
	
	public void setStage(Stage stage) {Budget.primaryStage = stage;}
	public void setName(String name) {this.name = name;}
	public static void setStaticTransactions(ObservableList<Transaction> transList) {
		Budget.staticTransactions = transList;}
	public void setDB(MyDatabase db) {Budget.db = db;}
}
