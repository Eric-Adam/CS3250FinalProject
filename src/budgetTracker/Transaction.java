package budgetTracker;

import java.time.LocalDate;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;


public class Transaction{
	
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

}
