package myGUI;

import budgetTracker.NewUser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import javax.swing.JOptionPane;

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
	private final double WINDOW_HEIGHT_LOGIN = 200.0; 
	private final double WINDOW_HEIGHT_NEW_USER = 350.0;
	
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
		HBox selectUserHbox = new HBox(5);
		selectUserHbox.getChildren().add(selectUserCombo);
		loginNodes.add(selectUserHbox);
		
		// Password Section
		Button enterButton = new Button("Enter");
		PasswordField passwordField = new PasswordField();
		HBox passwordHbox = new HBox(5);
		passwordHbox.getChildren().addAll(passwordField, enterButton);
		loginNodes.add(passwordHbox);
		
		
		// New User Section
		Label fNameLabel = new Label("First Name:     ");
		TextField fNameField = new TextField("");
		HBox fNameHbox = new HBox(5);
		fNameHbox.getChildren().addAll(fNameLabel, fNameField);
		newUserNodes.add(fNameHbox);
		
		Label lNameLabel = new Label("Last Name:      ");
		TextField lNameField = new TextField("");
		HBox lNameHbox = new HBox(5);
		lNameHbox.getChildren().addAll(lNameLabel, lNameField);
		newUserNodes.add(lNameHbox);
		
		// TODO: add password field and password hashing if I have time
		
		Label initialValueLabel = new Label("Intitial Value: $");
		TextField initialValueField = new TextField("0.00");
		HBox initialValueHbox = new HBox(5);
		initialValueHbox.getChildren().addAll(initialValueLabel, initialValueField);
		newUserNodes.add(initialValueHbox);
		
		Button submitUserButton = new Button("Submit");
		newUserNodes.add(submitUserButton);
		
		// Put it all together
		VBox centerVBox = new VBox(15);
		centerVBox.getChildren().addAll(loginHbox, selectUserHbox, passwordHbox,
				fNameHbox, lNameHbox, initialValueHbox, submitUserButton);
		setTopAnchor(centerVBox, 10.0);
		setLeftAnchor(centerVBox, 15.0);
		this.getChildren().addAll(centerVBox);
		
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
			}
			else {
				// Close new user
				disableNewUser(true);
			}
		});
		
		// --- Submit Button in New User Section 
		submitUserButton.setOnAction(e->{
			// Ensure fields aren't empty
			Boolean fNameFilled = !fNameField.getText().equals("");
			Boolean lNameFilled = !lNameField.getText().equals("");
			
			@SuppressWarnings("unlikely-arg-type") // Catch if the user emptied the field
			Boolean initalAmountFilled = !initialValueField.equals("");
			Boolean existingUser = false;
			
			String firstName = fNameField.getText().trim();
			String lastName = lNameField.getText().trim();
			String newUserName = (fNameFilled && lNameFilled)? firstName+" "+lastName: null;
			
			for (String user : usernames) {
				if (newUserName != null && newUserName.equalsIgnoreCase(user)) {
					existingUser = true;
					JOptionPane.showMessageDialog(null, "Profile for " + user + " already exists.");
					disableNewUser(true);
				}
			}

			if (newUserName != null && initalAmountFilled && !existingUser) {
				Double initialAmount = Double.parseDouble(initialValueField.getText());
				disableNewUser(true);
				createNewUser(initialAmount, firstName, lastName);
			}	
		});
		
		// --- Force Initial Value to be valid
		initialValueField.textProperty().addListener((observable, oldValue, newValue) -> {
	        if (!newValue.matches("\\d*(\\.\\d{0,2})?")) {
	        	initialValueField.setText(oldValue);
	        }
		});
	}
	
	// Creates new user, updates to SQL database
	private void createNewUser(Double initialAmount, String fName, String lName) {
		
		NewUser user = new NewUser(fName, lName, initialAmount);
		LocalDate today = LocalDate.now();
		
		// No longer in use due to the switch to SQL database
//		Transaction initialTransaction = new Transaction(user.getInitialAmount(), 
//				"Miscellaneous","Initial Transaction",true, today);
//
//		ArrayList<String> addUser = new ArrayList<String>();
//		addUser.add(escapeForCSV(user.getFullName()));
//		addUser.add(escapeForCSV(user.getFilePath()));
//		
//		
//		// Get users file path
//		File userFile = new File("src/resources/userData.csv");
//		
//		// Add user to user file
//		saveToCSV(userFile, addUser);
//		
//		// Save data
//		Budget budget = new Budget(user.getFilePath());
//		budget.transactions.add(initialTransaction);
//		budget.overwrite();
		
		
		
		// SQL statement for inserting user name
		String sqlName = "INSERT INTO UserList (Name)\r\n"
				+ "VALUES ('"+ user.getFullName() +"');";	
		
		// SQL statement for adding initial transaction
		String sqlTransaction = "INSERT INTO Transactions (Amount, Category, Note, Income, Date, Owner)\r\n"
				+ "VALUES ("+ user.getInitialAmount() + 
				", 'Miscellaneous','Initial Transaction',1, '"
				+ today + "','"+user.getFullName()+"');";	
				
				
		// Push new user data to database
		try {
			Connection conn = runGUI.getDB().getConnection();
	        Statement stmt = conn.createStatement();

	        stmt.execute(sqlName);
	        stmt.execute(sqlTransaction);
			
	        pullUserData();
	        
		} catch (Exception e) {
	    	System.out.println("Failed to add new user to database");
	        e.printStackTrace();
	    }
		
		// Switch to tracker page
		if (usernames.contains(user.getFullName()))
			runGUI.switchToTracker(user.getFullName());
		
	}
	
	
	// Appends to CSV
	@Deprecated
	public void saveToCSV(File file, ArrayList<String> arr) {			
		try (FileWriter writer = new FileWriter(file, true)) {
			for (int i = 0; i < arr.size(); i++) {
		        writer.append(arr.get(i));
		        if (i < arr.size() - 1) {
		            writer.append(",");
		        }
		    }
		    writer.append("\n");
		    
		} catch (IOException e) {
			showAlert("Failed to create user.","User Creation Failed");
		}
	}
	    
	private void showAlert(String message, String title) {
		Alert alert = new Alert(AlertType.NONE, message, ButtonType.OK);
		alert.setTitle(title);
		alert.initOwner(runGUI.primaryStage);
		alert.showAndWait();		
	}
	
//	// Escapes double quotes and removes commas
//	@Deprecated
//	private static String escapeForCSV(String value) {
//		if (value.contains("\"")) {
//	        value = value.replace("\"", "\"\"");
//	        return "\"" + value + "\"";
//	    }
//	    if (value.contains(","))
//    		value = value.replace(",", "");
//	    
//	    return value;
//	}
//	
//	@Deprecated
//	private void loadUserData() {
//		usernames.clear();
//		filePaths.clear();
//		
//        try (BufferedReader br = new BufferedReader(new FileReader(userFile))) {
//            String line;
//            int count = 0;
//            while ((line = br.readLine()) != null) {
//            	if(count>0) {
//	            	String[] values = line.split(",");
//	                usernames.add(values[0]);
//	                filePaths.add(values[1]);
//                }
//            	count++;
//            }           
//        } catch (Exception e) {
//            showAlert("Failed to load users","Load User Failed");
//        }
//	}
	
	// Load users from database
	private void pullUserData() {
		usernames.clear();
		hashedPasswords.clear();
		
		// SQL statement for selecting user names 
		String sql = "SELECT Name, Password_Hash \r\n"
				+ "FROM Users\r\n"
				+ "ORDER by Name ASC;";	
				
				
		// Pull user data from database
		try {
			Connection conn = runGUI.getDB().getConnection();
	        Statement stmt = conn.createStatement();
	        ResultSet rs = stmt.executeQuery(sql);
	        
			String username, password;
			
	        while (rs.next()) {
	        	username = rs.getString("Name");
	        	password = rs.getString("Password_Hash");
	            
	            // Add user data to lists
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
		
		if (result.get()) {
			runGUI.switchToTracker(user);
		}
	}

}
