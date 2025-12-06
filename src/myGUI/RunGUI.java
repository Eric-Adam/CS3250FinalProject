package myGUI;

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
	
	/**
	 * Main pane to contain and switch between the rest of the GUI
	 * 
	 * @param primaryStage Stage everything is set on
	 * @param db Database being used
	 */
	public RunGUI(Stage primaryStage, MyDatabase db){
		setStage(primaryStage);
		setDB(db);
	
		// Create Cover and Tracker pages
		userPage = new UserCoverPage(this);
		trackerPage = new TrackerPage(this, null);

		// Set User Page First
		switchToUser();
		
		// Add classes for style sheet
		trackerPage.getStyleClass().add("tracker-page");
		userPage.getStyleClass().add("user-page");	
		this.getStyleClass().add("main-gui");
	}

	
	/**
	 * Switches to the TrackerPane with the current user's data
	 * 
	 * @param name Name of the user logging in
	 */
	public void switchToTracker(String name) {
		// Set name into necessary pages since it was null at creation
		setUsername(name);
	    trackerPage.setName(name);
	    trackerPage.budget.setName(name);
	    
	    // Update tracker to display the current user
	    trackerPage.update();
	    
	    // Clear GUI and set to TrackerPage
	    this.getChildren().clear();
	    this.getChildren().setAll(trackerPage);

	    // Resize window to fit trackerPage
	    primaryStage.setMaxWidth(trackerWidth);
	    primaryStage.setMinWidth(trackerWidth);

	    primaryStage.setMaxHeight(trackerHeight);
	    primaryStage.setMinHeight(trackerHeight);

	    primaryStage.centerOnScreen();		
	}

	/**
	 * Switches to the UserCoverPage
	 */
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
	
	
	// Getters and setters
	public Stage getStage() {return primaryStage;}
	public void setStage(Stage Stage) {this.primaryStage = Stage;	}

	public MyDatabase getDB() {return db;}
	public void setDB(MyDatabase db) {this.db = db;}
	
	public String getUsername(){return this.username;}
	public void setUsername(String name) {this.username = name;}
}
