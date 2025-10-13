package budgetTracker;
import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Budget {
	private double budgetMax=0;
	private ArrayList<Transaction> income = new ArrayList<Transaction>();
	private ArrayList<Transaction> expenses = new ArrayList<Transaction>();
	private List<Transaction> transactions = null;
    public final ObservableList<Transaction> observableList = FXCollections.observableArrayList();


	
	public Budget() {
		// Load data
		loadTransactions();
		
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
	// TODO: Can't handle commas in note, think of work around
	private List<String[]> loadCSV() {
		String filePath = "src/resources/transactionDB.csv"; 
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


	public double getBudgetMax() {
		return budgetMax;
	}
	public void setBudgetMax(double budgetMax) {
		this.budgetMax = budgetMax;
	}
	
	// Adds tracked transactions
	public void addIncome(Transaction income) {
        this.income.add(income);
        JOptionPane.showMessageDialog(null, 
        		"$"+income.getTransactionAmount()+" added to income");
    }
    public void addExpense(Transaction expense) {
        this.expenses.add(expense);
        JOptionPane.showMessageDialog(null, 
        		"Expense of $"+expense.getTransactionAmount()+" added.");
    }
    
    // Removes tracked transactions
	public void removeIncome(Transaction in) {
        income.remove(in);
        JOptionPane.showMessageDialog(null, 
        		"$"+in.getTransactionAmount()+" removed from income");
    }
    public void removeExpense(Transaction expense) {
        expenses.remove(expense);
        JOptionPane.showMessageDialog(null, 
        		"Expense of $"+expense.getTransactionAmount()+" removed.");
    }

    public double getOverallBalance() {
    	double runningIncome = 0;
    	double runningExpense = 0;
    	double balance = 0;
    	
    	for (Transaction in: income) {
    		runningIncome += in.getTransactionAmount();
    	}
    	for (Transaction out: expenses) {
    		runningExpense += out.getTransactionAmount();
    	}
    	balance = runningIncome - runningExpense;
        
    	if(balance > 0)
    		return balance;
    	else
    		return 0.0;
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
    }
 
    public void refreshData() {
    	// Clear old data and reload
        observableList.clear();
        loadTransactions();
    }

}
