package computer;

import java.io.IOException;
import java.util.ArrayList;

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

		//printGame(game);
		game.getFirstPlayer().getTeam().get(0).setCurrentHP(1);
		Game game2 = clone(game);
		//printGame(game2);
	}

	public static void printGame(Game game) {
		// System.out.println(game);
		// System.out.println(game.getFirstPlayer());
		// System.out.println(game.getFirstPlayer().getName());
		//System.out.println(game.getFirstPlayer().getTeam().get(0));
		// System.out.println(game.getSecondPlayer());
		// System.out.println(game.getSecondPlayer().getName());
		//System.out.println(game.getSecondPlayer().getTeam().get(0));
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
			p2.getTeam().add(c2);
		}
	}
}
