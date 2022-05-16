package model.effects;

import model.world.Champion;

public class Embrace extends Effect {
	

	public Embrace(int duration) {
		super("Embrace", duration, EffectType.BUFF);
	}
	
	public void apply(Champion c) {
		super.apply(c);
		c.setCurrentHP((int) (c.getCurrentHP()+c.getMaxHP()*0.2));
		c.setMana((int) (c.getMana()*1.2));
		c.setSpeed((int) (c.getSpeed()*1.2));
		c.setAttackDamage((int) (c.getAttackDamage()*1.2));
	}
	public void remove(Champion c) {
		super.remove(c);
		c.setSpeed((int) (c.getSpeed()/1.2));
		c.setAttackDamage((int) (c.getAttackDamage()/1.2));
	}

}
