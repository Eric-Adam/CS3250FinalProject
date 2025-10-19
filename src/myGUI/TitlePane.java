package myGUI;

import budgetTracker.Budget;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class TitlePane extends AnchorPane{
	private String status;
	private String balance;
	private Budget budget;
	
	private Label balanceLabel = new Label();
	private Label statusLabel = new Label();
	
	private Stage primaryStage;
	private RunGUI runGUI;
    
	public TitlePane(Stage primaryStage, RunGUI runGUI, Budget budget) {
		this.primaryStage = primaryStage;
		this.runGUI = runGUI;
		this.budget = budget;
		
		// Main Title
		Label titleLabel = new Label("------ Budget Tracker ------");
		HBox  titlePane = new HBox (titleLabel);
		titlePane.setAlignment(Pos.CENTER);
		titlePane.setPrefWidth(1000.0);
		setTopAnchor(titlePane, 50.0);
		this.getChildren().add(titlePane);
		
		
		// Show remaining budget
		balance = String.format("%.2f", budget.getOverallBalance());
		balanceLabel.setText("Remaining Balance:\t$ " + balance); 
		setTopAnchor(balanceLabel, 100.0);							 
		setRightAnchor(balanceLabel, 200.0);
		this.getChildren().add(balanceLabel);
		

		// Show status of the budget
		status = budget.getBudgetStatus();
		statusLabel.setText("Status:\t" + status);
		setTopAnchor(statusLabel, 100.0);
		setLeftAnchor(statusLabel, 250.0);
		this.getChildren().add(statusLabel);
	}	
	
	public void update() {
		budget.refreshData();
		balance = String.format("%.2f", budget.getOverallBalance());
		status = budget.getBudgetStatus();
		balanceLabel.setText("Remaining Balance:\t$ " + balance); 
		statusLabel.setText("Status:\t" + status);
	}
	
	// TODO: Switch to User Page
	private void logOut() {
		
	}
}
