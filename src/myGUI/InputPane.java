package myGUI;

import budgetTracker.Budget;
import budgetTracker.HistoryTable;
import budgetTracker.Transaction;

import java.io.File;
import java.time.LocalDate;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;

import javafx.scene.Node;
import javafx.scene.chart.Chart;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
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
		
	private Budget budget;
	
	
	public InputPane(Budget budget) {
		super(20);
		// -----------------------------------Transaction Section ---------------------------------
		this.getChildren().add(new Separator());
		this.budget = budget;
		
		// Transaction control buttons
		HBox transactionButtons = new HBox(5);
		transactionButtons.getChildren().addAll(addNewTransactionButton,
				editTransactionButton,deleteTransactionButton);
		this.getChildren().add(transactionButtons);
	
		
		// -----------------------------------Chart Control Section -------------------------------
		this.getChildren().add(new Separator());
		
		// Chart control buttons
		Button saveChartButton = new Button("Save...");
		String[] chartTypes = {"30-Day Transactions", "Category", "In v Out"};
		ComboBox<String> chartTypeComboBox = new ComboBox<>();
		chartTypeComboBox.getItems().addAll(chartTypes);
		chartTypeComboBox.setValue("30-Day Transactions");
		
		HBox chartTypeHbox = new HBox(5);
		chartTypeHbox.getChildren().addAll(chartTypeComboBox,saveChartButton);
		this.getChildren().add(chartTypeHbox);
		this.getChildren().add(new Separator());
		
		
		// -------------------------------------Export File Section ---------------------------------
		Button exportButton = new Button("Export Data...");
		
		HBox exportHbox = new HBox(5);
		exportHbox.getChildren().add(exportButton);
		this.getChildren().add(exportHbox);
		this.getChildren().add(new Separator());
		

		// ----------------------------------- Listener Section -----------------------------------		
		// Transaction Listeners		
		// --- Add New Transaction: Displays buttons related to adding transactions
		addNewTransactionButton.setOnAction(event -> {
			if (addNewTransactionButton.isSelected()) {
				// Close out other sections
				disableEdit();
				disableDelete();
				
				addTransactionDialog();

            } else {
            	// Close section
            	disableAddNew();
            }
        });
			
		
		// --- Edit transaction listener: Makes table view editable and adds/removes column to change income type
		editTransactionButton.setOnAction(event->{
			// Close other sections
			disableAddNew();
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
		        disableAddNew();
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
		                    Budget.deleteTransaction(selected);
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
			disableAddNew();
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
		
		
		// Export Listener
		exportButton.setOnAction(event->{
			Budget.setStaticTransactions(budget.transactions);
			Budget.exportCSV();
			
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
            Budget.showAlert("Successfully saved chart " +file.getName(), "Saved Successfully");

        } 
		catch (Exception e) {
			Budget.showAlert("Failed to save chart", "Save Failure");
        }
	}

	// Keep everything in sync
	private void update() {		
		historyTable.update();
		title.update();
		chartPane.update();
	}

	// Enable/disable new transaction section
	private void disableAddNew() {
		addNewTransactionButton.setSelected(false);		
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
	
	// Add new transaction dialog box
	public void addTransactionDialog() {
		addNewTransactionButton.setSelected(false);
		
		Dialog<Transaction> dialog = new Dialog<>();
		dialog.setTitle("Add New Transaction");
		dialog.setHeaderText("Enter transaction information below.");
		
		ButtonType addNewTransactionButtonType = new ButtonType("Add", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addNewTransactionButtonType, ButtonType.CANCEL);
		
		GridPane grid = new GridPane();
		grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));
        
        
		 // Income or expense field
 		Label newTransactionTypeLabel = new Label("Transaction Type:  ");
 		String[] transactionTypes = {"Select", "Income", "Expense"};
 		ComboBox<String> transactionType = new ComboBox<>();	
 		transactionType.getItems().clear();
 		transactionType.getItems().addAll(transactionTypes);
 		transactionType.setPromptText("Select");
 		
 		grid.add(newTransactionTypeLabel, 0, 0);
 		grid.add(transactionType, 1, 0);
 				
 		// Category field
 		Label categoryLabel = new Label("Category: ");
 		ComboBox<String> transactionCategory = new ComboBox<>();
 		transactionCategory.setValue(Budget.categories[Budget.categories.length-1]);
 		
 		grid.add(categoryLabel, 0, 1);
 		grid.add(transactionCategory, 1, 1);
 		
 		// Note field
 		Label newNoteLabel = new Label("Note: ");
 		TextField newNoteField = new TextField("");
 		
 		grid.add(newNoteLabel, 0, 2);
 		grid.add(newNoteField, 1, 2);
 		
 		// Amount field
 		Label newTransactionAmountLabel = new Label("Amount:               $");
 		TextField newTransactionAmountText = new TextField("0.00");
 		
 		grid.add(newTransactionAmountLabel, 0, 3);
 		grid.add(newTransactionAmountText, 1, 3);
 		
 		// Date field
 		Label newTransactionDateLabel = new Label("Date: ");
 		DatePicker newTransactionDateEntry = new DatePicker();
 		newTransactionDateEntry.setValue(LocalDate.now());
 		
 		grid.add(newTransactionDateLabel, 0, 4);
 		grid.add(newTransactionDateEntry, 1, 4);
	
 		dialog.getDialogPane().setContent(grid);
 		
 		
 		// Disable Add button until valid
 	    Node addButton = dialog.getDialogPane().lookupButton(addNewTransactionButtonType);
 	    addButton.setDisable(true);
		
		// Validation
		// --- Check transaction type 
		transactionType.setOnAction(event->{
			transactionCategory.getItems().clear();
			
			if (transactionType.getValue() != null) {
				if (transactionType.getValue().equals(transactionTypes[1])) {
					transactionCategory.getItems().addAll(Budget.incomeCategories);
				}
				else if(transactionType.getValue().equals(transactionTypes[2])){
					transactionCategory.getItems().addAll(Budget.expenseCategories);
				}
			}
		});
		
		// --- Transaction amount entry
		newTransactionAmountText.textProperty().addListener((observable, oldValue, newValue) -> {
			// Force entry to be valid
            if (!newValue.matches("\\d*(\\.\\d{0,2})?")) { 
            	newTransactionAmountText.setText(oldValue);
            }
            else {
            	// Enable add button
            	addButton.setDisable(false);
            	}
        });
		
		    
		// Data capture
		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == addNewTransactionButtonType) {
				double amountEntry = Double.parseDouble(newTransactionAmountText.getText());
				String categoryEntry = transactionCategory.getValue();
				String noteEntry = newNoteField.getText();
				boolean typeEntry = transactionType.getValue().equalsIgnoreCase("income");
				LocalDate dateEntry = newTransactionDateEntry.getValue();
				
				Transaction newTransaction = new Transaction(amountEntry, categoryEntry, noteEntry, typeEntry, dateEntry, 0); 	   	 	
					  
				return newTransaction;
			}
				return null;
			});
		   
		
		Optional<Transaction> result = dialog.showAndWait();
		
		result.ifPresent(newTransaction -> {
			Budget.addTransaction(newTransaction, budget.getName());
			update();
			
			Budget.showAlert(
				"Added Transaction" +
				"\nCategory:\t" + newTransaction.getCategory() +
				"\nAmount:\t$" + newTransaction.getTransactionAmount() +
				"\nNote:\t" + newTransaction.getNote() +
				"\nDate:\t" + newTransaction.getDate(),
				"Added Transaction"
				);
			});	
		}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
