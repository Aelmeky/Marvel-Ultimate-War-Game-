package application;
import java.io.IOException;


import engine.*;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import model.world.Champion;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
public class Main extends Application {
	static Player player1;
	static Player player2;
	static Game game;
	public void start(Stage stage) {
			stage.setTitle("Marvel game");
			Image icon=new Image("/Assets/Marvel_Logo.png");
			stage.getIcons().add(icon);
			
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
				    			Game.loadAbilities("/home/ahme/Programming/Marvel-Ultimate-War-Game-/src/Abilities.csv");
				    			Game.loadChampions("/home/ahme/Programming/Marvel-Ultimate-War-Game-/src/Champions.csv");
				    		} catch (IOException e2) {
				    			System.out.println(e2);
				    			new errormes("Error","Error in Abilities and Champion Files");
				    		}
		                	toscene2(game.getFirstPlayer(),stage);
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
	
	public static void toscene2(Player p , Stage stage) {
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
	    
		VBox selected = new VBox();
	    selected.setSpacing(8);
	    selected.setPrefWidth(200);
	    selected.setStyle("-fx-background-color: #aaaaaa;");
	    VBox leftpane = new VBox();
	    leftpane.setSpacing(8);
	    leftpane.setPrefWidth(200);
	    leftpane.setStyle("-fx-background-color: #aaaaaa;");
	    leftpane.getChildren().add(selected);
	    Button removeChampion=new Button("Remove Last Champion");
	    leftpane.getChildren().add(removeChampion);
	    Button toscene3=new Button("Continue");
		border.setBottom(toscene3);
		toscene3.setVisible(false);
		ToggleGroup tog=new ToggleGroup();
        tog.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ob,Toggle o, Toggle n){
                RadioButton rb = (RadioButton)tog.getSelectedToggle();
                if (rb != null) {
                	for(Champion c:p.getTeam()) {
                		if(c.getName().equals(rb.getText())) {
                            p.setLeader(c);
                            break;
                		}
                	}
                }
            }
        });
	    removeChampion.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<Event>() {
			public void handle(Event arg0){
				if(selected.getChildren().size()==1) {
					new errormes("Error", "Your Team is Empty");
					return;
				}
				selected.getChildren().remove(selected.getChildren().size()-1);
				p.getTeam().remove(game.getFirstPlayer().getTeam().size()-1);
				toscene3.setVisible(false);
			}
		} );
	    
	    Text t2=new Text("Your Team:");
		t2.setFont(Font.font("verdana",15));
		selected.getChildren().add(t2);
	    
		GridPane grid =new GridPane();
		Button[] arr=new Button[15];
		arr[0]=new Button("B0");
		arr[1]=new Button("B1");
		arr[2]=new Button("B2");
		arr[3]=new Button("B3");
		arr[4]=new Button("B4");
		arr[5]=new Button("B5");
		arr[6]=new Button("B6");
		arr[7]=new Button("B7");
		arr[8]=new Button("B8");
		arr[9]=new Button("B9");
		arr[10]=new Button("B10");
		arr[11]=new Button("B11");
		arr[12]=new Button("B12");
		arr[13]=new Button("B13");
		arr[14]=new Button("B14");
		for(int i=0;i<15;i++){
			final int j=i;
			arr[i].addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<Event>() {
				public void handle(Event arg0){
					viewChampionStats(tostring,j);
				}
			} );
			arr[i].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<Event>() {
				public void handle(Event arg0){
					addChampion(tog,p,toscene3,selected,j);
				}
			} );
			grid.add(arr[i],i%5,i/5,1,1);
		}

		grid.setHgap(20);
		grid.setVgap(20);
		grid.setAlignment(Pos.CENTER);
			    
		Label t=new Label("Select Champions For "+p.getName()+":");
		t.setFont(Font.font("verdana",30));
		
	    border.setRight(tostring);
		border.setLeft(leftpane);
	    border.setTop(t);
	    border.setCenter(grid);
	    border.getCenter().setStyle("-fx-background-color: #654321;");
		//toscene3.setAlignment(Pos.TOP_RIGHT);
		toscene3.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<Event>() {
			public void handle(Event arg0){
		        if(p.getLeader()==null) {
		        	new errormes("Error", "Please Choose A Leader");
		        	return;
		        }
				toScene3(stage);
			}
		} );
	}
	
	public static void toScene3(Stage stage) {
		if(game.getSecondPlayer().getTeam().size()!=0) {
			scene4(stage);
		}else {
			toscene2(game.getSecondPlayer(), stage);
		}
	}
	public static void scene4(Stage stage) {
		game.prepareChampionTurns();
		BorderPane border = new BorderPane();
		border.setPrefHeight(1000);
		border.setPrefWidth(700);
		Scene scene = new Scene(border);
		border.setStyle("-fx-background-color: #87CEEB;");
		stage.setScene(scene);
		stage.setWidth(1001);
		stage.setHeight(701);
		
		Label current=new Label("Current Champion "+game.getCurrentChampion().getName()+":");
		current.setFont(Font.font("verdana",30));
				
		VBox rightpane=new VBox();
		rightpane.setSpacing(14);
		rightpane.setMaxWidth(200);
		rightpane.setMinWidth(200);
		rightpane.setStyle("-fx-background-color: #123456;");
		
		VBox leftpane=new VBox();
		leftpane.setSpacing(14);
		leftpane.setMaxWidth(250);
		leftpane.setMinWidth(250);
		leftpane.setStyle("-fx-background-color: #CAFFEE;");
		Label ava=new Label("Available Actions:");
		ava.setFont(Font.font("verdana",30));
		leftpane.getChildren().add(ava);
		
		GridPane grid =new GridPane();
		
		
		border.setRight(rightpane);
		border.setLeft(leftpane);
	    border.setTop(current);
	    border.setCenter(grid);
	}

	public static void addChampion(ToggleGroup t,Player p,Button b,VBox selected, int i) {
		if(p.getTeam().size()==3) {
			new errormes("Error", "Your Team can only Have 3 Members");
			return;
		}
		if(p.getTeam().size()==3) {}
//		Text t=new Text(game.getAvailableChampions().get(i).getName());
//		t.setFont(Font.font("verdana",15));
		RadioButton rb=new RadioButton(game.getAvailableChampions().get(i).getName());
		rb.setToggleGroup(t);
		selected.getChildren().add(rb);
		p.getTeam().add(game.getAvailableChampions().get(i));
		if(p.getTeam().size()==3) {
			b.setVisible(true);
		}

		
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
