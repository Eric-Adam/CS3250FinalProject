
public class Expense extends Transaction{
	private String justification;
	private boolean need;

	public Expense(double transactionAmount, boolean need, String justification) {
		super(transactionAmount);
		this.need = need;
		this.justification = justification;
	}
	public Expense(double transactionAmount, boolean need) {
		super(transactionAmount);
		this.need = need;
	}

	public String getJustification() {
		return justification;
	}

	public void setJustification(String justification) {
		this.justification = justification;
	}

	public boolean isNeed() {
		return need;
	}

	public void setNeed(boolean need) {
		this.need = need;
	}


}
