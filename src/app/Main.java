package app;

import java.io.IOException;
import java.util.ArrayList;
import engine.*;
import exceptions.AbilityUseException;
import exceptions.ChampionDisarmedException;
import exceptions.InvalidTargetException;
import exceptions.NotEnoughResourcesException;
import exceptions.UnallowedMovementException;
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
import model.abilities.Ability;
import model.abilities.AreaOfEffect;
import model.world.AntiHero;
import model.world.Champion;
import model.world.Cover;
import model.world.Damageable;
import model.world.Villain;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Main extends Application {
	static Player player1;
	static Player player2;
	static Game game;
	static ArrayList<Integer> chosenChamions;
	static Label player1stat;
	static Label player2stat;
	static Label current;
	static Label next;
	static String fastestChampion;

	public void start(Stage stage) {
		chosenChamions = new ArrayList<Integer>();
		stage.setTitle("Marvel game");
		Image icon = new Image("/Assets/Marvel_Logo.png");
		stage.getIcons().add(icon);

		BorderPane border = new BorderPane();
		Scene scene = new Scene(border);
		Image back1 = new Image("/Assets/Marvel_background1.jpg"); 
		ImageView bk1 = new ImageView(back1);
		bk1.fitHeightProperty().bind(stage.heightProperty());
		bk1.fitWidthProperty().bind(stage.widthProperty());
		
		border.getChildren().add(bk1);
		stage.setScene(scene);
		stage.setWidth(1000);
		stage.setHeight(700);
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(10));
		vbox.setSpacing(8);

		Text player1text = new Text("Please enter Player 1 name.");
		player1text.setFont(Font.font("verdana", 28));
		player1text.setFill(Color.WHITE);
		TextField player1field = new TextField();
		player1field.setFont(Font.font("verdana", 28));
		player1field.setMaxWidth(250);
		player1field.setStyle("-fx-background-image: url(\"/Assets//textBoxBackground.png\");" + "-fx-text-fill: white;");
		player1field.setBackground(Background.fill(Color.BLACK));
		Text player2text = new Text("Please enter Player 2 name.");
		player2text.setFont(Font.font("verdana", 28));
		player2text.setFill(Color.WHITE);
		TextField player2field = new TextField();
		player2field.setFont(Font.font("verdana", 28));
		player2field.setMaxWidth(250);
		player2field.setStyle("-fx-background-image: url(\"/Assets//redfield.jpg\");" + "-fx-text-fill: white;");
		player2field.setBackground(Background.fill(Color.BLACK));
		
		vbox.getChildren().add(player1text);
		vbox.getChildren().add(player1field);
		vbox.getChildren().add(player2text);
		vbox.getChildren().add(player2field);
		border.setLeft(vbox);

		Button toscene2 = new Button();
		toscene2.setShape(new Circle(5));
		Image bu = new Image("/Assets/button.jpg"); 
		ImageView bu1 = new ImageView(bu);
		bu1.setFitHeight(50);
		bu1.setFitWidth(50);
		toscene2.setGraphic(bu1);
//		toscene2.setMaxHeight(70);
//		toscene2.setMaxWidth(70);
		toscene2.setBackground(Background.fill(Color.BLACK));
		EventHandler<ActionEvent> proceed = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				if (!player1field.getText().equals("") && !player2field.getText().equals("")) {
					if (player1field.getText().equals(player2field.getText())) {
						new errormes("Error", "Names can't be identical");
					} else {
						player1 = new Player(player1field.getText());
						player2 = new Player(player2field.getText());
						game = new Game(player1, player2);
						try {
							Game.loadAbilities("./Abilities.csv");
							Game.loadChampions("./Champions.csv");
						} catch (IOException e2) {
							System.out.println(e2);
							new errormes("Error", "Error in Abilities and Champion Files");
						}
						toscene2(game.getFirstPlayer(), stage);
					}
				} else {
					new errormes("Error", "Names can't be empty");
				}
			}
		};
		toscene2.setOnAction(proceed);
		toscene2.setPadding(new Insets(10));
		border.setTop(toscene2);
//		border.setMargin(border.getBottom(), new Insets(50));
		border.setAlignment(toscene2, Pos.TOP_CENTER);
		stage.show();
	}

	public static void toscene2(Player p, Stage stage) {
		BorderPane border = new BorderPane();
		border.setPrefHeight(1000);
		border.setPrefWidth(700);
		Scene scene = new Scene(border);
		border.setStyle("-fx-background-image: url(\"/Assets//Marvel_background2.jpg\");" + "-fx-background-size: cover;");
		stage.setScene(scene);
		stage.setWidth(1001);
		stage.setHeight(701);
		VBox tostring = new VBox();

		tostring.setSpacing(14);
		tostring.setMaxWidth(200);
		tostring.setMinWidth(200);
//		tostring.setStyle("-fx-background-color: #123456;");

		VBox selected = new VBox();
		selected.setSpacing(8);
		selected.setPrefWidth(200);
//		selected.setStyle("-fx-background-color: #aaaaaa;");
		VBox leftpane = new VBox();
		leftpane.setSpacing(8);
		leftpane.setPrefWidth(200);
		
		leftpane.getChildren().add(selected);
		Button removeChampion = new Button("Remove Last Champion");
		leftpane.getChildren().add(removeChampion);
		Button toscene3 = new Button("Continue");
		border.setBottom(toscene3);
		border.setAlignment(toscene3, Pos.BOTTOM_CENTER);
		toscene3.setVisible(false);
		ToggleGroup tog = new ToggleGroup();
		tog.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(ObservableValue<? extends Toggle> ob, Toggle o, Toggle n) {
				RadioButton rb = (RadioButton) tog.getSelectedToggle();
				if (rb != null) {
					for (Champion c : p.getTeam()) {
						if (c.getName().equals(rb.getText())) {
							p.setLeader(c);
							break;
						}
					}
				}
			}
		});

		Text t2 = new Text("Your Team:");
		t2.setFont(Font.font("verdana", 15));
		t2.setFill(Color.WHITE);
		selected.getChildren().add(t2);

		GridPane grid = new GridPane();
		Button[] arr = new Button[15];
		ImageView [] images = new ImageView[15];
		arr[0] = new Button();
		Image ca = new Image("/Assets/captain-america-1.png"); 
		images[0] = new ImageView(ca);
		images[0].setFitHeight(70);
		images[0].setFitWidth(70);
		arr[0].setGraphic(images[0]);
		arr[0].setMaxHeight(70);
		arr[0].setMaxWidth(70);
		
		arr[1] = new Button();
		Image dp = new Image("/Assets/deadpool-1.jpg"); 
		images[1] = new ImageView(dp);
		images[1].setFitHeight(70);
		images[1].setFitWidth(70);
		arr[1].setGraphic(images[1]);
		arr[1].setMaxHeight(70);
		arr[1].setMaxWidth(70);
		
		arr[2] = new Button();
		Image dr = new Image("/Assets/dr-strange.jpg"); 
		images[2] = new ImageView(dr);
		images[2].setFitHeight(70);
		images[2].setFitWidth(70);
		arr[2].setGraphic(images[2]);
		arr[2].setMaxHeight(70);
		arr[2].setMaxWidth(70);
		
		
		arr[3] = new Button();
		Image el = new Image("/Assets/electro.png"); 
		images[3] = new ImageView(el);
		images[3].setFitHeight(70);
		images[3].setFitWidth(70);
		arr[3].setGraphic(images[3]);
		arr[3].setMaxHeight(70);
		arr[3].setMaxWidth(70);		
		
		arr[4] = new Button();
		Image gr = new Image("/Assets/ghost-rider.jpg"); 
		images[4] = new ImageView(gr);
		images[4].setFitHeight(70);
		images[4].setFitWidth(70);
		arr[4].setGraphic(images[4]);
		arr[4].setMaxHeight(70);
		arr[4].setMaxWidth(70);		
		
		arr[5] = new Button();
		Image hl = new Image("/Assets/hela.png"); 
		images[5] = new ImageView(hl);
		images[5].setFitHeight(70);
		images[5].setFitWidth(70);
		arr[5].setGraphic(images[5]);
		arr[5].setMaxHeight(70);
		arr[5].setMaxWidth(70);	
		
		arr[6] = new Button();
		Image hu = new Image("/Assets/hulk.jpg"); 
		images[6] = new ImageView(hu);
		images[6].setFitHeight(70);
		images[6].setFitWidth(70);
		arr[6].setGraphic(images[6]);
		arr[6].setMaxHeight(70);
		arr[6].setMaxWidth(70);	
		
		arr[7] = new Button();
		Image ice = new Image("/Assets/iceman.jpg"); 
		images[7] = new ImageView(ice);
		images[7].setFitHeight(70);
		images[7].setFitWidth(70);
		arr[7].setGraphic(images[7]);
		arr[7].setMaxHeight(70);
		arr[7].setMaxWidth(70);	
		
		arr[8] = new Button();
		Image im = new Image("/Assets/ironman.png"); 
		images[8] = new ImageView(im);
		images[8].setFitHeight(70);
		images[8].setFitWidth(70);
		arr[8].setGraphic(images[8]);
		arr[8].setMaxHeight(70);
		arr[8].setMaxWidth(70);	
		
		arr[9] = new Button();
		Image lo = new Image("/Assets/loki.jpg"); 
		images[9] = new ImageView(lo);
		images[9].setFitHeight(70);
		images[9].setFitWidth(70);
		arr[9].setGraphic(images[9]);
		arr[9].setMaxHeight(70);
		arr[9].setMaxWidth(70);	
		
		arr[10] = new Button();
		Image qs = new Image("/Assets/quicksilver.jpg"); 
		images[10] = new ImageView(qs);
		images[10].setFitHeight(70);
		images[10].setFitWidth(70);
		arr[10].setGraphic(images[10]);
		arr[10].setMaxHeight(70);
		arr[10].setMaxWidth(70);	
		
		arr[11] = new Button();
		Image sp = new Image("/Assets/spiderman.jpg"); 
		images[11] = new ImageView(sp);
		images[11].setFitHeight(70);
		images[11].setFitWidth(70);
		arr[11].setGraphic(images[11]);
		arr[11].setMaxHeight(70);
		arr[11].setMaxWidth(70);	
		
		arr[12] = new Button();
		Image th = new Image("/Assets/thor.jpg"); 
		images[12] = new ImageView(th);
		images[12].setFitHeight(70);
		images[12].setFitWidth(70);
		arr[12].setGraphic(images[12]);
		arr[12].setMaxHeight(70);
		arr[12].setMaxWidth(70);	
		
		arr[13] = new Button();
		Image vn = new Image("/Assets/venom.jpg"); 
		images[13] = new ImageView(vn);
		images[13].setFitHeight(70);
		images[13].setFitWidth(70);
		arr[13].setGraphic(images[13]);
		arr[13].setMaxHeight(70);
		arr[13].setMaxWidth(70);	
		
		arr[14] = new Button();
		Image yj = new Image("/Assets/yellowjacket.png"); 
		images[14] = new ImageView(yj);
		images[14].setFitHeight(70);
		images[14].setFitWidth(70);
		arr[14].setGraphic(images[14]);
		arr[14].setMaxHeight(70);
		arr[14].setMaxWidth(70);	
		
		if (!chosenChamions.isEmpty()) {
			for (int i = 0; i < chosenChamions.size(); i++) {
				final int j = chosenChamions.get(i);
				arr[j].setVisible(false);
			}
		}
		for (int i = 0; i < 15; i++) {
			final int j = i;
			arr[i].addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<Event>() {
				public void handle(Event arg0) {
					viewChampionStats(tostring, j);
				}
			});
			arr[i].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<Event>() {
				public void handle(Event arg0) {
					arr[j].setVisible(false);
					addChampion(tog, p, toscene3, selected, j, arr);
				}
			});
			grid.add(arr[i], i % 5, i / 5, 1, 1);
		}

		removeChampion.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<Event>() {
			public void handle(Event arg0) {
				if (selected.getChildren().size() == 1) {
					new errormes("Error", "Your Team is Empty");
					return;
				}
				Node n = selected.getChildren().remove(selected.getChildren().size() - 1);
				int x = chosenChamions.remove(chosenChamions.size() - 1);
				final int j = x;
				arr[j].setGraphic(null);
				arr[j].setGraphic(images[j]);
				arr[j].setVisible(true);
				if(p.getTeam().size()!=0&&p.getLeader()!=null&&p.getLeader().getName().equals(p.getTeam().get(p.getTeam().size()-1).getName())) {
					p.setLeader(null);
				}
				p.getTeam().remove(p.getTeam().size() - 1);
				toscene3.setVisible(false);
			}
		});

				
		
		grid.setHgap(20);
		grid.setVgap(20);
		grid.setAlignment(Pos.CENTER);

		Label t = new Label("Select Champions For " + p.getName() + ":");
		t.setFont(Font.font("verdana", 30));
		t.setTextFill(Color.WHITE);
		
		leftpane.setBackground(Background.fill(Color.TRANSPARENT));
		border.setRight(tostring);
		border.setLeft(leftpane);
		border.setTop(t);
		border.setCenter(grid);
//		border.getCenter().setStyle("-fx-background-image: url(\"/Assets//Marvel_background2.jpg\");" + "-fx-background-size: cover;");
		//border.getCenter().setStyle();
		toscene3.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<Event>() {
			public void handle(Event arg0) {
				if (p.getLeader() == null) {
					new errormes("Error", "Please Choose A Leader");
					return;
				}
				toScene3(stage);
			}
		});
		
	}

	public static void toScene3(Stage stage) {
		if (game.getSecondPlayer().getTeam().size() != 0) {
			scene4(stage);
		} else {
			toscene2(game.getSecondPlayer(), stage);
		}
	}

	public static void scene4(Stage stage) {
		// TODO effects update
		// TODO end Turn
		// TODO xyAbility exception=hide buttons
		// TODO removing leader from selected champion is not working
		game.prepareChampionTurns();
		fastestChampion = ((Champion) game.getTurnOrder().peekMin()).getName();
		BorderPane border = new BorderPane();
		border.setPrefHeight(1000);
		border.setPrefWidth(700);
		Scene scene = new Scene(border);
		border.setStyle("-fx-background-color: #87CEEB;");
		stage.setScene(scene);
		stage.setWidth(1001);
		stage.setHeight(701);

		current = new Label("Current Champion: " + game.getCurrentChampion().getName());
		current.setFont(Font.font("verdana", 30));
		Champion c = (Champion) game.getTurnOrder().remove();
		next = new Label("next Champion: " + ((Champion) game.getTurnOrder().peekMin()).getName());
		game.getTurnOrder().insert(c);
		next.setFont(Font.font("verdana", 30));

		HBox toprow1 = new HBox();
		toprow1.setSpacing(14);
		toprow1.getChildren().add(current);
		toprow1.getChildren().add(next);

		HBox toprow2 = new HBox();

		player1stat = new Label(
				game.getFirstPlayer().getName() + " used Leader Ability=" + game.isFirstLeaderAbilityUsed());
		player1stat.setFont(Font.font("verdana", 30));
		player2stat = new Label(
				game.getSecondPlayer().getName() + " used Leader Ability=" + game.isSecondLeaderAbilityUsed());
		player2stat.setFont(Font.font("verdana", 30));
		toprow2.setSpacing(14);
		toprow2.getChildren().add(player1stat);
		toprow2.getChildren().add(player2stat);

		VBox top = new VBox();
		top.setSpacing(14);
		top.getChildren().add(toprow1);
		top.getChildren().add(toprow2);

		VBox rightpane = new VBox();
		rightpane.setSpacing(14);
		rightpane.setMaxWidth(200);
		rightpane.setMinWidth(200);
		rightpane.setStyle("-fx-background-color: #123456;");
		Label stat = new Label("Stats:");
		stat.setTextFill(Color.BLACK);
		stat.setFont(Font.font("verdana", 19));
		rightpane.getChildren().add(stat);

		Label abilityData=new Label();
		abilityData.setFont(Font.font("verdana", 14));
		abilityData.setTextFill(Color.BLACK);

		
		VBox leftpane = new VBox();
		leftpane.setSpacing(14);
		leftpane.setMaxWidth(250);
		leftpane.setMinWidth(250);
		leftpane.setStyle("-fx-background-color: #CAFFEE;");
		Label ava = new Label("Available Actions:");
		ava.setFont(Font.font("verdana", 19));
		leftpane.getChildren().add(ava);

		VBox abilityBox = new VBox(14);
		leftpane.getChildren().add(abilityBox);
		Label abilityLabel = new Label("Your Abilities:");
		abilityLabel.setTextFill(Color.BLACK);
		abilityBox.getChildren().add(abilityLabel);

		GridPane grid = new GridPane();
		grid.setGridLinesVisible(true);
		grid.setAlignment(Pos.CENTER);
		VBox[][] grr = new VBox[5][5];
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				grr[j][i] = new VBox();
				grr[j][i].setMinHeight(100);
				grr[j][i].setMaxHeight(100);
				grr[j][i].setMinWidth(100);
				grr[j][i].setMaxWidth(100);
				grid.add(grr[j][i], j, i);
				final int j2 = j;
				final int i2 = i;
				grr[j][i].addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<Event>() {
					public void handle(Event arg0) {
						if (grr[j2][i2].getChildren().size() != 0) {
							dispalystats(((Label) grr[j2][i2].getChildren().get(1)), rightpane);
						}
					}
				});
			}
		}
		game.placeChampions();
		updateGrid(grid);
		grid.setVgap(5);
		grid.setHgap(5);

		Border b = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));
		abilityData.setBorder(b);
		rightpane.getChildren().add(abilityData);
		border.setRight(rightpane);
		border.setLeft(leftpane);
		border.setTop(top);
		border.setCenter(grid);
		prepareActions(leftpane, abilityBox, game.getCurrentChampion(), grid, stage,rightpane);

	}

	public static void prepareActions(VBox leftpane, VBox abilityBox, Champion c, GridPane grid, Stage stage,VBox rightpane) {
		while(leftpane.getChildren().size()!=2) {
			leftpane.getChildren().remove(2);
		}
		HBox moveBox = new HBox();
		Button move = new Button("Move");
		move.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<Event>() {
			public void handle(Event arg0) {
				actionDirectional(moveBox, grid, stage, "move", null);
			}
		});
		leftpane.getChildren().add(move);
		Button attack = new Button("Attack");
		leftpane.getChildren().add(attack);
		attack.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<Event>() {
			public void handle(Event arg0) {
				actionDirectional(moveBox, grid, stage, "attack", null);
			}

		});
		Border b = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));
		moveBox.setBorder(b);
		ArrayList<Ability> abilities = c.getAbilities();
//		HBox abilityBox1 = new HBox();
//		abilityBox1.getChildren().add(attack);
		ArrayList<Ability> justAbilities = new ArrayList<Ability>();
		ArrayList<Ability> xyAbilities = new ArrayList<Ability>();
		ArrayList<Ability> directionalAbilities = new ArrayList<Ability>();
		for (Ability a : abilities) {
			if (a.getCastArea() == AreaOfEffect.DIRECTIONAL) {
				directionalAbilities.add(a);
			}
			if (a.getCastArea() == AreaOfEffect.SINGLETARGET) {
				xyAbilities.add(a);
			} else {
				justAbilities.add(a);
			}
		}
		while(abilityBox.getChildren().size()!=1) {
			abilityBox.getChildren().remove(1);
		}
		for (int i = 0; i < game.getCurrentChampion().getAbilities().size(); i++) {
			Ability a = game.getCurrentChampion().getAbilities().get(i);
			Button button = new Button(a.getName());
			button.setTextFill(Color.BLACK);
			abilityBox.getChildren().add(button);
			button.addEventHandler(MouseEvent.MOUSE_ENTERED,new EventHandler<Event>() {
				public void handle(Event arg0) {
					((Label)rightpane.getChildren().get(rightpane.getChildren().size()-1)).setText(a.toString());	
				}
			});
			button.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<Event>() {
				public void handle(Event arg0) {
					if (justAbilities.contains(a)) {
						castJustAbility(game.getCurrentChampion(), a);
						updateGrid(grid);
						gameOver(stage);
					}
					if (directionalAbilities.contains(a)) {
						actionDirectional(moveBox, grid, stage, "castAbility", a);
						updateGrid(grid);
					}
					if (xyAbilities.contains(a)) {
						HBox xyBox = new HBox();
						 //castJustAbility(game.getCurrentChampion(),a);
						Text getx = new Text("Enter X Position");
						getx.setFont(Font.font("verdana", 15));

						TextField thex = new TextField();
						thex.setFont(Font.font("verdana", 15));

						Text gety = new Text("Enter Y Position");
						gety.setFont(Font.font("verdana", 15));

						TextField they = new TextField();
						
						they.setFont(Font.font("verdana", 15));
						xyBox.getChildren().add(thex);
						xyBox.getChildren().add(they);
						leftpane.getChildren().add(xyBox);
					
						Button submit = new Button("submit");
						submit.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<Event>() {
							public void handle(Event arg0) {
								int x = Integer.parseInt(thex.getText());
								int y = Integer.parseInt(they.getText());
								castxyabilities(c, a,x,y,grid);
								
							}
						});
						leftpane.getChildren().add(submit);
					}
				}
			});
		}
		Button LeaderAbility = new Button("Use Leader Ability");
		leftpane.getChildren().add(LeaderAbility);
		LeaderAbility.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<Event>() {
			public void handle(Event arg0) {
				useLeaderAbility();
				updateGrid(grid);
				gameOver(stage);
			}

		});
		leftpane.getChildren().add(moveBox);

		Button endTurn = new Button("End Turn");
		leftpane.getChildren().add(endTurn);
		endTurn.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<Event>() {
			public void handle(Event arg0) {
				game.endTurn();
				//System.out.println(game.getCurrentChampion().getName());
				current.setText("Current Champion: " + game.getCurrentChampion().getName());
				try {
					Champion c = (Champion) game.getTurnOrder().remove();
					if (game.getTurnOrder().size() == 0) {
						next.setText("next Champion: " + fastestChampion);
					} else {
						next.setText("next Champion: " + ((Champion) game.getTurnOrder().peekMin()).getName());
					}
					// System.out.println(((Champion)game.getTurnOrder().peekMin()).getName());
					game.getTurnOrder().insert(c);
				} catch (Exception e) {
					System.out.println("exc");
					game.prepareChampionTurns();
					next.setText("next Champion: " + ((Champion) game.getTurnOrder().peekMin()).getName());
				}
				updateGrid(grid);
				gameOver(stage);
				prepareActions(leftpane, abilityBox, game.getCurrentChampion(), grid, stage,rightpane);
			}

		});

	}
	public static void castxyabilities(Champion c, Ability a, int x,int y,GridPane grid) {
		try {
			//System.out.println(((Damageable)game.getBoard()[x][y]).getCurrentHP());
			//System.out.println(((Label)((VBox)getNodeFromGrid(grid, 4-x, y)).getChildren().get(1)).getText());
			game.castAbility(a, 4-x, y);
		}catch (Exception e) {
			//System.out.println(e);
			new errormes("Error", e.getMessage());
		}
	}

	public static void useLeaderAbility() {
		try {
			game.useLeaderAbility();
			player1stat.setText(
					game.getFirstPlayer().getName() + " used Leader Ability=" + game.isFirstLeaderAbilityUsed());
			player2stat.setText(
					game.getSecondPlayer().getName() + " used Leader Ability=" + game.isSecondLeaderAbilityUsed());
		} catch (Exception e) {
			new errormes("Error", e.getMessage());
		}

	}

	public static void castJustAbility(Champion currentChampion, Ability a) {
		System.out.println("here");
		System.out.println(a.getName());
		try {
			game.castAbility(a);
		} catch (Exception e) {
			new errormes("error", e.getMessage());
		}
	}

	public static void dispalystats(Label node, VBox rightpane) {
		Label l = new Label(node.getText());
		l.setFont(Font.font("verdana", 14));
		l.setTextFill(Color.BLACK);
		Label n = new Label();
		while (rightpane.getChildren().size() != 1) {
			if (rightpane.getChildren().get(1) instanceof Label) {
				n = (Label) rightpane.getChildren().remove(1);
			} else {
				rightpane.getChildren().remove(1);
			}
		}
		rightpane.getChildren().add(l);
		rightpane.getChildren().add(n);
	}

	public static void updateGrid(GridPane grid) {
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				Object d = game.getBoard()[i][j];
				ObservableList<Node> children = ((VBox) getNodeFromGrid(grid, 4 - i, j)).getChildren();
				while (!children.isEmpty()) {
					children.remove(0);
				}
				if (d instanceof Damageable) {
					int x = ((Damageable) d).getLocation().x;
					int y = ((Damageable) d).getLocation().y;
					if (d instanceof Champion) {
						String name = (((Champion) d).getName());
						Image ch = new Image(geticon(name));
						ImageView l = new ImageView(ch);
						l.setFitHeight(100);
						l.setFitWidth(100);
						((VBox) getNodeFromGrid(grid, 4 - x, y)).getChildren().add(l);
						Champion c = (Champion) d;
						Label l2 = new Label(enhancedToString(c));
						l2.setVisible(false);
						((VBox) getNodeFromGrid(grid, 4 - x, y)).getChildren().add(l2);
					} else {
						//Label l = new Label("Cover");
						Image cover = new Image("Assets/cover.png");
						ImageView l = new ImageView(cover);
						l.setFitHeight(100);
						l.setFitWidth(100);
						((VBox) getNodeFromGrid(grid, 4 - x, y)).getChildren().add(l);
						Label l2 = new Label(((Cover) d).toString());
						l2.setVisible(false);
						((VBox) getNodeFromGrid(grid, 4 - x, y)).getChildren().add(l2);

					}
				}
			}
		}
	}

	public static String geticon(String s)
	{
		
			switch(s)
			{
			case("Captain America"): return "\\Assets\\Captain-america-2.png";
			case("Deadpool"): return "\\Assets\\deadpool-2.jpg";
			case("Dr Strange") : return "\\Assets\\dr-strange-2.jpg";
			case("Electro"):return "\\Assets\\electro-2.png";
			case("Ghost Rider") : return "\\Assets\\ghost-rider-2.jpg";
			case("Hela") : return "\\Assets\\hela-2.jpg";
			case("Hulk"): return "\\Assets\\hulk-2.jpg";
			case("Iceman"):return "\\Assets\\iceman-2.jpg";
			case("Ironman"): return "\\Assets\\ironman-2.png";
			case("Loki"):return "\\Assets\\loki-2.png";
			case("Quicksilver"):return "\\Assets\\\\quicksilver-2.jpg";
			case("Spiderman"):return "\\Assets\\\\spiderman-2.jpg";
			case("Thor") :return "Thorbat.png";
			case("Venom"):return "Venombat.png";
			default : return "\\Assets\\deadpool-2.jpg";
			
			}
		
			
	}
	
	public static String enhancedToString(Champion c) {
		// TODO handle duplicate shield names
		// TODO damaging ability worked on cover only(i think so )
		String s2 = "";
		if (c instanceof Champion) {
			s2 = "Champion";
		}
		if (c instanceof Villain) {
			s2 = "Villain";
		}
		if (c instanceof AntiHero) {
			s2 = "AntiHero";
		}
		String s3 = "";
		for (int j = 0; j < c.getAbilities().size(); j++) {
			s3 += c.getAbilities().get(j).getName() + "\n";
		}
		String eff = "";
		for (int j = 0; j < c.getAppliedEffects().size(); j++) {
			eff += c.getAppliedEffects().get(j).getName() + " Duration=" + c.getAppliedEffects().get(j).getDuration()
					+ "\n";
		}
		if(c.getAppliedEffects().size()!=0) {
			System.out.println(c.getAppliedEffects());
			System.out.println(eff);
		}
		if (c.equals(game.getCurrentChampion())) {
			String s = "name=" + c.getName() + "\n" + "Type=" + s2 + "\n" + "currentHP=" + c.getCurrentHP() + "\n"
					+ "mana=" + c.getMana() + "\n" + "currentActionPoints=" + c.getCurrentActionPoints() + "\n"
					+ "attackRange=" + c.getAttackRange() + "\n" + "attackDamage=" + c.getAttackDamage() + "\n"
					+ "Effects=" + "\n" + eff + "Abilities=" + "\n" + s3;
			return s;
		} else {
			boolean flag = c.equals(game.getFirstPlayer().getLeader()) || c.equals(game.getSecondPlayer().getLeader());
			String s = "name=" + c.getName() + "\n" + "Type=" + s2 + "\n" + "currentHP=" + c.getCurrentHP() + "\n"
					+ "mana=" + c.getMana() + "\n" + "Speed=" + c.getSpeed() + "\n" + "maxActionPoints="
					+ c.getMaxActionPointsPerTurn() + "\n" + "attackRange=" + c.getAttackRange() + "\n"
					+ "attackDamage=" + c.getAttackDamage() + "\n" + "isLeader=" + flag + "\n" + "Effects=" + "\n"
					+ eff;
			return s;
		}
	}

	public static Node getNodeFromGrid(GridPane gridPane, final int row, final int column) {
		Node result = null;
		ObservableList<Node> childrens = gridPane.getChildren();
		for (Node node : childrens) {
			if (node != null) {
				if (gridPane.getRowIndex(node) != null && gridPane.getRowIndex(node) == row
						&& gridPane.getColumnIndex(node) != null && gridPane.getColumnIndex(node) == column) {
					result = node;
					break;
				}
			}
		}
		return result;
	}

	public static void addChampion(ToggleGroup t, Player p, Button b, VBox selected, int i, Button[] arr) {
		if (p.getTeam().size() == 3) {
			arr[i].setVisible(true);
			new errormes("Error", "Your Team can only Have 3 Members");
			return;
		}
		if (p.getTeam().size() == 3) {
		}
//		Text t=new Text(game.getAvailableChampions().get(i).getName());
//		t.setFont(Font.font("verdana",15));
		RadioButton rb = new RadioButton(game.getAvailableChampions().get(i).getName());
		rb.setFont(Font.font("verdana", 13));
		rb.setStyle("-fx-text-fill: White");
		rb.setGraphic(arr[i].getGraphic());
		rb.setToggleGroup(t);
		selected.getChildren().add(rb);
		chosenChamions.add(i);
		p.getTeam().add(game.getAvailableChampions().get(i));
		if (p.getTeam().size() == 3) {
			b.setVisible(true);
		}
	}

	public static void viewChampionStats(VBox tostring, int i) {
		Text t = new Text(game.getAvailableChampions().get(i).toString());
		t.setFont(Font.font("verdana", 15));
		t.setFill(Color.WHITE);
		if (!tostring.getChildren().isEmpty()) {
			tostring.getChildren().remove(0);
		}
		tostring.getChildren().add(t);
		
	}

	public static void main(String[] args) {
		launch(args);
	}

	public static void hideButtons(HBox box) {
		for (int i = 0; i < 4; i++)
			box.getChildren().remove(0);
	}

	public static void gameOver(Stage stage) {
		if (game.checkGameOver() == null) {
			return;
		} else if (game.checkGameOver().equals(player1)) {
			new Alert("Congrats", "Player 1 Won \n 9ame over");
			stage.setScene(new Scene(null,Color.SKYBLUE));
			//stage.setdisabeled(true);
		} else {
			new Alert("Congrats", "Player 2 Won \n 9ame over");
			stage.setScene(new Scene(null,Color.SKYBLUE));
		}

	}

	public static void actionDirectional(HBox directionBox, GridPane grid, Stage stage, String s, Ability a) {
		while (!directionBox.getChildren().isEmpty()) {
			directionBox.getChildren().remove(0);
		}
		Button[] movesbut = new Button[4];
		movesbut[0] = new Button("Up");
		movesbut[0].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<Event>() {
			public void handle(Event arg0) {
				try {
					if (s.equals("move")) {
						game.move(model.world.Direction.UP);
					} else if (s.equals("attack")) {
						game.attack(model.world.Direction.UP);
					} else if (s.equals("castAbility")) {
						game.castAbility(a, model.world.Direction.UP);
					}
					updateGrid(grid);
					hideButtons(directionBox);
					gameOver(stage);
				} catch (NotEnoughResourcesException | ChampionDisarmedException | AbilityUseException
						| InvalidTargetException | UnallowedMovementException | CloneNotSupportedException e) {
					new errormes("Error", e.toString());
				}
			}
		});
		movesbut[1] = new Button("Right");
		movesbut[1].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<Event>() {
			public void handle(Event arg0) {
				try {
					if (s.equals("move")) {
						game.move(model.world.Direction.RIGHT);
					} else if (s.equals("attack")) {
						game.attack(model.world.Direction.RIGHT);
					} else if (s.equals("castAbility")) {
						game.castAbility(a, model.world.Direction.RIGHT);
					}
					updateGrid(grid);
					hideButtons(directionBox);
					gameOver(stage);
				} catch (NotEnoughResourcesException | ChampionDisarmedException | InvalidTargetException
						| UnallowedMovementException | CloneNotSupportedException | AbilityUseException e) {
					new errormes("Error", e.toString());
				}
			}
		});
		movesbut[2] = new Button("Left");
		movesbut[2].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<Event>() {
			public void handle(Event arg0) {
				try {
					if (s.equals("move")) {
						game.move(model.world.Direction.LEFT);
					} else if (s.equals("attack")) {
						game.attack(model.world.Direction.LEFT);
					} else if (s.equals("castAbility")) {
						game.castAbility(a, model.world.Direction.LEFT);
					}
					updateGrid(grid);
					hideButtons(directionBox);
					gameOver(stage);
				} catch (NotEnoughResourcesException | ChampionDisarmedException | InvalidTargetException
						| UnallowedMovementException | CloneNotSupportedException | AbilityUseException e) {
					new errormes("Error", e.toString());
				}
			}
		});
		movesbut[3] = new Button("Down");
		movesbut[3].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<Event>() {
			public void handle(Event arg0) {
				try {
					if (s.equals("move")) {
						game.move(model.world.Direction.DOWN);
					} else if (s.equals("attack")) {
						game.attack(model.world.Direction.DOWN);
					} else if (s.equals("castAbility")) {
						game.castAbility(a, model.world.Direction.DOWN);
					}
					updateGrid(grid);
					hideButtons(directionBox);
					gameOver(stage);
				} catch (NotEnoughResourcesException | ChampionDisarmedException | InvalidTargetException
						| UnallowedMovementException | CloneNotSupportedException | AbilityUseException e) {
					new errormes("Error", e.toString());
				}
			}
		});
		for (int i = 0; i < 4; i++) {
			directionBox.getChildren().add(movesbut[i]);
		}
	}
}
