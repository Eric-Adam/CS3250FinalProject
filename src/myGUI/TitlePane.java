package myGUI;

import budgetTracker.Budget;
import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class TitlePane extends AnchorPane{
    
	public TitlePane() {
		Budget budget = new Budget();
		// Main Title
		Label titleLabel = new Label("------ Budget Tracker ------");
		HBox  titlePane = new HBox (titleLabel);
		titlePane.setAlignment(Pos.CENTER);
		titlePane.setPrefWidth(1000.0);
		setTopAnchor(titlePane, 50.0);
		this.getChildren().add(titlePane);
		
		
		// Show remaining budget
		String formattedBalance = String.format("%.2f", budget.getOverallBalance());
		Label balanceLabel = new Label("Remaining Balance:\t$ " + formattedBalance); 
		setTopAnchor(balanceLabel, 100.0);							 
		setRightAnchor(balanceLabel, 200.0);
		this.getChildren().add(balanceLabel);
		

		// Show status of the budget
		String status = budget.getBudgetStatus();
		Label statusLabel = new Label("Status:\t" + status);
		setTopAnchor(statusLabel, 100.0);
		setLeftAnchor(statusLabel, 250.0);
		this.getChildren().add(statusLabel);
		
		// Refresh status and balance
		new AnimationTimer() {
			long lastUpdate = System.nanoTime();
			private final long DELAY = 1_000_000_000; // 1 second
			
			@Override
			public void handle(long now) {
				if(now - lastUpdate >= DELAY) {
					Budget newBudget = new Budget();
					String newStatus = newBudget.getBudgetStatus();
					String newFormattedBalance = String.format("%.2f", newBudget.getOverallBalance());
					
					statusLabel.setText("Status:\t" + newStatus);
					balanceLabel.setText("Remaining Balance:\t$ " + newFormattedBalance);
					lastUpdate = now;
				}
			}
			
		}.start();
	}	
}
