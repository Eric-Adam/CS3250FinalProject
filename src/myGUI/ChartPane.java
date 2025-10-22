package myGUI;

import budgetTracker.*;
import javafx.scene.layout.StackPane;

public class ChartPane extends StackPane{
	private BuildChart charts;
	
	public ChartPane(Budget budget) {
		this.charts = new BuildChart(budget);
		this.getChildren().add(charts);
		
		charts.showPieChart();
	}
	
	public void update() {
		charts.createLineChart();
		charts.createPieChart();
		charts.createBarChart();
	}
}
