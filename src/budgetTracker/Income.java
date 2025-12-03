package budgetTracker;
import java.time.LocalDate;

//Exists to separate income from expenses

public class Income extends Transaction{
	public Income(double transactionAmount, String category, String note, LocalDate date, int id) {
		super(transactionAmount, category, note, true, date, id);
	}
}
