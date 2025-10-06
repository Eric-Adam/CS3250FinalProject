package budgetTracker;
import java.time.LocalDate;

public class Expense extends Transaction{
	public Expense(double transactionAmount, String category, String note, LocalDate date) {
		super(transactionAmount, category, note, false, date);
	}
}
