package budgetTracker;
import java.time.LocalDate;

public class Income extends Transaction{
	public Income(double transactionAmount, String category, String note, LocalDate date) {
		super(transactionAmount, category, note, true, date);
	}
}
