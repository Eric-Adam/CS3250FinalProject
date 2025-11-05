package myGUI;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import budgetTracker.NewUser;
import budgetTracker.Transaction;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class UserCoverPage extends AnchorPane{
	private String userFile = "src/resources/userData.csv";
	private ArrayList<String> usernames = new ArrayList<String>();
	private ArrayList<String> filePaths = new ArrayList<String>();
	private Stage primaryStage;
	private RunGUI runGUI;
	
	private ToggleButton loginButton;
	private ToggleButton newUserButton;
	
	private HBox selectUserVbox;
	
	private HBox fNameHbox;
	private HBox lNameHbox;
	private HBox initialValueHbox;
	private Button submitUserButton;

	public UserCoverPage(Stage primaryStage, RunGUI runGUI) {
		// Load UserData
		loadUserData();	
		this.primaryStage = primaryStage;
		this.runGUI = runGUI;
		
		// Login Section
		loginButton = new ToggleButton("Login");
		newUserButton = new ToggleButton("New User");
		HBox loginVbox = new HBox(5);
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
		fNameHbox = new HBox(5);
		fNameHbox.getChildren().addAll(fNameLabel, fNameField);
		
		Label lNameLabel = new Label("Last Name:      ");
		TextField lNameField = new TextField("");
		lNameHbox = new HBox(5);
		lNameHbox.getChildren().addAll(lNameLabel, lNameField);
		
		Label initialValueLabel = new Label("Intitial Value: $");
		TextField initialValueField = new TextField("0.00");
		initialValueHbox = new HBox(5);
		initialValueHbox.getChildren().addAll(initialValueLabel, initialValueField);
		
		submitUserButton = new Button("Submit");
		
		// Put it all together
		VBox centerVBox = new VBox(5);
		centerVBox.getChildren().addAll(loginVbox, selectUserVbox, fNameHbox, 
				lNameHbox, initialValueHbox, submitUserButton);
		setTopAnchor(centerVBox, 75.0);
		setLeftAnchor(centerVBox, 100.0);
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
				String filePath = filePaths.get(usernames.indexOf(selectedUser));
				runGUI.setUser(selectedUser);
				runGUI.switchToTracker(filePath);
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
			Boolean initalAmountFilled = !initialValueField.equals("");
			Boolean existingUser = false;
			
			String firstName = fNameField.getText().trim();
			String lastName = lNameField.getText().trim();
			String newUserName = (fNameFilled && lNameFilled)? firstName+" "+lastName: null;
			
			for (String user : usernames) {
				if (newUserName.equals(user))
					existingUser = true;
			}

			if (fNameFilled && lNameFilled && initalAmountFilled && !existingUser) {
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
	
	// Creates new user
	private void createNewUser(Double initialAmount, String fName, String lName) {
		
		NewUser user = new NewUser(fName, lName, initialAmount);
		
		LocalDate today = LocalDate.now();
		Transaction initialTransaction = new Transaction(user.getInitialAmount(), 
				"Miscellaneous","Initial Transaction",true, today);

		List<String> addUser = new ArrayList<String>();
		addUser.add(escapeForCSV(user.getFullName()));
		addUser.add(escapeForCSV(user.getFilePath()));
		
		List<String> newUserData = new ArrayList<String>();
		String[] headers = {"transactionAmount","category","note","income","date"};
		Collections.addAll(newUserData, headers);
		
		// Open files
		File userFile = new File("src/resources/userData.csv");
		File userDataFile = new File(user.getFilePath());
		
		// Save data
		saveToCSV(userFile, addUser);
		saveToCSV(userDataFile, newUserData);
		Transaction.saveToCSV(userDataFile.toString(), initialTransaction);
		
		runGUI.setUser(addUser.getFirst());
		runGUI.switchToTracker(userDataFile.toString());
	}
	
	
	// Appends to CSV
	public static void saveToCSV(File file, List<String> arr) {			
		try (FileWriter writer = new FileWriter(file, true)) {
			for (int i = 0; i < arr.size(); i++) {
		        writer.append(arr.get(i));
		        if (i < arr.size() - 1) {
		            writer.append(",");
		        }
		    }
		    writer.append("\n");
		    
		} catch (IOException e) {
			System.out.println("Failed to create file " + file.toString());
		}
	}
	    
	// Escapes double quotes and removes commas
	private static String escapeForCSV(String value) {
		if (value.contains(",") || value.contains("\"")) {
	        value = value.replace("\"", "\"\"");
	        value = value.replace(",", "");
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
            System.out.println("Failed to load User data:\n"+e);
        }
	}
	
	
	// Make sections appear and disappear	
	private void disableNewUser(boolean disable) {
		boolean enable = !disable;
		
		newUserButton.setSelected(enable);
		
		fNameHbox.setVisible(enable); 
		lNameHbox.setVisible(enable); 
		initialValueHbox.setVisible(enable); 
		submitUserButton.setVisible(enable); 
		
		fNameHbox.setManaged(enable);
		lNameHbox.setManaged(enable);
		initialValueHbox.setManaged(enable);
		submitUserButton.setManaged(enable);
		
		if (enable)
			resizeWindow(350, 400);
		else
			resizeWindow(200, 350);		
	}
	
	private void disableLogin(boolean disable) {
		boolean enable = !disable;

		loginButton.setSelected(enable);
		
		selectUserVbox.setVisible(enable); 
		selectUserVbox.setManaged(enable);
		
		if (enable)
			resizeWindow(200, 375);
		else
			resizeWindow(200, 350);
	}
	
	// Change window size to fit appearing/disappearing nodes
	private void resizeWindow(double height, double width) {
		primaryStage.setHeight(height);
		primaryStage.setWidth(width);
	}

}
