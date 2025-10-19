/* Final Project for CS3250
 * Fall 2025
 * Eric Adam
 * */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import myGUI.*;

public class Main extends Application{
	//public static final String DB_FILENAME = "src/resources/transactionDB.csv";
	
	public static void main(String[] args) {
		launch(args);
		
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		String stylesheet = getClass().getResource("styles/style.css").toExternalForm();
		
		Scene scene = new Scene(new RunGUI(primaryStage), 350.0, 200.0);
		scene.getStylesheets().add(stylesheet);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Adam's Expense Tracker");
		primaryStage.show();
		
		
		
	}
		

}