package model.effects;

import model.world.Champion;
import model.world.Condition;

public class Root extends Effect {

	public Root( int duration) {
		super("Root", duration, EffectType.DEBUFF);
		
	}
	
	public void apply(Champion c) {
		c.setCondition(Condition.ROOTED);
	}
	
	public void remove(Champion c) {
		c.setCondition(Condition.ACTIVE);
	}

}
