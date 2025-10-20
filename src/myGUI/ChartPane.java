package myGUI;

import budgetTracker.Budget;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class ChartPane extends StackPane{
	private Budget budget;
	
	public ChartPane(Budget budget) {
		this.budget = budget;
		
	for (int i=0; i<5; i++) {// TODO: replace with charts
		Label label = new Label(Integer.toString(i));
		label.setPrefWidth(300 -i*30);
		label.setPrefHeight(300 - i*30);
		label.setStyle("-fx-border-color:black;");
		this.getChildren().add(label);
		}
	}
	
	
	// Update values for budget
	public void update() {
		// TODO Auto-generated method stub
		
	}		
}
