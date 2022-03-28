package engine;

import java.util.*;
import java.awt.Point;
import java.io.*;

import model.abilities.*;
import model.world.*;
import model.effects.*;

public class Game {
	private Player firstPlayer;
	private Player secondPlayer;
	private boolean firstLeaderAbilityUsed;
	private boolean secondLeaderAbilityUsed;
	Object [][] board = new Object[5][5];
	private PriorityQueue turnOrder;
	private static ArrayList<Champion> availableChampions;
	private static ArrayList<Ability> availableAbilities;
	final private static int BOARDHEIGHT = 5;
	final private static int BOARDWIDTH = 5;
	public Game(Player first, Player second) throws IOException{
		this.firstPlayer = first;
		this.secondPlayer = second;
		availableChampions = new ArrayList<Champion>();
		availableAbilities = new ArrayList<Ability>();
		
		this.turnOrder = new PriorityQueue(6);
		placeChampions();
		placeCovers();
	}
	public PriorityQueue getTurnOrder() {
		return turnOrder;
	}
	public static int getBoardheight() {
		return BOARDHEIGHT;
	}
	public static int getBoardwidth() {
		return BOARDWIDTH;
	}
	public Player getFirstPlayer() {
		return firstPlayer;
	}
	public Player getSecondPlayer() {
		return secondPlayer;
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
		
		for(int i=1 ;i<BOARDWIDTH-1 && champ<firstPlayer.getTeam().size();i++) {
			firstPlayer.getTeam().get(champ).setLocation(new Point(0,i));
			board[0][i] = firstPlayer.getTeam().get(champ);

			champ++;
		}
		champ =0;
		for(int i=1 ;i<BOARDWIDTH-1 && champ<secondPlayer.getTeam().size();i++) {
			secondPlayer.getTeam().get(champ).setLocation(new Point(BOARDHEIGHT-1,i));
			board[BOARDHEIGHT-1][i] = secondPlayer.getTeam().get(champ);
			champ++;
		}
		
	}
	private void placeCovers() {
		for(int i=0;i<5;i++) {
			int v = (int)(Math.random()*3+1);
			int h = (int)(Math.random()*5);
			
			if(board[v][h] != null || (v==0 &&(h ==0 || h==4))||(v==4)&&(h==0||h==4)) i--;
			else board[v][h] = new Cover(h,v);
		}	
	}
	public void loadAbilities(String filePath)throws IOException{
		
		BufferedReader br= new BufferedReader(new FileReader(filePath));
		this.availableAbilities = new ArrayList<Ability>();
		String line = br.readLine();
		while(line != null) {
			String[] abilityInfo = line.split(",");
			if(abilityInfo[0].equals("DMG")) {
				//availableAbilities.add(new DamagingAbility(abilityInfo[1],Integer.parseInt(abilityInfo[2]), Integer.parseInt(abilityInfo[3]), Integer.parseInt(abilityInfo[4]), AreaOfEffect.valueOf(abilityInfo[5]),Integer.parseInt(abilityInfo[6]),Integer.parseInt(abilityInfo[7])));
				availableAbilities.add(new DamagingAbility(abilityInfo[1],Integer.parseInt(abilityInfo[2]), Integer.parseInt(abilityInfo[4]), Integer.parseInt(abilityInfo[3]), AreaOfEffect.valueOf(abilityInfo[5]),Integer.parseInt(abilityInfo[6]),Integer.parseInt(abilityInfo[7])));

			}
			else if(abilityInfo[0].equals("HEL")) {
				//availableAbilities.add(new HealingAbility(abilityInfo[1],Integer.parseInt(abilityInfo[2]), Integer.parseInt(abilityInfo[3]), Integer.parseInt(abilityInfo[4]), AreaOfEffect.valueOf(abilityInfo[5]),Integer.parseInt(abilityInfo[6]),Integer.parseInt(abilityInfo[7])));
				availableAbilities.add(new HealingAbility(abilityInfo[1],Integer.parseInt(abilityInfo[2]), Integer.parseInt(abilityInfo[4]), Integer.parseInt(abilityInfo[3]), AreaOfEffect.valueOf(abilityInfo[5]),Integer.parseInt(abilityInfo[6]),Integer.parseInt(abilityInfo[7])));

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
				
				//availableAbilities.add(new CrowdControlAbility(abilityInfo[1],Integer.parseInt(abilityInfo[2]), Integer.parseInt(abilityInfo[3]), Integer.parseInt(abilityInfo[4]), AreaOfEffect.valueOf(abilityInfo[5]),Integer.parseInt(abilityInfo[6]),effect));
				availableAbilities.add(new CrowdControlAbility(abilityInfo[1],Integer.parseInt(abilityInfo[2]), Integer.parseInt(abilityInfo[4]), Integer.parseInt(abilityInfo[3]), AreaOfEffect.valueOf(abilityInfo[5]),Integer.parseInt(abilityInfo[6]),effect));

			}
			line = br.readLine();
			
		}
	}
	public void loadChampions(String filePath)throws IOException{
		
		BufferedReader br= new BufferedReader(new FileReader(filePath));
		this.availableChampions = new ArrayList<Champion>();
		String line = br.readLine();
		while(line != null) {
			String[] championInfo = line.split(",");
			Champion currChamp = null;
			if(championInfo[0].equals("A")) {
				currChamp = new AntiHero(championInfo[1], Integer.parseInt(championInfo[2]), Integer.parseInt(championInfo[3]), Integer.parseInt(championInfo[4]), Integer.parseInt(championInfo[5]), Integer.parseInt(championInfo[6]), Integer.parseInt(championInfo[7]));
			}
			else if(championInfo[0].equals("H")) {
				currChamp = new Hero(championInfo[1], Integer.parseInt(championInfo[2]), Integer.parseInt(championInfo[3]), Integer.parseInt(championInfo[4]), Integer.parseInt(championInfo[5]), Integer.parseInt(championInfo[6]), Integer.parseInt(championInfo[7]));
			}
			else if(championInfo[0].equals("V")) {
				currChamp = new Villain(championInfo[1], Integer.parseInt(championInfo[2]), Integer.parseInt(championInfo[3]), Integer.parseInt(championInfo[4]), Integer.parseInt(championInfo[5]), Integer.parseInt(championInfo[6]), Integer.parseInt(championInfo[7]));
				
			}
			availableChampions.add(currChamp);
			currChamp.getAbilities().add(this.getAbilityByName(championInfo[8]));
			currChamp.getAbilities().add(this.getAbilityByName(championInfo[9]));
			currChamp.getAbilities().add(this.getAbilityByName(championInfo[10]));
			
			
			line = br.readLine();
		}

	}
	public Ability getAbilityByName(String name) {
		
		for(int i=0;i<this.availableAbilities.size();i++) {
			if(this.availableAbilities.get(i).getName().equals(name)) {
				
				return this.availableAbilities.get(i);
			}
		}
		return null;
	}
}
