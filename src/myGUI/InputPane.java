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
	
	private ToggleButton addNewTransactionButton = new ToggleButton("Add New");
	private ToggleButton editTransactionButton = new ToggleButton("Edit");
	private ToggleButton deleteTransactionButton = new ToggleButton("Delete");
	
	private HBox newTransactionBudgetEntry = new HBox();
	private HBox noteHbox = new HBox(5);
	private HBox newTransactionTypeEntry = new HBox();
	private HBox newTransactionAmountEntry = new HBox();
	private HBox newTransactionDate = new HBox();
	private HBox submitCancelTransaction = new HBox(5);
	
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

		newTransactionBudgetEntry.getChildren().addAll(newTransactionLabel,transactionCategory);
		this.getChildren().add(newTransactionBudgetEntry);
		
		// --- Note field
		Label newNoteLabel = new Label("Notes: ");
		TextField newNoteField = new TextField("");
		
		noteHbox.getChildren().addAll(newNoteLabel, newNoteField);
		this.getChildren().add(noteHbox);
		
		// --- Income or expense field
		Label newTransactionTypeLabel = new Label("Transaction Type:  ");
		String[] transactionTypes = {"Income", "Expense"};
		ComboBox<String> transactionType = new ComboBox<>();
		transactionType.getItems().addAll(transactionTypes);
		transactionType.setValue(transactionTypes[0]);
		
		newTransactionTypeEntry.getChildren().addAll(newTransactionTypeLabel,transactionType);
		this.getChildren().add(newTransactionTypeEntry);
		
		// --- Amount field
		Label newTransactionAmountLabel = new Label("Amount:      $");
		TextField newTransactionAmountText = new TextField("0.00");
		
		newTransactionAmountEntry.getChildren().addAll(newTransactionAmountLabel,newTransactionAmountText);
		this.getChildren().add(newTransactionAmountEntry);
		
		// --- Date field
		Label newTransactionDateLabel = new Label("Date: ");
		DatePicker newTransactionDateEntry = new DatePicker();
		newTransactionDateEntry.setValue(LocalDate.now());
		
		newTransactionDate.getChildren().addAll(newTransactionDateLabel,newTransactionDateEntry);
		this.getChildren().add(newTransactionDate);
		
		// --- Submit and cancel buttons
		Button submitTransactionButton = new Button("Submit Transaction");
		Button cancelNewTransactionButton = new Button("Cancel");
		
		submitCancelTransaction.getChildren().addAll(submitTransactionButton,cancelNewTransactionButton);
		this.getChildren().add(submitCancelTransaction);
		
		closeAddNew();
		
		
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
				// Close out other sections
				closeEdit();
				closeDelete();
				
            	// Set to default
    			transactionCategory.setValue(Budget.categories[Budget.categories.length-1]);
    			newNoteField.setText("");
    			transactionType.setValue(transactionTypes[0]);
    			newTransactionAmountText.setText("0.00");
    			newTransactionDateEntry.setValue(LocalDate.now());
    			
    			// Open Section
    			showAddNew();				
				
            } else {
            	// Close section
            	closeAddNew();
            }
			
			// Close addNewTransaction section if you hover over the historyTable
			// Listener in listener to avoid historyTable being null
			historyTable.hoverProperty().addListener((observable, wasHovering, isNowHovering) -> {
			    if (isNowHovering) {
			    	addNewTransactionButton.setSelected(false);
			    	closeAddNew();
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
		
		// --- Edit transaction listeners
		// historyTable.getColumns().add/remove(historyTable.incomeColumn);
		editTransactionButton.setOnAction(event->{
			// Close other sections
			if(addNewTransactionButton.isSelected()) {
				makeInvisible(newTransactionBudgetEntry,newTransactionAmountEntry,noteHbox,
						newTransactionDate,submitCancelTransaction,newTransactionTypeEntry);
				addNewTransactionButton.setSelected(false);
				deleteTransactionButton.setSelected(false);
			}
			if (editTransactionButton.isSelected()) {
				historyTable.setEditable(true);
				historyTable.getColumns().add(historyTable.incomeColumn);
			}
			else {
				historyTable.setEditable(false);
				historyTable.getColumns().remove(historyTable.incomeColumn);
			}
		});
		

		// --- TODO: Delete transaction listeners
		deleteTransactionButton.setOnAction(event->{
			if (deleteTransactionButton.isSelected()) {
				// Close other sections
				if(addNewTransactionButton.isSelected()) {
					makeInvisible(newTransactionBudgetEntry,newTransactionAmountEntry,noteHbox,
							newTransactionDate,submitCancelTransaction,newTransactionTypeEntry);
					addNewTransactionButton.setSelected(false);
				}
				if (editTransactionButton.isSelected()) {
					editTransactionButton.setSelected(false);
					historyTable.setEditable(false);
					historyTable.getColumns().remove(historyTable.incomeColumn);
				}
			}
				
		});
		
		
		// Chart Listeners		
		// --- Select chart listener: Changes which chart is displayed
		chartTypeComboBox.setOnAction(event -> {
			String selectedChart = chartTypeComboBox.getValue();
			
			editTransactionButton.setSelected(false);
			deleteTransactionButton.setSelected(false);

			if(selectedChart.equals(chartTypes[0]))
				chartPane.charts.showLineChart();
			else if(selectedChart.equals(chartTypes[1]))
				chartPane.charts.showPieChart();
			else
				chartPane.charts.showBarChart();
			
			if(addNewTransactionButton.isSelected()) {
				addNewTransactionButton.setSelected(false);
				makeInvisible(newTransactionBudgetEntry,newTransactionAmountEntry,noteHbox,
						newTransactionDate,submitCancelTransaction,newTransactionTypeEntry);
			}
			
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
	
	// TODO:--- --- Delete transaction section/function
	
	

	private void update() {
		historyTable.update();
		title.update();
		chartPane.update();
	}

	private void showAddNew() {
		newTransactionBudgetEntry.setVisible(true); 
		newTransactionAmountEntry.setVisible(true);
		noteHbox.setVisible(true); 
		newTransactionDate.setVisible(true); 
		submitCancelTransaction.setVisible(true); 
		newTransactionTypeEntry.setVisible(true); 
	
		newTransactionBudgetEntry.setManaged(true);
		newTransactionAmountEntry.setManaged(true);
		noteHbox.setManaged(true);
		newTransactionDate.setManaged(true);
		submitCancelTransaction.setManaged(true);
		newTransactionTypeEntry.setManaged(true);
	}
	private void closeAddNew() {
		addNewTransactionButton.setSelected(false);
		
		newTransactionBudgetEntry.setVisible(false); 
		newTransactionAmountEntry.setVisible(false);
		noteHbox.setVisible(false); 
		newTransactionDate.setVisible(false); 
		submitCancelTransaction.setVisible(false); 
		newTransactionTypeEntry.setVisible(false); 
	
		newTransactionBudgetEntry.setManaged(false);
		newTransactionAmountEntry.setManaged(false);
		noteHbox.setManaged(false);
		newTransactionDate.setManaged(false);
		submitCancelTransaction.setManaged(false);
		newTransactionTypeEntry.setManaged(false);
	}
	
	private void closeEdit() {
		if (editTransactionButton.isSelected()) {
			editTransactionButton.setSelected(false);
			historyTable.setEditable(false);
			historyTable.getColumns().remove(historyTable.incomeColumn);
		}
	}
	private void closeDelete() {
		
	}
}
