package budgetTracker;

import java.time.LocalDate;
import java.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.*;

public class HistoryTable extends TableView<Transaction>{
    private final ObservableList<Transaction> observableList = FXCollections.observableArrayList();
    private final Budget budget;
	
	@SuppressWarnings("unchecked")
	public HistoryTable(Budget b) {
		// Assign Budget object
		budget = b;
		observableList.setAll(budget.observableList);
		
		// Set up TableView		
		// --- Tableview Columns
		// --- --- Index column
		TableColumn<Transaction, Integer> indexColumn = new TableColumn<>("ID");
		indexColumn.setCellFactory(col -> new TableCell<>() {
		    @Override
		    protected void updateItem(Integer item, boolean empty) {
		        super.updateItem(item, empty);
		        if (empty || getTableRow() == null || getTableRow().getIndex() < 0) {
		            setText(null);
		        } else {
		            setText(String.valueOf(getIndex() + 1));
		        }
		    }
		});
		indexColumn.prefWidthProperty().bind(this.widthProperty().multiply(0.05)); 
		
		// --- --- Category column
		TableColumn<Transaction, String> categoryColumn = new TableColumn<>("Category");
		categoryColumn.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
		categoryColumn.prefWidthProperty().bind(this.widthProperty().multiply(0.2)); 
		
		// --- --- Amount column
		TableColumn<Transaction, Double> amountColumn = new TableColumn<>("Amount");
		amountColumn.setCellValueFactory(cellData -> cellData.getValue().transactionAmountProperty().asObject());
		amountColumn.prefWidthProperty().bind(this.widthProperty().multiply(0.1)); 
		
		// --- --- Note column
		TableColumn<Transaction, String> noteColumn = new TableColumn<>("Note");
		noteColumn.setCellValueFactory(cellData -> cellData.getValue().noteProperty());
		noteColumn.prefWidthProperty().bind(this.widthProperty().multiply(0.4)); 
		
		// --- --- Date column
		TableColumn<Transaction, LocalDate> dateColumn = new TableColumn<>("Date");
		dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
		dateColumn.prefWidthProperty().bind(this.widthProperty().multiply(0.15)); 
		
		// --- --- Income column
		TableColumn<Transaction, Boolean> incomeColumn = new TableColumn<>("Income");
		incomeColumn.setCellValueFactory(cellData -> cellData.getValue().incomeProperty());
		incomeColumn.prefWidthProperty().bind(this.widthProperty().multiply(0.1));
		

		// --- Set column alignment
		//--- --- Amount to the right
		amountColumn.setCellFactory(col -> new TableCell<>() {
		    @Override
		    protected void updateItem(Double amount, boolean empty) {
		        super.updateItem(amount, empty);
		        if (empty || amount == null) {
		            setText(null);
		        } else {
		            setText(String.format("%.2f", amount));
		            setStyle("-fx-alignment: CENTER-RIGHT;");
		        }
		    }
		});
		// --- --- Category to center
		categoryColumn.setCellFactory(col -> new TableCell<>() {
		    @Override
		    protected void updateItem(String category, boolean empty) {
		        super.updateItem(category, empty);
		        if (empty || category == null) {
		            setText(null);
		        } else {
		            setText(category);
		            setStyle("-fx-alignment: CENTER;");
		        }
		    }
		});
		// --- --- Note to center
		noteColumn.setCellFactory(col -> new TableCell<>() {
		    @Override
		    protected void updateItem(String note, boolean empty) {
		        super.updateItem(note, empty);
		        if (empty || note == null) {
		            setText(null);
		        } else {
		            setText(note);
		            setStyle("-fx-alignment: CENTER;");
		        }
		    }
		});
		
		// --- Color rows based on income or expense
		this.setRowFactory(tv -> new TableRow<>() {
		    @Override
		    protected void updateItem(Transaction item, boolean empty) {
		        super.updateItem(item, empty);
		        if (empty || item == null) {
		            setStyle("");
		        } else {
		            setStyle(item.isIncome()
		                ? "-fx-background-color: rgb(230,255,230);"
		                : "-fx-background-color: rgb(255,230,230);");
		        }
		    }
		});
		
		// Change income column to checkboxes instead of boolean values
		incomeColumn.setCellFactory(col -> new TableCell<Transaction, Boolean>() {
		    private final CheckBox checkBox = new CheckBox();
		    {
		        checkBox.setDisable(true); // Read only for now
		        setStyle("-fx-alignment: CENTER;");
		    }

		    @Override
		    protected void updateItem(Boolean item, boolean empty) {
		        super.updateItem(item, empty);
		        if (empty || item == null) {
		            setGraphic(null);
		        } else {
		            checkBox.setSelected(item);
		            setGraphic(checkBox);
		        }
		    }
		});
		
		// --- Add columns to tableview
		this.getColumns().addAll(indexColumn,categoryColumn,amountColumn, 
									  noteColumn,dateColumn,incomeColumn);
		
		// Sort the list by wrapping in a SortedList
		SortedList<Transaction> sortedList = new SortedList<>(observableList);
		sortedList.setComparator(Comparator.comparing(Transaction::getTransactionID).reversed());
		
		// Set data for tableview
		this.setItems(sortedList);
	}
	
	public void update() {
		budget.refreshData();
		observableList.setAll(budget.observableList);
	}
	
}
