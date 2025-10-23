package myGUI;

import budgetTracker.Budget;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.*;
import javafx.stage.Screen;

public class TrackerPage extends BorderPane{
	
	public TrackerPage(RunGUI runGUI, String filePath) {
		// Create budget
		Budget budget = new Budget(filePath);
		
		// Get screen sizes to set max limits for each Pane
		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		double maxTitleHeight = screenBounds.getHeight() * 0.2;
		double maxHistoryHeight = screenBounds.getHeight() * 0.4;
		double maxChartHeight = screenBounds.getHeight() * 0.4; 
		
		double screenWidth = screenBounds.getWidth();
		double maxInputWidth = screenBounds.getWidth() * 0.4;
		double maxCharttWidth = screenBounds.getWidth() * 0.6;
		
		// Padding
		Insets titleInsets = new Insets(25,10,25,10);
		Insets inputInsets = new Insets(25,10,25,10);
		Insets chartInsets = new Insets(25,10,25,10);
		Insets historyInsets = new Insets(25,10,10,10);
		
		// Top: Title, Status, Balance, Logout
		TitlePane titlePane = new TitlePane(runGUI, budget);
		titlePane.setPadding(titleInsets);
		titlePane.setMaxWidth(screenWidth);
		titlePane.setPrefWidth(screenWidth);
		titlePane.setMaxHeight(maxTitleHeight);
		this.setTop(titlePane);
		
	    // Bottom: Line-by-line history
		HistoryPane historyPane = new HistoryPane(budget);
		historyPane.setPadding(historyInsets);		
		historyPane.setMaxWidth(screenWidth);
		historyPane.setMaxHeight(maxHistoryHeight);
		historyPane.setPrefHeight(maxHistoryHeight);
		this.setBottom(historyPane);
		
	    // Center: Budget Charts
		ChartPane chartPane = new ChartPane(budget);
		chartPane.setPadding(chartInsets);
		chartPane.setMaxWidth(maxCharttWidth);
		chartPane.setMaxHeight(maxChartHeight);
		this.setCenter(chartPane);     
		
		// Left: Filled with UI Stuff (i.e. buttons, input fields)
		InputPane inputPane = new InputPane(budget); 
		inputPane.setPadding(inputInsets);
		inputPane.setMaxWidth(maxInputWidth);
		inputPane.setPrefWidth(maxInputWidth);
		
		inputPane.setHistoryTable(historyPane.getHistoryTable());
		inputPane.setTitle(titlePane);
		inputPane.setChart(chartPane);
		this.setLeft(inputPane);
	}
}
