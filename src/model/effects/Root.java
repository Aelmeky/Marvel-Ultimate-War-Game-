package model.effects;

import model.world.Champion;
import model.world.Condition;

public class Root extends Effect {

	public Root( int duration) {
		super("Root", duration, EffectType.DEBUFF);
		
	}
	
	public void apply(Champion c) {
		if(c.getCondition()!=Condition.INACTIVE) {
			c.setCondition(Condition.ROOTED);
		}
	}
	
	public void remove(Champion c) {
		for(int i=0;i<c.getAppliedEffects().size();i++) {
			if(c.getAppliedEffects().get(i).getName().equals("Root")) {
				return;
			}
		}
		if(c.getCondition()==Condition.ROOTED) {
			c.setCondition(Condition.ACTIVE);
		}
	}
}
