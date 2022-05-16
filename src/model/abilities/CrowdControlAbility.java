package model.abilities;

import java.util.ArrayList;

import exceptions.InvalidTargetException;
import model.effects.Effect;
import model.world.Champion;
import model.world.Cover;
import model.world.Damageable;

public class CrowdControlAbility extends Ability {
	private Effect effect;

	public CrowdControlAbility(String name, int cost, int baseCoolDown, int castRadius, AreaOfEffect area, int required,
			Effect effect) {
		super(name, cost, baseCoolDown, castRadius, area, required);
		this.effect = effect;

	}

	public Effect getEffect() {
		return effect;
	}
	
	public void execute(ArrayList<Damageable> targets) throws CloneNotSupportedException, InvalidTargetException {
		for(int i =0;i<targets.size();i++) {
			if(targets.get(i) instanceof Cover) {
				throw new InvalidTargetException();
			}
			((Effect)this.getEffect().clone()).apply((Champion) targets.get(i));
		}
	}
	public void remove() {
		
	}

}
