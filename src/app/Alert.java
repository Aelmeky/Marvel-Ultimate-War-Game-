package app;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
public class Alert extends Stage{
	public Alert(String title,String message) {
		super();
		super.setTitle(title);
		BorderPane border = new BorderPane();
		Scene scene = new Scene(border);
		//Image icon=new Image("/Assets/Warninglogo.png");
		//Image cancel=new Image("/Assets/Wrong.png");
		//ImageView cancel1 = new ImageView();
		//VBox imagBox = new VBox();
		//imagBox.getChildren().add(cancel1);
		//super.getIcons().add(icon);
		border.setStyle("-fx-background-color: #808080;");
		Button exit=new Button("Exit");
		exit.setOnAction( e -> super.close() );
		super.setScene(scene);
		super.setWidth(400);
		super.setHeight(200);
		Text t=new Text(message);
		t.setFont(Font.font("verdana",15));
		border.setCenter(t);
		border.setAlignment(t, Pos.BOTTOM_CENTER);
		//border.setRight(imagBox);
		border.setBottom(exit);
		border.setMargin(border.getBottom(),new Insets(50));
		border.setAlignment(exit, Pos.BOTTOM_CENTER);
		
		super.show();
	}
}
