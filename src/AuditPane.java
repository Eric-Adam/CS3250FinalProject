import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class AuditPane extends VBox{
	AuditPane(){
		super(10);
		
		// TODO: replace for loop with auditing functionality for Justifications; need database
		// --- invisible unless auditing
		Label justificationLabel = new Label("Justifications");
		this.getChildren().add(justificationLabel);
		
		for (int i=10; i>0; i--) {
			Label label = new Label(i+"\tDate:\t Sep/6/25\t Category:\t TBD\n" 
									+ "Short justification phrase\n"
									+ "acknowledge button\t add category button\n\n");
			this.getChildren().add(label);
		}
	}
}
