package myGUI;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class InputPane extends VBox{
	public InputPane() {
		super(20);
		// -----------------------------------Budget Section --------------------------------------
		// Basic control buttons
		Label budgetLabel =  new Label("Budget:");
		this.getChildren().add(budgetLabel);
		Button addNewBudgetButton = new Button("Add New");
		Button editBudgetButton = new Button("Edit");
		Button deleteBudgetButton = new Button("Delete");
		HBox budgetButtons = new HBox(5);
		budgetButtons.getChildren().addAll(addNewBudgetButton,
				editBudgetButton,deleteBudgetButton);
		this.getChildren().add(budgetButtons);
		
		// Add new budget buttons
		Label newBudgetLabel = new Label("New budget:  ");
		TextField newBudgetText = new TextField("New Budget Name");
		HBox newBudgetTextEntry = new HBox();
		newBudgetTextEntry.getChildren().addAll(newBudgetLabel,newBudgetText);
		this.getChildren().add(newBudgetTextEntry);
		Label newBudgetAmountLabel = new Label("Amount:      $");
		TextField newBudgetAmountText = new TextField("0.00");
		HBox newBudgetAmountEntry = new HBox();
		newBudgetAmountEntry.getChildren().addAll(newBudgetAmountLabel,newBudgetAmountText);
		this.getChildren().add(newBudgetAmountEntry);
		Button submitBudgetButton = new Button("Submit Budget");
		Button cancelNewBudgetButton = new Button("Cancel");
		HBox submitCancelBudget = new HBox(5);
		submitCancelBudget.getChildren().addAll(submitBudgetButton,cancelNewBudgetButton);
		this.getChildren().add(submitCancelBudget);
		makeInvisible(newBudgetTextEntry,newBudgetAmountEntry,submitCancelBudget,
				newBudgetTextEntry,newBudgetAmountEntry,submitCancelBudget);
		
		// TODO:--- --- Edit budget buttons
		// TODO:--- --- Delete budget buttons
			
		
		
		// -----------------------------------Transaction Section ---------------------------------
		// Basic transaction control buttons
		Label transactionLabel =  new Label("Transaction:");
		this.getChildren().add(transactionLabel);
		Button addNewTransactionButton = new Button("Add New");
		Button editTransactionButton = new Button("Edit");
		Button deleteTransactionButton = new Button("Delete");
		HBox transactionButtons = new HBox(5);
		transactionButtons.getChildren().addAll(addNewTransactionButton,
				editTransactionButton,deleteTransactionButton);
		this.getChildren().add(transactionButtons);
		
		// Add new transaction buttons
		Label newTransactionLabel = new Label("Transaction Budget:  ");
		String[] existingBudgets = {"Overall", "Household", "Wife", "Games"};//TODO: make dynamic with actual budgets
		ComboBox<String> transactionBudget = new ComboBox<>();
		transactionBudget.getItems().addAll(existingBudgets);
		transactionBudget.setPromptText("Overall");
		HBox newTransactionBudgetEntry = new HBox();
		newTransactionBudgetEntry.getChildren().addAll(newTransactionLabel,transactionBudget);
		this.getChildren().add(newTransactionBudgetEntry);
		Label newTransactionTypeLabel = new Label("Transaction Type:  ");
		String[] transactionTypes = {"Income", "Expense"};
		ComboBox<String> transactionType = new ComboBox<>();
		transactionType.getItems().addAll(transactionTypes);
		transactionType.setPromptText("Income");
		HBox newTransactionTypeEntry = new HBox();
		newTransactionTypeEntry.getChildren().addAll(newTransactionTypeLabel,transactionType);
		this.getChildren().add(newTransactionTypeEntry);
		Label newTransactionAmountLabel = new Label("Amount:      $");
		TextField newTransactionAmountText = new TextField("0.00");
		HBox newTransactionAmountEntry = new HBox();
		newTransactionAmountEntry.getChildren().addAll(newTransactionAmountLabel,newTransactionAmountText);
		this.getChildren().add(newTransactionAmountEntry);
		Button submitTransactionButton = new Button("Submit Transaction");
		Button cancelNewTransactionButton = new Button("Cancel");
		HBox submitCancelTransaction = new HBox(5);
		submitCancelTransaction.getChildren().addAll(submitTransactionButton,cancelNewTransactionButton);
		this.getChildren().add(submitCancelTransaction);
		makeInvisible(newTransactionBudgetEntry,newTransactionAmountEntry,submitCancelTransaction,
				newTransactionBudgetEntry,newTransactionAmountEntry,submitCancelTransaction,
				newTransactionTypeEntry,newTransactionTypeEntry);
				
		// TODO:--- --- Edit transaction buttons
		// TODO:--- --- Delete transaction buttons

				
				
				

		// ----------------------------------- Justifications Section -----------------------------
		Label justificationLabel =  new Label("Justifications:");
		this.getChildren().add(justificationLabel);
		ToggleButton justificationButton = new ToggleButton("Audit");
		this.getChildren().add(justificationButton);
		// TODO: add audit functionality

		
		
		
		// ----------------------------------- Listener Section -----------------------------------
		// Budget Listeners
		// --- Add New Budget
		addNewBudgetButton.setOnAction(e -> {
			makeVisible(newBudgetTextEntry,newBudgetAmountEntry,submitCancelBudget,
					newBudgetTextEntry,newBudgetAmountEntry,submitCancelBudget);
			makeInvisible(newTransactionBudgetEntry,newTransactionAmountEntry,submitCancelTransaction,
					newTransactionBudgetEntry,newTransactionAmountEntry,submitCancelTransaction,
					newTransactionTypeEntry,newTransactionTypeEntry);
        });
		submitBudgetButton.setOnAction(e->{
			String newBudgetname = newBudgetText.getText();
			//double newBudgetAmount = Double.parseDouble(newBudgetAmountText.getText());
			makeInvisible(newBudgetTextEntry,newBudgetAmountEntry,submitCancelBudget,
			newBudgetTextEntry,newBudgetAmountEntry,submitCancelBudget);
			// TODO: Create new budget class/methods to call here
			// CreateBudget(newBudgetname, newBudgetAmount);
			System.out.println(newBudgetname + " budget added.");
		});
		cancelNewBudgetButton.setOnAction(e -> {
			makeInvisible(newBudgetTextEntry,newBudgetAmountEntry,submitCancelBudget,
					newBudgetTextEntry,newBudgetAmountEntry,submitCancelBudget);
        });
		
		// Transaction Listeners		
		// --- Add New Transaction
		addNewTransactionButton.setOnAction(e -> {
			makeVisible(newTransactionBudgetEntry,newTransactionAmountEntry,submitCancelTransaction,
					newTransactionBudgetEntry,newTransactionAmountEntry,submitCancelTransaction,
					newTransactionTypeEntry,newTransactionTypeEntry);
			makeInvisible(newBudgetTextEntry,newBudgetAmountEntry,submitCancelBudget,
					newBudgetTextEntry,newBudgetAmountEntry,submitCancelBudget);
        });
		submitTransactionButton.setOnAction(e->{
			String newTransactionName = transactionBudget.getPromptText();
			//double newTransactionAmount = Double.parseDouble(newBudgetAmountText.getText());
			makeInvisible(newTransactionBudgetEntry,newTransactionAmountEntry,submitCancelTransaction,
					newTransactionBudgetEntry,newTransactionAmountEntry,submitCancelTransaction,
					newTransactionTypeEntry,newTransactionTypeEntry);
			// TODO: Create new budget class/methods to call here
			// something.CreateTransaction(newTransactionName, newTransactionAmount);
			System.out.println(newTransactionName + " transaction added.");
		});

		cancelNewTransactionButton.setOnAction(e -> {
			makeInvisible(newTransactionBudgetEntry,newTransactionAmountEntry,submitCancelTransaction,
					newTransactionBudgetEntry,newTransactionAmountEntry,submitCancelTransaction,
					newTransactionTypeEntry,newTransactionTypeEntry);
        });		
				
				
	}
	
	
	// Makes buttons invisible and visible only when I need them
	public void makeInvisible(Node n1,Node n2,Node n3,Node n4,Node n5,Node n6,Node n7,Node n8) {
		n1.setVisible(false); n4.setManaged(false);
		n2.setVisible(false); n5.setManaged(false);
		n3.setVisible(false); n6.setManaged(false);
		n7.setVisible(false); n7.setManaged(false);
	}
	public void makeInvisible(Node n1,Node n2,Node n3,Node n4,Node n5,Node n6) {
		n1.setVisible(false); n4.setManaged(false);
		n2.setVisible(false); n5.setManaged(false);
		n3.setVisible(false); n6.setManaged(false);
	}
	public void makeVisible(Node n1,Node n2,Node n3,Node n4,Node n5,Node n6,Node n7,Node n8) {
		n1.setVisible(true); n4.setManaged(true);
		n2.setVisible(true); n5.setManaged(true);
		n3.setVisible(true); n6.setManaged(true);
		n7.setVisible(true); n7.setManaged(true);
	}
	public void makeVisible(Node n1,Node n2,Node n3,Node n4,Node n5,Node n6) {
		n1.setVisible(true); n4.setManaged(true);
		n2.setVisible(true); n5.setManaged(true);
		n3.setVisible(true); n6.setManaged(true);
	}
}
