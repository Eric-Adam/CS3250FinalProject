package myGUI;

import budgetTracker.NewUser;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mindrot.jbcrypt.BCrypt;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class UserCoverPage extends AnchorPane{
	private final double WINDOW_HEIGHT_START = 160.0;
	private final double WINDOW_HEIGHT_LOGIN = 230.0; 
	private final double WINDOW_HEIGHT_NEW_USER = 475.0;
	
	private ArrayList<String> usernames = new ArrayList<>();
	private ArrayList<String> hashedPasswords = new ArrayList<>();
	private ArrayList<Node> newUserNodes = new ArrayList<>();
	private ArrayList<Node> loginNodes = new ArrayList<>();
	
	private RunGUI runGUI;
	
	private ToggleButton loginButton;
	private ToggleButton newUserButton;
	

	public UserCoverPage(RunGUI runGUI) {
		setRunGUI(runGUI);
				
		// Load users 
		pullUserData();	
		
		// Login Section
		loginButton = new ToggleButton("Login");
		newUserButton = new ToggleButton("New User");
		HBox loginHbox = new HBox(15);
		loginHbox.getChildren().addAll(loginButton, newUserButton);
		
		// Known User Section
		ComboBox<String> selectUserCombo = new ComboBox<String>();
		selectUserCombo.getItems().addAll(usernames);
        selectUserCombo.setPromptText("Username");	
        Button enterButton = new Button("Enter...");
        
		HBox selectUserHbox = new HBox(5);
		selectUserHbox.getChildren().addAll(selectUserCombo, enterButton);
		selectUserHbox.setMaxWidth(250.0);
		
		loginNodes.add(selectUserHbox);
		
		
		// New User Section
		// --- Names for user name
		Label fNameLabel = new Label("First Name:           ");
		Label lNameLabel = new Label("Last Name:            ");
		TextField fNameField = new TextField("");
		TextField lNameField = new TextField("");
		HBox fNameHbox = new HBox(5);
		HBox lNameHbox = new HBox(5);
		
		fNameHbox.getChildren().addAll(fNameLabel, fNameField);
		lNameHbox.getChildren().addAll(lNameLabel, lNameField);
		newUserNodes.add(fNameHbox);
		newUserNodes.add(lNameHbox);
		
		// --- Password and confirmation
		Label passwordLabel = new Label("Enter Password:    ");
		Label passwordConfirmLabel = new Label("Confirm Password:");
		Label passwordRequirementsLabel = new Label(
				"Passwords must contain lowercase, uppercase,\n a number and be at least 8 characters long");
		PasswordField passwordField = new PasswordField();
		PasswordField passwordConfirmField = new PasswordField();
		HBox passwordHbox = new HBox(5);
		HBox passwordConfirmHbox = new HBox(5);
		HBox passwordRequirementsHbox = new HBox(5);
		
		passwordHbox.getChildren().addAll(passwordLabel, passwordField);
		passwordConfirmHbox.getChildren().addAll(passwordConfirmLabel, passwordConfirmField);
		passwordRequirementsHbox.getChildren().add(passwordRequirementsLabel);
		newUserNodes.add(passwordHbox);
		newUserNodes.add(passwordConfirmHbox);
		newUserNodes.add(passwordRequirementsHbox);
		
		// --- Initial value for database 
		Label initialValueLabel = new Label("Intitial Value:       $");
		TextField initialValueField = new TextField("0.00");
		HBox initialValueHbox = new HBox(5);
		initialValueHbox.getChildren().addAll(initialValueLabel, initialValueField);
		newUserNodes.add(initialValueHbox);
		
		// --- Submit button
		Button submitUserButton = new Button("Submit");
		newUserNodes.add(submitUserButton);
		
		// Put it all together
		VBox centerVBox = new VBox(15);
		centerVBox.getChildren().addAll(loginHbox, selectUserHbox,fNameHbox, 
				lNameHbox, passwordHbox, passwordConfirmHbox,
				passwordRequirementsHbox, initialValueHbox, submitUserButton);
		setTopAnchor(centerVBox, 10.0);
		setLeftAnchor(centerVBox, 15.0);
		this.getChildren().addAll(centerVBox);
		
		// Display starting buttons only
		disableLogin(true);
		disableNewUser(true);


		// Listener Section
		// --- Login Button 
		loginButton.setOnAction(e->{
			if (loginButton.isSelected()) {
				// Ensure new user is closed
				disableNewUser(true);
				
				// Open login 
				disableLogin(false);
				
				// Set default values
				pullUserData();
				selectUserCombo.getItems().setAll(usernames);
				selectUserCombo.setValue("Username");

				// Need to override or cell is blank if the login section is left after selection
				selectUserCombo.setButtonCell(new ListCell<>() {
				    @Override
				    protected void updateItem(String item, boolean empty) {
				        super.updateItem(item, empty);
				        setText((empty || item == null) ? null : item);
				    }
				});
		    }
			else {
				// Close login
				disableLogin(true);
			}
		});
		
		// --- Enter Button in Login Section
		enterButton.setOnAction(e->{
			String selectedUser = selectUserCombo.getValue();
			disableLogin(true);
			
			// Open password dialog to verify password before login
			if (!selectedUser.equals("Username")) {
				passwordDialog(hashedPasswords.get(usernames.indexOf(selectedUser)), selectedUser);
			}
		});
		
		// --- New User Button
		newUserButton.setOnAction(e ->{
			if (newUserButton.isSelected()) {
				// Ensure login section is closed
				disableLogin(true);
				
				// Open new user
				disableNewUser(false);
				
				// Set defaults
				initialValueField.setText("0.00");
				fNameField.setText("");
				lNameField.setText("");
				passwordField.setText("");
				passwordConfirmField.setText("");
			}
			else {
				// Close new user
				disableNewUser(true);
			}
		});
		
		// --- Submit Button in New User Section 
		submitUserButton.setOnAction(e->{
			// Ensure fields aren't empty
			Boolean fNameFilled = !fNameField.getText().isBlank();
			Boolean lNameFilled = !lNameField.getText().isBlank();
			Boolean passwordFilled = !passwordField.getText().isBlank();
			Boolean passwordConfirmFilled = !passwordConfirmField.getText().isBlank();
			Boolean initialAmountFilled = !initialValueField.getText().isBlank();
			
			Boolean allFieldsFilled = fNameFilled && lNameFilled && passwordFilled 
					&& passwordConfirmFilled && initialAmountFilled ;
			
			// Capture fields
			String firstName = fNameField.getText().trim();
			String lastName = lNameField.getText().trim();
			String newUserName = (fNameFilled && lNameFilled)? firstName+" "+lastName: null;
			String initialPassword = passwordField.getText();
			String confirmPassword = passwordConfirmField.getText();
			
			// Verify passwords are good
			boolean passwordReqMet = passwordRequirements(initialPassword);
			boolean passwordMatch = initialPassword.equals(confirmPassword);
			
			boolean goodPassword = passwordMatch && passwordReqMet;
			
			String userFailTitle = "User Creation Failed";
			
			// Check for existing users
			Boolean existingUser = false;
			for (String user : usernames) {
				if (newUserName != null && newUserName.equalsIgnoreCase(user)) {
					existingUser = true;
					
					showAlert(String.format("Profile for %s already exists.", newUserName), userFailTitle);
					disableNewUser(true);
				}
			}
			
			// If everything checks out, create the user
			if (newUserName != null && allFieldsFilled && !existingUser && goodPassword) {
				Double initialAmount = Double.parseDouble(initialValueField.getText());
				
				disableNewUser(true);
				createNewUser(initialAmount, firstName, lastName, confirmPassword);
			}	
			// Display alert on failure
			else if(newUserName == null)
				showAlert("Username Creation Failed",userFailTitle);
			else if(!allFieldsFilled)
				showAlert("Unable to complete due to missing fields", userFailTitle);
			else if(!passwordMatch)
				showAlert("Password and confirmation do not match.", userFailTitle);
			else if (!passwordReqMet)
				showAlert("Password does not meet minimum requirements", userFailTitle);
		});
		
		// --- Force Initial Value to be valid
		initialValueField.textProperty().addListener((observable, oldValue, newValue) -> {
	        if (!newValue.matches("\\d*(\\.\\d{0,2})?")) {
	        	initialValueField.setText(oldValue);
	        }
		});
	}
	

	// Creates new user, updates to SQL database 
	private void createNewUser(Double initialAmount, String fName, String lName, String password) {
		NewUser user = new NewUser(fName, lName, initialAmount, password);
		LocalDate today = LocalDate.now();
		
		// SQL statement for inserting user name
		String sqlName = "INSERT INTO Users (Username, Password_Hash)\r\n"
				+ String.format("VALUES ('%s', '%s');", 
						user.getFullName(),
						user.getHashedPassword());	
		
		// SQL statement for adding initial transaction
		String sqlTransaction = "INSERT INTO Transactions (Amount, Category, Note, Income, Date, Owner)\r\n"
				+ String.format("VALUES (%.2f, 'Miscellaneous','Initial Transaction',1, '%s','%s');", 
						user.getInitialAmount(), today, user.getFullName());	
				
				
		// Push new user data to database
		try {
			// Connect to database
			Connection conn = runGUI.getDB().getConnection();
	        Statement stmt = conn.createStatement();
	        
	        // Execute statements
	        stmt.execute(sqlName);
	        stmt.execute(sqlTransaction);
	        
		} catch (Exception e) {
	    	System.out.println("Failed to add new user to database");
	        e.printStackTrace();
	    }
		
		// Update with new data
		pullUserData();
		
		// Switch to tracker page
		if (usernames.contains(user.getFullName())) 
			runGUI.switchToTracker(user.getFullName());
	}
	
	// Show alerts when needed
	private void showAlert(String message, String title) {
		Alert alert = new Alert(AlertType.NONE, message, ButtonType.OK);
		alert.setTitle(title);
		alert.initOwner(runGUI.primaryStage);
		alert.showAndWait();		
	}
	
	// Load users from database
	private void pullUserData() {
		// Start fresh
		usernames.clear();
		hashedPasswords.clear();
		
		// SQL statement for selecting user names 
		String sql = "SELECT Username, Password_Hash \r\n"
				+ "FROM Users\r\n"
				+ "ORDER by Username ASC;";	
				
		// Pull user data from database
		try {
			// Connect to database
			Connection conn = runGUI.getDB().getConnection();
	        Statement stmt = conn.createStatement();
	        
	        // Execute query
	        ResultSet rs = stmt.executeQuery(sql);
	        
	        // Place query data into usable lists
			String username, password;
	        while (rs.next()) {
	        	username = rs.getString("Username");
	        	password = rs.getString("Password_Hash");
	            
	            usernames.add(username);
	            hashedPasswords.add(password);
	        }
	        
		} catch (Exception e) {
	    	System.out.println("Failed to load data from database");
	        e.printStackTrace();
	    }
	}
	
	
	// Make sections appear and disappear	
	private void disableNodes(boolean disable, ToggleButton button, ArrayList<Node> nodeList, double height) {
		boolean enable = !disable;

		button.setSelected(enable);
		
		for(Node node : nodeList) {
			node.setVisible(enable);
			node.setManaged(enable);
		}
		
		if (enable)
			resizeWindow(height);
		else
			resizeWindow(WINDOW_HEIGHT_START);
	}
	private void disableLogin(boolean disable) {
		disableNodes(disable, loginButton, loginNodes, WINDOW_HEIGHT_LOGIN);
	}
	private void disableNewUser(boolean disable) {
		disableNodes(disable, newUserButton, newUserNodes, WINDOW_HEIGHT_NEW_USER);
	}
	
	
	// Change window size to fit appearing/disappearing nodes
	private void resizeWindow(double height) {
		runGUI.primaryStage.setMaxHeight(height);
		runGUI.primaryStage.setMinHeight(height);
	}


	private void setRunGUI(RunGUI runGUI) {
		this.runGUI = runGUI;
	}

	private boolean verifyPassword(String password, String hashedPassword) {
		return BCrypt.checkpw(password, hashedPassword);
	}
	
	private void passwordDialog(String userHashedPassword, String user) {
		Dialog<Boolean> dialog = new Dialog<>();
		dialog.setTitle("Enter Password");
		dialog.setHeaderText("Enter your password below.");
		
		ButtonType passwordLoginButtonType = new ButtonType("Login", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(passwordLoginButtonType, ButtonType.CANCEL);
		
		HBox passwordBox = new HBox(5);
		PasswordField passwordField = new PasswordField();
		passwordBox.getChildren().add(passwordField);		
	
 		dialog.getDialogPane().setContent(passwordBox);		
 	   
		// Data capture
		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == passwordLoginButtonType) {
				String rawPassword = passwordField.getText();
				
				return verifyPassword(rawPassword, userHashedPassword);
				
			}
				return null;
			});
		   
		
		Optional<Boolean> result = dialog.showAndWait();
		
		// Login
		if (result.isPresent()) {
			if (result.get()) 
				runGUI.switchToTracker(user);
			else if (!result.get())
				showAlert("Wrong Password Entered", "Wrong Password"); 
		}
	}
	
	// Check password meets minimum requirements
	private boolean passwordRequirements(String password) {
		String requirements = "^(?=.*[a-z])(?=."
                + "*[A-Z])(?=.*\\d).+$";
		
		Pattern p = Pattern.compile(requirements);
		Matcher m = p.matcher(password);
		
		boolean longEnough = (password.length()>=8)? true: false;
		
		return m.matches() && longEnough;
	}

}
