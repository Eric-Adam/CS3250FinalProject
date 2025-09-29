package budgetTracker;

public class Income extends Transaction{
	// exists just to overload constructor and make things more clear outside
	public Income(double transactionAmount, String category, String note) {
		super(transactionAmount, category, note, true);
	}
	public Income(double transactionAmount, String category) {
		super(transactionAmount, category, "-", true);
	}
}
