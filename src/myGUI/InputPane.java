package myGUI;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import budgetTracker.Budget;
import budgetTracker.HistoryTable;
import budgetTracker.Transaction;

import javafx.embed.swing.SwingFXUtils;

import javafx.scene.chart.Chart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class InputPane extends VBox{			
	private HistoryTable historyTable;
	private ChartPane chartPane;
	private TitlePane title;
	private Stage primaryStage;
	
	private ToggleButton addNewTransactionButton = new ToggleButton("Add New");
	private ToggleButton editTransactionButton = new ToggleButton("Edit");
	private ToggleButton deleteTransactionButton = new ToggleButton("Delete");
	
	private ArrayList<HBox> hBoxList = new ArrayList<>(); 
	
	private Budget budget;
	
	
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
		String[] transactionTypes = {"Select", "Income", "Expense"};
		ComboBox<String> transactionType = new ComboBox<>();	
		transactionType.getItems().clear();
		transactionType.getItems().addAll(transactionTypes);
		transactionType.setPromptText("Select");
		
		HBox transactionTypeBox = new HBox();
		transactionTypeBox.getChildren().addAll(newTransactionTypeLabel,transactionType);
		hBoxList.add(transactionTypeBox);
		this.getChildren().add(transactionTypeBox);
				
		// --- Category field
		Label categoryLabel = new Label("Category: ");
		ComboBox<String> transactionCategory = new ComboBox<>();
		transactionCategory.setValue(Budget.categories[Budget.categories.length-1]);
		
		HBox categoryBox = new HBox();
		categoryBox.getChildren().addAll(categoryLabel,transactionCategory);
		hBoxList.add(categoryBox);
		this.getChildren().add(categoryBox);
		
		// --- Note field
		Label newNoteLabel = new Label("Notes: ");
		TextField newNoteField = new TextField("");
		
		HBox noteBox = new HBox(5);
		noteBox.getChildren().addAll(newNoteLabel, newNoteField);
		hBoxList.add(noteBox);
		this.getChildren().add(noteBox);
		
		// --- Amount field
		Label newTransactionAmountLabel = new Label("Amount:      $");
		TextField newTransactionAmountText = new TextField("0.00");
		
		HBox transactionAmountBox = new HBox();
		transactionAmountBox.getChildren().addAll(newTransactionAmountLabel,newTransactionAmountText);
		hBoxList.add(transactionAmountBox);
		this.getChildren().add(transactionAmountBox);
		
		// --- Date field
		Label newTransactionDateLabel = new Label("Date: ");
		DatePicker newTransactionDateEntry = new DatePicker();
		newTransactionDateEntry.setValue(LocalDate.now());
		
		HBox transactionDateBox = new HBox();
		transactionDateBox.getChildren().addAll(newTransactionDateLabel,newTransactionDateEntry);
		hBoxList.add(transactionDateBox);
		this.getChildren().add(transactionDateBox);
		
		// --- Submit and cancel buttons
		Button submitTransactionButton = new Button("Submit Transaction");
		Button cancelNewTransactionButton = new Button("Cancel");
		
		HBox submitCancelBox = new HBox(5);
		submitCancelBox.getChildren().addAll(submitTransactionButton,cancelNewTransactionButton);
		hBoxList.add(submitCancelBox);
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
    			transactionType.setValue(transactionTypes[0]);
    			
    			
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
				if (transactionType.getValue().equals(transactionTypes[1])) {
					transactionCategory.getItems().addAll(Budget.incomeCategories);
					disableAddNew(false);
				}
				else if(transactionType.getValue().equals(transactionTypes[2])){
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
		
		// --- --- Submit Transaction: pulls data from fields, adds it to the transaction list and updates database
		submitTransactionButton.setOnAction(event -> {
			double amountEntry = Double.parseDouble(newTransactionAmountText.getText());
			String categoryEntry = transactionCategory.getValue();
			String noteEntry = newNoteField.getText();
			Boolean typeEntry = transactionType.getValue().equalsIgnoreCase("income");
			LocalDate dateEntry = newTransactionDateEntry.getValue();
			
			Transaction newTransaction = new Transaction(amountEntry, categoryEntry, noteEntry, typeEntry, dateEntry);
			budget.transactions.add(newTransaction);
			update();
			
			showAlert("Added Transaction"
					  +"\nCategory:\t"+categoryEntry
					  +"\nAmount:\t$"+amountEntry
					  +"\nNote:\t"+noteEntry
					  +"\nDate:\t"+dateEntry,
					  "Added Transaction");
			
			disableAddNew(true);			
		});
		
		// --- --- Cancel Transaction: Closes new transaction section
		cancelNewTransactionButton.setOnAction(event -> {	
			disableAddNew(true);
        });		
		
		// --- Edit transaction listener: Makes table view editable and adds/removes column to change income type
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
				// Close section
				disableEdit();
			}
		});
		

		// --- Delete transaction listener
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
		                    showAlert("Transaction removed","Removed Transaction");
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
		
		FileChooser fileChooser = new FileChooser();
		
        fileChooser.setTitle("Save File");
        fileChooser.setInitialFileName(fileName);
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home"), "Desktop"));
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PNG Image", "*.png")
            );
        
		try {
			
	        File file = fileChooser.showSaveDialog(primaryStage);
	        
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
			showAlert("Successfully saved chart " +file.getName(), "Saved Successfully");

        } 
		catch (Exception e) {
			showAlert("Failed to save chart", "Save Failure");
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
		
		for (HBox box : hBoxList) {
			box.setVisible(enabled);
			box.setManaged(enabled);
		}		
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
	
	// Show alerts
	private void showAlert(String message, String title) {
		Alert alert = new Alert(AlertType.NONE, message, ButtonType.OK);
		
		alert.setTitle(title);
		alert.initOwner(primaryStage);
		alert.showAndWait();
	}
	
	public Stage getStage() {
		return primaryStage;
	}
	public void setStage(Stage stage) {
		this.primaryStage = stage;
	}
	public void setHistoryTable(HistoryTable historyTable) {
		this.historyTable = historyTable;
	}
	public void setTitle(TitlePane title) {
		this.title = title;
	}
	public void setChart(ChartPane chartPane) {
		this.chartPane = chartPane;
	}
}
