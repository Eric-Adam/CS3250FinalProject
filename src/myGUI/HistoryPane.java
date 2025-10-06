package myGUI;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import budgetTracker.Transaction;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class HistoryPane extends VBox{
	public HistoryPane() {
		List<String[]> historyData = loadCSV();
		//TableView<Transaction> table = new TableView(); // TODO: fix table stuff 
		for (String[] s : historyData) {
			System.out.println(s[0]);
		}
		
		
//		for (String[] s : historyData) {
//			double amount = Double.parseDouble(s[0]);
//			String category = s[1];
//			String note = s[2];
//			boolean income = Boolean.parseBoolean(s[3]);
//			LocalDate date = LocalDate.parse(s[4]);
//			Transaction newTrans = new Transaction(amount,category,note,income,date);
//			Label label = new Label(s[3]);
//			this.getChildren().add(label);
//		}
		// TODO: Call the line-by-line method
	}
	
	// TODO: Line-by-line method
	
	
	// Loads data from CSV file as unparsed strings 
	private List<String[]> loadCSV() {
		String filePath = "src/resources/transactionDB.csv"; // replace with your CSV file path
        List<String[]> rows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                rows.add(values); 
            }

        } catch (IOException e) {
            e.printStackTrace(); // Or handle error as needed
        }

        return rows;
		
	}
	
}
