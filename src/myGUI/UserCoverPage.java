package myGUI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.JOptionPane;

import budgetTracker.Budget;
import budgetTracker.NewUser;
import budgetTracker.Transaction;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class UserCoverPage extends AnchorPane{
	private final double WINDOW_HEIGHT_START = 160.0;
	private final double WINDOW_HEIGHT_LOGIN = 230.0; // TODO: adjust once passwords are added
	private final double WINDOW_HEIGHT_NEW_USER = 350.0;
	
	private String userFile = "src/resources/userData.csv";
	private ArrayList<String> usernames = new ArrayList<String>();
	private ArrayList<String> filePaths = new ArrayList<String>();
	private ArrayList<Node> nodeList = new ArrayList<>();
	
	private RunGUI runGUI;
	
	private ToggleButton loginButton;
	private ToggleButton newUserButton;
	
	private HBox selectUserVbox;
	
	


	public UserCoverPage(RunGUI runGUI) {
		// Set defaults
		setRunGUI(runGUI);
				
		// Load UserData // TODO: replace with pulling users from database
		loadUserData();	
		
		
		
		// TODO: add password field and password hashing
		
		// Login Section
		loginButton = new ToggleButton("Login");
		newUserButton = new ToggleButton("New User");
		HBox loginVbox = new HBox(15);
		loginVbox.getChildren().addAll(loginButton, newUserButton);
		
		// Known User Section
		ComboBox<String> selectUserCombo = new ComboBox<String>();
		selectUserCombo.getItems().addAll(usernames);
        selectUserCombo.setPromptText("Username");			
		Button enterButton = new Button("Enter");
		selectUserVbox = new HBox(5);
		selectUserVbox.getChildren().addAll(selectUserCombo, enterButton);
		
		// New User Section
		Label fNameLabel = new Label("First Name:     ");
		TextField fNameField = new TextField("");
		HBox fNameHbox = new HBox(5);
		fNameHbox.getChildren().addAll(fNameLabel, fNameField);
		nodeList.add(fNameHbox);
		
		Label lNameLabel = new Label("Last Name:      ");
		TextField lNameField = new TextField("");
		HBox lNameHbox = new HBox(5);
		lNameHbox.getChildren().addAll(lNameLabel, lNameField);
		nodeList.add(lNameHbox);
		
		Label initialValueLabel = new Label("Intitial Value: $");
		TextField initialValueField = new TextField("0.00");
		HBox initialValueHbox = new HBox(5);
		initialValueHbox.getChildren().addAll(initialValueLabel, initialValueField);
		nodeList.add(initialValueHbox);
		
		Button submitUserButton = new Button("Submit");
		nodeList.add(submitUserButton);
		
		// Put it all together
		VBox centerVBox = new VBox(15);
		centerVBox.getChildren().addAll(loginVbox, selectUserVbox, fNameHbox, 
				lNameHbox, initialValueHbox, submitUserButton);
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
				loadUserData();
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
				runGUI.switchToTracker(selectedUser);
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
	
	// Creates new user // TODO: update to SQL database insert
	private void createNewUser(Double initialAmount, String fName, String lName) {
		
		NewUser user = new NewUser(fName, lName, initialAmount);
		
		LocalDate today = LocalDate.now();
		Transaction initialTransaction = new Transaction(user.getInitialAmount(), 
				"Miscellaneous","Initial Transaction",true, today);

		ArrayList<String> addUser = new ArrayList<String>();
		addUser.add(escapeForCSV(user.getFullName()));
		addUser.add(escapeForCSV(user.getFilePath()));
		
		
		// Get users file path
		File userFile = new File("src/resources/userData.csv");
		
		// Add user to user file
		saveToCSV(userFile, addUser);
		
		// Save data
		Budget budget = new Budget(user.getFilePath());
		budget.transactions.add(initialTransaction);
		budget.overwrite();
		
		runGUI.switchToTracker(user.getFilePath());
	}
	
	
	// Appends to CSV
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

	// Escapes double quotes and removes commas
	private static String escapeForCSV(String value) {
		if (value.contains("\"")) {
	        value = value.replace("\"", "\"\"");
	        return "\"" + value + "\"";
	    }
	    if (value.contains(","))
    		value = value.replace(",", "");
	    
	    return value;
	}
	
	private void loadUserData() {
		usernames.clear();
		filePaths.clear();
		
        try (BufferedReader br = new BufferedReader(new FileReader(userFile))) {
            String line;
            int count = 0;
            while ((line = br.readLine()) != null) {
            	if(count>0) {
	            	String[] values = line.split(",");
	                usernames.add(values[0]);
	                filePaths.add(values[1]);
                }
            	count++;
            }           
        } catch (Exception e) {
            showAlert("Failed to load users","Load User Failed");
        }
	}
	
	
	// Make sections appear and disappear	
	private void disableNewUser(boolean disable) {
		boolean enable = !disable;
		
		newUserButton.setSelected(enable);
		
		for(Node node : nodeList) {
			node.setVisible(enable);
			node.setManaged(enable);
		}
		
		if (enable)
			resizeWindow(WINDOW_HEIGHT_NEW_USER);
		else
			resizeWindow(WINDOW_HEIGHT_START);		
	}
	
	private void disableLogin(boolean disable) {
		boolean enable = !disable;

		loginButton.setSelected(enable);
		
		selectUserVbox.setVisible(enable); 
		selectUserVbox.setManaged(enable);
		
		if (enable)
			resizeWindow(WINDOW_HEIGHT_LOGIN);
		else
			resizeWindow(WINDOW_HEIGHT_START);
	}
	
	// Change window size to fit appearing/disappearing nodes
	private void resizeWindow(double height) {
		runGUI.primaryStage.setMaxHeight(height);
		runGUI.primaryStage.setMinHeight(height);
	}


	private void setRunGUI(RunGUI runGUI) {
		this.runGUI = runGUI;
	}


}
