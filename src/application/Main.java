package src.application;
import application.errormes;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
public class Main extends Application {
	static String player1name;
	static String player2name;
	public void start(Stage stage) {
			stage.setTitle("Marvel game");
			Image icon=new Image("/Assets/Marvel_Logo.png");
			stage.getIcons().add(icon);
			
			BorderPane border = new BorderPane();
			Scene scene = new Scene(border);
			border.setStyle("-fx-background-color: #87CEEB;");
			stage.setScene(scene);
			stage.setWidth(1000);
			stage.setHeight(800);
		    VBox vbox = new VBox();
		    vbox.setPadding(new Insets(10));
		    vbox.setSpacing(8);
		    
			Text player1text=new Text("Please enter Player 1 name.");
			player1text.setFont(Font.font("verdana",28));
			
			TextField player1field=new TextField();
			player1field.setFont(Font.font("verdana",28));
			
			Text player2text=new Text("Please enter Player 2 name.");
			player2text.setFont(Font.font("verdana",28));
			
			TextField player2field=new TextField();
			player2field.setFont(Font.font("verdana",28));

			vbox.getChildren().add(player1text);
			vbox.getChildren().add(player1field);
			vbox.getChildren().add(player2text);
			vbox.getChildren().add(player2field);
			border.setLeft(vbox);
			
			Button toscene2=new Button("Continue");
			EventHandler<ActionEvent> proceed = new EventHandler<ActionEvent>() {
	            public void handle(ActionEvent e){
	                if(!player1field.getText().equals("")&&!player2field.getText().equals("")) {
	                	System.out.println("good");
	                }else {
	                	new errormes("Error","Names can't be empty");
	                }
	            }
	        };
			toscene2.setOnAction(proceed);
			toscene2.setPadding(new Insets(10));
			border.setBottom(toscene2);
			border.setMargin(border.getBottom(),new Insets(50));
			border.setAlignment(toscene2, Pos.BOTTOM_RIGHT);
			stage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
