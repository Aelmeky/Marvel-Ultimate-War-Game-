package model.effects;

import model.abilities.Ability;
import model.abilities.DamagingAbility;
import model.abilities.HealingAbility;
import model.world.Champion;

public class PowerUp extends Effect {
	

	public PowerUp(int duration) {
		super("PowerUp", duration, EffectType.BUFF);
		
	}	
	public void remove(Champion c) {
		for(int i=0;i<c.getAbilities().size();i++) {
			Ability a=c.getAbilities().get(i);
			if(a instanceof HealingAbility) {
				((HealingAbility) a).setHealAmount((int) (((HealingAbility) a).getHealAmount()/1.2));
			}
			if(a instanceof DamagingAbility) {
				((DamagingAbility) a).setDamageAmount((int) (((DamagingAbility) a).getDamageAmount()/1.2));
			}
		}
	}
	public void apply(Champion c) {
		for(int i=0;i<c.getAbilities().size();i++) {
			Ability a=c.getAbilities().get(i);
			if(a instanceof HealingAbility) {
				((HealingAbility) a).setHealAmount((int) (((HealingAbility) a).getHealAmount()*1.2));
			}
			if(a instanceof DamagingAbility) {
				((DamagingAbility) a).setDamageAmount((int) (((DamagingAbility) a).getDamageAmount()*1.2));
			}
		}
	}
}
