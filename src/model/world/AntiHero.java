package model.world;

import java.util.ArrayList;

import model.effects.Effect;
import model.effects.EffectType;
import model.effects.Embrace;
import model.effects.Stun;

public class AntiHero extends Champion {

	public AntiHero(String name, int maxHP, int maxMana, int actions, int speed, int attackRange, int attackDamage) {
		super(name, maxHP, maxMana, actions, speed, attackRange, attackDamage);

	}
	
	public void useLeaderAbility(ArrayList<Champion> targets) {
		System.out.println("here");
		for(int i=0;i<targets.size();i++) {
			Champion c=targets.get(i);
			Stun s = new Stun(2);
			s.apply(c);
			c.getAppliedEffects().add(s);
		}
	}
	
}
