package budgetTracker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Budget {
	private double budgetMax=0;
	private ArrayList<Transaction> income = new ArrayList<Transaction>();
	private ArrayList<Transaction> expenses = new ArrayList<Transaction>();
	private List<Transaction> transactions;
    public final ObservableList<Transaction> observableList = FXCollections.observableArrayList();
    private final String filePath;

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
        
    	if(balance > 0) return balance;
    	else return 0.0;
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
        
    	if(balance > 0) return balance;
    	else return 0.0;
    }
    
    public String getBudgetStatus() {
    	String status="";
    	double currentRemaining = getOverallBalance();
    	if (currentRemaining > (0.4 * budgetMax))
    		status = "UNDER BUDGET";
    	else if (currentRemaining > (0.2 * budgetMax))
    		status = "TIGHT";
    	else if (currentRemaining > (0.05 * budgetMax))
    		status = "AT RISK";
    	else
    		status = "OVER BUDGET";
    	
    	return status;
    }
    
    private void loadTransactions() {
		// Pull data from CSV
		List<String[]> transactionData = loadCSV();
		
		// Convert data to Transaction objects
		transactions = transactionData.stream()
		    .skip(1) // skips header
		    .map(row -> new Transaction(Double.parseDouble(row[0]), 
		    							row[1], 
		    							row[2], 
		    							Boolean.parseBoolean(row[3]), 
		    							LocalDate.parse(row[4])))
		    .collect(Collectors.toList());
		observableList.setAll(transactions);
		
		// Fill income/expense lists
		for (Transaction t : transactions) {
			if (t.isIncome())
				income.add(t);
			else 
				expenses.add(t);
		}
		for (Transaction t : income)
			budgetMax += t.getTransactionAmount();
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
            System.out.println("Failed to load CSV data:\n"+e);
        }
        return rows;	
	}
 
    public void refreshData() {
    	// Clear old data and reload
    	transactions.clear();
    	income.clear();
    	expenses.clear();
    	observableList.clear();
        loadTransactions();
    }
}
