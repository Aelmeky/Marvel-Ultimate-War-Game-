package model.effects;

import model.world.Champion;

public class Silence extends Effect {

	public Silence( int duration) {
		super("Silence", duration, EffectType.DEBUFF);
		
	}
	public void apply(Champion c) {
		super.apply(c);
		c.setMaxActionPointsPerTurn(c.getMaxActionPointsPerTurn()+2);
		c.setCurrentActionPoints(c.getMaxActionPointsPerTurn()+2);
		
	}
	public void remove(Champion c) {
		super.remove(c);
		c.setMaxActionPointsPerTurn(c.getMaxActionPointsPerTurn()-2);
		c.setCurrentActionPoints(c.getMaxActionPointsPerTurn()-2);
	}
}
