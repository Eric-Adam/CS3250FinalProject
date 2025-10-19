package myGUI;

import budgetTracker.Budget;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class TrackerPage extends BorderPane{
	
	public TrackerPage(Stage primaryStage, RunGUI runGUI, String filePath) {
	Budget budget = new Budget(filePath);
	
	// Padding
	Insets titleInsets = new Insets(25,10,25,10);
	Insets inputInsets = new Insets(25,10,25,10);
	Insets chartInsets = new Insets(25,10,25,10);
	Insets historyInsets = new Insets(25,10,10,10);
	
	// Top: Title, Status, Selected Budget
	TitlePane titlePane = new TitlePane(primaryStage, runGUI, budget);
	titlePane.setPadding(titleInsets);
	this.setTop(titlePane);
	
    // Bottom: Line-by-line history
	HistoryPane historyPane = new HistoryPane(budget);
	historyPane.setPadding(historyInsets);		
	this.setBottom(historyPane);
	
    // Center: Budget Charts
	ChartPane chartPane = new ChartPane();
	chartPane.setMinSize(300, 300);
	chartPane.setPadding(chartInsets);
	chartPane.setMaxHeight(500);
	this.setCenter(chartPane);     
	
	// Left: Filled with UI Stuff (i.e. buttons, input fields)
	InputPane inputPane = new InputPane(budget); 
	inputPane.setPadding(inputInsets);
	inputPane.setHistoryTable(historyPane.getHistoryTable());
	inputPane.setTitle(titlePane);
	this.setLeft(inputPane);
	}
}
