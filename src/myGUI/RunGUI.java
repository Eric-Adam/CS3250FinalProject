package myGUI;

import javafx.geometry.Rectangle2D;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class RunGUI extends Pane{
	private UserCoverPage userPage;
	private TrackerPage trackerPage;
	private Stage primaryStage;
	private String user=null;
	
	public RunGUI(Stage primaryStage){
		this.primaryStage = primaryStage;
			
		// Create Cover Page
		userPage = new UserCoverPage(primaryStage, this);

		// Set User Page First
		this.getChildren().add(userPage);
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
	}

	public void switchToUser() {
		// Clear GUI and set UserPage
	    this.getChildren().clear();
	    this.getChildren().add(userPage);

	    // Resize to fit UserPage
	    primaryStage.setWidth(350);
	    primaryStage.setHeight(200);
	    primaryStage.centerOnScreen();
	}

	public String getUser() {return user;}
	public void setUser(String user) {this.user = user;	}
}
