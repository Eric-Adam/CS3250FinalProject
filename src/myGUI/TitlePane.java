package myGUI;

import budgetTracker.Budget;

import javafx.geometry.Rectangle2D;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import javafx.stage.Screen;

public class TitlePane extends AnchorPane{
	private String status;
	private String balance;
	private String welcomeMessage;
	
	private Budget budget;
	private RunGUI runGUI;
	
	private Label balanceLabel = new Label();
	private Label statusLabel = new Label();
	private Label welcomeLabel = new Label();
	
    /**
     * Pane containing title, status and budget
     * 
     * @param runGUI Instance of RunGUI connected to primaryStage
     * @param budget Budget with user data
     */
	public TitlePane(RunGUI runGUI, Budget budget) {
		this.budget = budget;
		this.runGUI = runGUI;
		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		
		// Main Title
		welcomeMessage = "------ Welcome " + runGUI.getUsername() + " ------";
		welcomeLabel.setText(welcomeMessage);
		HBox  welcomeHBox = new HBox (welcomeLabel);
		
		double welcomeLeftAnchor = screenBounds.getWidth() / 2 - 100;
		double welcomeTopAnchor = screenBounds.getHeight() * 0.05;
		setLeftAnchor(welcomeHBox, welcomeLeftAnchor);
		setTopAnchor(welcomeHBox, welcomeTopAnchor);
		this.getChildren().add(welcomeHBox);
		
		// Show remaining budget
		balance = String.format("%.2f", budget.getOverallBalance());
		balanceLabel.setText("Remaining Balance:\t$ " + balance); 
		
		double sideAnchor = screenBounds.getWidth() * 0.2;
		setTopAnchor(balanceLabel, welcomeTopAnchor*2);							 
		setRightAnchor(balanceLabel, sideAnchor);
		this.getChildren().add(balanceLabel);

		// Show status of the budget
		status = budget.getBudgetStatus();
		statusLabel.setText("Status:\t" + status);
		setTopAnchor(statusLabel, welcomeTopAnchor*2);
		setLeftAnchor(statusLabel, sideAnchor);
		this.getChildren().add(statusLabel);
		
		// Logout
		Button logoutButton = new Button("Log Out");
		setTopAnchor(logoutButton, welcomeTopAnchor/2);
		setRightAnchor(logoutButton, sideAnchor/2);
		this.getChildren().add(logoutButton);
		
		// Listener
		logoutButton.setOnAction(e->{
			runGUI.switchToUser();
		});

	}	

	/**
	 *Updates status and balance for when data changes and welcome message for when user changes
	 */
	public void update() {	
		budget.refreshData();
		balance = String.format("%.2f", budget.getOverallBalance());
		status = budget.getBudgetStatus();
		
		welcomeMessage = "------ Welcome " + runGUI.getUsername() + " ------";
		balanceLabel.setText("Remaining Balance:\t$ " + balance); 
		statusLabel.setText("Status:\t" + status);
		welcomeLabel.setText(welcomeMessage);
	}
	
}
