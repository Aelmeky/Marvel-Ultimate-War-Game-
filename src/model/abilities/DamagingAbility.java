package model.abilities;

import java.util.ArrayList;

import model.world.Champion;
import model.world.Damageable;

public class DamagingAbility extends Ability {
	
	private int damageAmount;
	public DamagingAbility(String name, int cost, int baseCoolDown, int castRadius, AreaOfEffect area,int required,int damageAmount) {
		super(name, cost, baseCoolDown, castRadius, area,required);
		this.damageAmount=damageAmount;
	}
	public int getDamageAmount() {
		return damageAmount;
	}
	public void setDamageAmount(int damageAmount) {
		this.damageAmount = damageAmount;
	}
	
	public void execute(ArrayList<Damageable> targets) {
		for(int i =0;i<targets.size();i++) {
			Damageable target = targets.get(i);
			target.setCurrentHP(target.getCurrentHP()-this.getDamageAmount());
		}
	}
	public void remove() {
		
	}

}
