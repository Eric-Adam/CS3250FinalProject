package myGUI;
import java.io.File;
import java.time.LocalDate;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import budgetTracker.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.chart.Chart;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class InputPane extends VBox{			
	private HistoryTable historyTable;
	private ChartPane chartPane;
	private TitlePane title;
	
	public void setHistoryTable(HistoryTable historyTable) {
		this.historyTable = historyTable;
	}
	public void setTitle(TitlePane title) {
		this.title = title;
	}
	public void setChart(ChartPane chartPane) {
		this.chartPane = chartPane;
	}
	
	public InputPane(Budget budget) {
		super(20);
		// -----------------------------------Transaction Section ---------------------------------
		// Transaction control buttons
		Label transactionLabel =  new Label("Transaction:");
		this.getChildren().add(transactionLabel);
		
		ToggleButton addNewTransactionButton = new ToggleButton("Add New");
		ToggleButton editTransactionButton = new ToggleButton("Edit");
		ToggleButton deleteTransactionButton = new ToggleButton("Delete");
		HBox transactionButtons = new HBox(5);
		transactionButtons.getChildren().addAll(addNewTransactionButton,
				editTransactionButton,deleteTransactionButton);
		this.getChildren().add(transactionButtons);
		
		// Add new transaction buttons/fields
		// --- Category field
		Label newTransactionLabel = new Label("Category: ");
		ComboBox<String> transactionCategory = new ComboBox<>();
		transactionCategory.setValue(Budget.categories[Budget.categories.length-1]);
		transactionCategory.getItems().addAll(Budget.categories);
		HBox newTransactionBudgetEntry = new HBox();
		newTransactionBudgetEntry.getChildren().addAll(newTransactionLabel,transactionCategory);
		this.getChildren().add(newTransactionBudgetEntry);
		
		// --- Note field
		Label newNoteLabel = new Label("Notes: ");
		TextField newNoteField = new TextField("");
		HBox noteHbox = new HBox(5);
		noteHbox.getChildren().addAll(newNoteLabel, newNoteField);
		this.getChildren().add(noteHbox);
		
		// --- Income or expense field
		Label newTransactionTypeLabel = new Label("Transaction Type:  ");
		String[] transactionTypes = {"Income", "Expense"};
		ComboBox<String> transactionType = new ComboBox<>();
		transactionType.getItems().addAll(transactionTypes);
		transactionType.setValue(transactionTypes[0]);
		HBox newTransactionTypeEntry = new HBox();
		newTransactionTypeEntry.getChildren().addAll(newTransactionTypeLabel,transactionType);
		this.getChildren().add(newTransactionTypeEntry);
		
		// --- Amount field
		Label newTransactionAmountLabel = new Label("Amount:      $");
		TextField newTransactionAmountText = new TextField("0.00");
		HBox newTransactionAmountEntry = new HBox();
		newTransactionAmountEntry.getChildren().addAll(newTransactionAmountLabel,newTransactionAmountText);
		this.getChildren().add(newTransactionAmountEntry);
		
		// --- Date field
		Label newTransactionDateLabel = new Label("Date: ");
		DatePicker newTransactionDateEntry = new DatePicker();
		newTransactionDateEntry.setValue(LocalDate.now());
		HBox newTransactionDate = new HBox();
		newTransactionDate.getChildren().addAll(newTransactionDateLabel,newTransactionDateEntry);
		this.getChildren().add(newTransactionDate);
		
		// --- Submit and cancel buttons
		Button submitTransactionButton = new Button("Submit Transaction");
		Button cancelNewTransactionButton = new Button("Cancel");
		HBox submitCancelTransaction = new HBox(5);
		submitCancelTransaction.getChildren().addAll(submitTransactionButton,cancelNewTransactionButton);
		this.getChildren().add(submitCancelTransaction);
		
		makeInvisible(newTransactionBudgetEntry,newTransactionAmountEntry,noteHbox,
				newTransactionDate,submitCancelTransaction,newTransactionTypeEntry);
		
		
		// TODO:--- --- Edit transaction section/function
		// TODO:--- --- Delete transaction section/function
				
				
		// -----------------------------------Chart Control Section -------------------------------
		// Chart control buttons
		Label chartLabel = new Label("Charts and Graphs:");
		this.getChildren().add(chartLabel);
		
		Button saveChartButton = new Button("Save");
		String[] chartTypes = {"30-Day Transactions", "Category", "In v Out"};
		ComboBox<String> chartTypeComboBox = new ComboBox<>();
		chartTypeComboBox.getItems().addAll(chartTypes);
		chartTypeComboBox.setValue("30-Day Transactions");
		HBox chartTypeHbox = new HBox(5);
		chartTypeHbox.getChildren().addAll(chartTypeComboBox,saveChartButton);
		this.getChildren().add(chartTypeHbox);
		
		
		
		
		// ----------------------------------- Listener Section -----------------------------------		
		// Transaction Listeners		
		// --- Add New Transaction: Displays buttons related to adding transactions
		addNewTransactionButton.setOnAction(event -> {
			if (addNewTransactionButton.isSelected()) {
            	// Set to default
    			transactionCategory.setValue(Budget.categories[Budget.categories.length-1]);
    			newNoteField.setText("");
    			transactionType.setValue(transactionTypes[0]);
    			newTransactionAmountText.setText("0.00");
    			newTransactionDateEntry.setValue(LocalDate.now());
    			
				makeVisible(newTransactionBudgetEntry,newTransactionAmountEntry,noteHbox,
						newTransactionDate,submitCancelTransaction,newTransactionTypeEntry);
 			
            } else {
            	makeInvisible(newTransactionBudgetEntry,newTransactionAmountEntry,noteHbox,
						newTransactionDate,submitCancelTransaction,newTransactionTypeEntry);
            }
			
			// Close addNewTransaction section if you hover over the historyTable
			// Listener in listener to avoid historyTable being null
			historyTable.hoverProperty().addListener((observable, wasHovering, isNowHovering) -> {
			    if (isNowHovering) {
			    	addNewTransactionButton.setSelected(false);
			    	makeInvisible(newTransactionBudgetEntry,newTransactionAmountEntry,noteHbox,
							newTransactionDate,submitCancelTransaction,newTransactionTypeEntry);
			    }
			});
        });
		
		// --- --- Transaction amount entry: forces entry to be valid
		newTransactionAmountText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d{0,2})?")) {
            	newTransactionAmountText.setText(oldValue);
            }
        });
		
		// --- --- Submit Transaction: pulls data from fields and saves to them to CSV
		submitTransactionButton.setOnAction(event -> {
			String file = budget.getFilePath();
			double amountEntry = Double.parseDouble(newTransactionAmountText.getText());
			String categoryEntry = transactionCategory.getValue();
			String noteEntry = newNoteField.getText();
			String typeEntry = transactionType.getValue();
			LocalDate dateEntry = newTransactionDateEntry.getValue();
			boolean inOut;
			
			if (typeEntry.equalsIgnoreCase("income")) 
				inOut = true;
			 else 
					inOut = false;
			
			Transaction newTransaction = new Transaction(amountEntry, categoryEntry, noteEntry, inOut, dateEntry);
			Transaction.saveToCSV(file, newTransaction);
			
			addNewTransactionButton.setSelected(false);
			makeInvisible(newTransactionBudgetEntry,newTransactionAmountEntry,noteHbox,
					newTransactionDate,submitCancelTransaction,newTransactionTypeEntry);
			
			update();
			
		});
		
		// --- --- Cancel Transaction: Resets addNewTransactionButton and closes new transaction section
		cancelNewTransactionButton.setOnAction(event -> {	
			addNewTransactionButton.setSelected(false);

			makeInvisible(newTransactionBudgetEntry,newTransactionAmountEntry,noteHbox,
					newTransactionDate,submitCancelTransaction,newTransactionTypeEntry);
        });		
		
		// --- TODO: Edit transaction listeners
		// --- TODO: Delete transaction listeners
		
		
		// Chart Listeners		
		// --- Select chart listener: Changes which chart is displayed
		chartTypeComboBox.setOnAction(event -> {
			String selectedChart = chartTypeComboBox.getValue();
			
			if(selectedChart.equals(chartTypes[0]))
				chartPane.charts.showLineChart();
			else if(selectedChart.equals(chartTypes[1]))
				chartPane.charts.showPieChart();
			else
				chartPane.charts.showBarChart();
		});
		
		// --- Save chart listener: Captures currently selected chart 
		saveChartButton.setOnAction(event -> {
			String[] fileNames = {"30DayTransactionOverview", "ExpenseReviewByCategory", "IncomeVsExpenses"};
			String chartNameCapture = chartTypeComboBox.getValue();
			
			if(chartNameCapture.equals(chartTypes[0]))
				saveChart(fileNames[0], chartPane.charts.lineChart);
			else if (chartNameCapture.equals(chartTypes[1]))
				saveChart(fileNames[1], chartPane.charts.pieChart);
			else 
				saveChart(fileNames[2], chartPane.charts.barChart);
        });
	
	}
	
	// Save chart to user specified location and name
	private void saveChart(String fileName, Chart chart) {
		WritableImage image = chart.snapshot(null, null);

		FileChooser fileChooser = new FileChooser();
		
        fileChooser.setTitle("Save File");
        fileChooser.setInitialFileName(fileName);
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home"), "Desktop"));
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PNG Image", "*.png")
            );

        Stage stage = (Stage) chart.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);
		
		try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } 
		catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to save chart", "Save Failure", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
	}
	
	// Make sections invisible and visible
	public void makeInvisible(Node n1,Node n2,Node n3,Node n4,Node n5,Node n6) {
		n1.setVisible(false); n1.setManaged(false);
		n2.setVisible(false); n2.setManaged(false);
		n3.setVisible(false); n3.setManaged(false);
		n4.setVisible(false); n4.setManaged(false);
		n5.setVisible(false); n5.setManaged(false);
		n6.setVisible(false); n6.setManaged(false);
	}
	public void makeVisible(Node n1,Node n2,Node n3,Node n4,Node n5,Node n6) {
		n1.setVisible(true); n1.setManaged(true);
		n2.setVisible(true); n2.setManaged(true);
		n3.setVisible(true); n3.setManaged(true);
		n4.setVisible(true); n4.setManaged(true);
		n5.setVisible(true); n5.setManaged(true);
		n6.setVisible(true); n6.setManaged(true);
	}

	private void update() {
		historyTable.update();
		title.update();
		chartPane.update();
	}

	
}
