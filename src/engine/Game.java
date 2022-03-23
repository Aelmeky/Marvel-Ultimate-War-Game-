package engine;

import java.util.ArrayList;

public class Game {
	private Player firstPlayer;
	private Player secoundPlayer;
	private boolean firstLeaderAbilityUsed;
	private boolean secondLeaderAbilityUsed;
	Object [][] board;
	static ArrayList<Champion> availableChampions = new ArrayList<Champion>();
	static ArrayList<Ability> availableAbilities = new ArrayList<Ability>();
	final static int BOARDHEIGHT;
	final static int BOARDWIDTH;
	public Game(Player first, Player second){
		this.firstPlayer = first;
		this.secoundPlayer = second;
	}
	public Player getFirstPlayer() {
		return firstPlayer;
	}
	public Player getSecoundPlayer() {
		return secoundPlayer;
	}
	public boolean isFirstLeaderAbilityUsed() {
		return firstLeaderAbilityUsed;
	}
	public boolean isSecondLeaderAbilityUsed() {
		return secondLeaderAbilityUsed;
	}
	public Object[][] getBoard() {
		return board;
	}
	public static ArrayList<Champion> getAvailableChampions() {
		return availableChampions;
	}
	public static int getBOARDHEIGHT() {
		return BOARDHEIGHT;
	}
	public static int getBOARDWIDTH() {
		return BOARDWIDTH;
	}
	public static void setAvailableAbilities(ArrayList<Ability> availableAbilities) {
		Game.availableAbilities = availableAbilities;
	}
	private void placeChampions() {
		
	}
	

}
