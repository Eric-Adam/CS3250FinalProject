package budgetTracker;

public class NewUser {
	private String fullName;
	private String filePath;
	private Double initialAmount;
	
	public NewUser(String fName, String lName, Double initialAmount) {	
		// Clear Whitespace
		fName = fName.trim();
		lName = lName.trim();
		// Set Variables
		this.setFullName(fName, lName);
		this.setFilePath("src/resources/"+fName+lName+"DB.csv");
		this.setInitialAmount(initialAmount);
	}

	public String getFullName() {return fullName;}
	public String getFilePath() {return filePath;}
	public Double getInitialAmount() {return initialAmount;}
	
	public void setFullName(String fName, String lName) {
		this.fullName = fName + " " + lName;}
	public void setFilePath(String filePath) {this.filePath = filePath;}
	public void setInitialAmount(Double initialAmount) {this.initialAmount = initialAmount;}

}
