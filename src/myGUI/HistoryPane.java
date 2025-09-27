package myGUI;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class HistoryPane extends VBox{
	public HistoryPane() {
		super(10);
		Label historyLabel = new Label("Transaction History");
		historyLabel.setAlignment(Pos.CENTER);
		this.getChildren().add(historyLabel);
		
		// TODO: replace with transaction history; need database stuff
		double temp = 0.0;
		for (int i=0; i<10; i++) {
			temp = (Math.round(Math.random()*100)*100)/100;
			HistoryLine line = new HistoryLine(i, "Hookers", temp, true);
			this.getChildren().add(line);
		}
	}
}
