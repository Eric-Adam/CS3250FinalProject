package myGUI;
import javafx.scene.layout.*;

public class RunGUI extends BorderPane{
	public RunGUI(){
		// Top: Title, Status, Selected Budget
		TitlePane titlePane = new TitlePane();
		this.setTop(titlePane);
		
		// Left: Filled with UI Stuff (i.e. buttons, input fields)
		InputPane inputPane = new InputPane();
		this.setLeft(inputPane);
		
		// Right: Justification Auditing
		AuditPane auditPane = new AuditPane();
		this.setRight(auditPane);
		
        // Center: Budget Charts
		ChartPane chartPane = new ChartPane();
		this.setCenter(chartPane);        
        
        // Bottom: Line-by-line history
		HistoryPane historyPane = new HistoryPane();
		this.setBottom(historyPane);
	}
}
