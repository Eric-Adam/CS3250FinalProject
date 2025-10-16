package myGUI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import budgetTracker.Transaction;
import budgetTracker.User;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class UserCoverPage extends AnchorPane{
	private String userFile = "src/resources/userData.csv";
	private ArrayList<String> usernames = new ArrayList<String>();
	private ArrayList<String> passwords = new ArrayList<String>();
	private ArrayList<String> filePaths = new ArrayList<String>();
	private Stage primaryStage;
	
	
	public UserCoverPage(Stage primaryStage) {
		// Load UserData
		loadUserData();	
		this.primaryStage = primaryStage;
		
		// Login Section
		ToggleButton loginButton = new ToggleButton("Login");
		ToggleButton newUserButton = new ToggleButton("New User");
		HBox loginVbox = new HBox(5);
		loginVbox.getChildren().addAll(loginButton, newUserButton);
		
		// Known User Section
		ComboBox<String> selectUserCombo = new ComboBox<String>();
		selectUserCombo.getItems().addAll(usernames);
		selectUserCombo.setPromptText("Username");
		PasswordField passwordField = new PasswordField();
		Button enterButton = new Button("Enter");
		HBox selectUserVbox = new HBox(5);
		selectUserVbox.getChildren().addAll(selectUserCombo, passwordField, enterButton);
		
		// New User Section
		Label fNameLabel = new Label("First Name: ");
		TextField fNameField = new TextField("");
		HBox fNameHbox = new HBox(5);
		fNameHbox.getChildren().addAll(fNameLabel, fNameField);
		
		Label lNameLabel = new Label("Last Name: ");
		TextField lNameField = new TextField("");
		HBox lNameHbox = new HBox(5);
		lNameHbox.getChildren().addAll(lNameLabel, lNameField);
		
		Label passwordLabel = new Label("Enter Password: ");
		TextField newPasswordField = new TextField("");
		HBox newPasswordHbox = new HBox(5);
		newPasswordHbox.getChildren().addAll(passwordLabel, newPasswordField);
		
		Label initialValueLabel = new Label("Intitial Value: $");
		TextField initialValueField = new TextField("0.00");
		HBox initialValueHbox = new HBox(5);
		initialValueHbox.getChildren().addAll(initialValueLabel, initialValueField);
		
		Button submitUserButton = new Button("Submit");
		
		// Put it all together
		VBox centerVBox = new VBox(5);
		centerVBox.getChildren().addAll(loginVbox, selectUserVbox, fNameHbox, 
				lNameHbox, newPasswordHbox, initialValueHbox, submitUserButton);
		setTopAnchor(centerVBox, 75.0);
		setLeftAnchor(centerVBox, 100.0);
		this.getChildren().addAll(centerVBox);
		
		makeInvisible(selectUserVbox);
		makeInvisible(fNameHbox, lNameHbox, newPasswordHbox, 
				initialValueHbox, submitUserButton);

		
		// Listener Section
		loginButton.setOnAction(e->{
			if (loginButton.isSelected()) {
				// Ensure new user is closed
				makeInvisible(fNameHbox, lNameHbox, newPasswordHbox, 
						initialValueHbox, submitUserButton);
				newUserButton.setSelected(false);
				
				// Open login 
				makeVisible(selectUserVbox);
				resizeWindow(200, 500);
			}
			else {
				// Close login
				makeInvisible(selectUserVbox);
				resizeWindow(200, 350);
			}
		});
		
		
		newUserButton.setOnAction(e ->{
			if (newUserButton.isSelected()) {
				// Ensure login is closed
				makeVisible(fNameHbox, lNameHbox, newPasswordHbox, 
						initialValueHbox, submitUserButton);
				loginButton.setSelected(false);
				
				// Open new user
				makeInvisible(selectUserVbox);
				resizeWindow(350, 400);
			}
			else {
				// Close login
				makeInvisible(fNameHbox, lNameHbox, newPasswordHbox, 
						initialValueHbox, submitUserButton);
				resizeWindow(200, 350);
			}
		});
		
		
		
	}
	
	
	
	
	private void login() {
		
	}
	
	private void createNewUser() {
		
	}
	// Appends to CSV
		public static void saveToCSV(String fileName, User user) {
			List<String> arr = new ArrayList<String>();
			arr.add(escapeForCSV(user.getFullName()));
			arr.add(escapeForCSV(user.getPassword()));
			arr.add(escapeForCSV(user.getFilePath()));
			File file = new File(fileName);
			
			try (FileWriter writer = new FileWriter(file, true)) {
				for (int i = 0; i < arr.size(); i++) {
			        writer.append(arr.get(i));
			        if (i < arr.size() - 1) {
			            writer.append(",");
			        }
			    }
			    writer.append("\n");
			    
			} catch (IOException e) {
				System.out.println("Failed to write to file");
			}
		}
	    
		// Escapes double quotes 
		private static String escapeForCSV(String value) {
		    if (value.contains(",") || value.contains("\"")) {
		        value = value.replace("\"", "\"\""); 
		        return "\"" + value + "\"";
		    }
		    return value;
		}
	
	private void loadUserData() {// TODO: Can't handle commas, think of work around
        try (BufferedReader br = new BufferedReader(new FileReader(userFile))) {
            String line;
            int count = 0;
            while ((line = br.readLine()) != null) {
            	if(count>0) {
	            	String[] values = line.split(",");
	                usernames.add(values[0]);
	                passwords.add(values[1]);
	                filePaths.add(values[2]);
                }
            	count++;
            }           
        } catch (Exception e) {
            System.out.println("Failed to load User data:\n"+e);
        }
	}
	
	private void makeVisible(Node n1) {
		n1.setVisible(true); n1.setManaged(true);
	}
	private void makeVisible(Node n1, Node n2, Node n3, Node n4, Node n5) {
		n1.setVisible(true); n1.setManaged(true);
		n2.setVisible(true); n2.setManaged(true);
		n3.setVisible(true); n3.setManaged(true);
		n4.setVisible(true); n4.setManaged(true);
		n5.setVisible(true); n5.setManaged(true);
	}
	
	private void makeInvisible(Node n1) {
		n1.setVisible(false); n1.setManaged(false);
	}
	private void makeInvisible(Node n1, Node n2, Node n3, Node n4, Node n5) {
		n1.setVisible(false); n1.setManaged(false);
		n2.setVisible(false); n2.setManaged(false);
		n3.setVisible(false); n3.setManaged(false);
		n4.setVisible(false); n4.setManaged(false);
		n5.setVisible(false); n5.setManaged(false);
	}
	
	private void resizeWindow(double height, double width) {
		primaryStage.setHeight(height);
		primaryStage.setWidth(width);
	}
}
