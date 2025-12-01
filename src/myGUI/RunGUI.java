package myGUI;

import java.sql.SQLException;

import budgetTracker.MyDatabase;
import javafx.geometry.Rectangle2D;

import javafx.scene.layout.Pane;

import javafx.stage.Screen;
import javafx.stage.Stage;

public class RunGUI extends Pane{
	private final double USER_WIDTH = 300.0;
	private final double USER_HEIGHT = 160.0;
	
	private Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
	private double trackerWidth = screenBounds.getWidth();
	private double trackerHeight = screenBounds.getHeight();
	
	private UserCoverPage userPage;
	public TrackerPage trackerPage;
	private MyDatabase db;
	
	public Stage primaryStage;
	public String username;
	
	
	public RunGUI(Stage primaryStage){
		setStage(primaryStage);
		
		// Create Database
		try {
			this.db = new MyDatabase("myData.db");
		} catch (SQLException e) {
			System.out.println("No database found.");
			e.printStackTrace();
		}
			
		// Create Cover Page
		userPage = new UserCoverPage(this);

		// Set User Page First
		switchToUser();
		
		// Set up classes for style sheet
		userPage.getStyleClass().add("user-page");	
		this.getStyleClass().add("main-gui");
	}

	public void switchToTracker(String name) {
		// Create trackerPage
		setUsername(name);
	    trackerPage = new TrackerPage(this, name);
	    
	    // Clear GUI and set to TrackerPage
	    this.getChildren().clear();
	    this.getChildren().setAll(trackerPage);

	    // Resize window to fit trackerPage
	    primaryStage.setMaxWidth(trackerWidth);
	    primaryStage.setMinWidth(trackerWidth);

	    primaryStage.setMaxHeight(trackerHeight);
	    primaryStage.setMinHeight(trackerHeight);

	    primaryStage.centerOnScreen();
	    
	    // Set up style for tracker page
		trackerPage.getStyleClass().add("tracker-page");
	}

	public void switchToUser() {
		// Clear GUI and set UserPage
	    this.getChildren().clear();
	    this.getChildren().add(userPage);

	    // Resize to fit UserPage
	    primaryStage.setMaxWidth(USER_WIDTH);
	    primaryStage.setMinWidth(USER_WIDTH);

	    primaryStage.setMaxHeight(USER_HEIGHT);
	    primaryStage.setMinHeight(USER_HEIGHT);
	    primaryStage.centerOnScreen();
	}
	
	public Stage getStage() {return primaryStage;}
	public void setStage(Stage Stage) {this.primaryStage = Stage;	}

	public MyDatabase getDB() {
		return db;
	}

	public void setDB(MyDatabase db) {
		this.db = db;
	}
	
	public String getUsername(){
		return this.username;
	}

	public void setUsername(String name) {
		this.username = name;
	}
	
}
