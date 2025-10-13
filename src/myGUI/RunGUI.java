package myGUI;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

public class RunGUI extends BorderPane{
	public RunGUI(){
		// Padding
		Insets titleInsets = new Insets(25,10,25,10);
		Insets inputInsets = new Insets(25,10,25,10);
		Insets chartInsets = new Insets(25,10,25,10);
		Insets historyInsets = new Insets(25,10,10,10);
		
		// Top: Title, Status, Selected Budget
		TitlePane titlePane = new TitlePane();
		titlePane.setPadding(titleInsets);
		this.setTop(titlePane);
		
		// Left: Filled with UI Stuff (i.e. buttons, input fields)
		InputPane inputPane = new InputPane();
		inputPane.setPadding(inputInsets);
		this.setLeft(inputPane);
		
        // Center: Budget Charts
		ChartPane chartPane = new ChartPane();
		chartPane.setMinSize(300, 300);
		chartPane.setPadding(chartInsets);
		chartPane.setMaxHeight(500);
		this.setCenter(chartPane);     
		
        
        // Bottom: Line-by-line history
		HistoryPane historyPane = new HistoryPane();
		historyPane.setPadding(historyInsets);		
		this.setBottom(historyPane);

	}
}
