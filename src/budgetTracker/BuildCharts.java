package budgetTracker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;

import javafx.scene.chart.*;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.layout.Pane;
import myGUI.ChartPane;

public class BuildCharts extends Pane{
	private Budget budget;
	private ChartPane chartPane;
	private double maxWidth;
	
    private CategoryAxis lineXAxis = new CategoryAxis();
    private NumberAxis lineYAxis = new NumberAxis();
	private LineChart<String, Number> lineChart = new LineChart<>(lineXAxis,lineYAxis);
	
	private PieChart pieChart = new PieChart();
	
	private BarChart barChart;

		
	public BuildCharts(Budget budget, ChartPane chartPane, double maxWidth) {
		this.budget = budget;
		this.chartPane = chartPane;
		this.maxWidth = maxWidth;
		
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
        
        for (Transaction transaction : budget.transactionList) {
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
		// Start fresh
		pieChart.getData().clear();
		
		// Create pie chart data and variables	
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
		double totalExpense = Budget.getTotalExpenses();
		double categoryExpense = 0.0;
		double runningRatio = 100.0;
		double ratio = 0.0;
		int length = Budget.categories.length-1;
		

		for(String category : Budget.categories) { 
			categoryExpense = 0;
			
			//  Keep running amount of expenses for each category
			for(Transaction transaction : budget.transactionList) {
				if (transaction.getCategory().equals(category) && !transaction.isIncome()) 
					categoryExpense += transaction.getTransactionAmount();
			}
			
			ratio = 100 * (categoryExpense / totalExpense);
			
			// Add the data to categoryRatio if it makes a large enough impact
			if (category.equals(Budget.categories[length])) {
				if (ratio > 1.0){
					pieChartData.add(new PieChart.Data(category,ratio));
				}
				else {
					pieChartData.add(new PieChart.Data("Other",runningRatio));
				}
			}
			else if (ratio>1.0) {
				pieChartData.add(new PieChart.Data(category,ratio));
				runningRatio -= ratio;
			}
		}
		
		// Add details	
		pieChart.setPrefWidth(maxWidth - 100.0);
		chartPane.setPrefWidth(maxWidth);
		pieChart.setPrefHeight(320.0);
		
		pieChart.setTitle("Expenses by Category");  
		pieChart.setLegendSide(Side.RIGHT);
		pieChart.getData().addAll(pieChartData); 
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
