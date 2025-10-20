package myGUI;

import javafx.scene.layout.Pane;
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
		trackerPage = new TrackerPage(this, filePath);
	    this.getChildren().clear(); 
	    this.getChildren().add(trackerPage);

	    primaryStage.setWidth(1000);
	    primaryStage.setHeight(750);
	    primaryStage.centerOnScreen();
	}
	public void switchToUser() {
	    this.getChildren().clear();
	    this.getChildren().add(userPage);

	    primaryStage.setWidth(350);
	    primaryStage.setHeight(200);
	    primaryStage.centerOnScreen();
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
}
