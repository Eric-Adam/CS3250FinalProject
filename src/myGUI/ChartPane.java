package myGUI;

import budgetTracker.*;
import javafx.scene.layout.AnchorPane;

public class ChartPane extends AnchorPane{
	private BuildCharts charts;
	private Budget budget;
	
	public ChartPane(Budget budget, double maxChartWidth) {
		this.budget = budget;
		this.charts = new BuildCharts(budget, this,maxChartWidth);
		this.getChildren().add(charts);	
		charts.showLineChart();
	}
	
	public void update() {
		budget.refreshData();
		charts.createLineChart();
		charts.createPieChart();
		charts.createBarChart();
	}
}
