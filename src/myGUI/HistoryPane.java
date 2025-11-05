package myGUI;

import budgetTracker.Budget;
import budgetTracker.HistoryTable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

public class HistoryPane extends VBox{
	private final HistoryTable historyTable;
	
	public HistoryPane(Budget budget) {
		// Section Title
		setAlignment(Pos.CENTER);
		Label historyTitleLabel = new Label("Transaction History:");
		this.getChildren().add(historyTitleLabel);
		
		// TableView of transaction history
		historyTable = new HistoryTable(budget);	
		double maxWidth =Screen.getPrimary().getVisualBounds().getWidth()-100;
		historyTable.setMaxWidth(maxWidth);
		this.getChildren().add(historyTable);
		
		Label spacing = new Label("");
		this.getChildren().add(spacing);
	}		
	
	public HistoryTable getHistoryTable() {
		return historyTable;
	}
}
