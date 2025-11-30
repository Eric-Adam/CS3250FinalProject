package myGUI;

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
	private TrackerPage trackerPage;
	private String user;
	
	public Stage primaryStage;
	
	public RunGUI(Stage primaryStage){
		setStage(primaryStage);
			
		// Create Cover Page
		userPage = new UserCoverPage(primaryStage, this);

		// Set User Page First
		switchToUser();
		
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

	public String getUser() {return user;}
	public void setUser(String user) {this.user = user;	}
	
	public Stage getStage() {return primaryStage;}
	public void setStage(Stage Stage) {this.primaryStage = Stage;	}
	
}
