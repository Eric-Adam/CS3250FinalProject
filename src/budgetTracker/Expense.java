package budgetTracker;
import java.time.LocalDate;

// Exists to separate income from expenses

public class Expense extends Transaction{
	public Expense(double transactionAmount, String category, String note, LocalDate date, int id) {
		super(transactionAmount, category, note, false, date, id);
	}
}
