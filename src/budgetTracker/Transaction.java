package budgetTracker;
import java.io.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.*;
import javafx.beans.property.*;

public class Transaction{
	/**
	 * Changed private variables to Simple__Property to use TableView for history
	 */
	
	// Unique for each transaction
	private final IntegerProperty transactionID = new SimpleIntegerProperty();
	// Limited to positive and 2 decimals by GUI
    private final DoubleProperty transactionAmount = new SimpleDoubleProperty();
    // For larger grouping of transactions (e.g. Household; Car)
    private final StringProperty category = new SimpleStringProperty();
    // Small phrase for more specific details (e.g. towels, sheets; gas, oil change)
    private final StringProperty note = new SimpleStringProperty();
    // True for income; false for expense
    private final BooleanProperty income = new SimpleBooleanProperty();
    // Formatted as yyyy-mm-dd
    private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    
	private static int currentID=0;
	
	
	public Transaction(double transactionAmount, String category, String note, boolean income, LocalDate date) {
        this.transactionID.set(++currentID);
        this.transactionAmount.set(transactionAmount);
        this.category.set(category);
        this.note.set(note);
        this.income.set(income);
        this.date.set(date);
	}

	// Getters and setters for property values
    public int getTransactionID() { return transactionID.get(); }
    public void setTransactionID(int id) { this.transactionID.set(id); }
    public IntegerProperty transactionIDProperty() { return transactionID; }

    public double getTransactionAmount() { return transactionAmount.get(); }
    public void setTransactionAmount(double amount) { this.transactionAmount.set(amount); }
    public DoubleProperty transactionAmountProperty() { return transactionAmount; }

    public String getCategory() { return category.get(); }
    public void setCategory(String category) { this.category.set(category); }
    public StringProperty categoryProperty() { return category; }

    public String getNote() { return note.get(); }
    public void setNote(String note) { this.note.set(note); }
    public StringProperty noteProperty() { return note; }

    public boolean isIncome() { return income.get(); }
    public void setIncome(boolean income) { this.income.set(income); }
    public BooleanProperty incomeProperty() { return income; }

    public LocalDate getDate() { return date.get(); }
    public void setDate(LocalDate date) { this.date.set(date); }
    public ObjectProperty<LocalDate> dateProperty() { return date; }
	
	// Appends to CSV
	public static void saveToCSV(String fileName, Transaction trans) {
		List<String> arr = new ArrayList<String>();
		arr.add(Double.toString(trans.getTransactionAmount()));
		arr.add(escapeForCSV(trans.getCategory()));
		arr.add(escapeForCSV(trans.getNote()));
		arr.add(Boolean.toString(trans.isIncome()));
		DateTimeFormatter  formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		arr.add(formatter.format(trans.getDate()));
		File file = new File(fileName);
		
		try (FileWriter writer = new FileWriter(file, true)) {
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
    
	// Escapes double quotes and removes commas
	private static String escapeForCSV(String value) {
	    if (value.contains(",") || value.contains("\"")) {
	        value = value.replace("\"", "\"\"");
	        value = value.replace(",", "");
	        return "\"" + value + "\"";
	    }	

	    return value;
	}
}
