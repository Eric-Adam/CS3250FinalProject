package budgetTracker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Budget {
	private ArrayList<Transaction> income = new ArrayList<Transaction>();
	private ArrayList<Transaction> expenses = new ArrayList<Transaction>();
	private String filePath;
	private String name;
	
	public ObservableList<Transaction> transactions = FXCollections.observableArrayList();
	public MyDatabase db;
	
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
	
	// Original constructor using CSV files
	public Budget(String filePath) {
		// Load data
		this.filePath = filePath;
		loadTransactions();		
	}	
	
	// New constructor utilizing SQL database
	public Budget(MyDatabase db, String name) {
		setDB(db);
		setName(name);
		getTransactions();
	}

	
	public void setDB(MyDatabase db) {
		this.db = db;
	}
	public String getFilePath() {
		return this.filePath;
	}

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
    
    public double getEarlierBalance(LocalDate startDate) {
    	double balance = 0;
    	double tempAmount =0;
    	
    	for (Transaction transaction : transactions) {
        	if (transaction.getDate().isBefore(startDate)) {
        		tempAmount = transaction.getTransactionAmount();
        		balance += (transaction.isIncome()) ? tempAmount : -tempAmount;
        	}
    	}
    	if(balance < 0.0)
			balance = 0.0;
        
    	return balance;
    }
    
    public double getTotalExpenses() {
    	double total = 0;
    	
    	for (Transaction out : expenses) {
    		total += out.getTransactionAmount();
    	}
        
    	return total;
    }
    
    public double getTotalIncome() {
    	double total = 0;
    	
    	for (Transaction in : income) 
        		total += in.getTransactionAmount();
        		
    	return total;
    }
    
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
    
    // TODO: Remove? Obsolete due to using database instead of CSV files
    // Load transactions from CSV file
    private void loadTransactions() {
		// Pull data from CSV
		List<String[]> transactionData = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                transactionData.add(values); 
            }
        } catch (Exception e) {
            System.out.println("Failed to load CSV data:\n" + e);
        }
		
		// Convert data to Transaction objects
		List<Transaction> transactionList = transactionData.stream()
		    .skip(1) // skip header
		    .map(row -> new Transaction(Double.parseDouble(row[0]), 
		    							row[1], 
		    							row[2], 
		    							Boolean.parseBoolean(row[3]), 
		    							LocalDate.parse(row[4])))
		    .collect(Collectors.toList());
		transactions.setAll(transactionList);
		
		fillIncomeExpense();
	}
	
    // Fill income/expense lists
	private void fillIncomeExpense() {
		
		for (Transaction t : transactions) {
			if (t.isIncome()) {
				income.add(t);
			}
			else 
				expenses.add(t);
		}
	}

	// Load transactions from SQL database
	public void getTransactions() {
		// Start fresh
		transactions.clear();
		
		// SQL statement for selecting transactions
		String sql = "SELECT Amount, Category, Note, Income, Date \r\n"
				+ "FROM Transactions\r\n"
				+ "WHERE owner = '" + name + "'\r\n"
				+ "ORDER by Date DESC;";	
		
		// Pull transactions from database
		try (Connection conn = db.getConnection();
		        Statement stmt = conn.createStatement();
		        ResultSet rs = stmt.executeQuery(sql)) {
			
				double amount;
				String category, note;
				Boolean income;
				LocalDate date;
				
		        while (rs.next()) {
		        	amount   = rs.getDouble("Amount");
		            category = rs.getString("Category");
		            note     = rs.getString("Note");
		            income   = (rs.getInt("Income")==1) ? true: false;
		            date     = LocalDate.parse(rs.getString("Date"));
		            
		            // Add transactions to observable list
		            transactions.add(new Transaction(amount, category, note, income, date));
		        }

		    } catch (Exception e) {
		    	System.out.println("Failed to load transaction from database");
		        e.printStackTrace();
		    }
		
		fillIncomeExpense();
	}
	
    public void refreshData() {
    	// Clear old data and reload
    	income.clear();
    	expenses.clear();
    	transactions.clear();
//      loadTransactions();
    	getTransactions();
    }
         
	// TODO: Remove? Obsolete due to using database instead of CSV files
 	// Escapes double quotes and removes commas 
 	private static String escapeForCSV(String value) {
 	    if (value.contains("\"")) {
 	        value = value.replace("\"", "\"\"");
 	        return "\"" + value + "\"";
 	    }	
 	    if (value.contains(","))
 	    		value = value.replace(",", "");
 	    return value;
 	}
 	
 	// TODO: Remove? Obsolete due to using database instead of CSV files
    public void overwrite() {
    	File file = new File(filePath);
    	String[] header = {"transactionAmount","category","note","income","date"};
    	List<String> transStrings = new ArrayList<String>();
    	DateTimeFormatter  formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    	
    	// Add header - Overwrites file
    	try (FileWriter writer = new FileWriter(file, false)) {
 			for (int i = 0; i < header.length; i++) {
 		        writer.append(header[i]);
 		        if (i < header.length - 1) {
 		            writer.append(",");
 		        }
 		    }
 		    writer.append("\n");
 		    
 		} catch (IOException e) {
 			System.out.println("Failed to write to file");
 		}
    	
    	// Sort transactions by date
    	FXCollections.sort(transactions, (t1, t2) -> t1.getDate().compareTo(t2.getDate()));
    	
    	// Add transactions back to file
    	for (Transaction trans : transactions) {
    		transStrings.clear();
    		transStrings.add(Double.toString(trans.getTransactionAmount()));
    		transStrings.add(escapeForCSV(trans.getCategory()));
    		transStrings.add(escapeForCSV(trans.getNote()));
    		transStrings.add(Boolean.toString(trans.isIncome()));
	 		transStrings.add(formatter.format(trans.getDate()));
 		
	 		try (FileWriter writer = new FileWriter(file, true)) {
				for (int i = 0; i < transStrings.size(); i++) {
			        writer.append(transStrings.get(i));
			        if (i < transStrings.size() - 1) {
			            writer.append(",");
			        }
			    }
			    writer.append("\n");
			    
			} catch (IOException e) {
				System.out.println("Failed to write transaction to file");
			}
    	}
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
