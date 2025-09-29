package budgetTracker;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Budget {
	private double budgetMax;
	private ArrayList<Income> income;
	private ArrayList<Expense> expenses;	
	
	public Budget(double budgetMax) {
		this.budgetMax = budgetMax;
	}
	
	
	public double getBudgetMax() {
		return budgetMax;
	}
	public void setBudgetMax(double budgetMax) {
		this.budgetMax = budgetMax;
	}
	
	// Adds tracked transactions
	public void addIncome(Income income) {
        this.income.add(income);
        JOptionPane.showMessageDialog(null, 
        		"$"+income.getTransactionAmount()+" added to income");
    }
    public void addExpense(Expense expense) {
        this.expenses.add(expense);
        JOptionPane.showMessageDialog(null, 
        		"Expense of $"+expense.getTransactionAmount()+" added.");
    }
    
    // Removes tracked transactions
	public void removeIncome(Income income) {
        this.income.remove(income);
        JOptionPane.showMessageDialog(null, 
        		"$"+income.getTransactionAmount()+" removed from income");
    }
    public void removeExpense(Expense expense) {
        this.expenses.remove(expense);
        JOptionPane.showMessageDialog(null, 
        		"Expense of $"+expense.getTransactionAmount()+" removed.");
    }

    public double getOverallBalance(ArrayList<Income> incomeArray, ArrayList<Expense> expenseArray) {
    	double runningIncome = 0;
    	double runningExpense = 0;
    	
    	for (Income income: incomeArray) {
    		runningIncome += income.getTransactionAmount();
    	}
    	for (Expense expense: expenseArray) {
    		runningExpense += expense.getTransactionAmount();
    	}
        return runningIncome - runningExpense;
    }
    
    public double getCategoryBalance(ArrayList<Income> incomeArray, ArrayList<Expense> expenseArray, String category) {
    	double runningIncome = 0;
    	double runningExpense = 0;
    	
    	for (Income income: incomeArray) {
    		if (category.equals(income.getCategory())) 
    			runningIncome += income.getTransactionAmount();
    	}
    	for (Expense expense: expenseArray) {
    		if (category.equals(expense.getCategory())) 
    			runningExpense += expense.getTransactionAmount();
    	}
        return runningIncome - runningExpense;
    }
    
    public String getBudgetStatus() {
    	String status="";
    	double currentRemaining = getOverallBalance(income, expenses);
    	
    	if (currentRemaining > (0.6 * budgetMax))
    		status = "UNDER";
    	else if (currentRemaining > (0.4 * budgetMax))
    		status = "TIGHT";
    	else if (currentRemaining > (0.2 * budgetMax))
    		status = "AT RISK";
    	else
    		status = "OVER";
    	
    	return status;
    }

}
