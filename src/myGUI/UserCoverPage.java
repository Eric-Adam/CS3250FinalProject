package myGUI;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class UserCoverPage extends AnchorPane{
	private String userFile = "src/resources/userData.csv";
	private ArrayList<String> usernames = new ArrayList<String>();
	private ArrayList<String> passwords = new ArrayList<String>();
	private ArrayList<String> filePaths = new ArrayList<String>();
	
	
	public UserCoverPage(Stage primaryStage) {
		// Load UserData
		loadUserData();		
		
		// Login Section
		ToggleButton loginButton = new ToggleButton("Login");
		ToggleButton newUserButton = new ToggleButton("New User");
		HBox loginVbox = new HBox();
		loginVbox.getChildren().addAll(loginButton, newUserButton);
		
		// Known User Section
		ComboBox<String> selectUserCombo = new ComboBox<String>();
		selectUserCombo.getItems().addAll(usernames);
		selectUserCombo.setPromptText("Username");
		PasswordField passwordField = new PasswordField();
		Button enterButton = new Button("Enter");
		HBox selectUserVbox = new HBox();
		selectUserVbox.getChildren().addAll(selectUserCombo, passwordField, enterButton);
		
		// New User Section
		Label fNameLabel = new Label("First Name: ");
		TextField fNameField = new TextField("");
		HBox fNameHbox = new HBox();
		fNameHbox.getChildren().addAll(fNameLabel, fNameField);
		
		Label lNameLabel = new Label("Last Name: ");
		TextField lNameField = new TextField("");
		HBox lNameHbox = new HBox();
		lNameHbox.getChildren().addAll(lNameLabel, lNameField);
		
		Label passwordLabel = new Label("Enter Password: ");
		TextField newPasswordField = new TextField("");
		HBox newPasswordHbox = new HBox();
		newPasswordHbox.getChildren().addAll(passwordLabel, newPasswordField);
		
		Label initialValueLabel = new Label("Intitial Value: $");
		TextField initialValueField = new TextField("0.00");
		HBox initialValueHbox = new HBox();
		initialValueHbox.getChildren().addAll(initialValueLabel, initialValueField);
		
		// Put it all together
		VBox centerVBox = new VBox();
		centerVBox.getChildren().addAll(loginVbox, selectUserVbox,
				fNameHbox, lNameHbox, newPasswordHbox, initialValueHbox);
		setTopAnchor(centerVBox, 50.0);
		setLeftAnchor(centerVBox, 25.0);
		this.getChildren().addAll(centerVBox);
		
		
		// Listener Section
		
		
		
		
		
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
	
	private void makeVisible(Node n1, Node n2) {
		
	}
	
}
