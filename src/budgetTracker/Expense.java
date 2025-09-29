package budgetTracker;

public class Expense extends Transaction{
	// exists just to overload constructor and make things more clear outside
	public Expense(double transactionAmount, String category, String note) {
		super(transactionAmount, category, note, false);
	}
	public Expense(double transactionAmount, String category) {
		super(transactionAmount, category, "-", false);
	}

}
