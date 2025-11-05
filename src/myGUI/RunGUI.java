package myGUI;

import javafx.geometry.Rectangle2D;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class RunGUI extends Pane{
	private UserCoverPage userPage;
	private TrackerPage trackerPage;
	private Stage primaryStage;
	private String user;
	
	public RunGUI(Stage primaryStage){
		this.primaryStage = primaryStage;
			
		// Create Cover Page
		userPage = new UserCoverPage(primaryStage, this);

		// Set User Page First
		this.getChildren().add(userPage);
		
		// Set up classes for style sheet
		userPage.getStyleClass().add("user-page");	
		this.getStyleClass().add("main-gui");
	}

	public void switchToTracker(String filePath) {
		// Create trackerPage
	    trackerPage = new TrackerPage(this, filePath);
	    
	    // Clear GUI and set to TrackerPage
	    this.getChildren().clear();
	    this.getChildren().setAll(trackerPage);

	    // Resize window to fit trackerPage
	    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
	    primaryStage.setWidth(screenBounds.getWidth());
	    primaryStage.setHeight(screenBounds.getHeight());
	    primaryStage.centerOnScreen();
	    
	    // Set up style for tracker page
		trackerPage.getStyleClass().add("tracker-page");
	}

	public void switchToUser() {
		// Clear GUI and set UserPage
	    this.getChildren().clear();
	    this.getChildren().add(userPage);

	    // Resize to fit UserPage
	    primaryStage.setWidth(375.0);
	    primaryStage.setHeight(225.0);
	    primaryStage.centerOnScreen();
	}

	public String getUser() {return user;}
	public void setUser(String user) {this.user = user;	}
}
