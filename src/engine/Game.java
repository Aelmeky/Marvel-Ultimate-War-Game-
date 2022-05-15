package engine;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import exceptions.*;
import model.abilities.Ability;
import model.abilities.AreaOfEffect;
import model.abilities.CrowdControlAbility;
import model.abilities.DamagingAbility;
import model.abilities.HealingAbility;
import model.effects.Disarm;
import model.effects.Dodge;
import model.effects.Effect;
import model.effects.Embrace;
import model.effects.PowerUp;
import model.effects.Root;
import model.effects.Shield;
import model.effects.Shock;
import model.effects.Silence;
import model.effects.SpeedUp;
import model.effects.Stun;
import model.world.AntiHero;
import model.world.Champion;
import model.world.Condition;
import model.world.Cover;
import model.world.Direction;
import model.world.Hero;
import model.world.Villain;

public class Game {
	private static ArrayList<Champion> availableChampions;
	private static ArrayList<Ability> availableAbilities;
	private Player firstPlayer;
	private Player secondPlayer;
	private Object[][] board;
	private PriorityQueue turnOrder;
	private boolean firstLeaderAbilityUsed;
	private boolean secondLeaderAbilityUsed;
	private final static int BOARDWIDTH = 5;
	private final static int BOARDHEIGHT = 5;

	public Game(Player first, Player second) {
		firstPlayer = first;

		secondPlayer = second;
		availableChampions = new ArrayList<Champion>();
		availableAbilities = new ArrayList<Ability>();
		board = new Object[BOARDWIDTH][BOARDHEIGHT];
		turnOrder = new PriorityQueue(6);
		placeChampions();
		placeCovers();
		prepareChampionTurns();
	}

	public static void loadAbilities(String filePath) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = br.readLine();
		while (line != null) {
			String[] content = line.split(",");
			Ability a = null;
			AreaOfEffect ar = null;
			switch (content[5]) {
			case "SINGLETARGET":
				ar = AreaOfEffect.SINGLETARGET;
				break;
			case "TEAMTARGET":
				ar = AreaOfEffect.TEAMTARGET;
				break;
			case "SURROUND":
				ar = AreaOfEffect.SURROUND;
				break;
			case "DIRECTIONAL":
				ar = AreaOfEffect.DIRECTIONAL;
				break;
			case "SELFTARGET":
				ar = AreaOfEffect.SELFTARGET;
				break;

			}
			Effect e = null;
			if (content[0].equals("CC")) {
				switch (content[7]) {
				case "Disarm":
					e = new Disarm(Integer.parseInt(content[8]));
					break;
				case "Dodge":
					e = new Dodge(Integer.parseInt(content[8]));
					break;
				case "Embrace":
					e = new Embrace(Integer.parseInt(content[8]));
					break;
				case "PowerUp":
					e = new PowerUp(Integer.parseInt(content[8]));
					break;
				case "Root":
					e = new Root(Integer.parseInt(content[8]));
					break;
				case "Shield":
					e = new Shield(Integer.parseInt(content[8]));
					break;
				case "Shock":
					e = new Shock(Integer.parseInt(content[8]));
					break;
				case "Silence":
					e = new Silence(Integer.parseInt(content[8]));
					break;
				case "SpeedUp":
					e = new SpeedUp(Integer.parseInt(content[8]));
					break;
				case "Stun":
					e = new Stun(Integer.parseInt(content[8]));
					break;
				}
			}
			switch (content[0]) {
			case "CC":
				a = new CrowdControlAbility(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[4]),
						Integer.parseInt(content[3]), ar, Integer.parseInt(content[6]), e);
				break;
			case "DMG":
				a = new DamagingAbility(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[4]),
						Integer.parseInt(content[3]), ar, Integer.parseInt(content[6]), Integer.parseInt(content[7]));
				break;
			case "HEL":
				a = new HealingAbility(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[4]),
						Integer.parseInt(content[3]), ar, Integer.parseInt(content[6]), Integer.parseInt(content[7]));
				break;
			}
			availableAbilities.add(a);
			line = br.readLine();
		}
		br.close();
	}

	public static void loadChampions(String filePath) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = br.readLine();
		while (line != null) {
			String[] content = line.split(",");
			Champion c = null;
			switch (content[0]) {
			case "A":
				c = new AntiHero(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[3]),
						Integer.parseInt(content[4]), Integer.parseInt(content[5]), Integer.parseInt(content[6]),
						Integer.parseInt(content[7]));
				break;

			case "H":
				c = new Hero(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[3]),
						Integer.parseInt(content[4]), Integer.parseInt(content[5]), Integer.parseInt(content[6]),
						Integer.parseInt(content[7]));
				break;
			case "V":
				c = new Villain(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[3]),
						Integer.parseInt(content[4]), Integer.parseInt(content[5]), Integer.parseInt(content[6]),
						Integer.parseInt(content[7]));
				break;
			}

			c.getAbilities().add(findAbilityByName(content[8]));
			c.getAbilities().add(findAbilityByName(content[9]));
			c.getAbilities().add(findAbilityByName(content[10]));
			availableChampions.add(c);
			line = br.readLine();
		}
		br.close();
	}

	private static Ability findAbilityByName(String name) {
		for (Ability a : availableAbilities) {
			if (a.getName().equals(name))
				return a;
		}
		return null;
	}

	public void placeCovers() {
		int i = 0;
		while (i < 5) {
			int x = ((int) (Math.random() * (BOARDWIDTH - 2))) + 1;
			int y = (int) (Math.random() * BOARDHEIGHT);

			if (board[x][y] == null) {
				board[x][y] = new Cover(x, y);
				i++;
			}
		}

	}

	public void placeChampions() {
		int i = 1;
		for (Champion c : firstPlayer.getTeam()) {
			board[0][i] = c;
			c.setLocation(new Point(0, i));
			i++;
		}
		i = 1;
		for (Champion c : secondPlayer.getTeam()) {
			board[BOARDHEIGHT - 1][i] = c;
			c.setLocation(new Point(BOARDHEIGHT - 1, i));
			i++;
		}
	
	}

	public static ArrayList<Champion> getAvailableChampions() {
		return availableChampions;
	}

	public static ArrayList<Ability> getAvailableAbilities() {
		return availableAbilities;
	}

	public Player getFirstPlayer() {
		return firstPlayer;
	}

	public Player getSecondPlayer() {
		return secondPlayer;
	}

	public Object[][] getBoard() {
		return board;
	}

	public PriorityQueue getTurnOrder() {
		return turnOrder;
	}

	public boolean isFirstLeaderAbilityUsed() {
		return firstLeaderAbilityUsed;
	}

	public boolean isSecondLeaderAbilityUsed() {
		return secondLeaderAbilityUsed;
	}

	public static int getBoardwidth() {
		return BOARDWIDTH;
	}

	public static int getBoardheight() {
		return BOARDHEIGHT;
	}
	public void endTurn() {
		// consider the possibility that shield effect may not be removes here at all
		turnOrder.remove();
		if(turnOrder.size()==0) {
			prepareChampionTurns();
		}
		Champion c=(Champion) turnOrder.remove();
		while(c.getCondition()!=Condition.INACTIVE) {
			c=(Champion) turnOrder.remove();
		}
		ArrayList<Effect> arr=c.getAppliedEffects();
		for(int i=0;i<arr.size();i++) {
			Effect eff=arr.get(i);
			eff.setDuration(eff.getDuration()-1);
			if(eff.getDuration()==0) {
				arr.remove(i);
			}
		}
		ArrayList<Ability> ar2=c.getAbilities();
		for(int i=0;i<ar2.size();i++) {
			Ability abl=ar2.get(i);
			abl.setCurrentCooldown(abl.getBaseCooldown());
		}
		c.setCurrentActionPoints(c.getMaxActionPointsPerTurn());
	}
	
	
	public Champion getCurrentChampion() {
		// if the champion has stun in his applied effects he passes his turn
		
		return (Champion) turnOrder.peekMin();
	}
	
	public void attack(Direction d) {
		
		// if the current champion has disarm in the applied effects dont attack
		// if the champion in the direction has dodge in the applied effects call math.rondom()*2 if >1 attact if <1 dont attack
		// if the target champion has shield in the applied effects dont attack but remove the shield
	}
	public void castAbility(Ability a) {
		//if the current champion has powerup in the applied effects increase the effect by 20%
		//if the currenct champion has silence in the applied effects dont cast
	}
	public void move(Direction d)throws NotEnoughResourcesException,UnallowedMovementException {
		if(this.getCurrentChampion().getCurrentActionPoints()<1)throw new NotEnoughResourcesException();
		
		int newX = this.getCurrentChampion().getLocation().x;
		int newY = this.getCurrentChampion().getLocation().y;

		int oldX = newX;
		int oldY = newY;
		
		if(d == Direction.UP)newX--;
		else if(d == Direction.DOWN)newX++;
		else if(d == Direction.LEFT)newY--;
		else if(d == Direction.RIGHT)newY++;
		
		
		if(newX>=this.BOARDHEIGHT || newY>=this.BOARDWIDTH || this.board[newX][newY] != null || this.getCurrentChampion().getCondition()!=Condition.ACTIVE) throw new UnallowedMovementException();
		
		this.board[newX][newY] = this.getCurrentChampion();
		this.board[oldX][oldY] = null;
		this.getCurrentChampion().setLocation(new Point(newX,newY));
		this.getCurrentChampion().setCurrentActionPoints(this.getCurrentChampion().getCurrentActionPoints());
		//if the champion has root in applied effects dont move
	}
	
	public static boolean hasEffect(Champion c,String effect) {
		for(int i =0;i<c.getAppliedEffects().size();i++) {
			if(c.getAppliedEffects().get(i).getName().equals(effect))return true;
		}
		return false;
	}
	private void prepareChampionTurns() {
		for(int i=0;i<firstPlayer.getTeam().size();i++) {
			Champion c=firstPlayer.getTeam().get(i);
			if(c.getCondition()!=Condition.KNOCKEDOUT) {
				turnOrder.insert(firstPlayer.getTeam().get(i));
			}
			
		}
		for(int i=0;i<secondPlayer.getTeam().size();i++) {
			Champion c=secondPlayer.getTeam().get(i);
			if(c.getCondition()!=Condition.KNOCKEDOUT) {
				turnOrder.insert(secondPlayer.getTeam().get(i));
			}		}
		
	}
	
	
}
