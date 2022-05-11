package model.world;

import java.util.ArrayList;

public class Human {
	private int maxHealth;
	private int currentHealth;
	private ArrayList<Gear> gears;
	public Human (int maxHealth) {
		this.maxHealth = maxHealth;
		gears = new ArrayList<Gear>();
		this.currentHealth = maxHealth;
	}
	public int getMaxHealth() {
		return maxHealth;
	}
	
	public int getCurrentHealth() {
		return currentHealth;
	}
	public void setCurrentHealth(int currentHealth) {
		if(currentHealth<0) { this.currentHealth = 0;
		}
		else if(currentHealth>maxHealth){ 
			 this.currentHealth = maxHealth;
		}else {
			this.currentHealth = currentHealth;
		}
	}
	public ArrayList<Gear> getGears() {
		return gears;
	}
	
	

}
