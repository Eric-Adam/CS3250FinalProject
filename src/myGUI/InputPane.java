package myGUI;
import java.io.File;
import java.time.LocalDate;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import budgetTracker.*;
import javafx.embed.swing.SwingFXUtils;
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
	
	private HBox categoryBox = new HBox();
	private HBox noteBox = new HBox(5);
	private HBox transactionTypeBox = new HBox();
	private HBox transactionAmountBox = new HBox();
	private HBox transactionDateBox = new HBox();
	private HBox submitCancelBox = new HBox(5);
	
	private Budget budget;
	
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
		this.getChildren().add(new Separator());
		this.budget = budget;
		
		// Transaction control buttons
		Label transactionLabel =  new Label("\t\tTransactions:");
		this.getChildren().add(transactionLabel);
		
		HBox transactionButtons = new HBox(5);
		transactionButtons.getChildren().addAll(addNewTransactionButton,
				editTransactionButton,deleteTransactionButton);
		this.getChildren().add(transactionButtons);
		
		// Add new transaction buttons/fields
		// --- Income or expense field
		Label newTransactionTypeLabel = new Label("Transaction Type:  ");
		String[] transactionTypes = {"Income", "Expense"};
		ComboBox<String> transactionType = new ComboBox<>();	
		transactionTypeBox.getChildren().addAll(newTransactionTypeLabel,transactionType);
		this.getChildren().add(transactionTypeBox);
				
		// --- Category field
		Label categoryLabel = new Label("Category: ");
		ComboBox<String> transactionCategory = new ComboBox<>();
		transactionCategory.setValue(Budget.categories[Budget.categories.length-1]);
		categoryBox.getChildren().addAll(categoryLabel,transactionCategory);
		this.getChildren().add(categoryBox);
		
		// --- Note field
		Label newNoteLabel = new Label("Notes: ");
		TextField newNoteField = new TextField("");
		noteBox.getChildren().addAll(newNoteLabel, newNoteField);
		this.getChildren().add(noteBox);
		
		// --- Amount field
		Label newTransactionAmountLabel = new Label("Amount:      $");
		TextField newTransactionAmountText = new TextField("0.00");
		transactionAmountBox.getChildren().addAll(newTransactionAmountLabel,newTransactionAmountText);
		this.getChildren().add(transactionAmountBox);
		
		// --- Date field
		Label newTransactionDateLabel = new Label("Date: ");
		DatePicker newTransactionDateEntry = new DatePicker();
		newTransactionDateEntry.setValue(LocalDate.now());
		transactionDateBox.getChildren().addAll(newTransactionDateLabel,newTransactionDateEntry);
		this.getChildren().add(transactionDateBox);
		
		// --- Submit and cancel buttons
		Button submitTransactionButton = new Button("Submit Transaction");
		Button cancelNewTransactionButton = new Button("Cancel");
		submitCancelBox.getChildren().addAll(submitTransactionButton,cancelNewTransactionButton);
		this.getChildren().add(submitCancelBox);
		
		disableAddNew(true);
		
		
		// -----------------------------------Chart Control Section -------------------------------
		this.getChildren().add(new Separator());
		
		// Chart control buttons
		Label chartLabel = new Label("\t\tCharts and Graphs:");
		this.getChildren().add(chartLabel);
		
		Button saveChartButton = new Button("Save");
		String[] chartTypes = {"30-Day Transactions", "Category", "In v Out"};
		ComboBox<String> chartTypeComboBox = new ComboBox<>();
		chartTypeComboBox.getItems().addAll(chartTypes);
		chartTypeComboBox.setValue("30-Day Transactions");
		HBox chartTypeHbox = new HBox(5);
		chartTypeHbox.getChildren().addAll(chartTypeComboBox,saveChartButton);
		this.getChildren().addAll(chartTypeHbox, new Separator());

		
		// ----------------------------------- Listener Section -----------------------------------		
		// Transaction Listeners		
		// --- Add New Transaction: Displays buttons related to adding transactions
		addNewTransactionButton.setOnAction(event -> {
			if (addNewTransactionButton.isSelected()) {
				// Close out other sections
				disableEdit();
				disableDelete();
				
            	// Set to default
    			transactionCategory.setValue(Budget.categories[Budget.categories.length-1]);
    			newNoteField.setText("");
    			newTransactionAmountText.setText("0.00");
    			newTransactionDateEntry.setValue(LocalDate.now());
    			
    			// Open Section
    			transactionTypeBox.setVisible(true);
    			transactionTypeBox.setManaged(true); 
    			
    			// Re-render stubborn comboBox
    			transactionType.getItems().clear();
    			transactionType.getItems().addAll(transactionTypes);
    			transactionType.applyCss();
    			transactionType.layout();
    			transactionType.setValue(null);
    			transactionType.setPromptText("Select");
    			
    			// Close addNewTransaction section if you hover over the historyTable
    			// Listener in listener to avoid historyTable being null
    			historyTable.hoverProperty().addListener((observable, wasHovering, isNowHovering) -> {
    			    if (isNowHovering) {
    			    	disableAddNew(true);
    			    }
    			});
				
            } else {
            	// Close section
            	disableAddNew(true);
            }
			
			
        });
		
		// --- --- Check transaction type before opening everything else
		transactionType.setOnAction(event->{
			transactionCategory.getItems().clear();
			if (transactionType.getValue() != null) {
				if (transactionType.getValue().equals(transactionTypes[0])) {
					transactionCategory.getItems().addAll(Budget.incomeCategories);
					disableAddNew(false);
				}
				else if(transactionType.getValue().equals(transactionTypes[1])){
					transactionCategory.getItems().addAll(Budget.expenseCategories);
					disableAddNew(false);
				}
			}
		});
		
		// --- --- Transaction amount entry: forces entry to be valid
		newTransactionAmountText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d{0,2})?")) { 
            	newTransactionAmountText.setText(oldValue);
            }
        });
		
		// --- --- Submit Transaction: pulls data from fields and saves to them to CSV
		submitTransactionButton.setOnAction(event -> {
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
			budget.transactions.add(newTransaction);
			update();
			
			disableAddNew(true);			
		});
		
		// --- --- Cancel Transaction: Resets addNewTransactionButton and closes new transaction section
		cancelNewTransactionButton.setOnAction(event -> {	
			disableAddNew(true);
        });		
		
		// --- Edit transaction listeners
		// historyTable.getColumns().add/remove(historyTable.incomeColumn);
		editTransactionButton.setOnAction(event->{
			// Close other sections
			disableAddNew(true);
			disableDelete();
			
			// Open Section
			if (editTransactionButton.isSelected()) {
				historyTable.setEditable(true);
				historyTable.getColumns().add(historyTable.incomeColumn);
			}
			else {
				disableEdit();
			}
		});
		

		// --- Delete transaction listeners
		deleteTransactionButton.setOnAction(event -> {
		    if (deleteTransactionButton.isSelected()) {
		        // Close other sections
		        disableAddNew(true);
		        disableEdit();

		        // Indicate deleting is active 
		        historyTable.setStyle("-fx-border-color: #f16357; -fx-border-width: 2;");

		        // Select which transaction to delete
		        historyTable.setOnMouseClicked(e -> {
		            Transaction selected = historyTable.getSelectionModel().getSelectedItem();

		            // Confirm user wants to delete transaction
		            if (selected != null) {
		                int response = JOptionPane.showConfirmDialog(null,
		                    "Are you sure you want to delete this transaction?\n\n"
		                    + "Category: " + selected.getCategory()
		                    + "\nAmount: $" + selected.getTransactionAmount()
		                    + "\nNote: " + selected.getNote()
		                    + "\nDate: " + selected.getDate(),
		                    "Confirm Deletion",
		                    JOptionPane.YES_NO_OPTION
		                );
		                
		                // Remove selected transaction
		                if (response == JOptionPane.YES_OPTION) {
		                    budget.transactions.remove(selected);
		                    update();
		                }
		            }
		        });
		    } else {
		        disableDelete();
		    }
		});
		
		// Chart Listeners		
		// --- Select chart listener: Changes which chart is displayed
		chartTypeComboBox.setOnAction(event -> {
			String selectedChart = chartTypeComboBox.getValue();
			
			// Close other Sections
			disableAddNew(true);
			disableEdit();
			disableDelete();
			
			// Display selected chart
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
	
	// Save chart with user specified location and name
	private void saveChart(String fileName, Chart chart) {
		WritableImage image = chart.snapshot(null, null);
		
		try {
			FileChooser fileChooser = new FileChooser();
			
	        fileChooser.setTitle("Save File");
	        fileChooser.setInitialFileName(fileName);
	        fileChooser.setInitialDirectory(new File(System.getProperty("user.home"), "Desktop"));
	        fileChooser.getExtensionFilters().add(
	                new FileChooser.ExtensionFilter("PNG Image", "*.png")
	            );
	
	        Stage stage = (Stage) chart.getScene().getWindow();
	        File file = fileChooser.showSaveDialog(stage);
		
		
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } 
		catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to save chart", "Save Failure", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
	}

	// Keep everything in sync
	private void update() {
		budget.overwrite();
		
		historyTable.update();
		title.update();
		chartPane.update();
	}

	// Enable/disable new transaction section
	private void disableAddNew(boolean disable) {
		boolean enabled = !disable;
		
		addNewTransactionButton.setSelected(enabled);
		
		transactionTypeBox.setVisible(enabled);
		categoryBox.setVisible(enabled); 
		transactionAmountBox.setVisible(enabled);
		noteBox.setVisible(enabled); 
		transactionDateBox.setVisible(enabled); 
		submitCancelBox.setVisible(enabled);  
		
		transactionTypeBox.setManaged(enabled);
		categoryBox.setManaged(enabled);
		transactionAmountBox.setManaged(enabled);
		noteBox.setManaged(enabled);
		transactionDateBox.setManaged(enabled);
		submitCancelBox.setManaged(enabled);
		
	}
	
	// Disable edit section
	private void disableEdit() {
		editTransactionButton.setSelected(false);
		
		historyTable.setEditable(false);
		historyTable.getColumns().remove(historyTable.incomeColumn);
	}
	
	// Disable delete section
	private void disableDelete() {
		deleteTransactionButton.setSelected(false);
		
		historyTable.setOnMouseClicked(null);
		historyTable.setStyle("");
	}
}
