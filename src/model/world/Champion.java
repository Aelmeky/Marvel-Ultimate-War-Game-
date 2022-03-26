package model.world;

import java.awt.*;
import java.util.ArrayList;

import model.abilities.Ability;
import model.effects.Effect;

public class Champion {
    private String name;
    private int maxHP;
    private int currentHP;
    private int mana;
    private int maxActionPointsPerTurn;
    private int currentActionPoints;
    private int attackRange;
    private int attackDamage;
    private int speed;
    private ArrayList<Ability> abilities;
    private ArrayList<Effect> appliedEffects;
    private Condition condition;
    private Point location;
    public Champion(String name, int maxHP, int mana, int maxActions, int speed, int attackRange,int attackDamage){
        this.name=name;
        this.maxHP=maxHP;
        this.mana=mana;
        this.maxActionPointsPerTurn=maxActions;
        this.speed=speed;
        this.attackRange=attackRange;
        this.attackDamage=attackDamage;
        abilities = new ArrayList<Ability>();
    }
    public Point getLocation() {
        return location;
    }
    public void setLocation(Point location) {
        this.location = location;
    }
    public Condition getCondition() {
        return condition;
    }
    public void setCondition(Condition condition) {
        this.condition = condition;
    }
    public ArrayList<Effect> getAppliedEffects() {
        return appliedEffects;
    }
    public ArrayList<Ability> getAbilities() {
        return abilities;
    }
    public int getSpeed() {
        return speed;
    }
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    public int getAttackDamage() {
        return attackDamage;
    }
    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }
    public String getName() {
        return name;
    }
    public int getMaxHP() {
        return maxHP;
    }
    public int getCurrentHP() {
        return currentHP;
    }
    public void setCurrentHP(int currentHP) {
    	if(currentHP>=this.maxHP)this.currentHP = this.maxHP;
    	else if(currentHP<=0)this.currentHP =0;
    	else this.currentHP = currentHP;
    }
    public int getMana() {
        return mana;
    }
    public void setMana(int mana) {
        this.mana = mana;
    }
    public int getAttackRange() {
        return attackRange;
    }
    public int getMaxActionPointsPerTurn() {
        return maxActionPointsPerTurn;
    }
    public int getCurrentActionPoints() {
        return currentActionPoints;
    }
    public void setCurrentActionPoints(int currentActionPoints) {
        this.currentActionPoints = currentActionPoints;
    }

    public void setMaxActionPointsPerTurn(int maxActionPointsPerTurn) {
        this.maxActionPointsPerTurn = maxActionPointsPerTurn;
    }
}
