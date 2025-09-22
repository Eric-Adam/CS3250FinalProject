
public class Transaction{
	private int transactionID;
	private double transactionAmount;
	private Category category;
	
	private static int currentID=0;
	
	
	public Transaction(double transactionAmount) {
		this.transactionID = ++currentID;
		this.transactionAmount = transactionAmount;
	}

	public int getTransactionID() {
		return transactionID;
	}

	public double getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public Category getCategory() {
		return category;
	}


	

}
