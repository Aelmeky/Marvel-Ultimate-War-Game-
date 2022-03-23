package engine;

import java.util.ArrayList;

public class Player {

	private String name;
	private Champion leader;
	ArrayList<Champion> team = new ArrayList<Champion>();
	public Player(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}

	public Champion getLeader() {
		return leader;
	}
	public void setLeader(Champion leader) {
		this.leader = leader;
	}
	public ArrayList<Champion> getTeam() {
		return team;
	}
	
	
}
