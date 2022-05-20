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

	public void useLeaderAbility() throws LeaderAbilityAlreadyUsedException, LeaderNotCurrentException {
		Champion c = this.getCurrentChampion();
		Player p = this.getChampionPlayer(c);
		if (c != p.getLeader()) {
			throw new LeaderNotCurrentException();
		}
		Player p2 = null;
		if (p == this.firstPlayer) {
			p2 = this.secondPlayer;
		} else {
			p2 = this.firstPlayer;
		}
		if ((p == this.firstPlayer && this.firstLeaderAbilityUsed)
				|| (p == this.secondPlayer && this.secondLeaderAbilityUsed)) {
			throw new LeaderAbilityAlreadyUsedException();
		} else {
			if (p == this.firstPlayer) {
				this.firstLeaderAbilityUsed = true;
				;
			} else {
				this.secondLeaderAbilityUsed = true;
			}
			if (c instanceof Hero) {
				c.useLeaderAbility(p.getTeam());
			}
			if (c instanceof Villain) {
				c.useLeaderAbility(p2.getTeam());
			}
			if (c instanceof AntiHero) {
				ArrayList<Champion> arr = new ArrayList<Champion>();
				for (int i = 0; i < p.getTeam().size(); i++) {
					if (p.getTeam().get(i) != p.getLeader()) {
						arr.add(p.getTeam().get(i));
					}
				}
				for (int i = 0; i < p2.getTeam().size(); i++) {
					if (p2.getTeam().get(i) != p2.getLeader()) {
						arr.add(p2.getTeam().get(i));
					}
				}
				c.useLeaderAbility(arr);
			}
		}
	}

	public void castAbility(Ability a, Direction d) throws AbilityUseException, NotEnoughResourcesException,
			InvalidTargetException, CloneNotSupportedException {
		if ((hasEffect(getCurrentChampion(), "Silence")) || a.getCurrentCooldown() != 0) {
			throw new AbilityUseException();
		}
		if (getCurrentChampion().getMana() < a.getManaCost()) {
			throw new NotEnoughResourcesException();
		}
		if (getCurrentChampion().getCurrentActionPoints() < a.getRequiredActionPoints()) {
			throw new NotEnoughResourcesException();
		}
		getCurrentChampion()
				.setCurrentActionPoints(getCurrentChampion().getCurrentActionPoints() - a.getRequiredActionPoints());
		getCurrentChampion().setMana(getCurrentChampion().getMana() - a.getManaCost());
		a.setCurrentCooldown(a.getBaseCooldown());
		int x = this.getCurrentChampion().getLocation().x;
		int y = this.getCurrentChampion().getLocation().y;
		boolean good = false;
		if ((a instanceof CrowdControlAbility && ((CrowdControlAbility) a).getEffect().getType() == EffectType.BUFF)
				|| a instanceof HealingAbility) {
			good = true;
		}
		ArrayList<Damageable> arr = new ArrayList<Damageable>();
		for (int i = 0; i < a.getCastRange(); i++) {
			if (d == Direction.UP)
				x++;
			else if (d == Direction.DOWN)
				x--;
			else if (d == Direction.LEFT)
				y--;
			else if (d == Direction.RIGHT)
				y++;
			
			if(x>=5||y>=5||x<0||y<0) {
				break;
			}
			if (this.board[x][y] != null) {
				// not null - friend, enemy or cover
				if (good && this.board[x][y] instanceof Champion
						&& !championIsEnemy(getCurrentChampion(), (Champion) this.board[x][y])) {
					arr.add((Damageable) this.board[x][y]);
				}
				//if not good covers should should only be with damaging abilities
				if (!good && (this.board[x][y] instanceof Cover || (this.board[x][y] instanceof Champion
						&& championIsEnemy(getCurrentChampion(), (Champion) this.board[x][y])))) {
					if (this.board[x][y] instanceof Champion && hasEffect((Champion) this.board[x][y], "Shield")) {
						//remove shield for damaging ability only
						removeShield((Champion) this.board[x][y]);
					} else {
						arr.add((Damageable) this.board[x][y]);
					}
				}
			}
		}
		a.execute(arr);
		for (int i = 0; i < arr.size(); i++) {
			this.ifDead(arr.get(i));
		}
	}

	public void castAbility(Ability a, int x, int y) throws InvalidTargetException, CloneNotSupportedException,
			AbilityUseException, NotEnoughResourcesException {
		if ((hasEffect(getCurrentChampion(), "Silence")) || a.getCurrentCooldown() != 0) {
			throw new AbilityUseException();
		}
		if (getCurrentChampion().getMana() < a.getManaCost()) {
			throw new NotEnoughResourcesException();
		}
		if (getCurrentChampion().getCurrentActionPoints() < a.getRequiredActionPoints()) {
			throw new NotEnoughResourcesException();
		}
		if (this.board[x][y] == null || this.board[x][y] instanceof Cover) {
			throw new InvalidTargetException();
		}
		int distanceToTarget = Math.abs(x - getCurrentChampion().getLocation().x)
				+ Math.abs(y - getCurrentChampion().getLocation().y);
		if (a.getCurrentCooldown() != 0 || distanceToTarget > a.getCastRange()) {
			throw new AbilityUseException();
		}
		ArrayList<Damageable> arr = new ArrayList<Damageable>();
		if (a instanceof HealingAbility || (a instanceof CrowdControlAbility
				&& ((CrowdControlAbility) a).getEffect().getType() == EffectType.BUFF)) {
			if (!championIsEnemy(getCurrentChampion(), (Champion) this.board[x][y])) {
				arr.add((Damageable) this.board[x][y]);
			} else {
				throw new InvalidTargetException();
			}
		}
		if (a instanceof DamagingAbility || (a instanceof CrowdControlAbility
				&& ((CrowdControlAbility) a).getEffect().getType() == EffectType.DEBUFF)) {
			if (championIsEnemy(getCurrentChampion(), (Champion) this.board[x][y])) {
				arr.add((Damageable) this.board[x][y]);
			} else {
				throw new InvalidTargetException();
			}
		}
		a.execute(arr);
		getCurrentChampion()
				.setCurrentActionPoints(getCurrentChampion().getCurrentActionPoints() - a.getRequiredActionPoints());
		getCurrentChampion().setMana(getCurrentChampion().getMana() - a.getManaCost());
		a.setCurrentCooldown(a.getBaseCooldown());
		this.ifDead((Damageable) this.board[x][y]);
	}

	public void ifDead(Damageable d) { 
	
		if (d != null && d.getCurrentHP() == 0) {
			if (d instanceof Champion) {
				Champion c = (Champion) d;
				c.setCondition(Condition.KNOCKEDOUT);
				ArrayList<Champion> team = getChampionPlayer(c).getTeam();
				for (int i = 0; i < team.size(); i++) {
					if (team.get(i) == c) {
						team.remove(i);
					}
				}
				PriorityQueue pq = new PriorityQueue(turnOrder.size());
				int j = turnOrder.size();
				for (int i = 0; i < j; i++) {
					Champion c2 = (Champion) turnOrder.remove();
					if (c2.getCurrentHP() > 0) {
						pq.insert(c2);
					}
				}
				this.turnOrder = pq;
			}
			this.board[d.getLocation().x][d.getLocation().y] = null;
		}
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
		Champion c1 = (Champion) turnOrder.remove();
		if (turnOrder.size() == 0) {
			prepareChampionTurns();
		}
		while (!this.turnOrder.isEmpty()
				&& ((Champion) this.turnOrder.peekMin()).getCondition() == Condition.INACTIVE) {
			Champion c = (Champion) this.turnOrder.peekMin();
			ArrayList<Effect> arr = c.getAppliedEffects();
			for (int j = 0; j < arr.size(); j++) {
				Effect eff = arr.get(j);
				eff.setDuration(eff.getDuration() - 1);
				if (eff.getDuration() == 0) {
					eff.remove(c);
					arr.remove(j);
					j--;
				}
			}
			ArrayList<Ability> ar2 = c.getAbilities();
			for (int j = 0; j < ar2.size(); j++) {
				Ability abl = ar2.get(j);
				abl.setCurrentCooldown(abl.getCurrentCooldown() - 1);
			}
			this.turnOrder.remove();
		}
		Champion c = (Champion) turnOrder.peekMin();
		ArrayList<Effect> arr = c.getAppliedEffects();
		for (int j = 0; j < arr.size(); j++) {
			Effect eff = arr.get(j);
			eff.setDuration(eff.getDuration() - 1);
			if (eff.getDuration() == 0) {
				eff.remove(c);
				arr.remove(j);
				j--;
			}
		}
		ArrayList<Ability> ar2 = c.getAbilities();
		for (int j = 0; j < ar2.size(); j++) {
			Ability abl = ar2.get(j);
			abl.setCurrentCooldown(abl.getCurrentCooldown() - 1);
		}
		c.setCurrentActionPoints(c.getMaxActionPointsPerTurn());
	}

	public Champion getCurrentChampion() {

		return (Champion) turnOrder.peekMin();
	}

	public void attack(Direction d) throws ChampionDisarmedException, NotEnoughResourcesException {
		/*
		 * For the attack method and all cast ability methods (except for casting a
		 * single target ability), if no valid target was found based on the area of
		 * effect and the cast range of the ability, no one will be affected by the
		 * action. However, all required resources for this action will be deducted from
		 * the champion carrying it out.
		 */
		if (hasEffect(this.getCurrentChampion(), "Disarm"))
			throw new ChampionDisarmedException();
		
		if (this.getCurrentChampion().getCurrentActionPoints() < 2)
			throw new NotEnoughResourcesException();
		
		this.getCurrentChampion().setCurrentActionPoints(this.getCurrentChampion().getCurrentActionPoints() - 2);
		int x = this.getCurrentChampion().getLocation().x;
		int y = this.getCurrentChampion().getLocation().y;
		Damageable target = null;
		int i = 0;
		while (target == null) {
			if (d == Direction.UP)
				x++;
			else if (d == Direction.DOWN)
				x--;
			else if (d == Direction.LEFT)
				y--;
			else if (d == Direction.RIGHT)
				y++;
			i++;
			if (x < 0 || y < 0 || x >= this.BOARDHEIGHT || y >= this.BOARDWIDTH || i > this.getCurrentChampion().getAttackRange())
				break;
			if (this.board[x][y] != null && this.board[x][y] instanceof Damageable) {
				if (this.board[x][y] instanceof Cover
						|| this.championIsEnemy(this.getCurrentChampion(), (Champion) this.board[x][y])) {
					target = (Damageable) this.board[x][y];
					break;
				}
			}
		}
		
		boolean attackflag = false;
		Champion c1 = getCurrentChampion();
		if (target != null) {
			if (target instanceof Cover) {
				target.setCurrentHP(target.getCurrentHP() - c1.getAttackDamage());
			} 
			else {
				if (hasEffect((Champion) target, "Shield")) {
					removeShield((Champion) target);
					return;
				} 
				else {
					attackflag = true;
				}
				if (hasEffect((Champion) target, "Dodge")) {
					if (Math.floor(Math.random() * 2) == 1) {
						attackflag = true;
					}
					else {
						return;
					}
				}
				if (attackflag) {
					if ((c1 instanceof Villain && target instanceof Villain)
							|| (target instanceof Hero && c1 instanceof Hero)
							|| (c1 instanceof AntiHero && target instanceof AntiHero)) {
						target.setCurrentHP(target.getCurrentHP() - c1.getAttackDamage());
					} else {
						target.setCurrentHP(target.getCurrentHP() - (int) (c1.getAttackDamage() * 1.5));
					}
				}
			}
		}
		this.ifDead(target);
	}

	public void castAbility(Ability a) throws AbilityUseException, CloneNotSupportedException, InvalidTargetException,
			NotEnoughResourcesException {
		if ((hasEffect(getCurrentChampion(), "Silence")) || a.getCurrentCooldown() != 0) {
			throw new AbilityUseException();
		}
		if (getCurrentChampion().getMana() < a.getManaCost()) {
			throw new NotEnoughResourcesException();
		}
		if (getCurrentChampion().getCurrentActionPoints() < a.getRequiredActionPoints()) {
			throw new NotEnoughResourcesException();
		}
		boolean good = false;
		if ((a instanceof CrowdControlAbility && ((CrowdControlAbility) a).getEffect().getType() == EffectType.BUFF)
				|| a instanceof HealingAbility) {
			good = true;
		}
		if (a.getCastArea() == AreaOfEffect.SELFTARGET || a.getCastArea() == AreaOfEffect.SURROUND
				|| a.getCastArea() == AreaOfEffect.TEAMTARGET) {
			if (a.getCastArea() == AreaOfEffect.SELFTARGET) {
				ArrayList<Damageable> arr = new ArrayList<Damageable>();
				arr.add((Damageable) getCurrentChampion());
				a.execute(arr);
				getCurrentChampion().setCurrentActionPoints(
						getCurrentChampion().getCurrentActionPoints() - a.getRequiredActionPoints());
				getCurrentChampion().setMana(getCurrentChampion().getMana() - a.getManaCost());
				a.setCurrentCooldown(a.getBaseCooldown());
			}
			if (a.getCastArea() == AreaOfEffect.SURROUND) {
				int x = getCurrentChampion().getLocation().x;
				int y = getCurrentChampion().getLocation().y;
				ArrayList<Object> arr = new ArrayList<Object>();
				if (x + 1 < 5) {
					Object o = this.board[x + 1][y];
					arr.add(o);
				}
				if (x - 1 >= 0) {
					Object o = this.board[x - 1][y];
					arr.add(o);
				}
				if (y + 1 < 5) {
					Object o = this.board[x][y + 1];
					arr.add(o);
				}
				if (y - 1 >= 0) {
					Object o = this.board[x][y - 1];
					arr.add(o);
				}

				if (x + 1 < 5 && y + 1 < 5) {
					Object o = this.board[x + 1][y + 1];
					arr.add(o);
				}
				if (x - 1 >= 0 && y - 1 >= 0) {
					Object o = this.board[x - 1][y - 1];
					arr.add(o);
				}
				if (y + 1 < 5 && x - 1 >= 0) {
					Object o = this.board[x - 1][y + 1];
					arr.add(o);
				}
				if (y - 1 >= 0 && x + 1 < 5) {
					Object o = this.board[x + 1][y - 1];
					arr.add(o);
				}
				ArrayList<Damageable> ar2 = new ArrayList<Damageable>();
				for (int i = 0; i < arr.size(); i++) {
					if ((arr.get(i) instanceof Champion
							&& ((Champion) arr.get(i)).getCondition() == Condition.KNOCKEDOUT) || arr.get(i) == null) {
						continue;
					}
					if (good) {
						if (arr.get(i) instanceof Cover
								|| championIsEnemy((Champion) arr.get(i), getCurrentChampion())) {
							continue;
						} else {
							ar2.add((Damageable) arr.get(i));
						}
					} else {
						if (arr.get(i) instanceof Champion
								&& championIsEnemy((Champion) arr.get(i), getCurrentChampion())) {
							if (hasEffect((Champion) arr.get(i), "Shield")) {
								removeShield((Champion) arr.get(i));
							} else {
								ar2.add((Damageable) arr.get(i));
							}
						} else {
							if (arr.get(i) instanceof Cover) {
								ar2.add((Damageable) arr.get(i));
							}
						}
					}
				}
				a.execute(ar2);
				for (int i = 0; i < ar2.size(); i++) {
					this.ifDead(ar2.get(i));
				}
				getCurrentChampion().setCurrentActionPoints(
						getCurrentChampion().getCurrentActionPoints() - a.getRequiredActionPoints());
				getCurrentChampion().setMana(getCurrentChampion().getMana() - a.getManaCost());
				a.setCurrentCooldown(a.getBaseCooldown());
			}
			if (a.getCastArea() == AreaOfEffect.TEAMTARGET) {
				ArrayList<Champion> arr = new ArrayList<Champion>();
				if (good) {
					arr = this.getChampionPlayer(getCurrentChampion()).getTeam();
				} else {
					if (getChampionPlayer(getCurrentChampion()) == this.firstPlayer) {
						arr = this.secondPlayer.getTeam();
					} else {
						arr = this.firstPlayer.getTeam();
					}
				}
				ArrayList<Damageable> ar2 = new ArrayList<Damageable>();
				for (int i = 0; i < arr.size(); i++) {
					Champion c = arr.get(i);
					if (inrange(getCurrentChampion(), c, a.getCastRange())) {
						ar2.add((Damageable) arr.get(i));
					}
				}
				a.execute(ar2);
				for (int i = 0; i < ar2.size(); i++) {
					this.ifDead(ar2.get(i));
				}
				getCurrentChampion().setCurrentActionPoints(
						getCurrentChampion().getCurrentActionPoints() - a.getRequiredActionPoints());
				getCurrentChampion().setMana(getCurrentChampion().getMana() - a.getManaCost());
				a.setCurrentCooldown(a.getBaseCooldown());
			}
		}
	}

	public static boolean inrange(Champion c1, Champion c2, int x) {
		int distanceToTarget = Math.abs(c1.getLocation().x - c2.getLocation().x)
				+ Math.abs(c1.getLocation().y - c2.getLocation().y);
		if (distanceToTarget <= x) {
			return true;
		}
		return false;
	}

	public void move(Direction d) throws NotEnoughResourcesException, UnallowedMovementException {
		if (this.getCurrentChampion().getCurrentActionPoints() <= 1)
			throw new NotEnoughResourcesException();

		int newX = this.getCurrentChampion().getLocation().x;
		int newY = this.getCurrentChampion().getLocation().y;
		int oldX = newX;
		int oldY = newY;
		if (d == Direction.UP)
			newX++;
		else if (d == Direction.DOWN)
			newX--;
		else if (d == Direction.LEFT)
			newY--;
		else if (d == Direction.RIGHT)
			newY++;
		if (newX < 0 || newY < 0 || newX >= this.BOARDHEIGHT || newY >= this.BOARDWIDTH
				|| this.board[newX][newY] != null || this.getCurrentChampion().getCondition() != Condition.ACTIVE)
			throw new UnallowedMovementException();
		this.board[newX][newY] = this.getCurrentChampion();
		this.board[oldX][oldY] = null;
		this.getCurrentChampion().setLocation(new Point(newX, newY));
		this.getCurrentChampion().setCurrentActionPoints(this.getCurrentChampion().getCurrentActionPoints() - 1);

	}

	public static boolean hasEffect(Champion c, String effect) {
		for (int i = 0; i < c.getAppliedEffects().size(); i++) {
			if (c.getAppliedEffects().get(i).getName().equals(effect))
				return true;
		}
		return false;
	}

	private void prepareChampionTurns() {
		for (int i = 0; i < firstPlayer.getTeam().size(); i++) {
			Champion c = firstPlayer.getTeam().get(i);
			if (c.getCondition() != Condition.KNOCKEDOUT) {
				turnOrder.insert(firstPlayer.getTeam().get(i));
			}

		}
		for (int i = 0; i < secondPlayer.getTeam().size(); i++) {
			Champion c = secondPlayer.getTeam().get(i);
			if (c.getCondition() != Condition.KNOCKEDOUT) {
				turnOrder.insert(secondPlayer.getTeam().get(i));
			}
		}
	}

	public Player checkGameOver() {
		if (firstPlayer.getTeam().isEmpty()) {
			return this.secondPlayer;
		}
		if (this.getSecondPlayer().getTeam().isEmpty()) {
			return this.firstPlayer;
		}
		return null;
	}

	public boolean championIsEnemy(Champion c1, Champion c2) {
		boolean c1InTeam1 = false;
		boolean c2InTeam1 = false;
		for (int i = 0; i < this.firstPlayer.getTeam().size(); i++) {
			if (this.firstPlayer.getTeam().get(i) == c1)
				c1InTeam1 = true;
			if (this.firstPlayer.getTeam().get(i) == c2)
				c2InTeam1 = true;
		}
		if (c1InTeam1 && c2InTeam1)
			return false;

		boolean c1InTeam2 = false;
		boolean c2InTeam2 = false;
		for (int i = 0; i < this.secondPlayer.getTeam().size(); i++) {
			if (this.secondPlayer.getTeam().get(i) == c1)
				c1InTeam2 = true;
			if (this.secondPlayer.getTeam().get(i) == c2)
				c2InTeam2 = true;
		}
		if (c1InTeam2 && c2InTeam2)
			return false;

		return true;
	}

	public Player getChampionPlayer(Champion c) {
		for (int i = 0; i < this.getFirstPlayer().getTeam().size(); i++) {
			if (this.getFirstPlayer().getTeam().get(i) == c) {
				return this.getFirstPlayer();
			}
		}
		for (int i = 0; i < this.getSecondPlayer().getTeam().size(); i++) {
			if (this.getSecondPlayer().getTeam().get(i) == c) {
				return this.getSecondPlayer();
			}
		}
		return null;
	}

	public void removeShield(Champion c) {
		for (int i = 0; i < c.getAppliedEffects().size(); i++) {
			if (c.getAppliedEffects().get(i).getName().equals("Shield")) {
				c.getAppliedEffects().get(i).remove(c);
				c.getAppliedEffects().remove(i);
			}
		}
	}
}
