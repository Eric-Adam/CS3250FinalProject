package myGUI;

import budgetTracker.Budget;
import budgetTracker.BuildCharts;

import javafx.scene.layout.AnchorPane;

public class ChartPane extends AnchorPane{
	public BuildCharts charts;
	private Budget budget;
	
	public ChartPane(Budget budget, double maxChartWidth) {
		this.budget = budget;
		this.charts = new BuildCharts(budget, this, maxChartWidth);
		
		setTopAnchor(charts, 15.0);
		this.getChildren().add(charts);
		
		charts.showLineChart();
		charts.setMaxWidth(maxChartWidth);
		charts.setMaxHeight(300.0);
	}
	
	public void update() {
		budget.refreshData();
		charts.createLineChart();
		charts.createPieChart();
		charts.createBarChart();
	}
	
	
}
