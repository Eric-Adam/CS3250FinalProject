package budgetTracker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Side;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.layout.Pane;

import myGUI.ChartPane;

public class BuildCharts extends Pane{
	private static final double MIN_IMPACT_PERCENTAGE = 1.5;
	private static final double CHART_HEIGHT = 300.0;
	
	private Budget budget;
	private ChartPane chartPane;
	private double maxWidth;
	
    private final CategoryAxis lineXAxis = new CategoryAxis();
    private final NumberAxis lineYAxis = new NumberAxis();
	public final LineChart<String, Number> lineChart 
					= new LineChart<>(lineXAxis,lineYAxis);
	
	public final PieChart pieChart = new PieChart();
	
	private final CategoryAxis barXAxis = new CategoryAxis();
    private final NumberAxis barYAxis = new NumberAxis();
	public final BarChart<String, Number> barChart 
					= new BarChart<String, Number>(barXAxis, barYAxis);

		
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
        XYChart.Series<String, Number> lineSeries = new XYChart.Series<>();
        XYChart.Data<String, Number> lastData = null;
        
        // Reverse order for transactions to correct date axis
        ObservableList<Transaction> reversedList  = FXCollections.observableArrayList(budget.transactions);
        FXCollections.reverse(reversedList);

        for (Transaction transaction : reversedList) {
			if(transaction.getDate().isAfter(startDate)) {
				// Format date to MM-dd and keep as string
				date = transaction.getDate().format(formatter).toString();
				
				// Adjust the balance to reflect what it was on that date
				tempAmount = transaction.getTransactionAmount();
				balance += (transaction.isIncome())? tempAmount : -tempAmount;
				
				if (balance < 0.0)
					balance = 0.0;
				
				// Keep only the data for the last transaction on any single date
				if (lastDate == null || !date.equals(lastDate)) {
					lineSeries.getData().add(new Data<String, Number>(date, balance));
			        lastDate = date;
			    }
				else {
					lastData = lineSeries.getData().get(lineSeries.getData().size() - 1);
				    lastData.setYValue(balance);
				}	
			}
		}
        
        // Place data in lineChart
        lineChart.getData().add(lineSeries);

        // Add details
        lineXAxis.setLabel("Date (MM-dd)");
        lineYAxis.setLabel("Balance ($)");
        
        lineChart.setPrefWidth(maxWidth * 0.5);
        lineChart.setPrefHeight(CHART_HEIGHT);
        lineChart.setTitle("30-Day Review");
        lineChart.setLegendVisible(false);
	}

	public void createPieChart() {
		// Start fresh
		pieChart.getData().clear();
		
		// Create pie chart data and variables	
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
		int budgetLength = Budget.categories.length-1;
		double totalExpense = budget.getTotalExpenses();
		double categoryExpense = 0.0;
		double runningPercentage = 100.0;
		double percentage = 0.0;		

		for(String category : Budget.categories) { 
			categoryExpense = 0;
			
			//  Keep running amount of expenses for each category
			for(Transaction transaction : budget.transactions) {
				if (transaction.getCategory().equals(category) && !transaction.isIncome()) 
					categoryExpense += transaction.getTransactionAmount();
			}
			
			percentage = 100 * (categoryExpense / totalExpense);
			
			// Add the data to categoryRatio if it makes a large enough impact
			if (category.equals(Budget.categories[budgetLength])) {
				if (percentage > MIN_IMPACT_PERCENTAGE){
					pieChartData.add(new PieChart.Data(category,percentage));
				}
				pieChartData.add(new PieChart.Data("Other",runningPercentage));
			}
			else if (percentage > MIN_IMPACT_PERCENTAGE) {
				pieChartData.add(new PieChart.Data(category,percentage));
				runningPercentage -= percentage;
			}
		}
		
		// Add details		
        pieChart.setPrefWidth(maxWidth * 0.8);
        pieChart.setPrefHeight(CHART_HEIGHT);
        
		pieChart.setTitle("Expenses by Category");  
		pieChart.setLegendSide(Side.RIGHT);
		pieChart.getData().addAll(pieChartData); 
	}
	
	public void createBarChart() {
		// Start fresh
		barChart.getData().clear();
		
		// Create variables
		XYChart.Series<String, Number> barSeriesExpenses = new XYChart.Series<>();
		XYChart.Series<String, Number> barSeriesIncome = new XYChart.Series<>();
		
		double totalExpenses = budget.getTotalExpenses();
		double totalIncome = budget.getTotalIncome();
		
		// Prepare data for for bar chart
		XYChart.Data<String, Number> expenseData = new XYChart.Data<String, Number>("Expense", totalExpenses);
		XYChart.Data<String, Number> incomeData = new XYChart.Data<String, Number>("Income", totalIncome);
		
		barSeriesExpenses.getData().add(expenseData);
		barSeriesIncome.getData().add(incomeData);

		// Place data in bar chart
		barChart.getData().add(barSeriesIncome);
		barChart.getData().add(barSeriesExpenses);
		
		// Add details
		barChart.legendVisibleProperty().set(false);
		barChart.setTitle("Income vs. Expenses");
		
		barChart.setPrefWidth(maxWidth * 0.25);
		barChart.setCategoryGap(50.0);
		}
	
	// Swap which chart is displayed
	public void showLineChart() {
		this.getChildren().clear();
		this.getChildren().add(lineChart);	
		
		// Adjust size to better fit chart
        ChartPane.setLeftAnchor(this, maxWidth*0.2);
        chartPane.setPrefWidth(maxWidth*0.5);
        lineChart.setPrefHeight(CHART_HEIGHT); 
	}
	
	public void showPieChart() {
		this.getChildren().clear();
		this.getChildren().add(pieChart);
		
		// Adjust size to better fit chart
        ChartPane.setLeftAnchor(this, 1.0);
		chartPane.setPrefWidth(maxWidth);
		pieChart.setPrefHeight(CHART_HEIGHT); 
	}
	
	public void showBarChart() {
		this.getChildren().clear();
		this.getChildren().add(barChart);	
		
		// Adjust size to better fit chart
        ChartPane.setLeftAnchor(this, maxWidth*0.2);
        chartPane.setPrefWidth(maxWidth*0.5); 
		barChart.setPrefHeight(CHART_HEIGHT); 
	}
}
