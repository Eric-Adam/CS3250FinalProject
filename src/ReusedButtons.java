import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class ReusedButtons extends HBox{
	public ReusedButtons() {
		super(10);
		Button addNewButton = new Button("Add New");
		Button EditButton = new Button("Edit");
		Button deleteButton = new Button("Delete");
		this.getChildren().addAll(addNewButton,EditButton,deleteButton);

	}
}
