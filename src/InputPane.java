import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;

public class InputPane extends VBox{
	public InputPane() {
		super(10);
		// Budget Section
		Label budgetLabel =  new Label("Budget:");
		this.getChildren().add(budgetLabel);
		ReusedButtons budgetButtons = new ReusedButtons();
		this.getChildren().add(budgetButtons);
		// TODO: add budget button functionality
		
		
		// Transaction Section
		Label transactionLabel =  new Label("Transaction:");
		this.getChildren().add(transactionLabel);
		ReusedButtons transactionButtons = new ReusedButtons();
		this.getChildren().add(transactionButtons);
		// TODO: add transaction button functionality
		

		// Justifications Section
		Label justificationLabel =  new Label("Justifications:");
		this.getChildren().add(justificationLabel);
		ToggleButton justificationButton = new ToggleButton("Audit");
		this.getChildren().add(justificationButton);
		// TODO: add audit functionality

}
}
