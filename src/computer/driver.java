package computer;

import java.io.IOException;
import java.util.ArrayList;

import engine.Game;
import engine.Player;
import model.world.Champion;

public class driver {
	public static void main(String[] args) {
		Player p1=new Player("Ahmed");
		Player p2=new Player("meky");
		Game game=new Game(p1, p2);
		ArrayList<Champion>team1=new ArrayList<Champion>();
		try {
			Game.loadAbilities("./Abilities.csv");
			Game.loadChampions("./Champions.csv");
		} catch (IOException e) {
		}
		game.getFirstPlayer().getTeam().add(game.getAvailableChampions().get(0));
		game.getFirstPlayer().getTeam().add(game.getAvailableChampions().get(1));
		game.getFirstPlayer().getTeam().add(game.getAvailableChampions().get(2));
		
		game.getSecondPlayer().getTeam().add(game.getAvailableChampions().get(3));
		game.getSecondPlayer().getTeam().add(game.getAvailableChampions().get(4));
		game.getSecondPlayer().getTeam().add(game.getAvailableChampions().get(5));
		
		printGame(game);
		Game game2=clone(game);
		printGame(game2);
	}
	public static void printGame(Game game){
		System.out.println(game);
		System.out.println(game.getFirstPlayer());
		System.out.println(game.getFirstPlayer().getName());
		System.out.println(game.getFirstPlayer().getTeam());
		System.out.println(game.getSecondPlayer());
		System.out.println(game.getSecondPlayer().getName());
		System.out.println(game.getSecondPlayer().getTeam());
		System.out.println("-------------");
	}
	public static Game clone(Game game) {
		String n1=game.getFirstPlayer().getName();
		String n2=game.getSecondPlayer().getName();
		Player p1=new Player(n1);
		Player p2=new Player (n2);
		Game newGame=new Game(p1,p2);
		return newGame;
	}
}
