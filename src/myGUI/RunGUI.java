package myGUI;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class RunGUI extends Pane{
	public RunGUI(Stage primaryStage){
		// Open to User Page
		UserCoverPage user = new UserCoverPage(primaryStage);
		this.getChildren().add(user);
		
		
		// TODO: Switch to Tracker Page based on what happens in UserCoverPage
		//TrackerPage tracker = new TrackerPage();
		//this.getChildren().add(tracker);
	}

	public void switchToTracker(Stage primaryStage) {
	    this.getChildren().clear(); // Remove current page
	    TrackerPage tracker = new TrackerPage();
	    this.getChildren().add(tracker);

	    primaryStage.setWidth(1000);
	    primaryStage.setHeight(750);
	}
}
