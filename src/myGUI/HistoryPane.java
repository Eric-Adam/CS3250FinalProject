package myGUI;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class HistoryPane extends VBox{
	public HistoryPane() {
		// Section Title
		setAlignment(Pos.CENTER);
		Label historyTitleLabel = new Label("Transaction History:");
		this.getChildren().add(historyTitleLabel);
		
		// TableView of transaction history
		HistoryTable historyTable = new HistoryTable();	
		this.getChildren().add(historyTable);
	}			
}
