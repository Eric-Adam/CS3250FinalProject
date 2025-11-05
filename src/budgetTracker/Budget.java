package budgetTracker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
	private final String filePath;
	
	public ObservableList<Transaction> transactions = FXCollections.observableArrayList();
    public static final String[] categories = {"Books","Car","Clothing","Credit Card","Entertainment",
								   		 "Events","Gifts","Groceries","Household","Insurance",
								   		 "Internet","Loan","Personal Care","Pets","Phone",
								   		 "Rent","Retirement","Salary", "Savings","School","Spouse",
								   		 "Subscriptions","Takeout/Delivery","Travel","Utilities","VA",
								   		 "Miscellaneous"};
    
    

	public Budget(String filePath) {
		// Load data
		this.filePath = filePath;
		loadTransactions();		
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
    
    private void loadTransactions() {
		// Pull data from CSV
		List<String[]> transactionData = loadCSV();
		
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
		
		// Fill income/expense lists
		for (Transaction t : transactions) {
			if (t.isIncome()) {
				income.add(t);
			}
			else 
				expenses.add(t);
		}			
	}
    
	// Loads data from CSV file as unparsed strings 
	private List<String[]> loadCSV() {
        List<String[]> rows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                rows.add(values); 
            }
        } catch (Exception e) {
            System.out.println("Failed to load CSV data:\n" + e);
        }
        return rows;	
	}
 
    public void refreshData() {
    	// Clear old data and reload
    	income.clear();
    	expenses.clear();
    	transactions.clear();
        loadTransactions();
    }
         
 	// Escapes double quotes and removes commas
 	private static String escapeForCSV(String value) {
 	    if (value.contains(",") || value.contains("\"")) {
 	        value = value.replace("\"", "\"\"");
 	        value = value.replace(",", "");
 	        return "\"" + value + "\"";
 	    }	
 	    if (value.contains(","))
 	    		value = value.replace(",", "");
 	    return value;
 	}
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
    	refreshData();
    }
}
