package myGUI;

import budgetTracker.Budget;

import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;

import javafx.scene.layout.BorderPane;

import javafx.stage.Screen;

public class TrackerPage extends BorderPane{
	public TitlePane titlePane;
	
	public TrackerPage(RunGUI runGUI, String filePath) {
		// Create budget
		Budget budget = new Budget(filePath);
		
		// Get screen sizes to set max limits for each Pane
		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		double maxTitleHeight = screenBounds.getHeight() * 0.1;
		double maxHistoryHeight = screenBounds.getHeight() * 0.35;
		double maxChartHeight = screenBounds.getHeight() * 0.4; 
		
		double screenWidth = screenBounds.getWidth();
		double maxInputWidth = screenBounds.getWidth() * 0.2;
		double maxChartWidth = screenBounds.getWidth() * 0.8;
		
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
		titlePane.getStyleClass().add("title-pane");
		this.setTop(titlePane);
		
	    // Center: Budget Charts
		ChartPane chartPane = new ChartPane(budget, maxChartWidth);
		chartPane.setPadding(chartInsets);
		chartPane.setMaxWidth(maxChartWidth);
		chartPane.setMaxHeight(maxChartHeight);
		chartPane.getStyleClass().add("chart-pane");
		this.setCenter(chartPane);     
		
	    // Bottom: Line-by-line history
		HistoryPane historyPane = new HistoryPane(budget);
		historyPane.setPadding(historyInsets);		
		historyPane.setMaxWidth(screenWidth);
		historyPane.setMaxHeight(maxHistoryHeight);
		historyPane.setPrefHeight(maxHistoryHeight);
		historyPane.getStyleClass().add("history-pane");
		
		historyPane.getHistoryTable().setTitle(titlePane);
		historyPane.getHistoryTable().setChart(chartPane);
		this.setBottom(historyPane);
		
		// Left: Filled with UI Stuff (i.e. buttons, input fields)
		InputPane inputPane = new InputPane(budget); 
		inputPane.setPadding(inputInsets);
		inputPane.setMaxWidth(maxInputWidth);
		inputPane.setPrefWidth(maxInputWidth);
		inputPane.getStyleClass().add("input-pane");
		
		inputPane.setHistoryTable(historyPane.getHistoryTable());
		inputPane.setTitle(titlePane);
		inputPane.setChart(chartPane);
		inputPane.setStage(runGUI.getStage());
		this.setLeft(inputPane);
		
		
	}
}
