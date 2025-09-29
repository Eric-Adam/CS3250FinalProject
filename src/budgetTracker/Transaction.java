package budgetTracker;

public class Transaction{
	private int transactionID;			// unique for each transaction
	private double transactionAmount;	// rounded to 2 decimals just in case
	private String category;			// for larger grouping of transactions (e.g. Household; Car)
	private String note; 				// small phrase for more specific details (e.g. towels, sheets; gas, oil change)
	private boolean incomeExpense; 		// true for income; false for expense

	private static int currentID=0;
	
	
	public Transaction(double transactionAmount, String category, String note, boolean incomeExpense) {
		this.transactionID = ++currentID;
		this.transactionAmount = Math.round(transactionAmount*100)/100;
		this.category = category;
		this.note = note;
		this.incomeExpense = incomeExpense;
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
}
