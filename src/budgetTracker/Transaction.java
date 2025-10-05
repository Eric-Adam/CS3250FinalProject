package budgetTracker;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Transaction{
	private int transactionID;			// unique for each transaction
	private double transactionAmount;	// rounded to 2 decimals just in case
	private String category;			// for larger grouping of transactions (e.g. Household; Car)
	private String note; 				// small phrase for more specific details (e.g. towels, sheets; gas, oil change)
	private boolean incomeExpense; 		// true for income; false for expense
	private LocalDate date;

	private static int currentID=0;
	
	
	public Transaction(double transactionAmount, String category, String note, boolean incomeExpense, LocalDate date) {
		this.transactionID = ++currentID;
		this.transactionAmount = transactionAmount;
		this.category = category;
		this.note = note;
		this.incomeExpense = incomeExpense;
		this.date = date;
	}

	// no setter to ensure the ID is unique
	public int getTransactionID() { 
		return transactionID;
	}
	
	public double getTransactionAmount() {
		return transactionAmount;
	}
	public void setTransactionAmount(double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
	
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	// no setter to separate Income and Expense
	public boolean isIncomeExpense() {
		return incomeExpense;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	// Save to CSV
	public static void saveToCSV(String fileName, Transaction trans) {
		List<String> arr = new ArrayList<String>();
		arr.add(Double.toString(trans.getTransactionAmount()));
		arr.add(escapeForCSV(trans.getCategory()));
		arr.add(escapeForCSV(trans.getNote()));
		arr.add(Boolean.toString(trans.isIncomeExpense()));
		DateTimeFormatter  formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		arr.add(formatter.format(trans.getDate()));
	
		try (FileWriter writer = new FileWriter(fileName)) {
			for (int i = 0; i < arr.size(); i++) {
		        writer.append(arr.get(i));
		        if (i < arr.size() - 1) {
		            writer.append(",");
		        }
		    }
		    writer.append("\n");
		    
		} catch (IOException e) {
			System.out.println("Failed to write to file");
		}
	}
		
	// Escape double quotes
	private static String escapeForCSV(String value) {
	    if (value.contains(",") || value.contains("\"")) {
	        value = value.replace("\"", "\"\""); 
	        return "\"" + value + "\"";
	    }
	    return value;
	}
}
