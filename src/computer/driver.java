package computer;

import java.awt.Point;
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
import model.world.Cover;
import model.world.Direction;
import model.world.Hero;
import model.world.Villain;

public class driver {
	static int i = 1;
	static Player p1;
	static Player p2;
	public static ArrayList<String> ComputerTurn(Game game){
		p1=game.getFirstPlayer();
		p2=game.getSecondPlayer();
		Game oldgame=clone(game);
		Game game2=clone(game);
		ArrayList<String>sol=new ArrayList<String>();
		sol= minimax(oldgame, game2, sol, 1);
		return sol;
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
		while (!((Champion) newGame.getTurnOrder().peekMin()).getName()
				.equals(((Champion) game.getTurnOrder().peekMin()).getName())) {
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
			Point p=new Point(c.getLocation().x, c.getLocation().y);
			c2.setLocation(p);
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
					a2.setCurrentCooldown(a.getCurrentCooldown());
					;
					c2.getAbilities().add(a2);
				}
				if (a instanceof HealingAbility) {
					a2 = new HealingAbility(a.getName(), a.getManaCost(), a.getBaseCooldown(), a.getCastRange(),
							a.getCastArea(), a.getRequiredActionPoints(), ((HealingAbility) a).getHealAmount());

					a2.setCurrentCooldown(a.getCurrentCooldown());
					;
					c2.getAbilities().add(a2);
				}
				if (a instanceof DamagingAbility) {
					a2 = new DamagingAbility(a.getName(), a.getManaCost(), a.getBaseCooldown(), a.getCastRange(),
							a.getCastArea(), a.getRequiredActionPoints(), ((DamagingAbility) a).getDamageAmount());
					a2.setCurrentCooldown(a.getCurrentCooldown());
					;
					c2.getAbilities().add(a2);
				}
			}
			for (Effect e : c.getAppliedEffects()) {
				try {
					Effect e2 = (Effect) e.clone();
					c2.getAppliedEffects().add(e2);
				} catch (CloneNotSupportedException e1) {}
			}
			p2.getTeam().add(c2);
			if (p1.getLeader()!=null&&p1.getLeader().getName().equals(c2.getName())) {
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
					if (isFriend(me, ((Champion) n[i][j]))
							&& !((Champion) n[i][j]).getName().equals((ogame.getCurrentChampion().getName()))) {
						// A friend health changed
						sum -= ((Champion) o[i][j]).getCurrentHP() - ((Champion) n[i][j]).getCurrentHP();
						//A friend has an Effect Applied
						sum+=newEffectApplied(((Champion) o[i][j]), ((Champion) n[i][j]),me);
					}
					if (ngame.getCurrentChampion().getName().equals(((Champion) n[i][j]).getName())) {
						// if me
						sum += (((Champion) n[i][j]).getCurrentHP() - ogame.getCurrentChampion().getCurrentHP())
								* (ogame.getCurrentChampion().getMaxHP() / ogame.getCurrentChampion().getCurrentHP());
						//System.out.println("here"+((Champion) n[i][j]).getAppliedEffects()+" "+sum);	
						sum+=newEffectApplied(ogame.getCurrentChampion(), ngame.getCurrentChampion(),me);
					}
					if (!isFriend(me, ((Champion) n[i][j]))) {
						// effect on an enemy
						sum+=newEffectApplied(((Champion) o[i][j]), ((Champion) n[i][j]),me);
						// damaged an enemy
						sum += (((Champion) o[i][j]).getCurrentHP() - ((Champion) n[i][j]).getCurrentHP()) * 1.5;
					}
				}

			}
		}
		// prioritizing down movement
		if (ngame.getCurrentChampion().getLocation().x < ogame.getCurrentChampion().getLocation().x) {
			sum += 15;
		}
		// handling covers
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (o[i][j] instanceof Cover) {
					int x = 0;
					try {
						x = ((Cover) n[i][j]).getCurrentHP();
					} catch (Exception e) {
					}
					sum += (((Cover) o[i][j]).getCurrentHP() - x) * 0.05;
				}
			}
		}
		// Handling dead champions
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
		// using Leader ability
		if (ngame.isSecondLeaderAbilityUsed() && !ogame.isSecondLeaderAbilityUsed()) {
			if (sum < 1000 && ogame.getCurrentChampion() instanceof Villain) {
				sum -= 2000;
			}
		}
		//to not use abilities on covers
		for(int j=0;j<ngame.getCurrentChampion().getAbilities().size();j++) {
			if(ngame.getCurrentChampion().getAbilities().get(j).getCurrentCooldown()!=ogame.getCurrentChampion().getAbilities().get(j).getCurrentCooldown()) {
				sum-=50;
			}
		}
		return sum;
	}
	public static int newEffectApplied(Champion oldc,Champion newc,Player p2) {
		EffectType ef=EffectType.DEBUFF;
		if(isFriend(p2, newc)) {
			ef=EffectType.BUFF;
		}
		int sum=0;
		boolean flag=false;
		for(Effect f:newc.getAppliedEffects()) {
			flag=false;
			if(f.getType()==ef) {
				flag=true;
				for(Effect f2:oldc.getAppliedEffects()) {
					if(f2.getName()==f.getName()) {
						flag=false;
					}
				}
				if(flag) {
					sum+=1;
				}
			}
		}
		//System.out.println(sum+" "+newc.getName()+" "+newc.getAppliedEffects()+" "+oldc.getAppliedEffects());
		if(flag) {
			sum*=200;
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
		arr.add("ability");
		return arr;
	}

	public static ArrayList<String> minimax(Game oldgame, Game game, ArrayList<String> arr, int depth) {
		if (isFriend(p2, game.getCurrentChampion())) {
			return getBestActions(oldgame,game,arr);
		} else {
			return null;
		}
	}
	@SuppressWarnings("unchecked")
	public static ArrayList<String> getBestActions(Game oldgame,Game game,ArrayList<String> arr) {
		int value=-1;
		ArrayList<String> sol = arr;
		i++;
		if (game.getCurrentChampion().getCurrentActionPoints() == 0) {
			int x = evaluate(oldgame, game, p2);
			arr.add(x + "");
			try { 	
				Game game3=clone(game);
				ArrayList<String>arr2=(ArrayList<String>) sol.clone();
				game3.useLeaderAbility();
				int x2=evaluate(oldgame, game3, p2);
				if(x2>x) {
					arr2.remove(arr.size());
					arr2.add("Use Leader Ability");
					arr2.add(x2+"");
					sol=arr;
					System.out.println("used Leader Ability "+x+" "+x2+" "+sol);
				}
			} catch (Exception e) {}
			return arr;
		}
		for (String s : getAvailableActions()) {
			switch (s) {
			case "moveup":
				try {
					Game ngame = clone(game);
					ngame.move(Direction.UP);
					ArrayList<String> arr2 = (ArrayList<String>) arr.clone();
					arr2.add("moveup");
					ArrayList<String> arr3 = getBestActions(oldgame, ngame,arr2);
					int x = Integer.parseInt(arr3.get(arr3.size() - 1));
					if (x > value) {
						value = x;
						sol = arr3;
					}
				} catch (Exception e) {}
				break;
			case "movedown":
				try {
					Game ngame = clone(game);
					ngame.move(Direction.DOWN);
					ArrayList<String> arr2 = (ArrayList<String>) arr.clone();
					arr2.add("movedown");
					ArrayList<String> arr3 = getBestActions(oldgame, ngame,arr2);
					int x = Integer.parseInt(arr3.get(arr3.size() - 1));
					if (x > value) {
						value = x;
						sol = arr3;
					}
				} catch (Exception e) {}
				break;
			case "moveleft":
				try {
					Game ngame = clone(game);
					ngame.move(Direction.LEFT);
					ArrayList<String> arr2 = (ArrayList<String>) arr.clone();
					arr2.add("moveleft");
					ArrayList<String> arr3 = getBestActions(oldgame, ngame,arr2);
					int x = Integer.parseInt(arr3.get(arr3.size() - 1));
					if (x > value) {
						value = x;
						sol = arr3;
					}
				} catch (Exception e) {}
				break;
			case "moveright":
				try {
					Game ngame = clone(game);
					ngame.move(Direction.RIGHT);
					ArrayList<String> arr2 = (ArrayList<String>) arr.clone();
					arr2.add("moveright");
					ArrayList<String> arr3 = getBestActions(oldgame, ngame,arr2);
					int x = Integer.parseInt(arr3.get(arr3.size() - 1));
					if (x > value) {
						value = x;
						sol = arr3;
					}
				} catch (Exception e) {}
				break;
			case "attackup":
				try {
					Game ngame = clone(game);
					ngame.attack(Direction.UP);
					ArrayList<String> arr2 = (ArrayList<String>) arr.clone();
					arr2.add("attackup");
					ArrayList<String> arr3 = getBestActions(oldgame, ngame,arr2);
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
					ArrayList<String> arr3 = getBestActions(oldgame, ngame,arr2);
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
					ArrayList<String> arr3 = getBestActions(oldgame, ngame,arr2);
					int x = Integer.parseInt(arr3.get(arr3.size() - 1));
					if (x > value) {
						value = x;
						sol = arr3;
					}
				} catch (Exception e) {}
				break;
			case "attackright":
				try {
					Game ngame = clone(game);
					ngame.attack(Direction.RIGHT);
					ArrayList<String> arr2 = (ArrayList<String>) arr.clone();
					arr2.add("attackright");
					ArrayList<String> arr3 = getBestActions(oldgame, ngame,arr2);
					int x = Integer.parseInt(arr3.get(arr3.size() - 1));
					if (x > value) {
						value = x;
						sol = arr3;
					}
				} catch (Exception e) {}
				break;
			case "ability":
				for (int j = 0; j < game.getCurrentChampion().getAbilities().size(); j++) {
					Ability a = game.getCurrentChampion().getAbilities().get(j);
					if (a instanceof HealingAbility) {
						try {
							Game ngame = clone(game);
							a=getAbilityByName(ngame.getCurrentChampion(),a.getName());
							ngame.castAbility(a);
							ArrayList<String> arr2 = (ArrayList<String>) arr.clone();
							arr2.add("usedability" + j);
							ArrayList<String> arr3 = getBestActions(oldgame, ngame,arr2);
							int x = Integer.parseInt(arr3.get(arr3.size() - 1));
							if (x > value) {
								value = x;
								sol = arr3;
							}
						} catch (Exception e) {}
					}
					if (a instanceof DamagingAbility && a.getCastArea() == AreaOfEffect.SURROUND) {
						try {
							Game ngame = clone(game);
							a=getAbilityByName(ngame.getCurrentChampion(),a.getName());
							ngame.castAbility(a);
							ArrayList<String> arr2 = (ArrayList<String>) arr.clone();
							arr2.add("usedability" + j);
							ArrayList<String> arr3 = getBestActions(oldgame, ngame,arr2);
							int x = Integer.parseInt(arr3.get(arr3.size() - 1));
							if (x > value) {
								value = x;
								sol = arr3;
							}
						} catch (Exception e) {}
					}
					if (a instanceof DamagingAbility && a.getCastArea() == AreaOfEffect.TEAMTARGET) {
						try {
							Game ngame = clone(game);
							a=getAbilityByName(ngame.getCurrentChampion(),a.getName());
							ngame.castAbility(a);
							ArrayList<String> arr2 = (ArrayList<String>) arr.clone();
							arr2.add("usedability" + j);
							ArrayList<String> arr3 = getBestActions(oldgame, ngame,arr2);
							int x = Integer.parseInt(arr3.get(arr3.size() - 1));
							if (x > value) {
								value = x;
								sol = arr3;
							}
						} catch (Exception e) {}
					}
					if (a instanceof DamagingAbility && a.getCastArea() == AreaOfEffect.DIRECTIONAL) {
						try {
							Game ngame = clone(game);
							a=getAbilityByName(ngame.getCurrentChampion(),a.getName());
							Direction d = Direction.DOWN;
							ngame.castAbility(a, d);
							ArrayList<String> arr2 = (ArrayList<String>) arr.clone();
							arr2.add("usedability" + j + d);
							ArrayList<String> arr3 = getBestActions(oldgame, ngame,arr2);
							int x = Integer.parseInt(arr3.get(arr3.size() - 1));
							if (x > value) {
								value = x;
								sol = arr3;
							}									
						} catch (Exception e) {}
						try {
							Game ngame = clone(game);
							a=getAbilityByName(ngame.getCurrentChampion(),a.getName());
							Direction d = Direction.UP;
							ngame.castAbility(a, d);
							ArrayList<String> arr2 = (ArrayList<String>) arr.clone();
							arr2.add("usedability" + j + d);
							ArrayList<String> arr3 = getBestActions(oldgame, ngame,arr2);
							int x = Integer.parseInt(arr3.get(arr3.size() - 1));
							if (x > value) {
								value = x;
								sol = arr3;
							}
						} catch (Exception e) {}
						try {
							Game ngame = clone(game);
							a=getAbilityByName(ngame.getCurrentChampion(),a.getName());
							Direction d = Direction.RIGHT;
							ngame.castAbility(a, d);
							ArrayList<String> arr2 = (ArrayList<String>) arr.clone();
							arr2.add("usedability" + j + d);
							ArrayList<String> arr3 = getBestActions(oldgame, ngame,arr2);
							int x = Integer.parseInt(arr3.get(arr3.size() - 1));
							if (x > value) {
								value = x;
								sol = arr3;
							}
						} catch (Exception e) {}
						try {
							Game ngame = clone(game);
							a=getAbilityByName(ngame.getCurrentChampion(),a.getName());
							Direction d = Direction.LEFT;
							ngame.castAbility(a, d);
							ArrayList<String> arr2 = (ArrayList<String>) arr.clone();
							arr2.add("usedability" + j + d);
							ArrayList<String> arr3 = getBestActions(oldgame, ngame,arr2);
							int x = Integer.parseInt(arr3.get(arr3.size() - 1));
							if (x > value) {
								value = x;
								sol = arr3;
							}
						} catch (Exception e) {}
					}
					if (a instanceof DamagingAbility && a.getCastArea() == AreaOfEffect.SINGLETARGET) {
						for(int k=0;k<game.getFirstPlayer().getTeam().size();k++) {
								try {
									Game ngame = clone(game);
									a=getAbilityByName(ngame.getCurrentChampion(),a.getName());
									int x=ngame.getFirstPlayer().getTeam().get(k).getLocation().x;
									int y=ngame.getFirstPlayer().getTeam().get(k).getLocation().y;
									ngame.castAbility(a,x,y);
									ArrayList<String> arr2 = (ArrayList<String>) arr.clone();
									arr2.add("usedability"+j+""+x+""+y);
									ArrayList<String> arr3 = getBestActions(oldgame, ngame,arr2);
									int x2 = Integer.parseInt(arr3.get(arr3.size() - 1));
									if (x2 > value) {
										value = x2;
										sol = arr3;
									}
								} catch (Exception e) {}
						}
					}
					if (a instanceof CrowdControlAbility&&a.getCastArea()==AreaOfEffect.SELFTARGET) {
						try {
							Game ngame = clone(game);
							a=getAbilityByName(ngame.getCurrentChampion(),a.getName());
							ngame.castAbility(a);
							ArrayList<String> arr2 = (ArrayList<String>) arr.clone();
							arr2.add("usedability" + j);
							ArrayList<String> arr3 = getBestActions(oldgame, ngame,arr2);
							int x = Integer.parseInt(arr3.get(arr3.size() - 1));
							if (x > value) {
								value = x;
								sol = arr3;
							}
						} catch (Exception e) {}
					}
					if (a instanceof CrowdControlAbility&&a.getCastArea()==AreaOfEffect.TEAMTARGET) {
						try {
							Game ngame = clone(game);
							a=getAbilityByName(ngame.getCurrentChampion(),a.getName());
							ngame.castAbility(a);
							ArrayList<String> arr2 = (ArrayList<String>) arr.clone();
							arr2.add("usedability" + j);
							ArrayList<String> arr3 = getBestActions(oldgame, ngame,arr2);
							int x = Integer.parseInt(arr3.get(arr3.size() - 1));
							if (x > value) {
								value = x;
								sol = arr3;
							}
						} catch (Exception e) {}
					}
					if (a instanceof CrowdControlAbility && a.getCastArea() == AreaOfEffect.SINGLETARGET) {
						for(int k=0;k<game.getFirstPlayer().getTeam().size();k++) {
								try {
									Game ngame = clone(game);
									a=getAbilityByName(ngame.getCurrentChampion(),a.getName());
									int x=ngame.getFirstPlayer().getTeam().get(k).getLocation().x;
									int y=ngame.getFirstPlayer().getTeam().get(k).getLocation().y;
									ngame.castAbility(a,x,y);
									ArrayList<String> arr2 = (ArrayList<String>) arr.clone();
									arr2.add("usedability"+j+""+x+""+y);
									ArrayList<String> arr3 = getBestActions(oldgame, ngame,arr2);
									int x2 = Integer.parseInt(arr3.get(arr3.size() - 1));
									if (x2 > value) {
										value = x2;
										sol = arr3;
									}
								} catch (Exception e) {}
						}
					}
					if (a instanceof CrowdControlAbility && a.getCastArea() == AreaOfEffect.DIRECTIONAL) {
						try {
							Game ngame = clone(game);
							a=getAbilityByName(ngame.getCurrentChampion(),a.getName());
							Direction d = Direction.DOWN;
							ngame.castAbility(a, d);
							ArrayList<String> arr2 = (ArrayList<String>) arr.clone();
							arr2.add("usedability" + j + d);
							ArrayList<String> arr3 = getBestActions(oldgame, ngame,arr2);
							int x = Integer.parseInt(arr3.get(arr3.size() - 1));
							if (x > value) {
								value = x;
								sol = arr3;
							}									
						} catch (Exception e) {}
						try {
							Game ngame = clone(game);
							a=getAbilityByName(ngame.getCurrentChampion(),a.getName());
							Direction d = Direction.UP;
							ngame.castAbility(a, d);
							ArrayList<String> arr2 = (ArrayList<String>) arr.clone();
							arr2.add("usedability" + j + d);
							ArrayList<String> arr3 = getBestActions(oldgame, ngame,arr2);
							int x = Integer.parseInt(arr3.get(arr3.size() - 1));
							if (x > value) {
								value = x;
								sol = arr3;
							}
						} catch (Exception e) {}
						try {
							Game ngame = clone(game);
							a=getAbilityByName(ngame.getCurrentChampion(),a.getName());
							Direction d = Direction.RIGHT;
							ngame.castAbility(a, d);
							ArrayList<String> arr2 = (ArrayList<String>) arr.clone();
							arr2.add("usedability" + j + d);
							ArrayList<String> arr3 = getBestActions(oldgame, ngame,arr2);
							int x = Integer.parseInt(arr3.get(arr3.size() - 1));
							if (x > value) {
								value = x;
								sol = arr3;
							}
						} catch (Exception e) {}
						try {
							Game ngame = clone(game);
							a=getAbilityByName(ngame.getCurrentChampion(),a.getName());
							Direction d = Direction.LEFT;
							ngame.castAbility(a, d);
							ArrayList<String> arr2 = (ArrayList<String>) arr.clone();
							arr2.add("usedability" + j + d);
							ArrayList<String> arr3 = getBestActions(oldgame, ngame,arr2);
							int x = Integer.parseInt(arr3.get(arr3.size() - 1));
							if (x > value) {
								value = x;
								sol = arr3;
							}
						} catch (Exception e) {}
					}
				}
			}
		}
		return sol;
	}
	public static Ability getAbilityByName(Champion c, String name) {
		for(Ability a:c.getAbilities()) {
			if(a.getName()==name) {
				return a;
			}
		}
		return null;
	}
}
