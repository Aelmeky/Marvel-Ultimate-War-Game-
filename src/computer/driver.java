package computer;

import java.io.IOException;
import java.util.ArrayList;

import org.omg.CORBA.PUBLIC_MEMBER;

import model.effects.*;
import engine.Game;
import engine.Player;
import model.abilities.Ability;
import model.abilities.AreaOfEffect;
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
				.add(game.getAvailableChampions().get(0));
		game.getFirstPlayer().getTeam()
				.add(game.getAvailableChampions().get(1));
		game.getFirstPlayer().getTeam()
				.add(game.getAvailableChampions().get(2));

		game.getSecondPlayer().getTeam()
				.add(game.getAvailableChampions().get(3));
		game.getSecondPlayer().getTeam()
				.add(game.getAvailableChampions().get(4));
		game.getSecondPlayer().getTeam()
				.add(game.getAvailableChampions().get(5));
		game.placeChampions();
		game.prepareChampionTurns();

		printGame(game);
		Game game2 = clone(game);
		printGame(game2);

	}

	public static void printGame(Game game) {
		// System.out.println(game);
		// System.out.println(game.getFirstPlayer());
		// System.out.println(game.getFirstPlayer().getName());
		// System.out.println(game.getFirstPlayer().getTeam().get(0).getAppliedEffects().get(0).getDuration());
		// System.out.println(game.getSecondPlayer());
		// System.out.println(game.getSecondPlayer().getName());
		// System.out.println(game.getSecondPlayer().getTeam().get(0).getAppliedEffects().get(0));
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (game.getBoard()[i][j] == null) {
					System.out.print(" ");
				}
				if (game.getBoard()[i][j] != null) {
					if (game.getBoard()[i][j] instanceof Champion) {
						System.out.print("C");
					}
					if (game.getBoard()[i][j] instanceof Cover) {
						System.out.print("v");
					}
				}
			}
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
				if (newGame.getBoard()[i][j] instanceof Cover) {
					newGame.getBoard()[i][j] = null;
				}
			}
		}
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (game.getBoard()[i][j] != null) {
					// if(game.getBoard()[i][j] instanceof Champion){
					// System.out.println(((Champion)game.getBoard()[i][j]).getName()+"||"+((Champion)newGame.getBoard()[i][j]).getName());
					// }
					if (game.getBoard()[i][j] instanceof Cover) {
						Cover c = new Cover(i, j);
						c.setCurrentHP(((Cover) game.getBoard()[i][j])
								.getCurrentHP());
						newGame.getBoard()[i][j] = c;
					}
				} else {
					continue;
				}
			}
		}
		return newGame;
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
			c2.setLocation(c.getLocation());
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
		Object[][] n=ngame.getBoard();
		Object[][] o=ogame.getBoard();
		if(ngame.getSecondPlayer().getTeam().size()==0){
			return Integer.MIN_VALUE;
		}
		if(ngame.getFirstPlayer().getTeam().size()==0){
			return Integer.MAX_VALUE;
		}
		int sum = 0;
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (o[i][j] == n[i][j]) {
					continue;
				} else if(o[i][j] instanceof Champion) {
					if (isFriend(me, ((Champion) n[i][j]))) {
						//effect on a friend
						if(n[i][j]==null){
							//a friend got killed
							sum-=2*((Champion) o[i][j]).getMaxHP();
						}else{
							//A friend got damaged
							sum-=((Champion) o[i][j]).getCurrentHP()-((Champion) n[i][j]).getCurrentHP();
						}
					}
					if (!isFriend(me, ((Champion) n[i][j]))) {
						//effect on an enemy
						if(n[i][j]==null){
							//killed an enemy
							sum+=2*((Champion) o[i][j]).getMaxHP();
						}else{
							//damaged an enemy
							sum+=((Champion) o[i][j]).getCurrentHP()-((Champion) n[i][j]).getCurrentHP();
						}
					}
				}
			}
		}
		return sum;
	}

	public static boolean isFriend(Player me, Champion c) {
		for (Champion c2 : me.getTeam()) {
			if (c2 == c) {
				return true;
			}
		}
		return false;
	}

	public static ArrayList<String> getAvailableActions(Game game,Champion c){
		ArrayList<String>sol=new ArrayList<String>();
		try {
			game.move(Direction.UP);
			sol.add("moveup");
		} catch (Exception e) {}
		try {
			game.move(Direction.DOWN);
			sol.add("movedown");
		} catch (Exception e) {}
		try {
			game.move(Direction.RIGHT);
			sol.add("moveright");
		} catch (Exception e) {}
		try {
			game.move(Direction.LEFT);
			sol.add("moveleft");
		} catch (Exception e) {}
		try {
			game.useLeaderAbility();;
			sol.add("leaderability");
		} catch (Exception e) {}
		
		try {
			game.attack(Direction.UP);
			sol.add("attackup");
		} catch (Exception e) {}
		try {
			game.attack(Direction.DOWN);
			sol.add("attackdown");
		} catch (Exception e) {}
		try {
			game.attack(Direction.RIGHT);
			sol.add("attackright");
		} catch (Exception e) {}
		try {
			game.attack(Direction.LEFT);
			sol.add("attackleft");
		} catch (Exception e) {}
		for(int i=0;i<c.getAbilities().size();i++){
			Ability a=c.getAbilities().get(i);
			if (a.getCastArea() == AreaOfEffect.DIRECTIONAL) {
				//directionalAbilities.add(a);
			}
			if (a.getCastArea() == AreaOfEffect.SINGLETARGET) {
				//xyAbilities.add(a);
			} else {
				try {
					game.castAbility(a);;
					sol.add("cast"+i);
				} catch (Exception e) {}
			}
		}
		return sol;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
