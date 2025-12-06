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
	
    private final DoubleProperty transactionAmount = new SimpleDoubleProperty();
    private final StringProperty category = new SimpleStringProperty();
    private final StringProperty note = new SimpleStringProperty();
    private final BooleanProperty income = new SimpleBooleanProperty();
    private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
   	private final IntegerProperty databaseID = new SimpleIntegerProperty();
	
	/**
	 * Transactions for budget
	 * 
	 * @param transactionAmount The amount of the transaction, limited to positive and 2 decimals by GUI
	 * @param category For larger grouping of transactions (e.g. Household; Car)
	 * @param note Small phrase for more specific details (e.g. towels, sheets; gas, oil change)
	 * @param income True for income; false for expense
	 * @param date Formatted as yyyy-mm-dd
	 * @param id ID in database for editing/deleting needs
	 */
	public Transaction(double transactionAmount, String category, String note, boolean income, LocalDate date, int id) {
		this.databaseID.set(id);
        this.transactionAmount.set(transactionAmount);
        this.category.set(category);
        this.note.set(note);
        this.income.set(income);
        this.date.set(date);
	}

	// Getters and setters for property values
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
    
    public int getId() { return databaseID.get();}
    public void setId(int id) { this.databaseID.set(id);}
	public IntegerProperty idProperty() { return databaseID;}
}

