package budgetTracker;

import org.mindrot.jbcrypt.BCrypt;

public class NewUser {
	private String fullName;
	private Double initialAmount;	
	private String hashedPassword;
	
	/**
	 * Condenses user data for easier user creation
	 * 
	 * @param fName new user's first name
	 * @param lName New user's last name
	 * @param initialAmount The initial amount to add to the database, limited to positive and 2 decimals by GUI
	 * @param password New user's password
	 */
	public NewUser(String fName, String lName, Double initialAmount, String password) {
		// Clear Whitespace
		fName = fName.trim();
		lName = lName.trim();
		
		// Set Variables
		setFullName(fName, lName);
		setInitialAmount(initialAmount);
		setHashedPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
	}

	// Getters and Setters
	public String getFullName() {return fullName;}
	public void setFullName(String fName, String lName) {
		this.fullName = fName + " " + lName;}
	
	public Double getInitialAmount() {return initialAmount;}
	public void setInitialAmount(Double initialAmount) {
		this.initialAmount = initialAmount;}
	
	public String getHashedPassword() {return hashedPassword;}
	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;}	
}
