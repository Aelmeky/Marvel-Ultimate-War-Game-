package model.world;

import java.util.ArrayList;

import model.effects.Effect;
import model.effects.EffectType;
import model.effects.Embrace;

public class Hero extends Champion {

	public Hero(String name, int maxHP, int maxMana, int actions, int speed, int attackRange, int attackDamage) {
		super(name, maxHP, maxMana, actions, speed, attackRange, attackDamage);

	}
	public void useLeaderAbility(ArrayList<Champion> targets) {
		for(int i=0;i<targets.size();i++) {
			Champion c=targets.get(i);
			for(int j=0;j<c.getAppliedEffects().size();j++) {
				Effect f=c.getAppliedEffects().get(j);
				if(f.getType()==EffectType.DEBUFF) {
					f.remove(c);
					c.getAppliedEffects().remove(j);
					j--;
				}
			}
			Embrace e=new Embrace(2);
			c.getAppliedEffects().add(e);
			e.apply(c);
		}
	}

	
}
