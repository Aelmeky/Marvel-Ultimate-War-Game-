package computer;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import model.effects.*;
import engine.Game;
import engine.Player;
import model.abilities.Ability;
import model.abilities.CrowdControlAbility;
import model.abilities.DamagingAbility;
import model.abilities.HealingAbility;
import model.world.AntiHero;
import model.world.Champion;
import model.world.Cover;
import model.world.Direction;
import model.world.Hero;
import model.world.Villain;

public class driver {
	static int i=1;
	public static void main(String[] args) {
		Player p1 = new Player("Ahmed");
		Player p2 = new Player("meky");
		Game game = new Game(p1, p2);
		ArrayList<Champion> team1 = new ArrayList<Champion>();
		try {
			Game.loadAbilities("./Abilities.csv");
			Game.loadChampions("./Champions.csv");
		} catch (IOException e) {
		}
		game.getFirstPlayer().getTeam()
				.add(game.getAvailableChampions().get(1));
		game.getFirstPlayer().getTeam()
				.add(game.getAvailableChampions().get(6));
		game.getFirstPlayer().getTeam()
				.add(game.getAvailableChampions().get(11));

		game.getSecondPlayer().getTeam()
				.add(game.getAvailableChampions().get(0));
		game.getSecondPlayer().getTeam()
				.add(game.getAvailableChampions().get(5));
		game.getSecondPlayer().getTeam()
				.add(game.getAvailableChampions().get(10));
		game.placeChampions();
		game.prepareChampionTurns();
		try {
			game.move(Direction.DOWN);
		} catch (Exception e) {
			System.out.println(e);
		}
		printGame(game);
		Game game2 = clone(game);
		printGame(game2);
		game2.getCurrentChampion().setCurrentActionPoints(2);
		ArrayList<String>arr=new ArrayList<String>();
		arr=minimax(game, game2, game2.getSecondPlayer(), arr, 4);
		System.out.println("------------");
		System.out.println(arr);
		}

	public static void printGame(Game game) {
		for (int i = 0; i < 5; i++) {
			System.out.print("|");
			for (int j = 0; j < 5; j++) {
				if (game.getBoard()[4-i][j] == null) {
					System.out.print(" ");
				}
				if (game.getBoard()[4-i][j] != null) {
					if (game.getBoard()[4-i][j] instanceof Champion) {
						System.out.print("C");

					}
					if (game.getBoard()[4-i][j] instanceof Cover) {
						System.out.print("v");
					}
				}
			}
			System.out.print("|");
			System.out.println();
		}
		System.out.println("-------------");
	}

	public static Game clone(Game game) {
		String n1 = game.getFirstPlayer().getName();
		String n2 = game.getSecondPlayer().getName();
		Player p1 = new Player(n1);
		Player p2 = new Player(n2);
		Game newGame = new Game(p1, p2);
		cloneChampions(game.getFirstPlayer(), p1);
		cloneChampions(game.getSecondPlayer(), p2);
		newGame.placeChampions();
		newGame.prepareChampionTurns();
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (newGame.getBoard()[4-i][j]!=null) {
					newGame.getBoard()[4-i][j] = null;
				}
			}
		}
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (game.getBoard()[4-i][j] != null) {
					if(game.getBoard()[4-i][j] instanceof Champion){
						Champion c=getChampionByName(newGame,((Champion)game.getBoard()[4-i][j]).getName());
						c.setLocation(new Point(((Champion)game.getBoard()[4-i][j]).getLocation().x,((Champion)game.getBoard()[4-i][j]).getLocation().y));
						newGame.getBoard()[4-i][j]=c;
					}
					if (game.getBoard()[4-i][j] instanceof Cover) {
						Cover c = new Cover(i, j);
						c.setCurrentHP(((Cover) game.getBoard()[4-i][j])
								.getCurrentHP());
						newGame.getBoard()[4-c.getLocation().x][c.getLocation().y] = c;
					}
				}				
			}
		}
		return newGame;
	}

	public static Champion getChampionByName(Game game, String name) {
		for(Champion c:game.getFirstPlayer().getTeam()){
			if(c.getName().equals(name)){
				return c;
			}
		}
		for(Champion c:game.getSecondPlayer().getTeam()){
			if(c.getName().equals(name)){
				return c;
			}
		}
		return null;
	}

	public static void cloneChampions(Player p1, Player p2) {
		for (Champion c : p1.getTeam()) {
			Champion c2 = null;
			if (c instanceof Hero) {
				c2 = new Hero(c.getName(), c.getMaxHP(), c.getMana(),
						c.getMaxActionPointsPerTurn(), c.getSpeed(),
						c.getAttackRange(), c.getAttackDamage());
			}
			if (c instanceof Villain) {
				c2 = new Villain(c.getName(), c.getMaxHP(), c.getMana(),
						c.getMaxActionPointsPerTurn(), c.getSpeed(),
						c.getAttackRange(), c.getAttackDamage());
			}
			if (c instanceof AntiHero) {
				c2 = new AntiHero(c.getName(), c.getMaxHP(), c.getMana(),
						c.getMaxActionPointsPerTurn(), c.getSpeed(),
						c.getAttackRange(), c.getAttackDamage());
			}
			c2.setCurrentHP(c.getCurrentHP());
			c2.setCurrentActionPoints(c.getCurrentActionPoints());
			c2.setCondition(c.getCondition());
			c2.setLocation(new Point(c.getLocation().x,c.getLocation().y));
			c2.setMana(c.getMana());
			for (Ability a : c.getAbilities()) {
				Ability a2 = null;
				if (a instanceof CrowdControlAbility) {
					Effect f = null;
					try {
						f = (Effect) ((CrowdControlAbility) a).getEffect()
								.clone();
					} catch (CloneNotSupportedException e) {
					}
					a2 = new CrowdControlAbility(a.getName(), a.getManaCost(),
							a.getBaseCooldown(), a.getCastRange(),
							a.getCastArea(), a.getRequiredActionPoints(), f);
					c2.getAbilities().add(a2);
				}
				if (a instanceof HealingAbility) {
					a2 = new HealingAbility(a.getName(), a.getManaCost(),
							a.getBaseCooldown(), a.getCastRange(),
							a.getCastArea(), a.getRequiredActionPoints(),
							((HealingAbility) a).getHealAmount());
					c2.getAbilities().add(a2);
				}
				if (a instanceof DamagingAbility) {
					a2 = new DamagingAbility(a.getName(), a.getManaCost(),
							a.getBaseCooldown(), a.getCastRange(),
							a.getCastArea(), a.getRequiredActionPoints(),
							((DamagingAbility) a).getDamageAmount());
					c2.getAbilities().add(a2);
				}
			}
			for (Effect e : c.getAppliedEffects()) {
				try {
					Effect e2 = (Effect) e.clone();
					c2.getAppliedEffects().add(e2);
				} catch (CloneNotSupportedException e1) {
				}
			}
			p2.getTeam().add(c2);
		}
	}

	public static int evaluate(Game ogame, Game ngame, Player me) {
		Object[][] n = ngame.getBoard();
		Object[][] o = ogame.getBoard();
		if (ngame.getSecondPlayer().getTeam().size() == 0) {
			return Integer.MIN_VALUE;
		}
		if (ngame.getFirstPlayer().getTeam().size() == 0) {
			return Integer.MAX_VALUE;
		}
		int sum = 0;
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (o[i][j] == n[i][j]) {
					continue;
				} else if (o[i][j] instanceof Champion) {
					if (isFriend(me, ((Champion) n[i][j]))) {
						// effect on a friend
						if (n[i][j] == null) {
							// a friend got killed
							sum -= 2 * ((Champion) o[i][j]).getMaxHP();
						} else {
							// A friend got damaged
							sum -= ((Champion) o[i][j]).getCurrentHP()
									- ((Champion) n[i][j]).getCurrentHP();
						}
					}
					if (!isFriend(me, ((Champion) n[i][j]))) {
						// effect on an enemy
						if (n[i][j] == null) {
							// killed an enemy
							sum += 2 * ((Champion) o[i][j]).getMaxHP();
						} else {
							// damaged an enemy
							sum += ((Champion) o[i][j]).getCurrentHP()
									- ((Champion) n[i][j]).getCurrentHP();
						}
					}
				}
			}
		}
		return sum;
	}

	public static boolean isFriend(Player me, Champion c) {
		for (Champion c2 : me.getTeam()) {
			if (c2.getName().equals(c.getName())) {
				return true;
			}
		}
		return false;
	}

 public static ArrayList<String> max(Game oldGame, Game game, Champion c){
	// ArrayList<String> arr, Player p) {
	// if (c.getCurrentActionPoints() == 0) {
	// int x = evaluate(oldGame, game, p);
	// arr.add(x + "");
	// return arr;
	// }
	// System.out.println("here");
	// try {
	// Game ngame = clone(game);
	// ngame.move(Direction.UP);
	// arr.add("moveup");
	// arr = max(oldGame, ngame, c, arr, p);
	// return arr;
	// } catch (Exception e) {
	// }
	// try {
	// Game ngame = clone(game);
	// game.move(Direction.DOWN);
	// arr.add("moveup");
	// arr = max(oldGame, ngame, c, arr, p);
	// return arr;
	// } catch (Exception e) {
	// }
	// try {
	// Game ngame = clone(game);
	// game.move(Direction.RIGHT);
	// arr.add("moveup");
	// arr = max(oldGame, ngame, c, arr, p);
	// return arr;
	// } catch (Exception e) {
	// }
	// try {
	// Game ngame = clone(game);
	// game.move(Direction.LEFT);
	// arr.add("moveup");
	// arr = max(oldGame, ngame, c, arr, p);
	// return arr;
	// } catch (Exception e) {
	// }
	// try {
	// game.useLeaderAbility();;
	// sol.add("leaderability");
	// } catch (Exception e) {}
	//
	// try {
	// game.attack(Direction.UP);
	// sol.add("attackup");
	// } catch (Exception e) {}
	// try {
	// game.attack(Direction.DOWN);
	// sol.add("attackdown");
	// } catch (Exception e) {}
	// try {
	// game.attack(Direction.RIGHT);
	// sol.add("attackright");
	// } catch (Exception e) {}
	// try {
	// game.attack(Direction.LEFT);
	// sol.add("attackleft");
	// } catch (Exception e) {}
	// for(int i=0;i<c.getAbilities().size();i++){
	// Ability a=c.getAbilities().get(i);
	// if (a.getCastArea() == AreaOfEffect.DIRECTIONAL) {
	// try {
	// game.castAbility(a);;
	// sol.add("cast"+i);
	// } catch (Exception e) {}
	// }
	// if (a.getCastArea() == AreaOfEffect.SINGLETARGET) {
	// //xyAbilities.add(a);
	// } else {
	// try {
	// game.castAbility(a);;
	// sol.add("cast"+i);
	// } catch (Exception e) {}
	// }
	// }
return null;
}

	public static ArrayList<String> getAvailableActions() {
		ArrayList<String> arr = new ArrayList<String>();
		arr.add("moveup");
		arr.add("moveleft");
		arr.add("moveright");
		arr.add("movedown");
		arr.add("attackup");
		arr.add("attackleft");
		arr.add("attackwright");
		arr.add("attackdown");
//		arr.add("useleaderability");
//		arr.add("ability0");
//		arr.add("ability1");
//		arr.add("ability2");
		return arr;
	}
	
	public static ArrayList<String> minimax(Game oldgame,Game game,Player p,ArrayList<String>arr, int depth){
		ArrayList<String>sol=null;
		System.out.println(i+" | "+depth+" | "+arr+" | "+game.getCurrentChampion().getCurrentActionPoints());
		i++;
		if(depth==0||game.getCurrentChampion().getCurrentActionPoints()==0){
			int x=evaluate(oldgame, game, p);
			arr.add(x+"");
			return arr;
		}
		if(isFriend(p, game.getCurrentChampion())){
			int value=0;
			for(String s:getAvailableActions()){
				switch (s) {
				case "moveup":
					try {
						Game ngame=clone(game);
						ngame.move(Direction.UP);
						ArrayList<String>arr2=(ArrayList<String>) arr.clone();
						arr2.add("moveup");
						ArrayList<String> arr3 =minimax(oldgame, ngame, p, arr2, depth-1);
						int x=Integer.parseInt(arr3.get(arr3.size()-1));
						if(x<value){
							value=x;
							sol=arr3;
						}	
					} catch (Exception e) {}
					break;
				case "movedown":
					try {
						Game ngame=clone(game);
						ngame.move(Direction.DOWN);
						ArrayList<String>arr2=(ArrayList<String>) arr.clone();
						arr2.add("movedown");
						ArrayList<String> arr3 =minimax(oldgame, ngame, p, arr2, depth-1);
						int x=Integer.parseInt(arr3.get(arr3.size()-1));
						if(x<value){
							value=x;
							sol=arr3;
						}	
					} catch (Exception e) {}
					break;
				case "moveleft":
					try {
						Game ngame=clone(game);
						ngame.move(Direction.LEFT);
						ArrayList<String>arr2=(ArrayList<String>) arr.clone();
						arr2.add("moveleft");
						ArrayList<String> arr3 =minimax(oldgame, ngame, p, arr2, depth-1);
						int x=Integer.parseInt(arr3.get(arr3.size()-1));
						if(x<value){
							value=x;
							sol=arr3;
						}	
					} catch (Exception e) {}
					break;
				case "moveright":
					try {
						Game ngame=clone(game);
						ngame.move(Direction.RIGHT);
						ArrayList<String>arr2=(ArrayList<String>) arr.clone();
						arr2.add("movereight");
						ArrayList<String> arr3 =minimax(oldgame, ngame, p, arr2, depth-1);
						int x=Integer.parseInt(arr3.get(arr3.size()-1));
						if(x<value){
							value=x;
							sol=arr3;
						}	
					} catch (Exception e) {}
					break;
				case "attackup":
					try {
						Game ngame=clone(game);
						ngame.attack(Direction.UP);
						ArrayList<String>arr2=(ArrayList<String>) arr.clone();
						arr2.add("attackup");
						ArrayList<String> arr3 =minimax(oldgame, ngame, p, arr2, depth-1);
						int x=Integer.parseInt(arr3.get(arr3.size()-1));
						if(x<value){
							value=x;
							sol=arr3;
						}	
					} catch (Exception e) {}
					break;
				case "attackdown":
					try {
						Game ngame=clone(game);
						ngame.attack(Direction.DOWN);
						ArrayList<String>arr2=(ArrayList<String>) arr.clone();
						arr2.add("attackdown");
						ArrayList<String> arr3 =minimax(oldgame, ngame, p, arr2, depth-1);
						int x=Integer.parseInt(arr3.get(arr3.size()-1));
						if(x<value){
							value=x;
							sol=arr3;
						}	
					} catch (Exception e) {}
					break;
				case "attackleft":
					try {
						Game ngame=clone(game);
						System.out.println(ngame.getCurrentChampion().getCurrentActionPoints());
						ngame.attack(Direction.LEFT);
						System.out.println(ngame.getCurrentChampion().getCurrentActionPoints());
						ArrayList<String>arr2=(ArrayList<String>) arr.clone();
						arr2.add("attackleft");
						ArrayList<String> arr3 =minimax(oldgame, ngame, p, arr2, depth-1);
						int x=Integer.parseInt(arr3.get(arr3.size()-1));
						if(x<value){
							value=x;
							sol=arr3;
						}	
					} catch (Exception e) {}
					break;
				case "attackright":
					try {
						Game ngame=clone(game);
						ngame.attack(Direction.RIGHT);
						ArrayList<String>arr2=(ArrayList<String>) arr.clone();
						arr2.add("attackreight");
						ArrayList<String> arr3 =minimax(oldgame, ngame, p, arr2, depth-1);
						int x=Integer.parseInt(arr3.get(arr3.size()-1));
						if(x<value){
							value=x;
							sol=arr3;
						}	
					} catch (Exception e) {}
					break;
				}
			}
			
			return sol;
		}else{
			return null;
		}
		

	}

}
