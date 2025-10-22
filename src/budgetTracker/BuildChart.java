package budgetTracker;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.*;
import javafx.scene.layout.Pane;

public class BuildChart extends Pane{
	private Budget budget;
	private LineChart lineChart;
	private BarChart barChart;
	private PieChart pieChart;
	
	
	public BuildChart(Budget budget) {
		this.budget = budget;
		
		createLineChart();
		createPieChart();
		createBarChart();
}
	
	public void createLineChart() {
		
	}
	
	public void createPieChart() {
		
		
		ObservableList<PieChart.Data> pieChartData =
	            FXCollections.observableArrayList(
	            new PieChart.Data("Grapefruit", 13),
	            new PieChart.Data("Oranges", 25),
	            new PieChart.Data("Plums", 42),
	            new PieChart.Data("Pears", 10),
	            new PieChart.Data("Apples", 10));
	    pieChart = new PieChart(pieChartData);
	    pieChart.setTitle("Imported Fruits");	    
	}
	
	public void createBarChart() {
		
	}
	
	// Swap which chart is displayed
	public void showLineChart() {
		this.getChildren().clear();
		this.getChildren().add(lineChart);			
	}
	public void showPieChart() {
		this.getChildren().clear();
		this.getChildren().add(pieChart);			
	}
	public void showBarChart() {
		this.getChildren().clear();
		this.getChildren().add(barChart);			
	}
}
