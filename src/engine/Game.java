package engine;

import java.util.*;
import java.io.*;

import model.abilities.*;
import model.world.*;
import model.effects.*;

public class Game {
	private Player firstPlayer;
	private Player secoundPlayer;
	private boolean firstLeaderAbilityUsed;
	private boolean secondLeaderAbilityUsed;
	Object [][] board = new Object[5][5];
	private PriorityQueue turnOrder;
	private static ArrayList<Champion> availableChampions = new ArrayList<Champion>();
	private static ArrayList<Ability> availableAbilities = new ArrayList<Ability>();
	final static int BOARDHEIGHT = 5;
	final static int BOARDWIDTH = 5;
	public Game(Player first, Player second){
		this.firstPlayer = first;
		this.secoundPlayer = second;
	}
	public PriorityQueue getTurnOrder() {
		return turnOrder;
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
	public static ArrayList<Ability> getAvailableAbilities() {
		return Game.availableAbilities;
	}
	private void placeChampions() {
		int champ =0;
		for(int i=1 ;i<BOARDWIDTH-1;i++) {
			
			board[0][i] = firstPlayer.getTeam().get(champ);
			board[BOARDHEIGHT-1][i] = secoundPlayer.getTeam().get(champ);
			champ++;
		}
		
	}
	private void placeCovers() {
		for(int i=0;i<5;i++) {
			int vertical = (int)(Math.random()*4+1);
			int horizontal = (int)(Math.random()*4+1);
			if(board[vertical][horizontal] != null) {
				i--;
				continue;
			}
			board[vertical][horizontal] = new Cover(horizontal,vertical);
		}	
	}
	public static void loadAbilities(String filePath)throws IOException{
		BufferedReader br= new BufferedReader(new FileReader(filePath));
		String line = br.readLine();
		while(line != null) {
			String[] abilityInfo = line.split(",");
			if(abilityInfo[0].equals("DMG")) {
				availableAbilities.add(new DamagingAbility(abilityInfo[1],Integer.parseInt(abilityInfo[2]), Integer.parseInt(abilityInfo[3]), Integer.parseInt(abilityInfo[4]), AreaOfEffect.valueOf(abilityInfo[5]),Integer.parseInt(abilityInfo[6]),Integer.parseInt(abilityInfo[7])));
			}
			else if(abilityInfo[0].equals("HEL")) {
				availableAbilities.add(new HealingAbility(abilityInfo[1],Integer.parseInt(abilityInfo[2]), Integer.parseInt(abilityInfo[3]), Integer.parseInt(abilityInfo[4]), AreaOfEffect.valueOf(abilityInfo[5]),Integer.parseInt(abilityInfo[6]),Integer.parseInt(abilityInfo[7])));
			}
			else if(abilityInfo[0].equals("CC")) {
				Effect effect=null;
				switch(abilityInfo[7]) {
				case "Disarm": effect = new Disarm(Integer.parseInt(abilityInfo[8]));break;
				case "Dodge": effect = new Dodge(Integer.parseInt(abilityInfo[8]));break;
				case "Embrace": effect = new Embrace(Integer.parseInt(abilityInfo[8]));break;
				case "PowerUp": effect = new PowerUp(Integer.parseInt(abilityInfo[8]));break;
				case "Root": effect = new Root(Integer.parseInt(abilityInfo[8]));break;
				case "Shield": effect = new Shield(Integer.parseInt(abilityInfo[8]));break;
				case "Shock": effect = new Shock(Integer.parseInt(abilityInfo[8]));break;
				case "Silence": effect = new Silence(Integer.parseInt(abilityInfo[8]));break;
				case "SpeedUp": effect = new SpeedUp(Integer.parseInt(abilityInfo[8]));break;
				case "Stun": effect = new Stun(Integer.parseInt(abilityInfo[8]));break;
				}
				
				availableAbilities.add(new CrowdControlAbility(abilityInfo[1],Integer.parseInt(abilityInfo[2]), Integer.parseInt(abilityInfo[3]), Integer.parseInt(abilityInfo[4]), AreaOfEffect.valueOf(abilityInfo[5]),Integer.parseInt(abilityInfo[6]),effect));
				
			}
			line = br.readLine();
			
		}
	}
	public static void loadChampions(String filePath)throws IOException{
		BufferedReader br= new BufferedReader(new FileReader(filePath));
		String line = br.readLine();
		while(line != null) {
			String[] championInfo = line.split(",");
			if(championInfo[0].equals("A")) {
				availableChampions.add(new AntiHero(championInfo[1], Integer.parseInt(championInfo[2]), Integer.parseInt(championInfo[3]), Integer.parseInt(championInfo[4]), Integer.parseInt(championInfo[5]), Integer.parseInt(championInfo[6]), Integer.parseInt(championInfo[7])));
			}
			else if(championInfo[0].equals("H")) {
				availableChampions.add(new Hero(championInfo[1], Integer.parseInt(championInfo[2]), Integer.parseInt(championInfo[3]), Integer.parseInt(championInfo[4]), Integer.parseInt(championInfo[5]), Integer.parseInt(championInfo[6]), Integer.parseInt(championInfo[7])));
			}
			else if(championInfo[0].equals("V")) {
				availableChampions.add(new Villain(championInfo[1], Integer.parseInt(championInfo[2]), Integer.parseInt(championInfo[3]), Integer.parseInt(championInfo[4]), Integer.parseInt(championInfo[5]), Integer.parseInt(championInfo[6]), Integer.parseInt(championInfo[7])));
			}
			line = br.readLine();
		}
	}
}
