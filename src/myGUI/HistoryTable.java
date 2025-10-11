package myGUI;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import budgetTracker.Transaction;
import javafx.animation.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.*;
import javafx.util.Duration;

public class HistoryTable extends TableView<Transaction>{
    private final ObservableList<Transaction> observableList = FXCollections.observableArrayList();

	
	@SuppressWarnings("unchecked")
	public HistoryTable() {
		// Pull data from CSV
		List<String[]> historyData = loadCSV();
		
		// Convert data to List of Transaction objects
		List<Transaction> transactions = historyData.stream()
		    .skip(1) // skips header
		    .map(row -> new Transaction(Double.parseDouble(row[0]), 
		    							row[1], 
		    							row[2], 
		    							Boolean.parseBoolean(row[3]), 
		    							LocalDate.parse(row[4])))
		    .collect(Collectors.toList());
		observableList.setAll(transactions);
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
		dateColumn.prefWidthProperty().bind(this.widthProperty().multiply(0.2)); 
		

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
		                ? "-fx-background-color: rgb(200,255,200);"
		                : "-fx-background-color: rgb(255,200,200);");
		        }
		    }
		});
		
		// --- Add columns to tableview
		this.getColumns().addAll(indexColumn,categoryColumn,amountColumn, 
									  noteColumn,dateColumn);
		
		// Wrap the list in a SortedList
		SortedList<Transaction> sortedList = new SortedList<>(observableList);
		sortedList.setComparator(Comparator.comparing(Transaction::getDate).reversed());
		
		// --- Set data set for tableview
		this.setItems(sortedList);
		
		// Auto-refresh every 5 seconds
		Timeline refresher = new Timeline(
		    new KeyFrame(Duration.seconds(5), e -> refreshTable())
		);
		refresher.setCycleCount(Animation.INDEFINITE); // Repeat forever
		refresher.play();
	}


	// Loads data from CSV file as unparsed strings 
	private List<String[]> loadCSV() {
		String filePath = "src/resources/transactionDB.csv"; 
        List<String[]> rows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                rows.add(values); 
            }
        } catch (Exception e) {
            System.out.println("Failed to load CSV data:\n"+e);
        }
        return rows;	
	}
	
	// Refreshes table contents when a transaction is added/deleted/edited
	public void refreshTable() {
	    List<String[]> historyData = loadCSV();

	    List<Transaction> transactions = historyData.stream()
	        .skip(1)
	        .map(row -> new Transaction(
	            Double.parseDouble(row[0]),
	            row[1],
	            row[2],
	            Boolean.parseBoolean(row[3]),
	            LocalDate.parse(row[4])
	        ))
	        .collect(Collectors.toList());

	    observableList.setAll(transactions); // Just update the list, not the TableView
	}

}
