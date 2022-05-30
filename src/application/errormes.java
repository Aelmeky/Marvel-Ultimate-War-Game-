package application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
public class errormes extends Stage{
	public errormes(String title,String message) {
		super();
		super.setTitle(title);
		BorderPane border = new BorderPane();
		Scene scene = new Scene(border);
		border.setStyle("-fx-background-color: #FF0000;");
		super.setScene(scene);
		super.setWidth(400);
		super.setHeight(200);
		Text t=new Text(message);
		t.setFont(Font.font("verdana",15));
		border.setCenter(t);
		super.show();
	}

}
