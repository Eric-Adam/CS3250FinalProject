package budgetTracker;

import java.time.LocalDate;
import java.util.*;

import javafx.collections.FXCollections;
import javafx.collections.transformation.SortedList;

import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import myGUI.ChartPane;
import myGUI.TitlePane;

public class HistoryTable extends TableView<Transaction>{
    private final Budget budget;
	private ChartPane chartPane;
	private TitlePane title;
    
    public TableColumn<Transaction, Boolean> incomeColumn = new TableColumn<>("Income");
    public TableColumn<Transaction, LocalDate> dateColumn = new TableColumn<>("Date");

	
	@SuppressWarnings("unchecked")
	public HistoryTable(Budget budget) {
		// Assign Budget object
		this.budget = budget;
		
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
		categoryColumn.setCellFactory(ComboBoxTableCell.forTableColumn(
			    FXCollections.observableArrayList(Budget.categories)
				));
		categoryColumn.prefWidthProperty().bind(this.widthProperty().multiply(0.2)); 
		categoryColumn.setOnEditCommit(event -> {
			event.getRowValue().setCategory(event.getNewValue());
			budget.overwrite();
            update();
		});

		// --- --- Amount column
		TableColumn<Transaction, Double> amountColumn = new TableColumn<>("Amount");
		amountColumn.setCellValueFactory(cellData -> cellData.getValue().transactionAmountProperty().asObject());
		amountColumn.setCellFactory(TextFieldTableCell.forTableColumn(new javafx.util.converter.DoubleStringConverter()));
		amountColumn.prefWidthProperty().bind(this.widthProperty().multiply(0.1)); 
		amountColumn.setOnEditCommit(event-> {
			try {
				event.getRowValue().setTransactionAmount(event.getNewValue());
				budget.overwrite();
                update();
			} catch (NumberFormatException e){
				System.out.println("Failed to edit amount.\n");
				e.printStackTrace();
			}
		});

		// --- --- Note column
		TableColumn<Transaction, String> noteColumn = new TableColumn<>("Note");
		noteColumn.setCellValueFactory(cellData -> cellData.getValue().noteProperty());
		noteColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		noteColumn.prefWidthProperty().bind(this.widthProperty().multiply(0.4)); 
		noteColumn.setOnEditCommit(event-> {
			event.getRowValue().setNote(event.getNewValue());
			budget.overwrite();
            update();
		});
		
		// --- --- Date column
		dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
		dateColumn.prefWidthProperty().bind(this.widthProperty().multiply(0.15)); 
		dateColumn.setOnEditCommit(event -> {
		    event.getRowValue().setDate(event.getNewValue());
		    budget.overwrite();
            update();
		});
		dateColumn.setCellFactory(column -> new TableCell<Transaction, LocalDate>() {
		    private final DatePicker datePicker = new DatePicker();

		    {
		        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

		        // Commit when a date is picked
		        datePicker.setOnAction(event -> {
		        	LocalDate newDate = datePicker.getValue();
	                commitEdit(newDate);
		        });
		    }

		    @Override
		    public void startEdit() {
		        super.startEdit();
		        if (!isEmpty()) {
		            datePicker.setDisable(false);
		            setGraphic(datePicker);
		        }
		    }

		    @Override
		    protected void updateItem(LocalDate item, boolean empty) {
		        super.updateItem(item, empty);
		        if (empty || item == null) {
		            setGraphic(null);
		        } else {
		            datePicker.setValue(item);
		            if (isEditing()) {
		                datePicker.setDisable(false);
		                setGraphic(datePicker);
		            } else {
		                datePicker.setDisable(true);
		                setGraphic(datePicker);
		            }
		        }
		    }
		});

		// --- --- Income column
		incomeColumn.setCellValueFactory(cellData -> cellData.getValue().incomeProperty());
		incomeColumn.prefWidthProperty().bind(this.widthProperty().multiply(0.1));
		incomeColumn.setCellFactory(col -> new TableCell<Transaction, Boolean>() {
		    private final CheckBox checkBox = new CheckBox();

		    {
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

		            // Remove old listener to avoid stacking
		            checkBox.setOnAction(null);

		            // Get the correct Transaction object
		            Transaction transaction = getTableView().getItems().get(getIndex());

		            checkBox.setOnAction(event -> {
		                transaction.setIncome(checkBox.isSelected());
		                budget.overwrite();
		                update();
		            });
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
		                ? "-fx-background-color: rgb(210, 255, 210);"
		                : "-fx-background-color: rgb(255, 225, 225);");
		        }
		    }
		});		
		
		// --- Add columns to tableview
		this.getColumns().addAll(indexColumn,categoryColumn,amountColumn, 
									  noteColumn,dateColumn);
		
		// Sort the list by wrapping in a SortedList
		SortedList<Transaction> sortedList = new SortedList<>(budget.transactions);
		sortedList.setComparator(Comparator.comparing(Transaction::getTransactionID).reversed());
		
		// Set data for tableview
		this.setItems(sortedList);
	}
	
	public void update() {
		budget.refreshData();
		title.update();
		chartPane.update();
	}

	public void setChart(ChartPane chartPane) {this.chartPane = chartPane;	}
	public void setTitle(TitlePane title) {this.title = title;	}	
}
