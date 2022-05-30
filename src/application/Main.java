package application;
import java.io.IOException;

import application.errormes;
import engine.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
public class Main extends Application {
	static Player player1;
	static Player player2;
	static Game game;
	public void start(Stage stage) {
			stage.setTitle("Marvel game");
//			Image icon=new Image("/Assets/Marvel_Logo.png");
//			stage.getIcons().add(icon);
			
			BorderPane border = new BorderPane();
			Scene scene = new Scene(border);
			border.setStyle("-fx-background-color: #87CEEB;");
			stage.setScene(scene);
			stage.setWidth(1000);
			stage.setHeight(700);
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
	                	if(player1field.getText().equals(player2field.getText())){
	                		new errormes("Error","Names can't be identical");
	                	}else {
		                	player1=new Player(player1field.getText());
				          	player2=new Player(player2field.getText());
				          	game=new Game(player1,player2);
				          	try {
				    			Game.loadAbilities("C:\\Users\\Abdelrahman Elmeky\\Documents\\My-Github\\Marvelgame\\Marvel-Ultimate-War-Game-\\Abilities.CSV");
				    			Game.loadChampions("C:\\Users\\Abdelrahman Elmeky\\Documents\\My-Github\\Marvelgame\\Marvel-Ultimate-War-Game-\\Champions.CSV");
				    		} catch (IOException e2) {
				    			System.out.println(e2);
				    			new errormes("Error","Error in Abilities and Champion Files");
				    		}
		                	toscene2(stage);
	                	}
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
	
	public static void toscene2(Stage stage) {
		BorderPane border = new BorderPane();
		border.setPrefHeight(1000);
		border.setPrefWidth(700);
		Scene scene = new Scene(border);
		border.setStyle("-fx-background-color: #87CEEB;");
		stage.setScene(scene);
		stage.setWidth(1001);
		stage.setHeight(701);
	    VBox tostring = new VBox();
	    tostring.setSpacing(14);
	    tostring.setMaxWidth(200);
	    tostring.setMinWidth(200);
	    tostring.setStyle("-fx-background-color: #123456;");
	    
		GridPane grid =new GridPane();
		
		Button b0=new Button("B0");
		b0.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<Event>() {
			public void handle(Event arg0){
				viewChampionStats(tostring,0);
			}
		} );
		Button b1=new Button("B1");
		b1.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<Event>() {
			public void handle(Event arg0){
				viewChampionStats(tostring,1);
			}
		} );
		Button b2=new Button("B2");
		b2.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<Event>() {
			public void handle(Event arg0){
				viewChampionStats(tostring,2);
			}
		} );
		Button b3=new Button("B3");
		b3.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<Event>() {
			public void handle(Event arg0){
				viewChampionStats(tostring,3);
			}
		} );
		Button b4=new Button("B4");
		b4.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<Event>() {
			public void handle(Event arg0){
				viewChampionStats(tostring,4);
			}
		} );
		Button b5=new Button("B5");
		b5.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<Event>() {
			public void handle(Event arg0){
				viewChampionStats(tostring,5);
			}
		} );
		Button b6=new Button("B6");
		b6.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<Event>() {
			public void handle(Event arg0){
				viewChampionStats(tostring,6);
			}
		} );
		Button b7=new Button("B7");
		b7.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<Event>() {
			public void handle(Event arg0){
				viewChampionStats(tostring,7);
			}
		} );
		Button b8=new Button("B8");
		b8.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<Event>() {
			public void handle(Event arg0){
				viewChampionStats(tostring,8);
			}
		} );
		Button b9=new Button("B9");
		b9.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<Event>() {
			public void handle(Event arg0){
				viewChampionStats(tostring,9);
			}
		} );
		Button b10=new Button("B10");
		b10.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<Event>() {
			public void handle(Event arg0){
				viewChampionStats(tostring,10);
			}
		} );
		Button b11=new Button("B11");
		b11.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<Event>() {
			public void handle(Event arg0){
				viewChampionStats(tostring,11);
			}
		} );
		Button b12=new Button("B12");
		b12.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<Event>() {
			public void handle(Event arg0){
				viewChampionStats(tostring,12);
			}
		} );
		Button b13=new Button("B13");
		b13.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<Event>() {
			public void handle(Event arg0){
				viewChampionStats(tostring,13);
			}
		} );
		Button b14=new Button("B14");
		b14.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<Event>() {
			public void handle(Event arg0){
				viewChampionStats(tostring,14);
			}
		} );
		
		grid.add(b0,0,0,1,1);
		grid.add(b1,1,0,1,1);
		grid.add(b2,2,0,1,1);
		grid.add(b3,3,0,1,1);
		grid.add(b4,4,0,1,1);
		grid.add(b5,0,1,1,1);
		grid.add(b6,1,1,1,1);
		grid.add(b7,2,1,1,1);
		grid.add(b8,3,1,1,1);
		grid.add(b9,4,1,1,1);
		grid.add(b10,0,2,1,1);
		grid.add(b11,1,2,1,1);
		grid.add(b12,2,2,1,1);
		grid.add(b13,3,2,1,1);
		grid.add(b14,4,2,1,1);

		//grid.setPadding(new Insets(10));
		grid.setHgap(20);
		grid.setVgap(20);
		//grid.setMaxWidth(400);
		//grid.setMaxHeight(150);
		grid.setAlignment(Pos.CENTER);
		
		
		VBox selected = new VBox();
	    selected.setSpacing(8);
	    selected.setPrefWidth(200);
	    selected.setStyle("-fx-background-color: #aaaaaa;");
	    
		Label t=new Label("Select Champions For "+player1.getName()+":");
		t.setFont(Font.font("verdana",30));
		//t.setAlignment(Pos.CENTER);
		
	    border.setRight(tostring);
		border.setLeft(selected);
	    border.setTop(t);
	    border.setCenter(grid);
	    border.getCenter().setStyle("-fx-background-color: #654321;");
	    //border.setMargin(border.getCenter(), new Insets(50));
	    
	}

	public static void viewChampionStats(VBox tostring,int i) {
		Text t=new Text(game.getAvailableChampions().get(i).toString());
		t.setFont(Font.font("verdana",15));
		if(!tostring.getChildren().isEmpty()) {
			tostring.getChildren().remove(0);
		}
		tostring.getChildren().add(t);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
