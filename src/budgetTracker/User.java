package budgetTracker;

public class User {
	private String fullName;
	private String filePath;
	private String password;
	
	public User(String fName, String lName, String password) {
		this.setFullName(fName, lName);
		this.setPassword(password);
		this.setFilePath(fName+lName+"DB.csv");
	}

	public String getFullName() {return fullName;}
	public String getFilePath() {return filePath;}
	public String getPassword() {return password;}
	
	public void setFullName(String fName, String lName) {
		this.fullName = fName + " " + lName;}
	public void setFilePath(String filePath) {this.filePath = filePath;}
	public void setPassword(String password) {this.password = password;}

}
