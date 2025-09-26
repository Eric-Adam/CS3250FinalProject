import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class TitlePane extends AnchorPane{
	public TitlePane() {
		// TODO: Center title
		Label titleLabel = new Label("------ Budget Tracker ------");
		HBox  titlePane = new HBox (titleLabel);
		titlePane.setAlignment(Pos.CENTER);
		titlePane.setPrefWidth(1000.0);
		setTopAnchor(titlePane, 50.0);
		this.getChildren().add(titlePane);
		
		
		Label budgetLabel = new Label("Budget");
		setTopAnchor(budgetLabel, 100.0);
		setRightAnchor(budgetLabel, 200.0);
		this.getChildren().add(budgetLabel);
		// TODO: add selected budget
		String[] budgetList = {"Overall", "Wife", "Household", "Game"};
		ComboBox<String> budgetComboBox = new ComboBox<>();
		budgetComboBox.getItems().addAll(budgetList);
		budgetComboBox.setPromptText("Overall");
		setTopAnchor(budgetComboBox, 100.0);
		setRightAnchor(budgetComboBox, 50.0);
		this.getChildren().add(budgetComboBox);

		
		Label statusLabel = new Label("Status:");
		setTopAnchor(statusLabel, 100.0);
		setLeftAnchor(statusLabel, 250.0);
		this.getChildren().add(statusLabel);
		// TODO: replace with dynamic budget status (i.e. UNDER BUDGET, TIGHT, AT RISK, OVER BUDGET)
		Label statusValLabel = new Label("TIGHT");
		setTopAnchor(statusValLabel, 100.0);
		setLeftAnchor(statusValLabel, 300.0);
		this.getChildren().add(statusValLabel);
		
		
	}

}
