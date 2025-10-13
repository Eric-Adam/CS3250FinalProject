package myGUI;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class ChartPane extends StackPane{
	// TODO: replace with charts
	public ChartPane() {
	for (int i=0; i<5; i++) {
		Label label = new Label(Integer.toString(i));
		label.setPrefWidth(300 -i*30);
		label.setPrefHeight(300 - i*30);
		label.setStyle("-fx-border-color:black;");
		this.getChildren().add(label);
		}
	}		
}
