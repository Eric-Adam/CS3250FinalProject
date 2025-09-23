import java.time.LocalDate;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class RunGUI extends VBox{
	public RunGUI(){
		super(10);
		String[] transCategories = {"Rent", "Groceries", "Streaming"};
		// ------ NEED TO CREATE CATEGORY OBJECTS
		
		// Title
		Label titleLabel =  new Label("Budget Tracker");
		this.getChildren().add(titleLabel);
		
		
		
		// Split screen in half--- Left has buttons
		// Categories
		Label categoryLabel =  new Label("Category:	");
		this.getChildren().add(categoryLabel);
		
		Button addNewCategory = new Button("Add New");
		Button EditCategory = new Button("Edit");
		Button deleteCategory = new Button("Delete");
		this.getChildren().addAll(addNewCategory,EditCategory,deleteCategory);
		
		// ---New category
		// ---Edit category
		// ---Delete category
		
		
		// Budgets
		Label budgetLabel =  new Label("Budget:	");
		this.getChildren().add(budgetLabel);
		
		Button addNewBudget = new Button("Add New");
		Button EditBudget = new Button("Edit");
		Button deleteBudget = new Button("Delete");
		this.getChildren().addAll(addNewBudget,EditBudget,deleteBudget);
		
		// ---New budget
		// ---Edit budget
		// ---Delete budget
		
		
		// Transactions
		Label transactionLabel =  new Label("Transaction:	");
		this.getChildren().add(transactionLabel);
		// WANT TO BE OFF TO THE SIDE NOT LINE-BY-LINE
		Button addNewTransaction = new Button("Add New");
		Button EditTransaction = new Button("Edit");
		Button deleteTransaction = new Button("Delete");
		this.getChildren().addAll(addNewTransaction,EditTransaction,deleteTransaction);
		
		// ---New transaction -- WANT INVISIBLE UNTIL ADD NEW IS CLICKED
		// --- ---Category		
		ComboBox<String> transCategoryComboBox = new ComboBox<>();
		transCategoryComboBox.getItems().addAll(transCategories);
		transCategoryComboBox.setPromptText("Category");
		this.getChildren().add(transCategoryComboBox);
		// --- ---Need/Want
		ToggleButton needButton = new ToggleButton("Need");
		this.getChildren().add(needButton);
		needButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
            	needButton.setText("Want");
            } else {
            	needButton.setText("Need");
            }
        });
		// --- ---Amount
		Label amountLabel = new Label("Amount:  ");
		TextField amountField = new TextField();	
        this.getChildren().addAll(amountLabel, amountField);
        // --- ---Date/Time
        DatePicker datePicker = new DatePicker();
        datePicker.setValue(LocalDate.now());
        this.getChildren().add(datePicker);
        // --- ---Submit
        Button submitTransaction = new Button("Submit");
        this.getChildren().add(submitTransaction);
		// ---Edit transaction
		// ---Delete transaction

        
        
        // Budget Charts in the right half of screen

        
        
        // Line-by-line history under charts and buttons
	}
}
