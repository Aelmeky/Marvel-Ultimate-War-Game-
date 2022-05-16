package model.effects;

import model.world.Champion;

public class Shield extends Effect {

	public Shield( int duration) {
		super("Shield", duration, EffectType.BUFF);
		
	}
	
	public void apply(Champion c) {
		super.apply(c);
		c.setSpeed((int) (c.getSpeed()*1.02));
	}
	public void remove(Champion c) {
		super.remove(c);
		c.setSpeed((int) (c.getSpeed()/1.02));
	}
}
