/* Final Project for CS3250
 * Fall 2025
 * Eric Adam
 * */

import budgetTracker.MyDatabase;
import myGUI.RunGUI;

import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

import javafx.stage.Stage;


public class Main extends Application{
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// Set style
		String stylesheet = getClass().getResource("styles/style.css").toExternalForm();
		
		try {
			// Create Database Object
			MyDatabase db = new MyDatabase("src/myData.db");
			
			// Create RunGUI 
			RunGUI root = new RunGUI(primaryStage, db);
			root.getStyleClass().add("main-gui");
			
			// Set scene and open window  
			Scene scene = new Scene(root, 375.0, 225.0);
			scene.getStylesheets().add(stylesheet);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Adam's Tracker");
			primaryStage.show();
			
		} catch (Exception e) {
			// Alert if either fails
			Alert alert = new Alert(AlertType.NONE, e.getLocalizedMessage(), ButtonType.OK);
			alert.setTitle("Failure to Start");
			alert.initOwner(primaryStage);
			alert.showAndWait();
			
			// Close application
			primaryStage.close();
		}				
	}
}