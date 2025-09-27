package myGUI;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;

public class HistoryLine extends TilePane{
	public HistoryLine(int id, String transaction, double amount, boolean need) {
		// TODO: replace with actual data; need database
		Label idLabel = new Label(Integer.toString(id));
		Label typeLabel = new Label("Type:\t");
		Label transactionLabel = new Label(transaction);
		Label amountLabel = new Label("Amount:\t$");
		Label amountValLabel = new Label(Double.toString(amount));
		Label needLabel = new Label("Need:\t");
		Label needValLabel = new Label(Boolean.toString(need));
		Label dataLabel = new Label("Date:\t");
		Label dateValLabel =new Label("Sep/6/2025");

		this.getChildren().addAll(idLabel, typeLabel, transactionLabel, amountLabel,
					amountValLabel, needLabel, needValLabel, dataLabel, dateValLabel);
	}
}
