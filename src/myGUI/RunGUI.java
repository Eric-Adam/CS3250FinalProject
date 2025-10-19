package myGUI;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class RunGUI extends Pane{
	private UserCoverPage user;
	private TrackerPage tracker;
	private Stage primaryStage;
	
	public RunGUI(Stage primaryStage){
		this.primaryStage = primaryStage;
		// Create Pages
		user = new UserCoverPage(primaryStage, this);

		// Set User Page First
		this.getChildren().add(user);
	}

	public void switchToTracker(String filePath) {
		tracker = new TrackerPage(primaryStage, this, filePath);
	    this.getChildren().clear(); 
	    this.getChildren().add(tracker);

	    primaryStage.setWidth(1000);
	    primaryStage.setHeight(750);
	    primaryStage.centerOnScreen();
	}
	public void switchToUser() {
	    this.getChildren().clear();
	    this.getChildren().add(user);

	    primaryStage.setWidth(350);
	    primaryStage.setHeight(200);
	    primaryStage.centerOnScreen();
	}
}
