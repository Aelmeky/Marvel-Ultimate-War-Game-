package engine;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.sql.rowset.CachedRowSet;

import exceptions.*;
import model.abilities.Ability;
import model.abilities.AreaOfEffect;
import model.abilities.CrowdControlAbility;
import model.abilities.DamagingAbility;
import model.abilities.HealingAbility;
import model.effects.Disarm;
import model.effects.Dodge;
import model.effects.Effect;
import model.effects.EffectType;
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
import model.world.Damageable;
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
	public void useLeaderAbility() throws LeaderAbilityAlreadyUsedException,LeaderNotCurrentException{
		Champion c=this.getCurrentChampion();
		Player p=this.getChampionPlayer(c);
		if(c!=p.getLeader()) {
			throw new LeaderNotCurrentException();
		}
		Player p2=null;
		if(p==this.firstPlayer) {
			p2=this.secondPlayer;
		}else {
			p2=this.firstPlayer;
		}
		if((p==this.firstPlayer&&this.firstLeaderAbilityUsed)||(p==this.secondPlayer&&this.secondLeaderAbilityUsed)) {
			throw new LeaderAbilityAlreadyUsedException();
		}else {
			if(p==this.firstPlayer) {
				this.firstLeaderAbilityUsed=true;;
			}else {
				this.secondLeaderAbilityUsed=true;
			}
			if(c instanceof Hero) {
				c.useLeaderAbility(p.getTeam());
			}
			if(c instanceof Villain) {
				c.useLeaderAbility(p2.getTeam());
			}
			if(c instanceof AntiHero) {
				ArrayList<Champion>arr=new ArrayList<Champion>();
				for(int i=0;i<p.getTeam().size();i++) {
					if(p.getTeam().get(i)!=p.getLeader()) {
						arr.add(p.getTeam().get(i));
					}
				}
				for(int i=0;i<p2.getTeam().size();i++) {
					if(p2.getTeam().get(i)!=p2.getLeader()) {
						arr.add(p2.getTeam().get(i));
					}
				}
				c.useLeaderAbility(arr);
			}
		}
	}
	public void castAbility(Ability a, Direction d) {
		
	}
	public void castAbility(Ability a, int x, int y) {
		
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
		while(!turnOrder.isEmpty()&&c.getCondition()!=Condition.INACTIVE) {
			c=(Champion) turnOrder.remove();
			
		}
		ArrayList<Effect> arr=c.getAppliedEffects();
		for(int i=0;i<arr.size();i++) {
			Effect eff=arr.get(i);
			eff.setDuration(eff.getDuration()-1);
			if(eff.getDuration()==0) {
				eff.remove(c);
				//arr.remove(i);
			}
		}
		ArrayList<Ability> ar2=c.getAbilities();
		for(int i=0;i<ar2.size();i++) {
			Ability abl=ar2.get(i);
			abl.setCurrentCooldown(abl.getCurrentCooldown()-1);
		}
		c.setCurrentActionPoints(c.getMaxActionPointsPerTurn());
	}
	
	
	public Champion getCurrentChampion() {
		
		return (Champion) turnOrder.peekMin();
	}
	
	public void attack(Direction d) throws ChampionDisarmedException,NotEnoughResourcesException {
		if(hasEffect(this.getCurrentChampion(),"Disarm")) throw new ChampionDisarmedException();
		if(this.getCurrentChampion().getCurrentActionPoints()<2)throw new NotEnoughResourcesException();
		int x = this.getCurrentChampion().getLocation().x;
		int y = this.getCurrentChampion().getLocation().y;
		Damageable target = null;
		int i=0;
		while(x>=0 && x<this.BOARDHEIGHT-1 && y>=0 && y<this.BOARDWIDTH-1 && target == null&&i<this.getCurrentChampion().getAttackRange()) {
			if(d == Direction.UP) x++;
			else if(d == Direction.DOWN)x--;
			else if(d == Direction.LEFT)y--;
			else if(d == Direction.RIGHT)y++;
			i++;
			if(x<0||y<0||x>=this.BOARDHEIGHT || y>=this.BOARDWIDTH) break;
			if(this.board[x][y] != null && this.board[x][y] instanceof Damageable) {
				if(this.board[x][y] instanceof Cover ||this.championIsEnemy(this.getCurrentChampion(),(Champion)this.board[x][y])) {
					target = (Damageable)this.board[x][y];
				}
			}
		}
		this.getCurrentChampion().setCurrentActionPoints(this.getCurrentChampion().getCurrentActionPoints()-2);
		boolean flag=false;
		Champion c1=getCurrentChampion();
		if(target != null) {
			if(target instanceof Cover) {
				target.setCurrentHP(target.getCurrentHP()-c1.getAttackDamage());
			}else {
				if(hasEffect((Champion)target,"Shield")) {
					removeShield((Champion)target);
					return;
				}else {
					flag=true;
				}
				if(hasEffect((Champion)target,"Dodge")) {
					if(Math.floor(Math.random()*2)==1) {
						flag=true;
					}else {
						return;
						}
				}
				if(flag) {
					if((c1 instanceof Villain && target instanceof Villain )||(target instanceof Hero && c1 instanceof Hero)||(c1 instanceof AntiHero&&target instanceof AntiHero)) {
						target.setCurrentHP(target.getCurrentHP()-c1.getAttackDamage());
					}else {
//						System.out.println("here");
						target.setCurrentHP(target.getCurrentHP()-(int)(c1.getAttackDamage()*1.5));						
					}
				}
			}
		}
		if(target!=null&&target.getCurrentHP()==0) {
			this.board[target.getLocation().x][target.getLocation().y]=null;
		}
	}
	public void castAbility(Ability a)throws AbilityUseException, CloneNotSupportedException, InvalidTargetException {
		if((hasEffect(getCurrentChampion(), "silence"))||a.getCurrentCooldown()!=0) {
			throw new AbilityUseException();
		}else {
			boolean good=false;
			if((a instanceof CrowdControlAbility && ((CrowdControlAbility) a).getEffect().getType()==EffectType.BUFF)|| a instanceof HealingAbility ) {
				good=true;
			}
			if(a.getCastArea()==AreaOfEffect.SELFTARGET||a.getCastArea()==AreaOfEffect.SURROUND||a.getCastArea()==AreaOfEffect.TEAMTARGET) {
				if(a.getCastArea()==AreaOfEffect.SELFTARGET) {
					ArrayList<Damageable> arr=new ArrayList<Damageable>();
					arr.add((Damageable)getCurrentChampion());
					a.execute(arr);
				}
				if(a.getCastArea()==AreaOfEffect.SURROUND) {
					int x=getCurrentChampion().getLocation().x;
					int y=getCurrentChampion().getLocation().y;
					ArrayList<Object>arr=new ArrayList<Object>();
					if(x+1<5) {
						Object o=this.board[x+1][y];
						arr.add(o);
					}
					if(x-1>=0) {
						Object o=this.board[x-1][y];
						arr.add(o);
					}
					if(y+1<5) {
						Object o=this.board[x][y+1];
						arr.add(o);
					}
					if(y-1>=0) {
						Object o=this.board[x][y-1];
						arr.add(o);
					}
					
					if(x+1<5&&y+1<5) {
						Object o=this.board[x+1][y+1];
						arr.add(o);
					}
					if(x-1>=0&&y-1>=0) {
						Object o=this.board[x-1][y-1];
						arr.add(o);
					}
					if(y+1<5&&x-1>=0) {
						Object o=this.board[x-1][y+1];
						arr.add(o);
					}
					if(y-1>=0&&x+1<5) {
						Object o=this.board[x+1][y-1];
						arr.add(o);
					}
					ArrayList<Damageable>ar2=new ArrayList<Damageable>();
					for(int i=0;i<arr.size();i++) {
						if((arr.get(i) instanceof Champion && ((Champion) arr.get(i)).getCondition() ==Condition.KNOCKEDOUT) ||arr.get(i)==null) {
							continue;
						}
						if(good) {
							if(championIsEnemy((Champion) arr.get(i), getCurrentChampion())) {
								continue;
							}else {
								ar2.add((Damageable) arr.get(i));
							}
						}else {
							if(championIsEnemy((Champion) arr.get(i), getCurrentChampion())) {
								ar2.add((Damageable) arr.get(i));
							}else {
								continue;
							}
							
						}
					}
					a.execute(ar2);
				}
				if(a.getCastArea()==AreaOfEffect.TEAMTARGET) {
					ArrayList<Champion>arr=new ArrayList<Champion>();
					if(good) {
						arr=this.getChampionPlayer(getCurrentChampion()).getTeam();
					}else {
						if(getChampionPlayer(getCurrentChampion())==this.firstPlayer) {
							arr=this.secondPlayer.getTeam();
						}else {
							arr=this.firstPlayer.getTeam();						}
					}
					ArrayList<Damageable>ar2=new ArrayList<Damageable>();
					for(int i=0;i<arr.size();i++) {
						ar2.add((Damageable)arr.get(i));
					}
					a.execute(ar2);
				}
			}
		}
	}
	
	public void move(Direction d)throws NotEnoughResourcesException,UnallowedMovementException {
		if(this.getCurrentChampion().getCurrentActionPoints()<=1)throw new NotEnoughResourcesException();

		int newX = this.getCurrentChampion().getLocation().x;
		int newY = this.getCurrentChampion().getLocation().y;
		int oldX = newX;
		int oldY = newY;
		if(d == Direction.UP)newX++;
		else if(d == Direction.DOWN)newX--;
		else if(d == Direction.LEFT)newY--;
		else if(d == Direction.RIGHT)newY++;
		if(newX<0||newY<0||newX>=this.BOARDHEIGHT || newY>=this.BOARDWIDTH || this.board[newX][newY] != null || this.getCurrentChampion().getCondition()!=Condition.ACTIVE) throw new UnallowedMovementException();
		this.board[newX][newY] = this.getCurrentChampion();
		this.board[oldX][oldY] = null;
		this.getCurrentChampion().setLocation(new Point(newX,newY));
		this.getCurrentChampion().setCurrentActionPoints(this.getCurrentChampion().getCurrentActionPoints()-1);

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
			}
		}
	}
	public Player checkGameOver() {
		boolean playerTwoWon = true;
		for(int i = 0;i<firstPlayer.getTeam().size();i++) {
			if(firstPlayer.getTeam().get(i).getCondition()!=Condition.KNOCKEDOUT) {
				playerTwoWon = false;
				break;
			}
		}
		if (playerTwoWon) return this.secondPlayer;
		
		boolean playerOneWon = true;
		for(int i = 0;i<secondPlayer.getTeam().size();i++) {
			if(secondPlayer.getTeam().get(i).getCondition()!=Condition.KNOCKEDOUT) {
				playerTwoWon = false;
				break;
			}
		}
		if (playerOneWon) return this.firstPlayer;
		
		return null;
	}
	public  boolean championIsEnemy(Champion c1 ,Champion c2) {
		boolean c1InTeam1 = false;
		boolean c2InTeam1 = false;
		for(int i = 0;i<this.firstPlayer.getTeam().size();i++) {
			if(this.firstPlayer.getTeam().get(i)==c1)c1InTeam1=true;
			if(this.firstPlayer.getTeam().get(i)==c2)c2InTeam1=true;
		}
		if(c1InTeam1 && c2InTeam1) return false;
		
		boolean c1InTeam2 = false;
		boolean c2InTeam2 = false;
		for(int i = 0;i<this.secondPlayer.getTeam().size();i++) {
			if(this.secondPlayer.getTeam().get(i)==c1)c1InTeam2=true;
			if(this.secondPlayer.getTeam().get(i)==c2)c2InTeam2=true;
		}
		if(c1InTeam2 && c2InTeam2) return false;
		
		return true;
	}
	public Player getChampionPlayer(Champion c) {
		for(int i=0;i<this.getFirstPlayer().getTeam().size();i++) {
			if(this.getFirstPlayer().getTeam().get(i)==c) {
				return this.getFirstPlayer();
			}
		}
		for(int i=0;i<this.getSecondPlayer().getTeam().size();i++) {
			if(this.getSecondPlayer().getTeam().get(i)==c) {
				return this.getSecondPlayer();
			}
		}
		return null;
	}
	
	public void removeShield(Champion c) {
		for(int i =0;i<c.getAppliedEffects().size();i++) {
			if(c.getAppliedEffects().get(i).getName().equals("Shield")) {
				c.getAppliedEffects().get(i).remove(c);
			}
		}
	}
	
	
}
