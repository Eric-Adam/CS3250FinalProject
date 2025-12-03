package budgetTracker;

import org.mindrot.jbcrypt.BCrypt;

public class NewUser {
	private String fullName;
	private String filePath;
	private Double initialAmount;	
	private String hashedPassword;
	
	public NewUser(String fName, String lName, Double initialAmount) {	
		// Clear Whitespace
		fName = fName.trim();
		lName = lName.trim();
		// Set Variables
		this.setFullName(fName, lName);
		this.setFilePath("src/resources/"+fName+lName+"DB.csv");
		this.setInitialAmount(initialAmount);
	}
	
	public NewUser(String fName, String lName, Double initialAmount, String password) {
		this(fName, lName, initialAmount);
		setHashedPassword(hashPassword(password));
	}

	// Hash password to send to database
	private String hashPassword(String password) {
		return BCrypt.hashpw(password, BCrypt.gensalt());
	}

	// Getters and Setters
	public String getFullName() {return fullName;}
	public void setFullName(String fName, String lName) {
		this.fullName = fName + " " + lName;}
	
	public String getFilePath() {return filePath;}
	public void setFilePath(String filePath) {
		this.filePath = filePath;}
	
	public Double getInitialAmount() {return initialAmount;}
	public void setInitialAmount(Double initialAmount) {
		this.initialAmount = initialAmount;}
	
	public String getHashedPassword() {return hashedPassword;}
	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;}
	
	
	

	
	
}
