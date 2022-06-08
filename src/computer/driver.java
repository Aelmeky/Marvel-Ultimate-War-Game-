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
	static int i = 1;
	static Player p1;
	public static void main(String[] args) {
		p1 = new Player("Ahmed");
		Player p2 = new Player("meky");
		Game game = new Game(p1, p2);
		ArrayList<Champion> team1 = new ArrayList<Champion>();
		try {
			Game.loadAbilities("./Abilities.csv");
			Game.loadChampions("./Champions.csv");
		} catch (IOException e) {
		}
		game.getFirstPlayer().getTeam().add(game.getAvailableChampions().get(1));
		game.getFirstPlayer().getTeam().add(game.getAvailableChampions().get(6));
		game.getFirstPlayer().getTeam().add(game.getAvailableChampions().get(11));

		game.getSecondPlayer().getTeam().add(game.getAvailableChampions().get(0));
		game.getSecondPlayer().getTeam().add(game.getAvailableChampions().get(5));
		game.getSecondPlayer().getTeam().add(game.getAvailableChampions().get(10));
		p2.setLeader(game.getAvailableChampions().get(5));
		p1.setLeader(game.getAvailableChampions().get(1));
		game.placeChampions();
		game.prepareChampionTurns();
		game.endTurn();
		game.endTurn();
		game.endTurn();
		game.endTurn();
		//((Champion) game.getBoard()[0][3]).setCurrentHP(200);
		Game game2 = clone(game);
		ArrayList<String> arr = new ArrayList<String>();
		arr = minimax(game, game2, game2.getSecondPlayer(), arr,8);
		System.out.println("------------");
		System.out.println(arr);
		printGame(game);
		printGame(game2);
	}

	public static void printGame(Game game) {
		for (int i = 0; i < 5; i++) {
			System.out.print("|");
			for (int j = 0; j < 5; j++) {
				if (game.getBoard()[4 - i][j] == null) {
					System.out.print(" ");
				}
				if (game.getBoard()[4 - i][j] != null) {
					if (game.getBoard()[4 - i][j] instanceof Champion) {
						System.out.print("C");

					}
					if (game.getBoard()[4 - i][j] instanceof Cover) {
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
				if (newGame.getBoard()[4 - i][j] != null) {
					newGame.getBoard()[4 - i][j] = null;
				}
			}
		}
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (game.getBoard()[4 - i][j] != null) {
					if (game.getBoard()[4 - i][j] instanceof Champion) {
						Champion c = getChampionByName(newGame, ((Champion) game.getBoard()[4 - i][j]).getName());
						c.setLocation(new Point(((Champion) game.getBoard()[4 - i][j]).getLocation().x,
								((Champion) game.getBoard()[4 - i][j]).getLocation().y));
						newGame.getBoard()[4 - i][j] = c;
					}
					if (game.getBoard()[4 - i][j] instanceof Cover) {
						Cover c = new Cover(i, j);
						c.setCurrentHP(((Cover) game.getBoard()[4 - i][j]).getCurrentHP());
						newGame.getBoard()[4 - c.getLocation().x][c.getLocation().y] = c;
					}
				}
			}
		}
		while(!((Champion)newGame.getTurnOrder().peekMin()).getName().equals(((Champion)game.getTurnOrder().peekMin()).getName())){
			newGame.getTurnOrder().remove();
		}
		return newGame;
	}

	public static Champion getChampionByName(Game game, String name) {
		for (Champion c : game.getFirstPlayer().getTeam()) {
			if (c.getName().equals(name)) {
				return c;
			}
		}
		for (Champion c : game.getSecondPlayer().getTeam()) {
			if (c.getName().equals(name)) {
				return c;
			}
		}
		return null;
	}

	public static void cloneChampions(Player p1, Player p2) {
		for (Champion c : p1.getTeam()) {
			Champion c2 = null;
			if (c instanceof Hero) {
				c2 = new Hero(c.getName(), c.getMaxHP(), c.getMana(), c.getMaxActionPointsPerTurn(), c.getSpeed(),
						c.getAttackRange(), c.getAttackDamage());
			}
			if (c instanceof Villain) {
				c2 = new Villain(c.getName(), c.getMaxHP(), c.getMana(), c.getMaxActionPointsPerTurn(), c.getSpeed(),
						c.getAttackRange(), c.getAttackDamage());
			}
			if (c instanceof AntiHero) {
				c2 = new AntiHero(c.getName(), c.getMaxHP(), c.getMana(), c.getMaxActionPointsPerTurn(), c.getSpeed(),
						c.getAttackRange(), c.getAttackDamage());
			}
			c2.setCurrentHP(c.getCurrentHP());
			c2.setCurrentActionPoints(c.getCurrentActionPoints());
			c2.setCondition(c.getCondition());
			c2.setLocation(new Point(c.getLocation().x, c.getLocation().y));
			c2.setMana(c.getMana());
			for (Ability a : c.getAbilities()) {
				Ability a2 = null;
				if (a instanceof CrowdControlAbility) {
					Effect f = null;
					try {
						f = (Effect) ((CrowdControlAbility) a).getEffect().clone();
					} catch (CloneNotSupportedException e) {
					}
					a2 = new CrowdControlAbility(a.getName(), a.getManaCost(), a.getBaseCooldown(), a.getCastRange(),
							a.getCastArea(), a.getRequiredActionPoints(), f);
					c2.getAbilities().add(a2);
				}
				if (a instanceof HealingAbility) {
					a2 = new HealingAbility(a.getName(), a.getManaCost(), a.getBaseCooldown(), a.getCastRange(),
							a.getCastArea(), a.getRequiredActionPoints(), ((HealingAbility) a).getHealAmount());
					c2.getAbilities().add(a2);
				}
				if (a instanceof DamagingAbility) {
					a2 = new DamagingAbility(a.getName(), a.getManaCost(), a.getBaseCooldown(), a.getCastRange(),
							a.getCastArea(), a.getRequiredActionPoints(), ((DamagingAbility) a).getDamageAmount());
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
			if(p1.getLeader().getName().equals(c2.getName())) {
				p2.setLeader(c2);
			}
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
		ArrayList<String> handel = new ArrayList<String>();
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (n[i][j] instanceof Champion) {
					handel.add(((Champion) n[i][j]).getName());
					// System.out.println("in "+((Champion) n[i][j]).getName());
					if (isFriend(me, ((Champion) n[i][j]))
							&& !ngame.getCurrentChampion().getName().equals(((Champion) n[i][j]).getName())) {
						// A friend got damaged
						sum -= ((Champion) o[i][j]).getCurrentHP() - ((Champion) n[i][j]).getCurrentHP();
					}
					if (!isFriend(me, ((Champion) n[i][j]))) {
						// effect on an enemy
						// damaged an enemy
						sum += (((Champion) o[i][j]).getCurrentHP() - ((Champion) n[i][j]).getCurrentHP())*1.5;
					}
				}

			}
		}
		//prioritizing down movement
		if(ngame.getCurrentChampion().getLocation().x<ogame.getCurrentChampion().getLocation().x) {
			sum+=5;
		}
		//handling covers
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (o[i][j] instanceof Cover) {
					int x=0;
					try {
						x=((Cover) n[i][j]).getCurrentHP();
					} catch (Exception e) {}
						sum += (((Cover) o[i][j]).getCurrentHP() -x)*0.5;
				}
			}
		}
		//Handling dead champions
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (o[i][j] instanceof Champion) {
					Champion c = (Champion) o[i][j];
					if (!handel.contains(c.getName())) {
						if (isFriend(me, c)) {
							sum -= c.getMaxHP();
						} else {
							sum += c.getMaxHP();
						}
					}
				}
			}
		}
		//using Leader ability
		if(ngame.isSecondLeaderAbilityUsed()&&!ogame.isSecondLeaderAbilityUsed()) {
			if(sum<1000) {
				sum-=2000;
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

	public static ArrayList<String> getAvailableActions() {
		ArrayList<String> arr = new ArrayList<String>();
		arr.add("moveup");
		arr.add("moveleft");
		arr.add("moveright");
		arr.add("movedown");
		arr.add("attackup");
		arr.add("attackleft");
		arr.add("attackright");
		arr.add("attackdown");
		arr.add("useleaderability");
		// arr.add("ability0");
		// arr.add("ability1");
		// arr.add("ability2");
		return arr;
	}

	public static ArrayList<String> minimax(Game oldgame, Game game, Player p, ArrayList<String> arr, int depth) {
		ArrayList<String> sol = null;
		System.out.println(i + " | " + depth + " | " + arr);
		i++;
		if (depth == 0 || game.getCurrentChampion().getCurrentActionPoints() == 0) {
			int x = evaluate(oldgame, game, p);
			arr.add(x + "");
			return arr;
		}
		if (isFriend(p, game.getCurrentChampion())) {
			int value = Integer.MIN_VALUE;
			for (String s:getAvailableActions()) {
				System.out.println(s+" "+i+" "+getAvailableActions().size());
				switch (s) {
				case "moveup":
					if (arr==null||arr.size()==0||(arr != null && arr.size() != 0 && !arr.get(arr.size() - 1).equals("movedown"))) {
						try {
							Game ngame = clone(game);
							ngame.move(Direction.UP);
							ArrayList<String> arr2 = (ArrayList<String>) arr.clone();
							arr2.add("moveup");
							ArrayList<String> arr3 = minimax(oldgame, ngame, p, arr2, depth - 1);
							int x = Integer.parseInt(arr3.get(arr3.size() - 1));
							if (x > value) {
								value = x;
								sol = arr3;
							}
						} catch (Exception e) {}
					}
					break;
				case "movedown":
					if (arr==null||arr.size()==0||(arr != null && arr.size() != 0 && !arr.get(arr.size() - 1).equals("moveup"))) {
						try {
							Game ngame = clone(game);
							ngame.move(Direction.DOWN);
							ArrayList<String> arr2 = (ArrayList<String>) arr.clone();
							arr2.add("movedown");
							ArrayList<String> arr3 = minimax(oldgame, ngame, p, arr2, depth - 1);
							int x = Integer.parseInt(arr3.get(arr3.size() - 1));
							if (x > value) {
								value = x;
								sol = arr3;
							}
						} catch (Exception e) {}
					}
					break;
				case "moveleft":
					if (arr==null||arr.size()==0||(arr != null && arr.size() != 0 && !arr.get(arr.size() - 1).equals("moveright"))) {
						try {
							Game ngame = clone(game);
							ngame.move(Direction.LEFT);
							ArrayList<String> arr2 = (ArrayList<String>) arr.clone();
							arr2.add("moveleft");
							ArrayList<String> arr3 = minimax(oldgame, ngame, p, arr2, depth - 1);
							int x = Integer.parseInt(arr3.get(arr3.size() - 1));
							if (x > value) {
								value = x;
								sol = arr3;
							}
						} catch (Exception e) {}
					}
					break;
				case "moveright":
					if (arr==null||arr.size()==0||(arr != null &&arr.size()!=0&& !arr.get(arr.size() - 1).equals("moveleft"))) {
						try {
							Game ngame = clone(game);
							ngame.move(Direction.RIGHT);
							ArrayList<String> arr2 = (ArrayList<String>) arr.clone();
							arr2.add("moveright");
							ArrayList<String> arr3 = minimax(oldgame, ngame, p, arr2, depth - 1);
							int x = Integer.parseInt(arr3.get(arr3.size() - 1));
							if (x > value) {
								value = x;
								sol = arr3;
							}
						} catch (Exception e) {}
					}
					break;
				case "attackup":
					try {
						Game ngame = clone(game);
						ngame.attack(Direction.UP);
						ArrayList<String> arr2 = (ArrayList<String>) arr.clone();
						arr2.add("attackup");
						ArrayList<String> arr3 = minimax(oldgame, ngame, p, arr2, depth - 1);
						int x = Integer.parseInt(arr3.get(arr3.size() - 1));
						if (x > value) {
							value = x;
							sol = arr3;
						}
					} catch (Exception e) {}
					break;
				case "attackdown":
					try {
						Game ngame = clone(game);
						ngame.attack(Direction.DOWN);
						ArrayList<String> arr2 = (ArrayList<String>) arr.clone();
						arr2.add("attackdown");
						ArrayList<String> arr3 = minimax(oldgame, ngame, p, arr2, depth - 1);
						int x = Integer.parseInt(arr3.get(arr3.size() - 1));
						if (x > value) {
							value = x;
							sol = arr3;
						}
					} catch (Exception e) {}
					break;
				case "attackleft":
					try {
						Game ngame = clone(game);
						ngame.attack(Direction.LEFT);
						ArrayList<String> arr2 = (ArrayList<String>) arr.clone();
						arr2.add("attackleft");
						ArrayList<String> arr3 = minimax(oldgame, ngame, p, arr2, depth - 1);
						int x = Integer.parseInt(arr3.get(arr3.size() - 1));
						if (x > value) {
							value = x;
							sol = arr3;
						}
					} catch (Exception e) {
					}
					break;
				case "attackright":
					try {
						Game ngame = clone(game);
						ngame.attack(Direction.RIGHT);
						ArrayList<String> arr2 = (ArrayList<String>) arr.clone();
						arr2.add("attackright");
						ArrayList<String> arr3 = minimax(oldgame, ngame, p, arr2, depth - 1);
						int x = Integer.parseInt(arr3.get(arr3.size() - 1));
						if (x > value) {
							value = x;
							sol = arr3;
						}
					} catch (Exception e) {
					}
					break;
				case "useleaderability":
					if(!arr.contains("useleaderability")) {
						try {
							Game ngame = clone(game);
							ngame.useLeaderAbility();
							System.out.println("arr "+i+" "+arr);
							ArrayList<String> arr2 = (ArrayList<String>)arr.clone();
							arr2.add("useleaderability");
							ArrayList<String> arr3 = minimax(oldgame, ngame, p, arr2, depth - 1);
							int x = Integer.parseInt(arr3.get(arr3.size() - 1));
							if (x > value) {
								value = x;
								sol = arr3;
							}
						} catch (Exception e) {
							System.out.println(e);
						}
					}
					break;
				}
			}

			return sol;
		} else {
			return null;
		}

	}

}
