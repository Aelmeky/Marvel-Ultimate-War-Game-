package model.effects;

import model.world.Champion;
import model.world.Condition;

public class Stun extends Effect {

	public Stun(int duration) {
		super("Stun", duration, EffectType.DEBUFF);
	}
	
	public void apply(Champion c) {
		
		c.setCondition(Condition.INACTIVE);
	}
	
	public void remove(Champion c) {
		boolean fr=false;
		boolean fs=false;
		for(int i=0;i<c.getAppliedEffects().size();i++) {
			if(c.getAppliedEffects().get(i).getName().equals("Root")) {
				fr=true;
			}
			if(c.getAppliedEffects().get(i).getName().equals("Stun")) {
				fs=true;
			}
		}
		if(fs) {
			return;
		}
		if(fr) {
			c.setCondition(Condition.ROOTED);
		}else {
			c.setCondition(Condition.ACTIVE);
		}
	}
	

}
