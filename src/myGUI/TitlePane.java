package myGUI;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class TitlePane extends AnchorPane{
	public TitlePane() {
		Double tempRemainingBudget = 2.25; // TODO: replace with dynamic remaining value
		
		// Main Title
		Label titleLabel = new Label("------ Budget Tracker ------");
		HBox  titlePane = new HBox (titleLabel);
		titlePane.setAlignment(Pos.CENTER);
		titlePane.setPrefWidth(1000.0);
		setTopAnchor(titlePane, 50.0);
		this.getChildren().add(titlePane);
		
		
		// Shows remaining budget
		//Label budgetLabel = new Label("Remaining Balance:  $" + Double.toString(tempRemainingBudget));
		Label budgetLabel = new Label("Remaining Balance:  $ FIXME" );
		setTopAnchor(budgetLabel, 100.0);
		setRightAnchor(budgetLabel, 200.0);
		this.getChildren().add(budgetLabel);
		

		// Shows status of the budget
		Label statusLabel = new Label("Status:");
		setTopAnchor(statusLabel, 100.0);
		setLeftAnchor(statusLabel, 250.0);
		this.getChildren().add(statusLabel);
		// TODO: Make dynamic
		//Label statusValLabel = new Label(budget.getBudgetStatus());
		Label statusValLabel = new Label("FIXME");
		setTopAnchor(statusValLabel, 100.0);
		setLeftAnchor(statusValLabel, 300.0);
		this.getChildren().add(statusValLabel);
		
		
	}

}
