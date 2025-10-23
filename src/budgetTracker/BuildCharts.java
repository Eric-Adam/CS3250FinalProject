package budgetTracker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.scene.chart.*;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.layout.Pane;

public class BuildCharts extends Pane{
	private Budget budget;
	
    private CategoryAxis lineXAxis = new CategoryAxis();
    private NumberAxis lineYAxis = new NumberAxis();
	private LineChart<String, Number> lineChart = new LineChart<>(lineXAxis,lineYAxis);
	
	private PieChart pieChart;
	private BarChart barChart;

		
	public BuildCharts(Budget budget) {
		this.budget = budget;
		
		createLineChart();
		createPieChart();
		createBarChart();
}
	
	public void createLineChart() {	
		// Start fresh
		lineChart.getData().clear();
		
		// Create necessary variables
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");	
		LocalDate today = LocalDate.now();
		LocalDate startDate = today.minusDays(30);
		String date = "";
		String lastDate = null;
		double balance = budget.getEarlierBalance(startDate);
		double tempAmount = 0;
        
        // Prepare data
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        XYChart.Data<String, Number> lastData = null;
        
        for (Transaction transaction : budget.observableList) {
			if(transaction.getDate().isAfter(startDate)) {
				// Format date to MM-dd and keep as string
				date = transaction.getDate().format(formatter).toString();
				
				// Adjust the balance to reflect what it was on that date
				tempAmount = transaction.getTransactionAmount();
				balance += (transaction.isIncome())? tempAmount : -tempAmount;
				
				// Only add data for last transaction on any single date
				if (lastDate == null || !date.equals(lastDate)) {
					series.getData().add(new Data<String, Number>(date, balance));
			        lastDate = date;
			    }
				else {
					lastData = series.getData().get(series.getData().size() - 1);
				    lastData.setYValue(balance);
				}	
			}
		}
        lineChart.getData().add(series);

        // Add details
        lineXAxis.setLabel("Date (MM-dd)");
        lineYAxis.setLabel("Balance ($)");
        
        lineChart.setTitle("Transactions for the Last 30-Days");
        lineChart.setLegendVisible(false);
        lineChart.setMaxHeight(300.0);
	}

	public void createPieChart() {
		// TODO: create pie chart
		
//		ObservableList<PieChart.Data> pieChartData =
//	            FXCollections.observableArrayList(
//	            new PieChart.Data("Grapefruit", 13),
//	            new PieChart.Data("Oranges", 25),
//	            new PieChart.Data("Plums", 42),
//	            new PieChart.Data("Pears", 10),
//	            new PieChart.Data("Apples", 10));
//	    pieChart = new PieChart(pieChartData);
//	    pieChart.setPrefHeight(300);
//	    pieChart.setTitle("Imported Fruits");	    
	}
	
	public void createBarChart() {
		// TODO: create Bart chart
		
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
