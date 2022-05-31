package application;
import java.io.IOException;
import java.util.ArrayList;

import engine.*;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import model.world.AntiHero;
import model.world.Champion;
import model.world.Cover;
import model.world.Damageable;
import model.world.Villain;
import model.world.Hero;
import javafx.scene.Node;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
public class Main extends Application {
	static Player player1;
	static Player player2;
	static Game game;
	static ArrayList<Integer> chosenChamions;
	public void start(Stage stage) {
			chosenChamions=new ArrayList<Integer>();
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
				    			Game.loadAbilities("./Abilities.csv");
				    			Game.loadChampions(".\\Champions.csv");
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
		if(!chosenChamions.isEmpty()) {
			for(int i=0;i<chosenChamions.size();i++) {
				final int j=chosenChamions.get(i);
				arr[j].setVisible(false);
			}
		}
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
					arr[j].setVisible(false);
				}
			} );
			grid.add(arr[i],i%5,i/5,1,1);
		}
		
	    removeChampion.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<Event>() {
			public void handle(Event arg0){
				if(selected.getChildren().size()==1) {
					new errormes("Error", "Your Team is Empty");
					return;
				}
				Node n=selected.getChildren().remove(selected.getChildren().size()-1);
				int x=chosenChamions.remove(chosenChamions.size()-1);
				final int j=x;
				arr[j].setVisible(true);
				p.getTeam().remove(p.getTeam().size()-1);
				toscene3.setVisible(false);
			}
		} );

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
		//TODO abilities to string somewhere
		
		game.prepareChampionTurns();
		BorderPane border = new BorderPane();
		border.setPrefHeight(1000);
		border.setPrefWidth(700);
		Scene scene = new Scene(border);
		border.setStyle("-fx-background-color: #87CEEB;");
		stage.setScene(scene);
		stage.setWidth(1001);
		stage.setHeight(701);
		
		Label current=new Label("Current Champion: "+game.getCurrentChampion().getName());
		current.setFont(Font.font("verdana",30));
		Champion c=(Champion) game.getTurnOrder().remove();
		Label next=new Label("next Champion: "+((Champion)game.getTurnOrder().peekMin()).getName());
		game.getTurnOrder().insert(c);
		next.setFont(Font.font("verdana",30));
		
		HBox toprow1=new HBox();
		toprow1.setSpacing(14);
		toprow1.getChildren().add(current);
		toprow1.getChildren().add(next);
		
		HBox toprow2=new HBox();

		Label player1stat =new Label(game.getFirstPlayer().getName()+" used Leader Ability="+game.isFirstLeaderAbilityUsed());
		player1stat.setFont(Font.font("verdana",30));
		Label player2stat =new Label(game.getSecondPlayer().getName()+" used Leader Ability="+game.isSecondLeaderAbilityUsed());
		player2stat.setFont(Font.font("verdana",30));
		toprow2.setSpacing(14);
		toprow2.getChildren().add(player1stat);
		toprow2.getChildren().add(player2stat);
		
		VBox top=new VBox();
		top.setSpacing(14);
		top.getChildren().add(toprow1);
		top.getChildren().add(toprow2);

				
		VBox rightpane=new VBox();
		rightpane.setSpacing(14);
		rightpane.setMaxWidth(200);
		rightpane.setMinWidth(200);
		rightpane.setStyle("-fx-background-color: #123456;");
		Label stat=new Label("Stats:");
		stat.setTextFill(Color.BLACK);
		stat.setFont(Font.font("verdana",19));
		rightpane.getChildren().add(stat);
		
		VBox leftpane=new VBox();
		leftpane.setSpacing(14);
		leftpane.setMaxWidth(250);
		leftpane.setMinWidth(250);
		leftpane.setStyle("-fx-background-color: #CAFFEE;");
		Label ava=new Label("Available Actions:");
		ava.setFont(Font.font("verdana",19));
		leftpane.getChildren().add(ava);
		
		GridPane grid =new GridPane();
		grid.setGridLinesVisible(true);
		grid.setAlignment(Pos.CENTER);
		VBox[][] grr=new VBox[5][5];
		for(int i=0;i<5;i++) {
			for(int j=0;j<5;j++) {
				grr[j][i]=new VBox();
				grr[j][i].setMinHeight(100);
				grr[j][i].setMaxHeight(100);
				grr[j][i].setMinWidth(100);
				grr[j][i].setMaxWidth(100);
				grid.add(grr[j][i], j, i);
				final int j2=j;
				final int i2=i;
				grr[j][i].addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<Event>() {
					public void handle(Event arg0){
						if(grr[j2][i2].getChildren().size()!=0) {
							dispalystats(((Label)grr[j2][i2].getChildren().get(1)),rightpane);
						}
					}
				});
			}
		}
		game.placeChampions();
		updateGrid(grid);
		grid.setVgap(5); 
	    grid.setHgap(5);
		
		border.setRight(rightpane);
		border.setLeft(leftpane);
	    border.setTop(top);
	    border.setCenter(grid);
	    prepareActions(leftpane,game.getCurrentChampion());
	}
	
	public static void prepareActions(VBox leftpane, Champion c) {
		Button[] arr=new Button[6];
		if(c.getCurrentActionPoints()>=1) {
			arr[0]=new Button("Move");
			arr[0].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<Event>() {
				public void handle(Event arg0){
			        //int[] ar2=getCoords();
			        //System.out.println(ar2[0]+" "+ar2[1]);
					
				}
			} );
		}
		if(c.getCurrentActionPoints()>=2) {
			arr[1]=new Button("Attack");
			arr[1].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<Event>() {
				public void handle(Event arg0){
					
				}
			} );
		}
		
		for(int i=0;i<6;i++) {
			if(arr[i]!=null) {
				leftpane.getChildren().add(arr[i]);
			}
		}
		
	}

	public static void dispalystats(Label node, VBox rightpane) {
		Label l=new Label(node.getText());
		l.setFont(Font.font("verdana",14));
		l.setTextFill(Color.BLACK);
		while(rightpane.getChildren().size()!=1) {
			rightpane.getChildren().remove(1);
		}
		rightpane.getChildren().add(l);
	}

	public static void updateGrid(GridPane grid) {
		for(int i=0;i<5;i++) {
			for(int j=0;j<5;j++) {
				Object d=game.getBoard()[i][j];
				if(d instanceof Damageable) {
					int x=((Damageable)d).getLocation().x;
					int y=((Damageable)d).getLocation().y;
					if(d instanceof Champion) {
//						System.out.print("Champ "+" "+x+" "+y);
						Label l=new Label(((Champion) d).getName());
						((VBox)getNodeFromGrid(grid,4-x,y)).getChildren().add(l);
//						System.out.print(((VBox)getNodeFromGrid(grid,4-x,y)));
						Champion c=(Champion)d;
						Label l2=new Label(enhancedToString(c));
						l2.setVisible(false);
						((VBox)getNodeFromGrid(grid,4-x,y)).getChildren().add(l2);
						
					}else {
						Label l=new Label("Cover");
						((VBox)getNodeFromGrid(grid,4-x,y)).getChildren().add(l);
						Label l2=new Label(((Cover) d).toString());
						l2.setVisible(false);
						((VBox)getNodeFromGrid(grid,4-x,y)).getChildren().add(l2);
						
					}
//					System.out.println(x+" "+y);
				}
			}
		}
	}
	
	public static String enhancedToString(Champion c) {
		String s2="";
		if(c instanceof Champion) {
			s2="Champion";
		}
		if(c instanceof Villain) {
			s2="Villain";
		}
		if(c instanceof AntiHero) {
			s2="AntiHero";
		}
		String s3="";
		for(int j=0;j<c.getAbilities().size();j++) {
			s3+=c.getAbilities().get(j).getName()+"\n";
		}
		String eff="";
		for(int j=0;j<c.getAppliedEffects().size();j++) {
			s3+=c.getAppliedEffects().get(j).getName()+" Duration="+c.getAppliedEffects().get(j).getDuration()+"\n";
		}
		if(c.equals(game.getCurrentChampion())) {
			String s="name=" + c.getName() +"\n"+"Type="+s2+"\n"+ "currentHP=" + c.getCurrentHP() +"\n"+ "mana=" + c.getMana()+"\n"
					+"currentActionPoints=" + c.getCurrentActionPoints()+"\n"+ "attackRange=" + c.getAttackRange() +"\n"+ "attackDamage=" + c.getAttackDamage()+"\n"+
					"Effects="+"\n"+eff+"Abilities="+"\n"+s3;
			return s;
		}else {
			boolean flag=c.equals(game.getFirstPlayer().getLeader())||c.equals(game.getSecondPlayer().getLeader());
			String s="name=" + c.getName() +"\n"+"Type="+s2+"\n"+ "currentHP=" + c.getCurrentHP() +"\n"+ "mana=" + c.getMana()+"\n"
					+"Speed="+c.getSpeed()+"\n"+"maxActionPoints=" + c.getMaxActionPointsPerTurn()+"\n"+ "attackRange=" 
					+ c.getAttackRange() +"\n"+ "attackDamage=" + c.getAttackDamage()+"\n"+"isLeader="+flag+"\n"+
					"Effects="+"\n"+eff;
			return s;
		}
	}
	
	public static Node getNodeFromGrid (GridPane gridPane,final int row, final int column) {
	    Node result = null;
	    ObservableList<Node> childrens = gridPane.getChildren();
	    for (Node node : childrens) {
	        if(node!=null) {
	        	if(gridPane.getRowIndex(node) !=null&&gridPane.getRowIndex(node)== row && gridPane.getColumnIndex(node) !=null &&gridPane.getColumnIndex(node) == column) {
	        		result = node;
	  	            break;
	  	        }
	        }
	    }
	    return result;
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
		chosenChamions.add(i);
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
